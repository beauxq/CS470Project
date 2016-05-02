package DAL;

import DataObjects.Comment;
import DataObjects.Post;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
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
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public class MongoConnection implements IConnection
{
    private static final String ipAddress = "136.63.18.225";
    private static final int port = 27444;
    private static final String userID = "cs470";
    private static final String password = "cs470lnw";
    private static final String databaseName = "cs470blog";
    private static final String URI =
            "mongodb://" + userID +":" + password 
            + "@" + ipAddress + ":" + port + "/" + databaseName;
    private static final int maxPosts = 1000000;

    private static MongoClient mongoClient;
    private static MongoDatabase db;
    private static MongoCollection<Document> posts;
    
    private static MongoConnection singleton = null;

    private MongoConnection()
    {
        try
        {
            System.out.println("Opening MongoDB connection...");

            MongoClientOptions.Builder options = new MongoClientOptions.Builder()
                    .socketKeepAlive(true)
                    .connectionsPerHost(100000)
                    .socketTimeout(Integer.MAX_VALUE)
                    .minHeartbeatFrequency(20000)
                    .heartbeatSocketTimeout(Integer.MAX_VALUE)
                    .connectTimeout(Integer.MAX_VALUE)
                    .maxWaitTime(Integer.MAX_VALUE);
            
            mongoClient = new MongoClient(new MongoClientURI(URI, options));
            
            db = mongoClient.getDatabase(databaseName);
            posts = db.getCollection("posts");
            
            posts.count(); // throws exception if connection not working
            System.out.println("Connected to MongoDB");
        }
        catch (Exception ex)
        {
            System.out.println("Failure connecting to MongoDB");
            System.out.println(ex.toString());
        }
    }
    
    public static MongoConnection GetConnection()
    {
        if (singleton == null)
        {
            singleton = new MongoConnection();
        }
        return singleton;
    }

    @Override
    public void Close()
    {
        try
        {
            System.out.println("Closing MongoDB connection...");
            posts = null;
            db = null;
            mongoClient.close();
            singleton = null;
            System.out.println("Closed MongoDB connection");
        }
        catch (Exception ex)
        {
            System.out.println("Failure closing MongoDB connection");
            System.out.println(ex.toString());
        }
    }
    
    @Override
    public Post GetPost(String pID)
    {
        Document document = posts.find(eq("_id", new ObjectId(pID))).first();
        return documentToPost(document);
    }
    
    @Override
    public List<Comment> GetComments(String pID)
    {
        Document document = posts.find(eq("_id", new ObjectId(pID))).first();
        List<Comment> comments = new ArrayList();
        for (Document c : (List<Document>)document.get("comments"))
        {
            comments.add(documentToComment(c));
        }
        return comments;
    }
    
    @Override
    public List<Post> GetRecentPosts(int count)
    {
        FindIterable<Document> documents = 
                posts.find().sort(new Document("date", -1)).limit(count);
        List<Post> searchResults = new ArrayList();
        for (Document document : documents)
        {
            searchResults.add(documentToPost(document));
        }
        return searchResults;
    }
    
    @Override
    public List<Post> GetPostsByAuthor(String aName)
    {
        FindIterable<Document> documents = posts.find(eq("author", aName));
        List<Post> searchResults = new ArrayList();
        for (Document document : documents)
        {
            searchResults.add(documentToPost(document));
        }
        return searchResults;
    }
    
    @Override
    public List<Post> Search(String byTitle, String byTags, 
            String byContent, String byAuthor, String searchTerm)
    {
        return Search (byTitle, byTags, byContent, byAuthor, searchTerm, maxPosts);
    }
    
    
    @Override
    public List<Post> Search(String byTitle, String byTags, 
            String byContent, String byAuthor, String searchTerm, int numPosts)
    {
        Bson searchTitle = regex("title", searchTerm, "i");
        Bson searchTags = in("tags", searchTerm);
        Bson searchText = regex("text", searchTerm, "i");
        Bson searchAuthor = eq("author", searchTerm);
        Bson searchCriteria; 

        if (byAuthor != null)
        {
            searchCriteria = or(searchTitle, searchTags, searchText, searchAuthor);
        }
        else if (byContent != null)
        {
            searchCriteria = or(searchTitle, searchTags, searchText);
        }
        else if (byTags != null)
        {
            searchCriteria = or(searchTitle, searchTags);
        }
        else
        {
            searchCriteria = searchTitle;
        }

        List<Post> searchResults = new ArrayList();
        FindIterable<Document> results = 
                posts.find().limit(numPosts).filter(searchCriteria);
        for (Document document : results)
        {
            searchResults.add(documentToPost(document));
        }
        return searchResults;
    }
    
    @Override
    public void AddPost(Post post)
    {        
        posts.insertOne(postToDocument(post));
    }
    
    @Override
    public void AddPosts(List<Post> posts)
    {
        List<Document> documents = new ArrayList<>();
        for (Post post : posts)
        {
            documents.add(postToDocument(post));
        }
        MongoConnection.posts.insertMany(documents);
    }
    
    @Override
    public void AddComment(String pID, Comment comment) 
    {
        posts.updateOne(
                eq("_id", new ObjectId(pID)), 
                new Document("$push", 
                        new Document("comments", commentToDocument(comment))));
    }
    
    @Override
    public void EmptyDatabase()
    {
        posts.deleteMany(new BsonDocument());
    }
    
    private Post documentToPost(Document document)
    {
        Post post = new Post();
        post.pTitle = document.getString("title");
        post.pDate = document.getString("date");
        post.aName = document.getString("author");
        post.pID = document.getObjectId("_id").toString();
        post.pText = document.getString("text");
        post.tags = (List<String>) document.get("tags");
        return post;
    }
    
    private Comment documentToComment(Document document)
    {
        Comment comment = new Comment();
        comment.aName = document.getString("cAuthor");
        comment.cDate = document.getString("cDate");
        comment.cText = document.getString("cText");
        return comment;
    }
    
    private Document postToDocument(Post post)
    {
        Document document = new Document("title", post.pTitle)
                .append("text", post.pText)
                .append("date", post.pDate)
                .append("author", post.aName)
                .append("comments", new ArrayList<Document>())
                .append("tags", post.tags);  
        return document;
    }
    
    private Document commentToDocument(Comment comment)
    {
        Document document = new Document("cAuthor", comment.aName)
                .append("cDate", comment.cDate)
                .append("cText", comment.cText);
        return document;
    }
}
