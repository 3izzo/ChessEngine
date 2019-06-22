package Pieces;

import java.util.ArrayList;
import java.util.List;

import Main.Grid;

public class Knight extends Piece
{

	public Knight(boolean white)
	{
		super(white);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Grid> getAllMoves(Grid g, int x, int y, boolean takeCastleIntoConsideration)
	{
		List<Grid> r = new ArrayList<Grid>();
		checkAndAdd(g, x, y, 1, 2, r);
		checkAndAdd(g, x, y, 1, -2, r);
		checkAndAdd(g, x, y, -1, 2, r);
		checkAndAdd(g, x, y, -1, -2, r);

		checkAndAdd(g, x, y, 2, 1, r);
		checkAndAdd(g, x, y, 2, -1, r);
		checkAndAdd(g, x, y, -2, 1, r);
		checkAndAdd(g, x, y, -2, -1, r);

		return r;
	}

	private void checkAndAdd(Grid g, int x, int y, int i, int j, List<Grid> r)
	{
		if (x + i < 0 || x + i > 7 || y + j < 0 || y + j > 7)
			return;
		Piece pieceAt = g.getPieceAt(x + i, y + j);
		if (pieceAt == null || pieceAt.white != this.white)
			r.add(new Grid(g, x, y, x + i, y + j));

	}

	@Override
	public List<Piece> getPiecesAttackedByMe(Grid g, int x, int y)
	{
		List<Piece> r = new ArrayList<Piece>();

		extracted(g, x + 1, y + 2, r);
		extracted(g, x - 1, y + 2, r);
		extracted(g, x + 1, y - 2, r);
		extracted(g, x - 1, y - 2, r);

		extracted(g, x + 2, y + 1, r);
		extracted(g, x - 2, y + 1, r);
		extracted(g, x + 2, y - 1, r);
		extracted(g, x - 2, y - 1, r);

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

	private void extracted(Grid g, int x, int y, List<Piece> r)
	{
		if (inRange(x, y))
			r.add(g.getPieceAt(x, y));
	}

	@Override
	public List<Piece> getPiecesDefendedByMe(Grid g, int x, int y)
	{
		List<Piece> r = new ArrayList<Piece>();

		extracted(g, x + 1, y + 2, r);
		extracted(g, x - 1, y + 2, r);
		extracted(g, x + 1, y - 2, r);
		extracted(g, x - 1, y - 2, r);

		extracted(g, x + 2, y + 1, r);
		extracted(g, x - 2, y + 1, r);
		extracted(g, x + 2, y - 1, r);
		extracted(g, x - 2, y - 1, r);

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

		result |= ifInRangeGiveShifted(x + 2, y + 1);
		result |= ifInRangeGiveShifted(x + 2, y - 1);
		result |= ifInRangeGiveShifted(x - 2, y + 1);
		result |= ifInRangeGiveShifted(x - 2, y - 1);
		result |= ifInRangeGiveShifted(x + 1, y + 2);
		result |= ifInRangeGiveShifted(x + 1, y - 2);
		result |= ifInRangeGiveShifted(x - 1, y + 2);
		result |= ifInRangeGiveShifted(x - 1, y - 2);

		return result;
	}

	private long ifInRangeGiveShifted(int x, int y)
	{
		return inRange(x, y) ? 1l << (y) * 8 + x : 0L;
	}

	private boolean inRange(int x, int y)
	{
		return x >= 0 && x < 8 && y >= 0 && y < 8;

	}

}
