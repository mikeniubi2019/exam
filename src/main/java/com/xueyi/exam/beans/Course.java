package com.xueyi.exam.beans;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Objects;

/**
 * <p>
 * 
 * </p>
 *
 * @author mike
 * @since 2020-12-06
 */
public class Course extends Model<Course> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String courseName;

    private String courseTeacher;

    private String courseDescription;

    private Integer courseXueNian;

    private String courseXueQi;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseTeacher() {
        return courseTeacher;
    }

    public void setCourseTeacher(String courseTeacher) {
        this.courseTeacher = courseTeacher;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public Integer getCourseXueNian() {
        return courseXueNian;
    }

    public void setCourseXueNian(Integer courseXueNian) {
        this.courseXueNian = courseXueNian;
    }

    public String getCourseXueQi() {
        return courseXueQi;
    }

    public void setCourseXueQi(String courseXueQi) {
        this.courseXueQi = courseXueQi;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(id, course.id) &&
                Objects.equals(courseName, course.courseName) &&
                Objects.equals(courseTeacher, course.courseTeacher) &&
                Objects.equals(courseDescription, course.courseDescription) &&
                Objects.equals(courseXueNian, course.courseXueNian) &&
                Objects.equals(courseXueQi, course.courseXueQi);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, courseName, courseTeacher, courseDescription, courseXueNian, courseXueQi);
    }

    @Override
    public String toString() {
        return "Course{" +
        "id=" + id +
        ", courseName=" + courseName +
        ", courseTeacher=" + courseTeacher +
        ", courseDescription=" + courseDescription +
        ", courseXueNian=" + courseXueNian +
        ", courseXueQi=" + courseXueQi +
        "}";
    }
}
