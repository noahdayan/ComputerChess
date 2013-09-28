/**
 * 
 * @author Noah Dayan
 * 
 * Chess Solver
 *
 */

import java.util.Scanner;

public class Chess
{
	public static final int SIZE = 64;
	public static final int N = 8;
	
	Pieces[][] Grid;
	int[][] Matrix;
	static int winner = 0;
	static int check = 0;
	static boolean playerTurn = true;
	
	int rowWhiteKing;
	int columnWhiteKing;
	int rowBlackKing;
	int columnBlackKing;
	
	public enum Pieces
	{
		Empty, WhitePawn, BlackPawn, WhiteBishop, BlackBishop, WhiteKnight, BlackKnight, WhiteRook, BlackRook, WhiteQueen, BlackQueen, WhiteKing, BlackKing;
		
		public int getNumber()
		{
			int num;
			switch(this)
			{
			case WhitePawn:
				num = 1;
				break;
			case BlackPawn:
				num = -1;
				break;
			case WhiteBishop:
				num = 2;
				break;
			case BlackBishop:
				num = -2;
				break;
			case WhiteKnight:
				num = 3;
				break;
			case BlackKnight:
				num = -3;
				break;
			case WhiteRook:
				num = 4;
				break;
			case BlackRook:
				num = -4;
				break;
			case WhiteQueen:
				num = 5;
				break;
			case BlackQueen:
				num = -5;
				break;
			case WhiteKing:
				num = 6;
				break;
			case BlackKing:
				num = -6;
				break;
			default:
				num = 0;
			}
			return num;
		}
		
		public int getTeam()
		{
			if(this.getNumber() > 0)
			{
				return 1;
			}
			else if(this.getNumber() < 0)
			{
				return -1;
			}
			else
			{
				return 0;
			}
		}
	}
	
	public Chess()
	{
		Grid = new Pieces[N][N];
		Matrix = new int[N][N];
        for(int i = 0; i < N; i++)
        {
            for(int j = 0; j < N; j++)
            {	
            	if(i == 1)
            	{
            		Matrix[i][j] = 2;
            		Grid[i][j] = Pieces.WhitePawn;
            	}
            	else if(i == 6)
            	{
            		Matrix[i][j] = 2;
            		Grid[i][j] = Pieces.BlackPawn;
            	}
            	else if(i == 2)
            	{
            		Matrix[i][j] = 1;
            		Grid[i][j] = Pieces.Empty;
            	}
            	else if(i == 5)
            	{
            		Matrix[i][j] = -1;
            		Grid[i][j] = Pieces.Empty;
            	}
            	else if(i == 0)
            	{
            		if(j == 0 || j == 7)
            		{
            			Matrix[i][j] = 2;
            			Grid[i][j] = Pieces.WhiteRook;
            		}
            		else if(j == 1 || j == 6)
            		{
            			Matrix[i][j] = 2;
            			Grid[i][j] = Pieces.WhiteKnight;
            		}
            		else if(j == 2 || j == 5)
            		{
            			Matrix[i][j] = 2;
            			Grid[i][j] = Pieces.WhiteBishop;
            		}
            	}
            	else if(i == 7)
            	{
            		if(j == 0 || j == 7)
            		{
            			Matrix[i][j] = 2;
            			Grid[i][j] = Pieces.BlackRook;
            		}
            		else if(j == 1 || j == 6)
            		{
            			Matrix[i][j] = 2;
            			Grid[i][j] = Pieces.BlackKnight;
            		}
            		else if(j == 2 || j == 5)
            		{
            			Matrix[i][j] = 2;
            			Grid[i][j] = Pieces.BlackBishop;
            		}
            	}
            	else
            	{
            		Matrix[i][j] = 0;
            		Grid[i][j] = Pieces.Empty;
            	}
            }
        }
        Grid[0][3] = Pieces.WhiteQueen;
        Grid[0][4] = Pieces.WhiteKing;
        Grid[7][3] = Pieces.BlackQueen;
        Grid[7][4] = Pieces.BlackKing;
        rowWhiteKing = 0;
        columnWhiteKing = 4;
        rowBlackKing = 7;
        columnBlackKing = 4;
	}

