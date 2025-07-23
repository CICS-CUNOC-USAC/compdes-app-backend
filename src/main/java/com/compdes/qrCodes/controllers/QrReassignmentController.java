package com.compdes.qrCodes.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.compdes.qrCodes.services.QrReassignmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/reassignment")
@RequiredArgsConstructor
public class QrReassignmentController {

    private final QrReassignmentService qrReassignmentService;

    @PatchMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void reassignQrsToApprovedParticipants() {
        qrReassignmentService.reassignQrsToApprovedParticipants();
    }
}
