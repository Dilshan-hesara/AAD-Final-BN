package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageResponseDto {
    private Long messageId;
    private String senderBranchName;
    private String content;
    private LocalDateTime timestamp;
}