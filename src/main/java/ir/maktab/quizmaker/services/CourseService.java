package ir.maktab.quizmaker.services;

import ir.maktab.quizmaker.domains.*;
import ir.maktab.quizmaker.dto.CourseDTO;
import ir.maktab.quizmaker.dto.ExamDTO;
import ir.maktab.quizmaker.repository.CourseRepository;
import ir.maktab.quizmaker.services.mappers.CourseMapperImpl;
import ir.maktab.quizmaker.services.mappers.ExamMapperImpl;
import ir.maktab.quizmaker.services.mappers.SubjectMapperImpl;
import ir.maktab.quizmaker.services.mappers.TeacherMapperImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Alireza.d.a
 */
@Service
public class CourseService {


    private final CourseRepository courseRepository;
    private final SubjectService subjectService;

    public CourseService(CourseRepository courseRepository, SubjectService subjectService) {
        this.courseRepository = courseRepository;
        this.subjectService = subjectService;
    }

    public List<Course> findAllWithoutTeacher() {
        return courseRepository.findAllByTeacherNull();
    }

    public Course findById(Long id) {
        return courseRepository.findById(id).isPresent() ? courseRepository.findById(id).get() : null;
    }

    public Course save(Course course) {
        return courseRepository.save(course);
    }

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public List<Course> findAllWithoutThisStudent(Student student) {
        return courseRepository.findAllByStudentsNotContains(student);
    }

    public Course addUser(Course course, User tempUser) {
        if (tempUser.getRole().equals("STUDENT")) {
            course.addStudent((Student) tempUser);
        } else {
            course.setTeacher((Teacher) tempUser);
        }
        tempUser.setAllowed(true);
        return courseRepository.save(course);
    }


    public void deleteById(Long courseId) {
        courseRepository.deleteById(courseId);
    }

    public boolean checkDate(Course course) {
        return course.getEndDate().compareTo(course.getStartDate()) < 0;
    }

    public CourseDTO convertToDto(Course course) {
        CourseDTO courseDTO = new CourseMapperImpl().sourceToDestination(course);
        courseDTO.setTeacher(new TeacherMapperImpl().sourceToDestination(course.getTeacher()));
        courseDTO.setSubject(new SubjectMapperImpl().sourceToDestination(course.getSubject()));
        Set<ExamDTO> examDTOS = course.getExams()
                .stream()
                .map(c -> new ExamMapperImpl().sourceToDestination(c))
                .collect(Collectors.toSet());
        courseDTO.setExams(examDTOS);
        return courseDTO;
    }

    public Course convertToEntity(CourseDTO courseDto) {
        Subject subject = subjectService.convertToEntity(courseDto.getSubject());
        Course course = new CourseMapperImpl().destinationToSource(courseDto);
        course.setSubject(subject);
        if (courseDto.getTeacher() != null)
            course.setTeacher(new TeacherMapperImpl().destinationToSource(courseDto.getTeacher()));

        return course;
    }

    public Course findByName(String name) {
        return courseRepository.findByName(name);
    }

    public Course findByNameAndStartDateAndEndDateAndTeacher(String name, String startDate, String endDate, Teacher teacher) {
        return courseRepository.findByNameAndStartDateAndEndDateAndTeacher(name,startDate,endDate,teacher);
    }
}
