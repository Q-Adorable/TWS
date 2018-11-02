package cn.thoughtworks.school.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Service
public class MultipartFileLoadService implements FileLoadService {

    @Override
    public String read(MultipartFile file) {
        String result = "";
        try {
            InputStream inputStream = file.getInputStream();
            result = new BufferedReader(new InputStreamReader(inputStream))
                    .lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;

    }
}
