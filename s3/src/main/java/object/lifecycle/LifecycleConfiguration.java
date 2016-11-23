package object.lifecycle;

import java.io.IOException;
import java.util.Arrays;

public class LifecycleConfiguration {
    public static String bucketName = "*** Provide bucket name ***"; 
    public static AmazonS3Client s3Client;

    public static void main(String[] args) throws IOException {
        s3Client = new AmazonS3Client(new ProfileCredentialsProvider());
        try {
            
            BucketLifecycleConfiguration.Rule rule1 = 
            new BucketLifecycleConfiguration.Rule()
            .withId("Archive immediately rule")
            .withPrefix("glacierobjects/")
            .addTransition(new Transition()
                                .withDays(0)
                                .withStorageClass(StorageClass.Glacier))
            .withStatus(BucketLifecycleConfiguration.ENABLED.toString());

            BucketLifecycleConfiguration.Rule rule2 = 
                new BucketLifecycleConfiguration.Rule()
                .withId("Archive and then delete rule")
                .withPrefix("projectdocs/")
                .addTransition(new Transition()
                                      .withDays(30)
                                      .withStorageClass(StorageClass.StandardInfrequentAccess))
                .addTransition(new Transition()
                                      .withDays(365)
                                      .withStorageClass(StorageClass.Glacier))
                .withExpirationInDays(3650)
                .withStatus(BucketLifecycleConfiguration.ENABLED.toString());

            BucketLifecycleConfiguration configuration = 
            new BucketLifecycleConfiguration()
            .withRules(Arrays.asList(rule1, rule2));
            
            // Save configuration.
            s3Client.setBucketLifecycleConfiguration(bucketName, configuration);
            
            // Retrieve configuration.
            configuration = s3Client.getBucketLifecycleConfiguration(bucketName);
            
            // Add a new rule.
            configuration.getRules().add(
                    new BucketLifecycleConfiguration.Rule()
                        .withId("NewRule")
                        .withPrefix("YearlyDocuments/")
                        .withExpirationInDays(3650)
                        .withStatus(BucketLifecycleConfiguration.
                            ENABLED.toString())
                       );
            // Save configuration.
            s3Client.setBucketLifecycleConfiguration(bucketName, configuration);
            
            // Retrieve configuration.
            configuration = s3Client.getBucketLifecycleConfiguration(bucketName);
            
            // Verify there are now three rules.
            configuration = s3Client.getBucketLifecycleConfiguration(bucketName);
            System.out.format("Expected # of rules = 3; found: %s\n", 
                configuration.getRules().size());

            System.out.println("Deleting lifecycle configuration. Next, we verify deletion.");
            // Delete configuration.
            s3Client.deleteBucketLifecycleConfiguration(bucketName);
            
            // Retrieve nonexistent configuration.
            configuration = s3Client.getBucketLifecycleConfiguration(bucketName);
            String s = (configuration == null) ? "No configuration found." : "Configuration found.";
            System.out.println(s);


        } catch (AmazonS3Exception amazonS3Exception) {
            System.out.format("An Amazon S3 error occurred. Exception: %s", amazonS3Exception.toString());
        } catch (Exception ex) {
            System.out.format("Exception: %s", ex.toString());
        }        
    }
}