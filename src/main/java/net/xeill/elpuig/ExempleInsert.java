package net.xeill.elpuig;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;

import com.mongodb.client.MongoClient;
import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class ExempleInsert {

  public static void main(String[] args) {
    MongoDBConnectionFactory mdbConnectionFactory = MongoDBConnectionFactory.getInstance();
    MongoClient client = mdbConnectionFactory.connect();

    MongoDatabase db = client.getDatabase("exemples");
    MongoCollection<Document> coll = db.getCollection("mascotes");

    coll.drop();

    Document document = new Document()
        .append("nom", "pere")
        .append("edat", 28)
        .append("interessos", Arrays.asList("bàsquet", "vídeojocs"))
        .append("telèfon", new Document("mòbil", "625121212")
            .append("fix", "931234567"))
        .append("actiu", true);

    LocalDate ld = LocalDate.of(2010, 04, 12);
    Date date = Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
    document.put("data_alta", date);
    coll.insertOne(document);

    document = new Document("nom", "Buffy")
        .append("edat", 3)
        .append("espècie", "gat");
    System.out.println(document.toJson());
    coll.insertOne(document);
    System.out.println(document.toJson());
    client.close();
  }
/*
> show databases
exemples  0.000GB
local     0.000GB
> use exemples
switched to db exemples
> show collections
mascotes
> db.mascotes.find()
{ "_id" : ObjectId("5695548fec24140c88a63bf1"), "nom" : "Buffy", "edat" : 3, "espècie" : "gat" }
>

 */
}
