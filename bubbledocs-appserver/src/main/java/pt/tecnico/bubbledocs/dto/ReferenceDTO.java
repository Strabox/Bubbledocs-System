package pt.tecnico.bubbledocs.dto;

import pt.tecnico.bubbledocs.domain.Cell;

public class ReferenceDTO {
	
	private Cell cell;
	
	private int line;
	
	private int column;
	
	public ReferenceDTO(Cell cell,int line,int column){
		this.cell = cell;
		this.line = line;
		this.column = column;
	}

	public Cell getCell() {
		return cell;
	}


	public int getLine() {
		return line;
	}


	public int getColumn() {
		return column;
	}
	
	
}
