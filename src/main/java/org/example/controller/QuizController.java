package org.example.controller;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import org.example.entity.Question;
import org.example.entity.Result;
import org.example.model.QuizModel;
import org.example.view.QuizView;

import java.util.List;

public class QuizController {

    private final QuizModel quizModel;
    private final QuizView quizView;
    private List<Question> listOfQuestions;
    private List<Result> listOfResults;
    private int chosenCategory = 0;

    public QuizController(QuizModel quizModel, QuizView quizView) {
        this.quizModel = quizModel;
        this.quizView = quizView;
    }

    private boolean checkForEnter() throws Exception {

        boolean escape = false;
        boolean goFurther = false;

        clearInputBuffer();
        while (!goFurther) {

            KeyStroke keyPressed = quizView.getUserInput();
            if (keyPressed != null) {
                // Sprawdzamy, jaki klawisz został naciśnięty
                System.out.println("Naciśnięty klawisz: " + keyPressed.getKeyType());

                switch (keyPressed.getKeyType()) {
                    case Enter:
                        // Jeśli naciśnięto strzałkę w górę, zakończymy pętlę
                        goFurther = true;

                        quizView.displayLoadingBar();

                        break;
                    case Escape:
                        goFurther = true;
                        escape = true;
                        quizView.exit();
                        break;
                    default:
                        System.out.println("Inny klawisz: " + keyPressed.getKeyType());
                        break;
                }
            }
        }

        if (escape) {
            System.out.println("Aplikacja kończy działanie...");
            System.exit(0); // Zakończenie aplikacji
        }
        return true;
    }

    public void start() throws Exception {

        clearInputBuffer();
        quizView.showWelcomeWindow();

        clearInputBuffer();
        checkForEnter();

    }

    public void showMenuAndChooseOption() throws Exception {

        quizView.clearScreen();
        quizView.showMenuBar();

        // deklaracja opcji MENU
        List<String> buttons = List.of("Start", "Choose the category", "Last results", "Back");
                /*
           0. Rozpocznij grę
           1. Instrukcja gry
           2. Wyjdź
        */

        List<String> listOfCategories = List.of("All", "History", "Sport", "Nature");

        // zmienna przechowująca index opcji do wybrania (domyślnie "Rozpocznij grę")
        // ta opcja będzie się wyróżniać białym tłem
        int selectedOption = 0;

        // zmienna kontrolująca wyjście z pętli
        boolean isOptionChosen = false;

        clearInputBuffer();

        while (!isOptionChosen) {
            // wyświetlenie napisu MENU
            quizView.showMenuBar();

            quizView.displayMenuOptions(buttons, selectedOption);

            quizView.displayChosenCategory(listOfCategories, chosenCategory);

            // pobranie przycisku od użytkownika
            KeyStroke keyPressed = quizView.getUserInput();

            if (keyPressed != null) {

                switch (keyPressed.getKeyType()) {
                    case ArrowUp:   // strzałka w górę przesuwa wybraną opcją w dół po indeksach

                        if (selectedOption > 0) {
                            selectedOption--;
                        } else if (selectedOption == 0) {    // jeśli wybrana opcja jest 0 to wraca na ostatni przycisk
                            selectedOption = buttons.size() - 1;
                        }
                        break;
                    case ArrowDown: //  strzałka w dół przesuwa wybraną opcję w górę po indeksach

                        if (selectedOption < buttons.size() - 1) {
                            selectedOption++;
                        } else if (selectedOption == buttons.size() - 1) {   // jeśli wybrany przycisk jest na ostatniej opcji
                            selectedOption = 0;                             // strzałka w dół przesuwa wybrany przycisk na indeks 0
                        }
                        break;

                    case Enter: // zatwierdza wybraną opcję i wychodzi z pętli
                        // Wybór opcji
                        isOptionChosen = true;  // wyjście z pętli
                        break;
                    default:
                        break;
                }
            }

        } // koniec pętli od przechodzenia po menu strzałkami

//        quizView.displaySelectedOption(selectedOption, buttons);

        switch (selectedOption) {
            case 0 -> startGame(listOfCategories);
            case 1 -> selectCategory(listOfCategories);
            case 2 -> showLastResults();
            case 3 -> exitGame();
        }
    }

    private void showLastResults() throws Exception{

        listOfResults = quizModel.getAllResults();
        boolean areThereResults = !listOfResults.isEmpty();

        quizView.showLastResults(areThereResults, listOfResults);

        clearInputBuffer();
        while (true) {
//            quizView.showLastResults(areThereResults, listOfResults);
            KeyStroke keyStroke = quizView.getUserInput();

            if (keyStroke != null && keyStroke.getKeyType() == KeyType.Enter) {
                // Wyczyść bufor wejściowy
                clearInputBuffer();
                break;
            }
        }

        showMenuAndChooseOption();

    }

