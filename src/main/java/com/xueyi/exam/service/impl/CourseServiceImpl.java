package com.xueyi.exam.service.impl;

import com.xueyi.exam.beans.Course;
import com.xueyi.exam.beans.CourseVo;
import com.xueyi.exam.mapper.CourseMapper;
import com.xueyi.exam.service.CourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {
    @Autowired
    CourseMapper courseMapper;

    @Override
    public List<Course> getCoursesByProfessionId(int professionId) {
        return courseMapper.getCoursesByProfessionId(professionId);
    }

    @Override
    public List<CourseVo> getOneStudentCoursesVoByCondition(Integer studentId, Integer xueNian, String xueQi) {
        return courseMapper.getOneStudentCoursesVoByCondition(studentId, xueNian, xueQi);
    }

    @Override
    public List<CourseVo> getBaseExamCoursesVoByCondition(Integer studentId, LocalDateTime nowTime) {
        return courseMapper.getBaseExamCoursesVoByCondition(studentId,nowTime);
    }
}
