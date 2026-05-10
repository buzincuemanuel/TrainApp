package com.buzincuemanuel.trainapp.service;

import com.buzincuemanuel.trainapp.model.Booking;
import com.buzincuemanuel.trainapp.model.TrainSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;

    public void sendBookingConfirmation(Booking booking) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(booking.getUser().getEmail());
            message.setSubject("Booking Confirmation - " + booking.getTrainSchedule().getTrain().getName());
            message.setText(
                    "Dear " + booking.getUser().getName() + ",\n\n" +
                            "Your booking has been confirmed.\n" +
                            "Train: " + booking.getTrainSchedule().getTrain().getName() + "\n" +
                            "From: " + booking.getFromStation() + "\n" +
                            "To: " + booking.getToStation() + "\n" +
                            "Seats booked: " + booking.getNumberOfSeats() + "\n" +
                            "Departure time: " + booking.getTrainSchedule().getDepartureTime() + "\n\n" +
                            "Thank you for choosing our service!"
            );
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Could not send email: " + e.getMessage());
        }
    }

    public void sendDelayNotification(TrainSchedule trainSchedule, Integer delayMinutes) {
        for (Booking booking : trainSchedule.getBookings()) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(booking.getUser().getEmail());
            message.setSubject("Delay Notification - " + trainSchedule.getTrain().getName());
            message.setText(
                    "Dear " + booking.getUser().getName() + ",\n\n" +
                            "We would like to inform you that train " + trainSchedule.getTrain().getName() +
                            " has been delayed by " + delayMinutes + " minutes.\n" +
                            "We apologize for the inconvenience.\n\n" +
                            "Thank you for your understanding."
            );
            mailSender.send(message);
        }
    }
}