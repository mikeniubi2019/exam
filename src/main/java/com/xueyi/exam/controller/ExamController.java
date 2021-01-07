package com.xueyi.exam.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.xueyi.exam.beans.*;
import com.xueyi.exam.common.AsyDoExam;
import com.xueyi.exam.service.*;
import com.xueyi.exam.utils.ExamUtils;
import com.xueyi.exam.utils.QuestionUtils;
import com.xueyi.exam.utils.RedisUtils;
import com.xueyi.exam.utils.SchoolUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mike
 * @since 2020-12-03
 */
@Controller
@RequestMapping("/exam")
public class ExamController {

    @Autowired
    StudentCourseService studentCourseService;
    @Autowired
    ExamService examService;
    @Autowired
    PageService pageService;
    @Autowired
    DictionnaryController dictionnaryController;
    @Autowired
    StudentService studentService;
    @Autowired
    SchoolUtils schoolUtils;
    @Autowired
    CourseService courseService;
    @Autowired
    QuestionService questionService;
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    ReadingService readingService;
    @Autowired
    ReadingRecordService readingRecordService;
    @Autowired
    EssayQuestionService essayQuestionService;
    @Autowired
    ExamUtils examUtils;
    @Autowired
    QuestionUtils questionUtils;
    @Autowired
    RedisTemplate redisTemplate;

    @RequestMapping("/addOneExam")
    @ResponseBody
    public R addOneExam(Exam exam) throws InterruptedException {
        if (exam.getEndTime().isBefore(LocalDateTime.now())){
            return R.fail;
        }
        if (examService.count(new QueryWrapper<Exam>().lambda().eq(Exam::getCourseId,exam.getCourseId()).eq(Exam::getNianji,exam.getNianji()).eq(Exam::getExamName,exam.getExamName()))>0){
            return R.fail;
        }
        setPageCourseId(exam);
        if (examService.save(exam)){
            AsyDoExam.doTransferExamQueue.put(exam);
            return R.success;
        }
        return R.fail;
    }

    @RequestMapping("/examSearch")
    @ResponseBody
    public LaiuiPage<Exam> examSearch(LaiuiPage laiuiPage){
        Page page = examService.page(new Page<>(laiuiPage.getPage(),laiuiPage.getLimit()));
        return LaiuiPage.creatDataPage(page.getRecords(),(int)page.getTotal());
    }

    @RequestMapping("/delOneExam")
    @ResponseBody
    public R delOneExam(int id){
        if (examService.removeById(id)){
            studentCourseService.update(new StudentCourse(),new QueryWrapper<StudentCourse>().lambda().eq(StudentCourse::getExamId,id));
            //studentCourseService.remove(new QueryWrapper<StudentCourse>().lambda().eq(StudentCourse::getExamId,id));
            examUtils.deleteExamById(id);
            return R.success;
        }
        return R.fail;
    }

