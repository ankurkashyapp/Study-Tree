package com.mounica.studytree.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mounica.studytree.R;
import com.mounica.studytree.api.response.UserResponse;
import com.mounica.studytree.models.Files;
import com.mounica.studytree.models.User;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by ankur on 11/5/16.
 */
public class ProfileEditFragment extends Fragment implements View.OnClickListener{

    private ImageView userImage;

    private TextView userName;
    private TextView userRegNo;

    private EditText editUserName;
    private EditText editUserAge;
    private EditText editUserEmail;
    private EditText editUserContact;
    private EditText editUserPwd;
    private EditText editUserPwdConfirm;

    private Button userChangePic;
    private Button submitUpdateProfile;

    private ProgressDialog progressDialog;


    public static ProfileEditFragment newInstance() {
        ProfileEditFragment profileEditFragment = new ProfileEditFragment();
        return profileEditFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_edit, container, false);
        initViews(view);
        fillUserData();
        return view;
    }

    private void initViews(View view) {
        userImage = (ImageView)view.findViewById(R.id.user_image_front);

        userName = (TextView)view.findViewById(R.id.user_name);
        userRegNo = (TextView)view.findViewById(R.id.user_reg_no);

        editUserName = (EditText)view.findViewById(R.id.edit_user_name);
        editUserAge = (EditText)view.findViewById(R.id.edit_user_age);
        editUserEmail = (EditText)view.findViewById(R.id.edit_user_email);
        editUserContact = (EditText)view.findViewById(R.id.edit_user_contact);
        editUserPwd = (EditText)view.findViewById(R.id.edit_user_password);
        editUserPwdConfirm = (EditText)view.findViewById(R.id.edit_user_password_confirm);

        userChangePic = (Button)view.findViewById(R.id.edit_user_image);
        submitUpdateProfile = (Button)view.findViewById(R.id.submit_update_profile);

        progressDialog = new ProgressDialog(getActivity());

        userChangePic.setOnClickListener(this);
        submitUpdateProfile.setOnClickListener(this);
    }

    private void fillUserData() {
        Picasso.with(getActivity())
                .load(User.IMAGE_ROOT_PATH + User.getLoggedInUserImageLink(getActivity()))
                .into(userImage);
        userName.setText(User.getLoggedInUserName(getActivity()));
        userRegNo.setText(User.getLoggedInUserRegNo(getActivity()));

        editUserName.setText(User.getLoggedInUserName(getActivity()));
        editUserAge.setText(User.getLoggedInUserAge(getActivity()));
        editUserEmail.setText(User.getLoggedInUserEmail(getActivity()));
        editUserContact.setText(User.getLoggedInUserContact(getActivity()));
        editUserPwd.setText(User.getLoggedInUserPassword(getActivity()));
        editUserPwdConfirm.setText(User.getLoggedInUserPassword(getActivity()));
    }

    private void showProgressDialog(String message) {
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    private void hideProgressDialog() {
        progressDialog.dismiss();
    }


    @Override
    public void onClick(View view) {
        if (view == userChangePic) {
            selectImage();
        }
        else if (view == submitUpdateProfile) {

        }
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 111);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 111 && resultCode == AppCompatActivity.RESULT_OK) {

            File f = null;
            Uri selectedImageUri = data.getData();

            try {

                File file = new File(selectedImageUri.getPath());
                String strFileName = file.getName();
                InputStream is = getActivity().getContentResolver().openInputStream(selectedImageUri);
                Bitmap bmp = BitmapFactory.decodeStream(is);
                f = new File(getActivity().getCacheDir(), "StudyTree_"+User.getLoggedInUserId(getActivity())+"_"+strFileName+".jpg");
                OutputStream output = new FileOutputStream(f);
                bmp.compress(Bitmap.CompressFormat.JPEG, 40, output);

                byte[] buffer = new byte[4 * 1024]; // or other buffer size
                int read;

                while ((read = is.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            uploadImage(f);
        }
    }

    private void uploadImage(File image) {
        showProgressDialog("Please wait. Your profile pic is being updated...");
        User.updateProfilePic(getActivity(), image, new User.UserProfilePicUpdate() {
            @Override
            public void onUpdatedSuccess(String path) {
                hideProgressDialog();
                Toast.makeText(getActivity(), "Profile pic updated successfully", Toast.LENGTH_SHORT).show();
                SharedPreferences credentials = getActivity().getSharedPreferences(User.USER_CREDENTIALS, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = credentials.edit();
                editor.putString(User.USER_IMAGE_LINK, path);
                editor.commit();
                fillUserData();
            }

            @Override
            public void onUpdatedFailure(String message) {
                hideProgressDialog();
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
