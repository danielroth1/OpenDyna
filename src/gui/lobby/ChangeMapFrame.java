package gui.lobby;

import gui.lobby.changeMap.ChangeMapPanel;
import gui.lobby.changeMap.MapButton;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import main.GameControl;
import main.GuiControl;
import maps.Map;

public class ChangeMapFrame extends JFrame{
	
	private GameControl gameControl;
	private GuiControl guiControl;
	private List<Map> maps = new LinkedList<Map>();
	private JPanel contentPanel;
	private ChangeMapPanel changeMapPanel;
	private JScrollPane scrollPane;
	private JButton backBtn;
	private JButton okBtn;
	
	
	public ChangeMapFrame(GameControl gameControl, GuiControl guiControl) {
		this.gameControl = gameControl;
		this.guiControl = guiControl;
//		gameControl.requestMaps();
		
		initiateContentPanel();
		initiateFrame();
	}
	
	private void initiateFrame(){
		setTitle("Change map");
		setBackground(GuiControl.STANDARD_COLOR);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				guiControl.changeMapBackBtnPressed();
			}
		});
		setSize(GuiControl.CHANGE_MAP_FRAME_SIZE);
		setLocationRelativeTo(null);
	}
	
	private void initiateContentPanel(){
		contentPanel = (JPanel)getContentPane();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));
		changeMapPanel = new ChangeMapPanel();
		changeMapPanel.setAlignmentX(CENTER_ALIGNMENT);
		scrollPane = new JScrollPane(changeMapPanel);
		contentPanel.add(scrollPane);
		contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		
		Box box = new Box(BoxLayout.LINE_AXIS);
		backBtn = new JButton("Back");
		okBtn = new JButton("OK");
		box.add(backBtn);
		box.add(Box.createRigidArea(new Dimension(10, 0)));
		box.add(okBtn);
		box.setAlignmentX(CENTER_ALIGNMENT);
		contentPanel.add(box);
		contentPanel.setBackground(GuiControl.STANDARD_COLOR);
		contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		backBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				guiControl.changeMapBackBtnPressed();
				
			}
		});
		okBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				guiControl.changeMapOkBtnPressed();
				Map selectedMap = changeMapPanel.getSelectedMap();
				if (selectedMap != null)
					gameControl.changeMapRequest(selectedMap);
			}
		});
	}
	
	public void setMaps(List<Map> maps){
		this.maps = maps;
		revalidateButtons();
	}
	
	public List<Map> getMaps(){
		return maps;
	}
	
	public void revalidateButtons(){
		changeMapPanel.resetButtons();
		changeMapPanel.addAllButtons(maps);
	}
	
}
