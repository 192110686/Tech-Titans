package org.ust.project.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ust.project.dto.UserRequestDTO;
import org.ust.project.dto.UserResponseDTO;
import org.ust.project.model.User;
import org.ust.project.repo.UserRepository;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;


    /* ================= CREATE USER ================= */
    public UserResponseDTO createUser(UserRequestDTO dto) {

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());

        // doctor & patient are NULL here
        User saved = userRepository.save(user);

        return new UserResponseDTO(
                saved.getId(),
                saved.getUsername(),
                saved.getRole()
        );
    }


    /* ================= GET USER BY ID ================= */
    public UserResponseDTO getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return toResponseDTO(user);
    }

    /* ================= GET ALL USERS ================= */
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /* ================= DELETE USER ================= */
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Optional cleanup
        if (user.getPatient() != null) {
            user.getPatient().setUser(null);
        }
        if (user.getDoctor() != null) {
            user.getDoctor().setUser(null);
        }

        userRepository.delete(user);
    }

    /* ================= DTO MAPPER ================= */
    private UserResponseDTO toResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
    }
}
