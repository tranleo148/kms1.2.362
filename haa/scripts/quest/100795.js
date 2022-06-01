importPackage(Packages.tools.packet);
importPackage(java.lang);
importPackage(java.util);
importPackage(java.awt);
importPackage(Packages.server);

var status = -1;
var sel = 0;

function start(mode, type, selection) {
    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0) {
        if (status == 3) {
            qm.sendOkS("너무하담! 친구들을 도와달람!\r\n\r\n#b#e[이벤트 기간]#n\r\n - 2021년 6월 16일(수) 23시 59분까지#n#k", 4, 9062506);
            qm.dispose();
            return;
        }
        status--;
    }
    if (mode == 1) {
        d = status;
        status++;
    }


    if (status == 0) {
        if (qm.getClient().getCustomData(100795, "start") == null) {
            qm.getClient().setCustomData(100795, "start", "0");
        }
        if (parseInt(qm.getClient().getCustomData(100795, "start")) == 1) {
            txt = "#b#e<돌의 정령을 도와달람!>#n#k\r\n\r\n#b돌의 정령을 도와달람#k에 도전하여 획득한 #r스피릿 포인트#k를 #b성장의 비약#k과 교환해주고 있담.\r\n#b오늘의 블루밍 코인 3000개를 전부 모은 친구들만 참여할 수 있담.#k\r\n\r\n"
            txt += "#e이번주 월드 내 최고 점수: #n#r#e"+qm.getClient().getCustomData(100795, "weekspoint")+"점#n#k #e/ 1500점#n\r\n"
            txt += "#e이번주 받은 스피릿 포인트: #n#r#e"+qm.getClient().getCustomData(100795, "weekspoint")+"점#n#k #e/ 1500점#n\r\n"
            txt += "#e현재 보유한 스피릿 포인트: #n#r#e"+qm.getClient().getCustomData(501368, "spoint")+"점#n\r\n"
            txt += "#L1# #b<돌의 정령을 도와달람!>에 도전하고 싶어요.#k#l\r\n"
            txt += "#L2# #b<돌의 정령을 도와달람!>에 대해 알려주세요.#k#l\r\n"
            txt += "#L3# #b<블루밍 성장의 비약>을 구매하고 싶어요.#k#l"
            qm.sendNextS(txt, 4, 9062506);
        } else {
            qm.sendNextS("\r\n큰일났담!", 4, 9062506);
        }
    } else if (status == 1) {
        sel = selection;
        if (parseInt(qm.getClient().getCustomData(100795, "start")) == 1) {
            if (selection == 1) {
                //도전
                if (qm.getPlayer().getParty() != null) {
                    qm.sendOkS("#b<돌의 정령을 도와달람!>#k은 혼자서만 참여할 수 있담.\r\n#r#e파티를 해제하고#n#k 다시 찾아와달람.", 4, 9062506);
                    qm.dispose();
                    return;
                }
                if (qm.getPlayer().getKeyValue(100794, "today") < 3000) {
                    qm.sendOkS("#b<돌의 정령을 도와달람!>#k은 오늘의 블루밍 코인 3000개를 전부 모은 친구들만 참여할 수 있담.\r\n\r\n#r블루밍 코인 3000개를 모은 후에 다시 찾아와달람#k.\r\n(오늘 모은 블루밍 코인: #r#e"+qm.getPlayer().getKeyValue(100794, "today")+"#n#k개)", 4, 9062506);
                    qm.dispose();
                    return;
                }
                qm.warp(993192700, 0);
                qm.dispose();
            } else if (selection == 2) {
                //설명
                qm.sendNextS("친구들이 또 꽃줄기에 묶여 못 나오고 있담.\r\n 그래서 #b<블루밍 포레스트>#k에 찾아온 다른 종족의 친구들에게 도와달라고 부탁하고 있담.\r\n#b제한 시간 2분#k 동안 #b꽃 덤불 몬스터를 처치하며#k 자신의 강함도 알 수 있담.\r\n그리고 획득한 점수에 따라 #b스피릿 포인트#k도 줄 것이담.\r\n스피릿 포인트는 #b성장의 비약#k과 교환할 수 있담.", 4, 9062506);
            } else if (selection == 3) {
                //상점오픈
                qm.openShop(50);
                qm.dispose();
            }
        } else {
            qm.sendNextPrevS("\r\n친구들이 덩굴에 얽혀서 나오지 못하고 있담.", 4, 9062506);
        }
    } else if (status == 2) {
        if (parseInt(qm.getClient().getCustomData(100795, "start")) == 1) {
            if (sel == 2) {
                qm.sendNextPrevS("돌의 정령을 도와달람!에는 #r꽃 덤불 몬스터#k가 등장하고 #r총 12단계#k에 걸쳐 #b방어율#k과 #b레벨#k이 증가한담.\r\n#b제한 시간 2분#k 동안 꽃 덤불 몬스터에게 #b입힌 피해량#k에 비례하여 #b점수#k를 획득할 수 있고, 그 점수를 기반으로 #b스피릿 포인트#k를 얻을 수 있담.\r\n제한 시간이 끝나기 전 #b1500점#k에 도달한다면 친구들을 모두 구해준 것이담.", 4, 9062506);
            }
        } else {
            qm.sendNextPrevS("\r\n어서 친구들을 구해달람! #b<돌의 정령을 도와달람!>#k에\r\n도전하면 #b스피릿 포인트#k를 주겠담.\r\n#b스피릿 포인트#k는 #b각종 성장의 비약#k으로 교환할 수 있담!", 4, 9062506);
        }
    } else if (status == 3) {
        if (parseInt(qm.getClient().getCustomData(100795, "start")) == 1) {
            if (sel == 2) {
                qm.sendNextPrevS("꽃 덤불은 어찌된 일인지 #b매주 목요일#k에 새로 생겨나고 있담.\r\n점수도 목요일마다 초기화 해주겠담.", 4, 9062506);
            }
        } else {
            qm.sendYesNoS("#b#h0##k!\r\n도와줄 마음이 생겼냠!", 4, 9062506);
        }
    } else if (status == 4) {
        if (parseInt(qm.getClient().getCustomData(100795, "start")) == 1) {
            if (sel == 2) {
                qm.sendNextPrevS("돌의 정령을 도와달람!에서 일주일 당 얻을 수 있는 최대 점수는 #r월드 당 최대 1500점#k이고, #b이번주 월드 내 최고 점수#k를 갱신하면 추가로 #b스피릿 포인트#k를 획득할 수 있담.\r\n예를 들어서 이번주 월드 내 최고 점수가 #e1100점#n인 상태에서 추가 도전을 통해 #e1200점#n을 기록하면 #b스피릿 포인트 100점#k을 추가로 얻을 수 있담.", 4, 9062506);
            }
        } else {
            qm.sendNextS("역시 도와줄 것이라 생각했담!\r\n그럼 나한테 다시 말을 걸어달람!", 4, 9062506);
            qm.getClient().setCustomData(501368, "spoint", "0");
            qm.getClient().setCustomData(100795, "weekspoint", "0");
            qm.getClient().setCustomData(100795, "point", "0");
            qm.getClient().setCustomData(100795, "start", "1");
            qm.dispose();
        }
    } else if (status == 5) {
        qm.sendPrevS("설명은 여기까지담. 친구들을 도와주고 싶으면 나에게 말을 걸어달람.\r\n\r\n#b※ 이벤트 기간\r\n  - 2021년 06월 16일(수) 23시 59분까지#k", 4, 9062506);
    } else if (status == 6) {
        qm.dispose();
    }
}
function statusplus(millsecond) {
    qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, millsecond));
}