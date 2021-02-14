package ir.maktab.quizmaker.services.mappers;

import ir.maktab.quizmaker.domains.QuestionExamScore;
import ir.maktab.quizmaker.dto.QuestionExamScoreDTO;

/**
 * @author Alireza.d.a
 */
public class QuestionExamScoreMapperImpl  implements QuestionExamScoreMapper {
    @Override
    public QuestionExamScoreDTO sourceToDestination(QuestionExamScore source) {
        if ( source == null ) {
            return null;
        }
        QuestionExamScoreDTO questionExamScoreDTO = new QuestionExamScoreDTO();
        questionExamScoreDTO.setQuestion(new QuestionMapperImpl().sourceToDestination(source.getQuestion()));
        questionExamScoreDTO.setScore(source.getScore());
        return questionExamScoreDTO;
    }

    @Override
    public QuestionExamScore destinationToSource(QuestionExamScoreDTO destination) {
        if ( destination == null ) {
            return null;
        }
        QuestionExamScore questionExamScore = new QuestionExamScore();
        questionExamScore.setQuestion(new QuestionMapperImpl().destinationToSource(destination.getQuestion()));

        questionExamScore.setScore(destination.getScore());
        return questionExamScore;
    }
}
