package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyAppointmentStatDto {
    private String date;
    private long count;

    public DailyAppointmentStatDto(long count, String date) {
        this.count = count;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}

