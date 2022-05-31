
package pers.zcc.scm.common.util;

import org.bson.BsonDocumentWrapper;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 * The Class DataSourceUtil.
 * @author zhangchangchun
 * @Date 2022年5月31日
 */
public class DataSourceUtil {

    private static final MongoClient mongoClient;

    static {
        String connectionString = "mongodb://docker:mongopw@localhost:49156";
        mongoClient = MongoClients.create(connectionString);
    }

    public static MongoClient getMongoClient() {
        return mongoClient;
    }

    public static void closeMongoClient() {
        mongoClient.close();
    }

    public static void main(String[] args) {
        MongoDatabase db = getMongoClient().getDatabase("test");
        db.getCollection("user").insertOne(Document.parse("{\"name\":\"tutu\",\"age\":12,\"score\":78}"));
        System.out.println(db.getCollection("user").countDocuments());
        for (Document doc : db.getCollection("user").find()) {
            System.out.println(doc);
        }
        Bson filter = BsonDocumentWrapper.parse("{\"age\":12}");
        db.getCollection("user").deleteOne(filter);
        closeMongoClient();
    }
}
