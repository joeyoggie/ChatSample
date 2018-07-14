package com.elvesapp.chatsample.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    private static final String TAG = LoginFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;
    private static final String EMAIL = "email";
    CallbackManager callbackManager;

    EditText emailEditText, passwordEditText;
    Button loginButton, signupButton;
    LoginButton facebookLoginButton;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        emailEditText = view.findViewById(R.id.login_email_edittext);
        passwordEditText = view.findViewById(R.id.login_password_edittext);
        loginButton = view.findViewById(R.id.login_button);
        facebookLoginButton = view.findViewById(R.id.facebook_login_button);
        signupButton = view.findViewById(R.id.signup_button);

        facebookLoginButton.setReadPermissions(Arrays.asList(EMAIL));
        facebookLoginButton.setFragment(this);

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        loginFacebook(loginResult.getAccessToken().getToken());
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction = Utils.setAnimations(fragmentTransaction, Utils.ANIMATION_TYPE_TRANSLATION);
                SignupFragment signupFragment = new SignupFragment();
                fragmentTransaction.replace(R.id.fragment_view, signupFragment, "signupFragment");
                fragmentTransaction.addToBackStack("signupFragment");
                fragmentTransaction.commit();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utils.validateInputs(emailEditText, passwordEditText)){
                    login(emailEditText.getText().toString(), passwordEditText.getText().toString());
                }
            }
        });
        return view;
    }

    private void login(final String email, final String password){
        String url = Constants.LOGIN_URL;

        final CustomProgressDialog customProgressDialog = CustomProgressDialog.show(getActivity(), "", "");

        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put(Constants.PARAMETER_EMAIL, email);
            jsonObject.put(Constants.PARAMETER_PASSWORD, password);
        }catch (JSONException e){

        }

        Log.d(TAG,  "login URL: " + url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loginButton.setEnabled(true);
                Log.d(TAG, "login response: " + response);

                if(response != null && response.has(Constants.PARAMETER_ERROR)){
                    try{
                        emailEditText.setError(response.getString(Constants.PARAMETER_ERROR));
                        passwordEditText.setError(response.getString(Constants.PARAMETER_ERROR));
                    }catch (JSONException e){

                    }
                }else{
                    Gson gson = new Gson();
                    Type type = new TypeToken<User>(){}.getType();
                    User user;
                    user = gson.fromJson(response.toString(), type);
                    MySettings.setActiveUser(user);
                }

                if (customProgressDialog != null) customProgressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loginButton.setEnabled(true);
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

    private void loginFacebook(final String accessToken){
        String url = Constants.LOGIN_URL;

        final CustomProgressDialog customProgressDialog = CustomProgressDialog.show(getActivity(), "", "");

        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put(Constants.PARAMETER_FACEBOOK_ACCESS_TOKEN, accessToken);
        }catch (JSONException e){

        }


        Log.d(TAG,  "loginFacebook URL: " + url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loginButton.setEnabled(true);
                Log.d(TAG, "login response: " + response);

                if (customProgressDialog != null) customProgressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loginButton.setEnabled(true);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
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
