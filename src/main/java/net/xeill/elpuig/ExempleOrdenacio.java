package net.xeill.elpuig;


import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Sorts.orderBy;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

import com.mongodb.client.MongoClient;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;

public class ExempleOrdenacio {

  public static void main(String[] args) {
    MongoDBConnectionFactory mdbConnectionFactory = MongoDBConnectionFactory.getInstance();
    MongoClient client = mdbConnectionFactory.connect();

    MongoDatabase db = client.getDatabase("exemples");
    MongoCollection<Document> coll = db.getCollection("punts");

    coll.drop();

    for (int i=0; i<100; i++) {
      int x = ThreadLocalRandom.current().nextInt(10);
      int y = ThreadLocalRandom.current().nextInt(10);
      int z = ThreadLocalRandom.current().nextInt(10);
      Document document = new Document("x",x).append("y", y).append("z", z);
      coll.insertOne(document);
    }

    Bson filter = eq("z", 5);
    Bson projection = fields(include("x","y"), excludeId());
    // Ordena primer per x de forma ascendent, i després per y de forma descendent.
    Bson sort = new Document("x", 1).append("y", -1);

    // db.punts.find({"z":{$eq:5}}, {"x":1,"y":1,"_id":0}).sort({"x":1,"y":-1})
    coll.find(filter).projection(projection).sort(sort).forEach((Consumer<? super Document>) doc -> System.out.println(doc.toJson()));
    System.out.println();

    // Retornem només 5 resultats
    // db.punts.find({"z":{$eq:5}}, {"x":1,"y":1,"_id":0}).sort({"x":1,"y":-1}).limit(5)
    coll.find(filter).projection(projection).sort(sort).limit(5).forEach((Consumer<? super Document>) doc -> System.out.println(doc.toJson()));
    System.out.println();

    // Retornem només 5 resultats i ens saltem els 3 primers resultats
    // db.punts.find({"z":{$eq:5}}, {"x":1,"y":1,"_id":0}).sort({"x":1,"y":-1}).skip(3).limit(5)
    coll.find(filter).projection(projection).sort(sort).skip(3).limit(5).forEach((Consumer<? super Document>) doc -> System.out.println(doc.toJson()));
    System.out.println();

    // Clàusula sort equivalent
    sort = Sorts.orderBy(Sorts.ascending("x"), Sorts.descending("y"));

    // I amb imports static:
    sort = orderBy(ascending("x"), descending("y"));

    // Si només volem ordenar per un camp:
    sort = ascending("x");

    // O per més d'un camp, però tots ascendents/descendents:
    sort = ascending("x","y");


    client.close();

  }

}
