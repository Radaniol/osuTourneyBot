package radaniol.tourneybot;

import java.util.Arrays;

public class Match {
	
	public Team blueTeam, redTeam;
	public Mappool mappool;
	public Channel channel;
	
	public boolean[] availableMaps;
	public Mod blueTeamLastMod = null, redTeamLastMod = null;
	
	public int blueTeamMatchScore = 0, redTeamMatchScore = 0;
	public int blueTeamMapScore = 0, redTeamMapScore = 0;
	public int blueTeamScoreCounter = 0, redTeamScoreCounter = 0;
	public int blueTeamModCheckCounter= 0, blueTeamPlayerCheckCounter = 0, redTeamModCheckCounter = 0, redTeamPlayerCheckCounter = 0;
	public boolean blueTeamTurn;
	
	public int blueTeamRoll = -1, redTeamRoll = -1, banCounter = 0, maxBans, pickCounter = 0, maxPicks, playerCount = 0;
	public boolean matchStarted = false, rollPhaseDone = false, banPhaseDone = false, pickPhaseDone = false;
	
	public boolean mapInProgress = false, requiredFreemod = false, matchEnd = false;
	
	public Match(Team blueTeam, Team redTeam, Mappool mappool, Channel channel, int bansPerTeam, int bestOf) {
		this.blueTeam = blueTeam;
		this.redTeam = redTeam;
		this.mappool = mappool;
		this.channel = channel;
		this.maxBans = bansPerTeam * 2;
		this.maxPicks = bestOf - 1;
		
		// Excluding tiebreaker
		availableMaps = new boolean[mappool.maps.length - 1];
		for (int i = 0; i < availableMaps.length; i++) {
			availableMaps[i] = true;
		}
		
		channel.print("!mp set 2 3 " + (Container.getInstance().mf.matchTeamSize * 2));
		
		// inviting players
		for (int i = 1; i < blueTeam.teamNames.length; i++) {
			channel.print("!mp invite " + blueTeam.teamNames[i]);
		}
		for (int i = 1; i < redTeam.teamNames.length; i++) {
			channel.print("!mp invite " + redTeam.teamNames[i]);
		}
	}

