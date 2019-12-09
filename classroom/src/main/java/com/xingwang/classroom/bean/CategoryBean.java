package com.xingwang.classroom.bean;

import com.xingwang.classroom.http.CommonEntity;

import java.util.List;

/**
 * Date:2019/8/26
 * Time;9:48
 * author:baiguiqiang
 */
public class CategoryBean  extends CommonEntity {
    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
