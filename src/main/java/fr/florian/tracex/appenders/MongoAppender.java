package fr.florian.tracex.appenders;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import fr.florian.tracex.enums.Priority;
import org.bson.Document;
import org.json.JSONObject;

import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MongoAppender {

    private static boolean start = false;
    private static String mongoUrl, databaseName, collectionName;
    private static int saveLevel = 0;

    private static MongoClient mongoClient;
    private static MongoDatabase mongoDatabase;
    private static MongoCollection mongoCollection;

    public static void setConfiguration(String mongoUrl, String databaseName, String collectionName) {
        MongoAppender.mongoUrl = mongoUrl;
        MongoAppender.databaseName = databaseName;
        MongoAppender.collectionName = collectionName;
    }

    public static void start() throws Exception {
        if (start) throw new Exception("your logger no sql has already started");
        if (mongoUrl == null || databaseName == null || collectionName == null)
            throw new Exception("you must configure the mongodb informations before launching the logger");
        disableMongoLogger();
        mongoClient = new MongoClient(new MongoClientURI(MongoAppender.mongoUrl));
        mongoDatabase = mongoClient.getDatabase(databaseName);
        mongoCollection = mongoDatabase.getCollection(collectionName);
        start = true;
    }

    public static void stop() throws Exception {
        if (!start) throw new Exception("logger no sql is already stopped");
        mongoClient.close();
        mongoDatabase = null;
        mongoCollection = null;
        start = false;
    }

    private static void disableMongoLogger() {
        Logger logger = Logger.getLogger("org.mongodb.driver");
        logger.setLevel(Level.OFF);
    }

    /*
     *      LOG
     */

    public static void addLog(ZonedDateTime zonedDateTime, Priority logType, Object message) throws Exception {
        if (logType.getLevel() < getSaveLevel()) return;
        if (start == false) throw new Exception("you must start the mongodb logger before sending some information");
        Document document = new Document();
        document.append("type", logType.getName());
        document.append("pid", ProcessHandle.current().pid());
        document.append("date", Date.from(zonedDateTime.toInstant()));
        if (message instanceof JSONObject) {
            document.append("message", Document.parse(message.toString()));
        } else {
            document.append("message", message.toString());
        }
        mongoCollection.insertOne(document);
    }

    public static void deleteAllLogs() {
        if (start) mongoCollection.deleteMany(new Document());
    }

    /*
     *      GETTER
     */

    public static boolean isStart() {
        return start;
    }

    public static String getMongoUrl() {
        return mongoUrl;
    }

    public static String getDatabaseName() {
        return databaseName;
    }

    public static String getCollectionName() {
        return collectionName;
    }

    public static MongoClient getMongoClient() {
        return mongoClient;
    }

    public static MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }

    public static MongoCollection getMongoCollection() {
        return mongoCollection;
    }

    public static int getSaveLevel() {
        return saveLevel;
    }

    public static void setSaveLevel(Priority logType) {
        MongoAppender.saveLevel = logType.getLevel();
    }

    public static void setSaveLevel(int saveLevel) {
        MongoAppender.saveLevel = saveLevel;
    }
}