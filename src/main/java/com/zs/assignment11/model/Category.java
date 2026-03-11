package com.zs.assignment11.model;

public class Category {
    private Integer id;
    private String name;

    public Category(Integer id,String name){
        this.id=id;
        this.name=name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Category{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}
