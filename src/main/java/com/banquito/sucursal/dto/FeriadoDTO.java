package com.banquito.sucursal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class FeriadoDTO {

    @NotNull(message = "La fecha del feriado es obligatoria")
    @Schema(description = "Fecha del feriado (YYYY-MM-DD)", example = "2025-12-25")
    private LocalDate date;

    @NotBlank(message = "El nombre del feriado es obligatorio")
    @Schema(description = "Nombre descriptivo del feriado", example = "Navidad")
    private String name;
}