package servlets;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SQLUtil
{
    public static int nextID(Statement stmt, String id, String tablename) throws SQLException
    {
        ResultSet rs;
        String selectMaxID = "select max(" + id + ") from " + tablename;
        rs = stmt.executeQuery(selectMaxID); // get largest id so far

        rs.next();
        return rs.getInt(1) + 1;
    }
    
    public static int getAuthorID(Statement stmt, String aName) throws SQLException
    {
        int aID = -1;
        ResultSet rs = stmt.executeQuery("Select aID from authors where aName = '" + aName + "'");
        if (rs.next()) // author already in db
        {
            aID = rs.getInt(1);
        }
        else // add author to db
        {
            aID = nextID(stmt, "aid", "authors");
            stmt.execute("Insert into authors values (" + aID + ", '" + aName + "', 'password')");
        }
        return aID;
    }
    
    public static String getCurrentDate()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Date d = new Date();
    	String currentDate = (dateFormat.format(d));
        return currentDate;
    }

    public static int getTagID(Statement stmt, String tText) throws SQLException
    {
        int tID = -1;
        ResultSet rs = stmt.executeQuery("Select tID from tags where tText = '" + tText + "'");
        if (rs.next()) // tag already in db
        {
            tID = rs.getInt(1);
        }
        else // add tag to db
        {
            tID = nextID(stmt, "tid", "tags");
            stmt.execute("Insert into tags values (" + tID + ", '" + tText + "')");
        }
        return tID;
    }

}
