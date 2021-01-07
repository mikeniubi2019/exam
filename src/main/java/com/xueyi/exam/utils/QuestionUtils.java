package com.xueyi.exam.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xueyi.exam.beans.EssayQuestion;
import com.xueyi.exam.beans.Exam;
import com.xueyi.exam.beans.Question;
import com.xueyi.exam.beans.Reading;
import com.xueyi.exam.service.EssayQuestionService;
import com.xueyi.exam.service.ExamService;
import com.xueyi.exam.service.QuestionService;
import com.xueyi.exam.service.ReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuestionUtils {
    private final String QUESTION="question_" ;
    private final String READING="reading_" ;
    private final String ESSAY="essay_" ;
    private final String LIST = "list_";
    private final int HOUER = 60*60;

    @Autowired
    RedisUtils redisUtils;
    @Autowired
    QuestionService questionService;
    @Autowired
    ReadingService readingService;
    @Autowired
    EssayQuestionService essayQuestionService;

    public List<Question> getQuestionListByPageId(int pageId){
        List<Question> questionList = (List<Question>)redisUtils.get(QUESTION+LIST+pageId);
        if (questionList==null){
            questionList = questionService.list(new QueryWrapper<Question>().lambda().eq(Question::getPageId,pageId));
            redisUtils.set(QUESTION+LIST+pageId,questionList,HOUER);
        }
        return questionList;
    }

    public List<Reading> getReadingListByPageId(int pageId){
        List<Reading> readingList = (List<Reading>)redisUtils.get(READING+LIST+pageId);
        if (readingList==null){
            readingList = readingService.getReadingsByPageId(pageId);;
            redisUtils.set(READING+LIST+pageId,readingList,HOUER);
        }
        return readingList;
    }

    public List<EssayQuestion> getEssayQuestionListByPageId(int pageId){
        List<EssayQuestion> essayQuestionList = (List<EssayQuestion>)redisUtils.get(ESSAY+LIST+pageId);
        if (essayQuestionList==null){
            essayQuestionList = essayQuestionService.list(new QueryWrapper<EssayQuestion>().lambda().eq(EssayQuestion::getPageId,pageId));
            redisUtils.set(ESSAY+LIST+pageId,essayQuestionList,HOUER);
        }
        return essayQuestionList;
    }

    public void deleteEssayQuestionListByPageId(int pageId){
        redisUtils.del(ESSAY+LIST+pageId);
    }

    public void deleteReadingListByPageId(int pageId){
        redisUtils.del(READING+LIST+pageId);
    }

    public void deleteQuestionListByPageId(int pageId){
        redisUtils.del(QUESTION+LIST+pageId);
    }
}
