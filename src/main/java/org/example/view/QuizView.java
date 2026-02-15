package org.example.view;

import com.googlecode.lanterna.*;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import org.example.entity.Question;
import org.example.entity.Result;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class QuizView {

    private final Terminal terminal;
    private final Screen screen;
    private final TextGraphics tg;
    private TerminalSize terminalSize;

    public QuizView() throws Exception{
        this.terminal = new DefaultTerminalFactory().createTerminal();
        this.screen = new TerminalScreen(terminal);
        this.tg = screen.newTextGraphics();
    }

    public void closeScreen() throws Exception{
        screen.close();
    }

    private void updateTerminalSize() throws Exception {
        TerminalSize newSize = screen.doResizeIfNecessary(); // Sprawdza, czy rozmiar się zmienił
        if (newSize != null) {
            terminalSize = newSize;
            screen.clear();
        }
    }

    // metoda zwraca rozmiar terminala
    public TerminalSize getTerminalSize() throws Exception{
        return this.terminalSize;
    }

    private void displayWelcomeText(TerminalSize terminalSize) throws Exception{

        // ustalenie pozycji dla welcomeText
        String welcomeText = "Welcome in Quiz game";
        int centerColumn1 = (terminalSize.getColumns() - welcomeText.length()) / 2;
        int centerRow1 = terminalSize.getRows() / 2;

        tg.putString(centerColumn1, centerRow1, welcomeText, SGR.BOLD);
        Thread.sleep(500);
        screen.refresh();
    }

    private void displayEnterText(TerminalSize terminalSize) throws Exception {

        String enterText = "Press ENTER to go to Menu";
        int centerColumn2 = (terminalSize.getColumns() - enterText.length()) / 2;
        int centerRow2 = terminalSize.getRows() - terminalSize.getRows()/4;

        tg.putString(centerColumn2, centerRow2, enterText, Arrays.asList(SGR.BOLD, SGR.BLINK));
        Thread.sleep(500);
        screen.refresh();
    }

    public void showLastResults(boolean areThereResults, List<Result> results) throws Exception{

        updateTerminalSize();
        hold(500);
        screen.clear();

        if(!areThereResults) {
            String noResultsMessage = "There are no results";
            String anotherMessage = "You can play a game and then comeback to see results";

            int x1 = (getTerminalSize().getColumns() - noResultsMessage.length() ) / 2;
            int y1 = (getTerminalSize().getRows()) / 2;
            int x2 = (getTerminalSize().getColumns() - anotherMessage.length()) / 2;

            tg.putString(x1, y1, noResultsMessage, SGR.BOLD);
            tg.putString(x2, y1 + 2, anotherMessage, SGR.BOLD);
            screen.refresh();
        } else {

            String yourResultsMessage = "List of your previous results";
            int x = (getTerminalSize().getColumns() - yourResultsMessage.length()) / 2;
            int y = getTerminalSize().getRows() / 10;
            tg.putString(x, y, yourResultsMessage, SGR.BOLD);
            screen.refresh();

            Thread.sleep(300);

            // Rysowanie ramki
            int boxWidth = getTerminalSize().getColumns() - 2; // szerokość ramki
            int boxHeight = Math.min(results.size(), 10) + 3; // wysokość ramki na podstawie liczby wyników (max 10)

            tg.enableModifiers(SGR.BOLD);
            // Rysowanie góry ramki
            tg.putString(1, y + 1, "+" + "-".repeat(boxWidth - 2) + "+");

            // Rysowanie wyników w ramce
            for (int i = 0; i < results.size() && i < 10; i++) {
                String singleScore = "Score: " + results.get(i).getScoredPoints() + "/10" + " in category: " + results.get(i).getCategory();
                x = (getTerminalSize().getColumns() - singleScore.length() ) / 2;
                tg.putString(x, y + 2 + i, "|" + " ".repeat(boxWidth - 2) + "|"); // puste miejsce w ramce
                tg.putString(x, y + 2 + i,  singleScore + " ".repeat(boxWidth - singleScore.length() - 2) + "|");
            }

            // Rysowanie dołu ramki
            tg.putString(1, y + 2 + results.size(), "+" + "-".repeat(boxWidth - 2) + "+");

            tg.disableModifiers(SGR.BOLD);
            screen.refresh();
        }

        hold(500);

        displayBackToMenu();
        screen.clear();
    }


    public void displayMenuOptions(List<String> buttons, int selectedOption) throws Exception {

        updateTerminalSize();
        // pierwsza opcja zostanie wyświetlona mniej więcej na 4/5 wysokości terminala
        int y = (getTerminalSize().getRows() / 5) * 4;

        // zmienna wypisuje opcje Menu
        // przy pierwszym wykonaniu wyświetli pierwszą opcję z białym tłem
        for(int i = 0; i < buttons.size(); i++) {
            if (i == selectedOption) {  // jeśli indeks "buttons" jest równy wybranej opcji (domyślnie 0)
                tg.setBackgroundColor(TextColor.ANSI.WHITE);
                tg.setForegroundColor(TextColor.ANSI.BLACK);
            } else {
                tg.setBackgroundColor(TextColor.ANSI.BLACK);
                tg.setForegroundColor(TextColor.ANSI.WHITE);
            }
            tg.putString(1, y + i, buttons.get(i), SGR.BOLD); // dodaje przycisk (jeden pod drugim)
        }

        // wyświetlenie wszystkich opcji
        screen.refresh();
    }

    public void exit() throws Exception {
        hold(1000);
        closeScreen();
        System.exit(0);
    }

    private void hold(int millis) throws Exception{
        Thread.sleep(millis);
    }

    public KeyStroke getUserInput() throws Exception{
        KeyStroke keyStroke = terminal.pollInput();
        return keyStroke;
    }

    public void displayLoadingBar() throws Exception {

        TerminalSize sizeOfTheTerminal = getTerminalSize();

        hold(100);

        tg.setForegroundColor(TextColor.ANSI.MAGENTA);
        Random random = new Random();
        for(int i = 20; i < sizeOfTheTerminal.getColumns()-20; i++) {
            tg.putString(i, sizeOfTheTerminal.getRows()-2, String.valueOf(Symbols.BLOCK_SOLID));
            hold(random.nextInt(100) + 50);
            screen.refresh();
        }
        tg.setForegroundColor(TextColor.ANSI.DEFAULT);
        screen.refresh();
        hold(1000);
        screen.clear();
    }

    public void displaySelectedOption(int selectedOption, List<String> buttons) throws Exception {
        Thread.sleep(500);
        updateTerminalSize();
        screen.clear();

        // ponowne ustawienie koloru tła ekranu na czarny oraz koloru czcionki na biały
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.setBackgroundColor(TextColor.ANSI.BLACK);
        tg.fill(' ');

        // wyświetlenie wybranego przycisku
        String option = buttons.get(selectedOption);
        int col = (getTerminalSize().getColumns() - option.length() ) / 2;
        int row = (getTerminalSize().getRows()/2) - 1;

        tg.putString(col, row, option, SGR.BOLD);
        screen.refresh();

        Thread.sleep(1500);

        // jeśli wybrana opcja to Rozpocznij grę
        if(selectedOption == 0){
            screen.clear();
            screen.refresh();
        } else if (selectedOption == buttons.size()-1) {
            screen.close();
        }
    }

    public void showMenuBar() throws Exception{

        updateTerminalSize();


        // pobranie rozmiaru terminala
        TerminalSize sizeOfTheTerminal = getTerminalSize();

        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.setBackgroundColor(TextColor.ANSI.BLACK);

        // wyświetlenie napisu MENU
        drawMenuAnimated(screen, tg, (sizeOfTheTerminal.getRows()/5));
    }

    public void displayChosenCategory(List<String> listOfCategories, int chosenCategory) throws Exception{

        updateTerminalSize();
        tg.setBackgroundColor(TextColor.ANSI.BLACK);
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        String category = "Chosen category: " + listOfCategories.get(chosenCategory);
        tg.putString(1, 1, category, SGR.BOLD);
        screen.refresh();
    }

    public void showWelcomeWindow() throws Exception {

        terminalSize = screen.getTerminalSize();

        screen.startScreen();
        updateTerminalSize();
        // pobranie rozmiaru terminala
        TerminalSize sizeOfTheTerminal = getTerminalSize();
        Thread.sleep(1000);

        // schowanie kursora
        screen.setCursorPosition(null);
        // Rysowanie napisu "Quiz" stopniowo
        drawQuizAnimated(screen, tg, (sizeOfTheTerminal.getRows() / 5));

        // wyświetlenie WelcomeText oraz EnterText, przekazanie rozmiaru terminala
        displayWelcomeText(sizeOfTheTerminal);
        displayEnterText(sizeOfTheTerminal);
    }

    public void displayQuestion(Question question, int numberOfQuestion) throws Exception {

        hold(500);
        updateTerminalSize();
        screen.clear();


        // ponowne ustawienie koloru tła ekranu na czarny oraz koloru czcionki na biały
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.setBackgroundColor(TextColor.ANSI.BLACK);
        tg.fill(' ');

        String questionText = question.getText();
        int col = (getTerminalSize().getColumns() - questionText.length() ) / 2;
        int row = getTerminalSize().getRows() / 2;
        tg.putString(col, row, questionText, SGR.BOLD);

        tg.putString(1,1, "Pytanie: " + numberOfQuestion + "/10", SGR.BOLD);

        screen.refresh();
    }

    public int getSelectedAnswer(List<String> answers) throws Exception {
        int selectedAnswer = 0;
        boolean isAnswerChosen = false;

        while(!isAnswerChosen) {

            updateTerminalSize();
            displayAnswers(answers, selectedAnswer);

            KeyStroke keyPressed = getUserInput();
            if (keyPressed != null) {
                switch (keyPressed.getKeyType()) {
                    case ArrowUp:
                        selectedAnswer = (selectedAnswer > 0) ? selectedAnswer - 1 : answers.size() - 1;
                        break;
                    case ArrowDown:
                        selectedAnswer = (selectedAnswer < answers.size() - 1) ? selectedAnswer + 1 : 0;
                        break;
                    case Enter:
                        isAnswerChosen = true;
                        break;
                    default:
                        break;
                }
            }
        }
        return selectedAnswer;
    }

    public void clearScreen() throws Exception {
        screen.clear();
    }

    public void displayAnswers(List<String> answers, int selectedAnswer) throws Exception {

        updateTerminalSize();
        int col = (getTerminalSize().getColumns() - answers.get(0).length() ) / 2;
        List<Character> characters = List.of('A', 'B', 'C', 'D');
        for (int i = 0; i < answers.size(); i++) {
            if (i == selectedAnswer) {
                tg.setBackgroundColor(TextColor.ANSI.WHITE);
                tg.setForegroundColor(TextColor.ANSI.BLACK);
            } else {
                tg.setBackgroundColor(TextColor.ANSI.BLACK);
                tg.setForegroundColor(TextColor.ANSI.WHITE);
            }
            tg.putString(col, ((getTerminalSize().getRows()/5) * 4) + i, characters.get(i) + ". " + answers.get(i));
        }
        screen.refresh();
    }

//    public void displayCategories(List<String> categories, int chosenCategory) throws Exception{
//
//        updateTerminalSize();
//        // pierwsza opcja zostanie wyświetlona mniej więcej na 4/5 wysokości terminala
//        int x = (getTerminalSize().getColumns() - categories.get(0).length()) / 2;
//        int y = (getTerminalSize().getRows() / 2);
//
//        // zmienna wypisuje opcje Menu
//        // przy pierwszym wykonaniu wyświetli pierwszą opcję z białym tłem
//        for(int i = 0; i < categories.size(); i++) {
//            if (i == chosenCategory) {  // jeśli indeks "buttons" jest równy wybranej opcji (domyślnie 0)
//                tg.setBackgroundColor(TextColor.ANSI.WHITE);
//                tg.setForegroundColor(TextColor.ANSI.BLACK);
//            } else {
//                tg.setBackgroundColor(TextColor.ANSI.BLACK);
//                tg.setForegroundColor(TextColor.ANSI.WHITE);
//            }
//
//            tg.putString(x, y + i, categories.get(i), SGR.BOLD); // dodaje przycisk (jeden pod drugim)
//        }
//        // wyświetlenie wszystkich opcji
//        screen.refresh();
//    }

    public void displayCategories(List<String> categories, int chosenCategory) throws Exception {

        updateTerminalSize();

        tg.setBackgroundColor(TextColor.ANSI.BLACK);
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        drawCategories(screen, tg, getTerminalSize().getRows() / 8 ); // "5" to wiersz, na którym napis się pojawi


        // Obliczanie wymiarów ramki
        int maxCategoryLength = categories.stream()
                .mapToInt(String::length)
                .max()
                .orElse(0);

        int padding = 4; // Dodatkowa przestrzeń po lewej i prawej stronie tekstu
        int frameWidth = maxCategoryLength + padding;
        int frameHeight = categories.size() + 2; // Dwie dodatkowe linie na górę i dół ramki

        int startX = (getTerminalSize().getColumns() - frameWidth) / 2;
        int startY = (getTerminalSize().getRows() / 2);

        // Rysowanie ramki
        drawFrame(startX, startY, frameWidth, frameHeight);

        // Wyświetlanie kategorii wewnątrz ramki
        for (int i = 0; i < categories.size(); i++) {
            if (i == chosenCategory) {  // Jeśli indeks odpowiada wybranej kategorii
                tg.setBackgroundColor(TextColor.ANSI.WHITE);
                tg.setForegroundColor(TextColor.ANSI.BLACK);
            } else {
                tg.setBackgroundColor(TextColor.ANSI.BLACK);
                tg.setForegroundColor(TextColor.ANSI.WHITE);
            }

            // Wycentrowanie tekstu kategorii w ramce
            int categoryX = startX + 2; // Lewa granica ramki + odstęp
            int categoryY = startY + 1 + i; // Górna granica ramki + przesunięcie

            tg.putString(categoryX, categoryY, categories.get(i), SGR.BOLD);
        }

        screen.refresh();
    }

    // Metoda pomocnicza do rysowania ramki
    private void drawFrame(int startX, int startY, int width, int height) {
        // Górna linia
        tg.setBackgroundColor(TextColor.ANSI.BLACK);
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.putString(startX, startY, "+" + "-".repeat(width - 2) + "+", SGR.BOLD);

        // Boki ramki
        for (int i = 1; i < height - 1; i++) {
            tg.putString(startX, startY + i, "|", SGR.BOLD);
            tg.putString(startX + width - 1, startY + i, "|", SGR.BOLD);
        }

        // Dolna linia
        tg.putString(startX, startY + height - 1, "+" + "-".repeat(width - 2) + "+", SGR.BOLD);
    }


    public void displayWrongMessage(Question question, int selectedAnswer) throws Exception {

        updateTerminalSize();
        // Resetowanie tła i koloru tekstu
        tg.setBackgroundColor(TextColor.ANSI.BLACK);
        tg.setForegroundColor(TextColor.ANSI.WHITE);

        String wrongAnswer = "Your answer is incorrect, right answer was: ";

        int row = getTerminalSize().getRows() / 5;
        int correctAnswerIndex = question.getCorrectAnswerIndex();
        String correctAnswer = question.getAnswers().get(correctAnswerIndex);

        int col = (getTerminalSize().getColumns() - correctAnswer.length() ) / 2;
        int col2 = (getTerminalSize().getColumns() - wrongAnswer.length() ) / 2;

        tg.setForegroundColor(TextColor.ANSI.RED_BRIGHT);
        tg.putString(col2, row, wrongAnswer);
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.putString(col, row + 1, correctAnswer, SGR.BOLD);
        screen.refresh();


        hold(1000);

    }

    public void showCorrectMessage() throws Exception{

        updateTerminalSize();
        // Resetowanie tła i koloru tekstu
        tg.setBackgroundColor(TextColor.ANSI.BLACK);
        tg.setForegroundColor(TextColor.ANSI.WHITE);

        String correctAnswer = "Your answer is correct";
        String cong = "Keep it this way";

        int row = getTerminalSize().getRows() / 5;
        int col1 = (getTerminalSize().getColumns() - correctAnswer.length() ) / 2;
        int col2 = (getTerminalSize().getColumns() - cong.length()) / 2;

        tg.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
        tg.putString(col1, row, correctAnswer);
        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.putString(col2, row + 1, cong);
        screen.refresh();

        hold(1000);
    }

    public void displayBackToMenu() throws Exception{
        updateTerminalSize();
        String info = "Press ENTER to return to the MENU";
        int col = (getTerminalSize().getColumns() - info.length() ) / 2;
        int row = getTerminalSize().getRows() - getTerminalSize().getRows() / 9;
        tg.putString(col, row, info, SGR.BOLD);
        screen.refresh();
    }

    public void displayNextButton() throws Exception {

        updateTerminalSize();
        String info = "Press ENTER to get to the next question";
        int col = (getTerminalSize().getColumns() - info.length() ) / 2;
        int row = getTerminalSize().getRows() - getTerminalSize().getRows() / 9;
        tg.putString(col, row, info, SGR.BOLD, SGR.BLINK);
        screen.refresh();
    }

    public void showScore(int points) throws Exception{
        screen.clear();

        updateTerminalSize();
        String scoreInfo;
        String score = "Your score is: " + points + "/10";
        if(points == 10) {
            scoreInfo = "Congratulations, you have answered correctly on all questions";
        } else if (points > 5 && points < 10) {
            scoreInfo = "Your score is impressive, it seems you're not bad in quiz";
        } else if (points > 1 && points <= 5) {
            scoreInfo = "It seems you have to practice more :)";
        } else {
            scoreInfo = "Oops, I think something went wrong";
        }

        int col = (getTerminalSize().getColumns() - scoreInfo.length() ) / 2;
        int col1 = (getTerminalSize().getColumns() - score.length() ) / 2;
        int row = getTerminalSize().getRows() / 2;

        tg.putString(col, row, scoreInfo, SGR.BOLD);
        tg.putString(col1, row + 1, score);
        screen.refresh();
    }

    public void displayCategoryFrame(List<String> categories) throws Exception {
        updateTerminalSize();
        screen.clear();

        // Ustalanie rozmiarów ramki
        int boxWidth = categories.stream().mapToInt(String::length).max().orElse(20) + 4; // Najdłuższy tekst + padding
        int boxHeight = categories.size() + 4; // Liczba kategorii + nagłówki

        int startX = (getTerminalSize().getColumns() - boxWidth) / 2;
        int startY = (getTerminalSize().getRows() - boxHeight) / 2;

        // Rysowanie górnej krawędzi ramki
        tg.putString(startX, startY, "+" + "-".repeat(boxWidth - 2) + "+");

        // Nagłówek
        String header = "Available Categories";
        int headerX = startX + (boxWidth - header.length()) / 2;
        tg.putString(headerX, startY + 1, header, SGR.BOLD);

        // Separator nagłówka
        tg.putString(startX, startY + 2, "+" + "-".repeat(boxWidth - 2) + "+");

        // Wyświetlanie kategorii wewnątrz ramki
        for (int i = 0; i < categories.size(); i++) {
            String category = categories.get(i);
            int padding = (boxWidth - category.length() - 2) / 2;
            String line = "|" + " ".repeat(padding) + category + " ".repeat(boxWidth - category.length() - 2 - padding) + "|";
            tg.putString(startX, startY + 3 + i, line);
        }

        // Dolna krawędź ramki
        tg.putString(startX, startY + boxHeight - 1, "+" + "-".repeat(boxWidth - 2) + "+");

        // Odświeżanie ekranu
        screen.refresh();
    }


    private static void drawMenuAnimated(Screen screen, TextGraphics tg, int startY) throws Exception {

        String[][] letters = {
                { // M
                        "##     ##",
                        "###   ###",
                        "#### ####",
                        "## ### ##",
                        "##  #  ##",
                        "##     ##",
                        "##     ##"
                },
                { // E
                        "########",
                        "##      ",
                        "##      ",
                        "########",
                        "##      ",
                        "##      ",
                        "########"
                },
                { // N
                        "##     ##",
                        "###    ##",
                        "####   ##",
                        "## ##  ##",
                        "##  ## ##",
                        "##   ####",
                        "##    ###"
                },
                { // U
                        "##     ##",
                        "##     ##",
                        "##     ##",
                        "##     ##",
                        "##     ##",
                        "##     ##",
                        " ####### "
                }
        };

        // Oblicz szerokość każdej litery i odstęp między nimi
        int letterSpacing = 1;
        int totalWidth = 0;
        for (String[] letter : letters) {
            totalWidth += letter[0].length() + letterSpacing;
        }
        totalWidth -= letterSpacing; // Odejmujemy ostatni odstęp

        // Pozycja początkowa na środku ekranu
        int startX = (screen.getTerminalSize().getColumns() - totalWidth) / 2;

        // Rysowanie każdej litery stopniowo
        int currentX = startX;
        for (String[] letter : letters) {
            drawLetter(tg, letter, currentX, startY);
            currentX += letter[0].length() + letterSpacing;
        }
    }


    private static void drawQuizAnimated(Screen screen, TextGraphics tg, int startY) throws Exception {

        String[][] letters = {
                { // Q
                        " $$$$$$$ ",
                        "$$     $$",
                        "$$     $$",
                        "$$     $$",
                        "$$     $$",
                        " $$$$$$$ ",
                        "     $$ "
                },
                { // U
                        "$$     $$",
                        "$$     $$",
                        "$$     $$",
                        "$$     $$",
                        "$$     $$",
                        " $$$$$$$ "
                },
                { // I
                        " $$$$$$",
                        "   $$  ",
                        "   $$  ",
                        "   $$  ",
                        "   $$  ",
                        " $$$$$$"
                },
                { // Z
                        "$$$$$$$",
                        "     $$",
                        "    $$ ",
                        "   $$  ",
                        "  $$   ",
                        "$$$$$$$"
                },
                { // I
                        " $$$$$$",
                        "   $$  ",
                        "   $$  ",
                        "   $$  ",
                        "   $$  ",
                        " $$$$$$"
                },
                { // Z
                        "$$$$$$$",
                        "     $$",
                        "    $$ ",
                        "   $$  ",
                        "  $$   ",
                        "$$$$$$$"
                }
        };

        // Oblicz szerokość każdej litery i odstęp między nimi
        int letterSpacing = 1;
        int totalWidth = 0;
        for (String[] letter : letters) {
            totalWidth += letter[0].length() + letterSpacing;
        }
        totalWidth -= letterSpacing; // Odejmujemy ostatni odstęp

        // Pozycja początkowa na środku ekranu
        int startX = (screen.getTerminalSize().getColumns() - totalWidth) / 2;

        // Rysowanie każdej litery stopniowo
        int currentX = startX;
        for (String[] letter : letters) {
            drawAnimated(tg, screen, letter, currentX, startY);
            currentX += letter[0].length() + letterSpacing;
        }
    }

    private static void drawAnimated(TextGraphics tg, Screen screen, String[] lines, int x, int y) throws Exception {
        for (int i = 0; i < lines.length; i++) {
            tg.putString(x, y + i, lines[i]);
            screen.refresh();
            Thread.sleep(30); // Opóźnienie między kolejnymi liniami
        }
    }

    private static void drawCategories(Screen screen, TextGraphics tg, int startY) throws Exception {

        // Mniejsza czcionka dla napisu "Categories"
        String[][] letters = {
                { // C
                        " $$$$ ",
                        "$    $",
                        "$     ",
                        "$     ",
                        "$    $",
                        " $$$$ "
                },
                { // A
                        "  $$  ",
                        " $  $ ",
                        "$$$$$$",
                        "$    $",
                        "$    $",
                        "$    $"
                },
                { // T
                        "$$$$$$",
                        "  $$  ",
                        "  $$  ",
                        "  $$  ",
                        "  $$  ",
                        "  $$  "
                },
                { // E
                        "$$$$$$",
                        "$     ",
                        "$$$$  ",
                        "$     ",
                        "$     ",
                        "$$$$$$"
                },
                { // G
                        " $$$$ ",
                        "$    $",
                        "$     ",
                        "$  $$$",
                        "$    $",
                        " $$$$ "
                },
                { // O
                        " $$$$ ",
                        "$    $",
                        "$    $",
                        "$    $",
                        "$    $",
                        " $$$$ "
                },
                { // R
                        "$$$$$ ",
                        "$    $",
                        "$$$$$ ",
                        "$  $  ",
                        "$   $ ",
                        "$    $"
                },
                { // I
                        " $$$$ ",
                        "  $$  ",
                        "  $$  ",
                        "  $$  ",
                        "  $$  ",
                        " $$$$ "
                },
                { // E
                        "$$$$$$",
                        "$     ",
                        "$$$$  ",
                        "$     ",
                        "$     ",
                        "$$$$$$"
                },
                { // S
                        " $$$$ ",
                        "$     ",
                        " $$$$ ",
                        "     $",
                        "$    $",
                        " $$$$ "
                }
        };

        int letterSpacing = 1;
        int totalWidth = 0;
        for (String[] letter : letters) {
            totalWidth += letter[0].length() + letterSpacing;
        }
        totalWidth -= letterSpacing; // Odejmujemy ostatni odstęp

        // Pozycja początkowa napisu na środku ekranu
        int startX = (screen.getTerminalSize().getColumns() - totalWidth) / 2;

        // Rysowanie całego napisu
        int currentX = startX;
        for (String[] letter : letters) {
            drawLetter(tg, letter, currentX, startY);
            currentX += letter[0].length() + letterSpacing;
        }

        // Odświeżenie ekranu po wyświetleniu całego napisu
        screen.refresh();
    }

    private static void drawLetter(TextGraphics tg, String[] lines, int x, int y) {
        for (int i = 0; i < lines.length; i++) {
            tg.putString(x, y + i, lines[i]);
        }
    }


}