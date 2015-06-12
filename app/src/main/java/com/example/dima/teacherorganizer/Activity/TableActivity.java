package com.example.dima.teacherorganizer.Activity;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dima.teacherorganizer.DataBase.TeacherDataBase;
import com.example.dima.teacherorganizer.R;
import com.example.dima.teacherorganizer.ThemeRegistration;

import java.util.ArrayList;

public class TableActivity extends ListActivity {

    private SQLiteDatabase database;
    public static final String ID_GROUP = " id group ";
    public static final String ID_SUBJECT = " id subject ";
    public static final String NUM_SUBJECT = " num subject ";
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("TAG", "table activity id groups " + String.valueOf(getIntent().getStringExtra(ID_GROUP)));
        Log.e("TAG", "table activity id subjects " + String.valueOf(getIntent().getStringExtra(ID_SUBJECT)));
        setContentView(R.layout.activity_table_grade);
        linearLayout = (LinearLayout) findViewById(R.id.header);
        database = new TeacherDataBase(TableActivity.this).getWritableDatabase();

        String tableStudents = TeacherDataBase.StudentTable.TABLE_NAME;
        String[] fieldsStudents = {TeacherDataBase.StudentTable.ID, TeacherDataBase.StudentTable._ID_TEACHER,
                TeacherDataBase.StudentTable._ID_GROUP, TeacherDataBase.StudentTable.STUDENT_NAME};

        String whereStudents = TeacherDataBase.StudentTable._ID_GROUP + " = " + getIntent().getStringExtra(ID_GROUP) + " and " +
                TeacherDataBase.StudentTable._ID_TEACHER + " = " + LoginActivity.getIdTeacher() + "  ;";
        Cursor students = database.query(tableStudents, fieldsStudents, whereStudents, null, null, null, null);

        String tableThemes = TeacherDataBase.ThemeTable.TABLE_NAME;
        String[] fieldsThemes = {TeacherDataBase.ThemeTable.ID, TeacherDataBase.ThemeTable.ID_SUBJECT,
                TeacherDataBase.ThemeTable.ID_SUBJECT, TeacherDataBase.ThemeTable.TITLE};
        String whereThemes = TeacherDataBase.ThemeTable.ID_SUBJECT + " = " + getIntent().getStringExtra(ID_SUBJECT) + " and " +
                TeacherDataBase.ThemeTable.ID_GROUP + " = " + getIntent().getStringExtra(ID_GROUP) + "  ";

        Cursor themes = database.query(tableThemes, fieldsThemes, whereThemes, null, null, null, null);
        // длинна шапки будет зависить от количества тем

        ArrayList<RowTablesGrades> listTablesInformation = new ArrayList<>();
        ArrayList<String> grads = new ArrayList<>();
        ArrayList<String> listThemes = new ArrayList<>();
        ArrayList<String> lessensList = new ArrayList<>();

