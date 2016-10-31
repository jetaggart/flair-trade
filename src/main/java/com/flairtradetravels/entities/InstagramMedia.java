package com.flairtradetravels.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class InstagramMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String instagramId;
    private String imageUrl;

    protected InstagramMedia() {

    }

    public InstagramMedia(String instagramId, String imageUrl) {
        this.instagramId = instagramId;
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return String.format("Customer[id=%d, instagramId=%s]", id, instagramId);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}