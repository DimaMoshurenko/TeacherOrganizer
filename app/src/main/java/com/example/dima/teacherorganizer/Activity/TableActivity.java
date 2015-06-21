package com.example.dima.teacherorganizer.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dima.teacherorganizer.DataBase.TeacherDataBase;
import com.example.dima.teacherorganizer.R;
import com.example.dima.teacherorganizer.ThemeRegistration;
import com.gc.materialdesign.views.ButtonFlat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TableActivity extends ActionBarActivity implements NumberPicker.OnValueChangeListener {

    private SQLiteDatabase database;
    public static final String ID_GROUP = " id group ";
    public static final String ID_SUBJECT = " id subject ";
    public static final String NB_ABSENCE = "Нб";


    public static final String NUM_SUBJECT = " num subject ";
    private LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.e("TAG", "table activity id groups " + String.valueOf(getIntent().getStringExtra(ID_GROUP)));
//        Log.e("TAG", "table activity id subjects " + String.valueOf(getIntent().getStringExtra(ID_SUBJECT)));
        setContentView(R.layout.activity_table_grade);
        ButtonFlat how = (ButtonFlat) findViewById(R.id.how);
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

        final ArrayList<RowTablesGrades> listTablesInformation = new ArrayList<>();
        RowTablesGrades information;
        Cursor lessens = null;
        if (students.moveToFirst())
            do {
                information = new RowTablesGrades();
                information.setStudentName(students.getString(students.getColumnIndex(TeacherDataBase.StudentTable.STUDENT_NAME)));
                ArrayList<GradsDataTheme> listThemes = new ArrayList<>();
//                Log.e("TAG", " ok  student " + students.getString(students.getColumnIndex(TeacherDataBase.StudentTable.STUDENT_NAME)));
//                Log.e("TAG", " themes " + String.valueOf(themes.getCount()));
                if (themes.moveToFirst()) {

                    do {

                        GradsDataTheme gradsThemeData = new GradsDataTheme();
                        // количество тем это количесво колонок не обезательно заполненых
                        gradsThemeData.setThemeTitle(themes.getString(themes.getColumnIndex(TeacherDataBase.ThemeTable.TITLE)));

                        String tableLessens = TeacherDataBase.LessonsTable.TABLE_NAME;
                        String[] fieldsLessens = {TeacherDataBase.LessonsTable.ID, TeacherDataBase.LessonsTable._ID_GROUP,
                                TeacherDataBase.LessonsTable._ID_TEACHER, TeacherDataBase.LessonsTable._ID_THEME,
                                TeacherDataBase.LessonsTable.DATE_};

                        String whereLessens = TeacherDataBase.LessonsTable._ID_THEME + " = " +
                                themes.getString(themes.getColumnIndex(TeacherDataBase.ThemeTable.ID)) + " " + " and " +
                                TeacherDataBase.LessonsTable._ID_GROUP + " = " + getIntent().getStringExtra(ID_GROUP) + "  and " +
                                TeacherDataBase.LessonsTable._ID_TEACHER + " = " + LoginActivity.getIdTeacher() + " ;";
                        lessens = database.query(tableLessens, fieldsLessens, whereLessens, null, null, null, null);
                        ArrayList<String> grads = new ArrayList<>();
                        if (lessens.moveToFirst()) {
                            do {
                                gradsThemeData.setData(lessens.getString(lessens.getColumnIndex(TeacherDataBase.LessonsTable.DATE_)));
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
//                                        Log.i("TAG", students.getString(students.getColumnIndex(TeacherDataBase.StudentTable.STUDENT_NAME)));
//                                        Log.i("TAG", "grade " + grades.getString(grades.getColumnIndex(TeacherDataBase.GradesTable.MARKS)));
//                                        Log.i("TAG", "theme " + themes.getString(themes.getColumnIndex(TeacherDataBase.ThemeTable.TITLE)));
                                        grads.add(grades.getString(grades.getColumnIndex(TeacherDataBase.GradesTable.MARKS)));

                                    } while (grades.moveToNext());
                                } else {
                                    grads.add("");
                                }

                                Log.e("TAG", "size " + String.valueOf(grads.size()));
                                gradsThemeData.setGrads(grads);
                            } while (lessens.moveToNext());
                        }
                        listThemes.add(gradsThemeData);
                    } while (themes.moveToNext());
                    information.setGradsDataTheme(listThemes);
                }
