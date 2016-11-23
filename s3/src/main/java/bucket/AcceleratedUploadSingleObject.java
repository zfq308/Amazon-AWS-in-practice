package bucket;

import java.io.File;
import java.io.IOException;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class AcceleratedUploadSingleObject {

    	private static String bucketName     = "*** Provide bucket name ***";
    private static String keyName        = "*** Provide key name ***";
    	private static String uploadFileName = "*** Provide file name with full path ***";
    	 	
    	public static void main(String[] args) throws IOException {
            AmazonS3 s3Client = new AmazonS3Client(new ProfileCredentialsProvider()); 
            s3Client.setRegion(Region.getRegion(Regions.US_WEST_2));
            
            // Use Amazon S3 Transfer Acceleration endpoint.           
            s3Client.setS3ClientOptions(S3ClientOptions.builder().setAccelerateModeEnabled(true).build());
            
            try {
            	System.out.println("Uploading a new object to S3 from a file\n");
                File file = new File(uploadFileName);
                s3Client.putObject(new PutObjectRequest(
                		                 bucketName, keyName, file));

             } catch (AmazonServiceException ase) {
                System.out.println("Caught an AmazonServiceException, which " +
                		"means your request made it " +
                        "to Amazon S3, but was rejected with an error response" +
                        " for some reason.");
                System.out.println("Error Message:    " + ase.getMessage());
                System.out.println("HTTP Status Code: " + ase.getStatusCode());
                System.out.println("AWS Error Code:   " + ase.getErrorCode());
                System.out.println("Error Type:       " + ase.getErrorType());
                System.out.println("Request ID:       " + ase.getRequestId());
            } catch (AmazonClientException ace) {
                System.out.println("Caught an AmazonClientException, which " +
                		"means the client encountered " +
                        "an internal error while trying to " +
                        "communicate with S3, " +
                        "such as not being able to access the network.");
                System.out.println("Error Message: " + ace.getMessage());
            }  
    }
}