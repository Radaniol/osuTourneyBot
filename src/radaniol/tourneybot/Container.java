package radaniol.tourneybot;

public class Container {
    public MainFrame mf;
    public IRCConnection ircc;
    
    private static Container instance = null;
    
    private void Container() {}
    
    public static Container getInstance() {
        if (instance == null) {
            instance = new Container();
        }
        return instance;
    }
}