//                themes.close();

                listTablesInformation.add(information);
            } while (students.moveToNext());
        ArrayList<TextView> header = new ArrayList<>();
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
                    ArrayList<String> lessensList = new ArrayList<>();
                    do {
                        lessensList.add(lessens.getString(lessens.getColumnIndex(TeacherDataBase.LessonsTable.DATE_)));
                        TextView data = new TextView(this);
                        data.setText(lessens.getString(lessens.getColumnIndex(TeacherDataBase.LessonsTable.DATE_)));
                        settingsLessensTextView(data);
                        linearLayout.addView(data);
                        header.add(data);
                    } while (lessens.moveToNext());
                }
            } while (themes.moveToNext());
        }

        int dp = 5;
        final float scale = getResources().getDisplayMetrics().density;
        int paddingPx = (int) (dp * scale + 0.5f);
        Button addTheme = new Button(this);
        addTheme.setLayoutParams(new ViewGroup.LayoutParams((int) (100 * scale + 0.5f), (int) (40 * scale + 0.5f)));
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
        TablesAdapter adapter = new TablesAdapter(this, listTablesInformation, header);
        final ListView list = (ListView) findViewById(R.id.list_grades);
        list.setAdapter(adapter);

//        setListAdapter(adapter);


        themes.close();
        students.close();
        how.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random generator = new Random();
                int i = generator.nextInt(listTablesInformation.size());
                Toast.makeText(TableActivity.this, " Отвечать будет " + listTablesInformation.get(i).getStudentName(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params) {
        super.addContentView(view, params);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_table, menu);
        return super.onCreateOptionsMenu(menu);

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

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        Log.e("value is", "" + newVal);
    }

    private class TablesAdapter extends BaseAdapter {

        private final LayoutInflater inflater;
        private ArrayList<RowTablesGrades> listTablesInformation;
        private ArrayList<TextView> header;
        private Context context;

        public TablesAdapter(Context context, ArrayList<RowTablesGrades> listTablesInformation, ArrayList<TextView> header) {
            this.context = context;
            this.listTablesInformation = listTablesInformation;
            this.header = header;
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
            LinearLayout linearLayout;
            final RowTablesGrades studentInformation = listTablesInformation.get(position);

            Log.i("TAG", "Student name " + studentInformation.getStudentName());

            Log.i("TAG", "size header " + header.size());


            if (convertView == null) {
                convertView = inflater.inflate(R.layout.tables_colum, parent, false);
                final TextView name = (TextView) convertView.findViewById(R.id.name_student_grade);
                name.setText(studentInformation.getStudentName());
                linearLayout = (LinearLayout) convertView.findViewById(R.id.info);
                String repeatTheme = null;
//                if (studentInformation.getGradsDataTheme()!=null) {

                    if (header.size()>0) {
                        for (int i = 0; i < header.size(); i++) {
                            final TextView grade = new TextView(context);
                            settingsGradsTextView(grade, 1);

                            if (studentInformation.getGradsDataTheme()!=null) {
                                if(studentInformation.getGradsDataTheme().get(i).getGrads()!=null) {
                                    String grads = new String();
                                    for (int j = 0; j < studentInformation.getGradsDataTheme().get(i).getGrads().size(); j++) {
                                        Log.e("TAG", String.valueOf(j) + " = "
                                                + String.valueOf(studentInformation.getGradsDataTheme().get(i).getGrads().size() - 1));
                                        grads = grads + studentInformation.getGradsDataTheme().get(i).getGrads().get(j);
                                        if (j == studentInformation.getGradsDataTheme().get(i).getGrads().size() - 1) {
                                            grads = grads + ".";
                                        } else {
                                            grads = grads + ",";
                                        }

                                    }
                                    grade.setText(grads);
                                }else {
                                    grade.setText(" ");
                                }
                            } else {
                                grade.setText(" ");
                            }
                            linearLayout.addView(grade);
                            final int index = i;

                            grade.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    final Dialog d = new Dialog(TableActivity.this);
                                    d.setTitle("NumberPicker");
                                    d.setContentView(R.layout.dialog);
                                    Button set = (Button) d.findViewById(R.id.Set);
                                    Button cencel = (Button) d.findViewById(R.id.cancel);
                                    final NumberPicker picker = (NumberPicker) d.findViewById(R.id.numberPicker1);
                                    picker.setMinValue(0);
                                    picker.setMaxValue(12);
                                    final String[] grads = new String[13];
                                    Log.e("TAG", "grads " + grads.length);
                                    grads[0] = NB_ABSENCE;
                                    for (int i = 1; i < grads.length; i++) {
                                        grads[i] = String.valueOf(i);

                                    }

                                    picker.setDisplayedValues(grads);
                                    picker.setWrapSelectorWheel(false);
                                    picker.setOnValueChangedListener(TableActivity.this);
                                    set.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Log.e("TAG", grads[picker.getValue()]);
                                            Log.e("TAG", " picker " + String.valueOf(picker.getValue()));
//                                        tv.setText(grads[picker.getValue()]);
                                            if (grads[picker.getValue()] != null & !grads[picker.getValue()].isEmpty()) {
                                                OnClickListener(studentInformation.getStudentName(), studentInformation.getGradsDataTheme().get(index).getThemeTitle(),
                                                        grads[picker.getValue()], grade, header.get(index).getText().toString());
                                                Log.e("TAG", " grades " + grads[picker.getValue()]);
                                            }
                                            d.dismiss();
                                        }
                                    });
                                    cencel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            d.dismiss();
                                        }
                                    });
                                    d.show();
