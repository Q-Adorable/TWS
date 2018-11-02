package cn.thoughtworks.school.controllers;

import cn.thoughtworks.school.annotations.Auth;
import cn.thoughtworks.school.entities.Tag;
import cn.thoughtworks.school.entities.User;
import cn.thoughtworks.school.handlers.BusinessException;
import cn.thoughtworks.school.repositories.TagRepository;
import cn.thoughtworks.school.services.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(value = "/api/v3/tags")
public class TagController {

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private Utils utils;

    @RequestMapping(value = "", method = RequestMethod.POST)
    @Transactional
    public ResponseEntity addTag(@RequestBody Map data, @Auth User current) throws BusinessException {
        Tag existTag = tagRepository
                .findTagByName((String) data.get("name"))
                .findFirst()
                .orElse(null);

        if(existTag != null) {
            throw new BusinessException(
                    String.format("Tag already with name: %s", data.get("name"))
            );
        }

        Tag tag = new Tag();
        tag.setMakerId(current.getId());
        tag.setReferenceNumber((long) 0);
        tag.setName((String) data.get("name"));
        tag.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        Tag newTag = tagRepository.save(tag);
        Map<String, Object> body = new HashMap<>();
        body.put("uri", "/api/v3/stacks/" + newTag.getId());
        body.put("id", newTag.getId());
        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity getTags(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "pageSize", defaultValue = "10") Integer size) {

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page - 1, size, sort);
        Page tagPage = tagRepository.findAll(pageable);
        Page result = utils.format(tagPage, pageable);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/{tagId}", method = RequestMethod.GET)
    public ResponseEntity getTag(@PathVariable Long tagId) throws BusinessException {
        Tag tag = tagRepository
                .findById(tagId)
                .orElseThrow(() -> new BusinessException(
                        String.format("Unknown tag with id: %s", tagId)
                ));

        return new ResponseEntity<>(tag, HttpStatus.OK);
    }

    @RequestMapping(value = "/{tagId}", method = RequestMethod.PUT)
    public ResponseEntity updateTag(@RequestBody Map data, @PathVariable Long tagId) throws BusinessException {
        Tag currentTag = tagRepository
                .findById(tagId)
                .orElseThrow(() -> new BusinessException(
                        String.format("Unknown tag with id: %s", tagId)
                ));

        currentTag.setName((String) data.get("name"));
        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        currentTag.setUpdateTime(simpleDateFormat.format(now));
        tagRepository.save(currentTag);
        return new ResponseEntity<>(currentTag, HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/{tagId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteTag(@PathVariable Long tagId) throws BusinessException {
        Tag currentTag = tagRepository
                .findById(tagId)
                .orElseThrow(() -> new BusinessException(
                        String.format("Unknown tag with id: %s", tagId)
                ));

        if (currentTag.getReferenceNumber() != 0) {
            throw new BusinessException("当前tag 不能删除");
        }
        tagRepository.deleteById(tagId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/topTenTags", method = RequestMethod.GET)
    ResponseEntity getTagsTopTen(@RequestParam(value = "name") String name) throws Exception {
        Pageable topTen = new PageRequest(0, 10);
        List<Tag> tags = tagRepository.getTagsByNameContaining(name, topTen);
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @RequestMapping(value = "/searchTags", method = RequestMethod.GET)
    ResponseEntity getSearchTags() throws Exception {
        List<Tag> tags = tagRepository.findAll();
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }
}
