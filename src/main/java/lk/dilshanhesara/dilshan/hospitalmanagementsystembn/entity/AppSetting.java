package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "app_settings")
public class AppSetting {
    @Id
    private String settingKey;
    private String settingValue;
}