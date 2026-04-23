package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

import javax.validation.Valid;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    void insertEmployee(EmployeeDTO employeeDTO);

    //List<Employee> list();

    PageResult lists(int pageNum, int pageNumSize, String name);

    void updateStatus(Integer status, Long id);

    void updateEmployee(@Valid EmployeeDTO employeeDTO);

    EmployeeDTO selectone(Long id);
}
