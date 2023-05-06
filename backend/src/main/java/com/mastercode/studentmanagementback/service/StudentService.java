package com.mastercode.studentmanagementback.service;

import com.mastercode.studentmanagementback.dto.StudentRequestModel;
import com.mastercode.studentmanagementback.dto.StudentResponseModel;
import com.mastercode.studentmanagementback.entity.Student;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface StudentService {

  void addStudent(StudentRequestModel student);

  StudentResponseModel getStudentById(String id);

  List<StudentResponseModel> getStudents();

  void updateStudent(String id, StudentRequestModel student);

  void deleteStudentById(String id);

  void uploadStudentProfileImage(String id, MultipartFile file);

  byte[] getStudentProfileImage(String id);
}
