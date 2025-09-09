package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.impl;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.ContactDto;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

//    @Override
//    @Transactional
//    public void sendMessage(Integer senderUserId, MessageRequestDto dto) {
//        UserAccount sender = userAccountRepository.findById(senderUserId).orElseThrow();
//        StaffProfile senderProfile = staffProfileRepository.findById(senderUserId).orElseThrow();
//        Branch receiverBranch = branchRepository.findById(dto.getReceiverBranchId()).orElseThrow();
//
//        Message message = new Message();
//        message.setSender(sender);
//        message.setSenderBranch(senderProfile.getBranch());
//        message.setReceiverBranch(receiverBranch);
//        message.setContent(dto.getContent());
//        messageRepository.save(message);
//    }

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










//
//    @Override
//    public void sendMessage(Integer senderUserId, MessageRequestDto dto) {
//        // ... find sender, senderProfile, receiverBranch
//
//        Message message = new Message();
//        // ... set sender, senderBranch, receiverBranch, content
//        message.setRecipientRole(dto.getRecipientRole()); // <-- Set the recipient role
//        messageRepository.save(message);
//    }

    // --- ADD THESE NEW METHODS for notifications ---
    @Override
    public long getUnreadMessageCount(Long branchId, UserAccount.Role role) {
        return messageRepository.countByReceiverBranchIdAndRecipientRoleAndIsReadFalse(branchId, role);
    }

    @Override
    @Transactional
    public void markConversationAsRead(Long myBranchId, Long otherBranchId, UserAccount.Role myRole) {
        List<Message> messages = messageRepository.findConversation(myBranchId, otherBranchId);
        messages.forEach(msg -> {
            // Mark messages as read if I am the recipient
            if (msg.getReceiverBranch().getId().equals(myBranchId) && msg.getRecipientRole() == myRole) {
                msg.setRead(true);
            }
        });
        messageRepository.saveAll(messages);
    }



//    @Override
//    @Transactional
//    public void sendMessage(Integer senderUserId, MessageRequestDto dto) {
//        // Find the sender's UserAccount and StaffProfile
//        UserAccount sender = userAccountRepository.findById(senderUserId)
//                .orElseThrow(() -> new RuntimeException("Sender user not found"));
//        StaffProfile senderProfile = staffProfileRepository.findById(senderUserId)
//                .orElseThrow(() -> new RuntimeException("Sender profile not found"));
//
//        // --- CRITICAL FIX: Find the receiver's Branch from the database ---
//        Branch receiverBranch = branchRepository.findById(dto.getReceiverBranchId())
//                .orElseThrow(() -> new RuntimeException("Receiver branch not found"));
//
//        Message message = new Message();
//        message.setSender(sender);
//        message.setSenderBranch(senderProfile.getBranch());
//        message.setReceiverBranch(receiverBranch); // <-- Set the found branch object
//        message.setRecipientRole(dto.getRecipientRole());
//        message.setContent(dto.getContent());
//
//        messageRepository.save(message);
//    }














    @Override
    public List<ContactDto> getContacts(Long myBranchId) {
        // 1. Find all Super Admins using the correct method
        List<ContactDto> superAdmins = userAccountRepository.findAllByRole(UserAccount.Role.SUPER_ADMIN)
                .stream()
                .map(ua -> {
                    ContactDto dto = new ContactDto();
                    dto.setBranchName("Super Admin");
                    dto.setRole("SUPER_ADMIN");
                    return dto;
                }).collect(Collectors.toList());

        // 2. Find all other branches
        List<ContactDto> branches = branchRepository.findAll().stream()
                .filter(branch -> !branch.getId().equals(myBranchId))
                .flatMap(branch -> Stream.of(
                        createContactDto(branch.getId(), branch.getName(), "BRANCH_ADMIN"),
                        createContactDto(branch.getId(), branch.getName(), "RECEPTIONIST")
                )).collect(Collectors.toList());

        // 3. Combine the lists
        List<ContactDto> allContacts = new ArrayList<>();
        allContacts.addAll(superAdmins);
        allContacts.addAll(branches);
        return allContacts;
    }

    // ... your other methods (getConversation, sendMessage, etc.)

    private ContactDto createContactDto(Long branchId, String branchName, String role) {
        ContactDto dto = new ContactDto();
        dto.setBranchId(branchId);
        dto.setBranchName(branchName);
        dto.setRole(role);
        return dto;
    }
//
//    @Override
//    public List<MessageDto> getConversation(Long myBranchId, UserAccount.Role myRole, Long otherBranchId, UserAccount.Role otherRole) {
//        List<Message> messages = messageRepository.findAdvancedConversation(myBranchId, myRole, otherBranchId, otherRole);
//        return messages.stream()
//                .map(this::convertToMessageDto)
//                .collect(Collectors.toList());
//    }

    @Override
    @Transactional
    public void sendMessage(Integer senderUserId, MessageRequestDto dto) {
        UserAccount sender = userAccountRepository.findById(senderUserId).orElseThrow();
        StaffProfile senderProfile = staffProfileRepository.findById(senderUserId).orElseThrow();

        Message message = new Message();
        message.setSender(sender);
        message.setSenderBranch(senderProfile.getBranch());
        message.setContent(dto.getContent());
        message.setRecipientRole(dto.getRecipientRole());

        // Handle sending to a branch vs. Super Admin
        if (dto.getRecipientRole() == UserAccount.Role.SUPER_ADMIN) {
            message.setReceiverBranch(null); // No specific branch for Super Admin
        } else {
            Branch receiverBranch = branchRepository.findById(dto.getReceiverBranchId()).orElseThrow();
            message.setReceiverBranch(receiverBranch);
        }

        messageRepository.save(message);
    }

    // ... your other existing methods for notifications ...


    @Override
    public List<MessageDto> getConversation(Long myBranchId, UserAccount.Role myRole, Long otherBranchId, UserAccount.Role otherRole) {
        // --- Use the new, powerful repository method ---
        List<Message> messages = messageRepository.findAdvancedConversation(myBranchId, myRole, otherBranchId, otherRole);
        return messages.stream()
                .map(this::convertToMessageDto)
                .collect(Collectors.toList());
    }














}