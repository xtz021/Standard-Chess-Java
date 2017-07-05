package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece.PieceType;
import com.google.common.collect.ImmutableList;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;

public class Knight extends Piece {

	private final int[] Candidate_move_Coordinate = { -17, -15, -10, -6, 6, 10, 15, 17};
	public Knight(final int piecePosition, final Alliance pieceAlliance) {
		super(PieceType.KNIGHT, piecePosition, pieceAlliance);
	}
	@Override
	public String toString(){
		return PieceType.KNIGHT.toString();
	}
	@Override
	public Collection<Move> calculateLegalMoves(final Board board){
		int candidateDestinationCoordinate;
		final List<Move> LegalMoves = new ArrayList<>();
		for(final int currentCandidateOffset : Candidate_move_Coordinate){
			candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
			if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
				if(isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) || 
						isSecondColumnExclusion(this.piecePosition, currentCandidateOffset) ||
						isSeventhColumnExclusion(this.piecePosition, currentCandidateOffset) ||
						isEighthColumnExclusion(this.piecePosition, currentCandidateOffset)){
					continue;
				}
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
	
	private static boolean isFirstColumnExclusion (final int currentPosition, final int candidateOffset){
		return BoardUtils.First_Column[currentPosition] &&(candidateOffset == -17 || candidateOffset == -10 ||
					candidateOffset == 6 || candidateOffset == 15);
	}
	
	private static boolean isSecondColumnExclusion (final int currentPosition, final int candidateOffset){
		return BoardUtils.Second_Column[currentPosition] && (candidateOffset == -10 || candidateOffset == 6);
	}
	
	private static boolean isSeventhColumnExclusion (final int currentPosition, final int candidateOffset){
		return BoardUtils.Seventh_Column[currentPosition] && (candidateOffset == -6 || candidateOffset == 10);
	}
	
	private static boolean isEighthColumnExclusion (final int currentPosition, final int candidateOffset){
		return BoardUtils.Eighth_Column[currentPosition] && (candidateOffset == -15 || candidateOffset == -6 ||
				candidateOffset == 10 || candidateOffset == 17);
	}
	@Override
	public Knight movePiece(Move move) {
		return new Knight(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAllance());
	}
}
