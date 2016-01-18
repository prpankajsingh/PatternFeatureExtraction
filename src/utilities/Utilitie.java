/**
 * 
 */
package utilities;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.trees.Tree;
import patternfeatureextractor.Triple;

/**
 * @author jatin khurana
 *
 */
public class Utilitie {

	public static void generateConfusionMatrix(HashMap<String,ArrayList<Triple>> patterns)
	{
		int c_alignement=0,c_parsetree=0,c_both=0,c_none=0;
		boolean isaligned=true,isparsetreevalid=true;
		for(Map.Entry<String, ArrayList<Triple>> entry:patterns.entrySet())
		{
			String pattern = entry.getKey();
			isaligned=true; isparsetreevalid=true;
			for(Triple triple: entry.getValue())
			{
				if(!triple.isAligned)
				{
					isaligned=false; break;
				}
			}
			for(Triple triple: entry.getValue())
			{
				if(!triple.isSyntaticComplete)
				{
					isparsetreevalid=false; break;
				}
			}
			if(isaligned && isparsetreevalid)
					c_both++;
				else if(isaligned)
					c_alignement++;
				else if(isparsetreevalid)
					c_parsetree++;
				else
					c_none++;
		}
		System.out.println("Total Number of Patterns:"+patterns.size());
		System.out.println("Both:"+c_both);
		System.out.println("Aligned:"+c_alignement);
		System.out.println("ParseTree:"+c_parsetree);
		System.out.println("None:"+c_none);
	}

	public static void getVisualTreeString(PrintWriter pw,Tree t,int OneWordSize) {
		// TODO Auto-generated method stub
		
	        int h = t.depth();
	        int i;
	        String  star = "\n";
	        String dash = "\n";
	        for(i=1;i<=(getSpace(t, OneWordSize)+t.label().toString().length()+1);i++)
	        {
	        	star += "*";
	        	dash += "-";
	        }
	        	
	        star += "\n";
	        dash += "\n";
	        pw.write(star);
	        for (i=1; i<=h; i++)
	        {
	            printGivenLevel(pw, t, i, OneWordSize);
	            pw.write(dash);
	        }
	 
	    /* Print nodes at the given level */
		
	}

/*	
	private static int getSpace(Tree t,int sepSize)
	{
		int space=0;
		int leafCount = t.getLeaves().size(); 
    	int totSpace = leafCount*sepSize-t.label().toString().length()+1;
		return space;
	}
*/
	private static int getSpace(Tree t,int sepSize)
	{
		int space=0;
		List<Tree> leaves = t.getLeaves();
		for(int i=0;i< leaves.size();i++)
		{
			space += leaves.get(i).label().toString().length()+2*sepSize+1;
		}
    	int totSpace = space-t.label().toString().length()-1;
		return totSpace;
	}

	
	private static void printGivenLevel(PrintWriter pw,Tree t, int level, int OneWordSize) {
		// TODO Auto-generated method stub
		//getChildrenAsList
		//public List<Tree> getChildrenAsList() : if no child then size of list will be 0
		//public int depth()
		//public <T extends Tree> List<T> getLeaves()

		
	    {
	    	List<Tree> children  = t.getChildrenAsList();
	        if (children.size() == 0 || t.isLeaf())
	        {
//	        	int leafCount = t.getLeaves().size(); 
	        	int totSpace = getSpace(t, OneWordSize);
	        	int lSpace = totSpace/2;
	        	int rSpace = totSpace - lSpace;
	        	pw.write(getSpace(lSpace)+t.label().toString()+getSpace(rSpace)+"|");
	        	return;
	        }
	        if (level == 1)
	        {
	        	//System.out.println(t.label().toString());
	        	//int leafCount = t.getLeaves().size(); 
	        	//int totSpace = leafCount*OneWordSize-t.label().toString().length()+1;
	        	int totSpace = getSpace(t, OneWordSize);
	        	int lSpace = totSpace/2;
	        	int rSpace = totSpace - lSpace;
	        	pw.write(getSpace(lSpace)+t.label().toString()+getSpace(rSpace)+"|");
	        }
	        	
	        else if (level > 1)
	        {
	        	for(int j=0;j<children.size();j++)
	        		printGivenLevel(pw,children.get(j), level-1,OneWordSize);
	        }
	    }
		
	}

	
	private static String getSpace(int len) {
		// TODO Auto-generated method stub
		String s="";
		for(int i=0;i<len;i++)
			s+=" ";
		return s;
	}
}
