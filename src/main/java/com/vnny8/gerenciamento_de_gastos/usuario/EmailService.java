package com.vnny8.gerenciamento_de_gastos.usuario;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    public void enviar(String destinatario, String assunto, String mensagem) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setTo(destinatario);
            helper.setSubject(assunto);
            helper.setText(mensagem, true); // O `true` habilita o envio de HTML

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new IllegalStateException("Falha ao enviar e-mail", e);
        }
    }
}
