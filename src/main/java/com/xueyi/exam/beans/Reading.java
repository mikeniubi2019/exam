package com.xueyi.exam.beans;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author mike
 * @since 2020-12-11
 */
public class Reading extends Model<Reading> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String title;

    private Integer pageId;

    @TableField(exist = false)
    private List<ReadingQuestion> readingQuestionList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPageId() {
        return pageId;
    }

    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }

    public List<ReadingQuestion> getReadingQuestionList() {
        return readingQuestionList;
    }

    public void setReadingQuestionList(List<ReadingQuestion> readingQuestionList) {
        this.readingQuestionList = readingQuestionList;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Reading{" +
        "id=" + id +
        ", title=" + title +
        ", score=" +
        ", pageId=" + pageId +
        "}";
    }
}
