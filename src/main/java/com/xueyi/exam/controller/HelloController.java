package com.xueyi.exam.controller;

import com.xueyi.exam.beans.Student;
import com.xueyi.exam.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
@RequestMapping("")
public class HelloController {
    @Autowired
    StudentService studentService;
    @RequestMapping("/hello")
    @ResponseBody
    public String hello(){

        List list = studentService.list(studentService.query().getWrapper().eq("username","11"));
        return "hello";
    }
}
