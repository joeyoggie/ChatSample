package com.elvesapp.chatsample;

import android.content.SharedPreferences;

import com.elvesapp.chatsample.entities.User;
import com.google.gson.Gson;
import com.lamudi.phonefield.Country;

public class MySettings {
    public static final String PREF_ACTIVE_USER = "pref_logged_in_user";
    public static final String PREF_ACTIVE_TRAINER = "pref_logged_in_trainer";
    public static final String PREF_ACTIVE_USER_TYPE = "pref_logged_in_user_type";
    public static final String PREF_USER_EMAIL = "pref_user_email";
    public static final String PREF_CURRNENT_ORDER = "pref_current_order";
    public static final String PREF_CURRNENT_CAR = "pref_current_car";
    public static final String PREF_SELECTED_SERVICES = "pref_selected_services";
    public static final String PREF_ACTIVE_LANGUAGE = "pref_active_language";
    public static final String PREF_ACTIVE_COUNTRY = "pref_active_country";
    public static final String PREF_ACTIVE_CITY_ID = "pref_active_city_id_country";
    public static final String PREF_INITIAL_STARTUP = "pref_initial_startup";
    public static final String PREF_INITIAL_STARTUP_LOGIN = "pref_initial_startup_logon";

    private static int loggedInUserType = -1;
    private static User loggedInUser;
    private static Country activeCountry;
    private static SharedPreferences sharedPref;
    private static Gson gson;
    private static long activeCityId = -1;

    private static String activeLanguage;
    private static boolean initialStartup;

    private MySettings(){

    }

    public static User getActiveUser() {
        if (loggedInUser != null) {
            return loggedInUser;
        } else {
            SharedPreferences prefs = getSettings();
            String json = prefs.getString(PREF_ACTIVE_USER, "");
            if (json.isEmpty() || json.equals("null")) {
                return null;
            } else {
                if(gson == null){
                    gson = new Gson();
                }
                loggedInUser = gson.fromJson(json, User.class);
                return loggedInUser;
            }
        }
    }

    public static void setActiveUser(User user) {
        MySettings.loggedInUser = user;

        if(gson == null){
            gson = new Gson();
        }
        String json = gson.toJson(MySettings.loggedInUser);
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(PREF_ACTIVE_USER, json);
        editor.apply();
    }

    public static SharedPreferences getSettings() {
        if(sharedPref == null){
            sharedPref = MyApp.getShardPrefs();
        }

        return sharedPref;
    }

    public static String getActiveLanguage(){
        if (activeLanguage != null && activeLanguage.length() >= 1) {
            return activeLanguage;
        } else {
            SharedPreferences prefs = getSettings();
            activeLanguage = prefs.getString(PREF_ACTIVE_LANGUAGE, "en");
            return activeLanguage;
        }
    }

    public static void setActiveLanguage(String language){
        MySettings.activeLanguage = language;

        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(PREF_ACTIVE_LANGUAGE, language);
        editor.apply();
    }

    public static boolean getInitialStartupState(){
        SharedPreferences prefs = getSettings();
        initialStartup = prefs.getBoolean(PREF_INITIAL_STARTUP, true);
        return initialStartup;
    }

    public static void setInitialStartupState(boolean state){
        MySettings.initialStartup = state;

        SharedPreferences.Editor editor = getSettings().edit();
        editor.putBoolean(PREF_INITIAL_STARTUP, state);
        editor.apply();
    }

    public static boolean getInitialStartupStateLogin(){
        SharedPreferences prefs = getSettings();
        initialStartup = prefs.getBoolean(PREF_INITIAL_STARTUP_LOGIN, true);
        return initialStartup;
    }

    public static void setInitialStartupStateLogin(boolean state){
        MySettings.initialStartup = state;

        SharedPreferences.Editor editor = getSettings().edit();
        editor.putBoolean(PREF_INITIAL_STARTUP_LOGIN, state);
        editor.apply();
    }

    public static Country getActiveCountry() {
        if (activeCountry != null) {
            return activeCountry;
        } else {
            SharedPreferences prefs = getSettings();
            String json = prefs.getString(PREF_ACTIVE_COUNTRY, "");
            if (json.isEmpty() || json.equals("null")) {
                return null;
            } else {
                if(gson == null){
                    gson = new Gson();
                }
                activeCountry = gson.fromJson(json, Country.class);
                return activeCountry;
            }
        }
    }

    public static void setActiveCountry(Country country) {
        MySettings.activeCountry = country;

        if(gson == null){
            gson = new Gson();
        }
        String json = gson.toJson(MySettings.activeCountry);
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(PREF_ACTIVE_COUNTRY, json);
        editor.apply();
    }

    public static void setActiveCityId(long cityId){
        MySettings.activeCityId = cityId;

        SharedPreferences.Editor editor = getSettings().edit();
        editor.putLong(PREF_ACTIVE_CITY_ID, activeCityId);
        editor.apply();
    }

    public static long getActiveCityId(){
        if (activeCityId != -1) {
            return activeCityId;
        } else {
            SharedPreferences prefs = getSettings();
            activeCityId = prefs.getLong(PREF_ACTIVE_CITY_ID, -1);
            return activeCityId;
        }
    }
}
