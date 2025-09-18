package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.impl;

import jakarta.persistence.criteria.Join;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.*;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.*;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.*;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.AppointmentService;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.NotificationService;
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



    @Override
    public void updateAppointmentStatus(Long appointmentId, String newStatus) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + appointmentId));

        appointment.setStatus(newStatus);

        appointmentRepository.save(appointment);
    }








    private final UserAccountRepository userAccountRepository;
    private final PatientService patientService;





    @Override
    @Transactional
    public Appointment createAppointmentForOnlineUser(AppointmentRequestDto dto, String username) {

        Appointment appointment = new Appointment();
        appointment.setReason(dto.getReason());

        appointment.setStatus("PENDING_PAYMENT");
        appointment.setFee(new java.math.BigDecimal("2500.00"));

        return appointmentRepository.save(appointment);
    }


    @Override
    public List<AppointmentResponseDto> findOnlineUserAppointmentsForToday(Long branchId) {
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        return appointmentRepository.findOnlineUserAppointmentsForToday(branchId, startOfDay, endOfDay)
                .stream().map(app -> {
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






//    SERCH


    public Page<AppointmentResponseDto> searchAppointments(Long branchId, String patientName, String status, LocalDate date, Pageable pageable) {
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

        return appointments.map(app -> modelMapper.map(app, AppointmentResponseDto.class));
    }

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

        Appointment appointment = new Appointment();
        appointment.setReason(dto.getReason());
        appointment.setStatus("PENDING_PAYMENT");

        appointment.setFee(DEFAULT_APPOINTMENT_FEE);

        return appointmentRepository.save(appointment);
    }
    private final SettingsService settingsService;


    @Override
    @Transactional
    public Appointment createAppointmentByStaff(AppointmentRequestDto dto, Long branchId) {
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new RuntimeException("Branch not found"));

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setBranch(branch);
        appointment.setAppointmentDate(dto.getAppointmentDate());
        appointment.setReason(dto.getReason());
        appointment.setStatus("CONFIRMED");
        appointment.setFee(settingsService.getAppointmentFee());

        return appointmentRepository.save(appointment);
    }




//    bill mange



    @Override
    public AppointmentResponseDto findAppointmentById(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        return modelMapper.map(appointment, AppointmentResponseDto.class);
    }





    @Override
    public List<BranchDto> getAllBranches() {
        return branchRepository.findAll().stream()
                .map(branch -> modelMapper.map(branch, BranchDto.class))
                .collect(Collectors.toList());
    }



    @Override
    public Page<AppointmentResponseDto> searchAllAppointments(String patientKeyword, String doctorKeyword, Long branchId, String status, LocalDate date, Pageable pageable) {

        Specification<Appointment> spec = (root, query, cb) -> {
            query.distinct(true);
            return cb.conjunction();
        };

        if (patientKeyword != null && !patientKeyword.isEmpty()) {
            spec = spec.and((root, query, cb) -> {
                Join<Appointment, Patient> patientJoin = root.join("patient");
                return cb.or(
                        cb.like(patientJoin.get("fullName"), "%" + patientKeyword + "%"),
                        cb.like(patientJoin.get("contactNumber"), "%" + patientKeyword + "%")
                );
            });
        }

        if (doctorKeyword != null && !doctorKeyword.isEmpty()) {
            spec = spec.and((root, query, cb) -> {
                Join<Appointment, Doctor> doctorJoin = root.join("doctor");
                return cb.or(
                        cb.like(doctorJoin.get("fullName"), "%" + doctorKeyword + "%"),
                        cb.like(doctorJoin.get("specialization"), "%" + doctorKeyword + "%"),
                        cb.like(doctorJoin.get("contactNumber"), "%" + doctorKeyword + "%")
                );
            });
        }

        if (branchId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("branch").get("id"), branchId));
        }

        if (status != null && !status.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }

        if (date != null) {
            spec = spec.and((root, query, cb) -> cb.between(root.get("appointmentDate"), date.atStartOfDay(), date.atTime(LocalTime.MAX)));
        }

        Page<Appointment> appointments = appointmentRepository.findAll(spec, pageable);
        return appointments.map(app -> modelMapper.map(app, AppointmentResponseDto.class));
    }




    @Override
    public AppointmentResponseDto findAppointmentByIdSperAD(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + appointmentId));

        AppointmentResponseDto dto = modelMapper.map(appointment, AppointmentResponseDto.class);
        dto.setPatientName(appointment.getPatient().getFullName());
        dto.setDoctorName(appointment.getDoctor().getFullName());
        dto.setBranchName(appointment.getBranch().getName());

        return dto;
    }


    @Override
    public List<AppointmentResponseDto> findAppointmentsByUsername(String username) {
        return appointmentRepository.findAppointmentsByOnlineUsername(username).stream()
                .map(app -> {
                    AppointmentResponseDto dto = new AppointmentResponseDto();
                    dto.setId(app.getId());
                    dto.setDoctorName(app.getDoctor().getFullName());
                    dto.setBranchName(app.getBranch().getName());
                    dto.setAppointmentDate(app.getAppointmentDate());
                    dto.setStatus(app.getStatus());
                    return dto;
                }).collect(Collectors.toList());
    }



    @Override
    @Transactional
    public Appointment createAppointmentForOnlineUser(OnlineUserAppointmentRequestDto dto, String username) {
        UserAccount account = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User account not found"));

        Patient patient = patientService.getOrCreatePatientForOnlineUser(account.getUserId());

        Doctor doctor = doctorRepository.findById(dto.getDoctorId()).orElseThrow();
        Branch branch = branchRepository.findById(dto.getBranchId()).orElseThrow();

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setBranch(branch);
        appointment.setAppointmentDate(dto.getAppointmentDate());
        appointment.setReason(dto.getReason());
        appointment.setStatus("PENDING_PAYMENT");
        appointment.setFee(new BigDecimal("2500.00"));

        return appointmentRepository.save(appointment);
    }


    @Override
    public void confirmAppointmentPayment(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setStatus("CONFIRMED");
        appointmentRepository.save(appointment);
    }






    private final OnlineUserProfileRepository profileRepository;

    @Override
    public OnlineUserDashboardDto getDashboardData(String username) {
        OnlineUserProfile profile = profileRepository.findByUserAccount_Username(username)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        LocalDateTime now = LocalDateTime.now();

        AppointmentResponseDto upcomingAppointment = appointmentRepository.findUpcomingAppointmentsByUsername(username, now)
                .stream()
                .findFirst()
                .map(app -> modelMapper.map(app, AppointmentResponseDto.class))
                .orElse(null);

        List<AppointmentResponseDto> recentActivity = appointmentRepository.findRecentAppointmentsByUsername(username, now)
                .stream()
                .limit(3)
                .map(app -> modelMapper.map(app, AppointmentResponseDto.class))
                .collect(Collectors.toList());

        OnlineUserDashboardDto dashboardDto = new OnlineUserDashboardDto();
        dashboardDto.setFullName(profile.getFullName());
        dashboardDto.setPatientId("P" + profile.getUserId());
        dashboardDto.setProfilePictureUrl(profile.getProfilePictureUrl());
        dashboardDto.setUpcomingAppointment(upcomingAppointment);
        dashboardDto.setRecentActivity(recentActivity);

        return dashboardDto;
    }



