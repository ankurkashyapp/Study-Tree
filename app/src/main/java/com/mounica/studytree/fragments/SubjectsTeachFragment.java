package com.mounica.studytree.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mounica.studytree.R;
import com.mounica.studytree.models.User;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by ankur on 11/5/16.
 */
public class SubjectsTeachFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{

    private ImageView userImage;

    private TextView userName;
    private TextView userRegNo;

    private CheckBox cCplus;
    private CheckBox java;
    private CheckBox operatingSystem;
    private CheckBox dataStructure;
    private CheckBox algorithm;
    private CheckBox database;
    private CheckBox maths;
    private CheckBox physics;
    private CheckBox chemistry;

    private Button userChangePic;
    private Button submitUpdateSubjects;

    private ArrayList<Integer> subjectIds;

    private ProgressDialog progressDialog;

    public static SubjectsTeachFragment newInstance() {
        SubjectsTeachFragment subjectsTeachFragment = new SubjectsTeachFragment();
        return subjectsTeachFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subject_teach, container, false);
        initViews(view);
        fillUserData();
        return view;
    }

    private void initViews(View view) {

        progressDialog = new ProgressDialog(getActivity());
        subjectIds = new ArrayList<>();

        userImage = (ImageView)view.findViewById(R.id.user_image_front);

        userName = (TextView)view.findViewById(R.id.user_name);
        userRegNo = (TextView)view.findViewById(R.id.user_reg_no);

        cCplus = (CheckBox)view.findViewById(R.id.c_cplus);
        java = (CheckBox)view.findViewById(R.id.java);
        operatingSystem = (CheckBox)view.findViewById(R.id.operating_system);
        dataStructure = (CheckBox)view.findViewById(R.id.data_structure);
        algorithm = (CheckBox)view.findViewById(R.id.algorithm);
        database = (CheckBox)view.findViewById(R.id.database);
        maths = (CheckBox)view.findViewById(R.id.maths);
        physics = (CheckBox)view.findViewById(R.id.physics);
        chemistry = (CheckBox)view.findViewById(R.id.chemistry);

        userChangePic = (Button)view.findViewById(R.id.edit_user_image);
        submitUpdateSubjects = (Button)view.findViewById(R.id.submit_update_subjects);

        userChangePic.setOnClickListener(this);
        submitUpdateSubjects.setOnClickListener(this);

        cCplus.setOnCheckedChangeListener(this);
        java.setOnCheckedChangeListener(this);
        operatingSystem.setOnCheckedChangeListener(this);
        dataStructure.setOnCheckedChangeListener(this);
        algorithm.setOnCheckedChangeListener(this);
        database.setOnCheckedChangeListener(this);
        maths.setOnCheckedChangeListener(this);
        physics.setOnCheckedChangeListener(this);
        chemistry.setOnCheckedChangeListener(this);
    }

    private void fillUserData() {
        Picasso.with(getActivity())
                .load(User.IMAGE_ROOT_PATH + User.getLoggedInUserImageLink(getActivity()))
                .into(userImage);
        userName.setText(User.getLoggedInUserName(getActivity()));
        userRegNo.setText(User.getLoggedInUserRegNo(getActivity()));
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
        else if (view == submitUpdateSubjects) {
            showProgressDialog("Please wait. We are updating your subject list...");

            User.updateSubjects(getActivity(), subjectIds, new User.UserSubjectsUpdate() {
                @Override
                public void onUpdated(String message) {
                    hideProgressDialog();
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
            });
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

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()) {
            case R.id.c_cplus:
                if (isChecked && !subjectIds.contains(1))
                    subjectIds.add(1);
                else
                    subjectIds.remove(Integer.valueOf(1));break;

            case R.id.java:
                if (isChecked && !subjectIds.contains(2))
                    subjectIds.add(2);
                else
                    subjectIds.remove(Integer.valueOf(2));break;

            case R.id.operating_system:
                if (isChecked && !subjectIds.contains(3))
                    subjectIds.add(3);
                else
                    subjectIds.remove(Integer.valueOf(3));break;

            case R.id.data_structure:
                if (isChecked && !subjectIds.contains(4))
                    subjectIds.add(4);
                else
                    subjectIds.remove(Integer.valueOf(4));break;

            case R.id.algorithm:
                if (isChecked && !subjectIds.contains(5))
                    subjectIds.add(5);
                else
                    subjectIds.remove(Integer.valueOf(5));break;

            case R.id.database:
                if (isChecked && !subjectIds.contains(6))
                    subjectIds.add(6);
                else
                    subjectIds.remove(Integer.valueOf(6));break;

            case R.id.maths:
                if (isChecked && !subjectIds.contains(7))
                    subjectIds.add(7);
                else
                    subjectIds.remove(Integer.valueOf(7));break;

            case R.id.physics:
                if (isChecked && !subjectIds.contains(8))
                    subjectIds.add(8);
                else
                    subjectIds.remove(Integer.valueOf(8));break;

            case R.id.chemistry:
                if (isChecked && !subjectIds.contains(9))
                    subjectIds.add(9);
                else
                    subjectIds.remove(Integer.valueOf(9));break;
        }
    }


}
