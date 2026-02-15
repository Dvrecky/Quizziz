package org.example.model;

import org.example.entity.Question;
import org.example.entity.Result;
import org.example.service.QuestionService;
import org.example.service.ResultService;

import java.util.List;

public class QuizModel {

    private final ResultService resultService;
    private final QuestionService questionService;

    public QuizModel(QuestionService questionService, ResultService resultService ) {
        this.questionService = questionService;
        this.resultService = resultService;
    }

    public List<Question> getRandomQuestions() {
        return this.questionService.getRandomQuestions();
    }

    public List<Question> getQuestionsByCategory(String category) {
        return this.questionService.getQuestionsByCategory(category);
    }

    public boolean checkAnswerCorrectness(Question question, int answer) {
        return this.questionService.checkAnswerCorrectness(question, answer);
    }

    public void addResult(Result result) {
        this.resultService.addResult(result);
    }

    public List<Result> getAllResults() {
        return this.resultService.getAllResults();
    }
}
