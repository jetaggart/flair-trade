package com.flairtradetravels.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InstagramMediaRepository extends CrudRepository<InstagramMedia, Long> {
    List<InstagramMedia> findByInstagramId(String instagramId);
    List<InstagramMedia> findAllByOrderByIdDesc();
}