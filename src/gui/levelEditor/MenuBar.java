package gui.levelEditor;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

public class MenuBar extends JPanel{
	
	private LeGuiControl leGuiControl;
	private LeLogicControl leLogicControl;
	
	private Box btnBox = new Box(BoxLayout.LINE_AXIS);
	private JButton newBtn;
	private JButton saveBtn;
	private JButton loadBtn;
	private JButton exitBtn;
	
	public MenuBar(LeLogicControl leLogicControl, LeGuiControl leGuiControl) {
		this.leLogicControl = leLogicControl;
		this.leGuiControl = leGuiControl;
		
		initiateButtons();
		initiatePanel();
		
		
		setBorder(BorderFactory.createRaisedBevelBorder());
		setBackground(LeGuiControl.MENU_COLOR);
	}
	
	public void addButton(JButton button){
		button.setSize(new Dimension(40, 40));
		btnBox.add(button);
		btnBox.add(Box.createRigidArea(new Dimension(10, 0)));
	}
	
	private void initiatePanel(){
		btnBox.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		addButton(newBtn);
		addButton(saveBtn);
		addButton(loadBtn);
		addButton(exitBtn);
		add(btnBox);
		add(Box.createHorizontalGlue());
	}
	
	private void initiateButtons(){
		newBtn = new JButton("New");
		saveBtn = new JButton("Save");
		loadBtn = new JButton("Load");
		exitBtn = new JButton("Exit");
		
		newBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				leLogicControl.newBtnPressed();
			}
		});
		saveBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				leLogicControl.saveBtnPressed();
			}
		});
		loadBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				leLogicControl.loadBtnPressed();
			}
		});
		exitBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				leLogicControl.exitBtnPressed();
			}
		});
	}
}
