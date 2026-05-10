package com.buzincuemanuel.trainapp.service;

import com.buzincuemanuel.trainapp.exception.OverbookingException;
import com.buzincuemanuel.trainapp.model.Booking;
import com.buzincuemanuel.trainapp.model.TrainSchedule;
import com.buzincuemanuel.trainapp.model.User;
import com.buzincuemanuel.trainapp.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final NotificationService notificationService;

    @Transactional
    public Booking createBooking(Booking booking) {
        int seatsBooked = bookingRepository.sumSeatsBookedForSchedule(booking.getTrainSchedule());
        int capacity = booking.getTrainSchedule().getTrain().getCapacity();
        int seatsRequested = booking.getNumberOfSeats();

        if (seatsBooked + seatsRequested > capacity) {
            throw new OverbookingException(
                    "Not enough seats available. Requested: " + seatsRequested +
                            ", Available: " + (capacity - seatsBooked)
            );
        }

        Booking savedBooking = bookingRepository.save(booking);
        notificationService.sendBookingConfirmation(savedBooking);
        return savedBooking;
    }

    public List<Booking> findByUser(User user) {
        return bookingRepository.findByUser(user);
    }

    public List<Booking> findByTrainSchedule(TrainSchedule trainSchedule) {
        return bookingRepository.findByTrainSchedule(trainSchedule);
    }

    public Optional<Booking> findById(Long id) {
        return bookingRepository.findById(id);
    }

    public void cancelBooking(Long id) {
        bookingRepository.deleteById(id);
    }
}