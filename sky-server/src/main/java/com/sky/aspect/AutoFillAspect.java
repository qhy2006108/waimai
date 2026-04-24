package com.sky.aspect;


import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import javassist.bytecode.SignatureAttribute;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
public class AutoFillAspect {
     @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void pt(){}

    @Before("pt()")
    public void before(JoinPoint joinPoint){
         //首先获取是插入数据还是更新数据
        //1. 根据AutoFill注解获得
      MethodSignature signature= (MethodSignature)joinPoint.getSignature();
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType value = autoFill.value();
        if (value ==null){
            return;
        }
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0){
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        Long currentId= BaseContext.getCurrentId();

        if (value.equals(OperationType.INSERT)){
            try {
                Method setCreateTime = args.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME,LocalDateTime.class);
                Method setUpdateTime = args.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME,LocalDateTime.class);
                Method setCreateUser = args.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER,Long.class);
                Method setUpdateUser = args.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER,Long.class);

                setCreateTime.invoke(args,now);
                setUpdateTime.invoke(args,now);
                setCreateUser.invoke(args,currentId);
              setCreateUser.invoke(args,currentId);


            } catch (Exception e) {
                throw new RuntimeException(e);
            }


        }else if (value.equals(OperationType.UPDATE)){
            try {
                Method setCreateTime = args.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME,LocalDateTime.class);
                Method setUpdateTime = args.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME,LocalDateTime.class);
               setUpdateTime.invoke(args,now);
               setCreateTime.invoke(args,now);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


        }






















    }


}
