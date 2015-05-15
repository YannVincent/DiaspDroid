package fr.android.scaron.diaspdroid.activity;

import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.acra.ACRA;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.Trace;
import org.androidannotations.annotations.ViewById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.model.DiasporaConfig;
import fr.android.scaron.diaspdroid.vues.fragment.ContactsFragment_;
import fr.android.scaron.diaspdroid.vues.fragment.FluxActivityFragment_;
import fr.android.scaron.diaspdroid.vues.fragment.FluxFragment_;
import fr.android.scaron.diaspdroid.vues.fragment.ParamsFragment_;
import fr.android.scaron.diaspdroid.vues.fragment.TagSuivisFragment_;
import fr.android.scaron.diaspdroid.vues.view.HeaderView;
import fr.android.scaron.diaspdroid.vues.view.HeaderView_;

/**
 * Created by Sébastien on 25/02/2015.
 */
@EActivity(R.layout.diaspora_main)
public class MainActivity extends ActionBarActivity {
    private static Logger LOGGEUR = LoggerFactory.getLogger(MainActivity.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    private static final String TAG = "MainActivity";

    int itemPositionCurrent = 0;
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    boolean listItemClicked=false;
    String[] drawerArray;
    private List<String> drawerItems;
    private ActionBarDrawerToggle mDrawerToggle;

    @ViewById(R.id.diaspora_main)
    DrawerLayout diasporaMain;

    @ViewById(R.id.diaspora_main_drawer)
    ListView diasporaMainDrawer;

    @ViewById(R.id.diaspora_main_content)
    FrameLayout diaporaMainContent;

    @ViewById(R.id.progress_loading)
    RelativeLayout progressLoading;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @AfterViews
    void init(){
        String TAG_METHOD = ".init -> ";
        resetActionBarMain("DiaspDroid");
        LOG.d(TAG + TAG_METHOD + "entrée");
        try{
            LOG.d(TAG + TAG_METHOD + "Add Activity in Config : " + this);
            DiasporaConfig.addActivity(this);
            LOG.d(TAG + TAG_METHOD + "Init Config with application and context");
            DiasporaConfig.init(this.getApplication(), this);
            drawerArray = getResources().getStringArray(R.array.drawer_array);
            drawerItems = Arrays.asList(drawerArray);
            setUpDrawer();
            if (DiasporaConfig.ParamsOK) {
                HeaderView headerView = HeaderView_.build(this.getBaseContext());
                headerView.bind();
                diasporaMainDrawer.addHeaderView(headerView);
                //On selectionne la vue Flux par défaut
                listItemClicked(drawerItems.get(0));
            }else{
                //On selectionne la vue Paramètres par défaut (la derniere)
                listItemClicked(drawerItems.get(drawerItems.size()-1));
            }
        }catch(Throwable thr) {
            LOG.e(TAG + TAG_METHOD + "Erreur : " + thr.toString(), thr);
            ACRA.getErrorReporter().handleException(thr);

            throw thr;
        }
    }
    @Trace(tag="MainActivity",level= Log.DEBUG)
    void trace(){
        String TAG_METHOD = ".trace -> ";
        LOG.d(TAG + TAG_METHOD + "Test @Trace with Tag and Level");
    }

    @Override
    public void onBackPressed() {
        if (itemPositionCurrent>0) {
            itemPositionCurrent = 0;
            setFluxFragment(drawerItems.get(0), 0);
        }else{
            if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
            {
                super.onBackPressed();
                finish();
                return;
            }
            else {
                Toast.makeText(getBaseContext(), "Cliquez deux fois sur 'retour' pour quitter l'application", Toast.LENGTH_LONG).show();
            }
            mBackPressed = System.currentTimeMillis();
        }
    }



    @ItemClick(R.id.diaspora_main_drawer)
    void listItemClicked(String itemClicked) {
        String TAG_METHOD = ".listItemClicked -> ";
        LOG.d(TAG + TAG_METHOD + "list item clicked ? "+itemClicked);
        listItemClicked  = true;
        int itemPosition = drawerItems.indexOf(itemClicked);
        diasporaMainDrawer.setItemChecked(itemPosition+1, true);
        diasporaMainDrawer.setSelection(itemPosition + 1);
        progressLoading.setVisibility(View.VISIBLE);
        setTitle(itemClicked);
        itemPositionCurrent = itemPosition;
        switch (itemPosition){
            case 0 : //Flux
                setFluxFragment(itemClicked, itemPosition);
                break;
            case 1 : //Mon activité
                setFluxActivityFragment(itemClicked, itemPosition);
                break;
            case 2 : //Tags suivis
                setTagSuivisFragment(itemClicked, itemPosition);
                break;
            case 3 : //Mes Amis
                setAmisFragment(itemClicked, itemPosition);
                break;
            case 4 : //Mon Profil
                setProfilFragment(itemClicked, itemPosition);
                break;
            case 5 : //Paramètres
                setParamsFragment(itemClicked, itemPosition);
                break;
            default :
                break;
        }
        progressLoading.setVisibility(View.GONE);
        diasporaMain.closeDrawer(diasporaMainDrawer);
    }

    void setFluxFragment(String title, int position){
        // update the main content by replacing fragments
        FluxFragment_ fluxFragment = new FluxFragment_();
        fluxFragment.setActivityParent(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.diaspora_main_content, fluxFragment)
                .commit();
        resetActionBarMain(title);
    }

    void setFluxActivityFragment(String title, int position){
        // update the main content by replacing fragments
        FluxActivityFragment_ fluxActivityFragment = new FluxActivityFragment_();
        fluxActivityFragment.setActivityParent(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.diaspora_main_content, fluxActivityFragment)
                .commit();
        resetActionBarMain(title);
    }
    void setAmisFragment(String title, int position){
        // update the main content by replacing fragments
        ContactsFragment_ contactsFragment = new ContactsFragment_();
        contactsFragment.setActivityParent(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.diaspora_main_content, contactsFragment)
                .commit();
        resetActionBarMain(title);
    }
    void setTagSuivisFragment(String title, int position){
        // update the main content by replacing fragments
        TagSuivisFragment_ tagSuivisFragment = new TagSuivisFragment_();
        tagSuivisFragment.setActivityParent(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.diaspora_main_content, tagSuivisFragment)
                .commit();
        resetActionBarMain(title);
    }
    void setProfilFragment(String title, int position){
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.diaspora_main_content, new ParamsFragment_())
                .commit();
        resetActionBarMain(title);
    }
    void setParamsFragment(String title, int position){
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.diaspora_main_content, new ParamsFragment_())
                .commit();
        resetActionBarMain(title);
    }

    void setUpDrawer(){
        // Set the adapter for the list view
        diasporaMainDrawer.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, drawerArray));

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                diasporaMain,         /* DrawerLayout object */
                R.string.navigation_drawer_open,  /* "open drawer" description */
                R.string.navigation_drawer_close  /* "close drawer" description */
        );
