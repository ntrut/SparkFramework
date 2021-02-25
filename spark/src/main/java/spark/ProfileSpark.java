package spark;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.TimeZone;

import com.google.gson.Gson;



public class ProfileSpark 
{
	private HashMap<Integer, Profile> profiles  = new HashMap<Integer, Profile>();
	private String url = "jdbc:mysql://localhost:3306/homework?serverTimezone=" + TimeZone.getDefault().getID();
	private String user = "root";
	private String pass = "linuxserver";
	private Gson gson = new Gson();
	
	public HashMap<Integer, spark.Profile> getProfiles(Request req, Response res)
	{
		//retrieve all of the profiles from mySQL
		Connection con = null;
		try 
		{
			//connect to mySQL
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url,user,pass);
			Statement state  = con.createStatement();
			ResultSet results = state.executeQuery("SELECT * FROM profile");
			int counter = 0;
			while(results.next())
			{
				
				Profile profile = new Profile();
				
				profile.setId(results.getInt(1));
				profile.setUsername(results.getString(2));
				profile.setLastname(results.getString(3));
				profile.setAge(results.getString(4));
				profile.setFavTeam(results.getString(5));
				
				profiles.put(counter, profile);
				counter++;
			}
			
			con.close();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		
		return profiles;
	}
	
	public spark.Profile getProfile(String id, Request req, Response res)
	{
		//make sure the number is a number

		int intid = -1;
		try {
			intid = Integer.parseInt(id);

		}catch(NumberFormatException e) {
			res.redirect("/500",500);
		}
		
		
		Connection con = null;
		Profile dummy = null;
		boolean error = true;
		try 
		{
			//connect to mySQL
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url,user,pass);
			Statement state  = con.createStatement();
			ResultSet results = state.executeQuery("SELECT * FROM profile");
			while(results.next())
			{
				
				Profile profile = new Profile();
				
				//if the int from the url equals a int on the table
				//then create hte profile
				if(intid == results.getInt(1))
				{
					profile.setId(results.getInt(1));
					profile.setUsername(results.getString(2));
					profile.setLastname(results.getString(3));
					profile.setAge(results.getString(4));
					profile.setFavTeam(results.getString(5));
					dummy = profile;
					error = false;
					
				}
				
			}
			con.close();
			
			if(error == true)
			{
				res.redirect("/404", 404);
			}
			
			
			
		} catch (SQLException | ClassNotFoundException e) {
			
			e.printStackTrace();
		}
		
