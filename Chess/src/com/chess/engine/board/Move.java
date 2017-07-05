package com.chess.engine.board;

import com.chess.engine.board.Board.Builder;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

public abstract class Move {
	
	final Board board;
	final Piece MovedPiece;
	final int destinationCoordinate;
	
	public static final Move NULL_MOVE = new NullMove();
	
	Move(final Board board,
		 final Piece movedPiece,
		 final int destinationCoordinate) {
		this.board = board;
		this.MovedPiece = movedPiece;
		this.destinationCoordinate = destinationCoordinate;
	}
	public Board execute() {
		final Builder builder = new Builder();
		for(final Piece piece : this.board.currentPlayer().getActivePieces()){
			//TODO hashcode and equals for pieces
			if(!this.MovedPiece.equals(piece)){
				builder.setPiece(piece);
			}
		}
		for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
			builder.setPiece(piece);
		}
		//move the moved piece!
		builder.setPiece(this.MovedPiece.movePiece(this));
		builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
		return builder.build();
	}
	
	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * result + this.destinationCoordinate;
		result = prime * result + this.MovedPiece.hashCode();
		return result;
	}
	
	@Override
	public boolean equals(final Object other){
		if(this == other){
			return true;
		}
		if(!(other instanceof Move)) {
			return false;
		}
		final Move otherMove = (Move) other;
		return getDestinationCoordinate() == otherMove.getDestinationCoordinate() &&
			   getMovedPiece().equals(otherMove.getMovedPiece());
	}
	
	public boolean isAttack(){
		return false;
	}
	
	public boolean isCastlingMove(){
		return false;
	}
	
	public Piece getAttackPiece(){
		return null;
	}

	public int getCurrentCoordinate(){
		return this.getMovedPiece().getPiecePosition();
	}
	
	
	public int getDestinationCoordinate(){
		return this.destinationCoordinate;
	}
	
	public static final class MajorMove extends Move {

		public MajorMove(final Board board, 
						 final Piece movedPiece, 
						 final int destinationCoordinate) {
			super(board, movedPiece, destinationCoordinate);
		}
	}
	
	public Piece getMovedPiece(){
		return this.MovedPiece;
	}
	
	public static class AttackMove extends Move {

		final Piece attackedPiece;
		public AttackMove(final Board board, 
						  final Piece movedPiece, 
						  final int destinationCoordinate,
				final Piece attackedPiece) {
			super(board, movedPiece, destinationCoordinate);
			this.attackedPiece = attackedPiece;
		}
		
	}
	public static final class PawnMove extends Move {

		public PawnMove(final Board board, 
						final Piece movedPiece, 
						final int destinationCoordinate) {
			super(board, movedPiece, destinationCoordinate);
		}
	}
	
	public static class PawnAttackMove extends AttackMove {

		public PawnAttackMove(final Board board, 
							  final Piece movedPiece, 
							  final int destinationCoordinate, 
							  final Piece attackedPiece) {
			super(board, movedPiece, destinationCoordinate, attackedPiece);
		}
	}
	
	public static class PawnEnPassantAttackMove extends AttackMove {

		public PawnEnPassantAttackMove(final Board board, 
									   final Piece movedPiece, 
									   final int destinationCoordinate, 
									   final Piece attackedPiece) {
			super(board, movedPiece, destinationCoordinate, attackedPiece);
		}
	}
	
	public static final class PawnJump extends Move {

		public PawnJump(final Board board, 
						final Piece movedPiece, 
						final int destinationCoordinate) {
			super(board, movedPiece, destinationCoordinate);
		}
	}

	static abstract class CastleMove extends Move {
		protected final Rook castleRook;
		protected final int castleRookStart;
		protected final int castleRookDestination;
		public CastleMove(final Board board, 
						  final Piece movedPiece, 
						  final int destinationCoordinate,
						  final Rook castleRook,
						  final int castleRookStart,
						  final int castleRookDestination) {
			super(board, movedPiece, destinationCoordinate);
			this.castleRook = castleRook;
			this.castleRookStart = castleRookStart;
			this.castleRookDestination = castleRookDestination;
		}
		
		public Rook getCastleRook(){
			return this.castleRook;
		}
		@Override
		public boolean isCastlingMove(){
			return true;
		}
		@Override
		public Board execute(){
			final Builder builder = new Builder();
			for(final Piece piece : this.board.currentPlayer().getActivePieces()){
				//TODO hashcode and equals for pieces
				if(!this.MovedPiece.equals(piece) && !castleRook.equals(piece)){
					builder.setPiece(piece);
				}
			}
			for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
				builder.setPiece(piece);
			}
			//move the moved piece!
			builder.setPiece(this.MovedPiece.movePiece(this));
			//TODO look into the first move on normal pieces
			builder.setPiece(new Rook(this.castleRookDestination, this.castleRook.getPieceAllance()));
			builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
			return builder.build();
		}
		
	}
	
	public static final class KingSideCastleMove extends CastleMove {

		public KingSideCastleMove(final Board board, 
								  final Piece movedPiece, 
								  final int destinationCoordinate,
								  final Rook castleRook,
								  final int castleRookStart,
								  final int castleRookDestination) {
			super(board, movedPiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
		}
		
		@Override
		public String toString(){
			return "0-0";
		}
		
	}
	
	public static final class QueenSideCastleMove extends CastleMove {

		public QueenSideCastleMove(final Board board, 
								   final Piece movedPiece, 
								   final int destinationCoordinate,
								   final Rook castleRook,
								   final int castleRookStart,
								   final int castleRookDestination) {
			super(board, movedPiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
		}
		@Override
		public String toString(){
			return "0-0-0";
		}
	}
	public static final class NullMove extends Move {

		public NullMove() {
			super(null, null, -1);
		}
		@Override
		public Board execute(){
			throw new RuntimeException("Cannot execute the null move!!!");
		}
	}
	
	public static class MoveFactory{
		private MoveFactory(){
			throw new RuntimeException("Not instantiable!!!");
		}
		public static Move createMove(final Board board,
				  final int currentCoordinate,
				  final int destinationCoordinate){
			for(final Move move : board.getAllLegalMoves()){
				if(move.getCurrentCoordinate() == currentCoordinate &&
				   move.getDestinationCoordinate() == destinationCoordinate){
					return move;
				}
			}
			return NULL_MOVE;
		}
	}
}

