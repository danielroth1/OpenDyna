package gui.levelEditor.toolsPanel;

import java.awt.Dimension;

import javax.swing.JToggleButton;

public class ToolButton extends JToggleButton{
	
	private Tool tool;
	
	public ToolButton(Tool tool) {
		setText(tool.getName());
		this.tool = tool;
		
//		setMinimumSize(new Dimension(40, 40));
//		setPreferredSize(new Dimension(40, 40));
//		setMaximumSize(new Dimension(40, 80));
	}

	public Tool getTool() {
		return tool;
	}

	public void setTool(Tool tool) {
		this.tool = tool;
	}
	
	

}
