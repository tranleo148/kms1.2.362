package handling.channel.handler;

import client.MapleCharacter;
import client.MapleClient;
import client.SecondaryStat;
import client.SkillFactory;
import client.inventory.MapleInventoryType;
import client.status.MonsterStatus;
import client.status.MonsterStatusEffect;
import constants.GameConstants;
import handling.channel.ChannelServer;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import scripting.EventInstanceManager;
import server.MapleInventoryManipulator;
import server.Obstacle;
import server.Randomizer;
import server.Timer;
import server.field.boss.MapleBossManager;
import server.field.boss.demian.MapleFlyingSword;
import server.field.boss.lotus.MapleEnergySphere;
import server.field.boss.lucid.Butterfly;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.life.MobAttack;
import server.life.MobAttackInfo;
import server.life.MobAttackInfoFactory;
import server.life.MobSkill;
import server.life.MobSkillFactory;
import server.maps.AnimatedMapleMapObject;
import server.maps.MapleMap;
import server.maps.MapleMapObject;
import server.maps.MapleMist;
import server.maps.MapleNodes;
import server.movement.LifeMovementFragment;
import tools.Pair;
import tools.Triple;
import tools.data.LittleEndianAccessor;
import tools.packet.CField;
import tools.packet.CWvsContext;
import tools.packet.MobPacket;
import tools.packet.SLFCGPacket;

public class MobHandler {

