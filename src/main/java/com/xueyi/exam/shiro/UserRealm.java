package com.xueyi.exam.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xueyi.exam.beans.Student;
import com.xueyi.exam.beans.Teacher;
import com.xueyi.exam.service.StudentService;
import com.xueyi.exam.service.TeacherService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

public class UserRealm extends AuthorizingRealm {
    @Autowired
    StudentService studentService;
    @Autowired
    TeacherService teacherService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String userName = (String)principalCollection.getPrimaryPrincipal();
        Student student = studentService.getOne(studentService.query().getWrapper().eq("username",userName));
        SimpleAuthorizationInfo simpleAuthorizationInfo;
        if (student==null){
            Teacher teacher = teacherService.getOne(new QueryWrapper<Teacher>().lambda().eq(Teacher::getUsername,userName));
            if (teacher!=null){
                simpleAuthorizationInfo = new SimpleAuthorizationInfo();
                simpleAuthorizationInfo.addRole("teacher");
            }else {
                return null;
            }
        }else {
            simpleAuthorizationInfo = new SimpleAuthorizationInfo();
            simpleAuthorizationInfo.addRole("student");
        }
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        if (StringUtils.isEmpty(authenticationToken.getPrincipal())) {
            return null;
        }
        //获取用户信息
        String name = authenticationToken.getPrincipal().toString();
        Student student = studentService.getOne(studentService.query().getWrapper().eq("username",name));
        SimpleAuthenticationInfo simpleAuthenticationInfo;
        if (student == null) {
            //这里返回后会报出对应异常
            Teacher teacher = teacherService.getOne(new QueryWrapper<Teacher>().lambda().eq(Teacher::getUsername,name));
            if (teacher!=null){
                simpleAuthenticationInfo = new SimpleAuthenticationInfo(name, teacher.getPassword(), getName());
            }else {
                return null;
            }
        } else {
            //这里验证authenticationToken和simpleAuthenticationInfo的信息
            simpleAuthenticationInfo = new SimpleAuthenticationInfo(name, student.getPassword(), getName());
        }
        return simpleAuthenticationInfo;
    }
}
