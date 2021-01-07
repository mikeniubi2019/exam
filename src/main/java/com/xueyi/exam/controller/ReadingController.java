package com.xueyi.exam.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xueyi.exam.beans.*;
import com.xueyi.exam.service.PageService;
import com.xueyi.exam.service.ReadingQuestionService;
import com.xueyi.exam.service.ReadingService;
import com.xueyi.exam.utils.QuestionUtils;
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
 * @since 2020-12-11
 */
@Controller
@RequestMapping("/reading")
public class ReadingController {
    @Autowired
    ReadingService readingService;
    @Autowired
    ReadingQuestionService readingQuestionService;
    @Autowired
    PageService pageService;
    @Autowired
    QuestionUtils questionUtils;

    @RequestMapping("/addOneReading")
    @ResponseBody
    public R addOneReading(Reading reading){
        if (readingService.save(reading)){
            return R.success;
        }
        return R.fail;
    }

    @RequestMapping("/readingSearch")
    @ResponseBody
    public LaiuiPage<Reading> readingSearch(Reading reading , LaiuiPage laiuiPage){
        com.baomidou.mybatisplus.extension.plugins.pagination.Page page = readingService.page(
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(laiuiPage.getPage(),laiuiPage.getLimit())
                ,new QueryWrapper<Reading>().lambda()
                        .eq(reading.getPageId()!=null,Reading::getPageId,reading.getPageId())
                        .eq(StringUtils.isNotBlank(reading.getTitle()),Reading::getTitle,reading.getTitle())
                        .orderByDesc(Reading::getId));
        return LaiuiPage.creatDataPage(page.getRecords(),(int)page.getTotal());
    }

    @RequestMapping("/delOneReading")
    @ResponseBody
    public R delOneReading(int id){
        Reading reading = readingService.getById(id);
        if (readingService.removeById(id)){
            Page page = pageService.getById(reading.getPageId());
            List<ReadingQuestion> readingQuestionList = readingQuestionService.list(new QueryWrapper<ReadingQuestion>().lambda().eq(ReadingQuestion::getTitleId,reading.getId()));

            page.setMaxScore(page.getMaxScore()-readingQuestionList.stream().mapToInt(ReadingQuestion::getQuestionScore).sum());
            page.setQuestionCount(page.getQuestionCount()-readingQuestionList.size());
            pageService.updateById(page);

            readingQuestionService.remove(new QueryWrapper<ReadingQuestion>().lambda().eq(ReadingQuestion::getTitleId,id));
            questionUtils.deleteReadingListByPageId(reading.getPageId());
            return R.success;
        }
        return R.fail;
    }

    @RequestMapping("/updateOneReading")
    @ResponseBody
    public R updateOneReading(Reading reading){
        if (readingService.updateById(reading)){
            return R.success;
        }
        return R.fail;
    }

    @RequestMapping("/readingList")
    public String readingList(){
        return "pages/question/readingList";
    }

    @RequestMapping("/readingDetails")
    public String readingDetails(int id, Model model){
        Reading reading = readingService.getById(id);
        model.addAttribute(reading);
        List<Page> pageList = pageService.list();

        model.addAttribute("pageList",pageList);
        List<ReadingQuestion> readingQuestionList = readingQuestionService.list(new QueryWrapper<ReadingQuestion>().lambda().eq(ReadingQuestion::getTitleId,reading.getId()));
        model.addAttribute("readingQuestionList",readingQuestionList);
        return "pages/question/readingDetails";
    }

    @RequestMapping("/readingEdit")
    public String readingEdit(int id, Model model){
        Reading reading = readingService.getById(id);
        model.addAttribute(reading);
        List<Page> pageList = pageService.list();

        model.addAttribute("pageList",pageList);
        List<ReadingQuestion> readingQuestionList = readingQuestionService.list(new QueryWrapper<ReadingQuestion>().lambda().eq(ReadingQuestion::getTitleId,reading.getId()));
        model.addAttribute("readingQuestionList",readingQuestionList);
        return "pages/question/readingEdit";
    }

    @RequestMapping("/readingAdd")
    public String readingAdd(Model model){
        List<Page> pageList = pageService.list();
        model.addAttribute("pageList",pageList);
        return "pages/question/readingAdd";
    }


