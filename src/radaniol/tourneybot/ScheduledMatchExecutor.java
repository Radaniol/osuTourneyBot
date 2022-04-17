package radaniol.tourneybot;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ScheduledMatchExecutor {
	
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	
	public void startScheduleMatch(ZonedDateTime zonedStartTime, Team blueTeam, Team redTeam) {
		
		// calculating scheduling delay
		LocalDateTime localNow = LocalDateTime.now();
		ZoneId currentZone = ZoneId.of("Europe/Vienna");
		ZonedDateTime zonedNow = ZonedDateTime.of(localNow, currentZone);
		Duration duration = Duration.between(zonedNow, zonedStartTime);
		long delay = duration.getSeconds();
		
		final ScheduledFuture<?> taskHandle = scheduler.schedule(
			new Runnable() {
				public void run() {
					try {
						Container.getInstance().ircc.getPrintStream().print("PRIVMSG BanchoBot !mp make (" + blueTeam.teamNames[0] + " VS " + redTeam.teamNames[0] + ")\r\n");
					} catch (Exception exc) {
						exc.printStackTrace();
					}
				}
			}, delay, TimeUnit.SECONDS);
	}
}