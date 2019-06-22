package Pieces;

import java.util.ArrayList;
import java.util.List;

import Main.Grid;

public class King extends Piece
{

	public King(boolean white)
	{
		super(white);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Grid> getAllMoves(Grid g, int x, int y, boolean takeCastleIntoConsideration)
	{
		List<Grid> r = new ArrayList<Grid>();
		for (int i = -1; i <= 1; i++)
		{
			for (int j = -1; j <= 1; j++)
			{
				if (i == 0 && j == 0)
					continue;
				if (x + i < 0 || x + i > 7 || y + j < 0 || y + j > 7)
					continue;
				Piece pieceAt = g.getPieceAt(x + i, y + j);
				if (pieceAt == null || pieceAt.white != this.white)
				{
					r.add(new Grid(g, x, y, x + i, y + j));
				}
			}
		}
		if (takeCastleIntoConsideration && !g.didPieceMove(x, y) && !g.isChecked(white))
		{
			// check for short castle
			if (!g.didPieceMove(7, y) && g.getPieceAt(6, y) == null && g.getPieceAt(5, y) == null)
			{
				Grid temp = new Grid(g, x, y, 5, y);
				if (!temp.isChecked(white))
				{
					Grid gShortCastle = new Grid(g, x, y, 6, y);// moved the king
					gShortCastle.setPieceTo(5, y, g.getPieceAt(7, y));
					gShortCastle.setPieceTo(7, y, null);
					r.add(gShortCastle);
				}
			}

			// check for long castle
			if (!g.didPieceMove(0, y) && g.getPieceAt(1, y) == null && g.getPieceAt(2, y) == null && g.getPieceAt(3, y) == null)
			{
				Grid temp = new Grid(g, x, y, 3, y);
				if (!temp.isChecked(white))
				{
					Grid gLongCastle = new Grid(g, x, y, 2, y);// moved the king
					gLongCastle.setPieceTo(3, y, g.getPieceAt(0, y));
					gLongCastle.setPieceTo(0, y, null);
					r.add(gLongCastle);
				}
			}

		}
		return r;
	}

	@Override
	public long getAllMovesAsBitBoard(Grid g, int x, int y)
	{
		long result = 0;
		for (int i = -1; i <= 1; i++)
		{
			for (int j = -1; j <= 1; j++)
			{
				if (i == 0 && j == 0)
					continue;
				if (x + i < 0 || x + i > 7 || y + j < 0 || y + j > 7)
					continue;
				Piece pieceAt = g.getPieceAt(x + i, y + j);
				if (pieceAt == null || pieceAt.white != this.white)
				{
					result |= 1L << (y + j) * 8 + (x + i);
				}
			}
		}
		return result;
	}
}
