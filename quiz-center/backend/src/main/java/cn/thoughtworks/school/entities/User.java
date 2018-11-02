package cn.thoughtworks.school.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class User {
    private String username;
    private List<Integer> roles;
    private Long id;
}
