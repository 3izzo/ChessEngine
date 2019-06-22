package Pieces;

import java.util.ArrayList;
import java.util.List;

import Main.Grid;

public class Pawn extends Piece
{

	public Pawn(boolean white)
	{
		super(white);
	}

	@Override
	public List<Piece> getPiecesAttackedByMe(Grid g, int x, int y)
	{
		List<Piece> r = new ArrayList<Piece>();
		for (int i = -1; i <= 1; i += 2)
		{
			int direction = !white ? 1 : -1;

			if (x + i >= 0 && x + i <= 7)
			{
				Piece pieceAt = g.getPieceAt(x + i, y + direction);

				if (pieceAt != null && pieceAt.white != white)
				{
					r.add(pieceAt);
				}
			}
		}
		return r;
	}

	@Override
	public List<Piece> getPiecesDefendedByMe(Grid g, int x, int y)
	{
		List<Piece> r = new ArrayList<Piece>();
		for (int i = -1; i <= 1; i += 2)
		{
			int direction = !white ? 1 : -1;
			if (x + i >= 0 && x + i <= 7)
			{
				Piece pieceAt = g.getPieceAt(x + i, y + direction);

				if (pieceAt != null && pieceAt.white == white)
				{
					r.add(pieceAt);
				}
			}
		}
		return r;
	}

	@Override
	public List<Grid> getAllMoves(Grid g, int x, int y, boolean takeCastleIntoConsideration)
	{
		List<Grid> r = new ArrayList<Grid>();
		int direction = !white ? 1 : -1;
		int limit = !white ? 7 : 0;
		// moving one step forward

		if (g.getPieceAt(x, y + direction) == null)
		{
			if (y + direction == limit)
			{
				Promote(g, x, y, x, limit, r);
			} else
				r.add(new Grid(g, x, y, x, y + direction));

			// moving two steps
			if (y + direction * 2 >= 0 && y + direction * 2 < 8)
				if (y == limit - direction * 6 && g.getPieceAt(x, y + direction * 2) == null)
					r.add(new Grid(g, x, y, x, y + direction * 2));
		}

		// atacking
		for (int i = -1; i <= 1; i += 2)
		{
			if (x + i < 0 || x + i > 7)
				continue;
			
			Piece pieceAt = g.getPieceAt(x + i, y + direction);
			if (pieceAt != null && pieceAt.white != white)
			{
				if (y + direction == limit)
				{
					Promote(g, x, y, x + i, limit, r);
				} else
					r.add(new Grid(g, x, y, x + i, y + direction));
			}

			// EP
			if (g.prevGrid != null && y == limit - direction * 3)
			{
				Piece pieceAtStartPosPrevGrid = g.prevGrid.getPieceAt(x + i, y + direction * 2);
				Piece pieceAtStartPosThisGrid = g.getPieceAt(x + i, y + direction * 2);
				if (pieceAtStartPosPrevGrid != null && pieceAtStartPosPrevGrid instanceof Pawn && pieceAtStartPosThisGrid == null)
				{
					Piece pieceAt2 = g.getPieceAt(x + i, y);
					if (g.prevGrid.getPieceAt(x + i, y + direction) == null && pieceAt2 != null && pieceAt2 instanceof Pawn && pieceAt2.white != white)
					{
						Grid grid = new Grid(g, x, y, x + i, y + direction);
						grid.setPieceTo(x + i, y, null);
						r.add(grid);
					}
				}
			}
		}

		return r;
	}

	private void Promote(Grid g, int fromX, int fromY, int toX, int toY, List<Grid> r)
	{
		Grid gqueen = new Grid(g, fromX, fromY, toX, toY);
		gqueen.setPieceTo(toX, toY, new Queen(white));

		Grid gRook = new Grid(g, fromX, fromY, toX, toY);
		gRook.setPieceTo(toX, toY, new Rook(white));

		Grid gBishop = new Grid(g, fromX, fromY, toX, toY);
		gBishop.setPieceTo(toX, toY, new Bishop(white));

		Grid gKnight = new Grid(g, fromX, fromY, toX, toY);
		gKnight.setPieceTo(toX, toY, new Knight(white));
		
		r.add(gqueen);
		r.add(gRook);
		r.add(gBishop);
		r.add(gKnight);
	}

	@Override
	public long getAllMovesAsBitBoard(Grid g, int x, int y)
	{
		long result = 0;
		int direction = !white ? 1 : -1;
//		int limit = !white ? 7 : 0;
		// moving one step forward
//
//		if (g.getPieceAt(x, y + direction) == null)
//		{
//			result |= 1L << (y + direction) * 8 + x;
//
//			// moving two steps
//			if (y + direction * 2 >= 0 && y + direction * 2 < 8)
//				if (y == limit - direction * 6 && g.getPieceAt(x, y + direction * 2) == null)
//					result |= 1L << (y + 2 * direction) * 8 + x;
//		}

		// atacking
		for (int i = -1; i <= 1; i += 2)
		{
			if (x + i < 0 || x + i > 7)
				continue;
			Piece pieceAt = g.getPieceAt(x + i, y + direction);
			if (pieceAt != null && pieceAt.white != white)
			{
				result |= 1L << (y + direction) * 8 + x + i;
			}

			// EP
//			if (g.prevGrid != null && y == limit - direction * 3)
//			{
//				Piece pieceAtStartPosPrevGrid = g.prevGrid.getPieceAt(x + i, y + direction * 2);
//				Piece pieceAtStartPosThisGrid = g.getPieceAt(x + i, y + direction * 2);
//				if (pieceAtStartPosPrevGrid != null && pieceAtStartPosPrevGrid instanceof Pawn && pieceAtStartPosThisGrid == null)
//				{
//					Piece pieceAt2 = g.getPieceAt(x + i, y);
//					if (g.prevGrid.getPieceAt(x + i, y + direction) == null && pieceAt2 != null && pieceAt2 instanceof Pawn && pieceAt2.white != white)
//					{
//						Grid grid = new Grid(g, x, y, x + i, y + direction);
//						grid.setPieceTo(x + i, y, null);
//						r.add(grid);
//					}
//				}
//			}
		}
		return result;
	}
}
