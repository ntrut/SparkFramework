package spark;

public class STeam 
{
	protected int id;
	protected String name;
	protected int founded;
	protected int matchesPlayed;
	protected int wins;
	protected int loses;
	protected int goals;
	protected int goalsconceded;
	protected double GoalsperMatch;
	protected double GoalsConcededperMatch;
	protected int leagueposition;
	protected int league_id;
	protected Player player[];
	
	
	
	public STeam()
	{
		
	}
	
	//constructor without player array
	public STeam(int id, String name, int founded, int matchesPlayed, int wins, int loses, int goals, int goalsconceded,
		double goalsperMatch, double goalsConcededperMatch,int leagueposition, int league_id) {
		super();
		this.id = id;
		this.name = name;
		this.founded = founded;
		this.matchesPlayed = matchesPlayed;
		this.wins = wins;
		this.loses = loses;
		this.goals = goals;
		this.goalsconceded = goalsconceded;
		this.GoalsperMatch = goalsperMatch;
		this.GoalsConcededperMatch = goalsConcededperMatch;
		this.league_id = league_id;
		this.leagueposition = leagueposition;
	}
	
	
	//constructor with player array
	public STeam(int id, String name, int founded, int matchesPlayed, int wins, int loses, int goals, int goalsconceded,
			double goalsperMatch, double goalsConcededperMatch, int leagueposition, int league_id, Player[] player) {
		super();
		this.id = id;
		this.name = name;
		this.founded = founded;
		this.matchesPlayed = matchesPlayed;
		this.wins = wins;
		this.loses = loses;
		this.goals = goals;
		this.goalsconceded = goalsconceded;
		this.GoalsperMatch = goalsperMatch;
		this.GoalsConcededperMatch = goalsConcededperMatch;
		this.league_id = league_id;
		this.leagueposition = leagueposition;
		this.player = player;
	}


	public int getLeagueposition() {
		return leagueposition;
	}

	public void setLeagueposition(int leagueposition) {
		this.leagueposition = leagueposition;
	}

	public int getLeague_id() {
		return league_id;
	}

	public void setLeague_id(int league_id) {
		this.league_id = league_id;
	}

	public int getMatchesPlayed() {
		return matchesPlayed;
	}
	public void setMatchesPlayed(int matchesPlayed) {
		this.matchesPlayed = matchesPlayed;
	}
	public int getWins() {
		return wins;
	}
	public void setWins(int wins) {
		this.wins = wins;
	}
	public int getLoses() {
		return loses;
	}
	public void setLoses(int loses) {
		this.loses = loses;
	}
	public int getGoalsconceded() {
		return goalsconceded;
	}
	public void setGoalsconceded(int goalsconceded) {
		this.goalsconceded = goalsconceded;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getFounded() {
		return founded;
	}
	public void setFounded(int founded) {
		this.founded = founded;
	}
	public int getGoals() {
		return goals;
	}
	public void setGoals(int goals) {
		this.goals = goals;
	}
	public double getGoalsperMatch() {
		return GoalsperMatch;
	}
	public void setGoalsperMatch(double goalsperMatch) {
		GoalsperMatch = goalsperMatch;
	}
	public double getGoalsConcededperMatch() {
		return GoalsConcededperMatch;
	}
	public void setGoalsConcededperMatch(double goalsConcededperMatch) {
		GoalsConcededperMatch = goalsConcededperMatch;
	}
	public Player[] getPlayer() {
		return player;
	}
	public void setPlayer(Player[] player) {
		this.player = player;
	}
	
	
	
	
	
}
