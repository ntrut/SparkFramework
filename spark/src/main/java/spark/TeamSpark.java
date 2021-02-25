package spark;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.TimeZone;

import com.google.gson.Gson;


public class TeamSpark 
{
	private String url = "jdbc:mysql://localhost:3306/homework?serverTimezone=" + TimeZone.getDefault().getID();
	private String user = "root";
	private String pass = "linuxserver";
	private Gson gson = new Gson();
	private HashMap<Integer, Player> players  = new HashMap<Integer, Player>();
	private HashMap<Integer, STeam> teams  = new HashMap<Integer, STeam>();
	
	//get all the teams
	public HashMap<Integer, spark.STeam> getTeams(Request req, Response res)
	{
		Connection con = null;
		try 
		{
			//connect to mySQL
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url,user,pass);
			Statement state  = con.createStatement();
			Statement state2  = con.createStatement();
			ResultSet results = state.executeQuery("SELECT * FROM teams");
			
			int counter = 0;
			while(results.next())
			{
				
				STeam team = new STeam();
				
				Player[] playerarray = new Player[24];
				
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
				team.setLeagueposition(results.getInt(11));
				team.setLeague_id(results.getInt(12));
				
				//get all the players that are associated with the teams
				int test = team.getId();
				ResultSet rs = state2.executeQuery("SELECT * FROM players WHERE team_id= '" + test + "'");
				
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
				
				//put the array of players into the team parameter Player[]
				team.setPlayer(truearray);
				
				//put team in the hashmap of teams
				//hashmap stores all the teams
				teams.put(counter, team);
				counter++;
			}
			
			con.close();
			
		} catch (SQLException | ClassNotFoundException e) {
			
			e.printStackTrace();
		}
		
