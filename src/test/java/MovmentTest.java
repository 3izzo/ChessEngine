import java.util.ArrayList;
import java.util.List;

import org.testng.TestException;
import org.testng.annotations.Test;

import Main.Grid;
import Pieces.Bishop;
import Pieces.King;
import Pieces.Knight;
import Pieces.Pawn;
import Pieces.Piece;
import Pieces.Queen;
import Pieces.Rook;

public class MovmentTest
{

	private static final boolean PERFT = true;
	private static final int maxDepth = 5;

	private void perft(long[] values, Grid g)
	{
		long startTimeTotal = System.currentTimeMillis();
		for (int i = 1; i <= maxDepth; i++)
		{
			long startTime = System.currentTimeMillis();
			if (i > values.length)
				break;
//			System.out.println("depth " + i + " ---------------------------");
			long numberOfChildren = getNumberOfChildren(g, i);
//			System.out.println(numberOfChildren + " / " + values[i - 1]);
			System.out.println(i + ") time: " + (System.currentTimeMillis() - startTime));
			if (numberOfChildren != values[i - 1])
				throw new TestException("value should be " + values[i - 1] + " but " + numberOfChildren + " is given");
		}
		System.out.println("total time:" + (System.currentTimeMillis() - startTimeTotal));
	}

	@Test(enabled = PERFT)
	public void perftInitialPos()
	{
		long[] values =
		{ 20, 400, 8902, 197281, 4865609, 119060324, 3195901860L, 84998978956L };

		Grid g = new Grid(true);
		perft(values, g);
	}

	@Test(enabled = PERFT)
	public void perftPosition2()
	{
		long[] values =
		{ 48, 2039, 97862, 4085603 };

		Grid g = createGridFromFEN("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq ");
		perft(values, g);
	}

	@Test(enabled = PERFT)
	public void perftPosition3()
	{
		long[] values =
		{ 14, 191, 2812, 43238, 674624, 11030083, 178633661 };

		Grid g = createGridFromFEN("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - -");

		perft(values, g);
	}

	@Test(enabled = PERFT)
	public void perftPosition4()
	{

		long[] values =
		{ 6, 264, 9467, 422333, 15833292, 706045033 };

		Grid g = createGridFromFEN("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1");
		perft(values, g);

	}

	@Test(enabled = PERFT)
	public void perftPosition4M()
	{
		long[] values =
		{ 6, 264, 9467, 422333, 15833292, 706045033 };

		Grid g = createGridFromFEN("r2q1rk1/pP1p2pp/Q4n2/bbp1p3/Np6/1B3NBn/pPPP1PPP/R3K2R b KQ - 0 1");
		perft(values, g);
	}

	@Test(enabled = PERFT)
	public void perftPosition5()
	{
		long[] values =
		{ 44, 1486, 62379, 2103487, 89941194 };

		Grid g = createGridFromFEN("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8");
		perft(values, g);
	}

	@Test(enabled = PERFT)
	public void perftPosition6()
	{
		long[] values =
		{ 46, 2079, 89890, 3894594 };

		Grid g = createGridFromFEN("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10");
		perft(values, g);
	}

	@Test
	public void doesKingShortCastle()
	{
		Grid g = new Grid(true);
		g.setPieceTo(6, 7, null);
		g.setPieceTo(5, 7, null);
		List<Grid> moves = new ArrayList<Grid>();
		g.getPieceAt(4, 7).getPossibleMoves(moves, g, 4, 7);

		if (moves.size() != 2)
		{
			g.display();
			throw new TestException("King doesnt move 2 moves");
		}
	}

	@Test
	public void doesKingLongCastle()
	{
		Grid g = new Grid(true);
		g.setPieceTo(1, 7, null);
		g.setPieceTo(2, 7, null);
		g.setPieceTo(3, 7, null);
		List<Grid> moves = new ArrayList<Grid>();
		g.getPieceAt(4, 7).getPossibleMoves(moves, g, 4, 7);

		if (moves.size() != 2)
		{
			g.display();
			throw new TestException("King doesnt move 2 moves");
		}
	}

