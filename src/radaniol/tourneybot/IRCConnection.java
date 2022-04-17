package radaniol.tourneybot;

import java.io.*;
import java.net.*;

public class IRCConnection {
   
    private String host;
    private int port;
    private String nickname;
    private String password;
    private PrintStream out;
    private Thread inputThread;
    
    public IRCConnection(String hostIn, int portIn, String nicknameIn) throws UnknownHostException, IOException {
        
        host = hostIn;
        port = portIn;
        nickname = nicknameIn;
        connect();
        register(false);
    }
    
    public IRCConnection(String hostIn, int portIn, String nicknameIn, String passwordIn) throws UnknownHostException, IOException {
        
        host = hostIn;
        port = portIn;
        nickname = nicknameIn;
        password = passwordIn;
        connect();
        register(true);
    }
    
    private void connect() throws UnknownHostException, IOException {
        Socket socket = new Socket(host, port);
        out = new PrintStream(socket.getOutputStream());
        
        inputThread = new InputThread(socket.getInputStream());
        inputThread.start();
    }
    
    private void register(boolean withPassword) {
        // TODO: Check server feedback
        if (withPassword) {
            out.print("PASS " + password + "\r\n");
        }
        out.print("NICK " + nickname + "\r\n");
        out.print("USER " + nickname + " 0 * :"  + nickname + "\r\n");
    }
    
    public Channel getChannel(String name) {
        return new Channel(name, out);
    }
    
    public PrintStream getPrintStream() {
        return out;
    }
}