package com.compdes.auth.password.services;

import org.springframework.stereotype.Service;

import com.compdes.auth.password.models.dto.request.ChangePasswordDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PasswordService {

    public void changePasswordUser(ChangePasswordDTO passwordDTO, String participantId) {

    }
}
