package com.flairtradetravels.entities;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InstagramTagRepository extends CrudRepository<InstagramTag, Long> {
    List<InstagramTag> findByText(String text);
    List<InstagramTag> findByIdIn(List<Long> ids);
}