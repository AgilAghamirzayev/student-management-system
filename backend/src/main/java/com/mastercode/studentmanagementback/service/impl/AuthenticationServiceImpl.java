package com.mastercode.studentmanagementback.service.impl;


import com.mastercode.studentmanagementback.dto.AuthenticationRequest;
import com.mastercode.studentmanagementback.dto.AuthenticationResponse;
import com.mastercode.studentmanagementback.entity.Student;
import com.mastercode.studentmanagementback.mapper.StudentMapper;
import com.mastercode.studentmanagementback.security.jwt.JWTUtil;
import com.mastercode.studentmanagementback.service.AuthenticationService;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthenticationServiceImpl implements AuthenticationService {

    StudentMapper studentMapper = StudentMapper.INSTANCE;
    AuthenticationManager authenticationManager;
    JWTUtil jwtUtil;

    public AuthenticationResponse login(AuthenticationRequest request) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var principal = (Student) authentication.getPrincipal();
        var studentDto = studentMapper.mapToStudentDto(principal);

        var roles = principal.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

        studentDto.setRoles(roles);

        String token = jwtUtil.issueToken(studentDto.getUsername(), studentDto.getRoles());
        return new AuthenticationResponse(token, studentDto);
    }

}
