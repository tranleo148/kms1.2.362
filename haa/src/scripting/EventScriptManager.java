package scripting;

import handling.channel.ChannelServer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import tools.FileoutputUtil;

public class EventScriptManager extends AbstractScriptManager {
  private static class EventEntry {
    public String script;
    
    public Invocable iv;
    
    public EventManager em;
    
    public EventEntry(String script, Invocable iv, EventManager em) {
      this.script = script;
      this.iv = iv;
      this.em = em;
    }
  }
  
  private final Map<String, EventEntry> events = new LinkedHashMap<>();
  
  private static final AtomicInteger runningInstanceMapId = new AtomicInteger(0);
  
  public static final int getNewInstanceMapId() {
    return runningInstanceMapId.addAndGet(1);
  }
  
  public EventScriptManager(ChannelServer cserv, String[] scripts) {
    for (String script : scripts) {
      if (!script.equals("")) {
        Invocable iv = getInvocable("event/" + script + ".js", null);
        if (iv != null)
          getEvents().put(script, new EventEntry(script, iv, new EventManager(cserv, iv, script))); 
      } 
    } 
  }
  
  public final EventManager getEventManager(String event) {
    EventEntry entry = getEvents().get(event);
    if (entry == null)
      return null; 
    return entry.em;
  }
  
  public final void init() {
    for (EventEntry entry : getEvents().values()) {
      try {
        ((ScriptEngine)entry.iv).put("em", entry.em);
        entry.iv.invokeFunction("init", new Object[] { null });
      } catch (Exception ex) {
        System.out.println("Error initiating event: " + entry.script + ":" + ex);
        FileoutputUtil.log("Log_Script_Except.rtf", "Error initiating event: " + entry.script + ":" + ex);
      } 
    } 
  }
  
  public final void cancel() {
    for (EventEntry entry : getEvents().values())
      entry.em.cancel(); 
  }
  
  public Map<String, EventEntry> getEvents() {
    return this.events;
  }
}
