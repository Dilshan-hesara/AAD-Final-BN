package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.impl;


import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.StaffCreationRequestDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.StaffProfileDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Branch;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.StaffProfile;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.UserAccount;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.BranchRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.StaffProfileRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.UserAccountRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.ReceptionistService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReceptionistServiceImpl implements ReceptionistService {

    private final UserAccountRepository userAccountRepository;
    private final StaffProfileRepository staffProfileRepository;
    private final BranchRepository branchRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<StaffProfileDto> findReceptionistsByBranch(Long branchId) {
        return userAccountRepository.findReceptionistsByBranch(branchId).stream()
                .map(account -> {
                    StaffProfile profile = staffProfileRepository.findById(account.getUserId()).orElse(new StaffProfile());
                    StaffProfileDto dto = new StaffProfileDto();
                    dto.setUserId(account.getUserId());
                    dto.setUsername(account.getUsername());
                    dto.setRole(account.getRole().name());
                    dto.setFullName(profile.getFullName());
                    dto.setBranchName(profile.getBranch() != null ? profile.getBranch().getName() : null);
                    return dto;
                }).collect(Collectors.toList());
    }

    @Override
    public void addReceptionist(StaffCreationRequestDto dto) {
        Branch branch = branchRepository.findById(dto.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found"));

        // Create the user account
        UserAccount account = new UserAccount();
        account.setUsername(dto.getUsername());
        account.setPassword(passwordEncoder.encode(dto.getPassword()));
        account.setRole(UserAccount.Role.RECEPTIONIST);
        account.setActive(true);
        account = userAccountRepository.save(account);

        // Create the staff profile
        StaffProfile profile = new StaffProfile();
        profile.setUserAccount(account);
        profile.setFullName(dto.getFullName());
        profile.setBranch(branch);
        staffProfileRepository.save(profile);
    }
}