package com.example.dima.teacherorganizer.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dima.teacherorganizer.Activity.LoginActivity;
import com.example.dima.teacherorganizer.DataBase.TeacherContentProvider;
import com.example.dima.teacherorganizer.DataBase.TeacherDataBase;
import com.example.dima.teacherorganizer.R;
import com.gc.materialdesign.views.Button;
import com.gc.materialdesign.views.ButtonFlat;

/**

 */
public class SettingsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ButtonFlat clear;
    private ButtonFlat example;


    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View FragmentView = inflater.inflate(R.layout.fragment_settings, container, false);


        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        clear = (ButtonFlat) view.findViewById(R.id.cleacr_db);
        example = (ButtonFlat) view.findViewById(R.id.example);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TeacherDataBase db = new TeacherDataBase(getActivity());
                SQLiteDatabase database = db.getWritableDatabase();
                database.delete(TeacherDataBase.SubjectsTable.TABLE_NAME, null, null);
                database.delete(TeacherDataBase.GroupsTable.TABLE_NAME, null, null);
                database.delete(TeacherDataBase.LessonsTable.TABLE_NAME, null, null);
                database.delete(TeacherDataBase.GradesTable.TABLE_NAME, null, null);
                database.delete(TeacherDataBase.TeacherSubjectTable.TABLE_NAME, null, null);
                database.delete(TeacherDataBase.ThemeTable.TABLE_NAME, null, null);
                database.delete(TeacherDataBase.TeachersTable.TABLE_NAME, null, null);
                database.delete(TeacherDataBase.StudentTable.TABLE_NAME, null, null);
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        example.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity()," Okey ", Toast.LENGTH_SHORT).show();
                Cursor cursor = getActivity().getContentResolver().query(Uri.parse(
                                TeacherContentProvider.CONTENT_URI+"/"+ TeacherDataBase.ThemeTable.TABLE_NAME),
                        null,
                        null,
                        null,
                        null);


            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
