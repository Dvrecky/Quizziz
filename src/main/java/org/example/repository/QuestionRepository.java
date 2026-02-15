package org.example.repository;

import org.example.entity.Question;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.*;

public class QuestionRepository {

    private final List<Question> questions;

    public QuestionRepository() {
        this.questions = new ArrayList<>();
        initQuestions();
    }

    private void initQuestions() {
        // History questions
        questions.add(new Question("Kiedy miała miejsce bitwa pod Grunwaldem?", List.of("1410", "1411", "966", "1250"), 0, "History"));
        questions.add(new Question("Kiedy miał miejsce chrzest Polski?", List.of("1410", "1411", "966", "1250"), 2, "History"));
        questions.add(new Question("Którego roku Bolesław Chrobry był koronowany na króla Polski?", List.of("1025", "1411", "966", "1250"), 0, "History"));
        questions.add(new Question("Jak miał na imię pierwszy król Polski?", List.of("Franciszek", "Jan", "Bolesław", "Andrzej"), 2, "History"));
        questions.add(new Question("W którym roku Polska odzyskała niepodległość?", List.of("1914", "1918", "1920", "1939"), 1, "History"));
        questions.add(new Question("Kto był wodzem Związku Radzieckiego podczas II wojny światowej?", List.of("Włodzimierz Lenin", "Józef Stalin", "Leonid Breżniew", "Michaił Gorbaczow"), 1, "History"));
        questions.add(new Question("Jak nazywał się pierwszy cesarz Chin?", List.of("Qin Shi Huang", "Kublai Khan", "Sun Yat-sen", "Mao Zedong"), 0, "History"));
        questions.add(new Question("Które imperium istniało najdłużej?", List.of("Imperium Brytyjskie", "Imperium Rzymskie", "Imperium Osmańskie", "Imperium Japońskie"), 2, "History"));
        questions.add(new Question("W którym roku upadł Związek Radziecki?", List.of("1989", "1991", "1993", "1995"), 1, "History"));
        questions.add(new Question("Który dokument zapoczątkował demokrację w Anglii?", List.of("Magna Carta", "Bill of Rights", "Deklaracja Niepodległości", "Konstytucja USA"), 0, "History"));

        // Nature questions
        questions.add(new Question("Które drzewo jest najszybszym rosnącym drzewem na świecie?", List.of("Bambus", "Dąb", "Sekwoja", "Sosna"), 0, "Nature"));
        questions.add(new Question("Jaki jest największy ocean na Ziemi?", List.of("Ocean Spokojny", "Ocean Atlantycki", "Ocean Indyjski", "Ocean Arktyczny"), 0, "Nature"));
        questions.add(new Question("Które zwierzę jest znane jako „król dżungli”?", List.of("Tygrys", "Lew", "Słoń", "Pantera"), 1, "Nature"));
        questions.add(new Question("Ile nóg ma pająk?", List.of("6", "8", "10", "12"), 1, "Nature"));
        questions.add(new Question("Jaki jest najwyższy wodospad na świecie?", List.of("Wodospad Niagara", "Wodospad Victoria", "Wodospad Angel", "Wodospad Iguazu"), 2, "Nature"));
        questions.add(new Question("Co jest największym ssakiem na Ziemi?", List.of("Słoń afrykański", "Płetwal błękitny", "Kaszalot", "Żyrafa"), 1, "Nature"));
        questions.add(new Question("Jakie jest naturalne środowisko pandy wielkiej?", List.of("Las bambusowy", "Sawanna", "Pustynia", "Tundra"), 0, "Nature"));
        questions.add(new Question("Które ptaki są znane z migracji na największe odległości?", List.of("Albatrosy", "Pingwiny", "Jaskółki", "Czajki"), 0, "Nature"));
        questions.add(new Question("Jak nazywa się największe jezioro na świecie?", List.of("Jezioro Bajkał", "Morze Kaspijskie", "Jezioro Wiktorii", "Jezioro Huron"), 1, "Nature"));
        questions.add(new Question("Która planeta jest znana jako 'czerwona planeta'?", List.of("Mars", "Wenus", "Jowisz", "Saturn"), 0, "Nature"));

        // Sport questions
        questions.add(new Question("Który kraj zdobył najwięcej tytułów mistrza świata w piłce nożnej?", List.of("Niemcy", "Argentyna", "Brazylia", "Francja"), 2, "Sport"));
        questions.add(new Question("Który sport jest znany jako „królowa sportu”?", List.of("Piłka nożna", "Lekkoatletyka", "Koszykówka", "Siatkówka"), 1, "Sport"));
        questions.add(new Question("Ile graczy jest w drużynie koszykówki na boisku?", List.of("4", "5", "6", "7"), 1, "Sport"));
        questions.add(new Question("Kto jest uznawany za jednego z najlepszych sprinterów wszech czasów?", List.of("Carl Lewis", "Usain Bolt", "Michael Johnson", "Mo Farah"), 1, "Sport"));
        questions.add(new Question("Który tenisista wygrał najwięcej turniejów Wielkiego Szlema?", List.of("Roger Federer", "Rafael Nadal", "Novak Djokovic", "Pete Sampras"), 2, "Sport"));
        questions.add(new Question("Jak nazywa się drużyna NBA z Los Angeles?", List.of("Celtics", "Bulls", "Lakers", "Heat"), 2, "Sport"));
        questions.add(new Question("Który kraj wynalazł grę w hokeja na lodzie?", List.of("Kanada", "Rosja", "Szwecja", "Stany Zjednoczone"), 0, "Sport"));
        questions.add(new Question("Który z tych sportów jest częścią triathlonu?", List.of("Szermierka", "Pływanie", "Piłka nożna", "Łucznictwo"), 1, "Sport"));
        questions.add(new Question("Jaką długość ma bieżnia olimpijska?", List.of("200m", "300m", "400m", "500m"), 2, "Sport"));
        questions.add(new Question("Który tenisista wygrał najwięcej turniejów Wielkiego Szlema?", List.of("Roger Federer", "Rafael Nadal", "Novak Djokovic", "Pete Sampras"), 2, "Sport"));

    }

    public List<Question> getAllQuestions() {
        return this.questions;
    }

    public List<Question> getRandomQuestions() {
        List<Question> shuffledQuestions = new ArrayList<>(this.questions);

        Collections.shuffle(shuffledQuestions);

        return shuffledQuestions.stream().limit(10).toList();
    }

    public List<Question> getQuestionsByCategory(String category) {

        List<Question> sameCategoryQuestions = new ArrayList<>();
        for (Question question : this.questions) {
            if(question.getCategory().equalsIgnoreCase(category)) {
                sameCategoryQuestions.add(question);
            }
        }

        Collections.shuffle(sameCategoryQuestions);
//        Random random = new Random();
//        List<Question> sameCategoryQuestionsRandom = new ArrayList<>();
//        for(int i = 0; i < 10; i++) {
//            sameCategoryQuestionsRandom.add(sameCategoryQuestions.get(random.nextInt(sameCategoryQuestions.size())));
//        }

        return sameCategoryQuestions.stream().limit(10).toList();
    }
}
