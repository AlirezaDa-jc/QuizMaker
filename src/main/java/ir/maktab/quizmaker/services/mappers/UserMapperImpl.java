package ir.maktab.quizmaker.services.mappers;

import ir.maktab.quizmaker.domains.User;
import ir.maktab.quizmaker.dto.UserDTO;

/**
 * @author Alireza.d.a
 */
public class UserMapperImpl implements UserMapper {
    @Override
    public UserDTO sourceToDestination(User source) {
        if ( source == null ) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName( source.getUserName() );
        userDTO.setPassword( source.getPassword() );
        userDTO.setAllowed( source.isAllowed() );
        userDTO.setRole( source.getRole() );

        return userDTO;
    }

    @Override
    public User destinationToSource(UserDTO destination) {
        if ( destination == null ) {
            return null;
        }
        User user = new User();
        user.setUserName( destination.getUserName() );
        user.setPassword( destination.getPassword() );
        user.setAllowed( destination.isAllowed() );
        user.setRole( destination.getRole() );
        return user;
    }
}
