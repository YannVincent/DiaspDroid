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

import org.acra.ACRA;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.vues.fragment.FluxFragment;
import fr.android.scaron.diaspdroid.vues.fragment.PlaceholderFragment;

public class DiaspActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, FluxFragment.OnFragmentInteractionListener {

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
            Log.e(this.getClass().getName(), "Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
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
                            .replace(R.id.container, FluxFragment.newInstance(mTitle.toString(), this))
                            .commit();
                    restoreActionBar();
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
            Log.e(this.getClass().getName(),"Erreur : "+thr.toString());
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
            Log.e(this.getClass().getName(),"Erreur : "+thr.toString());
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
            Log.e(this.getClass().getName(),"Erreur : "+thr.toString());
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
            Log.e(this.getClass().getName(),"Erreur : "+thr.toString());
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
                return true;
            }

            return super.onOptionsItemSelected(item);
        }catch(Throwable thr){
            Log.e(this.getClass().getName(),"Erreur : "+thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }


    @Override
    public void onFragmentInteraction(String id){
        Toast.makeText(this, "Flux Fragment : id("+id+")", Toast.LENGTH_SHORT).show();
    }

//    /**
//     * A placeholder fragment containing a simple view.
//     */
//    public static class PlaceholderFragment extends Fragment {
//        /**
//         * The fragment argument representing the section number for this
//         * fragment.
//         */
//        private static final String ARG_SECTION_NUMBER = "section_number";
//
//        /**
//         * Returns a new instance of this fragment for the given section
//         * number.
//         */
//        public static PlaceholderFragment newInstance(int sectionNumber) {
//            try{
//                PlaceholderFragment fragment = new PlaceholderFragment();
//                Bundle args = new Bundle();
//                args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//                fragment.setArguments(args);
//                return fragment;
//            }catch(Throwable thr){
//                ACRA.getErrorReporter().handleException(thr);
//                throw thr;
//            }
//        }
//
//        public PlaceholderFragment() {
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            try{
//                View rootView = inflater.inflate(R.layout.fragment_diasp, container, false);
//                return rootView;
//            }catch(Throwable thr){
//                ACRA.getErrorReporter().handleException(thr);
//                throw thr;
//            }
//        }
//
//        @Override
//        public void onAttach(Activity activity) {
//            try{
//                super.onAttach(activity);
//                ((DiaspActivity) activity).onSectionAttached(
//                        getArguments().getInt(ARG_SECTION_NUMBER));
//            }catch(Throwable thr){
//                ACRA.getErrorReporter().handleException(thr);
//                throw thr;
//            }
//        }
//    }

}