    @RequestMapping("/updateOneExam")
    @ResponseBody
    public R updateOneExam(Exam exam) throws InterruptedException {
        Exam oldExam = examService.getById(exam.getId());
        LocalDateTime now = LocalDateTime.now();
        setPageCourseId(exam);

        if (!oldExam.getNianji().equals(exam.getNianji())){
            //把旧的StudentCourse设置成最近的符合的考试id，如果不存在，旧的StudentCourse清掉examid
            List<Exam> examList = examService.list(new QueryWrapper<Exam>().lambda().eq(Exam::getCourseId,oldExam.getCourseId()).eq(Exam::getNianji,oldExam.getNianji()).ge(Exam::getEndTime,now));
            StudentCourse studentCourseExample = new StudentCourse();
            examList.remove(oldExam);
            if (examList.size()>0){
                Exam exam1 = examList.get(examList.size()-1);
                studentCourseExample.setExamId(exam1.getId());
            }
            studentCourseService.update(studentCourseExample,new QueryWrapper<StudentCourse>().lambda().eq(StudentCourse::getExamId,oldExam.getId()).le(StudentCourse::getScore,60));
            //把新的exam设置到符合的StudentCourse里面
            List<StudentCourse> studentCourseList = studentCourseService.getStudentCoursesByCourseIdAndNianJi(exam.getCourseId(),exam.getNianji());
            Exam finalExam = exam;
            studentCourseList.forEach(studentCourse -> {
                studentCourse.setExamId(finalExam.getId());
                studentCourse.setPageId(finalExam.getPageId());
                studentCourseService.updateById(studentCourse);
            });

        }else {
            //时间变化
            if (exam.getEndTime().isBefore(now)){
                List<Exam> examList = examService.list(new QueryWrapper<Exam>().lambda().eq(Exam::getCourseId,oldExam.getCourseId()).eq(Exam::getNianji,oldExam.getNianji()).ge(Exam::getEndTime,now));
                StudentCourse studentCourseExample = new StudentCourse();
                examList.remove(oldExam);
                if (examList.size()>0){
                    Exam exam1 = examList.get(examList.size()-1);
                    studentCourseExample.setExamId(exam1.getId());
                }
                studentCourseService.update(studentCourseExample,new QueryWrapper<StudentCourse>().lambda().eq(StudentCourse::getExamId,oldExam.getId()).le(StudentCourse::getScore,60));
            }else {
                List<StudentCourse> studentCourseList = studentCourseService.getStudentCoursesByCourseIdAndNianJi(exam.getCourseId(),exam.getNianji());
                Exam finalExam = exam;
                studentCourseList.forEach(studentCourse -> {
                    studentCourse.setExamId(finalExam.getId());
                    studentCourse.setPageId(finalExam.getPageId());
                    studentCourseService.updateById(studentCourse);
                });
            }
        }

        if (examService.updateById(exam)){
            examUtils.deleteExamById(exam.getId());
            return R.success;
        }
        return R.fail;
    }

    @RequestMapping("/examList")
    public String examList(){
        return "pages/exam/examList";
    }

    @RequestMapping("/examDetails")
    public String examDetails(int id, Model model){
        model.addAttribute("pageList",pageService.list());
        Exam exam = examService.getById(id);
        model.addAttribute(exam);
        model.addAttribute("nianJiList",dictionnaryController.getListByName("nianJi"));
        return "pages/exam/examDetails";
    }

    @RequestMapping("/examEdit")
    public String examEdit(int id, Model model){
        model.addAttribute("pageList",pageService.list());
        Exam exam = examService.getById(id);
        model.addAttribute(exam);
        model.addAttribute("nianJiList",dictionnaryController.getListByName("nianJi"));
        return "pages/exam/examEdit";
    }

    @RequestMapping("/examAdd")
    public String examAdd(Model model){
        model.addAttribute("pageList",pageService.list());
        Date date = new Date();
        model.addAttribute("startTime",date);
        model.addAttribute("endTime",date);
        model.addAttribute("nianJiList",dictionnaryController.getListByName("nianJi"));
        return "pages/exam/examAdd";
    }

    private void setPageCourseId(Exam exam){
        pageService.list().stream().forEach(page -> {
            if (exam.getPageId()==page.getId()) {
                exam.setCourseId(page.getCourseId());
            }
        });
    }

    @RequestMapping("/examListPage")
    public String examListPage(){
        return "pages/exam/examListPage";
    }

    @RequestMapping("/findCourseVoByStudent")
    @ResponseBody
    public LaiuiPage findCourseVoByStudent(){
        Student student = schoolUtils.getCurrentStudent();
        List<CourseVo> courseVoList = examUtils.getBaseExamCoursesVoByCondition(student.getId());
        return LaiuiPage.creatDataPage(courseVoList,courseVoList.size());
    }

    @RequestMapping("/generatorHotCache")
    @ResponseBody
    public R generatorHotCache(int examId){
        List<StudentCourse> studentCourseList = studentCourseService.list(new QueryWrapper<StudentCourse>().lambda().eq(StudentCourse::getExamId,examId));
        studentCourseList.forEach(studentCourse -> examUtils.getBaseExamCoursesVoByCondition(studentCourse.getStudentId()));
        return R.success;
    }

