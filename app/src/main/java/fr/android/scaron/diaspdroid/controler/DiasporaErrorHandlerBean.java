package fr.android.scaron.diaspdroid.controler;

import org.androidannotations.annotations.EBean;
import org.androidannotations.api.rest.RestErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NestedRuntimeException;

/**
 * Created by Sébastien on 23/03/2015.
 */
@EBean
public class DiasporaErrorHandlerBean implements RestErrorHandler {
    private static Logger LOGGEUR = LoggerFactory.getLogger(DiasporaBean.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    private static String TAG = "DiasporaBean";

    @Override
    public void onRestClientExceptionThrown(NestedRuntimeException e) {
        String TAG_METHOD = TAG + ".seLogguer : ";
        LOG.d(TAG_METHOD+ "Entrée");
        LOG.e("Exception rencontrée : " + e.getMessage(), e);
        LOG.d(TAG_METHOD+ "Sortie");
    }
}
