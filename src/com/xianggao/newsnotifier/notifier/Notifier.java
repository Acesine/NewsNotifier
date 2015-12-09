package com.xianggao.newsnotifier.notifier;

import com.sun.tools.corba.se.idl.constExpr.Not;
import com.xianggao.newsnotifier.page.PageParser;
import org.joda.time.DateTime;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * Created by Acesine on 12/8/15.
 */
public class Notifier implements Runnable {
    private final String[] to;
    private final String from;
    private final String pwd;
    public static DateTime started;
    private Set<String> storedUrls;

    public Notifier(Set<String> storedUrls,
                    final String[] to,
                    final String from,
                    final String pwd) {
        this.storedUrls = storedUrls;
        this.to = to;
        this.from = from;
        this.pwd = pwd;
    }

    private void send(final List<String> urls) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, pwd);
                    }
                });

        try {
            DateTime now = DateTime.now();
            if (now.isAfter(started.plusDays(2))) {
                started = now;
                storedUrls.clear();
            }
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setSubject("News from HN "+now.toLocalDate().toString());
            String content = "";
            for (String url : urls) {
                content += "<a href=\""+url+"\">"+url+"</a><br><br>";
            }
            message.setContent(content, "text/html");

            for (String subscriber : to) {
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(subscriber));
                Transport.send(message);
                System.out.println("Sent message to " + subscriber);
            }

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        List<String> urls = null;
        try {
            urls = PageParser.parsePage();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return;
        }
        List<String> urlsToSend = new ArrayList<>();
        for (String url : urls) {
            if (!storedUrls.contains(url)) {
                urlsToSend.add(url);
                storedUrls.add(url);
            }
        }
        if (!urlsToSend.isEmpty()) send(urlsToSend);
    }
}
