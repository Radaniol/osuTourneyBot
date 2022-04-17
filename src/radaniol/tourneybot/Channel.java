package radaniol.tourneybot;

import java.io.*;
import java.net.*;

public class Channel {
    String name;
    private PrintStream out;
    
    protected Channel(String nameIn, PrintStream outIn) {     
        // TODO: Check server feedback
        name = nameIn;
        out = outIn;
        out.print("JOIN " + "#" + name + "\r\n");
        System.out.println("JOIN " + "#" + name + "\r\n");
    }
    
    public void print(String msg) {
        out.print("PRIVMSG " + "#" + name + " :" + msg + "\r\n");
        Container.getInstance().mf.ircTab.ircChat.append("PRIVMSG " + "#" + name + " :" + msg + "\r\n");
    }
}