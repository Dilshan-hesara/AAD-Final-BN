package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service;

import java.math.BigDecimal;

public interface SettingsService {


    BigDecimal getAppointmentFee();
    void updateAppointmentFee(BigDecimal newFee);
}
