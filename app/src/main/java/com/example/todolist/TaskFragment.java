package com.example.todolist;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.UUID;

public class TaskFragment extends Fragment {

    private Task task;
    private EditText nameField;

    public static final String ARG_TASK_ID = "ARG_TASK_ID";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID taskId = (UUID) getArguments()
                .getSerializable(ARG_TASK_ID);
        task = TaskStorage.getInstance().getTask(taskId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_task,container, false);
        if(container != null){


            nameField = view.findViewById(R.id.task_name);
            if(nameField != null){

                nameField.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        task.setName(charSequence.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }

            Button dateButton = (Button) view.findViewById(R.id.button);
            if(dateButton != null) {
                dateButton.setText(task.getDate().toString());
                dateButton.setEnabled(false);
            }

            CheckBox doneCheckBox = (CheckBox) view.findViewById(R.id.task_done);
            if(doneCheckBox != null) {
                doneCheckBox.setChecked(task.isDone());
                doneCheckBox.setOnCheckedChangeListener((buttonView, isChecked) ->  task.setDone(isChecked));
            }
        }

        return view;
    }

    public static TaskFragment newInstance (UUID taskId){
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_TASK_ID, taskId);
        TaskFragment taskFragment = new TaskFragment();
        taskFragment.setArguments(bundle);
        return taskFragment;
    }
}
