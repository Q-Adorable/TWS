package cn.thoughtworks.school.services;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.Date;
import java.util.UUID;

@Service
public class AmazonClientService {

    private AmazonS3 s3client;

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;
    @Value("${amazonProperties.bucketName}")
    private String bucketName;
    @Value("${amazonProperties.picBucketName}")
    private String picBucketName;
    @Value("${amazonProperties.accessKey}")
    private String accessKey;
    @Value("${amazonProperties.secretKey}")
    private String secretKey;

    @PostConstruct
    private void initializeAmazon() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3client = AmazonS3ClientBuilder.standard().withRegion(Regions.CN_NORTH_1).withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + UUID.randomUUID() + "." +
                multiPart.getOriginalFilename().substring(multiPart.getOriginalFilename().lastIndexOf(".") + 1);
    }

    private void uploadFileTos3bucket(String fileName, File file, String directory, String rootPath) {
        s3client.putObject(new PutObjectRequest(rootPath + "/" + directory, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    public String uploadFile(MultipartFile multipartFile, String directory) {

        String fileName = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            fileName = generateFileName(multipartFile);
            uploadFileTos3bucket(fileName, file, directory, bucketName);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }

    public String uploadPic(MultipartFile multipartFile, String directory) {

        String fileName = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            fileName = generateFileName(multipartFile);
            uploadFileTos3bucket(fileName, file, directory, picBucketName);

            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }

    public String readFile(String fileName, String directory) throws IOException {

        S3Object object = s3client.getObject(
                new GetObjectRequest(bucketName + "/" + directory, fileName));
        InputStream objectData = object.getObjectContent();
        return IOUtils.toString(objectData, "utf-8");
    }


    public File getFile(String fileName, String directory) {

        S3Object object = s3client.getObject(
                new GetObjectRequest(bucketName + "/" + directory, fileName));
        InputStream objectData = object.getObjectContent();
        OutputStream outputStream = null;
        File fileTemp = new File(".");
        String temp = fileTemp.getAbsolutePath();

        String path = temp + "/backend/src/main/resources/static/" + fileName;

        File file = new File(path);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try {
            outputStream = new FileOutputStream(file);
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = objectData.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }
    public InputStream getInputStream(String fileName, String directory) throws IOException {

        S3Object object = s3client.getObject(
                new GetObjectRequest(bucketName + "/" + directory, fileName));
        InputStream objectData = object.getObjectContent();
        return objectData;

    }
}
