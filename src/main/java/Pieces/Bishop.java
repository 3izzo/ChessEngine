package Pieces;

import java.util.ArrayList;
import java.util.List;

import Main.Grid;

public class Bishop extends DirectionalPiece
{

	public Bishop(boolean white)
	{
		super(white);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Grid> getAllMoves(Grid g, int x, int y, boolean takeCastleIntoConsideration)
	{
		List<Grid> allMoves = new ArrayList<Grid>();

		getMovesInDirection(g, x, y, 1, 1, allMoves);
		getMovesInDirection(g, x, y, 1, -1, allMoves);
		getMovesInDirection(g, x, y, -1, 1, allMoves);
		getMovesInDirection(g, x, y, -1, -1, allMoves);

		return allMoves;
	}

	@Override
	public List<Piece> getPiecesAttackedByMe(Grid g, int x, int y)
	{
		List<Piece> r = new ArrayList<Piece>();

		r.add(getNearesetPieceInDirection(g, x, y, 1, 1));
		r.add(getNearesetPieceInDirection(g, x, y, -1, 1));
		r.add(getNearesetPieceInDirection(g, x, y, -1, -1));
		r.add(getNearesetPieceInDirection(g, x, y, 1, -1));

		// remove pieces from the same color
		for (int i = 0; i < r.size(); i++)
		{
			if (r.get(i) == null || r.get(i).white == white)
			{
				r.remove(i);
				i--;
			}
		}
		return r;
	}

	@Override
	public List<Piece> getPiecesDefendedByMe(Grid g, int x, int y)
	{
		List<Piece> r = new ArrayList<Piece>();

		r.add(getNearesetPieceInDirection(g, x, y, 1, 1));
		r.add(getNearesetPieceInDirection(g, x, y, -1, 1));
		r.add(getNearesetPieceInDirection(g, x, y, -1, -1));
		r.add(getNearesetPieceInDirection(g, x, y, 1, -1));

		// remove pieces from the same color
		for (int i = 0; i < r.size(); i++)
		{
			if (r.get(i) == null || r.get(i).white != white)
			{
				r.remove(i);
				i--;
			}
		}
		return r;
	}

	@Override
	public long getAllMovesAsBitBoard(Grid g, int x, int y)
	{
		long result = 0L;

		result |= getBitBoardMovesInDirection(g, x, y, 1, 1);
		result |= getBitBoardMovesInDirection(g, x, y, -1, 1);
		result |= getBitBoardMovesInDirection(g, x, y, -1, -1);
		result |= getBitBoardMovesInDirection(g, x, y, 1, -1);
		return result;
	}
}
