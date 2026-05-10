package com.buzincuemanuel.trainapp.controller;

import com.buzincuemanuel.trainapp.exception.OverbookingException;
import com.buzincuemanuel.trainapp.model.Booking;
import com.buzincuemanuel.trainapp.model.TrainSchedule;
import com.buzincuemanuel.trainapp.model.User;
import com.buzincuemanuel.trainapp.service.BookingService;
import com.buzincuemanuel.trainapp.service.TrainScheduleService;
import com.buzincuemanuel.trainapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class BookingController {

    private final TrainScheduleService trainScheduleService;
    private final BookingService bookingService;
    private final UserService userService;

    @GetMapping("/my-bookings")
    public String myBookings(Principal principal, Model model) {
        User user = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        model.addAttribute("bookings", bookingService.findByUser(user));
        return "my-bookings";
    }

    @GetMapping("/book/{scheduleId}")
    public String showBookingPage(@PathVariable Long scheduleId,
                                  @RequestParam String from,
                                  @RequestParam String to,
                                  Model model) {

        TrainSchedule schedule = trainScheduleService.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid schedule Id: " + scheduleId));

        model.addAttribute("schedule", schedule);
        model.addAttribute("from", from);
        model.addAttribute("to", to);
        return "book";
    }

    @PostMapping("/book/{scheduleId}")
    public String processBooking(@PathVariable Long scheduleId,
                                 @RequestParam String from,
                                 @RequestParam String to,
                                 @RequestParam Integer numberOfSeats,
                                 Principal principal,
                                 Model model) {

        TrainSchedule schedule = trainScheduleService.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid schedule Id: " + scheduleId));

        User user = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Booking booking = Booking.builder()
                .trainSchedule(schedule)
                .user(user)
                .fromStation(from)
                .toStation(to)
                .numberOfSeats(numberOfSeats)
                .build();

        try {
            bookingService.createBooking(booking);
            return "redirect:/?bookingSuccess=true";
        } catch (OverbookingException e) {
            model.addAttribute("schedule", schedule);
            model.addAttribute("from", from);
            model.addAttribute("to", to);
            model.addAttribute("error", e.getMessage());
            return "book";
        }
    }

    @PostMapping("/my-bookings/cancel/{id}")
    public String cancelBooking(@PathVariable Long id, Principal principal) {
        Booking booking = bookingService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (!booking.getUser().getEmail().equals(principal.getName())) {
            return "redirect:/my-bookings?error=unauthorized";
        }

        bookingService.cancelBooking(id);
        return "redirect:/my-bookings?cancelled=true";
    }
}