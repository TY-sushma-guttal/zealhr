package com.te.zealhr.aws.config;
/*
 * package com.te.zealhr.aws.config;
 * 
 * import javax.sql.DataSource;
 * 
 * import org.springframework.beans.factory.annotation.Value; import
 * org.springframework.boot.jdbc.DataSourceBuilder; import
 * org.springframework.context.annotation.Bean; import
 * org.springframework.context.annotation.Configuration; import
 * org.springframework.data.mongodb.MongoDatabaseFactory; import
 * org.springframework.data.mongodb.core.MongoTemplate; import
 * org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
 * 
 * import com.amazonaws.auth.AWSStaticCredentialsProvider; import
 * com.amazonaws.auth.BasicAWSCredentials; import
 * com.amazonaws.services.secretsmanager.AWSSecretsManager; import
 * com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder; import
 * com.amazonaws.services.secretsmanager.model.GetSecretValueRequest; import
 * com.amazonaws.services.secretsmanager.model.GetSecretValueResult; import
 * com.google.gson.Gson; import com.mongodb.MongoClientSettings; import
 * com.mongodb.MongoCredential; import com.mongodb.client.MongoClients; import
 * com.te.zealhr.exception.DataNotFoundException;
 * 
 * import lombok.extern.slf4j.Slf4j;
 * 
 * @Configuration
 * 
 * @Slf4j public class ApplicationConfig {
 * 
 * @Value("${amazonProperties.accessKey}") private String accessKey;
 * 
 * @Value("${amazonProperties.secretKey}") private String accessSecret;
 * 
 * private Gson gson = new Gson();
 * 
 * @Bean public DataSource dataSource() {
 * 
 * AwsSecrets secrets = getSecret(); return DataSourceBuilder.create()
 * .url("jdbc:" + secrets.getEngine() + "://" + secrets.getHost() + ":" +
 * secrets.getPort() + "/db_zealhr")
 * .username(secrets.getUsername()).password(secrets.getPassword()).build(); }
 * 
 * private AwsSecrets getSecret() { String secretName = "Prod-RDS-DB-Secret";
 * String region = "ap-south-1";
 * 
 * AWSSecretsManager client =
 * AWSSecretsManagerClientBuilder.standard().withRegion(region)
 * .withCredentials(new AWSStaticCredentialsProvider(new
 * BasicAWSCredentials(accessKey, accessSecret))) .build();
 * 
 * String secret; GetSecretValueRequest getSecretValueRequest = new
 * GetSecretValueRequest().withSecretId(secretName); GetSecretValueResult
 * getSecretValueResult = null;
 * 
 * try { getSecretValueResult = client.getSecretValue(getSecretValueRequest); }
 * catch (Exception exception) { log.info(exception.getMessage()); throw new
 * DataNotFoundException(exception.getMessage()); } if
 * (getSecretValueResult.getSecretString() != null) { secret =
 * getSecretValueResult.getSecretString(); return gson.fromJson(secret,
 * AwsSecrets.class); } return AwsSecrets.builder().build(); }
 * 
 * @Bean public MongoClientSettings mongoClientSettings() { String secretName =
 * "Prod-MongoDB-Secret"; String region = "ap-south-1";
 * 
 * AWSSecretsManager client =
 * AWSSecretsManagerClientBuilder.standard().withRegion(region)
 * .withCredentials(new AWSStaticCredentialsProvider(new
 * BasicAWSCredentials(accessKey, accessSecret))) .build();
 * 
 * String secret; GetSecretValueRequest getSecretValueRequest = new
 * GetSecretValueRequest().withSecretId(secretName); GetSecretValueResult
 * getSecretValueResult = null;
 * 
 * try { getSecretValueResult = client.getSecretValue(getSecretValueRequest); }
 * catch (Exception exception) { log.info(exception.getMessage()); throw new
 * DataNotFoundException(exception.getMessage()); } if
 * (getSecretValueResult.getSecretString() != null) { secret =
 * getSecretValueResult.getSecretString(); AwsSecretsMongo awsSecrets =
 * gson.fromJson(secret, AwsSecretsMongo.class); return
 * MongoClientSettings.builder()
 * .credential(MongoCredential.createCredential(awsSecrets.getUsername(),
 * awsSecrets.getDatabase() == null ? "db_zealhr" : awsSecrets.getDatabase(),
 * awsSecrets.getPassword().toCharArray())) .build(); } return null; }
 * 
 * @Bean public MongoDatabaseFactory mongoDatabaseFactory(MongoClientSettings
 * mongoClientSettings) { return new
 * SimpleMongoClientDatabaseFactory(MongoClients.create(mongoClientSettings),
 * "db_zealhr"); }
 * 
 * @Bean public MongoTemplate mongoTemplate(MongoDatabaseFactory
 * mongoDatabaseFactory) { return new MongoTemplate(mongoDatabaseFactory); } }
 */