package ir.maktab.quizmaker.services;

import ir.maktab.quizmaker.domains.*;
import ir.maktab.quizmaker.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Alireza.d.a
 */
@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private DescriptiveQuestionService descriptiveQuestionService;

    @Autowired
    private MultipleChoiceQuestionService multipleChoiceQuestionService;

    @Autowired
    private QuestionExamScoreService questionExamScoreService;

    public Question findById(Long questionId) {
        return questionRepository.findById(questionId).get();
    }

    public Question save(Question question) {
        if (question instanceof DescriptiveQuestion) {
            return descriptiveQuestionService.save((DescriptiveQuestion) question);
        } else {
            return multipleChoiceQuestionService.save((MultipleChoiceQuestion) question);
        }
    }

    public Question create(Question question, Exam exam, QuestionExamScore questionExamScore) {
        Subject subject = exam.getCourse().getSubject();
        if (questionRepository.findAll().contains(question))
            questionRepository.deleteById(question.getId());
        question.addSubject(subject);
        questionExamScore.setQuestion(question);
        if (question instanceof MultipleChoiceQuestion) {
            MultipleChoiceQuestion multipleChoiceQuestion = multipleChoiceQuestionService.save((MultipleChoiceQuestion) question);
            questionExamScoreService.save(questionExamScore);
            return multipleChoiceQuestion;
        } else {
            DescriptiveQuestion descriptiveQuestion = descriptiveQuestionService.save((DescriptiveQuestion) question);
            questionExamScoreService.save(questionExamScore);
            return descriptiveQuestion;
        }
    }

    public Set<Question> getQuestionBank(Exam exam) {
        Set<Question> teacherQuestions = exam.getTeacher().getQuestions()
                .stream()
                .filter(c -> c.getSubjects().contains(exam.getCourse().getSubject()))
                .filter(c -> {
                    Set<Exam> exams = new HashSet<>();
                    c.getScores().forEach(x -> exams.add(x.getExam()));
                    return !exams.contains(exam);
                })
                .collect(Collectors.toSet());

        Set<Question> subjectQuestions = exam.getCourse()
                .getSubject()
                .getQuestions()
                .stream()
                .filter(c -> {
                    Set<Exam> exams = new HashSet<>();
                    c.getScores().forEach(x -> exams.add(x.getExam()));
                    return !exams.contains(exam);
                })
                .collect(Collectors.toSet());

        Set<Question> bank = new HashSet<>();
        bank.addAll(subjectQuestions);
        bank.addAll(teacherQuestions);
        return bank;
    }

    public List<MultipleChoiceQuestion> findMultipleChoiceQuestions(Set<Question> questions) {
        return questions.stream()
                .filter(c -> c instanceof MultipleChoiceQuestion)
                .map(c -> (MultipleChoiceQuestion) c)
                .collect(Collectors.toList());
    }

    public List<DescriptiveQuestion>findDescriptiveQuestions(Set<Question> questions) {
        return questions.stream()
                .filter(c -> c instanceof DescriptiveQuestion)
                .map(c -> (DescriptiveQuestion) c)
                .collect(Collectors.toList());
    }

    public void deleteAllOption(){
        questionRepository.findAll().forEach(multipleChoiceQuestion -> {
            if(multipleChoiceQuestion instanceof MultipleChoiceQuestion) {
                MultipleChoiceQuestion question = (MultipleChoiceQuestion) multipleChoiceQuestion;
                List<String> options = question.getOptions();
                options.removeAll(Collections.singleton(""));
                question.setOptions(options);
                questionRepository.save(question);
            }
        });
    }
}
