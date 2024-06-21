import java.util.*;
import java.io.*;

public class Game {
    public static void main(String[] args) throws FileNotFoundException {
        char[] russianAlphabet = {
                'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й',
                'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф',
                'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я'
        };
        int attempts = 0;
        String[] hangmanStages = {
                """
           +---+
           |   |
               |
               |
               |
               |
        =========
                """,
                """
           +---+
           |   |
           O   |
               |
               |
               |
        =========
        """,
                """
           +---+
           |   |
           O   |
           |   |
               |
               |
        =========
        """,
                """
           +---+
           |   |
           O   |
          /|   |
               |
               |
        =========
        """,
                """
           +---+
           |   |
           O   |
          /|\\  |
               |
               |
        =========
        """,
                """
           +---+
           |   |
           O   |
          /|\\  |
          /    |
               |
        =========
        """,
                """
           +---+
           |   |
           O   |
          /|\\  |
          / \\  |
               |
        =========
        """
        };

        File file = new File("words.txt");
        Scanner scanner = new Scanner(file);

        List<String> words = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String word = scanner.nextLine();
            words.add(word);
        }
        scanner.close();

        Scanner scannerInput = new Scanner(System.in);
        String userResponseRestart = "";
        boolean stopGame = false;
        while (!stopGame) {

            System.out.println("Начать новую игру? \n [Y]es or [N]o: ");
            String userResponse = scannerInput.nextLine();
            if (userResponse.equalsIgnoreCase("Y")) {
                Random random = new Random();
                int randomWordIndex = random.nextInt(words.size());
                String word = words.get(randomWordIndex);
                String hiddenWord = "_".repeat(word.length());
                while (attempts < hangmanStages.length) {

                    System.out.println("Ваше слово: " + hiddenWord);
                    System.out.println(hangmanStages[attempts]);
                    if (attempts > 0) {
                        System.out.println("Количество ошибок: " + attempts);
                    }

                    System.out.println("Какая буква есть в слове? ");
                    String playerAnswer = scannerInput.nextLine();
                    char guessedLetter = Character.toLowerCase(playerAnswer.charAt(0));
                    if (playerAnswer.length() != 1) {
                        System.out.println("Вы должны ввести одну букву. Ошибка. Ответом будет принята первая буква.");
                        continue;
                    }

                    boolean isCyrillic = false;
                    while(!isCyrillic) {
                        for (char c : russianAlphabet) {
                            if (guessedLetter == c) {
                                isCyrillic = true;
                                break;
                            }
                        }

                        if (!isCyrillic) {
                            System.out.println("Вы ввели недопустимый символ. Ошибка. Введите букву кириллицы.");
                            playerAnswer = scannerInput.nextLine();
                            guessedLetter = playerAnswer.charAt(0);
                        }
                    }

                    boolean guessCorrect = false;
                    StringBuilder stringBuilder = new StringBuilder(hiddenWord);

                    for (int i = 0; i < word.length(); i++) {
                        if (Character.toLowerCase(guessedLetter) == Character.toLowerCase(word.charAt(i))) {
                            stringBuilder.setCharAt(i, word.charAt(i));
                            guessCorrect = true;
                        }
                    }
                    if (guessCorrect) {
                        hiddenWord = stringBuilder.toString();
                        if (hiddenWord.equalsIgnoreCase(word)) {
                            System.out.println("Молодец! Ты отгадал слово! - " + word);
                            System.out.println("Хотите начать новую игру? \n[Y]es or [N]o");
                            userResponseRestart = scannerInput.nextLine();
                            if (userResponseRestart.equalsIgnoreCase("y")) {
                                word = words.get(random.nextInt(words.size()));
                                attempts = 0;
                                hiddenWord = "_".repeat(word.length());
                            } else {
                                System.out.println("Игра остановлена.");
                                return;
                            }
                        }
                    } else {
                        attempts++;
                    }
                    if (attempts >= hangmanStages.length - 1) {

                        System.out.println(hangmanStages[hangmanStages.length - 1] + "Количество ошибок: " + attempts + "\nGame Over. Загаданное слово было: " + word);

                        System.out.println("Хотите начать новую игру? \n[Y]es or [N]o");
                        userResponseRestart = scannerInput.nextLine();
                        if (userResponseRestart.equalsIgnoreCase("y")) {
                            word = words.get(random.nextInt(words.size()));
                            attempts = 0;
                            hiddenWord = "_".repeat(word.length());
                        } else if (userResponseRestart.equalsIgnoreCase("n")) {
                            System.out.println("Игра остановлена.");
                            return;
                        }
                    }
                }
            } else if (userResponse.equalsIgnoreCase("N")) {
                return;
            } else {
                System.out.println("Вы ввели не ту букву!");
                return;
            }
        }
    }
}
