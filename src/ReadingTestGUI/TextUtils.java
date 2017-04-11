/**
 * 
 */
package ReadingTestGUI;

import java.util.ArrayList;

/**
 * @author Chris Spooner
 *
 */
public class TextUtils {
	public static String FormatText(String text, int width){
		String[] numberOfWords = text.split(" ");
		String out = "";
		String line = "";
		for (String each : numberOfWords){
			if (each.charAt(0) == '\n'){
				out += line;
				line = each + " ";
			}
			else if ((line.length() + each.length()) < width){
				line += each + " ";
			}else{
				out += line + "\n";
				line = each + " ";
			}
		}
		out += line;
		return out;
	}
	
	public static String FormatTextHTML(String text, int width){
		String[] numberOfWords = text.split(" ");
		String out = "<HTML>";
		String line = "";
		for (String each : numberOfWords){
			if (each.charAt(0) == '\n'){
				out += line + "<br>";
				line = each + " ";
			}
			else if ((line.length() + each.length()) < width){
				line += each + " ";
			}else{
				out += line + "<br>";
				line = each + " ";
			}
		}
		out += line;
		return out;
	}
	
	public static int WordCount(String text){
		int count = 0;
		for (char each : text.toCharArray()){
			if (each == ' '){
				count ++;
			}
		}
		if (count >= 1){
			count ++;
		}
		return count;
	}
	
	public static int SentenceCount(String text){
		int count = 0;
		char[] chars = text.toCharArray();
		for (int i = 0; i < text.length()-1; i++){
			if ((chars[i] == '.' || chars[i] == '?' || chars[i] == '!') && (chars[i+1] ==' ' || chars[i+1] =='\'' || chars[i+1] =='"')){
				count += 1;
			}
		}
		return count;
	}
	public static double AvgWordsPerSentance(String text){
		ArrayList <Integer> wordsPerSentance = new ArrayList<Integer>();
		char[] chars = text.toCharArray();
		int wordCount = 0;
		double sum = 0.0;
		for (int i = 0; i < text.length()-1; i++){
			if (!((chars[i] == '.' || chars[i] == '?' || chars[i] == '!') && (chars[i+1] ==' ' || chars[i+1] =='\'' || chars[i+1] =='"'))){
				if (chars[i] == ' '){
					wordCount ++;
				}
			}else{
				if (wordCount > 0){
					wordCount++;
				}
				wordsPerSentance.add(wordCount);
				wordCount = 0;
			}
		}
		for (Integer each: wordsPerSentance){
			sum += each;
		}
		return sum/wordsPerSentance.size();
	}
	
	public static double AvgStrokesPerWord(String text){
		ArrayList <Integer> strokesPerWord = new ArrayList<Integer>();
		char[] chars = text.toCharArray();
		int strokeCount = 0;
		double sum = 0.0;
		for (int i = 0; i < text.length()-1; i++){
			if (chars[i] != ' '){
					strokeCount ++;
			}else{
				strokesPerWord.add(strokeCount);
				strokeCount = 0;
			}
		}
		for (Integer each: strokesPerWord){
			sum += each;
		}
		return sum/strokesPerWord.size();
	}
	
	public static double AvgSyllablesPerWord(String text){
		//Algorithm implemented based on: www.shiffman.net/itp/classes/a2z/week01/FleschIndex.java
		ArrayList <Integer> syllablesPerWord = new ArrayList<Integer>();
		char[] chars = text.toCharArray();
		boolean vowel = false;
		int sylCount = 0;
		double sum = 0.0;
		for (int i = 0; i < text.length()-1; i++){
			if (chars[i] != ' '){
					if (isVowel(chars[i]) && !vowel){
						vowel = true;
						sylCount ++;
					} else if(isVowel(chars[i]) && vowel){
						vowel = true;
					} else{
						vowel = false;
					}
			}else{
				if (((chars[i-1] == 'e') || (chars[i-1] == 'E')) && (sylCount != 1)){
					sylCount --;
				}
				syllablesPerWord.add(sylCount);
				sylCount = 0;
				vowel = false;
			}
		}
		for (Integer each: syllablesPerWord){
			sum += each;
		}
		return sum/syllablesPerWord.size();
	}
	
	public static double SARI(String text){
		//Algorithm implemented based on: http://strainindex.wordpress.com/2011/12/23/simplified-automated-readability-index/
		return (AvgWordsPerSentance(text)*.5 + AvgStrokesPerWord(text)*4.71 - 21.43);
	}
	
	public static double Flesch_KincaideReadingLevel(String text){
		//Algorithm implemented based on: http://juicystudio.com/services/readability.php
		return ((0.39 * AvgWordsPerSentance(text)) + (11.8 * AvgSyllablesPerWord(text)) - 15.9);
	}
	
	private static boolean isVowel(char c) {
		//from: www.shiffman.net/itp/classes/a2z/week01/FleschIndex.java
	    if      ((c == 'a') || (c == 'A')) { return true;  }
	    else if ((c == 'e') || (c == 'E')) { return true;  }
	    else if ((c == 'i') || (c == 'I')) { return true;  }
	    else if ((c == 'o') || (c == 'O')) { return true;  }
	    else if ((c == 'u') || (c == 'U')) { return true;  }
	    else if ((c == 'y') || (c == 'Y')) { return true;  }
	    else                               { return false; }
	  }
	
}
