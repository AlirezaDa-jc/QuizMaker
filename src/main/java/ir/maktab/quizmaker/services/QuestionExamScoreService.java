package ir.maktab.quizmaker.services;

import ir.maktab.quizmaker.domains.Exam;
import ir.maktab.quizmaker.domains.Question;
import ir.maktab.quizmaker.domains.QuestionExamScore;
import ir.maktab.quizmaker.repository.QuestionExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Alireza.d.a
 */
@Service
public class QuestionExamScoreService {
    @Autowired
    private QuestionExamRepository questionExamRepository;
//TODO BANK!
    public QuestionExamScore save(QuestionExamScore questionExamScore){
        return questionExamRepository.save(questionExamScore);
    }

    public QuestionExamScore create(Exam exam) {
        QuestionExamScore questionExamScore = new QuestionExamScore();
        questionExamScore.setExam(exam);
        questionExamScore = questionExamRepository.save(questionExamScore);
        return questionExamScore;
    }

    public QuestionExamScore create(Exam exam, Question question) {
        QuestionExamScore questionExamScore = new QuestionExamScore();
        questionExamScore.setExam(exam);
        questionExamScore.setQuestion(question);
        questionExamScore = questionExamRepository.save(questionExamScore);
        return questionExamScore;
    }
}
