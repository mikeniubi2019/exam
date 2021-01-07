package com.xueyi.exam.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xueyi.exam.beans.*;
import com.xueyi.exam.service.*;
import com.xueyi.exam.utils.ExamUtils;
import com.xueyi.exam.utils.QuestionUtils;
import com.xueyi.exam.utils.RedisUtils;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;
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
@RequestMapping("/question")
public class QuestionController {
    @Autowired
    QuestionService questionService;
    @Autowired
    PageService pageService;
    @Autowired
    QuestionRecordService questionRecordService;
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    StudentCourseService studentCourseService;
    @Autowired
    ReadingService readingService;
    @Autowired
    ReadingRecordService readingRecordService;
    @Autowired
    EssayQuestionService essayQuestionService;
    @Autowired
    ReadingQuestionService readingQuestionService;
    @Autowired
    EssayRecordService essayRecordService;
    @Autowired
    ExamService examService;
    @Autowired
    ExamUtils examUtils;
    @Autowired
    QuestionUtils questionUtils;

    private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()+1);


    public static final String TOKEN = "token_";
    @RequestMapping("/addOneQuestion")
    @ResponseBody
    public R addOneQuestion(Question question){
        if (questionService.save(question)){
            Page page = pageService.getById(question.getPageId());
            page.setMaxScore(page.getMaxScore()+question.getQuestionScore());
            page.setQuestionCount(page.getQuestionCount()+1);
            pageService.updateById(page);
            questionUtils.deleteQuestionListByPageId(question.getPageId());
            return R.success;
        }
        return R.fail;
    }

    @RequestMapping("/questionSearch")
    @ResponseBody
    public LaiuiPage<Question> QuestionSearch(Question question ,LaiuiPage laiuiPage){
        com.baomidou.mybatisplus.extension.plugins.pagination.Page page = questionService.page(
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(laiuiPage.getPage(),laiuiPage.getLimit())
        ,new QueryWrapper<Question>().lambda()
                        .eq(question.getPageId()!=null,Question::getPageId,question.getPageId())
                .eq(StringUtils.isNotBlank(question.getQuestionTitle()),Question::getQuestionTitle,question.getQuestionTitle())
                .eq(question.getIsSingle()!=null,Question::getIsSingle,question.getIsSingle())
                .orderByDesc(Question::getId));
        return LaiuiPage.creatDataPage(page.getRecords(),(int)page.getTotal());
    }

    @RequestMapping("/delOneQuestion")
    @ResponseBody
    public R delOneQuestion(int id){
        Question question = questionService.getById(id);
        if (questionService.removeById(id)){
            Page page = pageService.getById(question.getPageId());
            page.setMaxScore(page.getMaxScore()-question.getQuestionScore());
            page.setQuestionCount(page.getQuestionCount()-1);
            pageService.updateById(page);
            questionUtils.deleteQuestionListByPageId(question.getPageId());
            return R.success;
        }
        return R.fail;
    }

    @RequestMapping("/updateOneQuestion")
    @ResponseBody
    public R updateOneQuestion(Question question){
        Question old = questionService.getById(question.getId());
        if (questionService.updateById(question)){
            if (question.getQuestionScore()!=null&&question.getQuestionScore()!=old.getQuestionScore()){
                Page page =  pageService.getById(old.getPageId());
                page.setMaxScore(page.getMaxScore()+question.getQuestionScore()-old.getQuestionScore());
                pageService.updateById(page);
            }
            questionUtils.deleteQuestionListByPageId(old.getPageId());
            return R.success;
        }
        return R.fail;
    }

    @RequestMapping("/questionList")
    public String questionList(){
        return "pages/question/questionList";
    }

    @RequestMapping("/questionDetails")
    public String questionDetails(int id, Model model){
        Question question = questionService.getById(id);
        model.addAttribute(question);
        List<Page> pageList = pageService.list();
        model.addAttribute("pageList",pageList);
        return "pages/question/questionDetails";
    }

    @RequestMapping("/questionEdit")
    public String questionEdit(int id, Model model){
        Question question = questionService.getById(id);
        model.addAttribute(question);
        List<Page> pageList = pageService.list();
        model.addAttribute("pageList",pageList);
        return "pages/question/questionEdit";
    }

    @RequestMapping("/questionAdd")
    public String questionAdd(Model model){
        List<Page> pageList = pageService.list();
        model.addAttribute("pageList",pageList);
        return "pages/question/questionAdd";
    }

    @RequestMapping("/addOneBlankQuestion")
    @ResponseBody
    public R addOneBlankQuestion(int pageId){
        Question question = new Question();
        question.setPageId(pageId);
        question.setIsSingle(1);
        question.setQuestionAnswer("");
        question.setQuestionChoice1("");
        question.setQuestionChoice2("");
        question.setQuestionChoice3("");
        question.setQuestionChoice4("");
        question.setQuestionChoice5("");
        question.setQuestionChoice6("");
        question.setQuestionScore(2);
        question.setQuestionTitle("");
        if (questionService.count(new QueryWrapper<Question>().lambda().eq(Question::getPageId,pageId).eq(Question::getQuestionTitle,""))>0){
            return R.fail;
        }
        return addOneQuestion(question);
    }

    @RequestMapping("/receiveExamResult")
    @ResponseBody
    public R receiveExamResult(@RequestParam Map<String,String> map){
        String examToken = map.get("examToken");
        if (redisUtils.get(TOKEN+examToken)==null){
            return R.fail;
        }
        redisUtils.del(TOKEN+examToken);

        Integer studentId = Integer.parseInt((String)map.get("studentId"));
        Integer pageId = Integer.parseInt((String)map.get("pageId"));
        Integer scId = Integer.parseInt((String)map.get("scId"));

        map.remove("examToken");
        map.remove("studentId");
        map.remove("pageId");
        map.remove("scId");

        StudentCourse studentCourse = studentCourseService.getById(scId);
        Exam exam = examUtils.getExamById(studentCourse.getExamId());

        int examCount = 1;

        if (studentCourse.getCount()!=null){
            examCount = studentCourse.getCount()+1;
        }

        if (exam.getStudentCount()<examCount){
            return R.fail;
        }

        studentCourse.setCount(examCount);
        studentCourse.setExamTime(LocalDateTime.now());

        AtomicInteger score = new AtomicInteger();
        int finalExamCount = examCount;
        String mark = UUID.randomUUID().toString();
        executorService.execute(()->{
            List<QuestionRecord> questionList = new ArrayList<>(60);
            List<ReadingRecord> readingQuestionList = new ArrayList<>(30);
            List<EssayRecord> essayQuestionList = new ArrayList<>(20);
            map.entrySet().stream().forEach(entry->{
                String[] key = entry.getKey().split("_");
                if (key.length<2){
                    Integer qId = Integer.parseInt(entry.getKey());
                    Question question = questionService.getById(qId);
                    QuestionRecord questionRecord = new QuestionRecord();
                    questionRecord.setAnwser(entry.getValue());
                    questionRecord.setQuestionId(qId);
                    questionRecord.setStudentId(studentId);
                    questionRecord.setStudentCourseId(scId);
                    questionRecord.setCount(finalExamCount);
                    if (question.getQuestionAnswer().equals(entry.getValue())){
                        score.addAndGet(question.getQuestionScore());
                        questionRecord.setIsCorect(1);
                    }else {
                        questionRecord.setIsCorect(0);
                    }

                    questionList.add(questionRecord);
                }else if (key[0].equals("r")){
                    Integer rid = Integer.parseInt(key[1]);
                    ReadingQuestion readingQuestion = readingQuestionService.getById(rid);
                    ReadingRecord readingRecord = new ReadingRecord();
                    readingRecord.setAnwser(entry.getValue());
                    readingRecord.setQuestionId(rid);
                    readingRecord.setStudentId(studentId);
                    readingRecord.setStudentCourseId(scId);
                    readingRecord.setCount(finalExamCount);
                    if (readingQuestion.getQuestionAnswer().equals(entry.getValue())){
                        score.addAndGet(readingQuestion.getQuestionScore());
                        readingRecord.setIsCorect(1);
                    }else {
                        readingRecord.setIsCorect(0);
                    }

                    readingQuestionList.add(readingRecord);
                }else if (key[0].equals("e")){
                    EssayRecord essayRecord = new EssayRecord();
                    essayRecord.setAnswer(entry.getValue());
                    essayRecord.setQuestionId(Integer.parseInt(key[1]));
                    essayRecord.setStudentCourseId(scId);
                    essayRecord.setStudentId(studentId);
                    essayRecord.setCount(finalExamCount);

                    essayQuestionList.add(essayRecord);
                }else {
                    System.out.println(studentId+":未知交卷数据:"+entry.getKey()+entry.getValue());
                }
            });
            questionRecordService.saveBatch(questionList);
            readingRecordService.saveBatch(readingQuestionList);
            essayRecordService.saveBatch(essayQuestionList);
            if (score.get()>studentCourse.getScore()){
                studentCourse.setScore(score.get());
                studentCourseService.updateById(studentCourse);
            }
            redisUtils.set(mark,score.get()+"",5*60);
            examUtils.deleteExamCoursesVoHotCache(studentId);
        });
        studentCourseService.updateById(studentCourse);
        redisUtils.set(mark,null,5*60);
        examUtils.deleteExamCoursesVoHotCache(studentId);
        R r = new R();
        r.setData(mark);
        r.setCode(100);
        return  r;
    }

    @RequestMapping("/upload")
    @ResponseBody
    public R upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return R.fail;
        }
        int successfulCount=0;
        String fileName = file.getOriginalFilename();
        if (fileName.endsWith(".xls")){
            List<Question> questionList = new ArrayList<>();
            InputStream inputStream = null;
            try {
                inputStream = file.getInputStream();
                Workbook workbook = Workbook.getWorkbook(inputStream);
                int sheet_size = workbook.getNumberOfSheets();
                for (int index=0;index<sheet_size-1;index++){
                    Sheet sheet = workbook.getSheet(index);
                    int columns = sheet.getColumns();
                    for (int row=0;row<sheet.getRows();row++){
                        Question question = new Question();
                        if (sheet.getCell(0,row)==null||sheet.getCell(0,row).getContents()==null||sheet.getCell(0,row).getContents().equals(""))
                            continue;
                        int pageId = Integer.parseInt(sheet.getCell(0,row).getContents());
                        int isSingle = Integer.parseInt(sheet.getCell(1,row).getContents());
                        String title = sheet.getCell(2,row).getContents();
                        int score = Integer.parseInt(sheet.getCell(3,row).getContents());
                        String answer = sheet.getCell(4,row).getContents();
                        String choice1 = sheet.getCell(5,row).getContents();
                        String choice2 = sheet.getCell(6,row).getContents();
                        if (columns>7){
                            String choice3 = sheet.getCell(7,row).getContents();
                            question.setQuestionChoice3(choice3);
                            if (columns>8){
                                String choice4 = sheet.getCell(8,row).getContents();
                                question.setQuestionChoice4(choice4);
                                if (columns>9){
                                    String choice5 = sheet.getCell(9,row).getContents();
                                    question.setQuestionChoice5(choice5);
                                    if (columns>10){
                                        String choice6 = sheet.getCell(10,row).getContents();
                                        question.setQuestionChoice6(choice6);
                                    }
                                }
                            }
                        }

                        question.setPageId(pageId);
                        question.setIsSingle(isSingle);
                        question.setQuestionTitle(title);
                        question.setQuestionScore(score);
                        question.setQuestionAnswer(answer);
                        question.setQuestionChoice1(choice1);
                        question.setQuestionChoice2(choice2);

                        questionList.add(question);
                    }
                }
            } catch (IOException | BiffException e) {
                e.printStackTrace();
                return R.fail;
            }finally {
                if (inputStream!=null){
                    inputStream.close();
                }
            }

            for (Question question : questionList){
                R r=null;
                try {
                    r = addOneQuestion(question);
                }catch (Exception e){
                    continue;
                }
                if (r!=null&&r.code==100){
                    successfulCount+=1;
                }
            }
            R r = new R();
            r.setCode(100);
            r.setM("成功了"+successfulCount+"条数据");
            return r;

        }else {
            return R.fail;
        }
    }

    @RequestMapping("/questionUpload")
    public String questionUpload(Model model){
        model.addAttribute("pageList",pageService.list());
        return "pages/upload/questionUpload";
    }

    @RequestMapping("/getScoreByMark")
    @ResponseBody
    public R getScoreByMark(String mark){
        String score = (String) redisUtils.get(mark);
        if (score==null){
            return R.fail;
        }
        redisUtils.del(mark);
        if (Integer.parseInt(score)<60){
            return new R(100,"您的客观题得分为"+score+"，请继续加油！");
        }else {
            return new R(100,"恭喜您，客观题得分为"+score+"，请再接再厉！");
        }
    }
}

