package com.mounica.studytree.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.mounica.studytree.R;
import com.mounica.studytree.models.Files;
import com.mounica.studytree.utilities.ValidationTools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ankur on 10/5/16.
 */
public class UploadImageActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private ImageView imagePreview;
    private ImageButton imageSelect;
    private EditText imageTitle;
    private EditText imageDescription;
    private Spinner imageSubject;
    private Spinner imageCategory;
    private Button submitImageupload;

    private File selectedImage = null;
    private String title;
    private String description;
    private int chosenSubject = 0;
    private int chosenCategory = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        initViews();
    }

    private void initViews() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        imagePreview = (ImageView)findViewById(R.id.image_preview);
        imageSelect = (ImageButton)findViewById(R.id.image_select);
        imageTitle = (EditText)findViewById(R.id.image_title);
        imageDescription = (EditText)findViewById(R.id.image_description);
        imageSubject = (Spinner)findViewById(R.id.image_subject);
        imageCategory = (Spinner)findViewById(R.id.image_category);
        submitImageupload = (Button)findViewById(R.id.submit_image_upload);
        toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        imageSelect.setOnClickListener(this);
        submitImageupload.setOnClickListener(this);
        setSupportActionBar(toolbar);
        setupSubjectSpinner();
        setupCategorySpinner();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setupSubjectSpinner() {
        List<String> subjects = new ArrayList<>();
        subjects.add("Please select a subject");
        subjects.add("C/C++");
        subjects.add("Java");
        subjects.add("Operating System");
        subjects.add("Data Structure");
        subjects.add("Algorithm");
        subjects.add("Database");
        subjects.add("Maths");
        subjects.add("Physics");
        subjects.add("Chemistry");
        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, subjects);
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        imageSubject.setAdapter(subjectAdapter);

        imageSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                chosenSubject = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                chosenSubject = 0;
            }
        });
    }

    private void setupCategorySpinner() {
        List<String> category = new ArrayList<>();
        category.add("Please select a category");
        category.add("Question Paper MTE");
        category.add("Question Paper ETE");
        category.add("Question Paper Class Test");
        category.add("Notes");
        category.add("Formulae");
        category.add("Tips and Tricks");

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, category);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        imageCategory.setAdapter(categoryAdapter);

        imageCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                chosenCategory = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private boolean isValid() {
        title = imageTitle.getText().toString();
        description = imageDescription.getText().toString();

        if (!ValidationTools.isValidImageFile(selectedImage)) {
            Toast.makeText(this, "Please select a image", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!ValidationTools.isValidImageTitle(title)) {
            imageTitle.setError("At least 8 characters");
            return false;
        }

        if (!ValidationTools.isValidImageDescription(description)) {
            imageDescription.setError("At least 10 characters");
            return false;
        }

        if (!ValidationTools.isValidSelectedDropDownItem(chosenSubject)) {
            Toast.makeText(this, "Please choose appropriate subject", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!ValidationTools.isValidSelectedDropDownItem(chosenCategory)) {
            Toast.makeText(this, "Please choose appropriate category", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        if (view == imageSelect) {
            selectImage();
        }
        else if (view == submitImageupload) {
            if (isValid()) {
                uploadImage(selectedImage);
            }
        }
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 111);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 111 && resultCode == AppCompatActivity.RESULT_OK) {

            File f = null;
            Uri selectedImageUri = data.getData();
            imagePreview.setImageURI(selectedImageUri);
            try {

                File file = new File(selectedImageUri.getPath());
                String strFileName = file.getName();
                InputStream is = getContentResolver().openInputStream(selectedImageUri);
                Bitmap bmp = BitmapFactory.decodeStream(is);
                f = new File(getCacheDir(), "StudyTree_"+strFileName+".jpg");
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
            selectedImage = f;
        }
    }

    private void uploadImage(File image) {
        Files.uploadImage(this, image, title, description, chosenSubject, chosenCategory, new Files.ImageUpload() {
            @Override
            public void onImageUploaded(String path) {
                Log.e("Image Url path", "http://192.168.1.5/ankur/uploads/"+path);
                //Picasso.with(MainActivity.this).load("http://192.168.1.5/ankur/uploads/"+path).into(view);
            }
        });
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
