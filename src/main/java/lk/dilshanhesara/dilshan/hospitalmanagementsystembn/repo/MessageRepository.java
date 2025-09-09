package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Message;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
public interface MessageRepository extends JpaRepository<Message, Long> {
//    @Query("SELECT m FROM Message m WHERE (m.senderBranch.id = :branch1Id AND m.receiverBranch.id = :branch2Id) OR (m.senderBranch.id = :branch2Id AND m.receiverBranch.id = :branch1Id) ORDER BY m.timestamp ASC")
//    List<Message> findConversation(Long branch1Id, Long branch2Id);


    @Query("SELECT m FROM Message m WHERE (m.senderBranch.id = :branch1Id AND m.receiverBranch.id = :branch2Id) OR (m.senderBranch.id = :branch2Id AND m.receiverBranch.id = :branch1Id) ORDER BY m.timestamp ASC")
    List<Message> findConversation(Long branch1Id, Long branch2Id);

    long countByReceiverBranchIdAndRecipientRoleAndIsReadFalse(Long branchId, UserAccount.Role role);

}