package radaniol.tourneybot;

import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class IRCPanel extends JPanel implements KeyListener {
	
    public JTextArea ircChat;
    public JTextField ircInput;
	
    public IRCPanel() {
    	FlowLayout ircFlow = new FlowLayout();
        this.setLayout(ircFlow);
        
        ircChat = new JTextArea(20,100);
        ircChat.setEditable(false);
        JScrollPane scroll = new JScrollPane(ircChat, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        ircInput = new JTextField(100);
        ircInput.addKeyListener(this);
        
        this.add(scroll);
        this.add(ircInput);
    }
    
	@Override
	public void keyPressed(KeyEvent ke) {
		Object source = ke.getSource();
        if (source == ircInput) {
            //System.out.println(ke.getKeyCode());
            if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
                Container.getInstance().ircc.getPrintStream().print(ircInput.getText() + "\r\n");
                ircChat.append(ircInput.getText() + "\r\n");
                ircInput.setText("");
            }    
        }	
	}

	@Override
	public void keyReleased(KeyEvent ke) {}

	@Override
	public void keyTyped(KeyEvent ke) {}
}