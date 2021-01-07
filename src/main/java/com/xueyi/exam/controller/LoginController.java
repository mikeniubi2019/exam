package com.xueyi.exam.controller;

import com.xueyi.exam.beans.Menu;
import com.xueyi.exam.beans.R;
import com.xueyi.exam.beans.Student;
import com.xueyi.exam.beans.Teacher;
import com.xueyi.exam.utils.RedisUtils;
import com.xueyi.exam.utils.SchoolUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;
import java.util.stream.Collectors;

@Controller

public class LoginController {
    @Autowired
    SchoolUtils schoolUtils;
    @Autowired
    RedisUtils redisUtils;
    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login(){
    return "login";
    }

    @RequestMapping("/logout")
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "logout";
    }

    @RequestMapping("/welcome")
    public String welcome(){
        return "welcome1";
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String login(Student student,Model model){
        if (StringUtils.isEmpty(student.getUsername())||StringUtils.isEmpty(student.getPassword())){
            return "loginError";
        }
        Subject subject = SecurityUtils.getSubject();
         try {
            long start = System.currentTimeMillis();
             try {
                 if (subject.hasRole("teacher")){
                     model.addAttribute("name",schoolUtils.getCurrentTeacher().getName());
                     model.addAttribute("studentRole",0);
                 }else {
                     model.addAttribute("studentRole",1);
                     model.addAttribute("name",schoolUtils.getCurrentStudent().getName());
                 }
            }catch (Exception e){
                e.printStackTrace();
                model.addAttribute("name","");
            }
            System.out.println(System.currentTimeMillis()-start);
            try {
                model.addAttribute("currentXueqi",schoolUtils.getCurrentXuenqi());
            }catch (Exception e){
                e.printStackTrace();
                model.addAttribute("currentXueqi","");
            }
            System.out.println(System.currentTimeMillis()-start);
            try {
                model.addAttribute("currentXuenian",schoolUtils.getCurrentXuenian());
            }catch (Exception e){
                e.printStackTrace();
                model.addAttribute("currentXueqi","");
            }
            System.out.println(System.currentTimeMillis()-start);
            return "index";
        }catch (Exception e){
            return "loginError";
        }

    }
    @RequestMapping("/loginError")
    public String loginError(){
        return "loginError";
    }

    @RequestMapping("/index")
    public String index(Model model){
        long start = System.currentTimeMillis();

        Subject subject = SecurityUtils.getSubject();
        try {
            if (subject.hasRole("teacher")){
                model.addAttribute("name",schoolUtils.getCurrentTeacher().getName());
                model.addAttribute("studentRole",0);
            }else {
                model.addAttribute("studentRole",1);
                model.addAttribute("name",schoolUtils.getCurrentStudent().getName());
            }
        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("name","");
        }
        System.out.println(System.currentTimeMillis()-start);
        try {
            model.addAttribute("currentXueqi",schoolUtils.getCurrentXuenqi());
        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("currentXueqi","");
        }
        System.out.println(System.currentTimeMillis()-start);
        try {
            model.addAttribute("currentXuenian",schoolUtils.getCurrentXuenian());
        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("currentXueqi","");
        }
        System.out.println(System.currentTimeMillis()-start);
        return "index";
    }

    @RequestMapping("/")
    public String home(Model model){
        Subject subject = SecurityUtils.getSubject();
        try {
            if (subject.hasRole("teacher")){
                model.addAttribute("name",schoolUtils.getCurrentTeacher().getName());
                model.addAttribute("studentRole",0);
            }else {
                model.addAttribute("studentRole",1);
                model.addAttribute("name",schoolUtils.getCurrentStudent().getName());
            }
        }catch (Exception e){
            model.addAttribute("name","");
        }
        try {
            model.addAttribute("currentXueqi",schoolUtils.getCurrentXuenian());
        }catch (Exception e){
            model.addAttribute("currentXueqi","");
        }
        try {
            model.addAttribute("currentXuenian",schoolUtils.getCurrentXuenqi());
        }catch (Exception e){
            model.addAttribute("currentXueqi","");
        }
        return "index";
    }

    @RequestMapping(value = "/updateLog")
    public String updateLog(){
        return "pages/upload/updateLog";
    }

    @RequestMapping("/menu")
    @ResponseBody
    public Map menu(){
        Subject subject = SecurityUtils.getSubject();
        Map menuMap = new HashMap();
        Map map = new HashMap();
        if (subject.hasRole("teacher")){
            Map teacherMap = (Map)redisUtils.get("teacher_menu");
            if (teacherMap==null){
                Menu menu = new Menu();
                menu.setId(1);
                menu.setName("学生管理");
                menu.setIcon("user");

                Menu menu13 = new Menu();
                menu13.setId(13);
                menu13.setName("学生列表");
                menu13.setUrl("/student/studentList");
                menu13.setIcon("user");

                Menu menu14 = new Menu();
                menu14.setId(14);
                menu14.setName("批量上传");
                menu14.setUrl("/student/studentUpload");
                menu14.setIcon("user");

                Menu[] lm1 = new Menu[]{menu13,menu14};
                menu.setChildren(Arrays.stream(lm1).collect(Collectors.toList()));

                Menu menu2 = new Menu();
                menu2.setId(2);
                menu2.setName("课程管理");
                menu2.setUrl("/course/courseList");
                menu2.setIcon("read");

                Menu menu3 = new Menu();
                menu3.setId(3);
                menu3.setName("考试管理");
                menu3.setIcon("form");

                Menu menu17 = new Menu();
                menu17.setId(17);
                menu17.setName("考试列表");
                menu17.setUrl("/exam/examList");
                menu17.setIcon("form");

                Menu menu18 = new Menu();
                menu18.setId(18);
                menu18.setName("成绩查询");
                menu18.setUrl("/student/studentExamList");
                menu18.setIcon("form");

                Menu[] lm3 = new Menu[]{menu17,menu18};
                menu3.setChildren(Arrays.stream(lm3).collect(Collectors.toList()));

                Menu menu4 = new Menu();
                menu4.setId(4);
                menu4.setName("试卷管理");
                menu4.setIcon("form");

                Menu menu15 = new Menu();
                menu15.setId(15);
                menu15.setName("试卷列表");
                menu15.setUrl("/page/pageList");
                menu15.setIcon("form");

                Menu menu16 = new Menu();
                menu16.setId(16);
                menu16.setName("批量上传");
                menu16.setUrl("/question/questionUpload");

                Menu menu23 = new Menu();
                menu23.setId(23);
                menu23.setName("主观题阅卷");
                menu23.setUrl("/essayQuestion/reviewEssayQuestionList");

                Menu[] lm2 = new Menu[]{menu15,menu16,menu23};
                menu4.setChildren(Arrays.stream(lm2).collect(Collectors.toList()));

                Menu menu5 = new Menu();
                menu5.setId(5);
                menu5.setName("专业管理");
                menu5.setUrl("/profession/professionList");

                Menu menu6 = new Menu();
                menu6.setId(6);
                menu6.setName("题目管理");
                menu6.setIcon("read");

                Menu menu19 = new Menu();
                menu19.setId(19);
                menu19.setName("单选/多选");
                menu19.setUrl("/question/questionList");
                menu19.setIcon("read");

                Menu menu20 = new Menu();
                menu20.setId(20);
                menu20.setName("阅读理解");
                menu20.setUrl("/reading/readingList");
                menu20.setIcon("read");

                Menu menu21 = new Menu();
                menu21.setId(21);
                menu21.setName("主观题");
                menu21.setUrl("/essayQuestion/essayQuestionList");
                menu21.setIcon("read");

                Menu[] lm4 = new Menu[]{menu19,menu20,menu21};
                menu6.setChildren(Arrays.stream(lm4).collect(Collectors.toList()));

                Menu menu7 = new Menu();
                menu7.setId(7);
                menu7.setName("系统设置");

                Menu menu8 = new Menu();
                menu8.setId(8);
                menu8.setName("年级设置");
                menu8.setUrl("/dictionnary/nianJiAddList");

                Menu menu9 = new Menu();
                menu9.setId(9);
                menu9.setName("学期设置");
                menu9.setUrl("/dictionnary/xueQiAddList");

                Menu menu10 = new Menu();
                menu10.setId(10);
                menu10.setName("学校配置");
                menu10.setUrl("/dictionnary/globalConfigEdit");

                Menu menu26 = new Menu();
                menu26.setId(26);
                menu26.setName("生成缓存");
                menu26.setUrl("/exam/generatorHotCachePage");

                Menu menu25 = new Menu();
                menu25.setId(25);
                menu25.setName("更新日志");
                menu25.setUrl("/updateLog");
                menu25.setIcon("read");

                Menu[] lm = new Menu[]{menu10,menu9,menu8,menu26};
                menu7.setChildren(Arrays.stream(lm).collect(Collectors.toList()));
                Menu[] menus = new Menu[]{menu,menu2,menu3,menu4,menu5,menu6,menu7,menu25};
                map.put("menu",menus);
                menuMap.put("code",1);
                menuMap.put("data",map);
                menuMap.put("msg","ok");
                redisUtils.set("teacher_menu",menuMap,60*30);
                return menuMap;
            }
            return teacherMap;
        }

        else {
            Map studentMap = (Map)redisUtils.get("student_menu");
            if (studentMap==null){
                Menu menu = new Menu();
                menu.setId(100);
                menu.setName("学生详情");
                menu.setUrl("/student/studentPage");
                menu.setIcon("user");

                Menu menu2 = new Menu();
                menu2.setId(101);
                menu2.setName("课程详情");
                menu2.setUrl("/course/courseListPage");
                menu2.setIcon("form");

                Menu menu3 = new Menu();
                menu3.setId(102);
                menu3.setName("考试详情");
                menu3.setUrl("/exam/examListPage");
                menu2.setIcon("read");


                map.put("menu",new Menu[]{menu,menu2,menu3});
                menuMap.put("code",1);
                menuMap.put("data",map);
                menuMap.put("msg","ok");

                redisUtils.set("student_menu",menuMap,60*30);
                return menuMap;
            }else {
                return studentMap;
            }

        }

        //return menuMap;
    }
}