	public void receiveInput(String msg) {
		System.out.println(msg);
		String[] splitArray = msg.split(" ");
		// first 3 elements are guaranteed to be [Name] PRIVMSG [Channel], 3rd index starts with :
		String commandSymbol = "?";
		String name = splitArray[0].substring(1, splitArray[0].length() - 11); // cutting off !cho@ppy.sh
		String firstWord = splitArray[3].substring(1, splitArray[3].length());
		
		if (mapInProgress) {
			System.out.println(name.equals("BanchoBot"));
			System.out.println(firstWord.equals("Match"));
			if (splitArray.length > 6) {
				System.out.println(splitArray[6].equals("5"));
			}			
			System.out.println();
			
			if (name.equals("BanchoBot") && splitArray[4].equals("finished")) {
				if (Arrays.asList(blueTeam.teamNames).contains(firstWord)) {
					blueTeamScoreCounter++;
					if ((splitArray[8].substring(0, splitArray[8].length() - 3)).equals("PASSED")) {
						try {						
							blueTeamMapScore += Integer.parseInt(splitArray[7].substring(0, splitArray[7].length() - 1));
						} catch (NumberFormatException nfe) {
							nfe.printStackTrace();
						}
					}				
				} else if (Arrays.asList(redTeam.teamNames).contains(firstWord)) {
					redTeamScoreCounter++;
					if ((splitArray[8].substring(0, splitArray[8].length() - 3)).equals("PASSED")) {
						try {
							redTeamMapScore += Integer.parseInt(splitArray[7].substring(0, splitArray[7].length() - 1));
						} catch (NumberFormatException nfe) {
							nfe.printStackTrace();
						}
					}				
				}
				if (blueTeamScoreCounter == Container.getInstance().mf.matchTeamSize && redTeamScoreCounter == Container.getInstance().mf.matchTeamSize) {
					if (blueTeamMapScore > redTeamMapScore) {
						blueTeamMatchScore++;
						postMatchChecks();
					} else if (blueTeamMapScore < redTeamMapScore) {
						redTeamMatchScore++;
						postMatchChecks();
					} else {
						resetMapCounters();
						channel.print("This map ended in a tie, so it must be played again.");
						channel.print("!mp start 60");
					}
				}
			}
			
			if (name.equals("BanchoBot") && firstWord.equals("Match") && splitArray[6].equals("5")) {			
				if (requiredFreemod) {
					channel.print("!mp settings");
				}
			}
			
			// FreeMod 2 mod check
			if  (name.equals("BanchoBot") && firstWord.equals("Slot")) {
				int i = 12;
				if(splitArray[5].equals("Ready")) {
					i--;
				}
				
				boolean isBluePlayer = Arrays.asList(blueTeam.teamNames).contains(splitArray[i - 4]);
				
				for (int j = i; j < splitArray.length; j++) {
					String x;
					if (j == splitArray.length - 1) {
						x = splitArray[j].substring(0, splitArray[j].length() - 2);
					} else if (splitArray[j].equals("/")) {
						break;
					} else {
						x = splitArray[j].substring(0, splitArray[j].length() - 1);
					}
					
					if (x.equals("Hidden") || x.equals("HardRock") || x.equals("Flashlight") || x.equals("Easy")) {
						blueTeamModCheckCounter = isBluePlayer ? blueTeamModCheckCounter++ : blueTeamModCheckCounter;
						redTeamModCheckCounter = !isBluePlayer ? redTeamModCheckCounter++ : redTeamModCheckCounter;
					} else if (x.equals("None")) {
						// Nothing happens
					} else {
						channel.print("!mp timer abort");
						channel.print("The only permissible mods for Freemod are Hidden, HardRock, Flashlight and Easy.");
					}
				}
				blueTeamPlayerCheckCounter = isBluePlayer ? blueTeamPlayerCheckCounter++ : blueTeamPlayerCheckCounter;
				redTeamPlayerCheckCounter = !isBluePlayer ? redTeamPlayerCheckCounter++ : redTeamPlayerCheckCounter;
							
				if (blueTeamPlayerCheckCounter == Container.getInstance().mf.matchTeamSize && blueTeamModCheckCounter < 2 || redTeamPlayerCheckCounter == Container.getInstance().mf.matchTeamSize && redTeamModCheckCounter < 2) {
					channel.print("!mp timer abort");
					channel.print("Each team must have at least a total of two mods for Freemod.");
				}
			}		
		}
		
		if(!mapInProgress) {
			if (name.equals("BanchoBot") && splitArray[4].equals("joined") && !matchStarted) {
				playerJoin(firstWord);
			}
			
			// roll phase
			if (name.equals("BanchoBot") && splitArray[4].equals("rolls") && matchStarted && !rollPhaseDone) {
				if (blueTeamRoll == -1 && Arrays.asList(blueTeam.teamNames).contains(firstWord)) {
					try {
						blueTeamRoll = Integer.parseInt(splitArray[5]);
					} catch (NumberFormatException nfe) {
						channel.print("Rolling error.");
					}
				} else if (redTeamRoll == -1 && Arrays.asList(redTeam.teamNames).contains(firstWord)) {
					try {
						redTeamRoll = Integer.parseInt(splitArray[5]);
					} catch (NumberFormatException nfe) {
						channel.print("Rolling error.");
					}
				}
				if (blueTeamRoll != -1 & redTeamRoll != -1) {
					blueTeamTurn = blueTeamRoll > redTeamRoll;
					channel.print("The winner of the roll is the " + (blueTeamTurn ? blueTeam.teamNames[0] : redTeam.teamNames[0]) + "!");
					rollPhaseDone = true;
					blueTeamTurn = !blueTeamTurn;
					channel.print((blueTeamTurn ? blueTeam.teamNames[0] : redTeam.teamNames[0]) + ", please start by banning a map.");					
				}
			}			
			
			
			
			
			
			// ban phase
			if (firstWord.equals(commandSymbol + "ban") && rollPhaseDone && !banPhaseDone) {
				if ((blueTeamTurn && Arrays.asList(blueTeam.teamNames).contains(name)) || (!blueTeamTurn && Arrays.asList(redTeam.teamNames).contains(name))) {
					try {
						boolean banSucceeded = banMap(Integer.parseInt(splitArray[4].substring(0, splitArray[4].length() - 1)));
						if (banSucceeded) {
							blueTeamTurn = !blueTeamTurn;
							banCounter++;
							if (banCounter >= maxBans) {
								banPhaseDone = true;
								blueTeamTurn = !blueTeamTurn;
								channel.print("That concludes the banning phase! " + (blueTeamTurn ? blueTeam.teamNames[0] : redTeam.teamNames[0]) + ", please start the match by picking a map.");							
							} else {
								channel.print((blueTeamTurn ? blueTeam.teamNames[0] : redTeam.teamNames[0]) + ", please ban another map.");
							}
						}
					} catch (NumberFormatException nfe) {
						channel.print("You must ban using the map's number.");
					}
				}
			}
			
			// pick phase
			if (firstWord.equals(commandSymbol + "pick") && rollPhaseDone && banPhaseDone && !pickPhaseDone) {
				if ((blueTeamTurn && Arrays.asList(blueTeam.teamNames).contains(name)) || (!blueTeamTurn && Arrays.asList(redTeam.teamNames).contains(name))) {
					try {
						boolean pickSucceeded = pickMap(Integer.parseInt(splitArray[4].substring(0, splitArray[4].length() - 1))); // removes endline char
						if (pickSucceeded) {
							blueTeamTurn = !blueTeamTurn;
							pickCounter++;
						}
					} catch(NumberFormatException nfe) {
						channel.print("You must pick using the map's number.");
					}
				}
			}
		}		
	}
	
