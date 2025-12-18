package com.banquito.sucursal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SucursalRequestDTO {

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El formato del correo electrónico no es válido")
    @Schema(description = "Correo electrónico de la sucursal", example = "sucursal.norte@banquito.com")
    private String emailAddress;

    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombre comercial de la sucursal", example = "Sucursal Norte - CCI")
    private String name;

    @NotBlank(message = "El teléfono es obligatorio")
    @Schema(description = "Teléfono de contacto", example = "022123456")
    private String phoneNumber;
}