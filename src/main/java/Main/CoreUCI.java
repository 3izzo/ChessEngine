package Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Pieces.Bishop;
import Pieces.King;
import Pieces.Knight;
import Pieces.Pawn;
import Pieces.Piece;
import Pieces.Queen;
import Pieces.Rook;

public class CoreUCI {

    private Grid grid;
    private PrintWriter printWriter;

    public static void main(String[] args) throws IOException {

	CoreUCI core = new CoreUCI();
	core.run();

    }

    private void run() throws IOException {
	Scanner sc = new Scanner(System.in);
//	PrintWriter pw = new PrintWriter(new FileWriter("C:\\Users\\3izzo\\Desktop\\log.txt"));
	FileWriter fileWriter = new FileWriter("C:\\Users\\3izzo\\Desktop\\log.dev");
	printWriter = new PrintWriter(fileWriter);
	try {
	    while (true) {
		mainLoop(sc, printWriter);
	    }
	} catch (Exception e) {
	    print(e.toString() + "\n");
	    for (StackTraceElement stackTraceElement : e.getStackTrace()) {
		print(stackTraceElement.toString() + "\n");
	    }
	}
    }

    private void mainLoop(Scanner sc, PrintWriter pw) {

	String nextLine = sc.nextLine();
	pw.printf("input:%s\n", nextLine);
	pw.flush();
	if (nextLine.equalsIgnoreCase("uci")) {
	    print("id name obadahFish\n");
	    print("id author Obadah\n");
	    print("uciok\n");
	} else if (nextLine.startsWith("isready"))
	    print("readyok\n");
	else if (nextLine.startsWith("position")) {
	    if (nextLine.contains("fen")) {
		grid = createGridFromFEN(nextLine.substring(nextLine.indexOf("fen") + 3).replace(" ", ""));
	    } else if (nextLine.contains("startpos")) {
		grid = new Grid(true);
	    }
	    if (nextLine.contains("moves")) {
		nextLine = nextLine.substring(nextLine.indexOf("moves") + 6);
		String[] split = nextLine.split(" ");
		for (String string : split) {
		    playPlayer(string);
		}
	    }
	} else if (nextLine.startsWith("go"))
	    playAiManual(4);
	else if (nextLine.equalsIgnoreCase("d"))
	    grid.display();
//	grid.display();
//	playPlayer(sc);
//	grid.display();

    }

    private void playAiManual(int depth) {
	long timeStart = System.currentTimeMillis();
	int best = grid.getValue(Grid.minValue - 6, Grid.maxValue + 6, depth, 2, true);
//	print("time: " + (System.currentTimeMillis() - timeStart) / 1000.0 + "s");

	List<Grid> bestGrids = new ArrayList<Grid>();
//	System.out.print(" best: " + best);
//	System.out.println(" current grid: " + grid.evaluateGrid());
	for (int i = 0; i < grid.children.size(); i++) {

	    Grid child = grid.children.get(i);
	    int childV = child.gridsValue;

	    if (childV == best)
		bestGrids.add(child);
	}
//		grid.children = null;

	if (bestGrids.isEmpty()) {
	    grid = new Grid(true);
	} else {
	    Grid gridToPlay = bestGrids.get((int) (Math.random() * bestGrids.size()));
	    playGrid(gridToPlay);
	    grid = gridToPlay;
	    grid.children = null;
	    grid.gridsValue = null;
//			grid.display();
	}

    }

