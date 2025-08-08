package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.AppointmentRequestDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.AppointmentResponseDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Appointment;

import java.util.List;

public interface AppointmentService {


    public List<AppointmentResponseDto> findAppointmentsByBranch(Long branchId);

    public void createAppointment(AppointmentRequestDto dto, Long branchId);



    public void updateAppointmentStatus(Long appointmentId, String newStatus) ;



    List<AppointmentResponseDto> findAppointmentsByUsername(String username);


    public Appointment createAppointmentForOnlineUser(AppointmentRequestDto dto, String username) ;


    public void confirmAppointmentPayment(Long appointmentId) ;
    }