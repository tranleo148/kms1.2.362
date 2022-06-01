package tools;

import constants.ServerConstants;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import tools.data.MaplePacketLittleEndianWriter;

public class MapleAESOFB {
  private byte[] iv;
  
  private Cipher cipher;
  
  private short mapleVersion;
  
  private boolean isChannel;
  
  private boolean isOutbound;
  
  private static final byte[] sSecretKey = new byte[] {
        (byte) 0x87, 0x00, 0x00, 0x00,
        (byte) 0xD0, 0x00, 0x00, 0x00,
        (byte) 0xDB, 0x00, 0x00, 0x00,
        (byte) 0x2B, 0x00, 0x00, 0x00,
        (byte) 0x7F, 0x00, 0x00, 0x00,
        (byte) 0x54, 0x00, 0x00, 0x00,
        (byte) 0xAA, 0x00, 0x00, 0x00,
        (byte) 0x0B, 0x00, 0x00, 0x00};
  
  private static final byte[] funnyBytes = new byte[] { 
      -20, 63, 119, -92, 69, -48, 113, -65, -73, -104, 
      32, -4, 75, -23, -77, -31, 92, 34, -9, 12, 
      68, 27, -127, -67, 99, -115, -44, -61, -14, 16, 
      25, -32, -5, -95, 110, 102, -22, -82, -42, -50, 
      6, 24, 78, -21, 120, -107, -37, -70, -74, 66, 
      122, 42, -125, 11, 84, 103, 109, -24, 101, -25, 
      47, 7, -13, -86, 39, 123, -123, -80, 38, -3, 
      -117, -87, -6, -66, -88, -41, -53, -52, -110, -38, 
      -7, -109, 96, 45, -35, -46, -94, -101, 57, 95, 
      -126, 33, 76, 105, -8, 49, -121, -18, -114, -83, 
      -116, 106, -68, -75, 107, 89, 19, -15, 4, 0, 
      -10, 90, 53, 121, 72, -113, 21, -51, -105, 87, 
      18, 62, 55, -1, -99, 79, 81, -11, -93, 112, 
      -69, 20, 117, -62, -72, 114, -64, -19, 125, 104, 
      -55, 46, 13, 98, 70, 23, 17, 77, 108, -60, 
      126, 83, -63, 37, -57, -102, 28, -120, 88, 44, 
      -119, -36, 2, 100, 64, 1, 93, 56, -91, -30, 
      -81, 85, -43, -17, 26, 124, -89, 91, -90, 111, 
      -122, -97, 115, -26, 10, -34, 43, -103, 74, 71, 
      -100, -33, 9, 118, -98, 48, 14, -28, -78, -108, 
      -96, 59, 52, 29, 40, 15, 54, -29, 35, -76, 
      3, -40, -112, -56, 60, -2, 94, 50, 36, 80, 
      31, 58, 67, -118, -106, 65, 116, -84, 82, 51, 
      -16, -39, 41, Byte.MIN_VALUE, -79, 22, -45, -85, -111, -71, 
      -124, Byte.MAX_VALUE, 97, 30, -49, -59, -47, 86, 61, -54, 
      -12, 5, -58, -27, 8, 73 };
  
  public MapleAESOFB(byte[] iv, short mapleVersion, boolean isChannel) {
    this(iv, mapleVersion, isChannel, false);
  }
  
