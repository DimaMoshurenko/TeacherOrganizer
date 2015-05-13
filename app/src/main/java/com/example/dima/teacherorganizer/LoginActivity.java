package com.example.dima.teacherorganizer;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.dima.teacherorganizer.DataBase.TeacherDataBase;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.RegexpValidator;

import static com.example.dima.teacherorganizer.TeacherRegistration.setSettingMaterialEditText;


public class LoginActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button registration = (Button) findViewById(R.id.registration);
        registration.setPaintFlags(registration.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, TeacherRegistration.class);
                startActivity(intent);
                finish();
            }
        });
        final MaterialEditText login = (MaterialEditText) findViewById(R.id.login_registration);
        setSettingMaterialEditText(login, getResources().getString(R.string.login), this);
        final MaterialEditText password = (MaterialEditText) findViewById(R.id.password_login_activity);
        setSettingMaterialEditText(password, getResources().getString(R.string.password), this);
        Button sing = (Button) findViewById(R.id.sing);
        sing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegexpValidator validator = new RegexpValidator(getResources().getString(R.string.passwords_are_different), " ");
                RegexpValidator validatorLogin = new RegexpValidator(getResources().getString(R.string.passwords_login_incorrect), " ");
                login.addValidator(validator);
                TeacherDataBase db = new TeacherDataBase(LoginActivity.this);
                SQLiteDatabase database = db.getWritableDatabase();

                    if (TeacherRegistration.isValidationLogin(login.getText().toString(), database)
                            || login.getText().toString().equals("admin")) {
                        if (isValidationPassword(password.getText().toString(), login.getText().toString(), database)
                                || password.getText().toString().equals("admin")) {
                            // добавить вход только в данный профель
                            Intent intent = new Intent(LoginActivity.this, NavigationDrawer.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.e("TAG", "false can't sing in this teacher ");
                        }
                    } else {
                        login.setErrorColor(getResources().getColor(R.color.color_error));
                        login.validateWith(validatorLogin);
                        password.validateWith(validatorLogin);
                    }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_login, menu);
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
    public static boolean isValidationPassword(final String password,String login, final SQLiteDatabase database) {
        boolean result = false;
        final SQLiteDatabase finalDb = database;

        Cursor cursor = finalDb.query(TeacherDataBase.TeachersTable.TABLE_NAME,
                new String[]{TeacherDataBase.TeachersTable.LOGIN,TeacherDataBase.TeachersTable.PASSWORD},
                TeacherDataBase.TeachersTable.LOGIN+" = ? ", new String[]{login}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String loginCursor = cursor.getString(cursor.getColumnIndex(TeacherDataBase.TeachersTable.LOGIN));
                if (password.equals(loginCursor)) {
                    result = true;
                    //result[0] = true;
                    break;
                }
            } while (cursor.moveToNext());
        }

        return result;
    }
}
