package ir.maktab.quizmaker.controller;

import ir.maktab.quizmaker.base.CustomTimer;
import ir.maktab.quizmaker.domains.*;
import ir.maktab.quizmaker.exception.UniqueException;
import ir.maktab.quizmaker.services.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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


    private final StudentService studentService;


    private final CourseService courseService;


    private final ExamService examService;


    private final StudentQuestionScoreService studentQuestionScoreService;


    private final QuestionExamScoreService questionExamScoreService;


    private final UserService userService;
    private int questions = 0;

    private Set<Course> courses;

    private CustomTimer customTimer = null;

    Thread time;

    public StudentController(StudentService studentService,
                             CourseService courseService,
                             ExamService examService,
                             StudentQuestionScoreService studentQuestionScoreService,
                             QuestionExamScoreService questionExamScoreService, UserService userService) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.examService = examService;
        this.studentQuestionScoreService = studentQuestionScoreService;
        this.questionExamScoreService = questionExamScoreService;
        this.userService = userService;
    }

    @GetMapping("home")
    public String showHome(Model model, HttpSession session) {
        student = (Student) userService.findByUsername(
                (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()
        );
        courses = student.getCourses();
        session.setAttribute("userName", SecurityContextHolder.getContext().getAuthentication().getName());

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
        model.addAttribute("joined_exam", false);
        return "student-show-exams";
    }

    @GetMapping("join-exam/{examId}/{questionId}")
    public String joinExam(@PathVariable Long examId, @PathVariable Long questionId, Model model) throws Exception {
        Exam exam = examService.findById(examId);
        List<Student> students = exam.getStudents().stream().filter(c -> c.equals(student)).collect(Collectors.toList());
        if (students.size() != 0) {
            model.addAttribute("exams", exam.getCourse().getExams());
            model.addAttribute("joined", true);
            model.addAttribute("joined_exam", false);
            return "student-show-exams";
        }
        if (customTimer == null) {
            customTimer = new CustomTimer(examId);
        }

        if(customTimer.getExamId() == examId) {
            if(time == null) {
                time = new Thread("time") {
                    public void run() {
                        try {
                            Thread.sleep(exam.getTime() * 60000);
                            examService.endExam(exam, student);
                            customTimer = null;
                            questions = 0;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                time.start();
            }
        }else {
            model.addAttribute("exams", exam.getCourse().getExams());
            model.addAttribute("joined", false);
            model.addAttribute("joined_exam", true);
            return "student-show-exams";
        }
        if (questionId < exam.getScores().size()) {
            List<MultipleChoiceQuestion> multipleChoiceQuestions = examService.findMultipleChoiceQuestions(exam);
            List<DescriptiveQuestion> descriptiveQuestions = examService.findDescriptiveQuestions(exam);
            model.addAttribute("exam", exam);
            model.addAttribute("questionId", questionId);
            double time = exam.getTime() * 60000 - customTimer.elapsedTime();
            model.addAttribute("time", time);
            studentQuestionScoreService.createStudentQuestionExamScores(student, exam.getScores());
            if (questionId < multipleChoiceQuestions.size()) {
                MultipleChoiceQuestion question = multipleChoiceQuestions.get(Math.toIntExact(questionId));
                QuestionExamScore questionExamScore = questionExamScoreService.findByExamAndQuestion(exam, question);
                StudentQuestionScore studentQuestionScore = studentQuestionScoreService
                        .findByStudentAndQuestionExamScore(student, questionExamScore);
                model.addAttribute("MultipleChoiceQuestion", question);
                model.addAttribute("studentQuestionScore", studentQuestionScore);
                model.addAttribute("score", questionExamScore);
                return "student-join-exam-multiple-choice-question";
            } else if ((questionId - multipleChoiceQuestions.size()) < descriptiveQuestions.size()) {
                DescriptiveQuestion question = descriptiveQuestions.get(Math.toIntExact(questionId - multipleChoiceQuestions.size()));
                QuestionExamScore questionExamScore = questionExamScoreService.findByExamAndQuestion(exam, question);
                StudentQuestionScore studentQuestionScore = studentQuestionScoreService
                        .findByStudentAndQuestionExamScore(student, questionExamScore);
                model.addAttribute("studentQuestionScore", studentQuestionScore);
                model.addAttribute("DescriptiveQuestion", question);
                model.addAttribute("score", questionExamScore);
                return "student-join-exam-descriptive-question";
            }
        }
        examService.endExam(exam, student);
        customTimer = null;
        questions=0;
        return "redirect:/student/show-exams/"+exam.getCourse().getId();

    }

    @PostMapping("correct-exam")
    public String correctExam(@ModelAttribute StudentQuestionScore studentQuestionScore) {
        studentQuestionScoreService.correctAnswer(studentQuestionScore);
        QuestionExamScore questionExamScore = studentQuestionScore.getQuestionExamScore();
        Long examId = questionExamScore.getExam().getId();
        questions++;
        return "redirect:/student/join-exam/" + examId + "/" + questions;
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
