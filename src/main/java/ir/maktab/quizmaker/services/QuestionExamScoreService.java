package ir.maktab.quizmaker.services;

import ir.maktab.quizmaker.domains.Exam;
import ir.maktab.quizmaker.domains.MultipleChoiceQuestion;
import ir.maktab.quizmaker.domains.Question;
import ir.maktab.quizmaker.domains.QuestionExamScore;
import ir.maktab.quizmaker.dto.QuestionExamScoreDTO;
import ir.maktab.quizmaker.repository.QuestionExamRepository;
import ir.maktab.quizmaker.services.mappers.ExamMapperImpl;
import ir.maktab.quizmaker.services.mappers.QuestionExamScoreMapperImpl;
import ir.maktab.quizmaker.services.mappers.QuestionMapperImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alireza.d.a
 */
@Service
public class QuestionExamScoreService {

    private final QuestionExamRepository questionExamRepository;


    public QuestionExamScoreService(QuestionExamRepository questionExamRepository) {
        this.questionExamRepository = questionExamRepository;

    }

    public QuestionExamScore save(QuestionExamScore questionExamScore) {
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

    public QuestionExamScore findByExamAndQuestion(Exam exam, Question question) {
        return questionExamRepository.findDistinctByExamAndQuestion(exam, question);
    }


    public List<QuestionExamScore> getScores(Exam exam) {
        List<QuestionExamScore> scores = new ArrayList<>();
        List<QuestionExamScore> multipleChoice = new ArrayList<>();
        List<QuestionExamScore> descriptive = new ArrayList<>();
        exam.getScores().forEach(c -> {
            if (c.getQuestion() instanceof MultipleChoiceQuestion) {
                multipleChoice.add(c);
            } else {
                descriptive.add(c);
            }
        });
        scores.addAll(multipleChoice);
        scores.addAll(descriptive);
        return scores;
    }

    public QuestionExamScoreDTO convertToDto(QuestionExamScore questionExamScore) {
        QuestionExamScoreDTO questionExamScoreDTO = new QuestionExamScoreMapperImpl().sourceToDestination(questionExamScore);
        questionExamScoreDTO.setExam(new ExamMapperImpl().sourceToDestination(questionExamScore.getExam()));
        if (questionExamScore.getQuestion() != null)
            questionExamScore.setQuestion(new QuestionMapperImpl().destinationToSource(questionExamScoreDTO.getQuestion()));
        return questionExamScoreDTO;
    }

    public QuestionExamScore convertToEntity(QuestionExamScoreDTO questionExamScoreDTO) {
        QuestionExamScore questionExamScore = new QuestionExamScoreMapperImpl().destinationToSource(questionExamScoreDTO);
        if(questionExamScoreDTO.getQuestion() != null)
        questionExamScore.setQuestion(new QuestionMapperImpl().destinationToSource(questionExamScoreDTO.getQuestion()));
        return questionExamScore;

    }
}
