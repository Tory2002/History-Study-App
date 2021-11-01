package com.example.data;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class view_dates_learn extends AppCompatActivity {

    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;

    // список для хранения всей информации из БД
    ArrayList<StruktDate> dates = new ArrayList<StruktDate>();

    // номер текущей даты
    int numDate = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_dates_learn);

        mDBHelper = new DatabaseHelper(this);

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (
                SQLException mSQLException) {
            throw mSQLException;
        }

        CreateDict();
        NextEvent();
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
    public void CreateDict(){
        Cursor cursor = mDb.rawQuery("SELECT * FROM DataBank", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            dates.add(new StruktDate(cursor.getInt(0),cursor.getString(1), cursor.getString(2)));
            cursor.moveToNext();
        }
        cursor.close();

        ArrayList<Integer> id_date = new ArrayList<Integer>();
        cursor = mDb.rawQuery("SELECT * FROM DatesLearned", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            id_date.add(cursor.getInt(1));
            cursor.moveToNext();
        }
        cursor.close();
        for(int i = 0; dates.size()>0 && i < dates.size();){
            int tempId = dates.get(i).getId();
            if (id_date.indexOf(tempId) == -1){
                dates.remove(i);
            }
            else{
                i++;
            }
        }
    }
    //функция загрузки очередного события и даты
    public void NextEvent(){

        TextView dateLearn = (TextView)findViewById(R.id.date_learn);
        TextView eventLearn = (TextView)findViewById(R.id.event_learn);

        dateLearn.setText(dates.get(numDate).getDate());
        eventLearn.setText(dates.get(numDate).getEvent());

    }

    // обработчик событий (клик) по кнопке next
    public void onClickBtnNext(View view){
        numDate = (numDate+1) % dates.size();
        NextEvent();
    }
    // обработчик событий (клик) по кнопке back
    public void onClickBtnBack(View view){
        if (numDate > 0) {
            numDate--;
        }
        else{
            numDate = dates.size() - 1;
        }
        NextEvent();
    }
}