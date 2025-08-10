package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service;


import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.DoctorDto;

import java.util.List;

public interface DoctorService {
    List<DoctorDto> findDoctorsByBranch(Long branchId);
    void addDoctor(DoctorDto doctorDto);


    List<DoctorDto> findActiveDoctorsByBranch(Long branchId);




    public void deactivateDoctor(int id) ;
    public void activateDoctor(int id) ;


    public List<DoctorDto> findInactiveDoctorsByBranch(Long branchId);
}