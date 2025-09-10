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

//    private MessageDto convertToMessageDto(Message message) {
//        MessageDto dto = new MessageDto();
//        dto.setContent(message.getContent());
//        dto.setTimestamp(message.getTimestamp());
//
//        StaffProfile senderProfile = staffProfileRepository.findById(message.getSender().getUserId()).orElse(new StaffProfile());
//        dto.setSenderFullName(senderProfile.getFullName());
//        dto.setSenderRole(message.getSender().getRole().name());
//        dto.setSenderBranchId(message.getSenderBranch().getId());
//        dto.setSenderBranchName(message.getSenderBranch().getName());
//        return dto;
//    }
//
//
//







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









    @Override
    public List<ContactDto> getContacts(Integer userId, Long myBranchId) {
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

    private ContactDto createContactDto(Long branchId, String branchName, String role) {
        ContactDto dto = new ContactDto();
        dto.setBranchId(branchId);
        dto.setBranchName(branchName);
        dto.setRole(role);
        return dto;
    }



    @Override
    public List<MessageDto> getConversation(Long myBranchId, UserAccount.Role myRole, Long otherBranchId, UserAccount.Role otherRole) {
        // --- Use the new, powerful repository method ---
        List<Message> messages = messageRepository.findAdvancedConversation(myBranchId, myRole, otherBranchId, otherRole);
        return messages.stream()
                .map(this::convertToMessageDto)
                .collect(Collectors.toList());


    }





//
//    public List<ContactDto> getReceptionistContacts(Long branchId, Integer myUserId) {
//        // Find the Branch Admin of the same branch
//        List<UserAccount> branchAdmin = userAccountRepository.findBranchAdminInBranch(branchId);
//        // Find all other Receptionists in the same branch
//        List<UserAccount> colleagues = userAccountRepository.findColleaguesInBranch(branchId, myUserId);
//
//        List<ContactDto> contacts = new ArrayList<>();
//        branchAdmin.forEach(ua -> contacts.add(convertToContactDto(ua)));
//        colleagues.forEach(ua -> contacts.add(convertToContactDto(ua)));
//        return contacts;
//    }
//
//    public void sendMessage(Integer senderUserId, MessageRequestDto dto) {
//        UserAccount sender = userAccountRepository.findById(senderUserId).orElseThrow();
//        UserAccount receiver = userAccountRepository.findById(dto.getReceiverId()).orElseThrow();
//
//        Message message = new Message();
//        message.setSender(sender);
//        message.setReceiver(receiver);
//        message.setContent(dto.getContent());
//        messageRepository.save(message);
//    }

















    @Override
    public List<ContactDto> getReceptionistContacts(Long branchId, Integer myUserId) {
        // 1. Find the Branch Admin of the same branch
        List<UserAccount> branchAdmins = userAccountRepository.findBranchAdminInBranch(branchId);

        // 2. Find all other Receptionists (colleagues) in the same branch
        List<UserAccount> colleagues = userAccountRepository.findColleaguesInBranch(branchId, myUserId);

        // 3. Convert both lists to Contact DTOs and combine them
        List<ContactDto> contacts = new ArrayList<>();
        branchAdmins.stream().map(this::convertToContactDto).forEach(contacts::add);
        colleagues.stream().map(this::convertToContactDto).forEach(contacts::add);

        return contacts;
    }

    @Override
    public List<MessageDto> getConversation(Integer userId1, Integer userId2) {
        List<Message> messages = messageRepository.findConversation(userId1, userId2);
        return messages.stream()
                .map(this::convertToMessageDto)
                .collect(Collectors.toList());
    }


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

        // --- CRITICAL FIX: Handle messages to Super Admin ---
        if (dto.getRecipientRole() == UserAccount.Role.SUPER_ADMIN) {
            // Find a Super Admin to be the receiver
            UserAccount superAdminReceiver = userAccountRepository.findAllByRole(UserAccount.Role.SUPER_ADMIN)
                    .stream().findFirst().orElseThrow(() -> new RuntimeException("No Super Admin found to receive the message"));
            message.setReceiver(superAdminReceiver);
            message.setReceiverBranch(null); // Super Admins have no branch
        } else {
            // Logic for sending to a branch
            Branch receiverBranch = branchRepository.findById(dto.getReceiverBranchId())
                    .orElseThrow(() -> new RuntimeException("Receiver branch not found"));
            // Find the Branch Admin of that branch to be the receiver
            UserAccount branchAdminReceiver = userAccountRepository.findBranchAdminInBranch(receiverBranch.getId())
                    .stream().findFirst().orElseThrow(() -> new RuntimeException("No Branch Admin found in the recipient branch"));
            message.setReceiver(branchAdminReceiver);
            message.setReceiverBranch(receiverBranch);
        }

        messageRepository.save(message);
    }
    // --- Helper method to convert UserAccount to ContactDto ---
    private ContactDto convertToContactDto(UserAccount account) {
        StaffProfile profile = staffProfileRepository.findById(account.getUserId())
                .orElseThrow(() -> new RuntimeException("Profile not found for user: " + account.getUsername()));

        ContactDto dto = new ContactDto();
        dto.setUserId(account.getUserId());
        dto.setFullName(profile.getFullName());
        dto.setRole(account.getRole().name());
        return dto;
    }

