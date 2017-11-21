package com.karacasoft.interestr.pages.signup;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.karacasoft.interestr.R;
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
    private TextView message;
    private View root;
    private ArrayList<User> users ;

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
        message= root.findViewById(R.id.tvSignupMsg);

        signup.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        validated(username.getText().toString(),email.getText().toString(),pass1.getText().toString(),pass2.getText().toString());
                        }
                }
        );

        return root;
    }

    private boolean validated(String username,String email, String pass1, String pass2){
        if (users.contains(new User(username))){//not sure
            message.setText("username is already taken.");
        }else if(!isEmailValid((CharSequence) email)){
            message.setText("invalid email.");
        }else if(pass1.length()<8){
            message.setText("password is too short.");
        }else if(!pass1.equals(pass2)){
            message.setText("passwords does not match.");
        }else{
            User newUser = new User(username,pass1,email);
            message.setText("User is successfully registered.");
            users.add(newUser);
            return true;
        }
        return false;
    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
