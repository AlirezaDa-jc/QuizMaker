package ir.maktab.quizmaker.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

/**
 * @author Alireza.d.a
 */
public class UserDTO {
    @NotEmpty(message = "Role is mandatory")
    @Valid
    private String role;
    @NotEmpty(message = "UserName is mandatory")
    @Valid
    private String userName;

    private String password;
    private boolean isAllowed = false;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAllowed() {
        return isAllowed;
    }

    public void setAllowed(boolean allowed) {
        isAllowed = allowed;
    }
}
