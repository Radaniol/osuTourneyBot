package radaniol.tourneybot;

import java.io.IOException;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class TourneyBotMain {
    public static void main(String[] args) {    	
        try {	
        	Container.getInstance().mf = new MainFrame();
        	Container.getInstance().mf.refreshAll();
            Container.getInstance().ircc = new IRCConnection("irc.ppy.sh", 6667, USERNAME, PASSWORD);
            ScheduledMatchExecutor sme = new ScheduledMatchExecutor();
            Team blueTeam = Container.getInstance().mf.teams[0];
            Team redTeam = Container.getInstance().mf.teams[1];
            
            //sme.startScheduleMatch(ZonedDateTime.of(2018, 10, 5, 17, 31, 30, 0, ZoneId.of("Europe/Vienna")), blueTeam, redTeam);
        } catch (UnknownHostException uhe) {
            System.out.println("UnknownHostException in Main!");
        } catch (IOException ioe) {
            System.out.println("IOException in Main!");
        }        
    }    
}