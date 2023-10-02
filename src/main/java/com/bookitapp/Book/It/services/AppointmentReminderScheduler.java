package com.bookitapp.Book.It.services;

import com.bookitapp.Book.It.models.Appointment;
import com.bookitapp.Book.It.models.EmailDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import java.util.List;

@Service
public class AppointmentReminderScheduler {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private EmailServiceImpl emailService;

    @Scheduled(cron = "0 0 14 * * ?")
    public void sendAppointmentReminders() {
        List<Appointment> upcomingAppointments = appointmentService.getUpocmingAppointments();
        for (Appointment appointment : upcomingAppointments) {
            EmailDetails details = new EmailDetails();
            details.setRecipient(appointment.getUser().getEmail());
            details.setUserFirstName((appointment.getUser().getFirstName()));
            details.setGroomerName(appointment.getGroomer().getName());
            details.setAppointmentTime(appointment.getAppointmentTime());
            details.setDogName(appointment.getDog().getName());
            details.setSubject("Appointment Reminder");
            Context context = new Context();
            context.setVariable("userId", details.getUserId());
            context.setVariable("securityToken", details.getSecurityToken());
            context.setVariable("groomerName", details.getGroomerName());
            context.setVariable("dogName", details.getDogName());
            context.setVariable("userFirstName", details.getUserFirstName());
            context.setVariable("appointmentTime", details.getAppointmentTime());
            emailService.sendEmailWithHtmlTemplate(details, "email/appointment-reminder-email", context);
        }
    }

}
