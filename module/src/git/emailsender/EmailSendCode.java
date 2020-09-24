package git.emailsender;

import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailSendCode {
	static String FROM = "보내는사람 이메일";
	static String FROMNAME = "보내는사람 이름";
	static String TO = "받는사람 이메일";
	static String SMTP_USERNAME = "SMTP계정 아이디";
	static String SMTP_PASSWORD = "SMTP계정 비밀번호";
	static String FILEPATH; //첨부파일 경로
	static String FILENAME; //첨부파일 명
	
	static String SUBJECT = "제목";
	static String BODY = "내용";

	static final String HOST = "ezsmtp.bizmeka.com";
	static final int PORT = 465;
	
	EmailSendCode(Map<String, String> emailData) {
		EmailSendCode.FROM = emailData.get("from");
		EmailSendCode.FROMNAME = emailData.get("fromName");
		EmailSendCode.TO = emailData.get("to");
		EmailSendCode.SMTP_USERNAME = emailData.get("username");
		EmailSendCode.SMTP_PASSWORD = emailData.get("password");
		EmailSendCode.SUBJECT = emailData.get("subject");
		EmailSendCode.BODY = emailData.get("body");
		
		if(emailData.containsKey("filePath") == true && emailData.containsKey("fileName") == true) {
			EmailSendCode.FILEPATH = emailData.get("filePath");
			EmailSendCode.FILENAME = emailData.get("fileName");
		}
		else {
			System.out.println("첨부파일이 없는 메일입니다.");
		}
	}
    
    private static void mailSender() throws AddressException, MessagingException {
        Properties props = System.getProperties();
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.trust", HOST);
         
        // 사용자 인증
        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication(){
                return new javax.mail.PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
            }
        });
        session.setDebug(true);
         
        Message mimeMessage = new MimeMessage(session); // MimeMesage 생성
        mimeMessage.setFrom(new InternetAddress(FROM)); // 보내는 EMAIL (정확히 적어야 SMTP 서버에서 인증 실패되지 않음)
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(TO)); 
         
        mimeMessage.setSubject(SUBJECT); // 메세지 셋
        
        Multipart multipart = new MimeMultipart();

        MimeBodyPart textBodyPart = new MimeBodyPart();
        
        textBodyPart.setText(BODY);

        MimeBodyPart attachmentBodyPart= new MimeBodyPart();
        DataSource source = new FileDataSource(FILEPATH); // ex : "C:\\test.pdf"
        attachmentBodyPart.setDataHandler(new DataHandler(source));
        attachmentBodyPart.setFileName(FILENAME); // ex : "test.pdf"
        
        multipart.addBodyPart(textBodyPart);
        multipart.addBodyPart(attachmentBodyPart);
        
        mimeMessage.setContent(multipart);
        
        Transport.send(mimeMessage); // Transfer
    }
    
    public static void main(String[] args) throws AddressException, MessagingException  {
    	mailSender();
    }
}