    @RequestMapping("/addOneBlankReadingQuestion")
    @ResponseBody
    public R addOneBlankReadingQuestion(int readingId){
        ReadingQuestion readingQuestion = new ReadingQuestion();
        readingQuestion.setTitleId(readingId);
        readingQuestion.setQuestionAnswer("");
        readingQuestion.setQuestionChoice1("");
        readingQuestion.setQuestionChoice2("");
        readingQuestion.setQuestionChoice3("");
        readingQuestion.setQuestionChoice4("");
        readingQuestion.setQuestionChoice5("");
        readingQuestion.setQuestionChoice6("");
        readingQuestion.setQuestionScore(2);
        readingQuestion.setQuestionTitle("");
        if (readingQuestionService.count(new QueryWrapper<ReadingQuestion>().lambda().eq(ReadingQuestion::getTitleId,readingId).eq(ReadingQuestion::getQuestionTitle,""))>0){
            return R.fail;
        }
        return addOneReadingQuestion(readingQuestion);
    }

    @RequestMapping("/addOneReadingQuestion")
    @ResponseBody
    public R addOneReadingQuestion(ReadingQuestion readingQuestion){
        if (readingQuestionService.save(readingQuestion)){
            Page page = pageService.getById(readingService.getById(readingQuestion.getTitleId()).getPageId());
            page.setMaxScore(page.getMaxScore()+readingQuestion.getQuestionScore());
            page.setQuestionCount(page.getQuestionCount()+1);
            pageService.updateById(page);
            return R.success;
        }
        return R.fail;
    }

    @RequestMapping("/readingQuestionSearch")
    @ResponseBody
    public LaiuiPage<ReadingQuestion> readingQuestionSearch(ReadingQuestion readingQuestion ,LaiuiPage laiuiPage){
        com.baomidou.mybatisplus.extension.plugins.pagination.Page page = readingQuestionService.page(
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(laiuiPage.getPage(),laiuiPage.getLimit())
                ,new QueryWrapper<ReadingQuestion>().lambda()
                        .eq(readingQuestion.getTitleId()!=null,ReadingQuestion::getTitleId,readingQuestion.getTitleId())
                        .eq(StringUtils.isNotBlank(readingQuestion.getQuestionTitle()),ReadingQuestion::getQuestionTitle,readingQuestion.getQuestionTitle())
                        .orderByDesc(ReadingQuestion::getId));
        return LaiuiPage.creatDataPage(page.getRecords(),(int)page.getTotal());
    }

    @RequestMapping("/delOneReadingQuestion")
    @ResponseBody
    public R delOneReadingQuestion(int id){
        ReadingQuestion readingQuestion= readingQuestionService.getById(id);
        if (readingQuestionService.removeById(id)){
            Page page = pageService.getById(readingService.getById(readingQuestion.getTitleId()).getPageId());
            page.setMaxScore(page.getMaxScore()-readingQuestion.getQuestionScore());
            page.setQuestionCount(page.getQuestionCount()-1);
            pageService.updateById(page);
            questionUtils.deleteReadingListByPageId(page.getId());
            return R.success;
        }
        return R.fail;
    }

    @RequestMapping("/updateOneReadingQuestion")
    @ResponseBody
    public R updateOneReadingQuestion(ReadingQuestion readingQuestion){
        ReadingQuestion old = readingQuestionService.getById(readingQuestion.getId());
        if (readingQuestionService.updateById(readingQuestion)){
            if (readingQuestion.getQuestionScore()!=null&&readingQuestion.getQuestionScore()!=old.getQuestionScore()){
                Page page = pageService.getById(readingService.getById(old.getTitleId()).getPageId());
                page.setMaxScore(page.getMaxScore()+readingQuestion.getQuestionScore()-old.getQuestionScore());
                pageService.updateById(page);
                questionUtils.deleteReadingListByPageId(page.getId());
            }
            return R.success;
        }
        return R.fail;
    }

    @RequestMapping("/readingQuestionList")
    public String readingQuestionList(int id,Model model){
        model.addAttribute("readingId",id);
        return "pages/question/readingQuestionAddList";
    }
}

