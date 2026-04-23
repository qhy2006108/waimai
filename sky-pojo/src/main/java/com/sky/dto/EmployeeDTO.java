package com.sky.dto;

import lombok.Data;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
public class EmployeeDTO implements Serializable {

    private Long id;

    private String username;

    private String name;
    @Pattern(regexp = "/^(?:(?:\\+|00)86)?1[3-9]\\d{9}$/" ,message = "手机号码格式错误")
    private String phone;

    private String sex;
    @Pattern(regexp = "/^[1-9]\\d{7}(?:0\\d|10|11|12)(?:0[1-9]|[1-2][\\d]|30|31)\\d{3}$/",message = "身份证号码出错")
    private String idNumber;

}
