package server.maps;

import client.MapleCharacter;
import client.MapleClient;
import client.MapleQuestStatus;
import client.PlatformerRecord;
import client.Skill;
import client.SkillEntry;
import client.SkillFactory;
import handling.channel.ChannelServer;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import scripting.EventManager;
import scripting.NPCScriptManager;
import server.MapleItemInformationProvider;
import server.Randomizer;
import server.Timer;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.life.OverrideMonsterStats;
import server.polofritto.BountyHunting;
import server.polofritto.DefenseTowerWave;
import server.polofritto.FrittoEagle;
import server.polofritto.FrittoEgg;
import server.quest.MapleQuest;
import server.quest.party.MapleNettPyramid;
import tools.FileoutputUtil;
import tools.packet.CField;
import tools.packet.CWvsContext;
import tools.packet.MobPacket;
import tools.packet.SLFCGPacket;

public class MapScriptMethods {
  private static final Point witchTowerPos = new Point(-60, 184);
  
  private static final String[] mulungEffects = new String[] { "무릉도장에 도전한 것을 후회하게 해주겠다! 어서 들어와봐!", "기다리고 있었다! 용기가 남았다면 들어와 보시지!", "배짱 하나는 두둑하군! 현명함과 무모함을 혼동하지말라고!", "무릉도장에 도전하다니 용기가 가상하군!", "패배의 길을 걷고싶다면 들어오라고!" };
  
   private static enum onFirstUserEnter {

        dojang_Eff,
        dojang_Msg,
        PinkBeen_before,
        onRewordMap,
        StageMsg_together,
        StageMsg_crack,
        StageMsg_davy,
        StageMsg_goddess,
        party6weatherMsg,
        StageMsg_juliet,
        StageMsg_romio,
        will_phase1,
        will_phase2,
        will_phase3,
        WUK_StageEnter,
        JinHillah_onFirstUserEnter,
        moonrabbit_mapEnter,
        Fenter_450004250,
        Fenter_450004150,
        astaroth_summon,
        boss_Ravana,
        boss_Ravana_mirror,
        killing_BonusSetting,
        killing_MapSetting,
        metro_firstSetting,
        balog_bonusSetting,
        balog_summon,
        easy_balog_summon,
        Sky_TrapFEnter,
        pyramidWeather,
        shammos_Fenter,
        PRaid_D_Fenter,
        PRaid_B_Fenter,
        summon_pepeking,
        Xerxes_summon,
        VanLeon_Before,
        cygnus_Summon,
        storymap_scenario,
        shammos_FStart,
        kenta_mapEnter,
        iceman_FEnter,
        iceman_Boss,
        Polo_Defence,
        prisonBreak_mapEnter,
        Visitor_Cube_poison,
        Visitor_Cube_Hunting_Enter_First,
        VisitorCubePhase00_Start,
        visitorCube_addmobEnter,
        Visitor_Cube_PickAnswer_Enter_First_1,
        visitorCube_medicroom_Enter,
        visitorCube_iceyunna_Enter,
        Visitor_Cube_AreaCheck_Enter_First,
        visitorCube_boomboom_Enter,
        visitorCube_boomboom2_Enter,
        CubeBossbang_Enter,
        MalayBoss_Int,
        mPark_summonBoss,
        hontale_boss1,
        hontale_boss2,
        queen_summon0,
        pierre_Summon1,
        pierre_Summon,
        banban_Summon,
        firstenter_bossBlackMage,
        dusk_onFirstUserEnter,
        NULL;

        private static onFirstUserEnter fromString(String Str) {
            try {
                return valueOf(Str);
            } catch (IllegalArgumentException ex) {
                return NULL;
            }
        }
    };

    private static enum onUserEnter {

        babyPigMap,
        crash_Dragon,
        evanleaveD,
        getDragonEgg,
        meetWithDragon,
        go1010100,
        go1010200,
        go1010300,
        go1010400,
        will_phase1_everyone,
        will_phase2_everyone,
        will_phase3_everyone,
        dunkel_timeRecord,
        dunkel_boss,
        JinHillah_onUserEnter,
        evanPromotion,
        PromiseDragon,
        evanTogether,
        incubation_dragon,
        TD_MC_Openning,
        TD_MC_gasi,
        TD_MC_title,
        magnus_enter_HP,
        Akayrum_ExpeditionEnter,
        bhb2_scEnterHp,
        bhb3_scEnterHp,
        cygnusJobTutorial,
        cygnusTest,
        Polo_Wave,
        Fritto_Eagle_Enter,
        Fritto_Egg_Enter,
        Fritto_Dancing_Enter,
        startEreb,
        enter_450004150,
        PinkBeenJob_Event,
        dojang_Msg,
        dojang_1st,
        reundodraco,
        undomorphdarco,
        explorationPoint,
        goAdventure,
        go10000,
        go20000,
        go30000,
        go40000,
        go50000,
        go1000000,
        go1010000,
        go1020000,
        go2000000,
        goArcher,
        goPirate,
        goRogue,
        goMagician,
        goSwordman,
        goLith,
        iceCave,
        mirrorCave,
        aranDirection,
        rienArrow,
        rien,
        check_count,
        Massacre_first,
        Massacre_result,
        aranTutorAlone,
        evanAlone,
        dojang_QcheckSet,
        Sky_StageEnter,
        outCase,
        balog_buff,
        balog_dateSet,
        Sky_BossEnter,
        Sky_GateMapEnter,
        shammos_Enter,
        shammos_Result,
        shammos_Base,
        dollCave00,
        dollCave01,
        dollCave02,
        Sky_Quest,
        enterBlackfrog,
        onSDI,
        blackSDI,
        summonIceWall,
        metro_firstSetting,
        start_itemTake,
        findvioleta,
        pepeking_effect,
        TD_MC_keycheck,
        TD_MC_gasi2,
        in_secretroom,
        sealGarden,
        TD_NC_title,
        TD_neo_BossEnter,
        PRaid_D_Enter,
        PRaid_B_Enter,
        PRaid_Revive,
        PRaid_W_Enter,
        PRaid_WinEnter,
        PRaid_FailEnter,
        Resi_tutor10,
        Resi_tutor20,
        Resi_tutor30,
        Resi_tutor40,
        Resi_tutor50,
        Resi_tutor60,
        Resi_tutor70,
        Resi_tutor80,
        Resi_tutor50_1,
        summonSchiller,
        q31102e,
        q31103s,
        jail,
        VanLeon_ExpeditionEnter,
        cygnus_ExpeditionEnter,
        knights_Summon,
        TCMobrevive,
        mPark_stageEff,
        mPark_Enter,
        moonrabbit_takeawayitem,
        StageMsg_crack,
        shammos_Start,
        iceman_Enter,
        prisonBreak_1stageEnter,
        VisitorleaveDirectionMode,
        visitorPT_Enter,
        VisitorCubePhase00_Enter,
        visitor_ReviveMap,
        cannon_tuto_01,
        cannon_tuto_direction,
        cannon_tuto_direction1,
        cannon_tuto_direction2,
        userInBattleSquare,
        merTutorDrecotion00,
        merTutorDrecotion10,
        merTutorDrecotion20,
        merStandAlone,
        merOutStandAlone,
        merTutorSleep00,
        merTutorSleep01,
        merTutorSleep02,
        EntereurelTW,
        ds_tuto_ill0,
        ds_tuto_0_0,
        ds_tuto_1_0,
        ds_tuto_3_0,
        ds_tuto_3_1,
        ds_tuto_4_0,
        ds_tuto_5_0,
        ds_tuto_2_prep,
        ds_tuto_1_before,
        ds_tuto_2_before,
        ds_tuto_home_before,
        ds_tuto_ani,
        PTtutor000,
        enter_993014200,
        enter_993018200,
        enter_993021200,
        enter_993029200,
        enter_hungryMuto,
        enter_hungryMutoEasy,
        enter_hungryMutoHard,
        enter_450002024,
        enter_993001000, //갓오컨 로비
        enter_pfTutorialStage,//갓오컨 시작
        enter_910143000,
        enter_450004200,
        enter_993192002,
        enter_993192003,
        enter_993192004,
        enter_993192005,
        enter_993192006,
        enter_993192007,
        BloomingRace_reset,
        enter_993194001,
        enter_993194002,
        miniGameVS_Start,
        fireWolf_Enter,
        NULL;

        private static onUserEnter fromString(String Str) {
            try {
                return valueOf(Str);
            } catch (IllegalArgumentException ex) {
                return NULL;
            }
        }
    };

    private static enum directionInfo {

        merTutorDrecotion01,
        merTutorDrecotion02,
        merTutorDrecotion03,
        merTutorDrecotion04,
        merTutorDrecotion05,
        merTutorDrecotion12,
        merTutorDrecotion21,
        ds_tuto_0_1,
        ds_tuto_0_2,
        ds_tuto_0_3,
        NULL;

        private static directionInfo fromString(String Str) {
            try {
                return valueOf(Str);
            } catch (IllegalArgumentException ex) {
                return NULL;
            }
        }
    };
  
