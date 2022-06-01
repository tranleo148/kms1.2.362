package server;

public class MapleSavedEmoticon {
  private int charid;
  
  private int emoticonid;
  
  private String text;
  
  public MapleSavedEmoticon(int charid, int emoticonid, String text) {
    this.charid = charid;
    this.emoticonid = emoticonid;
    this.text = text;
  }
  
  public int getCharid() {
    return this.charid;
  }
  
  public void setCharid(int charid) {
    this.charid = charid;
  }
  
  public int getEmoticonid() {
    return this.emoticonid;
  }
  
  public void setEmoticonid(int emoticonid) {
    this.emoticonid = emoticonid;
  }
  
  public String getText() {
    return this.text;
  }
  
  public void setText(String text) {
    this.text = text;
  }
}
