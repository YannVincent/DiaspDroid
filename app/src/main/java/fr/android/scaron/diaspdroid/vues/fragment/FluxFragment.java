package fr.android.scaron.diaspdroid.vues.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.HeadersResponse;
import com.koushikdutta.ion.Response;

import org.acra.ACRA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.activity.DiaspActivity;
import fr.android.scaron.diaspdroid.controler.DataControler;
import fr.android.scaron.diaspdroid.controler.ProfilControler;
import fr.android.scaron.diaspdroid.model.Post;
import fr.android.scaron.diaspdroid.vues.adapter.PostAdapter;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class FluxFragment extends Fragment implements AbsListView.OnItemClickListener {

    static String POD = "framasphere.org";
    public static Logger log = LoggerFactory.getLogger(FluxFragment.class);
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    // TODO: Rename and change types of parameters
    private String mTitle;
    private ActionBarActivity mActivity;

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private PostAdapter mAdapter;

    // TODO: Rename and change types of parameters
    public static FluxFragment newInstance(String title, ActionBarActivity activity, int sectionNumber) {
        try{
            FluxFragment fragment = new FluxFragment();
            fragment.setmTitle(title);
            fragment.setmActivity(activity);
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        } catch (Throwable thr) {
            Log.e(FluxFragment.class.getName(), "Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FluxFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);

            FutureCallback<Response<String>> loginCallback = new FutureCallback<Response<String>>() {
                @Override
                public void onCompleted(Exception e, Response<String> result) {
                    HeadersResponse resultHeaders = result.getHeaders();
                    int code = resultHeaders.code();
                    String message = resultHeaders.message();
                    if (message!=null && !message.isEmpty()){
                        message = message.replaceAll("[\r\n]+", "");

                        log.info(FluxFragment.class.getName() + "Réponse du login sur le pod " + POD + "\n--------------------------\n" + message + "\n--------------------------");
                    }
                    //Callback de la récupération du flux
                    FutureCallback<JsonObject> fluxCallback = new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {

                            log.info(FluxFragment.class.getName() + "Callback flux, exception ? " + e);
                            log.info(FluxFragment.class.getName() + "Callback flux, result ? " + result);
                            if (result!=null) {
                                String resultJson = result.toString();
                                log.info(FluxFragment.class.getName() + "Callback flux, json ? " + result.toString());
                            }
                            if (e==null) {
                                Type collectionType = new TypeToken<List<Post>>() {
                                }.getType();
                                List<Post> posts = new Gson().fromJson(result, collectionType);
                                Log.d(FluxFragment.class.getName(), "Stream json : " + result.toString());
                                mAdapter.setPosts(posts);
//                    mAdapter.notifyDataSetChanged();
                                return;
                            }
                            //Affichage de l'erreur
                            Toast.makeText(mActivity,e.getMessage(),Toast.LENGTH_LONG);
                        }
                    };

                    String reponseLoginOk = "OK"; //"<html><body>You are being<a href=\"https://framasphere.org/stream\">redirected</a>.</body></html>";
                    if (reponseLoginOk.equals(message)) {
                        log.info(FluxFragment.class.getName() + "Succès du login sur le pod " + POD);
                        Log.i(DataControler.class.getName(), "Succès du login sur le pod " + POD);
                        Toast.makeText(getmActivity(), "Succès du login sur le pod " + POD, Toast.LENGTH_LONG);
                        DataControler.getStream(getmActivity().getApplicationContext(), fluxCallback);
                    } else if (code == 302) {
                        log.info(FluxFragment.class.getName() + "Succès du login sur le pod " + POD);
                        Log.i(DataControler.class.getName(), "Succès du login sur le pod " + POD);
                        Toast.makeText(getmActivity(), "Succès du login sur le pod " + POD, Toast.LENGTH_LONG);
                        DataControler.getStream(getmActivity().getApplicationContext(), fluxCallback);
                    } else {
                        log.error(FluxFragment.class.getName() + "Echec du login sur le pod " + POD + "(err:" + code + ")\n" + result.getResult());
                        Log.e(DataControler.class.getName(), "Echec du login sur le pod " + POD + "(err:" + code + ")\n" + result.getResult());
                        Toast.makeText(getmActivity(), "Echec du login sur le pod " + POD + "(err:" + code + ")\n" + result.getResult(), Toast.LENGTH_LONG);
                    }
                }
            };


            ProfilControler.login(getmActivity(), "tilucifer", "Pikifou01" ,loginCallback);


            mAdapter = new PostAdapter(getActivity(), R.layout.fragment_flux_list, new ArrayList<Post>());//FluxContent.POSTS);

//            ActionBar actionBar = mActivity.getSupportActionBar();
//            actionBar.setDisplayShowTitleEnabled(true);
//            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
//            actionBar.setTitle(getString(R.string.title_section1));
        } catch (Throwable thr) {
            Log.e(FluxFragment.class.getName(), "Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try{
            View view = inflater.inflate(R.layout.fragment_flux, container, false);

            // Set the adapter
            mListView = (AbsListView) view.findViewById(R.id.fragment_flux_list);
            ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

            // Set OnItemClickListener so we can be notified on item clicks
            mListView.setOnItemClickListener(this);

            return view;
        } catch (Throwable thr) {
            Log.e(FluxFragment.class.getName(), "Erreur : " + thr.toString());
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
            mListener = (OnFragmentInteractionListener) activity;
        } catch (Throwable thr) {
            Log.e(FluxFragment.class.getName(), "Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }

    }

    @Override
    public void onDetach() {
        try{
            super.onDetach();
            mListener = null;
        } catch (Throwable thr) {
            Log.e(FluxFragment.class.getName(), "Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try{
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onFragmentInteraction(FluxContent.ITEMS.get(position).getIdStr());
            }
        } catch (Throwable thr) {
            Log.e(FluxFragment.class.getName(), "Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        try{
            View emptyView = mListView.getEmptyView();

            if (emptyView instanceof TextView) {
                ((TextView) emptyView).setText(emptyText);
            }
        } catch (Throwable thr) {
            Log.e(FluxFragment.class.getName(), "Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            throw thr;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }


    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public ActionBarActivity getmActivity() {
        return mActivity;
    }

    public void setmActivity(ActionBarActivity mActivity) {
        this.mActivity = mActivity;
    }
}
