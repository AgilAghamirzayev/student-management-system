package com.mastercode.studentmanagementback.mapper;

import com.mastercode.studentmanagementback.dto.StudentDto;
import com.mastercode.studentmanagementback.dto.StudentRequestModel;
import com.mastercode.studentmanagementback.dto.StudentResponseModel;
import com.mastercode.studentmanagementback.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface StudentMapper {

    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    Student mapToStudent(StudentRequestModel studentRequest);

    StudentResponseModel mapToStudentResponseModel(Student student);

    List<StudentResponseModel> mapToStudentResponseModels(List<Student> students);

  StudentDto mapToStudentDto(Student principal);
}