//        {
//
//            public void onDrawerClosed(View view) {
//                super.onDrawerClosed(view);
////                    getActionBar().setTitle(mTitle);
//            }
//            public void onDrawerOpened(View drawerView) {
//                super.onDrawerOpened(drawerView);
////                    getActionBar().setTitle(mDrawerTitle);
//            }
//        };
        diasporaMain.setDrawerListener(mDrawerToggle);
        diasporaMain.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        diasporaMain.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    void resetActionBarMain(String title){
        ActionBar actionBar = getSupportActionBar();
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
////        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayShowCustomEnabled(false);

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
////        actionBar.setDisplayShowCustomEnabled(false);
////        actionBar.setDisplayShowHomeEnabled(true);
////        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(title);
    }

//    void setActionBarMain(){
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
//////        actionBar.setDisplayShowHomeEnabled(false);
////        actionBar.setDisplayShowTitleEnabled(false);
//        LayoutInflater mInflater = LayoutInflater.from(this);
//
//        View mCustomView = mInflater.inflate(R.layout.actionbar_main, null);
//        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.actbar_name);
//        mTitleTextView.setText("My Own Title");
//
//        LinearLayout actbarDrawerBtn = (LinearLayout) mCustomView
//                .findViewById(R.id.actbar_drawer_btn);
//        actbarDrawerBtn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                if (diasporaMain.isDrawerOpen(diasporaMainDrawer)) {
//                    diasporaMain.closeDrawer(diasporaMainDrawer);
//                }else{
//                    diasporaMain.openDrawer(diasporaMainDrawer);
//                }
//            }
//        });
//
//        actionBar.setCustomView(mCustomView);
////        actionBar.setDisplayShowCustomEnabled(true);
//    }
}