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
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.widget.Button;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.DiasporaControler;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.model.DiasporaConfig;


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

    @Click(R.id.podConfigOK)
    void podConfigOKClicked() {
        String methodName = ".updateBookmarksClicked : ";
        LOG.d(methodName + "Entrée");
        final String user = poduser.getText().toString();
        final String password = podpassword.getText().toString();
        DiasporaConfig.setPodAuthenticationValues(user, password);
        DiasporaControler.testerConnexion();
        LOG.d(methodName + "Sortie");
    }

    @Click(R.id.podBtnSelect)
    void showDialogSelectPod(){
        PodsFragment dialogFragment = new PodsFragment_();
        dialogFragment.show(getFragmentManager(),"podSelection");
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
}