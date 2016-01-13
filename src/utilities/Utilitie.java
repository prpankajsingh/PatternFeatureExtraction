/**
 * 
 */
package utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
}
