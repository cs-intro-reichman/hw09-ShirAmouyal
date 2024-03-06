import java.util.HashMap;
import java.util.Random;

public class LanguageModel {

    // The map of this model.
    // Maps windows to lists of charachter data objects.
    static HashMap<String, List> CharDataMap;
    
    // The window length used in this model.
    int windowLength;
    
    // The random number generator used by this model. 
	private Random randomGenerator;

    /** Constructs a language model with the given window length and a given
     *  seed value. Generating texts from this model multiple times with the 
     *  same seed value will produce the same random texts. Good for debugging. */
    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<String, List>();
    }

    /** Constructs a language model with the given window length.
     * Generating texts from this model multiple times will produce
     * different random texts. Good for production. */
    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<String, List>();
    }

    /** Builds a language model from the text in the given file (the corpus). */
	public void train(String fileName) {
		// Your code goes here
        In in = new In(fileName);
        String wind = "";
        char ch;
        // Reads just enough characters to form the first window
        for (int k = 0; k < this.windowLength; k++) {
            wind += in.readChar();
        }
        // Processes the entire text, one character at a time
        while (!in.isEmpty()) {
            // Gets the next character
            ch = in.readChar();
            if (LanguageModel.CharDataMap.get(wind) == null) {
                List probs = new List();
                CharDataMap.put(wind, probs);
            }
            // Calculates the counts of the current character
            LanguageModel.CharDataMap.get(wind).update(ch);

            // Advances the window: adds c to the window’s end, and deletes the
            // window's first character.
            wind = wind.substring(1) + ch;
        }
        for (List probs : LanguageModel.CharDataMap.values()) {
            this.calculateProbabilities(probs);
        }
	}

    // Computes and sets the probabilities (p and cp fields) of all the
	// characters in the given list. */
	public void calculateProbabilities(List probs) {				
		// Your code goes here
        double countCh = 0;
        for (int i = 0; i < probs.getSize(); i++) {
            countCh += probs.get(i).count;
        }
        for (int i = 0; i < probs.getSize(); i++) {
            probs.get(i).p = (double) (1 / countCh) * probs.get(i).count;
        }
        probs.getFirst().cp = probs.getFirst().p;
        for (int n = 1; n < probs.getSize(); n++) {
            probs.get(n).cp = probs.get(n - 1).cp + probs.get(n).p;
        }
	}

    // Returns a random character from the given probabilities list.
	public char getRandomChar(List probs) {
		// Your code goes here
        double r = randomGenerator.nextDouble();

        for (int i = 0; i < probs.getSize(); i++) {
            if (probs.get(i).cp > r)
                return probs.get(i).chr;
        }
        return ' ';
	}

    /**
	 * Generates a random text, based on the probabilities that were learned during training. 
	 * @param initialText - text to start with. If initialText's last substring of size numberOfLetters
	 * doesn't appear as a key in Map, we generate no text and return only the initial text. 
	 * @param numberOfLetters - the size of text to generate
	 * @return the generated text
	 */
	public String generate(String initialText, int textLength) {
		// Your code goes here
        if (textLength < windowLength) {
            return initialText;
        }
        String window = initialText.substring(initialText.length() - windowLength);
        String genText = window;
        while (textLength != genText.length() - windowLength) {
            if (CharDataMap.get(window) == null) {
                return genText;
            }
            List probs = LanguageModel.CharDataMap.get(window);
            char c = getRandomChar(probs);
            genText = genText + c;
            window = genText.substring(genText.length() - windowLength);
        }
        return genText;
	}

    /** Returns a string representing the map of this language model. */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (String key : CharDataMap.keySet()) {
			List keyProbs = CharDataMap.get(key);
			str.append(key + " : " + keyProbs + "\n");
		}
		return str.toString();
	}

    
}
