package com.elvesapp.chatsample;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Locale;

public class Utils {
    public static final int ANIMATION_TYPE_TRANSLATION = 0;
    public static final int ANIMATION_TYPE_FADE = 1;


    public static FragmentTransaction setAnimations(FragmentTransaction originalFragmentTransaction, int animationType){
        switch (animationType){
            case ANIMATION_TYPE_TRANSLATION:
                if(MySettings.getActiveLanguage().equals("ar")){
                    originalFragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
                }else{
                    originalFragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                }
                return originalFragmentTransaction;
            case ANIMATION_TYPE_FADE:
                originalFragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
                return originalFragmentTransaction;

            default:
                return originalFragmentTransaction;
        }
    }

    public static String getImageUrl(String imageUrl){
        String fullUrl = "";
        if(imageUrl.startsWith(Constants.DOMAIN_URL)){
            fullUrl = imageUrl;
        }else{
            fullUrl = Constants.DOMAIN_URL + imageUrl;
        }

        return fullUrl;
    }

    public static void setGridViewHeightBasedOnChildren(GridView gridView, int columns) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int items = listAdapter.getCount();
        int rows = 0;

        View listItem = listAdapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();

        float x = 1;
        if( items > columns ){
            x = items/columns;
            rows = (int) (x + 1);
            totalHeight *= rows;
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);
    }

    public static void justifyListViewHeightBasedOnChildren (ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        if (adapter == null) {
            return;
        }

        ViewGroup vg = listView;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }

    public static JSONObject parseTypedBoolean(JSONObject jsonObject, String... keys){
        try{
            for (String key : keys) {
                //Log.d("parsing", "checking key: " + key);
                if(jsonObject.has(key)) {
                    //Log.d("parsing", "key: " + key + " available");
                    if (jsonObject.getInt(key) == 1) {
                        jsonObject.put(key, true);
                        //Log.d("parsing", "key: " + key + " changed successfully");
                    } else if (jsonObject.getInt(key) == 0) {
                        jsonObject.put(key, false);
                        //Log.d("parsing", "key: " + key + " changed successfully");
                    }
                }else{
                    //Log.d("parsing", "key: " + key + " not available, might be a child");
                    JSONArray jsonObjectKeys = jsonObject.names();
                    int jsonObjectKeysSize = jsonObjectKeys.length();
                    for (int i = 0; i < jsonObjectKeysSize; i++) {
                        String currentKey = jsonObjectKeys.getString(i); // Here's your key
                        //String value = jsonObject.getString (key); // Here's your value

                        //Log.d("parsing", "key: " + currentKey + " is of type:"  + jsonObject.get(currentKey).getClass().getName());
                        if(jsonObject.get(currentKey).getClass().getName().toLowerCase().contains("array")){
                            if(jsonObject.getJSONArray(currentKey).length() >= 1){
                                //Log.d("parsing", "key: " + currentKey + " is an array");
                                JSONArray jsonArray = jsonObject.getJSONArray(currentKey);
                                int size = jsonArray.length();
                                for(int x = 0; x < size; x++){
                                    if(jsonArray.get(x).getClass().getName().toLowerCase().contains("object")){
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(x);
                                        jsonArray.put(x, parseTypedBoolean(jsonObject1, keys));
                                    }
                                }
                                jsonObject.put(currentKey, jsonArray);
                            }
                        }
                        if(jsonObject.get(currentKey).getClass().getName().toLowerCase().contains("object")){
                            if(jsonObject.getJSONObject(currentKey).names().length() >= 1){
                                //Log.d("parsing", "key: " + currentKey + " is a json object");
                                jsonObject.put(currentKey, parseTypedBoolean(jsonObject.getJSONObject(currentKey), keys));
                            }
                        }
                    }
                }
            }
        }catch (JSONException e){
            Log.d("parsing", "exception: " + e.getMessage());
        }

        return jsonObject;
    }

    public static double getNearedDouble(double originalDouble){
        double value = 0.0;
        //value = new BigDecimal(originalDouble).setScale(3, RoundingMode.HALF_UP).doubleValue();

        DecimalFormat decimalFormat = new DecimalFormat("#.###");
        decimalFormat.setRoundingMode(RoundingMode.UP);
        value = Double.valueOf(decimalFormat.format(originalDouble));
        return value;
    }

    public static String getDayName(int dayID){
        String dayName = "";
        switch (dayID) {
            case 1:
                dayName = "Sunday";
                break;
            case 2:
                dayName = "Monday";
                break;
            case 3:
                dayName = "Tuesday";
                break;
            case 4:
                dayName = "Wednesday";
                break;
            case 5:
                dayName = "Thursday";
                break;
            case 6:
                dayName = "Friday";
                break;
            case 7:
                dayName = "Saturday";
                break;
        }
        return dayName;
    }

    public static String getMonthName(int monthID){
        String monthName = "";
        switch (monthID) {
            case 0:
                monthName = "January";
                break;
            case 1:
                monthName = "February";
                break;
            case 2:
                monthName = "March";
                break;
            case 3:
                monthName = "April";
                break;
            case 4:
                monthName = "May";
                break;
            case 5:
                monthName = "June";
                break;
            case 6:
                monthName = "July";
                break;
            case 7:
                monthName = "August";
                break;
            case 8:
                monthName = "September";
                break;
            case 9:
                monthName = "October";
                break;
            case 10:
                monthName = "November";
                break;
            case 11:
                monthName = "December";
                break;
        }
        return monthName;
    }

    /**
     * Get ISO 3166-1 alpha-2 country code for this device (or null if not available)
     * @param context Context reference to get the TelephonyManager instance from
     * @return country code or null
     */
    public static String getUserCountry(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                return simCountry.toUpperCase(Locale.US);
            }
            else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    return networkCountry.toUpperCase(Locale.US);
                }
            }
        }
        catch (Exception e) { }
        return null;
    }

    public static boolean validateInputs(EditText... editTexts){
        boolean inputsValid = true;

        for (EditText editText:editTexts) {
            if(editText == null || editText.getText().toString() == null || editText.getText().toString().length() < 1){
                inputsValid = false;
                YoYo.with(Techniques.Shake)
                        .duration(700)
                        .repeat(1)
                        .playOn(editText);
            }
        }

        return inputsValid;
    }

    public static void showErrorIfFound(JSONObject errorMessages, String errorKey, EditText editText){
        if(errorMessages != null){
            try{
                if(errorMessages.has(errorKey)){
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .repeat(1)
                            .playOn(editText);
                    String errorMessage = errorMessages.getJSONArray(errorKey).getString(0);
                    editText.setError(errorMessage);
                }
            }catch (JSONException e){
                Log.d("Utils", "Json exception: " + e.getMessage());
            }

        }
    }

    public static String getDateString(long timestamp){
        String dateString = "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        dateString = day + "/" + month + "/" + year;

        return dateString;
    }

    public static String getTimeString(long timestamp){
        String timeString = "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        timeString = hour + ":" + minute;
        return timeString;
    }
}
