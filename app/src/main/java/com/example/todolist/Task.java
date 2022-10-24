package com.example.todolist;

import java.util.Date;
import java.util.UUID;

public class Task {
    private UUID id;
    private String name;
    private Date date;
    private boolean done;

    public Task(){
        id = UUID.randomUUID();
        date = new Date();
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

    public boolean isDone(){
        return this.done;
    }
    public void setDone(boolean done){
        this.done = done;
    }

    public UUID Id(){
        return this.id;
    }

}
