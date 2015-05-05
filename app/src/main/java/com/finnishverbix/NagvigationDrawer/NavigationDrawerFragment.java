package com.finnishverbix.NagvigationDrawer;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.finnishverbix.Fragment.AboutFragment;
import com.finnishverbix.Fragment.FavoriteFragment;
import com.finnishverbix.Fragment.HelpFragment;
import com.finnishverbix.Fragment.SearchFragment;
import com.finnishverbix.Fragment.SettingFragment;
import com.finnishverbix.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A navigation drawer fragment {@link Fragment} class.
 */
public class NavigationDrawerFragment extends Fragment implements NavigationDrawerAdapter.ClickListener {

    //declare variable
    //create a tag of remembering the first time opens navigation drawer
    public static final String PREF_FILE_NAME = "pref";
    public static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";
    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;

    //Variable for the drawer
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationDrawerAdapter mDrawerAdapter;
    private RecyclerView recyclerView;
    private View containerView;

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Tell the apps that the user already know how to use the drawer
        mUserLearnedDrawer = Boolean.valueOf(readFromPreferences(getActivity(),KEY_USER_LEARNED_DRAWER,"false"));
        if(savedInstanceState != null){
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View navigationLayout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        //Initialize the recyclerView
        recyclerView = (RecyclerView) navigationLayout.findViewById(R.id.drawerList);
        //Set up the adapter.
        mDrawerAdapter = new NavigationDrawerAdapter(getActivity(),getData());
        mDrawerAdapter.setClickListener(this);
        //Set the recycler view
        recyclerView.setAdapter(mDrawerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return navigationLayout;
    }

    //Make a list of navigation drawer item
    private List<NavigationDrawerItem> getData() {
        List<NavigationDrawerItem> itemList = new ArrayList<NavigationDrawerItem>();
        itemList.add(new NavigationDrawerItem(R.drawable.ic_search_black_24dp, "Search"));
        itemList.add(new NavigationDrawerItem(R.drawable.ic_bookmark_outline_black_24dp, "Favorite"));

        itemList.add(new NavigationDrawerItem(R.drawable.ic_live_help_black_24dp, "Help"));
        itemList.add(new NavigationDrawerItem(R.drawable.ic_settings_applications_black_24dp, "Settings"));
        itemList.add(new NavigationDrawerItem(R.drawable.ic_info_outline_black_24dp, "About"));
        return  itemList;
    }

    //SET UP THE RECYCLE VIEW

    public void setUp(int fragmentID, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentID);
        mDrawerLayout = drawerLayout;
        //Handling the action opening and closing
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(),drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //When the drawer opened in the first time -> user learned drawer, -> set variable = true and saved
                //so that the drawer will not open automatically when opening the app.
                if(!mUserLearnedDrawer){
                    mUserLearnedDrawer = true;
                    saveToPreferences(getActivity(),KEY_USER_LEARNED_DRAWER,mUserLearnedDrawer+"");
                }
                getActivity().invalidateOptionsMenu(); //redraw the menu
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //if(slideOffset < 0.6)
                //  toolbar.setAlpha((float) (1- slideOffset));

                super.onDrawerSlide(drawerView, slideOffset);
            }
        };
        //Open drawer on the first time.
        if(mUserLearnedDrawer == false && !mFromSavedInstanceState){
            mDrawerLayout.openDrawer(containerView);
        }
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() { // TO MAKE THE ICON ON THE LEFT
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    // FOR NOTIFYING USER OF DRAWER FROM THE FIRST TIME
    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }
    public static String readFromPreferences(Context context,String preferenceName,String preferenceValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName,preferenceValue);
    }


    //ITEM CLICKED HANDLER
    public void selectItem(int position){
        Fragment fragment = null;
        FragmentManager fragmentManager = getFragmentManager();
        switch (position){
            case 0:
                //select Search fragment
                fragment = new SearchFragment();

                break;
            case 1:
                //select favorite fragment
                fragment = new FavoriteFragment();
                break;
            case 2:
                //select help fragment
                fragment = new HelpFragment();
                break;
            case 3:
                //select setting fragment
                fragment = new SettingFragment();
                break;
            case 4:
                //select about fragment
                fragment = new AboutFragment();
            default:
                break;

        }
        //Change the frame that is selected
        fragmentManager.beginTransaction().replace(R.id.content_frame,fragment).commit();
        //after view changed, close the drawer.
        mDrawerLayout.closeDrawer(containerView);
    }
    //implement the item clicked from the adapter
    @Override
    public void itemClicked(View view, int position) {
        selectItem(position);
    }

}
