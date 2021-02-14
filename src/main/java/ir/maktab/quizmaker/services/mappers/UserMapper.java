package ir.maktab.quizmaker.services.mappers;

import ir.maktab.quizmaker.domains.User;
import ir.maktab.quizmaker.dto.UserDTO;
import org.mapstruct.Mapper;

/**
 * @author Alireza.d.a
 */
@Mapper
public interface UserMapper {
    UserDTO sourceToDestination(User source);
    User destinationToSource(UserDTO destination);
}
