import edu.duke.*;
import java.util.*;

public class AdvancedGladLib {
	private HashMap<String, ArrayList<String>> myMap;
	private ArrayList<String> wordsSeen;

	private Random myRandom;

	private static String dataSourceURL = "http://dukelearntoprogram.com/course3/datalong";
	private static String dataSourceDirectory = "datalong";

	public AdvancedGladLib() {
		myRandom = new Random();
		wordsSeen = new ArrayList<String>();
		myMap = new HashMap<String, ArrayList<String>>();
		initializeFromSource(dataSourceDirectory);
	}

	public AdvancedGladLib(String source) {
		myRandom = new Random();
		wordsSeen = new ArrayList<String>();
		myMap = new HashMap<String, ArrayList<String>>();
		initializeFromSource(source);
	}

	private void initializeFromSource(String source) {
		String[] categories = new String[] { "adjective", "noun", "country", "name", "animal", "timeframe", "verb",
				"fruit", "color", "number" };
		for (String category : categories) {
			myMap.put(category, readIt(source + "/" + category + ".txt"));
		}
	}

	private String randomFrom(ArrayList<String> source) {
		int index = myRandom.nextInt(source.size());
		return source.get(index);
	}

	private String getSubstitute(String label) {
		if (myMap.containsKey(label)) {
			if (label.equals("time")) {
				return myRandom.nextInt(100000) + "";
			}
			return randomFrom(myMap.get(label));
		}
		return "**" + label + "**";
	}

	private String processWord(String w) {
		int first = w.indexOf("<");
		int last = w.indexOf(">", first);
		if (first == -1 || last == -1) {
			return w;
		}
		String prefix = w.substring(0, first);
		String suffix = w.substring(last + 1);
		String sub = getSubstitute(w.substring(first + 1, last));
		return prefix + sub + suffix;
	}

	private void printOut(String s, int lineWidth) {
		int charsWritten = 0;
		for (String w : s.split("\\s+")) {
			if (charsWritten + w.length() > lineWidth) {
				System.out.println();
				charsWritten = 0;
			}
			System.out.print(w + " ");
			charsWritten += w.length() + 1;
		}
	}

	private String fromTemplate(String source) {
		String story = "";
		if (source.startsWith("http")) {
			URLResource resource = new URLResource(source);
			for (String word : resource.words()) {
				story = story + processWord(word) + " ";
			}
		} else {
			FileResource resource = new FileResource(source);
			for (String word : resource.words()) {
				story = story + processWord(word) + " ";
			}
		}
		return story;
	}

	private ArrayList<String> readIt(String source) {
		ArrayList<String> list = new ArrayList<String>();
		if (source.startsWith("http")) {
			URLResource resource = new URLResource(dataSourceURL + source);
			for (String line : resource.lines()) {
				list.add(line);
			}
		} else {
			FileResource resource = new FileResource(source);
			for (String line : resource.lines()) {
				list.add(line);
			}
		}
		return list;
	}

	public void makeStory() {
		System.out.println("\n");
		String story = fromTemplate("datalong/madtemplate2.txt");
		printOut(story, 60);
		wordsSeen.clear();
	}

}
