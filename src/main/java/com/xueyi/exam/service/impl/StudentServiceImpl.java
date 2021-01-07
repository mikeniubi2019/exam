package com.xueyi.exam.service.impl;

import com.xueyi.exam.beans.LaiuiPage;
import com.xueyi.exam.beans.Student;
import com.xueyi.exam.beans.StudentCourseVo;
import com.xueyi.exam.mapper.StudentMapper;
import com.xueyi.exam.service.StudentService;
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
 * @since 2020-12-05
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
    @Autowired
    StudentMapper studentMapper;
    @Override
    public List<StudentCourseVo> findStudentExamByCondiction(LaiuiPage laiuiPage, Student student, Integer courseId) {
        return studentMapper.findStudentExamByCondiction(laiuiPage, student, courseId);
    }
}
