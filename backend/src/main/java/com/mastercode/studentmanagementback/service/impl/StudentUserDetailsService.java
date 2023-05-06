package com.mastercode.studentmanagementback.service.impl;

import com.mastercode.studentmanagementback.repository.StudentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class StudentUserDetailsService implements UserDetailsService {

  StudentRepository studentRepository;

  @Override
  public UserDetails loadUserByUsername(String username)
      throws UsernameNotFoundException {
    return studentRepository.findStudentByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found"));
  }
}
