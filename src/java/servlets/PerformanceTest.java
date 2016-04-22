package servlets;

import DAL.DAL;
import DataObjects.Post;
import PerformanceTesting.PostBuilder;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PerformanceTest extends HttpServlet
{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        String results = "";
        try
        {
            // NOTE: dictionary file location specified in PostBuilder.java
            // MUST be accurate on host computer
            String db = request.getParameter("db"); // 'MySQL' or 'MongoDB'
            DAL dal = DAL.GetDAL("MySQL".equals(db));
            PostBuilder pb = PostBuilder.GetPostBuilder();
            
//            List<Integer> dbSizes = Arrays.asList(0, 1000, 1070, 1150, 1235, 1320, 1415, 
//                    1520, 1630, 1750, 1875, 2010, 2155, 2310, 2475, 2655, 2850, 3055, 
//                    3275, 3510, 3765, 4035, 4330, 4640, 4975, 5335, 5720, 6135, 6580, 
//                    7055, 7565, 8110, 8695, 9325, 10000, 10725, 11500, 12330, 13220, 
//                    14175, 15200, 16300, 17475, 18740, 20090, 21545, 23100, 24770, 
//                    26560, 28480, 30540, 32745, 35110, 37650, 40370, 43290, 46415, 
//                    49770, 53365, 57225, 61360, 65795, 70550, 75645, 81115, 86975, 
//                    93260, 100000, 107225, 114975, 123285, 132195, 141745, 151990, 
//                    162975, 174755, 187380, 200925, 215445, 231015, 247710, 265610, 
//                    284805, 0, 327455, 351120, 376495, 403700, 432875, 464160, //305385
//                    497700, 533670, 572235, 613590, 657935, 705480, 756465, 811130, 
//                    869750, 932605, 1000000);
            
            List<Integer> dbSizes = Arrays.asList(0, 10, 20, 30);
            
            results += "Performance Results for " + db + ":<br>";
            results += "<table>";
            results += "<tr><td>NumPosts</td><td>title</td><td>titletags</td><td>titletagscontent</td>"
                    + "<td>titletagscontentauthor</td></tr>";

            System.out.println("Clearing " + db + " database...");
            dal.EmptyDatabase();

            for (int i = 1; i < dbSizes.size(); i++)
            {
                System.out.println("Timing test " + i + " of " + (dbSizes.size() - 1) + "...");
                results += "<tr><td>" + dbSizes.get(i) + "</td>";
                
                // insert new posts
                int numNewPosts = dbSizes.get(i) - dbSizes.get(i - 1);
                insertPosts(pb, dal, numNewPosts, dbSizes.get(i - 1));
                
                // perform search testing
                System.out.println("Searching " + dbSizes.get(i) + " posts...");
                results += search(dal);
                results += "</tr>";
            }
            results += "</table>";
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        response.sendRedirect("performance.jsp?results=" + results);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        processRequest(request, response);
    }
    
    private void insertPosts(PostBuilder pb, DAL dal, int numPosts, int baseCount)
    {
        while (numPosts > 10000)
        {
            numPosts -= 10000;
            insertPosts(pb, dal, 10000, baseCount);
            baseCount += 10000;
        }
        // posts limited to 10,000 to prevent memory overflow
        List<Post> posts = new ArrayList<>();
        for (int i = 0; i < numPosts; i++)
        {
            if (i % 100 == 0) 
            {
                System.out.println("Creating post " + (baseCount + i) + "..."); 
            }
            Post post = new Post();
            post.tags = pb.CreateTags();
            post.pText = pb.CreatePost(post.tags);
            post.pTitle = pb.CreateTitle(post.pText);
            post.aName = getName(post.pTitle);
            post.pDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
            posts.add(post);
        }
        System.out.println("Inserting posts to db...");
        dal.AddPosts(posts);
    }
    
    private String search(DAL dal)
    {
        String searchResults = "";
        
        // time searches (using 'eric' because it is in word 'american' and author 'eric')
        long start = System.nanoTime();
        dal.Search("on", null, null, null, "eric");
        long stop = System.nanoTime();
        double time = (double)(stop - start) / 1000000000.0;
        searchResults += "<td>" + time + "</td>";
        System.out.println("  title                    : " + time + " seconds");

        start = System.nanoTime();
        dal.Search("on", "on", null, null, "eric");
        stop = System.nanoTime();
        time = (double)(stop - start) / 1000000000.0;
        searchResults += "<td>" + time + "</td>";
        System.out.println("  title+tags               : " + time + " seconds");

        start = System.nanoTime();
        dal.Search("on", "on", "on", null, "eric");
        stop = System.nanoTime();
        time = (double)(stop - start) / 1000000000.0;
        searchResults += "<td>" + time + "</td>";
        System.out.println("  title+tags+content       : " + time + " seconds");

        start = System.nanoTime();
        dal.Search("on", "on", "on", "on", "eric");
        stop = System.nanoTime();
        time = (double)(stop - start) / 1000000000.0;
        searchResults += "<td>" + time + "</td>";
        System.out.println("  title+tags+content+author: " + time + " seconds");  
        
        return searchResults;
    }
    
    private String getName(String title)
    {
        if (title.length() % 4 == 0)
        {
            return "Atreya";
        }
        if (title.length() % 4 == 1)
        {
            return "Doug";
        }
        if (title.length() % 4 == 2)
        {
            return "Eric";
        }
        return "Sundar";
    }
}
