package com.xueyi.exam.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.xueyi.exam.beans.Page;
import com.xueyi.exam.beans.LaiuiPage;
import com.xueyi.exam.beans.R;
import com.xueyi.exam.service.CourseService;
import com.xueyi.exam.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mike
 * @since 2020-12-03
 */
@Controller
@RequestMapping("/page")
public class PageController {
    @Autowired
    PageService pageService;
    @Autowired
    CourseService courseService;

    @RequestMapping("/addOnePage")
    @ResponseBody
    public R addOnepage(Page page){
        QueryWrapper<Page> pageQueryWrapper = new QueryWrapper<>();
        pageQueryWrapper.eq("page_name",page.getPageName());
        Page page1 = pageService.getOne(pageQueryWrapper);
        if (page1!=null) return new R(500,"已存在");
        page.setCreatTime(LocalDateTime.now());
        if (pageService.save(page)){
            return R.success;
        }
        return R.fail;
    }

    @RequestMapping("/pageSearch")
    @ResponseBody
    public LaiuiPage<Page> pageSearch(LaiuiPage laiuiPage){
        com.baomidou.mybatisplus.extension.plugins.pagination.Page page = pageService.page(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(laiuiPage.getPage(),laiuiPage.getLimit()));
        return LaiuiPage.creatDataPage(page.getRecords(),(int)page.getTotal());
    }

    @RequestMapping("/delOnePage")
    @ResponseBody
    public R delOnepage(int id){
        if (pageService.removeById(id)){
            return R.success;
        }
        return R.fail;
    }

    @RequestMapping("/updateOnePage")
    @ResponseBody
    public R updateOnepage(Page page){
        if (pageService.updateById(page)){
            return R.success;
        }
        return R.fail;
    }

    @RequestMapping("/pageList")
    public String pageList(){
        return "pages/page/pageList";
    }

    @RequestMapping("/pageDetails")
    public String pageDetails(int id, Model model){
        Page page = pageService.getById(id);
        model.addAttribute(page);
        model.addAttribute("courseList",courseService.list());
        return "pages/page/pageDetails";
    }

    @RequestMapping("/pageEdit")
    public String pageEdit(int id, Model model){
        Page page = pageService.getById(id);
        model.addAttribute(page);
        model.addAttribute("courseList",courseService.list());
        return "pages/page/pageEdit";
    }

    @RequestMapping("/pageAdd")
    public String pageAdd(Model model){
        model.addAttribute("courseList",courseService.list());
        return "pages/page/pageAdd";
    }

    @RequestMapping("/questionAddList")
    public String questionAddList(int id,Model model){
        model.addAttribute("pageId",id);
        return "pages/question/questionAddList";
    }

}

