package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.impl;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.AppointmentRequestDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.AppointmentResponseDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.*;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.*;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.AppointmentService;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


}

