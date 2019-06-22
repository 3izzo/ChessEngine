package Main;

import java.util.ArrayList;
import java.util.List;

import Pieces.Bishop;
import Pieces.King;
import Pieces.Knight;
import Pieces.Pawn;
import Pieces.Piece;
import Pieces.Queen;
import Pieces.Rook;

public class Grid {
    public Piece[] pieces;
    public boolean whitePlaysNext;
    public List<Grid> children;
    public Grid prevGrid;
    public List<Integer> piecesThatDidntMove;
    public static final int maxValue = 123456789, minValue = -123456789;
    public Integer gridsValue;
    public int stepNumber = 1;

    private static final long AFile = 0b1000000010000000100000001000000010000000100000001000000010000000L;
    private static final long HFile = 0b0000000100000001000000010000000100000001000000010000000100000001L;

    private static final long RANK1 = 0b0000000000000000000000000000000000000000000000000000000011111111L;
    private static final long RANK8 = 0b1111111100000000000000000000000000000000000000000000000000000000L;

    private static final long CENTER_SQUARES = 0b00000000000000000000000000011000000110000000000000000000L;

    public Grid(Grid prevGrid, int fromX, int fromY, int toX, int toY) {
	this.whitePlaysNext = !prevGrid.whitePlaysNext;
	this.prevGrid = prevGrid;

	this.pieces = prevGrid.pieces.clone();
	this.piecesThatDidntMove = new ArrayList<Integer>(prevGrid.piecesThatDidntMove);

	setPieceTo(fromX, fromY, null);
	setPieceTo(toX, toY, prevGrid.getPieceAt(fromX, fromY));
	stepNumber = prevGrid.stepNumber + 1;
    }

    public Grid(boolean whitePlaysNext) {
	this.whitePlaysNext = whitePlaysNext;
	this.pieces = new Piece[64];
	this.piecesThatDidntMove = new ArrayList<Integer>();

	setPieceTo(0, 0, new Rook(false));
	setPieceTo(7, 0, new Rook(false));
	setPieceTo(1, 0, new Knight(false));
	setPieceTo(6, 0, new Knight(false));
	setPieceTo(2, 0, new Bishop(false));
	setPieceTo(5, 0, new Bishop(false));
	setPieceTo(3, 0, new Queen(false));
	setPieceTo(4, 0, new King(false));
	for (int i = 0; i < 8; i++)
	    setPieceTo(i, 1, new Pawn(false));

	setPieceTo(0, 7, new Rook(true));
	setPieceTo(7, 7, new Rook(true));
	setPieceTo(1, 7, new Knight(true));
	setPieceTo(6, 7, new Knight(true));
	setPieceTo(5, 7, new Bishop(true));
	setPieceTo(2, 7, new Bishop(true));
	setPieceTo(3, 7, new Queen(true));
	setPieceTo(4, 7, new King(true));
	for (int i = 0; i < 8; i++)
	    setPieceTo(i, 6, new Pawn(true));

	// black rooks
	piecesThatDidntMove.add(0);
	piecesThatDidntMove.add(7);
	// white rooks
	piecesThatDidntMove.add(7 * 8);
	piecesThatDidntMove.add(7 * 8 + 7);
	// kings
	piecesThatDidntMove.add(4);
	piecesThatDidntMove.add(7 * 8 + 4);

    }

    public Piece getPieceAt(int x, int y) {
	return pieces[y * 8 + x];
    }

    public void setPieceTo(int i, int j, Piece p) {
	int pos = j * 8 + i;
	if (!didPieceMove(i, j) && (p == null || !p.equals(getPieceAt(i, j))))
	    piecesThatDidntMove.remove(new Integer(pos));
	pieces[pos] = p;

    }

    public boolean didPieceMove(int x, int y) {
	int pos = y * 8 + x;
	return !piecesThatDidntMove.contains(pos);
    }

    public boolean isChecked(boolean checkForWhite) {

	long oponentAttacks = 0;
	long kingPos = 0;
	for (int x = 0; x < 8; x++) {
	    for (int y = 0; y < 8; y++) {
		Piece piece = getPieceAt(x, y);
		if (piece != null)
		    if (piece.white != checkForWhite)
			oponentAttacks |= piece.getAllMovesAsBitBoard(this, x, y);
		    else if (piece instanceof King) {
			kingPos = 1L << y * 8 + x;
		    }
	    }
	}
	return ((kingPos & oponentAttacks) != 0);
//		Grid oppositeTurn;
//
//		if (checkForWhite != whitePlaysNext)
//			oppositeTurn = this;
//		else
//			oppositeTurn = new Grid(this, 1, 1, 1, 1);
//
//		List<Grid> children2 = oppositeTurn.getAllChildren(false);
//		for (int j = 0; j < children2.size(); j++)
//		{
//			Grid grid = children2.get(j);
//			boolean hasKing = false;
//			for (int i = 0; i < grid.pieces.length; i++)
//			{
//				if (grid.pieces[i] instanceof King && grid.pieces[i].white == checkForWhite)
//				{
//					hasKing = true;
//					break;
//				}
//			}
//			if (!hasKing)
//				return true;
//		}
    }