		return dummy;
	}	
	
	//profile/ID/favteam
	public spark.STeam getFavTeam(String id, Request req, Response res)
	{
		String teamname = null;
		int intid = -1;
		try {
			intid = Integer.parseInt(id);

		}catch(NumberFormatException e) {
			res.redirect("/500",500);
		}
		
		Connection con = null;
		STeam sendtoJson= null;
		boolean error = true;
		try 
		{
			
			//connect to mySQL
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url,user,pass);
			Statement ls  = con.createStatement();
			Statement state2  = con.createStatement();
			Statement profile  = con.createStatement();
			
			//get the profile from the user that has the string of the favortie team
			ResultSet getProfile = profile.executeQuery("SELECT * FROM profile WHERE id='"+intid+"'");
			while(getProfile.next())
			{
				teamname = getProfile.getString(5);
			}
			
			//find the team in mySQL with the correct name
			ResultSet results = ls.executeQuery("SELECT * FROM teams WHERE name="+"'"+ teamname +"'");
			while(results.next())
			{
				Player[] playerarray = new Player[24];
				STeam team = new STeam();
				
				//set that team to team object so we can Json it
				team.setId(results.getInt(1));
				team.setName(results.getString(2));
				team.setFounded(results.getInt(3));
				team.setMatchesPlayed(results.getInt(4));
				team.setWins(results.getInt(5));
				team.setLoses(results.getInt(6));
				team.setGoals(results.getInt(7));
				team.setGoalsconceded(results.getInt(8));
				team.setGoalsConcededperMatch(results.getDouble(9));
				team.setGoalsperMatch(results.getDouble(10));
				error = false;	
				
				
				//get all the players that are associated with the teams
				ResultSet rs = state2.executeQuery("SELECT * FROM players WHERE team_id='"+team.getId()+"'");
				
				if(error != true)
				{
					int i = 0;
					while(rs.next())
					{
						Player player = new Player();
						player.setId(rs.getInt(1));
						player.setName(rs.getString(2));
						player.setAppearances(rs.getInt(3));
						player.setGoals(rs.getInt(4));
						player.setAssits(rs.getInt(5));
						player.setCleansheets(rs.getInt(6));
						player.setTeam_id(rs.getInt(7));
						
						playerarray[i] = player;
						i++;
					}
					Player[] truearray = new Player[i];
					
					//create a new array so it doesnt show all the nulls 
					int j = 0;
					while(playerarray[j] != null)
					{
						truearray[j] = playerarray[j];
						j++;
					}
					
					
					team.setPlayer(truearray);
					sendtoJson = team;
				}
				
				
			}
			
			if(error == true)
			{
				res.redirect("/404",404);
			}
			
			con.close();
			
		} catch (SQLException | ClassNotFoundException e) {
			
			e.printStackTrace();
		}
		return sendtoJson;
		
	}
	
	public String createProfile(Request req, Response res)
	{
		String json = null;
			
		String myprofile = req.body().toString();
		Profile newp;
		newp = gson.fromJson(myprofile, Profile.class);
		
		
		String sqlProfile = "INSERT INTO profile (id,name,lastname,age,favTeam)" + "VALUES('" + newp.id + "' , '"+newp.username+ "' , '"+newp.lastname+"', '"+newp.age+"' , '"+newp.favTeam+"')";
		//send to mySQL
		Connection con = null;
		try 
		{
			//connect to mySQL
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url,user,pass);
			Statement st = con.createStatement();
			Statement check = con.createStatement();
			
			ResultSet CHECKmysql = check.executeQuery("SELECT id FROM profile WHERE id='"+newp.id+"'");
			
			//check if a id already exists
			if(CHECKmysql.next())
			{
				json = "{ \"type\": \"error\", \"id\": " + newp.id + " }";
			}
			else 
			{
				//if a id doesnt exist then make the profile
				int m = st.executeUpdate(sqlProfile);
				
				if(m == 1)
				{
					json = "{ \"type\": \"success\", \"id\": " + newp.id + " }";
				}
			}
					
			
			con.close();
			
		} catch (SQLException | ClassNotFoundException e) {
			
			e.printStackTrace();
		}
	
	return json;	
		
	}
	
	//to update a profile
	public String updateProfile(String id, Request req, Response res)
	{
		String json = null;
		boolean error = true;
		
		//check if it can be parsed
		int intid = -1;
		try {
			intid = Integer.parseInt(id);

		}catch(NumberFormatException e) {
			res.redirect("/500",500);
		}
		
		String myprofile = req.body();
		Profile newp;
		newp = gson.fromJson(myprofile, Profile.class);
		
		//if the id exists
		//update the profile
		Connection con = null;
		try 
		{
			//connect to mySQL
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url,user,pass);
			Statement ID  = con.createStatement();
			Statement name  = con.createStatement();
			Statement lastname  = con.createStatement();
			Statement age  = con.createStatement();
			Statement favteam  = con.createStatement();
			
			Statement st = con.createStatement();
			
			ResultSet result = st.executeQuery("SELECT EXISTS(SELECT * FROM profile WHERE id='"+intid+"')");
			
			//check if a profile id already exists, and if it does then send an error
			Statement check = con.createStatement();
			ResultSet CHECKmysql = check.executeQuery("SELECT id FROM profile WHERE id='"+newp.id+"'");
			
			//check if a id already exists
			if(CHECKmysql.next() && intid != newp.id)
			{
				error = true;
			}
			else
			{
				//if there is something in the body, then we update
				result.next();
				int num = result.getInt(1);
				if(num == 1)
				{
					
					ID.executeUpdate("UPDATE profile SET id='"+newp.getId()+"' WHERE id ='"+intid+"'");	
						 
					name.executeUpdate("UPDATE profile SET name='"+newp.getUsername()+"' WHERE id ='"+newp.getId()+"'");	
						
					lastname.executeUpdate("UPDATE profile SET lastname='"+newp.getLastname()+"' WHERE id ='"+newp.getId()+"'");	
						
					age.executeUpdate("UPDATE profile SET age='"+newp.getAge()+"' WHERE id ='"+newp.getId()+"'");
						
					favteam.executeUpdate("UPDATE profile SET favTeam='"+newp.getFavTeam()+"' WHERE id ='"+newp.getId()+"'");
					error = false;
				}
			}
			
			//if a parameter got updated then say it was a success
			if(error == true)
			{
				json = "{ \"type\": \"error\", \"id\": " + intid + " }";
			}
			else
				json = "{ \"type\": \"success\", \"id\": " + newp.id + " }";
									
			con.close();
		
		}catch (SQLException | ClassNotFoundException e) {
			
			e.printStackTrace();
		}
		
		return json;
	}
	
	//delete pprofile
	public String deleteProfile(String id, Request req, Response res)
	{
		String json = null;
		int intid = -1;
		try {
			intid = Integer.parseInt(id);

		}catch(NumberFormatException e) {
			res.redirect("/500",500);
		}
		
		//delete from mysql
				String delete = "DELETE FROM profile WHERE id = '"+intid+"' ";
				Connection con = null;
				try 
				{
					//connect to mySQL
					Class.forName("com.mysql.cj.jdbc.Driver");
					con = DriverManager.getConnection(url,user,pass);
					Statement st = con.createStatement();
					int m = st.executeUpdate(delete);
					
					if(m == 1)
					{
						json = "{ \"type\": \"success\", \"id\": " + intid + " }";
					}
					else
						json = "{ \"type\": \"error\", \"id\": " + intid + " }";
					
					con.close();
					
				} catch (SQLException | ClassNotFoundException e) {
					
					e.printStackTrace();
				}
		return json;
	}
}