    public static final void MoveMonster(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr) {
        /*   75 */ long time = System.currentTimeMillis();
        /*   76 */ String tr = slea.toString();
        try {
            /*   78 */ int oid = slea.readInt();
            /*   79 */ if (chr == null || chr.getMap() == null) {
                return;
            }
            /*   82 */ MapleMonster monster = chr.getMap().getMonsterByOid(oid);
            /*   83 */ if (monster == null) {
                return;
            }
            /*   86 */ short moveid = slea.readShort();
            /*   87 */ monster.setCustomInfo(99991, moveid, 0);
            /*   88 */ byte bOption = slea.readByte();
            /*   89 */ int actionAndDir = slea.readByte();
            /*   90 */ long targetInfo = slea.readLong();
            /*   91 */ int skillId = (int) (targetInfo & 0xFFFFL);
            /*   92 */ int skillLevel = (int) (targetInfo >> 16L & 0xFFFFL);
            /*   93 */ int option = (int) (targetInfo >> 32L & 0xFFFFL);
            /*   94 */ boolean setNextSkill = true;
            /*   95 */ int action = actionAndDir;
            /*   96 */ if (action < 0) {
                /*   97 */ action = -1;
            } else {
                /*   99 */ action >>= 1;
            }

            /*  104 */ if ((monster.getId() == 8645009 || monster.getId() == 8645066) && ( /*  105 */actionAndDir == 30 || actionAndDir == 31)) {
                /*  106 */ chr.getMap().broadcastMessage(CField.enforceMSG("친위대장 듄켈 : 한번 받아 봐라! 별조차 갈라버리는 내 궁극의 검기를!", 272, 2000));
            }

            /*  109 */ boolean changeController = ((bOption & 0x10) == 16);
            /*  110 */ boolean movingAttack = ((bOption & 0x1) == 1);

            /*  113 */ slea.readByte();
            /*  114 */ slea.readByte();
            /*  115 */ int count = slea.readByte();
            /*  116 */ List<Point> multiTargetForBall = new ArrayList<>();
            /*  117 */ for (int i = 0; i < count; i++) {
                /*  119 */ multiTargetForBall.add(new Point(slea.readShort(), slea.readShort()));
            }

            /*  122 */ count = slea.readByte();
            /*  123 */ List<Short> randTimeForAreaAttack = new ArrayList<>();
            /*  124 */ for (int j = 0; j < count; j++) {
                /*  126 */ randTimeForAreaAttack.add(Short.valueOf(slea.readShort()));
            }
            /*  128 */ int type2 = slea.readInt();
            /*  129 */ if (type2 == 1) {
                /*  130 */ slea.skip(44);
            }
            /*  132 */ slea.readByte();

            /*  134 */ slea.readInt();
            /*  135 */ slea.readInt();
            /*  136 */ slea.readInt();
            /*  137 */ slea.readInt();
            /*  138 */ slea.readInt();
            /*  139 */ slea.readByte();

            /*  141 */ int tEncodedGatherDuration = slea.readInt();

            /*  143 */ Point startPos = slea.readPos();
            /*  144 */ Point startWobble = slea.readPos();
            /*  145 */ List<LifeMovementFragment> res = MovementParse.parseMovement(slea, 2);
            /*  146 */ boolean stop = false;

            /*  152 */ slea.readByte();
            /*  153 */ slea.readInt();
            /*  154 */ slea.readInt();
            /*  155 */ slea.readInt();
            /*  156 */ slea.readInt();
            /*  157 */ slea.readInt();
            /*  158 */ slea.readByte();
            /*  159 */ slea.readInt();
            /*  160 */ slea.readByte();
            /*  161 */ slea.readByte();
            /*  162 */ boolean cannotUseSkill = (slea.readByte() == 1);

            /*  164 */ MapleBossManager.changePhase(monster);

            /*  166 */ MapleBossManager.setBlockAttack(monster);
            /*  167 */ if (!monster.isSkillForbid()) {
                /*  168 */ if (action >= 13 && action <= 29) {
                    /*  169 */ int attackIdx = action - 13;

                    /*  171 */ if (attackIdx < monster.getStats().getAttacks().size()) {
                        /*  172 */ MobAttack attack = monster.getStats().getAttacks().get(attackIdx);

                        /*  175 */ if (attack.getAfterAttack() >= 0) {
                            /*  176 */ chr.dropMessageGM(-8, "mobId : " + monster.getId() + " afterattack : " + attack.getAfterAttack() + " attackcount : " + attack.getAfterAttackCount() + " attackidx : " + action);
                            /*  177 */ monster.getMap().broadcastMessage(MobPacket.setAfterAttack(oid, attack.getAfterAttack(), (attack.getAfterAttack() < 0) ? 0 : attack.getAfterAttackCount(), action, ((actionAndDir & 0x1) != 0)));
                        } /*  179 */ else if ((attackIdx == 0 || attackIdx == 1 || attackIdx == 6 || attackIdx == 7 || attackIdx == 12) && (monster.getId() == 8930000 || monster.getId() == 8930100)) {
                            /*  180 */ List<Point> pos = new ArrayList<>();
                            /*  181 */ pos.add(new Point(810, 443));
                            /*  182 */ pos.add(new Point(-2190, 443));
                            /*  183 */ pos.add(new Point(-1690, 443));
                            /*  184 */ pos.add(new Point(560, 443));
                            /*  185 */ pos.add(new Point(-190, 443));
                            /*  186 */ pos.add(new Point(-690, 443));
                            /*  187 */ pos.add(new Point(-1940, 443));
                            /*  188 */ pos.add(new Point(1310, 443));
                            /*  189 */ pos.add(new Point(-1190, 443));
                            /*  190 */ pos.add(new Point(1060, 443));
                            /*  191 */ pos.add(new Point(-940, 443));
                            /*  192 */ pos.add(new Point(-1440, 443));
                            /*  193 */ pos.add(new Point(1560, 443));
                            /*  194 */ pos.add(new Point(-440, 443));
                            /*  195 */ monster.getMap().broadcastMessage(MobPacket.dropStone("DropStone", pos));
                        }

                        /*  199 */ for (Triple<Integer, Integer, Integer> skill : (Iterable<Triple<Integer, Integer, Integer>>) attack.getSkills()) {
                            /*  200 */ MobSkill msi = MobSkillFactory.getMobSkill(((Integer) skill.left).intValue(), ((Integer) skill.mid).intValue());
                            /*  201 */ if (msi != null) {
                                /*  202 */ if (((Integer) skill.right).intValue() > 0) {
                                    /*  203 */ msi.setMobSkillDelay(c.getPlayer(), monster, ((Integer) skill.right).intValue(), (short) 0, ((actionAndDir & 0x1) != 0));
                                    continue;
                                    /*  204 */                                }
                                if (msi.getSkillAfter() > 0) {
                                    /*  205 */ msi.setMobSkillDelay(c.getPlayer(), monster, msi.getSkillAfter(), (short) 0, ((actionAndDir & 0x1) != 0));
                                    continue;
                                }
                                /*  207 */ if (option > 0) {
                                    /*  208 */ Timer.ShowTimer.getInstance().schedule(() -> {
                                        if (monster.isAlive()) {
                                            chr.dropMessageGM(-11, "op1 : " + option);
                                            msi.applyEffect(chr, monster, true, ((actionAndDir & 0x1) != 0));
                                        }
                                    }, option);

                                    continue;
                                }

                                /*  215 */ if (monster.isAlive()) {
                                    /*  216 */ msi.applyEffect(chr, monster, true, ((actionAndDir & 0x1) != 0));
                                }
                            }

                        }

                    }
                    /*  223 */                } else if (action >= 30 && action <= 46 && monster.getEliteGrade() <= -1) {
                    /*  224 */ if (monster.hasSkill(skillId, skillLevel)) {
                        /*  225 */ MobSkill msi = monster.getStats().getSkill(skillId, skillLevel);
                        /*  226 */ if (msi != null) {
                            /*  227 */ if (!msi.checkDealyBuff(chr, monster)) {
                                /*  228 */ monster.setNextSkill(0);
                                /*  229 */ monster.setNextSkillLvl(0);
                            } else {
                                /*  231 */ if (msi.getSkillAfter() > 0) {
                                    /*  232 */ msi.setMobSkillDelay(c.getPlayer(), monster, msi.getSkillAfter(), (short) 0, ((actionAndDir & 0x1) != 0));

                                    /*  234 */ monster.setNextSkill(0);
                                    /*  235 */ monster.setNextSkillLvl(0);
                                    /*  236 */ if (msi.getOtherSkillID() > 0) {
                                        /*  237 */ Timer.ShowTimer.getInstance().schedule(() -> {
                                            monster.setNextSkill(msi.getOtherSkillID());
                                            monster.setNextSkillLvl(msi.getOtherSkillLev());
                                        },(msi.getSkillAfter() + 5000
                                    
                                    
                                  ));
                   }
/*  242 */                 } else if (msi.onlyOnce() && monster.getLastSkillUsed(skillId, skillLevel) != 0L) {
                                    /*  243 */ if (chr.isGM()) {
                                        /*  244 */ chr.dropMessage(-8, monster.getId() + " 몬스터의 " + skillId + " / " + skillLevel + " 스킬이 1회용이지만, 여러번 사용되었습니다.");
                                    }
                                } else {
                                    /*  247 */ boolean dealy = true, react = true;
                                    /*  248 */ int delaytime = option;
                                    /*  249 */ if ((skillId == 203 && skillLevel == 1) || (skillId == 242 && (skillLevel == 10 || skillLevel == 11))) {
                                        /*  250 */ dealy = false;
                                    }
                                    /*  252 */ if (skillId == 215 && skillLevel == 4) {
                                        /*  253 */ delaytime = 2000;
                                    }
                                    /*  255 */ if (skillId == 238 && (skillLevel == 4 || skillLevel == 5 || skillLevel == 9 || skillLevel == 10)) {
                                        /*  256 */ if (skillLevel == 10 || skillLevel == 9) {
                                            /*  257 */ dealy = false;
                                        }
                                        /*  259 */ delaytime = 960;
                                    }
                                    /*  261 */ if (skillId == 247 || skillId == 264 || skillId == 263 || skillId == 260) {
                                        /*  262 */ dealy = false;
                                    }
                                    /*  264 */ if (skillId == 170) {
                                        /*  265 */ int a;
                                        int x;
                                        int b;
                                        int d;
                                        List<Point> pos;
                                        switch (skillLevel) {
                                            case 13:
                                                /*  267 */ monster.getMap().broadcastMessage(MobPacket.setAfterAttack(monster.getObjectId(), 8, 1, 30, ((actionAndDir & 0x1) != 0)));
                                                break;
                                            case 42:
                                                /*  270 */ a = ((actionAndDir & 0x1) == 0) ? 250 : -250;
                                                /*  271 */ b = ((actionAndDir & 0x1) == 0) ? 500 : -500;
                                                /*  272 */ d = ((actionAndDir & 0x1) == 0) ? 800 : -800;
                                                /*  273 */ pos = new ArrayList<>();
                                                /*  274 */ pos.add(new Point((monster.getPosition()).x + a, -80));
                                                /*  275 */ pos.add(new Point((monster.getPosition()).x + b, -80));
                                                /*  276 */ pos.add(new Point((monster.getPosition()).x + d, -80));
                                                /*  277 */ monster.getMap().broadcastMessage(MobPacket.AfterAttacklist(monster, skillId, skillLevel, ((actionAndDir & 0x1) != 0), 1, pos));
                                                /*  278 */ delaytime -= 500;
                                                break;

                                            case 45:
                                                /*  282 */ monster.getMap().broadcastMessage(MobPacket.AfterAttack(monster, skillId, skillLevel, ((actionAndDir & 0x1) != 0), 1, 3));
                                                break;
                                            case 46:
                                                /*  285 */ monster.getMap().broadcastMessage(MobPacket.AfterAttack(monster, skillId, skillLevel, ((actionAndDir & 0x1) != 0), 1, 1));
                                                break;
                                            case 47:
                                            case 61:
                                                /*  289 */ x = ((actionAndDir & 0x1) != 0) ? -600 : 600;
                                                /*  290 */ monster.getMap().broadcastMessage(MobPacket.AfterAttack(monster, skillId, skillLevel, ((actionAndDir & 0x1) != 0), 1, 7));
                                                /*  291 */ monster.getMap().broadcastMessage(MobPacket.TeleportMonster(monster, false, 16, new Point((monster.getPosition()).x + x, 17)));
                                                /*  292 */ react = false;
                                                break;
                                            case 48:
                                                /*  295 */ monster.setNextSkill(0);
                                                /*  296 */ monster.setNextSkillLvl(0);
                                                /*  297 */ monster.getMap().broadcastMessage(MobPacket.setAfterAttack(monster.getObjectId(), 2, 1, action, monster.isFacingLeft()));
                                                /*  298 */ react = false;
                                                break;
                                        }
                                    }
                                    /*  302 */ if (react) {
                                        /*  303 */ if (delaytime > 0 && dealy) {
                                            /*  304 */ Timer.ShowTimer.getInstance().schedule(() -> {
                                                if (monster.isAlive()) {
                                                    chr.dropMessageGM(-11, "op2 : " + option);
                                                    msi.applyEffect(chr, monster, true, ((actionAndDir & 0x1) != 0));
                                                }
                                            },delaytime
                                          );
 
 
 
 
                     
                     }
/*  311 */                     else if (monster.isAlive()) {
                                            /*  312 */ msi.applyEffect(chr, monster, true, ((actionAndDir & 0x1) != 0));
                                        }
                                    }

                                    /*  317 */ monster.setNextSkill(0);
                                    /*  318 */ monster.setNextSkillLvl(0);
                                    /*  319 */ if (msi.getOtherSkillID() > 0) {
                                        /*  320 */ Timer.ShowTimer.getInstance().schedule(() -> {
                                            monster.setNextSkill(msi.getOtherSkillID());
                                            monster.setNextSkillLvl(msi.getOtherSkillLev());
                                        },(option + 3000
                                    
                                
                                    
                                
                                ));
                   }
                 } 
 
 
 
                 
/*  327 */                 if (msi.getAfterAttack() >= 0) {
                                    /*  328 */ monster.getMap().broadcastMessage(MobPacket.setAfterAttack(oid, msi.getAfterAttack(), msi.getAfterAttackCount(), action, ((actionAndDir & 0x1) != 0)));
                                }
                            }

                        } /*  333 */ else if (chr.isGM()) {
                            /*  334 */ chr.dropMessage(-8, monster.getId() + " 몬스터의 " + skillId + " / " + skillLevel + " 스킬이 캐싱되지 않았습니다.");
                        }

                    } /*  338 */ else if (chr.isGM()) {
                        /*  339 */ chr.dropMessage(-8, monster.getId() + " 몬스터의 " + skillId + " / " + skillLevel + " 스킬 사용중 오류가 발생했습니다.");
                    }

                    /*  342 */                } else if (action <= 46) {

                    /*  345 */ MobAttackInfo attackInfo = MobAttackInfoFactory.getMobAttackInfo(monster, action);
                    /*  346 */ if (attackInfo != null && attackInfo.getSkill() != null) {
                        /*  347 */ monster.setNextSkill(attackInfo.getSkill().getSkill());
                        /*  348 */ monster.setNextSkillLvl(attackInfo.getSkill().getLevel());
                    } /*  350 */ else if (monster.getNoSkills() > 0) {
                        /*  351 */ List<MobSkill> useableSkills = new ArrayList<>();
                        /*  352 */ for (MobSkill msi : monster.getSkills()) {

                            /*  354 */ if (time - monster.getLastSkillUsed(msi.getSkillId(), msi.getSkillLevel()) >= 0L) {

                                /*  367 */ if (monster.getHPPercent() <= msi.getHP() && !msi.isOnlyOtherSkill() && !msi.checkCurrentBuff(chr, monster) && ( /*  368 */!msi.isOnlyFsm() || msi.getSkillId() == 223)) {
                                    /*  369 */ if (msi.onlyOnce() || msi.getSkillId() == 247) {
                                        /*  370 */ useableSkills.clear();
                                        /*  371 */ useableSkills.add(msi);
                                        break;
                                    }
                                    /*  374 */ useableSkills.add(msi);
                                }
                            }
                        }

                        /*  381 */ if (!useableSkills.isEmpty()) {
                            /*  382 */ MobSkill nextSkill = useableSkills.get(Randomizer.nextInt(useableSkills.size()));
                            /*  383 */ monster.setNextSkill(nextSkill.getSkillId());
                            /*  384 */ monster.setNextSkillLvl(nextSkill.getSkillLevel());
                            /*  385 */ monster.setLastSkillUsed(nextSkill, time, nextSkill.getInterval());
                        }
                    }
                }
            }

            /*  391 */ if (monster.getEliteGradeInfo().size() > 0) {
                /*  392 */ if (monster.getEliteGrade() >= 0 && monster.getCustomValue(9999) == null) {
                    /*  393 */ if (monster.isAlive()) {
                        /*  394 */ monster.setNextSkill(((Integer) ((Pair) monster.getEliteGradeInfo().get(0)).getLeft()).intValue());
                        /*  395 */ monster.setNextSkillLvl(((Integer) ((Pair) monster.getEliteGradeInfo().get(0)).getRight()).intValue());
                        /*  396 */ MobSkill msi2 = MobSkillFactory.getMobSkill(((Integer) ((Pair) monster.getEliteGradeInfo().get(0)).getLeft()).intValue(), ((Integer) ((Pair) monster.getEliteGradeInfo().get(0)).getRight()).intValue());
                        /*  397 */ msi2.applyEffect(chr, monster, true, ((actionAndDir & 0x1) != 0));
                        /*  398 */ monster.setCustomInfo(9999, 0, (int) msi2.getInterval() * 1000);
                    }
                    /*  400 */                } else if (monster.getCustomValue(9999) != null
                        && /*  401 */ monster.getNextSkill() > 0) {
                    /*  402 */ monster.setNextSkill(0);
                    /*  403 */ monster.setNextSkillLvl(0);
                }
            }

            /*  408 */ if (monster.getController() != null && monster.getController().getId() != c.getPlayer().getId()) {
                /*  409 */ if (!changeController) {
                    /*  410 */ c.getSession().writeAndFlush(MobPacket.stopControllingMonster(oid));
                    return;
                }
                /*  413 */ monster.switchController(chr, true);
            }

            /*  417 */ if (monster != null && c != null
                    && /*  418 */ setNextSkill) {
                /*  419 */ c.getSession().writeAndFlush(MobPacket.moveMonsterResponse(oid, moveid, monster.getMp(), movingAttack, monster.getNextSkill(), monster.getNextSkillLvl(), 0));
            }

            /*  423 */ if (monster.getId() == 8800002 || monster.getId() == 8800102) {

                /*  425 */ MapleBossManager.ZakumArmHandler(monster, chr, actionAndDir);
                /*  426 */            } else if (monster.getId() == 8870000 || monster.getId() == 8870100) {

                /*  428 */ MapleBossManager.HillaHandler(monster, chr, actionAndDir);
                /*  429 */            } else if (GameConstants.isAggressIveMonster(monster.getId())) {

                /*  431 */ MapleBossManager.AggressIve(monster);
                /*  432 */            } else if (monster.getId() == 8500001 || monster.getId() == 8500002 || monster.getId() == 8500011 || monster.getId() == 8500012 || monster.getId() == 8500021 || monster.getId() == 8500022) {

                /*  434 */ boolean phase1 = !(monster.getId() == 8500002 || monster.getId() == 8500012 || monster.getId() == 8500022);

                /*  436 */ if (phase1) {
                    /*  437 */ if (actionAndDir == 68 || actionAndDir == 69) {
                        /*  439 */ monster.getMap().broadcastMessage(MobPacket.SpeakingMonster(monster, 2, 0));
                    }
                } /*  442 */ else if (actionAndDir == 66 || actionAndDir == 67) {

                    /*  444 */ monster.getMap().broadcastMessage(MobPacket.SpeakingMonster(monster, 1, 0));
                }
            }

            /*  450 */ if (monster.getId() == 8880000 || monster.getId() == 8880002 || monster.getId() == 8880010) {

                /*  452 */ if ((actionAndDir == 30 || actionAndDir == 31)
                        && /*  453 */ bOption != 1 && bOption != 0) {

                    /*  455 */ boolean showchat = false;
                    /*  456 */ if ((monster.getPosition().getX() > 2475.0D && actionAndDir == 31) || (monster.getPosition().getX() < 1090.0D && actionAndDir == 30)) {
                        /*  457 */ showchat = true;
                    }
                    /*  459 */ if (showchat) {
                        /*  460 */ chr.getMap().broadcastMessage(MobPacket.getSmartNotice(monster.getId(), 0, 5, 1, "매그너스가 좁은 지역에 위협을 느껴 탈출을 시도합니다."));
                    }
                }

                /*  464 */ MapleBossManager.magnusHandler(monster, 1, actionAndDir);
                /*  465 */ MapleBossManager.magnusHandler(monster, 2, actionAndDir);
            } /*  467 */ else if ((monster.getId() == 8880111) ? (( /*  468 */actionAndDir != 26 && actionAndDir != 27)
                    || /*  469 */ bOption == 1 || bOption != 0) : (( /*  473 */monster.getId() != 8880341 && monster.getId() != 8880301) || ( /*  474 */actionAndDir != 32 && actionAndDir != 33)
                    || /*  475 */ bOption == 1 || bOption != 0)) {

            }

            /*  481 */ if (monster.getId() == 8880405
                    && /*  482 */ monster.getMap().getCustomValue0(28002) != 0
                    && /*  483 */ monster.getMap().getCustomValue0(28002) - 150 < (monster.getPosition()).x && monster.getMap().getCustomValue0(28002) + 150 > (monster.getPosition()).x) {
                /*  484 */ monster.getMap().removeCustomInfo(28002);
                /*  485 */ monster.getMap().broadcastMessage(CField.JinHillah(8, c.getPlayer(), c.getPlayer().getMap()));
                /*  486 */ monster.getMap().setReqTouched(0);
                /*  487 */ Timer.MapTimer.getInstance().schedule(() -> {
                    if (monster.isAlive() && monster.getMap().getAllChracater().size() > 0 && monster.getMap().getCandles() == monster.getMap().getLightCandles() && monster.getMap().getReqTouched() == 0) {
                        monster.getMap().broadcastMessage(CField.JinHillah(6, c.getPlayer(), c.getPlayer().getMap()));

                        monster.getMap().broadcastMessage(CField.enforceMSG("힐라가 접근하여 사라지기 전에 제단에서 채집키를 연타하여 영혼을 회수해야 한다.", 254, 6000));

                        monster.getMap().setReqTouched(30);
                    }
                    /*  495 */                },
                Randomizer.rand(10000, 20000)
            
            
            );
       } 
 
 
       
/*  500 */       if (res != null && c != null && c.getPlayer() != null && monster != null) {
                /*  501 */ MapleMap map = c.getPlayer().getMap();
                /*  502 */ MovementParse.updatePosition(res, (AnimatedMapleMapObject) monster, -1);
                /*  503 */ map.moveMonster(monster, monster.getPosition());
                /*  504 */ map.broadcastMessage(chr, MobPacket.moveMonster(bOption, actionAndDir, targetInfo, tEncodedGatherDuration, oid, startPos, startWobble, res, multiTargetForBall, randTimeForAreaAttack, cannotUseSkill), monster.getPosition());
            }
            /*  506 */        } catch (Exception e) {
           // /*  507 */ System.out.println(tr);
        }
    }

