/*
제작자 : qudtlstorl79@nate.com
*/

importPackage(java.lang);
importPackage(Packages.constants);
importPackage(Packages.handling.channel.handler);

파랑 = "#fc0xFF0054FF#";
연파 = "#fc0xFF6B66FF#";
연보 = "#fc0xFF8041D9#";
보라 = "#fc0xFF5F00FF#";
노랑 = "#fc0xFFEDD200#";
검정 = "#fc0xFF191919#";
분홍 = "#fc0xFFFF5AD9#";

앱솔랩스코인 = "#fUI/Basic.img/theblackcoin/0#";
스티그마코인 = "#fUI/Basic.img/theblackcoin/1#";
판타즈마코인 = "#fUI/Basic.img/theblackcoin/2#";
아라크노코인 = "#fUI/Basic.img/theblackcoin/3#";
소비 = "#fUI/Basic.img/theblackcoin/4#";
보조 = "#fUI/Basic.img/theblackcoin/5#";
보스 = "#fUI/Basic.img/theblackcoin/6#";
포인트 = "#fUI/Basic.img/theblackcoin/7#";
강화 = "#fUI/Basic.img/theblackcoin/8#";
악세 = "#fUI/Basic.img/theblackcoin/9#";
펫 = "#fUI/CashShop.img/CashItem_label/9#";
엠블렘 = "#fUI/Basic.img/theblackcoin/26#";
매그너스 = "#fUI/Basic.img/theblackcoin/27#";
십자 = "#fUI/Basic.img/theblackcoin/28#";
영웅의증표 = "#fUI/Basic.img/theblackcoin/34#";
뛰어라 = "#fUI/Basic.img/theblackcoin/33#";
더블랙 = "#fUI/Basic.img/theblackcoin/38#";
블라썸 = "#fUI/Basic.img/theblackcoin/11#";
유니온 = "#fUI/UIWindow4.img/pointShop/500629/iconShop#";
분홍콩 = "#fUI/UIWindow4.img/pointShop/100828/iconShop#";
네오젬 = "#fUI/UIWindow4.img/pointShop/100712/iconShop#";
검은콩 = "#fUI/UIWindow4.img/pointShop/501468/iconShop#";
잠수 = "#fUI/UIWindow4.img/pointShop/100161/iconShop#";
상점 = "#fUI/Basic.img/theblack/1#";
var status = -1, sel = 0;

function start() {
    action(1, 0, 0);
}

