package com.example.dima.teacherorganizer.DataBase;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import java.sql.SQLException;

public class TeacherContentProvider extends ContentProvider {

    private SQLiteDatabase database;
    public static final Uri CONTENT_URI = Uri.parse("content://com.example.TeacherOrganizer.KMK.KNTU.ContentProvider");
    private static final String AUTHORITY = "com.example.TeacherOrganizer.KMK.KNTU.ContentProvider";
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int THEMES_TABLE = 1;
    private static final int GROUPS_TABLE = 2;
    private static final int LESSENS_TABLE = 3;
    private static final int TEACHER_TABLE = 4;
    private static final int TEACHER_SUBJECTS_TABLE = 5;
    private static final int STUDENT_TABLE = 6;
    private static final int SUBJECTS_TABLE = 7;
    private static final int GRADES_TABLE = 8;

    static {
        uriMatcher.addURI(AUTHORITY, TeacherDataBase.ThemeTable.TABLE_NAME, THEMES_TABLE);
        uriMatcher.addURI(AUTHORITY, TeacherDataBase.GroupsTable.TABLE_NAME, GROUPS_TABLE);
        uriMatcher.addURI(AUTHORITY, TeacherDataBase.LessonsTable.TABLE_NAME, LESSENS_TABLE);
        uriMatcher.addURI(AUTHORITY, TeacherDataBase.TeachersTable.TABLE_NAME, TEACHER_TABLE);
        uriMatcher.addURI(AUTHORITY, TeacherDataBase.TeacherSubjectTable.TABLE_NAME, TEACHER_SUBJECTS_TABLE);
        uriMatcher.addURI(AUTHORITY, TeacherDataBase.StudentTable.TABLE_NAME, STUDENT_TABLE);
        uriMatcher.addURI(AUTHORITY, TeacherDataBase.SubjectsTable.TABLE_NAME, SUBJECTS_TABLE);
        uriMatcher.addURI(AUTHORITY, TeacherDataBase.GradesTable.TABLE_NAME, GRADES_TABLE);
    }

