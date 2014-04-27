package com.ngm.explaintome.data;

import java.util.ArrayList;
import java.util.List;

public class Question extends ModelElement {
	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String type) {
		this.questionType = type;
	}

	public List<Answer> getAnswers() {
		return options;
	}

	public void setAnswers(List<Answer> answers) {
		this.options = answers;
	}

	public Answer getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(Answer rightAnswer) {
		this.correctAnswer = rightAnswer;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	private String questionType;
	private List<Answer> options = new ArrayList<Answer>();
	private Answer correctAnswer;
	private long timestamp;
	private String text;
}
