package org.example.deu_courseregistration.service;

import jakarta.transaction.Transactional;
import org.example.deu_courseregistration.entity.*;
import org.example.deu_courseregistration.repository.courseRegistrationRepository;
import org.example.deu_courseregistration.repository.courseRepository;
import org.example.deu_courseregistration.repository.studentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class CourseRegistrationService {
    @Autowired
    private courseRegistrationRepository courseRegistrationRepository;

    @Autowired
    private courseRepository courseRepository;

    @Autowired
    private studentRepository studentRepository;

    @Transactional
    public String addToCourseRegistration(String studentId, Long courseId) {
        // 학생 정보와 강좌 정보 가져오기
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        Optional<Course> courseOptional = courseRepository.findById(courseId);

        if (studentOptional.isEmpty()) {
            return "해당 학번의 학생을 찾을 수 없습니다.";
        }
        if (courseOptional.isEmpty()) {
            return "해당 교과목번호의 강좌를 찾을 수 없습니다.";
        }

        Student student = studentOptional.get();
        Course course = courseOptional.get();

        // 이미 수강신청목록에 있는지 확인
        courseRegistrationId courseRegistrationId = new courseRegistrationId(student.getStudentId(), course.getCourseId());
        if (courseRegistrationRepository.existsById(courseRegistrationId)) {
            return "이미 수강 신청한 강좌입니다.";
        }

        // 수강신청에 추가
        CourseRegistration courseRegistration = new CourseRegistration();
        courseRegistration.setId(courseRegistrationId);
        courseRegistration.setStudentId(student);
        courseRegistration.setCourseId(course);

        courseRegistrationRepository.save(courseRegistration);

        return "수강신청이 성공적으로 완료되었습니다.";
    }
}