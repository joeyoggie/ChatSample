package com.elvesapp.chatsample;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import java.util.Locale;

public class MyApp extends Application {
    private static MyApp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        /*CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Heebo-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );*/

        Configuration config = getBaseContext().getResources().getConfiguration();

        String lang = MySettings.getActiveLanguage();
        LocaleUtils.setLocale(new Locale(lang));
        LocaleUtils.updateConfig(this, getBaseContext().getResources().getConfiguration());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        /*if (locale != null)
        {
            newConfig.locale = locale;
            Locale.setDefault(locale);
            getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        }*/
        LocaleUtils.updateConfig(this, newConfig);
    }

    public static synchronized MyApp getInstance() {
        return mInstance;
    }

    public static SharedPreferences getShardPrefs(){
        SharedPreferences prefs = mInstance.getSharedPreferences(Constants.PACKAGE_NAME, Context.MODE_PRIVATE);
        return prefs;
    }
}
