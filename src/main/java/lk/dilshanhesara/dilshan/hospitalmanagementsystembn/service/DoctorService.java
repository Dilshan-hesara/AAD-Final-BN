package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service;


import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.DoctorDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DoctorService {
    List<DoctorDto> findDoctorsByBranch(Long branchId);
    void addDoctor(DoctorDto doctorDto);


    List<DoctorDto> findActiveDoctorsByBranch(Long branchId);




    public void deactivateDoctor(int id) ;
    public void activateDoctor(int id) ;


    public List<DoctorDto> findInactiveDoctorsByBranch(Long branchId);




    public Page<DoctorDto> searchDoctors(Long branchId, String name, String specialization, Pageable pageable) ;
    public void updateDoctor(Integer doctorId, DoctorDto doctorDto) ;


    public Page<DoctorDto> searchActiveDoctorsByNameAndBranch(String name, Long branchId, Pageable pageable) ;
}