package com.mastercode.studentmanagementback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class StudentManagementBackApplication {

  public static void main(String[] args) {
    SpringApplication.run(StudentManagementBackApplication.class, args);
  }

}
