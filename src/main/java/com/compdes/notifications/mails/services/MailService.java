package com.compdes.notifications.mails.services;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.compdes.common.config.AppProperties;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-09
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final AppProperties appProperties;
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    private static final String FRONTEND_CREATE_PARTICIPANT_USER_PATH = "create-participant-user";
    private static final String THYMELEAF_VARIABLE_URL = "url";
    private static final String THYMELEAF_VARIABLE_PARTICIPANT_FIRST_NAME = "participantFullName";

    private static final String THYMELEAF_TEMPLATE_MAIL_REGISTRATION_APPROVED = "MailRegistrationApproved";

    public void sendRegistrationApprovedEmail(String participantEmail, String participantFullName, String userId) {
        Context context = new Context();

        // construye la url de activación del usuario concatenando el host del frontend
        // y el path con el userId
        String url = String.format("%s/%s/%s",
                appProperties.getFrontendHost(), FRONTEND_CREATE_PARTICIPANT_USER_PATH, userId);

        // establece las variables que se usarán en la plantilla de correo
        context.setVariable(THYMELEAF_VARIABLE_URL, url);
        context.setVariable(THYMELEAF_VARIABLE_PARTICIPANT_FIRST_NAME, participantFullName);

        // genera el contenido html del correo usando la plantilla thymeleaf
        String html = templateEngine.process(THYMELEAF_TEMPLATE_MAIL_REGISTRATION_APPROVED, context);

        try {
            // construye el mensaje mime con el asunto y contenido html
            MimeMessage mimeMessage = buildMimeMessage(participantEmail, "Inscripción confirmada", html);

            // envía el correo
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            // registra un error si falla la construcción o envío del mensaje
            log.error("Error al enviar el correo de registro aprobado a {}", participantEmail, e);
        } catch (MailException e) {
            // captura excepciones relacionadas con el envío del correo, pero no hace nada
            log.error("Error al enviar el correo de registro aprobado a {}", participantEmail, e);
        }
    }

    /**
     * Construye un mensaje MIME listo para ser enviado por correo electrónico.
     * 
     * Este método configura un mensaje con contenido HTML, destinatario, asunto
     * y remitente personalizado utilizando el nombre de la aplicación.
     * 
     * @param to          dirección de correo del destinatario
     * @param subject     asunto del correo electrónico
     * @param htmlContent contenido HTML que se incluirá en el cuerpo del mensaje
     * @return el objeto {@link MimeMessage} completamente configurado
     * @throws MessagingException si ocurre un error al construir el mensaje MIME
     */
    private MimeMessage buildMimeMessage(String to, String subject, String htmlContent) throws MessagingException {
        // crea un nuevo mensaje mime vacío
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        // envuelve el mensaje con un helper para configurar su contenido
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        // establece el contenido html del mensaje
        helper.setText(htmlContent, true);

        // define el destinatario del correo
        helper.setTo(to);

        // establece el asunto del correo
        helper.setSubject(subject);

        // define el remitente usando el nombre de la aplicación y el correo configurado
        helper.setFrom("COMPDES 2025 <" + appProperties.getMailFrom() + ">");

        // retorna el mensaje construido
        return mimeMessage;
    }

}
