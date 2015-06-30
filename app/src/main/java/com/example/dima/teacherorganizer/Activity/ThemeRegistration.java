package com.example.dima.teacherorganizer.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.dima.teacherorganizer.DataBase.TeacherContentProvider;
import com.example.dima.teacherorganizer.DataBase.TeacherDataBase;
import com.example.dima.teacherorganizer.R;
import com.gc.materialdesign.views.ButtonFlat;
import com.rengwuxian.materialedittext.MaterialEditText;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.dima.teacherorganizer.Activity.TeacherRegistration.setSettingMaterialEditText;


public class ThemeRegistration extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_registration);


        final MaterialEditText newTheme = (MaterialEditText) findViewById(R.id.new_theme);
        ButtonFlat addSubject = (ButtonFlat) findViewById(R.id.add_theme);
        setSettingMaterialEditText(newTheme, getResources().getString(R.string.new_theme), this);
        addSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TeacherRegistration.NotEmptyValidator notEmptyValidator =
                        new TeacherRegistration.NotEmptyValidator(getString(R.string.not_empty_warning));
                newTheme.addValidator(notEmptyValidator);
                if (newTheme.validate()) {
                    String idGroup = getIntent().getStringExtra(TableActivity.ID_GROUP);
                    String idSubject = getIntent().getStringExtra(TableActivity.ID_SUBJECT);
                    if (idGroup != null & idSubject != null) {
//                        String numSubject = null;
                        Cursor groupCursor = getContentResolver().query(Uri.parse(TeacherContentProvider.CONTENT_URI+"/"+
                                TeacherDataBase.SubjectsTable.TABLE_NAME),new String[]{TeacherDataBase.SubjectsTable.ID,
                                TeacherDataBase.SubjectsTable.NUMBER_SUBJECT, TeacherDataBase.SubjectsTable.ID_TEACHER},
                        TeacherDataBase.SubjectsTable.ID_TEACHER + " = " + LoginActivity.getIdTeacher() + " and " +
                                TeacherDataBase.SubjectsTable.ID + " = " + idSubject + " ;",
                                null, null);

//                        if (groupCursor.moveToFirst()) {
//                            numSubject = groupCursor.getString(groupCursor.getColumnIndex(TeacherDataBase.SubjectsTable.NUMBER_SUBJECT));
//                        }
                        ContentValues theme = new ContentValues();
                        theme.put(TeacherDataBase.ThemeTable.ID_GROUP, idGroup);
                        theme.put(TeacherDataBase.ThemeTable.ID_SUBJECT, idSubject);
                        theme.put(TeacherDataBase.ThemeTable.TITLE, newTheme.getText().toString());
                        Uri uri =getContentResolver().insert(Uri.parse(TeacherContentProvider.CONTENT_URI + "/" +
                                TeacherDataBase.ThemeTable.TABLE_NAME), theme);
                        long idTheme =Integer.valueOf(uri.getLastPathSegment());

                        ContentValues lessens = new ContentValues();
                        lessens.put(TeacherDataBase.LessonsTable._ID_GROUP, idGroup);
                        lessens.put(TeacherDataBase.LessonsTable._ID_TEACHER, LoginActivity.getIdTeacher());
                        lessens.put(TeacherDataBase.LessonsTable._ID_THEME, idTheme);
                        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
                        String date = dateFormat.format(new Date());
                        lessens.put(TeacherDataBase.LessonsTable.DATE_, date);
                        getContentResolver().insert(Uri.parse(TeacherContentProvider.CONTENT_URI+"/"
                                +TeacherDataBase.LessonsTable.TABLE_NAME),lessens);
                        Intent intent = new Intent(ThemeRegistration.this, TableActivity.class);
                        intent.putExtra(TableActivity.ID_SUBJECT, idSubject);
                        intent.putExtra(TableActivity.ID_GROUP, idGroup);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_theme_registration, menu);
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
