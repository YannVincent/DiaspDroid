package fr.android.scaron.diaspdroid.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
import fr.android.scaron.diaspdroid.vues.fragment.FluxFragment_;
import fr.android.scaron.diaspdroid.vues.fragment.ParamsFragment_;

/**
 * Created by Sébastien on 25/02/2015.
 */
@EActivity(R.layout.diaspora_main)
public class MainActivity extends ActionBarActivity {
    private static Logger LOGGEUR = LoggerFactory.getLogger(MainActivity.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    private static final String TAG = "MainActivity";

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

    @AfterViews
    void init(){
        String TAG_METHOD = ".init -> ";
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
        LOG.d(TAG + TAG_METHOD + "entrée");
        try{
            LOG.d(TAG + TAG_METHOD + "Add Activity in Config : " + this);
            DiasporaConfig.addActivity(this);
            LOG.d(TAG + TAG_METHOD + "Init Config with application and context");
            DiasporaConfig.init(this.getApplication(), this);

            drawerArray = getResources().getStringArray(R.array.drawer_array);
            drawerItems = Arrays.asList(drawerArray);
            // Set the adapter for the list view
            diasporaMainDrawer.setAdapter(new ArrayAdapter<String>(this,
                    R.layout.drawer_list_item, drawerArray));

            mDrawerToggle = new ActionBarDrawerToggle(
                    this,                  /* host Activity */
                    diasporaMain,         /* DrawerLayout object */
                    R.string.navigation_drawer_open,  /* "open drawer" description */
                    R.string.navigation_drawer_close  /* "close drawer" description */
            ) {
                public void onDrawerClosed(View view) {
                    super.onDrawerClosed(view);
//                    getActionBar().setTitle(mTitle);
                }
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
//                    getActionBar().setTitle(mDrawerTitle);
                }
            };
            diasporaMain.setDrawerListener(mDrawerToggle);


            //On selectionne la vue Flux par défaut
            listItemClicked(drawerItems.get(0));
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

    @ItemClick(R.id.diaspora_main_drawer)
    void listItemClicked(String itemClicked) {
        String TAG_METHOD = ".listItemClicked -> ";
        LOG.d(TAG + TAG_METHOD + "list item clicked ? "+itemClicked);
        listItemClicked  = true;
        int itemPosition = drawerItems.indexOf(itemClicked);
        diasporaMainDrawer.setItemChecked(itemPosition, true);
        setTitle(itemClicked);
        switch (itemPosition){
            case 0 : //Flux
                setFluxFragment(itemClicked, itemPosition);
                break;
            case 1 : //Mes Amis
                break;
            case 2 : //Mon Profil
                break;
            case 3 : //Paramètres
                setParamsFragment(itemClicked, itemPosition);
                break;
            default :
                break;
        }
        diasporaMain.closeDrawer(diasporaMainDrawer);
    }

    void setFluxFragment(String title, int position){
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.diaspora_main_content, new FluxFragment_())
                .commit();
        setActionBarMain();
    }
    void setParamsFragment(String title, int position){
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.diaspora_main_content, new ParamsFragment_())
                .commit();
        resetActionBarMain(title);
    }

    void resetActionBarMain(String title){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
//        actionBar.setDisplayShowCustomEnabled(false);
//        actionBar.setDisplayShowHomeEnabled(true);
//        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(title);
    }

    void setActionBarMain(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
//        actionBar.setDisplayShowHomeEnabled(false);
//        actionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.actionbar_main, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.actbar_name);
        mTitleTextView.setText("My Own Title");

        LinearLayout actbarDrawerBtn = (LinearLayout) mCustomView
                .findViewById(R.id.actbar_drawer_btn);
        actbarDrawerBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (diasporaMain.isDrawerOpen(diasporaMainDrawer)) {
                    diasporaMain.closeDrawer(diasporaMainDrawer);
                }else{
                    diasporaMain.openDrawer(diasporaMainDrawer);
                }
            }
        });

        actionBar.setCustomView(mCustomView);
//        actionBar.setDisplayShowCustomEnabled(true);
    }


}