package com.example.dima.teacherorganizer.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiEnterpriseConfig;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dima.teacherorganizer.DataBase.TeacherDataBase;
import com.example.dima.teacherorganizer.R;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

public class SubjectsTeacherActivity extends ActionBarActivity {
    public static final String ID_SUBJECT = "name subject";
    private ListView listGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects_teacher);
        listGroups = (ListView) findViewById(R.id.list_subjects_groups);
        SQLiteDatabase database = new TeacherDataBase(this).getReadableDatabase();
        final String idSubject = getIntent().getStringExtra(SubjectsTeacherActivity.ID_SUBJECT);
        if (idSubject != null) {
            Log.e("TAG", " idSubject " + String.valueOf(idSubject));
            String TABLE = TeacherDataBase.TeacherSubjectTable.TABLE_NAME;
            String[] FIELDS = {TeacherDataBase.TeacherSubjectTable.ID, TeacherDataBase.TeacherSubjectTable.GROUP_ID,
                    TeacherDataBase.TeacherSubjectTable.TEACHER_ID};
            String WHERE = TeacherDataBase.TeacherSubjectTable.TEACHER_ID + " =" + LoginActivity.getIdTeacher() + " " + " and " +
                    TeacherDataBase.TeacherSubjectTable.SUBJECT_ID + " = " + idSubject + "  ;";
            final Cursor cursor = database.query(TABLE, FIELDS, WHERE, null, null, null, null);

            List<String> list = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    Cursor query = database.query(TeacherDataBase.GroupsTable.TABLE_NAME, new String[]{TeacherDataBase.GroupsTable.ID,
                                    TeacherDataBase.GroupsTable.GROUP_},
                            TeacherDataBase.GroupsTable.ID + " = ? ",
                            new String[]{cursor.getString(cursor.getColumnIndex(TeacherDataBase.TeacherSubjectTable.GROUP_ID))}, null, null, null);
                    if (query.moveToFirst()) {
                        do {
                            list.add(query.getString(query.getColumnIndex(TeacherDataBase.GroupsTable.GROUP_)));

                        } while (query.moveToNext());
                    }
                } while (cursor.moveToNext());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_list_fragment, R.id.other_name, list);

            listGroups.setAdapter(adapter);
            listGroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView textView = (TextView) view.findViewById(R.id.other_name);
                    SQLiteDatabase database = new TeacherDataBase(SubjectsTeacherActivity.this).getReadableDatabase();
                    Cursor query = database.query(TeacherDataBase.GroupsTable.TABLE_NAME, new String[]{TeacherDataBase.GroupsTable.ID,
                                    TeacherDataBase.GroupsTable.GROUP_},
                            TeacherDataBase.GroupsTable.GROUP_ + " = ? ",
                            new String[]{textView.getText().toString()}, null, null, null);
                    String idGroup = null;
                    String numSubject = null;
                    if (query.moveToFirst()) {
                        do {
                            idGroup = query.getString(query.getColumnIndex(TeacherDataBase.GroupsTable.ID));
//                            if (idGroup != null) {
//                                Cursor groupCursor = database.query(TeacherDataBase.SubjectsTable.TABLE_NAME,
//                                        new String[]{TeacherDataBase.SubjectsTable.ID,
//                                                TeacherDataBase.SubjectsTable.NUMBER_SUBJECT, TeacherDataBase.SubjectsTable.ID_TEACHER},
//                                        TeacherDataBase.GroupsTable.GROUP_ + " = " + textView.getText().toString() + " and " +
//                                                TeacherDataBase.GroupsTable.ID + " = " + idGroup,
//                                        null, null, null, null);
//                                if (groupCursor.moveToFirst()) {
//                                    numSubject = groupCursor.getString(groupCursor.getColumnIndex(TeacherDataBase.SubjectsTable.NUMBER_SUBJECT));
//                                }
//                            }
                        } while (query.moveToNext());
                    }
                    Intent intent = new Intent(SubjectsTeacherActivity.this, TableActivity.class);
                    intent.putExtra(TableActivity.ID_GROUP, idGroup);
                    intent.putExtra(TableActivity.ID_SUBJECT, idSubject);
//                    intent.putExtra(TableActivity.NUM_SUBJECT, numSubject);
                    startActivity(intent);
                    finish();
                }
            });
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_subjects_teacher, menu);
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
