package ir.maktab.quizmaker.aspect;


import ir.maktab.quizmaker.domains.Course;
import ir.maktab.quizmaker.services.CourseService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TeacherAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CourseService courseService;

    @Pointcut("execution(* ir.maktab.quizmaker.services.TeacherService.signUp(..))")
    private void signUpTeacher() {
    }

    @Pointcut("execution(* ir.maktab.quizmaker.controller.TeacherController.*(..))")
    private void checkAuthorizationPoint() {
    }
//    @Before("selectAll()")
//    public void beforeAdvice(){
//        System.out.println("Going to setup student profile.");
//    }

    @Before(value = "@annotation(Authentication)")
    public void checkAuthorization(JoinPoint point) {
        logger.info(point.getSignature().getName() + " called...");
        Long arg = (Long) point.getArgs()[0];
        Course byId = courseService.findById(arg);

        if(!SecurityContextHolder.getContext().getAuthentication().getName().equals(byId.getTeacher().getUserName())){
            throw new BadCredentialsException("incorrect password!!!");
        }
    }

    @Before("signUpTeacher()")         //point-cut expression
    public void beforeSignUp(JoinPoint point) {
        logger.info(point.getSignature().getName() + " called...");
    }

    //    @After("execution(* ir.maktab.quizmaker.services.TeacherService.signUp(..))")         //point-cut expression
    @After("signUpTeacher()")
    public void afterSignUp(JoinPoint point) {
//        logger.info(point.getSignature().getName() + " called..."); Why it doesnt call it twice?
        logger.info(point.toShortString());
        Object target = point.getArgs()[0];
        logger.warn(target.toString());
    }




}
