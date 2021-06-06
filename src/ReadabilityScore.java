import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadabilityScore {

    static int characters;
    static int words;
    static int sentences;
    static int syllables;
    static int polysyllables;
    static String text;
    static String[] arrWords;
    static double avgSentences;
    static double avgCharacters;

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String file = Files.readString(Path.of(args[0]));
        text = file.toLowerCase();
        analyzeText();
        printText(file);
        String next = scanner.next();
        chooseScore(next);
    }

    private static void printText(String txt) {
        System.out.println("The text is:");
        System.out.println(txt);
        System.out
                .printf("%nWords: %d%n", words)
                .printf("Sentences: %d%n", sentences)
                .printf("Characters: %d%n", characters)
                .printf("Syllables: %d%n", syllables)
                .printf("Polysyllables: %d%n", polysyllables);
        System.out.println("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): all");
    }

    private static void analyzeText() {
        countWords();
        countSentences();
        countCharacters();
        countSyllables();
    }

    private static void countWords() {
        arrWords = text.split(" ");
        words = arrWords.length;
    }

    private static void countSentences() {
        String[] arrSentences = text.split("[.!?]");
        sentences = arrSentences.length;
        avgSentences = ((double) sentences / words) * 100;
    }

    private static void countCharacters() {
        for (String s : arrWords) {
            characters += s.length();
        }
        avgCharacters = ((double) characters / words) * 100;
    }

    private static void countSyllables() {
        String[] newWords = text.replaceAll("[.!?,]", "").split(" ");
        int count = 0;
        ArrayList<Integer> arr = new ArrayList<>();
        for (String word : newWords) {
            for (int i = 0; i < word.length(); i++) {
                char ch = word.charAt(i);
                if (ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u' || ch == 'y') {
                    count++;
                }
            }
            for (int i = 0; i < word.length() - 1; i++) {
                char ch = word.charAt(i);
                char chx = word.charAt(i + 1);
                if (ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u'|| ch == 'y') {
                    if (chx == 'a' || chx == 'e' || chx == 'i' || chx == 'o' || chx == 'u' || chx == 'y') {
                        count--;
                    }
                }
            }
            if (word.charAt(word.length() - 1) == 'e' && (word.charAt(word.length() - 2) != 'a' || word.charAt(word.length() - 2) != 'e' ||
                    word.charAt(word.length() - 2) != 'i' || word.charAt(word.length() - 2) != 'o' || word.charAt(word.length() - 2) != 'u'|| word.charAt(word.length() - 2) != 'y' )) {
                count--;
            }
            if (count == 0) {
                count = 1;
            }
            arr.add(count);
            syllables += count;
            count = 0;
        }

        for (Integer num : arr) {
            if (num > 2) {
                polysyllables++;
            }
        }
    }

    private static void chooseScore(String nextScore) {
        switch (nextScore) {
            case "ARI":
                System.out.printf("Automated Readability Index: %.2f (about %s-year-olds).", readabilityScore(), checkAge(readabilityScore()));
                break;
            case "FK":
                System.out.printf("Flesch–Kincaid readability tests: %.2f (about %s-year-olds).", fleschKincaid(), checkAge(fleschKincaid()));
                break;
            case "SMOG":
                System.out.printf("Simple Measure of Gobbledygook: %.2f (about %s-year-olds).", smogIndex(), checkAge(smogIndex()));
                break;
            case "CL":
                System.out.printf("Coleman–Liau index: %.2f (about %s-year-olds).", colemanLiau(), checkAge(colemanLiau()));
                break;
            case "all":
                System.out
                        .printf("%nAutomated Readability Index: %.2f (about %s-year-olds).%n", readabilityScore(), checkAge(readabilityScore()))
                        .printf("Flesch–Kincaid readability tests: %.2f (about %s-year-olds).%n", fleschKincaid(), checkAge(fleschKincaid()))
                        .printf("Simple Measure of Gobbledygook: %.2f (about %s-year-olds).%n", smogIndex(), checkAge(smogIndex()))
                        .printf("Coleman–Liau index: %.2f (about %s-year-olds).%n", colemanLiau(), checkAge(colemanLiau()));
                double avgAge = (Double.parseDouble(checkAge(readabilityScore())) + Double.parseDouble(checkAge(fleschKincaid())) +
                        Double.parseDouble(checkAge(fleschKincaid())) + Double.parseDouble(checkAge(colemanLiau()))) / 4;
                System.out.printf("%nThis text should be understood in average by %.2f-year-olds.", avgAge);
                break;
        }
    }

    private static double readabilityScore() {
        return 4.71 * ((double) characters / words) + 0.5 * ((double) words / sentences) - 21.43;
    }

    private static double fleschKincaid() {
        return 0.39 * ((double) words / sentences) + 11.8 * ((double) syllables / words) - 15.59;
    }

    private static double smogIndex() {
        return 1.043 * Math.sqrt((double) polysyllables * (30 / (double) sentences)) + 3.1291;
    }

    private static double colemanLiau() {
        return 0.0588 * avgCharacters - 0.296 * avgSentences - 15.8;
    }

    private static String checkAge(double number) {
        int newScore = (int) Math.ceil(number);
        if (newScore == 1) {
            return "6";
        } else if (newScore == 2) {
            return "7";
        } else if (newScore == 3) {
            return "9";
        } else if (newScore == 4) {
            return "10";
        } else if (newScore == 5) {
            return "11";
        } else if (newScore == 6) {
            return "12";
        } else if (newScore == 7) {
            return "13";
        } else if (newScore == 8) {
            return "14";
        } else if (newScore == 9) {
            return "15";
        } else if (newScore == 10) {
            return "16";
        } else if (newScore == 11) {
            return "17";
        } else if (newScore == 12) {
            return "18";
        } else if (newScore == 13) {
            return "24";
        } else {
            return "24";
        }
    }
}