    public boolean isCheckMated(boolean checkForWhite) {

	long oponentAttacks = 0;
	long kingPosAndMoves = 0;
	for (int x = 0; x < 8; x++) {
	    for (int y = 0; y < 8; y++) {
		Piece piece = getPieceAt(x, y);
		if (piece != null)
		    if (piece.white != checkForWhite)
			oponentAttacks |= piece.getAllMovesAsBitBoard(this, x, y);
		    else if (piece instanceof King) {
			kingPosAndMoves = 1L << y * 8 + x;
			kingPosAndMoves |= piece.getAllMovesAsBitBoard(this, x, y);
		    }
	    }
	}
	return ((kingPosAndMoves & ~oponentAttacks) == 0L);
    }

    public boolean isStaleMate() {
	return getPossibleChildren().size() == 0 && !isChecked(whitePlaysNext);
    }

    public List<Grid> getAllChildren(boolean takeCastleIntoConsideration) {

	List<Grid> children = new ArrayList<Grid>();
	for (int i = 0; i < 8; i++) {
	    for (int j = 0; j < 8; j++) {
		Piece piece = getPieceAt(i, j);
		if (piece != null && piece.white == whitePlaysNext) {
		    children.addAll(piece.getAllMoves(this, i, j, takeCastleIntoConsideration));
		}
	    }
	}

	return children;

    }

    public List<Grid> getPossibleChildren() {
	if (children == null) {
	    children = new ArrayList<Grid>();
	    for (int i = 0; i < 8; i++) {
		for (int j = 0; j < 8; j++) {
		    Piece piece = getPieceAt(i, j);
		    if (piece != null && piece.white == whitePlaysNext) {
			piece.getPossibleMoves(children, this, i, j);
		    }
		}
	    }
	}
	return children;

    }

    public void display() {
	System.out.println();
	for (int y = 0; y < 8; y++) {
	    System.out.print(8 - y + "  ");
	    for (int x = 0; x < 8; x++) {
		Piece piece = getPieceAt(x, y);
		if (piece != null)
		    System.out.print((piece.white ? "w" : "b") + piece.getClass().getSimpleName().substring(0, 2) + " ");
		else
		    System.out.print("    ");
	    }
	    System.out.println();
	}
	System.out.println("   a   b   c   d   e   f   g   h");
	System.out.println(whitePlaysNext);
    }

    @Override
    public String toString() {
	String s = "";

	for (int y = 0; y < 8; y++) {
	    for (int x = 0; x < 8; x++) {
		Piece piece = getPieceAt(x, y);
		if (piece != null)
		    s += (piece.white ? "w" : "b") + piece.getClass().getSimpleName().substring(0, 2);
		else
		    s += "   ";
	    }
	}
	s += " " + whitePlaysNext;
	return s;
    }

    public int getValue(int alpha, int beta, int depth, int depthIncreaseAllowed, boolean keepChildren) {
	if (depth == 0) {
	    gridsValue = evaluateGrid();
	} else {
	    try {
		if (this.arePiecesTheSame(prevGrid.prevGrid) && this.arePiecesTheSame(prevGrid.prevGrid.prevGrid.prevGrid) && prevGrid.arePiecesTheSame(prevGrid.prevGrid.prevGrid)) {
		    return 0;
		}
	    } catch (NullPointerException e) {
	    }

	    getPossibleChildren();

	    if (depthIncreaseAllowed >= 0 && children.size() <= 2) {
		depth += 2;
		depthIncreaseAllowed -= 2;
	    }

	    if (whitePlaysNext) {
//				if (isCheckMated(false))
//				{
//					gridsValue = maxValue;
//					return maxValue;
//				}
		gridsValue = minValue + depth;

		for (int i = 0; i < children.size(); i++) {
		    gridsValue = Math.max(gridsValue, children.get(i).getValue(alpha, beta, depth - 1, depthIncreaseAllowed, false));
		    alpha = Math.max(alpha, gridsValue);
		    CoreChessCom.numberOfCalculatedChildren++;

		    if (alpha > beta)
			break;
		}

		if (children.size() == 0) {
		    if (isChecked(false))
			return maxValue + depth;
		    else if (!isChecked(true))
			return 0 + depth;
		}
	    } else {
//				if (isCheckMated(true))
//				{
//					gridsValue = minValue;
//					return minValue;
//				}
		gridsValue = maxValue + depth;

//				
		for (int i = 0; i < children.size(); i++) {
		    gridsValue = Math.min(gridsValue, children.get(i).getValue(alpha, beta, depth - 1, depthIncreaseAllowed, false));
		    beta = Math.min(beta, gridsValue);
		    CoreChessCom.numberOfCalculatedChildren++;

		    if (alpha > beta)
			break;
		}
		if (children.size() == 0) {
		    if (isChecked(true))
			return minValue - depth;
		    else if (!isChecked(false))
			return 0 - depth;
		}
	    }
	}
	if (!keepChildren)
	    children = null;
	return gridsValue;

    }

