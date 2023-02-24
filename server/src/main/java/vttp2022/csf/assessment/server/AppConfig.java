package vttp2022.csf.assessment.server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AppConfig {

    // AMAZON S3 CLOUD
    @Value("${spaces.secret.key}")
    private String spacesSecretKey;
    @Value("${spaces.access.key}")
    private String spacesAccessKey;
    @Value("${spaces.endpoint.url}")
    private String spacesEndpointUrl;
    @Value("${spaces.endpoint.region}")
    private String spacesEndpointRegion;

    @Bean
    public AmazonS3 createS3Client() {
        BasicAWSCredentials cred = new BasicAWSCredentials(spacesAccessKey, spacesSecretKey);
        EndpointConfiguration epConfig = new EndpointConfiguration(spacesEndpointUrl, spacesEndpointRegion);
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(epConfig)
                .withCredentials(new AWSStaticCredentialsProvider(cred))
                .build();
    }
}
