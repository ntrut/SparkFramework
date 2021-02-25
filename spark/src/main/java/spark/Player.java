package spark;

public class Player 
{
	protected int id;
	protected String name;
	protected int appearances;
	protected int goals;
	protected int assits;
	protected int cleansheets;
	protected int team_id;
	
	public Player()
	{
		
	}
	
	public Player(int id, String name,int appearances, int goals, int assits, int cleansheets, int team_id) {
		super();
		this.id = id;
		this.name = name;
		this.appearances = appearances;
		this.goals = goals;
		this.assits = assits;
		this.cleansheets = cleansheets;
		this.team_id = team_id;
	}
	
	public int getAppearances() {
		return appearances;
	}

	public void setAppearances(int appearances) {
		this.appearances = appearances;
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
	public int getGoals() {
		return goals;
	}
	public void setGoals(int goals) {
		this.goals = goals;
	}
	public int getAssits() {
		return assits;
	}
	public void setAssits(int assits) {
		this.assits = assits;
	}
	public int getCleansheets() {
		return cleansheets;
	}
	public void setCleansheets(int cleansheets) {
		this.cleansheets = cleansheets;
	}
	public int getTeam_id() {
		return team_id;
	}
	public void setTeam_id(int team_id) {
		this.team_id = team_id;
	}
	
	
	
}
