package com.xueyi.exam.service;

import com.xueyi.exam.beans.Course;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xueyi.exam.beans.CourseVo;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mike
 * @since 2020-12-03
 */
public interface CourseService extends IService<Course> {
    List<Course> getCoursesByProfessionId(int professionId);
    List<CourseVo> getOneStudentCoursesVoByCondition(Integer studentId,Integer xueNian,String xueQi);
    List<CourseVo> getBaseExamCoursesVoByCondition(Integer studentId, LocalDateTime nowTime);
}
