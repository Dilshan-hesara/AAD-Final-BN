package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.impl;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Appointment;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your HMS Password Reset OTP");
        message.setText("Your OTP code is: " + otp + ". It will expire in 5 minutes.");
        mailSender.send(message);
    }



    public void sendAppointmentReminderEmail(String toEmail, Appointment appointment) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Upcoming Appointment Reminder - HMS");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("eeee, MMMM d 'at' h:mm a");
            String formattedDate = appointment.getAppointmentDate().format(formatter);

            String text = String.format(
                    "Dear %s,\n\n" +
                            "This is a friendly reminder for your upcoming appointment.\n\n" +
                            "Doctor: %s\n" +
                            "Branch: %s\n" +
                            "Date & Time: %s\n\n" +
                            "We look forward to seeing you.\n\n" +
                            "Sincerely,\nThe HMS Team",
                    appointment.getPatient().getFullName(),
                    appointment.getDoctor().getFullName(),
                    appointment.getBranch().getName(),
                    formattedDate
            );

            message.setText(text);
            mailSender.send(message);
            System.out.println("EMAIL SENT: Reminder sent to " + toEmail);
        } catch (Exception e) {
            System.err.println("EMAIL FAILED: Could not send reminder to " + toEmail + ". Error: " + e.getMessage());
        }
    }
}