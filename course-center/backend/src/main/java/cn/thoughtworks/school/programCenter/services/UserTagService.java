package cn.thoughtworks.school.programCenter.services;

import cn.thoughtworks.school.programCenter.entities.Tag;
import cn.thoughtworks.school.programCenter.entities.UserTag;
import cn.thoughtworks.school.programCenter.repositories.TagRepository;
import cn.thoughtworks.school.programCenter.repositories.UserTagRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserTagService {
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private UserTagRepository userTagRepository;

    @Transactional
    public void editUserTags(Long programId, Long studentId, Long operatorId, List<String> tags) {
        List<UserTag> userTags = userTagRepository.findByProgramIdAndStudentId(programId, studentId);
        List<String> tagsName = userTags.stream().map(item -> item.getTag().getName()).collect(Collectors.toList());

        List<String> shouldAdd = tags.stream().filter(item -> !tagsName.contains(item)).collect(Collectors.toList());
        List<UserTag> shouldDelete = userTags.stream().filter(item -> !tags.contains(item.getTag().getName())).collect(Collectors.toList());

        userTagRepository.deleteAll(shouldDelete);
        userTagRepository.saveAll(
                shouldAdd.stream().map(item -> {
                    Tag tag = tagRepository.findByName(item).findFirst().orElse(new Tag(item, new Date(), operatorId));
                    return new UserTag(studentId, new Date(), operatorId, tag, programId);
                }).collect(Collectors.toList())
        );
    }
}