    public static void startScript_FirstUser(MapleClient c, String scriptName) {
        if (c.getPlayer() == null) {
            return;
        }
        c.getPlayer().dropMessageGM(-8, "펄스트 : " + scriptName);
        block0 : switch (onFirstUserEnter.fromString(scriptName)) {
            case firstenter_bossBlackMage: {
                Timer.EventTimer.getInstance().schedule(() -> {
                    if (c.getPlayer().getMapId() == 450013100) {
                        c.getPlayer().getMap().broadcastMessage(CField.enforceMSG("검은 마법사와 대적하기 위해서는 그를 호위하는 창조와 파괴의 기사들을 물리쳐야 한다.", 265, 8000));
                    } else if (c.getPlayer().getMapId() == 450013300) {
                        c.getPlayer().getMap().broadcastMessage(CField.enforceMSG("드디어 검은 마법사의 앞에 바로 섰다. 모든 힘을 다해 그를 물리치자.", 265, 8000));
                    } else if (c.getPlayer().getMapId() == 450013500) {
                        c.getPlayer().getMap().broadcastMessage(CField.enforceMSG("저 모습은 마치 신의 권능이라도 얻은 것 같다. 설사 상대가 신이라고 할지라도 모두를 위해 여기서 저지해야 한다.", 265, 8000));
                    }
                    if (c.getPlayer().getMapId() == 450013100) {
                        c.getPlayer().getMap().broadcastMessage(CField.ImageTalkNpc(0, 4000, "이 지역에서 발생되는 공격은 창조나 파괴의 저주를 거는 것 같다. 만약 두 저주가 동시에 걸린다면 #b큰 피해#k를 입으니 조심하자"));
                    } else if (c.getPlayer().getMapId() == 450013300) {
                        c.getPlayer().getMap().broadcastMessage(CField.ImageTalkNpc(0, 4000, "이 지역에서 발생되는 공격은 창조나 파괴의 저주를 거는 것 같다. 만약 두 저주가 동시에 걸린다면 #b큰 피해#k를 입으니 조심하자"));
                    } else if (c.getPlayer().getMapId() == 450013500) {
                        c.getPlayer().getMap().broadcastMessage(CField.ImageTalkNpc(3003902, 4000, "#face1#가자. 나는 복수를, 너는 세계를 지키는 거야."));
                    } else if (c.getPlayer().getMapId() == 450013500) {
                        c.getPlayer().getMap().broadcastMessage(CField.ImageTalkNpc(0, 4000, "아무 것도 없는 공간…… 나 혼자 남은 것인가……"));
                    } else if (c.getPlayer().getMapId() == 450013750) {
                        c.getPlayer().getMap().broadcastMessage(CField.ImageTalkNpc(0, 5000, "창세의 알을 파괴하여 기나긴 싸움을 마무리 하자."));
                    }
                }, 1000L);
                break;
            }
            case PinkBeen_before: {
                break;
            }
            case onRewordMap: {
                MapScriptMethods.reloadWitchTower(c);
                break;
            }
            case moonrabbit_mapEnter: {
                c.getPlayer().getMap().startMapEffect("Gather the Primrose Seeds around the moon and protect the Moon Bunny!", 5120016);
                break;
            }
            case Fenter_450004250: {
                ArrayList<String> tags = new ArrayList<String>(){
                    {
                        for (int i = 1; i <= 4; ++i) {
                            this.add("except" + i);
                        }
                    }
                };
                c.getSession().writeAndFlush((Object)MobPacket.BossLucid.setStainedGlassOnOff(true, (List<String>)tags));
                break;
            }
            case StageMsg_goddess: {
                switch (c.getPlayer().getMapId()) {
                    case 920010000: {
                        c.getPlayer().getMap().startMapEffect("Please save me by collecting Cloud Pieces!", 5120019);
                        break;
                    }
                    case 920010100: {
                        c.getPlayer().getMap().startMapEffect("Bring all the pieces here to save Minerva!", 5120019);
                        break;
                    }
                    case 920010200: {
                        c.getPlayer().getMap().startMapEffect("Destroy the monsters and gather Statue Pieces!", 5120019);
                        break;
                    }
                    case 920010300: {
                        c.getPlayer().getMap().startMapEffect("Destroy the monsters in each room and gather Statue Pieces!", 5120019);
                        break;
                    }
                    case 920010400: {
                        c.getPlayer().getMap().startMapEffect("Play the correct LP of the day!", 5120019);
                        break;
                    }
                    case 920010500: {
                        c.getPlayer().getMap().startMapEffect("Find the correct combination!", 5120019);
                        break;
                    }
                    case 920010600: {
                        c.getPlayer().getMap().startMapEffect("Destroy the monsters and gather Statue Pieces!", 5120019);
                        break;
                    }
                    case 920010700: {
                        c.getPlayer().getMap().startMapEffect("Get the right combination once you get to the top!", 5120019);
                        break;
                    }
                    case 920010800: {
                        c.getPlayer().getMap().startMapEffect("Summon and defeat Papa Pixie!", 5120019);
                    }
                }
                break;
            }
            case StageMsg_crack: {
                if (c.getPlayer().getMapId() >= 922010401 && c.getPlayer().getMapId() <= 922010405) {
                    NPCScriptManager.getInstance().start(c.getPlayer().getClient(), 2007, "RP_stage2");
                }
                switch (c.getPlayer().getMapId()) {
                    case 922010100: {
                        c.getPlayer().getMap().startMapEffect("\ucc28\uc6d0\uc758 \ub77c\uce20\uc640 \ucc28\uc6d0\uc758 \ube14\ub799\ub77c\uce20\ub97c \ubaa8\ub450 \ud574\uce58\uc6b0\uace0 \ucc28\uc6d0\uc758 \ud1b5\ud589\uc99d 20\uc7a5\uc744 \ubaa8\uc544\ub77c!", 5120018);
                        break;
                    }
                    case 922010600: {
                        c.getPlayer().getMap().startMapEffect("\uc228\uaca8\uc9c4 \uc0c1\uc790\uc758 \uc554\ud638\ub97c \ud480\uace0 \uaf2d\ub300\uae30\ub85c \uc62c\ub77c\uac00\ub77c.", 5120018);
                        break;
                    }
                    case 922010700: {
                        c.getPlayer().getMap().startMapEffect("\uc774 \uacf3\uc5d0 \uc788\ub294 \ub86c\ubc14\ub4dc\ub97c \ubaa8\ub450 \ubb3c\ub9ac\uce58\uc790!", 5120018);
                        c.getPlayer().getMap().setRPTicket(0);
                        c.getPlayer().getMap().resetFully();
                        break;
                    }
                    case 922010800: {
                        c.getPlayer().getMap().startSimpleMapEffect("\ubb38\uc81c\ub97c \ub4e3\uace0 \uc815\ub2f5\uc5d0 \ub9de\ub294 \uc0c1\uc790 \uc704\ub85c \uc62c\ub77c\uac00\ub77c!", 5120018);
                        break;
                    }
                    case 922010900: {
                        c.getPlayer().getMap().startSimpleMapEffect("\uc54c\ub9ac\uc0e4\ub974\ub97c \ubb3c\ub9ac\uccd0 \uc8fc\uc138\uc694!", 5120018);
                        c.getPlayer().getMap().spawnMonsterWithEffectBelow(MapleLifeFactory.getMonster(9300012), new Point(704, 184), 15);
                        break;
                    }
                }
                break;
            }
            case StageMsg_together: {
                switch (c.getPlayer().getMapId()) {
                    case 103000800: {
                        c.getPlayer().getMap().startMapEffect("Solve the question and gather the amount of passes!", 5120017);
                        break;
                    }
                    case 103000801: {
                        c.getPlayer().getMap().startMapEffect("Get on the ropes and unveil the correct combination!", 5120017);
                        break;
                    }
                    case 103000802: {
                        c.getPlayer().getMap().startMapEffect("Get on the platforms and unveil the correct combination!", 5120017);
                        break;
                    }
                    case 103000803: {
                        c.getPlayer().getMap().startMapEffect("Get on the barrels and unveil the correct combination!", 5120017);
                        break;
                    }
                    case 103000804: {
                        c.getPlayer().getMap().startMapEffect("Defeat King Slime and his minions!", 5120017);
                    }
                }
                break;
            }
            case will_phase1: {
                c.getPlayer().getMap().broadcastMessage(CField.enforceMSG("전혀 다른 2개의 공간에 있는 윌을 동시에 공격해야 해요. 달빛을 모아서 사용하면 다른 쪽으로 이동이 가능할 것 같아요.", 245, 7000));
                break;
            }
            case will_phase2: {
                c.getPlayer().getMap().broadcastMessage(CField.enforceMSG("거울에 비친 진짜 모습을 조심하세요. 달빛을 모아서 사용하면 치유 불가 저주를 잠시 멈출 수 있을 것 같아요.", 245, 7000));
                for (MapleMonster m : c.getPlayer().getMap().getAllMonster()) {
                    if (m.getId() != 8880342 && m.getId() != 8880302) continue;
                    c.getPlayer().getMap().broadcastMessage(MobPacket.showBossHP(m));
                    c.getPlayer().getMap().broadcastMessage(MobPacket.BossWill.setWillHp(m.getWillHplist()));
                    break;
                }
                break;
            }
            case will_phase3: {
                c.getPlayer().getMap().broadcastMessage(CField.enforceMSG("윌이 진심이 된 것 같군요. 달빛을 모아서 사용하면 거미줄을 태워버릴 수 있을 것 같아요.", 245, 7000));
                for (MapleMonster m : c.getPlayer().getMap().getAllMonster()) {
                    if (m.getId() != 8880342 && m.getId() != 8880302) continue;
                    c.getPlayer().getMap().broadcastMessage(MobPacket.showBossHP(m));
                    break;
                }
                break;
            }
            case JinHillah_onFirstUserEnter: {
                c.getPlayer().getMap().broadcastMessage(CField.enforceMSG("영혼이 타오르는 양초를 힐라가 일정 시간마다 베어 없앨 것이다. 영혼을 빼앗기지 않게 조심하자.", 254, 8000));
                SkillFactory.getSkill(80002543).getEffect(1).applyTo(c.getPlayer(), false);
                break;
            }
            case dusk_onFirstUserEnter: {
                break;
            }
            case StageMsg_romio: {
                switch (c.getPlayer().getMapId()) {
                    case 926100000: {
                        c.getPlayer().getMap().startMapEffect("Please find the hidden door by investigating the Lab!", 5120021);
                        break;
                    }
                    case 926100001: {
                        c.getPlayer().getMap().startMapEffect("Find  your way through this darkness!", 5120021);
                        break;
                    }
                    case 926100100: {
                        c.getPlayer().getMap().startMapEffect("Fill the beakers to power the energy!", 5120021);
                        break;
                    }
                    case 926100200: {
                        c.getPlayer().getMap().startMapEffect("Get the files for the experiment through each door!", 5120021);
                        break;
                    }
                    case 926100203: {
                        c.getPlayer().getMap().startMapEffect("Please defeat all the monsters!", 5120021);
                        break;
                    }
                    case 926100300: {
                        c.getPlayer().getMap().startMapEffect("Find your way through the Lab!", 5120021);
                        break;
                    }
                    case 926100401: {
                        c.getPlayer().getMap().startMapEffect("Please, protect my love!", 5120021);
                    }
                }
                break;
            }
            case StageMsg_juliet: {
                switch (c.getPlayer().getMapId()) {
                    case 926110000: {
                        c.getPlayer().getMap().startMapEffect("Please find the hidden door by investigating the Lab!", 5120022);
                        break;
                    }
                    case 926110001: {
                        c.getPlayer().getMap().startMapEffect("Find  your way through this darkness!", 5120022);
                        break;
                    }
                    case 926110100: {
                        c.getPlayer().getMap().startMapEffect("Fill the beakers to power the energy!", 5120022);
                        break;
                    }
                    case 926110200: {
                        c.getPlayer().getMap().startMapEffect("Get the files for the experiment through each door!", 5120022);
                        break;
                    }
                    case 926110203: {
                        c.getPlayer().getMap().startMapEffect("Please defeat all the monsters!", 5120022);
                        break;
                    }
                    case 926110300: {
                        c.getPlayer().getMap().startMapEffect("Find your way through the Lab!", 5120022);
                        break;
                    }
                    case 926110401: {
                        c.getPlayer().getMap().startMapEffect("Please, protect my love!", 5120022);
                    }
                }
                break;
            }
    case party6weatherMsg: {
                switch (c.getPlayer().getMapId()) {
                    case 930000000: {
                        c.getPlayer().getMap().startMapEffect("\uc911\uc559\uc758 \ud3ec\ud0c8\uc744 \ud0c0\uace0 \uc785\uc7a5\ud574. \uc9c0\uae08 \ub108\uc5d0\uac8c \ubcc0\uc2e0 \ub9c8\ubc95\uc744 \uac78\uac8c.", 5120023);
                        break;
                    }
                    case 930000010: {
                        c.getPlayer().getMap().startMapEffect("\ubcf8\uc778\uc774 \ub204\uad70\uc9c0 \ud5f7\uac08\ub9ac\uc9c0 \uc54a\ub3c4\ub85d \uc790\uc2e0\uc758 \ubaa8\uc2b5\uc744 \ud655\uc778\ud574!", 5120023);
                        break;
                    }
                    case 930000100: {
                        c.getPlayer().getMap().startMapEffect("\ud2b8\ub9ac\ub85c\ub4dc \ub54c\ubb38\uc5d0 \uc232\uc774 \ub3c5\uc5d0 \uc624\uc5fc\ub418\uc5c8\uc5b4. \ud2b8\ub9ac\ub85c\ub4dc\ub97c \ubaa8\ub450 \uc5c6\uc560\uc918!", 5120023);
                        break;
                    }
                    case 930000200: {
                        c.getPlayer().getMap().startMapEffect("\uc6c5\ub369\uc774\uc5d0\uc11c \ub3c5\uc744 \ud76c\uc11d\ub41c \ub3c5\uc73c\ub85c \ubc14\uafb8\uace0, \ud76c\uc11d\ub41c \ub3c5\uc73c\ub85c \uac00\uc2dc\ub364\ubd88\uc744 \uc5c6\uc560!", 5120023);
                        break;
                    }
                    case 930000300: {
                        c.getPlayer().getMap().startMapEffect("\ub2e4\ub4e4 \uc5b4\ub514 \uac00\ubc84\ub9b0\uac70\uc57c? \ud3ec\ud0c8\uc744 \ud0c0\uace0 \ub0b4\uac00 \uc788\ub294 \uacf3\uae4c\uc9c0 \uc640!", 5120023);
                        break;
                    }
                    case 930000400: {
                        c.getPlayer().getMap().startMapEffect("\uc911\ub3c5\ub41c \uc2a4\ud504\ub77c\uc774\ud2b8\ub97c \ubb3c\ub9ac\uce58\uace0 \ubaac\uc2a4\ud130 \uad6c\uc2ac\uc744 \ubaa8\uc544\uc640\uc918!", 5120023);
                        break;
                    }
                    case 930000500: {
                        c.getPlayer().getMap().startMapEffect("\uad34\uc778\uc758 \ucc45\uc0c1 \uc55e\uc5d0 \uc788\ub294 \uc0c1\uc790\ub4e4\uc744 \uc5f4\uace0 \ubcf4\ub77c\uc0c9 \ub9c8\ub825\uc11d\uc744 \uac00\uc838\uc640!", 5120023);
                        break;
                    }
                    case 930000600: {
                        c.getPlayer().getMap().startMapEffect("\ubcf4\ub77c\uc0c9 \ub9c8\ub825\uc11d\uc744 \uac00\uc9c0\uace0 \uad34\uc778\uc758 \ucc45\uc0c1\uc744 \ud074\ub9ad\ud574 \ubd10!", 5120023);
                        break;
                    }
                }
                break;
            }
            case WUK_StageEnter: {
                switch (c.getPlayer().getMapId()) {
                    case 933011000: {
                        c.getPlayer().getMap().startMapEffect("\ud30c\ud2f0\uc6d0\ub4e4\uc740 \ud074\ub85c\ud1a0\ub97c \ucc3e\uc544\uac00\uc11c \uadf8\ub140\uac00 \ub9d0\ud55c \uac1c\uc218\ub9cc\ud07c \ub9ac\ucf00\uc774\ud130\ub97c \ubb3c\ub9ac\uce58\uace0 \ucfe0\ud3f0\uc744 \ubaa8\uc544\ub77c!", 5120017);
                        break;
                    }
                    case 933012000: {
                        c.getPlayer().getMap().startMapEffect("\ub2e4\uc74c \ub2e8\uacc4\ub85c \uac00\ub294 \ubb38\uc744 \uc5f4 \uc218 \uc788\ub294 \uc904 3\uac1c\ub97c \ucc3e\uc544\uc11c \ub9e4\ub2ec\ub824\ub77c!", 5120017);
                        c.getPlayer().getMap().broadcastMessage(CField.achievementRatio(15));
                        break;
                    }
                    case 933013000: {
                        c.getPlayer().getMap().startMapEffect("\ub2e4\uc74c \ub2e8\uacc4\ub85c \uac00\ub294 \ubb38\uc744 \uc5f4 \uc218 \uc788\ub294 \ubc1c\ud310 3\uac1c\ub97c \ucc3e\uc544\ub77c!", 5120017);
                        c.getPlayer().getMap().broadcastMessage(CField.achievementRatio(30));
                        break;
                    }
                    case 933014000: {
                        final EventManager em = c.getChannelServer().getEventSM().getEventManager("KerningPQ");
                        em.setProperty("stage4r", "0");
                        c.getPlayer().getMap().broadcastMessage(CField.achievementRatio(50));
                        final int randomNum = 0;
                        final int RanNumber = (int)Math.floor(Math.random() * 200.0 + 500.0);
                        final String to = Integer.toString(RanNumber);
                        c.getPlayer().getEventInstance().setProperty("stage4M", to);
                        c.getPlayer().getMap().setKerningPQ(0);
                        c.getPlayer().getMap().startMapEffect("\uae30\ud638\ub97c \ud65c\uc131\ud654\uc2dc\ud0a4\uace0 \uc22b\uc790\ub97c \uc8fc\uc6cc \ub2e4\uc74c \uc22b\uc790\ub97c \uc644\uc131\ud558\ub77c!" + RanNumber, 5120017);
                        break;
                    }
                    case 933015000: {
                        c.getPlayer().getMap().broadcastMessage(CField.achievementRatio(75));
                        c.getPlayer().getMap().startMapEffect("\ud0b9\uc2ac\ub77c\uc784\uc744 \ud574\uce58\uc6cc\ub77c!!", 5120017);
                        break;
                    }
                }
                break;
            }
            case prisonBreak_mapEnter: {
                switch (c.getPlayer().getMapId()) {
                    case 921160100: {
                        c.getPlayer().getMap().startMapEffect("\uc27f! \uc870\uc6a9\ud788 \uc7a5\uc560\ubb3c\ub4e4\uc744 \ud53c\ud574\uc11c \ud0d1\uc744 \ubc97\uc5b4\ub098\uc154\uc57c \ud569\ub2c8\ub2e4.", 5120053);
                        c.getPlayer().getMap().broadcastMessage(CField.achievementRatio(0));
                        break;
                    }
                    case 921160200: {
                        c.getPlayer().getMap().startMapEffect("\uacbd\ube44\ubcd1\ub4e4\uc744 \ubaa8\ub450 \ubb3c\ub9ac\uce58\uc154\uc57c \ud574\uc694. \uadf8\ub807\uc9c0 \uc54a\uc73c\uba74 \uadf8\ub4e4\uc774 \ub2e4\ub978 \uacbd\ube44\ubcd1\uae4c\uc9c0 \ubd88\ub7ec\uc62c\uaebc\uc5d0\uc694.", 5120053);
                        c.getPlayer().getMap().broadcastMessage(CField.achievementRatio(10));
                        break;
                    }
                    case 921160300: {
                        c.getPlayer().getMap().startMapEffect("\uac10\uc625\uc73c\ub85c\uc758 \uc811\uadfc\uc744 \ub9c9\uae30 \uc704\ud574 \uadf8\ub4e4\uc774 \ubbf8\ub85c\ub97c \ub9cc\ub4e4\uc5b4 \ub1a8\uc5b4\uc694. \uacf5\uc911\uac10\uc625\uc73c\ub85c \ud1b5\ud558\ub294 \ubb38\uc744 \ucc3e\uc73c\uc138\uc694!", 5120053);
                        c.getPlayer().getMap().broadcastMessage(CField.achievementRatio(20));
                        break;
                    }
                    case 921160400: {
                        c.getPlayer().getMap().startMapEffect("\ubb38\uc744 \uc9c0\ud0a4\uace0 \uc788\ub294 \uacbd\ube44\ubcd1\ub4e4\uc744 \ubaa8\ub450 \ucc98\uce58\ud558\uc138\uc694!", 5120053);
                        c.getPlayer().getMap().broadcastMessage(CField.achievementRatio(30));
                        break;
                    }
                    case 921160500: {
                        c.getPlayer().getMap().startMapEffect("\uc774\uac83\uc774 \ub9c8\uc9c0\ub9c9 \uc7a5\uc560\ubb3c\uc774\uad70\uc694. \uc7a5\uc560\ubb3c\uc744 \ud1b5\uacfc\ud574 \uacf5\uc911 \uac10\uc625\uc73c\ub85c \uc640\uc8fc\uc138\uc694.", 5120053);
                        c.getPlayer().getMap().broadcastMessage(CField.achievementRatio(50));
                        break;
                    }
                    case 921160600: {
                        c.getPlayer().getMap().broadcastMessage(CField.achievementRatio(60));
                        c.getPlayer().getMap().startMapEffect("\uacbd\ube44\ubcd1\uc744 \ucc98\uce58\ud558\uace0 \uac10\uc625 \uc5f4\uc1e0\ub97c \ub418\ucc3e\uc544 \ubb38\uc744 \uc5f4\uc5b4\uc8fc\uc138\uc694.", 5120053);
                        break;
                    }
                    case 921160700: {
                        c.getPlayer().getMap().broadcastMessage(CField.achievementRatio(70));
                        c.getPlayer().getMap().startMapEffect("\uad50\ub3c4\uad00\uc744 \ubb3c\ub9ac\uce58\uace0 \uc6b0\ub9ac\uc5d0\uac8c \uc790\uc720\ub97c \ub418\ucc3e\uc544\uc8fc\uc138\uc694!!!", 5120053);
                        c.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9300454), new Point(-954, -181));
                        break;
                    }
                }
                break;
            }
            case StageMsg_davy: {
                switch (c.getPlayer().getMapId()) {
                    case 925100000: {
                        c.getPlayer().getMap().startMapEffect("Defeat the monsters outside of the ship to advance!", 5120020);
                        break;
                    }
                    case 925100100: {
                        c.getPlayer().getMap().startMapEffect("We must prove ourselves! Get me Pirate Medals!", 5120020);
                        break;
                    }
                    case 925100200: {
                        c.getPlayer().getMap().startMapEffect("Defeat the guards here to pass!", 5120020);
                        break;
                    }
                    case 925100300: {
                        c.getPlayer().getMap().startMapEffect("Eliminate the guards here to pass!", 5120020);
                        break;
                    }
                    case 925100400: {
                        c.getPlayer().getMap().startMapEffect("Lock the doors! Seal the root of the Ship's power!", 5120020);
                        break;
                    }
                    case 925100500: {
                        c.getPlayer().getMap().startMapEffect("Destroy the Lord Pirate!", 5120020);
                    }
                }
                EventManager em = c.getChannelServer().getEventSM().getEventManager("Pirate");
                if (c.getPlayer().getMapId() != 925100500 || em == null || em.getProperty("stage5") == null) break;
                int mobId = Randomizer.nextBoolean() ? 9300107 : 9300119;
                int st = Integer.parseInt(em.getProperty("stage5"));
                switch (st) {
                    case 1: {
                        mobId = Randomizer.nextBoolean() ? 9300119 : 9300105;
                        break;
                    }
                    case 2: {
                        mobId = Randomizer.nextBoolean() ? 9300106 : 9300105;
                    }
                }
                MapleMonster shammos = MapleLifeFactory.getMonster(mobId);
                if (c.getPlayer().getEventInstance() != null) {
                    c.getPlayer().getEventInstance().registerMonster(shammos);
                }
                c.getPlayer().getMap().spawnMonsterOnGroundBelow(shammos, new Point(411, 236));
                break;
            }
            case astaroth_summon: {
                c.getPlayer().getMap().resetFully();
                c.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9400633), new Point(600, -26));
                break;
            }
            case boss_Ravana_mirror: 
            case boss_Ravana: {
                break;
            }
            case killing_BonusSetting: {
                c.getPlayer().getMap().resetFully();
                c.getSession().writeAndFlush((Object)CField.showEffect("killing/bonus/bonus"));
                c.getSession().writeAndFlush((Object)CField.showEffect("killing/bonus/stage"));
                Point pos1 = null;
                Point pos2 = null;
                Point pos3 = null;
                int spawnPer = 0;
                int mobId = 0;
                if (c.getPlayer().getMapId() >= 910320010 && c.getPlayer().getMapId() <= 910320029) {
                    pos1 = new Point(121, 218);
                    pos2 = new Point(396, 43);
                    pos3 = new Point(-63, 43);
                    mobId = 9700020;
                    spawnPer = 10;
                } else if (c.getPlayer().getMapId() >= 926010010 && c.getPlayer().getMapId() <= 926010029) {
                    pos1 = new Point(0, 88);
                    pos2 = new Point(-326, -115);
                    pos3 = new Point(361, -115);
                    mobId = 9700019;
                    spawnPer = 10;
                } else if (c.getPlayer().getMapId() >= 926010030 && c.getPlayer().getMapId() <= 926010049) {
                    pos1 = new Point(0, 88);
                    pos2 = new Point(-326, -115);
                    pos3 = new Point(361, -115);
                    mobId = 9700019;
                    spawnPer = 15;
                } else if (c.getPlayer().getMapId() >= 926010050 && c.getPlayer().getMapId() <= 926010069) {
                    pos1 = new Point(0, 88);
                    pos2 = new Point(-326, -115);
                    pos3 = new Point(361, -115);
                    mobId = 9700019;
                    spawnPer = 20;
                } else {
                    if (c.getPlayer().getMapId() < 926010070 || c.getPlayer().getMapId() > 926010089) break;
                    pos1 = new Point(0, 88);
                    pos2 = new Point(-326, -115);
                    pos3 = new Point(361, -115);
                    mobId = 9700029;
                    spawnPer = 20;
                }
                for (int i = 0; i < spawnPer; ++i) {
                    c.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(mobId), new Point(pos1));
                    c.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(mobId), new Point(pos2));
                    c.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(mobId), new Point(pos3));
                }
                break;
            }
            case mPark_summonBoss: {
                if (c.getPlayer().getEventInstance() == null || c.getPlayer().getEventInstance().getProperty("boss") == null || !c.getPlayer().getEventInstance().getProperty("boss").equals("0")) break;
                for (int i = 9800119; i < 9800125; ++i) {
                    MapleMonster boss = MapleLifeFactory.getMonster(i);
                    c.getPlayer().getEventInstance().registerMonster(boss);
                    c.getPlayer().getMap().spawnMonsterOnGroundBelow(boss, new Point(c.getPlayer().getMap().getPortal(2).getPosition()));
                }
                break;
            }
            case hontale_boss1: 
            case hontale_boss2: {
                c.getPlayer().getMap().killAllMonsters(false);
                break;
            }
            case shammos_Fenter: {
                if (c.getPlayer().getMapId() < 921120005 || c.getPlayer().getMapId() >= 921120500) break;
                MapleMonster shammos = MapleLifeFactory.getMonster(9300275);
                if (c.getPlayer().getEventInstance() != null) {
                    int averageLevel = 0;
                    int size = 0;
                    for (MapleCharacter pl : c.getPlayer().getEventInstance().getPlayers()) {
                        averageLevel += pl.getLevel();
                        ++size;
                    }
                    if (size <= 0) {
                        return;
                    }
                    shammos.changeLevel(averageLevel /= size);
                    c.getPlayer().getEventInstance().registerMonster(shammos);
                    if (c.getPlayer().getEventInstance().getProperty("HP") == null) {
                        c.getPlayer().getEventInstance().setProperty("HP", averageLevel + "000");
                    }
                    shammos.setHp(Long.parseLong(c.getPlayer().getEventInstance().getProperty("HP")));
                }
                c.getPlayer().getMap().spawnMonsterWithEffectBelow(shammos, new Point(c.getPlayer().getMap().getPortal(0).getPosition()), 12);
                shammos.switchController(c.getPlayer(), false);
                break;
            }
            case iceman_FEnter: {
                if (c.getPlayer().getMapId() < 932000100 || c.getPlayer().getMapId() >= 932000300) break;
                MapleMonster shammos = MapleLifeFactory.getMonster(9300438);
                if (c.getPlayer().getEventInstance() != null) {
                    int averageLevel = 0;
                    int size = 0;
                    for (MapleCharacter pl : c.getPlayer().getEventInstance().getPlayers()) {
                        averageLevel += pl.getLevel();
                        ++size;
                    }
                    if (size <= 0) {
                        return;
                    }
                    shammos.changeLevel(averageLevel /= size);
                    c.getPlayer().getEventInstance().registerMonster(shammos);
                    if (c.getPlayer().getEventInstance().getProperty("HP") == null) {
                        c.getPlayer().getEventInstance().setProperty("HP", averageLevel + "000");
                    }
                    shammos.setHp(Long.parseLong(c.getPlayer().getEventInstance().getProperty("HP")));
                }
                c.getPlayer().getMap().spawnMonsterWithEffectBelow(shammos, new Point(c.getPlayer().getMap().getPortal(0).getPosition()), 12);
                shammos.switchController(c.getPlayer(), false);
                break;
            }
            case PRaid_D_Fenter: {
                switch (c.getPlayer().getMapId() % 10) {
                    case 0: {
                        c.getPlayer().getMap().startMapEffect("몬스터를 모두 퇴치해라!", 5120033);
                        break;
                    }
                    case 1: {
                        c.getPlayer().getMap().startMapEffect("상자를 부수고, 나오는 몬스터를 모두 퇴치해라!", 5120033);
                        break;
                    }
                    case 2: {
                        c.getPlayer().getMap().startMapEffect("일등항해사를 퇴치해라!", 5120033);
                        break;
                    }
                    case 3: {
                        c.getPlayer().getMap().startMapEffect("몬스터를 모두 퇴치해라!", 5120033);
                        break;
                    }
                    case 4: {
                        c.getPlayer().getMap().startMapEffect("몬스터를 모두 퇴치하고, 점프대를 작동시켜서 건너편으로 건너가라!", 5120033);
                    }
                }
                break;
            }
            case PRaid_B_Fenter: {
                c.getPlayer().getMap().startMapEffect("상대편보다 먼저 몬스터를 퇴치하라!", 5120033);
                break;
            }
            case Polo_Defence: {
                c.getPlayer().getMap().resetFully();
                c.getPlayer().setBountyHunting(new BountyHunting(1));
                c.getPlayer().getBountyHunting().start(c);
                break;
            }
            case summon_pepeking: {
                c.getPlayer().getMap().resetFully();
                int rand = Randomizer.nextInt(10);
                int mob_ToSpawn = 100100;
                mob_ToSpawn = rand >= 4 ? 3300007 : (rand >= 1 ? 3300006 : 3300005);
                c.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(mob_ToSpawn), c.getPlayer().getPosition());
                break;
            }
            case Xerxes_summon: {
                c.getPlayer().getMap().resetFully();
                c.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(6160003), c.getPlayer().getPosition());
                break;
            }
            case shammos_FStart: {
                c.getPlayer().getMap().startMapEffect("Defeat the monsters!", 5120035);
                break;
            }
            case kenta_mapEnter: {
                switch (c.getPlayer().getMapId() / 100 % 10) {
                    case 1: {
                        c.getPlayer().getMap().startMapEffect("Eliminate all the monsters!", 5120052);
                        break;
                    }
                    case 2: {
                        c.getPlayer().getMap().startMapEffect("Get me 20 Air Bubbles for me to survive!", 5120052);
                        break;
                    }
                    case 3: {
                        c.getPlayer().getMap().startMapEffect("Help! Make sure I live for three minutes!", 5120052);
                        break;
                    }
                    case 4: {
                        c.getPlayer().getMap().startMapEffect("Eliminate the two Pianus!", 5120052);
                    }
                }
                break;
            }
            case cygnus_Summon: {
                break;
            }
            case iceman_Boss: {
                c.getPlayer().getMap().startMapEffect("You will perish!", 5120050);
                break;
            }
            case Visitor_Cube_poison: {
                c.getPlayer().getMap().startMapEffect("Eliminate all the monsters!", 5120039);
                break;
            }
            case Visitor_Cube_Hunting_Enter_First: {
                c.getPlayer().getMap().startMapEffect("Eliminate all the Visitors!", 5120039);
                break;
            }
            case VisitorCubePhase00_Start: {
                c.getPlayer().getMap().startMapEffect("Eliminate all the flying monsters!", 5120039);
                break;
            }
            case visitorCube_addmobEnter: {
                c.getPlayer().getMap().startMapEffect("Eliminate all the monsters by moving around the map!", 5120039);
                break;
            }
            case Visitor_Cube_PickAnswer_Enter_First_1: {
                c.getPlayer().getMap().startMapEffect("One of the aliens must have a clue to the way out.", 5120039);
                break;
            }
            case visitorCube_medicroom_Enter: {
                c.getPlayer().getMap().startMapEffect("Eliminate all of the Unjust Visitors!", 5120039);
                break;
            }
            case visitorCube_iceyunna_Enter: {
                c.getPlayer().getMap().startMapEffect("Eliminate all of the Speedy Visitors!", 5120039);
                break;
            }
            case Visitor_Cube_AreaCheck_Enter_First: {
                c.getPlayer().getMap().startMapEffect("The switch at the top of the room requires a heavy weight.", 5120039);
                break;
            }
            case visitorCube_boomboom_Enter: {
                c.getPlayer().getMap().startMapEffect("The enemy is powerful! Watch out!", 5120039);
                break;
            }
            case visitorCube_boomboom2_Enter: {
                c.getPlayer().getMap().startMapEffect("This Visitor is strong! Be careful!", 5120039);
                break;
            }
            case CubeBossbang_Enter: {
                c.getPlayer().getMap().startMapEffect("This is it! Give it your best shot!", 5120039);
                break;
            }
            case MalayBoss_Int: 
            case storymap_scenario: 
            case VanLeon_Before: 
            case dojang_Msg: 
            case balog_summon: 
            case easy_balog_summon: {
                break;
            }
            case metro_firstSetting: 
            case killing_MapSetting: 
            case Sky_TrapFEnter: 
            case balog_bonusSetting: {
                c.getPlayer().getMap().resetFully();
                break;
            }
            case pyramidWeather: {
                if (!c.getPlayer().isLeader()) break;
                boolean hard = c.getPlayer().nettDifficult == 2;
                MapleNettPyramid mnp = MapleNettPyramid.getInfo(c.getPlayer(), hard);
                if (mnp == null) break;
                for (MapleNettPyramid.MapleNettPyramidMember pm : mnp.getMembers()) {
                    if (pm.getChr() != null) {
                        int v = pm.getChr().getV("NettPyramid") == null ? 0 : Integer.parseInt(pm.getChr().getV("NettPyramid"));
                        pm.getChr().addKV("NettPyramid", String.valueOf(v + 1));
                        pm.getChr().setNettPyramid(mnp);
                        continue;
                    }
                    c.getPlayer().dropMessage(5, "오류가 발생했습니다.");
                }
                mnp.firstNettPyramid(c.getPlayer());
                break;
            }
            default: {
                FileoutputUtil.log("Log_Script_Except.rtf", "Unhandled script : " + scriptName + ", type : onFirstUserEnter - MAPID " + c.getPlayer().getMapId());
            }
        }
    }
  
   public static void startScript_User(final MapleClient c, final String scriptName) {
        if (c.getPlayer() == null) {
            return;
        }
        String data = "";
        if (c.getPlayer().isGM()) {
            c.getPlayer().dropMessage(6, "startScript_User : " + scriptName);
        }
        switch (onUserEnter.fromString(scriptName)) {
            case dojang_QcheckSet: {
                c.removeClickedNPC();
                NPCScriptManager.getInstance().dispose(c);
                c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                NPCScriptManager.getInstance().start(c, 2007, "dojo_exit");
                break;
            }
            case dojang_1st: {
                c.getPlayer().getMap().startMapEffect("\uc81c\ud55c\uc2dc\uac04\uc740 15\ubd84, \ucd5c\ub300\ud55c \uc2e0\uc18d\ud558\uac8c \ubaac\uc2a4\ud130\ub97c \uc4f0\ub7ec\ud2b8\ub9ac\uace0 \ub2e4\uc74c \uce35\uc73c\ub85c \uc62c\ub77c\uac00\uba74 \ub3fc!", 5120024, 8000);
                break;
            }
            case cannon_tuto_direction: {
                showIntro(c, "Effect/Direction4.img/cannonshooter/Scene00");
                showIntro(c, "Effect/Direction4.img/cannonshooter/out00");
                break;
            }
            case dunkel_timeRecord: {
                c.getPlayer().getMap().broadcastMessage(CField.enforceMSG("\uce5c\uc704\ub300\uc7a5 \ub4c4\ucf08 : \ub098\uc640 \ub098\uc758 \uad70\ub2e8\uc774 \uc788\ub294 \uc774\uc0c1 \uc704\ub300\ud558\uc2e0 \ubd84\uaed8\ub294 \uc190\ub05d \ud558\ub098 \ub300\uc9c0 \ubabb\ud55c\ub2e4!", 272, 5000));
                break;
            }
            case cannon_tuto_direction1: {
                c.getSession().writeAndFlush((Object)CField.UIPacket.IntroDisableUI(true));
                c.getSession().writeAndFlush((Object)CField.UIPacket.IntroLock(true));
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo("Effect/Direction4.img/effect/cannonshooter/balloon/0", 5000, 0, 0, 1, 0));
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo("Effect/Direction4.img/effect/cannonshooter/balloon/1", 5000, 0, 0, 1, 0));
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo("Effect/Direction4.img/effect/cannonshooter/balloon/2", 5000, 0, 0, 1, 0));
                c.getSession().writeAndFlush((Object)CField.EffectPacket.showWZEffect("Effect/Direction4.img/cannonshooter/face04"));
                c.getSession().writeAndFlush((Object)CField.EffectPacket.showWZEffect("Effect/Direction4.img/cannonshooter/out01"));
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo(1, 5000));
                break;
            }
            case cannon_tuto_direction2: {
                showIntro(c, "Effect/Direction4.img/cannonshooter/Scene01");
                showIntro(c, "Effect/Direction4.img/cannonshooter/out02");
                break;
            }
            case cygnusTest: {
                showIntro(c, "Effect/Direction.img/cygnus/Scene" + ((c.getPlayer().getMapId() == 913040006) ? 9 : (c.getPlayer().getMapId() - 913040000)));
                break;
            }
            case Polo_Wave: {
                c.getPlayer().getMap().resetFully();
                c.getPlayer().setDefenseTowerWave(new DefenseTowerWave(1, 20));
                c.getPlayer().getDefenseTowerWave().start(c);
                break;
            }
            case Fritto_Eagle_Enter: {
                c.getPlayer().getMap().resetFully();
                c.getPlayer().setFrittoEagle(new FrittoEagle(0, 20));
                c.getPlayer().getFrittoEagle().start(c);
                break;
            }
            case Fritto_Egg_Enter: {
                c.getPlayer().getMap().resetFully();
                c.getPlayer().setFrittoEgg(new FrittoEgg(0));
                c.getPlayer().getFrittoEgg().start(c);
                break;
            }
            case magnus_enter_HP: {
                if (c.getPlayer().getMap().getNumMonsters() == 0) {
                    final MapleMonster magnus = MapleLifeFactory.getMonster((c.getPlayer().getMapId() == 401060300) ? 8880010 : ((c.getPlayer().getMapId() == 401060200) ? 8880002 : 8880000));
                    magnus.setCustomInfo(magnus.getId(), 0, Randomizer.rand(20000, 40000));
                    c.getPlayer().getMap().spawnMonsterOnGroundBelow(magnus, new Point(2800, -1347));
                    break;
                }
                break;
            }
            case bhb2_scEnterHp:
            case bhb3_scEnterHp: {
                if (c.getPlayer().getMap().getNumMonsters() == 0) {
                    c.getPlayer().getMap().spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster((c.getPlayer().getMapId() == 350060190) ? 8950101 : ((c.getPlayer().getMapId() == 350060210) ? 8950102 : ((c.getPlayer().getMapId() == 350060200) ? 8950002 : 8950001))), c.getPlayer().getTruePosition());
                    break;
                }
                break;
            }
            case Akayrum_ExpeditionEnter: {
                if (c.getPlayer().getMap().getNumMonsters() == 0) {
                    c.getPlayer().getMap().broadcastMessage(CField.EffectPacket.showEffect(c.getPlayer(), 0, 0, 34, 0, 0, (byte)(c.getPlayer().isFacingLeft() ? 1 : 0), false, c.getPlayer().getTruePosition(), "Voice.img/akayrum/2", null));
                    c.getPlayer().getMap().broadcastMessage(CField.startMapEffect("\uc6a9\uae30\uc640 \ub9cc\uc6a9\uc744 \uad6c\ubd84\ud558\uc9c0 \ubabb\ud558\ub294 \uc790\ub4e4\uc774\uc5ec. \ubaa9\uc228\uc774 \uc544\uae5d\uc9c0 \uc54a\ub2e4\uba74 \ub0b4\uac8c \ub364\ube44\ub3c4\ub85d. \ud6c4\ud6c4.", 5120056, true));
                    c.getPlayer().getMap().spawnNpc(2144010, new Point(320, -181));
                    break;
                }
                break;
            }
            case cygnusJobTutorial: {
                showIntro(c, "Effect/Direction.img/cygnusJobTutorial/Scene" + (c.getPlayer().getMapId() - 913040100));
                break;
            }
            case shammos_Enter: {
                if (c.getPlayer().getEventInstance() != null && c.getPlayer().getMapId() == 921120500) {
                    NPCScriptManager.getInstance().dispose(c);
                    c.removeClickedNPC();
                    NPCScriptManager.getInstance().start(c, 2022006);
                    break;
                }
                break;
            }
            case iceman_Enter: {
                if (c.getPlayer().getEventInstance() != null && c.getPlayer().getMapId() == 932000300) {
                    NPCScriptManager.getInstance().dispose(c);
                    c.removeClickedNPC();
                    NPCScriptManager.getInstance().start(c, 2159020);
                    break;
                }
                break;
            }
            case start_itemTake: {
                final EventManager em = c.getChannelServer().getEventSM().getEventManager("OrbisPQ");
                if (em != null && em.getProperty("pre").equals("0")) {
                    NPCScriptManager.getInstance().dispose(c);
                    c.removeClickedNPC();
                    NPCScriptManager.getInstance().start(c, 2013001);
                    break;
                }
                break;
            }
            case TD_neo_BossEnter:
            case findvioleta: {
                c.getPlayer().getMap().resetFully();
                break;
            }
            case StageMsg_crack: {
                if (c.getPlayer().getMapId() == 922010400) {
                    final MapleMapFactory mf = c.getChannelServer().getMapFactory();
                    int q = 0;
                    for (int i = 0; i < 5; ++i) {
                        q += mf.getMap(922010401 + i).getNumMonsters();
                    }
                    if (q > 0) {
                        c.getPlayer().dropMessage(-1, "There are still " + q + " monsters remaining.");
                    }
                    break;
                }
                if (c.getPlayer().getMapId() < 922010401 || c.getPlayer().getMapId() > 922010405) {
                    break;
                }
                if (c.getPlayer().getMap().getNumMonsters() > 0) {
                    c.getPlayer().dropMessage(-1, "There are still some monsters remaining in this map.");
                    break;
                }
                c.getPlayer().dropMessage(-1, "There are no monsters remaining in this map.");
                break;
            }
            case q31102e: {
                if (c.getPlayer().getQuestStatus(31102) == 1) {
                    MapleQuest.getInstance(31102).forceComplete(c.getPlayer(), 2140000);
                    break;
                }
                break;
            }
            case q31103s: {
                if (c.getPlayer().getQuestStatus(31103) == 0) {
                    MapleQuest.getInstance(31103).forceComplete(c.getPlayer(), 2142003);
                    break;
                }
                break;
            }
            case Resi_tutor20: {
                c.getSession().writeAndFlush((Object)CField.MapEff("resistance/tutorialGuide"));
                break;
            }
            case Resi_tutor30: {
                c.getSession().writeAndFlush((Object)CField.EffectPacket.showWZEffect("Effect/OnUserEff.img/guideEffect/resistanceTutorial/userTalk"));
                break;
            }
            case Resi_tutor40: {
                NPCScriptManager.getInstance().dispose(c);
                c.removeClickedNPC();
                NPCScriptManager.getInstance().start(c, 2159012);
                break;
            }
            case Resi_tutor50: {
                c.getSession().writeAndFlush((Object)CField.UIPacket.IntroDisableUI(false));
                c.getSession().writeAndFlush((Object)CField.UIPacket.IntroLock(false));
                c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                NPCScriptManager.getInstance().dispose(c);
                c.removeClickedNPC();
                NPCScriptManager.getInstance().start(c, 2159006);
                break;
            }
            case Resi_tutor70: {
                showIntro(c, "Effect/Direction4.img/Resistance/TalkJ");
                break;
            }
            case prisonBreak_1stageEnter:
            case shammos_Start:
            case moonrabbit_takeawayitem:
            case TCMobrevive:
            case cygnus_ExpeditionEnter:
            case knights_Summon:
            case VanLeon_ExpeditionEnter:
            case Resi_tutor10:
            case Resi_tutor60:
            case Resi_tutor50_1:
            case sealGarden:
            case in_secretroom:
            case TD_MC_gasi2:
            case TD_MC_keycheck:
            case pepeking_effect:
            case userInBattleSquare:
            case summonSchiller:
            case VisitorleaveDirectionMode:
            case visitorPT_Enter:
            case VisitorCubePhase00_Enter:
            case visitor_ReviveMap:
            case PRaid_D_Enter:
            case PRaid_B_Enter:
            case PRaid_WinEnter:
            case PRaid_FailEnter:
            case PRaid_Revive:
            case metro_firstSetting:
            case blackSDI:
            case summonIceWall:
            case onSDI:
            case enterBlackfrog:
            case Sky_Quest:
            case dollCave00:
            case dollCave01:
            case dollCave02:
            case shammos_Base:
            case shammos_Result:
            case Sky_BossEnter:
            case Sky_GateMapEnter:
            case balog_dateSet:
            case balog_buff:
            case outCase:
            case Sky_StageEnter:
            case evanTogether:
            case merStandAlone:
            case EntereurelTW:
            case aranTutorAlone:
            case evanAlone: {
                c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                break;
            }
            case merOutStandAlone: {
                if (c.getPlayer().getQuestStatus(24001) == 1) {
                    MapleQuest.getInstance(24001).forceComplete(c.getPlayer(), 0);
                    c.getPlayer().dropMessage(5, "Quest complete.");
                    break;
                }
                break;
            }
            case merTutorSleep00: {
                showIntro(c, "Effect/Direction5.img/mersedesTutorial/Scene0");
                final Map<Skill, SkillEntry> sa = new HashMap<Skill, SkillEntry>();
                sa.put(SkillFactory.getSkill(20021181), new SkillEntry(-1, (byte)0, -1L));
                sa.put(SkillFactory.getSkill(20021166), new SkillEntry(-1, (byte)0, -1L));
                sa.put(SkillFactory.getSkill(20020109), new SkillEntry(1, (byte)1, -1L));
                sa.put(SkillFactory.getSkill(20021110), new SkillEntry(1, (byte)1, -1L));
                sa.put(SkillFactory.getSkill(20020111), new SkillEntry(1, (byte)1, -1L));
                sa.put(SkillFactory.getSkill(20020112), new SkillEntry(1, (byte)1, -1L));
                c.getPlayer().changeSkillsLevel(sa);
                break;
            }
            case merTutorSleep01: {
                while (c.getPlayer().getLevel() < 10) {
                    c.getPlayer().levelUp();
                }
                c.getPlayer().changeJob(2300);
                showIntro(c, "Effect/Direction5.img/mersedesTutorial/Scene1");
                break;
            }
            case merTutorSleep02: {
                c.getSession().writeAndFlush((Object)CField.UIPacket.IntroEnableUI(0));
                break;
            }
            case merTutorDrecotion00: {
                c.getSession().writeAndFlush((Object)CField.UIPacket.playMovie("Mercedes.avi", true));
                final Map<Skill, SkillEntry> sa = new HashMap<Skill, SkillEntry>();
                sa.put(SkillFactory.getSkill(20021181), new SkillEntry(1, (byte)1, -1L));
                sa.put(SkillFactory.getSkill(20021166), new SkillEntry(1, (byte)1, -1L));
                c.getPlayer().changeSkillsLevel(sa);
                break;
            }
            case merTutorDrecotion10: {
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionStatus(true));
                c.getSession().writeAndFlush((Object)CField.UIPacket.IntroEnableUI(1));
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo("Effect/Direction5.img/effect/mercedesInIce/merBalloon/6", 2000, 0, -100, 1, 0));
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo(1, 2000));
                c.getPlayer().setDirection(0);
                break;
            }
            case merTutorDrecotion20: {
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionStatus(true));
                c.getSession().writeAndFlush((Object)CField.UIPacket.IntroEnableUI(1));
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo("Effect/Direction5.img/effect/mercedesInIce/merBalloon/9", 2000, 0, -100, 1, 0));
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo(1, 2000));
                c.getPlayer().setDirection(0);
                break;
            }
            case ds_tuto_ani: {
                c.getSession().writeAndFlush((Object)CField.UIPacket.playMovie("DemonSlayer1.avi", true));
                break;
            }
            case Resi_tutor80:
            case startEreb:
            case mirrorCave:
            case babyPigMap:
            case evanleaveD: {
                c.getSession().writeAndFlush((Object)CField.UIPacket.IntroDisableUI(false));
                c.getSession().writeAndFlush((Object)CField.UIPacket.IntroLock(false));
                c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                break;
            }
            case dojang_Msg: {
                c.getPlayer().getMap().startMapEffect(MapScriptMethods.mulungEffects[Randomizer.nextInt(MapScriptMethods.mulungEffects.length)], 5120024);
                break;
            }
            case undomorphdarco:
            case reundodraco: {
                c.getPlayer().cancelEffect(MapleItemInformationProvider.getInstance().getItemEffect(2210016));
                break;
            }
            case goAdventure: {
                showIntro(c, "Effect/Direction3.img/goAdventure/Scene" + ((c.getPlayer().getGender() == 0) ? "0" : "1"));
                break;
            }
            case crash_Dragon: {
                showIntro(c, "Effect/Direction4.img/crash/Scene" + ((c.getPlayer().getGender() == 0) ? "0" : "1"));
                break;
            }
            case getDragonEgg: {
                showIntro(c, "Effect/Direction4.img/getDragonEgg/Scene" + ((c.getPlayer().getGender() == 0) ? "0" : "1"));
                break;
            }
            case meetWithDragon: {
                showIntro(c, "Effect/Direction4.img/meetWithDragon/Scene" + ((c.getPlayer().getGender() == 0) ? "0" : "1"));
                break;
            }
            case PromiseDragon: {
                showIntro(c, "Effect/Direction4.img/PromiseDragon/Scene" + ((c.getPlayer().getGender() == 0) ? "0" : "1"));
                break;
            }
            case evanPromotion: {
                switch (c.getPlayer().getMapId()) {
                    case 900090000: {
                        data = "Effect/Direction4.img/promotion/Scene0" + ((c.getPlayer().getGender() == 0) ? "0" : "1");
                        break;
                    }
                    case 900090001: {
                        data = "Effect/Direction4.img/promotion/Scene1";
                        break;
                    }
                    case 900090002: {
                        data = "Effect/Direction4.img/promotion/Scene2" + ((c.getPlayer().getGender() == 0) ? "0" : "1");
                        break;
                    }
                    case 900090003: {
                        data = "Effect/Direction4.img/promotion/Scene3";
                        break;
                    }
                    case 900090004: {
                        c.getSession().writeAndFlush((Object)CField.UIPacket.IntroDisableUI(false));
                        c.getSession().writeAndFlush((Object)CField.UIPacket.IntroLock(false));
                        c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                        final MapleMap mapto = c.getChannelServer().getMapFactory().getMap(900010000);
                        c.getPlayer().changeMap(mapto, mapto.getPortal(0));
                        return;
                    }
                }
                showIntro(c, data);
                break;
            }
            case will_phase2_everyone: {
                if (!c.getPlayer().getBuffedValue(80002404)) {
                    SkillFactory.getSkill(80002404).getEffect(1).applyTo(c.getPlayer(), false);
                    break;
                }
                break;
            }
            case will_phase3_everyone: {
                c.getSession().writeAndFlush((Object)MobPacket.BossWill.willThirdOne());
                break;
            }
            case JinHillah_onUserEnter: {
                c.getSession().writeAndFlush((Object)CField.JinHillah(0, c.getPlayer(), c.getPlayer().getMap()));
                c.getSession().writeAndFlush((Object)CField.JinHillah(1, c.getPlayer(), c.getPlayer().getMap()));
                if (c.getPlayer().getMap().getReqTouched() > 0) {
                    c.getSession().writeAndFlush((Object)CField.JinHillah(6, c.getPlayer(), c.getPlayer().getMap()));
                    c.getSession().writeAndFlush((Object)CField.JinHillah(7, c.getPlayer(), c.getPlayer().getMap()));
                }
                if (c.getPlayer().getMap().getSandGlassTime() > 0L) {
                    c.getSession().writeAndFlush((Object)CField.JinHillah(4, c.getPlayer(), c.getPlayer().getMap()));
                }
                if (c.getPlayer().liveCounts() > 0) {
                    c.getSession().writeAndFlush((Object)CField.JinHillah(3, c.getPlayer(), c.getPlayer().getMap()));
                    c.getPlayer().getMap().broadcastMessage(CField.JinHillah(10, c.getPlayer(), c.getPlayer().getMap()));
                    break;
                }
                break;
            }
            case mPark_stageEff: {
                c.getPlayer().dropMessage(-1, "\ud544\ub4dc \ub0b4\uc758 \ubaa8\ub4e0 \ubaac\uc2a4\ud130\ub97c \uc81c\uac70\ud574\uc57c \ub2e4\uc74c \uc2a4\ud14c\uc774\uc9c0\ub85c \uc774\ub3d9\ud558\uc2e4 \uc218 \uc788\uc2b5\ub2c8\ub2e4.");
                switch (c.getPlayer().getMapId() % 1000 / 100) {
                    case 0:
                    case 1:
                    case 2:
                    case 3: {
                        c.getSession().writeAndFlush((Object)CField.showEffect("monsterPark/stageEff/stage"));
                        c.getSession().writeAndFlush((Object)CField.showEffect("monsterPark/stageEff/number/" + (c.getPlayer().getMapId() % 1000 / 100 + 1)));
                        break;
                    }
                    case 4: {
                        if (c.getPlayer().getMapId() / 1000000 == 952) {
                            c.getSession().writeAndFlush((Object)CField.showEffect("monsterPark/stageEff/final"));
                            break;
                        }
                        c.getSession().writeAndFlush((Object)CField.showEffect("monsterPark/stageEff/stage"));
                        c.getSession().writeAndFlush((Object)CField.showEffect("monsterPark/stageEff/number/5"));
                        break;
                    }
                    case 5: {
                        c.getSession().writeAndFlush((Object)CField.showEffect("monsterPark/stageEff/final"));
                        break;
                    }
                }
                break;
            }
            case mPark_Enter: {
                if (c.getPlayer().getMapId() == 951000000) {
                    return;
                }
                break;
            }
            case TD_MC_title: {
                c.getSession().writeAndFlush((Object)CField.UIPacket.IntroDisableUI(false));
                c.getSession().writeAndFlush((Object)CField.UIPacket.IntroLock(false));
                c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                c.getSession().writeAndFlush((Object)CField.MapEff("temaD/enter/mushCatle"));
                break;
            }
            case TD_NC_title: {
                switch (c.getPlayer().getMapId() / 100 % 10) {
                    case 0: {
                        c.getSession().writeAndFlush((Object)CField.MapEff("temaD/enter/teraForest"));
                        break;
                    }
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6: {
                        c.getSession().writeAndFlush((Object)CField.MapEff("temaD/enter/neoCity" + c.getPlayer().getMapId() / 100 % 10));
                        break;
                    }
                }
                break;
            }
            case explorationPoint: {
                if (c.getPlayer().getMapId() == 104000000) {
                    c.getSession().writeAndFlush((Object)CField.UIPacket.IntroDisableUI(false));
                    c.getSession().writeAndFlush((Object)CField.UIPacket.IntroLock(false));
                    c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                    c.getSession().writeAndFlush((Object)CField.MapNameDisplay(c.getPlayer().getMapId()));
                }
                MapleQuest.MedalQuest m = null;
                for (final MapleQuest.MedalQuest mq : MapleQuest.MedalQuest.values()) {
                    for (final int j : mq.maps) {
                        if (c.getPlayer().getMapId() == j) {
                            m = mq;
                            break;
                        }
                    }
                }
                if (m != null && c.getPlayer().getLevel() >= m.level && c.getPlayer().getQuestStatus(m.questid) != 2) {
                    if (c.getPlayer().getQuestStatus(m.lquestid) != 1) {
                        MapleQuest.getInstance(m.lquestid).forceStart(c.getPlayer(), 0, "0");
                    }
                    if (c.getPlayer().getQuestStatus(m.questid) != 1) {
                        MapleQuest.getInstance(m.questid).forceStart(c.getPlayer(), 0, null);
                        final StringBuilder sb = new StringBuilder("enter=");
                        for (int i = 0; i < m.maps.length; ++i) {
                            sb.append("0");
                        }
                        c.getPlayer().updateInfoQuest(m.questid - 2005, sb.toString());
                        MapleQuest.getInstance(m.questid - 1995).forceStart(c.getPlayer(), 0, "0");
                    }
                    String quest = c.getPlayer().getInfoQuest(m.questid - 2005);
                    if (quest.length() != m.maps.length + 6) {
                        final StringBuilder sb2 = new StringBuilder("enter=");
                        for (int k = 0; k < m.maps.length; ++k) {
                            sb2.append("0");
                        }
                        quest = sb2.toString();
                        c.getPlayer().updateInfoQuest(m.questid - 2005, quest);
                    }
                    final MapleQuestStatus stat = c.getPlayer().getQuestNAdd(MapleQuest.getInstance(m.questid - 1995));
                    if (stat.getCustomData() == null) {
                        stat.setCustomData("0");
                    }
                    int number = Integer.parseInt(stat.getCustomData());
                    final StringBuilder sb3 = new StringBuilder("enter=");
                    boolean changedd = false;
                    for (int l = 0; l < m.maps.length; ++l) {
                        boolean changed = false;
                        if (c.getPlayer().getMapId() == m.maps[l] && quest.substring(l + 6, l + 7).equals("0")) {
                            sb3.append("1");
                            changed = true;
                            changedd = true;
                        }
                        if (!changed) {
                            sb3.append(quest.substring(l + 6, l + 7));
                        }
                    }
                    if (changedd) {
                        ++number;
                        c.getPlayer().updateInfoQuest(m.questid - 2005, sb3.toString());
                        MapleQuest.getInstance(m.questid - 1995).forceStart(c.getPlayer(), 0, String.valueOf(number));
                        c.getPlayer().dropMessage(-1, number + "/" + m.maps.length + "\uac1c \ud0d0\ud5d8");
                        c.getPlayer().dropMessage(-1, "\uce6d\ud638 - " + m.questname + " \ud0d0\ud5d8\uac00 \ub3c4\uc804 \uc911");
                        c.getSession().writeAndFlush((Object)CWvsContext.showQuestMsg("", "\uce6d\ud638 - " + m.questname + " \ud0d0\ud5d8\uac00 \ub3c4\uc804 \uc911. " + number + "/" + m.maps.length + "\uac1c \uc9c0\uc5ed \uc644\ub8cc"));
                    }
                    break;
                }
                break;
            }
            case go10000:
            case go1020000: {
                c.getSession().writeAndFlush((Object)CField.UIPacket.IntroDisableUI(false));
                c.getSession().writeAndFlush((Object)CField.UIPacket.IntroLock(false));
                c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
            }
            case go20000:
            case go30000:
            case go40000:
            case go50000:
            case go1000000:
            case go2000000:
            case go1010000:
            case go1010100:
            case go1010200:
            case go1010300:
            case go1010400: {
                c.getSession().writeAndFlush((Object)CField.MapNameDisplay(c.getPlayer().getMapId()));
                break;
            }
            case ds_tuto_ill0: {
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo(1, 6300));
                showIntro(c, "Effect/Direction6.img/DemonTutorial/SceneLogo");
                Timer.EventTimer.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        c.getSession().writeAndFlush((Object)CField.UIPacket.IntroDisableUI(false));
                        c.getSession().writeAndFlush((Object)CField.UIPacket.IntroLock(false));
                        c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                        final MapleMap mapto = c.getChannelServer().getMapFactory().getMap(927000000);
                        c.getPlayer().changeMap(mapto, mapto.getPortal(0));
                    }
                }, 6300L);
                break;
            }
            case ds_tuto_home_before: {
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo(3, 1));
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo(1, 30));
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionStatus(true));
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo(3, 0));
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo(1, 90));
                c.getSession().writeAndFlush((Object)CField.showEffect("demonSlayer/text11"));
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo(1, 4000));
                Timer.EventTimer.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        showIntro(c, "Effect/Direction6.img/DemonTutorial/Scene2");
                    }
                }, 1000L);
                break;
            }
            case ds_tuto_1_0: {
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo(3, 1));
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo(1, 30));
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionStatus(true));
                Timer.EventTimer.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo(3, 0));
                        c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo(4, 2159310));
                        NPCScriptManager.getInstance().start(c, 2159310);
                    }
                }, 1000L);
                break;
            }
            case ds_tuto_4_0: {
                c.getSession().writeAndFlush((Object)CField.UIPacket.IntroDisableUI(true));
                c.getSession().writeAndFlush((Object)CField.UIPacket.IntroEnableUI(1));
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionStatus(true));
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo(3, 0));
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo(4, 2159344));
                NPCScriptManager.getInstance().start(c, 2159344);
                break;
            }
            case cannon_tuto_01: {
                c.getSession().writeAndFlush((Object)CField.UIPacket.IntroDisableUI(true));
                c.getSession().writeAndFlush((Object)CField.UIPacket.IntroEnableUI(1));
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionStatus(true));
                c.getPlayer().changeSingleSkillLevel(SkillFactory.getSkill(110), 1, (byte)1);
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo(3, 0));
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo(4, 1096000));
                NPCScriptManager.getInstance().dispose(c);
                NPCScriptManager.getInstance().start(c, 1096000);
                break;
            }
            case ds_tuto_5_0: {
                c.getSession().writeAndFlush((Object)CField.UIPacket.IntroDisableUI(true));
                c.getSession().writeAndFlush((Object)CField.UIPacket.IntroEnableUI(1));
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionStatus(true));
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo(3, 0));
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo(4, 2159314));
                NPCScriptManager.getInstance().dispose(c);
                NPCScriptManager.getInstance().start(c, 2159314);
                break;
            }
            case ds_tuto_3_0: {
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo(3, 1));
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo(1, 30));
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionStatus(true));
                c.getSession().writeAndFlush((Object)CField.showEffect("demonSlayer/text12"));
                Timer.EventTimer.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo(3, 0));
                        c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo(4, 2159311));
                        NPCScriptManager.getInstance().dispose(c);
                        NPCScriptManager.getInstance().start(c, 2159311);
                    }
                }, 1000L);
                break;
            }
            case ds_tuto_3_1: {
                c.getSession().writeAndFlush((Object)CField.UIPacket.IntroDisableUI(true));
                c.getSession().writeAndFlush((Object)CField.UIPacket.IntroEnableUI(1));
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionStatus(true));
                if (!c.getPlayer().getMap().containsNPC(2159340)) {
                    c.getPlayer().getMap().spawnNpc(2159340, new Point(175, 0));
                    c.getPlayer().getMap().spawnNpc(2159341, new Point(300, 0));
                    c.getPlayer().getMap().spawnNpc(2159342, new Point(600, 0));
                }
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo("Effect/Direction5.img/effect/tuto/balloonMsg2/0", 2000, 0, -100, 1, 0));
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo("Effect/Direction5.img/effect/tuto/balloonMsg1/3", 2000, 0, -100, 1, 0));
                Timer.EventTimer.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo(3, 0));
                        c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo(4, 2159340));
                        NPCScriptManager.getInstance().dispose(c);
                        NPCScriptManager.getInstance().start(c, 2159340);
                    }
                }, 1000L);
                break;
            }
            case ds_tuto_2_before: {
                c.getSession().writeAndFlush((Object)CField.UIPacket.IntroEnableUI(1));
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo(3, 1));
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo(1, 30));
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionStatus(true));
                Timer.EventTimer.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo(3, 0));
                        c.getSession().writeAndFlush((Object)CField.showEffect("demonSlayer/text13"));
                        c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo(1, 500));
                    }
                }, 1000L);
                Timer.EventTimer.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        c.getSession().writeAndFlush((Object)CField.showEffect("demonSlayer/text14"));
                        c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo(1, 4000));
                    }
                }, 1500L);
                Timer.EventTimer.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        final MapleMap mapto = c.getChannelServer().getMapFactory().getMap(927000020);
                        c.getPlayer().changeMap(mapto, mapto.getPortal(0));
                        c.getSession().writeAndFlush((Object)CField.UIPacket.IntroEnableUI(0));
                        MapleQuest.getInstance(23204).forceStart(c.getPlayer(), 0, null);
                        MapleQuest.getInstance(23205).forceComplete(c.getPlayer(), 0);
                        final Map<Skill, SkillEntry> sa = new HashMap<Skill, SkillEntry>();
                        sa.put(SkillFactory.getSkill(30011170), new SkillEntry(1, (byte)1, -1L));
                        sa.put(SkillFactory.getSkill(30011169), new SkillEntry(1, (byte)1, -1L));
                        sa.put(SkillFactory.getSkill(30011168), new SkillEntry(1, (byte)1, -1L));
                        sa.put(SkillFactory.getSkill(30011167), new SkillEntry(1, (byte)1, -1L));
                        sa.put(SkillFactory.getSkill(30010166), new SkillEntry(1, (byte)1, -1L));
                        c.getPlayer().changeSkillsLevel(sa);
                    }
                }, 5500L);
                break;
            }
            case ds_tuto_1_before: {
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo(3, 1));
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo(1, 30));
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionStatus(true));
                Timer.EventTimer.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo(3, 0));
                        c.getSession().writeAndFlush((Object)CField.showEffect("demonSlayer/text8"));
                        c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo(1, 500));
                    }
                }, 1000L);
                Timer.EventTimer.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        c.getSession().writeAndFlush((Object)CField.showEffect("demonSlayer/text9"));
                        c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo(1, 3000));
                    }
                }, 1500L);
                Timer.EventTimer.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        final MapleMap mapto = c.getChannelServer().getMapFactory().getMap(927000010);
                        c.getPlayer().changeMap(mapto, mapto.getPortal(0));
                    }
                }, 4500L);
                break;
            }
            case ds_tuto_0_0: {
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionStatus(true));
                c.getSession().writeAndFlush((Object)CField.UIPacket.IntroEnableUI(1));
                c.getSession().writeAndFlush((Object)CField.UIPacket.IntroDisableUI(true));
                final Map<Skill, SkillEntry> sa = new HashMap<Skill, SkillEntry>();
                sa.put(SkillFactory.getSkill(30011109), new SkillEntry(1, (byte)1, -1L));
                sa.put(SkillFactory.getSkill(30010110), new SkillEntry(1, (byte)1, -1L));
                sa.put(SkillFactory.getSkill(30010111), new SkillEntry(1, (byte)1, -1L));
                sa.put(SkillFactory.getSkill(30010185), new SkillEntry(1, (byte)1, -1L));
                c.getPlayer().changeSkillsLevel(sa);
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo(3, 0));
                c.getSession().writeAndFlush((Object)CField.showEffect("demonSlayer/back"));
                c.getSession().writeAndFlush((Object)CField.showEffect("demonSlayer/text0"));
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo(1, 500));
                c.getPlayer().setDirection(0);
                if (!c.getPlayer().getMap().containsNPC(2159307)) {
                    c.getPlayer().getMap().spawnNpc(2159307, new Point(1305, 50));
                    break;
                }
                break;
            }
            case ds_tuto_2_prep: {
                if (!c.getPlayer().getMap().containsNPC(2159309)) {
                    c.getPlayer().getMap().spawnNpc(2159309, new Point(550, 50));
                    break;
                }
                break;
            }
            case goArcher: {
                showIntro(c, "Effect/Direction3.img/archer/Scene" + ((c.getPlayer().getGender() == 0) ? "0" : "1"));
                break;
            }
            case goPirate: {
                showIntro(c, "Effect/Direction3.img/pirate/Scene" + ((c.getPlayer().getGender() == 0) ? "0" : "1"));
                break;
            }
            case goRogue: {
                showIntro(c, "Effect/Direction3.img/rogue/Scene" + ((c.getPlayer().getGender() == 0) ? "0" : "1"));
                break;
            }
            case goMagician: {
                showIntro(c, "Effect/Direction3.img/magician/Scene" + ((c.getPlayer().getGender() == 0) ? "0" : "1"));
                break;
            }
            case goSwordman: {
                showIntro(c, "Effect/Direction3.img/swordman/Scene" + ((c.getPlayer().getGender() == 0) ? "0" : "1"));
                break;
            }
            case goLith: {
                showIntro(c, "Effect/Direction3.img/goLith/Scene" + ((c.getPlayer().getGender() == 0) ? "0" : "1"));
                break;
            }
            case TD_MC_Openning: {
                showIntro(c, "Effect/Direction2.img/open");
                break;
            }
            case TD_MC_gasi: {
                showIntro(c, "Effect/Direction2.img/gasi");
                break;
            }
            case aranDirection: {
                switch (c.getPlayer().getMapId()) {
                    case 914090010: {
                        data = "Effect/Direction1.img/aranTutorial/Scene0";
                        break;
                    }
                    case 914090011: {
                        data = "Effect/Direction1.img/aranTutorial/Scene1" + ((c.getPlayer().getGender() == 0) ? "0" : "1");
                        break;
                    }
                    case 914090012: {
                        data = "Effect/Direction1.img/aranTutorial/Scene2" + ((c.getPlayer().getGender() == 0) ? "0" : "1");
                        break;
                    }
                    case 914090013: {
                        data = "Effect/Direction1.img/aranTutorial/Scene3";
                        break;
                    }
                    case 914090100: {
                        data = "Effect/Direction1.img/aranTutorial/HandedPoleArm" + ((c.getPlayer().getGender() == 0) ? "0" : "1");
                        break;
                    }
                    case 914090200: {
                        data = "Effect/Direction1.img/aranTutorial/Maha";
                        break;
                    }
                }
                showIntro(c, data);
                break;
            }
            case iceCave: {
                final Map<Skill, SkillEntry> sa = new HashMap<Skill, SkillEntry>();
                sa.put(SkillFactory.getSkill(20000014), new SkillEntry(-1, (byte)0, -1L));
                sa.put(SkillFactory.getSkill(20000015), new SkillEntry(-1, (byte)0, -1L));
                sa.put(SkillFactory.getSkill(20000016), new SkillEntry(-1, (byte)0, -1L));
                sa.put(SkillFactory.getSkill(20000017), new SkillEntry(-1, (byte)0, -1L));
                sa.put(SkillFactory.getSkill(20000018), new SkillEntry(-1, (byte)0, -1L));
                c.getPlayer().changeSkillsLevel(sa);
                c.getSession().writeAndFlush((Object)CField.EffectPacket.showWZEffect("Effect/Direction1.img/aranTutorial/ClickLirin"));
                c.getSession().writeAndFlush((Object)CField.UIPacket.IntroDisableUI(false));
                c.getSession().writeAndFlush((Object)CField.UIPacket.IntroLock(false));
                c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                break;
            }
            case rienArrow: {
                if (c.getPlayer().getInfoQuest(21019).equals("miss=o;helper=clear")) {
                    c.getPlayer().updateInfoQuest(21019, "miss=o;arr=o;helper=clear");
                    c.getSession().writeAndFlush((Object)CField.EffectPacket.showWZEffect("Effect/OnUserEff.img/guideEffect/aranTutorial/tutorialArrow3"));
                    break;
                }
                break;
            }
            case rien: {
                if (c.getPlayer().getQuestStatus(21101) == 2 && c.getPlayer().getInfoQuest(21019).equals("miss=o;arr=o;helper=clear")) {
                    c.getPlayer().updateInfoQuest(21019, "miss=o;arr=o;ck=1;helper=clear");
                }
                c.getSession().writeAndFlush((Object)CField.UIPacket.IntroDisableUI(false));
                c.getSession().writeAndFlush((Object)CField.UIPacket.IntroLock(false));
                break;
            }
            case check_count: {
                if (c.getPlayer().getMapId() == 950101010 && (!c.getPlayer().haveItem(4001433, 20) || c.getPlayer().getLevel() < 50)) {
                    final MapleMap mapp = c.getChannelServer().getMapFactory().getMap(950101100);
                    c.getPlayer().changeMap(mapp, mapp.getPortal(0));
                    break;
                }
                break;
            }
            case Massacre_first: {
                break;
            }
            case Massacre_result: {
                c.getSession().writeAndFlush((Object)CField.showEffect("killing/fail"));
                break;
            }
            case enter_hungryMuto:
            case enter_hungryMutoEasy:
            case enter_hungryMutoHard: {
                c.getSession().writeAndFlush((Object)CField.environmentChange("event/start", 19));
                c.getSession().writeAndFlush((Object)CField.environmentChange("Dojang/clear", 5));
                c.getSession().writeAndFlush((Object)CField.environmentChange("Map/Effect3.img/hungryMutoMsg/msg1", 16));
                break;
            }
            case enter_450002024: {
                if (c.getPlayer().getMap().getNPCById(3003160) == null) {
                    break;
                }
                break;
            }
            case PTtutor000: {
                try {
                    c.getSession().writeAndFlush((Object)CField.UIPacket.playMovie("phantom_memory.avi", true));
                    c.getSession().writeAndFlush((Object)CField.showEffect("phantom/mapname1"));
                    c.getSession().writeAndFlush((Object)CField.UIPacket.IntroEnableUI(1));
                    c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo(3, 1));
                    c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionInfo("Effect/Direction6.img/effect/tuto/balloonMsg0/10", 0, 0, -110, 1, 0));
                    Thread.sleep(1300L);
                }
                catch (InterruptedException ex) {}
                c.getSession().writeAndFlush((Object)CField.UIPacket.getDirectionStatus(false));
                c.getSession().writeAndFlush((Object)CField.UIPacket.IntroEnableUI(0));
                c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                break;
            }
            case enter_993014200:
            case enter_993018200:
            case enter_993021200:
            case enter_993029200: {
                final int count = c.getPlayer().getLinkMobCount();
                c.getSession().writeAndFlush((Object)SLFCGPacket.FrozenLinkMobCount(count));
                for (final MapleMapObject monstermo : c.getPlayer().getMap().getAllMonstersThreadsafe()) {
                    final MapleMonster monster = (MapleMonster)monstermo;
                    if (monster.getOwner() == c.getPlayer().getId()) {
                        monster.setHp(0L);
                        c.getPlayer().getMap().broadcastMessage(MobPacket.killMonster(monster.getObjectId(), 1));
                        c.getPlayer().getMap().removeMapObject(monster);
                        monster.killed();
                    }
                }
                if (c.getPlayer().getV("linkMob") != null && count > 0) {
                    final int[] smobid = { 9010152, 9010153, 9010154, 9010155, 9010156, 9010157, 9010158, 9010159, 9010160, 9010161, 9010162, 9010163, 9010164, 9010165, 9010166, 9010167, 9010168, 9010169, 9010170, 9010171, 9010172, 9010173, 9010174, 9010175, 9010176, 9010177, 9010178, 9010179, 9010180, 9010181 };
                    final int[] smobx = { 1736, 1872, 1944, 2074, 2154, 2237, 2368, 2435, 2567, 2647, 2750 };
                    final int[] smoby = { 399, 132, -81 };
                    for (int i2 = 0; i2 < smobx.length; ++i2) {
                        for (int g = 0; g < smoby.length; ++g) {
                            final int id = smobid[Integer.parseInt(c.getPlayer().getV("linkMob"))];
                            final MapleMonster mob = MapleLifeFactory.getMonster(id);
                            mob.setOwner(c.getPlayer().getId());
                            c.getPlayer().getMap().spawnMonsterOnGroundBelow(mob, new Point(smobx[i2], smoby[g]));
                        }
                    }
                }
            }
            case enter_993001000: {
                if (c.getPlayer().getKeyValue(18838, "stage") == -1L) {
                    c.getPlayer().setKeyValue(18838, "count", "99");
                    c.getPlayer().setKeyValue(18838, "stageT", "0");
                    c.getPlayer().setKeyValue(18838, "hack", "0");
                    c.getPlayer().setKeyValue(18838, "stage", "0");
                    c.getPlayer().setKeyValue(18838, "mode", "0");
                }
                final List<PlatformerRecord> records = c.getPlayer().getPlatformerRecords();
                c.getPlayer().setKeyValue(18838, "count", c.getPlayer().getKeyValue(18838, "count") + "");
                c.getPlayer().setKeyValue(18838, "stage", records.size() + "");
                for (int a = 0; a < records.size(); ++a) {
                    final PlatformerRecord rec = records.get(a);
                    c.getPlayer().setKeyValue(18839 + a, "isClear", "1");
                    c.getPlayer().setKeyValue(18839 + a, "br", rec.getClearTime() + "");
                    c.getPlayer().setKeyValue(18839 + a, "cs", rec.getStars() + "");
                }
                break;
            }
            case enter_910143000: {
                MapleItemInformationProvider.getInstance().getItemEffect(2210217).applyTo(c.getPlayer(), true);
                break;
            }
            case miniGameVS_Start: {
                break;
            }
            case fireWolf_Enter: {
                if (c.getPlayer().getMap().getAllMonster().size() == 0) {
                    final MapleMapFactory mapFactory = ChannelServer.getInstance(c.getChannel()).getMapFactory();
                    final MapleMap map = mapFactory.getMap(993000500);
                    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(9101078), new Point(25, 353));
                }
                c.getPlayer().setFWolfDamage(0L);
                c.getPlayer().setFWolfKiller(false);
                c.getSession().writeAndFlush((Object)CField.startMapEffect("\ubd88\uaf43\ub291\ub300\ub97c \ucc98\uce58\ud560 \uc6a9\uc0ac\uac00 \ub298\uc5c8\uad70. \uc5b4\uc11c \ub140\uc11d\uc744 \uacf5\uaca9\ud574! \uba38\ubb34\ub97c \uc218 \uc788\ub294 \uc2dc\uac04\uc740 30\ucd08 \ubfd0\uc774\uc57c!", 5120159, true));
                c.getSession().writeAndFlush((Object)CField.getClock(30));
                Timer.MapTimer.getInstance().schedule(() -> {
                    if (c.getPlayer().getMapId() == 993000500) {
                        c.getPlayer().warp(993000600);
                    }
                    return;
                }, 30000L);
                break;
            }
            case enter_993192002: {
                c.removeClickedNPC();
                NPCScriptManager.getInstance().dispose(c);
                c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                NPCScriptManager.getInstance().start(c, 2007, "BloomingForest_0");
                break;
            }
            case enter_993192003: {
                c.removeClickedNPC();
                NPCScriptManager.getInstance().dispose(c);
                c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                NPCScriptManager.getInstance().start(c, 2007, "BloomingForest_1");
                break;
            }
            case enter_993192004: {
                c.removeClickedNPC();
                NPCScriptManager.getInstance().dispose(c);
                c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                NPCScriptManager.getInstance().start(c, 2007, "BloomingForest_RedFlower");
                break;
            }
            case enter_993192005: {
                c.removeClickedNPC();
                NPCScriptManager.getInstance().dispose(c);
                c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                NPCScriptManager.getInstance().start(c, 2007, "BloomingForest_BlueFlower");
                break;
            }
            case enter_993192006: {
                c.removeClickedNPC();
                NPCScriptManager.getInstance().dispose(c);
                c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                NPCScriptManager.getInstance().start(c, 2007, "BloomingForest_YellowFlower");
                break;
            }
            case enter_993192007: {
                c.removeClickedNPC();
                NPCScriptManager.getInstance().dispose(c);
                c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                NPCScriptManager.getInstance().start(c, 2007, "BloomingForest_2");
                break;
            }
            case BloomingRace_reset: {
                c.send(SLFCGPacket.ContentsWaiting(c.getPlayer(), 0, 11, 5, 1, 25));
                if (c.getPlayer().getMap().getCustomTime(c.getPlayer().getMap().getId()) != null) {
                    c.send(CField.getClock(c.getPlayer().getMap().getCustomTime(c.getPlayer().getMap().getId()) / 1000));
                    break;
                }
                break;
            }
            case enter_993194001: {
                c.removeClickedNPC();
                NPCScriptManager.getInstance().dispose(c);
                c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                NPCScriptManager.getInstance().start(c, 2007, "MapleLive_0");
                break;
            }
            case enter_993194002: {
                c.removeClickedNPC();
                NPCScriptManager.getInstance().dispose(c);
                c.getSession().writeAndFlush((Object)CWvsContext.enableActions(c.getPlayer()));
                NPCScriptManager.getInstance().start(c, 2007, "MapleLive_1");
                break;
            }
            default: {
                FileoutputUtil.log("Log_Script_Except.rtf", "Unhandled script : " + scriptName + ", type : onUserEnter - MAPID " + c.getPlayer().getMapId());
                break;
            }
        }
        switch (scriptName) {
            case "180onUser": {
                if (!c.getPlayer().isGM()) {
                    c.getPlayer().warp(100000000);
                    c.getPlayer().ban("\uc6b4\uc601\uc790\ub9f5 \uce68\uc785", true, true, true);
                    c.disconnect(true, false);
                    break;
                }
                break;
            }
        }
    }
  
  private static void showIntro(MapleClient c, String data) {
    c.getSession().writeAndFlush(CField.EffectPacket.showWZEffect(data));
  }
  
  private static void sendDojoClock(MapleClient c) {
    c.getSession().writeAndFlush(CField.getDojoClock(900, (int)((System.currentTimeMillis() - c.getPlayer().getDojoStartTime() - c.getPlayer().getDojoCoolTime()) / 1000L)));
  }
  
  private static void sendDojoStart(MapleClient c, int stage) {
    c.getSession().writeAndFlush(CField.environmentChange("Dojang/start", 5));
    c.getSession().writeAndFlush(CField.environmentChange("dojang/start/stage", 19));
    c.getSession().writeAndFlush(CField.environmentChange("dojang/start/number/" + stage, 19));
    c.getSession().writeAndFlush(CField.getDojoClockStop(false, 900));
    c.getSession().writeAndFlush(CField.trembleEffect(0, 1));
  }
  
  private static void handlePinkBeanStart(MapleClient c) {
    MapleMap map = c.getPlayer().getMap();
    if (!map.containsNPC(2141000))
      map.spawnNpc(2141000, new Point(-190, -42)); 
  }
  
  private static void reloadWitchTower(MapleClient c) {
    int mob;
    MapleMap map = c.getPlayer().getMap();
    map.killAllMonsters(false);
    int level = c.getPlayer().getLevel();
    if (level <= 10) {
      mob = 9300367;
    } else if (level <= 20) {
      mob = 9300368;
    } else if (level <= 30) {
      mob = 9300369;
    } else if (level <= 40) {
      mob = 9300370;
    } else if (level <= 50) {
      mob = 9300371;
    } else if (level <= 60) {
      mob = 9300372;
    } else if (level <= 70) {
      mob = 9300373;
    } else if (level <= 80) {
      mob = 9300374;
    } else if (level <= 90) {
      mob = 9300375;
    } else if (level <= 100) {
      mob = 9300376;
    } else {
      mob = 9300377;
    } 
    MapleMonster theMob = MapleLifeFactory.getMonster(mob);
    OverrideMonsterStats oms = new OverrideMonsterStats();
    oms.setOMp(theMob.getMobMaxMp());
    oms.setOExp(theMob.getMobExp());
    oms.setOHp((long)Math.ceil(theMob.getMobMaxHp() * level / 5.0D));
    theMob.setOverrideStats(oms);
    map.spawnMonsterOnGroundBelow(theMob, witchTowerPos);
  }
  
  public static void startDirectionInfo(MapleCharacter chr, boolean start) {
    final MapleClient c = chr.getClient();
    MapleNodes.DirectionInfo di = chr.getMap().getDirectionInfo(start ? 0 : chr.getDirection());
    if (di != null && di.eventQ.size() > 0) {
      if (start) {
        c.getSession().writeAndFlush(CField.UIPacket.IntroDisableUI(true));
        c.getSession().writeAndFlush(CField.UIPacket.getDirectionInfo(3, 4));
      } else {
        for (String s : di.eventQ) {
          MapleMap mapto;
          switch (directionInfo.fromString(s)) {
            case merTutorDrecotion01:
              c.getSession().writeAndFlush(CField.UIPacket.getDirectionInfo("Effect/Direction5.img/effect/mercedesInIce/merBalloon/0", 2000, 0, -100, 1, 0));
            case merTutorDrecotion02:
              c.getSession().writeAndFlush(CField.UIPacket.getDirectionInfo("Effect/Direction5.img/effect/mercedesInIce/merBalloon/1", 2000, 0, -100, 1, 0));
            case merTutorDrecotion03:
              c.getSession().writeAndFlush(CField.UIPacket.getDirectionInfo(3, 2));
              c.getSession().writeAndFlush(CField.UIPacket.getDirectionStatus(true));
              c.getSession().writeAndFlush(CField.UIPacket.getDirectionInfo("Effect/Direction5.img/effect/mercedesInIce/merBalloon/2", 2000, 0, -100, 1, 0));
            case merTutorDrecotion04:
              c.getSession().writeAndFlush(CField.UIPacket.getDirectionInfo(3, 2));
              c.getSession().writeAndFlush(CField.UIPacket.getDirectionStatus(true));
              c.getSession().writeAndFlush(CField.UIPacket.getDirectionInfo("Effect/Direction5.img/effect/mercedesInIce/merBalloon/3", 2000, 0, -100, 1, 0));
            case merTutorDrecotion05:
              c.getSession().writeAndFlush(CField.UIPacket.getDirectionInfo(3, 2));
              c.getSession().writeAndFlush(CField.UIPacket.getDirectionStatus(true));
              c.getSession().writeAndFlush(CField.UIPacket.getDirectionInfo("Effect/Direction5.img/effect/mercedesInIce/merBalloon/4", 2000, 0, -100, 1, 0));
              Timer.EventTimer.getInstance().schedule(new Runnable() {
                    public void run() {
                      c.getSession().writeAndFlush(CField.UIPacket.getDirectionInfo(3, 2));
                      c.getSession().writeAndFlush(CField.UIPacket.getDirectionStatus(true));
                      c.getSession().writeAndFlush(CField.UIPacket.getDirectionInfo("Effect/Direction5.img/effect/mercedesInIce/merBalloon/5", 2000, 0, -100, 1, 0));
                    }
                  }, 2000L);
              Timer.EventTimer.getInstance().schedule(new Runnable() {
                    public void run() {
                      c.getSession().writeAndFlush(CField.UIPacket.IntroEnableUI(0));
                      c.getSession().writeAndFlush(CWvsContext.enableActions(c.getPlayer()));
                    }
                  },  4000L);
            case merTutorDrecotion12:
              c.getSession().writeAndFlush(CField.UIPacket.getDirectionInfo(3, 2));
              c.getSession().writeAndFlush(CField.UIPacket.getDirectionStatus(true));
              c.getSession().writeAndFlush(CField.UIPacket.getDirectionInfo("Effect/Direction5.img/effect/mercedesInIce/merBalloon/8", 2000, 0, -100, 1, 0));
              c.getSession().writeAndFlush(CField.UIPacket.IntroEnableUI(0));
            case merTutorDrecotion21:
              c.getSession().writeAndFlush(CField.UIPacket.getDirectionInfo(3, 1));
              c.getSession().writeAndFlush(CField.UIPacket.getDirectionStatus(true));
              mapto = c.getChannelServer().getMapFactory().getMap(910150005);
              c.getPlayer().changeMap(mapto, mapto.getPortal(0));
            case ds_tuto_0_2:
              c.getSession().writeAndFlush(CField.showEffect("demonSlayer/text1"));
            case ds_tuto_0_1:
              c.getSession().writeAndFlush(CField.UIPacket.getDirectionInfo(3, 2));
            case ds_tuto_0_3:
              c.getSession().writeAndFlush(CField.showEffect("demonSlayer/text2"));
              Timer.EventTimer.getInstance().schedule(new Runnable() {
                    public void run() {
                      c.getSession().writeAndFlush(CField.UIPacket.getDirectionInfo(1, 4000));
                      c.getSession().writeAndFlush(CField.showEffect("demonSlayer/text3"));
                    }
                  }, 2000L);
              Timer.EventTimer.getInstance().schedule(new Runnable() {
                    public void run() {
                      c.getSession().writeAndFlush(CField.UIPacket.getDirectionInfo(1, 500));
                      c.getSession().writeAndFlush(CField.showEffect("demonSlayer/text4"));
                    }
                  }, 6000L);
              Timer.EventTimer.getInstance().schedule(new Runnable() {
                    public void run() {
                      c.getSession().writeAndFlush(CField.UIPacket.getDirectionInfo(1, 4000));
                      c.getSession().writeAndFlush(CField.showEffect("demonSlayer/text5"));
                    }
                  }, 6500L);
              Timer.EventTimer.getInstance().schedule(new Runnable() {
                    public void run() {
                      c.getSession().writeAndFlush(CField.UIPacket.getDirectionInfo(1, 500));
                      c.getSession().writeAndFlush(CField.showEffect("demonSlayer/text6"));
                    }
                  }, 10500L);
              Timer.EventTimer.getInstance().schedule(new Runnable() {
                    public void run() {
                      c.getSession().writeAndFlush(CField.UIPacket.getDirectionInfo(1, 4000));
                      c.getSession().writeAndFlush(CField.showEffect("demonSlayer/text7"));
                    }
                  }, 11000L);
              Timer.EventTimer.getInstance().schedule(new Runnable() {
                    public void run() {
                      c.getSession().writeAndFlush(CField.UIPacket.getDirectionInfo(4, 2159307));
                      NPCScriptManager.getInstance().dispose(c);
                      NPCScriptManager.getInstance().start(c, 2159307);
                    }
                  }, 15000L);
          } 
        } 
      } 
      c.getSession().writeAndFlush(CField.UIPacket.getDirectionInfo(1, 2000));
      chr.setDirection(chr.getDirection() + 1);
      if (chr.getMap().getDirectionInfo(chr.getDirection()) == null)
        chr.setDirection(-1); 
    } else if (start) {
      MapleMap mapto;
      switch (chr.getMapId()) {
        case 931050300:
          while (chr.getLevel() < 10)
            chr.levelUp(); 
          mapto = c.getChannelServer().getMapFactory().getMap(931050000);
          chr.changeMap(mapto, mapto.getPortal(0));
          break;
      } 
    } 
  }
}
