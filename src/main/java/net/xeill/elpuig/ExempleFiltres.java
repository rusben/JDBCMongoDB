package net.xeill.elpuig;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lt;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.client.MongoClient;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class ExempleFiltres {

  public static void main(String[] args) {
    MongoDBConnectionFactory mdbConnectionFactory = MongoDBConnectionFactory.getInstance();
    MongoClient client = mdbConnectionFactory.connect();

    MongoDatabase db = client.getDatabase("exemples");
    MongoCollection<Document> collection = db.getCollection("punts");

    collection.drop();

    for (int x=0; x<5; x++) {
      for (int y=0; y<5; y++) {
        collection.insertOne(new Document("x",x).append("y", y));
      }
    }

    // db.punts.find({x:3})
    Bson filter = new Document("x", 3);
    List<Document> result = collection.find(filter).into(new ArrayList<Document>());
    for (Document doc : result) {
      System.out.println(doc.toJson());
    }
    System.out.println();

    // db.punts.find({x:{$lt:3},y:{$gte:3}})
    // Sintaxi basada en documents
    filter = new Document("x", new Document("$lt", 3))
        .append("y", new Document("$gte", 3));
    result = collection.find(filter).into(new ArrayList<Document>());
    for (Document doc : result) {
      System.out.println(doc.toJson());
    }
    // Sintaxi basada en Filters
    filter = Filters.and(Filters.lt("x", 3), Filters.gte("y", 3));
    // Igual que l'anterior amb els mètodes de Filters importats de format estàtica
    filter = and(lt("x", 3), gte("y", 3));

    client.close();
  }

}
