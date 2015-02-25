package fr.android.scaron.diaspdroid.vues.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.acra.ACRA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.activity.DiaspActivity;
import fr.android.scaron.diaspdroid.controler.LogControler;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private static Logger LOGGEUR = LoggerFactory.getLogger(PlaceholderFragment.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);


    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        try{
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }catch(Throwable thr){
            LOG.e(PlaceholderFragment.class, "Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }

    public PlaceholderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try{
            View rootView = inflater.inflate(R.layout.fragment_diasp, container, false);
            return rootView;
        }catch(Throwable thr){
            LOG.e(PlaceholderFragment.class, "Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        try{
            super.onAttach(activity);
            ((DiaspActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }catch(Throwable thr){
            LOG.e(PlaceholderFragment.class, "Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }
}