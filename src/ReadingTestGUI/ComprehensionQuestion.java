/**
 * 
 */
package ReadingTestGUI;

import java.util.ArrayList;

/**
 * @author Chris Spooner
 *
 */
public class ComprehensionQuestion{
	private String question;
	private ArrayList<String> correctAnswers;
	
	public ComprehensionQuestion(String question, String answer){
		this.question = question;
		this.correctAnswers = new ArrayList<String>();
		String[] answers = answer.split(" / ");
		for (String each : answers){
			this.correctAnswers.add(each.trim());
		}
	}
	
	public boolean checkAnswer(String answer){
		for (String each : this.correctAnswers){
			if (answer.toLowerCase().contains(each)){
				return true;
			}
		}
		return false;
	}
	
	public String askQuestion(){
		return this.question+" ";
	}
	
	public String getCorrectAnswers(){
		String outStr = "";
		for (int i = 0; i < this.correctAnswers.size()-2; i++){
			outStr += this.correctAnswers.get(i) + ", "; 
		}
		if (this.correctAnswers.size() == 2){
			outStr = this.correctAnswers.get(0) + " and " + this.correctAnswers.get(1);
		} else if (this.correctAnswers.size() == 2){
			outStr = this.correctAnswers.get(0);
		} else {
			outStr += " and " + this.correctAnswers.get(this.correctAnswers.size()-1);
		}
		return outStr;
	}
	
	public String toString(){
		return askQuestion() + "\n\t" + getCorrectAnswers(); 
	}
}
