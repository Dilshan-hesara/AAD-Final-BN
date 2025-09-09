package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.UserAccount;
import lombok.Data;


@Data
public class MessageRequestDto {

    private Long receiverBranchId;
    private String content;
    private Integer receiverId;

    private UserAccount.Role recipientRole;
}