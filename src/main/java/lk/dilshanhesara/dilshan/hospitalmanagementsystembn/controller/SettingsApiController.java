package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.controller;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.SettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class SettingsApiController {

    private static final BigDecimal DEFAULT_APPOINTMENT_FEE = new BigDecimal("2500.00");



    private final SettingsService settingsService;

    @GetMapping("/appointment-fee")
    public ResponseEntity<Map<String, BigDecimal>> getAppointmentFee() {
        return ResponseEntity.ok(Map.of("fee", settingsService.getAppointmentFee()));
    }

    @PostMapping("/appointment-fee")
    public ResponseEntity<Void> updateAppointmentFee(@RequestBody Map<String, BigDecimal> payload) {
        BigDecimal newFee = payload.get("fee");
        settingsService.updateAppointmentFee(newFee);
        return ResponseEntity.ok().build();
    }



}
