package fr.android.scaron.diaspdroid.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;

import org.acra.ACRA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.model.DiasporaConfig;
import fr.android.scaron.diaspdroid.vues.fragment.FluxFragment;
import fr.android.scaron.diaspdroid.vues.fragment.PlaceholderFragment;

public class DiaspActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, FluxFragment.OnFragmentInteractionListener {

    private static Logger LOGGEUR = LoggerFactory.getLogger(DiaspActivity.class);
    private static LogControler LOG = LogControler.getInstance(LOGGEUR);
//    public static LogControler LOG = LogControler.getInstance(LoggerFactory.getLogger(DiaspActivity.class));
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);

            DiasporaConfig.init(this.getApplication(), this);
//            //SET SSL TODO DECOMMENTER
//            KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
//            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
//
//            ks.load(this.getResources().openRawResource(R.raw.keystore), "storepass".toCharArray());
//            kmf.init(ks, "storepass".toCharArray());
//
//
//            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//            KeyStore ts = KeyStore.getInstance(KeyStore.getDefaultType());
//            ts.load(this.getResources().openRawResource(R.raw.keystore), "storepass".toCharArray());
//            tmf.init(ts);
//
//            SSLContext sslContext = SSLContext.getInstance("TLS");
//            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
//            //END OF SET SSL

            // Get intent, action and MIME type
            Intent intent = getIntent();
            String action = intent.getAction();
            String type = intent.getType();
            if (Intent.ACTION_SEND.equals(action) && type != null) {
//                if ("text/plain".equals(type)) {
//                    handleSendText(intent); // Handle text being sent
//                } else
                if (type.startsWith("image/")) {
                    handleSendImage(intent); // Handle single image being sent

                    setContentView(R.layout.activity_share);
//                    LinearLayout shareLayout = (LinearLayout)findViewById(R.id.share_layout);
                    EditText shareText = (EditText)findViewById(R.id.share_text_entry);
                    Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
                    shareText.setText("Partage de la photo : "+imageUri.getEncodedPath());
                }
//            } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
//                if (type.startsWith("image/")) {
//                    handleSendMultipleImages(intent); // Handle multiple images being sent
//                }
            } else {
                // Handle other intents, such as being started from the home screen

            setContentView(R.layout.activity_diasp);

            mNavigationDrawerFragment = (NavigationDrawerFragment)
                    getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
            mTitle = getTitle();

            // Set up the drawer.
            mNavigationDrawerFragment.setUp(
                    R.id.navigation_drawer,
                    (DrawerLayout) findViewById(R.id.drawer_layout));
            }
        }catch(Throwable thr){
            LOG.e(this.getClass(), "Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
//            try { //TODO DECOMMENTER
                throw thr;
//            } catch (NoSuchAlgorithmException e) {
//                e.printStackTrace();
//            } catch (KeyStoreException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (CertificateException e) {
//                e.printStackTrace();
//            } catch (UnrecoverableKeyException e) {
//                e.printStackTrace();
//            } catch (KeyManagementException e) {
//                e.printStackTrace();
//            }
        }
    }

    void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            // Update UI to reflect image being shared
            Toast.makeText(this, "Image : "+imageUri.getPath(), Toast.LENGTH_LONG);
        }
    }

    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            // Update UI to reflect text being shared
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        try{
            // update the main content by replacing fragments
            FragmentManager fragmentManager = getSupportFragmentManager();

            // TODO VALIDER CE BLOC SWITCH
            switch(position){
                case 0 :
                    mTitle = getString(R.string.title_section1);
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, FluxFragment.newInstance(mTitle.toString(), DiaspActivity.this, position+1))
                            .commit();
//                    restoreActionBar();
                    break;
                default :
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                            .commit();
                break;
            }
//            fragmentManager.beginTransaction()
//                    .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
//                    .commit();
        }catch(Throwable thr){
            LOG.e(this.getClass(),"Erreur : "+thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }

    public void onSectionAttached(int number) {
        try{
            switch (number) {
                case 1:
                    mTitle = getString(R.string.title_section1);
                    break;
                case 2:
                    mTitle = getString(R.string.title_section2);
                    break;
                case 3:
                    mTitle = getString(R.string.title_section3);
                    break;
            }
        }catch(Throwable thr){
            LOG.e(this.getClass(),"Erreur : "+thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }

    public void restoreActionBar() {
        try{
            ActionBar actionBar = getSupportActionBar();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(mTitle);
        }catch(Throwable thr){
            LOG.e(this.getClass(),"Erreur : "+thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try{
            if (mNavigationDrawerFragment!=null && !mNavigationDrawerFragment.isDrawerOpen()) {
                // Only show items in the action bar relevant to this screen
                // if the drawer is not showing. Otherwise, let the drawer
                // decide what to show in the action bar.
                getMenuInflater().inflate(R.menu.diasp, menu);
                restoreActionBar();
                return true;
            }
            return super.onCreateOptionsMenu(menu);
        }catch(Throwable thr){
            LOG.e(this.getClass(),"Erreur : "+thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try{
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                Intent intent = ParamsActivity_.intent(this).get();
                startActivity(intent);
                return true;
            }

            return super.onOptionsItemSelected(item);
        }catch(Throwable thr){
            LOG.e(this.getClass(),"Erreur : "+thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }


    @Override
    public void onFragmentInteraction(String id){
        Toast.makeText(this, "Flux Fragment : id("+id+")", Toast.LENGTH_SHORT).show();
    }

}
