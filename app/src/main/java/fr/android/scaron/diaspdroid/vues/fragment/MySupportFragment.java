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

import android.app.ActivityManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.Trace;
import org.androidannotations.annotations.Transactional;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import fr.android.scaron.diaspdroid.MainApplication;
import fr.android.scaron.diaspdroid.R;

//import org.androidannotations.test15.R;
//import org.androidannotations.test15.ebean.SomeBean;
//import org.androidannotations.test15.roboguice.SampleRoboApplication;

@EFragment(R.layout.injected)
public class MySupportFragment extends Fragment {

//	@Bean
//	SomeBean someBean;

	@ViewById(R.id.injected_text_view)
	TextView myTextView;

//	@App
//	SampleRoboApplication customApplication;
    @App
    MainApplication customApplication;

	@SystemService
	ActivityManager activityManager;

//	@Click
//	void myButton() {
//	}

	@UiThread
	void uiThread() {

	}

	@Trace
	void trace() {

	}

	@Transactional
	void successfulTransaction(SQLiteDatabase db) {
	}

	@AfterInject
	void calledAfterInjection() {

	}

	@AfterViews
	void calledAfterViewInjection() {
        myTextView.setText("MySupportFragement loaded");
	}

}
