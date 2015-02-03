package fr.android.scaron.diaspdroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import org.acra.ACRA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.vues.fragment.DetailPostFragment;
import fr.android.scaron.diaspdroid.vues.fragment.ListPostFragment;

public class ListPostActivity extends ActionBarActivity implements ListPostFragment.OnItemSelectedListener {
    private static Logger LOGGEUR = LoggerFactory.getLogger(ListPostActivity.class);
    private static LogControler LOG = LogControler.getInstance(LOGGEUR);
    private boolean mHasOnePane;
    private String mLastSelectedLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try{
            LOG.d(".onCreate entree with savedInstanceState : "+savedInstanceState);
            super.onCreate(savedInstanceState);
            LOG.d(".onCreate setContentView R.layout.activity_listpost");
            setContentView(R.layout.activity_listpost);

            LOG.d(".onCreate determinate mHasOnePane");
            mHasOnePane = findViewById(R.id.container) != null;

            LOG.d(".onCreate mHasOnePane is true ? "+mHasOnePane+" (R.id.container present ?)");

            if (mHasOnePane) {
                LOG.d(".onCreate mHasOnePane is true, get FragmentManager");
                FragmentManager fm = getSupportFragmentManager();

                LOG.d(".onCreate determine if fragment 'list' is here");
                if (fm.findFragmentByTag("list") == null) {
                    LOG.d(".onCreate fragment 'list' to create");
                    // add list fragment
                    LOG.d(".onCreate create FragmentTransaction");
                    FragmentTransaction trx = fm.beginTransaction();
                    LOG.d(".onCreate add in FragmentTransaction fragment list");
                    trx.add(R.id.container, new ListPostFragment(), "list");
                    LOG.d(".onCreate commit FragmentTransaction");
                    trx.commit();
                }
            } // else, layout handles it

            LOG.d(".onCreate R.id.listFragment found ? "+ (getSupportFragmentManager().findFragmentById(R.id.listFragment)!=null));
            LOG.d(".onCreate R.id.detailFragment found ? "+ (getSupportFragmentManager().findFragmentById(R.id.detailFragment)!=null));


            LOG.d(".onCreate savedInstanceState is null ? : "+(savedInstanceState == null));
            if (savedInstanceState != null) {
                LOG.d(".onCreate savedInstanceState!=null");
                LOG.d(".onCreate get mLastSelectedLink");
                mLastSelectedLink = getIntent().hasExtra("selectedLink") ? savedInstanceState.getString("selectedLink") : null;
                LOG.d(".onCreate onPostItemSelected("+mLastSelectedLink+")");
    //            mLastSelectedLink = savedInstanceState.getString("selectedLink", null);
                onPostItemSelected(mLastSelectedLink);
            }
            LOG.d(".onCreate sortie");
        }catch(Throwable thr) {
            LOG.e(".onCreate sortie en Erreur ("+thr.toString()+")");
            ACRA.getErrorReporter().handleException(thr);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        try {
            LOG.d(".onSaveInstanceState entree");
            super.onSaveInstanceState(outState);
            LOG.d(".onSaveInstanceState mLastSelectedLink is null ? : " + (mLastSelectedLink == null));
            if (mLastSelectedLink != null) {
                LOG.d(".onSaveInstanceState mLastSelectedLink not null so put string selectedLink in outState : " + mLastSelectedLink);
                outState.putString("selectedLink", mLastSelectedLink);
            }
            LOG.d(".onSaveInstanceState sortie");
        }catch(Throwable thr) {
            LOG.e(".onSaveInstanceState sortie en Erreur ("+thr.toString()+")");
            ACRA.getErrorReporter().handleException(thr);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try{
            LOG.d(".onCreateOptionsMenu entree");
            LOG.d(".onCreateOptionsMenu inflate R.menu.listpost");
            getMenuInflater().inflate(R.menu.listpost, menu);
            LOG.d(".onCreateOptionsMenu sortie");
        }catch(Throwable thr) {
            LOG.e(".onCreateOptionsMenu sortie en Erreur ("+thr.toString()+")");
            ACRA.getErrorReporter().handleException(thr);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            LOG.d(".onOptionsItemSelected entree with menuitem : " + item);
            switch (item.getItemId()) {

                case R.id.action_refresh:
                    LOG.d(".onOptionsItemSelected case R.id.action_refresh");

                    LOG.d(".onOptionsItemSelected mHasOnePane ? " + mHasOnePane);
                    if (mHasOnePane) {
                        LOG.d(".onOptionsItemSelected get ListPostFragment by tag");
                        ListPostFragment fragment = (ListPostFragment) getSupportFragmentManager().findFragmentByTag("list");
                        LOG.d(".onOptionsItemSelected ListPostFragment by tag is null ? "+(fragment==null));
                        if (fragment!=null) {
                            LOG.d(".onOptionsItemSelected updateListContent on ListPostFragment by tag");
                            fragment.updateListContent();
                        }
                    } else {
                        LOG.d(".onOptionsItemSelected get ListPostFragment by id");
                        ListPostFragment fragment = (ListPostFragment) getSupportFragmentManager().findFragmentById(R.id.listFragment);
                        LOG.d(".onOptionsItemSelected ListPostFragment by id is null ? "+(fragment==null));
                        if (fragment!=null) {
                            LOG.d(".onOptionsItemSelected updateListContent on ListPostFragment by id");
                            fragment.updateListContent();
                        }
                    }
                    break;
                case R.id.action_settings:
                    LOG.d(".onOptionsItemSelected case R.id.action_settings");
                    LOG.d(".onOptionsItemSelected create intent with MyPreferenceActivity");
                    Intent intent = new Intent(this, MyPreferenceActivity.class);
                    LOG.d(".onOptionsItemSelected start activity MyPreferenceActivity");
                    startActivity(intent);
                    break;
                default:
                    break;
            }
            LOG.d(".onOptionsItemSelected sortie");
        }catch(Throwable thr) {
            LOG.e(".onOptionsItemSelected sortie en Erreur ("+thr.toString()+")");
            ACRA.getErrorReporter().handleException(thr);
        }
        return true;
    }

    @Override
    public void onPostItemSelected(String link) {
        try {
            LOG.d(".onPostItemSelected entree with link : " + link);
            LOG.d(".onPostItemSelected set link to mLastSelectedLink");
            mLastSelectedLink = link;

            LOG.d(".onPostItemSelected mHasOnePane ? " + mHasOnePane);
            if (mHasOnePane) {

                LOG.d(".onPostItemSelected mHasOnePane true, get FragmentManager");
                FragmentManager fm = getSupportFragmentManager();
                LOG.d(".onPostItemSelected mHasOnePane true, find by tag DetailPostFragment");
                DetailPostFragment detailFragment = (DetailPostFragment) fm.findFragmentByTag("detail");

                LOG.d(".onPostItemSelected mHasOnePane true, DetailPostFragment found ? " + (detailFragment == null));
                if (detailFragment == null) {
                    LOG.d(".onPostItemSelected mHasOnePane true, initialize fragment DetailPostFragment");
                    // create and initialize fragment
                    detailFragment = new DetailPostFragment();


                    LOG.d(".onPostItemSelected mHasOnePane true, add link to DetailPostFragment");
                    // configure link
                    Bundle bundle = new Bundle();
                    bundle.putString("link", link);
                    detailFragment.setArguments(bundle);

                    // add fragment
                    LOG.d(".onPostItemSelected mHasOnePane true, create FragmentTransaction to replace fragment detail");
                    FragmentTransaction trx = fm.beginTransaction();
                    LOG.d(".onPostItemSelected mHasOnePane true, replace fragment detail");
                    trx.replace(R.id.container, detailFragment, "detail");
                    LOG.d(".onPostItemSelected mHasOnePane true, addToBackStack null on transaction");
                    trx.addToBackStack(null);
                    LOG.d(".onPostItemSelected mHasOnePane true, commit transaction");
                    trx.commit();

                } else {

                    LOG.d(".onPostItemSelected mHasOnePane true, update link to DetailPostFragment");
                    detailFragment.getArguments().putString("link", link);
                }

            } else {

                LOG.d(".onPostItemSelected mHasOnePane false, find by id DetailPostFragment");
                DetailPostFragment fragment = (DetailPostFragment) getSupportFragmentManager().findFragmentById(R.id.detailFragment);
                LOG.d(".onPostItemSelected mHasOnePane false, setText link to DetailPostFragment");
                fragment.setText(link);
            }
            LOG.d(".onPostItemSelected sortie");
        }catch(Throwable thr) {
            LOG.e(".onPostItemSelected sortie en Erreur ("+thr.toString()+")");
            ACRA.getErrorReporter().handleException(thr);
        }

    }

}
