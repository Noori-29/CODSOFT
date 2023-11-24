package com.example.todolist;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.todolist.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SavedTasksActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "TaskPrefs";
    private static final String TASKS_KEY = "tasks";

    private ArrayList<Task> savedTaskList;
    private ArrayAdapter<Task> savedTaskAdapter;
    private ListView savedTaskListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_tasks);

        savedTaskList = loadSavedTasks();
        savedTaskAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, savedTaskList);

        savedTaskListView = findViewById(R.id.savedTaskListView);
        savedTaskListView.setAdapter(savedTaskAdapter);

        // Set item click listener for task editing or deleting
        savedTaskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editOrDeleteTask(position);
            }
        });
    }

    private ArrayList<Task> loadSavedTasks() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String tasksJson = prefs.getString(TASKS_KEY, null);

        if (tasksJson != null) {
            return fromJsonArray(tasksJson);
        }

        return new ArrayList<>();
    }

    private void editOrDeleteTask(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an action")
                .setItems(new CharSequence[]{"Edit", "Delete"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                editTask(position);
                                break;
                            case 1:
                                confirmDeleteTask(position);
                                break;
                        }
                    }
                });
        builder.create().show();
    }

    private void editTask(final int position) {
        Task taskToEdit = savedTaskList.get(position);

        Intent intent = new Intent(this, TaskEditActivity.class);
        intent.putExtra(TaskEditActivity.EXTRA_POSITION, position);
        intent.putExtra(TaskEditActivity.EXTRA_TITLE, taskToEdit.getTitle());
        intent.putExtra(TaskEditActivity.EXTRA_DESCRIPTION, taskToEdit.getDescription());

        startActivityForResult(intent, 1);
    }

    private void confirmDeleteTask(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteTask(position);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void deleteTask(int position) {
        savedTaskList.remove(position);
        savedTaskAdapter.notifyDataSetChanged();
        saveTasks();
    }

    // Save and load tasks methods...

    private void saveTasks() {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(TASKS_KEY, toJsonArray(savedTaskList));
        editor.apply();
    }

    private String toJsonArray(ArrayList<Task> taskList) {
        JSONArray jsonArray = new JSONArray();
        for (Task task : taskList) {
            JSONObject jsonTask = new JSONObject();
            try {
                jsonTask.put("title", task.getTitle());
                jsonTask.put("description", task.getDescription());
                jsonTask.put("completed", task.isCompleted());
                jsonArray.put(jsonTask);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonArray.toString();
    }

    private ArrayList<Task> fromJsonArray(String tasksJson) {
        ArrayList<Task> taskList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(tasksJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonTask = jsonArray.getJSONObject(i);
                String title = jsonTask.getString("title");
                String description = jsonTask.getString("description");
                boolean completed = jsonTask.getBoolean("completed");
                Task task = new Task(title, description);
                task.setCompleted(completed);
                taskList.add(task);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return taskList;
    }
}
