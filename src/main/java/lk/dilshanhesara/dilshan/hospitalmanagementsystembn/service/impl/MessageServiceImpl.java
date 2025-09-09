package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.impl;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.MessageDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.MessageRequestDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Branch;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Message;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.StaffProfile;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.UserAccount;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.BranchRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.MessageRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.StaffProfileRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.UserAccountRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

//... imports
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final BranchRepository branchRepository;
    private final ModelMapper modelMapper;
//    public List<MessageDto> getConversation(Long myBranchId, Long otherBranchId) {
//        return messageRepository.findConversation(myBranchId, otherBranchId).stream()
//                .map(msg -> modelMapper.map(msg, MessageDto.class))
//                .collect(Collectors.toList());
//    }
//    @Override
//    public void sendMessage(Long senderBranchId, MessageRequestDto dto) {
//        Branch sender = branchRepository.findById(senderBranchId).orElseThrow();
//        Branch receiver = branchRepository.findById(dto.getReceiverBranchId()).orElseThrow();
//        Message message = new Message();
//        message.setSenderBranch(sender);
//        message.setReceiverBranch(receiver);
//        message.setContent(dto.getContent());
//        messageRepository.save(message);
//    }
//
//
//
//    private final UserAccountRepository userAccountRepository;
//
//    // In MessageServiceImpl.java
//    @Override
//    public void sendMessage(Long senderBranchId, Integer senderUserId, MessageRequestDto dto) {
//        Branch senderBranch = branchRepository.findById(senderBranchId).orElseThrow();
//        Branch receiverBranch = branchRepository.findById(dto.getReceiverBranchId()).orElseThrow();
//        UserAccount sender = userAccountRepository.findById(senderUserId).orElseThrow();
//
//        Message message = new Message();
//        message.setSenderBranch(senderBranch);
//        message.setReceiverBranch(receiverBranch);
//        message.setSender(sender); // <-- Set the sender
//        message.setContent(dto.getContent());
//        messageRepository.save(message);
//    }
//
//    @Override
//    public List<MessageDto> getConversation(Long myBranchId, Long otherBranchId) {
//        return messageRepository.findConversation(myBranchId, otherBranchId).stream()
//                .map(msg -> {
//                    MessageDto dto = modelMapper.map(msg, MessageDto.class);
//                    // --- ADD SENDER'S NAME AND ROLE ---
//                    if (msg.getSender() != null && msg.getSender().getStaffProfile() != null) {
//                        dto.setSenderFullName(msg.getSender().getStaffProfile().getFullName());
//                    }
//                    dto.setSenderRole(msg.getSender().getRole().name());
//                    return dto;
//                }).collect(Collectors.toList());
//    }


    private final UserAccountRepository userAccountRepository;
    private final StaffProfileRepository staffProfileRepository;

    @Override
    public List<MessageDto> getConversation(Long myBranchId, Long otherBranchId) {
        return messageRepository.findConversation(myBranchId, otherBranchId).stream()
                .map(this::convertToMessageDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void sendMessage(Integer senderUserId, MessageRequestDto dto) {
        UserAccount sender = userAccountRepository.findById(senderUserId).orElseThrow();
        StaffProfile senderProfile = staffProfileRepository.findById(senderUserId).orElseThrow();
        Branch receiverBranch = branchRepository.findById(dto.getReceiverBranchId()).orElseThrow();

        Message message = new Message();
        message.setSender(sender);
        message.setSenderBranch(senderProfile.getBranch());
        message.setReceiverBranch(receiverBranch);
        message.setContent(dto.getContent());
        messageRepository.save(message);
    }

    private MessageDto convertToMessageDto(Message message) {
        MessageDto dto = new MessageDto();
        dto.setContent(message.getContent());
        dto.setTimestamp(message.getTimestamp());

        StaffProfile senderProfile = staffProfileRepository.findById(message.getSender().getUserId()).orElse(new StaffProfile());
        dto.setSenderFullName(senderProfile.getFullName());
        dto.setSenderRole(message.getSender().getRole().name());
        dto.setSenderBranchId(message.getSenderBranch().getId());
        dto.setSenderBranchName(message.getSenderBranch().getName());
        return dto;
    }
}