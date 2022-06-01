package log;

public class LogType {
  public enum Chat {
    General(0),
    Buddy(1),
    Party(2),
    Guild(3),
    Messenger(4),
    Trade(5),
    PlayerShop(6),
    HiredMerchant(7),
    MiniGame(8),
    Megaphone(9),
    SuperMegaphone(10),
    MessageBox(11),
    Weather(12),
    Pet(13),
    Note(14),
    Whisper(15),
    TripleMegaphone(16),
    ItemMegaphone(17);
    
    public final int i;
    
    Chat(int i) {
      this.i = i;
    }
  }
}