    public static final void FriendlyDamage(LittleEndianAccessor slea, MapleCharacter chr) {
        /*  512 */ MapleMap map = chr.getMap();
        /*  513 */ if (map == null) {
            return;
        }
        /*  516 */ MapleMonster mobfrom = map.getMonsterByOid(slea.readInt());
        /*  517 */ slea.skip(4);
        /*  518 */ MapleMonster mobto = map.getMonsterByOid(slea.readInt());
        /*  519 */ if (mobto.getId() == 8220110) {
            /*  520 */ mobto.damage(chr, 1L, true);
            /*  521 */ if (!mobto.isAlive()) {
                /*  522 */ chr.getMap().broadcastMessage(CField.startMapEffect("환상의 꽃 보호에 실패하였습니다. 킬러 비가 환상의 꽃의 기운을 빼앗고 사라집니다.", 5120124, true));
                /*  523 */ chr.getMap().setEliteChmpmap(false);
                /*  524 */ chr.getMap().setElitechmpcount(0);
                /*  525 */ chr.getMap().setElitechmptype(0);
                /*  526 */ chr.getMap().setCustomInfo(8222222, 0, 600000);
                /*  527 */ for (MapleMonster mob : chr.getMap().getAllMonster()) {
                    /*  528 */ if (mob.getId() == 8220111) {
                        /*  529 */ chr.getMap().killMonster(mob.getId());
                    }
                }
            }
            /*  533 */        } else if (mobfrom != null && mobto != null && mobto.getStats().isFriendly()) {
            /*  534 */ int damage = mobto.getStats().getLevel() * Randomizer.nextInt(mobto.getStats().getLevel()) / 2;
            /*  535 */ mobto.damage(chr, damage, true);
            /*  536 */ checkShammos(chr, mobto, map);
        }
    }

