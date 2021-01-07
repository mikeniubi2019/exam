package com.xueyi.exam.beans;

public class LaiuiPage<T> {
    private int page;
    private int limit;
    private T data;

    private int code;
    private int count;
    private String msg;

    public static <T>LaiuiPage creatDataPage(T t,int count){
        LaiuiPage laiuiPage = new LaiuiPage();
        laiuiPage.code=0;
        laiuiPage.count=count;
        laiuiPage.msg="成功";
        laiuiPage.data=t;
        return laiuiPage;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
