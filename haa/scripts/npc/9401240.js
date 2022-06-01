/*
?젣?옉?옄 : qudtlstorl79@nate.com
*/

importPackage(java.lang);
importPackage(Packages.constants);
importPackage(Packages.handling.channel.handler);
importPackage(Packages.tools.packet);
importPackage(Packages.handling.world);
importPackage(java.lang);
importPackage(Packages.constants);
importPackage(Packages.server.items);
importPackage(Packages.client.items);
importPackage(java.lang);
importPackage(Packages.launch.world);
importPackage(Packages.tools.packet);
importPackage(Packages.constants);
importPackage(Packages.client.inventory);
importPackage(Packages.server.enchant);
importPackage(java.sql);
importPackage(Packages.database);
importPackage(Packages.handling.world);
importPackage(Packages.constants);
importPackage(java.util);
importPackage(java.io);
importPackage(Packages.client.inventory);
importPackage(Packages.client);
importPackage(Packages.server);
importPackage(Packages.tools.packet);

집 = "#fUI/Basic.img/theblackcoin/10#";
코인 = "#fUI/Basic.img/theblackcoin/26#";
모루 = "#fUI/Basic.img/theblackcoin/30#";
훈장 = "#fUI/Basic.img/theblackcoin/31#";
의자 = "#fUI/Basic.img/theblackcoin/22#";
뎀스 = "#fUI/Basic.img/theblackcoin/23#";
라이딩 = "#fUI/Basic.img/theblackcoin/32#";
강화 = "#fUI/Basic.img/theblackcoin/34#";
커플 = "#fUI/Basic.img/theblackcoin/35#";
결혼 = "#fUI/Basic.img/theblackcoin/36#";
퀘스트 = "#fUI/Basic.img/theblackcoin/37#";
무릉 = "#fUI/Basic.img/theblackcoin/39#";
블랙스킬 = "#fUI/Basic.img/theblackcoin/40#";
검정 = "#fc0xFF191919#"
노랑 = "#fc0xFFE0B94F#"
보라 = "#fc0xFFB677FF#"
보라색 = "#fc0xFF8041D9#"
블루 = "#fc0xFF4641D9#"
회색 = "#fc0xFF4C4C4C#"
연파랑 = "#fc0xFF6799FF#"
동글보라 = "#fMap/MapHelper.img/weather/starPlanet/8#";
네오젬 = "#fUI/UIWindow4.img/pointShop/100712/iconShop#";
룰렛 = "#fUI/UIWindow4.img/pointShop/17015/iconShop#";
포켓 = "#fUI/Basic.img/theblackcoin/17#";
뛰어라 = "#fUI/Basic.img/theblackcoin/33#";
펫 = "#fUI/CashShop.img/CashItem_label/9#";
마라벨 = "#fUI/CashShop.img/CashItem_label/3#";
컨텐츠 = "#fUI/Basic.img/theblack/4#";

var sssss = 0;
var suc = 0;
var sel = 0;
var sell = 0;
var selching = 0;
var sel2 = 0;
var status = -1;
var succ = false;
var minusitemid = 0;
var rewarditemid = 0;
var etc = 0;
var etc1 = 0;
var sale = 0;
var sale1 = 0;
var ssssitem = 0;
var ssssitemc = 0;
var ssssitem2 = 0;
var ssssitemc2 = 0;
var ssssmeso = 0;
function start() {
    action(1, 0, 0);
}

