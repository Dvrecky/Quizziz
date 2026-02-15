package org.example.service;

import org.example.entity.Question;
import org.example.repository.QuestionRepository;

import java.util.List;

public class QuestionService {

     private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public List<Question> getQuestionsByCategory(String category) {
        return this.questionRepository.getQuestionsByCategory(category);
    }

    public List<Question> getRandomQuestions() {
        return this.questionRepository.getRandomQuestions();
    }

    public boolean checkAnswerCorrectness(Question question, int answer) {
        return question.checkIfCorrect(answer);
    }
}
