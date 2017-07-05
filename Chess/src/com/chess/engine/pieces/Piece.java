package com.chess.engine.pieces;

import java.util.Collection;

import com.chess.engine.Alliance;
import com.chess.engine.board.Move;
import com.chess.engine.board.Board;
public abstract class Piece {
	
	protected final PieceType pieceType;
	protected final int piecePosition;
	protected final Alliance pieceAlliance;
	protected final boolean isFirstMove;
	private final int cacheHashCode;
	
	Piece(final PieceType pieceType, final int piecePosition, final Alliance pieceAlliance){
		this.pieceType = pieceType;
		this.piecePosition = piecePosition;
		this.pieceAlliance = pieceAlliance;
		this.isFirstMove = false;
		this.cacheHashCode = computeHashCode();
	}
	
	@Override
	public boolean equals(final Object other){
		if(this == other){
			return true;
		}
		if(!(other instanceof Piece)){
			return false;
		}
		final Piece otherPiece = (Piece) other;
		return piecePosition == otherPiece.getPiecePosition() && pieceType == otherPiece.getPieceType() &&
			   pieceAlliance == otherPiece.getPieceAllance() && isFirstMove == otherPiece.isFirstMove();
	}
	@Override
	public int hashCode(){
		return this.cacheHashCode;
	}
	
	public int computeHashCode(){
		int result = pieceType.hashCode();
		result = 31 * result + pieceAlliance.hashCode();
		result = 31 * result + piecePosition;
		result = 31 * result + (isFirstMove ? 1 : 0);
		return result;
	}
	
	
	public boolean isFirstMove(){	
		return this.isFirstMove;
	}
	
	public int getPiecePosition(){	//Vị trí quân cờ trên bàn cờ
		return this.piecePosition;
	}
	
	public Alliance getPieceAllance(){	//Màu quân cờ
		return this.pieceAlliance;
	}
	
	public PieceType getPieceType(){	//Tên loại quân cờ
		return pieceType;
	}
	
	public abstract Piece movePiece(Move move);									// Di chuyển quân cờ
	public abstract Collection<Move> calculateLegalMoves(final Board board);	// Tính nước có thể đi được


	public enum PieceType {	//Nhận dạng tên loại quân cờ
		
		PAWN("P") {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return false;
			}
		},
		KNIGHT("N") {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return false;
			}
		},
		BISHOP("B") {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return false;
			}
		},
		ROOK("R") {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return true;
			}
		},
		QUEEN("Q") {
			@Override
			public boolean isKing() {
				return false;
			}

			@Override
			public boolean isRook() {
				return false;
			}
		},
		KING("K") {
			@Override
			public boolean isKing() {
				return true;
			}

			@Override
			public boolean isRook() {
				return false;
			}
		};
		private String pieceName;
		
		PieceType(final String pieceName){
			this.pieceName = pieceName;
		}
		
		@Override
		public String toString(){
			return this.pieceName;
		}
		
		public abstract boolean isKing();
		public abstract boolean isRook();
		
	}
	
	
}
