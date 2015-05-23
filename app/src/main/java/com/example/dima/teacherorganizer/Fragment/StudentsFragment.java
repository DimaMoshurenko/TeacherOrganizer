package com.example.dima.teacherorganizer.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.dima.teacherorganizer.DataBase.TeacherDataBase;
import com.example.dima.teacherorganizer.R;
import com.example.dima.teacherorganizer.RegistrationActivity.StudentRegistration;
import com.gc.materialdesign.views.ButtonFloat;

public class StudentsFragment extends Fragment implements AbsListView.OnItemClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private OnFragmentInteractionListener mListener;
    private ListView mListView;
    private SimpleCursorAdapter mAdapter;
    private SQLiteDatabase database;
    private int[] to;
    private String[] from;
    private ButtonFloat addTeacher;
    private String idTeacher;

    // TODO: Rename and change types of parameters
    public StudentsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.delete_db:
                database.delete(TeacherDataBase.TeachersTable.TABLE_NAME, null, null);
                database.delete(TeacherDataBase.SubjectsTable.TABLE_NAME, null, null);
                database.delete(TeacherDataBase.GroupsTable.TABLE_NAME, null, null);
                database.delete(TeacherDataBase.LessonsTable.TABLE_NAME, null, null);
                database.delete(TeacherDataBase.GradesTable.TABLE_NAME, null, null);
                database.delete(TeacherDataBase.TeacherSubjectTable.TABLE_NAME, null, null);
                database.delete(TeacherDataBase.ThemeTable.TABLE_NAME, null, null);
                database.delete(TeacherDataBase.StudentTable.TABLE_NAME, null, null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_main,menu);
//        MenuItem deleteItem = menu.findItem(R.id.delete_db);
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teachers_item, container, false);

//        database = new TeacherDataBase(getActivity()).getReadableDatabase();
//        Cursor cursor = database.query(TeacherDataBase.TeachersTable.TABLE_NAME,
//                null, null, null, null, null,null);



        from = new String[]{TeacherDataBase.StudentTable.STUDENT_NAME};
        to = new int[]{R.id.name_student_list};

        mListView = (ListView) view.findViewById(R.id.list_studets);



        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        database = new TeacherDataBase(getActivity()).getWritableDatabase();
        Cursor cursor = database.query(TeacherDataBase.StudentTable.TABLE_NAME,
                new String[]{TeacherDataBase.TeachersTable.ID, TeacherDataBase.StudentTable.STUDENT_NAME},
                null, null, null, null, null);
        idTeacher = getActivity().getIntent().getStringExtra(TeacherDataBase.TeachersTable.ID);
        mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.item_list_fragment, cursor, from, to, 0);
        mListView.setAdapter(mAdapter);
        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);
        addTeacher = (ButtonFloat) view.findViewById(R.id.float_button_groups);
        addTeacher.setBackgroundColor(getResources().getColor(R.color.color_primary));
        addTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), StudentRegistration.class);
                startActivityForResult(myIntent, 0);
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected
            // Открывать подробную и инфрмацию студента
            mListener.onFragmentInteraction("OK");
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {TeacherDataBase.StudentTable.STUDENT_NAME};
        return new CursorLoader(getActivity(), null, projection, null, null, null);
//        TeacherDataBase.TeachersTable.ID,
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (mAdapter == null) {
            mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.item_list_fragment, data, from, to, 0);
            if (mAdapter.getCount() == 0) {
//                emptyListTextView.setVisibility(View.VISIBLE);
//                gridView.setEmptyView(emptyListTextView);
            } else {
//                emptyListTextView.setVisibility(View.INVISIBLE);
                mListView.setAdapter(mAdapter);
            }
        } else {
            mAdapter.swapCursor(data);
//            mAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
// data is not available anymore, delete reference

        mAdapter.swapCursor(null);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}
