package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Message;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
public interface MessageRepository extends JpaRepository<Message, Long> {



    @Query("SELECT m FROM Message m WHERE (m.senderBranch.id = :branch1Id AND m.receiverBranch.id = :branch2Id) OR (m.senderBranch.id = :branch2Id AND m.receiverBranch.id = :branch1Id) ORDER BY m.timestamp ASC")
    List<Message> findConversation(Long branch1Id, Long branch2Id);

    long countByReceiverBranchIdAndRecipientRoleAndIsReadFalse(Long branchId, UserAccount.Role role);

    @Query("SELECT m FROM Message m WHERE " +
            // Messages between two branches, targeting specific roles
            "((m.senderBranch.id = :branch1Id AND m.receiverBranch.id = :branch2Id AND m.recipientRole = :role2 AND m.sender.role = :role1) OR " +
            "(m.senderBranch.id = :branch2Id AND m.receiverBranch.id = :branch1Id AND m.recipientRole = :role1 AND m.sender.role = :role2)) OR " +
            // Messages from a branch TO Super Admin
            "((m.senderBranch.id = :branch1Id AND m.recipientRole = 'SUPER_ADMIN' AND :role2 = 'SUPER_ADMIN') OR " +
            // Messages TO a branch FROM Super Admin
            "(m.receiverBranch.id = :branch1Id AND m.sender.role = 'SUPER_ADMIN' AND m.recipientRole = :role1)) " +
            "ORDER BY m.timestamp ASC")
    List<Message> findAdvancedConversation(Long branch1Id, UserAccount.Role role1, Long branch2Id, UserAccount.Role role2);





    @Query("SELECT m FROM Message m WHERE (m.sender.id = :userId1 AND m.receiver.id = :userId2) OR (m.sender.id = :userId2 AND m.receiver.id = :userId1) ORDER BY m.timestamp ASC")
    List<Message> findConversation(Integer userId1, Integer userId2);

    long countByReceiver_UserIdAndIsReadFalse(Integer receiverId);


}