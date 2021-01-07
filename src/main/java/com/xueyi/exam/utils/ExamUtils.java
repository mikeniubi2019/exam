package com.xueyi.exam.utils;

import com.xueyi.exam.beans.CourseVo;
import com.xueyi.exam.beans.Exam;
import com.xueyi.exam.service.CourseService;
import com.xueyi.exam.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ExamUtils {
    private final String EXAM="exam_" ;
    private final String ExamCoursesVo = "ExamCoursesVo_";
    private final int HOUER = 60*60;
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    ExamService examService;
    @Autowired
    CourseService courseService;

    public Exam getExamById(int id){
        Exam exam = (Exam)redisUtils.get(EXAM+id);
        if (exam==null){
            exam = examService.getById(id);
            redisUtils.set(EXAM+id,exam,HOUER);
        }
        return exam;
    }

    public void deleteExamById(int id){
        redisUtils.del(EXAM+id);
    }

    public List<CourseVo> getBaseExamCoursesVoByCondition(int studentId){
        List<CourseVo> courseVoList = (List<CourseVo>)redisUtils.get(ExamCoursesVo+studentId);
        if (courseVoList==null||courseVoList.size()<1){
            courseVoList = courseService.getBaseExamCoursesVoByCondition(studentId, LocalDateTime.now());
            redisUtils.set(ExamCoursesVo+studentId,courseVoList,60*60);
        }
        return courseVoList;
    }

    public void deleteExamCoursesVoHotCache(int studentId){
        redisUtils.del(ExamCoursesVo+studentId);
    }
}
