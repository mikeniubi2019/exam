package com.xueyi.exam.common;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xueyi.exam.beans.Exam;
import com.xueyi.exam.beans.Student;
import com.xueyi.exam.beans.StudentCourse;
import com.xueyi.exam.service.ExamService;
import com.xueyi.exam.service.PageService;
import com.xueyi.exam.service.StudentCourseService;
import com.xueyi.exam.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
@Component
public class AsyDoExam implements CommandLineRunner {
    public static ArrayBlockingQueue<Exam> doTransferExamQueue = new ArrayBlockingQueue(500);

    @Autowired
    StudentCourseService studentCourseService;
    @Autowired
    ExamService examService;
    @Autowired
    PageService pageService;
    @Autowired
    StudentService studentService;
    @Override
    public void run(String... args) throws Exception {
        while (true){

            Exam exam = doTransferExamQueue.take();

            exam = examService.getOne(new QueryWrapper<Exam>().lambda().eq(Exam::getCourseId,exam.getCourseId())
                    .eq(Exam::getNianji,exam.getNianji()).eq(Exam::getExamName,exam.getExamName()));

            List<StudentCourse> studentCourseList = studentCourseService.getStudentCoursesByCourseIdAndNianJi(exam.getCourseId(),exam.getNianji());

            Exam finalExam = exam;
            studentCourseList.forEach(studentCourse -> {
                studentCourse.setExamId(finalExam.getId());
                studentCourse.setPageId(finalExam.getPageId());
                studentCourseService.updateById(studentCourse);
            });

        }
    }
}
