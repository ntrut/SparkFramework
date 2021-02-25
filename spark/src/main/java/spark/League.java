package spark;

public class League 
{
	protected String name;
	protected int id;
	protected int numTeams;
	protected STeam teams[];
	
	public League()
	{
		
	}
	
	//without teams
	public League(String name, int id, int numTeams) 
	{
		super();
		this.name = name;
		this.id = id;
		this.numTeams = numTeams;
	}
	
	
	//with teams
	public League(String name, int id, int numTeams, STeam[] teams) {
		super();
		this.name = name;
		this.id = id;
		this.numTeams = numTeams;
		this.teams = teams;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getNumTeams() {
		return numTeams;
	}
	public void setNumTeams(int numTeams) {
		this.numTeams = numTeams;
	}
	public STeam[] getTeams() {
		return teams;
	}
	public void setTeams(STeam[] teams) {
		this.teams = teams;
	}
	
	
}
