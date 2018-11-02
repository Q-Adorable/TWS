package cn.thoughtworks.school.programCenter.services;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Service
public class AmazonClientService {

  private AmazonS3 s3client;

  @Value("${amazonProperties.endpointUrl}")
  private String endpointUrl;
  @Value("${amazonProperties.picBucketName}")
  private String bucketName;
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

  private String generateFileName(MultipartFile multipartFile) {
    return new Date().getTime() + "-" + UUID.randomUUID() + "." +
            multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1);
  }
  private void uploadFileTos3bucket(String fileName, File file, String directory) {
    s3client.putObject(new PutObjectRequest(bucketName + "/" + directory, fileName, file)
      .withCannedAcl(CannedAccessControlList.PublicRead));
  }

  public String uploadFile(MultipartFile multipartFile, String directory) {

    String fileName = "";
    try {
      File file = convertMultiPartToFile(multipartFile);
      fileName = generateFileName(multipartFile);
      uploadFileTos3bucket(fileName, file, directory);
      file.delete();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return fileName;
  }
}
