package ReadingTestGUI;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Chris Spooner
 *
 */
public class QuestionList {
	public ArrayList<ComprehensionQuestion> questions;
	
	public QuestionList(String fileName, int paragraphStart, int paragraphEnd){
		questions = new ArrayList<ComprehensionQuestion>();
		ArrayList<Integer> paras = new ArrayList<Integer>();
		for(int i = paragraphStart; i < paragraphEnd; i++){
			paras.add(i);
		}
		try {
			Scanner sc = new Scanner(new FileInputStream(fileName + "_questions"));
			String question = "";
			String answer = "";
			while (sc.hasNextLine()){
				int num = sc.nextInt();
				String qa = sc.next();
				String line = sc.nextLine();
				if (paras.contains(num)){
					if (qa.equals("Q")){
						question = line;
					} else if (qa.equals("A")){
						answer = line;
					}
					if ((question.length() != 0) && (answer.length() !=0)){
						ComprehensionQuestion newQuestion = new ComprehensionQuestion(question, answer);
						this.questions.add(newQuestion);
						question = "";
						answer = "";
					}
				}
			}
			sc.close();
		}catch (Exception e) {
			System.out.println("Could not open questions file");
		}
	}
	
	public double takeQuiz(){
		double score = 0;
		Scanner sc = new Scanner(System.in);
		for (ComprehensionQuestion each : this.questions){
			System.out.print(each.askQuestion());
			if (each.checkAnswer(sc.nextLine())){
				score++;
			}
			System.out.println();
		}
		return score/this.questions.size();
	}
	
	public String getQuestions(){
		String outStr = "";
		for (ComprehensionQuestion each : this.questions){
			outStr += each.askQuestion() + "\n";
		}
		return outStr;
	}
	
	public int size(){
		return this.questions.size();
	}
	
	public String toString(){
		String outStr = "";
		for (ComprehensionQuestion each : this.questions){
			outStr += each.toString() + "\n\n";
		}
		return outStr;
	}
}
