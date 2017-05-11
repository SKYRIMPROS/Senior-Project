/**
 * 
 */
package ReadingTestGUI;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.Scanner;

/**
 * @author Chris Spooner
 *
 */
public class ReadingSample implements Comparable<ReadingSample>{
	private String name;
	private String text = "";
	private Integer numberOfWords;
	private Double readingLevel;
	public QuestionList quiz;
	public int MIN_PARAGRAPH_WORDS = 1;
	
	public ReadingSample(String fileName, int paragraphes){
		try {
			Scanner sc = new Scanner(new FileInputStream(fileName));
			this.name = sc.nextLine();
			this.text = sc.nextLine()+"\n\n";
			int pCount = 0;
			String line = "";
			
			while (pCount < paragraphes){
				if (sc.hasNextLine()){
					line = sc.nextLine();
					this.text += line + "\n\n";
					if (TextUtils.WordCount(line) >= MIN_PARAGRAPH_WORDS){
						pCount += 1;
					}
				}
			}
			this.numberOfWords = TextUtils.WordCount(this.text);
			this.readingLevel = TextUtils.Flesch_KincaideReadingLevel(this.text);
			sc.close();
			this.quiz = new QuestionList(fileName, 0, paragraphes);
		} catch (Exception e) {
			System.out.println("Could not open file");
		}
	}
	
	public ReadingSample(String fileName, int chapterStart, int chapterEnd){
		try {
			Scanner sc = new Scanner(new FileInputStream(fileName));
			//this.name = sc.nextLine();
			int pCount = 0;
			String line = "";
			System.out.print("opened file: ");
			System.out.println(fileName);
			//Find Starting Paragraph
			while (pCount < chapterStart){
				if (sc.hasNextLine()){
					line = sc.nextLine();
					if (TextUtils.WordCount(line) >= MIN_PARAGRAPH_WORDS){
						pCount += 1;
					}
				}
			}
			
			//Add until End Paragraph
			while (pCount < chapterEnd){
				if (sc.hasNextLine()){
					line = sc.nextLine();
					this.text += line + "\n\n";
					if (TextUtils.WordCount(line) >= MIN_PARAGRAPH_WORDS){
						pCount += 1;
					}
				}
			}
			this.numberOfWords = TextUtils.WordCount(this.text);
			this.readingLevel = TextUtils.Flesch_KincaideReadingLevel(this.text);
			sc.close();
			this.quiz = new QuestionList(fileName, chapterStart, chapterEnd);
		} catch (Exception e) {
			System.out.println("Could not open file");
		}
	}
	
	public String toString(){
		BigDecimal score = new BigDecimal(this.readingLevel);
		score = score.setScale(2,BigDecimal.ROUND_HALF_UP);
		return this.name + "has " + this.numberOfWords.toString() + " and a reading level of: " + score; 
	}
	
	public String getName(){
		return this.name;
	}
	
	public Integer getNumberOfWords(){
		return this.numberOfWords;
	}
	
	public Double getReadingLevel(){
		return this.readingLevel;
	}
	
	public String getReadingLevelText(){
		BigDecimal score = new BigDecimal(this.readingLevel);
		score = score.setScale(2,BigDecimal.ROUND_HALF_UP);
		return score.toString();
	}
	
	public String getRawText(){
		return this.text;
	}
	
	public String getFormattedText(int width){
		String[] numberOfWords = this.text.split(" ");
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

	@Override
	public int compareTo(ReadingSample s) {
		// TODO Auto-generated method stub
		return this.getReadingLevel().compareTo(s.getReadingLevel());
	}
	
}

