package com.xueyi.exam.mapper;

import com.xueyi.exam.beans.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xueyi.exam.beans.CourseVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mike
 * @since 2020-12-03
 */
@Repository
public interface CourseMapper extends BaseMapper<Course> {
    List<Course> getCoursesByProfessionId(Integer professionId);
    List<CourseVo> getOneStudentCoursesVoByCondition(@Param("studentId") Integer studentId,@Param("xueNian") Integer xueNian,@Param("xueQi") String xueQi);
    List<CourseVo> getBaseExamCoursesVoByCondition(@Param("studentId") Integer studentId, @Param("nowTime")LocalDateTime nowTime);


}
