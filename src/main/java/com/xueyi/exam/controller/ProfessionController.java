package com.xueyi.exam.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xueyi.exam.beans.*;
import com.xueyi.exam.service.CourseService;
import com.xueyi.exam.service.ProfessionService;
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
 * @since 2020-12-05
 */
@Controller
@RequestMapping("/profession")
public class ProfessionController {

    @Autowired
    ProfessionService professionService;
    @Autowired
    CourseService courseService;

    @RequestMapping("/addOneProfession")
    @ResponseBody
    public R addOneProfession(Profession profession){
        QueryWrapper<Profession> professionQueryWrapper = new QueryWrapper<>();
        professionQueryWrapper.eq("profession_name",profession.getProfessionName().trim());
        Profession profession1 = professionService.getOne(professionQueryWrapper);
        if (profession1!=null) return new R(500,"已存在");
        if (professionService.save(profession)){
            return R.success;
        }
        return R.fail;
    }

    @RequestMapping("/professionSearch")
    @ResponseBody
    public LaiuiPage<Profession> courseSearch(LaiuiPage laiuiPage){
        Page page = professionService.page(new Page<>(laiuiPage.getPage(),laiuiPage.getLimit()));
        return LaiuiPage.creatDataPage(page.getRecords(),(int)page.getTotal());
    }

    @RequestMapping("/delOneProfession")
    @ResponseBody
    public R delOneProfession(int id){
        if (professionService.removeById(id)){
            return R.success;
        }
        return R.fail;
    }

    @RequestMapping("/updateOneProfession")
    @ResponseBody
    public R updateOneProfession(Profession profession){
        if (professionService.updateById(profession)){
            return R.success;
        }
        return R.fail;
    }

    @RequestMapping("/professionList")
    public String professionList(){
        return "pages/profession/professionList";
    }

    @RequestMapping("/professionDetails")
    public String courseDetails(int id, Model model){
        Profession profession = professionService.getById(id);
        model.addAttribute(profession);
        return "pages/profession/professionDetails";
    }

    @RequestMapping("/professionEdit")
    public String professionEdit(int id, Model model){
        Profession profession = professionService.getById(id);
        model.addAttribute(profession);
        List<Course> allCourse = courseService.list();
        List courses = courseService.getCoursesByProfessionId(id);
        allCourse.removeAll(courses);

        model.addAttribute("courseList",allCourse);
        return "pages/profession/professionEdit";
    }

    @RequestMapping("/professionAdd")
    public String professionAdd(){
        return "pages/profession/professionAdd";
    }

    @RequestMapping("/allProfession")
    @ResponseBody
    public List allProfession(){
        return professionService.list();
    }

    @RequestMapping("/getCoursesByProfessionId")
    @ResponseBody
    public LaiuiPage getCoursesByProfessionId(int professionId){
        return LaiuiPage.creatDataPage(courseService.getCoursesByProfessionId(professionId),0);
    }
}

