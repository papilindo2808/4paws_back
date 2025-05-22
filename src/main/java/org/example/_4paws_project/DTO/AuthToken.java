package org.example._4paws_project.DTO;

import org.example._4paws_project.models.User;

public class AuthToken {
    private String token;
    private User user;

    public AuthToken(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
