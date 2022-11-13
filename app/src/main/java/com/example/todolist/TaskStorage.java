package com.example.todolist;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskStorage {
    private static final TaskStorage taskStorage = new TaskStorage();

    private final List<Task> tasks;
    public static TaskStorage getInstance() {return taskStorage;}

    private TaskStorage(){
        tasks = new ArrayList<>();
        for(int i = 1; i <= 150; i++){
            Task task = new Task();
            task.setName("Pilne zadanie numer: " + i);
            task.setDone(i % 3 == 0);
            if(i % 3 == 0){
                task.setCategory(Category.Studies);
            }
            else{
                task.setCategory(Category.Home);
            }
            tasks.add(task);
        }
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public Task getTask(UUID id){
        for(int i = 0; i < tasks.size(); i++){
            Task tmp = tasks.get(i);
            if(tmp.Id().equals(id))
            {
                return tmp;
            }
        }

        return null;
    }

    public void addTask(Task task){
        tasks.add(task);
    }
}