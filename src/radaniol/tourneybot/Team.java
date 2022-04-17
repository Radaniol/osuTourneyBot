package radaniol.tourneybot;

public class Team {
	
	public String[] teamNames;
	
	public Team(String[] teamNames) {		
		this.teamNames = teamNames;
	}
	
	public static Team[] parseTeamArray(String[][] teamNames) {
		Team[] result = new Team[teamNames.length];
		for (int i = 0; i < teamNames.length; i++) {
			result[i] = new Team(teamNames[i]);
		}		
		return result;
	}
}
