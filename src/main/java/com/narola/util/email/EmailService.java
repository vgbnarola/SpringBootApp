package com.narola.util.email;

public interface EmailService {
	
	boolean sendSimpleMail(EmailDetails details);

	boolean sendSimpleHtmlMail(EmailDetails details);
    
}