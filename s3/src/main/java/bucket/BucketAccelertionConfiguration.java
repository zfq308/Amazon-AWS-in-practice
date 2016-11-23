package bucket;

import java.io.IOException;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.BucketAccelerateConfiguration;
import com.amazonaws.services.s3.model.BucketAccelerateStatus;
import com.amazonaws.services.s3.model.GetBucketAccelerateConfigurationRequest;
import com.amazonaws.services.s3.model.SetBucketAccelerateConfigurationRequest;

public class BucketAccelertionConfiguration {

    public static String bucketName = "*** Provide bucket name ***"; 
    public static AmazonS3Client s3Client;
    
    public static void main(String[] args) throws IOException {
       
        s3Client = new AmazonS3Client(new ProfileCredentialsProvider());
        s3Client.setRegion(Region.getRegion(Regions.US_WEST_2));
        
		// 1. Enable bucket for Amazon S3 Transfer Acceleration.
        s3Client.setBucketAccelerateConfiguration(new SetBucketAccelerateConfigurationRequest(bucketName,
                new BucketAccelerateConfiguration(BucketAccelerateStatus.Enabled)));
      		
        // 2. Get the acceleration status of the bucket.
        String accelerateStatus = s3Client.getBucketAccelerateConfiguration(new GetBucketAccelerateConfigurationRequest(bucketName)).getStatus();
    
        System.out.println("Acceleration status = " + accelerateStatus);
            
    }
}