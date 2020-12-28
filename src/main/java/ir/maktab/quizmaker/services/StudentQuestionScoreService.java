package ir.maktab.quizmaker.services;

import ir.maktab.quizmaker.domains.*;
import ir.maktab.quizmaker.repository.StudentQuestionScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Alireza.d.a
 */
@Service
public class StudentQuestionScoreService {
    @Autowired
    private StudentQuestionScoreRepository studentQuestionScoreRepository;

    public void correctAnswers(Exam exam, Student student, String[] answers) {
        List<Question> questions = new LinkedList<>();
        exam.getScores().forEach(c -> questions.add(c.getQuestion()));
        for (int i = 0; i < questions.size(); i++) {
            StudentQuestionScore studentQuestionScore = new StudentQuestionScore();
            studentQuestionScore.setStudent(student);
            QuestionExamScore questionExamScore = null;
            for (QuestionExamScore examScore : questions.get(i).getScores()) {
                if (examScore.getExam() == exam) {
                    questionExamScore = examScore;
                    break;
                }
            }
            studentQuestionScore.setQuestionExamScore(questionExamScore);
            if (questions.get(i).getAnswer().equals(answers[i])) {
                assert questionExamScore != null;
                studentQuestionScore.setScore(questionExamScore.getScore());
            } else {
                studentQuestionScore.setScore(0);
            }
            studentQuestionScore.setAnswer(answers[i]);
            studentQuestionScoreRepository.save(studentQuestionScore);
        }
    }

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
        for (QuestionExamScore questionExamScore : questionExamScores
        ) {
            StudentQuestionScore studentQuestionScore = studentQuestionScoreRepository.
                    findDistinctByQuestionExamScoreAndStudent(questionExamScore, student);
            studentQuestionScore.setScore(score);
            studentQuestionScoreRepository.save(studentQuestionScore);
        }
    }
}