	public void move()
	{
		Scanner input = new Scanner(System.in);
		boolean illegalMove = true;
		int fromRow = 0;
		int fromColumn = 0;
		int toRow = 0;
		int toColumn = 0;
		
		while(illegalMove)
		{
			illegalMove = false;
			
			System.out.println("Your Move: ");
			System.out.print("From: ");
			String from = input.next();
			System.out.print("To: ");
			String to = input.next();
			System.out.println();
			
			if(from.length() != 2 || to.length() != 2)
			{
				illegalMove = true;
				System.out.println("Illegal Input!\n");
				continue;
			}
			
			fromRow = (from.charAt(1) - 49);
			fromColumn = (from.charAt(0) - 97);
			toRow = (to.charAt(1) - 49);
			toColumn = (to.charAt(0) - 97);
			
			if(!checkMove(fromRow, fromColumn, toRow, toColumn))
			{
				illegalMove = true;
				System.out.println("Illegal Move!\n");
			}
			
			checkCheck();
			
			if(check == 1 || check == 2)
			{
				illegalMove = true;
				System.out.println("Illegal Check!\n");
			}
		}
		
		Pieces fromTemp = Grid[fromRow][fromColumn];
		Pieces toTemp = Grid[toRow][toColumn];
		Grid[toRow][toColumn] = Grid[fromRow][fromColumn];
		Grid[fromRow][fromColumn] = Pieces.Empty;
		
		if(fromTemp.equals(Pieces.WhiteKing))
		{
			rowWhiteKing = toRow;
			columnWhiteKing = toColumn;
		}
		
		System.out.println("Moving " + fromTemp + ".\n");
		
		if(!toTemp.equals(Pieces.Empty))
		{
			System.out.println(toTemp + " has been eaten.\n");
		}
	}
	
	private boolean checkMove(int fromRow, int fromColumn, int toRow, int toColumn)
	{
		boolean legalMove = true;
		
		if(fromRow < 0 || fromRow > 7 || fromColumn < 0 || fromColumn > 7 || toRow < 0 || toRow > 7 || toColumn < 0 || toColumn > 7)
		{
			legalMove =  false;
		}
		else if(Grid[fromRow][fromColumn].getTeam() != 1)
		{
			legalMove =  false;
		}
		else if(Grid[toRow][toColumn].getTeam() == 1)
		{
			legalMove = false;
		}
		else if((fromRow == toRow) && (fromColumn == toColumn))
		{
			legalMove = false;
		}
		else
		{
			switch(Grid[fromRow][fromColumn])
			{
			case WhitePawn:
				legalMove = checkPawnMove(fromRow, fromColumn, toRow, toColumn);
				break;
			case BlackPawn:
				legalMove = false;
				break;
			case WhiteBishop:
				legalMove = checkBishopMove(fromRow, fromColumn, toRow, toColumn);
				break;
			case BlackBishop:
				legalMove = false;
				break;
			case WhiteKnight:
				legalMove = checkKnightMove(fromRow, fromColumn, toRow, toColumn);
				break;
			case BlackKnight:
				legalMove = false;
				break;
			case WhiteRook:
				legalMove = checkRookMove(fromRow, fromColumn, toRow, toColumn);
				break;
			case BlackRook:
				legalMove = false;
				break;
			case WhiteQueen:
				legalMove = checkQueenMove(fromRow, fromColumn, toRow, toColumn);
				break;
			case BlackQueen:
				legalMove = false;
				break;
			case WhiteKing:
				legalMove = checkKingMove(fromRow, fromColumn, toRow, toColumn);
				break;
			case BlackKing:
				legalMove = false;
				break;
			default:
				legalMove = false;
			}
		}
		
		return legalMove;
	}
	