	private boolean checkVictory() {
		Team winningTeam = null;
		if (blueTeamMatchScore == (maxPicks / 2 + 1)) {
			winningTeam = blueTeam;
		} else if (blueTeamMatchScore == (maxPicks / 2 + 1)) {
			winningTeam = redTeam;
		}
		if (winningTeam != null) {
			channel.print("Congratulations to " + winningTeam.teamNames[0] + " for winning this match!");
			channel.print("The room will close in 60 seconds.");
			channel.print("!mp timer 60");
			return true;
		}
		return false;
	}
	
	private void postMatchChecks() {
		mapInProgress = false;
		resetMapCounters();
		channel.print("Current score: " + blueTeam.teamNames[0] + " " + blueTeamMatchScore + " - " + redTeamMatchScore + " " + redTeam.teamNames[0]);
		matchEnd = checkVictory();
		if (!matchEnd && pickCounter != maxPicks) {
			channel.print((blueTeamTurn ? blueTeam.teamNames[0] : redTeam.teamNames[0]) + ", please pick the next map!");
		} else if (!matchEnd && pickCounter == maxPicks) {
			channel.print("The final map is now the tie breaker. This is played in light free mod: you may pick mods, but are not required to.");
			channel.print("Picked map " + mappool.maps[mappool.maps.length - 1].artist + " - " + mappool.maps[mappool.maps.length - 1].name);
			channel.print("!mp map " + mappool.maps[mappool.maps.length - 1].id);
			channel.print("!mp mods Freemod");
			mapInProgress = true;
		}
	}
	
	private boolean banMap(int mapNr) {
		if (mapNr - 1 < 0 || mapNr - 1 >= availableMaps.length) {
			channel.print("Invalid map number!");
			return false;
		} else {
			if (availableMaps[mapNr - 1] == false) {
				channel.print("This map has already been stricken!");
				return false;
			} else {
				availableMaps[mapNr - 1] = false;
				channel.print("Banning map: " + mappool.maps[mapNr - 1].artist + " - " + mappool.maps[mapNr - 1].name);
				return true;
			}
		}
	}
	
	private boolean pickMap(int mapNr) {
		if (mapNr - 1 < 0 || mapNr - 1 >= availableMaps.length) {
			channel.print("Invalid map number!");
			return false;
		} else {
			if (availableMaps[mapNr - 1] == false) {
				channel.print("This map has already been picked or stricken!");
				return false;
			} else if ((blueTeamTurn && blueTeamLastMod == mappool.maps[mapNr].mod) || (!blueTeamTurn && redTeamLastMod == mappool.maps[mapNr].mod)) {
				channel.print("You already picked this mod on your most recent pick!");
				return false;
			} else {
				availableMaps[mapNr - 1] = false;
				if (blueTeamTurn) {
					blueTeamLastMod = mappool.maps[mapNr - 1].mod;
				} else {
					redTeamLastMod = mappool.maps[mapNr - 1].mod;
				}
				channel.print("Picked map " + mappool.maps[mapNr - 1].artist + " - " + mappool.maps[mapNr - 1].name);
				channel.print("!mp map " + mappool.maps[mapNr - 1].id);
				channel.print("!mp mods " + mappool.maps[mapNr - 1].mod.toString());
				channel.print("!mp start 20");
				if (mappool.maps[mapNr - 1].mod.toString().equals("Freemod")) {
					requiredFreemod = true;
				} else {
					requiredFreemod = false;
				}
				mapInProgress = true;
				return true;
			}
		}
	}

	public void playerJoin(String name) {
		playerCount++;
		if (playerCount == Container.getInstance().mf.matchTeamSize * 2) {
			introMessage();
			matchStarted = true;
		}
	}
	
	public void introMessage() {
		channel.print("Intro message placeholder... Welcome to match, please roll etc.");
	}
	
	public void resetMapCounters() {
		blueTeamScoreCounter = 0;
		redTeamScoreCounter = 0;
		blueTeamMapScore = 0;
		redTeamMapScore = 0;
	}
}