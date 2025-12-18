package org.ust.project.service;  // Corrected package name

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ust.project.dto.UserRequestDTO;
import org.ust.project.dto.UserResponseDTO;
import org.ust.project.model.User;
import org.ust.project.repo.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Create a new user
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        User user = new User();
        user.setUsername(userRequestDTO.getUsername());
        user.setPassword(userRequestDTO.getPassword());
        user.setRole(userRequestDTO.getRole());

        // Save the user to the database
        user = userRepository.save(user);

        return new UserResponseDTO(
            user.getId(),
            user.getUsername(),
            user.getRole()
        );
    }

    // Get user by ID
    public UserResponseDTO getUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getRole()
            );
        }
        return null; // Handle case where user is not found (could throw an exception)
    }

    // Get all users
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
            .map(user -> new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getRole()
            ))
            .collect(Collectors.toList());
    }

    // Update user details
    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUsername(userRequestDTO.getUsername());
            user.setPassword(userRequestDTO.getPassword());
            user.setRole(userRequestDTO.getRole());

            user = userRepository.save(user);

            return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getRole()
            );
        }
        return null; // Handle case where user is not found (could throw an exception)
    }

    // Delete a user
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false; // Handle case where user is not found
    }
}
