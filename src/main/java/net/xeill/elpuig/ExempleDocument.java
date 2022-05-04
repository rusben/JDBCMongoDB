package net.xeill.elpuig;

import org.bson.Document;
import org.bson.json.JsonWriterSettings;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;

public class ExempleDocument {
  public static void main(String[] args) {
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

    JsonWriterSettings settings = JsonWriterSettings.builder().indent(true).build();
    System.out.println(document.toJson(settings));

    Date d = document.get("data_alta", Date.class);
    System.out.println(d);
  }
}
