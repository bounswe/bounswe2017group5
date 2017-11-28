package com.karacasoft.interestr.pages.signup;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.karacasoft.interestr.FloatingActionButtonHandler;
import com.karacasoft.interestr.FloatingActionsMenuHandler;
import com.karacasoft.interestr.R;
import com.karacasoft.interestr.network.InterestrAPI;
import com.karacasoft.interestr.network.InterestrAPIImpl;
import com.karacasoft.interestr.network.InterestrAPIResult;
import com.karacasoft.interestr.network.models.User;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {
    private EditText username;
    private EditText email;
    private EditText pass1;
    private EditText pass2;
    private Button signup;
    private View root;
    private ArrayList<User> users ;
    private OnSignupSuccessfulListener onSignupSuccessfulListener;
    private InterestrAPI api;

    private FloatingActionsMenuHandler famHandler;
    private FloatingActionButtonHandler fabHandler;

    public static SignUpFragment newInstance() {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root= inflater.inflate(R.layout.fragment_sign_up, container, false);
        username= root.findViewById(R.id.etName);
        email= root.findViewById(R.id.etEmail);
        pass1 = root.findViewById(R.id.etPass);
        pass2= root.findViewById(R.id.etConfirmPass);
        signup= root.findViewById(R.id.btnSignup);

        signup.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        api.signup(username.getText().toString(),email.getText().toString(),
                                pass1.getText().toString(),pass2.getText().toString(),signupCallBack);
                        }
                }
        );

        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = new InterestrAPIImpl(getContext());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fabHandler = (FloatingActionButtonHandler) context;
        famHandler = (FloatingActionsMenuHandler) context;

        fabHandler.hideFloatingActionButton();
        famHandler.hideFloatingActionsMenu();


    }

    private InterestrAPI.Callback<User> signupCallBack = new InterestrAPI.Callback<User>() {
        @Override
        public void onResult(InterestrAPIResult<User> result) {
            if(onSignupSuccessfulListener!=null){
                onSignupSuccessfulListener.onSignupSuccessful(result.get());
            }
        }

        @Override
        public void onError(String error_message) {

        }
    };

    public interface OnSignupSuccessfulListener{
        void onSignupSuccessful(User user);
    }

}
