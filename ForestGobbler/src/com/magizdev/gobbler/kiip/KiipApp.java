package com.magizdev.gobbler.kiip;

import me.kiip.sdk.Kiip;
import android.app.Application;

public class KiipApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Kiip kiip = Kiip.init(this, "372fce3b751bb25c6a8d45f04f128abc",
				"a7a951bc7fb1f82613deaa6011bc66b2");
		Kiip.setInstance(kiip);
	}
}
