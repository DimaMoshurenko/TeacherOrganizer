package com.example.dima.teacherorganizer;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.dima.teacherorganizer.DataBase.TeacherDataBase;
import com.gc.materialdesign.views.ButtonFlat;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.METValidator;
import com.rengwuxian.materialedittext.validation.RegexpValidator;


public class TeacherRegistration extends ActionBarActivity {
    private static final int FLOAT_LABEL_TEXT_SIZE = 20;
    private static final int FLOAT_LABEL = 2;
    private SQLiteDatabase database;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class NotEmptyValidator extends METValidator {

        public NotEmptyValidator(String errorMessage) {
            super(errorMessage);

        }

        @Override
        public boolean isValid(CharSequence charSequence, boolean b) {
            return !TextUtils.isEmpty(charSequence);

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_teacher);
        final MaterialEditText teacherName = (MaterialEditText) findViewById(R.id.name_teacher);
        setSettingMaterialEditText(teacherName, getResources().getString(R.string.name_teacher));

        final MaterialEditText surname = (MaterialEditText) findViewById(R.id.surname);
        setSettingMaterialEditText(surname, getResources().getString(R.string.surname));

        final MaterialEditText middleName = (MaterialEditText) findViewById(R.id.middle_name);
        setSettingMaterialEditText(middleName, getResources().getString(R.string.middle_name));

        final MaterialEditText login = (MaterialEditText) findViewById(R.id.login);
        setSettingMaterialEditText(login, getResources().getString(R.string.login));

        final MaterialEditText password = (MaterialEditText) findViewById(R.id.password);
        setSettingMaterialEditText(password, getResources().getString(R.string.password));

        final MaterialEditText passwordRepeat = (MaterialEditText) findViewById(R.id.password_repeat);
        setSettingMaterialEditText(passwordRepeat, getResources().getString(R.string.password_repeat));


        ButtonFlat addTeacher = (ButtonFlat) findViewById(R.id.add_teacher);

        addTeacher.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final NotEmptyValidator notEmpty = new NotEmptyValidator(getString(R.string.not_empty_warning));

                password.addValidator(notEmpty);
                passwordRepeat.addValidator(notEmpty);
                login.addValidator(notEmpty);
                teacherName.addValidator(notEmpty);
                surname.addValidator(notEmpty);
                middleName.addValidator(notEmpty);
                RegexpValidator validator = new RegexpValidator(getResources().getString(R.string.passwords_are_different), " ");
                RegexpValidator validatorLogin = new RegexpValidator(getResources().getString(R.string.login_are_different), " ");
//                passwordRepeat.validate(" ", getResources().getString(R.string.passwords_are_different));
                if (password.validate() & passwordRepeat.validate() & login.validate() & teacherName.validate()
                        & surname.validate() & middleName.validate()) {
                    if (!password.getText().toString().equals(passwordRepeat.getText().toString())) {
                        password.validateWith(validator);
                        passwordRepeat.validateWith(validator);
                    } else {
                        TeacherDataBase db = new TeacherDataBase(TeacherRegistration.this);
                        database = db.getWritableDatabase();

                        if (!isValidationLogin(login.getText().toString(), database)) {


                            ContentValues contentValues = new ContentValues();
                            contentValues.put(TeacherDataBase.TeachersTable.TEACHER_NAME, surname.getText().toString() + " "
                                    + teacherName.getText().toString() + " " + middleName.getText().toString());
                            contentValues.put(TeacherDataBase.TeachersTable.LOGIN, login.getText().toString());
                            contentValues.put(TeacherDataBase.TeachersTable.PASSWORD, password.getText().toString());


                            database.insert(TeacherDataBase.TeachersTable.TABLE_NAME, null, contentValues);
                            database.close();
                            Log.e("TAG", " add new teacher ");
                            Intent intent = new Intent(TeacherRegistration.this, NavigationDrawer.class);
                            startActivity(intent);
                            finish();
                        } else {
                            login.validateWith(validatorLogin);
                        }
                    }
                }

            }


        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration_teacher, menu);
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

    public static boolean isValidationLogin(final String login, final SQLiteDatabase database) {
        boolean result = false;
        final SQLiteDatabase finalDb = database;

        Cursor cursor = finalDb.query(TeacherDataBase.TeachersTable.TABLE_NAME,
                new String[]{TeacherDataBase.TeachersTable.LOGIN},
                null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String loginCursor = cursor.getString(cursor.getColumnIndex(TeacherDataBase.TeachersTable.LOGIN));
                if (login.equals(loginCursor)) {
                    result = true;
                    //result[0] = true;
                    break;
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        return result;
    }

    public static void setSettingMaterialEditText(MaterialEditText text, String nextName, Context context) {
        text.setPrimaryColor(context.getResources().getColor(R.color.color_primary_dark));
        text.setUnderlineColor(context.getResources().getColor(R.color.color_primary_dark));
        text.setFloatingLabel(FLOAT_LABEL);
        text.setFloatingLabelText(nextName);
        text.setFloatingLabelTextSize(FLOAT_LABEL_TEXT_SIZE);
    }

    private void setSettingMaterialEditText(MaterialEditText text, String nextName) {

        text.setPrimaryColor(getResources().getColor(R.color.color_primary));
        text.setFloatingLabel(FLOAT_LABEL);
        text.setFloatingLabelText(nextName);
        text.setFloatingLabelTextSize(FLOAT_LABEL_TEXT_SIZE);

    }
}
