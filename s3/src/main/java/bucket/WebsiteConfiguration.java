package bucket;

import java.io.IOException;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.BucketWebsiteConfiguration;



public class WebsiteConfiguration {
    private static String bucketName = "*** bucket name ***";
	private static String indexDoc   = "*** index document name ***";
	private static String errorDoc   = "*** error document name ***";
	
	public static void main(String[] args) throws IOException {
        AmazonS3 s3Client = new AmazonS3Client(new ProfileCredentialsProvider());
   
        try {
        	// Get existing website configuration, if any.
            getWebsiteConfig(s3Client);
    		
    		// Set new website configuration.
    		s3Client.setBucketWebsiteConfiguration(bucketName, 
    		   new BucketWebsiteConfiguration(indexDoc, errorDoc));
    		
    		// Verify (Get website configuration again).
            getWebsiteConfig(s3Client);
            
            // Delete
            s3Client.deleteBucketWebsiteConfiguration(bucketName);

       		// Verify (Get website configuration again)
              getWebsiteConfig(s3Client);
            
  
            
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which" +
            		" means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means"+
            		" the client encountered " +
                    "a serious internal problem while trying to " +
                    "communicate with Amazon S3, " +
                    "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }

	private static BucketWebsiteConfiguration getWebsiteConfig(
	                                              AmazonS3 s3Client) {
		System.out.println("Get website config");   
		
		// 1. Get website config.
		BucketWebsiteConfiguration bucketWebsiteConfiguration = 
			  s3Client.getBucketWebsiteConfiguration(bucketName);
		if (bucketWebsiteConfiguration == null)
		{
			System.out.println("No website config.");
		}
		else
		{
		     System.out.println("Index doc:" + 
		       bucketWebsiteConfiguration.getIndexDocumentSuffix());
		     System.out.println("Error doc:" + 
		       bucketWebsiteConfiguration.getErrorDocument());
		}
		return bucketWebsiteConfiguration;
	}
}