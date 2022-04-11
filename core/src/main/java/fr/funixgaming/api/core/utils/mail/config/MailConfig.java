package fr.funixgaming.api.core.utils.mail.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Getter
@Setter
@Configuration
@ConfigurationProperties("spring.mail")
public class MailConfig {
    private String host;
    private Integer port;
    private String username;
    private String password;

    private String protocol = "smtp";
    private boolean auth  = true;
    private boolean tls = true;
    private boolean ssl = true;
    private boolean debug = false;

    @Bean
    public JavaMailSender getJavaMailSender() {
        final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(this.host);
        mailSender.setPort(this.port);
        mailSender.setUsername(this.username);
        mailSender.setPassword(this.password);

        final Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", protocol);
        properties.put("mail.smtp.auth", auth);
        properties.put("mail.smtp.starttls.enable", tls);
        properties.put("mail.smtp.ssl.enable", ssl);
        properties.put("mail.debug", debug);

        return mailSender;
    }
}
