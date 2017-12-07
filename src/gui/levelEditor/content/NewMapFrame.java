package gui.levelEditor.content;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import gui.levelEditor.LeGuiControl;
import gui.levelEditor.LeLogicControl;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import server.components.ArrayPosition;

/**
 * is set visible when a new map want to be created
 * @author Daniel Roth
 */
public class NewMapFrame extends JFrame{
	
	private JPanel panel;
	private LeLogicControl logicControl;
	private LeGuiControl guiControl;
	
	public NewMapFrame(LeLogicControl logicControl, LeGuiControl guiControl) {
		setTitle("New map");
		panel = new NewPanel();
		this.logicControl = logicControl;
		this.guiControl = guiControl;
		
		add(panel);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				NewMapFrame.this.guiControl.setVisibleNewMapFrame(false);
			}
		});
		setSize(LeGuiControl.SMAL);
		setVisible(false);
	}
	
	private class NewPanel extends JPanel{
		
		private JLabel nameLabel = new JLabel("Name: ");
		private JTextField nameField = new JTextField("New map");
		private JLabel sizeLabel = new JLabel("Size: ");
		private JTextField xSize = new JTextField("13");
		private JLabel xLabel = new JLabel(" X ");
		private JTextField ySize = new JTextField("13");
		private JCheckBox staticBox = new JCheckBox("static");
		private JCheckBox randomBox = new JCheckBox("random");
		
		private JButton okBtn = new JButton("OK");
		private JButton backBtn = new JButton("Back");
		
		public NewPanel(){
			
			initiateGui();
			okBtn.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					NewMapFrame.this.guiControl.setVisibleNewMapFrame(false);
					NewMapFrame.this.logicControl.setNewMap(nameField.getText(), getSizeInLabels(), 
							staticBox.isSelected(), randomBox.isSelected());
				}
			});
			backBtn.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					NewMapFrame.this.guiControl.setVisibleNewMapFrame(false);
				}
			});
			
			setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			setLocationRelativeTo(null);
		}
		
		public ArrayPosition getSizeInLabels(){
			return new ArrayPosition(Integer.valueOf(xSize.getText()), Integer.valueOf(ySize.getText()));
		}
		
		private void initiateGui(){
			setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
			
			Box nameBox = new Box(BoxLayout.LINE_AXIS);
			nameField.setMinimumSize(new Dimension(60, 30));
			nameField.setMaximumSize(new Dimension(1000, 30));
			nameBox.add(nameLabel);
			nameBox.add(nameField);
			nameBox.setAlignmentX(LEFT_ALIGNMENT);
			add(nameBox);
			add(Box.createRigidArea(new Dimension(0, 10)));
			
			Box sizeBox = new Box(BoxLayout.LINE_AXIS);
			sizeBox.setAlignmentX(LEFT_ALIGNMENT);
			xSize.setMaximumSize(new Dimension(60, 30));
			ySize.setMaximumSize(new Dimension(60, 30));
			sizeBox.add(sizeLabel);
			sizeBox.add(xSize);
			sizeBox.add(xLabel);
			sizeBox.add(ySize);
			add(sizeBox);
			
			staticBox.setAlignmentX(LEFT_ALIGNMENT);
			add(Box.createRigidArea(new Dimension(0, 5)));
			add(staticBox);
			
			randomBox.setAlignmentX(LEFT_ALIGNMENT);
			add(Box.createRigidArea(new Dimension(0, 5)));
			add(randomBox);
			
			add(Box.createRigidArea(new Dimension(0, 20)));
			Box btnBox = new Box(BoxLayout.LINE_AXIS);
			
			btnBox.setAlignmentX(LEFT_ALIGNMENT);
			btnBox.add(backBtn);
			btnBox.add(Box.createHorizontalGlue());
			btnBox.add(okBtn);
			add(btnBox);
		}
		
	}
	
}
