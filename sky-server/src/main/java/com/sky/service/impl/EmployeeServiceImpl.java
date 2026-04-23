package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.beans.BeanUtils.copyProperties;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();
        password =DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // TODO 后期需要进行md5加密，然后再进行比对0
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    @Override

    public void insertEmployee(EmployeeDTO employeeDTO){
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes(StandardCharsets.UTF_8)));
        employee.setStatus(StatusConstant.DISABLE);
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        //因为这里先完善的是管理的  没有用户的id所以先给一个值
        Long currentId = BaseContext.getCurrentId();
        employee.setCreateUser(currentId);
        employee.setUpdateUser(currentId);

        employeeMapper.insertEmployee(employee);




    }

    @Override
    public PageResult lists(int pageNum, int pageNumSize, String name) {
        PageHelper.startPage(pageNum, pageNumSize);
        List<Employee> selectlist = employeeMapper.selectlist(name);
        Page<Employee> p=(Page<Employee>)selectlist;
        PageResult pageResult = new PageResult();
        pageResult.setTotal(p.getTotal());
        pageResult.setRecords(p.getResult());
        return pageResult;
    }

    @Override
    public void updateStatus(Integer status, Long id) {
        Employee employee =new Employee();
        employee.setStatus(status);
        employee.setId(id);
        employeeMapper.update(employee);
    }

    @Override
    public void updateEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);
        employeeMapper.update(employee);

    }

    @Override
    public EmployeeDTO selectone(Long id) {
        Employee employee =employeeMapper.selectone(id);
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee,employeeDTO);
        return employeeDTO;
    }

    /*
    @Override
    public List<Employee> list() {
      List<Employee> list=  employeeMapper.selectlist();
        return list ;
    }

     */

}
