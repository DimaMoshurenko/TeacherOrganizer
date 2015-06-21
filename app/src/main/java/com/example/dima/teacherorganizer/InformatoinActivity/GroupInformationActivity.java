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
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.example.dima.teacherorganizer.Activity.LoginActivity;
import com.example.dima.teacherorganizer.DataBase.TeacherDataBase;
import com.example.dima.teacherorganizer.R;

import java.util.ArrayList;

public class GroupInformationActivity extends ActionBarActivity {

    public static final String ID_GROUP = " group ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_information);

//        setTitle(getResources().getString(R.string.other_information));
        Log.e("TAG", getIntent().getStringExtra(ID_GROUP));
        SQLiteDatabase database = new TeacherDataBase(this).getWritableDatabase();
        TextView nameKuratora = (TextView) findViewById(R.id.name_kuratora_information);
        final TextView phoneKuratora = (TextView) findViewById(R.id.phone_number_kurator);
        TextView nameGroup = (TextView) findViewById(R.id.name_group_information);
        TextView numberStudent = (TextView) findViewById(R.id.number_student_information);

        String tableGroups = TeacherDataBase.GroupsTable.TABLE_NAME;
        String[] fieldsGroups = {TeacherDataBase.GroupsTable.GROUP_, TeacherDataBase.GroupsTable.ID,
                TeacherDataBase.GroupsTable.NAME_KURATOTA, TeacherDataBase.GroupsTable.NUMBER_KURATOTA,
                TeacherDataBase.GroupsTable.ID_TEACHER};

        String whereGroups = TeacherDataBase.GroupsTable.ID + " = " + getIntent().getStringExtra(ID_GROUP) + " and " +
                TeacherDataBase.GroupsTable.ID_TEACHER + " = " + LoginActivity.getIdTeacher() + "  ";


        Cursor cursor = database.query(tableGroups, fieldsGroups, whereGroups, null, null, null, null);

        Log.e("TAG", String.valueOf(cursor.moveToFirst()));
        if (cursor.moveToFirst()) {
            do {
                nameKuratora.setText(cursor.getString(cursor.getColumnIndex(TeacherDataBase.GroupsTable.NAME_KURATOTA)));
                phoneKuratora.setText(cursor.getString(cursor.getColumnIndex(TeacherDataBase.GroupsTable.NUMBER_KURATOTA)));
                nameGroup.setText(cursor.getString(cursor.getColumnIndex(TeacherDataBase.GroupsTable.GROUP_)));
                Cursor numberStudentCursor = database.query(TeacherDataBase.StudentTable.TABLE_NAME,
                        new String[]{TeacherDataBase.StudentTable._ID_GROUP, TeacherDataBase.StudentTable._ID_TEACHER,
                                TeacherDataBase.StudentTable.STUDENT_NAME, TeacherDataBase.StudentTable.ID},
                        TeacherDataBase.StudentTable._ID_TEACHER + " =  " + LoginActivity.getIdTeacher() + " and " +
                                TeacherDataBase.StudentTable._ID_GROUP + "  = " + getIntent().getStringExtra(ID_GROUP), null, null, null, null, null);
                int number = 0;
                if (numberStudentCursor.moveToFirst()) {
                    do {
                        number++;
                    } while (numberStudentCursor.moveToNext());
                }

                numberStudent.setText(String.valueOf(number));
            } while (cursor.moveToNext());
        }
        phoneKuratora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = "tel:" + phoneKuratora.getText().toString().trim();
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
                startActivity(callIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_group_information, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
