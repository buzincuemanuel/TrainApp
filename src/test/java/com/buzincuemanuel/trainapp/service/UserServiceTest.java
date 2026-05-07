package com.buzincuemanuel.trainapp.service;

import com.buzincuemanuel.trainapp.model.Role;
import com.buzincuemanuel.trainapp.model.User;
import com.buzincuemanuel.trainapp.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Nested
    @DisplayName("When saving a user")
    class WhenSaving {

        @Test
        @DisplayName("Should return saved user with correct fields")
        void shouldReturnSavedUser() {
            User user = User.builder()
                    .name("Ion Popescu")
                    .email("ion@email.com")
                    .password("password")
                    .role(Role.USER)
                    .build();

            when(userRepository.save(user)).thenReturn(user);

            User result = userService.save(user);

            assertThat(result.getName()).isEqualTo("Ion Popescu");
            assertThat(result.getEmail()).isEqualTo("ion@email.com");
            assertThat(result.getRole()).isEqualTo(Role.USER);
            verify(userRepository).save(user);
        }
    }

    @Nested
    @DisplayName("When finding all users")
    class WhenFindingAll {

        @Test
        @DisplayName("Should return all users")
        void shouldReturnAllUsers() {
            List<User> users = List.of(
                    User.builder().name("Ion Popescu").email("ion@email.com").role(Role.USER).build(),
                    User.builder().name("Admin").email("admin@email.com").role(Role.ADMIN).build()
            );

            when(userRepository.findAll()).thenReturn(users);

            List<User> result = userService.findAll();

            assertThat(result).hasSize(2);
            assertThat(result.get(0).getName()).isEqualTo("Ion Popescu");
            verify(userRepository).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no users exist")
        void shouldReturnEmptyList_whenNoUsersExist() {
            when(userRepository.findAll()).thenReturn(List.of());

            List<User> result = userService.findAll();

            assertThat(result).isEmpty();
            verify(userRepository).findAll();
        }
    }

    @Nested
    @DisplayName("When finding user by id")
    class WhenFindingById {

        @Test
        @DisplayName("Should return user when it exists")
        void shouldReturnUser_whenExists() {
            User user = User.builder()
                    .name("Ion Popescu")
                    .email("ion@email.com")
                    .role(Role.USER)
                    .build();

            when(userRepository.findById(1L)).thenReturn(Optional.of(user));

            Optional<User> result = userService.findById(1L);

            assertThat(result).isPresent();
            assertThat(result.get().getName()).isEqualTo("Ion Popescu");
            verify(userRepository).findById(1L);
        }

        @Test
        @DisplayName("Should return empty when user does not exist")
        void shouldReturnEmpty_whenNotExists() {
            when(userRepository.findById(1L)).thenReturn(Optional.empty());

            Optional<User> result = userService.findById(1L);

            assertThat(result).isEmpty();
            verify(userRepository).findById(1L);
        }
    }

    @Nested
    @DisplayName("When finding user by email")
    class WhenFindingByEmail {

        @Test
        @DisplayName("Should return user when email exists")
        void shouldReturnUser_whenEmailExists() {
            User user = User.builder()
                    .name("Ion Popescu")
                    .email("ion@email.com")
                    .role(Role.USER)
                    .build();

            when(userRepository.findByEmail("ion@email.com")).thenReturn(Optional.of(user));

            Optional<User> result = userService.findByEmail("ion@email.com");

            assertThat(result).isPresent();
            assertThat(result.get().getEmail()).isEqualTo("ion@email.com");
            verify(userRepository).findByEmail("ion@email.com");
        }

        @Test
        @DisplayName("Should return empty when email does not exist")
        void shouldReturnEmpty_whenEmailNotExists() {
            when(userRepository.findByEmail("ion@email.com")).thenReturn(Optional.empty());

            Optional<User> result = userService.findByEmail("ion@email.com");

            assertThat(result).isEmpty();
            verify(userRepository).findByEmail("ion@email.com");
        }
    }

    @Nested
    @DisplayName("When deleting a user")
    class WhenDeleting {

        @Test
        @DisplayName("Should call repository deleteById")
        void shouldCallRepositoryDelete() {
            userService.deleteById(1L);

            verify(userRepository).deleteById(1L);
        }
    }

    @Nested
    @DisplayName("When updating a user")
    class WhenUpdating {

        @Test
        @DisplayName("Should set id and return updated user")
        void shouldSetIdAndReturnUpdatedUser() {
            User user = User.builder()
                    .name("Ion Popescu")
                    .email("ion@email.com")
                    .role(Role.USER)
                    .build();

            when(userRepository.save(user)).thenReturn(user);

            User result = userService.update(1L, user);

            assertThat(result.getName()).isEqualTo("Ion Popescu");
            verify(userRepository).save(user);
        }
    }
}