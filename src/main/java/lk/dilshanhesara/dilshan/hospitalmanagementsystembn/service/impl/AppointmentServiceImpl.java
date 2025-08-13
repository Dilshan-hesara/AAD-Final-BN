package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.impl;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.AppointmentRequestDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.AppointmentResponseDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.BranchDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.*;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.*;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.AppointmentService;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.PatientService;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.SettingsService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {






        private final AppointmentRepository appointmentRepository;
        private final PatientRepository patientRepository;
        private final DoctorRepository doctorRepository;
        private final BranchRepository branchRepository;
        private final ModelMapper modelMapper;

        @Override
        public List<AppointmentResponseDto> findAppointmentsByBranch(Long branchId) {
            return appointmentRepository.findByBranch_Id(branchId).stream()
                    .map(app -> {
                        AppointmentResponseDto dto = new AppointmentResponseDto();
                        dto.setId(app.getId());
                        dto.setPatientName(app.getPatient().getFullName());
                        dto.setDoctorName(app.getDoctor().getFullName());
                        dto.setAppointmentDate(app.getAppointmentDate());
                        dto.setStatus(app.getStatus());
                        return dto;
                    }).collect(Collectors.toList());
        }

        @Override
        public void createAppointment(AppointmentRequestDto dto, Long branchId) {
            Patient patient = patientRepository.findById(dto.getPatientId()).orElseThrow();
            Doctor doctor = doctorRepository.findById(dto.getDoctorId()).orElseThrow();
            Branch branch = branchRepository.findById(branchId).orElseThrow();

            Appointment appointment = new Appointment();
            appointment.setPatient(patient);
            appointment.setDoctor(doctor);
            appointment.setBranch(branch);
            appointment.setAppointmentDate(dto.getAppointmentDate());
            appointment.setStatus("CONFIRMED"); // Or PENDING_PAYMENT

            appointmentRepository.save(appointment);
        }



    // --- ADD THIS NEW METHOD ---
    @Override
    public void updateAppointmentStatus(Long appointmentId, String newStatus) {
        // Find the appointment in the database
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + appointmentId));

        // Update the status
        appointment.setStatus(newStatus);

        // Save the changes
        appointmentRepository.save(appointment);
    }





    // --- ADD THIS NEW METHOD IMPLEMENTATION ---
    @Override
    public List<AppointmentResponseDto> findAppointmentsByUsername(String username) {
        // Use the new repository method to find appointments
        return appointmentRepository.findAppointmentsByOnlineUsername(username).stream()
                .map(app -> {
                    // Convert each Appointment entity to a DTO
                    AppointmentResponseDto dto = new AppointmentResponseDto();
                    dto.setId(app.getId());
                    dto.setPatientName(app.getPatient().getFullName());
                    dto.setDoctorName(app.getDoctor().getFullName());
                    dto.setBranchName(app.getBranch().getName());
                    dto.setAppointmentDate(app.getAppointmentDate());
                    dto.setStatus(app.getStatus());
                    return dto;
                }).collect(Collectors.toList());
    }





    private final UserAccountRepository userAccountRepository;
    private final PatientService patientService; // <-- Now this exists





    @Override
    @Transactional
    public Appointment createAppointmentForOnlineUser(AppointmentRequestDto dto, String username) {
        // ... (logic to get/create patient, doctor, branch)

        Appointment appointment = new Appointment();
        // ... (set patient, doctor, branch, etc.)
        appointment.setReason(dto.getReason());

        // Set initial status and a sample fee
        appointment.setStatus("PENDING_PAYMENT");
        appointment.setFee(new java.math.BigDecimal("2500.00")); // Example fee

        return appointmentRepository.save(appointment); // Return the saved appointment
    }

    // --- ADD THIS NEW METHOD TO CONFIRM PAYMENT ---
    @Override
    public void confirmAppointmentPayment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        appointment.setStatus("CONFIRMED");
        appointmentRepository.save(appointment);
    }


    @Override
    public List<AppointmentResponseDto> findOnlineUserAppointmentsForToday(Long branchId) {
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        return appointmentRepository.findOnlineUserAppointmentsForToday(branchId, startOfDay, endOfDay)
                .stream().map(app -> {
                    // 1. Create a new DTO
                    AppointmentResponseDto dto = new AppointmentResponseDto();
                    dto.setId(app.getId());
                    dto.setPatientName(app.getPatient().getFullName());
                    dto.setDoctorName(app.getDoctor().getFullName());
                    dto.setBranchName(app.getBranch().getName());
                    dto.setAppointmentDate(app.getAppointmentDate());
                    dto.setStatus(app.getStatus());

                    // 2. CRITICAL: Return the created DTO
                    return dto;
                }).collect(Collectors.toList());
    }






