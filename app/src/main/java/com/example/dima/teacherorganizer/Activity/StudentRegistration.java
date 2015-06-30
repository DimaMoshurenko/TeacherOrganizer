package com.example.dima.teacherorganizer.Activity;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.dima.teacherorganizer.DataBase.TeacherContentProvider;
import com.example.dima.teacherorganizer.DataBase.TeacherDataBase;
import com.example.dima.teacherorganizer.NavigationDrawer;
import com.example.dima.teacherorganizer.R;
import com.gc.materialdesign.views.ButtonFlat;
import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.RegexpValidator;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.dima.teacherorganizer.Activity.TeacherRegistration.setSettingMaterialEditText;


public class StudentRegistration extends ActionBarActivity implements TextWatcher {

    private ArrayAdapter<String> adapter;
    ArrayList<String> listGroups;
    private MaterialAutoCompleteTextView group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration);

        listGroups = new ArrayList<>();
        final MaterialEditText name = (MaterialEditText) findViewById(R.id.new_name_student);
        final MaterialEditText middleName = (MaterialEditText) findViewById(R.id.new_middle_name_student);
        final MaterialEditText surname = (MaterialEditText) findViewById(R.id.new_surname_student);
        final MaterialEditText mail = (MaterialEditText) findViewById(R.id.new_student_mail);
        final MaterialEditText phoneNumber = (MaterialEditText) findViewById(R.id.new_phone_number_student);

        group = (MaterialAutoCompleteTextView) findViewById(R.id.student_group);
        ButtonFlat buttonFloat = (ButtonFlat) findViewById(R.id.add_student);

        setSettingMaterialEditText(name, getString(R.string.name), StudentRegistration.this);
        setSettingMaterialEditText(middleName, getString(R.string.middle_name), StudentRegistration.this);
        setSettingMaterialEditText(surname, getString(R.string.surname), StudentRegistration.this);
        setSettingMaterialEditText(mail, getString(R.string.mail), StudentRegistration.this);
        setSettingMaterialEditText(phoneNumber, getString(R.string.phone_number), StudentRegistration.this);

        group.setPrimaryColor(getResources().getColor(R.color.color_primary_dark));
        group.setUnderlineColor(getResources().getColor(R.color.color_primary_dark));
        group.setFloatingLabel(TeacherRegistration.FLOAT_LABEL);
        group.setFloatingLabelText(getString(R.string.group));
        group.setFloatingLabelTextSize(TeacherRegistration.FLOAT_LABEL_TEXT_SIZE);

        group.addTextChangedListener(this);

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

//
//                        Cursor cursor = database.query(TeacherDataBase.GroupsTable.TABLE_NAME,
//                                new String[]{TeacherDataBase.GroupsTable.ID, TeacherDataBase.GroupsTable.GROUP_},
//                                TeacherDataBase.GroupsTable.GROUP_ + " = ? ",
//                                new String[]{group.getText().toString()}, null, null, null);
                        Cursor cursor = getContentResolver().query(Uri.parse(TeacherContentProvider.CONTENT_URI + "/"
                                        + TeacherDataBase.GroupsTable.TABLE_NAME), new String[]{TeacherDataBase.GroupsTable.ID, TeacherDataBase.GroupsTable.GROUP_},
                                TeacherDataBase.GroupsTable.GROUP_ + " = ? ",
                                new String[]{group.getText().toString()}, null);
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
                        cursor.close();
                        if (groupRepeat) {
                            if (idGroup != null) {
                                try {
                                    content.put(TeacherDataBase.StudentTable._ID_GROUP, idGroup);
                                    content.put(TeacherDataBase.StudentTable._ID_TEACHER, LoginActivity.getIdTeacher());

                                    getContentResolver().insert(Uri.parse(TeacherContentProvider.CONTENT_URI + "/"
                                            + TeacherDataBase.StudentTable.TABLE_NAME), content);
                                    Log.e("TAG", "studetn registration " + idGroup);
                                    Intent intent = new Intent(StudentRegistration.this, NavigationDrawer.class);
                                    startActivity(intent);
                                    finish();
                                } catch (Exception e) {
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


    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        Cursor cursor = getContentResolver().query(Uri.parse(TeacherContentProvider.CONTENT_URI+"/"+TeacherDataBase.GroupsTable.TABLE_NAME),
                new String[]{TeacherDataBase.GroupsTable.ID, TeacherDataBase.GroupsTable.GROUP_},null,null,null);

        int repetition = 0;
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(cursor.getColumnIndex(TeacherDataBase.GroupsTable.GROUP_)) != null) {


                    if (listGroups.size() > 0) {
                        for (int c = 0; c < listGroups.size(); c++) {
                            if (cursor.getString(cursor.getColumnIndex(TeacherDataBase.GroupsTable.GROUP_)).equals(listGroups.get(c))) {
                                repetition++;
                            }
                        }
                        if (repetition == 0) {
                            listGroups.add(cursor.getString(cursor.getColumnIndex(TeacherDataBase.GroupsTable.GROUP_)));
                        }
                    } else {
                        listGroups.add(cursor.getString(cursor.getColumnIndex(TeacherDataBase.GroupsTable.GROUP_)));
                    }

                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        Collections.sort(listGroups);
        adapter = new ArrayAdapter<>(StudentRegistration.this, R.layout.item_groups, R.id.group_auto_complete, listGroups);
        group.setAdapter(adapter);

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
