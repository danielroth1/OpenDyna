package gui.levelEditor;

import gui.images.Images;
import gui.levelEditor.toolsPanel.Terrain;
import gui.levelEditor.toolsPanel.TextureButton;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;

import maps.Texture;

public class TexturePanel extends JPanel{
	
	private LeGuiControl guiControl; 
	private LeLogicControl logicControl;
	private List<TextureButton> textureButtons = new LinkedList<TextureButton>();
	private ButtonGroup textureGroup = new ButtonGroup();
	private List<Box> rows = new LinkedList<Box>();
	private Images images;
	private Texture selectedTexture = null;
	
	public TexturePanel(LeLogicControl logicControl, LeGuiControl guiControl) {
		this.guiControl = guiControl;
		this.logicControl = logicControl;
		this.images = guiControl.getImages();
		
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		initiatePanel();
//		setBackground(Color.BLUE);
	}
	
	private void initiatePanel(){
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Textures"), 
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));
//		contentPanel.setBackground(Color.CYAN);
	}
	
	public void addTextureButton(Texture texture){
		TextureButton button = new TextureButton(texture, images.getImage(texture));
		button.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e){
				if (e.getStateChange() == ItemEvent.SELECTED){
					TextureButton b = (TextureButton) e.getItem();
					selectedTexture = b.getTexture();
				}
			}
		});
		textureGroup.add(button);
		button.setAlignmentX(LEFT_ALIGNMENT);
		int row = (int) Math.floor(textureButtons.size()/3);
//		System.out.println(row);
		if (rows.size()<=row){
			Box box = new Box(BoxLayout.LINE_AXIS);
			rows.add(box);
			addBoxToPanel(box);
		}
		rows.get(row).add(button);
		rows.get(row).add(Box.createRigidArea(new Dimension(5, 0)));
		textureButtons.add(button);
		
	}
	
	private void addBoxToPanel(Box box){
		add(Box.createRigidArea(new Dimension(0, 5)));
		add(box);
	}
	
	public Texture getSelectedTexture(){
//		TextureButton b = (TextureButton) textureGroup.getSelection();
		return selectedTexture;
	}
	
}
