package com.example.dima.teacherorganizer.RegistrationActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.dima.teacherorganizer.DataBase.TeacherDataBase;
import com.example.dima.teacherorganizer.NavigationDrawer;
import com.example.dima.teacherorganizer.R;
import com.gc.materialdesign.views.ButtonFlat;
import com.rengwuxian.materialedittext.MaterialEditText;


public class GroupRegistration extends ActionBarActivity {

    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_registration);
        final MaterialEditText nameGroup = (MaterialEditText) findViewById(R.id.new_name_group);
        final MaterialEditText nameKuratora = (MaterialEditText) findViewById(R.id.name_kuratora);
        final MaterialEditText kuratoraPhoneNumber = (MaterialEditText) findViewById(R.id.number_kuratora);
        ButtonFlat addGroup = (ButtonFlat) findViewById(R.id.add_group);
        TeacherRegistration.NotEmptyValidator notEmptyValidator =
                new TeacherRegistration.NotEmptyValidator(getString(R.string.not_empty_warning));
        nameGroup.addValidator(notEmptyValidator);
        nameKuratora.addValidator(notEmptyValidator);
        kuratoraPhoneNumber.addValidator(notEmptyValidator);
        addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameGroup.validate() & kuratoraPhoneNumber.validate() & nameKuratora.validate())  {
                    TeacherDataBase db = new TeacherDataBase(GroupRegistration.this);
                    database = db.getWritableDatabase();
                    ContentValues content = new ContentValues();
                    content.put(TeacherDataBase.GroupsTable.GROUP_,nameGroup.getText().toString());
                    content.put(TeacherDataBase.GroupsTable.NAME_KURATOTA,nameKuratora.getText().toString());
                    content.put(TeacherDataBase.GroupsTable.NUMBER_KURATOTA,kuratoraPhoneNumber.getText().toString());
                    long idGroup = database.insert(TeacherDataBase.GroupsTable.TABLE_NAME,null,content);
                    Intent intent = new Intent(GroupRegistration.this, NavigationDrawer.class);
                    intent.putExtra(TeacherDataBase.GradesTable.ID, idGroup);
                    startActivity(intent);
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
