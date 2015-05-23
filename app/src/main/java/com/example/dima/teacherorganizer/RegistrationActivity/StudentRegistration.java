package com.example.dima.teacherorganizer.RegistrationActivity;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.dima.teacherorganizer.DataBase.TeacherDataBase;
import com.example.dima.teacherorganizer.NavigationDrawer;
import com.example.dima.teacherorganizer.R;
import com.gc.materialdesign.views.ButtonFlat;
import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.RegexpValidator;

import java.util.ArrayList;


public class StudentRegistration extends ActionBarActivity implements TextWatcher {
    private SQLiteDatabase database;
    private ArrayAdapter<String> adapter;
    ArrayList<String> listGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration);


        adapter = new ArrayAdapter<String>(getApplication(), R.layout.item_groups, R.id.group_auto_complete);
        final MaterialEditText name = (MaterialEditText) findViewById(R.id.new_name_student);
        final MaterialEditText middleName = (MaterialEditText) findViewById(R.id.new_middle_name_student);
        final MaterialEditText surname = (MaterialEditText) findViewById(R.id.new_surname_student);
        final MaterialEditText mail = (MaterialEditText) findViewById(R.id.new_student_mail);
        final MaterialAutoCompleteTextView group = (MaterialAutoCompleteTextView) findViewById(R.id.student_group);
        group.addTextChangedListener(this);
        final MaterialEditText phoneNumber = (MaterialEditText) findViewById(R.id.new_phone_number_student);
        ButtonFlat buttonFloat = (ButtonFlat) findViewById(R.id.add_student);

        buttonFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TeacherRegistration.NotEmptyValidator notEmptyValidator =
                        new TeacherRegistration.NotEmptyValidator(getString(R.string.not_empty_warning));
                name.addValidator(notEmptyValidator);
                middleName.addValidator(notEmptyValidator);
                surname.addValidator(notEmptyValidator);
                group.addValidator(notEmptyValidator);
                if (name.validate() & surname.validate() & middleName.validate() & group.validate()) {
                    ContentValues content = new ContentValues();
                    content.put(TeacherDataBase.StudentTable.STUDENT_NAME, surname.getText().toString() + " "
                            + name.getText().toString() + " " + middleName.getText().toString());
                    content.put(TeacherDataBase.StudentTable.TELEPHONE_NUMBER, phoneNumber.getText().toString());
                    content.put(TeacherDataBase.StudentTable.STUDENT_MAIL, mail.getText().toString());

                    if (group.getText().toString().length() > 0) {
                        Cursor cursor = database.query(TeacherDataBase.GroupsTable.TABLE_NAME,
                                new String[]{TeacherDataBase.GroupsTable.ID, TeacherDataBase.GroupsTable.GROUP_},
                                TeacherDataBase.GroupsTable.GROUP_ + " = ? ",
                                new String[]{group.getText().toString()}, null, null, null);
                        String idGroup = null;
                        boolean groupRepeat = false;
                        if (cursor.moveToFirst()) {
                            do {
                                if (cursor.getString(cursor.getColumnIndex(TeacherDataBase.GroupsTable.GROUP_))
                                        .equals(group.getText().toString())) {
                                    idGroup = cursor.getString(cursor.getColumnIndex(TeacherDataBase.GroupsTable.ID));

                                    if (group.getText().toString().equals(
                                            cursor.getString(cursor.getColumnIndex(TeacherDataBase.GroupsTable.GROUP_)))) {
                                        /// сделать валидацию по групах что такая група не существует
                                        groupRepeat = true;
                                    }
                                    //result[0] = true;
                                    break;
                                }
                            } while (cursor.moveToNext());
                        }

                        if (groupRepeat) {
                            if (idGroup != null) {
                                try {
                                    content.put(TeacherDataBase.StudentTable._ID_GROUP, idGroup);
                                    TeacherDataBase db = new TeacherDataBase(StudentRegistration.this);
                                    database = db.getWritableDatabase();
                                    long idStudent = database.insert(TeacherDataBase.StudentTable.TABLE_NAME, null, content);
                                    Intent intent = new Intent(StudentRegistration.this, NavigationDrawer.class);
                                    intent.putExtra(TeacherDataBase.StudentTable.ID, idStudent);
                                    startActivity(intent);
                                    database.close();
                                    finish();
                                }catch (Exception e){
                                    Toast.makeText(getApplication(), " Не вышло добавить студента попробуйте еще раз ", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getApplication(), " Не вышло добавить студента ", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            group.validateWith(new RegexpValidator(
                                    getResources().getString(R.string.group_not_repeat_error_message), " "));
                        }
                    }
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_student_registretion, menu);
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        Cursor cursor = database.query(TeacherDataBase.GroupsTable.TABLE_NAME,
                new String[]{TeacherDataBase.GroupsTable.ID, TeacherDataBase.GroupsTable.GROUP_},
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                listGroups.add(cursor.getString(cursor.getColumnIndex(TeacherDataBase.GroupsTable.GROUP_)));

            } while (cursor.moveToNext());
        }
        if (listGroups != null) {
            adapter.addAll(listGroups);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
