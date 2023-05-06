package com.mastercode.studentmanagementback.controller;

import com.mastercode.studentmanagementback.dto.StudentRequestModel;
import com.mastercode.studentmanagementback.dto.StudentResponseModel;
import com.mastercode.studentmanagementback.security.jwt.JWTUtil;
import com.mastercode.studentmanagementback.service.StudentService;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/students")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class StudentController {

  StudentService studentService;
  JWTUtil jwtUtil;

  @PostMapping
  ResponseEntity<Void> registerCustomer(@RequestBody StudentRequestModel student) {
    studentService.addStudent(student);
    String jwtToken = jwtUtil.issueToken(student.getEmail(), "ROLE_USER");
    return ResponseEntity.ok()
        .header(HttpHeaders.AUTHORIZATION, jwtToken)
        .build();
  }

  @GetMapping
  ResponseEntity<List<StudentResponseModel>> getStudents() {
    return ResponseEntity.ok(studentService.getStudents());
  }

  @GetMapping("/{id}")
  ResponseEntity<StudentResponseModel> getStudentById(@PathVariable String id) {
    return ResponseEntity.ok(studentService.getStudentById(id));
  }

  @PutMapping("/{id}")
  ResponseEntity<Void> updateStudent(@PathVariable String id,
      @RequestBody StudentRequestModel student) {
    studentService.updateStudent(id, student);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{id}")
  ResponseEntity<Void> deleteStudent(@PathVariable String id) {
    studentService.deleteStudentById(id);
    return ResponseEntity.ok().build();
  }

  @PostMapping(
      value = "{id}/profile-image",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  public void uploadCustomerProfileImage(@PathVariable("id") String id,
      @RequestParam("file") MultipartFile file) {
    System.out.println("Test---");
    studentService.uploadStudentProfileImage(id, file);
  }

  @GetMapping(
      value = "{id}/profile-image",
      produces = MediaType.IMAGE_JPEG_VALUE
  )
  public byte[] getCustomerProfileImage(@PathVariable("id") String id) {
    return studentService.getStudentProfileImage(id);
  }

}
