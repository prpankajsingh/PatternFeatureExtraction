/**
 * 
 */
package validation;

import java.util.ArrayList;
import java.util.HashMap;

import patternfeatureextractor.Pair;
import patternfeatureextractor.Triple;

/**
 * @author vishwajit
 *
 */
public class AlignmentValidation {

	public static int findmin(int []arr)
	{
		int min=arr[0];
		for(int i=0;i<arr.length;i++)
		{
			if(min>arr[i])
				min=arr[i];
		}
		return min;
	}
	
	public static int findmax(int []arr)
	{
		int max=arr[0];
		for(int i=0;i<arr.length;i++)
		{
			if(max<arr[i])
				max=arr[i];
		}
		return max;
	}
	
	public static boolean isExist(int []arr,int value)
	{
		for(int i=0;i<arr.length;i++)
		{
			if(arr[i]==value)
				return true;
		}
		return false;
	}
	
	public static boolean isContinues(HashMap<Integer,ArrayList<Pair>> alignment,int id,int begin,int end)	{
		
		boolean result=true;
		
		ArrayList<Pair> list=alignment.get(id);
		int arr[]=new int[end-begin+1];
		try
		{
			for(int i=begin,k=0;i<=end;i++,k++)
			{
				arr[k]=list.get(i).alignment;
			}
		int min=findmin(arr);
		int max=findmax(arr);
		for(int i=min;i<=max;i++)
		{
			if(!isExist(arr,i))
			{
				result=false;break;
			}
		}
		}
		catch(Exception e)
		{
			result=false;
		}
		return result;
		
	}
	
	public static boolean isValid(HashMap<Integer,ArrayList<Pair>> alignment,Triple t)
	{
		boolean flag=true;
		
		for(int i=0;i<t.startIndex.size();i++)
		{
			int beginpos=t.startIndex.get(i);
			int endpos=t.endIndex.get(i);
			if(!isContinues(alignment,t.sentenceId,beginpos,endpos))
			{
				flag=false;break;
			}
		}
		if(!flag)
			return flag;
		else
		{
			if(isContinues(alignment,t.sentenceId,t.start,t.end))
				return true;
			else
				return false;
		}
	}
	
}
