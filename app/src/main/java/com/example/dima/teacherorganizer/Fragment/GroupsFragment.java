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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.dima.teacherorganizer.Activity.LoginActivity;
import com.example.dima.teacherorganizer.Activity.TableActivity;
import com.example.dima.teacherorganizer.DataBase.TeacherDataBase;
import com.example.dima.teacherorganizer.Activity.GroupRegistration;
import com.example.dima.teacherorganizer.R;
import com.gc.materialdesign.views.ButtonFloat;

public class GroupsFragment extends Fragment implements AbsListView.OnItemClickListener,
        LoaderManager.LoaderCallbacks<Cursor>  {
    private OnFragmentInteractionListener mListener;

    private ListView mListView;
//    private GridView mGridView;
    private String[] from;


    private SimpleCursorAdapter mAdapter;
    private int[] to;
    private SQLiteDatabase database;
    private ButtonFloat addGroup;
    private Cursor cursor;


    public GroupsFragment() {
    }

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
        database = new TeacherDataBase(getActivity()).getReadableDatabase();
        cursor = database.query(TeacherDataBase.GroupsTable.TABLE_NAME,
                        new String[]{TeacherDataBase.GroupsTable.ID, TeacherDataBase.GroupsTable.GROUP_},
                TeacherDataBase.GroupsTable.ID_TEACHER + " = ? ", new String[]{LoginActivity.getIdTeacher()}, null, null, null, null);

        mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.item_list_fragment, cursor, from, to,0);
        mListView.setAdapter(mAdapter);
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
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.

            Log.e("Tag", "position "+String.valueOf(position)+" id "+String.valueOf(id));
            mListener.onFragmentInteraction(position);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {TeacherDataBase.GroupsTable.GROUP_};
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
