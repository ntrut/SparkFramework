package spark;

import static spark.Spark.*;
import com.google.gson.*;

public class test 
{
	
    public static void main(String[] args) 
    {
    	Gson gson = new Gson();
    	
        //profile webs
        get("/Project/profile", (req,res) -> {
        	ProfileSpark spark = new ProfileSpark();
        			return gson.toJson(spark.getProfiles(req, res));
        	
         });
        
        //get one profile
        get("/Project/profile/:id", (req,res) -> {
        	ProfileSpark spark = new ProfileSpark();
        	String id = req.params(":id");      	
        	return gson.toJson(spark.getProfile(id, req, res));
			 
        	
         });
        
        //get a profile favteam
        get("/Project/profile/:id/favteam", (req,res) -> {
        	ProfileSpark spark = new ProfileSpark();
        	String id = req.params(":id");      	
        	return gson.toJson(spark.getFavTeam(id, req, res));
         });
        
        //create a profile
        post("/Project/profile", (req,res) -> {
        	ProfileSpark spark = new ProfileSpark();
        	res.type("application/json");
        	return gson.toJson(spark.createProfile(req, res));
        });
        
        //edit a profile
        put("/Project/profile/:id", (req,res) -> {
        	ProfileSpark spark = new ProfileSpark();
        	String id = req.params(":id"); 
        	res.type("application/json");
        	return gson.toJson(spark.updateProfile(id, req, res));
        });
        
        //delete a profile
        delete("/Project/profile/:id", (req,res) -> {
        	ProfileSpark spark = new ProfileSpark();
        	res.type("application/json");
        	String id = req.params(":id");
        	return gson.toJson(spark.deleteProfile(id,req, res));
        });
        
        //players webs
        
        //for all players
        get("/Project/player", (req,res) -> {
        	PlayerSpark player = new PlayerSpark();
        			return gson.toJson(player.getPlayers(req, res));
        });
        			
        //for one players
        get("/Project/player/:id", (req,res) -> {
        	PlayerSpark player = new PlayerSpark();
        	String id = req.params(":id");      	
        	return gson.toJson(player.getPlayer(id, req, res));
        });
        
        //create a player
        post("/Project/player", (req,res) -> {
        	PlayerSpark player = new PlayerSpark();
        	res.type("application/json");
        	return gson.toJson(player.createPlayer(req, res));
        });
        
        //update a player
        put("/Project/player/:id", (req,res) -> {
        	PlayerSpark player = new PlayerSpark();
        	String id = req.params(":id"); 
        	res.type("application/json");
        	return gson.toJson(player.updatePlayer(id, req, res));
        });
        
        //delete a player
        delete("/Project/player/:id", (req,res) -> {
        	PlayerSpark player = new PlayerSpark();
        	res.type("application/json");
        	String id = req.params(":id");
        	return gson.toJson(player.deletePlayer(id,req, res));
        });
        
        //TEAM
        
        //get all the teams
        get("/Project/team", (req,res) -> {
        	TeamSpark team = new TeamSpark();
        			return gson.toJson(team.getTeams(req, res));
        });
        
        //get on team
        get("/Project/team/:id", (req,res) -> {
        	TeamSpark team = new TeamSpark();
        	String id = req.params(":id");
        			return gson.toJson(team.getTeam(id,req, res));
        });
        
        //get a teams players
        get("/Project/team/:id/player", (req,res) -> {
        	TeamSpark team = new TeamSpark();
        	String id = req.params(":id");
        			return gson.toJson(team.getTeamPlayers(id, req, res));
        });
        		
      //create a team
        post("/Project/team", (req,res) -> {
        	TeamSpark team = new TeamSpark();
        	res.type("application/json");
        	return gson.toJson(team.createTeam(req, res));
        });
        
      //delete team
        delete("/Project/team/:id", (req,res) -> {
        	TeamSpark team = new TeamSpark();
        	String id = req.params(":id");
        			return gson.toJson(team.deleteTeam(id, req, res));
        });
        
      //update a team
        put("/Project/team/:id", (req,res) -> {
        	TeamSpark team = new TeamSpark();
        	String id = req.params(":id"); 
        	res.type("application/json");
        	return gson.toJson(team.updateTeam(id, req, res));
        });
        
        //LEAGUES
        //for all leagues
        get("/Project/leagues", (req,res) -> {
        	LeagueSpark spark = new LeagueSpark();
        			return gson.toJson(spark.getLeagues(req, res));
        			
        });
        
        //get one league
        get("/Project/leagues/:id", (req,res) -> {
        	LeagueSpark spark = new LeagueSpark();
        	String id = req.params(":id"); 
        			return gson.toJson(spark.getLeague(id,req, res));
        			
        });
        
      //create a league
        post("/Project/leagues", (req,res) -> {
        	LeagueSpark spark = new LeagueSpark();
        			return gson.toJson(spark.createLeague(req, res));
        			
        });
        
        //delete League
        delete("/Project/leagues/:id", (req,res) -> {
        	LeagueSpark spark = new LeagueSpark();
        	String id = req.params(":id");
        			return gson.toJson(spark.deleteLeague(id, req, res));
        });
        
        //update a league
        put("/Project/leagues/:id", (req,res) -> {
        	LeagueSpark spark = new LeagueSpark();
        	String id = req.params(":id"); 
        			return gson.toJson(spark.updateLeague(id,req, res));
        });
        
    }

}
