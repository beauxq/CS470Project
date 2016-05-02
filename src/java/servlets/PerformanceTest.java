package servlets;

import DAL.DAL;
import DataObjects.Post;
import PerformanceTesting.PostBuilder;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PerformanceTest extends HttpServlet
{
    private static int numRetries = 100;
    private static Boolean usingSQL;
    
    protected void processRequest(HttpServletRequest request, 
            HttpServletResponse response) throws ServletException, IOException
    {
        String results = "";
        try
        {
            // NOTE: dictionary file location specified in PostBuilder.java
            // MUST be accurate on host computer
            String db = request.getParameter("db"); // 'MySQL' or 'MongoDB'
            usingSQL = "MySQL".equals(db);
            DAL dal = DAL.GetDAL(usingSQL);
            PostBuilder pb = PostBuilder.GetPostBuilder();
            
//            List<Integer> dbSizes = Arrays.asList(0, 1000, 1070, 1150, 1235, 1320, 1415, //0
//                    1520, 1630, 1750, 1875, 2010, 2155, 2310, 2475, 2655, 2850, 3055, //7
//                    3275, 3510, 3765, 4035, 4330, 4640, 4975, 5335, 5720, 6135, 6580, //18
//                    7055, 7565, 8110, 8695, 9325, 10000, 10725, 11500, 12330, 13220, //29
//                    14175, 15200, 16300, 17475, 18740, 20090, 21545, 23100, 24770, //39
//                    26560, 28480, 30540, 32745, 35110, 37650, 40370, 43290, 46415, //48
//                    49770, 53365, 57225, 61360, 65795, 70550, 75645, 81115, 86975, //57
//                    93260, 100000, 107225, 114975, 123285, 132195, 141745, 151990, //66
//                    162975, 174755, 187380, 200925, 215445, 231015, 247710, 265610, //74
//                    284805, 305385, 327455, 351120, 376495, 403700, 432875, 464160, //82
//                    497700, 533670, 572235, 613590, 657935, 705480, 756465, 811130, //90
//                    869750, 932605, 1000000); //98
            
            List<Integer> dbSizes = Arrays.asList(0, 25, 50, 75, 100);
            
            results += "<h2>Performance Results for " + db + ":</h2>";
            results += "<div class='post'><table>";
            results += "<tr><th>Number<br>Of Posts</th><th>Title</th>"
                    + "<th>Title<br>Tags</th><th>Title<br>Tags<br>Content</th>"
                    + "<th>Title<br>Tags<br>Content<br>Author</th></tr>";

            System.out.println("Clearing " + db + " database...");
            dal.EmptyDatabase();
            
            for (int i = 1; i < dbSizes.size(); i++)
            {
                System.out.println("Timing test " + i + " of " + (dbSizes.size() - 1) + "...");
                results += "<tr><td>" + dbSizes.get(i) + "</td>";

                //insert new posts
                int numNewPosts = dbSizes.get(i) - dbSizes.get(i - 1);
                insertPosts(pb, dal, numNewPosts, dbSizes.get(i - 1));            
                
                // perform search testing
                System.out.println("Searching " + dbSizes.get(i) + " posts...");
                results += search(dal);
                results += "</tr>";
            }
            results += "</table></div>";
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
        // posts limited to 10,000 at a time to prevent memory overflow
        while (numPosts > 1000)
        {
            numPosts -= 1000;
            insertPosts(pb, dal, 1000, baseCount);
            baseCount += 1000;
        }
        
        try
        {
            List<Post> posts = new ArrayList<>();
            for (int i = 0; i < numPosts; i++)
            {
                if ((numPosts + baseCount < 50000 && i % 100 == 0) || i % 1000 == 0)
                {
                    System.out.println("Creating post " + (baseCount + i) + "..."); 
                }
                posts.add(pb.GetNewPost());
            }
            System.out.println("Inserting posts to db...");
            dal.AddPosts(posts);
        }
        catch (Exception ex)
        {
            //recover from database errors
            System.out.println("Error inserting posts to db");
            System.out.println(ex.getMessage());
            if (numRetries > 0)
            {
                dal = resetConnection(dal);
                insertPosts(pb, dal, numPosts, baseCount);
            }
        }
    }
   
    private String search(DAL dal)
    {
        String searchResults = "";
        try
        {
            DecimalFormat df = new DecimalFormat("0.000"); 
            long start = System.nanoTime();
            dal.Search("on", null, null, null, "abcdefghijklmnop");
            long stop = System.nanoTime();
            double time = (double)(stop - start) / 1000000000.0;
            searchResults += "<td>" + df.format(time) + " seconds</td>";
            System.out.println("  title                    : " + time + " seconds");

            start = System.nanoTime();
            dal.Search("on", "on", null, null, "abcdefghijklmnop");
            stop = System.nanoTime();
            time = (double)(stop - start) / 1000000000.0;
            searchResults += "<td>" + df.format(time) + " seconds</td>";
            System.out.println("  title+tags               : " + time + " seconds");

            start = System.nanoTime();
            dal.Search("on", "on", "on", null, "abcdefghijklmnop");
            stop = System.nanoTime();
            time = (double)(stop - start) / 1000000000.0;
            searchResults += "<td>" + df.format(time) + " seconds</td>";
            System.out.println("  title+tags+content       : " + time + " seconds");

            start = System.nanoTime();
            dal.Search("on", "on", "on", "on", "abcdefghijklmnop");
            stop = System.nanoTime();
            time = (double)(stop - start) / 1000000000.0;
            searchResults += "<td>" + df.format(time) + " seconds</td>";
            System.out.println("  title+tags+content+author: " + time + " seconds");  
        }
        catch (Exception ex)
        {
            //recover from database errors
            searchResults = "";
            System.out.println("Error searching posts in db");
            System.out.println(ex.getMessage());
            if (numRetries > 0)
            {
                dal = resetConnection(dal);
                return search(dal);
            }
        }
        return searchResults;
    }
    
    private DAL resetConnection(DAL dal)
    {
        PerformanceTest.numRetries--;
        System.out.println("Retrying (retries left: " + numRetries + ")...");
        System.out.println("Resetting connection...");

        dal.CloseConnection();
        try 
        {
            Thread.sleep(600000); 
        }
        catch (InterruptedException ex) 
        { 
            System.out.println(ex.toString());
        }
        return DAL.GetDAL(usingSQL);
    }
}
