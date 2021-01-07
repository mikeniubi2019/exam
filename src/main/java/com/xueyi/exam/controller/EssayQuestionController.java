package com.xueyi.exam.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xueyi.exam.beans.*;
import com.xueyi.exam.service.EssayQuestionService;
import com.xueyi.exam.service.EssayRecordService;
import com.xueyi.exam.service.PageService;
import com.xueyi.exam.service.StudentCourseService;
import com.xueyi.exam.utils.ExamUtils;
import com.xueyi.exam.utils.QuestionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mike
 * @since 2020-12-11
 */
@Controller
@RequestMapping("/essayQuestion")
public class EssayQuestionController {
    @Autowired
    EssayQuestionService essayQuestionService;
    @Autowired
    PageService pageService;
    @Autowired
    EssayRecordService essayRecordService;
    @Autowired
    StudentCourseService studentCourseService;
    @Autowired
    QuestionUtils questionUtils;
    @Autowired
    ExamUtils examUtils;

    @RequestMapping("/addOneEssayQuestion")
    @ResponseBody
    public R addOneEssayQuestion(EssayQuestion essayQuestion){
        if (essayQuestionService.save(essayQuestion)){
            Page page = pageService.getById(essayQuestion.getPageId());
            page.setMaxScore(page.getMaxScore()+essayQuestion.getScore());
            page.setQuestionCount(page.getQuestionCount()+1);
            pageService.updateById(page);
            questionUtils.deleteEssayQuestionListByPageId(essayQuestion.getPageId());
            return R.success;
        }
        return R.fail;
    }

    @RequestMapping("/essayQuestionSearch")
    @ResponseBody
    public LaiuiPage<EssayQuestion> essayQuestionSearch(EssayQuestion essayQuestion , LaiuiPage laiuiPage){
        com.baomidou.mybatisplus.extension.plugins.pagination.Page page = essayQuestionService.page(
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(laiuiPage.getPage(),laiuiPage.getLimit())
                ,new QueryWrapper<EssayQuestion>().lambda()
                        .eq(essayQuestion.getPageId()!=null,EssayQuestion::getPageId,essayQuestion.getPageId())
                        .eq(StringUtils.isNotBlank(essayQuestion.getTitle()),EssayQuestion::getTitle,essayQuestion.getTitle())
                        .orderByDesc(EssayQuestion::getId));
        return LaiuiPage.creatDataPage(page.getRecords(),(int)page.getTotal());
    }

    @RequestMapping("/delOneEssayQuestion")
    @ResponseBody
    public R delOneEssayQuestion(int id){
        EssayQuestion essayQuestion = essayQuestionService.getById(id);
        if (essayQuestionService.removeById(id)){
            Page page = pageService.getById(essayQuestion.getPageId());
            if (page!=null){
                page.setMaxScore(page.getMaxScore()-essayQuestion.getScore());
                page.setQuestionCount(page.getQuestionCount()-1);
                pageService.updateById(page);
            }
            questionUtils.deleteEssayQuestionListByPageId(essayQuestion.getPageId());
            return R.success;
        }
        return R.fail;
    }

    @RequestMapping("/updateOneEssayQuestion")
    @ResponseBody
    public R updateOneEssayQuestion(EssayQuestion essayQuestion){
        EssayQuestion old = essayQuestionService.getById(essayQuestion.getId());
        if (essayQuestionService.updateById(essayQuestion)){
            if (essayQuestion.getScore()!=null&&essayQuestion.getScore()!=old.getScore()){
                Page page =  pageService.getById(old.getPageId());
                page.setMaxScore(page.getMaxScore()+essayQuestion.getScore()-old.getScore());
                pageService.updateById(page);
            }
            questionUtils.deleteEssayQuestionListByPageId(old.getPageId());
            return R.success;
        }
        return R.fail;
    }

    @RequestMapping("/essayQuestionList")
    public String essayQuestionList(){
        return "pages/question/essayQuestionList";
    }

    @RequestMapping("/essayQuestionDetails")
    public String essayQuestionDetails(int id, Model model){
        EssayQuestion essayQuestion = essayQuestionService.getById(id);
        model.addAttribute(essayQuestion);
        List<Page> pageList = pageService.list();
        model.addAttribute("pageList",pageList);
        return "pages/question/essayQuestionDetails";
    }

    @RequestMapping("/essayQuestionEdit")
    public String essayQuestionEdit(int id, Model model){
        EssayQuestion essayQuestion = essayQuestionService.getById(id);
        model.addAttribute(essayQuestion);
        List<Page> pageList = pageService.list();
        model.addAttribute("pageList",pageList);
        return "pages/question/essayQuestionEdit";
    }

    @RequestMapping("/essayQuestionAdd")
    public String essayQuestionAdd(Model model){
        List<Page> pageList = pageService.list();
        model.addAttribute("pageList",pageList);
        return "pages/question/essayQuestionAdd";
    }

