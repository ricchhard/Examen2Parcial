package com.banquito.sucursal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SucursalUpdateDTO {

    @NotBlank(message = "El nuevo número de teléfono es obligatorio")
    @Schema(description = "Nuevo número de contacto", example = "0998765432")
    private String phoneNumber;
}