package com.example.todolist;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class TaskFragment extends Fragment {

    private Task task;
    private EditText nameField;
    private EditText dateField;
    private Spinner categorySpinner;
    private final Calendar calendar = Calendar.getInstance();
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
                nameField.setText(task.getName());
            }

            dateField = (EditText) view.findViewById(R.id.task_date);
            DatePickerDialog.OnDateSetListener date = (view12, year, month, day) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                setupDateFieldValue(calendar.getTime());
                task.setDate(calendar.getTime());
            };

            dateField.setOnClickListener(view1 ->
                    new DatePickerDialog(getContext(), date, calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                            .show());
            setupDateFieldValue(task.getDate());

            CheckBox doneCheckBox = (CheckBox) view.findViewById(R.id.task_done);
            if(doneCheckBox != null) {
                doneCheckBox.setChecked(task.isDone());
                doneCheckBox.setOnCheckedChangeListener((buttonView, isChecked) ->  task.setDone(isChecked));
            }

            categorySpinner = view.findViewById(R.id.task_category);
            categorySpinner.setAdapter(new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, Category.values()));
            categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                    task.setCategory(Category.values()[position]);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            categorySpinner.setSelection(task.getCategory().ordinal());
        }

        return view;
    }

    private void setupDateFieldValue(Date date){
        Locale locale = new Locale("pl", "PL");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", locale);
        dateField.setText(dateFormat.format(date));
    }

    public static TaskFragment newInstance (UUID taskId){
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_TASK_ID, taskId);
        TaskFragment taskFragment = new TaskFragment();
        taskFragment.setArguments(bundle);
        return taskFragment;
    }
}
