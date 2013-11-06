package com.ibm.sbt.services.client.email;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

public class MailSessionFactory {

    private String            auth;
    private String            mailSmtpHost;
    private Boolean           mailSmtpAuth = false;
    private String            mailUser;
    private String            mailPassword;
    private String            mailFrom;
    private Boolean           mailDebug;

    private transient Session instance;

    public void setAuth(String auth) {
        // unused, tomcat specific setting?
        this.auth = auth;
    }

    public void setMailSmtpHost(String mailSmtpHost) {
        this.mailSmtpHost = mailSmtpHost;
    }

    public void setMailSmtpAuth(Boolean mailSmtpAuth) {
        this.mailSmtpAuth = mailSmtpAuth;
    }

    public void setMailUser(String mailUser) {
        this.mailUser = mailUser;
    }

    public void setMailPassword(String pmailPassword) {
        this.mailPassword = pmailPassword;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public void setMailDebug(Boolean mailDebug) {
        this.mailDebug = mailDebug;
    }

    private Properties getProperties() {
        Properties prop = new Properties();

        // we may want this to be configurable
        prop.put("mail.transport.protocol", "smtp");

        prop.put("mail.smtp.host", mailSmtpHost);
        prop.put("mail.user", mailUser);
        prop.put("mail.smtp.user", mailUser);

        return prop;
    }

    protected String isAuthRequired() {
        return auth;
    }

    protected String getMailFrom() {
        return mailFrom;
    }

    protected Boolean isDebugEnabled() {
        return mailDebug;
    }

    public synchronized Session getInstance() {
        if (instance != null) {
            return instance;
        }

        Authenticator authenticator = null;
        if (mailSmtpAuth) {
            final PasswordAuthentication pauth = new PasswordAuthentication(mailUser, mailPassword);
            authenticator = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return pauth;
                }
            };

        }

        this.instance = Session.getInstance(getProperties(), authenticator);

        return instance;
    }
}
