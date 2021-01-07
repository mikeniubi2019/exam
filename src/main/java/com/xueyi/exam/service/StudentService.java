package com.xueyi.exam.service;

import com.xueyi.exam.beans.LaiuiPage;
import com.xueyi.exam.beans.Student;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xueyi.exam.beans.StudentCourseVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mike
 * @since 2020-12-05
 */
public interface StudentService extends IService<Student> {

    List<StudentCourseVo> findStudentExamByCondiction(LaiuiPage laiuiPage, Student student, Integer courseId);
}
