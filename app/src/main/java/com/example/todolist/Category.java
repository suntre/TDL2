package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

public enum Category {
    Studies(R.string.category_studies),
    Home(R.string.category_home);

    private int resourceId;

    private Category(int id){
        resourceId = id;
    }

    @Override
    public String toString(){
        return MyApplication.getAppContext().getString(resourceId);
    }
}
