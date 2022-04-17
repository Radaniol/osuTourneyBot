package radaniol.tourneybot;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class MappoolPanel extends TabPanel implements KeyListener, ItemListener, FocusListener, ActionListener {
	
	int currentMappoolIndex;
	
	JComboBox<String> mappoolDropdown;
	JButton renameButton, addMappoolButton, deleteMappoolButton, addMapButton, deleteMapButton;
	JLabel mapNrLabel, artistLabel, mapNameLabel, modLabel, mapIDLabel;
	JLabel[][] mapNrLabels;
	JTextField[][] artistTextFields, mapNameTextFields, mapIDTextFields;
	JComboBox[][] modDropdowns;
	
	JButton test1, test2, test3;
	
	public MappoolPanel() {
		
		test1 = new JButton("1");
		test2 = new JButton("2");
		test3 = new JButton("3");
		
		currentMappoolIndex = 0;
		
		renameButton = new JButton("Rename");
		renameButton.addActionListener(this);
		
		addMappoolButton = new JButton("Add");
		addMappoolButton.addActionListener(this);
		
		deleteMappoolButton = new JButton("Delete");
		deleteMappoolButton.addActionListener(this);
		
		addMapButton = new JButton("Add Map");
		addMapButton.addActionListener(this);
		
		deleteMapButton = new JButton("Delete Map");
		deleteMapButton.addActionListener(this);
		
		mapNrLabel = new JLabel("#");
		artistLabel = new JLabel("Artist");
		mapNameLabel = new JLabel("Name");
		modLabel = new JLabel("Mod");
		mapIDLabel = new JLabel("ID");

	}
	
	public void refreshComponents() {
		String[] mappoolNames = new String[Container.getInstance().mf.mappools.length];
		
		mapNrLabels = new JLabel[Container.getInstance().mf.mappools.length][];
		artistTextFields = new JTextField[Container.getInstance().mf.mappools.length][];
		mapNameTextFields = new JTextField[Container.getInstance().mf.mappools.length][];
		mapIDTextFields = new JTextField[Container.getInstance().mf.mappools.length][];
		modDropdowns = new JComboBox[Container.getInstance().mf.mappools.length][];
		
		for (int i = 0; i < Container.getInstance().mf.mappools.length; i++) {
			
			mappoolNames[i] = Container.getInstance().mf.mappools[i].name;
			mapNrLabels[i] = new JLabel[Container.getInstance().mf.mappools[i].maps.length];
			artistTextFields[i] = new JTextField[Container.getInstance().mf.mappools[i].maps.length];
			mapNameTextFields[i] = new JTextField[Container.getInstance().mf.mappools[i].maps.length];
			mapIDTextFields[i] = new JTextField[Container.getInstance().mf.mappools[i].maps.length];
			modDropdowns[i] = new JComboBox[Container.getInstance().mf.mappools[i].maps.length];
			
			for (int j = 0; j < Container.getInstance().mf.mappools[i].maps.length; j ++) {
				mapNrLabels[i][j] = new JLabel(String.valueOf(j + 1));
				
				artistTextFields[i][j] = new JTextField(Container.getInstance().mf.mappools[i].maps[j].artist);
				artistTextFields[i][j].addKeyListener(this);
				artistTextFields[i][j].addFocusListener(this);				
				
				mapNameTextFields[i][j] = new JTextField(Container.getInstance().mf.mappools[i].maps[j].name);
				mapNameTextFields[i][j].addKeyListener(this);
				mapNameTextFields[i][j].addFocusListener(this);
				
				mapIDTextFields[i][j] = new JTextField(String.valueOf(Container.getInstance().mf.mappools[i].maps[j].id));
				mapIDTextFields[i][j].addKeyListener(this);
				mapIDTextFields[i][j].addFocusListener(this);
				
				modDropdowns[i][j] = new JComboBox(Mod.values());
				modDropdowns[i][j].setSelectedItem(Container.getInstance().mf.mappools[i].maps[j].mod);
				modDropdowns[i][j].addItemListener(this);
			}
		}
		mappoolDropdown = new JComboBox<String>(mappoolNames);
		mappoolDropdown.setSelectedIndex(currentMappoolIndex);
		mappoolDropdown.addItemListener(this);
	}
	
	public void refreshPanel() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 12;
		c.weightx = 12;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.PAGE_START;
		this.add(mappoolDropdown, c);
		
		c.gridx = 12;
		c.gridwidth = 1;
		c.weightx = 1;
		this.add(renameButton, c);
		
		c.gridx = 13;
		this.add(addMappoolButton, c);
		
		c.gridx = 14;
		this.add(deleteMappoolButton, c);
		
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 1;
		this.add(mapNrLabel, c);
		
		c.gridx = 1;
		c.gridwidth = 5;
		c.weightx = 5;
		this.add(artistLabel, c);
		
		c.gridx = 6;
		this.add(mapNameLabel, c);
		
		c.gridx = 11;
		c.gridwidth = 1;
		c.weightx = 1;
		this.add(modLabel, c);
		
		c.gridx = 12;
		c.gridwidth = 3;
		c.weightx = 3;
		this.add(mapIDLabel, c);
		
		for (int i = 0; i < Container.getInstance().mf.mappools[currentMappoolIndex].maps.length; i++) {
			c.gridy = i + 2;
			c.gridx = 0;
			c.gridwidth = 1;
			c.weightx = 1;
			this.add(mapNrLabels[currentMappoolIndex][i], c);
			
			c.gridx = 1;
			c.gridwidth = 5;
			c.weightx = 5;
			this.add(artistTextFields[currentMappoolIndex][i], c);
			
			c.gridx = 6;
			c.gridwidth = 5;
			c.weightx = 5;
			this.add(mapNameTextFields[currentMappoolIndex][i], c);
			
			c.gridx = 11;
			c.gridwidth = 1;
			c.weightx = 1;
			this.add(modDropdowns[currentMappoolIndex][i], c);
			
			c.gridx = 12;
			c.gridwidth = 3;
			c.weightx = 3;
			this.add(mapIDTextFields[currentMappoolIndex][i], c);
		}
		
		c.gridx = 0;
		c.gridy = Container.getInstance().mf.mappools[currentMappoolIndex].maps.length * 2 + 4;
		c.gridwidth = 6;
		c.weightx = 6;
		this.add(Box.createHorizontalStrut(10), c);
		
		c.gridx = 6;
		c.gridwidth = 1;
		c.weightx = 1;
		this.add(addMapButton, c);
		
		c.gridx = 7;
		c.gridwidth = 1;
		c.weightx = 1;
		this.add(deleteMapButton, c);
		
		c.gridx = 8;
		c.gridwidth = 7;
		c.weightx = 7;
		this.add(Box.createHorizontalStrut(10), c);
		
	}
	
	public void refresh() {
		refreshComponents();
		refreshPanel();
	}

	@Override
	public void itemStateChanged(ItemEvent ie) {
		Object source = ie.getSource();
		if (source == mappoolDropdown) {
			currentMappoolIndex = mappoolDropdown.getSelectedIndex();
			Container.getInstance().mf.refreshTab(this);
		} else {
			for (int i = 0; i < modDropdowns.length; i++) {
				for (int j = 0; j < modDropdowns[i].length; j++) {
					if (source == modDropdowns[i][j]) {
						Container.getInstance().mf.mappools[i].maps[j].mod = (Mod) ((JComboBox) source).getSelectedItem();
					}
				}
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		Object source = ke.getSource();
		for (int i = 0; i < mapNrLabels.length; i++) {
			for (int j = 0; j < mapNrLabels[i].length; j++) {
				if (source == artistTextFields[i][j]) {
					if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
						Container.getInstance().mf.mappools[i].maps[j].artist = artistTextFields[i][j].getText();
						mapNameTextFields[i][j].requestFocusInWindow();
					}
				} else if (source == mapNameTextFields[i][j]) {
					if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
						Container.getInstance().mf.mappools[i].maps[j].name = mapNameTextFields[i][j].getText();
						modDropdowns[i][j].requestFocusInWindow();
					}
				} else if (source == mapIDTextFields[i][j]) {
					if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
						try {
							Container.getInstance().mf.mappools[i].maps[j].id = Integer.parseInt(mapIDTextFields[i][j].getText());
		        		} catch (NumberFormatException nfe) {
		        			nfe.printStackTrace();
		        			mapIDTextFields[i][j].setText(String.valueOf(Container.getInstance().mf.mappools[i].maps[j].id));
		        		}
					}
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent ke) {}

	@Override
	public void keyTyped(KeyEvent ke) {}

	@Override
	public void focusGained(FocusEvent fe) {}

	@Override
	public void focusLost(FocusEvent fe) {
		Object source = fe.getSource();
		for (int i = 0; i < mapNrLabels.length; i++) {
			for (int j = 0; j < mapNrLabels[i].length; j++) {
				if (source == artistTextFields[i][j]) {
					Container.getInstance().mf.mappools[i].maps[j].artist = artistTextFields[i][j].getText();
				} else if (source == mapNameTextFields[i][j]) {
					Container.getInstance().mf.mappools[i].maps[j].name = mapNameTextFields[i][j].getText();
				} else if (source == mapIDTextFields[i][j]) {
					try {
						Container.getInstance().mf.mappools[i].maps[j].id = Integer.parseInt(mapIDTextFields[i][j].getText());
		        	} catch (NumberFormatException nfe) {
		        		nfe.printStackTrace();
		        		mapIDTextFields[i][j].setText(String.valueOf(Container.getInstance().mf.mappools[i].maps[j].id));
		        	}
				}
			}
		}		
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		Object source = ae.getSource();
		if (source == renameButton) {
			
		} else if (source == addMappoolButton) {
			Container.getInstance().mf.addMappool();
			currentMappoolIndex = Container.getInstance().mf.mappools.length - 1;
			Container.getInstance().mf.refreshTab(this);
		} else if (source == deleteMappoolButton) {
			Container.getInstance().mf.deleteMappool(currentMappoolIndex);
			if (currentMappoolIndex > 0) {
				currentMappoolIndex--;
			}
			Container.getInstance().mf.refreshTab(this);
		} else if (source == addMapButton) {
			Container.getInstance().mf.addMap(currentMappoolIndex);
			Container.getInstance().mf.refreshTab(this);
		} else if (source == deleteMapButton) {
			Container.getInstance().mf.deleteMap(currentMappoolIndex);
			Container.getInstance().mf.refreshTab(this);
		}
	}
}