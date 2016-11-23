package object.copy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.*;
import com.amazonaws.services.s3.model.*;

public class LowLevel_LargeObjectCopy {

    public static void main(String[] args) throws IOException {
        String sourceBucketName = "*** Source-Bucket-Name ***";
        String targetBucketName = "*** Target-Bucket-Name ***";
        String sourceObjectKey  = "*** Source-Object-Key ***";
        String targetObjectKey  = "*** Target-Object-Key ***";   
        AmazonS3Client s3Client = new AmazonS3Client(new
                PropertiesCredentials(
        				LowLevel_LargeObjectCopy.class.getResourceAsStream(
        						"AwsCredentials.properties")));    
        
        // List to store copy part responses.

        List<CopyPartResult> copyResponses =
                  new ArrayList<CopyPartResult>();
                          
        InitiateMultipartUploadRequest initiateRequest = 
        	new InitiateMultipartUploadRequest(targetBucketName, targetObjectKey);
        
        InitiateMultipartUploadResult initResult = 
        	s3Client.initiateMultipartUpload(initiateRequest);

        try {
            // Get object size.
            GetObjectMetadataRequest metadataRequest = 
            	new GetObjectMetadataRequest(sourceBucketName, sourceObjectKey);

            ObjectMetadata metadataResult = s3Client.getObjectMetadata(metadataRequest);
            long objectSize = metadataResult.getContentLength(); // in bytes

            // Copy parts.
            long partSize = 5 * (long)Math.pow(2.0, 20.0); // 5 MB

            long bytePosition = 0;
            for (int i = 1; bytePosition < objectSize; i++)
            {
            	CopyPartRequest copyRequest = new CopyPartRequest()
                   .withDestinationBucketName(targetBucketName)
                   .withDestinationKey(targetObjectKey)
                   .withSourceBucketName(sourceBucketName)
                   .withSourceKey(sourceObjectKey)
                   .withUploadId(initResult.getUploadId())
                   .withFirstByte(bytePosition)
                   .withLastByte(bytePosition + partSize -1 >= objectSize ? objectSize - 1 : bytePosition + partSize - 1) 
                   .withPartNumber(i);

                copyResponses.add(s3Client.copyPart(copyRequest));
                bytePosition += partSize;

            }
            CompleteMultipartUploadRequest completeRequest = new 
            	CompleteMultipartUploadRequest(
            			targetBucketName,
            			targetObjectKey,
            			initResult.getUploadId(),
            			GetETags(copyResponses));

            CompleteMultipartUploadResult completeUploadResponse =
                s3Client.completeMultipartUpload(completeRequest);
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        }
     }
     
    // Helper function that constructs ETags.
    static List<PartETag> GetETags(List<CopyPartResult> responses)
    {
        List<PartETag> etags = new ArrayList<PartETag>();
        for (CopyPartResult response : responses)
        {
            etags.add(new PartETag(response.getPartNumber(), response.getETag()));
        }
        return etags;
    }   
}