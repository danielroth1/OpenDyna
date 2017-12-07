package gui.levelEditor.content;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import gui.levelEditor.LeGuiControl;
import gui.levelEditor.LeLogicControl;
import gui.lobby.changeMap.ChangeMapPanel;
import gui.lobby.changeMap.MapButton;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import main.SaveLoad;

public class SaveLoadFrame extends JFrame{
	
	public static int SAVE = 0;
	public static int LOAD = 1;
	
	private LeGuiControl leGuiControl;
	private LeLogicControl leLogicControl;
	private JPanel contentPanel;
	private ChangeMap changeMap;
	private int state = SAVE;
	private JLabel nameLabel;
	private JTextField nameField;
	private JButton saveLoadBtn;
	private JButton backBtn;
	private SaveLoad saveLoad;
	
	public SaveLoadFrame(LeLogicControl logicControl, LeGuiControl guiControl) {
		this.leLogicControl = logicControl;
		this.leGuiControl = guiControl;
		this.saveLoad = leLogicControl.getSaveLoad();
		contentPanel = (JPanel) getContentPane();
		changeMap = new ChangeMap();
		changeMap.setAlignmentX(LEFT_ALIGNMENT);
		JScrollPane scrollPane = new JScrollPane(changeMap);
		reloadMaps();
		
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));
		contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		scrollPane.setAlignmentX(LEFT_ALIGNMENT);
		contentPanel.add(scrollPane);
		
		nameLabel = new JLabel("Map name: ");
		nameField = new JTextField();
		nameField.setMaximumSize(new Dimension(200, 25));
		Box nameBox = new Box(BoxLayout.LINE_AXIS);
		nameBox.setAlignmentX(LEFT_ALIGNMENT);
		nameBox.add(nameLabel);
		nameBox.add(nameField);
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(nameBox);
		
		saveLoadBtn = new JButton();
		backBtn = new JButton("Back");
		saveLoadBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if (state == SAVE){
					SaveLoadFrame.this.leLogicControl.saveMap(nameField.getText());
				}
				if (state == LOAD){
					SaveLoadFrame.this.leLogicControl.loadMap(nameField.getText());
				}
				reloadMaps();
				setVisible(false);
			}
		});
		backBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				setVisible(false);
			}
		});
		
		add(Box.createRigidArea(new Dimension(0, 10)));
		Box buttonBox = new Box(BoxLayout.LINE_AXIS);
		buttonBox.setAlignmentX(LEFT_ALIGNMENT);
		buttonBox.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonBox.add(backBtn);
		buttonBox.add(Box.createHorizontalGlue());
		buttonBox.add(saveLoadBtn);
		buttonBox.add(Box.createRigidArea(new Dimension(10, 0)));
		contentPanel.add(buttonBox);
		
		initiateFrame();
	}
	
	public void setState(int state){
		this.state = state;
		if (state == SAVE){
			saveLoadBtn.setText("Save");
			setTitle("Save");
		}
		if (state == LOAD){
			saveLoadBtn.setText("Load");
			setTitle("Load");
		}
	}
	
	private void initiateFrame(){
		
		setTitle("Save");
		setSize(LeGuiControl.MEDIUM);
		setLocationRelativeTo(null);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				setVisible(false);
			}
		});
	}
	
	public void reloadMaps(){
		changeMap.resetButtons();
		changeMap.addAllButtons(saveLoad.loadMapsObject());
	}
	
	public void setNameTextField(String text){
		nameField.setText(text);
	}
	
	private class ChangeMap extends ChangeMapPanel{
		
		public ChangeMap(){
			super();
		}

		@Override
		protected void addButton(MapButton button) {
			super.addButton(button);
			button.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					MapButton b2 = (MapButton) e.getSource();
					setNameTextField(b2.getMap().getName());
					System.out.println(b2.getMap().getName());
				}
			});
		}
		
		
		
		
	}
}
