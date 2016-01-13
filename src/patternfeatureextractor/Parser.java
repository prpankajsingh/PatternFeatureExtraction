package patternfeatureextractor;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


import validation.*;
import utilities.*;

public class Parser {
	
	public static HashMap<String,ArrayList<Triple>> patterns = new HashMap<String,ArrayList<Triple>>();
	public static HashMap<Integer,ArrayList<Pair>> alignment=new HashMap<Integer,ArrayList<Pair>>();
	public static HashMap<Integer,String> corpus = new HashMap<Integer,String>();
	
	
	/* This method reverse the input string */
	public static String rstring(String input)
	{
		StringBuffer sb=new StringBuffer();
		sb.append(input);
		sb = sb.reverse();
		return sb.toString();
	}
	
	public static void readPatternFile() throws IOException
	{
		System.out.println("Reading Pattern File ....................");
		Scanner sc= new Scanner(new FileInputStream(PatternFeatureExtractorConfiguration.PatternFile));
		String line="";
		while(sc.hasNextLine())
		{
			line = sc.nextLine();
			String rline = rstring(line.trim());
			String arr[]=rline.split(" : ",2);
			arr[0] = rstring(arr[0]);
			arr[1] = rstring(arr[1]);
			
			String pattern=arr[1];
			String instanceString = arr[0].trim().replaceAll("^\\(|\\)$", "");
			String instances[]= instanceString.split("\\) \\(");
			ArrayList<Triple> list=new ArrayList<Triple>();
			for(String inst:instances)
			{
				String a[]=inst.split(",");
				Triple t=new Triple(Integer.parseInt(a[0].trim()),Integer.parseInt(a[1].trim()),Integer.parseInt(a[2].trim()));
				if(a.length>3)
				{
					for(int i=3;i<a.length;i=i+2)
					{
						t.startIndex.add(Integer.parseInt(a[i].trim()));
						t.endIndex.add(Integer.parseInt(a[i+1].trim()));
					}
				}
				list.add(t);
			}
			patterns.put(pattern, list);
		}
		sc.close();
		System.out.println("Reading Pattern File Ended...............");
	}
	
	public static void readAlignmentFile() throws IOException
	{
		System.out.println("Reading Alignment File Started........ ");
		Scanner sc=new Scanner(new FileInputStream(PatternFeatureExtractorConfiguration.AlignmentFile));
		int sentenceid=1;
		ArrayList<Pair> value=new ArrayList<Pair>();
		HashMap<Integer,Integer> alignmentPosMap = new HashMap<Integer,Integer>();
		while(sc.hasNextLine())
		{
			String line=sc.nextLine();
			if(line=="" || line==null || line.isEmpty())
			{
				int maparr[]=new int[value.size()];
				int key=0;
				for(int i=0;i<value.size();i++)
				{
					int temp = alignmentPosMap.get(key);
					maparr[i]=temp;
					key = temp;
				}
				for(int i=0;i<value.size();i++)
				{
					value.get(maparr[i]-1).alignment=i;
				}
				alignment.put(sentenceid, value);
				sentenceid++;
				value=new ArrayList<Pair>();
				alignmentPosMap.clear();
				continue;
			}
			String arr[]=line.split("\t");
			Pair p=new Pair(arr[1],Integer.parseInt(arr[6]));
			alignmentPosMap.put(Integer.parseInt(arr[6]), Integer.parseInt(arr[0]));
			value.add(p);
		}
		sc.close();
		System.out.println("Reading Alignment File Ended........ ");
	}
	
	public static void readCorpusFile() throws IOException
	{
		System.out.println("Reading Corpus File ................");
		Scanner sc = new Scanner(new FileInputStream(PatternFeatureExtractorConfiguration.CorpusFile));
		String line ="";
		while(sc.hasNextLine())
		{
			line = sc.nextLine();
			String arr[] = line.split("- ",2);
			corpus.put(Integer.parseInt(arr[0]), arr[1]);
		}
		sc.close();
		System.out.println("Reading Corpus File Ended............................");
	}
	
