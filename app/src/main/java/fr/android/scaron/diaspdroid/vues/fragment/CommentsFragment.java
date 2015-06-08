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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import org.acra.ACRA;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import fr.android.scaron.diaspdroid.R;
import fr.android.scaron.diaspdroid.controler.DiasporaBean;
import fr.android.scaron.diaspdroid.controler.LogControler;
import fr.android.scaron.diaspdroid.model.Comment;
import fr.android.scaron.diaspdroid.vues.adapter.CommentsAdapter;


@EFragment(R.layout.fragment_comment_list)
public class CommentsFragment extends DialogFragment {

    private static Logger LOGGEUR = LoggerFactory.getLogger(CommentsFragment.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    private static String TAG = CommentsFragment.class.getSimpleName();


    @ViewById(R.id.fragment_comments_list)
    ListView commentList;
    @Bean
    DiasporaBean diasporaService;

    @Bean
    CommentsAdapter adapter;
    View footerView;

    List<Comment> comments;

    Fragment fragment;

    public void setFragmentParent(Fragment fragment){
        this.fragment = fragment;
    }

    @AfterViews
    void bindAdapter() {
        commentList.setAdapter(adapter);
        adapter.setFragmentParent(this);
        getDialog().setTitle("Commentaires");
    }

    @Background
    void getComments(Integer postID){
        addFooterView();
        comments = diasporaService.getComments(postID);
        updateAdapter();
    }

    @UiThread
    void addFooterView(){
        commentList.addFooterView(footerView);
    }
    @UiThread
    void updateAdapter(){
        if (footerView != null) {
            commentList.removeFooterView(footerView);
        }
        adapter.setComments(comments);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        String TAG_METHOD = TAG + ".onCreate : ";
        LOG.d(TAG_METHOD+ "Entrée");
        try{
            super.onCreate(savedInstanceState);
            footerView = ((LayoutInflater) getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.base_list_item_loading_footer, null, false);
            if (this.getArguments()!=null) {
                Integer postID = this.getArguments().getInt(Intent.EXTRA_UID);
                LOG.d(TAG_METHOD + "postID is null ? " + (postID == null));
                LOG.d(TAG_METHOD + "call getPodsService");
                getComments(postID);
            }
            LOG.d(TAG_METHOD+ "Sortie");
        } catch (Throwable thr) {
            LOG.e(TAG_METHOD+ "Erreur : " + thr.toString());
            ACRA.getErrorReporter().handleException(thr);
            LOG.d(TAG_METHOD+ "Sortie en erreur");
            throw thr;
        }
    }
}
