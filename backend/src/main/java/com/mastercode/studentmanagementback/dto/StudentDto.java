package com.mastercode.studentmanagementback.dto;

import com.mastercode.studentmanagementback.entity.Gender;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentDto{
  String id;
  String name;
  String email;
  Gender gender;
  Integer course;
  List<String> roles;
  String username;
  String profileImageId;
}
