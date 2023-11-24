package com.example.todolist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "TaskPrefs";
    private static final String TASKS_KEY = "tasks";

    private ArrayList<Task> taskList;
    private ArrayAdapter<Task> taskAdapter;
    private EditText taskTitleEditText;
    private EditText taskDescriptionEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskList = new ArrayList<>();
        taskAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, taskList);

        taskTitleEditText = findViewById(R.id.taskTitleEditText);
        taskDescriptionEditText = findViewById(R.id.taskDescriptionEditText);
    }

    public void addTask(View view) {
        String title = taskTitleEditText.getText().toString();
        String description = taskDescriptionEditText.getText().toString();

        if (!title.isEmpty()) {
            Task newTask = new Task(title, description);
            taskList.add(newTask);
            taskAdapter.notifyDataSetChanged();
            saveTasks();
            clearInputFields();
        } else {
            Toast.makeText(this, "Task title cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    public void viewSavedTasks(View view) {
        Intent intent = new Intent(this, SavedTasksActivity.class);
        startActivity(intent);
        }

    private void editTask(int position) {
        Task selectedTask = taskList.get(position);

        Intent intent = new Intent(this, TaskEditActivity.class);
        intent.putExtra(TaskEditActivity.EXTRA_POSITION, position);
        intent.putExtra(TaskEditActivity.EXTRA_TITLE, selectedTask.getTitle());
        intent.putExtra(TaskEditActivity.EXTRA_DESCRIPTION, selectedTask.getDescription());

        startActivityForResult(intent, 1);
    }

    private void deleteTask(int position) {
        taskList.remove(position);
        taskAdapter.notifyDataSetChanged();
        saveTasks();
    }


    private void clearInputFields() {
        taskTitleEditText.setText("");
        if (taskDescriptionEditText != null) {
            taskDescriptionEditText.setText("");
        }
    }


    private void saveTasks() {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(TASKS_KEY, Task.toJsonArray(taskList).toString());
        editor.apply();
    }

    private void loadTasks() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String tasksJson = prefs.getString(TASKS_KEY, null);

        if (tasksJson != null) {
            taskList = Task.fromJsonArray(tasksJson);
            taskAdapter.addAll(taskList);
            taskAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            int position = data.getIntExtra(TaskEditActivity.EXTRA_POSITION, -1);
            String title = data.getStringExtra(TaskEditActivity.EXTRA_TITLE);
            String description = data.getStringExtra(TaskEditActivity.EXTRA_DESCRIPTION);

            if (position != -1) {
                Task editedTask = taskList.get(position);
                editedTask.setTitle(title);
                editedTask.setDescription(description);
                taskAdapter.notifyDataSetChanged();
                saveTasks();
            }
        }
    }
}
