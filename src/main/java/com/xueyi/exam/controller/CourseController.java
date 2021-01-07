package com.xueyi.exam.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xueyi.exam.beans.*;
import com.xueyi.exam.service.CourseService;
import com.xueyi.exam.service.ExamService;
import com.xueyi.exam.utils.SchoolUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mike
 * @since 2020-12-03
 */
@Controller
@RequestMapping("/course")
public class CourseController {
    @Autowired
    CourseService courseService;
    @Autowired
    ExamService examService;
    @Autowired
    DictionnaryController dictionnaryController;
    @Autowired
    SchoolUtils schoolUtils;

    @RequestMapping("/addOneCourse")
    @ResponseBody
    public R addOneCourse(Course course){
        QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<>();
        courseQueryWrapper.eq("course_name",course.getCourseName().trim());
        Course course1 = courseService.getOne(courseQueryWrapper);
        if (course1!=null) return new R(500,"已存在");
        if (courseService.save(course)){
            return R.success;
        }
        return R.fail;
    }

    @RequestMapping("/courseSearch")
    @ResponseBody
    public LaiuiPage<Course> courseSearch(LaiuiPage laiuiPage){
        Page page = courseService.page(new Page<>(laiuiPage.getPage(),laiuiPage.getLimit()));
        return LaiuiPage.creatDataPage(page.getRecords(),(int)page.getTotal());
    }

    @RequestMapping("/delOneCourse")
    @ResponseBody
    public R delOneCourse(int id){
        if (examService.count(new QueryWrapper<Exam>().lambda().eq(Exam::getCourseId,id))>0){
            return new R(500,"失败，删除课程前请把匹配了本课程的考试先删除！");
        }
        if (courseService.removeById(id)){
            return R.success;
        }
        return R.fail;
    }

    @RequestMapping("/updateOneCourse")
    @ResponseBody
    public R updateOneCourse(Course course){
        if (courseService.updateById(course)){
            return R.success;
        }
        return R.fail;
    }

    @RequestMapping("/courseList")
    public String courseList(){
        return "pages/course/courseList";
    }

    @RequestMapping("/courseDetails")
    public String courseDetails(int id, Model model){
        Course course = courseService.getById(id);
        model.addAttribute(course);
        model.addAttribute("xueQiList",dictionnaryController.getListByName("xueQi"));
        return "pages/course/courseDetails";
    }

    @RequestMapping("/courseEdit")
    public String courseEdit(int id, Model model){
        Course course = courseService.getById(id);
        model.addAttribute(course);
        model.addAttribute("xueQiList",dictionnaryController.getListByName("xueQi"));
        return "pages/course/courseEdit";
    }

    @RequestMapping("/courseAdd")
    public String courseAdd(Model model){
        model.addAttribute("xueQiList",dictionnaryController.getListByName("xueQi"));
        return "pages/course/courseAdd";
    }

    @RequestMapping("/allCourse")
    @ResponseBody
    public List allCourse(){
        return courseService.list();
    }

    @RequestMapping("/courseListPage")
    public String courseListPage(Model model){
        model.addAttribute("xuenianList",schoolUtils.getAllXueNian());
        model.addAttribute("xueqiList",schoolUtils.getAllXueQi());
        model.addAttribute("currentXuenian",schoolUtils.getCurrentXuenian());
        model.addAttribute("currentXueqi",schoolUtils.getCurrentXuenqi());
        return "pages/course/courseListPage";
    }
    @RequestMapping("/findCourseVoByXuenianAndXueqi")
    @ResponseBody
    public LaiuiPage findCourseVoByXuenianAndXueqi(String xuenian,String xueqi){
        Student student = schoolUtils.getCurrentStudent();

        if (StringUtils.isEmpty(xuenian)){
            xuenian = schoolUtils.getCurrentXuenian();
        }
        if (StringUtils.isEmpty(xueqi)){
            xueqi = schoolUtils.getCurrentXuenqi();
        }

        List<CourseVo> courseVoList = courseService.getOneStudentCoursesVoByCondition(student.getId(),Integer.parseInt(xuenian)-Integer.parseInt(student.getNianJi()),xueqi);

        return LaiuiPage.creatDataPage(courseVoList,courseVoList.size());
    }

}

