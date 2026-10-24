package com.userservice.service;

import com.userservice.dto.JwtResponseDto;
import com.userservice.dto.LoginRequestDto;
import com.userservice.dto.MessageResponseDto;
import com.userservice.dto.SignupRequestDto;
import com.userservice.entity.ERole;
import com.userservice.entity.Role;
import com.userservice.entity.User;
import com.userservice.jwt.JwtUtils;
import com.userservice.jwt.UserDetailsImpl;
import com.userservice.repositories.RoleRepository;
import com.userservice.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AuthServices {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;


    //Login User with username and password
    public ResponseEntity<?> authenticateUser(LoginRequestDto loginRequest) {
        Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt=jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl  userDetails= (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return ResponseEntity.ok(new JwtResponseDto(jwt,
                "Bearer",
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles)) ;
    }

    //Register new user
    public ResponseEntity<?> registerUser(SignupRequestDto signupRequestDto){
        if(userRepository.existsByUsername(signupRequestDto.getUsername())){
            return ResponseEntity
                    .badRequest().body("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(signupRequestDto.getEmail())){
            return ResponseEntity
                    .badRequest().body("Error: Email is already in use!");
        }

        User  user=new User(signupRequestDto.getUsername(),
                signupRequestDto.getEmail(),
                encoder.encode(signupRequestDto.getPassword()));

        Set<String> strRoles=signupRequestDto.getRole();
        Set<Role> roles=new HashSet<>();

        if(strRoles==null){
            Role userRole=roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(()->new RuntimeException("Error: Role is not found"));
            roles.add(userRole);
        }else {
            strRoles.forEach(role->{
                switch(role){
                    case "ROLE_ADMIN":
                        Role adminRole=roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(()->new RuntimeException("Error: Role is not found"));
                        roles.add(adminRole);
                        break;
                    default:
                        Role userRole=roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(()->new RuntimeException("Error: Role is not found"));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponseDto("User Registered successfully"));
    }
}
