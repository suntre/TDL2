package com.example.todolist;

import java.util.Date;
import java.util.UUID;

public class Task {
    private UUID id;
    private String name;
    private Date date;
    private boolean done;
    private Category Category;
    public Task(){
        id = UUID.randomUUID();
        date = new Date();
        Category = com.example.todolist.Category.Studies;
    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }

    public Date getDate(){
        return date;
    }
    public void setDate(Date date){this.date = date;}

    public boolean isDone(){
        return this.done;
    }
    public void setDone(boolean done){
        this.done = done;
    }

    public UUID Id(){
        return this.id;
    }

    public Category getCategory(){
        return this.Category;
    }
    public void setCategory(Category category){
        this.Category = category;
    }

}
