package com.example.todolist;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.resources.TextAppearance;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.List;

public class TaskListFragment extends Fragment {
    private TaskAdapter adapter;
    private RecyclerView recyclerView;
    private boolean subtitleVisible;
    public static String KEY_EXTRA_TASK_ID = "KEY_EXTRA_TASK_ID";
    public static String SUBTITLE_VISIBLE_KEY = "SUBTITLE_VISIBLE_KEY";
    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_task_menu, menu);
        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if(subtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);
        }
        else{
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        recyclerView = view.findViewById(R.id.task_recycler_view);
        if(savedInstanceState != null){
            subtitleVisible = savedInstanceState.getBoolean(SUBTITLE_VISIBLE_KEY);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateView();
        return view;
    }

    private void updateView(){
        TaskStorage taskStorage = TaskStorage.getInstance();
        List<Task> tasks = taskStorage.getTasks();
        
        if(adapter == null){
            adapter = new TaskAdapter(tasks);
            recyclerView.setAdapter(adapter);
        }else{
            adapter.notifyDataSetChanged();
        }

        updateSubtitle();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(SUBTITLE_VISIBLE_KEY, subtitleVisible);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.new_task:
                Task task = new Task();
                TaskStorage.getInstance().addTask(task);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra(TaskListFragment.KEY_EXTRA_TASK_ID, task.Id());
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                subtitleVisible = !subtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateSubtitle(){
        TaskStorage taskStorage = TaskStorage.getInstance();
        List<Task> tasks = taskStorage.getTasks();
        int taskTodoCount = 0;
        for(Task task : tasks){
            if(!task.isDone()){
                taskTodoCount++;
            }
        }

        String subtitle = getString(R.string.subtitle_format, taskTodoCount);
        if(!subtitleVisible){
            subtitle = null;
        }
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.getSupportActionBar().setSubtitle(subtitle);
    }

    private class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameTextView;
        TextView dateTextView;
        ImageView categoryImageView;
        CheckBox doneCheckbox;
        Task task;
        public TaskHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_task, parent, false));
            itemView.setOnClickListener(this);

            nameTextView = itemView.findViewById(R.id.task_item_name);
            dateTextView = itemView.findViewById(R.id.task_item_date);
            categoryImageView = itemView.findViewById(R.id.imageView);
            doneCheckbox = itemView.findViewById(R.id.doneCheckbox);


        }

        public void bind(Task task){
            this.task = task;
            nameTextView.setSingleLine(true);
            nameTextView.setText(task.getName());
            dateTextView.setText(task.getDate().toString());
            if(task.getCategory().equals(Category.Home)){
                categoryImageView.setImageResource(R.drawable.ic_house);
            }
            else{
                categoryImageView.setImageResource(R.drawable.ic_study);
            }

            doneCheckbox.setChecked(task.isDone());

            if(doneCheckbox.isChecked()){
                if(!nameTextView.getPaint().isStrikeThruText()){
                    nameTextView.setPaintFlags(nameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
            }
            else{
                nameTextView.setPaintFlags(nameTextView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra(KEY_EXTRA_TASK_ID, task.Id());
            startActivity(intent);
        }

        public CheckBox getCheckBox(){
            return this.doneCheckbox;
        }

        public TextView getNameTextView(){
            return  this.nameTextView;
        }
    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {

        private List<Task> tasks;

        public TaskAdapter(List<Task> tasks){
            this.tasks = tasks;
        }

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new TaskHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
            Task task = tasks.get(position);
            holder.bind(task);
            CheckBox checkBox = holder.getCheckBox();
            TextView nameTextView =  holder.getNameTextView();
            checkBox.setChecked(tasks.get(position).isDone());
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                tasks.get(holder.getBindingAdapterPosition()).setDone(isChecked);
                if(isChecked){
                    if(!nameTextView.getPaint().isStrikeThruText()){
                        nameTextView.setPaintFlags(nameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    }
                }
                else{
                    nameTextView.setPaintFlags(nameTextView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                }
            });

            
        }

        @Override
        public int getItemCount() {
            return tasks.size();
        }
    }
}
