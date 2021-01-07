package com.xueyi.exam.mapper;

import com.xueyi.exam.beans.StudentCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mike
 * @since 2020-12-04
 */
@Repository
public interface StudentCourseMapper extends BaseMapper<StudentCourse> {
    List<StudentCourse> getStudentCoursesByCourseIdAndNianJi(@Param("courseId")Integer courseId,@Param("nianJi")String nianJi);

    int getScoreByIdAndCount(@Param("studentCourseId")Integer studentCourseId, @Param("count")Integer count);
}
