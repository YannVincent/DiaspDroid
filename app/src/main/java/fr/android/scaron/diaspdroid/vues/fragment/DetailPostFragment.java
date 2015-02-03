package fr.android.scaron.diaspdroid.vues.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.android.scaron.diaspdroid.controler.LogControler;

/**
 * Created by CARON-08651 on 03/02/2015.
 */
public class DetailPostFragment extends Fragment {
    private static Logger LOGGEUR = LoggerFactory.getLogger(DetailPostFragment.class);
    private static LogControler LOG = LogControler.getInstance(LOGGEUR);
    private WebView webview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LOG.d(".onCreateView entree with savedInstanceState : "+savedInstanceState);
        webview = new WebView(getActivity());
        LOG.d(".onCreateView sortie with webview : "+webview);
        return webview;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LOG.d(".onActivityCreated entree with savedInstanceState : "+savedInstanceState);
        Bundle bundle = getArguments();
        LOG.d(".onActivityCreated bundle is null ? : "+(bundle==null));
        if (bundle != null) {
            LOG.d(".onActivityCreated setText with text : "+savedInstanceState);
            setText(bundle.getString("link"));
        }
        LOG.d(".onActivityCreated entree with savedInstanceState : "+savedInstanceState);
    }

    public void setText(String item) {
        LOG.d(".setText entree with text : "+item);
        if (webview!=null) {
            LOG.d(".setText setInitialScale");
            webview.setInitialScale(50);
            LOG.d(".setText loadUrl");
            webview.loadUrl(item);
        }else{
            LOG.d(".setText not possible webview not found");
        }
        LOG.d(".setText sortie");
    }
}
