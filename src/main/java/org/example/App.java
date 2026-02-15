package org.example;

import org.example.controller.QuizController;
import org.example.model.QuizModel;
import org.example.repository.QuestionRepository;
import org.example.repository.ResultRepository;
import org.example.service.QuestionService;
import org.example.service.ResultService;
import org.example.view.QuizView;

public class App 
{
    public static void main( String[] args ) throws Exception
    {
        ResultRepository resultRepository = new ResultRepository();
        ResultService resultService = new ResultService(resultRepository);

        QuestionRepository questionRepository = new QuestionRepository();
        QuestionService questionService = new QuestionService(questionRepository);
        QuizView quizView = new QuizView();
        QuizModel quizModel = new QuizModel(questionService, resultService);
        QuizController quizController = new QuizController(quizModel, quizView);

        quizController.start();
        quizController.showMenuAndChooseOption();
    }
}
