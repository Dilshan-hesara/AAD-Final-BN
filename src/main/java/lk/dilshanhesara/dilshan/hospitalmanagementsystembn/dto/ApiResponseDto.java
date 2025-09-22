package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseDto {
    private int status;
    private String message;
    private Object data;

}