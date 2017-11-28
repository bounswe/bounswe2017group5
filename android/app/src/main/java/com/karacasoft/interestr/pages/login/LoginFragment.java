package com.karacasoft.interestr.pages.login;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.karacasoft.interestr.R;
import com.karacasoft.interestr.network.InterestrAPI;
import com.karacasoft.interestr.network.InterestrAPIImpl;
import com.karacasoft.interestr.network.InterestrAPIResult;
import com.karacasoft.interestr.network.models.Token;
import com.karacasoft.interestr.network.models.User;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private EditText username;
    private EditText password;
    private Button login;
    private View root;
    private ArrayList<User> users;
    private OnLoginSuccessfulListener onLoginSuccessfulListener;

    private InterestrAPI api;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_login, container, false);
        username=root.findViewById(R.id.etUsername);
        password = root.findViewById(R.id.etPassword);
        login = root.findViewById(R.id.btnLogin);

        login.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //validated(username.getText().toString(),password.getText().toString());
                        Log.d("Login OnClickListener","onClick Pressed");
                        api.login(username.getText().toString(),password.getText().toString(),loginCallBack);
                    }
                }
        );

        return root;
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = new InterestrAPIImpl(getContext());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        onLoginSuccessfulListener = (OnLoginSuccessfulListener) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        onLoginSuccessfulListener = null;
    }

    private InterestrAPI.Callback<Token>  loginCallBack = new InterestrAPI.Callback<Token>() {
        @Override
        public void onResult(InterestrAPIResult<Token> result) {
            if(onLoginSuccessfulListener != null) {
                onLoginSuccessfulListener.onLoginSuccessful(result.get());
            }
        }

        @Override
        public void onError(String error_message) {
            Log.d("LoginFragment Error", error_message);
        }
    };

    public void setOnLoginSuccessfulListener(OnLoginSuccessfulListener onLoginSuccessfulListener) {
        this.onLoginSuccessfulListener = onLoginSuccessfulListener;
    }

    public interface OnLoginSuccessfulListener{
        void onLoginSuccessful(Token token);
    }

}
