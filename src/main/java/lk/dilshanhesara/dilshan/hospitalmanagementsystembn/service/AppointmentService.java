package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.AppointmentRequestDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.AppointmentResponseDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.BranchDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {


    public List<AppointmentResponseDto> findAppointmentsByBranch(Long branchId);

    public void createAppointment(AppointmentRequestDto dto, Long branchId);


    public void updateAppointmentStatus(Long appointmentId, String newStatus);


    List<AppointmentResponseDto> findAppointmentsByUsername(String username);


    public Appointment createAppointmentForOnlineUser(AppointmentRequestDto dto, String username);


    public void confirmAppointmentPayment(Long appointmentId);

    public List<AppointmentResponseDto> findOnlineUserAppointmentsForToday(Long branchId);

    public Page<AppointmentResponseDto> searchAppointments(Long branchId, String patientName, String status, LocalDate date, Pageable pageable);


    public Appointment createAppointmentForWalk(AppointmentRequestDto dto, String username) ;


    public Appointment createAppointmentByStaff(AppointmentRequestDto dto, Long branchId) ;


    public AppointmentResponseDto findAppointmentById(Long appointmentId) ;


    public List<BranchDto> getAllBranches() ;
    }