package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

public class Bishop extends Piece {
	private final static int[] Candidate_Move_Vector_Coordinates = {-9, -7, 7, 9};
	public Bishop(final int piecePosition, final Alliance pieceAlliance) {
		super(PieceType.BISHOP, piecePosition, pieceAlliance);
	}
	@Override
	public String toString(){
		return PieceType.BISHOP.toString();
	}
	
	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {
		final List<Move> LegalMoves = new ArrayList<>();
		for(final int candidateCoordinateOffset : Candidate_Move_Vector_Coordinates){
			int candidateDestinationCoordinate = this.piecePosition;
			while(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
				if(isFirstColumn(candidateDestinationCoordinate, candidateCoordinateOffset) ||
						isEigthColumn(candidateDestinationCoordinate, candidateCoordinateOffset))
					candidateDestinationCoordinate += candidateCoordinateOffset;
				if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
					final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
					if(!candidateDestinationTile.isTileOccupied()){
						LegalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
					} else{
						final Piece pieceAtDestination = candidateDestinationTile.getPiece();
						final Alliance pieceAlliance = pieceAtDestination.getPieceAllance();
						if(this.pieceAlliance != pieceAlliance){
							LegalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
						}
						break;
					}
				}
			}
		}
		return ImmutableList.copyOf(LegalMoves);
	}

	private static boolean isFirstColumn(final int currentPosition, final int candidateOffset){
		return BoardUtils.First_Column[currentPosition] && (candidateOffset == -9 || candidateOffset == 7);
	}
	private static boolean isEigthColumn(final int currentPosition, final int candidateOffset){
			return BoardUtils.Eighth_Column[currentPosition] && (candidateOffset == -7 || candidateOffset == 9);
	}
	
	@Override
	public Bishop movePiece(Move move) {
		return new Bishop(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAllance());
	}
	
	
	
}
