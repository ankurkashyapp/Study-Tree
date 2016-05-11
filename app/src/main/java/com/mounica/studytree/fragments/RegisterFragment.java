package com.mounica.studytree.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mounica.studytree.R;
import com.mounica.studytree.api.response.UserResponse;
import com.mounica.studytree.models.User;
import com.mounica.studytree.utilities.ValidationTools;

/**
 * Created by ankur on 5/5/16.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener{

    private EditText regNo;
    private EditText name;
    private EditText age;
    private EditText email;
    private EditText contact;
    private EditText password;
    private EditText confirmPassword;

    private Button submitRegistration;

    private String registerRegNo;
    private String registerName;
    private String registerAge;
    private String registerEmail;
    private String registerContact;
    private String registerPassword;
    private String registerConfirmPassword;

    private ProgressDialog progressDialog;

    public static RegisterFragment newInstance() {
        RegisterFragment registerFragment = new RegisterFragment();
        return registerFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        initViews(view);

        return view;
    }

    private void initViews(View view) {
        regNo = (EditText)view.findViewById(R.id.register_reg_no);
        name = (EditText)view.findViewById(R.id.register_name);
        age = (EditText)view.findViewById(R.id.register_age);
        email = (EditText)view.findViewById(R.id.register_email);
        contact = (EditText)view.findViewById(R.id.register_contact);
        password = (EditText)view.findViewById(R.id.register_password);
        confirmPassword = (EditText)view.findViewById(R.id.register_confirm_password);

        submitRegistration = (Button)view.findViewById(R.id.submit_registration);

        submitRegistration.setOnClickListener(this);
    }

    private boolean isValidated() {
        registerRegNo = regNo.getText().toString();
        registerName = name.getText().toString();
        registerAge = age.getText().toString();
        registerEmail = email.getText().toString();
        registerContact = contact.getText().toString();
        registerPassword = password.getText().toString();
        registerConfirmPassword = confirmPassword.getText().toString();

        if (!ValidationTools.isValidName(registerName)) {
            name.setError("Enter a valid name");
            return false;
        }

        if (!ValidationTools.isValidRegNo(registerRegNo)) {
            regNo.setError("Enter valid reg no (Exactly 8 digits)");
            return false;
        }

        if (!ValidationTools.isValidAge(registerAge)) {
            age.setError("Enter a valid age");
            return false;
        }

        if (!ValidationTools.isValidEmail(registerEmail)) {
            email.setError("Enter a valid email address");
            return false;
        }

        if (!ValidationTools.isValidContact(registerContact)) {
            contact.setError("Enter a valid contact number (at least 10 digits)");
            return false;
        }

        if (!ValidationTools.isValidPassword(registerPassword)) {
            password.setError("At least 6 characters");
            return false;
        }

        if (!registerPassword.equals(registerConfirmPassword)) {
            password.setError("Both password should match each other");
            confirmPassword.setError("Both password should match each other");
            return false;
        }

        return true;
    }

    private void resetFields() {
        regNo.setText(null);
        name.setText(null);
        age.setText(null);
        email.setText(null);
        contact.setText(null);
        password.setText(null);
        confirmPassword.setText(null);
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Registering...Please wait.");
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

            User.createUser(getActivity(), Integer.parseInt(registerRegNo), registerName, Integer.parseInt(registerAge), registerEmail, registerContact, registerPassword, new User.UserCreated() {

                @Override
                public void onUserCreated(UserResponse userResponse) {
                    Toast.makeText(getActivity(), "Created user id is: "+userResponse.getUser_id(), Toast.LENGTH_SHORT).show();
                    hideProgressDialog();
                    resetFields();
                }

                @Override
                public void onUserNotCreated(String message) {
                    if (message.equals("This email already exists"))
                        email.setError(message);
                    else if (message.equals("This Reg No already exists"))
                        regNo.setError(message);

                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                    hideProgressDialog();
                }
            });
        }
    }
}
