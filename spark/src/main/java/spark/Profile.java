package spark;

public class Profile 
{
	int id;
	String username;
	String lastname;
	String favTeam;
	String age;
	
	
	
	public Profile() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Profile(int id, String username, String lastname, String favTeam, String age) {
		super();
		this.id = id;
		this.username = username;
		this.lastname = lastname;
		this.favTeam = favTeam;
		this.age = age;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getFavTeam() {
		return favTeam;
	}
	public void setFavTeam(String favTeam) {
		this.favTeam = favTeam;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	
	
	
}
