package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.impl;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.AppointmentResponseDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.OnlineUserDashboardDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.OnlineUserProfile;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.AppointmentRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.OnlineUserProfileRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.OnlineUserDashboardService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OnlineUserDashboardServiceImpl implements OnlineUserDashboardService {

    private final OnlineUserProfileRepository profileRepository;
    private final AppointmentRepository appointmentRepository;
    private final ModelMapper modelMapper;

    @Override
    public OnlineUserDashboardDto getDashboardData(String username) {
        OnlineUserProfile profile = profileRepository.findByUserAccount_Username(username)
                .orElseThrow(() -> new RuntimeException("Profile not found for username: " + username));

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
}