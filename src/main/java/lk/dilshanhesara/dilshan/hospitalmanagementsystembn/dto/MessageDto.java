package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto;

import lombok.Data;
import java.time.LocalDateTime;
@Data
public class MessageDto {
    private Long id;
    private Long senderBranchId;
    private String senderBranchName;
    private String content;
    private LocalDateTime timestamp;

    private String senderFullName;
    private String senderRole;
}