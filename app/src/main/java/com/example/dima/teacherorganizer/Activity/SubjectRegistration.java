package com.example.dima.teacherorganizer.Activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dima.teacherorganizer.DataBase.TeacherDataBase;
import com.example.dima.teacherorganizer.NavigationDrawer;
import com.example.dima.teacherorganizer.R;
import com.gc.materialdesign.views.ButtonFlat;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.RegexpValidator;

import java.util.ArrayList;
import java.util.List;

import static com.example.dima.teacherorganizer.Activity.TeacherRegistration.setSettingMaterialEditText;

public class SubjectRegistration extends ActionBarActivity {

    private SQLiteDatabase database;
    private List<ModelGroupsList> listGroups;
    private MaterialAutoCompleteTextView groups;
    private AdapterGroups adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = new TeacherDataBase(SubjectRegistration.this).getWritableDatabase();

        listGroups = new ArrayList<>();
        setContentView(R.layout.activity_subject_registration);
        database = new TeacherDataBase(this).getWritableDatabase();
        final MaterialEditText newSubject = (MaterialEditText) findViewById(R.id.new_subject);
        final MaterialEditText numberSubject = (MaterialEditText) findViewById(R.id.number_subject);
        ButtonFlat addSubject = (ButtonFlat) findViewById(R.id.add_subject);
        ListView groupsList = (ListView) findViewById(R.id.set_list_group_subjects);
        Cursor cursor = database.query(TeacherDataBase.GroupsTable.TABLE_NAME, new String[]{
                TeacherDataBase.GroupsTable.ID, TeacherDataBase.GroupsTable.GROUP_}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                listGroups.add(new ModelGroupsList(cursor.getString(cursor.getColumnIndex(TeacherDataBase.GroupsTable.GROUP_))));

            } while (cursor.moveToNext());
        }

        adapter = new AdapterGroups(this, listGroups);
//                new ArrayAdapter<String>(this, R.layout.item_list_group_subjects, R.id.groups_checkbox, listGroups);
        groupsList.setAdapter(adapter);
        setSettingMaterialEditText(newSubject, getString(R.string.new_subject), this);
        setSettingMaterialEditText(numberSubject, getString(R.string.number_subject), this);


        addSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOnClickListenerAddSubjects(newSubject, numberSubject);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_subject_registration, menu);
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

    private void setOnClickListenerAddSubjects(MaterialEditText newSubject, MaterialEditText numberSubject) {

        TeacherRegistration.NotEmptyValidator notEmptyValidator =
                new TeacherRegistration.NotEmptyValidator(getString(R.string.not_empty_warning));
        newSubject.addValidator(notEmptyValidator);
        if (newSubject.validate()) {
            ContentValues subject = new ContentValues();
            subject.put(TeacherDataBase.SubjectsTable.SUBJECT, newSubject.getText().toString());
            subject.put(TeacherDataBase.SubjectsTable.ID_TEACHER, LoginActivity.getIdTeacher());

            subject.put(TeacherDataBase.SubjectsTable.NUMBER_SUBJECT, numberSubject.getText().toString());
            final long idSubject = database.insert(TeacherDataBase.SubjectsTable.TABLE_NAME, null, subject);
            Log.e("TAG", "id idSubject " + idSubject);

            if (LoginActivity.getIdTeacher() != null) {

                List<ModelGroupsList> list = adapter.getListGroups();
                Boolean no = false;
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isChecked()) {
                        no = true;
                        Cursor cursor = database.query(TeacherDataBase.GroupsTable.TABLE_NAME,
                                new String[]{TeacherDataBase.GroupsTable.ID,
                                        TeacherDataBase.GroupsTable.GROUP_}, null, null, null, null, null);
                        cursor.moveToFirst();

                        if (cursor.moveToFirst()) {
                            do {
                                if (list.get(i).getNameGroup().equals(cursor.getString(
                                        cursor.getColumnIndex(TeacherDataBase.GroupsTable.GROUP_)))) {
                                    if (LoginActivity.getIdTeacher() != null) {
                                        ContentValues content = new ContentValues();
                                        content.put(TeacherDataBase.TeacherSubjectTable.TEACHER_ID,
                                                LoginActivity.getIdTeacher());
                                        content.put(TeacherDataBase.TeacherSubjectTable.SUBJECT_ID, idSubject);
                                        content.put(TeacherDataBase.TeacherSubjectTable.GROUP_ID, cursor.getString(
                                                cursor.getColumnIndex(TeacherDataBase.GroupsTable.ID)));
                                        database.insert(TeacherDataBase.TeacherSubjectTable.TABLE_NAME, null, content);
                                        Log.e("TAG", " add TeacherSubjectTable ");
                                        Log.i("TAG", "List groups " + String.valueOf(list.get(i).getNameGroup()) + " cursor " + cursor.getString(
                                                cursor.getColumnIndex(TeacherDataBase.GroupsTable.GROUP_)));
                                    }
                                }

                            } while (cursor.moveToNext());
                        }
                        cursor.close();
                    } else {
                        Toast.makeText(getApplication(), " Выберете группу ", Toast.LENGTH_LONG).show();
                    }
                }
                if (no) {
                    // testing
                    Intent intent = new Intent(SubjectRegistration.this, NavigationDrawer.class);
                    startActivity(intent);
                    finish();
                }


            } else {
                Log.e("TAG", "Error have not id teacher ");
            }
        }
    }

    private class ModelGroupsList {
        private Boolean checked;
        private String nameGroup;

        ModelGroupsList(String nameGroup) {
            this.nameGroup = nameGroup;
            checked = false;
        }

        public Boolean isChecked() {
            return checked;
        }

        public void setChecked(Boolean checked) {
            this.checked = checked;
        }

        public String getNameGroup() {
            return nameGroup;
        }

    }

    public static class AdapterGroups extends BaseAdapter {


        Activity context;
        List<Boolean> itemChecked;
        LayoutInflater inflater;
        List<ModelGroupsList> listGroups;

        public AdapterGroups(Activity context, List<ModelGroupsList> listGroups) {
            super();
            this.listGroups = listGroups;
            this.itemChecked = new ArrayList<>();
            this.context = context;
            this.inflater = context.getLayoutInflater();
        }

        private class ViewHolder {
            CheckBox checkBox;
        }

        public int getCount() {
            return listGroups.size();
        }

        public Object getItem(int position) {
            return listGroups.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            final ViewHolder holder;


            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_list_group_subjects, parent, false);
                holder = new ViewHolder();
                holder.checkBox = (CheckBox) convertView
                        .findViewById(R.id.groups_checkbox);
                convertView.setTag(holder);

            } else {

                holder = (ViewHolder) convertView.getTag();
            }

            if (listGroups.get(position) != null) {
                holder.checkBox.setText(listGroups.get(position).getNameGroup());
                holder.checkBox.refreshDrawableState();
                notifyDataSetChanged();
            }

            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (holder.checkBox.getText().equals(listGroups.get(position).getNameGroup())) {
                            listGroups.get(position).setChecked(true);
                        }
                    } else {
                        if (holder.checkBox.getText().equals(listGroups.get(position).getNameGroup())) {
                            listGroups.get(position).setChecked(false);
                        }
                    }
                }

            });
            return convertView;

        }

        public List<ModelGroupsList> getListGroups() {
            return listGroups;
        }

    }


}
