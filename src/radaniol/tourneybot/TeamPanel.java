package radaniol.tourneybot;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class TeamPanel extends TabPanel implements ActionListener {
	
	public JButton[] teamButtons;

	public void refresh() {
		Container.getInstance().mf.refreshTeamArray();
		
    	GridLayout teamGrid = new GridLayout(Container.getInstance().mf.tourneySize / 4, 4, 2, 2);
        this.setLayout(teamGrid);
        
        teamButtons = new JButton[Container.getInstance().mf.tourneySize];
        for (int i = 0; i < Container.getInstance().mf.tourneySize; i++) {
            teamButtons[i] = new JButton(Container.getInstance().mf.teams[i].teamNames[0]);
            teamButtons[i].addActionListener(this);
            this.add(teamButtons[i]);
        }
    }
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		Object source = ae.getSource();
		for (int i = 0; i < teamButtons.length; i++) {
			if (source == teamButtons[i]) {
				new TeamFrame(i);
			}
		}		
	}	
}
