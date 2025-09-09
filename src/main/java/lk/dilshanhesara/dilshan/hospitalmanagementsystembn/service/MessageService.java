package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service;


import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.MessageDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.MessageRequestDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.UserAccount;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
public interface MessageService {
//    List<MessageDto> getConversation(Long myBranchId, Long otherBranchId);
//    void sendMessage(Long senderBranchId, MessageRequestDto dto);
//
//    public void sendMessage(Long senderBranchId, Integer senderUserId, MessageRequestDto dto) ;


    List<MessageDto> getConversation(Long myBranchId, Long otherBranchId);
    void sendMessage(Integer senderUserId, MessageRequestDto dto);



    public long getUnreadMessageCount(Long branchId, UserAccount.Role role) ;
    public void markConversationAsRead(Long myBranchId, Long otherBranchId, UserAccount.Role myRole) ;
    }