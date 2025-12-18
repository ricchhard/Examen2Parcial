package com.banquito.sucursal.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banquito.sucursal.dto.FeriadoDTO;
import com.banquito.sucursal.dto.SucursalRequestDTO;
import com.banquito.sucursal.dto.SucursalUpdateDTO;
import com.banquito.sucursal.model.Sucursal;
import com.banquito.sucursal.service.SucursalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/sucursales")
@RequiredArgsConstructor
@Tag(name = "Sucursales", description = "Gestión de sucursales y feriados")
public class SucursalController {

    private final SucursalService service;

    @GetMapping
    @Operation(summary = "1. Listar todas las sucursales")
    public ResponseEntity<List<Sucursal>> getAll() {
        log.info("API: Solicitud listar todas");
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping
    @Operation(summary = "2. Crear sucursal")
    public ResponseEntity<Sucursal> create(@Valid @RequestBody SucursalRequestDTO dto) {
        log.info("API: Solicitud crear sucursal: {}", dto.getName());
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "3. Obtener por ID")
    public ResponseEntity<Sucursal> getById(@PathVariable String id) {
        log.info("API: Solicitud buscar ID: {}", id);
        return ResponseEntity.ok(service.getById(id));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "4. Modificar teléfono")
    public ResponseEntity<Sucursal> updatePhone(@PathVariable String id, @Valid @RequestBody SucursalUpdateDTO dto) {
        log.info("API: Solicitud actualizar teléfono ID: {}", id);
        return ResponseEntity.ok(service.updatePhone(id, dto));
    }

    @PostMapping("/{id}/feriados")
    @Operation(summary = "5. Crear feriado")
    public ResponseEntity<Sucursal> addHoliday(@PathVariable String id, @Valid @RequestBody FeriadoDTO dto) {
        log.info("API: Solicitud agregar feriado ID: {}", id);
        return ResponseEntity.ok(service.addHoliday(id, dto));
    }

    @DeleteMapping("/{id}/feriados/{date}")
    @Operation(summary = "6. Eliminar feriado")
    public ResponseEntity<Sucursal> removeHoliday(
            @PathVariable String id, 
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("API: Solicitud eliminar feriado ID: {}", id);
        return ResponseEntity.ok(service.removeHoliday(id, date));
    }

    @GetMapping("/{id}/feriados")
    @Operation(summary = "7. Obtener todos los feriados")
    public ResponseEntity<List<FeriadoDTO>> getHolidays(@PathVariable String id) {
        log.info("API: Solicitud listar feriados ID: {}", id);
        return ResponseEntity.ok(service.getHolidays(id));
    }

    @GetMapping("/{id}/feriados/check/{date}")
    @Operation(summary = "8. Verificar si es feriado")
    public ResponseEntity<Map<String, Object>> checkHoliday(
            @PathVariable String id, 
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("API: Solicitud verificar feriado ID: {}", id);
        boolean esFeriado = service.isHoliday(id, date);
        return ResponseEntity.ok(Map.of("esFeriado", esFeriado, "fecha", date));
    }
}