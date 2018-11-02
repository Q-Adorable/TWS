package cn.thoughtworks.school.services;

import org.springframework.web.multipart.MultipartFile;

public interface FileLoadService {
    String read(MultipartFile file);
}
