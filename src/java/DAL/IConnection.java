package DAL;

import DataObjects.Comment;
import DataObjects.Post;
import java.util.List;

public interface IConnection
{
    public void Close();
    
    public Post GetPost(String pID);
    
    public List<Comment> GetComments(String pID);
    
    public List<Post> GetRecentPosts();
    
    public List<Post> GetPostsByAuthor(String aName);
    
    public List<Post> Search(String byTitle, String byContent, 
            String byTags, String byAuthor, String searchTerm);
    
    public void AddPost(String pTitle, String pText, String pDate, 
            String aName, String[] tags);
    
    public void AddComment(String pID, String cText, String cDate, String aName);
}