		return teams;
	}
	
	public spark.STeam getTeam(String id, Request req, Response res)
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
		STeam sendtoJson= null;
		boolean error = true;
		try 
		{
			//connect to mySQL
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url,user,pass);
			Statement state  = con.createStatement();
			Statement state2  = con.createStatement();
			
			//find the team in mySQL
			ResultSet results = state.executeQuery("SELECT * FROM teams WHERE id='"+intid+"'");
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
				team.setLeagueposition(results.getInt(11));
				team.setLeague_id(results.getInt(12));
				error = false;	
				
				
				//get all the players that are associated with the teams
				ResultSet rs = state2.executeQuery("SELECT * FROM players WHERE team_id='"+intid+"'");
				
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
				
				//set the player array to the team parameter Player[]
				team.setPlayer(truearray);
				
				//sendtoJson == team object so we can send it as json
				sendtoJson = team;
				
			}
			
			if(error == true)
			{
				res.redirect("/404",404);
			}
			
			con.close();
			
		} catch (SQLException | ClassNotFoundException e) {
			
			e.printStackTrace();
		}
		
		//return a team obejct
		return sendtoJson;
		
	}
	
	//this is for the url of /team/2/player
	//GETS ALL THE PLAYERS FROM THAT SPECIFIC TEAM
	public HashMap<Integer, Player> getTeamPlayers(String id, Request req, Response res)
	{
		
		//make sure the number is a number
		int intid = -1;
		try {
			intid = Integer.parseInt(id);

		}catch(NumberFormatException e) {
			res.redirect("/500",500);
		}
		
		Connection con = null;
		try 
		{
			//connect to mySQL
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url,user,pass);
			Statement state  = con.createStatement();
			ResultSet results = state.executeQuery("SELECT * FROM players WHERE team_id='"+intid+"'");
			int counter = 0;
			boolean error = true;
			
			/*get all the players that have the same team_id
			/as the teams id*/
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
				error = false;
			}
			
			if(error == true)
			{
				res.redirect("/404",404);
			}
			
			
			con.close();
			
		} catch (SQLException | ClassNotFoundException e) {
			
			e.printStackTrace();
		}
		
		return players;
	}
	
	//do post that creates a team
	public String createTeam(Request req, Response res)
	{
		String json = null;
		
		String myteam = req.body().toString();
		STeam team;
		team = gson.fromJson(myteam, STeam.class);
		
		String sqlProfile = "INSERT INTO teams (id,name,founded,matchesPlayed,wins,loses,goals,goalsConceded,GoalsperMatch,GoalsConcededperMatch,leaguePosition,league_id)" + 
				"VALUES('" + team.id + "' , '"+team.name+ "' , '"+team.founded+ "' , '"+team.matchesPlayed+"' , "
						+ "'"+ team.wins+"' , '"+ team.loses+"', '"+team.goals+ "' ,'"+team.goalsconceded+"', "
								+ "'"+team.GoalsperMatch+"', '"+team.GoalsConcededperMatch+"', '"+team.leagueposition+"', '"+team.league_id+"')";
				//send to mySQL
				Connection con = null;
				try 
				{
					//connect to mySQL
					Class.forName("com.mysql.cj.jdbc.Driver");
					con = DriverManager.getConnection(url,user,pass);
					Statement st = con.createStatement();
					Statement check = con.createStatement();
					
					ResultSet CHECKmysql = check.executeQuery("SELECT id FROM teams WHERE id='"+team.id+"'");
					
					//check if a id already exists
					if(CHECKmysql.next())
					{
						json = "{ \"type\": \"error\", \"id\": " + team.id + " }";
					}
					else 
					{
						//if a id doesnt exist then make the team
						int m = st.executeUpdate(sqlProfile);
						
						if(m == 1)
						{
							json = "{ \"type\": \"success\", \"id\": " + team.id + " }";
						}
					}
					
					con.close();
					
				} catch (SQLException | ClassNotFoundException e) {
					
					e.printStackTrace();
				}
				
		
		return json;
	}
	
	
	
	//delete that deletes a team
	public String deleteTeam(String id, Request req, Response res)
	{
		String json = null;
		
		//make sure the number is a number
		int teamid = -1;
		try {
			teamid = Integer.parseInt(id);

		}catch(NumberFormatException e) {
			res.redirect("/500",500);
		}
		
		//delete from mysql
		String delete = "DELETE FROM teams WHERE id = '"+teamid+"' ";
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
				json = "{ \"type\": \"success\", \"id\": " + teamid + " }";
			}
			else
				json = "{ \"type\": \"error\", \"id\": " + teamid + " }";
					
			con.close();
					
		} catch (SQLException | ClassNotFoundException e) {
					
			e.printStackTrace();
		}
		
		return json;
	}
	
	public String updateTeam(String id, Request req, Response res)
	{
		String json = null;
		boolean error = true;
		
		//make sure the number is a number
		int teamid = -1;
		try {
			teamid = Integer.parseInt(id);

		}catch(NumberFormatException e) {
			res.redirect("/500",500);
		}
		
		String myteam = req.body().toString();
		STeam team;
		team = gson.fromJson(myteam, STeam.class);
		
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
			Statement founded  = con.createStatement();
			Statement matchesplayed  = con.createStatement();
			Statement goals  = con.createStatement();
			Statement wins  = con.createStatement();
			Statement loses  = con.createStatement();
			Statement conceded  = con.createStatement();
			Statement goalspermatch  = con.createStatement();
			Statement last  = con.createStatement();
			Statement league  = con.createStatement();
			Statement leagueid  = con.createStatement();

					
			Statement st = con.createStatement();
					
			ResultSet result = st.executeQuery("SELECT EXISTS(SELECT * FROM teams WHERE id='"+teamid+"')");
			
			Statement check = con.createStatement();
			ResultSet CHECKmysql = check.executeQuery("SELECT id FROM teams WHERE id='"+team.id+"'");
			
			//check if a id already exists
			if(CHECKmysql.next() && teamid != team.id)
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
					ID.executeUpdate("UPDATE teams SET id='"+team.getId()+"' WHERE id ='"+teamid+"'");	
				
					name.executeUpdate("UPDATE teams SET name='"+team.getName()+"' WHERE id ='"+team.getId()+"'");	
				
					founded.executeUpdate("UPDATE teams SET founded='"+team.getFounded()+"' WHERE id ='"+team.getId()+"'");	
			
					matchesplayed.executeUpdate("UPDATE teams SET matchesPlayed='"+team.getMatchesPlayed()+"' WHERE id ='"+team.getId()+"'");
				
					goals.executeUpdate("UPDATE teams SET goals='"+team.getGoals()+"' WHERE id ='"+team.getId()+"'");
				
					wins.executeUpdate("UPDATE teams SET wins='"+team.getWins()+"' WHERE id ='"+team.getId()+"'");
		
					loses.executeUpdate("UPDATE teams SET loses='"+team.getLoses()+"' WHERE id ='"+team.getId()+"'");
			
					conceded.executeUpdate("UPDATE teams SET goalsConceded='"+team.getGoalsconceded()+"' WHERE id ='"+team.getId()+"'");
			
					goalspermatch.executeUpdate("UPDATE teams SET GoalsperMatch='"+team.getGoalsperMatch()+"' WHERE id ='"+team.getId()+"'");
			
					last.executeUpdate("UPDATE teams SET GoalsConcededperMatch='"+team.getGoalsConcededperMatch()+"' WHERE id ='"+team.getId()+"'");

					league.executeUpdate("UPDATE teams SET leaguePosition='"+team.getLeagueposition()+"' WHERE id ='"+teamid+"'");	
					
					leagueid.executeUpdate("UPDATE teams SET league_id='"+team.league_id+"' WHERE id ='"+teamid+"'");	
					
					error = false;
				}
			}
		
					//if a parameter got updated then say it was a success
			if(error == false)
			{
				json = "{ \"type\": \"success\", \"id\": " + team.getId() + " }";
			}
			else
				json = "{ \"type\": \"error\", \"id\":" + teamid + " }";
											
			con.close();
				
		}catch (SQLException | ClassNotFoundException e) {
					
			e.printStackTrace();
		}
		
		return json;
		
	}
	
}