    @RequestMapping("/reviewEssayQuestionList")
    public String reviewEssayQuestionList(Model model){
        List<Page> pageList = pageService.list();
        model.addAttribute("pageList",pageList);
        return "pages/question/reviewEssayQuestionList";
    }

    @RequestMapping("/essayQuestionVoSearch")
    @ResponseBody
    public LaiuiPage<EssayQuestionVo> essayQuestionVoSearch(EssayQuestion essayQuestion , LaiuiPage laiuiPage){
        com.baomidou.mybatisplus.extension.plugins.pagination.Page page = essayQuestionService.page(
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(laiuiPage.getPage(),laiuiPage.getLimit())
                ,new QueryWrapper<EssayQuestion>().lambda()
                        .eq(essayQuestion.getPageId()!=null,EssayQuestion::getPageId,essayQuestion.getPageId())
                        .eq(StringUtils.isNotBlank(essayQuestion.getTitle()),EssayQuestion::getTitle,essayQuestion.getTitle())
                        .orderByDesc(EssayQuestion::getId));
        List<EssayQuestion> essayQuestionList = page.getRecords();
        List list = essayQuestionList.stream().map(essay->essayQuestionTransferToVo(essay)).collect(Collectors.toList());
        return LaiuiPage.creatDataPage(list,(int)page.getTotal());
    }


    @RequestMapping("/essayReviewPage")
    public String essayReviewPage(int id , Model model){
        EssayQuestionVo essayQuestionVo = essayQuestionTransferToVo(essayQuestionService.getById(id));
        model.addAttribute("essayQuestion",essayQuestionVo);
        R r = getOneEssayRecordByQuestionId(essayQuestionVo.getId());
        model.addAttribute("essayRecord",r.getData());
        return "pages/question/essayReviewPage";
    }

    @RequestMapping("/updateOneEssayRecord")
    @ResponseBody
    public R updateOneEssayRecord(EssayRecord essayRecord){
        if (essayRecordService.updateById(essayRecord)){
            return R.success;
        }
        return R.fail;
    }


    @RequestMapping("/getOneEssayRecordByQuestionId")
    public R getOneEssayRecordByQuestionId(int id){
        EssayRecord essayRecord = essayRecordService.getOne(new QueryWrapper<EssayRecord>().lambda().eq(EssayRecord::getQuestionId,id).isNull(EssayRecord::getGetScore).last("limit 1"));
        R r = new R();
        if (essayRecord==null){
            r.setCode(200);
            r.setM("本题已经批改完，暂无该题");
        }else {
            r.setCode(100);
            r.setData(essayRecord);
        }
        return r;
    }

    @RequestMapping("/updateOneEssayRecordAndSendNext")
    @ResponseBody
    public R updateOneEssayRecordAndSendNext(EssayRecord essayRecord){
        EssayRecord old = essayRecordService.getById(essayRecord.getId());
        EssayQuestion essayQuestion = essayQuestionService.getById(old.getQuestionId());
        if (essayRecord.getGetScore()>essayQuestion.getScore()){
            EssayRecord essayRecord2 = essayRecordService.getOne(new QueryWrapper<EssayRecord>().lambda().eq(EssayRecord::getQuestionId,essayRecord.getQuestionId()).isNull(EssayRecord::getGetScore).last("limit 1"));
            R r = new R();
            r.setCode(100);
            r.setData(essayRecord2);
            return r;
        }

        if (essayRecordService.updateById(essayRecord)){
            StudentCourse studentCourse = studentCourseService.getById(old.getStudentCourseId());
            if (studentCourse.getCount().equals(old.getCount())){
                studentCourse.setScore(studentCourse.getScore()+essayRecord.getGetScore());
                studentCourseService.updateById(studentCourse);
            }else {
                int score = studentCourseService.getScoreByIdAndCount(old.getStudentCourseId(),old.getCount());
                if (score>studentCourse.getScore()){
                    studentCourse.setScore(score);
                    studentCourseService.updateById(studentCourse);
                }
            }
            EssayRecord essayRecord2 = essayRecordService.getOne(new QueryWrapper<EssayRecord>().lambda().eq(EssayRecord::getQuestionId,essayRecord.getQuestionId()).isNull(EssayRecord::getGetScore).last("limit 1"));
            R r = new R();
            r.setCode(100);
            r.setData(essayRecord2);
            examUtils.deleteExamCoursesVoHotCache(studentCourse.getStudentId());
            return r;
        }
        return R.fail;
    }

    private EssayQuestionVo essayQuestionTransferToVo(EssayQuestion essay){
        EssayQuestionVo essayQuestionVo = new EssayQuestionVo();
        essayQuestionVo.setId(essay.getId());
        essayQuestionVo.setTitle(essay.getTitle());
        essayQuestionVo.setPageId(essay.getPageId());
        essayQuestionVo.setScore(essay.getScore());
        int count = essayRecordService.count(new QueryWrapper<EssayRecord>().lambda().eq(EssayRecord::getQuestionId,essay.getId()).isNull(EssayRecord::getGetScore));
        essayQuestionVo.setCount(count);
        return essayQuestionVo;
    }

}

