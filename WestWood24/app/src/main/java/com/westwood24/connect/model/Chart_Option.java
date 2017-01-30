package com.westwood24.connect.model;


import java.io.Serializable;

/**
 * Created by aipxperts on 15/9/16.
 */
public class Chart_Option implements  Serializable{

    String option_title;
    String option_value;

    public String getOption_title() {
        return option_title;
    }

    public void setOption_title(String option_title) {
        this.option_title = option_title;
    }

    public String getOption_value() {
        return option_value;
    }

    public void setOption_value(String option_value) {
        this.option_value = option_value;
    }
}