    private void selectCategory(List<String> categories) throws Exception{

        // zmienna przechowująca index opcji do wybrania (domyślnie "Rozpocznij grę")
        // ta opcja będzie się wyróżniać białym tłem
        int selectedOption = 0;

        // zmienna kontrolująca wyjście z pętli
        boolean isOptionChosen = false;

        quizView.clearScreen();

        clearInputBuffer();
        while(!isOptionChosen) {

            quizView.displayCategories(categories, selectedOption);

            KeyStroke keyPressed = quizView.getUserInput();

            if (keyPressed != null) {

                switch (keyPressed.getKeyType()) {
                    case ArrowUp:   // strzałka w górę przesuwa wybraną opcją w dół po indeksach

                        if (selectedOption > 0) {
                            selectedOption--;
                        } else if (selectedOption == 0) {    // jeśli wybrana opcja jest 0 to wraca na ostatni przycisk
                            selectedOption = categories.size() - 1;
                        }
                        break;
                    case ArrowDown: //  strzałka w dół przesuwa wybraną opcję w górę po indeksach

                        if (selectedOption < categories.size() - 1) {
                            selectedOption++;
                        } else if (selectedOption == categories.size() - 1) {   // jeśli wybrany przycisk jest na ostatniej opcji
                            selectedOption = 0;                             // strzałka w dół przesuwa wybrany przycisk na indeks 0
                        }
                        break;

                    case Enter: // zatwierdza wybraną opcję i wychodzi z pętli
                        // Wybór opcji
                        isOptionChosen = true;  // wyjście z pętli
                        chosenCategory = selectedOption;
                        break;
                    default:
                        break;
                }
            }
        }


        showMenuAndChooseOption();
    }

    private void startGame(List<String> categories) throws Exception {

        // pobranie listy pytań w zależności od kategorii
        if(chosenCategory == 0) {
            listOfQuestions = quizModel.getRandomQuestions();
        } else {
            listOfQuestions = quizModel.getQuestionsByCategory(categories.get(chosenCategory));
        }
        // quizView.startGame(listOfQuestions);

        // lista odpowiedzi do danego pytania
        List<String> answers;
        // wybrana odpowiedź, pobrana od użytkownika
        int selectedAnswer = 0;
        // czy odpowiedź użytkownika jest poprawna
        boolean isCorrect;

        // zmienna to oznaczania podświetlonej odpowiedzi do wybrania
        int selectedOption = 0;
        boolean isOptionChosen = false;

        int pointsCounter = 0;
        int numberOfQuestion = 1;
        TerminalSize terminalSize;
        for (Question question : listOfQuestions) {
            // pobranie listy odpowiedzi do pytania
            answers = question.getAnswers();
            // wyświetlenie pytania
            quizView.displayQuestion(question, numberOfQuestion);

            selectedOption = 0;
            isOptionChosen = false;

            clearInputBuffer();
            while (!isOptionChosen) {
                // wyświetlenie wszystkich odpowiedzi, parametry to lista odpowiedzi do wyświetlenia i wybrana opcja (do oznaczenia podświetlanej odpowiedzi)
                quizView.displayAnswers(answers, selectedOption);

                // pobranie przycisku od użytkownika
                KeyStroke keyPressed = quizView.getUserInput();

                if (keyPressed != null) {

                    switch (keyPressed.getKeyType()) {
                        case ArrowUp:   // strzałka w górę przesuwa wybraną opcją w dół po indeksach

                            if (selectedOption > 0) {
                                selectedOption--;
                            } else if (selectedOption == 0) {    // jeśli wybrana opcja jest 0 to wraca na ostatni przycisk
                                selectedOption = answers.size() - 1;
                            }
                            break;
                        case ArrowDown: //  strzałka w dół przesuwa wybraną opcję w górę po indeksach

                            if (selectedOption < answers.size() - 1) {
                                selectedOption++;
                            } else if (selectedOption == answers.size() - 1) {   // jeśli wybrany przycisk jest na ostatniej opcji
                                selectedOption = 0;                             // strzałka w dół przesuwa wybrany przycisk na indeks 0
                            }
                            break;

                        case Enter: // zatwierdza wybraną opcję i wychodzi z pętli
                            // Wybór opcji
                            isOptionChosen = true;  // wyjście z pętli
                            selectedAnswer = selectedOption;
                            break;
                        default:
                            break;
                    }
                }

            }

            isCorrect = quizModel.checkAnswerCorrectness(question, selectedAnswer);

            if (isCorrect) {
                pointsCounter++;
                quizView.showCorrectMessage();
            } else {

                quizView.displayWrongMessage(question, selectedAnswer);
            }

            waitForEnter();
            numberOfQuestion++;
        }

        Result result = new Result(pointsCounter, categories.get(chosenCategory));
        quizModel.addResult(result);

        showAndSaveScore(pointsCounter);

        showMenuAndChooseOption();
    }

    private void showAndSaveScore(int points) throws Exception {
        quizView.showScore(points);
        goBackToMenu();
    }

    private void goBackToMenu() throws Exception {
        while (true) {
            quizView.displayBackToMenu(); // Wyświetlenie przycisku "Dalej"
            KeyStroke keyStroke = quizView.getUserInput();

            if (keyStroke != null && keyStroke.getKeyType() == KeyType.Enter) {
                clearInputBuffer();
                break;
            }
        }
    }



    private void waitForEnter() throws Exception {
        while (true) {
            quizView.displayNextButton(); // Wyświetlenie przycisku "Dalej"
            KeyStroke keyStroke = quizView.getUserInput();

            if (keyStroke != null && keyStroke.getKeyType() == KeyType.Enter) {
                // Wyczyść bufor wejściowy
                clearInputBuffer();
                break;
            }
        }

        // Opcjonalne krótkie opóźnienie, aby uniknąć przypadkowych przeskoków
        Thread.sleep(100);
    }

    private void clearInputBuffer() throws Exception {
        while (quizView.getUserInput() != null) {

        }
    }

    private void exitGame() throws Exception {
        quizView.exit(); // wyjście z aplikacji
    }

}