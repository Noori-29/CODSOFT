

package com.example.todolist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Task {

    private String title;
    private String description;
    private boolean completed;

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.completed = false;
    }

    public static ArrayList<Task> fromJsonArray(String tasksJson) {
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

    public static JSONArray toJsonArray(ArrayList<Task> taskList) {
        JSONArray jsonArray = new JSONArray();
        for (Task task : taskList) {
            try {
                JSONObject jsonTask = new JSONObject();
                jsonTask.put("title", task.getTitle());
                jsonTask.put("description", task.getDescription());
                jsonTask.put("completed", task.isCompleted());
                jsonArray.put(jsonTask);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonArray;
    }
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return title;
    }

    public void setTitle(String title) {
    }

    public void setDescription(String description) {
    }
}