    public static final void BindMonster(LittleEndianAccessor slea, MapleClient c) {
        /*  541 */ MapleMap map = c.getPlayer().getMap();
        /*  542 */ if (map == null) {
            return;
        }
        /*  545 */ MapleMonster mob = map.getMonsterByOid(slea.readInt());
    }

    public static final void MobBomb(LittleEndianAccessor slea, MapleCharacter chr) {
        /*  550 */ MapleMap map = chr.getMap();
        /*  551 */ if (map == null) {
            return;
        }
        /*  554 */ MapleMonster mobfrom = map.getMonsterByOid(slea.readInt());
        /*  555 */ slea.skip(4);
        /*  556 */ slea.readInt();

        /*  558 */ if (mobfrom == null || mobfrom.getBuff(MonsterStatus.MS_TimeBomb) != null);
    }

    public static final void checkShammos(MapleCharacter chr, MapleMonster mobto, MapleMap map) {
        /*  568 */ if (!mobto.isAlive() && mobto.getStats().isEscort()) {
            /*  569 */ for (MapleCharacter chrz : map.getCharactersThreadsafe()) {
                /*  570 */ if (chrz.getParty() != null && chrz.getParty().getLeader().getId() == chrz.getId()) {

                    /*  572 */ if (chrz.haveItem(2022698)) {
                        /*  573 */ MapleInventoryManipulator.removeById(chrz.getClient(), MapleInventoryType.USE, 2022698, 1, false, true);
                        /*  574 */ mobto.heal((int) mobto.getMobMaxHp(), mobto.getMobMaxMp(), true);
                        return;
                    }
                    break;
                }
            }
            /*  580 */ map.broadcastMessage(CWvsContext.serverNotice(6, "", "Your party has failed to protect the monster."));
            /*  581 */ MapleMap mapp = chr.getMap().getForcedReturnMap();
            /*  582 */ for (MapleCharacter chrz : map.getCharactersThreadsafe()) {
                /*  583 */ chrz.changeMap(mapp, mapp.getPortal(0));
            }
            /*  585 */        } else if (mobto.getStats().isEscort() && mobto.getEventInstance() != null) {
            /*  586 */ mobto.getEventInstance().setProperty("HP", String.valueOf(mobto.getHp()));
        }
    }

    public static final void MonsterBomb(int oid, MapleCharacter chr) {
        /*  591 */ MapleMonster monster = chr.getMap().getMonsterByOid(oid);

        /*  593 */ if (monster == null || !chr.isAlive() || chr.isHidden() || monster.getLinkCID() > 0) {
            return;
        }
        /*  596 */ byte selfd = monster.getStats().getSelfD();
        /*  597 */ if (selfd != -1) {
            /*  598 */ MapleMonster mob = MapleLifeFactory.getMonster(monster.getId());
            /*  599 */ chr.getMap().killMonster(monster, chr, false, false, (byte) 2);
            /*  600 */ if (mob.getId() == 9833959) {
                /*  601 */ mob.setOwner(chr.getId());
                /*  602 */ Timer.EventTimer.getInstance().schedule(() -> chr.getMap().spawnMonsterOnGroundBelow(mob, monster.getPosition()), 3000L);
            }
        }
    }

    public static final void AutoAggro(int monsteroid, MapleCharacter chr) {
        /*  610 */ if (chr == null || chr.getMap() == null || chr.isHidden()) {
            return;
        }
        /*  613 */ MapleMonster monster = chr.getMap().getMonsterByOid(monsteroid);

        /*  615 */ if (monster != null && chr.getTruePosition().distanceSq(monster.getTruePosition()) < 200000.0D && monster.getLinkCID() <= 0) {
            /*  616 */ if (monster.getController() != null) {
                /*  617 */ if (chr.getMap().getCharacterById(monster.getController().getId()) == null) {
                    /*  618 */ monster.switchController(chr, true);
                } else {
                    /*  620 */ monster.switchController(monster.getController(), true);
                }
            } else {
                /*  623 */ monster.switchController(chr, true);
            }
        }
    }

    public static final void HypnotizeDmg(LittleEndianAccessor slea, MapleCharacter chr) {
        /*  629 */ MapleMonster mob_from = chr.getMap().getMonsterByOid(slea.readInt());
        /*  630 */ slea.skip(4);
        /*  631 */ int to = slea.readInt();
        /*  632 */ slea.skip(1);
        /*  633 */ int damage = slea.readInt();

        /*  637 */ MapleMonster mob_to = chr.getMap().getMonsterByOid(to);

        /*  639 */ if (mob_from != null && mob_to != null && mob_to.getStats().isFriendly()) {
            /*  640 */ if (damage > 30000) {
                return;
            }
            /*  643 */ mob_to.damage(chr, damage, true);
            /*  644 */ checkShammos(chr, mob_to, chr.getMap());
        }
    }

    public static final void MobNode(LittleEndianAccessor slea, MapleCharacter chr) {
        /*  649 */ MapleMonster mob_from = chr.getMap().getMonsterByOid(slea.readInt());
        /*  650 */ int newNode = slea.readInt();
        /*  651 */ int nodeSize = chr.getMap().getNodes().size();
        /*  652 */ if (mob_from != null && nodeSize > 0) {
            /*  653 */ MapleNodes.MapleNodeInfo mni = chr.getMap().getNode(newNode);
            /*  654 */ if (mni == null) {
                return;
            }
            /*  657 */ if (mni.attr == 2) {
                /*  658 */ switch (chr.getMapId() / 100) {
                    case 9211200:
                    case 9211201:
                    case 9211202:
                    case 9211203:
                    case 9211204:
                        /*  664 */ chr.getMap().talkMonster("Please escort me carefully.", 5120035, mob_from.getObjectId());
                        break;
                    case 9320001:
                    case 9320002:
                    case 9320003:
                        /*  669 */ chr.getMap().talkMonster("Please escort me carefully.", 5120051, mob_from.getObjectId());
                        break;
                }
            }
            /*  673 */ mob_from.setLastNode(newNode);
            /*  674 */ if (chr.getMap().isLastNode(newNode)) {
                /*  675 */ switch (chr.getMapId() / 100) {
                    case 9211200:
                    case 9211201:
                    case 9211202:
                    case 9211203:
                    case 9211204:
                    case 9320001:
                    case 9320002:
                    case 9320003:
                        /*  684 */ chr.getMap().broadcastMessage(CWvsContext.serverNotice(5, "", "Proceed to the next stage."));
                        /*  685 */ chr.getMap().removeMonster(mob_from);
                        break;
                }
            }
        }
    }

    public static final void OrgelHit(LittleEndianAccessor slea, MapleCharacter chr) {
        /*  694 */ MapleMonster Attacker = chr.getMap().getMonsterByOid(slea.readInt());
        /*  695 */ slea.skip(4);
        /*  696 */ MapleMonster Defender = chr.getMap().getMonsterByOid(slea.readInt());
        /*  697 */ slea.skip(3);
        /*  698 */ int Damage = slea.readInt();
        /*  699 */ EventInstanceManager em = chr.getEventInstance();
        /*  700 */ if (Attacker == null || Defender == null || em == null) {
            return;
        }
        /*  703 */ if (Attacker.getId() >= 9833090 && Attacker.getId() <= 9833099 && (( /*  704 */Defender.getId() >= 9833070 && Defender.getId() <= 9833074) || Defender.getId() == 9833100)) {
            /*  705 */ int HP = ((int) (Defender.getHp() - Damage) < 0) ? 0 : (int) (Defender.getHp() - Damage);
            /*  706 */ Defender.setHp(HP);
            /*  707 */ if (Defender.getHp() <= 0L) {
                /*  708 */ Defender.getMap().killMonster(Defender);
            }
        }
    }

