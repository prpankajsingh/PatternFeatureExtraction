package _stanfordParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Document implements Serializable
{
	private static final long serialVersionUID = -8362626053122851900L;
	static int documentCount = 0;
	int id;
	public List<Sentence> sentences;
	public HashMap<Integer, Sentence> speakers = new HashMap<Integer, Sentence>();
	String documentPath;
	String serializedDocumentPath;

	public Document(String documentPath, String serializedDocumentPath)
	{
		id = ++documentCount;
		this.documentPath = documentPath;
		this.serializedDocumentPath = serializedDocumentPath;
		sentences = new ArrayList<Sentence>();
	}

	public void synthesizeDocument(boolean deserialize) throws IOException
	{
		if (!deserialize)
		{
			System.out.println("parsing starts ....");
			readDocument();
			//generateParsesForSentences();
			boolean success = SerializationAndDeserializationUtilities.Serialize(this,
					serializedDocumentPath);
			System.out.println("Serialization was : " + (success ? "successful." : "unsuccessful"));
		} else
		{
			SerializationAndDeserializationUtilities.DeSerialize(this, serializedDocumentPath);
			System.out
					.println("Successfully deserialized " + this.sentences.size() + " sentences.");
		}
	}

	private void readDocument() throws IOException
	{
		FileReader fr = new FileReader(documentPath);
		@SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(fr);

		// Add dummy sentence at top to start indexing of "sentences" from 1.
		Sentence dummy = new Sentence();
		sentences.add(dummy);

		String line = "";
		while ((line = br.readLine()) != null)
		{
			int id=Integer.parseInt(line.substring(0, line.indexOf("- ")));
			System.out.println(id);
			line = line.substring(line.indexOf(" ") + 1);
			Sentence newSentence = new Sentence(id,line);
			sentences.add(newSentence);
			//System.out.println(sents.size());
			speakers.put(id, newSentence);
		}
	}
}
