package net.xeill.elpuig;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExempleConnexio {

  public static void main(String[] args) {
    String user = "admin";
    String database = "admin";
    char[] password = "password".toCharArray();

    MongoCredential credential = MongoCredential.createCredential(user,
        database, password);

    MongoClient mongoClient = MongoClients.create(
        MongoClientSettings.builder()
            .applyToClusterSettings(builder ->
                builder.hosts(Arrays.asList(new ServerAddress("192.168.249.108", 27017))))
            .credential(credential)
            .build());

    MongoDatabase db = mongoClient.getDatabase("exemples");
    MongoCollection<Document> coll = db.getCollection("zips");

    List<Document> all = coll.find().into(new ArrayList<Document>());
    for (Document doc : all) {
      System.out.println(doc.toJson());
    }

    mongoClient.close();
  }
}
