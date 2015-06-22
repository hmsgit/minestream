import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

/**
 * This class is the connection between model and database that contains the training data. 
 */
public class DatabaseConnectionPreprocessed {


	/**
	 * Connection to the database
	 */
	private Connection connection;


	/**
	 * preprocessor that cleans the tweets
	 */
	private Preprocessor pp;

	/**
	 * ArrayList that contains the training Istances
	 */
	ArrayList<ExtendedTweet> trainingSet = new ArrayList<ExtendedTweet>();


	PreparedStatement statement;
	String start = "";
	String date = "";

	ResultSet rs;

	PreparedStatement stmt;



	/**
	 * Controller
	 * @throws java.sql.SQLException
	 * @throws ClassNotFoundException
	 * @throws java.io.IOException
	 */
	public DatabaseConnectionPreprocessed(String db, String user, String password) throws SQLException, ClassNotFoundException, IOException{
		Class.forName("com.mysql.jdbc.Driver");

		pp = new Preprocessor();


		// DB connection
	    connection = DriverManager.getConnection(
						"jdbc:mysql://localhost/"+db, user, password);

	    // db query
		//stmt = connection.prepareStatement("SELECT sentiment, text, date from text order by date asc");

            stmt = connection.prepareStatement("SELECT sentiment, minimalText, STR_TO_DATE(date, '%W %M %d %T PDT %Y') AS stamp FROM tweets ORDER BY stamp ASC");
                    rs = stmt.executeQuery();
            System.out.println("After SQL query");

                    //generate dataset from the results
                    this.generateDataSet();
            System.out.println("After dataset generation");
	}


/**
 * Methods generates the trainingSet from the result set of te DB
 * @throws java.sql.SQLException
 * @throws java.io.IOException
 */
public void generateDataSet() throws SQLException, IOException {
	 
	int i = 0;
	while(	rs.next()){
		
		
		//Sentiment of the tweet
		String type = rs.getString(1);
		
		//content of the tweet
		String text = rs.getString(2);
		
		//date of the tweet
		date = rs.getString(3);
		

		//Generate Tweet Instance
		ExtendedTweet tweet = new ExtendedTweet();
		tweet.setText(text);
		tweet.setType(type);
		
		//Add Tweet to the Trainingset
		trainingSet.add(tweet);
		i++;
        //System.out.println(i);
		
		
	 }

//   connection.close();
//   
//   
//	connection = DriverManager.getConnection(
//			"jdbc:mysql://localhost/training2", "root", "ilpplm");
//	
//		for(int j = 0; j<trainingSet.size(); j++){
//			
//			Tweet t = trainingSet.get(j);
//			Statement stmt2;
//			String sql;     
//			   
//			
//	
//	
//			sql = "INSERT INTO tweets " 
//			    + "VALUES "
//			    + "('" + t.getText() + "',"
//			    + "'" + t.getType() + "',"
//			    + "'" + date + "')";
//	
//			stmt2 = connection.createStatement(); 
//			stmt2.executeUpdate(sql); 
//			stmt2.close();
//		}
//   
//	
	

}


//public static void main (String[] args) throws SQLException, ClassNotFoundException, IOException{
//	DatabaseConnection db = new DatabaseConnection();
//	
//}

}
