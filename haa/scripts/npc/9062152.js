var status = -1;

var year, month, date2, date, day
var hour, minute;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
        return;
    }
    switch (cm.getPlayer().getMapId()) {
        case 993080500:
            if (mode == 0) {
                if (status == 0 && type == 3) {
                    cm.sendOk("언제든 나가고 싶다면 다시 말을 걸도록");
                    cm.dispose();
                    return;
                }
                status--;
            }
            if (mode == 1) {
                status++;
            }
            if (status == 0) {
                cm.sendYesNo("열심히 연습하는 모습 보기 좋군!\r\n그럼 이만 나가 보겠나?");
            } else if (status == 1) {
                cm.warp(100000000, 1);
                cm.dispose();
            }
            break;
        case 993080400:
            if (mode == 0) {
                if (status == 0 && type == 3) {
                    cm.sendOk("언제든 나가고 싶다면 다시 말을 걸도록");
                    cm.dispose();
                    return;
                }
                status--;
            }
            if (mode == 1) {
                status++;
            }
            if (status == 0) {
                cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.getClock(0));
                var em = cm.getEventManager("AdventureDrill");
                eim = cm.getPlayer().getEventInstance();
                if (eim == null) {
                    cm.dispose();
                    cm.getPlayer().dropMessage(5, "캐릭터 재접속 등의 이유로 허수아비 처치정보가 기록되지 않았습니다. 어드벤처 폴로 엔피시를 통해 다시 이용해 주세요.")
                    cm.warp(100000000, 1);
                    return;
                }
                mobcount = parseInt(em.getProperty("Monster"));
                coinqty = (Math.floor((mobcount - 1) / 4) + 1) * 10
                if (mobcount == 0) {
                    cm.sendNext("허수아비를 하나도 처치하지 못했으니 보상을 줄 수 없겠군.\r\n" +
                        "좀 더 단련해서 다시 돌아 오도록!");
                } else {
                    talk = "허수아비 #b#e" + mobcount + "마리#k#n를 처치했군!\r\n"
                    talk += "그렇다면 #b#e#i4009005##z4009005# " + coinqty + "개#k#n를 받아갈 수 있다."
                    cm.sendNext(talk);
                }
            } else if (status == 1) {
                if (mobcount == 0) {
                    eim.restartEventTimer(100);
                    cm.dispose();
                    return;
                } else {
                    cm.sendNextPrev("만약 결과에 만족하지 않는다면 #e보상을 받지 않고 퇴장#k#n해도 좋다.");
                }
            } else if (status == 2) {
                cm.sendNextPrev("그럴 경우 #e오늘의 도전 횟수#n가 차감되지 않고 언제든 재도전이 가능하지.");
            } else if (status == 3) {
                talk = "지금 당장 보상을 받고 퇴장하겠나?\r\n\r\n"
                talk += "#b#e- 처치한 허수아비 : " + mobcount + " / 40\r\n"
                talk += "- 지급 아이템 #b#i4009005##z4009005# : " + coinqty + "개#k\r\n\r\n"
                talk += "#L0#보상을 받고 퇴장한다.#l\r\n"
                talk += "#L1#보상을 받지 않고 퇴장한다.#l\r\n\r\n\r\n"
                talk += "#r※ 보상은 캐릭터 당 일일 1회만 받아갈 수 있습니다.#k#n"
                cm.sendSimple(talk);
            } else if (status == 4) {
                if (selection == 0) {
		    getData();
                    cm.setKeyValue(date, "AdventureDrill", "1");
                    cm.gainItem(4009005, coinqty);
                    eim.restartEventTimer(100);
                    cm.dispose();
                    return;
                } else {
                    cm.sendNext("좋다. 그렇다면 언제든 다시 도전할 수 있으니 참고하도록.");
                }
            } else if (status == 5) {
                eim.restartEventTimer(100);
                cm.dispose();
                return;
            }
            break;
        case 993080200:
            if (mode == 0) {
                if (status == 0 && type == 3) {
                    cm.sendOk("그렇다면 어서 빨리 허수아비를 더 처치해 보도록!");
                    cm.dispose();
                    return;
                }
                status--;
            }
            if (mode == 1) {
                status++;
            }
            if (status == 0) {
                cm.sendYesNo("아직 시간이 남아 있다!\r\n그래도 여기서 만족하고 퇴장하겠나?");
            } else if (status == 1) {
                eim = cm.getPlayer().getEventInstance();
                eim.restartEventTimer(1);
                cm.dispose();
            }
            break;
        default:
            if (mode == 0) {
                status--;
            }
            if (mode == 1) {
                status++;
            }
            cm.sendOk("[오류] 호출되어야 하지 않는 디폴트 값이 호출되었습니다. 운영진께 문의해주세요.");
            cm.dispose();
            return;
    }
}

function getData() {
/*
	year = CurrentTime.년() + 1900;
	month = CurrentTime.월() + 1;
	date2 = CurrentTime.일();
	date = year * 10000 + month * 100 + date2;
	day = CurrentTime.요일();
	hour = CurrentTime.시();
	minute = CurrentTime.분();
*/
   time = new Date();
   year = time.getFullYear();
   month = time.getMonth() + 1;
   if (month < 10) {
      month = "0"+month;
   }
   date2 = time.getDate() < 10 ? "0"+time.getDate() : time.getDate();
   date = year+""+month+""+date2;
   day = time.getDay();
   hour = time.getHours();
   minute = time.getMinutes();

}