	//TODO En passant and transformation
	private boolean checkPawnMove(int fromRow, int fromColumn, int toRow, int toColumn)
	{
		boolean legalMove = true;
		
		//Eating move
		if(Math.abs(fromColumn - toColumn) == 1 && (toRow - fromRow) == 1 && Grid[toRow][toColumn].getTeam() == -1)
		{
			return true;
		}
		
		//Straight move and Double move
		if(!((fromColumn == toColumn) && ((toRow - fromRow) == 1 || (fromRow == 1 && (toRow - fromRow) == 2 && Grid[2][fromColumn].equals(Pieces.Empty)))))
		{
			legalMove = false;
		}
		else if(!Grid[toRow][toColumn].equals(Pieces.Empty))
		{
			legalMove = false;
		}
		
		return legalMove;
	}
	
	private boolean checkBishopMove(int fromRow, int fromColumn, int toRow, int toColumn)
	{
		boolean legalMove = false;

		//Diagonal move
		if(Math.abs(toRow - fromRow) == Math.abs(toColumn - fromColumn))
		{
			if(toRow > fromRow && toColumn > fromColumn)
			{
				for(int i = fromRow + 1; i < toRow; i++)
				{
					for(int j = fromColumn + 1; j < toColumn; j++)
					{
						if(Math.abs(toRow - i) == Math.abs(toColumn - j) && !Grid[i][j].equals(Pieces.Empty))
						{
							return false;
						}
					}
				}
			}
			else if(toRow > fromRow && toColumn < fromColumn)
			{
				for(int i = fromRow + 1; i < toRow; i++)
				{
					for(int j = fromColumn - 1; j > toColumn; j--)
					{
						if(Math.abs(toRow - i) == Math.abs(toColumn - j) && !Grid[i][j].equals(Pieces.Empty))
						{
							return false;
						}
					}
				}
			}
			else if(toRow < fromRow && toColumn > fromColumn)
			{
				for(int i = fromRow - 1; i > toRow; i--)
				{
					for(int j = fromColumn + 1; j < toColumn; j++)
					{
						if(Math.abs(toRow - i) == Math.abs(toColumn - j) && !Grid[i][j].equals(Pieces.Empty))
						{
							return false;
						}
					}
				}
			}
			else if(toRow < fromRow && toColumn < fromColumn)
			{
				for(int i = fromRow - 1; i > toRow; i--)
				{
					for(int j = fromColumn - 1; j > toColumn; j--)
					{
						if(Math.abs(toRow - i) == Math.abs(toColumn - j) && !Grid[i][j].equals(Pieces.Empty))
						{
							return false;
						}
					}
				}
			}
			legalMove = true;
		}
		
		return legalMove;
	}
	
	private boolean checkKnightMove(int fromRow, int fromColumn, int toRow, int toColumn)
	{
		//Double plus Single omnidirectional move
		if(Math.abs(toRow - fromRow) + Math.abs(toColumn - fromColumn) == 3 && Math.abs(toRow - fromRow) <= 2 && Math.abs(toColumn - fromColumn) <= 2)
		{
			return true;
		}
		return false;
	}
	
	private boolean checkRookMove(int fromRow, int fromColumn, int toRow, int toColumn)
	{
		boolean legalMove = false;

		//Horizontal move
		if(fromRow == toRow)
		{
			if(toColumn > fromColumn)
			{
				for(int i = fromColumn + 1; i < toColumn; i++)
				{
					if(!Grid[fromRow][i].equals(Pieces.Empty))
					{
						return false;
					}
				}
			}
			else
			{
				for(int i = fromColumn - 1; i > toColumn; i--)
				{
					if(!Grid[fromRow][i].equals(Pieces.Empty))
					{
						return false;
					}
				}
			}
			legalMove = true;
		}
		//Vertical move
		else if(fromColumn == toColumn)
		{
			if(toRow > fromRow)
			{
				for(int i = fromRow + 1; i < toRow; i++)
				{
					if(!Grid[i][fromColumn].equals(Pieces.Empty))
					{
						return false;
					}
				}
			}
			else
			{
				for(int i = fromRow - 1; i > toRow; i--)
				{
					if(!Grid[i][fromColumn].equals(Pieces.Empty))
					{
						return false;
					}
				}
			}
			legalMove = true;
		}

		return legalMove;
	}
	
