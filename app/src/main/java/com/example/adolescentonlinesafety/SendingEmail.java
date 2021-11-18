package com.example.adolescentonlinesafety;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendingEmail extends AsyncTask<Void, Void, PasswordAuthentication>

{
    // VARIABLES
    private Context context;
    private Session session;

    //INFORMATION STRUCTURE
    private  String Email;
    private  String subject;
    private  String Message;

    // CONSTRUCTOR
    public SendingEmail(Context context, String Email, String subject, String message) {
        this.context = context;
        this.Email = Email;
        this.subject = subject;
        Message = message;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //showing progress dialog while sending email
        Log.v("msg","Sending Email.....");
    }

    @Override
    protected void onPostExecute(PasswordAuthentication unused) {
        super.onPostExecute(unused);
        //showing results success
        Log.v("msg","mail sent");

    }

    @Override
    protected PasswordAuthentication doInBackground(Void... voids)

    {
        //CREATING PROPERTIES
        Properties props = new Properties();

        //PROPERTIES FOR GMAIL SERVER
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        //UPDATING SESSION

       session = Session.getDefaultInstance(props,
               new javax.mail.Authenticator() {
           protected PasswordAuthentication getPasswordAuthentication() {
               return new PasswordAuthentication(Configuration.Email, Configuration.Password);
           }
    });

        try {
            //Creating MimeMessage object
            MimeMessage mm = new MimeMessage(session);

            //Setting sender address
            mm.setFrom(new InternetAddress(Configuration.Email));
            //Adding receiver
            mm.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(Email));
            //Adding subject
            mm.setSubject(subject);
            //Adding message
            mm.setText(Message);

            //Sending email
            Transport.send(mm);

        } catch (MessagingException e) {
            e.printStackTrace();

        }




        return null;
    }
}