    @RequestMapping("/deleteHotCache")
    @ResponseBody
    public R deleteHotCache(){
        Set set = redisUtils.keys("ExamCoursesVo_*");
        redisTemplate.delete(set);
        return R.success;
    }

    @RequestMapping("/generatorHotCachePage")

    public String generatorHotCachePage(Model model){
        model.addAttribute("examList",examService.list());
        return "pages/exam/generatorHotCachePage";
    }

    @RequestMapping("/examingPage")
    public String examingPage(int pageId ,int examId ,int scId,Model model){
        Subject subject = SecurityUtils.getSubject();
        Student student = schoolUtils.getCurrentStudent();
        String userName = subject.getPrincipal().toString();

        Exam exam = examUtils.getExamById(examId);

        if (exam==null){
            return "error";
        }else {
            LocalDateTime localDateTime = LocalDateTime.now();
            if (exam.getEndTime().isBefore(localDateTime)||exam.getStartTime().isAfter(localDateTime)){
                return "error";
            }
        }

        int duration = exam.getExamDuration();
        int hour = duration/60;
        int mi = duration%60;

        String examTime = hour+":"+(mi>9?mi:"0"+mi);

        List<Question> questionList = questionUtils.getQuestionListByPageId(pageId);
        Map<Integer,List<Question>> singleListMap = questionList.stream().collect(Collectors.groupingBy(Question::getIsSingle));

        model.addAttribute("examTime",examTime);
        model.addAttribute("duration",duration);
        model.addAttribute("singel",singleListMap.get(1));
        model.addAttribute("muilty",singleListMap.get(0));

        if (singleListMap.get(1)!=null){
            model.addAttribute("singelCount",singleListMap.get(1).size());
            model.addAttribute("singelScore",singleListMap.get(1).stream().mapToInt(Question::getQuestionScore).reduce(0, Integer::sum));

        }else {
            model.addAttribute("singelCount",null);
            model.addAttribute("singelScore",null);
        }

        if (singleListMap.get(0)!=null){
            model.addAttribute("muiltyCount",singleListMap.get(0).size());
            model.addAttribute("muiltyScore",singleListMap.get(0).stream().mapToInt(Question::getQuestionScore).reduce(0, Integer::sum));
        }else {
            model.addAttribute("muiltyCount",null);
            model.addAttribute("muiltyScore",null);

        }

        List<Reading> readingList = questionUtils.getReadingListByPageId(pageId);
        model.addAttribute("readingList",readingList);

        if (readingList!=null&&readingList.size()>0){
            int score = readingList.stream().flatMap(reading -> reading.getReadingQuestionList().stream()).mapToInt(ReadingQuestion::getQuestionScore).sum();
            int count = readingList.stream().mapToInt(reading -> reading.getReadingQuestionList().size()).sum();
            model.addAttribute("readingCount",count);
            model.addAttribute("readingScore",score);
        }else {
            model.addAttribute("readingCount",null);
            model.addAttribute("readingScore",null);
        }

        List<EssayQuestion> essayQuestionList = questionUtils.getEssayQuestionListByPageId(pageId);
        model.addAttribute("essayQuestionList",essayQuestionList);

        if (essayQuestionList!=null&&essayQuestionList.size()>0){
            model.addAttribute("essayCount",essayQuestionList.size());
            model.addAttribute("essayScore",essayQuestionList.stream().mapToInt(EssayQuestion::getScore).sum());
        }else {
            model.addAttribute("essayCount",null);
            model.addAttribute("essayScore",null);
        }

        redisUtils.set(QuestionController.TOKEN+userName+student.getId(),userName,exam.getExamDuration()*60);

        model.addAttribute("examToken",userName+student.getId());
        model.addAttribute("studentId",student.getId());
        model.addAttribute("pageId",pageId);
        model.addAttribute("scId",scId);

        return "pages/exam/examingPage";
    }



}

