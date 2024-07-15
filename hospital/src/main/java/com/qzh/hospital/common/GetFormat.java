package com.qzh.hospital.common;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

public class GetFormat {

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    String flag;
    LocalDate date;
    LocalTime time;
    Map<String,String> data;

    private static int PAGE_SIZE = 20;
    private static int PAGE_NUM = 1;

    public int getPage_Size() {
        return page_Size;
    }

    public void setPage_Size(int page_Size) {
        this.page_Size = page_Size;
    }

    public int getPage_Num() {
        return page_Num;
    }

    public void setPage_Num(int page_Num) {
        this.page_Num = page_Num;
    }

    private int page_Size=PAGE_SIZE;
    private int page_Num = PAGE_NUM;
}
