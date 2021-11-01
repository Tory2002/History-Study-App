package com.example.data;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.database.SQLException;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDBHelper = new DatabaseHelper(this);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }
    }

    //переопределяем метод создания главного меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    // обработчик кнопак меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.action_dates_teach){
            onClickTeach(findViewById(R.id.action_dates_teach));
        }
        else if(id == R.id.action_dates_checking){
            onClickCheck(findViewById(R.id.action_dates_checking));
        }
        else if(id == R.id.action_dates_all){
            Intent intent = new Intent(this, view_dates_all.class);
            startActivity(intent);
        }
        else if(id == R.id.action_dates_learn){
            Intent intent = new Intent(this, view_dates_learn.class);
            startActivity(intent);
        }
        else if(id == R.id.action_developer){
            Intent intent = new Intent(this, developer.class);
            startActivity(intent);
        }
        return true;
    }
    // обработчик события (клилк) кнопки перехода в режим изучения новых дат
    public void onClickTeach(View view){
        Intent intent = new Intent(this, teach.class);
        startActivity(intent);
    }
    // обработчик события (клилк) кнопки перехода в режим плвторения дат
    public void onClickCheck(View view){
        Intent intent = new Intent(this, checking_dates.class);
        startActivity(intent);


    }
}