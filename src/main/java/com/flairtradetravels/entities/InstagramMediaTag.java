package com.flairtradetravels.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class InstagramMediaTag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long instagramMediaId;
    private Long instagramTagId;

    protected InstagramMediaTag() {
    }

    public InstagramMediaTag(Long instagramMediaId, Long instagramTagId) {
        this.instagramMediaId = instagramMediaId;
        this.instagramTagId = instagramTagId;
    }

    public Long getInstagramTagId() {
        return instagramTagId;
    }
}