    public static final void SpiritHit(LittleEndianAccessor slea, MapleCharacter chr) {
        /*  715 */ int asdf, i, oid = slea.readInt();
        /*  716 */ if (chr == null || chr.getMap() == null) {
            return;
        }
        /*  719 */ MapleMonster mob = chr.getMap().getMonsterByOid(oid);
        /*  720 */ EventInstanceManager em = chr.getEventInstance();
        /*  721 */ if (mob == null || em == null) {
            /*  722 */ chr.getMap().killMonster(mob);

            return;
        }
        /*  726 */ int life = (int) chr.getKeyValue(16215, "life");
        /*  727 */ switch (mob.getId()) {
            case 8644201:
                /*  729 */ chr.setKeyValue(16215, "life", "" + (life - 5));
                break;
            case 8644301:
            case 8644302:
            case 8644303:
            case 8644304:
            case 8644305:
                /*  736 */ chr.setKeyValue(16215, "life", "" + (life - 50));
                /*  737 */ asdf = 0;
                /*  738 */ for (i = 0; i < 5; i++) {
                    /*  739 */ if (em.getProperty("ObjectId" + i) != null && !em.getProperty("ObjectId" + i).equals("")) {
                        /*  740 */ chr.getClient().getSession().writeAndFlush(SLFCGPacket.SpawnPartner(false, Integer.parseInt(em.getProperty("ObjectId" + i)), 0));
                        /*  741 */ em.setProperty("ObjectId" + i, "");
                        /*  742 */ asdf++;
                    }
                }
                /*  745 */ if (asdf == 5) {
                    /*  746 */ chr.getClient().getSession().writeAndFlush(CField.startMapEffect("", 0, false));
                    /*  747 */ chr.getClient().getSession().writeAndFlush(CField.startMapEffect("친구들이... 맹독의 정령에게 당하고 말았담!", 5120175, true));
                    /*  748 */ chr.getClient().getSession().writeAndFlush(CField.environmentChange("Map/Effect3.img/savingSpirit/failed", 19));
                }

                /*  751 */ chr.setKeyValue(16215, "chase", "0");
                break;
            case 8644101:
            case 8644102:
            case 8644103:
            case 8644104:
            case 8644105:
            case 8644106:
            case 8644107:
            case 8644108:
            case 8644109:
            case 8644110:
            case 8644111:
            case 8644112:
                /*  765 */ chr.setKeyValue(16215, "life", "" + (life - 5));
                break;
            case 8880315:
            case 8880316:
            case 8880317:
            case 8880318:
            case 8880319:
            case 8880345:
            case 8880346:
            case 8880347:
            case 8880348:
            case 8880349:
                /*  777 */ chr.addHP((long) (-chr.getStat().getCurrentMaxHp() * 0.1D));
                break;
        }
        /*  780 */ chr.getMap().killMonster(mob, chr, false, false, (byte) 1);

        /*  782 */ if (chr.getKeyValue(16215, "life") <= 0L && chr.getMapId() == 921172300) {
            /*  783 */ MapleMap target = chr.getClient().getChannelServer().getMapFactory().getMap(921172400);
            /*  784 */ chr.changeMap(target, target.getPortal(0));
            /*  785 */ chr.getClient().getSession().writeAndFlush(CField.environmentChange("Map/Effect2.img/event/gameover", 16));
        }
    }

    public static void AirAttack(LittleEndianAccessor slea, MapleClient c) {
    }

    public static void demianBind(LittleEndianAccessor slea, MapleClient c) {
        /*  812 */ MapleCharacter chr = c.getPlayer();
        /*  813 */ int result = slea.readInt();

        /*  815 */ Map<SecondaryStat, Pair<Integer, Integer>> cancelList = new HashMap<>();

        /*  817 */ if (result == 0
                && /*  818 */ chr.getDiseases().containsKey(SecondaryStat.Lapidification)) {
            /*  819 */ chr.getDiseases().remove(SecondaryStat.Lapidification);

            /*  821 */ cancelList.put(SecondaryStat.Lapidification, new Pair(Integer.valueOf(0), Integer.valueOf(0)));
            /*  822 */ c.getSession().writeAndFlush(CWvsContext.BuffPacket.cancelBuff(cancelList, chr));
            /*  823 */ chr.getMap().broadcastMessage(chr, CWvsContext.BuffPacket.cancelForeignBuff(chr, cancelList), false);
        }
    }

    public static void demianAttacked(LittleEndianAccessor slea, MapleClient c) {
        /*  829 */ int objectId = slea.readInt();

        /*  831 */ MapleMonster mob = c.getPlayer().getMap().getMonsterByOid(objectId);

        /*  833 */ if (mob != null) {
            /*  834 */ int skillId = slea.readInt();

            /*  837 */ if (skillId == 214) {
                /*  838 */ MobSkillFactory.getMobSkill(170, 51).applyEffect(c.getPlayer(), mob, true, mob.isFacingLeft());
            }
        }
    }

    public static void useStigmaIncinerate(LittleEndianAccessor slea, MapleClient c) {
        /*  844 */ int state = slea.readInt();
        /*  845 */ int id = slea.readInt();
        /*  846 */ int type2 = slea.readInt();
        /*  847 */ int[] demians = {8880100, 8880110, 8880101, 8880111};

        /*  849 */ MapleMonster demian = null;
        /*  850 */ for (int ids : demians) {
            /*  851 */ demian = c.getPlayer().getMap().getMonsterById(ids);
            /*  852 */ if (demian != null) {
                break;
            }
        }
        /*  856 */ MapleCharacter chr = c.getPlayer();
        /*  857 */ Map<SecondaryStat, Pair<Integer, Integer>> cancelList = new HashMap<>();
        /*  858 */ if (demian != null) {
            /*  859 */ int stigma;
            switch (state) {

                case 1:
                    /*  863 */ stigma = chr.Stigma;
                    /*  864 */ if (chr.Stigma > 0) {
                        /*  865 */ chr.Stigma = 0;
                        /*  866 */ cancelList.put(SecondaryStat.Stigma, new Pair(Integer.valueOf(0), Integer.valueOf(0)));
                        /*  867 */ c.getSession().writeAndFlush(CWvsContext.BuffPacket.cancelBuff(cancelList, chr));
                        /*  868 */ chr.getMap().broadcastMessage(chr, CWvsContext.BuffPacket.cancelForeignBuff(chr, cancelList), false);
                        /*  869 */ chr.getMap().broadcastMessage(MobPacket.incinerateObject(null, false));
                        /*  870 */ c.getPlayer().getMap().broadcastMessage(MobPacket.StigmaImage(c.getPlayer(), true));
                    }
                    /*  872 */ if (type2 == 1) {

                        /*  874 */ MapleCharacter target = c.getPlayer().getMap().getCharacterById(id);
                        /*  875 */ target.Stigma += stigma - 1;
                        /*  876 */ MobSkill ms = MobSkillFactory.getMobSkill(237, 1);
                        /*  877 */ ms.applyEffect(target, demian, true, demian.isFacingLeft());
                        /*  878 */ target.getMap().broadcastMessage(MobPacket.StigmaImage(target, false));
                    }
                    break;
            }
        }
    }

    public static void stoneAttacked(LittleEndianAccessor slea, MapleClient c) {
        /*  888 */ MapleCharacter chr = c.getPlayer();
        /*  889 */ if (slea.readInt() == 0
                && /*  890 */ chr.isAlive()) {
            /*  891 */ if (chr.getMap().getId() % 1000 == 410 || chr.getMap().getId() % 1000 == 810) {
                /*  892 */ MobSkillFactory.getMobSkill(123, 44).applyEffect(chr, null, true, false);
                /*  893 */            } else if (chr.getMap().getId() % 1000 == 210 || chr.getMap().getId() % 1000 == 610) {
                /*  894 */ MobSkillFactory.getMobSkill(174, 3).applyEffect(chr, null, true, false);
            }
        }
    }

