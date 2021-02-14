package ir.maktab.quizmaker.controller;

import ir.maktab.quizmaker.aspect.Authentication;
import ir.maktab.quizmaker.domains.*;
import ir.maktab.quizmaker.dto.*;
import ir.maktab.quizmaker.services.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
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
    private final SubjectService subjectService;
    CourseDTO tempCourse;

    private Set<Course> courses;
    SubjectDTO tempSubject;
    Exam exam ;
    Question question;
    private QuestionExamScoreDTO questionExamScoreDTO;
    public TeacherController(TeacherService teacherService, CourseService courseService, ExamService examService,
                             UserService userService, QuestionService questionService,
                             QuestionExamScoreService questionExamScoreService, StudentQuestionScoreService
                                     studentQuestionScoreService, StudentService studentService,
                             SubjectService subjectService) {
        this.teacherService = teacherService;
        this.courseService = courseService;
        this.examService = examService;
        this.userService = userService;
        this.questionService = questionService;
        this.questionExamScoreService = questionExamScoreService;
        this.studentQuestionScoreService = studentQuestionScoreService;
        this.studentService = studentService;
        this.subjectService = subjectService;
    }

    @GetMapping("home")
    public String showHome(Model model, HttpSession session) {
        teacher = (Teacher) userService.findByUsername(
                (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()
        );
        courses = teacher.getCourses();
        session.setAttribute("userName", SecurityContextHolder.getContext().getAuthentication().getName());
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
    public String editUser(Model model, HttpServletRequest req) {
        boolean flag = userService.editUser(req, model);
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String nationalCode = req.getParameter("nationalCode");
        if (flag)
            teacherService.edit(firstName, lastName, nationalCode, teacher);
        model.addAttribute("teacher", teacher);
        return "teacher-edit-user";
    }

    @Authentication
    @GetMapping("add-exam/{courseId}")
    public String sendAddExamForm(@PathVariable Long courseId, Model model) throws Exception {
        Course course = courseService.findById(courseId);
        if (!course.getTeacher().getUserName().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new Exception("403 Forbidden!");
        }
        tempCourse = courseService.convertToDto(course);
        model.addAttribute("exam", new ExamDTO());
        model.addAttribute("course", tempCourse);
        model.addAttribute("teacher", teacherService.convertToDto(teacher));
        model.addAttribute("error", false);
        return "teacher-add-exam";
    }

    @PostMapping("add-exam")
    public String addExam(@Valid @ModelAttribute ExamDTO examDTO, Model model) {
        Exam exam = examService.convertToEntity(examDTO);
        try {
            Course course = courseService.convertToEntity(tempCourse);
            Course course1 = courseService.findByNameAndStartDateAndEndDateAndTeacher(course.getName(), course.getStartDate(), course.getEndDate(), teacher);
            exam.setCourse(course1);
            exam.setTeacher(teacher);
            examService.save(exam);
            model.addAttribute("exams", examService.findAllByTeacher(this.teacher));
        } catch (Exception ex) {
            model.addAttribute("error", true);
            model.addAttribute("exam", new ExamDTO());
            model.addAttribute("course", tempCourse);
            model.addAttribute("teacher", teacherService.convertToDto(teacher));

            return "redirect:/teacher/add-exam/" + exam.getCourse().getId();
        }
        tempCourse = null;
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
        ExamDTO examDTO = examService.convertToDto(exam);
        tempCourse = courseService.convertToDto(exam.getCourse());
        tempSubject = subjectService.convertToDto(exam.getCourse().getSubject());

        tempCourse.setTeacher(teacherService.convertToDto(teacher));

        model.addAttribute("exam", examDTO);
        model.addAttribute("id", examId);
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
    public String editExam(@Valid @ModelAttribute ExamDTO examDTO, Model model, ServletRequest request) {
        Exam exam = examService.convertToEntity(examDTO);
        Long id = Long.valueOf(request.getParameter("id"));
        exam.setId(id);
        exam.setTeacher(teacher);
        exam.setCourse(courseService.convertToEntity(tempCourse));
        try {
            examService.save(exam);
        } catch (Exception ex) {
            model.addAttribute("error", true);
        }
        model.addAttribute("exam", examService.convertToDto(exam));
        model.addAttribute("id", id);
        tempCourse = null;
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
        CourseDTO courseDTO = courseService.convertToDto(exam.getCourse());
        tempSubject = subjectService.convertToDto(exam.getCourse().getSubject());
        tempCourse = courseDTO;
        ExamDTO examDTO = examService.convertToDto(exam);
        examDTO.setTeacher(teacherService.convertToDto(teacher));
        courseDTO.setSubject(subjectService.convertToDto(exam.getCourse().getSubject()));
        examDTO.setCourse(courseDTO);
        DescriptiveQuestionDTO descriptiveQuestionDTO = new DescriptiveQuestionDTO();
        questionExamScoreDTO = questionExamScoreService.convertToDto(new QuestionExamScore());
        model.addAttribute("exam", examDTO);
        model.addAttribute("examid", examId);
        model.addAttribute("descriptiveQuestion", descriptiveQuestionDTO);
        model.addAttribute("score", questionExamScoreDTO);
        return "teacher-descriptive-question";
    }

    @GetMapping("multiple-question/{examId}")
    public String sendAddMultipleQuestionForm(@PathVariable Long examId, Model model) throws Exception {
        Exam exam = examService.findById(examId);
        if (!exam.getTeacher().getUserName().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new Exception("403 Forbidden!");
        }
        tempSubject = subjectService.convertToDto(exam.getCourse().getSubject());
        CourseDTO courseDTO = courseService.convertToDto(exam.getCourse());
        tempCourse = courseDTO;
        ExamDTO examDTO = examService.convertToDto(exam);
        examDTO.setTeacher(teacherService.convertToDto(teacher));
        courseDTO.setSubject(subjectService.convertToDto(exam.getCourse().getSubject()));
        examDTO.setCourse(courseDTO);
        MultipleChoiceQuestionDTO multipleChoiceQuestion = new MultipleChoiceQuestionDTO();
        questionExamScoreDTO = questionExamScoreService.convertToDto(new QuestionExamScore());
        model.addAttribute("exam", examDTO);
        model.addAttribute("examid", examId);
        model.addAttribute("multipleChoiceQuestion", multipleChoiceQuestion);
        model.addAttribute("score", questionExamScoreDTO);
        return "teacher-multiple-question";
    }

    @PostMapping("multiple-question")
    public String addMultipleQuestion(@Valid @ModelAttribute MultipleChoiceQuestionDTO multipleChoiceQuestionDTO, Model model,
                                      HttpServletRequest req) {
        long examId = Long.parseLong(req.getParameter("examId"));
        int score = Integer.parseInt(req.getParameter("score"));
        Exam exam = examService.findById(examId);
        QuestionExamScore questionExamScore = questionExamScoreService.convertToEntity(questionExamScoreDTO);
        questionExamScore.setScore(score);
        questionExamScore.setExam(exam);
        String id = req.getParameter("id");
        if (id != null) {
            exam.setId(Long.valueOf(id));
        }
        MultipleChoiceQuestion multipleChoiceQuestion = (MultipleChoiceQuestion) questionService.convertToEntity(multipleChoiceQuestionDTO);
        Course course = courseService.convertToEntity(tempCourse);
        Course byName = courseService.findByNameAndStartDateAndEndDateAndTeacher(course.getName(), course.getStartDate(), course.getEndDate(), teacher);
        multipleChoiceQuestion.setCourse(byName);
        multipleChoiceQuestion.setTeacher(teacher);
        multipleChoiceQuestion.addSubject(subjectService.convertToEntity(tempSubject));

        questionService.create(multipleChoiceQuestion, exam, questionExamScore);
        model.addAttribute("added", true);

        ExamDTO examDTO = examService.convertToDto(exam);
        examDTO.setCourse(tempCourse);
        model.addAttribute("exam", examDTO);
        model.addAttribute("examid", examId);
        model.addAttribute("multipleChoiceQuestion", new MultipleChoiceQuestionDTO());
        model.addAttribute("score", questionExamScoreDTO);
        return "teacher-multiple-question";
    }

    @PostMapping("descriptive-question")
    public String addDescriptiveQuestion(@Valid @ModelAttribute DescriptiveQuestionDTO descriptiveQuestionDTO, Model model,
                                         HttpServletRequest req) {
        long examId = Long.parseLong(req.getParameter("examId"));
        int score = Integer.parseInt(req.getParameter("score"));
        Exam exam = examService.findById(examId);
        questionExamScoreDTO.setScore(score);
        ExamDTO examDTO = examService.convertToDto(exam);
        examDTO.setTeacher(teacherService.convertToDto(teacher));
        examDTO.setCourse(tempCourse);
        model.addAttribute("exam", examDTO);
        String id = req.getParameter("id");
        if (id != null) {
            exam.setId(Long.valueOf(id));
        }
        DescriptiveQuestion descriptiveQuestion = (DescriptiveQuestion) questionService.convertToEntity(descriptiveQuestionDTO);
        Course course = courseService.convertToEntity(tempCourse);
        Course byName = courseService.findByNameAndStartDateAndEndDateAndTeacher(course.getName(), course.getStartDate(), course.getEndDate(), teacher);
        descriptiveQuestion.setCourse(byName);
        descriptiveQuestion.setTeacher(teacher);
        descriptiveQuestion.addSubject(subjectService.convertToEntity(tempSubject));

        questionService.create(descriptiveQuestion, exam, questionExamScoreService.convertToEntity(questionExamScoreDTO));
        model.addAttribute("added", true);
        model.addAttribute("exam", examDTO);
        model.addAttribute("examid", examId);
        model.addAttribute("descriptiveQuestion", new DescriptiveQuestion());
        model.addAttribute("score", questionExamScoreDTO);
        return "teacher-descriptive-question";
    }

    @GetMapping("add_question_from_bank/{examId}/{page}")
    public String sendBankQuestions(@PathVariable Long examId,@PathVariable int page, Model model) throws Exception {
        Exam exam = examService.findById(examId);
        if (!exam.getTeacher().getUserName().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new Exception("403 Forbidden!");
        }
        List<Question> questions = questionService.getQuestionBank(exam,page);
        int size = questionService.getSize();
        int pages = (int) Math.ceil((double) size/10);
        model.addAttribute("multipleChoice", questionService.findMultipleChoiceQuestions(questions));
        model.addAttribute("descriptive", questionService.findDescriptiveQuestions(questions));
        model.addAttribute("examId", examId);
        model.addAttribute("page", pages);
        return "teacher-add-question-from-bank";
    }

    @GetMapping("add_question/{examId}/{questionId}")
    public String sendBankQuestions(@PathVariable Long examId, @PathVariable Long questionId, Model model) throws Exception {
        exam = examService.findById(examId);
        if (!exam.getTeacher().getUserName().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new Exception("403 Forbidden!");
        }
        question = questionService.findById(questionId);
       questionExamScoreDTO = new QuestionExamScoreDTO();
        ExamDTO examDTO = examService.convertToDto(exam);

        QuestionDTO questionDTO = questionService.convertToDto(question);
        model.addAttribute("questionExamScore", questionExamScoreDTO);
        model.addAttribute("exam", examDTO);
        model.addAttribute("question", questionDTO);
        return "teacher-add-score-question-from-bank";
    }

    @PostMapping("add-score-question-from-bank")
    public String addQuestionFromBank(@Valid @ModelAttribute QuestionExamScoreDTO questionExamScoreDTO) throws Exception {
        QuestionExamScore questionExamScore = questionExamScoreService.convertToEntity(questionExamScoreDTO);
        questionExamScore.setExam(exam);

        if (!exam.getTeacher().getUserName().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new Exception("403 Forbidden!");
        }
        questionExamScore.setQuestion(question);
        questionExamScoreService.save(questionExamScore);
        return "redirect:/teacher/add_question_from_bank/"+exam.getId()+"/"+"1";
    }

    @GetMapping("edit-multiple-choice-question/{examId}/{questionId}")
    private String sendEditMultipleChoiceQuestionForm(@PathVariable Long examId, @PathVariable Long questionId, Model model) throws Exception {
        Exam exam = examService.findById(examId);
//        First Token Check After Session
        if (!exam.getTeacher().getUserName().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new Exception("403 Forbidden!");
        }
        ExamDTO examDTO = examService.convertToDto(exam);
        MultipleChoiceQuestionDTO multipleChoiceQuestionDTO = (MultipleChoiceQuestionDTO) questionService.convertToDto(questionService.findById(questionId));
        questionExamScoreDTO = questionExamScoreService.convertToDto(questionExamScoreService.create(exam));
        model.addAttribute("exam", examDTO);
        model.addAttribute("descriptiveQuestion", multipleChoiceQuestionDTO);
        model.addAttribute("score", questionExamScoreDTO);
        model.addAttribute("id", questionId);
        return "teacher-multiple-question";
    }

    @GetMapping("edit-descriptive-question/{examId}/{questionId}")
    private String sendEditDescriptiveQuestionForm(@PathVariable Long examId, @PathVariable Long questionId, Model model) throws Exception {
        Exam exam = examService.findById(examId);
        if (!exam.getTeacher().getUserName().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new Exception("403 Forbidden!");
        }
        ExamDTO examDTO = examService.convertToDto(exam);
        DescriptiveQuestionDTO descriptiveQuestionDTO = (DescriptiveQuestionDTO) questionService.convertToDto(questionService.findById(questionId));
        questionExamScoreDTO = questionExamScoreService.convertToDto(questionExamScoreService.create(exam));
        model.addAttribute("exam", examDTO);
        model.addAttribute("descriptiveQuestion", descriptiveQuestionDTO);
        model.addAttribute("score", questionExamScoreDTO);
        model.addAttribute("id", questionId);

        return "teacher-descriptive-question";
    }

    @GetMapping("students-joined-exam/{examId}")
    public String showStudentsJoinedExam(@PathVariable Long examId, Model model) throws Exception {
        Exam exam = examService.findById(examId);
        if (!exam.getTeacher().getUserName().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new Exception("403 Forbidden!");
        }
        model.addAttribute("exam", exam);


        return "teacher-students-joined-exam";
    }

    @GetMapping("correct-exam/{examId}/{studentId}")
    public String correctExam(@PathVariable Long examId, @PathVariable Long studentId, Model model) throws Exception {
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
        model.addAttribute("questionExamScores", questionExamScores);
        model.addAttribute("studentQuestionScores", studentQuestionScores);
        model.addAttribute("exam", exam);
        model.addAttribute("student", student);
        model.addAttribute("multipleChoice", examService.findMultipleChoiceQuestions(exam));
        model.addAttribute("descriptive", examService.findDescriptiveQuestions(exam));
        model.addAttribute("sumStudent", sumStudent);
        model.addAttribute("sumExam", sumExam);

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
        for (int i = 0; i < descriptiveQuestions.size(); i++) {
            String input = request.getParameter(String.valueOf(i + multipleChoiceQuestions.size()));
            if (input != null && !input.equals("")) {
                int score = Integer.parseInt(input);
                List<QuestionExamScore> questionExamScores = descriptiveQuestions.get(i).getScores();
                studentQuestionScoreService.assignScore(questionExamScores, student, score);
            }
        }

        return "redirect:/teacher/correct-exam/" + exam.getId() + "/" + student.getId();
    }


//    @ExceptionHandler(Exception.class)
//    public String handle(Exception ex, Model model) {
//        model.addAttribute("message", ex.getMessage());
//        ex.printStackTrace();
//        return "user-exception";
//    }
//
//    @ExceptionHandler(UniqueException.class)
//    public String handle(UniqueException ex, Model model) {
//
//        model.addAttribute("message", ex.getMessage());
//        return "user-exception";
//    }
//
//    @ExceptionHandler(IllegalArgumentException.class)
//    public String handle(IllegalArgumentException ex, Model model) {
//
//        model.addAttribute("message", ex.getMessage());
//        return "user-exception";
//    }
}
