/**
 * Copyright (C) 2010-2013 eBusiness Information, Excilys Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed To in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package fr.android.scaron.diaspdroid.vues.fragment;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.activity.MainActivity;
import fr.android.scaron.diaspdroid.activity.MainActivity_;
import fr.android.scaron.diaspdroid.controler.DiasporaBean;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.model.DiasporaConfig;
import fr.android.scaron.diaspdroid.model.Pod;


@EFragment(R.layout.fragment_params)
public class ParamsFragment extends Fragment {

    private static Logger LOGGEUR = LoggerFactory.getLogger(ParamsFragment.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    private static String TAG = ParamsFragment.class.getSimpleName();

    @ViewById(R.id.poduser)
    TextView poduser;
    @ViewById(R.id.podpassword)
    TextView podpassword;
    @ViewById(R.id.podselected)
    TextView podselected;
    @ViewById(R.id.podBtnSelect)
    Button podBtnSelect;

    PodsFragment dialogFragment;

    @Bean
    DiasporaBean diasporaBean;

    ActionBarActivity activity;

    public void setActivityParent(ActionBarActivity activity){
        this.activity = activity;
    }

    @Click(R.id.podConfigOK)
    @Background
    void podConfigOKClicked() {
        String methodName = ".podConfigOKClicked : ";
        LOG.d(methodName + "Entrée");
        final String user = poduser.getText().toString();
        final String password = podpassword.getText().toString();
        DiasporaConfig.setPodAuthenticationValues(user, password);
        showResultLogin(diasporaBean.seLogguer());
        LOG.d(methodName + "Sortie");
    }


    @UiThread
    public void showResultLogin(boolean loginOK){
        String methodName = ".showResultLogin : ";
        LOG.d(methodName + "Entrée");
        if (loginOK){
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DiasporaConfig.APPLICATION_CONTEXT);
            final AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, final int which) {
                    String methodName = ".showResultLogin/loginOK/onClick : ";
                    LOG.d(methodName + "Entrée");
                    DiasporaConfig.ParamsOK = true;
                    DiasporaConfig.DB.putBoolean("configOK", true);
                    LOG.d(methodName + "Params to true");

                    LOG.d(methodName + "main activity type is " + activity.getClass().getName());
                    if (activity instanceof MainActivity_) {
                        LOG.d(methodName + "call set defaultView on MainActivity_");
                        ((MainActivity_) activity).setDefaultView();
                    } else if (activity instanceof MainActivity) {
                        LOG.d(methodName + "call set defaultView on MainActivity");
                        ((MainActivity) activity).setDefaultView();
                    }
                    alertDialog.dismiss();
                }
            });
            alertDialog.setIcon(R.drawable.ic_launcher);
            alertDialog.setTitle("Connexion réussie");
            alertDialog.setMessage("Vos paramètres sont correctes.\nBon surf sur Diaspora !");
            alertDialog.show();
            LOG.d(methodName + "Sortie");
            return;
        }
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DiasporaConfig.APPLICATION_CONTEXT);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                String methodName = ".showResultLogin/loginKO/onClick : ";
                LOG.d(methodName + "Entrée");
                DiasporaConfig.ParamsOK = false;
                DiasporaConfig.DB.putBoolean("configOK", false);
                LOG.d(methodName + "Params to false");
                alertDialog.dismiss();
            }
        });
        alertDialog.setIcon(R.drawable.ic_launcher);
        alertDialog.setTitle("PB Connexion");
        alertDialog.setMessage("La connexion a Diaspora a échouée");
        alertDialog.show();
        LOG.d(methodName + "Sortie en erreur");
    }

    @Click(R.id.podBtnSelect)
    void showDialogSelectPod(){
        dialogFragment = new PodsFragment_();
        dialogFragment.setFragmentParent(this);
        dialogFragment.show(getFragmentManager(), "podSelection");
    }
    void validSelectedPod(Pod pod){
        podselected.setText(pod.getDomain());
        if (dialogFragment!=null) {
            dialogFragment.dismiss();
        }
    }

    @AfterViews
    void updateBarIcon() {
        DiasporaConfig.addActivity(getActivity());
        String methodName = ".updateBarIcon : ";
        LOG.d(methodName + "Entrée");
        LOG.d(methodName + "getActionBar");
        ActionBar actionBar = getActivity().getActionBar();
        if (actionBar != null) {
            LOG.d(methodName + "actionBar.setTitle");
            actionBar.setTitle("Paramètres");
        }
        LOG.d(methodName + "Entrée");
    }

    @AfterViews
    void updateViewValues(){
        poduser.setText(DiasporaConfig.POD_USER);
        podpassword.setText(DiasporaConfig.POD_PASSWORD);
        podselected.setText(DiasporaConfig.POD_URL);
    }
}