	@Test
	public void doesKingCastleAfterTheRookMoved()
	{
		Grid g = new Grid(true);
		g.setPieceTo(5, 7, null);
		g.setPieceTo(6, 7, null);
		g = new Grid(g, 7, 7, 6, 7);
		g = new Grid(g, 7, 1, 7, 2);
		g = new Grid(g, 6, 7, 7, 7);
		List<Grid> moves = new ArrayList<Grid>();
		g.getPieceAt(4, 7).getPossibleMoves(moves, g, 4, 7);

		if (moves.size() != 1)
		{
			g.display();
			throw new TestException("King doesnt move 2 moves");
		}
	}

	@Test
	public void doesKingCastleAfterTheRookWasCaptured()
	{
		Grid g = new Grid(false);
		g.setPieceTo(5, 7, null);
		g.setPieceTo(6, 7, null);

		g.setPieceTo(5, 6, new Knight(false));

		g = new Grid(g, 5, 6, 7, 7);
		List<Grid> moves = new ArrayList<Grid>();
		g.getPieceAt(4, 7).getPossibleMoves(moves, g, 4, 7);

		if (moves.size() != 1)
		{
			g.display();
			throw new TestException("King doesnt move 1 move");
		}
	}

	@Test
	public void doDirectionalPiecesMove()
	{
		Grid g = new Grid(true);
		g.setPieceTo(0, 7, null);
		g.setPieceTo(1, 7, null);
		g.setPieceTo(2, 7, null);
		g.setPieceTo(3, 7, null);
		g.setPieceTo(4, 7, null);
		g.setPieceTo(5, 7, null);
		g.setPieceTo(6, 7, null);

		g.setPieceTo(4, 5, new King(true));

		List<Grid> moves = new ArrayList<Grid>();
		g.getPieceAt(7, 7).getPossibleMoves(moves, g, 7, 7);

		if (moves.size() != 7)
		{
			g.display();
			throw new TestException("Rook doesnt move 7 moves");
		}
	}

	@Test
	public void doDirectionalPiecesCaptureDifferentColor()
	{
		Grid g = new Grid(true);
		g.setPieceTo(0, 7, new Rook(false));
		g.setPieceTo(1, 7, null);
		g.setPieceTo(2, 7, null);
		g.setPieceTo(3, 7, null);
		g.setPieceTo(4, 7, null);
		g.setPieceTo(5, 7, null);
		g.setPieceTo(6, 7, null);

		g.setPieceTo(4, 5, new King(true));

		List<Grid> moves = new ArrayList<Grid>();
		g.getPieceAt(7, 7).getPossibleMoves(moves, g, 7, 7);

		if (moves.size() != 7)
		{
			g.display();
			throw new TestException("Rook doesnt move 7 moves");
		}
	}

	@Test
	public void doDirectionalPiecesCaptureSameColor()
	{
		Grid g = new Grid(true);

		g.setPieceTo(1, 7, null);
		g.setPieceTo(2, 7, null);
		g.setPieceTo(3, 7, null);
		g.setPieceTo(4, 7, null);
		g.setPieceTo(5, 7, null);
		g.setPieceTo(6, 7, null);

		g.setPieceTo(4, 5, new King(true));

		List<Grid> moves = new ArrayList<Grid>();
		g.getPieceAt(7, 7).getPossibleMoves(moves, g, 7, 7);

		if (moves.size() != 6)
		{
			g.display();
			throw new TestException("Rook doesnt move 6 moves");
		}
	}

	@Test
	public void doesKingMove()
	{
		Grid g = new Grid(true);

		g.setPieceTo(4, 7, null);

		g.setPieceTo(4, 4, new King(true));

		List<Grid> moves = new ArrayList<Grid>();
		g.getPieceAt(4, 4).getPossibleMoves(moves, g, 4, 4);

		if (moves.size() != 8)
		{
			g.display();
			throw new TestException("King doesnt move 8 moves");
		}
	}

