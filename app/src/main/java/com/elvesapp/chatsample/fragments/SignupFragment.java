package com.elvesapp.chatsample.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.elvesapp.chatsample.Constants;
import com.elvesapp.chatsample.CustomProgressDialog;
import com.elvesapp.chatsample.HttpConnector;
import com.elvesapp.chatsample.MySettings;
import com.elvesapp.chatsample.R;
import com.elvesapp.chatsample.Utils;
import com.elvesapp.chatsample.entities.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignupFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignupFragment extends Fragment {
    private static final String TAG = SignupFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;

    EditText nameEditText, emailEditText, passwordEditText;
    Button signupButton;

    public SignupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignupFragment.
     */
    public static SignupFragment newInstance(String param1, String param2) {
        SignupFragment fragment = new SignupFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        nameEditText = view.findViewById(R.id.signup_name_edittext);
        emailEditText = view.findViewById(R.id.signup_email_edittext);
        passwordEditText = view.findViewById(R.id.signup_password_edittext);
        signupButton = view.findViewById(R.id.signup_button);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utils.validateInputs(nameEditText, emailEditText, passwordEditText)){
                    signup(nameEditText.getText().toString(), emailEditText.getText().toString(), passwordEditText.getText().toString());
                }
            }
        });

        return view;
    }

    private void signup(String name, String email, String password){
        String url = Constants.SIGN_UP;

        final CustomProgressDialog customProgressDialog = CustomProgressDialog.show(getActivity(), "", "");

        JSONObject jsonObject = new JSONObject();
        JSONObject userJsonObject = new JSONObject();
        JSONObject loginEmailAttributes = new JSONObject();
        try{
            loginEmailAttributes.put(Constants.PARAMETER_CONTENT, email);

            jsonObject.put(Constants.PARAMETER_NAME, name);
            jsonObject.put(Constants.PARAMETER_PASSWORD, password);
            jsonObject.put(Constants.PARAMETER_LOGIN_EMAIL_ATTRIBUTES, loginEmailAttributes.toString());

            userJsonObject.put(Constants.PARAMETER_USER, jsonObject.toString());
        }catch (JSONException e){

        }

        Log.d(TAG,  "signup URL: " + url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                signupButton.setEnabled(true);
                Log.d(TAG, "signup response: " + response);

                if(response != null && response.has(Constants.PARAMETER_ERRORS)){
                    try{
                        JSONArray errorsJsonArray = response.getJSONArray(Constants.PARAMETER_ERRORS);
                        int numberOfErrors = errorsJsonArray.length();
                        if (errorsJsonArray != null) {
                            for (int i=0; i<numberOfErrors; i++){
                                String errorMessage = errorsJsonArray.getString(i);
                                if(errorMessage.contains(Constants.PARAMETER_NAME)){
                                    nameEditText.setError(errorMessage);
                                }
                                if(errorMessage.contains(Constants.PARAMETER_EMAIL)) {
                                    emailEditText.setError(errorMessage);
                                }
                                if(errorMessage.contains(Constants.PARAMETER_PASSWORD)) {
                                    passwordEditText.setError(errorMessage);
                                }
                            }
                        }
                    }catch (JSONException e){

                    }
                }else {
                    Gson gson = new Gson();
                    Type type = new TypeToken<User>() {
                    }.getType();
                    User user;
                    user = gson.fromJson(response.toString(), type);
                    MySettings.setActiveUser(user);
                }

                if (customProgressDialog != null) customProgressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                signupButton.setEnabled(true);
                if (customProgressDialog != null) customProgressDialog.dismiss();
                Log.d(TAG, "Volley Error: " + error.getMessage());
                Toast.makeText(getActivity(), getString(R.string.server_connection_error), Toast.LENGTH_SHORT).show();
            }
        }){
            /*@Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //params.put(Constants.PARAMETER_FACEBOOK_ACCESS_TOKEN, accessToken);

                return params;
            }*/
            @Override
            public Map<String,String> getHeaders(){
                Map<String,String> params = new HashMap<String, String>();
                //params.put("X-Requested-With", "XMLHttpRequest");
                //params.put(Constants.PARAMETER_LANGUAGE, MySettings.getActiveLanguage());

                return params;
            }
        };
        request.setShouldCache(false);
        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        HttpConnector.getInstance(getActivity()).addToRequestQueue(request);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
