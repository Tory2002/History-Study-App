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

import android.os.Bundle;

public class checking_dates extends AppCompatActivity {

    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;

    // список для хранения всей информации из БД
    ArrayList<StruktDate> dates = new ArrayList<StruktDate>();

    // номер текущей даты
    int numDate = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checking_dates);

        mDBHelper = new DatabaseHelper(this);

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (
                SQLException mSQLException) {
            throw mSQLException;
        }

        CreateDict();
        numDate = RandomNextNumDate();
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
    //функция загрузки очередного события и дат ответов (среди трех только одна верная)
    public void NextEvent(){

        TextView eventCheck = (TextView)findViewById(R.id.eventCheck);
        if (dates.size() <= 3){
            eventCheck.setText("Мало изученных дат!\nПерейдите в режим изучения дат!!!");
        }
        else {
            eventCheck.setText(dates.get(numDate).getEvent());
            String date1 = dates.get(numDate).getDate();
            String date2;
            String date3;
            Random random = new Random();
            int temp = random.nextInt() % 3;
            if (temp == 0 && numDate > 1){
                date2 = dates.get(numDate-2).getDate();
                date3 = dates.get(numDate-1).getDate();
            }
            else if(temp == 1 && numDate > 0 && numDate < dates.size() - 1){
                date2 = dates.get(numDate-1).getDate();
                date3 = dates.get(numDate+1).getDate();
            }
            else if (temp == 2 &&  numDate < dates.size() - 2){
                date2 = dates.get(numDate+1).getDate();
                date3 = dates.get(numDate+2).getDate();
            }
            else{
                date2 = dates.get(RandomNextNumDate()).getDate();
                date3 = dates.get(RandomNextNumDate()).getDate();
            }

            Button btnDate1 = (Button)findViewById(R.id.btnDate);
            Button btnDate2 = (Button)findViewById(R.id.btnDate1);
            Button btnDate3 = (Button)findViewById(R.id.btnDate2);
            temp = random.nextInt() % 3;
            if(temp == 0){
                btnDate1.setText(date1);
                btnDate2.setText(date2);
                btnDate3.setText(date3);
            }
            else if(temp == 1){
                btnDate1.setText(date2);
                btnDate2.setText(date1);
                btnDate3.setText(date3);
            }
            else {
                btnDate1.setText(date2);
                btnDate2.setText(date3);
                btnDate3.setText(date1);
            }
        }
    }
    // функция выбора следующей даты случайным образом
    public int RandomNextNumDate(){
        Random rand = new  Random();
        if (dates.size() != 0)
            return  rand.nextInt(dates.size());
        else
            return 0;
    }
    // обработчик события (клик) по нажатию по любой из трех кнопок с датой
    public void onClickBtnDates(View view){
        Button tempBtn = (Button)view;
        String answer = tempBtn.getText().toString();
        if (answer.equals(dates.get(numDate).getDate())){
            Toast.makeText(this, "Ответ верный", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Ответ не верный", Toast.LENGTH_SHORT).show();
            mDb.delete("DatesLearned", "id_date = ?", new String[]{String.valueOf(dates.get(numDate).getId())});
            dates.remove(numDate);
        }
        numDate = RandomNextNumDate();
        NextEvent();


    }
}