package com.example.dima.teacherorganizer.Activity;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.dima.teacherorganizer.DataBase.TeacherDataBase;
import com.example.dima.teacherorganizer.R;

import java.util.ArrayList;

public class TableActivity extends ActionBarActivity {

    //    public static String ID;
    private SQLiteDatabase database;
    public static final String ID_GROUP = " id group ";
    public static final String ID_SUBJECT = " id subject ";
    public static final String NUM_SUBJECT = " num subject ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("TAG", "table activity id groups " + String.valueOf(getIntent().getStringExtra(ID_GROUP)));
        Log.e("TAG", "table activity id subjects " + String.valueOf(getIntent().getStringExtra(ID_SUBJECT)));
        Log.e("TAG", "table activity num subjects " + String.valueOf(getIntent().getStringExtra(NUM_SUBJECT)));
        setContentView(R.layout.activity_table_grade);

        database = new TeacherDataBase(TableActivity.this).getWritableDatabase();
        String tableStudents = TeacherDataBase.StudentTable.TABLE_NAME;
        String[] fieldsStudents = {TeacherDataBase.StudentTable.ID, TeacherDataBase.StudentTable._ID_TEACHER,
                TeacherDataBase.StudentTable._ID_GROUP ,TeacherDataBase.StudentTable.STUDENT_NAME};


        String whereStudents = TeacherDataBase.StudentTable._ID_GROUP + " = " + getIntent().getStringExtra(ID_GROUP)  + " and " +
                TeacherDataBase.StudentTable._ID_TEACHER + " = " + LoginActivity.getIdTeacher()+ "  ;";
        Cursor students = database.query(tableStudents, fieldsStudents, whereStudents, null, null, null, null);

        String tableThemes = TeacherDataBase.ThemeTable.TABLE_NAME;
        String[] fieldsThemes = {TeacherDataBase.ThemeTable.ID, TeacherDataBase.ThemeTable.ID_SUBJECT,
                TeacherDataBase.ThemeTable.ID_SUBJECT, TeacherDataBase.ThemeTable.TITLE};

        String whereThemes = TeacherDataBase.ThemeTable.ID_SUBJECT + " = " + getIntent().getStringExtra(ID_SUBJECT) + " " + "; ";
//                TeacherDataBase.ThemeTable.NUM_PP+" = "+ getIntent().getStringExtra(NUM_SUBJECT)+"  ;";
        Cursor themes = database.query(tableThemes, fieldsThemes, whereThemes, null, null, null, null);
        // длинна шапки будет зависить от количества тем

        ArrayList<RowTablesGrades> listTablesInformation = new ArrayList<>();
        Log.e("TAG", " ok theme and student ");
        if (students.moveToFirst() & themes.moveToFirst()) {
            do {
                String tableLessens = TeacherDataBase.LessonsTable.TABLE_NAME;
                String[] fieldsLessens = {TeacherDataBase.LessonsTable.ID, TeacherDataBase.LessonsTable._ID_GROUP,
                        TeacherDataBase.LessonsTable._ID_TEACHER, TeacherDataBase.LessonsTable._ID_THEAM,
                        TeacherDataBase.LessonsTable.DATE_};

                String whereLessens = TeacherDataBase.LessonsTable._ID_THEAM + " = " +
                        themes.getString(themes.getColumnIndex(TeacherDataBase.ThemeTable.ID)) + " " + " and " +
                        TeacherDataBase.LessonsTable._ID_GROUP + " = " + getIntent().getStringExtra(ID_GROUP) + "  and " +
                        TeacherDataBase.LessonsTable._ID_TEACHER + " = " + LoginActivity.getIdTeacher() + "  and " +
                        TeacherDataBase.LessonsTable._ID_GROUP + " = " + getIntent().getStringExtra(ID_SUBJECT) + " ;";
                Cursor lessens = database.query(tableLessens, fieldsLessens, whereLessens, null, null, null, null);
                Log.e("TAG", " ok lessens ");

                if (lessens.moveToFirst()) {
                    do {
                        String tableGrades = TeacherDataBase.GradesTable.TABLE_NAME;
                        String[] fieldsGrades = {TeacherDataBase.GradesTable.ID, TeacherDataBase.GradesTable.ID_STUDENT,
                                TeacherDataBase.GradesTable.ID_LESSON, TeacherDataBase.GradesTable.MARKS};

                        String whereGrades = TeacherDataBase.GradesTable.ID_LESSON + " = " +
                                lessens.getString(lessens.getColumnIndex(TeacherDataBase.LessonsTable.ID)) + " and " +
                                TeacherDataBase.GradesTable.ID_STUDENT + " = " +
                                students.getString(students.getColumnIndex(TeacherDataBase.StudentTable.ID)) + " ;";
                        Cursor grades = database.query(tableGrades, fieldsGrades, whereGrades, null, null, null, null);
                        Log.e("TAG", " ok grads ");
//                        Log.e("TAG", "name : " + students.getString(students.getColumnIndex(TeacherDataBase.StudentTable.STUDENT_NAME)));

                    } while (lessens.moveToNext());

                }

            }
            while (students.moveToNext() & themes.moveToFirst());
        }
        ListView list = (ListView) findViewById(R.id.list_grades);

//        TablesAdapter adapter = new TablesAdapter(this);
//        list.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_table, menu);
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

    private class TablesAdapter extends BaseAdapter {

        private final LayoutInflater inflater;

        public TablesAdapter(Activity context) {
            this.inflater = context.getLayoutInflater();
        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_list_group_subjects, parent, false);
                for (int i = 0; i < 3; i++) {

                }

            } else {

//                holder = (ViewHolder) convertView.getTag();
            }

            return null;
        }

    }

    public class RowTablesGrades {
        private String studentName;
        private ArrayList<String> grades;

        public RowTablesGrades() {
            grades = new ArrayList<>();
        }

        public String getStudentName() {
            return studentName;
        }

        public void setStudentName(String studentName) {
            this.studentName = studentName;
        }

        public ArrayList<String> getGrades() {
            return grades;
        }

        public void setGrades(ArrayList<String> grades) {
            this.grades = grades;
        }
    }

}