	private boolean checkQueenMove(int fromRow, int fromColumn, int toRow, int toColumn)
	{
		//Check bishop or rook move
		if(checkBishopMove(fromRow, fromColumn, toRow, toColumn) || checkRookMove(fromRow, fromColumn, toRow, toColumn))
		{
			return true;
		}
		return false;
	}
	
	//TODO Castle
	private boolean checkKingMove(int fromRow, int fromColumn, int toRow, int toColumn)
	{
		//Single omnidirectional move
		if(Math.abs(fromRow - toRow) <= 1 && Math.abs(fromColumn - toColumn) <= 1)
		{
			return true;
		}
		return false;
	}
	
	public void checkCheck()
	{
		if((Matrix[rowWhiteKing][columnWhiteKing] == 1 || Matrix[rowWhiteKing][columnWhiteKing] == 2) && (Matrix[rowBlackKing][columnBlackKing] == -1 || Matrix[rowBlackKing][columnBlackKing] == 2))
		{
			check = 2;
		}
		else if(Matrix[rowWhiteKing][columnWhiteKing] == 1 || Matrix[rowWhiteKing][columnWhiteKing] == 2)
		{
			check = 1;
		}
		else if(Matrix[rowBlackKing][columnBlackKing] == -1 || Matrix[rowBlackKing][columnBlackKing] == 2)
		{
			check = -1;
		}
		else
		{
			check = 0;
		}
		
		if(check == 1 || check == 2)
		{
			winner = -1;
			if(Matrix[rowWhiteKing + 1][columnWhiteKing] == 0 || Matrix[rowWhiteKing][columnWhiteKing + 1] == 0 || Matrix[rowWhiteKing - 1][columnWhiteKing] == 0 || Matrix[rowWhiteKing][columnWhiteKing - 1] == 0 || Matrix[rowWhiteKing + 1][columnWhiteKing + 1] == 0 || Matrix[rowWhiteKing - 1][columnWhiteKing - 1] == 0 || Matrix[rowWhiteKing + 1][columnWhiteKing - 1] == 0 || Matrix[rowWhiteKing - 1][columnWhiteKing + 1] == 0)
			{
				winner = 0;
			}
		}
		if(check == -1 || check == 2)
		{
			winner = 1;
			if(Matrix[rowBlackKing + 1][columnBlackKing] == 0 || Matrix[rowBlackKing][columnBlackKing + 1] == 0 || Matrix[rowBlackKing - 1][columnBlackKing] == 0 || Matrix[rowBlackKing][columnBlackKing - 1] == 0 || Matrix[rowBlackKing + 1][columnBlackKing + 1] == 0 || Matrix[rowBlackKing - 1][columnBlackKing - 1] == 0 || Matrix[rowBlackKing + 1][columnBlackKing - 1] == 0 || Matrix[rowBlackKing - 1][columnBlackKing + 1] == 0)
			{
				winner = 0;
			}
		}
	}
	
	public void solve()
	{
		System.out.println("Computer's Move: ");
		System.out.println("From: ");
		System.out.println("To: ");
		System.out.println();
	}
	
	public void print()
	{
        for(int i = 0; i < N; i++)
        {
            for(int j = 0; j < N; j++)
            {
            	System.out.print(Grid[(N - 1) - i][j].getNumber() + " ");
            }
            System.out.println();
        }
        System.out.println();
	}
	
	public static void main(String[] args)
	{
		Chess c = new Chess();
		
		System.out.println("New Game\n");
		
		c.print();
		
		while(winner == 0)
		{
			if(playerTurn)
			{
				c.move();
			}
			else
			{
				c.solve();
			}
			c.print();
			c.checkCheck();
			playerTurn = !playerTurn;
		}
	}

}
