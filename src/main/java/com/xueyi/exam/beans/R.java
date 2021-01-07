package com.xueyi.exam.beans;

public class R {
    public static R success;
    public static R fail;
    static {
        success = new R();
        fail = new R();
        success.setCode(100);
        success.setM("成功");
        fail.setCode(500);
        fail.setM("失败");
    }
    public int code;
    public String m;
    public Object data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public R(int code, String m) {
        this.code = code;
        this.m = m;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public R() {
    }
}