/*
            cm.dispose();
            InterServerHandler.EnterCS(cm.getPlayer().getClient(),cm.getPlayer(), false); 罹먯떆?꺏
*/

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        if (status == 2 && sel == 4) {//승급 거부
            cm.sendOkS("#fs11#그래! 마음이 바뀌면 다시 돌아오게나!", 0x04, 9401232);
        }
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {

        if (!cm.getClient().getCreated().getMonth() + 1 < 7) {// 7월이전에 생성한경우
            for (var i = 50000; i < 50009; i++) {
		if (cm.getClient().getQuestStatus(i) != 2) {
			cm.forceCompleteQuest(i, true);
		}
	}
	for (var i = 60000; i < 60015; i++) {
		if (cm.getClient().getQuestStatus(i) != 2) {
			cm.forceCompleteQuest(i, true);
		}
	}
        }
        var choose = "#fn나눔고딕#Happy ! 컨텐츠 관련 NPC입니다.#fs11#\r\n";
        choose += "#fn나눔고딕##fs11##L0#" + 의자 + " #fc0xFFF15F5F#의자 뽑기#l#k　";
        choose += "#L1#" + 뎀스 + " #fc0xFF6799FF#뎀스 뽑기#l　";
        choose += "#L2#" + 라이딩 + " #fc0xFFA566FF#탈것 뽑기#l　\r\n";
        choose += "#L6#" + 커플 + " " + 보라 + "커플 제작　#l";
        choose += "#L15#" + 마라벨 + " #fc0xFF5954ED#캐시 모루#l　";
        choose += "#L16#" + 룰렛 + " #fc0xFFFFA648#룰렛 뽑기#l　\r\n\r\n";
        choose += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
        choose += "#L11#" + 무릉 + " " + 블루 + "무릉 도장#k 을 이용하겠습니다.\r\n";
        choose += "#L12#" + 블랙스킬 + " #fc0xFFFF5E00#미니게임 원카드#k 를 이용하겠습니다.\r\n";
        choose += "#L7#" + 결혼 + " #fc0xFFF361DC#도박장#k 으로 이동하고싶습니다.\r\n#l";
        choose += "#L8#" + 강화 + " #fc0xFF998A00#어드벤처 드릴#k 을 이용하고 싶습니다.\r\n#l";
        cm.sendOkS(choose, 0x04);

    } else if (status == 1) {
        sel = selection;
        if (selection == 0) {
            cm.dispose();
            cm.openNpc(1540105);
        } else if (selection == 1) {
            cm.dispose();
            cm.openNpc(1540106);
        } else if (selection == 2) {
            cm.dispose();
            cm.openNpc(1540109);
        } else if (selection == 4) {
            말 = "#fs11#" + 검정 + "어서오게! #b페스티벌#k" + 검정 + "은 재밌게 즐기고 있나? 더 재밌게 즐기기 위한 #r승급제도#k" + 검정 + "가 있다네. 자네도 한번 해볼텐가?\r\n";
            말 += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
            말 += "#L3#" + 검정 + "승급 제도가 무엇인지 궁금해요.#k#l\r\n\r\n"
            if (cm.haveItem(3700900, 1)) {
                말 += "#L0#" + 블루 + "#z3700901##n#k" + 검정 + "로 승급하고 싶습니다.#k#l\r\n"
            }
            if (cm.haveItem(3700901, 1)) {
                말 += "#L1#" + 블루 + "#z3700902##n#k" + 검정 + "로 승급하고 싶습니다.#k#l\r\n"
            }
            cm.sendSimpleS(말, 0x04, 9401232);
        } else if (selection == 5) {
            var choose = "#fs11#" + 검정 + "초월자의 힘에 다가가 볼텐가?\r\n";
            choose += "#L5#" + 노랑 + "여명의 뱃지#n#k" + 검정 + "를 제작하고 싶습니다.#k#l\r\n"
            choose += "#L6#" + 블루 + "황혼의 지배자#n#k" + 검정 + "를 제작하고 싶습니다.#k#l\r\n"
            choose += "#L9#" + 연파랑 + "여명의 샘물#n#k" + 검정 + "을 정제하고 싶습니다.#k#l\r\n"
            choose += "#L7#" + 보라 + "차원의 스톤#n#k" + 검정 + "을 제작/분해하고 싶습니다.#k#l\r\n\r\n"
            choose += "#fc0xFFD5D5D5#───────────────────────────#k#l\r\n";
            choose += "#L10##r여명의 뱃지를 업그레이드 하고 싶습니다.#k#l\r\n"
            choose += "#L11##r여명의 뱃지를 차원의 스톤으로 업그레이드 하고 싶습니다.#k#l\r\n\r\n"
            choose += "#fc0xFFD5D5D5#───────────────────────────#k#l\r\n";
            choose += "#L12##d여명 시스템과 차원의 스톤은 무엇인가요?#k#l\r\n"
            choose += "#L13##d황혼 시스템은 무엇인가요?#k#l\r\n"
            cm.sendSimpleS(choose, 0x04, 9401232);
        } else if (selection == 6) {
            cm.openNpc(9201000);
            cm.dispose();
            //cm.openNpc(9201000);
        } else if (selection == 7) {
            cm.dispose();
            cm.warp(193000000);
        } else if (selection == 8) {
            cm.dispose();
cm.openNpc(9062147);
        } else if (selection == 9) {
            var choose = "#fs11#" + 검정 + "자네를 위해 다양한 퀘스트가 준비되어 있다네!\r\n";
            choose += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
            choose += "#L20##fc0xFF8041D9#일일 퀘스트#k" + 검정 + "를 확인하고 싶습니다.#k#l\r\n"
            choose += "#L21##fc0xFF4374D9#주간 퀘스트#k" + 검정 + "를 확인하고 싶습니다.#k#l\r\n"
            choose += "#L22##fc0xFF47C83E#점프맵 일일 퀘스트#k" + 검정 + "를 확인하고 싶습니다.#k#l\r\n"
            choose += "#L23##fc0xFFD3C64A#파티 퀘스트#k" + 검정 + "를 확인하고 싶습니다.#k#l\r\n"
            cm.sendSimpleS(choose, 0x04, 9401232);
        } else if (selection == 10) { // 나만의 집
            cm.sendOkS("현재 준비중인 시스템입니다.", 2);
            cm.dispose();
            //cm.warp(871000000);
        } else if (selection == 11) { // 무릉
            cm.dispose();
            cm.warp(925020000, 0);
        } else if (selection == 12) {
            cm.dispose();
cm.openNpc(9062296);
        } else if (selection == 13) {
            cm.dispose();
            cm.openNpc(3001860);
        } else if (selection == 14) {
            cm.dispose();
            cm.openNpc(3001861);
        } else if (selection == 15) {
            cm.dispose();
            cm.openNpc(9401232, "CashMoru");
        } else if (selection == 16) {
            cm.dispose();
            cm.openNpc(9401232, "FestivalGatcha");
        } else if (selection == 17) {
            cm.dispose();
            cm.openNpc(9401232, "ClonAvater");
        }
    } else if (status == 2) {
        sell = selection;
        if (sel == 4) {// 칭호 승급
            if (selection == 3) {
                cm.dispose();
                cm.openNpc(2004);
            } else {
                말 = "#fs11#칭호 승급을 하기 위해선 아래의 조건이 필요하다네.\r\n"
                말 += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
                selching = selection;
                if (selching == 0) {
                    ssssitem = 4310012;
                    ssssitemc = 3000;
                    ssssitem2 = 4310010;
                    ssssitemc2 = 200;
                    ssssitemmeso = 500000000
                    sucs = 70
                    
                } else {
                    ssssitem = 4310012;
                    ssssitemc = 9999;
                    ssssitem2 = 4310010;
                    ssssitemc2 = 350;
                    ssssitemmeso = 1500000000
                    sucs = 50
                }
                말 += "#i" + ssssitem + "# #b#z" + ssssitem + "##k #r" + ssssitemc + "개#k\r\n"
                말 += "#i" + ssssitem2 + "# #b#z" + ssssitem2 + "##k #r" + ssssitemc2 + "개#k\r\n"
                말 += "#i5200002# #r" + ssssitemmeso + "#k #b메소#k\r\n\r\n"

                말 += "정말 #e승급#n을 진행하겠나? 승급 확률은 #e#r"+sucs+"%#k#n 이라네!"
                cm.sendYesNoS(말, 0x04, 9401232);
            }
        } else if (sel == 5) {
            if (selection == 5) {
                말 = "#fs11##i1182000# #b#z1182000##k 아이템을 제작하겠나?"
                말 += "\r\n제작을 하려면 아래의 재료가 필요하지.\r\n"
                말 += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
                말 += "#i4310010# #b#z4310010##k #r50개#k\r\n"
                말 += "#i4310310# #b#z4310310##k #r350개#k\r\n"
                말 += "#i5200002# #r700,000,000#k #b메소#k\r\n\r\n"
                말 += "제작 확률은 #b50%#k 입니다. 정말로 제작하시겠습니까?"
                cm.sendYesNoS(말, 0x04, 9401232);
            } else if (selection == 6) {
                말 = "#fs11#" + 검정 + "제작하고 싶은 아이템을 선택해보게.\r\n";
                말 += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
                말 += "#L1#" + 블루 + "황혼의 지배자 (청)#n#k" + 검정 + "을 제작하고 싶습니다.#k#l\r\n"
                말 += "#L2##fc0xFF751400#황혼의 지배자 (흑)#n#k" + 검정 + "을 제작하고 싶습니다.#k#l\r\n"
                말 += "#L3##fc0xFF751400#황혼의 지배자 (흑)#n#k" + 검정 + "을 잔해로 제작하고 싶습니다.#k#l\r\n"
                cm.sendSimpleS(말, 0x04, 9401232);
            } else if (selection == 7) {
                말 = "#fs11#" + 검정 + "어떤 시스템을 이용할지 선택해보게나."
                말 += "\r\n"
                말 += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
                말 += "#L7#" + 보라 + "차원의 스톤#n#k" + 검정 + "" + 검정 + "을 #fc0xFF6B66FF#제작#k" + 검정 + "하고 싶습니다.#k#l\r\n"
                말 += "#L8#" + 보라 + "차원의 스톤#n#k" + 검정 + "" + 검정 + "을 #fc0xFFF15F5F#분해#k" + 검정 + "하고 싶습니다.#k#l\r\n"
                cm.sendSimpleS(말, 0x04, 9401232);

            } else if (selection == 9) {
                cm.dispose();
                cm.openNpc(2074147);
            } else if (selection == 10) {
                cm.dispose();
                cm.openNpc(1033100);
            } else if (selection == 11) {
                cm.dispose();
                cm.openNpc(1002000);
            } else if (selection == 12) {
                cm.dispose();
                cm.openNpc(2007);
            } else if (selection == 13) {
                cm.dispose();
                cm.openNpc(3001013);
            }
        } else if (sel == 8) {
            if (selection == 14) {
                cm.dispose();
                cm.openNpc(1540211);
            }
        } else if (sel == 9) {
            if (selection == 22) {
                if (cm.getClient().getKeyValue("jump_1") == null) {
                    cm.getClient().setKeyValue("jump_1", "0");
                }
                if (cm.getClient().getKeyValue("jump_2") == null) {
                    cm.getClient().setKeyValue("jump_2", "0");
                }
                if (cm.getClient().getKeyValue("jump_3") == null) {
                    cm.getClient().setKeyValue("jump_3", "0");
                }
                if (cm.getClient().getKeyValue("jump_4") == null) {
                    cm.getClient().setKeyValue("jump_4", "0");
                }
                if (cm.getClient().getKeyValue("jump_5") == null) {
                    cm.getClient().setKeyValue("jump_5", "0");
                }
                if (cm.getClient().getKeyValue("jump_6") == null) {
                    cm.getClient().setKeyValue("jump_6", "0");
                }
                말 = "#fs11#" + 검정 + "페스티벌에서 다양한 점프맵도 준비해봤다네!\r\n"
                말 += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
                말 += "#L100# " + 검정 + "고지를 향해서 ( #b" + parseInt(cm.getClient().getKeyValue("jump_1")) + "#k" + 검정 + " / #r1#k " + 검정 + ")#k#l\r\n\r\n";
                말 += "         " + 뛰어라 + " #fc0xFFFFBB00##z4310011# #fc0xFFF15F5F#5개\r\n";
                말 += "#L101# " + 검정 + "헤네시스 - 펫산책로 ( #b" + parseInt(cm.getClient().getKeyValue("jump_2")) + "#k" + 검정 + " / #r1#k " + 검정 + ")#k#l\r\n\r\n";
                말 += "         " + 뛰어라 + " #fc0xFFFFBB00##z4310011# #fc0xFFF15F5F#2개\r\n";
                말 += "#L102# " + 검정 + "루디브리엄 - 루디 펫 산책로 ( #b" + parseInt(cm.getClient().getKeyValue("jump_3")) + "#k" + 검정 + " / #r1#k " + 검정 + ")#k#l\r\n\r\n";
                말 += "         " + 뛰어라 + " #fc0xFFFFBB00##z4310011# #fc0xFFF15F5F#2개\r\n";
                말 += "#L103# " + 검정 + "아도비스의임무2 - 화산의숨결 ( #b" + parseInt(cm.getClient().getKeyValue("jump_4")) + "#k" + 검정 + " / #r1#k " + 검정 + ")#k#l\r\n\r\n";
                말 += "         " + 뛰어라 + " #fc0xFFFFBB00##z4310011# #fc0xFFF15F5F#8개\r\n";
                말 += "#L104# " + 검정 + "인내의 숲 ( #b" + parseInt(cm.getClient().getKeyValue("jump_5")) + "#k" + 검정 + " / #r1#k " + 검정 + ")#k#l\r\n\r\n";
                말 += "         " + 뛰어라 + " #fc0xFFFFBB00##z4310011# #fc0xFFF15F5F#10개\r\n";
                말 += "#L105# " + 검정 + "끈기의 숲 ( #b" + parseInt(cm.getClient().getKeyValue("jump_6")) + "#k" + 검정 + " / #r1#k " + 검정 + ")#k#l\r\n\r\n";
                말 += "         " + 뛰어라 + " #fc0xFFFFBB00##z4310011# #fc0xFFF15F5F#13개\r\n";
                cm.sendSimpleS(말, 0x04, 9401232);
            }
        } else if (sel == 12) {
            if (selection == 30) {
                cm.dispose();
                cm.openNpc(9401232, "PinkBeanReword");
            } else if (selection == 31) {
                cm.dispose();
                cm.openNpc(9401232, "YetiReword");
            }
        }
    } else if (status == 3) {
        if (selection == 100) {
            cm.dispose();
            cm.warp(109040000);
        } else if (selection == 101) {
            cm.dispose();
            cm.warp(100000202, 1);
            //cm.getPlayer().getClient().send(SLFCGPacket.CharReLocationPacket(5, 34));
        } else if (selection == 102) {
            cm.dispose();
            cm.warp(220000006, 1);
        } else if (selection == 103) {
            cm.dispose();
            cm.warp(280020000, 0);
        } else if (selection == 104) {
            cm.dispose();
            cm.warp(910130000, 0);
        } else if (selection == 105) {
            cm.dispose();
            cm.warp(910530000, 0);
        } else if (sel == 4) {
            leftslot = cm.getPlayer().getInventory(MapleInventoryType.SETUP).getNumFreeSlot();
            if (leftslot < 2) {
                cm.sendOkS("#fs11##r기타칸 2 칸 이상을 확보하고 다시 말을 걸어주게.", 0x04, 9401232);
                cm.dispose();
                return;
            }
            if (cm.getPlayer().getLevel() < 230) {
                cm.sendOkS("#fs11##r레벨 230 이상이 되어야 승급이 가능하다네.", 0x4, 9401232);
                cm.dispose();
                return;
            }
            if (cm.getPlayer().getMeso() < ssssitemmeso || !cm.haveItem(ssssitem2, ssssitemc2) || !cm.haveItem(ssssitem, ssssitemc)) {   //재료체크
                cm.sendOkS("#fs11##r승급에 필요한 재료가 부족한거 같네.", 0x04, 9401232);
                cm.dispose();
                return;
            }
            cm.getClient().send(CField.UIPacket.getDirectionStatus(true));
            cm.getClient().send(SLFCGPacket.SetIngameDirectionMode(true, false, false, false));
            statusplus(2000);
        } else if (sel == 5) {// 뱃지제작
            if (sell == 5) {
                if (cm.getPlayer().getMeso() < 700000000 || !cm.haveItem(4310010, 50)) {   //재료체크
                    cm.sendOkS("#fs11##b#z1182000##k를 제작하기 위한 재료 아이템이 부족한거 같군.", 0x04, 9401232);
                    cm.dispose();
                    return;
                }
                if (cm.getPlayer().getKeyValue(100794, "point") < 350) {
                    cm.sendOkS("#b#i4310310##z4310310##k이 부족한거 같네.", 0x04, 9401232);
                    cm.dispose();
                    return;
                }
                if (Math.floor(Math.random() * 100) <= 50) {// 성공했을때
                    cm.gainMeso(-700000000);
                    cm.gainItem(4310010, -50);
                    cm.getPlayer().AddBloomingCoin(-350, null);
                    cm.gainItem(1182000, 1);
                    말 = "#fs11#축하하네! 초월자의 힘에 다가가기 위한 첫걸음에 성공했네.\r\n\r\n"
                    말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"
                    말 += "#i1182000##b#z1182000##k"
                    cm.sendOkS(말, 0x04, 9401232);
                    cm.dispose();
                } else { // 실패했을때
                    cm.gainMeso(-700000000);
                    cm.gainItem(4310010, -50);
                    cm.getPlayer().AddBloomingCoin(-350, null);
                    cm.sendOkS("#fs11##r아쉽게도 제작에 실패했다네..", 0x04, 9401232);
                    cm.dispose();
                    return;
                }
            } else if (sell == 6) {
                sel2 = selection;
                if (selection == 1) {
                    suc = 5;
                    말 = "#fs11##i1162000# #b#z1162000##k 아이템을 제작하겠나?"
                    말 += "\r\n제작을 하려면 아래의 재료가 필요하지.\r\n"
                    if (cm.haveItem(4310009, 1)) {   //확률업 체크
                        말 += "자네는 " + 블루 + "#z4310009##k을 보유하고 있군!\r\n";
                        suc += 15;
                    }
                    말 += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
                    말 += "#i4310007# #b#z4310007##k #r30개#k\r\n"
                    말 += "#i4310310# #b#z4310310##k #r500개#k\r\n"
                    말 += "제작 확률은 #b" + suc + "%#k 입니다. 정말로 제작하시겠습니까?"
                    cm.sendYesNoS(말, 0x04, 9401232);
                } else if (selection == 2) {
                    suc = 30;
                    말 = "#fs11##i1162002# #b#z1162002##k 아이템을 제작하겠나?"
                    말 += "\r\n제작을 하려면 아래의 재료가 필요하지.\r\n"
                    말 += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
                    말 += "#i1162001# #b#z1162001##k #r2개#k\r\n\r\n\r\n"
                    말 += "#fc0xFF9E9E9E#제작 실패시,\r\n#z4310008#가 지급됩니다.#k\r\n"
                    말 += "제작 확률은 #b" + suc + "%#k 입니다. 정말로 제작하시겠습니까?"
                    cm.sendYesNoS(말, 0x04, 9401232);
                } else if (selection == 2) {
                    suc = 30;
                    말 = "#fs11##i1162002# #b#z1162002##k 아이템을 제작하겠나?"
                    말 += "\r\n제작을 하려면 아래의 재료가 필요하지.\r\n"
                    말 += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
                    말 += "#i1162001# #b#z1162001##k #r2개#k\r\n\r\n\r\n"
                    말 += "#fc0xFF9E9E9E#제작 실패시,\r\n#z4310008#가 지급됩니다.#k\r\n"
                    말 += "제작 확률은 #b" + suc + "%#k 입니다. 정말로 제작하시겠습니까?"
                    cm.sendYesNoS(말, 0x04, 9401232);
                } else if (selection == 3) {
                    if (!cm.haveItem(4310008, 5)) {
                        cm.sendOkS("#i4310008# #r#z4310008# 아이템이 부족하거나 없는거 같네..", 0x04, 9401232);
                        cm.dispose();
                        return;
                    }
                    var item = MapleItemInformationProvider.getInstance().getEquipById(1162002);
                    item.setState(4);
                    MapleInventoryManipulator.addbyItem(cm.getClient(), item);
                    World.Broadcast.broadcastMessage(CWvsContext.serverMessage(11, cm.getPlayer().getClient().getChannel(), "", cm.getPlayer().getName() + "님이 황혼 제작 시스템에서 {} 아이템을 획득하셨습니다!", true, item));
                    말 = "#fs11#축하하네! 초월자의 힘에 다가가기 위한 첫걸음에 성공했네.\r\n\r\n"
                    말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"
                    말 += "#i" + 1162002 + "# #b#z" + 1162002 + "##k"
                    cm.gainItem(4310008, -5);
                    cm.sendOkS(말, 0x04, 9401232);
                    cm.dispose();
                }
            } else if (sell == 7) {
                if (selection == 7) {
                    cm.dispose();
                    cm.openNpc(3003153);
                } else {
                    cm.dispose();
                    cm.openNpc(2074146);
                }
            }
        }
    } else if (status == 4) {
        if (sel == 4) {//승급 연출
            cm.sendNextS("자, 준비됐나 #r#h #!?#k 이젠 정말 돌이 킬 수 없다네!", 0x25, 9401232);
        }
        if (sel == 5) {
            var needitem1 = sel2 == 1 ? 4310007 : 1162001;
            var needitem1count = sel2 == 1 ? 30 : 2;
            var needstardust = sel2 == 1 ? 500 : 0;
            var sucitemname = sel2 == 1 ? "청" : "흑"
            var sucitemid = sel2 == 1 ? 1162000 : 1162002;
            cm.getPlayer().dropMessage(5, needstardust + " : " + cm.getPlayer().getKeyValue(100794, "point"));
            if (!cm.haveItem(needitem1, needitem1count)) {   //재료체크
                cm.sendOkS("#fs11##b#z" + sucitemid + "##k를 제작하기 위한 재료 아이템이 부족한거 같군.", 0x04, 9401232);
                cm.dispose();
                return;
            }
            if (cm.getPlayer().getKeyValue(100794, "point") < needstardust && needstardust > 0) {
                cm.sendOkS("#b#i4310310##z4310310##k이 부족한거 같네.", 0x04, 9401232);
                cm.dispose();
                return;
            }
            //필요한 아이템 차감
            cm.gainItem(needitem1, -needitem1count);
            if (sel2 == 1) {
                cm.gainItem(4310009, -1);
                cm.getPlayer().AddBloomingCoin(-needstardust, null);
            } else {
                inz = cm.getInventory(1)
                var counttt = 0;
                for (w = 0; w < inz.getSlotLimit(); w++) {
                    if (!inz.getItem(w)) {
                        continue;
                    }
                    if (inz.getItem(w).getItemId() == 1162001) {
                        MapleInventoryManipulator.removeFromSlot(cm.getPlayer().getClient(), GameConstants.getInventoryType(inz.getItem(w).getItemId()), inz.getItem(w).getPosition(), inz.getItem(w).getQuantity(), false);
                        counttt++;
                        if (counttt >= 2) {
                            break;
                        }
                    }
                }
            }

            if (Randomizer.isSuccess(suc)) {// 성공했을때
                var item = MapleItemInformationProvider.getInstance().getEquipById(sucitemid);
                if (sel2 == 2) {
                    item.setState(4);
                    MapleInventoryManipulator.addbyItem(cm.getClient(), item);
                } else {
                    cm.gainItem(sucitemid, 1);
                }
                World.Broadcast.broadcastMessage(CWvsContext.serverMessage(11, cm.getPlayer().getClient().getChannel(), "", cm.getPlayer().getName() + "님이 황혼 제작 시스템에서 {} 아이템을 획득하셨습니다!", true, item));
                말 = "#fs11#축하하네! 초월자의 힘에 다가가기 위한 첫걸음에 성공했네.\r\n\r\n"
                말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"
                말 += "#i" + sucitemid + "# #b#z" + sucitemid + "##k"
                cm.sendOkS(말, 0x04, 9401232);
                cm.dispose();
            } else { // 실패했을때
                txt = "";
                if (sel2 == 1) {
                    txt += "#fs11##r아쉽게도 제작에 실패했다네..";
                } else {
                    txt += "#fs11#아쉽게도 합성에 실패했다네..\r\n\r\n"
                    txt += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"
                    txt += "#i4310008##b#z4310008##k"
                }
                if (sel2 == 2) {//합성 실패시엔 황혼의 핵과 청뱃지가 지급
                    cm.gainItem(4310008, 1);
                }
                cm.sendOkS(txt, 0x04, 9401232);
                cm.dispose();
                return;
            }
        }
    } else if (status == 5) {
        if (sel == 4) {//승급 연출
            cm.sendNextS("네...!(꿀꺽)", 0x39, 2);
        }
    } else if (status == 6) {
        if (sel == 4) {//승급 연출
            succ2 = 0;
            if (selching == 0) {
                succ2 = 70;
            } else if (selching == 1) {
                succ2 = 50;
            }
            if (Randomizer.isSuccess(succ2)) {
                succ = true;
            }
            cm.sendNextS("그러면 정말 #e#b승급#n#k 시도를 진행하겠네!\r\n성공 확률은 #r" + succ2 + "%#k라네!", 0x25, 9401232);
        }
    } else if (status == 7) {
        if (sel == 4) {//승급 연출
            Packages.server.Timer.EtcTimer.getInstance().schedule(function () {
                cm.getPlayer().getClient().getSession().writeAndFlush(SLFCGPacket.MakeBlind(1, 0x00ff, 0x00f0, 0x00f0, 0x00f0, 1500, 0));
                statusplus(2000);
            }, 3000);
        }
    } else if (status == 8) {
        if (sel == 4) {//승급 연출
            cm.sendNextS("해치웠나...?", 0x39, 2);
        }
    } else if (status == 9) {
        if (sel == 4) {//승급 연출
            cm.getPlayer().getClient().getSession().writeAndFlush(SLFCGPacket.MakeBlind(1, 0, 0, 0, 0, 1500, 0));
            Packages.server.Timer.EtcTimer.getInstance().schedule(function () {
                cm.sendNextS("아니!!!!?!?!!?!?!?!!", 0x25, 9401232);
            }, 2000);
        }
    } else if (status == 10) {
        if (sel == 4) {//승급 연출
            name2 = "";
            if (selching == 0) {
                name2 = "블랙 페스티벌 적응자";
                minusitemid = 3700900;
                rewarditemid = 3700901;
                etc = 4310010;
                etc1 = 4310012;
                sale = 200;
                sale1 = 3000;
                meso = 500000000;
            } else if (selching == 1) {
                name2 = "블랙 페스티벌 마스터";
                minusitemid = 3700901;
                rewarditemid = 3700902;
                etc = 4310010;
                etc1 = 4310012;
                sale = 350;
                sale1 = 9999;
                meso = 1500000000;
            }
            cm.getClient().send(SLFCGPacket.SetIngameDirectionMode(true, true, false, false));
            Packages.server.Timer.EtcTimer.getInstance().schedule(function () {
                if (succ) {
                    cm.getClient().send(SLFCGPacket.showWZEffect("Effect/CharacterEff.img/GradeUp", 1));
                    cm.getClient().send(SLFCGPacket.showWZEffect("Effect/CharacterEff.img/allianceGradeup", 1));
                    cm.getClient().send(SLFCGPacket.getItemTopMsg(rewarditemid, "[알림] " + name2 + " 획득!!!"));
                    cm.getClient().send(SLFCGPacket.Chatonchr(cm.getPlayer().getPlayer(), "#fs15# #e#r승급#k에 성공했다!!!!!!!!!", 3000));
                } else {
                    cm.getClient().send(SLFCGPacket.Chatonchr(cm.getPlayer().getPlayer(), "#fs15# #e#r승급#k에 실패했다.......", 3000));
                }
                statusplus(4000);
            }, 2000);
        }
    } else if (status == 11) {
        if (sel == 4) {//승급 연출
            cm.getClient().send(CField.UIPacket.getDirectionStatus(false));
            cm.getClient().send(SLFCGPacket.SetIngameDirectionMode(false, false, false, false));
            if (succ) {
                cm.gainItem(minusitemid, -1);
                cm.gainItem(rewarditemid, 1);
                cm.gainItem(etc, -sale);
                cm.gainItem(etc1, -sale1);
                cm.gainMeso(-meso);
                말 = "#fs11#블랙 페스티벌에서의 승급을 축하한다네!\r\n\r\n"
                말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"
                말 += "#i" + rewarditemid + "##b#z" + rewarditemid + "##k";
                cm.sendOkS(말, 0x04, 9401232);
                cm.dispose();
            } else {
                말 = "#fs11#너무 상심하지 말게 #h #.. 다음엔 좋은 결과가 있을거라네..!\r\n\r\n"
                cm.gainItem(etc, -sale);
                cm.gainItem(etc1, -sale1);
                cm.gainMeso(-meso);
                cm.sendOkS(말, 0x04, 9401232);
                cm.dispose();
            }
        }
    }
}


function statusplus(millsecond) {
    cm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, millsecond));
}