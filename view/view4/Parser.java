package aqua;
import java.util.regex.Pattern;


public class Parser {

    /* Entrée : "addFish PoissonNain at 61x52, 4x3, RandomWayPoint"
     * Sortie : {addFish,PoissonNain,at,61,52,4,3,RandomWayPoint,}
     */

    public static String[] parseFishPosition(String cmd){
	String delims = "[ x,]+";
	String[] tokens = cmd.split(delims);
	return tokens;
    }

    /* Entrée :"list [PoissonRouge at 92 x 40, 10x4 ,5] 
     *              [PoissonClown at 22x80,12x6,5]
     *              [PoissonRouge at 92x40,10x4,5]
     *              [PoissonClown at 22x80,12x6,5]"
     * Sortie :{
     *         {PoissonRouge,at,92,40,10,4,5,}
     *         {PoissonClown,at,22,80,12,6,5,}
     *         {PoissonRouge,at,92,40,10,4,5,}
     *         {PoissonClown,at,22,80,12,6,5,}
     *         }
     */

    public static String[][] parseListFishPosition(String cmd){
	String delims ="[\\[\\]]";
	String[] tokens = cmd.split(delims);
	String[][] arrayToken= new String[tokens.length/2][];
	int j = 0;
	for (int i = 0; i < tokens.length ; i++){
	    if (i % 2 != 0){
		arrayToken[j] = parseFishPosition(tokens[i]);
		j++;
	    }
	}
	return arrayToken;
    }


    
    
}
