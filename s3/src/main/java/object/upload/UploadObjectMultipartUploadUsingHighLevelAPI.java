package object.upload;

import java.io.File;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;

public class UploadObjectMultipartUploadUsingHighLevelAPI {

    public static void main(String[] args) throws Exception {
        String existingBucketName = "*** Provide existing bucket name ***";
        String keyName            = "*** Provide object key ***";
        String filePath           = "*** Path to and name of the file to upload ***";  
        
        TransferManager tm = new TransferManager(new ProfileCredentialsProvider());        
        System.out.println("Hello");
        // TransferManager processes all transfers asynchronously, 
        // so this call will return immediately.
        Upload upload = tm.upload(
                existingBucketName, keyName, new File(filePath));
        System.out.println("Hello2");

        try {
        	// Or you can block and wait for the upload to finish
        	upload.waitForCompletion();
        	System.out.println("Upload complete.");
        } catch (AmazonClientException amazonClientException) {
        	System.out.println("Unable to upload file, upload was aborted.");
        	amazonClientException.printStackTrace();
        }
    }
}