	@Test
	public void doesKingCaptureAnUnprotectedPieceWhileNotChecked()
	{
		Grid g = new Grid(true);

		g.setPieceTo(4, 7, null);
		g.setPieceTo(4, 4, new King(true));
		g.setPieceTo(4, 5, new Pawn(false));

		List<Grid> moves = new ArrayList<Grid>();
		g.getPieceAt(4, 4).getPossibleMoves(moves, g, 4, 4);

		if (moves.size() != 8)
		{
			g.display();
			throw new TestException("King doesnt move 8 moves");
		}
	}

	@Test
	public void doesKingCaptureAProtectedPieceWhileNotChecked()
	{
		Grid g = new Grid(true);

		g.setPieceTo(4, 7, null);
		g.setPieceTo(4, 4, new King(true));
		g.setPieceTo(4, 3, new Pawn(false));
		g.setPieceTo(3, 2, new Pawn(false));

		List<Grid> moves = new ArrayList<Grid>();
		g.getPieceAt(4, 4).getPossibleMoves(moves, g, 4, 4);

		if (moves.size() != 5)
		{
			g.display();
			System.out.println(moves.size());
			throw new TestException("King doesnt move 5 moves");
		}
	}

	@Test
	public void doesKingCaptureAnUnprotectedPieceWhileCheckedByAProtectedPiece()
	{
		Grid g = new Grid(true);

		g.setPieceTo(4, 7, null);
		g.setPieceTo(4, 4, new King(true));

		g.setPieceTo(4, 5, new Pawn(false));
		g.setPieceTo(3, 3, new Pawn(false));
		g.setPieceTo(2, 2, new Pawn(false));

		List<Grid> moves = new ArrayList<Grid>();
		g.getPieceAt(4, 4).getPossibleMoves(moves, g, 4, 4);

		if (moves.size() != 7)
		{
			g.display();
			throw new TestException("King doesnt move 7 moves");
		}
	}

	@Test
	public void doesKingCaptureAnUnprotectedPieceWhileCheckedByAnUnorotectedPiece()
	{
		Grid g = new Grid(true);

		g.setPieceTo(4, 7, null);
		g.setPieceTo(4, 4, new King(true));

		g.setPieceTo(4, 5, new Pawn(false));
		g.setPieceTo(3, 3, new Pawn(false));

		List<Grid> moves = new ArrayList<Grid>();
		g.getPieceAt(4, 4).getPossibleMoves(moves, g, 4, 4);

		if (moves.size() != 8)
		{
			g.display();
			throw new TestException("King doesnt move 8 moves");
		}
	}

	@Test
	public void doesKingCaptureAPieceProtectedByTheOtherKingWhileChecked()
	{
		Grid g = new Grid(true);

		g.setPieceTo(4, 7, null);
		g.setPieceTo(4, 4, new King(true));

		g.setPieceTo(3, 3, new Pawn(false));

		g.setPieceTo(4, 0, null);
		g.setPieceTo(2, 2, new King(false));

		List<Grid> moves = new ArrayList<Grid>();
		g.getPieceAt(4, 4).getPossibleMoves(moves, g, 4, 4);

		if (moves.size() != 7)
		{
			g.display();
			throw new TestException("King doesnt move 7 moves");
		}
	}

	@Test
	public void doesKingCaptureAPieceProtectedByTheOtherKingWhileNotChecked()
	{
		Grid g = new Grid(true);

		g.setPieceTo(4, 7, null);
		g.setPieceTo(3, 4, new King(true));

		g.setPieceTo(3, 3, new Pawn(false));

		g.setPieceTo(4, 0, null);
		g.setPieceTo(2, 2, new King(false));

		List<Grid> moves = new ArrayList<Grid>();
		g.getPieceAt(3, 4).getPossibleMoves(moves, g, 3, 4);

		if (moves.size() != 4)
		{
			g.display();
			throw new TestException("King doesnt move 4 moves");
		}
	}

