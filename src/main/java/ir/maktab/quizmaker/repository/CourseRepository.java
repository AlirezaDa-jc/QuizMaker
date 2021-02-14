package ir.maktab.quizmaker.repository;

import ir.maktab.quizmaker.domains.Course;
import ir.maktab.quizmaker.domains.Student;
import ir.maktab.quizmaker.domains.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Alireza.d.a
 */
@Repository
public interface CourseRepository extends JpaRepository<Course,Long> {
    Course findByNameAndStartDateAndEndDateAndTeacher(String name, String startDate, String endDate, Teacher teacher);
    Course findByName(String name);
    List<Course> findAllByStudentsNotContains(Student student);
    List<Course> findAllByTeacherNull();
}
