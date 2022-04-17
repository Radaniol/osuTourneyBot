package radaniol.tourneybot;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class MainFrame extends JFrame {
    
    public SettingsPanel settingsTab;
    public TeamPanel teamsTab;
    public JPanel bracketTab;
    public MappoolPanel mappoolTab;
    public IRCPanel ircTab;

    public JTabbedPane tabs;
    
    // Defaults without a config file
    public int matchTeamSize = 2;
    public int tourneySize = 16;
    public int teamSize = 2;
    public String eliminationType = "Double Elimination";
    public boolean bracketReset = false;
    
    public Team[] teams = Team.parseTeamArray(new String[][] {{"Team 1", "", ""},{"Team 2", "", ""},{"Team 3", "", ""},{"Team 4", "", ""}});
    
    public Mappool[] mappools = {new Mappool("Mappool 1", new OsuMap[] {new OsuMap("DECO*27 ft. Hatsune Miku", "First Storm", Mod.NM, 1480224), new OsuMap("DECO*27 ft. Hatsune Miku", "First Storm", Mod.HD, 1480224)})};
    
    public ArrayList<Match> matches = new ArrayList<Match>(4);
    
    public MainFrame() {
        super("TourneyBot");
        setSize(1200, 600);
        
        // Adds behavior when the user attempts to close the window
        this.addWindowListener(new WindowAdapter() {
        	public void windowClosing(WindowEvent we) {
        		writeConfigFile();
        		System.exit(0);
        	}
        });
        
        readConfigFile();
        writeConfigFile();
        setupSettingsTab();
        setupTeamsTab();
        setupBracketTab();
        setupMappoolTab();
        setupIRCTab();
        tabs = setupTabs();
        add(tabs);
        
        setVisible(true);
    }
    
	public void setupSettingsTab() {
        settingsTab = new SettingsPanel(matchTeamSize, tourneySize, teamSize, eliminationType, bracketReset);
    }
    
    public void setupTeamsTab() {
        teamsTab = new TeamPanel();
    }
    
    public void setupBracketTab() {
        bracketTab = new JPanel();
    }
    
    public void setupMappoolTab() {
        mappoolTab = new MappoolPanel();
    }
    
    public void setupIRCTab() {
        ircTab = new IRCPanel();
    }
    
    public JTabbedPane setupTabs() {       
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Settings", settingsTab);
        tabs.addTab("Teams", teamsTab);
        tabs.addTab("Bracket", bracketTab);
        tabs.addTab("Mappool", mappoolTab);   
        tabs.addTab("IRC", ircTab);
        return tabs;
    }
    
    public void receiveIRCInput(String msg) {
    	String[] splitArray = msg.split(" ");
    	// ircTab.ircChat.append(msg);
    	if (!(splitArray.length >= 2 && (splitArray[1].equals("JOIN") || splitArray[1].equals("QUIT") || splitArray[1].equals("PART")))) {
    		ircTab.ircChat.append(msg);
    	}
        if (splitArray[0].equals("PING")) {
            Container.getInstance().ircc.getPrintStream().print("PONG " + splitArray[1]);
            ircTab.ircChat.append("PONG " + splitArray[1]);
        }
        if (splitArray.length >= 3) {
        	if (splitArray[1].equals("PRIVMSG") && splitArray[2].equals("Radaniol")) {
        		if (splitArray[0].equals(":BanchoBot!cho@ppy.sh")) {
        			if (splitArray[3].equals(":Created")) {
        				//TODO: rework for schedule
        				Team blueTeam = null, redTeam = null;
        				for (Team t : teams) {
        					if (t.teamNames[0].equals(splitArray[8].substring(1, splitArray[8].length()))) {
        						blueTeam = t;
        					} else if (t.teamNames[0].equals(splitArray[10].substring(0, splitArray[10].length() - 2))) { // - )*
        						redTeam = t;
        					}
        				}
        				System.out.println((blueTeam == null) + " " + (redTeam == null));
        				System.out.println(splitArray[10]);
        				System.out.println(splitArray[10].length());
        				matches.add(new Match(blueTeam, redTeam, mappools[0], new Channel("mp_" + splitArray[7].substring(22, splitArray[7].length()), Container.getInstance().ircc.getPrintStream()), 2, 7));
        			}
        		} else {
        			// TODO: Log player feedback
        		}       		
        	} else if (splitArray[2].substring(0, 4).equals("#mp_")) {
        		for (Iterator it = matches.iterator(); it.hasNext(); ) {
        			Match match = (Match) it.next();
        			if (splitArray[2].substring(1, splitArray[2].length()).equals(match.channel.name)) {
        				match.receiveInput(msg);
        			}
        		}
        	}
        }
    }
    
    public void writeConfigFile(){
        BufferedWriter bw = null;
        try {
            File configFile = new File("tourneyConfig");
            //System.out.println(configFile.getCanonicalPath());
            bw = new BufferedWriter(new FileWriter(configFile));
            
            // Write variables in order
            bw.write(Integer.toString(matchTeamSize)); bw.newLine();
            bw.write(Integer.toString(tourneySize)); bw.newLine();
            bw.write(Integer.toString(teamSize)); bw.newLine();
            bw.write(eliminationType); bw.newLine();
            bw.write(Boolean.toString(bracketReset)); bw.newLine();
            
            for (int i = 0; i < teams.length; i++) {
                for (int j = 0; j < teams[0].teamNames.length; j++) {
                    bw.write(teams[i].teamNames[j]); bw.newLine();
                } 
            }
            
            bw.write(Integer.toString(mappools.length)); bw.newLine();
            
            for (int i = 0; i < mappools.length; i++) {
            	bw.write(Integer.toString(mappools[i].maps.length)); bw.newLine();
            	bw.write(mappools[i].name); bw.newLine();
            	for (int j = 0; j < mappools[i].maps.length; j++) {
            		bw.write(mappools[i].maps[j].artist); bw.newLine();
            		bw.write(mappools[i].maps[j].name); bw.newLine();
            		bw.write(mappools[i].maps[j].mod.toString()); bw.newLine();
            		bw.write(Integer.toString(mappools[i].maps[j].id)); bw.newLine();
            	}
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                bw.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }      
    }
    
    public void readConfigFile() {
        BufferedReader br = null;
        try {
            File configFile = new File("tourneyConfig");
            if (configFile.exists()) {
                //System.out.println(configFile.getCanonicalPath());
                br = new BufferedReader(new FileReader(configFile));

                matchTeamSize = Integer.parseInt(br.readLine());
                tourneySize = Integer.parseInt(br.readLine());
                teamSize = Integer.parseInt(br.readLine());
                eliminationType = br.readLine();
                bracketReset = Boolean.parseBoolean(br.readLine());
                
                String[][] teamNames = new String[tourneySize][teamSize + 1];
                for (int i = 0; i < teamNames.length; i++) {
                    for (int j = 0; j < teamNames[0].length; j++) {
                        teamNames[i][j] = br.readLine();
                    } 
                }
                teams = Team.parseTeamArray(teamNames);
                
                mappools = new Mappool[Integer.parseInt(br.readLine())];
                
                for (int i = 0; i < mappools.length; i++) {
                	OsuMap[] maps = new OsuMap[Integer.parseInt(br.readLine())];
                	String mappoolName = br.readLine();
                	mappools[i] = new Mappool(mappoolName, maps);
                	for (int j = 0; j < mappools[i].maps.length; j++) {
                		String artist = br.readLine();
                		String name = br.readLine();
                		Mod mod = Mod.valueOf(br.readLine());
                		int id = Integer.parseInt(br.readLine());
                		mappools[i].maps[j] = new OsuMap(artist, name, mod, id);
                	}
                }
            } else {
            	writeConfigFile();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (br != null) {
                  br.close();  
                }               
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
    
    public void refreshTeamArray() {   	
    	// Copying the old values
    	String[][] newArray = new String[tourneySize][teamSize + 1];
    	for (int i = 0; i < Math.min(newArray.length, teams.length); i++) {
    		for (int j = 0; j < Math.min(newArray[0].length, teams[0].teamNames.length); j++) {
    			newArray[i][j] = teams[i].teamNames[j];
    		}
    	}
    	
    	//Padding extra teams
    	if (tourneySize > teams.length) {
    		for (int i = teams.length; i < tourneySize; i++) {
    			newArray[i][0] = "Team " + (i + 1);
    			for (int j = 1; j < (teamSize + 1); j++) {
    				newArray[i][j] = "";
    			}
    		}
    	}
    	
    	//Padding extra players
    	if (teamSize + 1 > teams[0].teamNames.length) {
    		for (int i = 0; i < tourneySize; i++) {
    			for (int j = teams[0].teamNames.length; j < teamSize + 1; j++) {
    				newArray[i][j] = "";
    			}
    		}
    	}
    	teams = Team.parseTeamArray(newArray);
    }
    
    public void refreshTab(TabPanel tab) {
    	tab.removeAll();
    	tab.refresh();
    	tab.revalidate();
    	tab.repaint();
    }
    
    public void refreshAll() {
    	refreshTab(teamsTab);
    	refreshTab(mappoolTab);
    }

	public void addMappool() {
		Mappool[] newArray = new Mappool[mappools.length + 1];
		for (int i = 0; i < mappools.length; i++) {
			newArray[i] = mappools[i];
		}
		newArray[mappools.length] = new Mappool("Mappool " + (newArray.length), new OsuMap[] {new OsuMap("Artist", "Map Name", Mod.NM, 0)});
		mappools = newArray;
	}

	public void deleteMappool(int currentMappoolIndex) {
		if (mappools.length > 1) {
			Mappool[] newArray = new Mappool[mappools.length - 1];
			for (int i = 0; i < mappools.length - 1; i++) {
				newArray[i] = mappools[i + ((i >= currentMappoolIndex) ? 1 : 0)];
			}
			mappools = newArray;
		}	
	}

	public void addMap(int currentMappoolIndex) {
		Mappool temp = mappools[currentMappoolIndex];
		mappools[currentMappoolIndex] = new Mappool(mappools[currentMappoolIndex].name, new OsuMap[temp.maps.length + 1]);
		for (int i = 0; i < temp.maps.length; i++) {
			mappools[currentMappoolIndex].maps[i] = temp.maps[i];
		}
		mappools[currentMappoolIndex].maps[temp.maps.length] = new OsuMap("Artist", "Map Name", Mod.NM, 0);
	}

	public void deleteMap(int currentMappoolIndex) {
		if (mappools[currentMappoolIndex].maps.length > 1) {
			Mappool temp = mappools[currentMappoolIndex];
			mappools[currentMappoolIndex] = new Mappool(mappools[currentMappoolIndex].name, new OsuMap[temp.maps.length - 1]);
			for (int i = 0; i < temp.maps.length - 1; i++) {
				mappools[currentMappoolIndex].maps[i] = temp.maps[i];
			}	
		}		
	}
}