	@Test
	public void doPinnedPiecesMoveIfTheyCannotAttack()
	{
		Grid g = getAnEmptyGrid();

		g.setPieceTo(3, 4, new King(true));
		g.setPieceTo(7, 4, new Queen(false));
		g.setPieceTo(4, 4, new Bishop(true));
		List<Grid> moves = new ArrayList<Grid>();
		g.getPieceAt(4, 4).getPossibleMoves(moves, g, 4, 4);

		if (moves.size() != 0)
		{
			g.display();
			throw new TestException("Bishop should not move");
		}
	}

	@Test
	public void doPinnedPiecesMoveIfTheyCanAttack()
	{
		Grid g = getAnEmptyGrid();

		g.setPieceTo(3, 4, new King(true));
		g.setPieceTo(7, 4, new Queen(false));
		g.setPieceTo(4, 4, new Rook(true));
		List<Grid> moves = new ArrayList<Grid>();
		g.getPieceAt(4, 4).getPossibleMoves(moves, g, 4, 4);

		if (moves.size() != 3)
		{
			g.display();
			throw new TestException("rook doesnt move 3 moves");
		}
	}

	@Test
	public void doPinnedPiecesMoveIfTheyCanCheckMate()
	{
		Grid g = getAnEmptyGrid();

		g.setPieceTo(3, 4, new King(true));
		g.setPieceTo(4, 4, new Rook(true));

		g.setPieceTo(7, 4, new Queen(false));

		g.setPieceTo(7, 3, new Pawn(false));
		g.setPieceTo(6, 3, new Pawn(false));

		g.setPieceTo(7, 2, new King(false));

		g.setPieceTo(7, 1, new Pawn(false));
		g.setPieceTo(6, 1, new Pawn(false));

		List<Grid> moves = new ArrayList<Grid>();
		g.getPieceAt(4, 4).getPossibleMoves(moves, g, 4, 4);

		if (moves.size() != 3)
		{
			g.display();
			throw new TestException("rook doesnt move 3 moves");
		}
	}

	@Test
	public void doesKingMoveIntoChekByPinnedPiece()
	{

		Grid g = getAnEmptyGrid();

		g.setPieceTo(5, 5, new King(true));
		g.setPieceTo(6, 6, new Rook(true));

		g.setPieceTo(7, 7, new Bishop(false));

		g.setPieceTo(7, 0, new King(false));

		List<Grid> moves = new ArrayList<Grid>();
		g.getPieceAt(7, 0).getPossibleMoves(moves, g, 7, 0);

		if (moves.size() != 1)
		{
			g.display();
			throw new TestException("King doesnt move 1 move ");
		}
	}

	@Test
	public void doesPawnMoveOneStepIfItIsNotOccuipied()
	{

		Grid g = getAnEmptyGrid();

		g.setPieceTo(0, 7, new King(true));
		g.setPieceTo(7, 0, new King(false));

		g.setPieceTo(0, 2, new Pawn(true));

		List<Grid> moves = new ArrayList<Grid>();
		g.getPieceAt(0, 2).getPossibleMoves(moves, g, 0, 2);

		if (moves.size() != 1)
		{
			g.display();
			throw new TestException("Pawn doesnt move 1 move ");
		}
	}

	@Test
	public void doesPawnMoveTwoStepsIfItIsNotOccuipied()
	{

		Grid g = getAnEmptyGrid();

		g.setPieceTo(0, 7, new King(true));
		g.setPieceTo(7, 0, new King(false));

		g.setPieceTo(0, 6, new Pawn(true));

		List<Grid> moves = new ArrayList<Grid>();
		g.getPieceAt(0, 6).getPossibleMoves(moves, g, 0, 6);

		if (moves.size() != 2)
		{
			g.display();
			throw new TestException("Pawn doesnt move 2 moves ");
		}
	}

