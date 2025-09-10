package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service;


import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.ContactDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.MessageDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.MessageRequestDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.UserAccount;

import java.util.List;
public interface MessageService {
//    List<MessageDto> getConversation(Long myBranchId, Long otherBranchId);
//    void sendMessage(Long senderBranchId, MessageRequestDto dto);
//
//    public void sendMessage(Long senderBranchId, Integer senderUserId, MessageRequestDto dto) ;


    List<MessageDto> getConversation(Long myBranchId, Long otherBranchId);
//    void sendMessage(Integer senderUserId, MessageRequestDto dto);



    public long getUnreadMessageCount(Long branchId, UserAccount.Role role) ;
    public void markConversationAsRead(Long myBranchId, Long otherBranchId, UserAccount.Role myRole) ;


    List<ContactDto> getContacts(Integer userId, Long myBranchId);
    List<MessageDto> getConversation(Long myBranchId, UserAccount.Role myRole, Long otherBranchId, UserAccount.Role otherRole);
    void sendMessage(Integer senderUserId, MessageRequestDto dto);


    public List<ContactDto> getReceptionistContacts(Long branchId, Integer myUserId) ;
    public List<MessageDto> getConversation(Integer userId1, Integer userId2) ;





    List<ContactDto> getSuperAdminContacts();



    public void sendMessageSuper(Integer senderUserId, MessageRequestDto dto) ;


}