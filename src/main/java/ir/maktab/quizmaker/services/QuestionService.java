package ir.maktab.quizmaker.services;

import ir.maktab.quizmaker.domains.*;
import ir.maktab.quizmaker.dto.DescriptiveQuestionDTO;
import ir.maktab.quizmaker.dto.MultipleChoiceQuestionDTO;
import ir.maktab.quizmaker.dto.QuestionDTO;
import ir.maktab.quizmaker.repository.QuestionRepository;
import ir.maktab.quizmaker.services.mappers.DescriptiveQuestionMapperImpl;
import ir.maktab.quizmaker.services.mappers.MultipleChoiceQuestionMapperImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Alireza.d.a
 */
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    private final DescriptiveQuestionService descriptiveQuestionService;

    private final MultipleChoiceQuestionService multipleChoiceQuestionService;

    private final QuestionExamScoreService questionExamScoreService;


    public QuestionService(QuestionRepository questionRepository, DescriptiveQuestionService descriptiveQuestionService, MultipleChoiceQuestionService multipleChoiceQuestionService, QuestionExamScoreService questionExamScoreService) {
        this.questionRepository = questionRepository;
        this.descriptiveQuestionService = descriptiveQuestionService;
        this.multipleChoiceQuestionService = multipleChoiceQuestionService;
        this.questionExamScoreService = questionExamScoreService;
    }

    private List<Question> bankQuestions;
    private int size;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Question findById(Long questionId) {
        return questionRepository.findById(questionId).isPresent() ? questionRepository.findById(questionId).get() : null;
    }

    public Question save(Question question) {
        if (question instanceof DescriptiveQuestion) {
            return descriptiveQuestionService.save((DescriptiveQuestion) question);
        } else {
            return multipleChoiceQuestionService.save((MultipleChoiceQuestion) question);
        }
    }

    public Question create(Question question, Exam exam, QuestionExamScore questionExamScore) {
        if (questionRepository.findAll().contains(question))
            questionRepository.deleteById(question.getId());
        questionExamScore.setQuestion(question);
        questionExamScore.setExam(exam);
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

    public List<Question> getQuestionBank(Exam exam, int page) {
        if (page == 1) {
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
                        c.getScores()
                                .forEach(x -> exams.add(x.getExam()));
                        return !exams.contains(exam);
                    })
                    .filter(Question::getPublic)
                    .collect(Collectors.toSet());


            Set<Question> bank = new HashSet<>();
            bank.addAll(subjectQuestions);
            bank.addAll(teacherQuestions);
            size = bank.size();
            bankQuestions = new ArrayList<>(bank);
        }
        List<Question> questions = new ArrayList<>();
        for (int j = ((page - 1) * 10); j < page * 10 && j < bankQuestions.size(); j++) {
            questions.add(bankQuestions.get(j));
        }

        return questions;
    }

    public List<MultipleChoiceQuestion> findMultipleChoiceQuestions(List<Question> questions) {
        return questions.stream()
                .filter(c -> c instanceof MultipleChoiceQuestion)
                .map(c -> (MultipleChoiceQuestion) c)
                .collect(Collectors.toList());
    }

    public List<DescriptiveQuestion> findDescriptiveQuestions(List<Question> questions) {
        return questions.stream()
                .filter(c -> c instanceof DescriptiveQuestion)
                .map(c -> (DescriptiveQuestion) c)
                .collect(Collectors.toList());
    }

    public void deleteAllOption() {
        questionRepository.findAll().forEach(multipleChoiceQuestion -> {
            if (multipleChoiceQuestion instanceof MultipleChoiceQuestion) {
                MultipleChoiceQuestion question = (MultipleChoiceQuestion) multipleChoiceQuestion;
                question.setOptions(new ArrayList<>());
                questionRepository.save(question);
            }
        });
    }

    public QuestionDTO convertToDto(Question question) {
        if (question instanceof DescriptiveQuestion)
            return new DescriptiveQuestionMapperImpl().sourceToDestination((DescriptiveQuestion) question);
        return new MultipleChoiceQuestionMapperImpl().sourceToDestination((MultipleChoiceQuestion) question);


    }

    public Question convertToEntity(QuestionDTO questionDTO) {
        if (questionDTO instanceof DescriptiveQuestionDTO)
            return new DescriptiveQuestionMapperImpl().destinationToSource((DescriptiveQuestionDTO) questionDTO);
        return new MultipleChoiceQuestionMapperImpl().destinationToSource((MultipleChoiceQuestionDTO) questionDTO);

    }
}