    private void playGrid(Grid toPlay) {
	int fromX = 0, fromY = 0, toX = 0, toY = 0;
	Class<? extends Piece> typeOF = null;
	char promotedTo = ' ';
	for (int y = 0; y < 8; y++) {
	    for (int x = 0; x < 8; x++) {
		Piece pieceAt = toPlay.prevGrid.getPieceAt(x, y);
		if (pieceAt != null && toPlay.getPieceAt(x, y) == null) {
		    fromX = x;
		    fromY = y;
		    typeOF = pieceAt.getClass();

		    break;
		}
	    }
	}
	for (int y = 0; y < 8; y++) {
	    for (int x = 0; x < 8; x++) {
		Piece pieceAt = toPlay.getPieceAt(x, y);
		if (!(x == fromX && y == fromY) && toPlay.prevGrid.getPieceAt(x, y) != pieceAt) {
		    if (typeOF == King.class && pieceAt.getClass() != King.class)
			continue;
		    toX = x;
		    toY = y;
		    if (typeOF == Pawn.class && (toY == 7 || toY == 0)) {
			switch (pieceAt.getClass().getSimpleName()) {
			case "Knight":
			    promotedTo = 'n';
			    break;
			case "Queen":
			    promotedTo = 'q';
			    break;
			case "Bishop":
			    promotedTo = 'b';
			    break;
			case "Rook":
			    promotedTo = 'r';
			    break;
			}
		    }
		    break;
		}
	    }
	}
	print("bestmove " + (char) ('a' + fromX) + "" + (8 - fromY) + "" + (char) ('a' + toX) + "" + (8 - toY) + "" + promotedTo + "\n");
    }

    private void playPlayer(String in) {

//	    System.out.print("Move piece from: to:");
	int fromX = in.charAt(0) - 'a';
	int fromY = 8 - Integer.parseInt(in.charAt(1) + "");
	int toX = in.charAt(2) - 'a';
	int toY = 8 - Integer.parseInt(in.charAt(3) + "");

	Grid possibleChild = new Grid(grid, fromX, fromY, toX, toY);
//	possibleChild.display();
	if (grid.getPieceAt(fromX, fromY) instanceof Pawn && (toY == 0 || toY == 7)) {
//		System.out.println("Promote to: ");
	    char promoteTo = in.charAt(4);
	    Piece p = null;
	    switch (promoteTo) {
	    case 'q':
		p = new Queen(grid.whitePlaysNext);
		break;
	    case 'n':
		p = new Knight(grid.whitePlaysNext);
		break;
	    case 'b':
		p = new Bishop(grid.whitePlaysNext);
		break;
	    case 'r':
		p = new Rook(grid.whitePlaysNext);
		break;
	    }
	    possibleChild.setPieceTo(toX, toY, p);
	}

	if (grid.getPieceAt(fromX, fromY) instanceof King) {
	    int abs = Math.abs(fromX - toX);
	    System.out.println(abs);
	    if (abs == 2) {
		boolean shortCastle = fromX - toX < 0;
		System.out.println(shortCastle);
		if (shortCastle) {
		    possibleChild.setPieceTo(5, toY, grid.getPieceAt(7, toY));
		    possibleChild.setPieceTo(7, toY, null);
		} else {
		    possibleChild.setPieceTo(3, toY, grid.getPieceAt(0, toY));
		    possibleChild.setPieceTo(0, toY, null);
		}
	    }
	}

	List<Grid> possibleChildren = grid.getPossibleChildren();

	for (Grid childGrid : possibleChildren) {
	    if (childGrid.arePiecesTheSame(possibleChild)) {
		grid.children = null;
		grid = possibleChild;
		return;
	    }
	}
    }

    private Grid createGridFromFEN(String FEN) {
	String[] split = FEN.split(" ");
	boolean whitePlaysNext = split[1].startsWith("w");
	Grid g = new Grid(whitePlaysNext);

	for (int i = 0; i < 8; i++) {
	    for (int j = 0; j < 8; j++) {
		g.setPieceTo(i, j, null);
	    }
	}

	String[] ranks = split[0].split("/");
	for (int i = 0; i < ranks.length; i++) {
	    String rank = ranks[i];
	    int offset = 0;
	    for (int j = 0; j < rank.length(); j++) {
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
		else {
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

    private void print(String s) {
	System.out.print(s);
	printWriter.print(s);
	printWriter.flush();
    }
}
