// 
// Decompiled by Procyon v0.5.36
// 
package client;

import static client.SecondaryStat.values;
import tools.Pair;
import java.util.Map;
import server.Randomizer;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public enum SecondaryStat implements Serializable {
    IndiePad(0),
    IndieMad(1),
    IndiePdd(2),
    IndieHp(3),
    IndieHpR(4),
    IndieMp(5),
    IndieMpR(6),
    IndieAcc(7),
    IndieEva(8),
    IndieJump(9),
    IndieSpeed(10),
    IndieAllStat(11),
    IndieAllStatR(12),
    IndieDodgeCriticalTime(13),
    IndieExp(14),
    IndieBooster(15),
    IndieFixedDamageR(16),
    PyramidStunBuff(17),
    PyramidFrozenBuff(18),
    PyramidFireBuff(19),
    PyramidBonusDamageBuff(20),
    IndieRelaxEXP(21),
    IndieStr(22),
    IndieDex(23),
    IndieInt(24),
    IndieLuk(25),
    IndieDamR(26),
    IndieScriptBuff(27),
    IndieMaxDamageR(28),
    IndieAsrR(29),
    IndieTerR(30),
    IndieCr(31),
    IndiePddR(32),
    IndieCD(33),
    IndieBDR(34),
    IndieStatR(35),
    IndieStance(36),
    IndieIgnoreMobPdpR(37),
    IndieEmpty(38),
    IndiePadR(39),
    IndieMadR(40),
    IndieEvaR(41),
    IndieDrainHP(42),
    IndiePmdR(43),
    IndieForceJump(44),
    IndieForceSpeed(45),
    IndieDamageReduce(46),
    IndieSummon(47),
    IndieReduceCooltime(48),
    IndieNotDamaged(49),
    IndieJointAttack(50),
    IndieKeyDownMoving(51),
    IndieUnkIllium(52),
    IndieEvasion(53),
    IndieShotDamage(54),
    IndieSuperStance(55),
    IndieGrandCross(56),
    IndieDamReduceR(57),
    IndieWickening1(58),
    IndieWickening2(59),
    IndieWickening3(30),
    IndieWickening4(61),
    IndieFloating(62),
    IndieUnk1(63),
    IndieUnk2(64),
    IndieDarkness(65),
    IndieBlockSkill(66),
    IndieBarrier(68),
    IndieNDR(70),
    Indie_STAT_COUNT(72),
    Pad(73),
    Pdd(74),
    Mad(75),
    Acc(76),
    Eva(77),
    Craft(78),
    Speed(79),
    Jump(80),
    MagicGaurd(81),
    DarkSight(82),
    Booster(83),
    PowerGaurd(84),
    MaxHP(85),
    MaxMP(86),
    Invincible(87),
    SoulArrow(88),
    Stun(89),
    Poison(90),
    Seal(91),
    Darkness(92),
    ComboCounter(93),
    BlessedHammer(94),
    BlessedHammer2(95),
    SnowCharge(96),
    HolySymbol(97),
    MesoUp(98),
    ShadowPartner(99),
    Steal(100), //362 new
    PickPocket(101),
    Murderous(102), //362 new
    Thaw(103),
    Weakness(104),
    Curse(105),
    Slow(106),
    Morph(107),
    Recovery(108), // 361 sniffer ok
    BasicStatUp(109),
    Stance(110),
    SharpEyes(111),
    ManaReflection(112),
    Attract(113),
    NoBulletConsume(114),
    Infinity(115),
    AdvancedBless(116),
    Illusion(117),
    Blind(118),
    Concentration(119),
    BanMap(120),
    MaxLevelBuff(121),
    MesoUpByItem(122),
    WealthOfUnion(123),
    RuneOfGreed(124),
    Ghost(125),
    Barrier(126),
    ReverseInput(127),
    ItemUpByItem(128),
    RespectPImmune(129),
    RespectMImmune(130),
    DefenseAtt(131),
    DefenseState(132),
    DojangBerserk(133),
    DojangInvincible(134),
    DojangShield(135),
    SoulMasterFinal(136),
    WindBreakerFinal(137),
    ElementalReset(138),
    HideAttack(139),
    EventRate(140),
    AranCombo(141),
    AuraRecovery(142),
    UnkBuffStat1(143),
    BodyPressure(144),
    RepeatEffect(145),
    ExpBuffRate(146),
    StopPortion(147),
    StopMotion(148),
    Fear(149),
    HiddenPieceOn(150),
    MagicShield(151),
    SoulStone(152),
    Flying(154),
    Frozen(155),
    AssistCharge(156),
    Enrage(157),
    DrawBack(158),
    NotDamaged(159),
    FinalCut(160),
    HowlingParty(161),
    BeastFormDamage(162),
    Dance(163),
    EnhancedMaxHp(164),
    EnhancedMaxMp(165),
    EnhancedPad(166),
    EnhancedMad(167),
    EnhancedPdd(168),
    PerfectArmor(169),
    UnkBuffStat2(170),
    IncreaseJabelinDam(171),
    PinkbeanMinibeenMove(172),
    Sneak(173),
    Mechanic(174),
    BeastFormMaxHP(175),
    DiceRoll(176),
    BlessingArmor(177),
    DamR(178),
    TeleportMastery(179),
    CombatOrders(180),
    Beholder(181),
    DispelItemOption(182),
    Inflation(183),
    OnixDivineProtection(184),
    Web(185),
    Bless(186),
    TimeBomb(187),
    DisOrder(188),
    Thread(189),
    Team(190),
    Explosion(191),
    BuffLimit(192),
    STR(193),
    INT(194),
    DEX(195),
    LUK(196),
    DispelByField(197),
    DarkTornado(198),
    PVPDamage(199),
    PvPScoreBonus(200),
    PvPInvincible(201),
    PvPRaceEffect(202),
    WeaknessMdamage(203),
    Frozen2(204),
    PvPDamageSkill(205),
    AmplifyDamage(206),
    Shock(207),
    InfinityForce(208),
    IncMaxHP(209),
    IncMaxMP(210),
    HolyMagicShell(211), // 361 sniffer ok
    KeyDownTimeIgnore(212),
    ArcaneAim(213),
    MasterMagicOn(214),
    Asr(215),
    Ter(216),
    DamAbsorbShield(217),
    DevilishPower(218), // 361 sniffer ok
    Roulette(219),
    SpiritLink(220),
    AsrRByItem(221),
    Event(222),
    CriticalIncrease(223),
    DropItemRate(224),
    DropRate(225),
    ItemInvincible(226),
    Awake(227),
    ItemCritical(228),
    ItemEvade(229),
    Event2(230),
    DrainHp(231),
    IncDefenseR(232),
    IncTerR(233),
    IncAsrR(234),
    DeathMark(235),
    Infiltrate(236),
    Lapidification(237),
    VenomSnake(238),
    CarnivalAttack(239),
    CarnivalDefence(240),
    CarnivalExp(241),
    SlowAttack(242),
    PyramidEffect(243),
    KillingPoint(244), //이거사라진것같음
    UnkBuffStat3(244),
    KeyDownMoving(245),
    IgnoreTargetDEF(246),
    ReviveOnce(247), //361 sniffer 247 ok
    Invisible(248),
    EnrageCr(249),
    EnrageCrDamMin(250),
    Judgement(251),
    DojangLuckyBonus(252),
    PainMark(253),
    Magnet(254),
    MagnetArea(255),
    GuidedArrow(256),
    UnkBuffStat4(257),
    BlessMark(258),
    BonusAttack(259),
    UnkBuffStat5(260),
    FlowOfFight(261),
    GrandCrossSize(262),
    LuckOfUnion(263),
    PinkBeanFighting(264),
    VampDeath(266),
    BlessingArmorIncPad(267),
    KeyDownAreaMoving(268),
    Larkness(269),
    StackBuff(270),
    BlessOfDarkness(271),
    AntiMagicShell(272), // 361 sniffer ok
    LifeTidal(273),
    HitCriDamR(274),
    SmashStack(275),
    RoburstArmor(276),
    ReshuffleSwitch(277),
    SpecialAction(278),
    VampDeathSummon(279),
    StopForceAtominfo(280),
    SoulGazeCriDamR(281),
    Affinity(282),
    PowerTransferGauge(283),
    AffinitySlug(284), // 361 sniffer ok
    Trinity(285), // 361 sniffer ok
    IncMaxDamage(286),
    BossShield(287),
    MobZoneState(288),
    GiveMeHeal(289),
    TouchMe(290),
    Contagion(291),
    ComboUnlimited(292),
    SoulExalt(293), // 361 sniffer ok
    IgnorePCounter(294),
    IgnoreAllCounter(295),
    IgnorePImmune(296),
    IgnoreAllImmune(297),
    UnkBuffStat6(298),
    FireAura(299),
    VengeanceOfAngel(300), //361 sniffer ok
    HeavensDoor(301), //361 sniffer ok
    Preparation(302),
    BullsEye(303),
    IncEffectHPPotion(304),
    IncEffectMPPotion(305),
    BleedingToxin(306),
    IgnoreMobDamR(307),
    Asura(308),
    MegaSmasher(309),
    FlipTheCoin(310),
    UnityOfPower(311),
    Stimulate(312),
    ReturnTeleport(313),
    DropRIncrease(314),
    IgnoreMobPdpR(315),
    BdR(316),
    CapDebuff(317),
    Exceed(318),
    DiabloicRecovery(319),
    FinalAttackProp(320),
    ExceedOverload(321),
    OverloadCount(322),
    Buckshot(323),
    FireBomb(324),
    HalfstatByDebuff(325),
    SurplusSupply(326),
    SetBaseDamage(327),
    EvaR(328),
    NewFlying(329),
    AmaranthGenerator(330),
    OnCapsule(331),
    CygnusElementSkill(332),
    StrikerHyperElectric(333),
    EventPointAbsorb(334),
    EventAssemble(335),
    StormBringer(336),
    AccR(337),
    DexR(338),
    Albatross(339), //361 sniffer ok
    Translucence(340),
    PoseType(341),
    LightOfSpirit(342),
    ElementSoul(343),
    GlimmeringTime(344),
    SolunaTime(345),
    WindWalk(346),
    SoulMP(347),
    FullSoulMP(348),
    SoulSkillDamageUp(349),
    ElementalCharge(350),
    Listonation(351), //361 sniffer ok
    CrossOverChain(352),
    ChargeBuff(353),
    ReincarnationFull(354),
    Reincarnation(355),
    ReincarnationAccept(356),
    ChillingStep(357),
    DotBasedBuff(358),
    BlessingAnsanble(359), //361 sniffer +1
    ComboCostInc(360),
    ExtremeArchery(361),
    NaviFlying(362), //361 sniffer +1 362
    QuiverCatridge(363),
    AdvancedQuiver(364),
    UserControlMob(365),
    ImmuneBarrier(365),
    ArmorPiercing(367),
    CardinalMark(368),
    QuickDraw(369),
    BowMasterConcentration(370),
    TimeFastABuff(371),
    TimeFastBBuff(372),
    GatherDropR(373),
    AimBox2D(374),
    TrueSniping(375),
    DebuffTolerance(376),
    UnkBuffStat8(377),
    DotHealHPPerSecond(378),
    DotHealMPPerSecond(379),
    SpiritGuard(380),
    PreReviveOnce(381), //361 sniffer ok
    SetBaseDamageByBuff(382),
    LimitMP(383),
    ReflectDamR(384),
    ComboTempest(385),
    MHPCutR(386),
    MMPCutR(387),
    SelfWeakness(388),
    ElementDarkness(389),
    FlareTrick(390),
    Ember(391),
    Dominion(392), // 361 sniffer 392 ok
    SiphonVitality(393),
    DarknessAscension(394),
    BossWaitingLinesBuff(395),
    DamageReduce(396),
    ShadowServant(397),
    ShadowIllusion(398),
    KnockBack(399),
    IgnisRore(400),
    ComplusionSlant(401),
    JaguarSummoned(402),
    JaguarCount(403),
    SSFShootingAttack(404),
    DevilCry(405),
    ShieldAttack(406),
    DarkLighting(407),
    AttackCountX(408),
    BMageDeath(409),
    BombTime(410),
    NoDebuff(411),
    BattlePvP_Mike_Shield(412),
    BattlePvP_Mike_Bugle(413),
    AegisSystem(414),
    SoulSeekerExpert(415),
    HiddenPossession(416),
    ShadowBatt(417),
    MarkofNightLord(418),
    WizardIgnite(419),
    FireBarrier(420),
    ChangeFoxMan(421),
    HolyUnity(422),
    DemonFrenzy(423),
    ShadowSpear(424),
    DemonDamageAbsorbShield(425),
    Ellision(426),
    QuiverFullBurst(427),
    LuminousPerfusion(428),
    WildGrenadier(429),
    GrandCross(430),
    BattlePvP_Helena_Mark(432),
    BattlePvP_Helena_WindSpirit(433),
    BattlePvP_LangE_Protection(434),
    BattlePvP_LeeMalNyun_ScaleUp(435),
    BattlePvP_Revive(436),
    PinkbeanAttackBuff(437),
    PinkbeanRelax(438),
    PinkbeanRollingGrade(439),
    PinkbeanYoYoStack(440),
    RandAreaAttack(441),
    NextAttackEnhance(442),
    BeyondNextAttackProb(443),
    AranCombotempastOption(444),
    NautilusFinalAttack(445),
    ViperTimeLeap(446),
    RoyalGuardState(447),
    RoyalGuardPrepare(448),
    MichaelSoulLink(449),
    MichaelProtectofLight(450),
    TryflingWarm(451), //362 sniffer ok
    AddRange(452),
    KinesisPsychicPoint(453),
    KinesisPsychicOver(454),
    KinesisIncMastery(455),
    KinesisPsychicEnergeShield(456), //362 sniffer 456 
    BladeStance(457),
    DebuffActiveHp(458),
    DebuffIncHp(459),
    MortalBlow(460),
    SoulResonance(461),
    Fever(462),
    SikSin(463),
    TeleportMasteryRange(464),
    FixCooltime(465),
    IncMobRateDummy(466),
    AdrenalinBoost(467),
    AranSmashSwing(468),
    AranDrain(469),
    AranBoostEndHunt(470),
    HiddenHyperLinkMaximization(471),
    RWCylinder(472), // 361 sniffer 473 ok
    RWCombination(473),
    RWUnk(474),
    RwMagnumBlow(475),
    RwBarrier(476),
    RWBarrierHeal(477),
    RWMaximizeCannon(478),
    RWOverHeat(479),
    UsingScouter(480),
    RWMovingEvar(481),
    Stigma(482),
    InstallMaha(483),
    CooldownHeavensDoor(484), // 361 sniffer ok
    CooldownSkill(485),
    CooldownRune(486),
    PinPointRocket(487),
    Transform(488),
    EnergyBurst(489),
    Striker1st(490),
    BulletParty(491), // 361 sniffer ok
    SelectDice(492),
    Pray(493),
    ChainArtsFury(494),
    DamageDecreaseWithHP(495),
    PinkbeanYoYoAttackStack(496),
    AuraWeapon(497),
    OverloadMana(498),
    RhoAias(499),
    PsychicTornado(500), //361 sniffer ok
    SpreadThrow(501),
    HowlingGale(502),
    VMatrixStackBuff(503),
    MiniCannonBall(504), //362 sniffer 504 -1
    ShadowAssult(505),
    MultipleOption(506),
    UnkBuffStat15(507),
    BlitzShield(508),
    SplitArrow(509), //361 sniffer 510
    FreudsProtection(510),
    Overload(511),
    Spotlight(512), //361 sniffer 513
    KawoongDebuff(513),
    WeaponVariety(514),
    GloryWing(515),
    ShadowerDebuff(516),
    OverDrive(517),
    Etherealform(518),
    ReadyToDie(519),
    CriticalReinForce(520),
    CurseOfCreation(521),
    CurseOfDestruction(522),
    BlackMageDebuff(523),
    BodyOfSteal(524),
    PapulCuss(525),
    PapulBomb(526),
    HarmonyLink(527),
    FastCharge(528),
    UnkBuffStat20(529),
    CrystalBattery(530), // 361 sniffer +1
    Deus(531),
    UnkBuffStat21(532),
    BattlePvP_Rude_Stack(533),
    UnkBuffStat23(534),
    UnkBuffStat24(535),
    UnkBuffStat25(536),
    SpectorGauge(537), //361 sniffer 538 ok
    SpectorTransForm(538),
    PlainBuff(539),
    ScarletBuff(540),
    GustBuff(541),
    AbyssBuff(542),
    ComingDeath(543),
    FightJazz(544),
    ChargeSpellAmplification(545),
    InfinitySpell(546),
    MagicCircuitFullDrive(547),
    LinkOfArk(548),
    MemoryOfSource(549),
    UnkBuffStat26(550),
    WillPoison(551),
    UnkBuffStat27(552),
    UnkBuffStat28(553),
    CooltimeHolyMagicShell(554), // 361 sinffer +1 555
    Striker3rd(555),
    ComboInstict(556),
    WindWall(557),
    UnkBuffStat29(558),
    SwordOfSoulLight(559),
    MarkOfPhantomStack(560),
    MarkOfPhantomDebuff(561),
    UnkBuffStat30(562),
    UnkBuffStat31(563),
    UnkBuffStat32(564),
    UnkBuffStat33(565),
    UnkBuffStat34(566),
    EventSpecialSkill(567),
    PmdReduce(568),
    ForbidOpPotion(569),
    ForbidEquipChange(570),
    YalBuff(571), //361 sniffer 572 +1 ok
    IonBuff(572),
    UnkBuffStat35(573),
    UnkBuffStat36(574),
    DefUp(575),
    Protective(576),
    BloodFist(577),
    AncientGuidance(578),
    BattlePvP_Wonky_ChargeA(579),
    BattlePvP_Wonky_Charge(580),
    BattlePvP_Wonky_Awesome(581),
    UnkBuffStat42(582),
    UnkBuffStat43(583),
    UnkBuffStat44(584),
    Bless5th(587),
    PinkBeanMatroCyca(588),
    UnkBuffStat46(589),
    UnkBuffStat47(590),
    UnkBuffStat48(591),
    UnkBuffStat49(592),
    UnkBuffStat50(593),
    PapyrusOfLuck(594),
    HoyoungThirdProperty(595),
    TidalForce(596),
    Alterego(597),
    AltergoReinforce(598),
    ButterflyDream(599),
    Sungi(600), //361 sniffer ok
    SageWrathOfGods(601),
    EmpiricalKnowledge(602),
    UnkBuffStat52(603),
    UnkBuffStat53(604),
    Graffiti(605),
    DreamDowon(606),
    WillofSwordStrike(607),
    AdelGauge(608),
    Creation(609),
    Dike(610),
    Wonder(611),
    Restore(612),
    Novility(613),
    AdelResonance(614),
    RuneOfPure(615),
    RuneOfTransition(616),
    DuskDarkness(617),
    YellowAura(618),
    DrainAura(619),
    BlueAura(620),
    DarkAura(621),
    DebuffAura(622),
    UnionAura(623),
    IceAura(624),
    KnightsAura(625),
    ZeroAuraStr(626),
    ZeroAuraSpd(627),
    IncarnationAura(628),
    AdventOfGods(629),
    Revenant(630),
    RevenantDamage(631),
    SilhouetteMirage(632),
    BlizzardTempest(633),
    PhotonRay(634), //361 sniffer ok
    AbyssalLightning(635),
    Striker4th(636),
    RoyalKnights(637),
    SalamanderMischief(638),
    LawOfGravity(639),
    RepeatingCrossbowCatridge(640),
    CrystalGate(641),
    ThrowBlasting(642),
    SageElementalClone(643),
    DarknessAura(644),
    WeaponVarietyFinale(645),
    LiberationOrb(646),
    LiberationOrbActive(647),
    EgoWeapon(648),
    RelikUnboundDischarge(649),
    MoraleBoost(650),
    AfterImageShock(651),
    Malice(652),
    Possession(653),
    DeathBlessing(654),
    ThanatosDescent(655),
    RemainIncense(656),
    GripOfAgony(657),
    DragonPang(658),
    SerenDebuffs(659),
    SerenDebuff(660),
    SerenDebuffUnk(661),
    PriorPryperation(662),
    AdrenalinBoostActive(668),
    YetiAnger(670),
    YetiAngerMode(671),
    YetiSpicy(672),
    YetiFriendsPePe(673),
    PinkBeanMagicShow(674),
    용맥_읽기(676),
    산의씨앗(677),
    산_무등(678),
    흡수_강(679),
    흡수_바람(680),
    흡수_해(681),
    자유로운용맥(682),
    Lotus(684),
    NatureFriend(685),
    //리마스터 new
    SeaSerpent(688),
    SerpentStone(689),
    SerpentScrew(690),
    HolyWater(695),
    Triumph(696),
    FlashMirage(697),
    HolyBlood(698),
    
    EnergyCharged(699),
    DashJump(700),
    DashSpeed(701),
    RideVehicle(702),
    PartyBooster(703),
    GuidedBullet(704),
    Undead(705),
    RideVehicleExpire(706),
    RelikGauge(707),
    Grave(708),
    CountPlus1(709);

    private static final long serialVersionUID = 0L;
    private int buffstat;
    private int first;
    private boolean stacked;
    private int disease;
    private int flag;
    private int x;
    private int y;

    private SecondaryStat(final int flag) {
        this.stacked = false;
        this.buffstat = 1 << 31 - flag % 32;
        this.setFirst(31 - (byte) Math.floor(flag / 32));
        this.setStacked(this.name().startsWith("Indie") || this.name().startsWith("Pyramid"));
        this.setFlag(flag);
    }

    private SecondaryStat(final int flag, final int disease) {
        this.stacked = false;
        this.buffstat = 1 << 31 - flag % 32;
        this.setFirst(31 - (byte) Math.floor(flag / 32));
        this.setStacked(this.name().startsWith("Indie") || this.name().startsWith("Pyramid"));
        this.setFlag(flag);
        this.disease = disease;
    }

    private SecondaryStat(final int flag, final int first, final int disease) {
        this.stacked = false;
        this.buffstat = 1 << 31 - flag % 32;
        this.setFirst(first);
        this.setFlag(flag);
        this.disease = disease;
    }

    public final int getPosition() {
        return this.getFirst();
    }

    public final int getPosition(final boolean stacked) {
        if (!stacked) {
            return this.getFirst();
        }
        switch (this.getFirst()) {
            case 16: {
                return 0;
            }
            case 15: {
                return 1;
            }
            case 14: {
                return 2;
            }
            case 13: {
                return 3;
            }
            case 12: {
                return 4;
            }
            case 11: {
                return 5;
            }
            case 10: {
                return 6;
            }
            case 9: {
                return 7;
            }
            case 8: {
                return 8;
            }
            case 7: {
                return 9;
            }
            case 6: {
                return 10;
            }
            case 5: {
                return 11;
            }
            case 4: {
                return 12;
            }
            case 3: {
                return 13;
            }
            case 2: {
                return 14;
            }
            case 1: {
                return 15;
            }
            case 0: {
                return 16;
            }
            default: {
                return 0;
            }
        }
    }

    public final int getValue() {
        return this.getBuffstat();
    }

    public final boolean canStack() {
        return this.isStacked();
    }

    public int getDisease() {
        return this.disease;
    }

    public int getX() {
        return this.x;
    }

    public void setX(final int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(final int y) {
        this.y = y;
    }

    public static final SecondaryStat getByFlag(final int flag) {
        for (final SecondaryStat d : values()) {
            if (d.getFlag() == flag) {
                return d;
            }
        }
        return null;
    }

    public static final SecondaryStat getBySkill(final int skill) {
        for (final SecondaryStat d : values()) {
            if (d.getDisease() == skill) {
                return d;
            }
        }
        return null;
    }

    public static final List<SecondaryStat> getUnkBuffStats() {
        final List<SecondaryStat> stats = new ArrayList<SecondaryStat>();
        for (final SecondaryStat d : values()) {
            if (d.name().startsWith("UnkBuff")) {
                stats.add(d);
            }
        }
        return stats;
    }

    public static final SecondaryStat getRandom() {
        SecondaryStat dis = null;
        Block_1:
        while (true) {
            final SecondaryStat[] values = values();
            for (int length = values.length, i = 0; i < length; ++i) {
                dis = values[i];
                if (Randomizer.nextInt(values().length) == 0) {
                    break Block_1;
                }
            }
        }
        return dis;
    }

    public static boolean isEncode4Byte(final Map<SecondaryStat, Pair<Integer, Integer>> statups) {
        final SecondaryStat[] array;
        final SecondaryStat[] stats = array = new SecondaryStat[]{SecondaryStat.CarnivalDefence, SecondaryStat.SpiritLink, SecondaryStat.DojangLuckyBonus, SecondaryStat.SoulGazeCriDamR, SecondaryStat.PowerTransferGauge, SecondaryStat.ReturnTeleport, SecondaryStat.ShadowPartner, SecondaryStat.SetBaseDamage, SecondaryStat.QuiverCatridge, SecondaryStat.ImmuneBarrier, SecondaryStat.NaviFlying, SecondaryStat.Dance, SecondaryStat.AranSmashSwing, SecondaryStat.DotHealHPPerSecond, SecondaryStat.SetBaseDamageByBuff, SecondaryStat.MagnetArea, SecondaryStat.MegaSmasher, SecondaryStat.RwBarrier, SecondaryStat.VampDeath, SecondaryStat.RideVehicle, SecondaryStat.RideVehicleExpire, SecondaryStat.Protective, SecondaryStat.BlitzShield, SecondaryStat.UnkBuffStat2, SecondaryStat.HolyUnity, SecondaryStat.BattlePvP_Rude_Stack};
        for (final SecondaryStat stat : array) {
            if (statups.containsKey(stat)) {
                return true;
            }
        }
        return false;
    }

    public boolean isSpecialBuff() {
        switch (this) {
            case EnergyCharged:
            case DashSpeed:
            case DashJump:
            case RideVehicle:
            case PartyBooster:
            case GuidedBullet:
            case Undead:
            case RideVehicleExpire:
            case RelikGauge:
            case Grave: {
                return true;
            }
            default: {
                return false;
            }
        }
    }

    public int getFlag() {
        return this.flag;
    }

    public void setFlag(final int flag) {
        this.flag = flag;
    }

    public boolean isItemEffect() {
        switch (this) {
            case DropItemRate:
            case ItemUpByItem:
            case MesoUpByItem:
            case ExpBuffRate:
            case WealthOfUnion:
            case LuckOfUnion: {
                return true;
            }
            default: {
                return false;
            }
        }
    }

    public boolean SpectorEffect() {
        switch (this) {
            case SpectorGauge:
            case SpectorTransForm:
            case PlainBuff:
            case ScarletBuff:
            case GustBuff:
            case AbyssBuff: {
                return true;
            }
            default: {
                return false;
            }
        }
    }

    public int getBuffstat() {
        return this.buffstat;
    }

    public void setBuffstat(final int buffstat) {
        this.buffstat = buffstat;
    }

    public int getFirst() {
        return this.first;
    }

    public void setFirst(final int first) {
        this.first = first;
    }

    public boolean isStacked() {
        return this.stacked;
    }

    public void setStacked(final boolean stacked) {
        this.stacked = stacked;
    }
}
