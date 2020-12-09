package ir.maktab.quizmaker.services;

import ir.maktab.quizmaker.domains.Subject;
import ir.maktab.quizmaker.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author Alireza.d.a
 */
@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    public Subject save(Subject subject){
        return subjectRepository.save(subject);
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
