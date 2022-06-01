


/*

    * 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

    * (Guardian Project Development Source Script)

    화이트 에 의해 만들어 졌습니다.

    엔피시아이디 : 9071000

    엔피시 이름 : 슈피겔만

    엔피시가 있는 맵 : 몬스터파크 : 몬스터파크 (951000000)

    엔피시 설명 : MISSINGNO


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
var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status--;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        if (cm.haveItem(1182193, 1) && cm.haveItem(1182194, 1) && cm.haveItem(1182195, 1) && cm.haveItem(1182196, 1) && cm.haveItem(1182197, 1) && cm.haveItem(1182198, 1) && cm.haveItem(1182199, 1)) {
            talk = "몬스터파크에 온 것을 환영하네! 크크. 나는 몬스터파크의 주인, 슈피겔만일세!\r\n"
            talk += "#L10##b슈피겔만씨! 7가지 요일 뱃지를 모두 모아 왔어요!"
            cm.sendSimpleS(talk, 0x24);
        } else {
            talk = "몬스터파크에 온 것을 환영하네! 크크. 나는 몬스터파크의 주인, 슈피겔만일세!\r\n"
            talk += "궁금한 것이 있으면 무엇이든 물어봐도 좋아!\r\n\r\n"
            talk += "#L0##b<몬스터파크 REBORN>에 대해서 알려주세요.#l\r\n"
            talk += "#L1##b하루 이용 횟수와 이용료를 알려주세요.#l\r\n"
            talk += "#L2##b요일 마다 어떤 보상이 있는지 알려주세요.#l\r\n"
            talk += "#L3##b몬스터파크 요일 뱃지에 대해서 알려주세요.#l\r\n"
            talk += "#L99##b설명을 그만 들을게요.#l\r\n"
            cm.sendSimpleS(talk, 0x84);
        }
    } else if (status == 1) {
        if (selection == 0) {
            sel = 0
            talk = "몬스터파크의 새로운 컨셉에 대해서 궁금한 모양이군! 크크.\r\n"
            talk += "#b<몬스터파크 REBORN>#k은 #b요일마다 다른 컨셉#k의 보상이 준비된 재미있는 컨셉의 테마파크지!\r\n\r\n"
            cm.sendNextS(talk, 0x24);
        } else if (selection == 1) {
            sel = 1
            talk = "#b몬스터파크 REBORN>#k은 하루 7번까지 이용할 수 있다네.\r\n"
            talk += "입장할 때 티켓을 내던 이전과는 다르게 #b<몬스터파크 REBORN>#k은 #b후.불.제.#k라네, 크크.\n\r\n"
            cm.sendNextS(talk, 0x24);
        } else if (selection == 2) {
            sel = 2
            talk = "#b월요일 보상#k : 제작 재료 아이템이 들어 있는 #b<창조의 월요일 상자>#k\r\n"
            talk += "#b화요일 보상#k : 강화 재료 아이템이 들어 있는 #b<강화의 화요일 상자>#k\r\n"
            talk += "#b수요일 보상#k : 성향 향상 아이템이 들어 있는 #b<창조의 수요일 상자>#k\r\n"
            talk += "#b목요일 보상#k : 명성치 향상 아이템이 들어 있는 #b<창조의 목요일 상자>#k\r\n"
            cm.sendNextS(talk, 0x24);
        } else if (selection == 3) {
            sel = 3
            cm.sendNextS("취미로 이런 저런걸 만들다 보니 #b특정 요일에만 숨겨진 효과#k가 드러나는\r\n#b재미있는 장신구#k를 만들수 있게 되었지 뭐야.", 0x24);
        } else if (selection == 10) {
            sel = 4
            cm.sendNextS("#b7가지 요일 뱃지#k를 모두 모아오다니!\r\n몬스터파크 우량 고객이시군! 크크!", 0x24);
        } else if (selection == 99) {
            cm.dispose();
        }
    } else if (status == 2) {
        if (sel == 0) {
            cm.sendNextS("이용 방법도 완전히 달라졌다네. 몬스터파크에서 몬스터를 사냥하면 보상으로 받을 수 있는 경험치가 #b점점 축적#k된다네.", 0x24);
        } else if (sel == 1) {
            cm.sendNextS("한번 이용료는 #b해피 코인 30개#k!\r\n#b이용료는 퇴장할 때#k 받으니까 들어와서 중간에 나간다고\r\n이용료가 차감되거나 이용 횟수가 올라가지는 않으니 안심하라구.", 0x24);
        } else if (sel == 2) {
            talk = "#b금요일 보상#k : 메소가 들어 있는 #b<황금의 금요일 상자>#k\r\n"
            talk += "#b토요일 보상#k : 다른 모든 요일의 보상을 만날 수 있는 #b<창조의 토요일 상자>#k\r\n"
            talk += "#b일요일 보상#k : 경험치 쿠폰, 정령의 펜던트가 들어 있는 #b<창조의 일요일 상자>#k\r\n"
            talk += "　　　　　　#b+ 경험치 보상이 무려 1.5배!!#k\r\n"
            cm.sendNextS(talk, 0x24);
        } else if (sel == 3) {
            cm.sendNextS("예전에 만들어 두었던걸 #b슈피겔만의 보물 창고#k에 넣어 두었는데\r\n이번 #b<몬스터파크 REBORN>#k 컨셉에 딱 맞는 거 같아 꺼내왔지! 크크크.", 0x24);
        } else if (sel == 4) {
            cm.sendYesNoS("그래도 #b7기자 재미있고 신기한 요일 뱃지#k가 더 좋아보이는데\r\n굳이 평범한 #i1182200# #b#z1182200##k로 교환하겠나?", 0x24);
        }
    } else if (status == 3) {
        if (sel == 0) {
            cm.sendNextS("마지막 스테이지까지 모든 몬스터를 사냥하고 나갈 때..#b축적된 경험치#k와 #b보너스 요일 상자#k를 한꺼번에 받게 되는 방식이지, 크크.", 0x24);
        } else if (sel == 1) {
            cm.sendNextS("#b매일 하루 2번#k은 #b무료 이용#k이 가능하니까 부담없이 이용해보시게.\r\n#b엄청난 경험치#k와 #b재미있는 요일 상자 보상#k을 보면\r\n이용료가 아깝지 않다는 생각이 들꺼야! 하하하!", 0x24);
        } else if (sel == 2) {
            cm.openNpc(9071000);
            cm.dispose();
        } else if (sel == 3) {
            cm.sendNextS("뱃지는 요일 상자에서 얻을 수 있다네, 운이 좋다면 말이지, 크크크.\r\n만약 #b7가지 요일 뱃지#k를 전부 모아오면 언제나 효과가 발동되는\r\n#b평범한 장신구#k로 교환해주도록 하지! 크크.", 0x24);
        } else if (sel == 4) {
            inz = cm.getInventory(1)
            for (w = 0; w < inz.getSlotLimit(); w++) {
                if (!inz.getItem(w)) {
                    continue;
                }
                if (inz.getItem(w).getItemId() >= 1182193 && inz.getItem(w).getItemId() <= 1182199) {
                    MapleInventoryManipulator.removeFromSlot(cm.getPlayer().getClient(), GameConstants.getInventoryType(inz.getItem(w).getItemId()), inz.getItem(w).getPosition(), inz.getItem(w).getQuantity(), false);
                }
            }
            cm.gainItem(1182200, 1);
            cm.dispose();
        }
    } else if (status == 4) {
        cm.openNpc(9071000);
        cm.dispose();
    }
}