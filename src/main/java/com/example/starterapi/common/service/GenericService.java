package com.example.starterapi.common.service;

import com.example.starterapi.common.response.PagedResponse;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;

/**
 * Generic Service interface demonstrating OOP Abstraction and Java Generics.
 * Defines contract for standard CRUD operations across domain entities.
 *
 * @param <ID>  Identifier type (e.g., Long, UUID)
 * @param <REQ> Request DTO type
 * @param <RES> Response DTO type
 */
public interface GenericService<ID extends Serializable, REQ, RES> {

    RES create(REQ request);

    RES getById(ID id);

    PagedResponse<RES> getAll(Pageable pageable);

    RES update(ID id, REQ request);

    void delete(ID id);
}
