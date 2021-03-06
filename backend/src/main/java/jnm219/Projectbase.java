package jnm219;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.net.URISyntaxException;
// Imports for time functionality
import java.security.Timestamp;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import java.util.ArrayList;


public class Projectbase {
    /**
     * The connection to the database.  When there is no connection, it should
     * be null.  Otherwise, there is a valid open connection
     */
    private Connection mConnection;

    /**
     * A prepared statement for getting all messages
     */
    private PreparedStatement mSelectAllProjects;

    private PreparedStatement mAddProject;

    private PreparedStatement mCheckProjects;
    private PreparedStatement mAddUser;
    private PreparedStatement mCheckIndex;
    /**
     * Give the Database object a connection, fail if we cannot get one
     * Must be logged into heroku on a local computer to be able to use mvn heroku:deploy
     */
    private static Connection getConnection() throws URISyntaxException, SQLException {

        String dbUrl = System.getenv("JDBC_DATABASE_URL"); // Url for heroku database connection
        Connection conn = DriverManager.getConnection(dbUrl);
        return DriverManager.getConnection(dbUrl);
    }
    /**
     * Close the current connection to the database, if one exists.
     *
     * NB: The connection will always be null after this call, even if an
     *     error occurred during the closing operation.
     *
     * @return True if the connection was cleanly closed, false otherwise
     */
    boolean disconnect()
    {
        if (mConnection == null) {
            System.err.println("Unable to close connection: Connection was null");
            return false;
        }
        try {
            mConnection.close();
        } catch (SQLException e) {
            System.err.println("Error: Connection.close() threw a SQLException");
            e.printStackTrace();
            mConnection = null;
            return false;
        }
        mConnection = null;
        return true;
    }

    static Projectbase getProjectbase(int connectionType) {
        Projectbase pb = new Projectbase();

        // Give the Database object a connection, fail if we cannot get one
        try {
            Connection conn;
            if(connectionType == 1)
            {
                conn = getConnection();
            }
            else if(connectionType == 2)
            {
                conn = getConnection();
            }
            else
            {
                conn = getConnection();
            }
            if (conn == null) {
                System.err.println("Error: DriverManager.getConnection() returned a null object");
                return null;
            }
            pb.mConnection = conn;
        } catch (SQLException e) {
            System.err.println("Error: DriverManager.getConnection() threw a SQLException");
            e.printStackTrace();
            return null;
        } catch (URISyntaxException e) {
            System.err.println("Error: DriverManager.getConnection() threw a URISyntaxException");
            e.printStackTrace();
            return null;
        }

        try{
            pb.mSelectAllProjects = pb.mConnection.prepareStatement("SELECT * FROM projects");
            pb.mAddProject = pb.mConnection.prepareStatement("INSERT INTO projects Values (default,?,?,?,?,default)");
            pb.mAddUser = pb.mConnection.prepareStatement("INSERT into user_project values (?,?)");
            pb.mCheckIndex = pb.mConnection.prepareStatement("select id FROM projects WHERE name = ?");
            pb.mCheckProjects = pb.mConnection.prepareStatement("SELECT * FROM  user_project WHERE email = ?");

        } catch (SQLException e) {
            System.err.println("Error creating prepared statement");
            e.printStackTrace();
            pb.disconnect();
            return null;
        }
        return pb;
    }

    /**
     * Returning arraylist of projects which displays all the projects ever made
     */
    ArrayList<ProjectRow> selectAllProjects(String email) {
        ArrayList<ProjectRow> res = new ArrayList<ProjectRow>();
        ArrayList<Integer> checkRes = new ArrayList<Integer>();
        try {
            ResultSet rs = mSelectAllProjects.executeQuery();
            mCheckProjects.setString(1, email);
            ResultSet checkSet = mCheckProjects.executeQuery();
            System.out.println("SubFiles Are Here");

            System.out.println("IN SELECT ALL PROJECTS");
            while(checkSet.next()){
                checkRes.add(checkSet.getInt("projectid"));
                System.out.println("projectid: " + String.valueOf(checkSet.getInt("projectid")));
            }
            while (rs.next()) {
                if(checkRes.contains(rs.getInt("id"))){
                    ProjectRow Projectrow = new ProjectRow(rs.getInt("id"), rs.getString("name"),
                            rs.getString("description"), rs.getString("owner"),
                            rs.getString("organization"));
                    res.add(Projectrow);
                    System.out.println("id: " + String.valueOf(rs.getInt("id")));
                }
            }
            rs.close();
            checkSet.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    //Method for adding a new Project
    boolean addProject(String name,String description, String owner, String organization) {
        int rs=0;

        try {
            System.out.println("Adding Project: " + name);
            mAddProject.setString(1,name);
            mAddProject.setString(2,description);
            mAddProject.setString(3,owner);
            mAddProject.setString(4,organization);
            rs +=mAddProject.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    //Adds a User to a Project
    boolean addUser(String email, int projectId){
        try{
            System.out.println("Adding user: " + email);
            mAddUser.setInt(1, projectId);
            mAddUser.setString(2, email);
            mAddUser.executeUpdate();
        }catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
        return  true;
    }
    int checkIndex(String name) {
        System.out.println("checkIndex");
        try {
            mCheckIndex.setString(1, name);
            ResultSet rs = mCheckIndex.executeQuery();
            rs.next();
            int check = rs.getInt("id");
            System.out.println("Check is " + check);
            rs.close();
            if (check == 0)
            {
                return 0;
            }
            else {
                return check;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }


}