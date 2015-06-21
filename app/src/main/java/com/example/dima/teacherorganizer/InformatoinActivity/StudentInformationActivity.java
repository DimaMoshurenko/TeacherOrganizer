package com.example.dima.teacherorganizer.InformatoinActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.dima.teacherorganizer.DataBase.TeacherDataBase;
import com.example.dima.teacherorganizer.R;

public class StudentInformationActivity extends ActionBarActivity {


    public static final String ID_STUDENT = "id student";
    public static  String idStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_information);
//        setTitle(getResources().getString(R.string.other_information));
        idStudent = getIntent().getStringExtra(ID_STUDENT);
        Log.e("TAG", "intent " + idStudent);
        SQLiteDatabase database = new TeacherDataBase(this).getReadableDatabase();

        Cursor cursor = database.query(TeacherDataBase.StudentTable.TABLE_NAME,
                new String[]{TeacherDataBase.StudentTable.STUDENT_NAME, TeacherDataBase.StudentTable.STUDENT_MAIL,
                        TeacherDataBase.StudentTable.TELEPHONE_NUMBER, TeacherDataBase.StudentTable._ID_GROUP, TeacherDataBase.StudentTable.ID},
                TeacherDataBase.StudentTable.ID + " = ? ", new String[]{idStudent},
                null, null, null, null);
        TextView email = (TextView) findViewById(R.id.information_student_email);
        TextView name = (TextView) findViewById(R.id.information_student_name);
        TextView group = (TextView) findViewById(R.id.information_student_group);
        final TextView phone = (TextView) findViewById(R.id.information_student_phone);
        if (cursor.moveToFirst()) {
            do {
                email.setText(cursor.getString(cursor.getColumnIndex(TeacherDataBase.StudentTable.STUDENT_MAIL)));
                name.setText(cursor.getString(cursor.getColumnIndex(TeacherDataBase.StudentTable.STUDENT_NAME)));
                phone.setText(cursor.getString(cursor.getColumnIndex(TeacherDataBase.StudentTable.TELEPHONE_NUMBER)));
                Cursor groupInformation = database.query(TeacherDataBase.GroupsTable.TABLE_NAME,
                        new String[]{TeacherDataBase.GroupsTable.ID,TeacherDataBase.GroupsTable.GROUP_},
                        TeacherDataBase.GroupsTable.ID + " = ? ", new String[]{cursor.getString(cursor.getColumnIndex(TeacherDataBase.StudentTable._ID_GROUP))},
                        null, null, null, null);
                if(groupInformation.moveToFirst()){
                    group.setText(groupInformation.getString(groupInformation.getColumnIndex(TeacherDataBase.GroupsTable.GROUP_)));
                }

            } while (cursor.moveToNext());
        }
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = "tel:" + phone.getText().toString().trim();
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
                startActivity(callIntent);

            }
        });
           }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_student_information, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
