package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto;

import lombok.Data;


@Data
public class MessageRequestDto {
    private Long receiverBranchId;
    private String content;
}