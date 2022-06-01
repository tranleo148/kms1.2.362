package tools.packet;

import client.MapleCharacter;
import client.inventory.Item;
import client.inventory.MaplePet;
import handling.SendPacketOpcode;
import java.awt.Point;
import java.util.List;
import server.movement.LifeMovementFragment;
import tools.data.MaplePacketLittleEndianWriter;

public class PetPacket {
  public static final byte[] updatePet(MapleCharacter player, MaplePet pet, Item item, boolean unequip, boolean petLoot) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
        mplew.write(0);
        mplew.writeInt(2);
        mplew.write(0);
        mplew.write(3);
        mplew.write(5);
        mplew.writeShort(pet.getInventoryPosition());
        mplew.write(0);
        mplew.write(5);
        mplew.writeShort(pet.getInventoryPosition());
        mplew.write(3);
        mplew.writeInt(pet.getPetItemId());
        mplew.write(1);
        mplew.writeLong(pet.getUniqueId());
    PacketHelper.addPetItemInfo(mplew, player, item, pet, unequip, petLoot);
    return mplew.getPacket();
  }
  
  public static final byte[] showPet(MapleCharacter chr, MaplePet pet, boolean remove, boolean hunger) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.SPAWN_PET.getValue());
    mplew.writeInt(chr.getId());
    mplew.writeInt(chr.getPetIndex(pet));
    if (remove) {
      mplew.writeShort(hunger ? 256 : 0);
    } else {
      mplew.write(1);
      mplew.write(1);
      mplew.writeInt(pet.getPetItemId());
      mplew.writeMapleAsciiString(pet.getName());
      mplew.writeLong(pet.getUniqueId());
      mplew.writeShort((pet.getPos()).x);
      mplew.writeShort((pet.getPos()).y - 20);
      mplew.write(pet.getStance());
      mplew.writeShort(pet.getFh());
      mplew.writeInt(pet.getColor());
      mplew.writeShort(pet.getWonderGrade());
      mplew.writeShort(pet.getPetSize());
      mplew.write(0);
      mplew.write(0);
      mplew.write(0);
      mplew.write(0);
    } 
    return mplew.getPacket();
  }
  
  public static final byte[] movePet(int cid, long pid, byte slot, List<LifeMovementFragment> moves, Point startPos) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.MOVE_PET.getValue());
    mplew.writeInt(cid);
    mplew.writeInt(slot);
    mplew.writeInt(0);
    mplew.writePos(startPos);
    mplew.writePos(new Point(0, 0));
    PacketHelper.serializeMovementList(mplew, moves);
    return mplew.getPacket();
  }
  
  public static final byte[] petChat(int cid, int un, String text, byte slot) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.PET_CHAT.getValue());
    mplew.writeInt(cid);
    mplew.writeInt(slot);
    mplew.writeShort(un);
    mplew.writeMapleAsciiString(text);
    mplew.write(0);
    return mplew.getPacket();
  }
  
  public static final byte[] commandResponse(int cid, byte command, byte slot, boolean success) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.PET_COMMAND.getValue());
    mplew.writeInt(cid);
    mplew.writeInt(slot);
    mplew.write((command == 1) ? 0 : 1);
    mplew.write(command);
    mplew.writeShort(0);
    return mplew.getPacket();
  }
  
  public static final byte[] updatePetLootStatus(int status) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.PET_LOOT_STATUS.getValue());
    mplew.write(status);
    mplew.write(1);
    return mplew.getPacket();
  }
  
  public static byte[] petExceptionList(MapleCharacter chr, MaplePet pet) {
    MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
    mplew.writeShort(SendPacketOpcode.PET_EXCEPTION_LIST.getValue());
    mplew.writeInt(chr.getId());
    mplew.writeInt(chr.getPetIndex(pet));
    String[] sb = pet.getExceptionList().split(",");
    if (sb.length <= 0) {
      mplew.write(0);
    } else {
      mplew.write(sb.length);
      for (int i = 0; i < sb.length; i++)
        mplew.writeInt(Integer.parseInt(sb[i])); 
    } 
    return mplew.getPacket();
  }
}
