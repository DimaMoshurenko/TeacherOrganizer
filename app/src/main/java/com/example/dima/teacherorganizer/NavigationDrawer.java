package com.example.dima.teacherorganizer;


import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.dima.teacherorganizer.Activity.LoginActivity;
import com.example.dima.teacherorganizer.DataBase.TeacherDataBase;
import com.example.dima.teacherorganizer.Fragment.GroupsFragment;
import com.example.dima.teacherorganizer.Fragment.SettingsFragment;
import com.example.dima.teacherorganizer.Fragment.StudentsFragment;
import com.example.dima.teacherorganizer.Fragment.SubjectsFragment;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Badgeable;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

public class NavigationDrawer extends ActionBarActivity implements StudentsFragment.OnFragmentInteractionListener,
        Drawer.OnDrawerItemClickListener, SettingsFragment.OnFragmentInteractionListener, GroupsFragment.OnFragmentInteractionListener,
        SubjectsFragment.OnFragmentInteractionListener {

    private Drawer.Result drawerResult = null;
    private final String TAG = "TAG";
    private static final int GROUPS = 0;
    private static final int SUBJECTS = 1;
    private static final int STUDENTS = 2;
    private static final int SETTING = 4;
    private static final int EXIT = 5;
    private String idTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        idTeacher = LoginActivity.getIdTeacher();
        /*
Инициализируем Toolbar
Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
setSupportActionBar(toolbar);
*/
//        getSupportActionBar().setTitle(getResources().getString(R.string.students));

        // Инициализируем Navigation Drawer
        Drawer navigationDrawer = new Drawer();

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, new GroupsFragment())
                .commit();
        navigationDrawer.withActivity(this);
