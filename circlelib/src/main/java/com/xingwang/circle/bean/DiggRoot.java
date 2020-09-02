package com.xingwang.circle.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DiggRoot implements Serializable {
    private int total;
    private List<Digg> items;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Digg> getItems() {
        return items;
    }

    public void setItems(List<Digg> items) {
        this.items = items;
    }
}
