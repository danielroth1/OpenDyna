package server.components;

import java.io.Serializable;

public class ArrayPosition implements Serializable{
	
	int row;
	int column;
	
	public ArrayPosition(int row, int column) {
		this.row = row;
		this.column = column;
	}
	
	@Override
	public boolean equals(Object a){
		ArrayPosition a1 = (ArrayPosition) a;
		if (row == a1.getRow()
				&& column == a1.getColumn())
			return true;
		return false;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	@Override
	public String toString() {
		return "[R:" + row + "/C:" + column + "]";
	}
	
	

}
