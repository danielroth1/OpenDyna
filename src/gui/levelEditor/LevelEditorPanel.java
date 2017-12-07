package gui.levelEditor;

import gui.levelEditor.toolsPanel.Terrain;
import gui.levelEditor.toolsPanel.Tool;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import server.components.ArrayPosition;
import server.components.Block;
import server.components.PlayingField;

import main.GuiControl;
import maps.Map;
import maps.Texture;

public class LevelEditorPanel extends JPanel{
	
	private GuiControl guiControl;
	private LeGuiControl leGuiControl;
	private LeLogicControl leLogicControl;
	private JButton backBtn;
	private ToolsBar toolsBar;
	private MenuBar menuBar;
	private CanvasPanel canvasPanel;
	private JScrollPane scrollPane;
	
	public LevelEditorPanel(GuiControl guiControl) {
		this.guiControl = guiControl;
		
//		setMinimumSize(LeGuiControl.FRAME_SIZE);
//		setPreferredSize(LeGuiControl.FRAME_SIZE);
//		setMaximumSize(LeGuiControl.FRAME_SIZE);
		setVisible(false);
	}
	
	private void initiateComponents(){
		toolsBar = new ToolsBar(leLogicControl, leGuiControl);
		canvasPanel = new CanvasPanel(leLogicControl, leGuiControl);
		scrollPane = new JScrollPane(canvasPanel);
		menuBar = new MenuBar(leLogicControl, leGuiControl);
		backBtn = new JButton("Back");
		canvasPanel.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				Point p = e.getPoint();
				leLogicControl.buttonPressedInCanvasPanel(LeLogicControl.getArrayPosition(p.x, p.y));
			}
		});
		canvasPanel.addMouseMotionListener(new MouseAdapter(){
			public void mouseDragged(MouseEvent e){
				Point p = e.getPoint();
				leLogicControl.buttonPressedInCanvasPanel(LeLogicControl.getArrayPosition(p.x, p.y));
			}
		});
		backBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				LevelEditorPanel.this.guiControl.backToMenue();
			}
		});
	}
	
	private void initiatePanel(){
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
//		menuBar.setBackground(Color.GREEN);
//		toolsBar.setBackground(Color.BLUE);
//		canvasPanel.setBackground(Color.CYAN);
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(menuBar)
						.addGroup(layout.createSequentialGroup()
								.addComponent(toolsBar, 250, 250, 250)
								.addComponent(scrollPane))));
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(menuBar, 35, 35, 35)
				.addGroup(layout.createParallelGroup()
						.addComponent(toolsBar)
						.addComponent(scrollPane))
				);
		
		add(backBtn);
		setSize(GuiControl.MENU_SIZE);
		setBackground(LeGuiControl.CANVAS_COLOR);
	}
	
	public void setComponents(LeGuiControl leGuiControl, LeLogicControl leLogicControl){
		this.leGuiControl = leGuiControl;
		this.leLogicControl = leLogicControl;
		
		start();
	}
	
	private void start(){
		initiateComponents();
		initiatePanel();
	}

	public void updateCanvasPanel(Map map){
		canvasPanel.update(map);
		scrollPane.revalidate();
		scrollPane.repaint();
	}
	
	public void updateCanvasPanel(){
		canvasPanel.update();
		scrollPane.revalidate();
		scrollPane.repaint();
	}
	
	/**
	 * NOT IMPLEMENTED
	 * @param a
	 */
	public void updateCanvasPanel(ArrayPosition a){
		canvasPanel.update(a);
		scrollPane.revalidate();
		scrollPane.repaint();
	}
	
	/**
	 * returns the selected Texture in texturePanel
	 * @return Texture
	 */
	public Texture getSelectedTexture(){
		return toolsBar.getSelectedTexture();
	}
	
	/**
	 * returns the selection in tools panel
	 * @return String
	 */
	public String getSelection(){
		return toolsBar.getSelection();
	}
	
	public Block getSelectedBlock(){
		return toolsBar.getSelectedBlock();
	}
	
	public Terrain getSelectedTerrain(){
		return toolsBar.getSelectedTerrain();
	}
	
	public Tool getSelectedTool(){
		return toolsBar.getSelectedTool();
	}
}
