package DAL;

import DataObjects.Comment;
import DataObjects.Post;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Filters.or;
import static com.mongodb.client.model.Filters.regex;
import java.util.ArrayList;
import java.util.List;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.types.ObjectId;

public class MongoConnection implements IConnection
{
    private static String ipAddress = "136.63.18.225";
    private static String port = "27444";
    private static String userID = "cs470";
    private static String password = "cs470lnw";
    private static String databaseName = "cs470blog";
    private static String URI =
            "mongodb://" + userID +":" + password 
            + "@" + ipAddress + ":" + port + "/" + databaseName;

    private static MongoClient mongoClient;
    private static MongoDatabase db;
    private static MongoCollection<Document> posts;

    MongoConnection()
    {
        try
        {
            System.out.println("Opening MongoDB connection...");

            mongoClient = new MongoClient(new MongoClientURI(URI));
            db = mongoClient.getDatabase(databaseName);
            posts = db.getCollection("posts");

            System.out.println("Connected to MongoDB");
        }
        catch (Exception ex)
        {
            System.out.println("Failure connecting to MongoDB");
            System.out.println(ex.toString());
        }
    }

    public void Close()
    {
        try
        {
            System.out.println("Closing MongoDB connection...");

            posts = null;
            db = null;
            mongoClient.close();

            System.out.println("Closed MongoDB connection");
        }
        catch (Exception ex)
        {
            System.out.println("Failure closing MongoDB connection");
            System.out.println(ex.toString());
        }
    }
    
    public Post GetPost(String pID)
    {
        Document document = posts.find(eq("_id", new ObjectId(pID))).first();
        Post post = new Post();
        post.pTitle = document.getString("title");
        post.pDate = document.getString("date");
        post.aName = document.getString("author");
        post.pID = document.getObjectId("_id").toString();
        post.pText = document.getString("text");
        post.tags = (List<String>) document.get("tags");
        return post;
    }
    
    public List<Comment> GetComments(String pID)
    {
        Document document = posts.find(eq("_id", new ObjectId(pID))).first();
        List<Comment> comments = new ArrayList();
        List<Document> commentsArray = (List<Document>) document.get("comments");
        for (Document c : commentsArray)
        {
            Comment comment = new Comment();
            comment.aName = c.getString("cAuthor");
            comment.cDate = c.getString("cDate");
            comment.cText = c.getString("cText");
            comments.add(comment);
        }
        
        return comments;
    }
    
    public List<Post> GetRecentPosts(int count)
    {
        FindIterable<Document> documents = posts.find().sort(new Document("date", -1)).limit(count);
        List<Post> searchResults = new ArrayList();
        for (Document document : documents)
        {
            Post post = new Post();
            post.pTitle = document.getString("title");
            post.pDate = document.getString("date");
            post.aName = document.getString("author");
            post.pID = document.getObjectId("_id").toString();
            post.pText = document.getString("text");
            post.tags = (List<String>) document.get("tags");
            searchResults.add(post);
        }
        return searchResults;
    }
    
    public List<Post> GetPostsByAuthor(String aName)
    {
        FindIterable<Document> documents = posts.find(eq("author", aName));
        List<Post> searchResults = new ArrayList();
        for (Document document : documents)
        {
            Post post = new Post();
            post.pTitle = document.getString("title");
            post.pDate = document.getString("date");
            post.aName = document.getString("author");
            post.pID = document.getObjectId("_id").toString();
            post.pText = document.getString("text");
            post.tags = (List<String>) document.get("tags");
            searchResults.add(post);
        }
        return searchResults;
    }
    
    public List<Post> Search(String byTitle, String byTags, 
            String byContent, String byAuthor, String searchTerm)
    {
        FindIterable<Document> documents;
        if (byAuthor != null)
        {
            documents = posts.find(or(
                    regex("title", searchTerm, "i"), 
                    in("tags", searchTerm),
                    regex("text", searchTerm, "i"),
                    eq("author", searchTerm)));
        }
        else if (byContent != null)
        {
            documents = posts.find(or(
                    regex("title", searchTerm, "i"), 
                    in("tags", searchTerm),
                    regex("text", searchTerm, "i")));
        }
        else if (byTags != null)
        {
            documents = posts.find(or(
                    regex("title", searchTerm, "i"), 
                    in("tags", searchTerm)));
        }
        else
        {
            documents = posts.find(regex("title", searchTerm, "i"));
        }
        
        List<Post> searchResults = new ArrayList();
        for (Document document : documents)
        {
            Post post = new Post();
            post.pTitle = document.getString("title");
            post.pDate = document.getString("date");
            post.aName = document.getString("author");
            post.pID = document.getObjectId("_id").toString();
            post.pText = document.getString("text");
            post.tags = (List<String>) document.get("tags");
            searchResults.add(post);
        }
        return searchResults;
    }
    
    public void AddPost(Post post)
    {
        Document document = new Document("title", post.pTitle)
                .append("text", post.pText)
                .append("date", post.pDate)
                .append("author", post.aName)
                .append("comments", new ArrayList<Document>())
                .append("tags", post.tags);         
        posts.insertOne(document);
    }
    
    public void AddPosts(List<Post> posts)
    {
        List<Document> documents = new ArrayList<>();
        for (Post post : posts)
        {
            Document document = new Document("title", post.pTitle)
                    .append("text", post.pText)
                    .append("date", post.pDate)
                    .append("author", post.aName)
                    .append("comments", new ArrayList<Document>())
                    .append("tags", post.tags);
            documents.add(document);
        }
        MongoConnection.posts.insertMany(documents);
    }
    
    public void AddComment(String pID, Comment comment) 
    {
        Document cDocument = new Document("cAuthor", comment.aName)
                .append("cDate", comment.cDate)
                .append("cText", comment.cText);
        posts.updateOne(eq("_id", new ObjectId(pID)), 
                new Document("$push", new Document("comments", cDocument)));
    }
    
    public void EmptyDatabase()
    {
        posts.deleteMany(new BsonDocument());
    }
}
