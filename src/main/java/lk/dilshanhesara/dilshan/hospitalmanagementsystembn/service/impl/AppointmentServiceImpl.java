package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.impl;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.AppointmentRequestDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.AppointmentResponseDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Appointment;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Branch;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Doctor;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Patient;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.AppointmentRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.BranchRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.DoctorRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.PatientRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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


}

