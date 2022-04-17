package radaniol.tourneybot;

import java.io.*;

public class InputThread extends Thread {
    BufferedReader in;
    
    public InputThread(InputStream is) {
        in = new BufferedReader(new InputStreamReader(is));
    }
    
    public void run() {
        try {
            String msg;
            while ((msg = in.readLine()) != null) {
                Container.getInstance().mf.receiveIRCInput(msg + "\n");
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }       
    }
}