	@Test
	public void doesPawnMoveTwoStepsIfTheFirstIsOccuipied()
	{

		Grid g = getAnEmptyGrid();

		g.setPieceTo(0, 7, new King(true));
		g.setPieceTo(7, 0, new King(false));

		g.setPieceTo(0, 6, new Pawn(true));
		g.setPieceTo(0, 5, new Bishop(true));

		List<Grid> moves = new ArrayList<Grid>();
		g.getPieceAt(0, 6).getPossibleMoves(moves, g, 0, 6);

		if (moves.size() != 0)
		{
			g.display();
			throw new TestException("Pawn doesnt move 0 moves ");
		}
	}

	@Test
	public void doesPawnMoveTwoStepsIfTheSecondIsOccuipied()
	{

		Grid g = getAnEmptyGrid();

		g.setPieceTo(0, 7, new King(true));
		g.setPieceTo(7, 0, new King(false));

		g.setPieceTo(0, 6, new Pawn(true));
		g.setPieceTo(0, 4, new Bishop(true));

		List<Grid> moves = new ArrayList<Grid>();
		g.getPieceAt(0, 6).getPossibleMoves(moves, g, 0, 6);

		if (moves.size() != 1)
		{
			g.display();
			throw new TestException("Pawn doesnt move 1 moves ");
		}
	}

	@Test
	public void doesPawnPromote()
	{

		Grid g = getAnEmptyGrid();

		g.setPieceTo(0, 7, new King(true));
		g.setPieceTo(7, 0, new King(false));

		g.setPieceTo(0, 1, new Pawn(true));

		List<Grid> moves = new ArrayList<Grid>();
		g.getPieceAt(0, 1).getPossibleMoves(moves, g, 0, 1);

		if (moves.size() != 4)
		{
			g.display();
			throw new TestException("Pawn doesnt move 4 moves ");
		}
	}

	@Test
	public void doesPawnCapturePromote()
	{

		Grid g = getAnEmptyGrid();

		g.setPieceTo(0, 7, new King(true));
		g.setPieceTo(7, 0, new King(false));

		g.setPieceTo(1, 0, new Bishop(false));

		g.setPieceTo(0, 1, new Pawn(true));

		List<Grid> moves = new ArrayList<Grid>();
		g.getPieceAt(0, 1).getPossibleMoves(moves, g, 0, 1);

		if (moves.size() != 8)
		{
			g.display();
			System.out.println(moves.size());
			throw new TestException("Pawn doesnt move 8 moves ");
		}
	}

	@Test
	public void doesPawnMoveOneStepIfItIsOccuipiedBySameColor()
	{

		Grid g = getAnEmptyGrid();

		g.setPieceTo(0, 7, new King(true));
		g.setPieceTo(7, 0, new King(false));

		g.setPieceTo(0, 3, new Bishop(true));
		g.setPieceTo(0, 4, new Pawn(true));

		List<Grid> moves = new ArrayList<Grid>();
		g.getPieceAt(0, 4).getPossibleMoves(moves, g, 0, 4);

		if (moves.size() != 0)
		{
			g.display();
			throw new TestException("Pawn doesnt move 0 moves ");
		}
	}

	@Test
	public void doesPawnMoveOneStepIfItIsOccuipiedByDifferentColor()
	{

		Grid g = getAnEmptyGrid();

		g.setPieceTo(0, 7, new King(true));
		g.setPieceTo(7, 0, new King(false));

		g.setPieceTo(0, 3, new Bishop(false));
		g.setPieceTo(0, 4, new Pawn(true));

		List<Grid> moves = new ArrayList<Grid>();
		g.getPieceAt(0, 4).getPossibleMoves(moves, g, 0, 4);

		if (moves.size() != 0)
		{
			g.display();
			throw new TestException("Pawn doesnt move 0 moves ");
		}
	}

	@Test
	public void doesPawnMoveCaptureDifferentColor()
	{

		Grid g = getAnEmptyGrid();

		g.setPieceTo(0, 7, new King(true));
		g.setPieceTo(7, 0, new King(false));

		g.setPieceTo(0, 3, new Bishop(false));
		g.setPieceTo(2, 3, new Bishop(false));
		g.setPieceTo(1, 4, new Pawn(true));

		List<Grid> moves = new ArrayList<Grid>();
		g.getPieceAt(1, 4).getPossibleMoves(moves, g, 1, 4);

		if (moves.size() != 3)
		{
			g.display();
			throw new TestException("Pawn doesnt move 0 moves ");
		}
	}