    public static void jinHillahBlackHand(LittleEndianAccessor slea, MapleClient c) {
        /*  901 */ MapleCharacter chr = c.getPlayer();
        /*  902 */ MapleMonster mob = chr.getMap().getMonsterByOid(slea.readInt());
        /*  903 */ if (mob != null) {
            /*  904 */ int skillLevel = slea.readInt();
            /*  905 */ int id = slea.readInt();
            /*  906 */ Point pos = slea.readPos();

            /*  908 */ if (chr.getId() == id && !chr.hasDisease(SecondaryStat.Stun, 123, 83)) {
                /*  909 */ Rectangle rect = new Rectangle(slea.readInt(), slea.readInt(), slea.readInt(), slea.readInt());
                /*  910 */ if (skillLevel == 2) {
                    /*  911 */ skillLevel = 1;
                }
                /*  913 */ int act_x = 0, apply_x = 0;
                /*  914 */ boolean minus = false;
                /*  915 */ int[] randXs = {0, 280, 560, 840};
                /*  916 */ pos.x -= 50;
                /*  917 */ if (pos.x < 0) {
                    /*  918 */ pos.x *= -1;
                    /*  919 */ minus = true;
                }
                int arrayOfInt1[], i;
                byte b;
                /*  921 */ for (arrayOfInt1 = randXs, i = arrayOfInt1.length, b = 0; b < i;) {
                    Integer randX = Integer.valueOf(arrayOfInt1[b]);
                    /*  922 */ int calc_x = pos.x - randX.intValue();
                    /*  923 */ if (calc_x < 0) {
                        /*  924 */ calc_x *= -1;
                    }
                    /*  926 */ if (act_x == 0) {
                        /*  927 */ act_x = calc_x;
                    }
                    /*  929 */ if (calc_x < act_x) {
                        /*  930 */ act_x = calc_x;
                        /*  931 */ apply_x = randX.intValue();
                    }
                    b++;
                }

                /*  934 */ pos.x = apply_x;
                /*  935 */ if (minus) {
                    /*  936 */ pos.x *= -1;
                }
                /*  938 */ MobSkill ms = MobSkillFactory.getMobSkill(123, 83);
                /*  939 */ chr.giveDebuff(SecondaryStat.Stun, ms);
                /*  940 */ chr.getMap().broadcastMessage(MobPacket.jinHillahSpirit(mob.getObjectId(), chr.getId(), rect, pos, skillLevel));

                /*  943 */ if (chr.liveCounts() == 1) {

                    /*  945 */ for (int j = 0; j < (chr.getDeathCounts()).length; j++) {
                        /*  946 */ if (chr.getDeathCounts()[j] == 1) {
                            /*  947 */ chr.getDeathCounts()[j] = 0;

                            break;
                        }
                    }
                    /*  952 */ chr.playerIGDead();
                } else {
                    /*  954 */ for (int j = 0; j < (chr.getDeathCounts()).length; j++) {
                        /*  955 */ if (chr.getDeathCounts()[j] == 1) {
                            /*  956 */ chr.getDeathCounts()[j] = 0;
                            break;
                        }
                    }
                    /*  960 */ if (chr.getMap().getLightCandles() < chr.getMap().getCandles()) {

                        /*  962 */ chr.getMap().setLightCandles(chr.getMap().getLightCandles() + 1);
                        /*  963 */ chr.getMap().broadcastMessage(CField.JinHillah(1, chr, chr.getMap()));
                    }
                }

                /*  968 */ c.getSession().writeAndFlush(CField.JinHillah(3, chr, chr.getMap()));
                /*  969 */ c.getPlayer().getMap().broadcastMessage(CField.JinHillah(10, c.getPlayer(), chr.getMap()));

                /*  971 */ chr.dropMessageGM(6, "candle : " + chr.getMap().getCandles() + " / " + chr.getMap().getLightCandles());

                /*  973 */ if (chr.getMap().getCandles() == chr.getMap().getLightCandles() && chr.getMap().getReqTouched() == 0) {
                    /*  974 */ chr.getMap().broadcastMessage(CField.enforceMSG("힐라가 접근하여 사라지기 전에 제단에서 채집키를 연타하여 영혼을 회수해야 한다.", 254, 6000));
                    /*  975 */ chr.getMap().setReqTouched(30);
                    /*  976 */ chr.getMap().broadcastMessage(CField.JinHillah(6, chr, chr.getMap()));
                }
            }
        }
    }

    public static void touchAlter(LittleEndianAccessor slea, MapleClient c) {
        /*  983 */ MapleMap map = c.getPlayer().getMap();

        /*  985 */ if (map.getReqTouched() > 0) {
            /*  986 */ map.setReqTouched(map.getReqTouched() - 1);
            /*  987 */ if (map.getReqTouched() != 0) {
                /*  988 */ map.broadcastMessage(CField.JinHillah(7, c.getPlayer(), c.getPlayer().getMap()));
            } else {
                /*  990 */ map.broadcastMessage(CField.JinHillah(8, c.getPlayer(), c.getPlayer().getMap()));
                /*  991 */ map.setLightCandles(0);
                /*  992 */ map.broadcastMessage(CField.JinHillah(1, c.getPlayer(), c.getPlayer().getMap()));

                /*  994 */ for (MapleCharacter chr : map.getAllCharactersThreadsafe()) {
                    /*  995 */ SkillFactory.getSkill(80002544).getEffect(1).applyTo(chr, false);
                   // /*  996 */ chr.cancelEffectFromBuffStat(SecondaryStat.DebuffIncHp, 80002543);

                    /*  998 */ for (int i = 0; i < (chr.getDeathCounts()).length; i++) {
                        /*  999 */ int dc = chr.getDeathCounts()[i];
                        /* 1000 */ if (dc == 0) {
                            /* 1001 */ chr.getDeathCounts()[i] = 1;
                        }
                    }

                    /* 1005 */ chr.getClient().getSession().writeAndFlush(CField.JinHillah(3, chr, c.getPlayer().getMap()));
                    /* 1006 */ c.getPlayer().getMap().broadcastMessage(CField.JinHillah(10, c.getPlayer(), c.getPlayer().getMap()));
                }
            }
        }
    }

    public static void unkJinHillia(LittleEndianAccessor slea, MapleClient c) {
    }

    public static void lucidStateChange(MapleCharacter chr) {
        /* 1017 */ MapleMonster lucid = null;
        /* 1018 */ int[] lucids = {8880140, 8880141, 8880142, 8880150, 8880151, 8880155};
        /* 1019 */ for (int ids : lucids) {
            /* 1020 */ lucid = chr.getMap().getMonsterById(ids);
            /* 1021 */ if (lucid != null) {
                break;
            }
        }
        /* 1025 */ if (lucid != null
                && /* 1026 */ chr.getMap().getLucidCount() > 0) {
            /* 1027 */ chr.getMap().setLucidCount(chr.getMap().getLucidCount() - 1);
            /* 1028 */ chr.getMap().setLucidUseCount(chr.getMap().getLucidUseCount() + 1);
            /* 1029 */ chr.getMap().broadcastMessage(MobPacket.BossLucid.changeStatueState(false, chr.getMap().getLucidCount(), true));
            /* 1030 */ chr.getMap().broadcastMessage(MobPacket.BossLucid.RemoveButterfly());
            /* 1031 */ lucid.removeCustomInfo(8880140);
            /* 1032 */ chr.getMap().broadcastMessage(MobPacket.BossLucid.setButterflyAction(Butterfly.Mode.ERASE, new int[]{700, -600}));
        }
    }

    public static void mobSkillDelay(LittleEndianAccessor slea, MapleClient c) {
        /* 1038 */ int objectId = slea.readInt();
        /* 1039 */ int skillId = slea.readInt();
        /* 1040 */ int skillLevel = slea.readInt();
        /* 1041 */ slea.skip(1);
        /* 1042 */ int RectCount = slea.readInt();

        /* 1044 */ switch (skillId) {
            case 213:
            case 217:
                return;
        }

        /* 1050 */ MobSkill msi = MobSkillFactory.getMobSkill(skillId, skillLevel);
        /* 1051 */ if (c.getPlayer() != null && msi != null) {
            /* 1052 */ MapleMonster monster = c.getPlayer().getMap().getMonsterByOid(objectId);
            /* 1053 */ if (monster != null) {
                /* 1054 */ if (RectCount > 0) {
                    /* 1055 */ msi.applyEffect(c.getPlayer(), monster, true, monster.isFacingLeft(), RectCount);
                } else {
                    /* 1057 */ msi.applyEffect(c.getPlayer(), monster, true, monster.isFacingLeft());
                }
            }
        }
    }

    public static void spawnMistArea(LittleEndianAccessor slea, MapleClient c) {
        /* 1064 */ int mobid = slea.readInt();
        /* 1065 */ int objectId = slea.readInt();
        /* 1066 */ int skillId = slea.readInt();
        /* 1067 */ int skillLevel = slea.readInt();
        /* 1068 */ Point pos = slea.readIntPos();
        /* 1069 */ MapleMonster monster = c.getPlayer().getMap().getMonsterByOid(mobid);
        /* 1070 */ MobSkill msi = MobSkillFactory.getMobSkill(skillId, skillLevel);
        /* 1071 */ if (c.getPlayer() != null && msi != null && monster != null) {
            /* 1072 */ if (skillId == 217 && skillLevel == 21) {
                /* 1073 */ Rectangle re = new Rectangle(pos.x - 64, pos.y - 50, 128, 60);
                /* 1074 */ MapleMist mist = new MapleMist(re, monster, msi, (int) msi.getDuration());
                /* 1075 */ mist.setPosition(pos);
                /* 1076 */ monster.getMap().spawnMist(mist, true);
            } else {
                /* 1078 */ MapleMist mist = new MapleMist(msi.calculateBoundingBox(pos, monster.isFacingLeft()), monster, msi, 10000);
                /* 1079 */ mist.setPosition(pos);
                /* 1080 */ monster.getMap().spawnMist(mist, true);
            }
        }
    }

