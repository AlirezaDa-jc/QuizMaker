package ir.maktab.quizmaker.services.mappers;

import ir.maktab.quizmaker.domains.StudentQuestionScore;
import ir.maktab.quizmaker.dto.StudentQuestionScoreDTO;

/**
 * @author Alireza.d.a
 */
public class StudentQuestionScoreMapperImpl implements StudentQuestionScoreMapper{
    @Override
    public StudentQuestionScoreDTO sourceToDestination(StudentQuestionScore source) {
        if ( source == null ) {
            return null;
        }
        StudentQuestionScoreDTO studentQuestionScoreDTO = new StudentQuestionScoreDTO();
        studentQuestionScoreDTO.setScore(source.getScore());
        studentQuestionScoreDTO.setQuestionExamScore(new QuestionExamScoreMapperImpl().sourceToDestination(source.getQuestionExamScore()));
        studentQuestionScoreDTO.setAnswer(source.getAnswer());
        studentQuestionScoreDTO.setStudent(new StudentMapperImpl().sourceToDestination(source.getStudent()));

        return studentQuestionScoreDTO;
    }

    @Override
    public StudentQuestionScore destinationToSource(StudentQuestionScoreDTO destination) {
        if ( destination == null ) {
            return null;
        }
        StudentQuestionScore studentQuestionScore = new StudentQuestionScore();
        studentQuestionScore.setScore(destination.getScore());
        studentQuestionScore.setQuestionExamScore(new QuestionExamScoreMapperImpl().destinationToSource(destination.getQuestionExamScore()));
        studentQuestionScore.setAnswer(destination.getAnswer());
        studentQuestionScore.setStudent(new StudentMapperImpl().destinationToSource(destination.getStudent()));

        return studentQuestionScore;
    }
}
