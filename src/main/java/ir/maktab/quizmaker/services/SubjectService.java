package ir.maktab.quizmaker.services;

import ir.maktab.quizmaker.domains.Subject;
import ir.maktab.quizmaker.exception.UniqueException;
import ir.maktab.quizmaker.repository.SubjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author Alireza.d.a
 */
@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public Subject save(Subject subject) {
        if (subjectRepository.findByName(subject.getName()) == null)
            return subjectRepository.save(subject);
        throw new UniqueException("Subject Should Be Unique");
    }

    public List<Subject> findAll() {
        return subjectRepository.findAll();
    }

    public Subject findById(Long id) {
        return subjectRepository.findById(id).get();
    }

    public void deleteById(Long id) {
        subjectRepository.deleteById(id);
    }
}