    public static final void SpawnMoveMobMist(LittleEndianAccessor slea, MapleCharacter chr) {
        /* 1087 */ int mobid = slea.readInt();
        /* 1088 */ int skillid = slea.readInt();
        /* 1089 */ int skilllv = slea.readInt();
        /* 1090 */ int callskill = 0;
        /* 1091 */ int level = 0;
        /* 1092 */ if (skillid == 202) {
            /* 1093 */ switch (skilllv) {
                case 1:
                    /* 1095 */ callskill = 131;
                    /* 1096 */ level = 16;
                    break;
                case 2:
                    /* 1099 */ callskill = 131;
                    /* 1100 */ level = 18;
                    break;
                case 3:
                    /* 1103 */ callskill = 131;
                    /* 1104 */ level = 10;
                    break;
            }

        }
        /* 1109 */ Point pos = slea.readPos();
        /* 1110 */ MapleMonster monster = chr.getMap().getMonsterByOid(mobid);

        /* 1112 */ if (monster == null) {
            return;
        }
        /* 1115 */ if (callskill > 0 && level > 0) {
            /* 1116 */ MobSkill ms = MobSkillFactory.getMobSkill(callskill, level);
            /* 1117 */ if (ms != null) {
                /* 1118 */ MapleMist mist = new MapleMist(ms.calculateBoundingBox(pos, monster.isFacingLeft()), monster, ms, (int) ms.getDuration());
                /* 1119 */ monster.getMap().spawnMist(mist, true);
            }
        }
    }

    public static void PapulaTusBomb(LittleEndianAccessor slea, MapleClient c, int type) {
        /* 1125 */ MapleMonster mob = null;
        /* 1126 */ for (MapleMonster monster : c.getPlayer().getMap().getAllMonster()) {
            /* 1127 */ if (monster.getId() == 8500001 || monster.getId() == 8500002 || monster.getId() == 8500011 || monster.getId() == 8500012 || monster.getId() == 8500021 || monster.getId() == 8500022) {
                /* 1128 */ mob = monster;
                break;
            }
        }
        /* 1132 */ if (mob != null) {
            /* 1133 */ if (type == 0) {
                /* 1134 */ c.getPlayer().getMap().setPapulratusTime(Randomizer.rand(15, 25));
                /* 1135 */ c.getPlayer().getMap().broadcastMessage(MobPacket.BossPapuLatus.PapulLatusLaser(true, 1));
                /* 1136 */ c.getPlayer().getMap().broadcastMessage(MobPacket.BossPapuLatus.PapulLatusLaser(true, 2));
                /* 1137 */            } else if (type == 1) {
                /* 1138 */ int typeed = mob.getId() % 100 / 10;
                /* 1139 */ String difical = (typeed == 0) ? "Easy" : ((typeed == 1) ? "Normal" : "Chaos");
                /* 1140 */ int percent = difical.equals("Chaos") ? 200 : (difical.equals("Normal") ? 100 : 50);
                /* 1141 */ for (MapleCharacter allchr : c.getPlayer().getMap().getAllCharactersThreadsafe()) {
                    /* 1142 */ if (allchr != null && allchr.getBuffedValue(SecondaryStat.NotDamaged) == null && allchr.getBuffedValue(SecondaryStat.IndieNotDamaged) == null) {
                        /* 1143 */ allchr.getPercentDamage(mob, 999, 999, percent, false);
                    }
                }
            }
        }
    }

    public static void PapulaTusPincers(LittleEndianAccessor slea, MapleClient c) {
        /* 1151 */ int type = slea.readInt();
        /* 1152 */ int y = slea.readInt();
        /* 1153 */ MobSkill ms1 = MobSkillFactory.getMobSkill(241, 8);
        /* 1154 */ ms1.setDuration(2000L);
        /* 1155 */ c.getPlayer().giveDebuff(SecondaryStat.Stun, ms1);
        /* 1156 */ c.getPlayer().getMap().broadcastMessage(MobPacket.BossPapuLatus.PapulLatusPincers(false, c.getPlayer().getId(), type, y));
    }

    public static void PapulaTusPincersreset(LittleEndianAccessor slea, MapleClient c) {
        /* 1160 */ int type = slea.readInt();
        /* 1161 */ c.getPlayer().getMap().broadcastMessage(MobPacket.BossPapuLatus.PapulLatusPincers(false, 0, type, 0));
    }

    public static void BloodyQueenBress(LittleEndianAccessor slea, MapleClient c) {
        /* 1165 */ int modid = slea.readInt();
        /* 1166 */ int typeed = slea.readByte();
        /* 1167 */ c.getPlayer().getMap().broadcastMessage(MobPacket.AfterAttackone(modid, typeed, (typeed == 1)));
    }

    public static void BloodyQueenMirror(LittleEndianAccessor slea, MapleClient c) {
        /* 1171 */ int mobid = slea.readInt();
        /* 1172 */ boolean Chaos = (mobid == 8920005);
        /* 1173 */ int percent = Chaos ? 25 : 5;
        /* 1174 */ MapleMonster queen = null;
        /* 1175 */ for (MapleMonster monster : c.getPlayer().getMap().getAllMonster()) {
            /* 1176 */ if ((monster.getId() >= 8920100 && monster.getId() <= 8920103) || (monster.getId() >= 8920000 && monster.getId() <= 8920003)) {
                /* 1177 */ queen = monster;
                break;
            }
        }
        /* 1181 */ if (queen != null) {
            /* 1182 */ List<SecondaryStat> cancellist = new ArrayList<>();
            /* 1183 */ cancellist.add(SecondaryStat.Slow);
            /* 1184 */ cancellist.add(SecondaryStat.Stance);
            /* 1185 */ cancellist.add(SecondaryStat.Attract);
            /* 1186 */ for (SecondaryStat cancel : cancellist) {
                /* 1187 */ if (c.getPlayer().hasDisease(cancel)) {
                    /* 1188 */ c.getPlayer().cancelDisease(cancel);
                }
            }
            /* 1191 */ int minushp = (int) -c.getPlayer().getStat().getCurrentMaxHp();
            /* 1192 */ c.getPlayer().addHP(minushp);
            /* 1193 */ c.send(CField.ChangeFaceMotion(0, 1));
            /* 1194 */ c.send(CField.EffectPacket.showEffect(c.getPlayer(), 0, minushp, 36, 0, 0, (byte) 0, true, null, null, null));
            /* 1195 */ c.getPlayer().getMap().broadcastMessage(c.getPlayer(), CField.EffectPacket.showEffect(c.getPlayer(), 0, minushp, 36, 0, 0, (byte) 0, false, null, null, null), false);
            /* 1196 */ queen.addHp(queen.getStats().getHp() * percent / 100L, true);
        }
    }

    public static void VanVanTimeOver(LittleEndianAccessor slea, MapleClient c) {
        /* 1201 */ if ((c.getPlayer().getMapId() >= 105200120 && c.getPlayer().getMapId() <= 105200129) || (c.getPlayer().getMapId() >= 105200520 && c.getPlayer().getMapId() <= 105200529)) {
            /* 1202 */ boolean suc = !(c.getPlayer().getMap().getAllMonster().size() > 0);
            /* 1203 */ if (suc) {
                /* 1205 */ MapleMap warp = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(c.getPlayer().getMapId() - 10);
                /* 1206 */ MapleMonster vanvan = warp.getMonsterById(8910000);
                /* 1207 */ if (vanvan != null
                        && /* 1208 */ vanvan.getBuff(MonsterStatus.MS_Stun) == null) {
                    /* 1209 */ vanvan.applyStatus(c, MonsterStatus.MS_Stun, new MonsterStatusEffect(80001227, 7000), 1, SkillFactory.getSkill(80001227).getEffect(1));
                }
            } else {
                /* 1214 */ c.send(CField.setPlayerDead());
                /* 1215 */ Timer.MapTimer.getInstance().schedule(() -> {
                    if (c.getPlayer().isAlive() && ((c.getPlayer().getMapId() >= 105200120 && c.getPlayer().getMapId() <= 105200129) || (c.getPlayer().getMapId() >= 105200520 && c.getPlayer().getMapId() <= 105200529))) {
                        c.getPlayer().warp(c.getPlayer().getMapId() - 10);
                    }
                },
            
          
            
          
            3000L);
       
       }
 
     
     }
     else {
       
/* 1223 */       MapleMonster vanvan = c.getPlayer().getMap().getMonsterById(8910000);
            /* 1224 */ if (vanvan != null) {
                /* 1225 */ boolean suc = (c.getChannelServer().getMapFactory().getMap(c.getPlayer().getMapId() + 10).getAllMonster().size() <= 0);
                /* 1226 */ c.send(CField.environmentChange("Pt", 3));
                /* 1227 */ if (suc) {

                    /* 1229 */ if (vanvan.getBuff(MonsterStatus.MS_Stun) == null) {
                        /* 1230 */ vanvan.applyStatus(c, MonsterStatus.MS_Stun, new MonsterStatusEffect(80001227, 7000), 1, SkillFactory.getSkill(80001227).getEffect(1));
                    }
                } else {

                    /* 1234 */ c.send(MobPacket.StopVanVanBind(vanvan.getObjectId()));
                    /* 1235 */ c.send(CField.setPlayerDead());
                }
            }
        }
    }

