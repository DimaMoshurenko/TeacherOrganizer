package com.example.dima.teacherorganizer.Fragment;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.dima.teacherorganizer.DataBase.TeacherDataBase;
import com.example.dima.teacherorganizer.R;

public class SubjectsFragment extends Fragment implements AbsListView.OnItemClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private OnFragmentInteractionListener mListener;

    private ListView mGridView;

    private SimpleCursorAdapter mAdapter;
    private SQLiteDatabase database;
    private String[] from;
    private int[] to;

    public SubjectsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subjects_item, container, false);
        database = new TeacherDataBase(getActivity()).getWritableDatabase();
        Cursor cursor = database.query(TeacherDataBase.StudentTable.TABLE_NAME,
                new String[]{TeacherDataBase.TeachersTable.ID, TeacherDataBase.StudentTable.STUDENT_NAME},
                null, null, null, null, null);

        from = new String[]{TeacherDataBase.StudentTable.STUDENT_NAME};
        to = new int[]{R.id.name_student_list};
        mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.item_list_fragment, cursor, from, to,0);
        // Set the adapter
        mGridView = (ListView) view.findViewById(R.id.list_subjects);
        mGridView.setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mGridView.setOnItemClickListener(this);

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
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(position);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {TeacherDataBase.StudentTable.STUDENT_NAME};
        return new CursorLoader(getActivity(), null, projection, null, null, null);
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
                mGridView.setAdapter(mAdapter);
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(int position);
    }

}
