package com.userservice.service;

import com.userservice.dto.MessageResponseDto;
import com.userservice.dto.UserProfileRequestDto;
import com.userservice.entity.User;
import com.userservice.entity.UserProfile;
import com.userservice.jwt.UserDetailsImpl;
import com.userservice.repositories.UserProfileRepository;
import com.userservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;

@Service
public class    UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository  userProfileRepository;

    public ResponseEntity<?> getCurrentUser(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails=(UserDetailsImpl) authentication.getPrincipal();

        User user=userRepository.findById(userDetails.getId())
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));

        return ResponseEntity.ok(user);
    }

    public ResponseEntity<?> getUserById(String id){
        User user=userRepository.findById(id)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return ResponseEntity.ok(user);
    }

    public ResponseEntity<?> getUserProfile(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails=(UserDetailsImpl) authentication.getPrincipal();

        User user=userRepository.findById(userDetails.getId())
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        UserProfile profile =userProfileRepository.findByUser(user)
                .orElse(null);
        return ResponseEntity.ok(profile);
    }

    public ResponseEntity<?> createdOrUpdatedUserProfile(UserProfileRequestDto userProfileRequestDto){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl  userDetails=(UserDetailsImpl) authentication.getPrincipal();

        User user=userRepository.findById(userDetails.getId())
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        UserProfile profile=userProfileRepository.findByUser(user)
                .orElse(new UserProfile());

        profile.setUser(user);
        profile.setFirstName(userProfileRequestDto.getFirstName());
        profile.setLastName(userProfileRequestDto.getLastName());
        profile.setPhoneNumber(userProfileRequestDto.getPhoneNumber());
        profile.setDateOfBirth(LocalDate.parse(userProfileRequestDto.getDateOfBirth()));
        profile.setAddress(userProfileRequestDto.getAddress());

        userProfileRepository.save(profile);
        return ResponseEntity.ok(new MessageResponseDto("User profile created or updated successfully."));

    }


}

