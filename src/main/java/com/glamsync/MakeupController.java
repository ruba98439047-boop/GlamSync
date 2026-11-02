package com.glamsync;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/makeup")
public class MakeupController {

    private final MakeupPreferenceRepository preferenceRepository;
    private final MakeupHistoryRepository historyRepository;

    public MakeupController(
            MakeupPreferenceRepository preferenceRepository,
            MakeupHistoryRepository historyRepository) {

        this.preferenceRepository = preferenceRepository;
        this.historyRepository = historyRepository;
    }

    // Get saved preferences
    @GetMapping("/preferences")
    public MakeupPreference getPreferences(Principal principal) {

        String username = getUsername(principal);

        return preferenceRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Preferences not found"
                ));
    }

    // Save or update preferences
    @PutMapping("/preferences")
    public MakeupPreference savePreferences(
            @RequestBody MakeupPreference request,
            Principal principal) {

        String username = getUsername(principal);

        MakeupPreference preference =
                preferenceRepository.findByUsername(username)
                        .orElseGet(MakeupPreference::new);

        preference.setUsername(username);
        preference.setOccasion(request.getOccasion());
        preference.setOutfitColour(request.getOutfitColour());

        return preferenceRepository.save(preference);
    }

    // Get latest 20 history records
    @GetMapping("/history")
    public List<MakeupHistory> getHistory(Principal principal) {

        String username = getUsername(principal);

        return historyRepository
                .findTop20ByUsernameOrderByCreatedAtDesc(username);
    }

    // Add makeup history
    @PostMapping("/history")
    public MakeupHistory addHistory(
            @RequestBody MakeupHistory request,
            Principal principal) {

        String username = getUsername(principal);

        MakeupHistory history = new MakeupHistory();
        history.setUsername(username);
        history.setTitle(request.getTitle());
        history.setDetails(request.getDetails());

        return historyRepository.save(history);
    }

    // Delete makeup history
    @DeleteMapping("/history")
    public String deleteHistory(Principal principal) {

        String username = getUsername(principal);

        historyRepository.deleteByUsername(username);

        return "Makeup history deleted successfully";
    }

    // Check logged-in user
    private String getUsername(Principal principal) {

        if (principal == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Please login first"
            );
        }

        return principal.getName();
    }
}