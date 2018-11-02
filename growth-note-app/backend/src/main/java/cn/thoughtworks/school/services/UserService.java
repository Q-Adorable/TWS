package cn.thoughtworks.school.services;

import cn.thoughtworks.school.entities.Comment;
import cn.thoughtworks.school.entities.PractiseDiary;
import cn.thoughtworks.school.feign.UserCenterFeign;
import cn.thoughtworks.school.repositories.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserCenterFeign userCenterFeign;

    public List attachComments(List<PractiseDiary> practiseDiaries) {
        List practiseAndCommentMaps = new ArrayList();
        for (PractiseDiary practiseDiary : practiseDiaries) {
            List<Comment> comments = commentRepository.findByPractiseDiaryId(practiseDiary.getId());
            List commentsInfo = getCommonInfo(comments);
            Map<String, Object> practiseAndCommentMap = new HashMap();
            practiseAndCommentMap.put("practiseDiary", practiseDiary);
            practiseAndCommentMap.put("comments", commentsInfo);
            practiseAndCommentMaps.add(practiseAndCommentMap);
        }

        return practiseAndCommentMaps;
    }

    public List getCommonInfo(List<Comment> comments) {
        ArrayList commentsInfo = new ArrayList();
        List<Long> commentAuthorIds = comments.stream().map(Comment::getCommentAuthorId).collect(Collectors.toList());
        List<Map> users = getUsersByIds(commentAuthorIds);
        comments.forEach(comment -> {
            HashMap<String, Object> commentInfo = new HashMap();
            Map commonUserInfo = users.stream().filter(item ->
                    Objects.equals(Long.valueOf(item.get("id").toString()), comment.getCommentAuthorId()))
                    .findFirst()
                    .orElse(new HashMap());
            commentInfo.put("commentAuthorInfo", commonUserInfo);
            commentInfo.put("comment", comment);
            commentsInfo.add(commentInfo);
        });

        return commentsInfo;
    }

    public List getUsersByIds(List<Long> userIds) {
        if ( userIds.isEmpty()) {
            return new ArrayList<>();
        }
        String ids = String.join(",", userIds.stream().map(Object::toString).collect(Collectors.toList()));
        return userCenterFeign.getUsersByIds(ids);
    }

    public Map getDiaryAuthorInfo(Long userId) {
        return userCenterFeign.getUserById(userId);
    }

    public List getUsersLikeUsername(String username) {
        return userCenterFeign.getUsersLikeUsername(username);
    }

    public List<Map> getUserByUserNameOrEmail(String nameOrEmail) {
        return userCenterFeign.getUserByUserNameOrEmail(nameOrEmail);
    }
}
