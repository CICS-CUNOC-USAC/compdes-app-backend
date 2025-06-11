package com.compdes.common.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.compdes.auth.users.enums.RolesEnum;
import com.compdes.auth.users.models.dto.request.CreateNonParticipantCompdesUserDTO;
import com.compdes.auth.users.services.CompdesUserService;
import com.compdes.qrCodes.services.QrCodeService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SeedersConfig implements CommandLineRunner {

	private final CompdesUserService compdesUserService;
	private final QrCodeService qrCodeService;

	@Override
	@Transactional(rollbackOn = Exception.class)
	public void run(String... args) throws Exception {

		if (compdesUserService.count() == 0) {
			System.out.println("Creando el usuario admin.");
			CreateNonParticipantCompdesUserDTO compdesUserDTO = new CreateNonParticipantCompdesUserDTO("admin",
					"12345678",
					RolesEnum.ADMIN.getRoleLabel());
			compdesUserService.createNonParticipantUser(compdesUserDTO);

		}

		// generar 500 qr si no hay ninguno creado
		if (qrCodeService.count() == 0) {
			System.out.println("Creando qrs.");

			for (int x = 0; x < 500; x++) {
				qrCodeService.createQrCode();
			}
		}

	}

}
