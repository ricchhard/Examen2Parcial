package com.banquito.sucursal.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.banquito.sucursal.dto.FeriadoDTO;
import com.banquito.sucursal.dto.SucursalRequestDTO;
import com.banquito.sucursal.dto.SucursalUpdateDTO;
import com.banquito.sucursal.exception.BusinessException;
import com.banquito.sucursal.model.Sucursal;
import com.banquito.sucursal.repository.SucursalRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SucursalService {

    private final SucursalRepository repository;
    public List<Sucursal> getAll() {
        log.info("SERVICE: Iniciando consulta de todas las sucursales");
        List<Sucursal> list = repository.findAll();
        log.info("SERVICE: Se encontraron {} sucursales", list.size());
        return list;
    }

    public Sucursal create(SucursalRequestDTO dto) {
        log.info("SERVICE: Creando nueva sucursal con nombre: {}", dto.getName());
        Sucursal sucursal = Sucursal.builder()
                .emailAddress(dto.getEmailAddress())
                .name(dto.getName())
                .phoneNumber(dto.getPhoneNumber())
                .state("ACTIVE")
                .creationDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .branchHolidays(new ArrayList<>()) 
                .build();

        Sucursal saved = repository.save(sucursal);
        log.info("SERVICE: Sucursal creada exitosamente con ID: {}", saved.getId());
        return saved;
    }
    public Sucursal getById(String id) {
        log.info("SERVICE: Buscando sucursal con ID: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.error("SERVICE: Error, sucursal no encontrada con ID: {}", id);
                    return new BusinessException("Sucursal no encontrada con ID: " + id);
                });
    }

    public Sucursal updatePhone(String id, SucursalUpdateDTO dto) {
        log.info("SERVICE: Actualizando teléfono para la sucursal ID: {}", id);
        
        Sucursal sucursal = getById(id);

        log.debug("SERVICE: Teléfono anterior: {}, Nuevo teléfono: {}", sucursal.getPhoneNumber(), dto.getPhoneNumber());
        
        sucursal.setPhoneNumber(dto.getPhoneNumber());
        sucursal.setLastModifiedDate(LocalDateTime.now()); 
        
        return repository.save(sucursal);
    }

    public Sucursal addHoliday(String id, FeriadoDTO dto) {
        log.info("SERVICE: Intentando agregar feriado [{}] a la sucursal ID: {}", dto.getDate(), id);
        
        Sucursal sucursal = getById(id);

        boolean exists = sucursal.getBranchHolidays().stream()
                .anyMatch(h -> h.getDate().equals(dto.getDate()));

        if (exists) {
            log.warn("SERVICE: Intento fallido, el feriado {} ya existe en la sucursal {}", dto.getDate(), id);
            throw new BusinessException("El feriado con fecha " + dto.getDate() + " ya existe en esta sucursal");
        }

        Sucursal.SucursalFeriado nuevoFeriado = Sucursal.SucursalFeriado.builder()
                .date(dto.getDate())
                .name(dto.getName())
                .build();

        sucursal.getBranchHolidays().add(nuevoFeriado);
        sucursal.setLastModifiedDate(LocalDateTime.now()); 
        
        log.info("SERVICE: Feriado agregado exitosamente");
        return repository.save(sucursal);
    }

    public Sucursal removeHoliday(String id, LocalDate date) {
        log.info("SERVICE: Intentando eliminar feriado [{}] de la sucursal ID: {}", date, id);
        
        Sucursal sucursal = getById(id);

        boolean removed = sucursal.getBranchHolidays().removeIf(h -> h.getDate().equals(date));

        if (!removed) {
            log.warn("SERVICE: No se encontró el feriado {} para eliminar en sucursal {}", date, id);
            throw new BusinessException("No existe un feriado con la fecha " + date + " en esta sucursal");
        }

        sucursal.setLastModifiedDate(LocalDateTime.now());
        log.info("SERVICE: Feriado eliminado exitosamente");
        return repository.save(sucursal);
    }

    public List<FeriadoDTO> getHolidays(String id) {
        log.info("SERVICE: Consultando lista de feriados para sucursal ID: {}", id);
        
        Sucursal sucursal = getById(id);
        
        return sucursal.getBranchHolidays().stream()
                .map(h -> FeriadoDTO.builder()
                        .date(h.getDate())
                        .name(h.getName())
                        .build())
                .collect(Collectors.toList());
    }

    public boolean isHoliday(String id, LocalDate date) {
        log.info("SERVICE: Verificando si [{}] es feriado en sucursal ID: {}", date, id);
        
        Sucursal sucursal = getById(id);

        boolean isHoliday = sucursal.getBranchHolidays().stream()
                .anyMatch(h -> h.getDate().equals(date));
        
        log.info("SERVICE: Resultado de verificación para {}: {}", date, isHoliday);
        return isHoliday;
    }
}