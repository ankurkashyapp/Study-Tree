package com.mounica.studytree.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mounica.studytree.R;
import com.mounica.studytree.activity.MainActivity;
import com.mounica.studytree.api.response.UserResponse;
import com.mounica.studytree.models.User;
import com.mounica.studytree.utilities.ValidationTools;

/**
 * Created by ankur on 5/5/16.
 */
public class LoginFragment extends Fragment implements View.OnClickListener{

    private EditText regNo;
    private EditText password;

    private Button submitLogin;

    private String loginRegNo;
    private String loginPassword;

    private ProgressDialog progressDialog;

    public static LoginFragment newInstance() {
        LoginFragment loginFragment = new LoginFragment();
        return loginFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        regNo = (EditText)view.findViewById(R.id.login_reg_no);
        password = (EditText)view.findViewById(R.id.login_password);

        submitLogin = (Button)view.findViewById(R.id.submit_login);

        submitLogin.setOnClickListener(this);
    }

    private boolean isValidated() {
        loginRegNo = regNo.getText().toString();
        loginPassword = password.getText().toString();

        if (!ValidationTools.isValidRegNo(loginRegNo)) {
            regNo.setError("Enter valid reg no (Exactly 8 digits)");
            return false;
        }

        if (!ValidationTools.isValidPassword(loginPassword)) {
            password.setError("At least 6 characters");
            return false;
        }

        return true;
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Logging In...Please wait.");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void onClick(View view) {
        if (isValidated()) {
            showProgressDialog();
            User.loginUser(getActivity(), Integer.parseInt(loginRegNo), loginPassword, new User.UserLogin() {
                @Override
                public void onUserLoginSuccess(UserResponse userResponse) {
                    saveLoggedInUserData(userResponse);
                    Toast.makeText(getActivity(), "Successfully logged In", Toast.LENGTH_SHORT).show();
                    hideProgressDialog();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    getActivity().finish();
                }

                @Override
                public void onUserLoginFailed(String message) {
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    hideProgressDialog();
                }
            });
        }
    }

    private void saveLoggedInUserData(UserResponse userResponse) {
        SharedPreferences credentials = getActivity().getSharedPreferences(User.USER_CREDENTIALS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = credentials.edit();
        editor.putInt(User.USER_ID, Integer.parseInt(userResponse.getUser_id()));
        editor.putString(User.USER_REG_NO, userResponse.getReg_no());
        editor.putString(User.USER_NAME, userResponse.getName());
        editor.putString(User.USER_AGE, userResponse.getAge());
        editor.putString(User.USER_EMAIL, userResponse.getEmail());
        editor.putString(User.USER_CONTACT, userResponse.getContact());
        editor.putString(User.USER_PASSWORD, userResponse.getPassword());
        editor.putString(User.USER_IMAGE_LINK, userResponse.getImage_path());
        editor.commit();
    }
}
