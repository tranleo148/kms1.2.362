// Copyright MelonK All rights reserved.
importPackage(Packages.server);
importPackage(Packages.tools.packet)
importPackage(Packages.tools)
importPackage(java.util);
importPackage(java.lang);
var status = -1;

/* 아래는 세팅부분 */
mapids = [993014200, 993018200, 993021200, 993029200];
setting = [{
        'smobid': [9010152, 9010153, 9010154, 9010155, 9010156, 9010157, 9010158, 9010159, 9010160, 9010161, 9010162, 9010163, 9010164, 9010165, 9010166, 9010167,
            9010168, 9010169, 9010170, 9010171, 9010172, 9010173, 9010174, 9010175, 9010176, 9070177, 9010178, 8644005, 8644006, 8644424, 8645042
        ],
        'smobx': [1736, 1872, 1944, 2074, 2154, 2237, 2368, 2435, 2567, 2647, 2750],
        'smoby': [399, 132, -81]
    },
    {
        'smobid': [9010152, 9010153, 9010154, 9010155, 9010156, 9010157, 9010158, 9010159, 9010160, 9010161, 9010162, 9010163, 9010164, 9010165, 9010166, 9010167,
            9010168, 9010169, 9010170, 9010171, 9010172, 9010173, 9010174, 9010175, 9010176, 9070177, 9010178, 8644005, 8644006, 8644424, 8645042
        ],
        'smobx': [3346, 3083, 2866, 2370, 2194, 1962, 1797],
        'smoby': [440, 185, -295, -55, -535, -655, -415, -175, 65]
    },
    {
        'smobid': [9010152, 9010153, 9010154, 9010155, 9010156, 9010157, 9010158, 9010159, 9010160, 9010161, 9010162, 9010163, 9010164, 9010165, 9010166, 9010167,
            9010168, 9010169, 9010170, 9010171, 9010172, 9010173, 9010174, 9010175, 9010176, 9070177, 9010178, 8644005, 8644006, 8644424, 8645042
        ],
        'smobx': [2042, 2213, 2372, 2412, 2565, 2612, 2773, 2812, 2991],
        'smoby': [440, 217, -23]
    },
    {
        'smobid': [9010152, 9010153, 9010154, 9010155, 9010156, 9010157, 9010158, 9010159, 9010160, 9010161, 9010162, 9010163, 9010164, 9010165, 9010166, 9010167,
            9010168, 9010169, 9010170, 9010171, 9010172, 9010173, 9010174, 9010175, 9010176, 9070177, 9010178, 8644005, 8644006, 8644424, 8645042
        ],
        'smobx': [1925, 2021, 2128, 2232, 2317, 2438, 2520, 2672, 2742, 2885, 2925, 3050],
        'smoby': [446, 218, -21]
    }
]
var mapid;
var temp;
var mobid;
var mobx;
var moby;

addmobcount = [119, 139, 159, 179, 199, 250]

ccoin = 4310261;

howsec = 2; // 얼마를 주기로 몹을 리스폰 할지 [단위 : 초]

var quest = "링크3";
var year, month, date2, date, day;

function start() {
    status = -1;
    action(1, 0, 0);
}

function getk(key) {
	if (cm.getPlayer().getKeyValue(201801, date+"_"+quest+"_"+key) == -1)
		setk(key, 0);
	return cm.getPlayer().getKeyValue(201801, date+"_"+quest+"_"+key);
}
function setk(key, value) {
	cm.getPlayer().setKeyValue(201801, date+"_"+quest+"_"+key, value);
}

