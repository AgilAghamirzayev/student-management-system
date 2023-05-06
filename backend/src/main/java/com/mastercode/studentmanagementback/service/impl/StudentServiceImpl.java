package com.mastercode.studentmanagementback.service.impl;

import com.mastercode.studentmanagementback.dto.StudentRequestModel;
import com.mastercode.studentmanagementback.dto.StudentResponseModel;
import com.mastercode.studentmanagementback.entity.Student;
import com.mastercode.studentmanagementback.exception.DuplicateResourceException;
import com.mastercode.studentmanagementback.exception.RequestValidationException;
import com.mastercode.studentmanagementback.exception.ResourceNotFoundException;
import com.mastercode.studentmanagementback.mapper.StudentMapper;
import com.mastercode.studentmanagementback.repository.StudentRepository;
import com.mastercode.studentmanagementback.s3.S3Buckets;
import com.mastercode.studentmanagementback.s3.S3Service;
import com.mastercode.studentmanagementback.service.StudentService;
import io.micrometer.common.util.StringUtils;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class StudentServiceImpl implements StudentService {

  StudentMapper studentMapper = StudentMapper.INSTANCE;
  PasswordEncoder passwordEncoder;
  S3Service s3Service;
  S3Buckets s3Buckets;
  StudentRepository studentRepository;
  MongoTemplate mongoTemplate;
  CacheManager cacheManager;


  @Override
  public void addStudent(StudentRequestModel studentRequest) {
    String email = studentRequest.getEmail();
    if (studentRepository.existsStudentByEmail(email)) {
      throw new DuplicateResourceException("email already taken");
    }

    Student student = studentMapper.mapToStudent(studentRequest);
    student.setPassword(passwordEncoder.encode(studentRequest.getPassword()));
    log.info("Mapped to Student");
    studentRepository.save(student);
    Objects.requireNonNull(cacheManager.getCache("response")).clear();

    log.info("Saved student! ");
  }

  @Override
  public StudentResponseModel getStudentById(String id) {
    Student student = studentRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Student not found by id: " + id));

    StudentResponseModel response = studentMapper.mapToStudentResponseModel(student);
    log.info("Mapped to StudentResponseModel {} ", response);

    return response;
  }

  @Override
  @Cacheable(value = "response")
  public List<StudentResponseModel> getStudents() {
    List<Student> students = studentRepository.findAll();

    List<StudentResponseModel> response = studentMapper.mapToStudentResponseModels(students);
    log.info("Mapped to StudentResponseModels {} ", response.size());

    return response;
  }

  @Override
  public void updateStudent(String id, StudentRequestModel studentRequest) {
    Student student = studentRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Student not found by id: " + id));

    boolean changes = false;

    changes = updateIfNotNullAndNotSame(studentRequest, student, changes);

    if (!changes) {
      throw new RequestValidationException("no data changes found");
    }

    studentRepository.save(student);
    Objects.requireNonNull(cacheManager.getCache("response")).clear();
    log.info("Updated student! ");
  }

  private static boolean updateIfNotNullAndNotSame(StudentRequestModel studentRequest,
      Student student,
      boolean changes) {
    if (studentRequest.getName() != null && !studentRequest.getName().equals(student.getName())) {
      student.setName(studentRequest.getName());
      changes = true;
    }

    if (studentRequest.getSurname() != null && !studentRequest.getSurname()
        .equals(student.getSurname())) {
      student.setSurname(studentRequest.getSurname());
      changes = true;
    }

    if (studentRequest.getGroup() != null && !studentRequest.getGroup()
        .equals(student.getGroup())) {
      student.setGroup(studentRequest.getGroup());
      changes = true;
    }

    if (studentRequest.getEmail() != null && !studentRequest.getEmail()
        .equals(student.getEmail())) {
      student.setEmail(studentRequest.getEmail());
      changes = true;
    }

    if (studentRequest.getCourse() != null && !studentRequest.getCourse()
        .equals(student.getCourse())) {
      student.setCourse(student.getCourse());
      changes = true;
    }
    return changes;
  }

  @Override
  public void deleteStudentById(String id) {
    studentRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Student not found by id: " + id));

    studentRepository.deleteById(id);
    Objects.requireNonNull(cacheManager.getCache("response")).clear();
    log.info("Deleted student! ");
  }

  @Override
  public void uploadStudentProfileImage(String id, MultipartFile file) {
    studentRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Student not found by id: " + id));
    String imageId = UUID.randomUUID().toString();

    try {
      s3Service.putObject(
          s3Buckets.getStudent(),
          "profile-images/%s/%s".formatted(id, imageId),
          file.getBytes()
      );
    } catch (IOException e) {
      throw new RuntimeException("failed to upload profile image", e);
    }

    Query query = new Query(Criteria.where("id").is(id));
    Update update = new Update().set("profileImageId", imageId);
    mongoTemplate.updateFirst(query, update, Student.class);
    log.info("Image successfully uploaded");
  }

  @Override
  public byte[] getStudentProfileImage(String id) {
    Student student = studentRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Student not found by id: " + id));

    if (StringUtils.isBlank(student.getProfileImageId())) {
      throw new ResourceNotFoundException(
          "customer with id [%s] profile image not found".formatted(id));
    }

    return s3Service.getObject(
        s3Buckets.getStudent(),
        "profile-images/%s/%s".formatted(id, student.getProfileImageId())
    );
  }
}
