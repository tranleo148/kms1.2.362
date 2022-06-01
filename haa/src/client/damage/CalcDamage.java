package client.damage;

import client.MapleCharacter;
import client.SkillFactory;
import handling.channel.handler.AttackInfo;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import server.SecondaryStatEffect;
import server.life.MapleMonster;
import tools.AttackPair;
import tools.Pair;

public class CalcDamage {
  CRand32 rndGenForCharacter = new CRand32();
  
  int invalidCount = 0;
  
  public void SetSeed(int seed1, int seed2, int seed3) {
    this.rndGenForCharacter.Seed(seed1, seed2, seed3);
  }
  
  private int numRand = 11;
  
  public List<Pair<Long, Boolean>> PDamage(MapleCharacter chr, AttackInfo attack) {
    List<Pair<Long, Boolean>> realDamageList = new ArrayList<>();
    for (AttackPair eachMob : attack.allDamage) {
      MapleMonster monster = chr.getMap().getMonsterByOid(eachMob.objectId);
      long[] rand = new long[this.numRand];
      for (int i = 0; i < this.numRand; i++)
        rand[i] = this.rndGenForCharacter.Random(); 
      byte index = 0;
      for (Pair<Long, Boolean> att : eachMob.attack) {
        double realDamage = 0.0D;
        boolean critical = false;
        index = (byte)(index + 1);
        index = (byte)(index + 1);
        long unkRand1 = rand[index % this.numRand];
        long maxDamage = 38L;
        long minDamage = 8L;
        index = (byte)(index + 1);
        double adjustedRandomDamage = RandomInRange(rand[index % this.numRand], maxDamage, minDamage);
        realDamage += adjustedRandomDamage;
        if (monster == null) {
          chr.dropMessageGM(6, "monster null");
          continue;
        } 
        if (monster.getStats() == null) {
          chr.dropMessageGM(6, "stat null");
          continue;
        } 
        SecondaryStatEffect skillEffect = null;
        if (attack.skill > 0)
          skillEffect = SkillFactory.getSkill(attack.skill).getEffect(chr.getTotalSkillLevel(attack.skill)); 
        if (skillEffect != null) {
          chr.dropMessageGM(6, "skillDamage : " + skillEffect.getDamage());
          realDamage = realDamage * skillEffect.getDamage() / 100.0D;
        } 
        index = (byte)(index + 1);
        if (RandomInRange(rand[index % this.numRand], 100L, 0L) < (chr.getStat()).critical_rate) {
          critical = true;
          int maxCritDamage = (chr.getStat()).critical_damage;
          index = (byte)(index + 1);
          int criticalDamageRate = (int)RandomInRange(rand[index % this.numRand], maxCritDamage, maxCritDamage);
          realDamage += criticalDamageRate / 100.0D * (int)realDamage;
        } 
        realDamageList.add(new Pair<>(Long.valueOf((long)realDamage), Boolean.valueOf(critical)));
      } 
    } 
    return realDamageList;
  }
  
  public double RandomInRange(long randomNum, long maxDamage, long minDamage) {
    double value;
    BigInteger ECX = new BigInteger("" + randomNum);
    BigInteger EAX = new BigInteger("1801439851");
    BigInteger multipled = ECX.multiply(EAX);
    long highBit = multipled.shiftRight(32).longValue();
    long rightShift = highBit >>> 22L;
    double newRandNum = randomNum - rightShift * 1.0E7D;
    if (minDamage != maxDamage) {
      if (minDamage > maxDamage) {
        long temp = maxDamage;
        maxDamage = minDamage;
        minDamage = temp;
      } 
      value = (maxDamage - minDamage) * newRandNum / 9999999.0D + minDamage;
    } else {
      value = maxDamage;
    } 
    return value;
  }
}
