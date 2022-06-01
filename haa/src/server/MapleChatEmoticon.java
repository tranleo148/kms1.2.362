package server;

import java.util.ArrayList;
import java.util.List;
import tools.Pair;

public class MapleChatEmoticon {
  private int charid;
  
  private int emoticonid;
  
  private long time;
  
  private List<Pair<Integer, Short>> bookmarks = new ArrayList<>();
  
  public MapleChatEmoticon(int charid, int emoticonid, long time, String bookMarkss) {
    this.charid = charid;
    this.emoticonid = emoticonid;
    this.time = time;
    if (bookMarkss != null) {
      String[] bookMarks = bookMarkss.split(",");
      if (bookMarks.length > 2)
        for (int i = 0; i < bookMarks.length; i += 2)
          this.bookmarks.add(new Pair<>(Integer.valueOf(Integer.parseInt(bookMarks[i])), Short.valueOf(Short.parseShort(bookMarks[i + 1]))));  
    } 
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
  
  public long getTime() {
    return this.time;
  }
  
  public void setTime(long time) {
    this.time = time;
  }
  
  public List<Pair<Integer, Short>> getBookmarks() {
    return this.bookmarks;
  }
  
  public String getBookmark() {
    StringBuilder sb = new StringBuilder();
    for (Pair<Integer, Short> a : this.bookmarks)
      sb.append(a.left).append(",").append(a.right).append(","); 
    if (sb.toString().contains(","))
      sb.deleteCharAt(sb.lastIndexOf(",")); 
    return sb.toString();
  }
  
  public void setBookmarks(List<Pair<Integer, Short>> bookmarks) {
    this.bookmarks = bookmarks;
  }
}
