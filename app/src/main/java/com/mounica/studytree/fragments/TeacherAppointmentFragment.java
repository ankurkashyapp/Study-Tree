package com.mounica.studytree.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mounica.studytree.R;
import com.mounica.studytree.activity.MainActivity;
import com.mounica.studytree.api.response.SubjectsResponse;
import com.mounica.studytree.api.response.UserCreatedResponse;
import com.mounica.studytree.api.response.UserResponse;
import com.mounica.studytree.models.User;
import com.mounica.studytree.utilities.ValidationTools;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by ankur on 20/5/16.
 */
public class TeacherAppointmentFragment extends Fragment implements View.OnClickListener{

    public static final String USER_ID = "userId";

    private Calendar selectedDate = null;
    private ProgressDialog progressDialog;
    private EditText description;
    private TextView appointmentDate;
    private ImageButton appointmentChooseDate;
    private Spinner appointmentChooseSubject;
    private Button submitMakeCall, submitMakeEmail;

    private int userId;
    private int chosenSubject = 0;
    private String descriptionValue;
    private List<SubjectsResponse> availableSubjects;
    private UserResponse userResponse;

    public static TeacherAppointmentFragment newInstance(int userId) {
        Bundle bundle = new Bundle();
        bundle.putInt(USER_ID, userId);
        TeacherAppointmentFragment teacherAppointmentFragment = new TeacherAppointmentFragment();
        teacherAppointmentFragment.setArguments(bundle);
        return teacherAppointmentFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_appointment, container, false);
        initViews(view);
        loadData();
        return view;
    }

    private void initViews(View view) {
        description = (EditText) view.findViewById(R.id.appointment_description);
        appointmentDate = (TextView)view.findViewById(R.id.appointment_date);
        appointmentChooseDate = (ImageButton)view.findViewById(R.id.appointment_select_date);
        appointmentChooseSubject = (Spinner)view.findViewById(R.id.appointment_choose_subject);
        submitMakeCall = (Button)view.findViewById(R.id.submit_make_call);
        submitMakeEmail = (Button)view.findViewById(R.id.submit_email);

        appointmentChooseDate.setOnClickListener(this);
        submitMakeCall.setOnClickListener(this);
        submitMakeEmail.setOnClickListener(this);
        //sendMail("shiv.ankurkashyap@gmail.com", "This is a test email for sending mail", "Please ignore this as this is a test email done via Mounica's Project for testing purpose");
    }

    private void loadData() {
        showProgressDialog();
        User.getUserById(getActivity(), getArguments().getInt(USER_ID, -1), new User.UserById() {
            @Override
            public void onUserLoadedSuccess(UserCreatedResponse userCreatedResponse) {
                availableSubjects = userCreatedResponse.getSubjects();
                userResponse = userCreatedResponse.getUser();
                fillData();
                hideProgressDialog();
            }

            @Override
            public void onUserLoadedFailure(String message) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                hideProgressDialog();
            }
        });
    }

    private void fillData() {
        int i = 0;
        List<String> subjects = new ArrayList<>();
        subjects.add("Please select a subject");
        while (i<availableSubjects.size()) {
            subjects.add(availableSubjects.get(i).getSubject_name());
            i++;
        }
        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, subjects);
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        appointmentChooseSubject.setAdapter(subjectAdapter);

        appointmentChooseSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                chosenSubject = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                chosenSubject = 0;
            }
        });
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Sending Mail. Please wait...");
        progressDialog.show();
    }

    private void hideProgressDialog() {
        progressDialog.dismiss();
    }

    private void sendMail(String toEmail, String subject, String messageBody) {
        Session session = createSessionObject();

        try {
            Message message = createMessage(toEmail, subject, messageBody, session);
            new SendMailTask().execute(message);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Session createSessionObject() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.live.com");
        properties.put("mail.smtp.port", "25");

        return Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("shiv.ankurkashyap@hotmail.com", "Kashyap)(021994");
            }
        });
    }

    private Message createMessage(String toEmail, String subject, String messageBody, Session session) throws MessagingException, UnsupportedEncodingException {

        InternetAddress[] addresses = {new InternetAddress(toEmail)};
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(User.getLoggedInUserEmail(getActivity()), User.getLoggedInUserName(getActivity())));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail, toEmail));
        message.setReplyTo(addresses);
        message.setSubject(subject);
        message.setText(messageBody);
        return message;
    }

    private void showDateDialog() {
        final Calendar today= Calendar.getInstance();
        DatePickerDialog datePickerDialog= new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                today.set(Calendar.YEAR,year);
                today.set(Calendar.MONTH,monthOfYear);
                today.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                selectedDate=today;
                fillDate();
            }
        },today.get(Calendar.YEAR),today.get(Calendar.MONTH),today.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void fillDate() {
        appointmentDate.setTextColor(ContextCompat.getColor(getActivity(), R.color.white_transparent));
        appointmentDate.setText(formatDate(selectedDate.getTime(),"yyyy-MM-dd"));
    }

    public String formatDate(Date date, String format) {
        SimpleDateFormat sdf= new SimpleDateFormat(format);
        return  sdf.format(date);
    }

    @Override
    public void onClick(View view) {
        if (view == submitMakeCall) {
            makeCall();
        }
        else if (view == submitMakeEmail) {
            makeEmail();
        }
        else if (view == appointmentChooseDate) {
            showDateDialog();
        }
    }

    private void makeCall() {

    }

    private void makeEmail() {
        if (isValid()) {
            sendMail(userResponse.getEmail(), "Study Tree Appointment", descriptionValue);
        }
    }

    private boolean isValid() {
        descriptionValue = description.getText().toString();
        if (!ValidationTools.isValidImageDescription(descriptionValue)) {
            description.setError("At least 10 characters");
            return false;
        }

        if (selectedDate == null) {
            appointmentDate.setError("Please select a date first");
            return false;
        }

        if (chosenSubject == 0) {
            Toast.makeText(getActivity(), "Please select a subject", Toast.LENGTH_SHORT).show();
            return false;
        }

        descriptionValue = descriptionValue+"\nSubject: "+availableSubjects.get(chosenSubject-1).getSubject_name();
        descriptionValue = descriptionValue + "\nDate: "+formatDate(selectedDate.getTime(),"yyyy-MM-dd");
        return true;
    }

    private class SendMailTask extends AsyncTask<Message, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            hideProgressDialog();
        }

        @Override
        protected Void doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
            } catch (MessagingException e) {
                e.printStackTrace();
                hideProgressDialog();
            }
            return null;
        }
    }
}
