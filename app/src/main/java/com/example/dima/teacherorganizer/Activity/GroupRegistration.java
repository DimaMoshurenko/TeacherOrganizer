package com.example.dima.teacherorganizer.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.dima.teacherorganizer.DataBase.TeacherContentProvider;
import com.example.dima.teacherorganizer.DataBase.TeacherDataBase;
import com.example.dima.teacherorganizer.NavigationDrawer;
import com.example.dima.teacherorganizer.R;
import com.gc.materialdesign.views.ButtonFlat;
import com.rengwuxian.materialedittext.MaterialEditText;

import static com.example.dima.teacherorganizer.Activity.TeacherRegistration.setSettingMaterialEditText;


public class GroupRegistration extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_registration);
        final MaterialEditText nameGroup = (MaterialEditText) findViewById(R.id.new_name_group);
        final MaterialEditText nameKuratora = (MaterialEditText) findViewById(R.id.name_kuratora);
        final MaterialEditText kuratoraPhoneNumber = (MaterialEditText) findViewById(R.id.number_kuratora);
        setSettingMaterialEditText(nameGroup, getResources().getString(R.string.group), GroupRegistration.this);
        setSettingMaterialEditText(nameKuratora, getResources().getString(R.string.name_kuratora), GroupRegistration.this);
        setSettingMaterialEditText(kuratoraPhoneNumber, getResources().getString(R.string.number_kuratora), GroupRegistration.this);
        ButtonFlat addGroup = (ButtonFlat) findViewById(R.id.add_group);
        TeacherRegistration.NotEmptyValidator notEmptyValidator =
                new TeacherRegistration.NotEmptyValidator(getString(R.string.not_empty_warning));
        nameGroup.addValidator(notEmptyValidator);
        nameKuratora.addValidator(notEmptyValidator);
        kuratoraPhoneNumber.addValidator(notEmptyValidator);
        addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameGroup.validate() & kuratoraPhoneNumber.validate() & nameKuratora.validate()) {

                    ContentValues content = new ContentValues();
                    content.put(TeacherDataBase.GroupsTable.GROUP_, nameGroup.getText().toString());
                    content.put(TeacherDataBase.GroupsTable.NAME_KURATOTA, nameKuratora.getText().toString());
                    content.put(TeacherDataBase.GroupsTable.ID_TEACHER, LoginActivity.getIdTeacher());
                    content.put(TeacherDataBase.GroupsTable.NUMBER_KURATOTA, kuratoraPhoneNumber.getText().toString());
                    Uri uri = getContentResolver().insert(Uri.parse(TeacherContentProvider.CONTENT_URI + "/"
                            + TeacherDataBase.GroupsTable.TABLE_NAME),content);
                    int idGroup = Integer.valueOf(uri.getLastPathSegment());
                    Intent intent = new Intent(GroupRegistration.this, NavigationDrawer.class);
                    intent.putExtra(TeacherDataBase.GroupsTable.ID, idGroup);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_group_registration, menu);
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
