package cn.thoughtworks.school.services;

import cn.thoughtworks.school.entities.Stack;
import cn.thoughtworks.school.entities.StackCommand;
import cn.thoughtworks.school.repositories.StackCommandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author yywei
 **/
@Service
public class StackCommandService {
    @Autowired
    StackCommandRepository stackCommandRepository;

    public StackCommand getStackCommandByName(String name) {
        return stackCommandRepository.findByName(name);
    }

    public List<StackCommand> getAllStackCommand() {
        return stackCommandRepository.findAll();
    }

    public StackCommand save(StackCommand stackCommand) {
        return stackCommandRepository.save(stackCommand);
    }

    public void delete(Long id) {
        stackCommandRepository.deleteById(id);
    }
}
