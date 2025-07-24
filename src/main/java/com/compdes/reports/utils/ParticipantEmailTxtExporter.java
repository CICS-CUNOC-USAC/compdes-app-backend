package com.compdes.reports.utils;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.stereotype.Component;

import com.compdes.common.exceptions.CustomRuntimeException;
import com.compdes.common.exceptions.enums.CustomRuntimeErrorEnum;
import com.compdes.participants.models.entities.Participant;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ParticipantEmailTxtExporter {
    private final String LINE_IN_DOCUMENT = "%s: %s - Correo: %s";

    /**
     * Genera un archivo de texto en memoria que contiene los correos electrónicos
     * de los participantes aprobados.
     *
     * <p>
     * Se omiten correos nulos o en blanco.
     * </p>
     *
     * @param participants lista de participantes desde la cual se extraen los
     *                     correos.
     * @return un arreglo de bytes representando el contenido del archivo .txt
     *         generado.
     * @throws CustomRuntimeException si ocurre un error durante la generación del
     *                                archivo.
     */
    public byte[] writeEmailsToTxt(List<Participant> participants, String participantType) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                // Asocia un PrintWriter al flujo, con codificación UTF-8 y autoflush habilitado
                PrintWriter writer = new PrintWriter(baos, true, StandardCharsets.UTF_8)) {

            for (Participant participant : participants) {
                writer.println(String.format(LINE_IN_DOCUMENT, participantType, participant.getFullName(),
                        participant.getEmail()));
            }
            return baos.toByteArray();

        } catch (Exception e) {
            throw CustomRuntimeErrorEnum.EMAIL_REPORT_GENERATION_FAILED.getCustomRuntimeException();
        }
    }
}
