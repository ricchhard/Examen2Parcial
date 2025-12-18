package com.banquito.sucursal.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Document(collection = "sucursales")
public class Sucursal {

    @Id
    private String id;

    private String emailAddress;
    private String name;
    private String phoneNumber;
    private String state; 

    private LocalDateTime creationDate;
    private LocalDateTime lastModifiedDate;

    private List<SucursalFeriado> branchHolidays;

    @Data
    @Builder
    public static class SucursalFeriado {
        private LocalDate date;
        private String name;
    }
}