package com.example.dima.teacherorganizer.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.dima.teacherorganizer.Activity.LoginActivity;
import com.example.dima.teacherorganizer.Activity.SubjectsTeacherActivity;
import com.example.dima.teacherorganizer.DataBase.TeacherContentProvider;
import com.example.dima.teacherorganizer.DataBase.TeacherDataBase;
import com.example.dima.teacherorganizer.R;
import com.example.dima.teacherorganizer.Activity.SubjectRegistration;
import com.gc.materialdesign.views.ButtonFloat;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;

import java.util.ArrayList;
import java.util.List;

public class SubjectsFragment extends Fragment implements AbsListView.OnItemClickListener,
        LoaderManager.LoaderCallbacks<Cursor>, AbsListView.OnItemLongClickListener {

    private OnFragmentInteractionListener mListener;
    private ListView mListView;
    private SimpleCursorAdapter mAdapter;
    private String[] from;
    private int[] to;
    private ButtonFloat addSubject;

    public SubjectsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subjects_item, container, false);
        addSubject = (ButtonFloat) view.findViewById(R.id.float_button_subjects);
        Cursor cursor = getActivity().getContentResolver().query(Uri.parse(TeacherContentProvider.CONTENT_URI + "/" +
                        TeacherDataBase.SubjectsTable.TABLE_NAME), new String[]{TeacherDataBase.SubjectsTable.ID, TeacherDataBase.SubjectsTable.SUBJECT,
                        TeacherDataBase.SubjectsTable.NUMBER_SUBJECT, TeacherDataBase.SubjectsTable.ID_TEACHER},
                TeacherDataBase.GroupsTable.ID_TEACHER + " = ? ", new String[]{LoginActivity.getIdTeacher()}, null, null);

        from = new String[]{TeacherDataBase.SubjectsTable.SUBJECT, TeacherDataBase.SubjectsTable.NUMBER_SUBJECT};
        to = new int[]{R.id.teacher_subjects, R.id.number_list_teacher_subject};
        mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.item_list_subjects_fragment, cursor, from, to, 0);
        // Set the adapter
        mListView = (ListView) view.findViewById(R.id.list_subjects);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemLongClickListener(this);
        addSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cursor cursor = getActivity().getContentResolver().query(Uri.parse(TeacherContentProvider.CONTENT_URI + "/" +
                                TeacherDataBase.GroupsTable.TABLE_NAME), new String[]{TeacherDataBase.GroupsTable.ID, TeacherDataBase.GroupsTable.ID_TEACHER},
                        TeacherDataBase.GroupsTable.ID_TEACHER + " = ? ", new String[]{LoginActivity.getIdTeacher()}, null, null);

                cursor.moveToLast();
                if (LoginActivity.getIdTeacher() != null && cursor.getCount() > 0) {
                    Intent myIntent = new Intent(getActivity(), SubjectRegistration.class);
                    startActivity(myIntent);
                } else {
                    Toast.makeText(getActivity(), "Добавте сначала группу прежде чем добавить предмет!", Toast.LENGTH_SHORT).show();
                }
                cursor.close();

            }
        });
        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);
        return view;
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
            TextView subject = (TextView) view.findViewById(R.id.teacher_subjects);
            Log.e("TAG", "subject " + String.valueOf(subject.getText()));
            Intent myIntent = new Intent(getActivity(), SubjectsTeacherActivity.class);
            Cursor cursor = getActivity().getContentResolver().query(Uri.parse(TeacherContentProvider.CONTENT_URI + "/" +
                            TeacherDataBase.SubjectsTable.TABLE_NAME), new String[]{TeacherDataBase.SubjectsTable.SUBJECT, TeacherDataBase.SubjectsTable.ID},
                    TeacherDataBase.SubjectsTable.SUBJECT + " = ? ", new String[]{subject.getText().toString()}, null, null);

            String idSubjects = "";
            if (cursor.moveToFirst()) {
                do {
                    idSubjects = cursor.getString(cursor.getColumnIndex(TeacherDataBase.SubjectsTable.ID));

                } while (cursor.moveToNext());
            }

            myIntent.putExtra(SubjectsTeacherActivity.ID_SUBJECT, idSubjects);
            startActivity(myIntent);
            
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(position);

        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {TeacherDataBase.SubjectsTable.SUBJECT};
        return new CursorLoader(getActivity(), null, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (mAdapter == null) {
            mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.item_list_subjects_fragment, data, from, to, 0);
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
        mAdapter.swapCursor(null);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            TextView group = (TextView) view.findViewById(R.id.teacher_subjects);

            Cursor cursor = getActivity().getContentResolver().query(Uri.parse(TeacherContentProvider.CONTENT_URI + "/" +
                            TeacherDataBase.SubjectsTable.TABLE_NAME), new String[]{TeacherDataBase.SubjectsTable.ID, TeacherDataBase.SubjectsTable.ID_TEACHER, TeacherDataBase.SubjectsTable.SUBJECT},
                    TeacherDataBase.SubjectsTable.SUBJECT + " =  '" + group.getText().toString() + "' and " +
                            TeacherDataBase.SubjectsTable.ID_TEACHER + " = " + LoginActivity.getIdTeacher(), null, null);
            String idSubject = null;
            String nameSubject = null;
            if (cursor.moveToFirst()) {
                do {
                    idSubject = cursor.getString(cursor.getColumnIndex(TeacherDataBase.SubjectsTable.ID));
                    nameSubject = cursor.getString(cursor.getColumnIndex(TeacherDataBase.SubjectsTable.SUBJECT));
                } while (cursor.moveToNext());
            }
            cursor.close();
            final String finalIdSubject = idSubject;
            new MaterialDialog.Builder(getActivity())
                    .title(R.string.delete_group)
                    .content(nameSubject)
                    .positiveText(R.string.delete)
                    .negativeText(R.string.okey)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            try {
                                getActivity().getContentResolver().delete(Uri.parse(TeacherContentProvider.CONTENT_URI + "/" +
                                                TeacherDataBase.SubjectsTable.TABLE_NAME),
                                        TeacherDataBase.SubjectsTable.ID + " = ?", new String[]{finalIdSubject});

                                getActivity().getContentResolver().delete(Uri.parse(TeacherContentProvider.CONTENT_URI + "/" +
                                                TeacherDataBase.TeacherSubjectTable.TABLE_NAME),TeacherDataBase.TeacherSubjectTable.SUBJECT_ID + " = " + finalIdSubject +
                                        " and " + TeacherDataBase.TeacherSubjectTable.TEACHER_ID + " = " + LoginActivity.getIdTeacher(), null);

                                getActivity().getContentResolver().delete(Uri.parse(TeacherContentProvider.CONTENT_URI + "/" +
                                                TeacherDataBase.ThemeTable.TABLE_NAME),
                                        TeacherDataBase.ThemeTable.ID_SUBJECT + " = ?", new String[]{finalIdSubject});
                                mListView.notifyAll();
                                mListView.deferNotifyDataSetChanged();
                                dialog.dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            dialog.dismiss();
                        }
                    }).show();

            mListener.onFragmentInteraction(position);
        }
        return false;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(int position);
    }

}
