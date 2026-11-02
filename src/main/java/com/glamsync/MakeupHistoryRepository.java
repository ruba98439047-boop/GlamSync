package com.glamsync;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface MakeupHistoryRepository
        extends JpaRepository<MakeupHistory, Long> {

    List<MakeupHistory> findTop20ByUsernameOrderByCreatedAtDesc(
            String username
    );

    @Modifying
    @Transactional
    @Query("DELETE FROM MakeupHistory h WHERE h.username = :username")
    void deleteByUsername(String username);
}