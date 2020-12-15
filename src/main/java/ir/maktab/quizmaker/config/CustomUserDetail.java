package ir.maktab.quizmaker.config;


import ir.maktab.quizmaker.domains.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomUserDetail implements UserDetails {

    private final User user;

    public CustomUserDetail(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        String ROLE_PRE_FIX = "ROLE_";
        authorities.add(
                new SimpleGrantedAuthority(
                        ROLE_PRE_FIX + user.getRole()
                )
        );
        return authorities;
    }
/*
noop = Bcrypt
 */

    @Override
    public String getPassword() {
//        return "{noop}"+user.getPassword();
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
