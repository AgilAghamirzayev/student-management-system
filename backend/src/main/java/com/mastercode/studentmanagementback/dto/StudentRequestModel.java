package com.mastercode.studentmanagementback.dto;

import com.mastercode.studentmanagementback.entity.Gender;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PACKAGE)
public class StudentRequestModel {
    String name;
    String surname;
    String email;
    String password;
    String group;
    Integer course;
    Gender gender;
}
