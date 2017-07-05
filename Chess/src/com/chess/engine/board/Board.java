package com.chess.engine.board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chess.engine.Alliance;
import com.chess.engine.pieces.Bishop;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Knight;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Queen;
import com.chess.engine.pieces.Rook;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public class Board {
	
	private final List<Tile> gameboard;
	private final Collection<Piece> whitePieces;
	private final Collection<Piece> blackPieces;
	private final Player currentPlayer;
	private final WhitePlayer whitePlayer;
	private final BlackPlayer blackPlayer;
	
	private Board(Builder builder){
		this.gameboard = createGameBoard(builder);
		this.whitePieces = calculateActivePieces(this.gameboard, Alliance.White);
		this.blackPieces = calculateActivePieces(this.gameboard, Alliance.Black);
		final Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces);
		final Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);
		this.whitePlayer = new WhitePlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
		this.blackPlayer = new BlackPlayer(this, blackStandardLegalMoves, whiteStandardLegalMoves);
		this.currentPlayer = builder.nextMoveMaker.choosePlayer(whitePlayer, blackPlayer);
	}
	public Player currentPlayer() {
		
		return this.currentPlayer;
	}
	
	private Collection<Move> calculateLegalMoves(final Collection<Piece> Pieces){
		final List<Move> LegalMoves = new ArrayList<>();
		
		for(final Piece piece : Pieces){
			LegalMoves.addAll(piece.calculateLegalMoves(this));
		}
		return ImmutableList.copyOf(LegalMoves);
	}

	public Player whitePlayer(){
		return this.whitePlayer;
	}

	public Player blackPlayer(){
		return this.blackPlayer();
	}
	
	
	@Override
	public String toString(){
		final StringBuilder builder = new StringBuilder();
		for(int i = 0; i < BoardUtils.Num_Tiles; i++){
			final String tileText = this.gameboard.get(i).toString();
			builder.append(String.format("%3s", tileText));
			if((i+1)%BoardUtils.Num_Tiles_Per_Row == 0){
				builder.append("\n");
			}
		}
		return builder.toString();
	}

	public Collection<Piece> getBlackPieces() {
		return blackPieces;
	}
	public Collection<Piece> getWhitePieces() {
		return whitePieces;
	}
	
	private static Collection<Piece> calculateActivePieces(final List<Tile> gameboard, final Alliance alliance){
		final List<Piece> activePieces = new ArrayList<>();
		for(Tile tile : gameboard){
			if(tile.isTileOccupied()){
				final Piece piece = tile.getPiece();
				if(piece.getPieceAllance() == alliance){
					activePieces.add(piece);
				}
			}
		}
		
		return ImmutableList.copyOf(activePieces);
		
	}
	
	public static Board createStandardBoard(){
		final Builder builder = new Builder();
		//Black Layout
		builder.setPiece(new Rook(0,Alliance.Black));
		builder.setPiece(new Knight(1,Alliance.Black));
		builder.setPiece(new Bishop(2,Alliance.Black));
		builder.setPiece(new Queen(3,Alliance.Black));
		builder.setPiece(new King(4,Alliance.Black));
		builder.setPiece(new Bishop(5,Alliance.Black));
		builder.setPiece(new Knight(6,Alliance.Black));
		builder.setPiece(new Rook(7,Alliance.Black));
		builder.setPiece(new Pawn(8,Alliance.Black));
		builder.setPiece(new Pawn(9,Alliance.Black));
		builder.setPiece(new Pawn(10,Alliance.Black));
		builder.setPiece(new Pawn(11,Alliance.Black));
		builder.setPiece(new Pawn(12,Alliance.Black));
		builder.setPiece(new Pawn(13,Alliance.Black));
		builder.setPiece(new Pawn(14,Alliance.Black));
		builder.setPiece(new Pawn(15,Alliance.Black));
		//White Layout
		builder.setPiece(new Pawn(48,Alliance.White));
		builder.setPiece(new Pawn(49,Alliance.White));
		builder.setPiece(new Pawn(50,Alliance.White));
		builder.setPiece(new Pawn(51,Alliance.White));
		builder.setPiece(new Pawn(52,Alliance.White));
		builder.setPiece(new Pawn(53,Alliance.White));
		builder.setPiece(new Pawn(54,Alliance.White));
		builder.setPiece(new Pawn(55,Alliance.White));
		builder.setPiece(new Rook(56,Alliance.White));
		builder.setPiece(new Knight(57,Alliance.White));
		builder.setPiece(new Bishop(58,Alliance.White));
		builder.setPiece(new Queen(59,Alliance.White));
		builder.setPiece(new King(60,Alliance.White));
		builder.setPiece(new Bishop(61,Alliance.White));
		builder.setPiece(new Knight(62,Alliance.White));
		builder.setPiece(new Rook(63,Alliance.White));
		return builder.build();
	}
	
	private static List<Tile> createGameBoard(final Builder builder){
		final Tile[] tiles = new Tile[BoardUtils.Num_Tiles];
		for(int i = 0; i < BoardUtils.Num_Tiles; i++){
			tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
		}
		return ImmutableList.copyOf(tiles);
	}
	
	public Tile getTile(final int tileCoordinate){
		return gameboard.get(tileCoordinate);
	}
	
	public static class Builder{
		
		Map<Integer, Piece> boardConfig;
		Alliance nextMoveMaker;
		Pawn enPassanPawn;
		
		public Builder(){
			this.boardConfig = new HashMap<>();
		}
		
		public Builder setPiece(final Piece piece){
			this.boardConfig.put(piece.getPiecePosition(), piece);
			return this;
		}
		
		public Builder setMoveMaker(final Alliance nextMoveMaker){
			this.nextMoveMaker = nextMoveMaker;
			return this;
		}
		
		public Board build(){
			return new Board(this);
		}
		
		public void setEnPassanPawn(Pawn enPassanPawn){
			this.enPassanPawn = enPassanPawn;
		}
		
	}

	public Iterable<Move> getAllLegalMoves() {
		return Iterables.unmodifiableIterable(Iterables.concat(this.whitePlayer.getLegalMoves(), this.blackPlayer.getLegalMoves()));
	}

	

}
