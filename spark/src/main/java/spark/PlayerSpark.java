package spark;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.TimeZone;

import com.google.gson.Gson;

public class PlayerSpark 
{
	private String url = "jdbc:mysql://localhost:3306/homework?serverTimezone=" + TimeZone.getDefault().getID();
	private String user = "root";
	private String pass = "linuxserver";
	private Gson gson = new Gson();
	private HashMap<Integer, Player> players  = new HashMap<Integer, Player>();
	
	//For all players
	public HashMap<Integer, Player> getPlayers(Request req, Response res)
	{
		
		//retrieve all of the profiles from mySQL
		Connection con = null;
		try 
		{
					//connect to mySQL
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url,user,pass);
			Statement state  = con.createStatement();
			ResultSet results = state.executeQuery("SELECT * FROM players");
			int counter = 0;
			while(results.next())
			{
						
				Player player = new Player();
				
				player.setId(results.getInt(1));
				player.setName(results.getString(2));
				player.setAppearances(results.getInt(3));
				player.setGoals(results.getInt(4));
				player.setAssits(results.getInt(5));
				player.setCleansheets(results.getInt(6));
				player.setTeam_id(results.getInt(7));
				
				players.put(counter, player);
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
	
		return players;
	}
	
	//for one player
	public spark.Player getPlayer(String id, Request req, Response res)
	{
		//make sure the string can be parsed as an int
		int intid = -1;
		try {
			intid = Integer.parseInt(id);

		}catch(NumberFormatException e) {
			res.redirect("/500",500);
		}
		
		Connection con = null;
		Player dummy = null;
		boolean error = true;
		
		//try catch block
		//catch is cant connect to mysql
		try 
		{
			//connect to mySQL
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url,user,pass);
			Statement state  = con.createStatement();
			ResultSet results = state.executeQuery("SELECT * FROM players");
			while(results.next())
			{
				
				Player player = new Player();
				
				//if the int from the url equals a int on the table
				//then return the player
				if(intid == results.getInt(1))
				{
					player.setId(results.getInt(1));
					player.setName(results.getString(2));
					player.setAppearances(results.getInt(3));
					player.setGoals(results.getInt(4));
					player.setAssits(results.getInt(5));
					player.setCleansheets(results.getInt(6));
					player.setTeam_id(results.getInt(7));
					dummy = player;
					error = false;
					
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
		
		return dummy;
	}
	
	public String createPlayer(Request req, Response res)
	{
		String json = null;
		
		String myplayer = req.body().toString();
		
		Player player;
		player = gson.fromJson(myplayer, Player.class);
		
		String sqlProfile = "INSERT INTO players (id,name,appearances,goals,assits,cleansheets,team_id)" + 
		"VALUES('" + player.id + "' , '"+player.name+ "' , '"+player.appearances+ "' ,'"+player.goals+"', '"+player.assits+"' , '"+ player.cleansheets+"' , '"+ player.team_id+"')";
		//send to mySQL
		Connection con = null;
		try 
		{
			//connect to mySQL
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url,user,pass);
			Statement st = con.createStatement();
			Statement check = con.createStatement();
			
			ResultSet CHECKmysql = check.executeQuery("SELECT id FROM players WHERE id='"+player.id+"'");
			
			//check if a id already exists
			if(CHECKmysql.next())
			{
				json = "{ \"type\": \"error\", \"id\": " + player.id + " }";
			}
			else 
			{
				//if a id doesnt exist then make the player
				int m = st.executeUpdate(sqlProfile);
				
				if(m == 1)
				{
					json = "{ \"type\": \"success\", \"id\": " + player.id + " }";
				}
			}
					
			con.close();
					
		} catch (SQLException | ClassNotFoundException e) {
					
			e.printStackTrace();
		}
		
		return json;
	}
	
	//update a player
	public String updatePlayer(String id, Request req, Response res)
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
		
		//get from the body json
		String myplayer = req.body().toString();
		
		Player player;
		player = gson.fromJson(myplayer, Player.class);
		
		//if the id exists
				//update the player
				Connection con = null;
				try 
				{
					//connect to mySQL
					Class.forName("com.mysql.cj.jdbc.Driver");
					con = DriverManager.getConnection(url,user,pass);
					Statement ID  = con.createStatement();
					Statement name  = con.createStatement();
					Statement appear  = con.createStatement();
					Statement goals  = con.createStatement();
					Statement assits  = con.createStatement();
					Statement cleansheets  = con.createStatement();
					Statement teamid  = con.createStatement();
					
					Statement st = con.createStatement();
					
					ResultSet result = st.executeQuery("SELECT EXISTS(SELECT * FROM players WHERE id='"+intid+"')");
					
					Statement check = con.createStatement();
					ResultSet CHECKmysql = check.executeQuery("SELECT id FROM players WHERE id='"+player.id+"'");
					
					//check if a id already exists
					if(CHECKmysql.next() && intid != player.id)
					{
						error = true;
					}
					else
					{
						//if from the profile a parameter is null then we dont update it
						//if there is something in the body, then we update
						result.next();
						int num = result.getInt(1);
						if(num == 1)
						{
							
							ID.executeUpdate("UPDATE players SET id='"+player.getId()+"' WHERE id ='"+intid+"'");	
		
							name.executeUpdate("UPDATE players SET name='"+player.getName()+"' WHERE id ='"+player.getId()+"'");	
						
							appear.executeUpdate("UPDATE players SET appearances='"+player.getAppearances()+"' WHERE id ='"+player.getId()+"'");	
			
							goals.executeUpdate("UPDATE players SET goals='"+player.getGoals() +"' WHERE id ='"+player.getId()+"'");
								
							assits.executeUpdate("UPDATE players SET assits='"+player.getAssits()+"' WHERE id ='"+player.getId()+"'");
		
							cleansheets.executeUpdate("UPDATE players SET cleansheets='"+player.getCleansheets()+"' WHERE id ='"+player.getId()+"'");
		
							teamid.executeUpdate("UPDATE players SET team_id='"+player.getTeam_id()+"' WHERE id ='"+player.getId()+"'");
									
							error = false;
						}
					}
					
							
					//if a parameter got updated then say it was a success
					if(error == false)
					{
						json = "{ \"type\": \"success\", \"id\": " + player.getId() + " }";
					}
					else
						json = "{ \"type\": \"error\", \"id\": " + intid + " }";
											
					con.close();
				
				}catch (SQLException | ClassNotFoundException e) {
					
					e.printStackTrace();
				}	
		
		return json;
	}
	
	//delete a player
	public String deletePlayer(String id, Request req, Response res)
	{
		String json = null;
		
		//make sure id can be parsed
		int intid = -1;
		try {
			intid = Integer.parseInt(id);

		}catch(NumberFormatException e) {
			res.redirect("/500",500);
		}
		
		String delete = "DELETE FROM players WHERE id = '"+intid+"' ";
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