//    SERCH


    public Page<AppointmentResponseDto> searchAppointments(Long branchId, String patientName, String status, LocalDate date, Pageable pageable) {
        // Create a specification to build the dynamic query
        Specification<Appointment> spec = Specification.where(hasBranchId(branchId));

        if (patientName != null && !patientName.isEmpty()) {
            spec = spec.and(patientNameContains(patientName));
        }
        if (status != null && !status.isEmpty()) {
            spec = spec.and(hasStatus(status));
        }
        if (date != null) {
            spec = spec.and(isonDate(date));
        }

        Page<Appointment> appointments = appointmentRepository.findAll(spec, pageable);

        // Convert the Page of entities to a Page of DTOs
        return appointments.map(app -> modelMapper.map(app, AppointmentResponseDto.class));
    }

    // --- Specification helper methods ---
    private Specification<Appointment> hasBranchId(Long branchId) {
        return (root, query, cb) -> cb.equal(root.get("branch").get("id"), branchId);
    }
    private Specification<Appointment> patientNameContains(String name) {
        return (root, query, cb) -> cb.like(root.get("patient").get("fullName"), "%" + name + "%");
    }
    private Specification<Appointment> hasStatus(String status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }
    private Specification<Appointment> isonDate(LocalDate date) {
        return (root, query, cb) -> cb.between(root.get("appointmentDate"), date.atStartOfDay(), date.atTime(LocalTime.MAX));
    }



    // pric adding booking appiment

    private static final BigDecimal DEFAULT_APPOINTMENT_FEE = new BigDecimal("2500.00");

    @Override
    public Appointment createAppointmentForWalk(AppointmentRequestDto dto, String username) {
        // ... your existing logic to get patient, doctor, etc.

        Appointment appointment = new Appointment();
        // ... set patient, doctor, date, etc.
        appointment.setReason(dto.getReason());
        appointment.setStatus("PENDING_PAYMENT");

        // Set the default fee
        appointment.setFee(DEFAULT_APPOINTMENT_FEE);

        return appointmentRepository.save(appointment);
    }
    private final SettingsService settingsService; // <-- INJECT THE SETTINGS SERVICE


    @Override
    @Transactional
    public Appointment createAppointmentByStaff(AppointmentRequestDto dto, Long branchId) {
        // Find the patient and doctor from the DTO
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new RuntimeException("Branch not found"));

        // Create the new appointment entity
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setBranch(branch);
        appointment.setAppointmentDate(dto.getAppointmentDate());
        appointment.setReason(dto.getReason());
        appointment.setStatus("CONFIRMED"); // Staff bookings can be confirmed directly
        appointment.setFee(settingsService.getAppointmentFee());

        // Save and return the new appointment
        return appointmentRepository.save(appointment);
    }




//    bill mange



    // --- ADD THIS NEW METHOD ---
    @Override
    public AppointmentResponseDto findAppointmentById(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        // Convert the entity to a DTO
        return modelMapper.map(appointment, AppointmentResponseDto.class);
    }







    // In BranchServiceImpl.java
    @Override
    public List<BranchDto> getAllBranches() {
        return branchRepository.findAll().stream()
                .map(branch -> modelMapper.map(branch, BranchDto.class))
                .collect(Collectors.toList());
    }
}


