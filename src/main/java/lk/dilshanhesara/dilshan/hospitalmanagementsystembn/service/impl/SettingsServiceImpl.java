package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.impl;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.AppSetting;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.AppSettingsRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.SettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class SettingsServiceImpl implements SettingsService {

    private final AppSettingsRepository settingsRepository;
    private final String FEE_KEY = "APPOINTMENT_FEE";

    @Override
    public BigDecimal getAppointmentFee() {
        return settingsRepository.findById(FEE_KEY)
                .map(setting -> new BigDecimal(setting.getSettingValue()))
                .orElse(new BigDecimal("2500.00"));
    }

    @Override
    public void updateAppointmentFee(BigDecimal newFee) {
        AppSetting feeSetting = settingsRepository.findById(FEE_KEY)
                .orElse(new AppSetting());

        feeSetting.setSettingKey(FEE_KEY);
        feeSetting.setSettingValue(newFee.toString());
        settingsRepository.save(feeSetting);
    }

}
