package ir.maktab.quizmaker.services;

import ir.maktab.quizmaker.domains.Subject;
import ir.maktab.quizmaker.dto.SubjectDTO;
import ir.maktab.quizmaker.repository.SubjectRepository;
import ir.maktab.quizmaker.services.mappers.SubjectMapperImpl;
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

    public SubjectDTO convertToDto(Subject subject) {
        return new SubjectMapperImpl().sourceToDestination(subject);
    }

    public Subject convertToEntity(SubjectDTO subjectDTO) {

        Subject subject = new SubjectMapperImpl().destinationToSource(subjectDTO);
        if (subjectRepository.findByName(subject.getName()) != null) {
            subject.setId(subjectRepository.findByName(subject.getName())
                    .getId());
        }
        return subject;
    }

    public Subject findByName(String subjectName) {
        return subjectRepository.findByName(subjectName);
    }
}
