package client.status;

import java.io.Serializable;

public enum MonsterStatus implements Serializable {
    MS_IndiePdr(0),
    MS_IndieMdr(1),
    MS_IndieUNK(2),
    MS_IndieUNK2(3),
    MS_Pad(5),
    MS_Pdr(6),
    MS_Mad(7),
    MS_Mdr(8),
    MS_Acc(9),
    MS_Eva(10),
    MS_Speed(11),
    MS_Stun(12),
    MS_Freeze(13), //361 13 +1
    MS_Poison(14),
    MS_Seal(15),
    MS_Darkness(16),
    MS_Powerup(17),
    MS_Magicup(18),
    MS_PGuardup(19),
    MS_MGuardup(20),
    MS_PImmune(21),
    MS_MImmune(22),
    MS_Web(23),
    MS_Hardskin(24),
    MS_Ambush(25),
    MS_Venom(26),
    MS_Blind(27), //362 sniffer 27 ok
    MS_SealSkill(28),
    MS_Dazil(29),
    MS_PCounter(30),
    MS_MCounter(31),
    MS_RiseByToss(32),
    MS_BodyPressure(33),
    MS_Weakness(34),
    MS_Showdown(35), //362 ok 35 +1
    MS_MagicCrash(36), 
    MS_Puriaus(39), //362 sniffer 39 ok
    MS_AdddamParty(39),
    MS_HitCritDamR(40),
    MS_Lifting(41),
    MS_LandCrash(42),
    MS_DeadlyCharge(43),
    MS_Smite(44), //362 sniffer 44 ..
    MS_AdddamSkill(45),
    MS_Incizing(46), //362 sniffer 46 ..
    MS_DodgeBodyAttack(47), 
    MS_DebuffHealing(48),
    MS_AdddamSkill2(49), 
    MS_BodyAttack(50),
    MS_TempMoveAbility(51),
    MS_FixAtkRBuff(52),
    MS_FixdamRBuff(53),
    MS_ElementDarkness(54),
    MS_AreaInstallByHit(55),
    MS_BMageDebuff(56),
    MS_JaguarProvoke(57),
    MS_JaguarBleeding(58), //362 sniffer 58 ok
    MS_DarkLightning(59),
    MS_PinkbeanFlowerPot(60),
    MS_PvPHelenaMark(59),
    MS_PsychicLock(61),
    MS_PsychicLockCooltime(62),
    MS_PsychicGroundMark(63), //362 sniffer 63 ok
    MS_PowerImmune(64),
    MS_MultiPMDR(65),
    MS_UnkFlameWizard(66),
    MS_ElementResetBySummon(67),
    MS_CurseMark(68), //361 sniffer ok
    MS_Unk1(69),
    MS_DragonStrike(70),
    MS_Unk2(71),
    MS_BlessterDamage(72),
    MS_Unk4(73),
    MS_Unk5(74),
    MS_PopulatusTimer(75),
    MS_PopulatusRing(76),
    MS_PVPRude_Stack(79),
    MS_BahamutLightElemAddDam(80), //362 sniffer ok 80
    MS_BossPropPlus(81),
    MS_MultiDamSkill(82),
    MS_RWLiftPress(83),
    MS_RWChoppingHammer(84),
    MS_TimeBomb(85),
    MS_Treasure(86),
    MS_AddEffect(87),
    MS_TheSeedBuff(88),
    
    MS_CriticalBind_N(92),
    MS_HillaCount(94),
    MS_Invincible(95),
    MS_Explosion(96), //361 sniffer 96 ok
    MS_HangOver(97),
    MS_PopulatusInvincible(98),
    MS_UNK9(99),
    MS_Burned(100), //361 sniffer 100 ok
    MS_BalogDisable(101),
    MS_ExchangeAttack(102),
    MS_AddBuffStat(103),
    MS_LinkTeam(104),
    MS_SoulExplosion(105),
    MS_SeperateSoulP(106),
    MS_SeperateSoulC(107),
    MS_Ember(108),
    MS_TrueSight(109),
    MS_Laser(110),
    MS_StatResetSkill(111),
    MS_Unk10(112),
    MS_Unk11(113),
    MS_Unk12(114),
    MS_Unk13(115),
    MS_Unk14(116),
    MS_Unk15(117),
    MS_Unk16(118),
    MS_Unk17(119),
    MS_Unk18(120),
    MS_Unk19(121);

    static final long serialVersionUID = 0L;

    private final int i;

    private final int first;

    private final int flag;

    private final boolean end;

    private boolean stacked;

    MonsterStatus(int flag) {
        this.i = 1 << 31 - flag % 32;
        this.first = 4 - (byte) (int) Math.floor((flag / 32));
        this.flag = flag;
        this.end = false;
        setStacked(name().startsWith("MS_Indie"));
    }

    public int getPosition() {
        return this.first;
    }

    public boolean isEmpty() {
        return this.end;
    }

    public int getValue() {
        return this.i;
    }

    public int getFlag() {
        return this.flag;
    }

    public boolean isStacked() {
        return this.stacked;
    }

    public void setStacked(boolean stacked) {
        this.stacked = stacked;
    }

    public static boolean IsMovementAffectingStat(MonsterStatus skill) {
        switch (skill) {
            case MS_Stun:
            case MS_Speed:
            case MS_Freeze:
            case MS_RiseByToss:
            case MS_Lifting:
            case MS_Smite:
            case MS_TempMoveAbility:
            case MS_StatResetSkill:
            case MS_RWLiftPress:
            case MS_AdddamSkill2:
            case MS_PCounter:
            case MS_MCounter:
                return true;
        }
        return false;
    }
}
