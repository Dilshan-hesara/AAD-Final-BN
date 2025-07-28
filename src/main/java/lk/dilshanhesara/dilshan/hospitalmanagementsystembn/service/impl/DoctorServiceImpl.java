package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.impl;


import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.DoctorDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Branch;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Doctor;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.BranchRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.DoctorRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final BranchRepository branchRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<DoctorDto> findDoctorsByBranch(Long branchId) {
        List<Doctor> doctors = doctorRepository.findByBranch_Id(branchId);
        return modelMapper.map(doctors, new TypeToken<List<DoctorDto>>() {}.getType());
    }

    @Override
    public void addDoctor(DoctorDto doctorDto) {
        Branch branch = branchRepository.findById(doctorDto.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found"));

        Doctor doctor = modelMapper.map(doctorDto, Doctor.class);
        doctor.setBranch(branch);
        doctor.setStatus("ACTIVE");
        doctorRepository.save(doctor);
    }
}