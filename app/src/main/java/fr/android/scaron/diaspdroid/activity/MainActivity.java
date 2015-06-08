package fr.android.scaron.diaspdroid.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.google.gson.Gson;

import org.acra.ACRA;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
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
import fr.android.scaron.diaspdroid.model.Post;
import fr.android.scaron.diaspdroid.vues.fragment.ContactsFragment_;
import fr.android.scaron.diaspdroid.vues.fragment.FluxActivityFragment_;
import fr.android.scaron.diaspdroid.vues.fragment.FluxFragment_;
import fr.android.scaron.diaspdroid.vues.fragment.ParamsFragment_;
import fr.android.scaron.diaspdroid.vues.fragment.ProfileFragment_;
import fr.android.scaron.diaspdroid.vues.fragment.ShareFragment_;
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
    private static String TAG = MainActivity.class.getSimpleName();

    int itemPositionCurrent = 0;
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    boolean listItemClicked=false;
    String[] drawerArray;
    private List<String> drawerItems;
    private ActionBarDrawerToggle mDrawerToggle;
    @Extra
    Uri imageUri;

    Post postParent;

    @ViewById(R.id.diaspora_main)
    DrawerLayout diasporaMain;

    @ViewById(R.id.diaspora_main_drawer)
    ListView diasporaMainDrawer;

    @ViewById(R.id.diaspora_main_content)
    FrameLayout diaporaMainContent;

    @ViewById(R.id.progress_loading)
    RelativeLayout progressLoading;

    Fragment fragementActif;

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
                if (imageUri!=null){
                    //On selectionne la vue Partagez car on a recu une image
                    listItemClicked(drawerItems.get(3));
                }else {
                    HeaderView headerView = HeaderView_.build(this.getBaseContext());
                    headerView.bind();
                    diasporaMainDrawer.addHeaderView(headerView);
                    //On selectionne la vue Flux par défaut
                    listItemClicked(drawerItems.get(0));
                }
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

    public void setDefaultView(){
        String TAG_METHOD = TAG + ".setDefaultView : ";
        LOG.d(TAG_METHOD+ "Entrée");
        //On selectionne la vue Flux par défaut
        LOG.d(TAG_METHOD+"On sélectionne la vue par défaut : "+drawerItems.get(0));
        listItemClicked(drawerItems.get(0));
//            setFluxFragment(drawerItems.get(0), 0);
        LOG.d(TAG_METHOD+ "Sortie");
    }

    @Override
    public void onBackPressed() {
        if (itemPositionCurrent>0) {
            itemPositionCurrent = 0;
            setDefaultView();
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

    public void listItemClicked(String itemClicked, Post postParent){
        this.postParent = postParent;
        listItemClicked(itemClicked);
    }
    public void listItemClicked(String itemClicked, Uri uri){
        imageUri = uri;
        listItemClicked(itemClicked);
    }

    @ItemClick(R.id.diaspora_main_drawer)
    public void listItemClicked(String itemClicked) {
        String TAG_METHOD = ".listItemClicked -> ";
        LOG.d(TAG + TAG_METHOD + "list item clicked ? "+itemClicked);
        listItemClicked  = true;
        int itemPosition = drawerItems.indexOf(itemClicked);

        diasporaMainDrawer.setItemChecked(itemPosition+1, true);
        diasporaMainDrawer.setSelection(itemPosition+1);

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
            case 3 : //Partager
                if (imageUri==null) {
                    if (postParent==null) {
                        setShareFragment(itemClicked, itemPosition);
                    }else {
                        setShareFragment(itemClicked, itemPosition, postParent);
                    }
                }else{
                    setShareFragment(itemClicked, itemPosition, imageUri);
                }
                break;
            case 4 : //Mes Amis
                setAmisFragment(itemClicked, itemPosition);
                break;
            case 5 : //Mon Profil
                setProfilFragment(itemClicked, itemPosition);
                break;
            case 6 : //Paramètres
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
        fragementActif = fluxFragment;
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
        fragementActif = fluxActivityFragment;
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
        fragementActif = contactsFragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.diaspora_main_content, contactsFragment)
                .commit();
        resetActionBarMain(title);
    }
    void setShareFragment(String title, int position){
        // update the main content by replacing fragments
        ShareFragment_ shareFragment = new ShareFragment_();
        shareFragment.setActivityParent(this);
        fragementActif = shareFragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.diaspora_main_content, shareFragment)
                .commit();
        resetActionBarMain(title);
    }
    void setShareFragment(String title, int position, Uri imageUri){
        String TAG_METHOD = TAG + ".setShareFragment : ";
        LOG.d(TAG + TAG_METHOD + "Entrée");
        // update the main content by replacing fragments
        ShareFragment_ shareFragment = new ShareFragment_();
        shareFragment.setActivityParent(this);
        fragementActif = shareFragment;
        Bundle bundle = new Bundle();
        LOG.d(TAG_METHOD + "On met l'EXTRA_STREAM '"+imageUri+"' en argument au fragment de partage");
        bundle.putParcelable(Intent.EXTRA_STREAM, imageUri);
        shareFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.diaspora_main_content, shareFragment)
                .commit();
        resetActionBarMain(title);
        LOG.d(TAG + TAG_METHOD + "Sortie");
    }
    void setShareFragment(String title, int position, Post postParent){
        String TAG_METHOD = TAG + ".setShareFragment : ";
        LOG.d(TAG + TAG_METHOD + "Entrée");
        // update the main content by replacing fragments
        ShareFragment_ shareFragment = new ShareFragment_();
        shareFragment.setActivityParent(this);
        fragementActif = shareFragment;
        Bundle bundle = new Bundle();
        LOG.d(TAG_METHOD + "On met l'EXTRA_UID '" + postParent.getId() + "' en argument au fragment de partage");
        Gson gson = new Gson();
        String postJson = gson.toJson(postParent);
        bundle.putString(Intent.EXTRA_TEXT, postJson);
        shareFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.diaspora_main_content, shareFragment)
                .commit();
        resetActionBarMain(title);
        LOG.d(TAG + TAG_METHOD + "Sortie");
    }

    void setTagSuivisFragment(String title, int position){
        // update the main content by replacing fragments
        TagSuivisFragment_ tagSuivisFragment = new TagSuivisFragment_();
        tagSuivisFragment.setActivityParent(this);
        fragementActif = tagSuivisFragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.diaspora_main_content, tagSuivisFragment)
                .commit();
        resetActionBarMain(title);
    }
    void setProfilFragment(String title, int position){
        // update the main content by replacing fragments
        ProfileFragment_ profileFragment = new ProfileFragment_();
        profileFragment.setActivityParent(this);
        fragementActif = profileFragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.diaspora_main_content, profileFragment)
                .commit();
        resetActionBarMain(title);
    }
    void setParamsFragment(String title, int position){
        // update the main content by replacing fragments
        ParamsFragment_ paramsFragment = new ParamsFragment_();
        paramsFragment.setActivityParent(this);
        fragementActif = paramsFragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.diaspora_main_content, paramsFragment)
                .commit();
        resetActionBarMain(title);
    }

    public Fragment getFragementActif(){
        return fragementActif;
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
        diasporaMain.setDrawerListener(mDrawerToggle);
        diasporaMain.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        diasporaMain.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    public void resetActionBarMain(String title){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setTitle(title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String TAG_METHOD = TAG + ".onCreate : ";
        LOG.d(TAG_METHOD + "entrée");
        try {
            super.onCreate(savedInstanceState);
            Intent intent = getIntent();
            String action = intent.getAction();
            LOG.d(TAG_METHOD + "action ? "+action);
            String type = intent.getType();
            LOG.d(TAG_METHOD + "type ? "+type);

            if (Intent.ACTION_SEND.equals(action) && type != null) {
                if (type.startsWith("image/") && imageUri == null) {
                    imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
                    LOG.d(TAG_METHOD + "imageUri ? "+imageUri);
                }
            }
        }catch(Throwable thr){
            LOG.e(TAG_METHOD + "Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            LOG.d(TAG_METHOD + "sortie en erreur");
            throw thr;
        }
        LOG.d(TAG_METHOD + "sortie");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

}