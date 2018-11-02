package cn.thoughtworks.school.programCenter.entities;

import java.util.List;

public class User {
    private String username;
    private List<Integer> roles;
    private Long id;

    public User(String username, Long id) {
        this.username = username;
        this.id = id;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Integer> getRoles() {
        return roles;
    }

    public void setRoles(List<Integer> roles) {
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
