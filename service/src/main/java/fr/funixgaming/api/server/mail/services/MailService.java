package fr.funixgaming.api.server.mail.services;

import fr.funixgaming.api.client.mail.dtos.MailDTO;
import fr.funixgaming.api.core.exceptions.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public void sendMail(final MailDTO mailDTO) throws ApiException {
        try {
            final SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(mailDTO.getTo());
            message.setFrom(mailDTO.getFrom());
            message.setSubject(mailDTO.getSubject());
            message.setText(mailDTO.getText());

            mailSender.send(message);
        } catch (MailException e) {
            throw new ApiException("Une erreur est survenue lors de l'envoi de mail.", e);
        }
    }

    public void sendMail(final MailDTO mailDTO, final File attachment) throws ApiException {
        try {
            final MimeMessage message = mailSender.createMimeMessage();
            final MimeMessageHelper helper = new MimeMessageHelper(message, true);
            final FileSystemResource file = new FileSystemResource(attachment);

            helper.setFrom(mailDTO.getFrom());
            helper.setTo(mailDTO.getTo());
            helper.setSubject(mailDTO.getSubject());
            helper.setText(mailDTO.getText());
            helper.addAttachment("Invoice", file);

            mailSender.send(message);
        } catch (MessagingException | MailException e) {
            throw new ApiException("Une erreur est survenue lors de l'envoi de mail.", e);
        }
    }

}
