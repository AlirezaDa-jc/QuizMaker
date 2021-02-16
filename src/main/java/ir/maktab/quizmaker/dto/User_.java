package ir.maktab.quizmaker.dto;

import ir.maktab.quizmaker.domains.User;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(User.class)
public class User_ {
    public static final String USERNAME = "userName";
    public static final String PASSWORD = "password";
    public static final String IS_ALLOWED = "isAllowed";
    public static final String ROLE = "role";
    public static final String ID = "id";
    public static volatile SingularAttribute<User, String> username;
    public static volatile SingularAttribute<User, String> password;
    public static volatile SingularAttribute<User, String> isAllowed;
    public static volatile SingularAttribute<User, String> role;
    public static volatile SingularAttribute<User, String> id;

}
