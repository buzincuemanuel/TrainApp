package com.buzincuemanuel.trainapp.service;

import com.buzincuemanuel.trainapp.exception.OverbookingException;
import com.buzincuemanuel.trainapp.model.*;
import com.buzincuemanuel.trainapp.repository.BookingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookingService tests")
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private BookingService bookingService;

    private Train buildTrain(int capacity) {
        return Train.builder()
                .name("IR 1234")
                .capacity(capacity)
                .build();
    }

    private TrainSchedule buildSchedule(Train train) {
        return TrainSchedule.builder()
                .train(train)
                .departureTime(LocalTime.of(8, 0))
                .build();
    }

    private User buildUser() {
        return User.builder()
                .name("Ion Popescu")
                .email("ion@email.com")
                .password("password")
                .role(Role.USER)
                .build();
    }

    private Booking buildBooking(TrainSchedule schedule, User user, int seats) {
        return Booking.builder()
                .trainSchedule(schedule)
                .user(user)
                .numberOfSeats(seats)
                .fromStation("Cluj")
                .toStation("Bucuresti")
                .build();
    }

    @Nested
    @DisplayName("When creating a booking")
    class WhenCreatingBooking {

        @Test
        @DisplayName("Should save booking when enough seats are available")
        void shouldSaveBooking_whenEnoughSeatsAvailable() {
            Train train = buildTrain(100);
            TrainSchedule schedule = buildSchedule(train);
            User user = buildUser();
            Booking booking = buildBooking(schedule, user, 2);

            when(bookingRepository.sumSeatsBookedForSchedule(schedule)).thenReturn(0);
            when(bookingRepository.save(booking)).thenReturn(booking);

            Booking result = bookingService.createBooking(booking);

            assertThat(result.getNumberOfSeats()).isEqualTo(2);
            assertThat(result.getFromStation()).isEqualTo("Cluj");
            verify(bookingRepository).save(booking);
            verify(notificationService).sendBookingConfirmation(booking);
        }

        @Test
        @DisplayName("Should throw OverbookingException when not enough seats available")
        void shouldThrowOverbookingException_whenNotEnoughSeats() {
            Train train = buildTrain(100);
            TrainSchedule schedule = buildSchedule(train);
            User user = buildUser();
            Booking booking = buildBooking(schedule, user, 10);

            when(bookingRepository.sumSeatsBookedForSchedule(schedule)).thenReturn(95);

            assertThatThrownBy(() -> bookingService.createBooking(booking))
                    .isInstanceOf(OverbookingException.class)
                    .hasMessageContaining("Not enough seats available");

            verify(bookingRepository, never()).save(any());
            verify(notificationService, never()).sendBookingConfirmation(any());
        }

        @Test
        @DisplayName("Should throw OverbookingException when train is fully booked")
        void shouldThrowOverbookingException_whenTrainFullyBooked() {
            Train train = buildTrain(100);
            TrainSchedule schedule = buildSchedule(train);
            User user = buildUser();
            Booking booking = buildBooking(schedule, user, 1);

            when(bookingRepository.sumSeatsBookedForSchedule(schedule)).thenReturn(100);

            assertThatThrownBy(() -> bookingService.createBooking(booking))
                    .isInstanceOf(OverbookingException.class);

            verify(bookingRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should send confirmation email after successful booking")
        void shouldSendConfirmationEmail_afterSuccessfulBooking() {
            Train train = buildTrain(100);
            TrainSchedule schedule = buildSchedule(train);
            User user = buildUser();
            Booking booking = buildBooking(schedule, user, 2);

            when(bookingRepository.sumSeatsBookedForSchedule(schedule)).thenReturn(0);
            when(bookingRepository.save(booking)).thenReturn(booking);

            bookingService.createBooking(booking);

            verify(notificationService).sendBookingConfirmation(booking);
        }

        @Test
        @DisplayName("Should book exactly remaining seats when train is almost full")
        void shouldBookExactlyRemainingSeats_whenAlmostFull() {
            Train train = buildTrain(100);
            TrainSchedule schedule = buildSchedule(train);
            User user = buildUser();
            Booking booking = buildBooking(schedule, user, 5);

            when(bookingRepository.sumSeatsBookedForSchedule(schedule)).thenReturn(95);
            when(bookingRepository.save(booking)).thenReturn(booking);

            Booking result = bookingService.createBooking(booking);

            assertThat(result.getNumberOfSeats()).isEqualTo(5);
            verify(bookingRepository).save(booking);
        }
    }

    @Nested
    @DisplayName("When finding bookings by user")
    class WhenFindingByUser {

        @Test
        @DisplayName("Should return all bookings for user")
        void shouldReturnAllBookings_forUser() {
            User user = buildUser();
            Train train = buildTrain(100);
            TrainSchedule schedule = buildSchedule(train);
            List<Booking> bookings = List.of(
                    buildBooking(schedule, user, 2),
                    buildBooking(schedule, user, 1)
            );

            when(bookingRepository.findByUser(user)).thenReturn(bookings);

            List<Booking> result = bookingService.findByUser(user);

            assertThat(result).hasSize(2);
            verify(bookingRepository).findByUser(user);
        }

        @Test
        @DisplayName("Should return empty list when user has no bookings")
        void shouldReturnEmptyList_whenUserHasNoBookings() {
            User user = buildUser();

            when(bookingRepository.findByUser(user)).thenReturn(List.of());

            List<Booking> result = bookingService.findByUser(user);

            assertThat(result).isEmpty();
            verify(bookingRepository).findByUser(user);
        }
    }

    @Nested
    @DisplayName("When finding bookings by train schedule")
    class WhenFindingByTrainSchedule {

        @Test
        @DisplayName("Should return all bookings for train schedule")
        void shouldReturnAllBookings_forTrainSchedule() {
            Train train = buildTrain(100);
            TrainSchedule schedule = buildSchedule(train);
            User user = buildUser();
            List<Booking> bookings = List.of(
                    buildBooking(schedule, user, 2),
                    buildBooking(schedule, user, 3)
            );

            when(bookingRepository.findByTrainSchedule(schedule)).thenReturn(bookings);

            List<Booking> result = bookingService.findByTrainSchedule(schedule);

            assertThat(result).hasSize(2);
            verify(bookingRepository).findByTrainSchedule(schedule);
        }
    }

    @Nested
    @DisplayName("When finding booking by id")
    class WhenFindingById {

        @Test
        @DisplayName("Should return booking when it exists")
        void shouldReturnBooking_whenExists() {
            Train train = buildTrain(100);
            TrainSchedule schedule = buildSchedule(train);
            User user = buildUser();
            Booking booking = buildBooking(schedule, user, 2);

            when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

            Optional<Booking> result = bookingService.findById(1L);

            assertThat(result).isPresent();
            verify(bookingRepository).findById(1L);
        }

        @Test
        @DisplayName("Should return empty when booking does not exist")
        void shouldReturnEmpty_whenNotExists() {
            when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

            Optional<Booking> result = bookingService.findById(1L);

            assertThat(result).isEmpty();
            verify(bookingRepository).findById(1L);
        }
    }

    @Nested
    @DisplayName("When cancelling a booking")
    class WhenCancellingBooking {

        @Test
        @DisplayName("Should call repository deleteById")
        void shouldCallRepositoryDelete() {
            bookingService.cancelBooking(1L);

            verify(bookingRepository).deleteById(1L);
        }
    }
}