package com.example.demo.crawler.model;
import java.util.List;
public class AstronomyMonth {
    private int month;

    private String monthTitle;
    private List<String> title;
    private List<String> content ;
    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getMonthTitle() {
        return monthTitle;
    }

    public void setMonthTitle(String monthTitle) {
        this.monthTitle = monthTitle;
    }

    public List<String> getTitle() {
        return title;
    }

    public void setTitle(List<String> title) {
        this.title = title;
    }

    public List<String> getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "AstronomyMonth{" +
                "month=" + month +
                ", monthTitle='" + monthTitle + '\'' +
                ", title=" + title +
                ", content=" + content +
                '}';
    }

    public void setContent(List<String> content) {
        this.content = content;
    }





}
