package net.xeill.elpuig;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

public class MongoDBConnectionFactory {
  private String dbname;
  private String host;
  private int port;
  private String user;
  private String password;
  private static MongoDBConnectionFactory instance;

  public MongoDBConnectionFactory() {
    init();
  }

  public static MongoDBConnectionFactory getInstance() {
    if (instance == null) {
      instance = new MongoDBConnectionFactory();
    }
    return instance;
  }

  public MongoClient connect() {
    MongoCredential credential = MongoCredential.createCredential(this.user,
        this.dbname, this.password.toCharArray());

    MongoClient mongoClient = MongoClients.create(
        MongoClientSettings.builder()
            .applyToClusterSettings(builder ->
                builder.hosts(Arrays.asList(new ServerAddress(this.host, this.port))))
            .credential(credential)
            .build());

    return mongoClient;
  }

  public void init() {
    Properties prop = new Properties();
    InputStream propStream = this.getClass().getClassLoader().getResourceAsStream("db.properties");

    try {
      prop.load(propStream);
      this.host = prop.getProperty("host");
      this.port = Integer.parseInt(prop.getProperty("port"));
      this.user = prop.getProperty("user");
      this.password = prop.getProperty("password");
      this.dbname = prop.getProperty("dbname");
    } catch (IOException e) {
      String message = "ERROR: db.properties file could not be found";
      System.err.println(message);
      throw new RuntimeException(message, e);
    }
  }
}
