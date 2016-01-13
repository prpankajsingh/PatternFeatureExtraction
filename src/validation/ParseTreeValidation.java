package validation;

import java.io.PrintWriter;
import java.util.List;

import edu.stanford.nlp.trees.Tree;
import patternfeatureextractor.PatternFeatureExtractorConfiguration;
import patternfeatureextractor.Triple;
import _stanfordParser.*;

public class ParseTreeValidation {
	static Document document = new Document(PatternFeatureExtractorConfiguration.CorpusFile,
			PatternFeatureExtractorConfiguration.ParseTreeFile);
	static
	{
		try
		{
		document.synthesizeDocument(true);
		}
		catch(Exception e)
		{
			
		}
	}
	
	public static boolean isValid(Triple triple)
	{
		Tree tree = (document.speakers.get(triple.sentenceId)).parseTree;
		List<Tree> leaves = tree.getLeaves();
		
		//System.out.println(leaves.size());
		if(leaves.size()>triple.start && leaves.size()>triple.end)
		{
			List<Tree> aptLeaves = tree.joinNode(leaves.get(triple.start), leaves.get(triple.end)).getLeaves();
			if (aptLeaves.size() == (triple.end - triple.start + 1))
			{
				//return consecutiveNP(pattern);
				return true;
			} 
			else
			{
				return false;
			}
		}
		else{
			return false;
		}
	}
	
	public static String getLabel(Triple triple)
	{
		Tree tree = (document.speakers.get(triple.sentenceId)).parseTree;
		List<Tree> leaves = tree.getLeaves();
		Tree childtree = tree.joinNode(leaves.get(triple.start), leaves.get(triple.end));
		return childtree.label().toString();
		
	}
	
	public static void getSiblingTag(Triple triple)
	{
		Tree tree = (document.speakers.get(triple.sentenceId)).parseTree;
		List<Tree> leaves = tree.getLeaves();
		Tree childtree = tree.joinNode(leaves.get(triple.start), leaves.get(triple.end));
		
		
	}
	
	public static void dumpParseTree(PrintWriter pw)
	{
		for(int i=1;i<=5000;i++)
		{
			Tree tree = (document.speakers.get(i)).parseTree;
			pw.write(tree.toString()+"\n");
		}
		pw.close();
	}
}
