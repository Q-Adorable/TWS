package cn.thoughtworks;

import cn.thoughtworks.school.programCenter.Application;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.Objects;

/**
 * @Author yywei
 **/
@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
@AutoConfigureStubRunner
@ActiveProfiles("test")
public class UserCenterServiceTest {

    private TestRestTemplate testRestTemplate;

    @Before
    public void setup() {
        testRestTemplate = new TestRestTemplate();
    }


    @Test
    public void test_getUserInfo() {
        String getUsersUrl = "http://localhost:10004/api/users/1";
        ResponseEntity<Map> result = testRestTemplate.getForEntity(getUsersUrl, Map.class);
        Map body = result.getBody();
        int statusCodeValue = result.getStatusCodeValue();
        Assert.assertEquals(200, statusCodeValue);
        String expected = "{\n" +
                "                          \"id\": 1,\n" +
                "                          \"userName\": \"zhang\",\n" +
                "                          \"email\": \"zhang@qq.com\",\n" +
                "                          \"mobilePhone\": \"12345678901\",\n" +
                "                          \"roles\": [2]\n" +
                "                  }";
        JSONAssert.assertEquals(expected, new JSONObject(body), false);
    }

    @Test
    public void test_getUsersByIds() {
        String getUsersByIdsUrl = "http://localhost:10004/api/users/ids/1,2";
        ResponseEntity<Object[]> result = testRestTemplate.getForEntity(getUsersByIdsUrl, Object[].class);
        int statusCodeValue = result.getStatusCodeValue();
        JSONArray jsonArray = new JSONArray(Objects.requireNonNull(result.getBody()));
        Assert.assertEquals(200, statusCodeValue);
        String expected = "[{\n" +
                "                          \"id\": 1,\n" +
                "                          \"username\": \"zhang\",\n" +
                "                          \"email\": \"zhang@qq.com\",\n" +
                "                          \"mobilePhone\": \"12345678901\",\n" +
                "                          \"roles\": [2]\n" +
                "                  },{\n" +
                "                          \"id\": 2,\n" +
                "                          \"username\": \"wang\",\n" +
                "                          \"email\": \"wang@qq.com\",\n" +
                "                          \"mobilePhone\": \"12345678902\",\n" +
                "                          \"roles\": [2,9]\n" +
                "                  }]";
        JSONAssert.assertEquals(expected, jsonArray, false);
    }

    @Test
    public void test_getUserByNameOrEmail() {
        String getUserByNameOrEmailUrl = "http://localhost:10004/api/users";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getUserByNameOrEmailUrl)
                .queryParam("nameOrEmail", "@qq");

        HttpEntity entity = new HttpEntity<>(headers);
        ResponseEntity<String> result = testRestTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class);

        int statusCodeValue = result.getStatusCodeValue();
        JSONArray jsonArray = new JSONArray(Objects.requireNonNull(result.getBody()));
        Assert.assertEquals(200, statusCodeValue);
        String expected = "[\n" +
                "                    {\n" +
                "                        \"id\": 1,\n" +
                "                        \"username\": \"zhang\",\n" +
                "                        \"email\": \"zhang@qq.com\",\n" +
                "                        \"mobilePhone\": \"12345678901\"\n" +
                "                    }\n" +
                "                  ]";
        JSONAssert.assertEquals(expected, jsonArray, false);
    }

    @Test
    public void test_getUserByName() {
        String getUserByNameUrl = "http://localhost:10004/api/users/username";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getUserByNameUrl)
                .queryParam("username", "zhang");

        HttpEntity entity = new HttpEntity<>(headers);

        ResponseEntity<Map> result = testRestTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                Map.class);

        int statusCodeValue = result.getStatusCodeValue();
        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(result.getBody()));
        Assert.assertEquals(200, statusCodeValue);
        String expected = "{\n" +
                "                          \"id\": 1,\n" +
                "                          \"username\": \"zhang\",\n" +
                "                          \"password\": \"12345678\",\n" +
                "                          \"email\": \"zhang@qq.com\",\n" +
                "                          \"mobilePhone\": \"12345678901\",\n" +
                "                          \"createDate\": \"2018-08-08\"\n" +
                "                  }";
        JSONAssert.assertEquals(expected, jsonObject, false);
    }

    @Test
    public void test_addTutorRole() {
        String getUserByNameUrl = "http://localhost:10004/api/users/roles";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", 1);
        jsonObject.put("role", 2);
        ResponseEntity<String> result = testRestTemplate.postForEntity(getUserByNameUrl, new HttpEntity<>(jsonObject.toString(), headers), String.class);
        int statusCodeValue = result.getStatusCodeValue();
        Assert.assertEquals(201, statusCodeValue);
    }

    @Test
    public void test_getUsersByUsernameOrEmail() {
//        String getUsersByUsernameOrEmailUrl = "http://localhost:10003/api/users/searches";
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json;charset=UTF-8");
//
//        ArrayList<String> arrayList = new ArrayList<String>();
//        arrayList.add("zhang");
//        arrayList.add("12314com");
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("usernameOrEmail", arrayList);
//        ResponseEntity<String> result = testRestTemplate.postForEntity(getUsersByUsernameOrEmailUrl, new HttpEntity<>(jsonObject.toString(), headers), String.class);
//        int statusCodeValue = result.getStatusCodeValue();
//        String body = result.getBody();
//        JSONArray jsonArray = new JSONArray(body);
//        String json = "{\"usernameOrEmail\":[\"zhang\",\"1234com\"]}";
//        Object read = JsonPath.parse(json).read("$.usernameOrEmail[?(@.[*] =~ /(\\w+))/]");
//        $.usernameOrEmail[?(@.[*] =~ /(\w+)/)]
    }

    // 调用的getUsersByIds方法，该方法已经在test_getUsersByIds测过
    // 归根结底，这个方法应该属于业务层，不应该是服务调用层
    @Test
    public void test_getUsers() {

    }

}
