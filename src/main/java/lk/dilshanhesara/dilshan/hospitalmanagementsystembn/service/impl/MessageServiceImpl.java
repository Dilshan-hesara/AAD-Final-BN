package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.impl;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.MessageDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.MessageRequestDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Branch;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Message;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.BranchRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.MessageRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

//... imports
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final BranchRepository branchRepository;
    private final ModelMapper modelMapper;
    @Override
    public List<MessageDto> getConversation(Long myBranchId, Long otherBranchId) {
        return messageRepository.findConversation(myBranchId, otherBranchId).stream()
                .map(msg -> modelMapper.map(msg, MessageDto.class))
                .collect(Collectors.toList());
    }
    @Override
    public void sendMessage(Long senderBranchId, MessageRequestDto dto) {
        Branch sender = branchRepository.findById(senderBranchId).orElseThrow();
        Branch receiver = branchRepository.findById(dto.getReceiverBranchId()).orElseThrow();
        Message message = new Message();
        message.setSenderBranch(sender);
        message.setReceiverBranch(receiver);
        message.setContent(dto.getContent());
        messageRepository.save(message);
    }
}