    public boolean arePiecesTheSame(Grid compareTo) {
	for (int i = 0; i < pieces.length; i++) {
	    if (compareTo.pieces[i] == null)
		continue;
	    if (!compareTo.pieces[i].equals(pieces[i]))
		return false;
	}
	return true;
    }

    // ******************************* Pieces Values
    // *************************************
    // Queen 9000
    // no mobility

    // Rook 5000
    // mobility bonus + 1 per square

    // Bishop 3500
    // pair + 400
    // -100 per enemy pawn
    // mobility bonus + 3 per square

    // knight 2200
    // outpost: +500
    // + 100 per enemy pawn
    // mobility bonus + 5 per square. squares controlled by enemy pawns are not
    // counted

    // Pawn : default value: 1000
    // backward pawn: - 300
    // doubled pair: -300 per pair
    // passed pawn: +700 -100 * distance
    // no mobility bonus

    // king:
    // bonus for attacking near the king + 150 per square
    // no mobility bonus
    // ******************************* grid evaluation methods below
    // *************************************

    public int evaluateGrid() {
//		if (isStaleMate())
//			return 0;
//		if (isCheckMated(false))
//			return minValue;
//		if (isCheckMated(true))
//			return maxValue;
	int result = 0;

	long whitePawns = 0L;
	long blackPawns = 0L;
	long whiteKnights = 0L;
	long blackKnights = 0L;

	int numOfWhiteBishops = 0;
	int numOfBlackBishops = 0;

	long whiteAttacks = 0l;
	long blackAttacks = 0l;

	for (int i = 0; i < 64; i++) {
	    Piece piece = pieces[i];
	    if (piece != null) {
		if (piece instanceof Pawn) {
		    if (piece.white)
			whitePawns |= (1L << i);
		    else
			blackPawns |= (1L << i);
		} else if (piece instanceof Knight) {
		    if (piece.white)
			whiteKnights |= (1L << i);
		    else
			blackKnights |= (1L << i);
		} else if (piece instanceof Bishop) {
		    if (piece.white)
			numOfWhiteBishops++;
		    else
			numOfBlackBishops++;
		}

		if (piece.white)
		    whiteAttacks |= piece.getAllMovesAsBitBoard(this, i % 8, i / 8);
		else
		    blackAttacks |= piece.getAllMovesAsBitBoard(this, i % 8, i / 8);

	    }
	}

	long whitePawnAttacks = getAttacks(true, true, whitePawns) | getAttacks(true, false, whitePawns);
	long blackPawnAttacks = getAttacks(false, true, blackPawns) | getAttacks(false, false, blackPawns);

	// ******************* mobility *********************
	for (int x = 0; x < 8; x++) {
	    for (int y = 0; y < 8; y++) {
		Piece piece = getPieceAt(x, y);
		if (piece != null) {
		    long moves = piece.getAllMovesAsBitBoard(this, x, y);

		    if (piece instanceof Rook) {
			if (piece.white)
			    result += Long.bitCount(moves) * 1;
			else
			    result -= Long.bitCount(moves) * 1;
		    } else if (piece instanceof Bishop) {
			if (piece.white)
			    result += Long.bitCount(moves) * 3;
			else
			    result -= Long.bitCount(moves) * 3;

		    } else if (piece instanceof Knight) {
			if (piece.white)
			    result += Long.bitCount(moves & ~blackPawnAttacks) * 4;
			else
			    result -= Long.bitCount(moves & ~whitePawnAttacks) * 4;

		    }
		}
	    }
	}

	whiteAttacks |= whitePawnAttacks;
	blackAttacks |= blackPawnAttacks;

	int numOfWhitePawns = Long.bitCount(whitePawns);
	int numOfBlackPawns = Long.bitCount(blackPawns);

	// **************** pawn evaluation *****************

	// backward pawns
	long whiteBackwardPawns = getBackwardPawns(true, whitePawns, blackPawns);
	long blackBackwardPawns = getBackwardPawns(false, whitePawns, blackPawns);

	result -= Long.bitCount(whiteBackwardPawns) * 300;
	result += Long.bitCount(blackBackwardPawns) * 300;

	// doubled pawns

	result -= getNumberOfDoubledPairs(whitePawns) * 300;
	result += getNumberOfDoubledPairs(blackPawns) * 300;

	// passed pawns

	long whitePassedPawns = getPassedPawns(true, whitePawns, blackPawns);
	long blackPassedPawns = getPassedPawns(false, whitePawns, blackPawns);

	for (int i = 0; i < 8; i++) {
	    result -= Long.bitCount((whitePassedPawns >>> i) & RANK1) * i * 100;
	    result += Long.bitCount((blackPassedPawns >>> -i) & RANK8) * i * 100;
	}

	result += Long.bitCount(whitePassedPawns) * 700;
	result -= Long.bitCount(blackPassedPawns) * 700;

	// center pawns
	result += Long.bitCount(whitePawns & CENTER_SQUARES) * 75;
	result -= Long.bitCount(blackPawns & CENTER_SQUARES) * 75;

	// **************** knight outposts *****************

	long whiteOutposts = whiteKnights & whitePawnAttacks & ~blackPawnAttacks & CENTER_SQUARES;
	long blackOutposts = blackKnights & blackPawnAttacks & ~whitePawnAttacks & CENTER_SQUARES;

	result += Long.bitCount(whiteOutposts) * 500;
	result -= Long.bitCount(blackOutposts) * 500;

	// knight pawn bonus
	result += numOfBlackPawns * Long.bitCount(whiteKnights) * 100;
	result -= numOfWhitePawns * Long.bitCount(blackKnights) * 100;

	// ****************** bishop pair *******************

	if (numOfWhiteBishops == 2)
	    result += 400;
	if (numOfBlackBishops == 2)
	    result -= 400;

	// bishop pawn penalty

	result -= numOfBlackPawns * numOfWhiteBishops * 100;
	result += numOfWhitePawns * numOfBlackBishops * 100;

	// **************** attacks near kings **************

	int whiteKingPosX = 0;
	int whiteKingPosY = 0;
	int blackKingPosX = 0;
	int blackKingPosY = 0;

	for (int x = 0; x < 8; x++) {
	    for (int y = 0; y < 8; y++) {
		Piece piece = getPieceAt(x, y);
		if (piece != null && piece instanceof King) {
		    if (piece.white) {
			whiteKingPosX = x;
			whiteKingPosY = y;
		    } else {
			blackKingPosX = x;
			blackKingPosY = y;
		    }
		}
	    }
	}
	// pawn shield
//		long pawnShieldWhite = 
	long squaresNextToWhtiteKing = 0L;// including the kings
	long squaresNextToBlackKing = 0L;

	for (int i = -1; i <= 1; i++) {
	    for (int j = -1; j <= 1; j++) {
		int whiteX = whiteKingPosX + i;
		int whiteY = whiteKingPosY + j;

		if (whiteX >= 0 && whiteX < 8 && whiteY >= 0 && whiteY < 8) {
		    squaresNextToWhtiteKing |= 1L << (whiteY * 8 + whiteX);
		}

		int blackX = blackKingPosX + i;
		int blackY = blackKingPosY + j;

		if (blackX >= 0 && blackX < 8 && blackY >= 0 && blackY < 8) {
		    squaresNextToBlackKing |= 1L << (blackY * 8 + blackX);
		}
	    }
	}

	long attacksNearWhiteKing = squaresNextToWhtiteKing & blackAttacks;
	long attacksNearBlackKing = squaresNextToBlackKing & whiteAttacks;

	result -= Long.bitCount(attacksNearWhiteKing) * 150;
	result += Long.bitCount(attacksNearBlackKing) * 150;

	// ******************** Material ********************

	for (int x = 0; x < 8; x++) {
	    for (int y = 0; y < 8; y++) {

		Piece piece = getPieceAt(x, y);
		if (piece != null) {
		    int value = 0;
//					int tropValue = 0;
		    if (piece instanceof Pawn) {
			value = 1000;
//						tropValue = 150;
		    } else if (piece instanceof Knight) {
			value = 2200;
//						tropValue = 220;
		    } else if (piece instanceof Bishop) {
			value = 3500;
//						tropValue = 220;
		    } else if (piece instanceof Rook) {
			value = 5000;
//						tropValue = 500;
		    } else if (piece instanceof Queen) {
			value = 9000;
//						tropValue = 700;
		    }

		    if (piece.white) {
//						int dx = x - blackKingPosX;
//						int dy = y - blackKingPosY;
//						int distanceFromOtherKing = (int) Math.sqrt(dx * dx + dy * dy);
//						result -= distanceFromOtherKing * tropValue;
			result += value;
		    } else {
//						int dx = x - whiteKingPosX;
//						int dy = y - whiteKingPosY;
//						int distanceFromOtherKing = (int) Math.sqrt(dx * dx + dy * dy);
//						result += distanceFromOtherKing * tropValue;
			result -= value;
		    }
		}
	    }
	}
	return result;
    }

