package radaniol.tourneybot;

public class OsuMap {
	
	public String artist;
	public String name;
	public int id;
	public Mod mod;
	
	public OsuMap(String artist, String name, Mod mod, int id) {
		this.artist = artist;
		this.name = name;
		this.id = id;
		this.mod = mod;
	}
}