	public static void praseTreeValidation()
	{
		System.out.println("Parse Tree Validation starts...................");
		for(Map.Entry<String, ArrayList<Triple>> entry:patterns.entrySet())
		{
			for(Triple t : entry.getValue())
			{
				t.isSyntaticComplete = ParseTreeValidation.isValid(t);
				if(t.isSyntaticComplete)
					t.ConvergeTag = ParseTreeValidation.getLabel(t);
			}
		}
		System.out.println("Parse Tree Validation ends...................");
	}
	
	public static void alignmentValidation()
	{
		System.out.println("Alignment Validation starts...................");
		for(Map.Entry<String, ArrayList<Triple>> entry:patterns.entrySet())
		{
			if(entry.getKey().trim().equals("NP thing NP have to"))
				System.out.println("rrruuuukkkoooooooo");
			for(Triple t : entry.getValue())
			{
				t.isAligned = AlignmentValidation.isValid(alignment, t);
			}
		}
		System.out.println("Alignment Validation ends...................");
	}
	
	public static void assignTagSequence() throws IOException
	{
		System.out.println("Corpus Tag Reading starts...................");
		ArrayList<String> tags = new ArrayList<String>();
		Scanner sc = new Scanner(new FileInputStream(PatternFeatureExtractorConfiguration.CorpusTagFile));
		tags.add(""); // add dummy tag
		String line="";
		while(sc.hasNextLine())
		{
			line = sc.nextLine();
			tags.add(line);
		}
		sc.close();
		System.out.println("Corpus Tag Reading ends...................");
		
		for(Map.Entry<String, ArrayList<Triple>> entry:patterns.entrySet())
		{
			for(Triple t : entry.getValue())
			{
				String sentenceTag[] = tags.get(t.sentenceId).trim().split(" ");
				t.tagSequence = "";
				for(int i=t.start;i<=t.end;i++)
				{
					t.tagSequence += sentenceTag[i]+" ";
				}
			}
		}
	}
	
	public static void writeIntoFile() throws IOException
	{
		PrintWriter pw = new PrintWriter(new FileOutputStream(PatternFeatureExtractorConfiguration.OutputFile));
		int p=1;
		for(Map.Entry<String, ArrayList<Triple>> entry:patterns.entrySet())
		{
			pw.write(entry.getKey()+" : "+"\n");
			for(Triple t : entry.getValue())
			{
				String sentence[] = corpus.get(t.sentenceId).trim().split(" ");
				String instance="";
				for(int i=t.start;i<=t.end;i++)
				{
					instance += sentence[i]+" ";
				}
				pw.write(instance+" : ");
				pw.write(t.sentenceId+","+t.start+","+t.end+" ||| ");
				pw.write(t.tagSequence.trim());
				pw.write(" ||| ");
				if(t.isAligned)
					pw.write("YES");
				else
					pw.write("NO");
				if(t.isSyntaticComplete)
				{
					pw.write(" ||| YES ||| "+t.ConvergeTag);
				}
				else
				{
					pw.write(" ||| NO");
				}
				pw.write("\n");
			}
			pw.write("\n");
		}
		pw.close();
	}
	
	public static void writeParseTree() throws IOException
	{
		System.out.println("Writing Parse Starts .......");
		PrintWriter pw = new PrintWriter(new FileOutputStream(PatternFeatureExtractorConfiguration.ParseTreeOutputFile));
		ParseTreeValidation.dumpParseTree(pw);
		System.out.println("Writing Parse Ends .......");
	}
		
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		readPatternFile();
		readAlignmentFile();
		readCorpusFile();
		praseTreeValidation();
		alignmentValidation();
		assignTagSequence();
		Utilitie.generateConfusionMatrix(patterns);
		writeParseTree();
		writeIntoFile();
	}

}
