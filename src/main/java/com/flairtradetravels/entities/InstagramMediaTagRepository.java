package com.flairtradetravels.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InstagramMediaTagRepository extends CrudRepository<InstagramMediaTag, Long> {
    List<InstagramMediaTag> findByInstagramMediaId(Long id);
}