    public static long getPassedPawns(boolean white, long whitePawns, long blackPawns) {
	long oppositePawns = white ? blackPawns : whitePawns;
	long pawns = white ? whitePawns : blackPawns;
	long frontAndAttackSpans = getAttacks(!white, true, oppositePawns) | getAttacks(!white, false, oppositePawns) | oppositePawns;

	frontAndAttackSpans = fill(frontAndAttackSpans, white);

	return pawns & ~frontAndAttackSpans;
    }

    public static void displayBitBoard(long bitboard) {

	for (int i = 0; i < 64; i++) {

	    System.out.print(((bitboard >> i) & 1L) + " ");
	    if (i % 8 == 7)
		System.out.println();
	}
	System.out.println("=========================");
    }

    public static int getNumberOfDoubledPairs(long pawns) {
	int result = 0;
	for (int i = 0; i < 8; i++) {
	    int n = Long.bitCount(pawns & (AFile >>> i));
	    result += n * (n - 1) / 2;
	}
	return result;
    }

    public static long getBackwardPawns(boolean white, long whitePawns, long blackPawns) {
	long checkingFor = white ? whitePawns : blackPawns;
	long opposite = !white ? whitePawns : blackPawns;

	long frontAttackSpan = Grid.getFrontAttackSpan(white, checkingFor);
	long oppositeAttacks = Grid.getAttacks(!white, true, opposite) | Grid.getAttacks(!white, false, opposite);

	long stopsDominatedByOpposite = ~frontAttackSpan & oppositeAttacks;
	long filledDown = Grid.fill(stopsDominatedByOpposite, white);

	return (filledDown & checkingFor);
    }

