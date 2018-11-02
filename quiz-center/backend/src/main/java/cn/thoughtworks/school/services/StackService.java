package cn.thoughtworks.school.services;


import cn.thoughtworks.school.entities.Stack;
import cn.thoughtworks.school.handlers.BusinessException;
import cn.thoughtworks.school.repositories.StackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StackService {
    @Autowired
    StackRepository stackRepository;

    public Stack addStack(Stack stack){
        return stackRepository.save(stack);
    }

    public void deleteStack(Long id) throws BusinessException {
        Stack stack = stackRepository.findById(id).orElseThrow(()->{
            String message = String.format("stack is not exist with id: %s", id);
            return new BusinessException(message);
        });
        stack.setAvailable(false);
        stackRepository.save(stack);
    }

    public void updateStack(Long id ,Stack stack) throws BusinessException{
        stack.setId(id);
        stackRepository.save(stack);
    }

    public Stack getStack(Long id) throws BusinessException{
        return stackRepository.findById(id).orElseThrow(()->{
            String message = String.format("stack is not exist with id: %s", id);
            return new BusinessException(message);
        });
    }
}
