package com.xueyi.exam.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xueyi.exam.beans.*;
import com.xueyi.exam.service.ExamService;
import com.xueyi.exam.service.ProfessionCourseService;
import com.xueyi.exam.service.StudentCourseService;
import com.xueyi.exam.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mike
 * @since 2020-12-05
 */
@Controller
@RequestMapping("/professionCourse")
public class ProfessionCourseController {
    @Autowired
    ProfessionCourseService professionCourseService;
    @Autowired
    StudentCourseService studentCourseService;
    @Autowired
    StudentService studentService;
    @Autowired
    ExamService examService;

    @RequestMapping("/delOneProfessionCourse")
    @ResponseBody
    public R delOneProfessionCourse(ProfessionCourse professionCourse){
        if (professionCourseService.remove(new QueryWrapper<ProfessionCourse>().lambda()
                .eq(ProfessionCourse::getCourseId,professionCourse.getCourseId())
                .eq(ProfessionCourse::getProfessionId,professionCourse.getProfessionId()))
        ){
            List<Student> studentList = studentService.list(new QueryWrapper<Student>().lambda().eq(Student::getProfessionId,professionCourse.getProfessionId()));
            studentList.forEach(student ->
                studentCourseService.remove(new QueryWrapper<StudentCourse>().lambda()
                        .eq(StudentCourse::getStudentId,student.getId())
                        .eq(StudentCourse::getCourseId,professionCourse.getCourseId()))
            );
            return R.success;
        }
        return R.fail;
    }

    @RequestMapping("/addOneProfessionCourse")
    @ResponseBody
    public R addOneProfessionCourse(ProfessionCourse professionCourse){
        if (professionCourseService.count(new QueryWrapper<ProfessionCourse>().lambda()
                .eq(ProfessionCourse::getCourseId,professionCourse.getCourseId())
                .eq(ProfessionCourse::getProfessionId,professionCourse.getProfessionId()))>0){
            return R.fail;
        }
        if (professionCourseService.save(professionCourse)){
            List<Student> studentList = studentService.list(new QueryWrapper<Student>().lambda().eq(Student::getProfessionId,professionCourse.getProfessionId()));
            StudentCourse studentCourse = new StudentCourse();
            studentCourse.setCourseId(professionCourse.getCourseId());
            List<Exam> examList = examService.list(new QueryWrapper<Exam>().lambda().eq(Exam::getCourseId,professionCourse.getCourseId()).ge(Exam::getEndTime, LocalDateTime.now()));
            Map<String,Exam> examMap = examList.stream().collect(Collectors.toMap(Exam::getNianji, exam -> exam,(e1,e2)->e1));
            studentList.forEach(student -> {
                studentCourse.setStudentId(student.getId());
                Exam exam = examMap.get(student.getNianJi());
                if (exam!=null){
                    studentCourse.setExamId(exam.getId());
                    studentCourse.setPageId(exam.getPageId());
                }
                studentCourseService.save(studentCourse);
            });
            return R.success;
        }
        return R.fail;
    }
}

