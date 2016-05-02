package DAL;

import DataObjects.Comment;
import DataObjects.Post;
import java.util.List;

public interface IConnection
{
    public void Close();
    
    public Post GetPost(String pID);
    
    public List<Comment> GetComments(String pID);
    
    public List<Post> GetRecentPosts(int count);
    
    public List<Post> GetPostsByAuthor(String aName);
    
    public List<Post> Search(String byTitle, String byTags, 
            String byContent, String byAuthor, String searchTerm);
    
    public List<Post> Search(String byTitle, String byTags, 
        String byContent, String byAuthor, String searchTerm, int numPosts);
    
    public void AddPost(Post post);
    
    public void AddPosts(List<Post> posts);
    
    public void AddComment(String pID, Comment comment);
    
    public void EmptyDatabase();
}
