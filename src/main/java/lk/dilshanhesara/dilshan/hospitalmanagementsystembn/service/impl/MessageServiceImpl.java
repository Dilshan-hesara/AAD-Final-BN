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

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final BranchRepository branchRepository;
    private final ModelMapper modelMapper;


    private final UserAccountRepository userAccountRepository;
    private final StaffProfileRepository staffProfileRepository;

    @Override
    public List<MessageDto> getConversation(Long myBranchId, Long otherBranchId) {
        return messageRepository.findConversation(myBranchId, otherBranchId).stream()
                .map(this::convertToMessageDto).collect(Collectors.toList());
    }


    @Override
    public long getUnreadMessageCount(Long branchId, UserAccount.Role role) {
        return messageRepository.countByReceiverBranchIdAndRecipientRoleAndIsReadFalse(branchId, role);
    }

    @Override
    @Transactional
    public void markConversationAsRead(Long myBranchId, Long otherBranchId, UserAccount.Role myRole) {
        List<Message> messages = messageRepository.findConversation(myBranchId, otherBranchId);
        messages.forEach(msg -> {
            if (msg.getReceiverBranch().getId().equals(myBranchId) && msg.getRecipientRole() == myRole) {
                msg.setRead(true);
            }
        });
        messageRepository.saveAll(messages);
    }



    @Override
    public List<ContactDto> getContacts(Integer userId, Long myBranchId) {
        List<ContactDto> superAdmins = userAccountRepository.findAllByRole(UserAccount.Role.SUPER_ADMIN)
                .stream()
                .map(ua -> {
                    ContactDto dto = new ContactDto();
                    dto.setBranchName("Super Admin");
                    dto.setRole("SUPER_ADMIN");
                    return dto;
                }).collect(Collectors.toList());

        List<ContactDto> branches = branchRepository.findAll().stream()
                .filter(branch -> !branch.getId().equals(myBranchId))
                .flatMap(branch -> Stream.of(
                        createContactDto(branch.getId(), branch.getName(), "BRANCH_ADMIN"),
                        createContactDto(branch.getId(), branch.getName(), "RECEPTIONIST")
                )).collect(Collectors.toList());

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
        List<Message> messages = messageRepository.findAdvancedConversation(myBranchId, myRole, otherBranchId, otherRole);
        return messages.stream()
                .map(this::convertToMessageDto)
                .collect(Collectors.toList());


    }



    @Override
    public List<ContactDto> getReceptionistContacts(Long branchId, Integer myUserId) {
        List<UserAccount> branchAdmins = userAccountRepository.findBranchAdminInBranch(branchId);

        List<UserAccount> colleagues = userAccountRepository.findColleaguesInBranch(branchId, myUserId);

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

        if (dto.getRecipientRole() == UserAccount.Role.SUPER_ADMIN) {
            UserAccount superAdminReceiver = userAccountRepository.findAllByRole(UserAccount.Role.SUPER_ADMIN)
                    .stream().findFirst().orElseThrow(() -> new RuntimeException("No Super Admin found to receive the message"));
            message.setReceiver(superAdminReceiver);
            message.setReceiverBranch(null);
        } else {
            Branch receiverBranch = branchRepository.findById(dto.getReceiverBranchId())
                    .orElseThrow(() -> new RuntimeException("Receiver branch not found"));
            UserAccount branchAdminReceiver = userAccountRepository.findBranchAdminInBranch(receiverBranch.getId())
                    .stream().findFirst().orElseThrow(() -> new RuntimeException("No Branch Admin found in the recipient branch"));
            message.setReceiver(branchAdminReceiver);
            message.setReceiverBranch(receiverBranch);
        }

        messageRepository.save(message);
    }
    private ContactDto convertToContactDto(UserAccount account) {
        StaffProfile profile = staffProfileRepository.findById(account.getUserId())
                .orElseThrow(() -> new RuntimeException("Profile not found for user: " + account.getUsername()));

        ContactDto dto = new ContactDto();
        dto.setUserId(account.getUserId());
        dto.setFullName(profile.getFullName());
        dto.setRole(account.getRole().name());
        return dto;
    }


    private MessageDto convertToMessageDto(Message message) {
        MessageDto dto = new MessageDto();

        dto.setContent(message.getContent());
        dto.setTimestamp(message.getTimestamp());

        if (message.getSender() != null) {
            dto.setSenderId(message.getSender().getUserId());
            dto.setSenderRole(message.getSender().getRole().name());

            StaffProfile senderProfile = staffProfileRepository.findById(message.getSender().getUserId()).orElse(null);
            if (senderProfile != null) {
                dto.setSenderFullName(senderProfile.getFullName());
            }
        }

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

        StaffProfile senderProfile = staffProfileRepository.findById(senderUserId).orElse(null);
        StaffProfile receiverProfile = staffProfileRepository.findById(receiver.getUserId()).orElse(null);

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(dto.getContent());

        message.setSenderBranch(senderProfile != null ? senderProfile.getBranch() : null);
        message.setReceiverBranch(receiverProfile != null ? receiverProfile.getBranch() : null);

        message.setRecipientRole(receiver.getRole());

        messageRepository.save(message);
    }













// super adming log send msg

//    @Override
//    @Transactional
//    public void sendMessage(Integer senderUserId, MessageRequestDto dto) {
//        UserAccount sender = userAccountRepository.findById(senderUserId)
//                .orElseThrow(() -> new RuntimeException("Sender user not found"));
//        UserAccount receiver = userAccountRepository.findById(dto.getReceiverId())
//                .orElseThrow(() -> new RuntimeException("Receiver user not found"));
//
//        // --- CRITICAL FIX: Handle the case where the sender is a Super Admin ---
//        // A Super Admin might not have a StaffProfile, so we find it optionally.
//        StaffProfile senderProfile = staffProfileRepository.findById(senderUserId).orElse(null);
//
//        Message message = new Message();
//        message.setSender(sender);
//        message.setReceiver(receiver);
//        message.setContent(dto.getContent());
//
//        // If the sender has a profile (is not a Super Admin), set their branch.
//        if (senderProfile != null) {
//            message.setSenderBranch(senderProfile.getBranch());
//        } else {
//            message.setSenderBranch(null); // Super Admin has no branch
//        }
//
//        messageRepository.save(message);
//    }







}