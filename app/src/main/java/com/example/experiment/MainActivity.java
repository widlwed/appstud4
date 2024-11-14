package com.example.experiment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private MyAsyncTask myAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);


        if (!NetworkUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, "Запуск в автономном режиме", Toast.LENGTH_SHORT).show();
        } else {
            myAsyncTask = new MyAsyncTask(dbHelper);
            myAsyncTask.execute("http://media.ifmo.ru/api_get_current_song.php");
        }

        Button btnViewSongs = findViewById(R.id.btnViewSongs);
        btnViewSongs.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SongsActivity.class);
            startActivity(intent);
        });
    }
}
