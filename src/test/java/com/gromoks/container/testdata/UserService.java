package com.gromoks.container.testdata;

public class UserService {
    private EmailService emailService;

    public EmailService getEmailService() {
        return emailService;
    }

    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }
}
