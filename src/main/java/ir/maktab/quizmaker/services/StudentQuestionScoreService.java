package ir.maktab.quizmaker.services;

import ir.maktab.quizmaker.domains.MultipleChoiceQuestion;
import ir.maktab.quizmaker.domains.QuestionExamScore;
import ir.maktab.quizmaker.domains.Student;
import ir.maktab.quizmaker.domains.StudentQuestionScore;
import ir.maktab.quizmaker.dto.StudentQuestionScoreDTO;
import ir.maktab.quizmaker.repository.StudentQuestionScoreRepository;
import ir.maktab.quizmaker.services.mappers.StudentQuestionScoreMapperImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alireza.d.a
 */
@Service
public class StudentQuestionScoreService {

    private final StudentQuestionScoreRepository studentQuestionScoreRepository;


    public StudentQuestionScoreService(StudentQuestionScoreRepository studentQuestionScoreRepository) {
        this.studentQuestionScoreRepository = studentQuestionScoreRepository;

    }

/*
    Correct Answer For Template student-join-exam!
     */
//    public void correctAnswers(Exam exam, Student student, String[] answers) {
//        List<Question> questions = new LinkedList<>();
//        List<MultipleChoiceQuestion> multipleChoice = new LinkedList<>();
//        List<DescriptiveQuestion> decriptive = new LinkedList<>();
//        exam.getScores().forEach(c -> {
//            if (c.getQuestion() instanceof MultipleChoiceQuestion) {
//                multipleChoice.add((MultipleChoiceQuestion) c.getQuestion());
//            } else {
//                decriptive.add((DescriptiveQuestion) c.getQuestion());
//            }
//        });
//        questions.addAll(multipleChoice);
//        questions.addAll(decriptive);
//        for (int i = 0; i < questions.size(); i++) {
//            StudentQuestionScore studentQuestionScore = new StudentQuestionScore();
//            studentQuestionScore.setStudent(student);
//            QuestionExamScore questionExamScore = null;
//            for (QuestionExamScore examScore : questions.get(i).getScores()) {
//                if (examScore.getExam() == exam) {
//                    questionExamScore = examScore;
//                    break;
//                }
//            }
//            studentQuestionScore.setQuestionExamScore(questionExamScore);
//            if (questions.get(i).getAnswer().equals(answers[i])) {
//                assert questionExamScore != null;
//                studentQuestionScore.setScore(questionExamScore.getScore());
//            } else {
//                studentQuestionScore.setScore(0);
//            }
//            studentQuestionScore.setAnswer(answers[i]);
//            studentQuestionScoreRepository.save(studentQuestionScore);
//
//        }
//    }

    public List<StudentQuestionScore> findByQuestionExamScoresAndStudent(List<QuestionExamScore> questionExamScores, Student student) {
        List<StudentQuestionScore> studentQuestionScores = new ArrayList<>();
        for (QuestionExamScore questionExamScore : questionExamScores
        ) {
            StudentQuestionScore studentQuestionScore = studentQuestionScoreRepository.
                    findDistinctByQuestionExamScoreAndStudent(questionExamScore, student);
            studentQuestionScores.add(studentQuestionScore);
        }
        return studentQuestionScores;
    }

    public void assignScore(List<QuestionExamScore> questionExamScores, Student student, int score) {
        for (QuestionExamScore questionExamScore : questionExamScores) {
            StudentQuestionScore studentQuestionScore = studentQuestionScoreRepository.
                    findDistinctByQuestionExamScoreAndStudent(questionExamScore, student);
            if(score <= questionExamScore.getScore()) {
                studentQuestionScore.setScore(score);
                studentQuestionScoreRepository.save(studentQuestionScore);
            }
        }
    }


    public void correctAnswer(StudentQuestionScore studentQuestionScore) {
        QuestionExamScore questionExamScore = studentQuestionScore.getQuestionExamScore();
        if (questionExamScore.getQuestion() instanceof MultipleChoiceQuestion) {
            if (studentQuestionScore.getAnswer() != null) {
                if (studentQuestionScore.getAnswer().equals(questionExamScore.getQuestion().getAnswer())) {
                    studentQuestionScore.setScore(questionExamScore.getScore());
                }
            }
        } else {
            studentQuestionScore.setScore(Integer.MIN_VALUE);
        }
        studentQuestionScoreRepository.save(studentQuestionScore);

    }

    public StudentQuestionScore findByStudentAndQuestionExamScore(Student student, QuestionExamScore questionExamScore) {
        List<StudentQuestionScore> studentQuestionScores = studentQuestionScoreRepository.findAll().stream().filter(c -> c.getStudent().getId().equals(student.getId()) && c.getQuestionExamScore().getId().equals(questionExamScore.getId())).collect(Collectors.toList());
        return studentQuestionScores.get(0);
    }

    public void createStudentQuestionExamScores(Student student,List<QuestionExamScore> questionExamScores){
        List<StudentQuestionScore> studentQuestionScores = studentQuestionScoreRepository.findAll().stream().filter(c -> c.getStudent().getId().equals(student.getId()) && c.getQuestionExamScore().getId().equals(questionExamScores.get(0).getId())).collect(Collectors.toList());
        if(studentQuestionScores.size() == 0){
            for (QuestionExamScore questionExamScore:questionExamScores) {
                StudentQuestionScore studentQuestionScore = new StudentQuestionScore();
                studentQuestionScore.setStudent(student);
                studentQuestionScore.setQuestionExamScore(questionExamScore);
                studentQuestionScore.setScore(0);
                studentQuestionScoreRepository.save(studentQuestionScore);
            }
        }
    }

    public int getSumOfScores(List<StudentQuestionScore> studentQuestionScores) {
        return studentQuestionScores.stream().filter(c -> c != null &&c.getScore() >= 0).mapToInt(StudentQuestionScore::getScore).sum();
    }
    public StudentQuestionScoreDTO convertToDto(StudentQuestionScore studentQuestionScore){
        return new StudentQuestionScoreMapperImpl().sourceToDestination(studentQuestionScore);
    }

    public StudentQuestionScore convertToEntity(StudentQuestionScoreDTO studentQuestionScore) {
        return new StudentQuestionScoreMapperImpl().destinationToSource(studentQuestionScore);

    }
}
