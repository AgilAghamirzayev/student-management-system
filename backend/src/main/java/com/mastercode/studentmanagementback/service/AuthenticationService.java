package com.mastercode.studentmanagementback.service;

import com.mastercode.studentmanagementback.dto.AuthenticationRequest;
import com.mastercode.studentmanagementback.dto.AuthenticationResponse;

public interface AuthenticationService {
   AuthenticationResponse login(AuthenticationRequest request);
}
