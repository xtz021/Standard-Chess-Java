package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece.PieceType;
import com.google.common.collect.ImmutableList;

public class King extends Piece {

	private final static int[] Candidate_Move_Coordinates = {-9, -8, -7, -1, 1, 7, 8, 9};
	public King(int piecePosition, Alliance pieceAlliance) {
		super(PieceType.KING, piecePosition, pieceAlliance);
	}
	@Override
	public String toString(){
		return PieceType.KING.toString();
	}
	@Override
	public Collection<Move> calculateLegalMoves(Board board) {
		final List<Move> LegalMoves = new ArrayList<>();
		int candidateDestinationCoordinate;
		for(final int currentCandidateOffset : Candidate_Move_Coordinates){
			if(isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) || 
					isEighthColumnExclusion(this.piecePosition, currentCandidateOffset)){
				continue;
			}
			candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
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
				}
			}
		}
		return ImmutableList.copyOf(LegalMoves);
	}
	
	
	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffSet){
		return BoardUtils.First_Column[currentPosition] && (candidateOffSet == -9 || candidateOffSet == -1 || candidateOffSet == 7 );
	}
	
	private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffSet){
		return BoardUtils.Eighth_Column[currentPosition] && (candidateOffSet == -7 || candidateOffSet == 1 || candidateOffSet == 9 );
	}
	
	@Override
	public King movePiece(Move move) {
		return new King(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAllance());
	}
	
	
}


