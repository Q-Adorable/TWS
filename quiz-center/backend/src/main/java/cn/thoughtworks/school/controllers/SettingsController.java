package cn.thoughtworks.school.controllers;

import cn.thoughtworks.school.handlers.BusinessException;
import cn.thoughtworks.school.services.AmazonClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping(value = "/api")
public class SettingsController {

    @Value("${appContextPath}")
    private String appContextPath;
    @Autowired
    private AmazonClientService amazonClientService;
    @Value("${amazonProperties.picBucketName}")
    private String bucketName;
    @Value("${amazonProperties.rootPath}")
    private String rootPath;
    @Value("${amazonProperties.image-directory}")
    private String imageDirectory;
    @Value("${amazonProperties.temp-image-directory}")
    private String tempImageDirectory;

    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    public Map<String, String> getSettings() {
        Map<String, String> result = new HashMap<>();

        result.put("appContextPath", appContextPath);
        return result;
    }

    @RequestMapping(value = "/uploadImages",method = RequestMethod.POST)
    public ResponseEntity upload(@RequestParam("file") MultipartFile file) throws BusinessException {
        String directory = imageDirectory;
        if (!Objects.equals("/learn", appContextPath)) {
            directory = tempImageDirectory;
        }
        String imageName = amazonClientService.uploadPic(file, directory);
        if (Objects.isNull(imageName)) {
            throw new BusinessException("Image upload failed.");
        }
        String fullPath = rootPath + "/" + bucketName + "/" + directory + "/" + imageName;

        return new ResponseEntity(fullPath,HttpStatus.OK);
    }

}