    public static long getFrontAttackSpan(boolean white, long pawns) {
	long result = 0;
	int direction = white ? -8 : 8;

	for (int i = 0; i < 64; i++) {
	    if (((pawns >> i) & 1) == 1) {
		int x = i % 8;

		if (x != 0)// not on the left
		{
		    for (int j = i + direction; j < 64 && j >= 0; j += direction) {
			result |= (1L << j - 1);
		    }
		}
		if (x != 7)// not on the right
		{
		    for (int j = i + direction; j < 64 && j >= 0; j += direction) {
			result |= (1L << j + 1);
		    }
		}

	    }
	}
	return result;
    }

    public static long getAttacks(boolean white, boolean east, long pawns) {
	long result = 0;

	if (white) {
	    if (east)
		result = pawns >> 7;
	    else
		result = pawns >> 9;
	} else {
	    if (east)
		result = pawns << 9;
	    else
		result = pawns << 7;
	}
	if (!east)
	    result &= ~AFile;
	else
	    result &= ~HFile;

	return result;
    }

    public static long fill(long bitBoard, boolean down) {
	if (down) {
	    for (int i = 0; i < 8; i++) {
		bitBoard |= bitBoard << 8;
	    }
	} else {
	    for (int i = 0; i < 8; i++) {
		bitBoard |= bitBoard >> 8;
	    }
	}
	return bitBoard;
    }

    public static long safePawnSquares(long whitePawns, long blackPawns) {
	long wPawnEastAttacks = getAttacks(true, true, whitePawns);
	long wPawnWestAttacks = getAttacks(true, false, whitePawns);
	long bPawnEastAttacks = getAttacks(false, true, blackPawns);
	long bPawnWestAttacks = getAttacks(false, false, blackPawns);

	long wPawnDblAttacks = wPawnEastAttacks & wPawnWestAttacks;
	long wPawnOddAttacks = wPawnEastAttacks ^ wPawnWestAttacks;
	long bPawnDblAttacks = bPawnEastAttacks & bPawnWestAttacks;
	long bPawnAnyAttacks = bPawnEastAttacks | bPawnWestAttacks;

	return wPawnDblAttacks | ~bPawnAnyAttacks | (wPawnOddAttacks & ~bPawnDblAttacks);
    }
}
