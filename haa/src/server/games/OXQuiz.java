package server.games;

public class OXQuiz {
  private String Question;
  
  private String Explaination;
  
  private boolean isX;
  
  public OXQuiz(String a1, String a2, boolean a3) {
    this.Question = a1;
    this.Explaination = a2;
    this.isX = a3;
  }
  
  public String getQuestion() {
    return this.Question;
  }
  
  public String getExplaination() {
    return this.Explaination;
  }
  
  public boolean isX() {
    return this.isX;
  }
}
