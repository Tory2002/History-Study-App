package com.example.data;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class developer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);
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
            Intent intent = new Intent(this, teach.class);
            startActivity(intent);
        }
        else if(id == R.id.action_dates_checking){
            Intent intent = new Intent(this, checking_dates.class);
            startActivity(intent);
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
}