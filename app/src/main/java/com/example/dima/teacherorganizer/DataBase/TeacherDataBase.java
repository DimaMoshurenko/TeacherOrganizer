package com.example.dima.teacherorganizer.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class TeacherDataBase extends SQLiteOpenHelper {
    public static final int VERSION = 1;
    private static final String DB_NAME = "teachersDataBase.db";
    private static final String CREATE_TEACHERS_TABLE = "CREATE TABLE " + TeachersTable.TABLE_NAME +
            " ( " + TeachersTable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TeachersTable.LOGIN + " VARCHAR(300), " +
            TeachersTable.PASSWORD + " VARCHAR(300), " +
//            TeachersTable.LOGIN+ " VARCHAR(300) NOT NULL, "+
//            TeachersTable.PASSWORD +" VARCHAR(300)NOT NULL, "+
            TeachersTable.TEACHER_NAME + " VARCHAR(300) )";

    private static final String CREATE_GROUPS_TABLE = "CREATE TABLE " + GroupsTable.TABLE_NAME +
            " ( " + GroupsTable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            GroupsTable.NAME_KURATOTA + " VARCHAR(100), " +
            GroupsTable. NUMBER_KURATOTA+ " INTEGER, " +
            GroupsTable.GROUP_ + "  VARCHAR(100))";

    private static final String CREATE_LESSONS_TABLE = "CREATE TABLE " + LessonsTable.TABLE_NAME +
            " ( " + LessonsTable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            LessonsTable.DATE_ + " VARCHAR(100), " +
            LessonsTable._ID_THEAM + " INTEGER, " +
            LessonsTable._ID_GROUP + " INTEGER, " +
            LessonsTable._ID_TEACHER + " INTEGER) ";


    private static final String CREATE_STUDENT_TABLE = "CREATE TABLE " + StudentTable.TABLE_NAME +
            " ( " + StudentTable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            StudentTable.STUDENT_NAME + " VARCHAR(300), " +
            StudentTable.TELEPHONE_NUMBER +  " VARCHAR(15), "+
            StudentTable.STUDENT_MAIL +  " VARCHAR(100), "+
            StudentTable._ID_GROUP + " INTEGER )";

    private static final String CREATE_TEACHER_SUBJECT_TABLE = "CREATE TABLE " + TeacherSubjectTable.TABLE_NAME +
            " ( " + TeacherSubjectTable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TeacherSubjectTable.SUBJECT_ID + " INTEGER, " +
            TeacherSubjectTable.TEACHER_ID + " INTEGER )";

    private static final String CREATE_DATE_TABLE = "CREATE TABLE " + ThemeTable.TABLE_NAME +
            " ( " + ThemeTable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ThemeTable.ID_SUBJECT + " INTEGER, " +
            ThemeTable.TITLE + "  VARCHAR(250), " +
            ThemeTable.NUM_PP + " INTEGER )";

    private static final String CREATE_SUBJECTS_TABLE = "CREATE TABLE " + SubjectsTable.TABLE_NAME +
            " ( " + SubjectsTable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            SubjectsTable.SUBJECT + " VARCHAR(250) )";

    public class GradesTable {
        public static final String TABLE_NAME = "GradesTable";

        public static final String ID = BaseColumns._ID;
        public static final String ID_STUDENT = "_id_student";
        public static final String ID_LESSON = "_id_lesson";

        public static final String MARKS = "grades";
    }

    private static final String CREATE_GRADES_TABLE = "CREATE TABLE " + GradesTable.TABLE_NAME +
            " ( " + GradesTable.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            GradesTable.ID_LESSON + " INTEGER, " +
            GradesTable.ID_STUDENT + " INTEGER, " +
            GradesTable.MARKS + " INTEGER ) ";

    public TeacherDataBase(Context context) {

        super(context, DB_NAME, null, VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LESSONS_TABLE);
        db.execSQL(CREATE_GROUPS_TABLE);
        db.execSQL(CREATE_GRADES_TABLE);
        db.execSQL(CREATE_STUDENT_TABLE);
        db.execSQL(CREATE_SUBJECTS_TABLE);
        db.execSQL(CREATE_TEACHER_SUBJECT_TABLE);
        db.execSQL(CREATE_TEACHERS_TABLE);
        db.execSQL(CREATE_DATE_TABLE);
        Log.e("TAG", "onCreate DB");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + GradesTable.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + GroupsTable.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + LessonsTable.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + StudentTable.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + SubjectsTable.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + TeacherSubjectTable.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + TeachersTable.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + ThemeTable.TABLE_NAME);
            onCreate(db);
        } catch (SQLiteException e) {
            Log.e("lol", "lol");
        }
    }

    public class TeachersTable {
        public static final String TABLE_NAME = "TeachersTable";

        public static final String ID = BaseColumns._ID;
        public static final String LOGIN = "login";
        public static final String PASSWORD = "password";
        public static final String TEACHER_NAME = "Teacher_Name";
    }

    public class ThemeTable {
        public static final String TABLE_NAME = "ThemeTable";

        public static final String ID = BaseColumns._ID;
        public static final String TITLE = "title";
        public static final String ID_SUBJECT = "_id_subject";

        public static final String NUM_PP = "num_pp";
    }

    public class TeacherSubjectTable {
        public static final String TABLE_NAME = "TeacherSubjectsTable";
        public static final String ID = BaseColumns._ID;
        public static final String TEACHER_ID = "teacher_id";
        public static final String SUBJECT_ID = "subject_id";
    }

    public class SubjectsTable {
        public static final String TABLE_NAME = "SubjectsTable";

        public static final String ID = BaseColumns._ID;
        public static final String SUBJECT = "subject";
    }

    public class StudentTable {
        public static final String TABLE_NAME = "StudentTable";

        public static final String ID = BaseColumns._ID;
        public static final String STUDENT_NAME = "Student_Name";
        public static final String STUDENT_MAIL = "Mail";

        public static final String  TELEPHONE_NUMBER ="telephone_number";
        public static final String _ID_GROUP = "_id_Group";
    }

    public class LessonsTable {
        public static final String TABLE_NAME = "LessonsTable";

        public static final String ID = BaseColumns._ID;
        public static final String DATE_ = "Data_";
        public static final String _ID_THEAM = "_id_theme";
        public static final String _ID_GROUP = "_id_group";
        public static final String _ID_TEACHER = "_id_teacher";
    }

    public class GroupsTable {
        public static final String TABLE_NAME = "GroupsTable";

        public static final String ID = BaseColumns._ID;
        public static final String GROUP_ = "groups";
        public static final String NAME_KURATOTA = "name_kuratora";
        public static final String NUMBER_KURATOTA = "number_kuratora";
    }
}