//        navigationDrawer.withToolbar(toolbar);
        navigationDrawer.withActionBarDrawerToggle(true);
        navigationDrawer.withHeader(R.layout.drawer_image);
        navigationDrawer.addDrawerItems(

//                new PrimaryDrawerItem().withName(R.string.teachers)
//                        .withIcon(FontAwesome.Icon.faw_user),

                new PrimaryDrawerItem().withName(R.string.groups)
                        .withIcon(FontAwesome.Icon.faw_group),
                new PrimaryDrawerItem().withName(R.string.subjects)
                        .withIcon(FontAwesome.Icon.faw_book),
                new PrimaryDrawerItem().withName(R.string.students)
                        .withIcon(getResources().getDrawable(R.drawable.ic_account_multiple_black_24dp)),
                new DividerDrawerItem(),
                new SecondaryDrawerItem().withName(R.string.settings)
                        .withIcon(FontAwesome.Icon.faw_cog),
                new SecondaryDrawerItem().withName(R.string.exit)
                        .withIcon(getResources().getDrawable(R.drawable.ic_exit_to_app_grey600_48dp))


//                new SecondaryDrawerItem().withName(R.string.drawer_item_open_source)
//                        .withIcon(FontAwesome.Icon.faw_question).setEnabled(false),
//                new DividerDrawerItem(),
//                new SecondaryDrawerItem().withName(R.string.drawer_item_contact)
//                        .withIcon(FontAwesome.Icon.faw_github).withBadge("12+").withIdentifier(1)

        );
        navigationDrawer.withOnDrawerListener(new Drawer.OnDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                // Скрываем клавиатуру при открытии Navigation Drawer
                InputMethodManager inputMethodManager = (InputMethodManager) NavigationDrawer.
                        this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(NavigationDrawer.this.getCurrentFocus().getWindowToken(), 0);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }

            @Override
            public void onDrawerSlide(View view, float v) {

            }
        });
        navigationDrawer.withOnDrawerItemClickListener(this);
        navigationDrawer.withOnDrawerItemLongClickListener(new Drawer.OnDrawerItemLongClickListener() {
            @Override
            // Обработка длинного клика, например, только для SecondaryDrawerItem
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                if (drawerItem instanceof SecondaryDrawerItem) {
                    Toast.makeText(NavigationDrawer.this, NavigationDrawer.
                            this.getString(((SecondaryDrawerItem) drawerItem).getNameRes()), Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        drawerResult = navigationDrawer.build();
    }

    @Override
    public void onBackPressed() {
        // Закрываем Navigation Drawer по нажатию системной кнопки "Назад" если он открыт
        if (drawerResult.isDrawerOpen()) {
            drawerResult.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    // Заглушка, работа с меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // Заглушка, работа с меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(String id) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l, IDrawerItem iDrawerItem) {
        Fragment fragment;
        switch (position) {
            case STUDENTS:
                fragment = new StudentsFragment();
                onDrawTitleNameNavigationDrawer(position, fragment, iDrawerItem);
                break;
            case GROUPS:
                fragment = new GroupsFragment();
                onDrawTitleNameNavigationDrawer(position, fragment, iDrawerItem);
                break;
            case SUBJECTS:
                fragment = new SubjectsFragment();
                onDrawTitleNameNavigationDrawer(position, fragment, iDrawerItem);
                break;
            case SETTING:
                fragment = new SettingsFragment();
                onDrawTitleNameNavigationDrawer(position, fragment, iDrawerItem);
                break;
            case EXIT:
                finish();
                break;
        }

        // Insert the fragment by replacing any existing fragment
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(int position) {

    }

    private void onDrawTitleNameNavigationDrawer(int position, Fragment fragment, IDrawerItem iDrawerItem) {
        Bundle args = new Bundle();
        args.putInt("Position", position);

        fragment.setArguments(args);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        if (iDrawerItem instanceof Nameable) {

            getSupportActionBar().setTitle(NavigationDrawer.this.getString(((Nameable) iDrawerItem).getNameRes()));
            getSupportActionBar().setTitle(NavigationDrawer.this.getString(((Nameable) iDrawerItem).getNameRes()));
            Toast.makeText(NavigationDrawer.this, NavigationDrawer.this.getString(((Nameable) iDrawerItem).getNameRes()),
                    Toast.LENGTH_SHORT).show();
        }
        if (iDrawerItem instanceof Badgeable) {
            Badgeable badgeable = (Badgeable) iDrawerItem;
            if (badgeable.getBadge() != null) {
                // учтите, не делайте так, если ваш бейдж содержит символ "+"
                try {
                    int badge = Integer.valueOf(badgeable.getBadge());
                    if (badge > 0) {
                        drawerResult.updateBadge(String.valueOf(badge + 1), position);
                    }
                } catch (Exception e) {
                    Log.e("TAG", "Не нажимайте на бейдж, содержащий плюс! :)");
                }
            }
        }
    }
}

///**
//     * A placeholder fragment containing a simple view.
//     */
//    public class PlaceholderFragment extends Fragment {
//        /**
//         * The fragment argument representing the section number for this
//         * fragment.
//         */
//        private static final String ARG_SECTION_NUMBER = "section_number";
//        private ListView listView;
//        private ButtonFloat floatButton;
//        private SQLiteDatabase database;
//
//        /**
//         * Returns a new instance of this fragment for the given section
//         * number.
//         */
//        public static PlaceholderFragment newInstance(int sectionNumber) {
//            PlaceholderFragment fragment = new PlaceholderFragment();
//            Bundle args = new Bundle();
//            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//            fragment.setArguments(args);
//            return fragment;
//        }
//
//        public PlaceholderFragment() {
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragment_navigetion, container, false);
//            listView = (ListView) rootView.findViewById(R.id.list_item);
//            floatButton= (ButtonFloat) rootView.findViewById(R.id.float_button);
//            return rootView;
//        }
//
//        @Override
//        public void onViewCreated(View view, Bundle savedInstanceState) {
//            super.onViewCreated(view, savedInstanceState);;
//            database = new TeacherDataBase(getActivity()).getWritableDatabase();
//            Cursor cursor = database.query(TeacherDataBase.TeachersTable.TABLE_NAME,
//                    new String[]{TeacherDataBase.TeachersTable.TEACHER_NAME}, null, null, null, null, null);
//            Log.e("TAG", String.valueOf(cursor.getCount()));
//            ArrayList<String> arrayList = new ArrayList<>();
//            if (cursor.moveToFirst()) {
//                do {
//                    arrayList.add(cursor.getString(cursor.getColumnIndex(TeacherDataBase.TeachersTable.TEACHER_NAME)));
//                } while (cursor.moveToNext());
//            }
//
//
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
//                    R.layout.item_list_fragment,R.id.name, arrayList);
//            listView.setAdapter(adapter);
//            floatButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent myIntent = new Intent(getActivity(), TeacherRegistration.class);
//                    startActivityForResult(myIntent, 1);
//                }
//            });
//        }
//
//        @Override
//        public void onAttach(Activity activity) {
//            super.onAttach(activity);
//            ((NavigationDrawer) activity).onSectionAttached(
//                    getArguments().getInt(ARG_SECTION_NUMBER));
//        }
//    }

//}
