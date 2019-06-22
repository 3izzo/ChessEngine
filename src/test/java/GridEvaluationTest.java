import java.util.Scanner;

import org.junit.Test;
import org.testng.TestException;

import Main.Grid;

public class GridEvaluationTest
{
	@Test
	public void blackForwardSpan()
	{
		long blackPawns = getBitboard(". . . . . . . . . . . . . . . . . . . . 1 . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .");

		long frontSpan = Grid.getFrontAttackSpan(false, blackPawns);

		if (frontSpan != getBitboard("0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 1 0 0 0 0 0 1 0 1 0 0 0 0 0 1 0 1 0 0 0 0 0 1 0 1 0 0 0 0 0 1 0 1 0 0 "))
			throw new TestException("");
	}

	@Test
	public void whiteForwardSpan()
	{
		long pawns = getBitboard(". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 1 . . . . . . . . . . . . . . .");

		long frontSpan = Grid.getFrontAttackSpan(true, pawns);

		if (frontSpan != getBitboard("0 1 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0"))
		{
			throw new TestException("");
		}
	}

	@Test
	public void fillDown()
	{
		long pawns = getBitboard(". . . . . . . . . . . 1 . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .");

		long filledDown = Grid.fill(pawns, true);

		if (filledDown != getBitboard("0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 1 0 0 0 0 "))
		{
			displayBitBoard(pawns);
			displayBitBoard(filledDown);
			throw new TestException("");
		}
	}

	@Test
	public void fillUp()
	{
		long pawns = getBitboard(". . . . . . . . . . . . . . . . . . . . . . . . . . . . 1 . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .");

		long filledUp = Grid.fill(pawns, false);

		if (filledUp != getBitboard("0 0 0 0 1 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "))
		{
			displayBitBoard(pawns);
			displayBitBoard(filledUp);
			throw new TestException("");
		}
	}

	@Test
	public void backwardsPawn()
	{
		long whitePawns = getBitboard(". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 1 . 1 . 1 . . . . . . . . . . . . 1 . 1 . . . . . . . . . . . . . . ");
		long blackPawns = getBitboard(". . . . . . . . . . . . . 1 . . . . . . . . 1 . 1 . 1 . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .");

		long backwardPawns = Grid.getBackwardPawns(true, whitePawns, blackPawns);
		if (Long.bitCount(backwardPawns) != 2)
			throw new TestException("");
	}

	@Test
	public void attacksBlack()
	{
		long blackPawns = getBitboard(". . . . . . . . 1 . . . 1 . . 1 . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .");
		long bAttacks = Grid.getAttacks(false, true, blackPawns) | Grid.getAttacks(false, false, blackPawns);
		if (bAttacks != getBitboard("0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 1 0 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "))
		{
			displayBitBoard(blackPawns);
			displayBitBoard(bAttacks);
			throw new TestException("");
		}
	}

	@Test
	public void attacksWhite()
	{
		long whitePawns = getBitboard(". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 1 . . . 1 . . 1 . . . . . . . .");
		long attacks = Grid.getAttacks(true, true, whitePawns) | Grid.getAttacks(true, false, whitePawns);
		if (attacks != getBitboard("0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 1 0 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 "))
		{
			displayBitBoard(whitePawns);
			displayBitBoard(attacks);
			throw new TestException("");
		}
	}
//
//	@Test
//	public void fill()
//	{
//		//@formatter:off
//		long whitePawns = getBitboard(
//				  ". . . . . . . . "
//				+ ". . . . . . . . "
//				+ ". 1 . . . . . . "
//				+ ". . 1 . . . . . "
//				+ ". . . . . . . . "
//				+ ". . . . . . . . "
//				+ ". . . . . . 1 . "
//				+ ". . . . . . . . ");
//		
//		displayBitBoard(Grid.fill(whitePawns, true));
//		//@formatter:on
//	}

//	@Test
//	public void candidates()
//	{
//		//@formatter:off
//		long whitePawns = getBitboard(
//				  ". . . . . . . . "
//				+ ". . . . . . . . "
//				+ ". 1 . . . . . . "
//				+ ". . 1 . . . . . "
//				+ ". . . . . . . . "
//				+ ". . . . . . . . "
//				+ ". . . . . . 1 . "
//				+ ". . . . . . . . ");
//		
//		long blackPawns = getBitboard(
//				  ". . . . . . . . "
//				+ ". . 1 . . . . . "
//				+ ". . . . . . . . "
//				+ ". . . . . . . . "
//				+ ". . . . . 1 1 . "
//				+ ". . . . . . . . "
//				+ ". . . . . . . . "
//				+ ". . . . . . . . ");
//		
//		//@formatter:on
//
//		long blackAttacks = Grid.getAttacks(false, true, blackPawns) | Grid.getAttacks(false, false, blackPawns);
//		long safeSquares = Grid.safePawnSquares(whitePawns, blackPawns) >> 8;
//		long attackedSafeSqr = safeSquares & blackAttacks;
//		long blackFrontSpan = Grid.getFrontAttackSpan(false, blackPawns);
//		long result = whitePawns & ~blackFrontSpan & (attackedSafeSqr >> 8);
//
//		displayBitBoard(result);
//	}

	public static void displayBitBoard(long board)
	{
		for (int i = 0; i < 64; i++)
		{

			System.out.print(((board >> i) & 1L) + " ");
			if (i % 8 == 7)
				System.out.println();
		}
		System.out.println("=========================");
	}

	public static long getBitboard(String s)
	{
		long result = 0;
//		Scanner sc = new Scanner(System.in);
		Scanner sc = new Scanner(s);
		for (int i = 0; i < 64; i++)
		{
			String next = sc.next();
			if (next.equals("1"))
			{
				result |= (1L << i);
			}
		}
		return result;
	}

}