/*
            cm.dispose();
            InterServerHandler.EnterCS(cm.getPlayer().getClient(),cm.getPlayer(), false); 캐시샵
*/

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        var choose = ""+상점+"#fs11#\r\n";
        //choose = "#fc0xFFD5D5D5#───────────────────────────#k\r\n\r\n";
        choose += "#fs11##L0##fc0xFF6B66FF#" + 강화 + " 강화 아이템 상점#l";
        choose += "#L1##fc0xFF5587ED#" + 소비 + " 소비 아이템 상점#l\r\n";
        choose += "#L3##fc0xFFDBBC68#" + 보조 + " 보조 장비템 상점#l";
        choose += "#L4##fc0xFFF15F5F#" + 보스 + " 보스 결정석 판매#l\r\n";
        choose += "#L6##fc0xFFEDA900#" + 악세 + " 악세 장비템 상점#l";
        choose += "#L7##fc0xFFDBC000#" + 유니온 + " 블랙 유니온 상점#l\r\n";
        choose += "#L16##fc0xFF030066#" + 엠블렘 + " 강화 엠블렘 상점#l";
        choose += "#L23##fc0xFF030066#" + 잠수 + " 잠수 포인트 상점#l\r\n\r\n";
        choose += "#fs11##L20#" + 분홍콩 + " " + 분홍 + "분홍콩 상점#k#l";
        choose += "#L21#" + 네오젬 + " " + 파랑 + "돌의 정령 상점#k#l";
        choose += "#L22#" + 검은콩 + " #fc0xFF4374D9#검은콩 상점#k#l\r\n";
        choose += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
        choose += "#L8##fc0xFFFFBB00#" + 앱솔랩스코인 + " 앱솔랩스 장비 상점#l           #L9#앱솔랩스 코인 교환#l\r\n";
        choose += "#L10##fc0xFFFFBB00#" + 스티그마코인 + " 앱솔랩스 장비 상점#l           #L11#스티그마 코인 교환#l\r\n";
        choose += "#L12##fc0xFF8041D9#" + 판타즈마코인 + " 아케인셰이드 장비 상점#l　　#L13#판타즈마 코인 교환#l\r\n";
        choose += "#L14##fc0xFF8041D9#" + 아라크노코인 + " 아케인셰이드 장비 상점#l　　#L15#아라크노 코인 교환#l\r\n\r\n";
        cm.sendOkS(choose, 0x4);

    } else if (status == 1) {
        sel = selection;
        if (selection == 0) {
            말 = "#fs11#" + 검정 + "어떤 상점을 이용할지 골라보게.\r\n"
            말 += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
            말 += "#fs11##fc0xFF6B66FF##L0#" + 강화 + " 메소로 강화아이템#k" + 검정 + "을 구매하고 싶습니다.\r\n"
            말 += "#fs11##fc0xFFFF5AD9##L1#" + 분홍콩 + " 분홍콩으로 강화아이템#k" + 검정 + "을 구매하고 싶습니다.\r\n"
            cm.sendSimpleS(말, 0x04, 9062277)
        } else if (selection == 1) {
            cm.dispose();
            cm.openShop(1);
        } else if (selection == 2) {
            cm.sendYesNoS("#fs11##b페스티벌#k#fc0xFF191919#을 혼자 즐기기엔 쓸쓸한 모양이군!\r\n내가 준비한 아이들이 많으니 #r캐시샵#k#fc0xFF191919#에서 천천히 둘러보게나\r\n\r\n물론 #b무료#k#fc0xFF191919#로 분양하니 걱정말게.\r\n\r\n#r캐시샵#k #fc0xFF191919#입장을 원하시면 #r예#k #fc0xFF191919#버튼을 눌려주세요.", 0x04, 9062277);
        } else if (selection == 3) {
            cm.dispose();
            cm.openShop(2);
        } else if (selection == 4) {
            cm.dispose();
            cm.openShop(9001212);
        } else if (selection == 5) {
            말 = "#fs11##L20#" + 분홍콩 + " " + 분홍 + "분홍콩 상점#k" + 검정 + "을 이용하고 싶습니다.#l\r\n";
            말 += "#L11#" + 네오젬 + " " + 파랑 + "돌의 정령 코인 상점#k" + 검정 + "을 이용하고 싶습니다.#l\r\n";
            말 += "#L12#" + 검은콩 + " #fc0xFF4374D9#검은콩 상점#k" + 검정 + "을 이용하고 싶습니다.#l\r\n";
			말 += "#L1234#" + 매그너스 + " #fc0xFF4374D9##i2438696#마정석 상점#k" + 검정 + "을 이용하고 싶습니다.#l\r\n\r\n";
            말 += "#fc0xFFD5D5D5#───────────────────────────#k#l\r\n";
            말 += "#L13#" + 잠수 + " #fc0xFFAD8EDB#블랙 잠수 포인트 상점 관련#k" + 검정 + "을 이용하고 싶습니다.#l\r\n\r\n";
            말 += "#fc0xFFD5D5D5#───────────────────────────#k#l\r\n";
            말 += "#L14#" + 매그너스 + " #fc0xFFCC723D#타일런트 상점#k" + 검정 + "을 이용하고 싶습니다.#l\r\n";
            말 += "#L15#" + 영웅의증표 + " #fc0xFFCCA63D#영웅의 증표 상점#k" + 검정 + "을 이용하고 싶습니다.#l\r\n";
            말 += "#L16#" + 뛰어라 + " #fc0xFFCCA63D#뛰어라 코인 상점#k" + 검정 + "을 이용하고 싶습니다.#l\r\n";
            말 += "#L17#" + 더블랙 + " #fc0xFF997000#블랙 코인 상점#k" + 검정 + "을 이용하고 싶습니다.#l\r\n";
            말 += "#L18#" + 십자 + " #fc0xFFF2CB61#십자 코인 상점#k" + 검정 + "을 이용하고 싶습니다.#l\r\n";
            cm.sendSimpleS(말, 0x04, 9062277);
        } else if (selection == 6) {
            cm.dispose();
            cm.openShop(3003414);
        } else if (selection == 7) {
            cm.dispose();
            cm.openShop(9010107);
        } else if (selection == 8) {
            cm.dispose();
            cm.openShop(11);
        } else if (selection == 9) {
            cm.dispose();
            cm.openNpc(2155009);
        } else if (selection == 10) {
            cm.dispose();
            cm.openShop(15);
        } else if (selection == 20) {
            cm.dispose();
            cm.openShop(24);
        } else if (selection == 21) {
            cm.dispose();
            cm.openShop(26);
        } else if (selection == 22) {
            if (cm.getClient().getKeyValue("WishCoinGain") != null) {
                cm.getPlayer().setKeyValue(501372, "point", cm.getClient().getKeyValue("WishCoinGain"));
            }
            cm.dispose();
            cm.openShop(29);
        } else if (selection == 11) {
            cm.dispose();
            cm.openNpc(1540893);
        } else if (selection == 12) {
            cm.dispose();
            cm.openShop(16);
        } else if (selection == 13) {
            cm.dispose();
            cm.openNpc(3003105);
        } else if (selection == 14) {
            cm.dispose();
            cm.openShop(17);
        } else if (selection == 15) {
            cm.dispose();
            cm.openNpc(3003536);
        } else if (selection == 16) {
            cm.dispose();
            cm.openShop(27);
        } else if (selection == 17) {
            말 = "#fs11#" + 검정 + "#b블랙 이벤트#k" + 검정 + "로 다양한 아이템이 있는 상점이 개봉되었다네! 원하는걸 선택해보게나! 크크.\r\n"
            말 += "#fc0xFFD5D5D5#───────────────────────────#k#l\r\n";
            말 += "#fs11##L19##fc0xFF00B700#블랙 스타 코인 상점#k을 이용하고 싶습니다.#l\r\n";
            말 += "#L20##fc0xFF2478FF#블랙 스타 코인으로 훈장#k을 뽑아보고 싶습니다.\r\n";
            말 += "#L21##fc0xFFDB9700#블랙 스타 코인으로 뽑기 시스템#k을 이용하고 싶습니다.\r\n";
            cm.sendSimpleS(말, 0x04, 9062277);
        } else if (selection == 20) {
            cm.dispose();
            cm.openShop(43);
	}
    } else if (status == 2) {
        if (sel == 2) {
            InterServerHandler.EnterCS(cm.getPlayer().getClient(), cm.getPlayer(), false);
            cm.dispose();
            return;
        }
        if (selection == 0) {
            cm.dispose();
            cm.openShop(20);
        } else if (selection == 1) {
            cm.dispose();
            cm.openShop(23);
        } else if (selection == 10) {
            cm.dispose();
            cm.openShop(24);
        } else if (selection == 11) {
            cm.dispose();
            cm.openShop(26);
        } else if (selection == 12) {
            if (cm.getClient().getKeyValue("WishCoinGain") != null) {
                cm.getPlayer().setKeyValue(501372, "point", cm.getClient().getKeyValue("WishCoinGain"));
            }
            cm.dispose();
            cm.openShop(29);
        } else if (selection == 13) {
            말 = "#fs11#" + 검정 + "뭐든지 물어보게! 어떤게 궁금한가?\r\n"
            말 += "#fc0xFFD5D5D5#───────────────────────────#k#l\r\n";
            말 += "#L0##b블랙 잠수 포인트 상점을 이용하고 싶습니다.\r\n";
            말 += "#L1##b잠수 포인트는 어떻게 획득하나요?\r\n\r\n";
            말 += "#L99##r더이상 궁금한게 없습니다.";
            cm.sendSimpleS(말, 0x04, 9062277);
            /*cm.dispose();
            cm.openShop(28);*/
        } else if (selection == 14) {
            cm.dispose();
            cm.openShop(30);
        } else if (selection == 15) {
            cm.dispose();
            cm.openShop(32);
		} else if (selection == 1234) {
            cm.dispose();
            cm.openShop(9000179);	
        } else if (selection == 16) {
            cm.dispose();
            cm.openShop(33);
        } else if (selection == 17) {
            cm.dispose();
            cm.openShop(35);
        } else if (selection == 18) {
            cm.dispose();
            cm.openShop(34);
        } else if (selection == 19) {
            cm.dispose();
            cm.openShop(42);
        } else if (selection == 20) {
            cm.dispose();
            cm.sendOk("준비중입니다.");
        } else if (selection == 21) {
            cm.dispose();
            cm.sendOk("준비중입니다.");
        } else if (selection == 22) {
            cm.dispose();
            cm.openShop(24);
        } else if (selection == 23) {
            cm.dispose();
            cm.openShop(25);
        } else if (selection == 24) {
            cm.dispose();
            cm.openShop(14);
        } else if (selection == 25) {
            cm.dispose();
            cm.openShop(26);
        }
    } else if (status == 3) {
        if (selection == 0) {
            cm.openShop(28);
            cm.dispose();
        } else if (selection == 1) {
            if (cm.getClient().getQuestStatus(50006) == 1) {
                cm.getClient().setCustomKeyValue(50006, "1", "1");
            }
            말 = "#fs11##b블랙 잠수 포인트#k" + 검정 + " 획득 방법에 대해 알려주겠네.\r\n\r\n"
            말 += "" + 검정 + "우선, #b블랙 포인트#k" + 검정 + "는 #e마을#n" + 검정 + "에서만 획득이 가능하다네.\r\n"
            말 += "" + 검정 + "마을에서 #b의자나 라이딩#k" + 검정 + "을 사용중일때 #r60초#k" + 검정 + "마다 #r1 포인트#k" + 검정 + "씩 적립된다네.\r\n"
            말 += "#b어떠한 의자나 라이딩#k" + 검정 + "을 사용해도 상관없다네! 상점에 #b다양한 물품#k" + 검정 + "이 있으니 열심히 모아보게나! 크크크.\r\n"
            cm.sendOkS(말, 0x04, 9062277);
            cm.dispose();
        } else if (selection == 99) {
            cm.dispose();
        }
    }
}