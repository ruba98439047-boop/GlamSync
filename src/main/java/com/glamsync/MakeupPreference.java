package com.glamsync;

import jakarta.persistence.*;

@Entity
@Table(name = "makeup_preferences")
public class MakeupPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    private String occasion;
    private String outfitColour;

    public MakeupPreference() {
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOccasion() {
        return occasion;
    }

    public void setOccasion(String occasion) {
        this.occasion = occasion;
    }

    public String getOutfitColour() {
        return outfitColour;
    }

    public void setOutfitColour(String outfitColour) {
        this.outfitColour = outfitColour;
    }
}