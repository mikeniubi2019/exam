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
 * @since 2020-12-05
 */
public class Profession extends Model<Profession> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String professionName;

    private String professionDescription;

    private String professionImg;

    private Integer studentCout;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProfessionName() {
        return professionName;
    }

    public void setProfessionName(String professionName) {
        this.professionName = professionName;
    }

    public String getProfessionDescription() {
        return professionDescription;
    }

    public void setProfessionDescription(String professionDescription) {
        this.professionDescription = professionDescription;
    }

    public String getProfessionImg() {
        return professionImg;
    }

    public void setProfessionImg(String professionImg) {
        this.professionImg = professionImg;
    }

    public Integer getStudentCout() {
        return studentCout;
    }

    public void setStudentCout(Integer studentCout) {
        this.studentCout = studentCout;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Profession{" +
        "id=" + id +
        ", professionName=" + professionName +
        ", professionDescription=" + professionDescription +
        ", professionImg=" + professionImg +
        ", studentCout=" + studentCout +
        "}";
    }
}
