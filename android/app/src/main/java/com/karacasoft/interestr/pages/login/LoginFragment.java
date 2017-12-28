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

import com.karacasoft.interestr.ErrorHandler;
import com.karacasoft.interestr.InterestrApplication;
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
    private Button signUp;
    private View root;
    private OnLoginFragmentInteractionListener onLoginFragmentInteractionListener;

    private InterestrAPI api;
    private ErrorHandler errorHandler;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_login, container, false);
        username=root.findViewById(R.id.etUsername);
        password = root.findViewById(R.id.etPassword);
        login = root.findViewById(R.id.btnLogin);
        signUp = root.findViewById(R.id.btnSignUp);

        login.setOnClickListener(
                (view) -> api.login(username.getText().toString(),password.getText().toString(),loginCallBack)
        );

        signUp.setOnClickListener((view) -> onLoginFragmentInteractionListener.onSignUpPressed());

        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        errorHandler = (ErrorHandler) context;

        onLoginFragmentInteractionListener = (OnLoginFragmentInteractionListener) getActivity();
        api = ((InterestrApplication) getActivity().getApplication()).getApi();

        if(api.isLoggedIn()) {
            onLoginFragmentInteractionListener.onLoginSuccessful(null);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        onLoginFragmentInteractionListener = null;
    }

    private InterestrAPI.Callback<Token>  loginCallBack = new InterestrAPI.Callback<Token>() {
        @Override
        public void onResult(InterestrAPIResult<Token> result) {
            if(onLoginFragmentInteractionListener != null) {
                api.authenticate(result.get());
                onLoginFragmentInteractionListener.onLoginSuccessful(result.get());
            }
        }

        @Override
        public void onError(String error_message) {
            errorHandler.onError(error_message);
        }
    };

    public interface OnLoginFragmentInteractionListener {
        void onLoginSuccessful(Token token);
        void onSignUpPressed();
    }
}
