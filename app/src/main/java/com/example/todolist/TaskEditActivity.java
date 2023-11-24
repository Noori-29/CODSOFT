

package com.example.todolist;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class TaskEditActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "position";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_DESCRIPTION = "description";

    private EditText editTitle;
    private EditText editDescription;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);

        editTitle = findViewById(R.id.editTaskTitle);
        editDescription = findViewById(R.id.editTaskDescription);

        Intent intent = getIntent();
        position = intent.getIntExtra(EXTRA_POSITION, -1);
        String title = intent.getStringExtra(EXTRA_TITLE);
        String description = intent.getStringExtra(EXTRA_DESCRIPTION);

        editTitle.setText(title);
        editDescription.setText(description);
    }

    public void saveChanges(View view) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_POSITION, position);
        resultIntent.putExtra(EXTRA_TITLE, editTitle.getText().toString());
        resultIntent.putExtra(EXTRA_DESCRIPTION, editDescription.getText().toString());
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}

