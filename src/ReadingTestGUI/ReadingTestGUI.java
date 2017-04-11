/**
 * 
 */
package ReadingTestGUI;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * @author Chris Spooner
 *
 */
public class ReadingTestGUI extends JFrame 
							implements ActionListener, KeyListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4959995150339388118L;
	
	ArrayList<ReadingSample> passages = new ArrayList<ReadingSample>();
	int passageIndex = 0;
	long start;
	long elapsedTimeMillis;
	Double score;
	Double wpm;
	
	
	private JLabel welcomeHeadingLabel, instructionsLabel,
	resultsLabel, titleLabel, wordCountLabel, readingLevelLabel,
	wpmLabel, scoreLabel;
	private ArrayList<JLabel> questions = new ArrayList<JLabel>();
	private ArrayList<JRadioButton> options = new ArrayList<JRadioButton>();
	private ArrayList<JTextField> answers = new ArrayList<JTextField>();
	private JButton beginButton, startTestButton, doneButton, resultsButton, againButton, quitButton;
	private JTextArea passageText;
	private JPanel base;
	private CardLayout window;
	
	public ReadingTestGUI(){
		//Populate list of passages
		this.passages.add(new ReadingSample("bookOne", 1, 6));
		this.passages.add(new ReadingSample("alice_in_wonderland", 1, 5));
		Collections.sort(this.passages);
		int minQuestions = 10;
		for(ReadingSample each : passages){
			if(each.quiz.questions.size() < minQuestions){
				minQuestions = each.quiz.questions.size();
			}
		}
		
		
		// Window
		setTitle("Book Reader");
		setSize(550,(200+(this.passages.size()*20)) );
		setMinimumSize(new Dimension(275, 100));
		centerWindow(this);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//-----Welcome------
		final String WELCOMEPANEL = "Welcome"; 
		JPanel welcomePanel = new JPanel();
		welcomePanel.setLayout(new GridLayout(3, 1));
		//Heading
		welcomeHeadingLabel = new JLabel("Please choose one of the following chapters:");
		welcomePanel.add(welcomeHeadingLabel);
		//Passages
		JPanel optionsPanel = new JPanel();
		ButtonGroup optionsGroup = new ButtonGroup();
		optionsPanel.setLayout(new GridLayout(this.passages.size(), 1));
		optionsPanel.setBorder(BorderFactory.createTitledBorder("Chapters"));
		options.add(new JRadioButton("Chapter 1"));
		options.add(new JRadioButton("Chapter 2"));
		for (JRadioButton each : options){
			each.addActionListener(this);
			optionsGroup.add(each);
			optionsPanel.add(each);
		}
		welcomePanel.add(optionsPanel);
		//Begin Button
		JPanel beginPanel = new JPanel();
		beginButton = new JButton("Begin");
		beginButton.setEnabled(false);
		beginButton.addActionListener(this);
		beginPanel.add(beginButton);
		welcomePanel.add(beginPanel);
		
		
		//--Instructions---
		/*
		final String INSTRUCTIONSPANEL = "Instructions";
		JPanel instructionsPanel = new JPanel();
		instructionsPanel.setLayout(new GridLayout(2,1));
		JPanel instructionLabelPanel = new JPanel();
		instructionsLabel = new JLabel(TextUtils.FormatTextHTML("When you click begin you will be presented with a passage of text." +
				"Once you finish reading the passage please click the 'Done' button, which will bring you to a quiz on the passage you just read. \n<br> " +
				"You will be scored on both your reading speed and the questions Click the 'Start' button when you are ready to begin.", 60));
		instructionLabelPanel.add(instructionsLabel);
		instructionsPanel.add(instructionLabelPanel);
		JPanel instructionsButtonPanel = new JPanel();
		startTestButton = new JButton("Start");
		startTestButton.setPreferredSize(new Dimension(325,50));
		startTestButton.addActionListener(this);
		instructionsButtonPanel.add(startTestButton);
		instructionsPanel.add(instructionsButtonPanel);
		*/
		
		//---Read Passage--
		final String PASSAGEPANEL = "Passage";
		JPanel passagePanel = new JPanel();
		passagePanel.setLayout(new GridLayout(2,1));
		JPanel passageTextPanel = new JPanel();
		passageText = new JTextArea(this.passages.get(passageIndex).getFormattedText(70));
		passageTextPanel.add(passageText);
		passageTextPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		passagePanel.add(passageTextPanel);
		JPanel doneButtonPanel = new JPanel();
		doneButton = new JButton("Done");
		doneButton.setPreferredSize(new Dimension(325,50));
		doneButton.addActionListener(this);
		doneButtonPanel.add(doneButton);
		passagePanel.add(doneButtonPanel);
		
		//---Quiz---
		final String QUIZPANEL = "Quiz";
		JPanel quizPanel = new JPanel();
		quizPanel.setLayout(new GridLayout(2,1));
		JPanel questionsPanel = new JPanel();
		questionsPanel.setLayout(new GridLayout(minQuestions, 1));
		for(ComprehensionQuestion each : this.passages.get(this.passageIndex).quiz.questions){
			if(questions.size() >= minQuestions){
				break;
			}
			JPanel qa = new JPanel();
			qa.setLayout(new GridLayout(2,1));
			JLabel q = new JLabel(each.askQuestion());
			questions.add(q);
			this.answers.add(new JTextField());
			qa.add(q);
			qa.add(this.answers.get(this.answers.size()-1));
			questionsPanel.add(qa);
		}
		quizPanel.add(questionsPanel);
		JPanel resultsButtonPanel = new JPanel();
		resultsButton = new JButton("Get Results");
		resultsButton.setPreferredSize(new Dimension(325,50));
		resultsButton.addActionListener(this);
		resultsButtonPanel.add(resultsButton);
		quizPanel.add(resultsButtonPanel);
		
		//---Results---
		final String RESULTSPANEL = "Results";
		JPanel resultsPanel = new JPanel();
		resultsPanel.setLayout(new GridLayout(3,1));
		JPanel resultsLabelPanel = new JPanel();
		resultsLabel = new JLabel("Your Test Results:");
		resultsLabelPanel.add(resultsLabel);
		resultsPanel.add(resultsLabelPanel);
		JPanel numberPanel = new JPanel();
		numberPanel.setLayout(new GridLayout(5,1));
		numberPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		titleLabel = new JLabel(this.passages.get(this.passageIndex).getName());
		numberPanel.add(titleLabel);
		JPanel wordCountPanel = new JPanel();
		wordCountPanel.setLayout(new GridLayout(1,2));
		JLabel wordCountTitleLabel = new JLabel("Word Count:");
		wordCountPanel.add(wordCountTitleLabel);
		wordCountLabel = new JLabel(this.passages.get(this.passageIndex).getNumberOfWords().toString());
		wordCountPanel.add(wordCountLabel);
		numberPanel.add(wordCountPanel);
		JPanel readingLevelPanel = new JPanel();
		readingLevelPanel.setLayout(new GridLayout(1,2));
		JLabel readingLevelTitleLabel = new JLabel("Reading Level:");
		readingLevelPanel.add(readingLevelTitleLabel);
		readingLevelLabel = new JLabel(this.passages.get(this.passageIndex).getReadingLevel().toString());
		readingLevelPanel.add(readingLevelLabel);
		numberPanel.add(readingLevelPanel);
		JPanel wpmPanel = new JPanel();
		wpmPanel.setLayout(new GridLayout(1,2));
		JLabel wpmTitleLabel = new JLabel("WPM:");
		wpmPanel.add(wpmTitleLabel);
		wpmLabel = new JLabel(this.passages.get(this.passageIndex).getReadingLevel().toString());
		wpmPanel.add(wpmLabel);
		numberPanel.add(wpmPanel);
		JPanel scorePanel = new JPanel();
		scorePanel.setLayout(new GridLayout(1,2));
		JLabel scoreTitleLabel = new JLabel("Score:");
		scorePanel.add(scoreTitleLabel);
		scoreLabel = new JLabel(this.passages.get(this.passageIndex).getReadingLevel().toString());
		scorePanel.add(scoreLabel);
		numberPanel.add(scorePanel);
		resultsPanel.add(numberPanel);
		JPanel finnishedButtonPanel = new JPanel();
		finnishedButtonPanel.setLayout(new GridLayout(1,2));
		againButton = new JButton("Start Over");
		againButton.setPreferredSize(new Dimension(75,15));
		againButton.addActionListener(this);
		finnishedButtonPanel.add(againButton);
		quitButton = new JButton("Quit");
		quitButton.addActionListener(this);
		quitButton.setPreferredSize(new Dimension(75,15));
		finnishedButtonPanel.add(quitButton);
		resultsPanel.add(finnishedButtonPanel);
		
		//---Card Holder----
		JPanel resultsBase = new JPanel();
		resultsBase.add(resultsPanel);
		JPanel quizBase = new JPanel();
		quizBase.add(quizPanel);
		JPanel passageBase = new JPanel();
		passageBase.add(passagePanel);
		//JPanel instructionsBase = new JPanel();
		//instructionsBase.add(instructionsPanel);
		JPanel welcomeBase = new JPanel();
		welcomeBase.add(welcomePanel);
		
		base = new JPanel();
		base.setLayout(new CardLayout());
		base.add(welcomeBase, WELCOMEPANEL);
		//base.add(instructionsBase, INSTRUCTIONSPANEL);
		base.add(passageBase, PASSAGEPANEL);
		base.add(quizBase, QUIZPANEL);
		base.add(resultsBase, RESULTSPANEL);
		this.add(base);

		window = (CardLayout) (base.getLayout());
		window.first(base);
		
		
	}
	
	private void centerWindow(Window w) {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		setLocation((d.width - w.getWidth()) / 2,
				(d.height - w.getHeight()) / 2);
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		for (JRadioButton each : this.options){
			if (e.getSource() == each){
				this.beginButton.setEnabled(true);
				this.passageIndex = this.options.indexOf(each);
				
			}
		}
		/*
		if (e.getSource() == this.beginButton){
			//System.out.println("h");
			this.window.next(this.base);	
			this.setSize(400, 240);
			this.centerWindow(this);
			this.passageText.setText(this.passages.get(this.passageIndex).getFormattedText(70));
		}
		*/
		if (e.getSource() == this.beginButton){   //this.startTestButton){
			this.window.next(this.base);	
			this.setSize(450, this.passageText.getHeight()+125);
			this.centerWindow(this);
			start = System.currentTimeMillis();
		}
		
		if (e.getSource() == this.doneButton){
			this.window.next(this.base);	
			this.setSize(575, 85+(this.answers.size()*50));
			this.centerWindow(this);
			elapsedTimeMillis = System.currentTimeMillis()-start;
			double elapsedTimeMin = elapsedTimeMillis/1000F/60;
//			System.out.println(this.passages.size());
//			System.out.println(this.passageIndex);
			this.wpm = this.passages.get(this.passageIndex).getNumberOfWords()/elapsedTimeMin;
			for (int i=0; i<this.questions.size(); i++){
				questions.get(i).setText(this.passages.get(this.passageIndex).quiz.questions.get(i).askQuestion());
			}
			
		}
		
		if (e.getSource() == this.resultsButton){
			this.score = this.checkAnswers();
			this.window.next(this.base);	
			this.setSize(200, 300);
			this.centerWindow(this);
			this.titleLabel.setText(this.passages.get(this.passageIndex).getName());
			this.wordCountLabel.setText(this.passages.get(this.passageIndex).getNumberOfWords().toString());
			this.readingLevelLabel.setText(this.passages.get(this.passageIndex).getReadingLevelText());
			BigDecimal wpmText = new BigDecimal(this.wpm);
			wpmText = wpmText.setScale(2,BigDecimal.ROUND_HALF_UP);
			this.wpmLabel.setText(wpmText.toString());
			BigDecimal scoreText = new BigDecimal(this.score);
			scoreText = scoreText.setScale(2,BigDecimal.ROUND_HALF_UP);
			this.scoreLabel.setText(scoreText.toString());		
		}
			
		if (e.getSource() == this.againButton){
			this.window.next(this.base);
			setSize(550,(200+(this.passages.size()*20)) );
			this.centerWindow(this);
		} 
		
		if (e.getSource() == quitButton) {
				System.exit(0);
		}
		
	}


	private Double checkAnswers(){
		double newScore = 0;
		for (int i=0; i<this.questions.size(); i++){
			String a = this.answers.get(i).getText();
			if (this.passages.get(passageIndex).quiz.questions.get(i).checkAnswer(a)){
				newScore++;}
		}
		return newScore/this.questions.size();
	}
	
	
}
