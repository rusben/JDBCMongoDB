package net.xeill.elpuig;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

import com.mongodb.client.MongoClient;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;

public class ExempleProjection {

  public static void main(String[] args) {
    MongoDBConnectionFactory mdbConnectionFactory = MongoDBConnectionFactory.getInstance();
    MongoClient client = mdbConnectionFactory.connect();

    MongoDatabase db = client.getDatabase("exemples");
    MongoCollection<Document> coll = db.getCollection("punts");

    coll.drop();

    for (int i=0; i<100; i++) {
      int x = ThreadLocalRandom.current().nextInt(100);
      int y = ThreadLocalRandom.current().nextInt(100);
      int z = ThreadLocalRandom.current().nextInt(10);
      Document document = new Document("x",x).append("y", y).append("z", z);
      coll.insertOne(document);
    }

    Bson filter = and(gt("x", 50), eq("z", 5));
    // Retorna tots els elements, excepte que els s'indiqui amb un 0.
    // db.punts.find({$and : [{"x" : {$gt:50}}, {"z" : {$eq:5}}]}, {"x":0,"_id":0})
    Bson projection = new Document("x", 0).append("_id", 0);
    coll.find(filter).projection(projection).forEach((Consumer<? super Document>) (Document doc) -> System.out.println(doc.toJson()));
    System.out.println();

    // Retorna només els elements indicats amb un 1, i l'_id.
    // db.punts.find({$and : [{"x" : {$gt:50}}, {"z" : {$eq:5}}]}, {"x":1})
    projection = new Document("x",1);
    coll.find(filter).projection(projection).forEach((Consumer<? super Document>) (Document doc) -> System.out.println(doc.toJson()));
    System.out.println();

    // Retorna només els elements indicats amb un 1.
    // db.punts.find({$and : [{"x" : {$gt:50}}, {"z" : {$eq:5}}]}, {"y":1,"_id":0})
    projection = new Document("y", 1).append("_id", 0);
    coll.find(filter).projection(projection).forEach((Consumer<? super Document>) (Document doc) -> System.out.println(doc.toJson()));
    System.out.println();

    // Equivalent al primer exemple
    projection = Projections.exclude("x","_id");
    coll.find(filter).projection(projection).forEach((Consumer<? super Document>) (Document doc) -> System.out.println(doc.toJson()));
    System.out.println();

    // Equivalent al segon exemple
    projection = Projections.include("x");
    coll.find(filter).projection(projection).forEach((Consumer<? super Document>) (Document doc) -> System.out.println(doc.toJson()));
    System.out.println();

    // Equivalent al tercer exemple
    projection = Projections.fields(Projections.include("y"), Projections.excludeId());
    coll.find(filter).projection(projection).forEach((Consumer<? super Document>) (Document doc) -> System.out.println(doc.toJson()));
    System.out.println();

    client.close();
  }

}
