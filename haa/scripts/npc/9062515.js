


/*

    * 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

    * (Guardian Project Development Source Script)

    캡틴 에 의해 만들어 졌습니다.

    엔피시아이디 : 9062515

    엔피시 이름 : 바람의 정령

    엔피시가 있는 맵 : 블루밍 포레스트 : 꽃 피는 숲 (993192000)

    엔피시 설명 : 위시 코인샵


*/

var status = -1;
var tuto = false;
var bossname = ["이지 시그너스", "하드 힐라", "카오스 핑크빈", "노멀 시그너스", "카오스 자쿰", "카오스 피에르", "카오스 반반", "카오스 블러디퀸", "하드 매그너스", "카오스 벨룸", "카오스 파풀라투스", "노멀 스우", "노멀 데미안", "이지 루시드", "노멀 루시드", "노멀 윌", "노멀 더스크", "노멀 듄켈", "하드 데미안", "하드 스우", "하드 루시드", "하드 윌", "카오스 더스크", "하드 듄켈", "진 힐라", "세렌"];
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
        if (cm.getClient().getKeyValue("WishCoin") != null) {
            tuto = true;
        }
        if (tuto) {
            cm.sendNextS("  여기도~ 저기도~ 꽃들이~ 행복해~\r\n#L0# #r#e<위시 코인샵>#n을 이용하고 싶어.#k#l\r\n#L1# #b#e<위시 코인 주간 획득 현황>#n을 알려줘.#k#l\r\n#L2# #b#e<위시 코인>#n에 대해 알려줘.#k#l", 4, 9062515);
        } else {
            cm.sendNextS("두.근.두.근.콩.닥.콩.닥.기.분.좋.아. 예!\r\n흠, 흠, #b#e위시 코인샵#k#n이 처음이면~ 알려주지~ 냐냐냥~", 4, 9062515);
        }
    } else if (status == 1) {
        if (tuto) {
            if (selection == 0) {
                //상점오픈
                if (cm.getClient().getKeyValue("WishCoinGain") != null) {
                    cm.getPlayer().setKeyValue(501372, "point", cm.getClient().getKeyValue("WishCoinGain"));
                }
                cm.openShop(29);
                cm.dispose();
            } else if (selection == 1) {
                txt = "#b#e위시 코인#n#k 얼마나~ 찾았는지~ 알려주지~\r\n\r\n";
                txt += "- 이번 주차 : #r#e" + cm.getClient().getKeyValue("WishCoinWeekGain") + "#n#k 개 (주간 최대 400개 획득 가능)\r\n- 현재 보유 : #b#e" + cm.getClient().getKeyValue("WishCoinGain") + "#n#k 개\r\n\r\n"
                txt += "#e[주간 획득 현황]#n\r\n"
                var str = cm.getClient().getKeyValue("WishCoin");
                var bosslist = str.split("");
                for (a = 0; a < bosslist.length; a++) {
                    if (parseInt(bosslist[a]) == 1) {
                        txt += "#r- " + bossname[a] + " 획득#k\r\n"
                    } else {
                        txt += "- " + bossname[a] + " 미획득\r\n"
                    }
                }
                cm.sendNextS(txt, 4, 9062515);
                cm.dispose();
            } else if (selection == 2) {
                cm.sendNextS("냐냐냐~ 소망~ 바람~ #b#e위시 코인#n#k 궁금한 건~ 못 참지~", 4, 9062515);
            }
        } else {
            cm.sendNextS("살랑살랑~ 슬금슬금~ 강한 보스~ 지나치며~\r\n#b#e위시 코인#n#k~ 숨겨놨지~ 스.릴.넘.쳐. 예!", 4, 9062515);
        }
    } else if (status == 2) {
        if (tuto) {
            cm.sendNextS("살랑살랑~ 슬금슬금~ 강한 보스~ 지나치며~\r\n#e#b위시 코인#k#n~ 숨겨놨지~ 스.릴.넘.쳐. 예!", 4, 9062515);
        } else {
            cm.sendNextS("하지만~ #b#e위시 코인#n#k 되찾기~ 어려워~", 4, 9062515);
        }
    } else if (status == 3) {
        cm.sendNextS("보스~ 처치하고~ #b#e위시 코인#n#k 찾아오면~\r\n듬뿍~ 듬뿍~ 선물 가득~\r\n\r\n#e※위시 코인샵 이용 기간#r\r\n6월 20일 오후 11시 59분까지#k#n\r\n\r\n#e※위시 코인 획득 가능 기간#r\r\n4월 29일 자정 ~ 6월 16일 오후 11시 59분까지#k#n\r\n\r\n#e※위시 코인 획득 제한량\r\n주간 최대 #r400개#k까지#n\r\n\r\n(주간 위시 코인 획득 기록은 #r매주 목요일 자정#k에 초기화)", 4, 9062515);
    } else if (status == 4) {
        cm.sendNextS("강한 보스면~ 숨겨둔 #b#e위시 코인#n#k 많은 게~ 인지상정~\r\n\r\n#e[#i2633304# #t2633304# 5개]#n\r\n 이지 시그너스\r\n 하드 힐라\r\n 카오스 핑크빈\r\n 노멀 시그너스\r\n#e[#i2633304# #t2633304# 10개]#n\r\n 카오스 자쿰\r\n 카오스 피에르\r\n 카오스 반반\r\n 카오스 블러디퀸\r\n#e[#i2633304# #t2633304# 20개]#n\r\n 하드 매그너스\r\n 카오스 벨룸\r\n#e[#i2633304# #t2633304# 30개]#n\r\n 카오스 파풀라투스\r\n 노멀 스우\r\n 노멀 데미안\r\n 이지 루시드\r\n#e[#i2633304# #t2633304# 40개]#n\r\n 노멀 루시드\r\n 노멀 윌\r\n 노멀 더스크\r\n 노멀 듄켈\r\n#e[#i2633304# #t2633304# 60개]#n\r\n 하드 데미안\r\n 하드 스우\r\n 하드 루시드\r\n 하드 윌\r\n#e[#i2633304# #t2633304# 70개]#n\r\n 카오스 더스크\r\n 하드 듄켈\r\n 진 힐라\r\n 세렌\r\n\r\n#r※주의※\r\n- 카오스 블러디퀸, 카오스 피에르, 세렌은 처치 후 보물상자를 오픈해야 위시 코인이 드롭됩니다.\r\n- 루시드는 처치 후 악몽의 오르골을 격파해야 위시 코인이 드롭됩니다.", 4, 9062515);
    } else if (status == 5) {
        cm.dispose();
        if (cm.getClient().getKeyValue("EnterDay") == null) {
            cm.getClient().setKeyValue("WishCoin", "00000000000000000000000000");
            cm.getClient().setKeyValue("WishCoinGain", "0");
            cm.getClient().setKeyValue("WishCoinWeekGain", "0");
            cm.openNpc(9062515);
        }
    }
}
