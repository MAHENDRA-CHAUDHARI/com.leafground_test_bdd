package com.extra.activity;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time. LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet. InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet. MimeMultipart;
import com.aventstack.extentreports.Status;
import com.framework.Base;
public class Mail extends Base{

	public static void sendMail (String summaryReportContent) {
		String currentDate = null;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDateTime now = LocalDateTime.now();
		currentDate = dtf.format(now).toString();
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream("./src/main/resources/pirm/email.properties"));
			String from = prop.getProperty("email.from");
			String toAddresses = prop.getProperty("email.to");
			String mailSubject = 
			prop.getProperty("email.subject") + currentDate;
			Properties properties = System.getProperties();
			properties.setProperty("mail.smtp.port", prop.getProperty("mail.port"));
			properties.put("mail.smtp.auth", "true");
			properties.put("mail.smtp.starttis.enable", "false");
			properties.put("mail.smtp.ssl.trust", prop.getProperty("mail.host"));
			
			Session session = Session.getDefaultInstance (properties, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(prop.getProperty("mail.mail_username"), prop.getProperty("mail.mail_password"));
				}
			});
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		for (String to:toAddresses.split(";")) {
			message.addRecipient (Message. RecipientType. TO, new InternetAddress (to));
		}
		message.setSubject(mailSubject);
		String msg="";
		for (String line: prop.getProperty("email.content").split("\n")) {
			msg+="<H5> "+line+" </H5>";
		}
		System.out.println("*****Check_1*******");
		msg=msg +summaryReportContent;
		MimeBodyPart messageBodyPart1 = new MimeBodyPart();
		messageBodyPart1.setContent(msg, "text/html");
		
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart1);
		
		MimeBodyPart messageBodyPart2;
		String filename = "";
		DataSource source = null;
		System.out.println("***** ***Check_2**********************");
		Path dir = Paths.get(System.getProperty("user.dir") + "/Reports"); // specify your directory
		
		// Here we get the stream with full directory listing
		Optional<Path> lastFilePath = java.nio.file.Files.list(dir)
				.max(Comparator.comparingLong(f-> f.toFile().lastModified()));
		String pathname=lastFilePath.get().toString();
		
		File[] files=new File(pathname).listFiles();
		
		for(File f: files) {
			if(f.getName().contains(".htmls")) {
				continue;
			} else {
				if(10<=0 && f.getName().contains("Thread")) {
//					if(Base.FAILED_RECORD_COUNT<=0 && f.getName().contains("Thread")) {
//					continue;
				}else {
					messageBodyPart2 = new MimeBodyPart();
					filename = f.getAbsolutePath();
					if(filename.contains("html") || filename.contains(".xlsx")) {
						source = new FileDataSource(filename );
						messageBodyPart2.setDataHandler(new DataHandler(source));
						messageBodyPart2.setFileName(f.getName());
						multipart.addBodyPart(messageBodyPart2);
					}
				}
			}
		}
		message.setContent(multipart);
		Transport.send(message);
		logReport(Status.INFO, "Mail sent....");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	} 

	public static void Start_sendMail(String summaryReportContent) {
		String currentDate = null;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDateTime now = LocalDateTime.now();
		currentDate = dtf.format(now).toString();
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream("./src/main/resources/pirm/email.properties"));
			String from = prop.getProperty("email.from");
			String toAddresses = prop.getProperty("startemail.to");
			String mailSubject = prop.getProperty("startemail.subject") + currentDate + "- Started";
			Properties properties = System.getProperties();
			properties.setProperty("mail.smtp.host", prop.getProperty("mail.host"));
			properties.put("mail.smtp.port", 
			prop.getProperty("mail.port"));
			properties.put("mail.smtp.auth", "false");
			properties.put("mail.smtp.starttis.enable", "false");
			properties.put("mail.smtp.ssl.trust", prop.getProperty("mail.host"));
			Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication (prop.getProperty("mail.mail_username"), prop.getProperty("mail,mail_passwor"));
				}
			});
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			for (String to : toAddresses.split(";")) {
				message.addRecipient (Message.RecipientType. TO, new InternetAddress (to));
			}
			message.setSubject(mailSubject);
			String msg="";
			String serverstr="<SERVER URL>";
			for (String line: prop.getProperty("startemail.content").split("\n")) {
				if (line.contains (serverstr)) {
					line=line.replace(serverstr, "'"+prop.getProperty("url")+"'");
				}
				msg += "<H5> + line + </H5>";
			}
		
			msg=msg+ summaryReportContent;
			MimeBodyPart messageBodyPart1 = new MimeBodyPart();
			messageBodyPart1.setContent(msg, "text/html");
			
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart1);
			message.setContent(multipart);
			Transport.send(message);
			logReport(Status.INFO, "Mail sent....");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void abcd(String summaryReportContent) {
		String currentDate = null;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDateTime now = LocalDateTime.now();
		currentDate = dtf.format(now).toString();
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream("./src/main/resources/pirm/email.properties"));
			String from = prop.getProperty("email.from");
			String toAddresses = prop.getProperty("email.to");
			String mailSubject = prop.getProperty("email.subject") + currentDate;
			Properties properties = System.getProperties();
			properties.setProperty("mail.smtp.port", prop.getProperty("mail.port"));
			properties.put("mail.smtp.auth", "true");
			properties.put("mail.smtp.starttis.enable", "false");
			properties.put("mail.smtp.ssl.trust", prop.getProperty("mail.host"));
			
			Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(prop.getProperty("mail.mail_username"), prop.getProperty("mail.mail_password"));
				}
			});
					
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress (from));
			for (String to: toAddresses.split(";")) {
				message.addRecipient (Message. RecipientType. TO, new InternetAddress(to));
			}
			
			message.setSubject(mailSubject);
			String msg="";
			for (String line: prop.getProperty("email.content").split("\n")) {
				msg+="<H5> "+line+" </H5>";
			}
			msg=msg +summaryReportContent;
			MimeBodyPart messageBodyPart1 = new MimeBodyPart();
			messageBodyPart1.setContent(msg, "text/html");
			
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart1);
			MimeBodyPart messageBodyPart2;
			
			messageBodyPart2 = new MimeBodyPart();
			multipart.addBodyPart(messageBodyPart2);
			message.setContent(multipart);
			
			Transport.send(message);
			logReport(Status.INFO, "Mail sent....");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}

