package gui.levelEditor.toolsPanel;

public enum Tool {
	ADD, REPLACE, REMOVE, FILL;
	
	static{
		ADD.setName("add");
		REPLACE.setName("replace");
		REMOVE.setName("remove");
		FILL.setName("fill");
	}
	
	private String name;
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
}
