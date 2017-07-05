package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.pieces.Piece.PieceType;
import com.google.common.collect.ImmutableList;

public class Pawn extends Piece {

	private final static int[] Candidate_Move_Vector_Coordinates = {8, 16, 7, 9};
	public Pawn(int piecePosition, Alliance pieceAlliance) {
		super(PieceType.PAWN, piecePosition, pieceAlliance);
	}

	@Override
	public String toString(){
		return PieceType.PAWN.toString();
	}
	@Override
	public Collection<Move> calculateLegalMoves(Board board) {
		final List<Move> LegalMoves = new ArrayList<>();
		for(final int currentCandidateOffset : Candidate_Move_Vector_Coordinates){
			
			int candidateDestinationCoordinate = this.piecePosition + (currentCandidateOffset * this.getPieceAllance().getDirection());
			
			if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
				continue;
			}
			
			if(currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()){
				//More work to do here
				LegalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
			} else if(currentCandidateOffset == 16 && this.isFirstMove && 
					(BoardUtils.Second_Row[this.piecePosition] && this.getPieceAllance().isBlack()) ||
					(BoardUtils.Seventh_Row[this.piecePosition]) && this.getPieceAllance().isWhite()){
				final int behindCandidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * 8);
				if(!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() && 
					!board.getTile(candidateDestinationCoordinate).isTileOccupied()){
					LegalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
				}
			} else if(currentCandidateOffset == 7 &&
					(BoardUtils.Eighth_Column[this.piecePosition] && this.pieceAlliance.isWhite()) ||
					(BoardUtils.First_Column[this.piecePosition] && this.pieceAlliance.isBlack())){
				if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
					//TODO more to do here
					LegalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
				}
			} else if(currentCandidateOffset == 9 &&
					(BoardUtils.Eighth_Column[this.piecePosition] && this.pieceAlliance.isBlack()) ||
					(BoardUtils.First_Column[this.piecePosition] && this.pieceAlliance.isWhite())){
				LegalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
			}
		}
		return ImmutableList.copyOf(LegalMoves);
	}
	@Override
	public Pawn movePiece(Move move) {
		return new Pawn(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAllance());
	}

}
