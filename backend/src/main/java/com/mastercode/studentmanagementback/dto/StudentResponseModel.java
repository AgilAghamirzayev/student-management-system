package com.mastercode.studentmanagementback.dto;

import com.mastercode.studentmanagementback.entity.Gender;
import java.io.Serial;
import java.io.Serializable;
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
public class StudentResponseModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 6529685098267757690L;

    String id;
    String name;
    String surname;
    String email;
    String group;
    String phoneNumber;
    Integer course;
    Gender gender;
}