//
                                }
                            });
                        }

                        }
//                    }


            }
            return convertView;
        }
    }

    public class RowTablesGrades {
        private String studentName;
        private ArrayList<GradsDataTheme> themes;
        public RowTablesGrades(){
        }

        public void setGradsDataTheme(ArrayList<GradsDataTheme> themes) {
            this.themes = themes;
        }

        public List<GradsDataTheme> getGradsDataTheme() {

            return themes;
        }

        public String getStudentName() {
            return studentName;
        }

        public void setStudentName(String studentName) {

            this.studentName = studentName;
        }
    }

    public class GradsDataTheme {
        private String themeTitle;
        private String data;
        private ArrayList<String> grads;
        public GradsDataTheme(){
            grads = new ArrayList<>();
        }

        public String getThemeTitle() {
            return themeTitle;
        }

        public void setThemeTitle(String themeTitle) {
            this.themeTitle = themeTitle;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public ArrayList<String> getGrads() {
            return grads;
        }

        public void setGrads(ArrayList<String> grads) {
            this.grads = grads;
        }
    }

    private void settingsGradsTextView(TextView textView, int id) {
        int dp = 5;
        final float scale = getResources().getDisplayMetrics().density;
        int paddingPx = (int) (dp * scale + 0.5f);
        textView.setLayoutParams(new ViewGroup.LayoutParams((int) (93 * scale + 0.5f), (int) (30 * scale + 0.5f)));
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

    private void OnClickListener(String nameStudent, String nameTitleTheme, String grades, TextView grade, String data) {
        if (grades.length() > 0 && getIntent().getStringExtra(ID_GROUP) != null && getIntent().getStringExtra(ID_SUBJECT) != null) {
            database = new TeacherDataBase(TableActivity.this).getWritableDatabase();
//                                                Log.e("TAG", "grads " + grades[0] + " student name " + info.getStudentName());
            String tableStudents = TeacherDataBase.StudentTable.TABLE_NAME;
            String[] fieldsStudents = {TeacherDataBase.StudentTable.ID, TeacherDataBase.StudentTable._ID_TEACHER,
                    TeacherDataBase.StudentTable._ID_GROUP, TeacherDataBase.StudentTable.STUDENT_NAME};

            String whereStudents = TeacherDataBase.StudentTable._ID_GROUP + " = " + getIntent().getStringExtra(ID_GROUP) + " and " +
                    TeacherDataBase.StudentTable._ID_TEACHER + " = " + LoginActivity.getIdTeacher() + " and " +
                    TeacherDataBase.StudentTable.STUDENT_NAME + " = '" + nameStudent + "' ;";
            Cursor student = database.query(tableStudents, fieldsStudents, whereStudents, null, null, null, null);
            String idStudent = null;
            String idTheme = null;
            String idLessen = null;
            Log.e("TAG", "theme bed");
            String tableTheme = TeacherDataBase.ThemeTable.TABLE_NAME;
            String[] fieldsTheme = {TeacherDataBase.ThemeTable.ID, TeacherDataBase.ThemeTable.ID_SUBJECT,
                    TeacherDataBase.ThemeTable.ID_SUBJECT, TeacherDataBase.ThemeTable.TITLE};
            String whereTheme = TeacherDataBase.ThemeTable.ID_SUBJECT + " = " + getIntent().getStringExtra(ID_SUBJECT) + " and " +
                    TeacherDataBase.ThemeTable.ID_GROUP + " = " + getIntent().getStringExtra(ID_GROUP) + " and " +
                    TeacherDataBase.ThemeTable.TITLE + " = '" + nameTitleTheme + "'  ;";

            Cursor theme = database.query(tableTheme, fieldsTheme, whereTheme, null, null, null, null);

            if (theme.moveToFirst()) {
                Log.e("TAG", "theme okey");
                idTheme = theme.getString(theme.getColumnIndex(TeacherDataBase.ThemeTable.ID));
                String tableLessens = TeacherDataBase.LessonsTable.TABLE_NAME;
                String[] fieldsLessens = {TeacherDataBase.LessonsTable.ID, TeacherDataBase.LessonsTable._ID_GROUP,
                        TeacherDataBase.LessonsTable._ID_TEACHER, TeacherDataBase.LessonsTable._ID_THEME,
                        TeacherDataBase.LessonsTable.DATE_};

                String whereLessens = TeacherDataBase.LessonsTable._ID_THEME + " = " + idTheme + " " + " and " +
                        TeacherDataBase.LessonsTable._ID_GROUP + " = " + getIntent().getStringExtra(ID_GROUP) + "  and " +
                        TeacherDataBase.LessonsTable.DATE_ + " = '" + data + "'  and " +
                        TeacherDataBase.LessonsTable._ID_TEACHER + " = " + LoginActivity.getIdTeacher() + " ;";

                Cursor lessen = database.query(tableLessens, fieldsLessens, whereLessens, null, null, null, null);
                if (lessen.moveToFirst()) {
                    do {
                        idLessen = lessen.getString(lessen.getColumnIndex(TeacherDataBase.LessonsTable.ID));
                        if (student.moveToFirst()) {
                            idStudent = student.getString(student.getColumnIndex(TeacherDataBase.StudentTable.ID));
                            String tableAbsence = TeacherDataBase.GradesTable.TABLE_NAME;
                            String[] fieldsAbsence = {TeacherDataBase.GradesTable.ID, TeacherDataBase.GradesTable.ID_LESSON,
                                    TeacherDataBase.GradesTable.ID_STUDENT, TeacherDataBase.GradesTable.MARKS};

                            String whereAbsence = TeacherDataBase.GradesTable.ID_LESSON + " = " + idLessen + " " + " and " +
                                    TeacherDataBase.GradesTable.ID_STUDENT + " = " + idStudent + " ;";

                            Cursor absence = database.query(tableAbsence, fieldsAbsence, whereAbsence, null, null, null, null);
                            ArrayList absenceList = new ArrayList();
                            if (absence.moveToFirst()) {
                                do {
                                    absenceList.add(absence.getString(absence.getColumnIndex(TeacherDataBase.GradesTable.MARKS)));
                                } while (absence.moveToNext());
                            }
                            Boolean nb = false;
                            for (int i = 0; i < absenceList.size(); i++) {

                                if (absenceList.get(i).equals(NB_ABSENCE)) {
                                    nb = true;
                                }
                            }
                            if (!nb) {
                                ContentValues content = new ContentValues();
                                content.put(TeacherDataBase.GradesTable.ID_STUDENT, idStudent);
                                content.put(TeacherDataBase.GradesTable.ID_LESSON, idLessen);
                                content.put(TeacherDataBase.GradesTable.MARKS, grades);
                                database.insert(TeacherDataBase.GradesTable.TABLE_NAME, null, content);
                                grade.setText(grades);
                                Log.e("TAG", " student " + idStudent);
                                Log.e("TAG", " lessens " + idLessen);
                                Log.e("TAG", " theme " + idTheme);
                                Log.e("TAG", " okey greds add");
                            } else {
                                Toast.makeText(getApplication(), " Студент відсутній  ", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    while (lessen.moveToNext());
                }
            }
        } else {
            Log.e("TAG", "Error");
        }
    }
}