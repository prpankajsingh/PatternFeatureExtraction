/**
 * 
 */
package patternfeatureextractor;

import java.util.ArrayList;

public class Triple
{
	public int sentenceId;
	public int start;
	public int end;
	public ArrayList<Integer> startIndex;
	public ArrayList<Integer> endIndex;
	public String tagSequence;
	public boolean isSyntaticComplete;
	public String ConvergeTag;
	public boolean isAligned;	
	

	public Triple(int sentenceId_, int start_, int end_)
	{
		sentenceId = sentenceId_;
		start = start_;
		end = end_;
		startIndex = new ArrayList<Integer>();
		endIndex = new ArrayList<Integer>();
	}

	public String toString()
	{
		String ret = new String("(" + sentenceId + "," + start + "," + (end - 1) + ")");
		return ret;
	}

	public boolean equals(Object obj)
	{
		Triple t2 = (Triple) obj;
		return sentenceId == t2.sentenceId && start == t2.start && end == t2.end;
	}

	public int hashCode()
	{
		return sentenceId + start + end;
	}
}

