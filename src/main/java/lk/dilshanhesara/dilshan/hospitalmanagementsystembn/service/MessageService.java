package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service;


import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.MessageDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.MessageRequestDto;

import java.util.List;
public interface MessageService {
    List<MessageDto> getConversation(Long myBranchId, Long otherBranchId);
    void sendMessage(Long senderBranchId, MessageRequestDto dto);
}