package radaniol.tourneybot;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TeamFrame extends JFrame implements KeyListener {
	private JPanel teamPanel;
	private JLabel[] teamNameLabels;
	private JTextField[] teamNameTextFields;
	String[] teamNames;
	
	
	public TeamFrame(int teamIndex) {		
		super(Container.getInstance().mf.teams[teamIndex].teamNames[0]);
		setSize(400, 50 * (Container.getInstance().mf.teamSize + 1));		
		
		// Update Teams tab when closing the frame
		this.addWindowListener(new WindowAdapter() {
        	public void windowClosing(WindowEvent we) {
        		Container.getInstance().mf.refreshTab(Container.getInstance().mf.teamsTab);
        	}
        });
		
		teamNames = Container.getInstance().mf.teams[teamIndex].teamNames;
				
        teamNameLabels = new JLabel[teamNames.length];
		teamNameTextFields = new JTextField[teamNames.length];		
		
		teamNameLabels[0] = new JLabel("Team Name");
		for (int i = 1; i < teamNameLabels.length; i++) {
			teamNameLabels[i] = new JLabel("Player " + i);
		}
		
		for (int i = 0; i < teamNameTextFields.length; i++) {
			teamNameTextFields[i] = new JTextField(teamNames[i]);
			teamNameTextFields[i].addKeyListener(this);
		}
		
		teamPanel = new JPanel();
		GridLayout teamFrameGrid = new GridLayout(teamNames.length, 2, 20, 20);
		teamPanel.setLayout(teamFrameGrid);
		
		for (int i = 0; i < teamNames.length; i++) {
			teamPanel.add(teamNameLabels[i]);
			teamPanel.add(teamNameTextFields[i]);			
		}
		
		add(teamPanel);
		setVisible(true);
	}

	@Override
	public void keyPressed(KeyEvent ke) {}

	@Override
	public void keyReleased(KeyEvent ke) {
		Object source = ke.getSource();
		for (int i = 0; i < teamNames.length; i++) {
			if (source == teamNameTextFields[i]) {
				teamNames[i] = teamNameTextFields[i].getText();
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent ke) {}
}
