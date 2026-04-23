package com.sky.mapper;

import com.sky.dto.EmployeeDTO;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    @Insert("insert into employee (name,username ,password,phone,sex,id_number,status,create_time,update_time,create_user,update_user)" +
            "values " +
            "(#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void insertEmployee(Employee employee);


    List<Employee> selectlist(String name);


    void update(Employee employee);

    @Select("select * from employee where id=#{id}")
    Employee selectone(Long id);

   /*@Select("select *from employee where name like contact('%',#{name},'%') order by create_time desc ")
    List<Employee> selectlist(String name);


    */

}
