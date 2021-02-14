package ir.maktab.quizmaker.dto;

import ir.maktab.quizmaker.domains.Student;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Student.class)
public class Student_ {
    public static final String USERNAME = "userName";
    public static final String PASSWORD = "password";
    public static final String IS_ALLOWED = "isAllowed";
    public static final String ROLE = "role";
    public static final String ID = "id";
    public static final String STUDENTCODE = "studentCode";
    public static final String FIRSTNAME = "firstName";
    public static final String LASTNAME = "lastName";
    public static final String NATIONALCODE = "nationalCode";
    public static volatile SingularAttribute<Student, String> userName;
    public static volatile SingularAttribute<Student, String> password;
    public static volatile SingularAttribute<Student, String> isAllowed;
    public static volatile SingularAttribute<Student, String> role;
    public static volatile SingularAttribute<Student, String> id;
    public static volatile SingularAttribute<Student, String> studentCode;
    public static volatile SingularAttribute<Student, String> nationalCode;
    public static volatile SingularAttribute<Student, String> firstName;
    public static volatile SingularAttribute<Student, String> lastName;
}
