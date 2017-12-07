package gui.levelEditor;

import gui.levelEditor.toolsPanel.Terrain;
import gui.levelEditor.toolsPanel.Tool;
import gui.levelEditor.toolsPanel.ToolButton;
import gui.levelEditor.toolsPanel.content.BlockToolPanel;
import gui.levelEditor.toolsPanel.content.TerrainToolPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import server.components.ArrayPosition;
import server.components.Block;

public class ToolsPanel extends JPanel{
	
	private LeGuiControl guiControl; 
	private LeLogicControl logicControl;
	
	private Tools tools = new Tools();
	private JLabel selectionLabel;
	private JComboBox<String> comboBox;
	private TerrainToolPanel terrainToolPanel;
	private BlockToolPanel blockToolPanel;
	public static String BLOCK = "Block";
	public static String TERRAIN = "Terrain";
	public static String STARTING_POSITION = "Starting position";
	
	public ToolsPanel(LeLogicControl logicControl, LeGuiControl guiControl) {
		this.guiControl = guiControl;
		this.logicControl = logicControl;
		setMinimumSize(new Dimension(0, 250));
		setPreferredSize(new Dimension(300, 200));
//		setMaximumSize(new Dimension(300, 1000));
		initiatePanel();
	}
	
	private void initiatePanel(){
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Tools"), 
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		
		
		add(tools);
		add(Box.createRigidArea(new Dimension(0, 5)));
		
		Box selectionBox = new Box(BoxLayout.LINE_AXIS);
		selectionBox.setAlignmentX(LEFT_ALIGNMENT);
		selectionLabel = new JLabel("Selection: ");
		comboBox = new JComboBox<String>();
		comboBox.setMinimumSize(new Dimension(60, 25));
		comboBox.setMaximumSize(new Dimension(1000, 25));
		
		comboBox.addItem(BLOCK);
		comboBox.addItem(TERRAIN);
		comboBox.addItem(STARTING_POSITION);
		comboBox.setSelectedIndex(1);
		
		selectionBox.add(selectionLabel);
		selectionBox.add(Box.createRigidArea(new Dimension(5, 0)));
		selectionBox.add(comboBox);
		
		add(selectionBox);
		
		terrainToolPanel = new TerrainToolPanel();
		blockToolPanel = new BlockToolPanel();
		terrainToolPanel.setAlignmentX(LEFT_ALIGNMENT);
		blockToolPanel.setAlignmentX(LEFT_ALIGNMENT);
		add(Box.createRigidArea(new Dimension(0, 5)));
		add(terrainToolPanel);
		add(blockToolPanel);
		comboBox.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e){
				revalidateSelection();
			}
		});
		revalidateSelection();
	}
	
	public void revalidateSelection(){
		if (comboBox.getSelectedItem() == TERRAIN){
			showTerrainToolPanel();
		}
		if (comboBox.getSelectedItem() == BLOCK){
			showBlockToolPanel();
		}
		if (comboBox.getSelectedItem() == STARTING_POSITION){
			hideAllSelections();
		}
		
	}
	
	public void showTerrainToolPanel(){
		hideAllSelections();
		terrainToolPanel.setVisible(true);
	}
	
	public void showBlockToolPanel(){
		hideAllSelections();
		blockToolPanel.setVisible(true);
	}
	
	public void hideAllSelections(){
		terrainToolPanel.setVisible(false);
		blockToolPanel.setVisible(false);
	}
	
	public String getSelection(){
		String s = (String) comboBox.getSelectedItem();
		return s;
	}
	
	public Block getSelectedBlock(){
		Block b = new Block(new ArrayPosition(0, 0), blockToolPanel.getDestructable(), null);
		return b;
	}
	
	public Terrain getSelectedTerrain(){
		Terrain t = new Terrain(terrainToolPanel.getTerraintType());
		return t;
	}
	
	public Tool getSelectedTool(){
		return tools.getSelectedTool();
	}
	
	private class Tools extends JPanel{
		
		private int numberOfTools;
		private Box currentBox;
		private Tool selectedTool;
		private ButtonGroup buttonGroup;
		private List<ToolButton> buttons = new LinkedList<ToolButton>();
		
		public Tools(){
			numberOfTools = 0;
			buttonGroup = new ButtonGroup();
			setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
			setAlignmentX(LEFT_ALIGNMENT);
			
			for (Tool t : Tool.values()){
				addTool(t);
			}
			buttons.get(0).setSelected(true);
		}
		
		public void addTool(Tool t){
			if (numberOfTools%3 == 0){
				currentBox = new Box(BoxLayout.LINE_AXIS);
				currentBox.setAlignmentX(LEFT_ALIGNMENT);
				add(Box.createRigidArea(new Dimension(0, 4)));
				add(currentBox);
				
			}
			ToolButton button = new ToolButton(t);
			button.setAlignmentX(LEFT_ALIGNMENT);
			button.addItemListener(new ItemListener(){
				public void itemStateChanged(ItemEvent e){
					if (e.getStateChange() == ItemEvent.SELECTED){
						ToolButton tb = (ToolButton) e.getSource();
						selectedTool = tb.getTool();
					}
				}
			});
			if (numberOfTools%3 != 0)
				currentBox.add(Box.createRigidArea(new Dimension(4, 0)));
			buttonGroup.add(button);
			buttons.add(button);
			currentBox.add(button);
			numberOfTools += 1;
		}

		public int getNumberOfTools() {
			return numberOfTools;
		}

		public Tool getSelectedTool() {
			return selectedTool;
		}
		
		
	}
	

}
