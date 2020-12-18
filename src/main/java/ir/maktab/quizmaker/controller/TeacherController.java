package ir.maktab.quizmaker.controller;

import ir.maktab.quizmaker.domains.*;
import ir.maktab.quizmaker.exception.UniqueException;
import ir.maktab.quizmaker.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * @author Alireza.d.a
 */
@Controller
@RequestMapping("teacher")
public class TeacherController {

    private Teacher teacher;


    @Autowired
    private TeacherService teacherService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ExamService examService;

    @Autowired
    private UserService userService;

    @Autowired
    private DescriptiveQuestionService descriptiveQuestionService;

    @Autowired
    private MultipleChoiceQuestionService multipleChoiceQuestionService;


    private Set<Course> courses;

    @GetMapping("home")
    public String showHome(Model model) {
        teacher = (Teacher) userService.findByUsername(
                (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()
        );
        courses = teacher.getCourses();
        model.addAttribute("teacher", teacher);
        return "teacher-home";
    }

    @GetMapping("show-courses")
    public String showCourses(Model model) {
        model.addAttribute("courses", courses);
        return "teacher-show-courses";
    }


    @GetMapping("edit-user")
    public String sendEditUserForm(Model model) {
        model.addAttribute("teacher", teacher);
        model.addAttribute("edited", false);
        model.addAttribute("unmatched_error", false);
        model.addAttribute("current_password_error", false);
        return "teacher-edit-user";
    }

    @PostMapping("edit-user")
    public String editUser(Model model, HttpServletRequest req) throws Exception {
        boolean flag = userService.editUser(req, model);
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String nationalCode = req.getParameter("nationalCode");
        if (flag)
            teacherService.edit(firstName, lastName, nationalCode, teacher);
        model.addAttribute("teacher", teacher);
        return "teacher-edit-user";
    }

    @GetMapping("add-exam/{courseId}")
    public String sendAddExamForm(@PathVariable Long courseId, Model model) {
        Course course = courseService.findById(courseId);
        model.addAttribute("exam", new Exam(course, teacher));
        model.addAttribute("course", course);
        model.addAttribute("teacher", teacher);
        return "teacher-add-exam";
    }

    @PostMapping("add-exam")
    public String addExam(@ModelAttribute Exam exam, Model model) {
        try {
            examService.save(exam);
            model.addAttribute("exams", examService.findAllByTeacher(teacher));
        } catch (Exception ex) {
            model.addAttribute("exam", new Exam());
            model.addAttribute("course", exam.getCourse());
            model.addAttribute("teacher", teacher);
            return "redirect:/teacher/add-exam/" + exam.getCourse().getId();
        }
        return "redirect:/teacher/show-exams";
    }

    @GetMapping("show-exams")
    public String sendExamsList(Model model) {
        model.addAttribute("exams", examService.findAllByTeacher(teacher));
        return "teacher-show-exams";
    }

    @GetMapping("set-exam-available/{examId}")
    public String setExamAvailable(@PathVariable Long examId, Model model) {
        examService.setExamAvailable(examId);
        model.addAttribute("exams", examService.findAllByTeacher(teacher));
        return "teacher-show-exams";
    }

    @GetMapping("set-exam-unavailable/{examId}")
    public String setExamUnAvailable(@PathVariable Long examId, Model model) {
        examService.setExamUnAvailable(examId);
        model.addAttribute("exams", examService.findAllByTeacher(teacher));
        return "teacher-show-exams";
    }

    @GetMapping("show-exams/{courseId}")
    public String showCourseExams(@PathVariable Long courseId, Model model) {
        Course course = courseService.findById(courseId);
        model.addAttribute("exams", course.getExams());
        return "teacher-show-exams";
    }

    @GetMapping("show-students/{courseId}")
    public String showCourseStudents(@PathVariable Long courseId, Model model) {
        Course course = courseService.findById(courseId);
        model.addAttribute("students", course.getStudents());
        return "teacher-show-course-students";
    }

    @GetMapping("edit-exam/{examId}")
    public String sendEditExamForm(@PathVariable Long examId, Model model) {
        Exam exam = examService.findById(examId);
        model.addAttribute("exam", exam);
        return "teacher-edit-exam";
    }

    @GetMapping("delete-exam/{examId}")
    public String deleteExam(@PathVariable Long examId,Model model){
        examService.deleteById(examId);
        model.addAttribute("exams", examService.findAllByTeacher(teacher));
        return "teacher-show-exams";
    }
    @PostMapping("edit-exam")
    public String editExam(@ModelAttribute Exam exam, Model model) {

        examService.save(exam);

        model.addAttribute("exam", exam);
        return "teacher-edit-exam";
    }

    @GetMapping("descriptive-question/{examId}")
    public String sendAddDescriptiveQuestionForm(@PathVariable Long examId, Model model) {
        Exam exam = examService.findById(examId);

        model.addAttribute("exam", exam);
        model.addAttribute("question", new DescriptiveQuestion(exam));
        return "teacher-descriptive-question";
    }

    @GetMapping("multiple-question/{examId}")
    public String sendAddMultipleQuestionForm(@PathVariable Long examId, Model model) {
        Exam exam = examService.findById(examId);

        model.addAttribute("exam", exam);
        model.addAttribute("question", new DescriptiveQuestion(exam));
        return "teacher-multiple-question";
    }

    @PostMapping("multiple-question}")
    public String addMultipleQuestion(@ModelAttribute MultipleChoiceQuestion multipleChoiceQuestion, Model model,
                                      HttpServletRequest req) {
        long examId = Long.parseLong(req.getParameter("examId"));
        Exam exam = examService.findById(examId);
        try {
            multipleChoiceQuestionService.save(multipleChoiceQuestion);
            model.addAttribute("added",true);
        } catch (Exception ex) {
            model.addAttribute("added",false);
        }
        model.addAttribute("exam", exam);
        model.addAttribute("question", new MultipleChoiceQuestion(exam));
        return "teacher-multiple-question";
    }

    @PostMapping("descriptive-question")
    public String addDescriptiveQuestion(@ModelAttribute DescriptiveQuestion descriptiveQuestion, Model model,
                                         HttpServletRequest req) {
        long examId = Long.parseLong(req.getParameter("examId"));
        Exam exam = examService.findById(examId);
        model.addAttribute("exam", exam);
        try {
            descriptiveQuestionService.save(descriptiveQuestion);
            model.addAttribute("added",true);
        } catch (Exception ex) {
            model.addAttribute("added",false);
        }
        model.addAttribute("question", new DescriptiveQuestion(exam));
        return "teacher-descriptive-question";
    }

    @ExceptionHandler(Exception.class)
    public String handle(Exception ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "user-exception";
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
