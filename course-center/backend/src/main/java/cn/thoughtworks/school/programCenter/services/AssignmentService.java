package cn.thoughtworks.school.programCenter.services;

import cn.thoughtworks.school.programCenter.entities.*;
import cn.thoughtworks.school.programCenter.repositories.QuizSuggestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AssignmentService {
    @Autowired
    private QuizSuggestionRepository quizSuggestionRepository;
    @Autowired
    private UserCenterService userCenterService;

    public String getAssignmentStatus(Assignment assignment, List<ReviewQuiz> submitAssignments) {
        List<AssignmentQuiz> selectedQuizzes = assignment.getSelectedQuizzes();
        if (isUnfinished(assignment.getType(), selectedQuizzes, submitAssignments)) {
            return "unfinished";
        }
        if (isReviewing(assignment.getType(), submitAssignments)) {
            return "reviewing";
        }
        if (isFinished(assignment.getType(), submitAssignments)) {
            return "finished";
        }
        return "excellent";
    }

    private boolean isFinished(String type, List<ReviewQuiz> submitAssignments) {
        if (Objects.equals(type, "BASIC_TYPE")) {
            return submitAssignments.get(0).getStatus().equals("已完成");
        }
        return submitAssignments.stream().anyMatch(item -> Objects.equals(item.getStatus(), "已完成"));
    }

    private boolean isReviewing(String type, List<ReviewQuiz> submitAssignments) {
        if (Objects.equals(type, "BASIC_TYPE")) {
            return submitAssignments.get(0).getStatus().equals("已提交");
        }

        return submitAssignments.stream().anyMatch(item -> Objects.equals(item.getStatus(), "已提交"));
    }

    private boolean isUnfinished(String type, List<AssignmentQuiz> selectedQuizzes, List<ReviewQuiz> submitAssignments) {
        if (Objects.equals(type, "BASIC_QUIZ")) {
            return submitAssignments.size() != 1;
        }
        return selectedQuizzes.size() != submitAssignments.size() || selectedQuizzes.size() == 0;

    }

    public List<QuizSuggestion> getQuizSuggestions(Long studentId, Long assignmentId, Long quizId) {
        List<QuizSuggestion> suggestions = quizSuggestionRepository.findByAssignmentIdAndQuizIdAndStudentId(assignmentId, quizId, studentId);
        List<Map> users = getSuggestionsUsers(suggestions);
        suggestions.forEach(suggestion->{
            suggestion.setFromUser(findSuggestionUser(users, suggestion.getFromUserId()));
            suggestion.setToUser(findSuggestionUser(users, suggestion.getToUserId()));
        });

        return suggestions;
    }

    private User findSuggestionUser(List<Map> users, Long fromUserId) {
        Map foundUser = users.stream().filter(user -> Objects.equals(Long.parseLong(user.get("id").toString()), fromUserId))
                .findFirst().orElse(new HashMap());
        Object userName = Objects.isNull(foundUser.get("name")) ? foundUser.get("username") : foundUser.get("name");
        Long id = Long.parseLong(foundUser.get("id").toString());

        return new User(Objects.isNull(userName) ? "" : userName.toString(), id);
    }

    private List<Map> getSuggestionsUsers(List<QuizSuggestion> suggestions) {
        List<Long> userIdsArr = new ArrayList<>();
        suggestions.forEach(suggestion->{
            userIdsArr.add(suggestion.getFromUserId());
            userIdsArr.add(suggestion.getToUserId());
        });

        String userIds = Arrays.toString(userIdsArr.stream().distinct().collect(Collectors.toList()).toArray())
                .replace("[","").replace("]","").replace(" ","");
        return userCenterService.getUsersByIds(userIds);
    }
}
