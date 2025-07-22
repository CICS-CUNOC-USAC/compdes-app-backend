package com.compdes.reports.services;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.stereotype.Service;

import com.compdes.common.exceptions.CustomRuntimeException;
import com.compdes.common.exceptions.enums.CustomRuntimeErrorEnum;
import com.compdes.participants.models.dto.request.ParticipantFilterDTO;
import com.compdes.participants.models.entities.Participant;
import com.compdes.participants.services.ParticipantService;

import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de generar un reporte en formato texto (.txt)
 * que contiene los correos electrónicos de los participantes aprobados.
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-07-22
 */
@Service
@RequiredArgsConstructor
public class ApprovedParticipantsEmailReportService {
    private final String LINE_IN_DOCUMENT = "Participante: %s - Correo: %s";
    private final ParticipantService participantService;

    /**
     * Genera un reporte en formato `.txt` con los correos electrónicos de los
     * participantes aprobados.
     * 
     * @return un arreglo de bytes que contiene el contenido del archivo `.txt` con
     *         los correos aprobados.
     * @throws CustomRuntimeException si ocurre un error durante la generación del
     *                                archivo.
     */
    public byte[] generateReport() {
        // busca solamente los participantes aprovados
        List<Participant> approvedParticipants = participantService.getAllParticipants(
                new ParticipantFilterDTO(null, null, null, null, null, null, null, null, null, null, null, true, null));

        // Escribe los correos en un archivo de texto (en memoria)
        return writeEmailsToTxt(approvedParticipants);
    }

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
    private byte[] writeEmailsToTxt(List<Participant> participants) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                // Asocia un PrintWriter al flujo, con codificación UTF-8 y autoflush habilitado
                PrintWriter writer = new PrintWriter(baos, true, StandardCharsets.UTF_8)) {

            for (Participant participant : participants) {
                writer.println(String.format(LINE_IN_DOCUMENT, participant.getFullName(), participant.getEmail()));
            }
            return baos.toByteArray();

        } catch (Exception e) {
            throw CustomRuntimeErrorEnum.EMAIL_REPORT_GENERATION_FAILED.getCustomRuntimeException();
        }
    }
}
