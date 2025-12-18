package com.banquito.sucursal.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.banquito.sucursal.model.Sucursal;

@Repository
public interface SucursalRepository extends MongoRepository<Sucursal, String> {
}