package ir.maktab.quizmaker.services;

import ir.maktab.quizmaker.domains.*;
import ir.maktab.quizmaker.repository.StudentQuestionScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        for(int i = 0; i < questions.size() ; i++){
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
            if(questions.get(i).getAnswer().equals(answers[i])){
                assert questionExamScore != null;
                studentQuestionScore.setScore(questionExamScore.getScore());
            }else{
                studentQuestionScore.setScore(0);
            }
            studentQuestionScore.setAnswer(answers[i]);
            studentQuestionScoreRepository.save(studentQuestionScore);
        }
    }
}
