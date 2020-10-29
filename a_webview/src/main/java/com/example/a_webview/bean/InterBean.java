package com.example.a_webview.bean;

public class InterBean {
    public Object object;
    public String tab;

    public InterBean(Object object, String tab) {
        this.object = object;
        this.tab = tab;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }
}
