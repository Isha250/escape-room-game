public class Puzzle {
    private String question;
    private String answer;

    public Puzzle(String question, String answer) {
        this.question = question;
        this.answer = answer.toLowerCase();
    }

    public String getQuestion() {
        return question;
    }

    public boolean checkAnswer(String userAnswer) {
        return userAnswer.toLowerCase().equals(answer);
    }
}