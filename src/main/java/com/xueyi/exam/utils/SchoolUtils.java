package com.xueyi.exam.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xueyi.exam.beans.Dictionnary;
import com.xueyi.exam.beans.Student;
import com.xueyi.exam.beans.Teacher;
import com.xueyi.exam.service.DictionnaryService;
import com.xueyi.exam.service.StudentService;
import com.xueyi.exam.service.TeacherService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SchoolUtils {
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    StudentService studentService;
    @Autowired
    TeacherService teacherService;
    @Autowired
    DictionnaryService dictionnaryService;

    public final static long ONE_DAY = 10;
    public final static long ONE_HOUR = 10;
    public final static String STUDENT_PREFIX = "student_";
    public final static String TEACHER_PREFIX = "teacher_";
    public final static String CURRENT_XUENIAN = "current_xuenian";
    public final static String CURRENT_XUEQI = "current_xueqi";
    public final static String LIST_XUENIAN = "list_xuenian";
    public final static String LIST_XUEQI = "list_xueqi";

    public Student getCurrentStudent(){
        Subject subject = SecurityUtils.getSubject();
        String name = subject.getPrincipal().toString();
        Student student = (Student)redisUtils.get(STUDENT_PREFIX+name);
        if (student==null){
            student = studentService.getOne(studentService.query().getWrapper().eq("username",name));
            redisUtils.set(STUDENT_PREFIX+name,student,ONE_HOUR);
        }
        return student;
    }

    public Teacher getCurrentTeacher(){

        Subject subject = SecurityUtils.getSubject();
        String name = subject.getPrincipal().toString();
        Teacher teacher = (Teacher)redisUtils.get(TEACHER_PREFIX+name);
        if (teacher==null){
            teacher = teacherService.getOne(teacherService.query().getWrapper().eq("username",name));
            redisUtils.set(TEACHER_PREFIX+name,teacher,ONE_HOUR);
        }
        return teacher;
    }

    public String getCurrentXuenian(){
        String xuenian = (String) redisUtils.get(CURRENT_XUENIAN);
        if (xuenian==null){
            Dictionnary dictionnary = dictionnaryService.getOne(new QueryWrapper<Dictionnary>().lambda().eq(Dictionnary::getName,"globalXueNian"));
            xuenian = dictionnary.getValue();
            redisUtils.set(CURRENT_XUENIAN,xuenian,ONE_DAY);
        }
        return xuenian;
    }

    public String getCurrentXuenqi(){
        String xueqi = (String) redisUtils.get(CURRENT_XUEQI);
        if (xueqi==null){
            Dictionnary dictionnary = dictionnaryService.getOne(new QueryWrapper<Dictionnary>().lambda().eq(Dictionnary::getName,"globalXueQi"));
            xueqi = dictionnary.getValue();
            redisUtils.set(CURRENT_XUEQI,xueqi,ONE_DAY);
        }
        return xueqi;
    }

    public List<Dictionnary> getAllXueNian(){
        List<Dictionnary> xuenianList = (List<Dictionnary>)redisUtils.get(LIST_XUENIAN);
        if (xuenianList==null){
            xuenianList = dictionnaryService.list(new QueryWrapper<Dictionnary>().lambda().eq(Dictionnary::getName,"nianJi"));
            redisUtils.set(LIST_XUENIAN,xuenianList,ONE_DAY);
        }
        return xuenianList;
    }

    public List<Dictionnary> getAllXueQi(){
        List<Dictionnary> xueqiList = (List<Dictionnary>)redisUtils.get(LIST_XUEQI);
        if (xueqiList==null){
            xueqiList = dictionnaryService.list(new QueryWrapper<Dictionnary>().lambda().eq(Dictionnary::getName,"xueQi"));
            redisUtils.set(LIST_XUEQI,xueqiList,ONE_DAY);
        }
        return xueqiList;
    }
}
