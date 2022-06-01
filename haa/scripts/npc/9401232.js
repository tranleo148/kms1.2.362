/*
제작자 : qudtlstorl79@nate.com
*/

importPackage(Packages.constants);

검정 = "#fc0xFF191919#";
연파 = "#fc0xFF4374D9#";
연보 = "#fc0xFF8041D9#";
보라 = "#fc0xFF5F00FF#";
노랑 = "#fc0xFFEDD200#";
파랑 = "#fc0xFF4641D9#";
빨강 = "#fc0xFFF15F5F#";
하늘 = "#fc0xFF0099CC#";

퀘스트 = "#fs11##fUI/UIWindow2.img/Quest/icon/iconInfo#";
가이드 = "#fs11##fUI/Basic.img/theblackcoin/26#";
var status = -1;
var seld = 0;
function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        var choose = "#fs11##b블랙 페스티벌dd#k" + 검정 + "에 온 것을 환영하네! 자네들을 위해 #b페스티벌#k" + 검정 + "에서 궁금해할 만한 것들을 모아봤다네.\r\n\r\n";
        choose += "#fUI/UIWindow2.img/UtilDlgEx/list4#\r\n#fs12#";
        choose += "#L0#" + 하늘 + "[블랙] 서버 규칙 및 필수 안내" + 퀘스트 + "#l\r\n";
        choose += "#fs12##L1#" + 하늘 + "[블랙] 블랙 페스티벌 칭호 받기" + 퀘스트 + "#l\r\n";
        choose += "#fs12##L2##d[블랙] 추천인 설명 및 상점#l\r\n";
        choose += "#fs12##L3##d[블랙] 데일리기프트 상점#l\r\n";
        choose += "#fs12##L4##d[블랙] 블랙 페스티벌로 이동하기#l\r\n";
        cm.sendSimple(choose);

    } else if (status == 1) {
        seld = selection;
        if (selection == 0) {
            말 = "#fs11##b블랙 페스티벌#k에 온 #e#h ##n용사여, 어떤게 궁금한가?#k\r\n"
            말 += "#fc0xFFD5D5D5#─────────────────────────────#k#l\r\n";
            말 += "#L0##fs11##b블랙 배율에 대해서 궁금해요.\r\n"
            말 += "#L1#기본적인 규칙에 대해 알려주세요.\r\n\r\n"
            말 += "#L2##r대화 그만하기"
            cm.sendOkS(말, 0x04);
        } else if (selection == 1) {
            if (cm.haveItem(3700900, 1)) {
                cm.sendOk("#fs11#" + 검정 + "흠, 자네는 이미 칭호를 가지고 있는것 같네. 만약 잃어버린다면 다시 지급해주도록 하겠네!");
                cm.dispose();
                return;
            }
            var choose = "#fs11#"+검정+" 칭호를 지급 했다네. 더 좋은 칭호를 얻기 위해 #fc0xFF050099#블랙 페스티벌#k"+검정+"에서 열심히 즐겨보게나!\r\n";
            choose += "#fc0xFFD5D5D5#─────────────────────────────#k#l\r\n\r\n";
            choose += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"
            choose += "#i3700900# #b#z3700900##k"
            cm.gainItem(3700900, 1);
            cm.sendSimpleS(choose, 0x04);
            cm.dispose();
        } else if (selection == 2) {
            var choose = "#fs11#"+검정+"#b블랙 페스티벌#k"+검정+"에서 느낀 즐거움을 다른 사람에게도 나눠주게!\r\n";
            choose += "#fc0xFFD5D5D5#───────────────────────────#k#l#b\r\n";
            choose += "#L10#추천인 상점을 이용하고 싶어요.\r\n"
            choose += "#L11#시스템에 대해서 궁금해요.\r\n\r\n"
            choose += "#r#L2##r대화 그만하기"
            cm.sendSimpleS(choose, 0x04);
        } else if (selection == 3) {
            cm.dispose();
            cm.openShop(40);
        } else if (selection == 4) {
            cm.dispose();
            cm.warp(100000051);
        }
    } else if (status == 2) {
        if (selection == 0) {
            말 = "#fs11##e 레벨　　   경험치　　메소　　드랍#n\r\n"
            말 += "" + 파랑 + " 1~300　　　　　　　　  x 3　　　x 1\r\n"
            말 += "" + 파랑 + "1~199　　   x 500\r\n"
            말 += "" + 파랑 + "200~209　　 x 200\r\n"
            말 += "" + 파랑 + "210~219　　 x 175\r\n"
            말 += "" + 파랑 + "220~229　　 x 125\r\n"
            말 += "" + 파랑 + "230~239　　 x 100\r\n"
            말 += "" + 파랑 + "240~249　　 x 50\r\n"
            말 += "" + 파랑 + "250~259　　 x 25\r\n"
            말 += "" + 파랑 + "260~269　　 x 10\r\n"
            말 += "" + 파랑 + "270~300　　 x 20\r\n"
            cm.sendGetText(말, 9401232);
            cm.dispose();
        } else if (selection == 1) {
            말 = "#fs11#" + 검정 + "정말 규칙을 확인하겠나?\r\n\r\n"
            말 += "#L0##b확인하고 싶습니다.\r\n"
            말 += "#L1##r아니요."
            cm.sendSimpleS(말, 0x04, 9401232);
        } else if (selection == 2) {
            cm.dispose();
        } else if (selection == 10) {
            if (cm.getClient().getKeyValue("RecommendPoint") != null) {
                cm.getPlayer().setKeyValue(501215, "point", cm.getClient().getKeyValue("RecommendPoint"));
                
            }
            cm.openShop(39);
            cm.dispose();
        } else if (selection == 11) {
            말 ="#fs11#"+보라+"추천인 시스템"+검정+"에 대해 알려주겠네.\r\n\r\n"
            말 +="우선, #b편의시스템#k"+검정+"에 있는 #b추천인 시스템#k"+검정+"을 통해서 추천 등록이 가능하다네.\r\n"
            말 +="추천은 #r계정당 1회#k"+검정+"만 가능하고 추천을 등록하면 블랙에서 지급해주는 #b특별한 아이템#k"+검정+"을 받을 수 있지. 크크\r\n"
            말 +="자네가 추천을 받게 된다면 #r추천인 포인트가 1#k"+검정+"씩 오른다네.\r\n"
            말 +="받은 포인트로 상점에서 #b다양한 아이템#k"+검정+"을 구매할 수 있으니 블랙 서버를 널리 알려주게. 크크크."
            cm.sendOk(말);
            cm.dispose();
        }
    } else if (status == 3) {
        if (seld == 0) {
            cm.dispose();
            cm.openNpc(2006, "ServerNotice_0");
        }
    } else if (seld == 1) {
        cm.dispose();
    }
}