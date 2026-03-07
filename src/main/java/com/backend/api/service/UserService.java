package com.backend.api.service;

import com.backend.api.common.response.ApiResponse;
import com.backend.api.dto.requestDto.UserRequestDto;
import com.backend.api.dto.responseDto.UserResponseDto;
import com.backend.api.entity.Role;
import com.backend.api.entity.User;
import com.backend.api.repository.RoleRepository;
import com.backend.api.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;



    public ApiResponse createUser(UserRequestDto request) {

        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() -> new EntityNotFoundException("role not found."));

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);

        userRepository.save(user);

        UserResponseDto dto = modelMapper.map(user, UserResponseDto.class);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(Map.of("currentUser", dto))
                .message("User account created Successfully.")
                .build();
    }

    public List<UserResponseDto> getUsersByRole(Long roleId) {

        roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Role not found."));

        return userRepository.findByRole_Id(roleId)
                .stream()
                .map(user -> modelMapper.map(user, UserResponseDto.class))
                .toList();
    }

    public ApiResponse deleteUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));

        userRepository.deleteById(userId);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("User deleted successfully.")
                .build();
    }

    public ApiResponse updateUser(Long id, UserRequestDto request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));

        user.setEmail(request.getEmail());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(request.getPassword());
        }

        if (request.getRole() != null) {
            Role role = roleRepository.findByName(request.getRole())
                    .orElseThrow(() -> new EntityNotFoundException("Role not found."));
            user.setRole(role);
        }

        User updatedUser = userRepository.save(user);

        UserResponseDto dto = modelMapper.map(updatedUser, UserResponseDto.class);

        return ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(Map.of("updatedUser", dto))
                .message("User updated successfully.")
                .build();
    }

}
