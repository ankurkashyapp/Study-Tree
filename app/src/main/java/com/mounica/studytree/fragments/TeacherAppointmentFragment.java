package com.mounica.studytree.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mounica.studytree.R;
import com.mounica.studytree.activity.MainActivity;

import java.io.UnsupportedEncodingException;
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
public class TeacherAppointmentFragment extends Fragment {

    private ProgressDialog progressDialog;
    private EditText description;

    public static TeacherAppointmentFragment newInstance() {
        TeacherAppointmentFragment teacherAppointmentFragment = new TeacherAppointmentFragment();
        return teacherAppointmentFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_appointment, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        //sendMail("shiv.ankurkashyap@gmail.com", "This is a test email", "Please ignore this as this is a test email done via Mounica's Project");
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

    private void sendMail(String email, String subject, String messageBody) {
        Session session = createSessionObject();

        try {
            Message message = createMessage(email, subject, messageBody, session);
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
        properties.put("mail.smtp.port", "465");

        return Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("shiv.ankurkashyap@hotmail.com", "Kashyap)(021994");
            }
        });
    }

    private Message createMessage(String email, String subject, String messageBody, Session session) throws MessagingException, UnsupportedEncodingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("shiv.ankurkashyap@hotmail.com", "Ankur Kashyap"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email, email));
        message.setSubject(subject);
        message.setText(messageBody);
        return message;
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
            }
            return null;
        }
    }
}