	@Test
	public void doesPawnCaptureSameColor()
	{

		Grid g = getAnEmptyGrid();

		g.setPieceTo(0, 7, new King(true));
		g.setPieceTo(7, 0, new King(false));

		g.setPieceTo(0, 3, new Bishop(true));
		g.setPieceTo(2, 3, new Bishop(true));
		g.setPieceTo(1, 4, new Pawn(true));

		List<Grid> moves = new ArrayList<Grid>();
		g.getPieceAt(1, 4).getPossibleMoves(moves, g, 1, 4);

		if (moves.size() != 1)
		{
			g.display();
			throw new TestException("Pawn doesnt move 0 moves ");
		}
	}

	@Test
	public void doesPawnEnpassant()
	{

		Grid g = getAnEmptyGrid();
		g.whitePlaysNext = false;
		g.setPieceTo(0, 7, new King(true));
		g.setPieceTo(7, 0, new King(false));

		g.setPieceTo(1, 3, new Pawn(true));
		g.setPieceTo(0, 1, new Pawn(false));

		g = new Grid(g, 0, 1, 0, 3);
		List<Grid> moves = new ArrayList<Grid>();

		g.getPieceAt(1, 3).getPossibleMoves(moves, g, 1, 3);

		if (moves.size() != 2)
		{
			g.display();
			for (Grid grid : moves)
			{
				grid.display();
			}
			throw new TestException("Pawn doesnt move 2 moves ");
		}
	}

	@Test
	public void staleMateKingNoPinnedPieces()
	{
		Grid g = getAnEmptyGrid();

		g.setPieceTo(0, 7, new King(true));
		g.setPieceTo(7, 0, new King(false));

		g.setPieceTo(0, 1, new Rook(true));
		g.setPieceTo(6, 6, new Rook(true));

		Grid gBlack = new Grid(g, 1, 1, 1, 1);

		if (g.isStaleMate() || !gBlack.isStaleMate())
		{
			g.display();
			throw new TestException("");
		}
	}

	@Test
	public void staleMateKingWithPinnedPieces()
	{
		Grid g = getAnEmptyGrid();

		g.setPieceTo(0, 7, new King(true));

		g.setPieceTo(7, 0, new King(false));

		g.setPieceTo(0, 1, new Rook(true));
		g.setPieceTo(0, 0, new Rook(true));

		g.setPieceTo(6, 0, new Bishop(false));

		Grid gBlack = new Grid(g, 1, 1, 1, 1);

		if (g.isStaleMate() || !gBlack.isStaleMate())
		{
			g.display();
			throw new TestException("");
		}
	}

	private static Grid getAnEmptyGrid()
	{
		Grid g = new Grid(true);
		for (int i = 0; i < 8; i++)
		{
			for (int j = 0; j < 8; j++)
			{
				g.setPieceTo(i, j, null);
			}
		}
		return g;
	}

	private long getNumberOfChildren(Grid g, int depth)// , boolean b)
	{
		if (depth == 1)
		{
			return g.getPossibleChildren().size();
		} else
		{
			long result = 0L;
			for (Grid grid : g.getPossibleChildren())
			{
				long numberOfChildren = getNumberOfChildren(grid, depth - 1);// , false);
				result += numberOfChildren;
//				if (b)
//				{
//					System.out.print(getPlayedPiece(grid));
//					System.out.println(numberOfChildren);
//				}
			}
			g.children = null;
			return result;
		}
	}

