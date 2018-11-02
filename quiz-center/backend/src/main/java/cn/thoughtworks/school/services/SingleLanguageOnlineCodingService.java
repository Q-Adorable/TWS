package cn.thoughtworks.school.services;

import cn.thoughtworks.school.entities.OnlineLanguageQuiz;
import cn.thoughtworks.school.handlers.BusinessException;
import cn.thoughtworks.school.repositories.OnlineLanguageQuizRepository;
import org.apache.commons.io.FileUtils;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @Author yywei
 **/
@Service
public class SingleLanguageOnlineCodingService {

    @Autowired
    OnlineLanguageQuizRepository onlineLanguageQuizRepository;

    public OnlineLanguageQuiz getOnlineLanguageQuiz(Long id) throws BusinessException {
        return onlineLanguageQuizRepository.findById(id).orElseThrow(() ->
                new BusinessException(String.format("Unknown basicQuiz with id: %s", id)));
    }


    public String parseResourceFile(String template, String fileName) throws IOException {
        String filename = String.format(template, fileName);
        File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + filename);
        return FileUtils.readFileToString(file, "utf-8");
    }


    /**
     * hard code
     * need improve
     * -.-
     */
    private static final String jsonSchema = "{\n" +
            "  \"$schema\": \"http://json-schema.org/draft-06/schema#\",\n" +
            "  \"type\": \"array\",\n" +
            "  \"items\": {\n" +
            "    \"$ref\": \"#/definitions/WelcomeElement\"\n" +
            "  },\n" +
            "  \"definitions\": {\n" +
            "    \"WelcomeElement\": {\n" +
            "      \"type\": \"object\",\n" +
            "      \"additionalProperties\": false,\n" +
            "      \"properties\": {\n" +
            "        \"input\": {\n" +
            "          \"type\": \"string\"\n" +
            "        },\n" +
            "        \"expectedOutput\": {\n" +
            "          \"type\": \"string\"\n" +
            "        }\n" +
            "      },\n" +
            "      \"required\": [\n" +
            "        \"expectedOutput\",\n" +
            "        \"input\"\n" +
            "      ],\n" +
            "      \"title\": \"WelcomeElement\"\n" +
            "    }\n" +
            "  }\n" +
            "}\n";

    public void Jsonerification(String json) {
//        File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "online_language_testcase_schema.json");
//        JSONObject rawSchema = new JSONObject(new JSONTokener(FileUtils.openInputStream(file)));
        JSONObject rawSchema = new JSONObject(jsonSchema);
        SchemaLoader loader = SchemaLoader.builder().schemaJson(rawSchema).draftV6Support().build();
        Schema schema = loader.load().build();
        schema.validate(new JSONArray(json));// throws a ValidationException if this object is invalid
        //
    }

    public Page<OnlineLanguageQuiz> getRemarkLike(String remark, Pageable pageable) {
        Page<OnlineLanguageQuiz> onlineLanguageQuizs = onlineLanguageQuizRepository.
                findByRemarkLikeAndIsAvailable(remark, true,pageable);
        return onlineLanguageQuizs;
    }

    public Page<OnlineLanguageQuiz> getDescriptionLike(String description, Pageable pageable) {
        Page<OnlineLanguageQuiz> onlineLanguageQuizs = onlineLanguageQuizRepository.
                findByDescriptionLikeAndIsAvailable(description,true, pageable);
        return onlineLanguageQuizs;
    }

}
