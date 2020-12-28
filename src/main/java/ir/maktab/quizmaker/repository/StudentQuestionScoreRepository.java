package ir.maktab.quizmaker.repository;

import ir.maktab.quizmaker.domains.QuestionExamScore;
import ir.maktab.quizmaker.domains.Student;
import ir.maktab.quizmaker.domains.StudentQuestionScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Alireza.d.a
 */
@Repository
public interface StudentQuestionScoreRepository extends JpaRepository<StudentQuestionScore,Long> {
    List<StudentQuestionScore> findAllByQuestionExamScoreAndStudent(QuestionExamScore questionExamScores, Student student);

    StudentQuestionScore findDistinctByQuestionExamScoreAndStudent(QuestionExamScore questionExamScore, Student student);
}