function getData() {
	time = new Date();
	year = time.getFullYear();
	month = time.getMonth() + 1;
	if (month < 10) {
		month = "0"+month;
	}
	date2 = time.getDate() < 10 ? "0"+time.getDate() : time.getDate();
	date = Integer.parseInt(year+""+month+""+date2);
	day = time.getDay();
}
function action(mode, type, selection) {
    mapid = cm.getPlayer().getMapId();
    temp = mapids.indexOf(mapid);
    mobid = setting[temp]['smobid'];
    mobx = setting[temp]['smobx'];
    moby = setting[temp]['smoby'];
    cD = cm.getQuestRecord(12345);

    if (cD.getCustomData() == null) {;
        cD.setCustomData("0");
    }

    for (i = 0; i < addmobcount.length; i++) {
        if (cm.getPlayer().getLevel() <= addmobcount[i]) {
            mcount = Number(i + 1)
            break;
        }
    }
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        if (status == 0 && selection == -1) {
            selection = 1
        }
        status++;
    }

    if (status == 0) {
	getData();
	if (getk("day") == 6) {
		cm.sendOk("오늘은 더 이상 충전할 수 없답니다.");
		cm.dispose();
		return;
	}
        말 = "메이플월드에서 가장 쉬원한 사냥터!\r\n"
        말 += "#b<#m" + cm.getMapId() + "#>#k에 온 걸 진심으로 환영해~!\r\n\r\n";
        말 += "#b#L0# 소환되는 몬스터 종류를 변경#k할래.\r\n"
        말 += "#b#L1# 사냥 가능 몬스터 수를 충전#k할래.\r\n"
        cm.sendSimple(말);
    } else if (status == 1) {
        S = selection;
        if (selection == 0) {
            말 = "변경혹은 소환하실 몬스터를 선택해 주세요.\r\n\r\n"
            for (i = 0; i < mobid.length; i++) {
              //  gL = Math.floor(cm.getPlayer().getLevel() / 5)
               // if (i >= gL - 3 && i <= Number(gL + 1)) {
                    gS = Number(i + 1) * 10
                    말 += "#L" + i + "# #b(Lv." + gS + ") #o" + mobid[i] + "#\r\n"
            }
            cm.sendSimple(말);
        } else if (selection == 1) {
            limit = Math.floor(cm.getPlayer().getStarDustCoin()) < 6 ? Math.floor(cm.getPlayer().getStarDustCoin()) : 6;
            cm.sendGetNumber("#fs11#몇 회나 충전할 거니?\r\n\r\n#r1회당 몬스터 500마리를 충전합니다.\r\n한 번에 2500마리 이상 충전해 발생한 문제는 책임지지 않습니다.\r\n갑자기 남은 마리수가 모두 사라질 경우 캐릭터를 재접속 해주세요.\r\n\r\n#b( 보유 중인 #z" + ccoin + "# : " + cm.getPlayer().getStarDustCoin() + "개)", 1, 1, limit);
        } else {
            cm.sendOk(selection)
        }
    } else if (status == 2) {
        if (cD.getCustomData() == "0") {
            cD.setCustomData("1");
        } else {
            cD.setCustomData("0");
        }
        if (S == 1) {
            if (selection <= limit) {
		setk("day", (getk("day") + selection));
                cm.addFrozenMobCount(500 * selection);
                cm.getPlayer().addStarDustCoin(-selection);
                cm.getPlayer().dropMessage(1, selection * 500 + "마리 충전이 완료되었습니다.");
                selection = Math.floor(cm.getPlayer().getLevel() / 10) - 1
            } else {
                cm.sendOk("#fs11#오류가 감지되었습니다.");
                cm.dispose();
                return;
            }
        }
        cm.killAllMob();
        gCD = cD.getCustomData();
        var tick = 0;
        var schedule = Packages.server.Timer.MapTimer.getInstance().register(function () {
            if (Number(cm.getFrozenMobCount()) <= 0 || cm.getPlayer().getMapId() != mapid || gCD != cm.getQuestRecord(12345).getCustomData()) {
                if (cm.getPlayer().getMapId() != mapid) {
                    cm.getClient().getChannelServer().getMapFactory().getMap(mapid).killAllMonsters(true);
                }
                schedule.cancel(true);
            }
            if (tick == 0) {
                for (i = 0; i < mobx.length; i++) {
                    for (g = 0; g < moby.length; g++) {
                        cm.spawnLinkMob(mobid[selection], mobx[i], moby[g]);
                    }
                }
                cm.killAllMob();
            }
            if (tick > 0 && (tick % howsec == 0)) {
                realmcount = cm.getMonsterCount(mapid);
                howmany = mobx.length * moby.length
                for (i = 0; i < howmany - realmcount; i++) {
                    rd1 = Math.floor(Math.random() * mobx.length);
                    rd2 = Math.floor(Math.random() * moby.length);
                    cm.spawnLinkMob(mobid[selection], mobx[rd1], moby[rd2]);
                }
            }
            tick++;

        }, howsec)
        cm.dispose();
    }
}