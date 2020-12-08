package ir.maktab.quizmaker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    private final CustomAuthenticationProvider authenticationProvider;

    public ProjectConfig(CustomAuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
        return new CustomUrlAuthenticationSuccessHandler();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {



        http.httpBasic();

        http
                .formLogin()
                .loginPage("/menu")
                .permitAll()
                .successHandler(myAuthenticationSuccessHandler())
                .failureUrl("/login_error")
                .and()
                .logout()
                .logoutSuccessUrl("/menu");



        http.authorizeRequests()
                .mvcMatchers("/admin/**").hasRole("ADMIN")
                .mvcMatchers("/teacher/**").hasRole("TEACHER")
                .mvcMatchers("/student/**").hasRole("STUDENT")
                .mvcMatchers("/resources/**").permitAll()
                .anyRequest().permitAll();

        http.csrf().disable();
    }

}
