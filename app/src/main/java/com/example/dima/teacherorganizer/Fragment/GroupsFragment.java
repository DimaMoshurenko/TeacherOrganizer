package com.example.dima.teacherorganizer.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
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

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.dima.teacherorganizer.Activity.GroupRegistration;
import com.example.dima.teacherorganizer.DataBase.TeacherContentProvider;
import com.example.dima.teacherorganizer.DataBase.TeacherDataBase;
import com.example.dima.teacherorganizer.InformatoinActivity.GroupInformationActivity;
import com.example.dima.teacherorganizer.R;
import com.gc.materialdesign.views.ButtonFloat;

public class GroupsFragment extends Fragment implements AbsListView.OnItemClickListener,
        LoaderManager.LoaderCallbacks<Cursor>, AbsListView.OnItemLongClickListener {
    private OnFragmentInteractionListener mListener;

    private ListView mListView;
    private String[] from;


    private SimpleCursorAdapter mAdapter;
    private int[] to;
    private ButtonFloat addGroup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groups_item, container, false);

        from = new String[]{TeacherDataBase.GroupsTable.GROUP_};
        to = new int[]{R.id.other_name};
        // Set the adapter
        mListView = (ListView) view.findViewById(R.id.list_groups);
        addGroup = (ButtonFloat) view.findViewById(R.id.float_button_groups);
//        getActivity().getSupportLoaderManager().initLoader(0, null, this);
        // Set OnItemClickListener so we can be notified on item clicks
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView.setOnItemClickListener(this);

        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);
        addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), GroupRegistration.class);
                startActivity(myIntent);

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

            TextView group = (TextView) view.findViewById(R.id.other_name);
            Log.e("TAG", "group " + String.valueOf(group.getText()));
            Intent myIntent = new Intent(getActivity(), GroupInformationActivity.class);
            Cursor cursor = getActivity().getContentResolver().query(Uri.parse(TeacherContentProvider.CONTENT_URI + "/" +
                            TeacherDataBase.GroupsTable.TABLE_NAME), new String[]{TeacherDataBase.GroupsTable.GROUP_, TeacherDataBase.GroupsTable.ID},
                    TeacherDataBase.GroupsTable.GROUP_ + " = " + group.getText().toString(), null, null);

            String idGroup = null;
            if (cursor.moveToFirst()) {
                do {
                    idGroup = cursor.getString(cursor.getColumnIndex(TeacherDataBase.GradesTable.ID));
                } while (cursor.moveToNext());
            }
            mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.item_list_fragment, cursor, from, to, 0);
            mListView.setAdapter(mAdapter);
            myIntent.putExtra(GroupInformationActivity.ID_GROUP, idGroup);
            startActivity(myIntent);
            mListener.onFragmentInteraction(position);
            Log.e("Tag", "position " + String.valueOf(position) + " id " + String.valueOf(id));
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            TextView group = (TextView) view.findViewById(R.id.other_name);

            Cursor cursor = getActivity().getContentResolver().query(Uri.parse(TeacherContentProvider.CONTENT_URI + "/" +
                            TeacherDataBase.GroupsTable.TABLE_NAME), new String[]{TeacherDataBase.GroupsTable.GROUP_,
                            TeacherDataBase.GroupsTable.ID},
                    TeacherDataBase.GroupsTable.GROUP_ + " = " + group.getText().toString(), null, null);
            String idGroup = null;
            String nameGroup = null;
            if (cursor.moveToFirst()) {
                do {
                    idGroup = cursor.getString(cursor.getColumnIndex(TeacherDataBase.GroupsTable.ID));
                    nameGroup = cursor.getString(cursor.getColumnIndex(TeacherDataBase.GroupsTable.GROUP_));
                } while (cursor.moveToNext());
            }

            final String finalIdGroup = idGroup;
            new MaterialDialog.Builder(getActivity())
                    .title(R.string.delete_group)
                    .content(nameGroup)
                    .positiveText(R.string.delete)
                    .negativeText(R.string.okey)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            try {
                                getActivity().getContentResolver().delete(Uri.parse(TeacherContentProvider.CONTENT_URI + "/"
                                                + TeacherDataBase.GroupsTable.TABLE_NAME),
                                        TeacherDataBase.GroupsTable.ID + " = ?", new String[]{finalIdGroup});

                                getActivity().getContentResolver().delete(Uri.parse(TeacherContentProvider.CONTENT_URI + "/"
                                                + TeacherDataBase.TeacherSubjectTable.TABLE_NAME),
                                        TeacherDataBase.TeacherSubjectTable.GROUP_ID + " = ?", new String[]{finalIdGroup});

                                getActivity().getContentResolver().delete(Uri.parse(TeacherContentProvider.CONTENT_URI + "/"
                                                + TeacherDataBase.StudentTable.TABLE_NAME),
                                        TeacherDataBase.StudentTable._ID_GROUP + " = ?", new String[]{finalIdGroup});



                                getActivity().getContentResolver().delete(Uri.parse(TeacherContentProvider.CONTENT_URI + "/"
                                                + TeacherDataBase.ThemeTable.TABLE_NAME),
                                        TeacherDataBase.ThemeTable.ID_GROUP + " = ?", new String[]{finalIdGroup});

                                getActivity().getContentResolver().delete(Uri.parse(TeacherContentProvider.CONTENT_URI + "/"
                                                + TeacherDataBase.LessonsTable.TABLE_NAME),
                                        TeacherDataBase.LessonsTable._ID_GROUP + " = ?", new String[]{finalIdGroup});



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
            // Открывать подробную и инфрмацию студента
            mListener.onFragmentInteraction(position);
        }
        return false;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {TeacherDataBase.GroupsTable.GROUP_};
        return new CursorLoader(getActivity(),
                Uri.parse(TeacherContentProvider.CONTENT_URI + "/" + TeacherDataBase.GroupsTable.TABLE_NAME),
                projection, null, null, null);
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
        loader.notify();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(int position);
    }

}
