package com.eds.cogua.util;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EnvioCorreos 
{
	@Autowired
    private JavaMailSender mailSender;
	
	public EnvioCorreos()
	{
		
	}

    public void sendEmail(String to, String nombreCliente, String fechaSOAT)
    {

        SimpleMailMessage email = new SimpleMailMessage();
        
        String subject = "Recordatorio pago SOAT";
        String content = "Hola " + nombreCliente + " somos la empresa E.D.S San Antonio Cogua y queremos recordarte que tu SOAT se vencerá el " + fechaSOAT + ".";
        
        email.setTo(to);
        email.setSubject(subject);
        email.setText(content);

        mailSender.send(email);
    }
    
    public void sendEmail2(String to, String nombreTrabajador, byte[] pdf)
    {

    	MimeMessage message = mailSender.createMimeMessage();
        try 
        {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            
            String subject = "Desprendible de Nómina San Antonio Cogua";
            String content = "Hola " + nombreTrabajador + " te hacemos el envío del desprendible de nómina correspondiente del mes.";
            
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content);
            helper.addAttachment("DesprendibleNomina" + nombreTrabajador + ".pdf", new ByteArrayResource(pdf));
            
            mailSender.send(message);
            
        } 
        catch (MessagingException e) 
        {
            e.printStackTrace();
        }
    }
}
