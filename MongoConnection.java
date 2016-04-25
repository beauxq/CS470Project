package DAL;

import DataObjects.Comment;
import DataObjects.Post;
import com.mongodb.MongoClient;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.BasicDBList;
import com.mongodb.DBCursor;

import com.mongodb.client.MongoDatabase;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MongoConnection implements IConnection
{
    private static String ipAddress = "72.129.239.46";
    private static String port = "27444";
    private static String userID = "cs470";
    private static String password = "cs470lnw";
    private static String databaseName = "cs470blog";
    private static String URI =
            "mongodb://" + userID +":" + password 
            + "@" + ipAddress + ":" + port + "/" + databaseName;

    private static MongoClient mongoClient;
    public static MongoDatabase db;
    private static DBCollection coll;

    MongoConnection()
    {
        try
        {
            System.out.println("Opening MongoDB connection...");
            MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
	    //MongoClient mongoClient = new MongoClient(new MongoClientURI(URI));		
            // Now connect to your databases
            DB db = mongoClient.getDB( databaseName );
            //System.out.println("Connect to database successfully");
			        
            coll = db.getCollection("Posts");
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

            coll = null;
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
        Post post = new Post();
       
        try
        {
             
            post =  getPost(pID);
	}
        
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return post;
        
 
    }
 
    
    public List<Comment> GetComments(String pID)
    {
       List<Comment> comments = new ArrayList<>();
       BasicDBObject whereQuery = new BasicDBObject();
       whereQuery.put("_id", pID);
       BasicDBObject fields = new BasicDBObject();
       fields.put("comments", 1);
       DBCursor cursor = coll.find(whereQuery);
       
       boolean more = cursor.hasNext();
       while (more)
        {
            Comment c = new Comment();
            c.cID = cursor.toString();
            
            comments.add(c);
            more = cursor.hasNext();
        }
        return comments;
    }
    
    public List<Post> GetRecentPosts()
    {
        
     return null;   
    }
    
    public List<Post> GetPostsByAuthor(String aName)
    {
        List<Post> posts = new ArrayList();
        try
        {
           BasicDBObject whereQuery = new BasicDBObject();
           whereQuery.put("author", aName); 
           posts = getPostList(whereQuery);
        }
        catch (SQLException ex)
        {
            System.out.println(ex.getMessage());
        }
        return posts;
        
    }
    
    public List<Post> Search(String byTitle, String byContent, 
            String byTags, String byAuthor, String searchTerm)
    {
        List<Post> posts = new ArrayList();
        
        BasicDBObject whereQuery = new BasicDBObject();
        
        if (byAuthor != null)
        {
            DBObject clause1 = new BasicDBObject("pTitle", "/"+searchTerm+"/");  
            DBObject clause2 = new BasicDBObject("pText", "/"+searchTerm+"/"); 
            DBObject clause3 = new BasicDBObject("tText", "/"+searchTerm+"/");  
            DBObject clause4 = new BasicDBObject("aName", "/"+searchTerm+"/");
            BasicDBList or = new BasicDBList();
            or.add(clause1);
            or.add(clause2);
            or.add(clause3);
            or.add(clause4);
            whereQuery = new BasicDBObject("$or", or);
        }
        else if (byContent != null)
        {
            DBObject clause1 = new BasicDBObject("pTitle", "/"+searchTerm+"/");  
            DBObject clause2 = new BasicDBObject("pText", "/"+searchTerm+"/"); 
            DBObject clause3 = new BasicDBObject("tText", "/"+searchTerm+"/");  
           
            BasicDBList or = new BasicDBList();
            or.add(clause1);
            or.add(clause2);
            or.add(clause3);
            
            whereQuery = new BasicDBObject("$or", or);
        }
        else if (byTags != null)
        {
            DBObject clause1 = new BasicDBObject("pTitle", "/"+searchTerm+"/");  
            DBObject clause2 = new BasicDBObject("tText", "/"+searchTerm+"/"); 
            
            BasicDBList or = new BasicDBList();
            or.add(clause1);
            or.add(clause2);
            
            whereQuery = new BasicDBObject("$or", or);
        }
        else
        {
            DBObject clause1 = new BasicDBObject("pTitle", "/"+searchTerm+"/");  
            
            BasicDBList or = new BasicDBList();
            or.add(clause1);
            
            whereQuery = new BasicDBObject("$or", or);
        }
        try
        {
            posts = getPostList(whereQuery);
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        return posts;
        
    }
    
    public void AddPost(String pTitle, String pText, String pDate, 
            String aName, String[] tags)
    {
        BasicDBObject doc = new BasicDBObject("title", pTitle).
            append("text", pText).
            append("date", pDate).
            append("author", aName).
            append("tags", tags);
				
         coll.insert(doc);
         System.out.println("Document inserted successfully");
    }
    
    public void AddComment(String pID, String cText, String cDate, String aName) 
    {
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("_id", pID);
        DBCursor cursor = coll.find(whereQuery);
        /*
        BasicDBObject doc = new BasicDBObject("title", pTitle).
            append("text", pText).
            append("date", pDate).
            append("author", aName).
            append("tags", 1);//need to modify sundar
				
         coll.insert(doc);*/
         System.out.println("Document inserted successfully");
    }
    private static Post getPost(String pID) throws SQLException
    {
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("_id", pID);
        DBCursor cursor = coll.find(whereQuery);
        
        boolean more = cursor.hasNext();
        Post post = new Post();
        post.pID = (String) cursor.curr().get("_id");
        post.pTitle = (String) cursor.curr().get("pTitle");
        post.aName = (String) cursor.curr().get("aName");
        post.pDate = (String) cursor.curr().get("pDate");
        post.pText = (String) cursor.curr().get("pText");
        //post.tags = (String) cursor.one().get("pTags");
        
        return post;
    }
    
    private static List<Post> getPostList(BasicDBObject whereQuery) throws SQLException
    {
        List<Post> posts = new ArrayList<>();
        DBCursor cursor = coll.find(whereQuery);
        boolean more = cursor.hasNext();
        while (more)
        {
            Post p = new Post();
            p.pID = (String) cursor.curr().get("_id");
            p.pTitle = (String) cursor.curr().get("pTitle");
            p.aName = (String) cursor.curr().get("aName");
            p.pDate = (String) cursor.curr().get("pDate");
            posts.add(p);
            more = cursor.hasNext();
        }
        return posts;
    }
    
}