  public MapleAESOFB(byte[] iv, short mapleVersion, boolean isChannel, boolean isOutbound) {
    Key pKey = new SecretKeySpec(sSecretKey, "AES");
    SecureRandom pRandom = new SecureRandom();
    pRandom.nextBytes(iv);
    try {
      this.cipher = Cipher.getInstance("AES");
    } catch (NoSuchAlgorithmException ex) {
      Logger.getLogger(MapleAESOFB.class.getName()).log(Level.SEVERE, (String)null, ex);
    } catch (NoSuchPaddingException ex) {
      Logger.getLogger(MapleAESOFB.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
    try {
      this.cipher.init(1, pKey);
    } catch (InvalidKeyException ex) {
      Logger.getLogger(MapleAESOFB.class.getName()).log(Level.SEVERE, (String)null, ex);
    } 
    this.isChannel = isChannel;
    this.isOutbound = isOutbound;
    setIv(iv);
    this.mapleVersion = (short)(mapleVersion >> 8 & 0xFF | mapleVersion << 8 & 0xFF00);
  }
  
  private void setIv(byte[] iv) {
    this.iv = iv;
  }
  
  public byte[] getIv() {
    return this.iv;
  }
  
  public byte[] crypt(byte[] data) throws IllegalBlockSizeException, BadPaddingException {
    if (this.isChannel && this.isOutbound) {
      byte[] arrayOfByte = new byte[data.length];
      System.arraycopy(data, 0, arrayOfByte, 0, data.length);
      for (int i = 0; i < arrayOfByte.length; i++)
        arrayOfByte[i] = (byte)(arrayOfByte[i] + this.iv[0]); 
      updateIv();
      return arrayOfByte;
    } 
    int remaining = data.length;
    byte[] datac = new byte[remaining];
    System.arraycopy(data, 0, datac, 0, data.length);
    int llength = 1456;
    int start = 0;
    while (remaining > 0) {
      byte[] myIv = BitTools.multiplyBytes(this.iv, 4, 4);
      if (remaining < llength)
        llength = remaining; 
      for (int x = start; x < start + llength; x++) {
        if ((x - start) % myIv.length == 0) {
          byte[] newIv = this.cipher.doFinal(myIv);
          for (int j = 0; j < myIv.length; j++)
            myIv[j] = newIv[j]; 
        } 
        datac[x] = (byte)(datac[x] ^ myIv[(x - start) % myIv.length]);
      } 
      start += llength;
      remaining -= llength;
      llength = 1460;
    } 
    updateIv();
    return datac;
  }
  
  private void updateIv() {
    this.iv = getNewIv(this.iv);
  }
  
    public byte[] getPacketHeader(int length) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        int uSeqSnd = ((iv[2] & 0xFF) | (iv[3] << 8)) & 0xFFFF;
        uSeqSnd ^= (0xFFFF - ServerConstants.MAPLE_VERSION);

        mplew.writeShort((short) uSeqSnd);
        if (length >= 0xFF00) {
            mplew.writeShort((short) (0xFF00 ^ uSeqSnd));
            mplew.writeInt(length ^ uSeqSnd);
        } else {
            mplew.writeShort((short) (length ^ uSeqSnd));
        }
        return mplew.getPacket();
    }
  
  public static int getPacketLength(int packetHeader) {
    int packetLength = packetHeader >>> 16 ^ packetHeader & 0xFFFF;
    packetLength = packetLength << 8 & 0xFF00 | packetLength >>> 8 & 0xFF;
    return packetLength;
  }
  
  public boolean checkPacket(byte[] packet) {
    return true;
  }
  
  public boolean checkPacket(int packetHeader) {
    byte[] packetHeaderBuf = new byte[2];
    packetHeaderBuf[0] = (byte)(packetHeader >> 24 & 0xFF);
    packetHeaderBuf[1] = (byte)(packetHeader >> 16 & 0xFF);
    return checkPacket(packetHeaderBuf);
  }
  
  public static byte[] getNewIv(byte[] oldIv) {
    byte[] newIv = { -14, 83, 80, -58 };
    for (int i = 0; i < 4; i++)
      Shuffle(oldIv[i], newIv); 
    return newIv;
  }
  
  private static byte[] Shuffle(byte inputValue, byte[] newIV) {
    byte elina = newIV[1];
    byte anna = inputValue;
    byte moritz = funnyBytes[elina & 0xFF];
    moritz = (byte)(moritz - inputValue);
    newIV[0] = (byte)(newIV[0] + moritz);
    moritz = newIV[2];
    moritz = (byte)(moritz ^ funnyBytes[anna & 0xFF]);
    elina = (byte)(elina - (moritz & 0xFF));
    newIV[1] = elina;
    elina = newIV[3];
    moritz = elina;
    elina = (byte)(elina - (newIV[0] & 0xFF));
    moritz = funnyBytes[moritz & 0xFF];
    moritz = (byte)(moritz + inputValue);
    moritz = (byte)(moritz ^ newIV[2]);
    newIV[2] = moritz;
    elina = (byte)(elina + (funnyBytes[anna & 0xFF] & 0xFF));
    newIV[3] = elina;
    int merry = newIV[0] & 0xFF;
    merry |= newIV[1] << 8 & 0xFF00;
    merry |= newIV[2] << 16 & 0xFF0000;
    merry |= newIV[3] << 24 & 0xFF000000;
    int ret_value = merry >>> 29;
    merry <<= 3;
    ret_value |= merry;
    newIV[0] = (byte)(ret_value & 0xFF);
    newIV[1] = (byte)(ret_value >> 8 & 0xFF);
    newIV[2] = (byte)(ret_value >> 16 & 0xFF);
    newIV[3] = (byte)(ret_value >> 24 & 0xFF);
    return newIV;
  }
  
  public String toString() {
    return "IV: " + HexTool.toString(this.iv);
  }
}
