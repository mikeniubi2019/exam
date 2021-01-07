package com.xueyi.exam.service;

import com.xueyi.exam.beans.StudentCourse;
import com.baomidou.mybatisplus.extension.service.IService;


import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mike
 * @since 2020-12-03
 */
public interface StudentCourseService extends IService<StudentCourse> {
    List<StudentCourse> getStudentCoursesByCourseIdAndNianJi(Integer courseId, String nianJi);

    int getScoreByIdAndCount(Integer studentCourseId, Integer count);
}