//    // --- Helper method to convert Message Entity to Message DTO ---
//    private MessageDto convertToMessageDto(Message message) {
//        MessageDto dto = modelMapper.map(message, MessageDto.class);
//        StaffProfile senderProfile = staffProfileRepository.findById(message.getSender().getUserId()).orElseThrow();
//        dto.setSenderFullName(senderProfile.getFullName());
//        dto.setSenderRole(message.getSender().getRole().name());
//        return dto;
//    }
//
//
    private MessageDto convertToMessageDto(Message message) {
        MessageDto dto = new MessageDto();

        // --- CRITICAL FIX: Manually map the fields ---
        dto.setContent(message.getContent());
        dto.setTimestamp(message.getTimestamp());

        // Map sender details from the UserAccount
        if (message.getSender() != null) {
            dto.setSenderId(message.getSender().getUserId());
            dto.setSenderRole(message.getSender().getRole().name());

            // Get full name from the linked profile
            StaffProfile senderProfile = staffProfileRepository.findById(message.getSender().getUserId()).orElse(null);
            if (senderProfile != null) {
                dto.setSenderFullName(senderProfile.getFullName());
            }
        }

        // Map sender branch details from the Branch entity
        if (message.getSenderBranch() != null) {
            dto.setSenderBranchId(message.getSenderBranch().getId());
            dto.setSenderBranchName(message.getSenderBranch().getName());
        }

        return dto;
    }













    @Override
    public List<ContactDto> getSuperAdminContacts() {
        return userAccountRepository.findAllStaffUsers().stream()
                .map(this::convertToContactDtos)
                .collect(Collectors.toList());
    }

    // This helper method is reused
    private ContactDto convertToContactDtos(UserAccount account) {
        StaffProfile profile = staffProfileRepository.findById(account.getUserId())
                .orElse(new StaffProfile());

        ContactDto dto = new ContactDto();
        dto.setUserId(account.getUserId());
        dto.setFullName(profile.getFullName());
        dto.setRole(account.getRole().name());
        if (profile.getBranch() != null) {
            dto.setBranchName(profile.getBranch().getName());
        }
        return dto;
    }







    @Transactional
    public void sendMessageSuper(Integer senderUserId, MessageRequestDto dto) {
        UserAccount sender = userAccountRepository.findById(senderUserId)
                .orElseThrow(() -> new RuntimeException("Sender user not found"));
        UserAccount receiver = userAccountRepository.findById(dto.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver user not found"));

        // Find profiles for both users (they might not exist, e.g., for online users)
        StaffProfile senderProfile = staffProfileRepository.findById(senderUserId).orElse(null);
        StaffProfile receiverProfile = staffProfileRepository.findById(receiver.getUserId()).orElse(null);

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(dto.getContent());

        // --- CRITICAL FIX: Set sender and receiver branch correctly ---
        message.setSenderBranch(senderProfile != null ? senderProfile.getBranch() : null);
        message.setReceiverBranch(receiverProfile != null ? receiverProfile.getBranch() : null);

        // Set the role of the intended recipient
        message.setRecipientRole(receiver.getRole());

        messageRepository.save(message);
    }





















}