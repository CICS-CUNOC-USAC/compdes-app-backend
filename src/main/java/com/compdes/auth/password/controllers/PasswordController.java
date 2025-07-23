package com.compdes.auth.password.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.compdes.auth.password.models.dto.request.ChangePasswordDTO;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@Controller
@RequestMapping("/api/v2/password")
public class PasswordController {

    @PatchMapping("/{participantId}")
    public void changePasswordUser(
            @RequestBody ChangePasswordDTO newPassword, @PathVariable String participantId) {

    }

}
