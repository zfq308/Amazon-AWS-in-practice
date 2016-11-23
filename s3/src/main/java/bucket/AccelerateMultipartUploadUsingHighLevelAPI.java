package bucket;

import java.io.File;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;

import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;

public class AccelerateMultipartUploadUsingHighLevelAPI {
 
    private static String EXISTING_BUCKET_NAME = "*** Provide bucket name ***";
    private static String KEY_NAME  = "*** Provide key name ***";
    private static String FILE_PATH = "*** Provide file name with full path ***";

    public static void main(String[] args) throws Exception {
        
        AmazonS3Client s3Client = new AmazonS3Client(new ProfileCredentialsProvider());
        s3Client.configureRegion(Regions.US_WEST_2);
           
        // Use Amazon S3 Transfer Acceleration endpoint.           
        s3Client.setS3ClientOptions(S3ClientOptions.builder().setAccelerateModeEnabled(true).build());
       
    	TransferManager tm = new TransferManager(s3Client);        
        System.out.println("TransferManager");
        // TransferManager processes all transfers asynchronously, 
        // so this call will return immediately.
        Upload upload = tm.upload(
        		EXISTING_BUCKET_NAME, KEY_NAME, new File(FILE_PATH));
        System.out.println("Upload");

        try {
        	// Or you can block and wait for the upload to finish
        	upload.waitForCompletion();
        	System.out.println("Upload complete");
        } catch (AmazonClientException amazonClientException) {
        	System.out.println("Unable to upload file, upload was aborted.");
        	amazonClientException.printStackTrace();
        }
    }
}