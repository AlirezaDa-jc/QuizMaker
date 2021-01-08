package ir.maktab.quizmaker.controller;

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

/**
 * @author Alireza.d.a
 */
@Controller
@RequestMapping("teacher")
public class TeacherController {

    private Teacher teacher;


    private final TeacherService teacherService;

    private final CourseService courseService;

    private final ExamService examService;

    private final UserService userService;

    private final QuestionService questionService;

    private final QuestionExamScoreService questionExamScoreService;

    private final StudentQuestionScoreService studentQuestionScoreService;

    private final StudentService studentService;

    private QuestionExamScore tempQuestionExamScore;

    private Set<Course> courses;

    public TeacherController(TeacherService teacherService,
                             CourseService courseService,
                             ExamService examService,
                             UserService userService,
                             QuestionService questionService,
                             QuestionExamScoreService questionExamScoreService,
                             StudentQuestionScoreService studentQuestionScoreService, StudentService studentService) {
        this.teacherService = teacherService;
        this.courseService = courseService;
        this.examService = examService;
        this.userService = userService;
        this.questionService = questionService;
        this.questionExamScoreService = questionExamScoreService;
        this.studentQuestionScoreService = studentQuestionScoreService;
        this.studentService = studentService;
    }

    @GetMapping("home")
    public String showHome(Model model, HttpSession session) {
        teacher = (Teacher) userService.findByUsername(
                (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()
        );
        courses = teacher.getCourses();
        session.setAttribute("userName",SecurityContextHolder.getContext().getAuthentication().getName());
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
    public String sendAddExamForm(@PathVariable Long courseId, Model model) throws Exception {
        Course course = courseService.findById(courseId);
        if (!course.getTeacher().getUserName().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new Exception("403 Forbidden!");
        }
        model.addAttribute("exam", new Exam(course, teacher));
        model.addAttribute("course", course);
        model.addAttribute("teacher", teacher);
        model.addAttribute("error", false);
        return "teacher-add-exam";
    }

    @PostMapping("add-exam")
    public String addExam(@ModelAttribute Exam exam, Model model) {
        try {
            examService.save(exam);
            model.addAttribute("exams", examService.findAllByTeacher(teacher));
        } catch (Exception ex) {
            model.addAttribute("error", true);
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
    public String setExamAvailable(@PathVariable Long examId, Model model) throws Exception {
        if (!examService.findById(examId).getTeacher().getUserName().
                equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new Exception("403 Forbidden!");
        }
        examService.setExamAvailable(examId);
        model.addAttribute("exams", examService.findAllByTeacher(teacher));
        return "teacher-show-exams";
    }

    @GetMapping("set-exam-unavailable/{examId}")
    public String setExamUnAvailable(@PathVariable Long examId, Model model) throws Exception {
        if (!examService.findById(examId).getTeacher().getUserName().
                equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new Exception("403 Forbidden!");
        }
        examService.setExamUnAvailable(examId);
        model.addAttribute("exams", examService.findAllByTeacher(teacher));
        return "teacher-show-exams";
    }

    @GetMapping("show-exams/{courseId}")
    public String showCourseExams(@PathVariable Long courseId, Model model) throws Exception {
        Course course = courseService.findById(courseId);
        if (!course.getTeacher().getUserName().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new Exception("403 Forbidden!");
        }
        model.addAttribute("exams", course.getExams());
        return "teacher-show-exams";
    }

    @GetMapping("show-students/{courseId}")
    public String showCourseStudents(@PathVariable Long courseId, Model model) throws Exception {
        Course course = courseService.findById(courseId);
        if (!course.getTeacher().getUserName().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new Exception("403 Forbidden!");
        }
        model.addAttribute("students", course.getStudents());
        return "teacher-show-course-students";
    }

    @GetMapping("edit-exam/{examId}")
    public String sendEditExamForm(@PathVariable Long examId, Model model) throws Exception {
        if (!examService.findById(examId).getTeacher().getUserName().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new Exception("403 Forbidden!");
        }
        Exam exam = examService.findById(examId);
        model.addAttribute("exam", exam);
        model.addAttribute("error", false);
        return "teacher-edit-exam";
    }

    @GetMapping("delete-exam/{examId}")
    public String deleteExam(@PathVariable Long examId, Model model) throws Exception {
        if (!examService.findById(examId).getTeacher().getUserName().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new Exception("403 Forbidden!");
        }
        examService.deleteById(examId);
        model.addAttribute("exams", examService.findAllByTeacher(teacher));
        return "teacher-show-exams";
    }

    @PostMapping("edit-exam")
    public String editExam(@ModelAttribute Exam exam, Model model) {
        try {
            examService.save(exam);
        } catch (Exception ex) {
            model.addAttribute("error", true);
        }
        model.addAttribute("exam", exam);
        return "teacher-edit-exam";
    }

    @GetMapping("show-questions/{examId}")
    public String showQuestionsExam(@PathVariable Long examId, Model model) throws Exception {
        Exam exam = examService.findById(examId);
        if (!exam.getTeacher().getUserName().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new Exception("403 Forbidden!");
        }
        List<MultipleChoiceQuestion> multipleChoiceQuestions = examService.findMultipleChoiceQuestions(exam);
        List<DescriptiveQuestion> descriptiveQuestions = examService.findDescriptiveQuestions(exam);
        model.addAttribute("multiple", multipleChoiceQuestions);
        model.addAttribute("descriptive", descriptiveQuestions);
        model.addAttribute("exam", exam);
        return "teacher-show-questions";
    }

    @GetMapping("descriptive-question/{examId}")
    public String sendAddDescriptiveQuestionForm(@PathVariable Long examId, Model model) throws Exception {
        Exam exam = examService.findById(examId);
        if (!exam.getTeacher().getUserName().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new Exception("403 Forbidden!");
        }
        DescriptiveQuestion descriptiveQuestion = new DescriptiveQuestion(exam);
        QuestionExamScore questionExamScore = questionExamScoreService.create(exam);
        tempQuestionExamScore = questionExamScore;
        model.addAttribute("exam", exam);
        model.addAttribute("descriptiveQuestion", descriptiveQuestion);
        model.addAttribute("score", questionExamScore);
        return "teacher-descriptive-question";
    }

    @GetMapping("multiple-question/{examId}")
    public String sendAddMultipleQuestionForm(@PathVariable Long examId, Model model) throws Exception {
        Exam exam = examService.findById(examId);
        if (!exam.getTeacher().getUserName().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new Exception("403 Forbidden!");
        }
        MultipleChoiceQuestion multipleChoiceQuestion = new MultipleChoiceQuestion(exam);
        QuestionExamScore questionExamScore = questionExamScoreService.create(exam);
        tempQuestionExamScore = questionExamScore;
        model.addAttribute("exam", exam);
        model.addAttribute("multipleChoiceQuestion", multipleChoiceQuestion);
        model.addAttribute("score", questionExamScore);
        return "teacher-multiple-question";
    }

    @PostMapping("multiple-question")
    public String addMultipleQuestion(@ModelAttribute MultipleChoiceQuestion multipleChoiceQuestion, Model model,
                                      HttpServletRequest req) {
        long examId = Long.parseLong(req.getParameter("examId"));
        int score = Integer.parseInt(req.getParameter("score"));
        Exam exam = examService.findById(examId);
        tempQuestionExamScore.setScore(score);
        try {
            questionService.create(multipleChoiceQuestion, exam, tempQuestionExamScore);
            model.addAttribute("added", true);
        } catch (Exception ex) {
            model.addAttribute("added", false);
        }
        model.addAttribute("exam", exam);
        model.addAttribute("multipleChoiceQuestion", new MultipleChoiceQuestion(exam));
        return "teacher-multiple-question";
    }

    @PostMapping("descriptive-question")
    public String addDescriptiveQuestion(@ModelAttribute DescriptiveQuestion descriptiveQuestion, Model model,
                                         HttpServletRequest req) {
        long examId = Long.parseLong(req.getParameter("examId"));
        int score = Integer.parseInt(req.getParameter("score"));
        Exam exam = examService.findById(examId);
        tempQuestionExamScore.setScore(score);
        model.addAttribute("exam", exam);
        try {
            questionService.create(descriptiveQuestion, exam, tempQuestionExamScore);
            model.addAttribute("added", true);
        } catch (Exception ex) {
            model.addAttribute("added", false);
        }
        model.addAttribute("descriptiveQuestion", new DescriptiveQuestion(exam));
        return "teacher-descriptive-question";
    }

    @GetMapping("add_question_from_bank/{examId}")
    public String sendBankQuestions(@PathVariable Long examId, Model model) throws Exception {
        Exam exam = examService.findById(examId);
        if (!exam.getTeacher().getUserName().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new Exception("403 Forbidden!");
        }
        Set<Question> questions = questionService.getQuestionBank(exam);
        model.addAttribute("multipleChoice", questionService.findMultipleChoiceQuestions(questions));
        model.addAttribute("descriptive", questionService.findDescriptiveQuestions(questions));
        model.addAttribute("examId", examId);
        return "teacher-add-question-from-bank";
    }

    @GetMapping("add_question/{examId}/{questionId}")
    public String sendBankQuestions(@PathVariable Long examId, @PathVariable Long questionId, Model model) throws Exception {
        Exam exam = examService.findById(examId);
        if (!exam.getTeacher().getUserName().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new Exception("403 Forbidden!");
        }
        Question question = questionService.findById(questionId);
        QuestionExamScore questionExamScore = questionExamScoreService.create(exam,question);
        model.addAttribute("questionExamScore",questionExamScore);
        model.addAttribute("exam",exam);
        model.addAttribute("question",question);
        return "teacher-add-score-question-from-bank";
    }

    @PostMapping("add-score-question-from-bank")
    public String addQuestionFromBank(@ModelAttribute QuestionExamScore questionExamScore ,Model model) throws Exception {
        System.out.println(questionExamScore);
        if (!questionExamScore.getExam().getTeacher().getUserName()
                .equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new Exception("403 Forbidden!");
        }
        questionExamScoreService.save(questionExamScore);
        Exam exam = questionExamScore.getExam();
        Set<Question> questions = questionService.getQuestionBank(exam);
        model.addAttribute("multipleChoice", questionService.findMultipleChoiceQuestions(questions));
        model.addAttribute("descriptive", questionService.findDescriptiveQuestions(questions));
        model.addAttribute("examId", exam.getId());
        return "teacher-add-question-from-bank";
    }

    @GetMapping("edit-multiple-choice-question/{examId}/{questionId}")
    private String sendEditMultipleChoiceQuestionForm(@PathVariable Long examId, @PathVariable Long questionId, Model model) throws Exception {
        Exam exam = examService.findById(examId);
//        First Token Check After Session
        if (!exam.getTeacher().getUserName().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new Exception("403 Forbidden!");
        }
        MultipleChoiceQuestion multipleChoiceQuestion = (MultipleChoiceQuestion) questionService.findById(questionId);
        QuestionExamScore questionExamScore = questionExamScoreService.create(exam);
        tempQuestionExamScore = questionExamScore;
        model.addAttribute("exam", exam);
        model.addAttribute("multipleChoiceQuestion", multipleChoiceQuestion);
        model.addAttribute("score", questionExamScore);
        return "teacher-multiple-question";
    }

    @GetMapping("edit-descriptive-question/{examId}/{questionId}")
    private String sendEditDescriptiveQuestionForm(@PathVariable Long examId, @PathVariable Long questionId, Model model) throws Exception {
        Exam exam = examService.findById(examId);
        if (!exam.getTeacher().getUserName().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new Exception("403 Forbidden!");
        }
        DescriptiveQuestion descriptiveQuestion = (DescriptiveQuestion) questionService.findById(questionId);
        QuestionExamScore questionExamScore = questionExamScoreService.create(exam);
        tempQuestionExamScore = questionExamScore;
        model.addAttribute("exam", exam);
        model.addAttribute("descriptiveQuestion", descriptiveQuestion);
        model.addAttribute("score", questionExamScore);
        return "teacher-descriptive-question";
    }

    @GetMapping("students-joined-exam/{examId}")
    public String showStudentsJoinedExam(@PathVariable Long examId, Model model) throws Exception {
        Exam exam = examService.findById(examId);
        if (!exam.getTeacher().getUserName().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new Exception("403 Forbidden!");
        }
        model.addAttribute("exam",exam);


        return "teacher-students-joined-exam";
    }

    @GetMapping("correct-exam/{examId}/{studentId}")
    public String correctExam(@PathVariable Long examId,@PathVariable Long studentId,Model model) throws Exception {
        Exam exam = examService.findById(examId);
        if (!exam.getTeacher().getUserName().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new Exception("403 Forbidden!");
        }
        Student student = studentService.findById(studentId);
        List<QuestionExamScore> questionExamScores = questionExamScoreService.getScores(exam);
        List<StudentQuestionScore> studentQuestionScores = studentQuestionScoreService
                .findByQuestionExamScoresAndStudent(questionExamScores, student);
        int sumStudent = studentQuestionScoreService.getSumOfScores(studentQuestionScores);
        int sumExam = questionExamScores.stream().mapToInt(QuestionExamScore::getScore).sum();
        model.addAttribute("questionExamScores",questionExamScores);
        model.addAttribute("studentQuestionScores",studentQuestionScores);
        model.addAttribute("exam",exam);
        model.addAttribute("student",student);
        model.addAttribute("multipleChoice",examService.findMultipleChoiceQuestions(exam));
        model.addAttribute("descriptive",examService.findDescriptiveQuestions(exam));
        model.addAttribute("sumStudent",sumStudent);
        model.addAttribute("sumExam",sumExam);

        return "teacher-correct-exam";
    }

    @PostMapping("correct-exam")
    public String sendMarkDescriptive(HttpServletRequest request) throws Exception {
        Exam exam = examService.findById(Long.valueOf(request.getParameter("exam")));
        Student student = studentService.findById(Long.valueOf(request.getParameter("student")));
        List<MultipleChoiceQuestion> multipleChoiceQuestions = examService.findMultipleChoiceQuestions(exam);
        if (!exam.getTeacher().getUserName().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new Exception("403 Forbidden!");
        }
        List<DescriptiveQuestion> descriptiveQuestions = examService.findDescriptiveQuestions(exam);
        for(int i = 0 ; i < descriptiveQuestions.size() ; i++){
            String input = request.getParameter(String.valueOf(i + multipleChoiceQuestions.size()));
            if(input != null && !input.equals("")) {
                int score = Integer.parseInt(input);
                List<QuestionExamScore> questionExamScores = descriptiveQuestions.get(i).getScores();
                studentQuestionScoreService.assignScore(questionExamScores, student, score);
            }
        }

        return "redirect:/teacher/correct-exam/"+exam.getId()+"/"+student.getId();
    }


    @ExceptionHandler(Exception.class)
    public String handle(Exception ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        ex.printStackTrace();
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
