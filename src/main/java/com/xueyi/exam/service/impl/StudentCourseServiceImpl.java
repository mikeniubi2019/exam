package com.xueyi.exam.service.impl;

import com.xueyi.exam.beans.StudentCourse;
import com.xueyi.exam.mapper.StudentCourseMapper;
import com.xueyi.exam.service.StudentCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mike
 * @since 2020-12-03
 */
@Service
public class StudentCourseServiceImpl extends ServiceImpl<StudentCourseMapper, StudentCourse> implements StudentCourseService {
    @Autowired
    StudentCourseMapper studentCourseMapper;
    @Override
    public List<StudentCourse> getStudentCoursesByCourseIdAndNianJi(Integer courseId, String nianJi) {
        return studentCourseMapper.getStudentCoursesByCourseIdAndNianJi(courseId,nianJi);
    }

    @Override
    public int getScoreByIdAndCount(Integer studentCourseId, Integer count) {
        return studentCourseMapper.getScoreByIdAndCount(studentCourseId,count);
    }
}
