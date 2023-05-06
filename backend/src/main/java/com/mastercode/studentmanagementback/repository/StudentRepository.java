package com.mastercode.studentmanagementback.repository;

import com.mastercode.studentmanagementback.entity.Student;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {

  boolean existsStudentByEmail(String email);

  boolean existsStudentById(String id);

  Optional<Student> findStudentByEmail(String email);

}
