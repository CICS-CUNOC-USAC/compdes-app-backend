package com.compdes.reports.txt.services;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.compdes.participants.mappers.ParticipantMapper;
import com.compdes.participants.models.dto.response.AdminParticipantProfileDTO;
import com.compdes.participants.models.entities.Participant;
import com.compdes.participants.services.ParticipantService;
import com.compdes.reports.txt.models.dto.response.UniversityAttendanceReportDTO;

import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de generar reportes estadísticos de asistencia de
 * participantes agrupados por universidad.
 *
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-07-09
 */
@Service
@RequiredArgsConstructor
public class UniversityAttendanceReportService {

        private final ParticipantService participantService;
        private final ParticipantMapper participantMapper;

        /**
         * Genera un reporte agrupado por universidad que contiene la lista de
         * participantes,
         * el total de registros, total de aprobados y total de pendientes por
         * inscripción.
         * 
         * @return lista de objetos {@link UniversityAttendanceReportDTO} por
         *         universidad
         */
        public List<UniversityAttendanceReportDTO> getAttendanceReportByUniversity() {
                // Obtiene y agrupa los participantes por universidad
                Map<String, List<Participant>> groupedByUniversity = getParticipantsGroupedByUniversity();

                // Crea un reporte por cada universidad
                return groupedByUniversity.entrySet().stream()
                                .map(entry -> buildUniversityReport(entry.getKey(), entry.getValue()))
                                .sorted(Comparator.comparing(UniversityAttendanceReportDTO::getUniversity))
                                .toList();
        }

        /**
         * Recupera todos los participantes y los agrupa por el nombre de la
         * universidad.
         * 
         * @return un mapa con la universidad como clave y la lista de participantes
         *         como valor
         */
        private Map<String, List<Participant>> getParticipantsGroupedByUniversity() {
                List<Participant> allParticipants = participantService.getAllParticipants(null);

                // Agrupa por nombre de universidad con limpieza de espacios
                return allParticipants.stream()
                                .collect(Collectors.groupingBy(p -> p.getOrganisation().trim()));
        }

        /**
         * Construye un reporte detallado para una universidad específica, incluyendo
         * la cantidad total de participantes, cuántos han sido aprobados y cuántos
         * están pendientes.
         * 
         * @param university   nombre de la universidad
         * @param participants lista de participantes asociados a dicha universidad
         * @return DTO con los datos de asistencia para esa universidad
         */
        private UniversityAttendanceReportDTO buildUniversityReport(String university, List<Participant> participants) {

                // Mapea los participantes a su DTO con información privada
                List<AdminParticipantProfileDTO> participantDTOs = participants.stream()
                                .map(participantMapper::participantToPrivateParticipantInfoDto)
                                .toList();

                // Cuenta cuántos tienen inscripción aprobada
                long totalApproved = participants.stream()
                                .filter(p -> p.getRegistrationStatus() != null &&
                                                Boolean.TRUE.equals(p.getRegistrationStatus().getIsApproved()))
                                .count();

                // El resto se considera pendiente
                long totalPending = participants.size() - totalApproved;

                return new UniversityAttendanceReportDTO(
                                university,
                                participantDTOs.size(),
                                totalApproved,
                                totalPending,
                                participantDTOs);
        }

}
