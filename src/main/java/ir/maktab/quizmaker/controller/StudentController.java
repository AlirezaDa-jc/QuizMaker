package ir.maktab.quizmaker.controller;

import ir.maktab.quizmaker.domains.Course;
import ir.maktab.quizmaker.domains.Exam;
import ir.maktab.quizmaker.domains.Student;
import ir.maktab.quizmaker.exception.UniqueException;
import ir.maktab.quizmaker.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author Alireza.d.a
 */
@Controller
@RequestMapping("student")
public class StudentController {

    private Student student;

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ExamService examService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private StudentQuestionScoreService studentQuestionScoreService;

    @Autowired
    private UserService userService;

    private Set<Course> courses;

    @GetMapping("home")
    public String showHome(Model model) {
        student = (Student) userService.findByUsername(
                (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()
        );
        courses = student.getCourses();
        model.addAttribute("student", student);
        return "student-home";
    }

    @GetMapping("edit-user")
    public String sendEditUserForm(Model model) {
        model.addAttribute("student", student.getUserName());
        model.addAttribute("edited", false);
        model.addAttribute("unmatched_error", false);
        model.addAttribute("current_password_error", false);
        return "student-edit-user";
    }

    @PostMapping("edit-user")
    public String editUser(Model model, HttpServletRequest req) throws Exception {
        userService.editUser(req, model);
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String nationalCode = req.getParameter("nationalCode");
        String studentCode = req.getParameter("studentCode");
        studentService.edit(firstName, lastName, nationalCode, studentCode, student);
        model.addAttribute("student", student.getUserName());
        return "student-edit-user";
    }

    @GetMapping("show-courses")
    public String showCourses(Model model) {
        model.addAttribute("courses", courses);
        return "student-show-courses";
    }

    @GetMapping("show-exams/{courseId}")
    public String showExams(Model model, @PathVariable Long courseId) {
        List<Exam> exams = courseService.findById(courseId).getExams().stream()
                .filter(Exam::isAvailable)
                .collect(Collectors.toList());
        model.addAttribute("exams", exams);
        model.addAttribute("joined", false);
        return "student-show-exams";
    }

    @GetMapping("join-exam/{examId}")
    public String joinExam(@PathVariable Long examId, Model model) throws Exception {
        Exam exam = examService.findById(examId);
//        if(exam.getStudents().contains(student)){
        if (examService.checkStudentJoinedExam(student, exam)) {
            model.addAttribute("exams", exam.getCourse().getExams());
            model.addAttribute("joined", true);
            return "student-show-exams";

        }
        model.addAttribute("exam", exam);
        model.addAttribute("multipleChoice", examService.findMultipleChoiceQuestions(exam));
        model.addAttribute("descriptive", examService.findDescriptiveQuestions(exam));
        exam.addStudent(student);
        examService.save(exam);
        return "student-join-exam";
    }

    @PostMapping("correct-exam/{examId}")
    public String correctExam(@PathVariable Long examId, HttpServletRequest request, Model model) {
        Exam exam = examService.findById(examId);

        if (examService.checkStudentJoinedExam(student, exam)) {
            model.addAttribute("exams", exam.getCourse().getExams());
            model.addAttribute("joined", true);
            return "student-show-exams";
        }

        String[] answers = new String[exam.getScores().size()];
        int i = 0;
        String answer = request.getParameter(String.valueOf(i));
        while (answer != null) {
            answers[i] = answer;
            i++;
            answer = request.getParameter(String.valueOf(i));
        }
        studentQuestionScoreService.correctAnswers(exam, student, answers);
//TODO Quiz Front!
        return "redirect:/student/home";

    }


    @ExceptionHandler(UniqueException.class)
    public String handle(UniqueException ex, Model model) {

        model.addAttribute("message", ex.getMessage());
        return "user-exception";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handle(IllegalArgumentException ex, Model model) {

        model.addAttribute("message", ex.getMessage());
        return "user-exception";
    }

}
