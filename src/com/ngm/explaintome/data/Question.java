package com.ngm.explaintome.data;

import java.util.List;

public class Question extends ModelElement {
	public QuestionType getType() {
		return type;
	}

	public void setType(QuestionType type) {
		this.type = type;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
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

	private QuestionType type;
	private List<Answer> answers;
	private Answer correctAnswer;
	private long timestamp;
	private String text;
}
