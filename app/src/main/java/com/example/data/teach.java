package com.example.data;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class teach extends AppCompatActivity {

    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;

    // список для хранения всей информации из БД
    ArrayList<StruktDate> dates = new ArrayList<StruktDate>();

    // номер текущей даты
    int numDate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teach);

        mDBHelper = new DatabaseHelper(this);

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }

        CreateDict();
        numDate = RandomNextNumDate();
        NextDate();
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
    // функция выбора следующей даты случайным образом
    public int RandomNextNumDate(){
        Random rand = new  Random();
        if (dates.size() != 0)
            return  rand.nextInt(dates.size());
        else
            return 0;
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
            if (id_date.indexOf(tempId) != -1){
                dates.remove(i);
            }
            else{
                i++;
            }
        }
    }

    //функция загрузки очередной даты и события
    public void NextDate(){
        TextView dateTeach = (TextView)findViewById(R.id.dateTeach);
        TextView eventTeach = (TextView)findViewById(R.id.eventTeach);
        if (dates.size() == 0){
            dateTeach.setText("Все даты выучены");
            eventTeach.setText("Перейдите в режим проверки знаний");
        }
        else {
            dateTeach.setText(dates.get(numDate).getDate());
            eventTeach.setText(dates.get(numDate).getEvent());
        }
    }

    // обработчик событий (клик) по кнопке знаю
    public void onClickBtnTrue(View view){
        if (dates.size() > 0) {
            ContentValues cv = new ContentValues();
            cv.put("id_date", dates.get(numDate).getId());
            mDb.insert("DatesLearned", "_id", cv);
            dates.remove(numDate);
        }
        numDate = RandomNextNumDate();
        NextDate();
        //numDate = (numDate+1) % dates.size();
    }
    // обработчик событий (клик) по кнопке знаю
    public void onClickBtnFalse(View view){
        if (dates.size() != 0) {
            numDate = RandomNextNumDate();
        }
        NextDate();
    }
}