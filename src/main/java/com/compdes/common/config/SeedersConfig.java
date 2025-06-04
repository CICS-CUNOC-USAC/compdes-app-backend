package com.compdes.common.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.compdes.auth.users.enums.RolesEnum;
import com.compdes.auth.users.models.dto.request.CreateCompdesUserDTO;
import com.compdes.auth.users.services.CompdesUserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SeedersConfig implements CommandLineRunner {

	private final CompdesUserService compdesUserService;

	@Override
	@Transactional(rollbackOn = Exception.class)
	public void run(String... args) throws Exception {

		if (compdesUserService.count() == 0) {
			System.out.println("Creando el usuario admin.");
			CreateCompdesUserDTO createCompdesUserDTO = new CreateCompdesUserDTO("admin", "12345678",
					RolesEnum.ADMIN.getRoleLabel());
			compdesUserService.createNonParticipantUser(createCompdesUserDTO);

		}
	}

}