    public static final void SpawnBellumMist(LittleEndianAccessor slea, MapleClient c) {
        /* 1242 */ int mobid = slea.readInt();
        /* 1243 */ int attacktype = slea.readInt();
        /* 1244 */ Point pos = slea.readIntPos();
        /* 1245 */ int dealy = slea.readInt();
        /* 1246 */ final MapleMonster monster = c.getPlayer().getMap().getMonsterByOid(mobid);
        /* 1247 */ if (monster == null) {
            return;
        }
        /* 1250 */ if (monster.getId() == 8930000 || monster.getId() == 8930100 || ((monster.getId() == 8850011 || monster.getId() == 8850111) && attacktype == 2)) {
            /* 1251 */ MobSkill ms = MobSkillFactory.getMobSkill(131, 17);
            /* 1252 */ ms.setDuration((dealy + 4000));
            /* 1253 */ if (monster.getId() == 8850011 || monster.getId() == 8850111) {
                /* 1254 */ ms = MobSkillFactory.getMobSkill(131, 13);
                /* 1255 */ ms.setDuration((dealy + 5000));
            }
            ///* 1257 */ System.out.println(ms.getDuration());
            /* 1258 */ final MapleMist mist = new MapleMist(ms.calculateBoundingBox(pos, monster.isFacingLeft()), monster, ms, (int) ms.getDuration());
            /* 1259 */ Timer.MapTimer.getInstance().schedule(new Runnable() {
                public void run() {
                    /* 1262 */ if (mist != null) {
                        /* 1263 */ monster.getMap().spawnMist(mist, true);
                    }
                }
            },dealy);
     } 
   }
   
   public static final void RemoveObstacle(LittleEndianAccessor slea, MapleClient c) {
        /* 1271 */ int size = slea.readInt();
        /* 1272 */ for (int i = 0; i < size; i++) {
            /* 1273 */ int objid = slea.readInt();
            /* 1274 */ int hit = slea.readInt();
            /* 1275 */ Point pos = slea.readIntPos();
            /* 1276 */ if (hit == 1) {
                /* 1277 */ Obstacle ob = null;
                /* 1278 */ for (Obstacle o : c.getPlayer().getMap().getAllObstacle()) {
                    /* 1279 */ if (o.getObjectId() == objid) {
                        /* 1280 */ ob = o;
                        break;
                    }
                }
                /* 1284 */ if (ob != null && ( /* 1285 */c.getPlayer().getMapId() == 450008750 || c.getPlayer().getMapId() == 450008150) && ob.getKey() == 64) {
                    /* 1286 */ boolean already = false;
                    /* 1287 */ for (MapleMist m : c.getPlayer().getMap().getAllMistsThreadsafe()) {
                        /* 1288 */ if (m.getMobSkill().getSkillId() == 242) {
                            /* 1289 */ already = true;
                            break;
                        }
                    }
                    /* 1293 */ if (!already) {
                        /* 1294 */ for (MapleMonster monster : c.getPlayer().getMap().getAllMonster()) {
                            /* 1295 */ if (monster.getId() == 8880305) {
                                /* 1296 */ MapleMist mist = new MapleMist(new Rectangle(-204, (monster.getTruePosition()).y, 408, 300), monster, MobSkillFactory.getMobSkill(242, 4), 210000000);
                                /* 1297 */ mist.setPosition(monster.getPosition());
                                /* 1298 */ monster.getMap().spawnMist(mist, false);
                                /* 1299 */ mist = new MapleMist(new Rectangle(-204, (monster.getTruePosition()).y, 408, 300), monster, MobSkillFactory.getMobSkill(242, 4), 210000000);
                                /* 1300 */ mist.setPosition(monster.getPosition());
                                /* 1301 */ monster.getMap().spawnMist(mist, false);
                            }
                        }
                    }
                }
            }

            /* 1308 */ List<Obstacle> remove = new ArrayList<>();
            /* 1309 */ for (Obstacle ob : c.getPlayer().getMap().getAllObstacle()) {
                /* 1310 */ if (ob.getObjectId() == objid) {
                    /* 1311 */ remove.add(ob);
                }
            }
            /* 1314 */ for (Obstacle o : remove) {
                /* 1315 */ c.getPlayer().getMap().removeMapObject((MapleMapObject) o);
            }
        }
    }

    public static final void RemoveEnergtSphere(LittleEndianAccessor slea, MapleClient c) {
        /* 1321 */ int objid = slea.readInt();
        /* 1322 */ boolean hit = (slea.readInt() == 1);
        /* 1323 */ if (hit) {
            /* 1324 */ if (c.getPlayer().getBuffedEffect(SecondaryStat.IndieNotDamaged) == null) {
                /* 1325 */ MobSkill ms = MobSkillFactory.getMobSkill(123, 63);
                /* 1326 */ ms.setDuration(2000L);
                /* 1327 */ c.getPlayer().giveDebuff(SecondaryStat.Stun, ms);
            }
            /* 1329 */        } else if (!hit) {
            /* 1330 */ MapleEnergySphere mes = c.getPlayer().getMap().getEnergySphere(objid);
            /* 1331 */ if (mes != null) {
                /* 1332 */ int num = mes.getNum();
                /* 1333 */ if (c.getPlayer().getMap().getNodez().getEnvironments().get(num) != null) {
                    /* 1334 */ ((MapleNodes.Environment) c.getPlayer().getMap().getNodez().getEnvironments().get(num)).setX(mes.getCustomx() + 91);
                    /* 1335 */ ((MapleNodes.Environment) c.getPlayer().getMap().getNodez().getEnvironments().get(num)).setY(-16);
                    /* 1336 */ ((MapleNodes.Environment) c.getPlayer().getMap().getNodez().getEnvironments().get(num)).setShow(true);
                }
                /* 1338 */ c.getPlayer().getMap().broadcastMessage(CField.getUpdateEnvironment(c.getPlayer().getMap().getNodez().getEnvironments()));
                /* 1339 */ c.getPlayer().getMap().removeMapObject((MapleMapObject) mes);
            }
        }
    }

    public static final void DemianSwordHandle(LittleEndianAccessor slea, MapleClient c) {
        /* 1345 */ int objid = slea.readInt();
        /* 1346 */ slea.readShort();
        /* 1347 */ int count = slea.readShort();
        /* 1348 */ Point pos1 = slea.readIntPos();
        /* 1349 */ Point pos2 = slea.readIntPos();
        /* 1350 */ MapleFlyingSword mfs = c.getPlayer().getMap().getFlyingSword(objid);
        /* 1351 */ int[] demians = {8880100, 8880110, 8880101, 8880111};

        /* 1353 */ MapleMonster demian = null;
        /* 1354 */ for (int id : demians) {
            /* 1355 */ demian = c.getPlayer().getMap().getMonsterById(id);
            /* 1356 */ if (demian != null) {
                break;
            }
        }

        /* 1361 */ if (mfs != null && demian != null && mfs.getNodes().size() > 10) {
            /* 1362 */ if (mfs.getTarget() == null) {
                /* 1363 */ mfs.setTarget(c.getRandomCharacter());
            }
            /* 1365 */ if (mfs.getNodes().size() - 1 == count) /* 1366 */ {
                mfs.tryAttack(c.getPlayer().getMap(), pos1);
            }
        }
    }
}


/* Location:              C:\Users\Phellos\Desktop\\Ozoh디컴.jar!\handling\channel\handler\MobHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
