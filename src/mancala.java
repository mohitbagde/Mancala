import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;



class Node
{
	String label = null;
	int stones = 0;
}
class Game
{
	int depth = 0;
	int value = 0;
	int player;
	int maxdepth = 0;
	String label = "";
	int alpha = Integer.MIN_VALUE;
	int beta = Integer.MAX_VALUE;
	boolean playagain = false;
	Node [][] board;
	Game parentstate;
	}
public class mancala {
	
	public static int maxvalue = Integer.MIN_VALUE;
	public static int minvalue = Integer.MAX_VALUE;
	public static String chosenmove = null;
	public static Game nextState = new Game();
	public static ArrayList<Game> list = new ArrayList<Game>();
	public static String traverse = "";
	public static String ns = "";
	public static void displayBoard(int[] p1, int[] p2)
	{
		System.out.print("P2's board : ");
		for(int i = 0; i < p2.length; i++)
			System.out.print(p2[i]+" ");
		System.out.println();
		System.out.print("P1's board : ");
		for(int i = 0; i < p1.length; i++)
			System.out.print(p1[i]+" ");
		System.out.println();
	}
	public static void displayMancala(Node[][] game)
	{
		for(int i = 0; i < game.length; i++)
		{
			for(int j = 0; j < game[i].length; j++)
			{	
				//System.out.print(game[i][j].label+" ");
				System.out.print(game[i][j].stones+" ");
			}
			System.out.println();
		}
	}
	public static void displayNextState(Node[][] game) throws Exception
	{
		for(int i = 0; i < game.length; i++)
		{
			for(int j = 0; j < game[i].length; j++)
			{
				if(j==0) continue;
				if(j == game[i].length-1) continue;
				ns+=game[i][j].stones+" ";
			}
			ns+="\n";
		}
		ns+=game[0][0].stones+"\n";
		ns+=game[1][game[1].length-1].stones+"\n";
	}
	public static Game createGameCopy(Game mancala)
	{
		Node [][]game = mancala.board;
		int value = mancala.value;
		int depth = mancala.depth;
		boolean playagain = mancala.playagain;
		int player = mancala.player;
		String label  =mancala.label;
		int maxdepth = mancala.maxdepth;
		int alpha = mancala.alpha ;
		int beta = mancala.beta;
		Game nextstate = mancala.parentstate;
		Node [][]tempgame = new Node[game.length][game[1].length];
		for(int i = 0; i < game.length; i++)
		{
			for(int j = 0; j < game[1].length; j++)
				{
					Node oldtemp = new Node();
					
					oldtemp.label = game[i][j].label;
					oldtemp.stones = game[i][j].stones;
					
					tempgame[i][j] = oldtemp;
				}
		}
		Game temp = new Game();
		temp.board = tempgame;
		temp.value = value;
		temp.depth = depth;
		temp.playagain = playagain;
		temp.label = label;
		temp.player = player;
		temp.maxdepth = maxdepth;
		temp.alpha = alpha;
		temp.beta = beta;
		temp.parentstate = nextstate;
		return temp;
	}
	public static int max(Game state) throws Exception
	{
		//terminal state checking
		Game temp = createGameCopy(state);
		if(isGameOver(state))
		{
			System.out.println("max game over"+state.label);
			//displayMancala(state.board);
		//	traverse+= "gameover"+"\n";
			moveCoinsToOppMancala(state);
			traverseMaxChild(state);
			//displayMancala(state.board);
			return evaluateMax(state);
		}
		
		if(state.depth == state.maxdepth && state.playagain == false)
		{
			System.out.println("max cut off"+state.label);
			displayMancala(state.board);
			traverseMaxChild(state);
			return evaluateMax(state);
		}
		
		for(int i = 1; i < state.board[1].length -1; i++)
		{	
			int pit;
			if(state.player == 1) 	pit = state.board[1][i].stones;
			else 				pit = state.board[0][i].stones;
			if(pit == 0)				continue;
			
			traverseLog(state);
			
			//System.out.println("max state ="+state.label);
			//displayMancala(state.board);
			int isbonusmove = 0;
			if(state.playagain == true)
			{
				isbonusmove = 1;
				state.playagain = false;
			}
			Game childstate = getChild(state, i);
			int realstatevalue = state.value;
			state = createGameCopy(temp);
			state.value = realstatevalue;
			childstate.parentstate = state;
			
			
			if(isbonusmove == 1)
				childstate.depth = state.depth;
	
			if(childstate.playagain == true)
			{
				childstate.value = Integer.MIN_VALUE;
				state.value = getMax(state.value, max(childstate));
				
			}
			//normal move
			else
			{
				childstate.value = Integer.MAX_VALUE;
				childstate.player = (childstate.player+1)%2;
				state.value = getMax(state.value, min(childstate));
			}
			
		}
		
		traverseLog(state);
		//System.out.println("leaving max = "+state.label);
		//displayMancala(state.board);

		return state.value;
	}
	public static void traverseLog(Game state) throws Exception
	{
		String value = null;	
		if(state.value == Integer.MAX_VALUE) value = "Infinity";
		else if(state.value == Integer.MIN_VALUE) value = "-Infinity";
		else	value = new String(""+state.value);
		System.out.println(state.label+","+state.depth+","+value);
		traverse += state.label+","+state.depth+","+value+"\n";
	}
	public static void traverseLogAB(Game state) throws Exception
	{
		String value = null;
		String alpha = null;
		String beta = null;
		if(state.value == Integer.MAX_VALUE) value = "Infinity";
		else if(state.value == Integer.MIN_VALUE) value = "-Infinity";
		else	value = new String(""+state.value);
		
		if(state.alpha == Integer.MAX_VALUE) alpha = "Infinity";
		else if(state.alpha == Integer.MIN_VALUE) alpha = "-Infinity";
		else	alpha = ""+state.alpha;
		
		if(state.beta == Integer.MAX_VALUE) beta = "Infinity";
		else if(state.beta == Integer.MIN_VALUE) beta = "-Infinity";
		else	beta = ""+state.beta;
		System.out.println(state.label+","+state.depth+","+value+","+alpha+","+beta);
		traverse +=state.label+","+state.depth+","+value+","+alpha+","+beta+"\n";
	}
	public static void traverseMaxChild(Game state) throws Exception
	{
		int eval = evaluateMax(state);	
		String value = null;
		if(eval == Integer.MAX_VALUE) value = "Infinity";
		else if(eval == Integer.MIN_VALUE) value = "-Infinity";
		else	value = new String(""+eval);
		System.out.println(state.label+","+state.depth+","+value);
		traverse += state.label+","+state.depth+","+value+"\n";
	}
	public static void traverseMaxChildAB(Game state) throws Exception
	{
		int eval = evaluateMax(state);
		String value = null;
		String alpha = null;
		String beta = null;
		if(eval == Integer.MAX_VALUE) value = "Infinity";
		else if(eval == Integer.MIN_VALUE) value = "-Infinity";
		else	value = ""+eval;
		
		if(state.alpha == Integer.MAX_VALUE) alpha = "Infinity";
		else if(state.alpha == Integer.MIN_VALUE) alpha = "-Infinity";
		else	alpha =""+state.alpha;
		
		if(state.beta == Integer.MAX_VALUE) beta = "Infinity";
		else if(state.beta == Integer.MIN_VALUE) beta = "-Infinity";
		else	beta = ""+state.beta;
		System.out.println(state.label+","+state.depth+","+value+","+alpha+","+beta);
		traverse+=state.label+","+state.depth+","+value+","+alpha+","+beta+"\n";
	}
	public static void traverseMinChild(Game state) throws Exception
	{
		int eval = evaluateMin(state);	
		String value = null;
		
		if(eval == Integer.MAX_VALUE) value = "Infinity";
		else if(eval == Integer.MIN_VALUE) value = "-Infinity";
		else	value = new String(""+eval);
		System.out.println(state.label+","+state.depth+","+value);
		traverse+=state.label+","+state.depth+","+value+"\n";
	}
	public static void traverseMinChildAB(Game state) throws Exception
	{
		int eval = evaluateMin(state);
		String value = null;
		String alpha = null;
		String beta = null;
		if(eval == Integer.MAX_VALUE) value = "Infinity";
		else if(eval == Integer.MIN_VALUE) value = "-Infinity";
		else	value = ""+eval;
		
		if(state.alpha == Integer.MAX_VALUE) alpha = "Infinity";
		else if(state.alpha == Integer.MIN_VALUE) alpha = "-Infinity";
		else	alpha =""+state.alpha;
		
		if(state.beta == Integer.MAX_VALUE) beta = "Infinity";
		else if(state.beta == Integer.MIN_VALUE) beta = "-Infinity";
		else	beta = ""+state.beta;
		System.out.println(state.label+","+state.depth+","+value+","+alpha+","+beta);
		traverse+=state.label+","+state.depth+","+value+","+alpha+","+beta+"\n";
	}
	public static int min(Game state) throws Exception
	{
		Game temp = createGameCopy(state);
		if(isGameOver(state))
		{
		System.out.println("min game over"+state.label);
		displayMancala(state.board);
			moveCoinsToOppMancala(state);
		//	traverse+= "min gameover"+"\n";
			traverseMinChild(state);
		//	displayMancala(state.board);
			return evaluateMin(state);
		}
		
		if(state.depth == state.maxdepth && state.playagain == false)
		{
			System.out.println("min cut off");
			displayMancala(state.board);
			traverseMinChild(state);
			return evaluateMin(state);
		}
		
		for(int i = 1; i < state.board[1].length -1; i++)
		{	
			int pit;
			if(state.player == 1)	pit = state.board[1][i].stones;
			else				pit = state.board[0][i].stones;
			if(pit == 0)
				continue;
			
			traverseLog(state);
			
			//System.out.println("min state"+state.label);
			//displayMancala(state.board);
			int isbonusmove = 0;
			if(state.playagain == true)
			{
				isbonusmove = 1;
				state.playagain = false;
			}
			
			Game childstate = getChild(state, i);
			
			int realstatevalue = state.value;
			state = createGameCopy(temp);
			state.value = realstatevalue;
			
			
			
			if(isbonusmove == 1)
				childstate.depth = state.depth;
			
			if(state.depth == 1)
			{
				if(state.value != Integer.MAX_VALUE) 
				{
				if(!list.contains(state))
					list.add(state);
				}
			}
			
			//bonus move
			if(childstate.playagain == true)
			{
				childstate.value = Integer.MAX_VALUE;
				state.value = getMin(state.value, min(childstate));
			}
			//normal move
			else
			{
				childstate.value = Integer.MIN_VALUE;
				childstate.player = (childstate.player+1)%2;
				state.value = getMin(state.value, max(childstate));
			}
		}
		traverseLog(state);
	//	System.out.println("leaving min state"+state.label);
	//displayMancala(state.board);
	//	System.out.println("---Leaving Min---");
		return state.value;
	}
	public static int maxAB(Game state) throws Exception
	{
		//terminal state checking
		
		if(isGameOver(state))
		{
			moveCoinsToOppMancala(state);
			traverseMaxChildAB(state);
			return evaluateMax(state);
		}
		
		if(state.depth == state.maxdepth && state.playagain == false)
		{
			traverseMaxChildAB(state);
			return evaluateMax(state);
		}
		Game temp = createGameCopy(state);
		for(int i = 1; i < state.board[1].length -1; i++)
		{	
			int pit;
			if(state.player == 1) 	pit = state.board[1][i].stones;
			else 				pit = state.board[0][i].stones;
			if(pit == 0)				continue;
			
			traverseLogAB(state);
			int isbonusmove = 0;
			if(state.playagain == true)
			{
				isbonusmove = 1;
				state.playagain = false;
			}
			Game childstate = getChild(state, i);
			
			
			int realstatevalue = state.value;
			int realalpha = state.alpha;
			int realbeta = state.beta;
			state = createGameCopy(temp);
			state.value = realstatevalue;
			state.alpha = realalpha;
			state.beta = realbeta;
			
			if(state.depth == 0 && state.playagain == false)
			{
				list.add(childstate);
			}
			
			if(isbonusmove == 1)
				childstate.depth = state.depth;

			//bonus move
			if(childstate.playagain == true)
			{
				childstate.value = Integer.MIN_VALUE;
				state.value = getMax(state.value, maxAB(childstate));
				//pruning
				state.alpha = getMax(state.alpha,state.value);
				if(state.value >= state.beta)
				{
					traverseLogAB(state);
					return state.value;
				}
				state.alpha = getMax(state.alpha,state.value);
			}
			//normal move
			else
			{
				childstate.value = Integer.MAX_VALUE;
				childstate.player = (childstate.player+1)%2;
				state.value = getMax(state.value, minAB(childstate));
				//pruning
				if(childstate.value >= childstate.beta)
				{
					traverseLogAB(childstate);
					return childstate.value;
				}
				//else
				childstate.alpha = getMax(childstate.alpha,childstate.value);
			}
		}
	
		traverseLogAB(state);
		return state.value;
	}
	public static int minAB(Game state) throws Exception
	{
		//terminal state checking
		
		if(isGameOver(state))
		{
			moveCoinsToOppMancala(state);
			traverseMinChildAB(state);
			return evaluateMin(state);
		}
		
		if(state.depth == state.maxdepth && state.playagain == false)
		{
			traverseMinChildAB(state);
			return evaluateMin(state);
		}
		Game temp = createGameCopy(state);
		for(int i = 1; i < state.board[1].length -1; i++)
		{	
			int pit;
			if(state.player == 1)	pit = state.board[1][i].stones;
			else				pit = state.board[0][i].stones;
			if(pit == 0)
				continue;
			
			traverseLogAB(state);
			
			int isbonusmove = 0;
			if(state.playagain == true)
			{
				isbonusmove = 1;
				state.playagain = false;
			}
			Game childstate = getChild(state, i);
			
			int realstatevalue = state.value;
			int realalpha = state.alpha;
			int realbeta = state.beta;
			state = createGameCopy(temp);
			state.value = realstatevalue;
			state.alpha = realalpha;
			state.beta = realbeta;
			
			if(isbonusmove == 1)
				childstate.depth = state.depth;
			
			if(state.depth == 1)
			{
				if(state.value != Integer.MAX_VALUE) 
				{
				if(!list.contains(state))
					list.add(state);
				}
			}
			//bonus move
			if(childstate.playagain == true)
			{
				childstate.value = Integer.MAX_VALUE;
				state.value = getMin(state.value, minAB(childstate));
				//pruning
				state.beta = getMin(state.beta,state.value);
				if(state.value <= state.alpha)
				{
					traverseLogAB(state);
					return state.value;
				}
				state.beta = getMin(state.beta,state.value);
			}
			//normal move
			else
			{
				childstate.value = Integer.MIN_VALUE;
				childstate.player = (childstate.player+1)%2;
				state.value = getMin(state.value, maxAB(childstate));
				//pruning
				state.beta = getMin(state.beta,state.value);
				if(state.value <= state.alpha)
				{
					traverseLogAB(state);
					return state.value;
				}
				state.beta = getMin(state.beta,state.value);
			}
		}
		traverseLogAB(state);
		return state.value;
	}
	public static int getMax(int a, int b)
	{
		return a > b ? a:b;
	}
	public static int getMin(int a, int b)
	{
		return a < b ? a:b;
	}
	public static void moveCoinsToOppMancala(Game state)
	{
		int lsum = 0;
		int rsum = 0;
		for(int i = 1; i < state.board[0].length-1; i++)
		{
			lsum += state.board[0][i].stones;
			rsum += state.board[1][i].stones;
		}
		state.board[0][0].stones += lsum;
		state.board[1][state.board[1].length-1].stones += rsum;
		for(int i = 1; i < state.board[0].length-1; i++)
		{
			state.board[0][i].stones = 0;
			state.board[1][i].stones = 0;
		}
	}
	public static boolean isGameOver(Game state)
	{
		boolean terminateFlag = true;
		//if even one of the pits has more than 1 stone, set the terminateflag to false
		//if flag is true, then the game is over
		if(state.player == 1)
		{
			for(int i = 1; i < state.board[1].length-1; i++)
				if(state.board[1][i].stones > 0)
					terminateFlag = false;
			return terminateFlag;
		}
		else
		{
			for(int i = state.board[0].length-1; i > 0; i--)
				if(state.board[0][i].stones > 0)
					terminateFlag = false;
			return terminateFlag;
		}	
	}
	public static int evaluateMin(Game state)
	{
		int eval = 0;
		if(state.player == 1)
		{
			eval = state.board[0][0].stones - state.board[1][state.board[1].length-1].stones ;
		}
		else
		{
			eval = state.board[1][state.board[1].length-1].stones - state.board[0][0].stones ;
		}
		
		return eval;
	}
	public static int evaluateMax(Game state)
	{
		int eval = 0;
		if(state.player == 1)
		{
			eval = state.board[1][state.board[1].length-1].stones - state.board[0][0].stones ;
		}
		else
		{
			eval = state.board[0][0].stones - state.board[1][state.board[1].length-1].stones ;
		}
		return eval;
	}
	public static Game getChild(Game state, int pitnum)
	{
		//if terminal state reached means call
		
		
		int j = pitnum;
		int currstones = 0;
		if(state.player == 1)
		{
			//update the depth
			currstones = state.board[1][j].stones;
			state.label = state.board[1][j].label;
			state.board[1][j].stones = 0;
		}
		//player 2's moves
		else
		{
			currstones = state.board[0][j].stones;
			state.label = state.board[0][j].label;
			state.board[0][j].stones = 0;
			
		}
		addFactor(state.player, state, currstones);
		addRemainder(state.player, state, currstones, j);
		return state;
	}
	public static void addFactor(int player, Game mancala, int currstones)
	{
		if(player == 1)
		{
			int p1num = mancala.board[1].length-2;
			int boxes = p1num*2 + 1; // +1 for the p1's mancala
			int factor = currstones/boxes;
			//add stones factor to all boxes 
			if(factor!=0)
			{
				for(int x = 0; x < mancala.board.length; x++)
					for(int y = 1; y < mancala.board[1].length-1; y++)
						mancala.board[x][y].stones += factor;
			}
			//add to mancala also
			mancala.board[1][mancala.board[1].length-1].stones += factor;
		}
		else
		{
			int p1num = mancala.board[0].length-2;
			int boxes = p1num*2 + 1; // +1 for the p1's mancala
			int factor = currstones/boxes;
		//	System.out.println("Add "+factor+" to all "+boxes +" boxes and 1 to "+ remainder+" boxes");
			//add stones factor to all boxes 
			if(factor!=0)
			{
				for(int x = 0; x < mancala.board.length; x++)
					for(int y = 1; y < mancala.board[0].length-1; y++)
						//if(x != 1 && y != j)
						mancala.board[x][y].stones += factor;
			}
			//add to mancala also
			mancala.board[0][0].stones += factor;
		}
	}
	public static void addRemainder(int player, Game state, int currstones, int pitnum)
	{
		if(state.playagain == false)
		{
			state.depth +=1;
		}
		if(player == 1)
		{
		int p1num = state.board[1].length-2;
		int boxes = p1num*2 + 1; // +1 for the p1's mancala
		int remainder = currstones%boxes;
		//add the remainder one by one
		
		int k = pitnum+1;
		int l = state.board[1].length-2;
		while(remainder > 0)
		{
			if(remainder == 1)
			{
				//check if last box is mancala
				if(k == state.board[1].length-1)
				{
				//	System.out.println("bonus move for "+state.label);
					state.board[1][k].stones += 1;
					k++;
					remainder--;
					//allow the player to play again
					state.playagain = true;
					break;
				}
				//if the box that the player is empty
				// first check whether the last element is on the player's side, in this case the k value shd be betwe 1 to (n-1)
				if(k < state.board[1].length)
				{
					if(state.board[1][k].stones == 0)
					{
						//add those stones to the mancala
					//	System.out.println("player 1 steals opp stones "+state.label);
						state.board[1][state.board[1].length-1].stones += 1 + state.board[0][k].stones;
						//System.out.println("value ="+state.board[1][state.board[1].length-1].stones);
						state.board[0][k].stones = 0;
						remainder--;
						break;
					} 
				}
			}
			if(k > state.board[1].length-1)
			{
				//don't add to opponent's mancala
				if(l == 0)
				{
					k = 1;
					l = state.board[1].length - 2;
					state.board[1][k].stones += 1;
					k++;
					remainder--;
				}
				else
				{
					state.board[0][l].stones += 1;
					remainder--;
					l--;
				}
			}
			else
			{
				state.board[1][k].stones += 1;
				k++;
				remainder--;
			}
		}
		}
		else
		{
			int p1num = state.board[0].length-2;
			int boxes = p1num*2 + 1; // +1 for the p1's mancala
			int remainder = currstones%boxes;
			//add the remainder one by one
			int k = pitnum-1;
			int l = 1;
			while(remainder > 0)
			{
				if(remainder == 1)
				{
					//check if last box is mancala (first box)
					if(k == 0)
					{
						//player gets a free move
					//	System.out.println("bonus move for "+state.label);
						state.board[0][k].stones += 1;
						k--;
						remainder--;
						//update the playagain 
						state.playagain = true;
						break;
						
					}
					//if the box that the player is empty
					// first check whether the last element is on the player's side, in this case the k value shd be betwe 1 to (n-1)
					if(k > 0)
					{
						if(state.board[0][k].stones == 0)
						{
							//add those stones to the mancala
						//	System.out.println("player 2 steals opp stones "+state.label);
							state.board[0][0].stones += 1 + state.board[1][k].stones;
						//	System.out.println("value = "+state.board[0][0].stones);
							state.board[1][k].stones = 0;
							remainder--;
							break;
						} 
					}
				}
				if(k < 0)
				{
					//don't add to opponent's mancala
					if(l == state.board[0].length-1)
					{
						k = state.board[0].length-2;
						l = 1;
						state.board[0][k].stones += 1;
						k--;
						remainder--;
					}
					else
					{
						state.board[1][l].stones += 1;
						remainder--;
						l++;
					}
				}
				else
				{
					state.board[0][k].stones += 1;
					k--;
					remainder--;
				}
			}
			
		}
	}
	public static int maxGreedy(Game state) throws Exception
	{
		
		Game temp = createGameCopy(state);
		if(state.depth == 1 && state.playagain == false)
		{
			if(evaluateMax(state) > maxvalue)
			{
				maxvalue = evaluateMax(state);
				chosenmove = state.label;
				nextState = createGameCopy(state);
			}
			return evaluateMax(state);
		}
		for(int i = 1; i < state.board[1].length -1; i++)
		{	
			int pit;
			if(state.player == 1) 	pit = state.board[1][i].stones;
			else 				pit = state.board[0][i].stones;
			if(pit == 0)			
				continue;
			int isbonusmove = 0;
			
			if(state.playagain == true)
			{
				isbonusmove = 1;
				state.playagain = false;
			}
			Game childstate = getChild(state, i);
			
			state = createGameCopy(temp);
			
			if(isbonusmove == 1)
				childstate.depth = state.depth;
			state.value = getMax(state.value, maxGreedy(childstate));
			
		}	
		return state.value;
	}
	public static void main(String args[]) throws Exception
	{
		String ipfile = "input.txt";
		String travstr = "traverse_log.txt";
		String nextstr = "next_state.txt";
		FileWriter travfile;
		FileWriter nextfile;
		String[] input = null;
		BufferedReader br=new BufferedReader(new FileReader(ipfile));
        String str="", line = "";
        while((line=br.readLine())!=null)
        {
            if(line.isEmpty()) break;
            str += line.trim()+"\n";
        }
        input = str.split("\n");
        int task_num = Integer.parseInt(input[0]);
		
		int pnum = Integer.parseInt(input[1]);
		int cutoffdepth = Integer.parseInt(input[2]);
		
		String[] p2_board = input[3].split(" ");
		String[] p1_board = input[4].split(" ");
		//boardlength = number of pits + player's mancala (1)
		int board_length = p2_board.length+1;

		int p2_mancala = Integer.parseInt(input[5]);
		int p1_mancala = Integer.parseInt(input[6]);
		
		int[] p2 = new int[board_length];
		int[] p1 = new int[board_length];
		
		for(int i = 0; i < board_length-1; i++)
		{
			p2[i+1] = Integer.parseInt(p2_board[i]);
			p1[i] = Integer.parseInt(p1_board[i]);
		}
		p2[0] = p2_mancala;
		p1[board_length-1] = p1_mancala;
		Node[][] gameboard = new Node[2][board_length+1];
		//the mancala pieces for player 1
		Node temp = new Node();
		temp.label = "B"+(board_length+1);
		temp.stones = p1_mancala;
		gameboard[0][board_length] = temp;
		gameboard[1][board_length] = temp;
		//the mancala pieces for player 2
		Node temp2 = new Node();
		temp2.label = "A1";
		temp2.stones = p2_mancala;
		gameboard[0][0] = temp2;
		gameboard[1][0] = temp2;
		for(int j = 0; j < board_length-1; j++)
		{
			Node temp1 = new Node();
			temp1.label = "B"+(j+2);
			temp1.stones = p1[j];
			gameboard[1][j+1] = temp1;
			Node temp21 = new Node();
			temp21.label = "A"+(j+2);
			temp21.stones = p2[j+1];
			gameboard[0][j+1] = temp21;
		}
		Game mancala = new Game();
		
		mancala.board = gameboard;
		mancala.value = Integer.MIN_VALUE;
		mancala.label = "root";
		mancala.player = pnum;
		mancala.maxdepth = cutoffdepth;
		mancala.alpha = Integer.MIN_VALUE;
		mancala.beta = Integer.MAX_VALUE;
		
		nextState = createGameCopy(mancala);
		int max = Integer.MIN_VALUE;
		switch(task_num)
		{
		//Greedy
		case 1: 
			nextfile = new FileWriter(nextstr);
			maxGreedy(mancala);
			displayNextState(nextState.board);		
			nextfile.write(ns);
			nextfile.close();
			
		break;
		//Minimax
		case 2: 
		nextfile = new FileWriter(nextstr);
		travfile = new FileWriter(travstr);
		traverse += "Node,Depth,Value"+"\n";
		max(mancala);		
		
		for(Game g: list)
		{
		//	System.out.println("max = "+max + " label="+g.label +" g.value = "+ g.value);
		//	displayMancala(g.board);
			if(g.value == Integer.MAX_VALUE) continue;
			if(g.value > max)
			{
				max = g.value;
				nextState = createGameCopy(g);
			}
			if(g.playagain == true) continue;
		}
		displayNextState(nextState.board);
		travfile.write(traverse);
		nextfile.write(ns);
		travfile.close();
		nextfile.close();
		
		break;
		
		//Alpha Beta
		case 3:  	
			nextfile = new FileWriter(nextstr);
			travfile = new FileWriter(travstr);
			traverse += "Node,Depth,Value,Alpha,Beta"+"\n";
			maxAB(mancala);
		for(Game g: list)
		{
		//	System.out.println("max = "+max + " label="+g.label +" g.value = "+ g.value);
			if(g.value == Integer.MAX_VALUE) continue;
			if(g.value > max)
			{
				max = g.value;
				nextState = createGameCopy(g);
			}
		}
		displayNextState(nextState.board);
		travfile.write(traverse);
		nextfile.write(ns);
		travfile.close();
		nextfile.close();
		break;
		
		default: 
			System.out.println("Choices are not between 1-4");
			break;
		}
        br.close();
    }
}
