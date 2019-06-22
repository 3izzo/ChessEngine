package Pieces;

import java.util.List;

import Main.Grid;

public class Piece
{
	public boolean white;

	public Piece(boolean white)
	{
		this.white = white;
	}

	public List<Grid> getAllMoves(Grid g, int x, int y, boolean takeCastleIntoConsideration)
	{
		return null;
	}

	public List<Piece> getPiecesAttackedByMe(Grid g, int x, int y)
	{
		return null;
	}

	public List<Piece> getPiecesDefendedByMe(Grid g, int x, int y)
	{
		return null;
	}

	public long getAllMovesAsBitBoard(Grid g, int x, int y)
	{
		return 0L;
	}

	public void getPossibleMoves(List<Grid> possibleMoves, Grid g, int x, int y)
	{
		List<Grid> allMoves = getAllMoves(g, x, y, true);
		for (int i = 0; i < allMoves.size(); i++)
		{
			Grid grid = allMoves.get(i);
			if (!grid.isChecked(white))
			{
				possibleMoves.add(grid);
			}
		}
	}

	@Override
	public boolean equals(Object obj)
	{
		try
		{
			Piece p = (Piece) obj;
			if (p != null)
				return p.white == white && p.getClass() == this.getClass();
		} catch (Exception e)
		{
			// TODO: handle exception
		}
		return false;
	}
}
