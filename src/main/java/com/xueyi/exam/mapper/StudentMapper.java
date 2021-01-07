package com.xueyi.exam.mapper;

import com.xueyi.exam.beans.LaiuiPage;
import com.xueyi.exam.beans.Student;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xueyi.exam.beans.StudentCourseVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mike
 * @since 2020-12-05
 */
@Repository
public interface StudentMapper extends BaseMapper<Student> {
    List<Student> getStudentsByProfessionId(int professionId);

    List<StudentCourseVo> findStudentExamByCondiction(@Param("laiuiPage") LaiuiPage laiuiPage, @Param("student")Student student, @Param("courseId")Integer courseId);
}
