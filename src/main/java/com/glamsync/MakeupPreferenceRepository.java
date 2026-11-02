package com.glamsync;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MakeupPreferenceRepository
        extends JpaRepository<MakeupPreference, Long> {

    Optional<MakeupPreference> findByUsername(String username);
}