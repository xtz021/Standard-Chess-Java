package com.chess.engine.board;

public class BoardUtils {
	
	public static final boolean[] First_Column = initColumn(0);
	public static final boolean[] Second_Column = initColumn(1);
	public static final boolean[] Seventh_Column = initColumn(6);
	public static final boolean[] Eighth_Column = initColumn(7);
	
	public static final boolean[] Second_Row = initRow(8);
	public static final boolean[] Seventh_Row = initRow(48);
	
	public static final int Num_Tiles = 64;
	public static final int Num_Tiles_Per_Row = 8;
	private BoardUtils(){
		throw new RuntimeException("You cannot instantiate me!");
	}
	
	private static boolean[] initRow(int rowNumber){
		final boolean[] row = new boolean[Num_Tiles];
		do{
			row[rowNumber] = true;
			rowNumber++;
		} while(rowNumber % Num_Tiles_Per_Row != 0);
		
		return row;
	}
	
	private static boolean[] initColumn(int columnNumber) {
		final boolean [] column = new boolean[Num_Tiles];
		do{
			column[columnNumber] = true;
			columnNumber+=Num_Tiles_Per_Row;
		}while(columnNumber < Num_Tiles);
		return column;
	}
	
	

	public static boolean isValidTileCoordinate(int coordinate){
		return coordinate >=0 && coordinate < Num_Tiles;
	}

}
