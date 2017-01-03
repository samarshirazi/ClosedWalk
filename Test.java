import java.util.Random;

public class Test {
	
	public static String str="";
	public static String pathOfCW = "D:/graph/ClosedWalk/";
	public static String bottomSize = "";
	public static void main(String [] args){
		if( args.length != 3)
		{
			System.out.println("parameters should be three (dataset maxSizeOfSample howManyToRemoveFromSample)");
			return;
		}	
		
		int graphMxId = -1;

		str = args[0];
		bottomSize = args[2];
		if( str.equals("skitter"))
			graphMxId = 1696415;
		else if (str.equals("modifiedyoutube"))
			graphMxId = 1000000;
		else if (str.equals("journal"))
			graphMxId = 4847000;
		else if (str.equals("barabasi1") || str.equals("barabasi2"))
			graphMxId = 484700;
		else if (str.equals("dblp"))
			graphMxId = 317080;
		else if (str.equals("amazon"))
			graphMxId = 334863;
		
		
		System.out.println(str);
		//String path = pathOfCW+"data/"+str+".txt";
		String path = "D:/graph/"+"dataset/"+str+".txt";
		Random rand = new Random();
		int sampleNumber = rand.nextInt(graphMxId);
		Graph G = new Graph(path,sampleNumber);
		G.reset(Integer.parseInt(args[1]));	
		System.out.println("sample number " + sampleNumber );
		G.greedy();
	}
}
