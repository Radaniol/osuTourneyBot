package radaniol.tourneybot;

import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SettingsPanel extends JPanel implements KeyListener, ItemListener, FocusListener {	
	
	JComboBox<String> matchTeamSizeDropdown;
    JTextField tourneySizeTextField;
    JTextField teamSizeTextField;
    JComboBox<String> eliminationTypeDropDown;
    JCheckBox bracketResetCheckBox;
	
	public SettingsPanel(int matchTeamSize, int tourneySize, int teamSize, String eliminationType, boolean bracketReset) {
		
		GridLayout settingsGrid = new GridLayout(5, 2, 20, 20);
        setLayout(settingsGrid);
        
        JLabel matchTeamSizeLabel = new JLabel("Tourney Type");
        matchTeamSizeDropdown = new JComboBox<String>(new String[] {"1v1", "2v2", "3v3", "4v4"});
        matchTeamSizeDropdown.setSelectedIndex(matchTeamSize - 1);
        
        JLabel tourneySizeLabel = new JLabel("Number of teams: ");
        tourneySizeTextField = new JTextField(Integer.toString(tourneySize));
        tourneySizeTextField.addKeyListener(this);
        tourneySizeTextField.addFocusListener(this);
        
        JLabel teamSizeLabel = new JLabel("Team size: ");
        teamSizeTextField = new JTextField(Integer.toString(teamSize));
        teamSizeTextField.addKeyListener(this);
        teamSizeTextField.addFocusListener(this);
        
        JLabel eliminationTypeLabel = new JLabel("Elimination type: ");
        eliminationTypeDropDown = new JComboBox<String>(new String[] {"Single Elimination", "Double Elimination"});
        eliminationTypeDropDown.setSelectedItem(eliminationType);
        
        JLabel bracketResetLabel = new JLabel("Bracket reset: ");
        bracketResetCheckBox = new JCheckBox("", bracketReset);
        bracketResetCheckBox.setSelected(bracketReset);
        
        matchTeamSizeDropdown.addItemListener(this);
        eliminationTypeDropDown.addItemListener(this);
        bracketResetCheckBox.addItemListener(this);
        
        add(matchTeamSizeLabel);
        add(matchTeamSizeDropdown);
        add(tourneySizeLabel);
        add(tourneySizeTextField);
        add(teamSizeLabel);
        add(teamSizeTextField);
        add(eliminationTypeLabel);
        add(eliminationTypeDropDown);
        add(bracketResetLabel);
        add(bracketResetCheckBox);
	}

	@Override
	public void focusGained(FocusEvent fe) {}

	@Override
	public void focusLost(FocusEvent fe) {
		Object source = fe.getSource();
		if (source == tourneySizeTextField) {
			try {
    			Container.getInstance().mf.tourneySize = Integer.parseInt(tourneySizeTextField.getText());
    			Container.getInstance().mf.refreshTab(Container.getInstance().mf.teamsTab);
    		} catch (NumberFormatException nfe) {
    			tourneySizeTextField.setText(String.valueOf(Container.getInstance().mf.tourneySize));
    		}
		} else if (source == teamSizeTextField) {
			try {
				Container.getInstance().mf.teamSize = Integer.parseInt(teamSizeTextField.getText());
				Container.getInstance().mf.refreshTab(Container.getInstance().mf.teamsTab);
    		} catch (NumberFormatException nfe) {
    			teamSizeTextField.setText(String.valueOf(Container.getInstance().mf.teamSize));
    		}
		}		
	}

	@Override
	public void itemStateChanged(ItemEvent ie) {
		Object source = ie.getSource();
		if (source == matchTeamSizeDropdown) {
			Container.getInstance().mf.matchTeamSize = matchTeamSizeDropdown.getSelectedIndex() + 1;
			Container.getInstance().mf.refreshTab(Container.getInstance().mf.teamsTab);
		} else if (source == eliminationTypeDropDown) {
			Container.getInstance().mf.eliminationType = (String) eliminationTypeDropDown.getSelectedItem();
			Container.getInstance().mf.refreshTab(Container.getInstance().mf.teamsTab);
		} else if (source == bracketResetCheckBox) {
			Container.getInstance().mf.bracketReset = bracketResetCheckBox.isSelected();
			Container.getInstance().mf.refreshTab(Container.getInstance().mf.teamsTab);
		}		
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		Object source = ke.getSource();
		if (source == tourneySizeTextField) {
        	if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
        		try {
        			Container.getInstance().mf.tourneySize = Integer.parseInt(tourneySizeTextField.getText());
        			Container.getInstance().mf.refreshTab(Container.getInstance().mf.teamsTab);
        		} catch (NumberFormatException nfe) {
        			nfe.printStackTrace();
        			tourneySizeTextField.setText(String.valueOf(Container.getInstance().mf.tourneySize));
        		}
        		teamSizeTextField.requestFocusInWindow();
        	}        	
        } else if (source == teamSizeTextField) {
        	if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
        		try {
        			Container.getInstance().mf.teamSize = Integer.parseInt(teamSizeTextField.getText());
        			Container.getInstance().mf.refreshTab(Container.getInstance().mf.teamsTab);
        		} catch (NumberFormatException nfe) {
        			teamSizeTextField.setText(String.valueOf(Container.getInstance().mf.teamSize));
        		}
        		eliminationTypeDropDown.requestFocusInWindow();
        	}       	
        }	
	}

	@Override
	public void keyReleased(KeyEvent ke) {}

	@Override
	public void keyTyped(KeyEvent ke) {}
}
