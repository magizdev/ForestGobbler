/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.magizdev.babyoneday.profilewizard;

import android.content.Context;

import com.example.android.wizardpager.wizard.model.AbstractWizardModel;
import com.example.android.wizardpager.wizard.model.PageList;

public class ProfileWizardModel extends AbstractWizardModel {
	public static final String STEP_NAME = "NAME";
	public static final String STEP_GENDER = "GENDER";
	public static final String STEP_BIRTHDATA = "BIRTHDATA";
	public static final String STEP_AVATAR = "AVATAR";

	public ProfileWizardModel(Context context) {
		super(context);
	}

	@Override
	protected PageList onNewRootPageList() {
		return new PageList(new NamePage(this, STEP_NAME), new GenderPage(this,
				STEP_GENDER), new BirthDataPage(this, STEP_BIRTHDATA),
				new AvatarPage(this, STEP_AVATAR)
		);
	}
}
