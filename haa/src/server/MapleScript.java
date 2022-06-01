package server;

import java.util.ArrayList;
import java.util.List;

public class MapleScript {
  private boolean bSecondSpeaker;
  
  private boolean bPrev;
  
  private boolean bNext;
  
  private boolean bClose;
  
  private byte nMsgType;
  
  private byte bParam;
  
  private byte eColor;
  
  private short nLenMin;
  
  private short nLenMax;
  
  private int nSpeakerTypeId;
  
  private int nSecondSpeakerTemplateId;
  
  private int nSpeakerTemplateId;
  
  private int tWait;
  
  private int nDef;
  
  private int nMin;
  
  private int nMax;
  
  private int nMinInput;
  
  private int nMaxInput;
  
  private int nCorrect;
  
  private int nRemain;
  
  private String sScript;
  
  private String sTitle;
  
  private String sProblemText;
  
  private String sQuestion;
  
  private List<Object> objs = new ArrayList();
  
  public MapleScript(int nSpeakerTypeId, int nSpeakerTemplateId, byte nMsgType, byte bParam, byte eColor) {
    setnSpeakerTypeId(nSpeakerTypeId);
    setnSpeakerTemplateId(nSpeakerTemplateId);
    setbSecondSpeaker(false);
    setnMsgType(nMsgType);
    setbParam(bParam);
    seteColor(eColor);
  }
  
  public MapleScript(int nSpeakerTypeId, int nSpeakerTemplateId, int nSecondSpeakerTemplateId, byte nMsgType, byte bParam, byte eColor) {
    setnSpeakerTypeId(nSpeakerTypeId);
    setnSpeakerTemplateId(nSpeakerTemplateId);
    setnSecondSpeakerTemplateId(nSecondSpeakerTemplateId);
    setnMsgType(nMsgType);
    setbParam(bParam);
    seteColor(eColor);
  }
  
  public int nSpeakerTypeId() {
    return this.nSpeakerTypeId;
  }
  
  public void setnSpeakerTypeId(int nSpeakerTypeId) {
    this.nSpeakerTypeId = nSpeakerTypeId;
  }
  
  public int nSpeakerTemplateId() {
    return this.nSpeakerTemplateId;
  }
  
  public void setnSpeakerTemplateId(int nSpeakerTemplateId) {
    this.nSpeakerTemplateId = nSpeakerTemplateId;
  }
  
  public boolean bSecondSpeaker() {
    return this.bSecondSpeaker;
  }
  
  public void setbSecondSpeaker(boolean bSecondSpeaker) {
    this.bSecondSpeaker = bSecondSpeaker;
  }
  
  public int nSecondSpeakerTemplateId() {
    return this.nSecondSpeakerTemplateId;
  }
  
  public void setnSecondSpeakerTemplateId(int nSecondSpeakerTemplateId) {
    this.nSecondSpeakerTemplateId = nSecondSpeakerTemplateId;
  }
  
  public byte nMsgType() {
    return this.nMsgType;
  }
  
  public void setnMsgType(byte nMsgType) {
    this.nMsgType = nMsgType;
  }
  
  public byte bParam() {
    return this.bParam;
  }
  
  public void setbParam(byte bParam) {
    this.bParam = bParam;
  }
  
  public byte eColor() {
    return this.eColor;
  }
  
  public void seteColor(byte eColor) {
    this.eColor = eColor;
  }
  
  public String sScript() {
    return this.sScript;
  }
  
  public void setsScript(String sScript) {
    this.sScript = sScript;
  }
  
  public boolean bPrev() {
    return this.bPrev;
  }
  
  public void setbPrev(boolean bPrev) {
    this.bPrev = bPrev;
  }
  
  public boolean bNext() {
    return this.bNext;
  }
  
  public void setbNext(boolean bNext) {
    this.bNext = bNext;
  }
  
  public int tWait() {
    return this.tWait;
  }
  
  public void settWait(int tWait) {
    this.tWait = tWait;
  }
  
  public List<Object> getObjs() {
    return this.objs;
  }
  
  public void setObjs(List<Object> objs) {
    this.objs = objs;
  }
  
  public short nLenMin() {
    return this.nLenMin;
  }
  
  public void setnLenMin(short nLenMin) {
    this.nLenMin = nLenMin;
  }
  
  public short nLenMax() {
    return this.nLenMax;
  }
  
  public void setnLenMax(short nLenMax) {
    this.nLenMax = nLenMax;
  }
  
  public int nDef() {
    return this.nDef;
  }
  
  public void setnDef(int nDef) {
    this.nDef = nDef;
  }
  
  public int nMin() {
    return this.nMin;
  }
  
  public void setnMin(int nMin) {
    this.nMin = nMin;
  }
  
  public int nMax() {
    return this.nMax;
  }
  
  public void setnMax(int nMax) {
    this.nMax = nMax;
  }
  
  public boolean bClose() {
    return this.bClose;
  }
  
  public void setbClose(boolean bClose) {
    this.bClose = bClose;
  }
  
  public String sTitle() {
    return this.sTitle;
  }
  
  public void setsTitle(String sTitle) {
    this.sTitle = sTitle;
  }
  
  public String sProblemText() {
    return this.sProblemText;
  }
  
  public void setsProblemText(String sProblemText) {
    this.sProblemText = sProblemText;
  }
  
  public int nMinInput() {
    return this.nMinInput;
  }
  
  public void setnMinInput(int nMinInput) {
    this.nMinInput = nMinInput;
  }
  
  public int nMaxInput() {
    return this.nMaxInput;
  }
  
  public void setnMaxInput(int nMaxInput) {
    this.nMaxInput = nMaxInput;
  }
  
  public int nCorrect() {
    return this.nCorrect;
  }
  
  public void setnCorrect(int nCorrect) {
    this.nCorrect = nCorrect;
  }
  
  public int nRemain() {
    return this.nRemain;
  }
  
  public void setnRemain(int nRemain) {
    this.nRemain = nRemain;
  }
  
  public String sQuestion() {
    return this.sQuestion;
  }
  
  public void setsQuestion(String sQuestion) {
    this.sQuestion = sQuestion;
  }
}
