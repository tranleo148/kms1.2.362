package handling.channel.handler;

import client.MapleCharacter;
import client.MapleClient;
import client.SkillFactory;
import constants.GameConstants;
import java.awt.Point;
import server.SecondaryStatEffect;
import server.field.skill.MapleOrb;
import server.maps.MapleMapObjectType;
import tools.data.LittleEndianAccessor;
import tools.packet.CField;
import tools.packet.CWvsContext;

public class OrbHandler {
  public static void spawnOrb(LittleEndianAccessor slea, MapleClient c) {
    MapleCharacter chr = c.getPlayer();
    if (chr == null || chr.getMap() == null)
      return; 
    int skillId = slea.readInt();
    int delay = slea.readInt();
    int unk1 = slea.readInt();
    int unk2 = slea.readInt();
    Point pos = new Point(slea.readInt(), slea.readInt());
    int facing = slea.readInt();
    int unk3 = slea.readInt();
    if (chr.getSkillLevel(GameConstants.getLinkedSkill(skillId)) > 0) {
      SecondaryStatEffect effect = SkillFactory.getSkill(skillId).getEffect(chr.getSkillLevel(skillId));
      effect.applyTo(chr);
      chr.getClient().getSession().writeAndFlush(CField.skillCooldown(effect.getSourceId(), effect.getCooldown(chr)));
      chr.addCooldown(effect.getSourceId(), System.currentTimeMillis(), effect.getCooldown(chr));
      c.getSession().writeAndFlush(CWvsContext.enableActions(chr, true, false));
      MapleOrb orb = new MapleOrb(chr.getId(), 1, skillId, effect.getX(), effect.getSubTime() / 1000, effect.getDuration() / 1000, delay, unk1, unk2, pos, facing, unk3);
      chr.getMap().spawnOrb(orb);
    } 
  }
  
  public static void moveOrb(LittleEndianAccessor slea, MapleClient c) {
    MapleCharacter chr = c.getPlayer();
    if (chr == null || chr.getMap() == null)
      return; 
    int type = slea.readInt();
    int objectId = slea.readInt();
    int chrId = slea.readInt();
    int skillId = slea.readInt();
    int action = slea.readInt();
    Point pos = slea.readPos();
    if (chr.getId() == chrId && chr.getMap().getMapObject(objectId, MapleMapObjectType.ORB) != null && chr.getSkillLevel(GameConstants.getLinkedSkill(skillId)) > 0)
      chr.getMap().broadcastMessage(CField.moveOrb(chrId, type, objectId, action, pos, slea.readInt(), slea.readInt(), slea.readInt())); 
  }
  
  public static void removeOrb(LittleEndianAccessor slea, MapleClient c) {
    MapleCharacter chr = c.getPlayer();
    if (chr == null || chr.getMap() == null)
      return; 
    int objectId = slea.readInt();
    int type = slea.readInt();
    int count = slea.readInt();
    MapleOrb orb = (MapleOrb)chr.getMap().getMapObject(objectId, MapleMapObjectType.ORB);
    if (chr.getSkillLevel(orb.getSkillId()) > 0) {
      SecondaryStatEffect effect = SkillFactory.getSkill(orb.getSkillId()).getEffect(chr.getSkillLevel(orb.getSkillId()));
      double a = effect.getT();
      chr.changeCooldown(400021094, -((int)(count * a) * 1000));
    } 
    if (orb != null)
      chr.getMap().removeOrb(chr.getId(), orb); 
  }
}
