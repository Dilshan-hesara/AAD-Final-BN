package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.impl;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.BranchDetailDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.BranchDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.BranchSummaryDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Branch;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.StaffProfile;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.UserAccount;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.*;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.BranchService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BranchServiceImpl implements BranchService {

    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private StaffProfileRepository staffProfileRepository;
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private  PatientRepository patientRepository;

    @Autowired
    private  DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
    public Branch getBranchById(Long branchId) {
        return branchRepository.findById(branchId)
                .orElseThrow(() -> new RuntimeException("Branch not found with id: " + branchId));
    }

    @Override
    public Long getCurrentAdminBranchId() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();


        UserAccount account = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User account not found for username: " + username));


        StaffProfile profile = staffProfileRepository.findById(account.getUserId())
                .orElseThrow(() -> new RuntimeException("Staff profile not found for user ID: " + account.getUserId()));

        if (profile.getBranch() == null) {
            throw new RuntimeException("Admin is not assigned to any branch.");
        }

        return profile.getBranch().getId();
    }


    // ... other autowired repositories
    @Autowired
    private ModelMapper modelMapper;


    // --- ADD THIS NEW METHOD IMPLEMENTATION ---
    @Override
    public List<BranchDto> getAllBranches() {
        // Fetch all branch entities from the database
        List<Branch> branches = branchRepository.findAll();

        // Convert the list of entities to a list of DTOs
        return branches.stream()
                .map(branch -> modelMapper.map(branch, BranchDto.class))
                .collect(Collectors.toList());
    }


    //super admin

//
//    public List<BranchDetailDto> getAllBranchDetails() {
//        List<Branch> branches = branchRepository.findAll();
//
//        return branches.stream().map(branch -> {
//            // For each branch, get its statistics
//            LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
//            LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
//
//            // You need to implement these count methods in your repositories
//            long patientCount = patientRepository.countByAppointments_Branch_Id(branch.getId());
//            long doctorCount = doctorRepository.countByBranch_Id(branch.getId());
//            long appointmentsToday = appointmentRepository.countByBranch_IdAndAppointmentDateBetween(branch.getId(), startOfDay, endOfDay);
//
//            // Create the DTO
//            BranchDetailDto dto = new BranchDetailDto();
//            dto.setId(branch.getId());
//            dto.setName(branch.getName());
//            dto.setLocation(branch.getLocation());
//            dto.setPatientCount(patientCount);
//            dto.setDoctorCount(doctorCount);
//            dto.setAppointmentsTodayCount(appointmentsToday);
//
//            return dto;
//        }).collect(Collectors.toList());
//    }

//    @Override
//    public List<BranchSummaryDto> getAllBranchSummaries() {
//        return branchRepository.findAll().stream().map(branch -> {
//            BranchSummaryDto dto = modelMapper.map(branch, BranchSummaryDto.class);
//            // Logic to calculate patient and doctor counts for the branch
//            dto.(patientRepository.countByBranchId(branch.getId()));
//            dto.setDoctorCount(doctorRepository.countByBranch_Id(branch.getId()));
//            return dto;
//        }).collect(Collectors.toList());
//    }
    @Override
    public Branch addBranch(BranchDto branchDto) {
        Branch branch = modelMapper.map(branchDto, Branch.class);
        return branchRepository.save(branch);
    }


    //mange super admin branch deatals

    @Override
    public void updateBranch(Long branchId, BranchDto branchDto) {
        Branch branch = branchRepository.findById(branchId).orElseThrow();
        branch.setName(branchDto.getName());
        branch.setLocation(branchDto.getLocation());
        branch.setContactNumber(branchDto.getContactNumber());
        branch.setEmail(branchDto.getEmail());
        branchRepository.save(branch);
    }
    @Override
    public void deleteBranch(Long branchId) {
        branchRepository.deleteById(branchId);
    }
    @Override
    public void updateBranchStatus(Long branchId, String status) {
        Branch branch = branchRepository.findById(branchId).orElseThrow();
        branch.setStatus(status);
        branchRepository.save(branch);
    }




    @Override
    public List<BranchSummaryDto> getAllBranchSummaries() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        return branchRepository.findAll().stream().map(branch -> {
            BranchSummaryDto dto = modelMapper.map(branch, BranchSummaryDto.class);

            // Fetch all required counts
            dto.setTotalPatientCount(patientRepository.countTotalPatientsByBranch(branch.getId()));
            dto.setActiveDoctorCount(doctorRepository.countByBranch_IdAndStatus(branch.getId(), "ACTIVE"));
            dto.setTotalAppointmentCount(appointmentRepository.countByBranch_Id(branch.getId()));
            dto.setTodaysAppointmentCount(appointmentRepository.countByBranch_IdAndAppointmentDateBetween(branch.getId(), startOfDay, endOfDay));
            dto.setActiveReceptionistCount(userAccountRepository.countActiveReceptionistsByBranch(branch.getId()));

            return dto;
        }).collect(Collectors.toList());
    }


    // act and incavte brnach

    @Override
    public List<BranchDto> getActiveBranches() {
        return branchRepository.findByStatus("ACTIVE").stream()
                .map(branch -> modelMapper.map(branch, BranchDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<BranchDto> getInactiveBranches() {
        return branchRepository.findByStatus("INACTIVE").stream()
                .map(branch -> modelMapper.map(branch, BranchDto.class))
                .collect(Collectors.toList());
    }
}