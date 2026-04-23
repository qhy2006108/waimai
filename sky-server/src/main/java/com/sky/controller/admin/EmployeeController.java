package com.sky.controller.admin;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.JwtClaimsConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工相关接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @ApiOperation(value= "员工登录")
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @ApiOperation("员工退出")
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }


    @ApiOperation("新增员工")
    @PostMapping
    public  Result<String> addEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        employeeService.insertEmployee(employeeDTO);
        return Result.success();


    }

    @ApiOperation("分页查询")
    @GetMapping("/page")
    public Result<PageResult> page(@RequestParam(defaultValue = "1") String page,
                                   @RequestParam(defaultValue = "10")String pageSize,
                                   String name){

        Integer i = Integer.valueOf(page);
        Integer j = Integer.valueOf(pageSize);
        int pageNum = i.intValue();
        int pageNumSize = j.intValue();
        /*
        PageHelper.startPage(pageNum, pageNumSize);
          List<Employee> emplist= employeeService.list();
        Page<Employee> p =(Page<Employee>) emplist;
        long total = p.getTotal();
        List<Employee> result = p.getResult();
       PageResult pr=new PageResult();
       pr.setTotal(total);
       pr.setRecords(result);
        return  Result.success(pr);
         */
        PageResult pr = employeeService.lists(pageNum, pageNumSize,name);
        return Result.success(pr);

    }

    @ApiOperation("员工的启用和禁用")
    @PostMapping("/status/{status}")
    public Result<String> updateStatus(@PathVariable Integer status,Long id){

        employeeService.updateStatus(status,id);
        return Result.success();
    }
    @ApiOperation("根据id查询员工信息")
    @GetMapping("/{id}")
    public Result<EmployeeDTO> getEmployee(@PathVariable Long id){
        EmployeeDTO selectone = employeeService.selectone(id);
        return Result.success(selectone);
    }


    @ApiOperation("修改员工信息")
    @PutMapping
    public  Result<String> updateEmployee(@Valid @RequestBody EmployeeDTO employeeDTO){
        employeeService.updateEmployee(employeeDTO);
        return Result.success();

    }


}