        RowTablesGrades information = null;
        Cursor lessens = null;
        if (students.moveToFirst()) {
            do {
                information = new RowTablesGrades();
                information.setStudentName(students.getString(students.getColumnIndex(TeacherDataBase.StudentTable.STUDENT_NAME)));
                Log.e("TAG", " ok  student " + students.getString(students.getColumnIndex(TeacherDataBase.StudentTable.STUDENT_NAME)));
                Log.e("TAG", " themes " + String.valueOf(themes.getCount()));
                if (themes.moveToFirst()) {
                    do {
                        Log.e("TAG", "Themes ok");
                        // количество тем это количесво колонок не обезательно заполненых
                        listThemes.add(themes.getString(themes.getColumnIndex(TeacherDataBase.ThemeTable.TITLE)));

                        String tableLessens = TeacherDataBase.LessonsTable.TABLE_NAME;
                        String[] fieldsLessens = {TeacherDataBase.LessonsTable.ID, TeacherDataBase.LessonsTable._ID_GROUP,
                                TeacherDataBase.LessonsTable._ID_TEACHER, TeacherDataBase.LessonsTable._ID_THEME,
                                TeacherDataBase.LessonsTable.DATE_};

                        String whereLessens = TeacherDataBase.LessonsTable._ID_THEME + " = " +
                                themes.getString(themes.getColumnIndex(TeacherDataBase.ThemeTable.ID)) + " " + " and " +
                                TeacherDataBase.LessonsTable._ID_GROUP + " = " + getIntent().getStringExtra(ID_GROUP) + "  and " +
                                TeacherDataBase.LessonsTable._ID_TEACHER + " = " + LoginActivity.getIdTeacher() + " ;";

                        lessens = database.query(tableLessens, fieldsLessens, whereLessens, null, null, null, null);
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
                                if (grades.moveToFirst()) {
                                    do {
                                        grads.add(grades.getString(grades.getColumnIndex(TeacherDataBase.GradesTable.MARKS)));

                                    } while (grades.moveToNext());
                                } else {
                                    grads.add("");
                                }

                            } while (lessens.moveToNext());

                        }

                    } while (themes.moveToNext());
                    information.setThemes(listThemes);
                    information.setGrades(grads);
                    information.setLessens(lessensList);
                }
                listTablesInformation.add(information);
            } while (students.moveToNext());
        }

        if (themes.moveToFirst()) {
            do {
                String tableLessens = TeacherDataBase.LessonsTable.TABLE_NAME;
                String[] fieldsLessens = {TeacherDataBase.LessonsTable.ID, TeacherDataBase.LessonsTable._ID_GROUP,
                        TeacherDataBase.LessonsTable._ID_TEACHER, TeacherDataBase.LessonsTable._ID_THEME,
                        TeacherDataBase.LessonsTable.DATE_};

                String whereLessens = TeacherDataBase.LessonsTable._ID_THEME + " = " +
                        themes.getString(themes.getColumnIndex(TeacherDataBase.ThemeTable.ID)) + " " + " and " +
                        TeacherDataBase.LessonsTable._ID_GROUP + " = " + getIntent().getStringExtra(ID_GROUP) + "  and " +
                        TeacherDataBase.LessonsTable._ID_TEACHER + " = " + LoginActivity.getIdTeacher() + " ;";

                lessens = database.query(tableLessens, fieldsLessens, whereLessens, null, null, null, null);
                if (lessens.moveToFirst()) {
                    do {
                        lessensList.add(lessens.getString(lessens.getColumnIndex(TeacherDataBase.LessonsTable.DATE_)));
                        TextView data = new TextView(this);
                        data.setText(lessens.getString(lessens.getColumnIndex(TeacherDataBase.LessonsTable.DATE_)));
                        settingsLessensTextView(data);
                        linearLayout.addView(data);
                        Log.e("TAG", "themes " + String.valueOf(lessens.getString(lessens.getColumnIndex(TeacherDataBase.LessonsTable.DATE_))));
                    } while (lessens.moveToNext());
                }
            } while (themes.moveToNext());

        }


        int dp = 5;
        final float scale = getResources().getDisplayMetrics().density;
        int paddingPx = (int) (dp * scale + 0.5f);
        Button addTheme = new Button(this);
        addTheme.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) (40 * scale + 0.5f)));
        addTheme.setText("Тему добавить");
        addTheme.setPadding(paddingPx, paddingPx,
                paddingPx, paddingPx);
        addTheme.setBackgroundColor(getResources().getColor(R.color.color_primary));
        addTheme.setTextSize(20);
        addTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TableActivity.this, ThemeRegistration.class);
                intent.putExtra(ID_SUBJECT, String.valueOf(getIntent().getStringExtra(ID_SUBJECT)));
                intent.putExtra(ID_GROUP, String.valueOf(getIntent().getStringExtra(ID_GROUP)));
                startActivity(intent);
            }
        });
        linearLayout.addView(addTheme);
        Log.e("TAG", "list tables " + String.valueOf(listTablesInformation.size()));
        TablesAdapter adapter = new TablesAdapter(this, listTablesInformation, lessensList);
        setListAdapter(adapter);
    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params) {
        super.addContentView(view, params);
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
        private ArrayList<RowTablesGrades> listTablesInformation;
        private ArrayList<String> themes;
        private Context context;

        public TablesAdapter(Context context, ArrayList<RowTablesGrades> listTablesInformation, ArrayList<String> themes) {
            this.context = context;
            this.listTablesInformation = listTablesInformation;
            this.themes = themes;
            Activity activity = (Activity) context;
            this.inflater = activity.getLayoutInflater();

        }

        @Override
        public int getCount() {
            return listTablesInformation.size();
        }

        @Override
        public Object getItem(int position) {
            return listTablesInformation.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout linearLayout = null;
            RowTablesGrades info = listTablesInformation.get(position);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.tables_colum, parent, false);
                TextView name = (TextView) convertView.findViewById(R.id.name_student_grade);
                name.setText(info.getStudentName());
                linearLayout = (LinearLayout) convertView.findViewById(R.id.info);
                for (int i = 0; i < themes.size(); i++) {
                    TextView grade = new TextView(context);

                    settingsGradsTextView(grade, 1);
                    if (info.getGrades() != null) {
                        grade.setText(info.getGrades().get(i));
                    } else {
                        grade.setText(" ");
                    }
                    linearLayout.addView(grade);
                }
            }
            return convertView;
        }
    }

    public class RowTablesGrades {
        private String studentName;
        private ArrayList<String> grades;
        private ArrayList<String> themes;
        private ArrayList<String> lessens;

        public ArrayList<String> getLessens() {
            return lessens;
        }

        public void setLessens(ArrayList<String> lessens) {
            this.lessens = lessens;
        }

        public void setThemes(ArrayList<String> themes) {
            this.themes = themes;
        }

        public ArrayList<String> getThemes() {

            return themes;
        }

        public RowTablesGrades() {
            grades = new ArrayList<>();
            themes = new ArrayList<>();
            lessens = new ArrayList<>();
//            studentName = new String();
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

    private void settingsGradsTextView(TextView textView, int id) {
        int dp = 5;
        final float scale = getResources().getDisplayMetrics().density;
        int paddingPx = (int) (dp * scale + 0.5f);
        textView.setLayoutParams(new ViewGroup.LayoutParams((int) (96 * scale + 0.5f), R.dimen.height_text_grade));
        textView.setBackgroundResource(R.drawable.border_text_view);
        textView.setPadding(paddingPx, paddingPx, paddingPx, paddingPx);
        textView.setTextSize(15);
    }

    private void settingsLessensTextView(TextView textView) {
        int dp = 5;
        final float scale = getResources().getDisplayMetrics().density;
        int paddingPx = (int) (dp * scale + 0.5f);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) (40 * scale + 0.5f)));
        textView.setPadding(paddingPx, paddingPx, paddingPx, paddingPx);

        textView.setBackgroundResource(R.drawable.border_text_view);
        textView.setTextSize(20);
    }
}