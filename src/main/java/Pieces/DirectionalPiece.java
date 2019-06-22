package Pieces;

import java.util.List;

import Main.Grid;

public class DirectionalPiece extends Piece
{

	public DirectionalPiece(boolean white)
	{
		super(white);
	}

	protected void getMovesInDirection(Grid g, int x, int y, int directionX, int directionY, List<Grid> result)
	{
		for (int i = 1; i < 8; i++)
		{
			int xCalculated = x + i * directionX;
			int yCalculated = y + i * directionY;
			if (xCalculated < 0 || xCalculated > 7 || yCalculated < 0 || yCalculated > 7)
				break;
			Piece pieceAt = g.getPieceAt(xCalculated, yCalculated);

			if (pieceAt == null)
				result.add(new Grid(g, x, y, xCalculated, yCalculated));
			else
			{
				if (pieceAt.white != g.whitePlaysNext)
					result.add(new Grid(g, x, y, xCalculated, yCalculated));

				break;
			}
		}
	}

	protected Piece getNearesetPieceInDirection(Grid g, int x, int y, int directionX, int directionY)
	{
		for (int i = 1; i < 8; i++)
		{
			int xCalculated = x + i * directionX;
			int yCalculated = y + i * directionY;
			if (xCalculated < 0 || xCalculated > 7 || yCalculated < 0 || yCalculated > 7)
				break;
			Piece pieceAt = g.getPieceAt(xCalculated, yCalculated);

			if (pieceAt != null)
				return pieceAt;
		}
		return null;
	}
	
	/**
	 * return all moves in direction. defending/attacking a piece is considered a move
	 */
	protected long getBitBoardMovesInDirection(Grid g, int x, int y, int directionX, int directionY)
	{
		long result = 0L;

		for (int i = 1; i < 8; i++)
		{
			int xCalculated = x + i * directionX;
			int yCalculated = y + i * directionY;

			if (xCalculated < 0 || xCalculated > 7 || yCalculated < 0 || yCalculated > 7)
				break;

			Piece pieceAt = g.getPieceAt(xCalculated, yCalculated);

			result |= 1L << (xCalculated + yCalculated * 8);

			if (pieceAt != null)
				break;

		}

		return result;
	}
}
