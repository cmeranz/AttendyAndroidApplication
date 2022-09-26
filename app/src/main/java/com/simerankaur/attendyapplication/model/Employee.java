package com.simerankaur.attendyapplication.model;

public class Employee {
    public Employee(String id,String name, String percentage) {
        this.name = name;
        this.id=id;
        this.percentage = percentage;
    }

    String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    String percentage;
}
