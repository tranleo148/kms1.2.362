package server.events;

import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import server.Randomizer;
import tools.Pair;

public class MapleOxQuizFactory {
  private final Map<Pair<Integer, Integer>, MapleOxQuizEntry> questionCache = new HashMap<>();
  
  private static final MapleOxQuizFactory instance = new MapleOxQuizFactory();
  
  public MapleOxQuizFactory() {
    initialize();
  }
  
  public static MapleOxQuizFactory getInstance() {
    return instance;
  }
  
  public Map.Entry<Pair<Integer, Integer>, MapleOxQuizEntry> grabRandomQuestion() {
    int size = this.questionCache.size();
    while (true) {
      for (Map.Entry<Pair<Integer, Integer>, MapleOxQuizEntry> oxquiz : this.questionCache.entrySet()) {
        if (Randomizer.nextInt(size) == 0)
          return oxquiz; 
      } 
    } 
  }
  
  private void initialize() {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      con = DatabaseConnection.getConnection();
      ps = con.prepareStatement("SELECT * FROM wz_oxdata");
      rs = ps.executeQuery();
      while (rs.next())
        this.questionCache.put(new Pair<>(Integer.valueOf(rs.getInt("questionset")), Integer.valueOf(rs.getInt("questionid"))), get(rs)); 
      rs.close();
      ps.close();
      con.close();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (con != null)
          con.close(); 
        if (ps != null)
          ps.close(); 
        if (rs != null)
          rs.close(); 
      } catch (SQLException se) {
        se.printStackTrace();
      } 
    } 
  }
  
  private MapleOxQuizEntry get(ResultSet rs) throws SQLException {
    return new MapleOxQuizEntry(rs.getString("question"), rs.getString("display"), getAnswerByText(rs.getString("answer")), rs.getInt("questionset"), rs.getInt("questionid"));
  }
  
  private int getAnswerByText(String text) {
    if (text.equalsIgnoreCase("x"))
      return 0; 
    if (text.equalsIgnoreCase("o"))
      return 1; 
    return -1;
  }
  
  public static class MapleOxQuizEntry {
    private String question;
    
    private String answerText;
    
    private int answer;
    
    private int questionset;
    
    private int questionid;
    
    public MapleOxQuizEntry(String question, String answerText, int answer, int questionset, int questionid) {
      this.question = question;
      this.answerText = answerText;
      this.answer = answer;
      this.questionset = questionset;
      this.questionid = questionid;
    }
    
    public String getQuestion() {
      return this.question;
    }
    
    public String getAnswerText() {
      return this.answerText;
    }
    
    public int getAnswer() {
      return this.answer;
    }
    
    public int getQuestionSet() {
      return this.questionset;
    }
    
    public int getQuestionId() {
      return this.questionid;
    }
  }
}
