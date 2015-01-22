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

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.koushikdutta.ion.HeadersResponse;
import com.koushikdutta.ion.Response;

import org.acra.ACRA;
import org.apache.http.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

            //Callback de la récupération du flux
            final FutureCallback<List<Post>> fluxCallback = new FutureCallback<List<Post>>() {
                @Override
                public void onCompleted(Exception e, List<Post> posts) {

                    log.info(FluxFragment.class.getName() + "Callback flux, exception ? " + e);
                    if (e!=null && e.getCause()!=null) {
                        log.info(FluxFragment.class.getName() + "Callback flux, cause exception ? " + e.getCause().getMessage());
                    }
                    if (posts!=null){
                        mAdapter.setPosts(posts);
                        return;
                    }
                    Log.e(FluxFragment.class.getName(), "Callback flux, Erreur : " + e.toString());
                    if (e.getCause()!=null) {
                        Log.e(FluxFragment.class.getName(), "Callback flux, cause exception ? " + e.getCause().getMessage());
                    }
                    ACRA.getErrorReporter().handleException(e);
                    //Affichage de l'erreur
                    Toast.makeText(mActivity,e.getMessage(),Toast.LENGTH_LONG);
                }
            };

            //Callback d'envoi du formulaire de login
            final FutureCallback<Response<String>> loginCallback = new FutureCallback<Response<String>>() {
                @Override
                public void onCompleted(Exception e, Response<String> result) {
                    HeadersResponse resultHeaders = result.getHeaders();
                    int code = resultHeaders.code();
                    //code = 401 i,n
                    String message = resultHeaders.message();
                    if (message!=null && !message.isEmpty()){
                        message = message.replaceAll("[\r\n]+", "");
                        log.debug(FluxFragment.class.getName() + "Header Réponse du login sur le pod " + POD + "\n--------------------------\n" + message + "\n--------------------------");
                        Log.d(FluxFragment.class.getName(), "Header Réponse du login sur le pod " + POD + "\n--------------------------\n" + message + "\n--------------------------");
                    }
                    message = result.getResult();
                    if (message!=null && !message.isEmpty()){
                        message = message.replaceAll("[\r\n]+", "");
                        log.debug(FluxFragment.class.getName() + "Réponse du login sur le pod " + POD + "\n--------------------------\n" + message + "\n--------------------------");
                        Log.d(FluxFragment.class.getName(), "Réponse du login sur le pod " + POD + "\n--------------------------\n" + message + "\n--------------------------");
                    }

                    String reponseLoginOk = "OK"; //"<html><body>You are being<a href=\"https://framasphere.org/stream\">redirected</a>.</body></html>";
//                    if (reponseLoginOk.equals(message)) {
                        List<String> cookies = new ArrayList<String>();
                        if (result.getHeaders() != null && result.getHeaders().getHeaders() != null) {
                            cookies = result.getHeaders().getHeaders().getAll("set-cookie");
                            log.info(FluxFragment.class.getName() + "Succès du login sur le pod " + POD + "\n" +
                                    cookies.toString());
                            Log.i(DataControler.class.getName(), "Succès du login sur le pod " + POD + "\n" +
                                    result.getHeaders().getHeaders().toString());
                            Toast.makeText(getmActivity(), "Succès du login sur le pod " + POD, Toast.LENGTH_LONG);
                        }else {

                            log.info(FluxFragment.class.getName() + "Pas de cookie pour le login sur le pod " + POD + "\n" +
                                    cookies.toString());
                            Log.i(DataControler.class.getName(), "Pas de cookie pour le login sur le pod " + POD + "\n" +
                                    result.getHeaders().getHeaders().toString());
                        }
                        //BOUCHON on met manuellement le cookie
                        cookies.clear();
                        cookies.add("_pk_ref.26.270d=%5B%22%22%2C%22%22%2C1421845797%2C%22https%3A%2F%2Fwww.youtube.com%2Fwatch%3Fv%3DaZ7Y-CSFTbY%22%5D; remember_user_token=BAhbB1sGaQISCUkiIiQyYSQxMCRTcmhiZC9yS2JBczlqWUk5cVNZVU9PBjoGRVQ%3D--a111fa31a16b0451130d7598978cda8257466368; _pk_id.26.270d=d9a7ba39dd808238.1412778655.23.1421845886.1421843838.; _diaspora_session=BAh7CEkiD3Nlc3Npb25faWQGOgZFVEkiJWZjNjIzY2M2Y2RlMDk0YTMyMzlhZmE2Y2I0ZGMzYzRlBjsAVEkiGXdhcmRlbi51c2VyLnVzZXIua2V5BjsAVFsHWwZpAhIJSSIiJDJhJDEwJFNyaGJkL3JLYkFzOWpZSTlxU1lVT08GOwBUSSIQX2NzcmZfdG9rZW4GOwBGSSIxOTVEUjltNmd2UlRpVFNPWGQvaXVCOWU3TkFCdWwxeStjekRUUXQxb3BIRT0GOwBG--e8931f6b938a2324f7e7ab50db247cbf14027007");
                        //On est loggué donc on demande le flux
                        DataControler.getStream(getmActivity().getApplicationContext(), fluxCallback, cookies);
//                    }else {
//                        log.error(FluxFragment.class.getName() + "Echec du login sur le pod " + POD + "(err:" + code + ")\n" + result.getResult());
//                        Log.e(DataControler.class.getName(), "Echec du login sur le pod " + POD + "(err:" + code + ")\n" + result.getResult());
//                        Toast.makeText(getmActivity(), "Echec du login sur le pod " + POD + "(err:" + code + ")\n" + result.getResult(), Toast.LENGTH_LONG);
//                    }
                }
            };

            //Callback de récupération du formulaire de login et d'accès au authenticity_token
            final FutureCallback<Response<String>> tokenCallback = new FutureCallback<Response<String>>() {
                @Override
                public void onCompleted(Exception e, Response<String> result) {
                    HeadersResponse resultHeaders = result.getHeaders();
                    int code = resultHeaders.code();
                    String message = resultHeaders.message();
                    if (message!=null && !message.isEmpty()){
                        message = message.replaceAll("[\r\n]+", "");
                        log.debug(FluxFragment.class.getName() + "Header Réponse du get login sur le pod " + POD + "\n--------------------------\n" + message + "\n--------------------------");
                        Log.d(FluxFragment.class.getName(), "Header Réponse du get login sur le pod " + POD + "\n--------------------------\n" + message + "\n--------------------------");
                    }
                    message = result.getResult();
                    if (message!=null && !message.isEmpty()){
                        message = message.replaceAll("[\r\n]+", "");
                        log.debug(FluxFragment.class.getName() + "Réponse du get login sur le pod " + POD + "\n--------------------------\n" + message + "\n--------------------------");
                        Log.d(FluxFragment.class.getName(), "Réponse du get login sur le pod " + POD + "\n--------------------------\n" + message + "\n--------------------------");
                    }
                    if (code == 200) {
                        //on récupère les lignes
//                        <meta content="authenticity_token" name="csrf-param" />
//                        <meta content="tHvh4NDDRCihQtdiHKhxTtDFwH5TFfC4jLQe2MOWnOM=" name="csrf-token" />
                        String reponse = result.getResult();
                        int indexTokenName = reponse.indexOf("<meta content=\"authenticity_token\" name=\"csrf-param\" />",0);
                        if (indexTokenName>0) {
                            int indexToken = reponse.indexOf("<meta content=\"", indexTokenName + 1);
                            indexToken = indexToken+1+"<meta content=\"".length();
                            int indexEndToken = reponse.indexOf("\" name=\"csrf-token\"", indexToken);
                            String token = reponse.substring(indexToken, indexEndToken);

                            log.debug(FluxFragment.class.getName() + "token récolté '" + token + "'");
                            Log.d(this.getClass().getName(),"token récolté '" + token + "'");

                            //On a le token donc on demande le login
                            ProfilControler.login(getmActivity(), "tilucifer", "Pikifou01", token, loginCallback);
                        }

                    }
                }
            };


            final AsyncHttpClient.StringCallback loginCallBackAsync = new AsyncHttpClient.StringCallback() {
                // Callback is invoked with any exceptions/errors, and the result, if available.
                @Override
                public void onCompleted(Exception e, AsyncHttpResponse response, String result) {
                    if (e != null) {
                        e.printStackTrace();
                        return;
                    }
                    List<String> cookies = new ArrayList<String>();
                    if (result!=null && !result.isEmpty()){
                        result = result.replaceAll("[\r\n]+", "");
                        log.debug(FluxFragment.class.getName() + "Réponse login async sur le pod " + POD + "\n--------------------------\n" + result + "\n--------------------------");
                        Log.d(FluxFragment.class.getName(), "Réponse login async sur le pod " + POD + "\n--------------------------\n" + result + "\n--------------------------");
                    }
                    if (response.headers()!=null) {
                        Header[] headers = response.headers().toHeaderArray();
                        for (Header header : headers) {
                            log.debug(FluxFragment.class.getName() + "\tHeader : " + header.getName() + " -> " + header.getValue() + " (" + header.getElements() + ")");
                            if ("set-cookie".equals(header.getName().toLowerCase())) {
                                cookies.add(header.getValue());
                            }
                        }
                    }

                    //On a le token donc on demande le login
                    DataControler.getStream(getmActivity().getApplicationContext(), fluxCallback, cookies);
                }
            };

            final AsyncHttpClient.StringCallback tokenCallBackAsync = new AsyncHttpClient.StringCallback() {
                // Callback is invoked with any exceptions/errors, and the result, if available.
                @Override
                public void onCompleted(Exception e, AsyncHttpResponse response, String result) {
                    if (e != null) {
                        e.printStackTrace();
                        return;
                    }
                    if (result!=null && !result.isEmpty()){
                        result = result.replaceAll("[\r\n]+", "");
                        log.debug(FluxFragment.class.getName() + "Réponse du get login async sur le pod " + POD + "\n--------------------------\n" + result + "\n--------------------------");
                        Log.d(FluxFragment.class.getName(), "Réponse du get login async sur le pod " + POD + "\n--------------------------\n" + result + "\n--------------------------");
                    }
                    int indexTokenName = result.indexOf("<meta content=\"authenticity_token\" name=\"csrf-param\" />",0);
                    if (indexTokenName>0) {
                        int indexToken = result.indexOf("<meta content=\"", indexTokenName + 1);
                        indexToken = indexToken+1+"<meta content=\"".length();
                        int indexEndToken = result.indexOf("\" name=\"csrf-token\"", indexToken);
                        String token = result.substring(indexToken, indexEndToken);

                        log.debug(FluxFragment.class.getName() + "token récolté '" + token + "'");
                        Log.d(this.getClass().getName(),"token récolté '" + token + "'");

                        //On a le token donc on demande le login
                        ProfilControler.loginAsync("tilucifer", "Pikifou01", token, loginCallBackAsync);
                    }
                }
            };



            ProfilControler.getTokenAsync(tokenCallBackAsync);
//            ProfilControler.getToken(getmActivity(), tokenCallback);


            mAdapter = new PostAdapter(getActivity(), R.layout.fragment_flux_list, new ArrayList<Post>());
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