	private long getNumberOfChildren(Grid g, int depth, boolean b)
	{
		if (depth == 0)
		{
			return 1;
		} else
		{
			long result = 0L;
			for (Grid grid : g.getPossibleChildren())
			{
				long numberOfChildren = getNumberOfChildren(grid, depth - 1, false);
				result += numberOfChildren;
				if (b)
				{
					System.out.print(getPlayedPiece(grid));
					System.out.println(numberOfChildren);
				}
			}
			g.children = null;
			return result;
		}
	}

	private String getPlayedPiece(Grid toPlay)
	{
		int fromX = 0, fromY = 0, toX = 0, toY = 0;
		Class<? extends Piece> typeOF = null;
		String promotedTo = "";
		for (int y = 0; y < 8; y++)
		{
			for (int x = 0; x < 8; x++)
			{
				Piece pieceAt = toPlay.prevGrid.getPieceAt(x, y);
				if (pieceAt != null && toPlay.getPieceAt(x, y) == null)
				{
					fromX = x;
					fromY = y;
					typeOF = pieceAt.getClass();
					break;
				}
			}
		}
		for (int y = 0; y < 8; y++)
		{
			for (int x = 0; x < 8; x++)
			{
				Piece pieceAt = toPlay.getPieceAt(x, y);
				if (!(x == fromX && y == fromY) && toPlay.prevGrid.getPieceAt(x, y) != pieceAt)
				{
					if (typeOF == King.class && pieceAt.getClass() != King.class)
						continue;
					toX = x;
					toY = y;
					if (typeOF == Pawn.class && (toY == 7 || toY == 0))
					{
						switch (pieceAt.getClass().getSimpleName())
						{
						case "Knight":
							promotedTo = "n";
							break;
						case "Queen":
							promotedTo = "q";
							break;
						case "Bishop":
							promotedTo = "b";
							break;
						case "Rook":
							promotedTo = "r";
							break;
						}
					}
					break;
				}
			}
		}

		return String.format("%s%d%s%d%s: ", (char) ('a' + fromX), 8 - fromY, (char) ('a' + toX), 8 - toY, promotedTo);

	}

	private Grid createGridFromFEN(String FEN)
	{
		String[] split = FEN.split(" ");
		boolean whitePlaysNext = split[1].startsWith("w");
		Grid g = new Grid(whitePlaysNext);

		for (int i = 0; i < 8; i++)
		{
			for (int j = 0; j < 8; j++)
			{
				g.setPieceTo(i, j, null);
			}
		}

		String[] ranks = split[0].split("/");
		for (int i = 0; i < ranks.length; i++)
		{
			String rank = ranks[i];
			int offset = 0;
			for (int j = 0; j < rank.length(); j++)
			{
				boolean white = rank.charAt(j) == rank.toUpperCase().charAt(j);
				if (rank.toLowerCase().charAt(j) == 'p')
					g.setPieceTo(j + offset, i, new Pawn(white));
				else if (rank.toLowerCase().charAt(j) == 'n')
					g.setPieceTo(j + offset, i, new Knight(white));
				else if (rank.toLowerCase().charAt(j) == 'b')
					g.setPieceTo(j + offset, i, new Bishop(white));
				else if (rank.toLowerCase().charAt(j) == 'r')
					g.setPieceTo(j + offset, i, new Rook(white));
				else if (rank.toLowerCase().charAt(j) == 'q')
					g.setPieceTo(j + offset, i, new Queen(white));
				else if (rank.toLowerCase().charAt(j) == 'k')
					g.setPieceTo(j + offset, i, new King(white));
				else
				{
					offset += Integer.parseInt("" + rank.charAt(j)) - 1;
				}
			}
		}

		// kings
		g.piecesThatDidntMove.add(4);
		g.piecesThatDidntMove.add(7 * 8 + 4);

		if (split[2].contains("K"))
			g.piecesThatDidntMove.add(7 * 8 + 7);
		if (split[2].contains("Q"))
			g.piecesThatDidntMove.add(7 * 8);
		if (split[2].contains("k"))
			g.piecesThatDidntMove.add(7);
		if (split[2].contains("q"))
			g.piecesThatDidntMove.add(0);

		return g;
	}

}
