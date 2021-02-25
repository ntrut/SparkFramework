package spark;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.TimeZone;

import com.google.gson.Gson;

public class LeagueSpark 
{
	private String url = "jdbc:mysql://localhost:3306/homework?serverTimezone=" + TimeZone.getDefault().getID();
	private String user = "root";
	private String pass = "linuxserver";
	private Gson gson = new Gson();
	private HashMap<Integer, League> leagues  = new HashMap<Integer, League>();
	
	//get all leagues, doGet
	public HashMap<Integer, League> getLeagues(Request req, Response res)
	{
		//retrieve all of the leagues from mySQL
				Connection con = null;
				try 
				{
					//connect to mySQL
					Class.forName("com.mysql.cj.jdbc.Driver");
					con = DriverManager.getConnection(url,user,pass);
					Statement state  = con.createStatement();
					ResultSet results = state.executeQuery("SELECT * FROM Leagues");
					int counter = 0;
					while(results.next())
					{
						
						League league = new League();
						
						league.setId(results.getInt(1));
						league.setName(results.getString(2));
						league.setNumTeams(results.getInt(3));
						
						leagues.put(counter, league);
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
				
				return leagues;
	}
	
	//get one league but with all the teams
	public League getLeague(String id, Request req, Response res)
	{
		//make sure the number is a number
		int intid = -1;
		try {
			intid = Integer.parseInt(id);

		}catch(NumberFormatException e) {
			res.redirect("/500",500);
		}
		
		//create connection to mysql
				Connection con = null;
				League sendtoJson= null;
				boolean error = true;
				try 
				{
					//connect to mySQL
					Class.forName("com.mysql.cj.jdbc.Driver");
					con = DriverManager.getConnection(url,user,pass);
					Statement state  = con.createStatement();
					Statement state2  = con.createStatement();
					
					//find the league in mySQL
					ResultSet results = state.executeQuery("SELECT * FROM Leagues WHERE id='"+intid+"'");
					while(results.next())
					{
						
						League league = new League();
						error = false;
						//set that object league 
						league.setId(results.getInt(1));
						league.setName(results.getString(2));
						league.setNumTeams(results.getInt(3));
						STeam[] teams = new STeam[league.getNumTeams()];
						
						//get all the players that are associated with the teams
						ResultSet rs = state2.executeQuery("SELECT * FROM teams WHERE league_id='"+intid+"'");
						
						int i = 0;
						while(rs.next())
						{
							STeam team = new STeam();
							
							//set that team to team object so we can Json it
							team.setId(rs.getInt(1));
							team.setName(rs.getString(2));
							team.setFounded(rs.getInt(3));
							team.setMatchesPlayed(rs.getInt(4));
							team.setWins(rs.getInt(5));
							team.setLoses(rs.getInt(6));
							team.setGoals(rs.getInt(7));
							team.setGoalsconceded(rs.getInt(8));
							team.setGoalsConcededperMatch(rs.getDouble(9));
							team.setGoalsperMatch(rs.getDouble(10));
							team.setLeagueposition(rs.getInt(11));
							team.setLeague_id(rs.getInt(12));
							
							teams[i] = team;
							i++;
						}
						STeam[] truearray = new STeam[i];
						
						//create a new array so it doesnt show all the nulls 
						int j = 0;
						while(teams[j] != null)
						{
							truearray[j] = teams[j];
							j++;
						}
						
						//set the teams array to the team parameter Teams[]
						league.setTeams(truearray);
						
						//sendtoJson == team object so we can send it as json
						sendtoJson = league;
						
					}
					
					if(error == true)
					{
						res.redirect("/404",404);
					}
					
					con.close();
					
				} catch (SQLException | ClassNotFoundException e) {
					
					e.printStackTrace();
				}
				
		//return a league obejct
		return sendtoJson;
		
	}
		
	
	//create League, doPost
	public String createLeague(Request req, Response res)
	{
		String json = null;
		
		String myleague = req.body().toString();
		League league;
		league = gson.fromJson(myleague, League.class);
		
		String sqlProfile = "INSERT INTO Leagues (id,name,numTeams)" + "VALUES('" + league.id + "' , '"+league.name+ "', '"+league.numTeams+ "')";
		//send to mySQL
		Connection con = null;
		try 
		{
			//connect to mySQL
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url,user,pass);
			Statement st = con.createStatement();
			Statement check = con.createStatement();
			
			ResultSet CHECKmysql = check.executeQuery("SELECT id FROM Leagues WHERE id='"+league.id+"'");
			
			//check if a id already exists
			if(CHECKmysql.next())
			{
				json = "{ \"type\": \"error\", \"id\": " + league.id + " }";
			}
			else 
			{
				//if a id doesnt exist then make the league
				int m = st.executeUpdate(sqlProfile);
				
				if(m == 1)
				{
					json = "{ \"type\": \"success\", \"id\": " + league.id + " }";
				}
			}
			
			con.close();
			
		} catch (SQLException | ClassNotFoundException e) {
			
			e.printStackTrace();
		}
	
	return json;	
	}
	
	
	public String updateLeague(String id, Request req, Response res)
	{
		String json = null;
		boolean error = true;
		
		int intid = -1;
		try {
			intid = Integer.parseInt(id);

		}catch(NumberFormatException e) {
			res.redirect("/500",500);
		}
		
		String myleague = req.body().toString();
		League league;
		league = gson.fromJson(myleague, League.class);
		
		//if the id exists
				//update the league
				Connection con = null;
				try 
				{
					//connect to mySQL
					Class.forName("com.mysql.cj.jdbc.Driver");
					con = DriverManager.getConnection(url,user,pass);
					Statement ID  = con.createStatement();
					Statement name  = con.createStatement();
					Statement numTeams  = con.createStatement();
						
					Statement st = con.createStatement();
					
					ResultSet result = st.executeQuery("SELECT EXISTS(SELECT * FROM Leagues WHERE id='"+intid+"')");
					
					Statement check = con.createStatement();
					ResultSet CHECKmysql = check.executeQuery("SELECT id FROM Leagues WHERE id='"+league.id+"'");
					
					//check if a id already exists
					if(CHECKmysql.next() && intid != league.id)
					{
						error = true;
					}
					else
					{
						//if there is something in the body, then we update
						//if result is 1 then there exists a id
						result.next();
						int num = result.getInt(1);
						if(num == 1)
						{
							
							ID.executeUpdate("UPDATE Leagues SET id='"+league.getId()+"' WHERE id ='"+intid+"'");	
								 
							name.executeUpdate("UPDATE Leagues SET name='"+league.getName()+"' WHERE id ='"+league.getId()+"'");	
								
							numTeams.executeUpdate("UPDATE Leagues SET numTeams='"+league.getNumTeams()+"' WHERE id ='"+league.getId()+"'");	

							error = false;
						}
					}
					
					
							
					//if a parameter got updated then say it was a success
					if(error == false)
					{
						json = "{ \"type\": \"success\", \"id\": " + league.getId() + " }";
					}
					else
						json = "{ \"type\": \"error\", \"id\": " + intid + " }";
											
					con.close();
				
				}catch (SQLException | ClassNotFoundException e) {
					
					e.printStackTrace();
				}
		
		return json;
		
	}
	//delete a League, doDelete
	public String deleteLeague(String id, Request req, Response res)
	{
		String json = null;
		
		//make sure the number is a number
		int intid = -1;
		try {
			intid = Integer.parseInt(id);

		}catch(NumberFormatException e) {
			res.redirect("/500",500);
		}
		
		//delete from mysql
		String delete = "DELETE FROM Leagues WHERE id = '"+intid+"' ";
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
				json = "{ \"type\": \"error\", \"id\": " + intid + "}";
			
			con.close();
			
		} catch (SQLException | ClassNotFoundException e) {
			
			e.printStackTrace();
		}
		
		
		return json;
	}
}
