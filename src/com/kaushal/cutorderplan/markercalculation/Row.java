package com.kaushal.cutorderplan.markercalculation;

public class Row {
	
	public int[] cells;
	
	public Row(int rowVal){
		cells = new int[rowVal];
		for (int i = 0; i < rowVal; i++){
			cells[i] = 0;
		}
	}

}
