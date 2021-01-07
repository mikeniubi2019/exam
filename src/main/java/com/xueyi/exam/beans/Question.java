package com.xueyi.exam.beans;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author mike
 * @since 2020-12-03
 */
public class Question extends Model<Question> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer pageId;

    private Integer isSingle;

    private String questionTitle;

    private String questionChoice1;

    private String questionChoice2;

    private String questionChoice3;

    private String questionChoice4;

    private String questionChoice5;

    private String questionChoice6;

    private String questionAnswer;

    private Integer questionScore;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPageId() {
        return pageId;
    }

    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }

    public Integer getIsSingle() {
        return isSingle;
    }

    public void setIsSingle(Integer isSingle) {
        this.isSingle = isSingle;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getQuestionChoice1() {
        return questionChoice1;
    }

    public void setQuestionChoice1(String questionChoice1) {
        this.questionChoice1 = questionChoice1;
    }

    public String getQuestionChoice2() {
        return questionChoice2;
    }

    public void setQuestionChoice2(String questionChoice2) {
        this.questionChoice2 = questionChoice2;
    }

    public String getQuestionChoice3() {
        return questionChoice3;
    }

    public void setQuestionChoice3(String questionChoice3) {
        this.questionChoice3 = questionChoice3;
    }

    public String getQuestionChoice4() {
        return questionChoice4;
    }

    public void setQuestionChoice4(String questionChoice4) {
        this.questionChoice4 = questionChoice4;
    }

    public String getQuestionChoice5() {
        return questionChoice5;
    }

    public void setQuestionChoice5(String questionChoice5) {
        this.questionChoice5 = questionChoice5;
    }

    public String getQuestionChoice6() {
        return questionChoice6;
    }

    public void setQuestionChoice6(String questionChoice6) {
        this.questionChoice6 = questionChoice6;
    }

    public String getQuestionAnswer() {
        return questionAnswer;
    }

    public void setQuestionAnswer(String questionAnswer) {
        this.questionAnswer = questionAnswer;
    }

    public Integer getQuestionScore() {
        return questionScore;
    }

    public void setQuestionScore(Integer questionScore) {
        this.questionScore = questionScore;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Question{" +
        "id=" + id +
        ", pageId=" + pageId +
        ", isSingle=" + isSingle +
        ", questionTitle=" + questionTitle +
        ", questionChoice1=" + questionChoice1 +
        ", questionChoice2=" + questionChoice2 +
        ", questionChoice3=" + questionChoice3 +
        ", questionChoice4=" + questionChoice4 +
        ", questionChoice5=" + questionChoice5 +
        ", questionChoice6=" + questionChoice6 +
        ", questionAnswer=" + questionAnswer +
        ", questionScore=" + questionScore +
        "}";
    }
}
