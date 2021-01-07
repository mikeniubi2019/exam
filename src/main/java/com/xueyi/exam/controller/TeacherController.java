package com.xueyi.exam.controller;

import com.xueyi.exam.beans.R;
import com.xueyi.exam.beans.Teacher;
import com.xueyi.exam.service.TeacherService;
import com.xueyi.exam.utils.SchoolUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mike
 * @since 2020-12-03
 */
@Controller
@RequestMapping("/teacher")
public class TeacherController {
    @Autowired
    SchoolUtils schoolUtils;
    @Autowired
    TeacherService teacherService;

    @RequestMapping("/teacherEditNoId")
    public String teacherEditNoId(Model model){
        Teacher teacher = schoolUtils.getCurrentTeacher();
        model.addAttribute(teacher);
        return "pages/teacher/teacherEdit";
    }

    @RequestMapping("/updateOneTeacher")
    @ResponseBody
    public R updateOneTeacher(Teacher teacher){
        if (teacherService.updateById(teacher)){
            return R.success;
        }
        return R.fail;
    }
}

