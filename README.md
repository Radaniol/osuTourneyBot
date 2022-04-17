# osuTourneyBot
A simple, automatic referee in IRC chat for use in osu! tournaments (osu.ppy.sh)
While it is already 4 years old and much can be criticized about its design (the use of a singleton instead of listeners, for example), it was my first major project, and I decided to upload it for posterity.
It can be almost be run as is, provided you replace the username and password in TourneyBotMain using the IRC credentials that are available from the osu! website. The bracket tab is not yet implemented, and was meant to hold a visual representation of the bracket as well as scheduling capabilites - the matches must currently be manually coded in, an example is commented out in TourneyBotMain.
