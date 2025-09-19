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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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



    @Override
    public List<DoctorDto> findActiveDoctorsByBranch(Long branchId) {
        List<Doctor> activeDoctors = doctorRepository.findByBranch_IdAndStatus(branchId, "ACTIVE");

        return modelMapper.map(activeDoctors, new TypeToken<List<DoctorDto>>() {}.getType());
    }



@Override
    public void deactivateDoctor(int id) {
        if (!doctorRepository.existsById(id)) { throw new RuntimeException("Doctor not found"); }
        doctorRepository.updateDoctorStatusToInactive(id);
    }

    @Override
    public void activateDoctor(int id) {
        if (!doctorRepository.existsById(id)) { throw new RuntimeException("Doctor not found"); }
        doctorRepository.updateDoctorStatusToActive(id);
    }


    @Override
    public List<DoctorDto> findInactiveDoctorsByBranch(Long branchId) {
        List<Doctor> inactiveDoctors = doctorRepository.findByBranch_IdAndStatus(branchId, "INACTIVE");
        return modelMapper.map(inactiveDoctors, new TypeToken<List<DoctorDto>>() {}.getType());
    }



//    serch


    public Page<DoctorDto> searchDoctors(Long branchId, String name, String specialization, Pageable pageable) {
        Specification<Doctor> spec = Specification.where((root, query, cb) -> cb.equal(root.get("branch").get("id"), branchId));

        if (name != null && !name.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.like(root.get("fullName"), "%" + name + "%"));
        }
        if (specialization != null && !specialization.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.like(root.get("specialization"), "%" + specialization + "%"));
        }

        Page<Doctor> doctors = doctorRepository.findAll(spec, pageable);
        return doctors.map(doctor -> modelMapper.map(doctor, DoctorDto.class));
    }

    @Override
    public void updateDoctor(Integer doctorId, DoctorDto doctorDto) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        doctor.setFullName(doctorDto.getFullName());
        doctor.setEmail(doctorDto.getEmail());
        doctor.setContactNumber(doctorDto.getContactNumber());
        doctor.setSpecialization(doctorDto.getSpecialization());

        doctorRepository.save(doctor);
    }


    public Page<DoctorDto> searchActiveDoctorsByNameAndBranch(String name, Long branchId, Pageable pageable) {
        Page<Doctor> doctors = doctorRepository.findByBranch_IdAndStatusAndFullNameContainingIgnoreCase(branchId, "ACTIVE", name, pageable);
        return doctors.map(doctor -> modelMapper.map(doctor, DoctorDto.class));
    }





    public Page<DoctorDto> searchAllDoctors(String keyword, Long branchId, Pageable pageable) {
        // Specification to build a dynamic query
        Specification<Doctor> spec = Specification.where(null);

        if (keyword != null && !keyword.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.or(
                            cb.like(root.get("fullName"), "%" + keyword + "%"),
                            cb.like(root.get("specialization"), "%" + keyword + "%"),
                            cb.like(root.get("contactNumber"), "%" + keyword + "%")
                    )
            );
        }

        if (branchId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("branch").get("id"), branchId));
        }

        Page<Doctor> doctors = doctorRepository.findAll(spec, pageable);
        return doctors.map(doctor -> {
            DoctorDto dto = modelMapper.map(doctor, DoctorDto.class);
            if (doctor.getBranch() != null) {
                dto.setBranchName(doctor.getBranch().getName());
            }
            return dto;
        });
    }



    @Override
    public DoctorDto findDoctorById(Integer id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));

        DoctorDto dto = modelMapper.map(doctor, DoctorDto.class);
        if (doctor.getBranch() != null) {
            dto.setBranchName(doctor.getBranch().getName());
        }
        return dto;
    }
}
