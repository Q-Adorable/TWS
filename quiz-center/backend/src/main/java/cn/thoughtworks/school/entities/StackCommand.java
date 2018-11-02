package cn.thoughtworks.school.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author yywei
 **/
@Getter
@Setter
@Entity
@Table(name = "stackCommand")
public class StackCommand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String name;
    private String compile;
    private String execute;
    private String sourcePostfix;
    private String executePostfix;
    private String templateCode;
    private String testcase;
    private Date createTime;
    private Date updateTime;

    public StackCommand() {

    }

    public StackCommand(String name, String compile, String execute, String sourcePostfix, String executePostfix, String templateCode, String testcase) {
        this.name = name;
        this.compile = compile;
        this.execute = execute;
        this.sourcePostfix = sourcePostfix;
        this.executePostfix = executePostfix;
        this.templateCode = templateCode;
        this.testcase = testcase;
    }
}