    @Override
    public boolean onCreate() {
        database = new TeacherDataBase(getContext()).getWritableDatabase();
        return (database != null);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();


        switch (uriMatcher.match(uri)) {
            case THEMES_TABLE:
                Log.e("TAG", "theme " + String.valueOf(THEMES_TABLE));
                queryBuilder.setTables(TeacherDataBase.ThemeTable.TABLE_NAME);
                break;
            case GROUPS_TABLE:
                Log.e("TAG", String.valueOf(THEMES_TABLE));
                queryBuilder.setTables(TeacherDataBase.GroupsTable.TABLE_NAME);
                break;
            case LESSENS_TABLE:
                Log.e("TAG", String.valueOf(THEMES_TABLE));
                queryBuilder.setTables(TeacherDataBase.LessonsTable.TABLE_NAME);
                break;
            case GRADES_TABLE:
                Log.e("TAG", String.valueOf(THEMES_TABLE));
                queryBuilder.setTables(TeacherDataBase.GradesTable.TABLE_NAME);
                break;
            case TEACHER_TABLE:
                Log.e("TAG", String.valueOf(THEMES_TABLE));
                queryBuilder.setTables(TeacherDataBase.TeachersTable.TABLE_NAME);
                break;
            case TEACHER_SUBJECTS_TABLE:
                Log.e("TAG", String.valueOf(THEMES_TABLE));
                queryBuilder.setTables(TeacherDataBase.TeacherSubjectTable.TABLE_NAME);
                break;
            case STUDENT_TABLE:
                Log.e("TAG", String.valueOf(THEMES_TABLE));
                queryBuilder.setTables(TeacherDataBase.StudentTable.TABLE_NAME);
                break;
            case SUBJECTS_TABLE:
                Log.e("TAG", String.valueOf(THEMES_TABLE));
                queryBuilder.setTables(TeacherDataBase.SubjectsTable.TABLE_NAME);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        Cursor c = queryBuilder.query(database, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public String getType(Uri uri) {
        String type;
        switch (uriMatcher.match(uri)) {
            case THEMES_TABLE:
                type = TeacherDataBase.ThemeTable.TABLE_NAME;
                break;
            case GROUPS_TABLE:
                type = TeacherDataBase.GroupsTable.TABLE_NAME;
                break;
            case LESSENS_TABLE:
                type = TeacherDataBase.LessonsTable.TABLE_NAME;
                break;
            case GRADES_TABLE:
                type = TeacherDataBase.GradesTable.TABLE_NAME;
                break;
            case TEACHER_TABLE:
                type = TeacherDataBase.TeachersTable.TABLE_NAME;
                break;
            case TEACHER_SUBJECTS_TABLE:
                type = TeacherDataBase.TeacherSubjectTable.TABLE_NAME;
                break;
            case STUDENT_TABLE:
                type = TeacherDataBase.StudentTable.TABLE_NAME;
                break;
            case SUBJECTS_TABLE:
                type = TeacherDataBase.SubjectsTable.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return type;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowId;
        switch (uriMatcher.match(uri)) {
            case THEMES_TABLE:
                rowId = database.insert(TeacherDataBase.ThemeTable.TABLE_NAME, null, values);
                break;
            case GROUPS_TABLE:
                rowId = database.insert(TeacherDataBase.GroupsTable.TABLE_NAME, null, values);
                break;
            case LESSENS_TABLE:
                rowId = database.insert(TeacherDataBase.LessonsTable.TABLE_NAME, null, values);
                break;
            case GRADES_TABLE:
                rowId = database.insert(TeacherDataBase.GradesTable.TABLE_NAME, null, values);
                break;
            case TEACHER_TABLE:
                rowId = database.insert(TeacherDataBase.TeachersTable.TABLE_NAME, null, values);
                break;
            case TEACHER_SUBJECTS_TABLE:
                rowId = database.insert(TeacherDataBase.TeacherSubjectTable.TABLE_NAME, null, values);
                break;
            case STUDENT_TABLE:
                rowId = database.insert(TeacherDataBase.StudentTable.TABLE_NAME, null, values);
                break;
            case SUBJECTS_TABLE:
                rowId = database.insert(TeacherDataBase.SubjectsTable.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if (rowId <= 0) {
            try {
                throw new SQLException("Failed to insert row into " + uri);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            Uri url = ContentUris.withAppendedId(CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(url, null);

            return url;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int retVal;

        switch (uriMatcher.match(uri)) {
            case THEMES_TABLE:
                retVal = database.delete(TeacherDataBase.ThemeTable.TABLE_NAME, selection, selectionArgs);
                break;
            case GROUPS_TABLE:
                retVal = database.delete(TeacherDataBase.GroupsTable.TABLE_NAME, selection, selectionArgs);
                break;
            case LESSENS_TABLE:
                retVal = database.delete(TeacherDataBase.LessonsTable.TABLE_NAME, selection, selectionArgs);
                break;
            case GRADES_TABLE:
                retVal = database.delete(TeacherDataBase.GradesTable.TABLE_NAME, selection, selectionArgs);
                break;
            case TEACHER_TABLE:
                retVal = database.delete(TeacherDataBase.TeachersTable.TABLE_NAME, selection, selectionArgs);
                break;
            case TEACHER_SUBJECTS_TABLE:
                retVal = database.delete(TeacherDataBase.TeacherSubjectTable.TABLE_NAME, selection, selectionArgs);
                break;
            case STUDENT_TABLE:
                retVal = database.delete(TeacherDataBase.StudentTable.TABLE_NAME, selection, selectionArgs);
                break;
            case SUBJECTS_TABLE:
                retVal = database.delete(TeacherDataBase.SubjectsTable.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return retVal;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int retVal;
        switch (uriMatcher.match(uri)) {
            case THEMES_TABLE:
                retVal = database.update(TeacherDataBase.ThemeTable.TABLE_NAME, values, selection, selectionArgs);
                break;
            case GROUPS_TABLE:
                retVal = database.update(TeacherDataBase.GroupsTable.TABLE_NAME, values, selection, selectionArgs);
                break;
            case LESSENS_TABLE:
                retVal = database.update(TeacherDataBase.LessonsTable.TABLE_NAME, values, selection, selectionArgs);
                break;
            case GRADES_TABLE:
                retVal = database.update(TeacherDataBase.GradesTable.TABLE_NAME, values, selection, selectionArgs);
                break;
            case TEACHER_TABLE:
                retVal = database.update(TeacherDataBase.TeachersTable.TABLE_NAME, values, selection, selectionArgs);
                break;
            case TEACHER_SUBJECTS_TABLE:
                retVal = database.update(TeacherDataBase.TeacherSubjectTable.TABLE_NAME, values, selection, selectionArgs);
                break;
            case STUDENT_TABLE:
                retVal = database.update(TeacherDataBase.StudentTable.TABLE_NAME, values, selection, selectionArgs);
                break;
            case SUBJECTS_TABLE:
                retVal = database.update(TeacherDataBase.SubjectsTable.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return retVal;
    }
}