//
//
    private final NotificationService notificationService; // <-- INJECT THE SERVICE

    @Override
    @Transactional
    public Appointment createAppointmentForOnlineUserBA(AppointmentRequestDto dto, String username) {
        UserAccount account = userAccountRepository.findByUsername(username).orElseThrow();
        Patient patient = patientService.getOrCreatePatientForOnlineUser(account.getUserId());
        Doctor doctor = doctorRepository.findById(dto.getDoctorId()).orElseThrow();
        Branch branch = branchRepository.findById(dto.getBranchId()).orElseThrow();

        // --- CRITICAL FIX: Create the Appointment object first ---
        Appointment newAppointment = new Appointment();
        newAppointment.setPatient(patient);
        newAppointment.setDoctor(doctor);
        newAppointment.setBranch(branch);
        newAppointment.setAppointmentDate(dto.getAppointmentDate());
        newAppointment.setReason(dto.getReason());
        newAppointment.setStatus("PENDING_PAYMENT");
        newAppointment.setFee(new BigDecimal("2500.00"));

        // Now, save the object you just created
        Appointment savedAppointment = appointmentRepository.save(newAppointment);

        // Trigger the notification for the admin
        notificationService.createNewAppointmentNotificationForAdmin(savedAppointment);

        return savedAppointment;
    }


}


