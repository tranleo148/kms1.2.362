/*
제작자 : qudtlstorl79@nate.com
*/

importPackage(java.lang);
importPackage(Packages.constants);
importPackage(Packages.handling.channel.handler);

별회 = "#fUI/GuildMark.img/Mark/Pattern/00004001/15#";
보라 = "#fMap/MapHelper.img/weather/starPlanet/7#";
파랑 = "#fMap/MapHelper.img/weather/starPlanet/8#";
더블랙 = "#fUI/Basic.img/theblack/3#";
var status = 0;
var status2 = -1;
var beauty = 0;
var facenew;
var mfacenew;
var hfacenew;
var colors;
var colors_;
var mhairnew;
var fhairnew;
var haircolor;
var haircolor_;
var st;
var skin = Array(0, 1, 2, 3, 4, 9, 10, 11, 12, 13, 15, 16, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27);
//var skin = Array(0, 1, 2, 3, 4, 5, 9, 10, 11, 12, 13, 15, 16);

var select = -1;
var select2 = -1;

var page = -1;
var page2 = -1;
function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection, selection2) {
    if (status >= 0 && mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 1 && status == 1 && selection == 0 && (cm.getPlayer().getJob() >= 6500 && cm.getPlayer().getJob() <= 6512)) {
        cm.dispose();
        return;
    } else if (mode == 1) {
        status++;
    } else {
        status--;
    }
    if (status == 0) {
        var change = "#fs11#\r\n";
        change += "                           " + 보라 + " #k#fc0xFF3B00DB#코디 관련#k " + 보라 + "\r\n#fc0xFF7112FF#         ";
        change += "#L9# 헤어 변경#l";
        change += "#L10# 성형 변경#l";
        change += "#L999# 캐시검색#l\r\n\r\n\r\n";
        change += "                           " + 보라 + " #k#fc0xFFC900A7#컬러 관련#k " + 보라 + "\r\n#fc0xFFF361A6#         ";
        change += "#L0# 피부 변경#l";
        change += "#L7# 믹스 염색#l";
        change += "#L4# 렌즈 변경#l\r\n\r\n\r\n";
        change += "                           " + 보라 + " #k#fc0xFF6E2FC7#기타 관련#k " + 보라 + "\r\n#fc0xFF9253EB#         ";
        change += "#L11# 각종 치장#l";
        change += "#L8# 성별 전환#l";
        change += "#L6# 안드로이드 치장#l";
        cm.sendOkS(change, 0x4);
    } else if (status == 1) {
        if (selection == 101) { // 캐시샵
            cm.dispose();
            InterServerHandler.EnterCS(cm.getPlayer().getClient(), cm.getPlayer(), false);
        }
        if (selection == 0) { // 스킨
            beauty = 1;
            cm.askCoupon(5153015, skin);
        } else if (selection == 1) {
            cm.dispose();
            cm.openNpc(1540493);
        } else if (selection == 999) {
			cm.dispose();
			cm.openNpcCustom(cm.getClient(), 1052208, "Itemsearch");
			return;
        } else if (selection == 2) { // 염색
            beauty = 3;
            haircolor = Array();
            haircolor_ = Array();
            var idx = 0;
            if (cm.getPlayer().getJob() == 10112) {
                var current = parseInt(Math.floor(cm.getPlayer().getHair() / 10)) * 10;
                for (var i = 0; i < 8; i++) {
                    //         if (cm.isExistFH(current + i)) {
                    haircolor[idx++] = current + i;
                    //         }
                }
                current = parseInt(Math.floor(cm.getPlayer().getSecondHair() / 10)) * 10;
                idx = 0;
                for (var i = 0; i < 8; i++) {
                    //         if (cm.isExistFH(current + i)) {
                    haircolor_[idx++] = current + i;
                    //         }
                }
                cm.sendStyle("특별히 #h #님을 위해 무료로 외형을 바꿔드릴게요…. 마음에 드시는 것으로 선택하세요.", haircolor, haircolor_);
            } else {
                var current = parseInt(Math.floor(cm.getPlayer().getHair() / 10)) * 10;
                for (var i = 0; i < 8; i++) {
                    //                    if (cm.isExistFH(current + i)) {
                    haircolor[idx++] = current + i;
                    //                    }
                }
                cm.sendStyle("#h #님이 원하시는 스타일을 골라보세요!", haircolor, haircolor);
            }
        } else if (selection == 4) { // 컬러 렌즈
            if (cm.getPlayer().getJob() == 10112) {
                beauty = 5;
                for (var a = 0; a < 2; a++) {
                    faceid = a == 1 ? cm.getPlayer().getSecondFace(): cm.getPlayer().getFace();
                    if (faceid > 99999) {
                        //얼굴믹스렌즈로 되있는경우엔 다시 처음으로돌리고나서.
                       faceid = Math.floor(faceid / 1000);
                    }
                    var front = faceid / 1000;
                    var current = (Math.floor(front) * 1000) + faceid % 100;
                    if (a == 0) {
                        colors = Array(current, current + 100, current + 200, current + 300, current + 400, current + 500, current + 600, current + 700);
                    } else {
                        colors_ = Array(current, current + 100, current + 200, current + 300, current + 400, current + 500, current + 600, current + 700);
                    }
                }
                if (cm.getPlayer().getGender() == 0) {
                    cm.sendStyle("#h #님이 원하시는 스타일을 골라보세요!", colors);
                } else {
                    cm.sendStyle("#h #님이 원하시는 스타일을 골라보세요!", colors_);
                }
            } else {
                beauty = 5;
                faceid = cm.getPlayer().getDressup() ? cm.getPlayer().getSecondFace(): cm.getPlayer().getFace();
                if (faceid > 99999) {
                    //얼굴믹스렌즈로 되있는경우엔 다시 처음으로돌리고나서.
                   faceid = Math.floor(faceid / 1000);
                }
                
                var front = faceid / 1000;
                var current = (Math.floor(front) * 1000) + faceid % 100;
                colors = Array(current, current + 100, current + 200, current + 300, current + 400, current + 500, current + 600, current + 700);
                cm.askAvatar("#h #님이 원하시는 스타일을 골라보세요!", colors, null);
            }
        } else if (selection == 6) {
            cm.dispose();
            cm.openNpc(1012123);
        } else if (selection == 7) { // 믹스 염색
            if (cm.getPlayer().getHair() >= 39600 && cm.getPlayer().getHair() <= 39997) {
                cm.sendOk("#fs11##b스페셜 헤어#k 상태에서는 믹스염색이 불가능하답니다.");
                cm.dispose();
                return;
            }
            st = selection;
            cm.askCustomMixHairAndProb("2가지 색깔을 믹스해 머리색깔을 변경할 수 있어요. 베이스 컬러와 믹스 컬러를 선택하고 스크롤을 움직여 자신만의 색을 만들어 보세요.");
        } else if (selection == 8) { // 성별 전환
            status2 = selection;
            if (cm.getPlayer().getJob() == 10112 || cm.getPlayer().getJob() == 6001 || (cm.getPlayer().getJob() >= 6500 && cm.getPlayer().getJob() <= 6512)) {
                cm.sendNext("#fs11##r현재 직업군은 사용할 수 없는 기능입니다.");
                cm.dispose();
                return;
            }
            말 = "#fs11# #d남자 캐릭터#k로 변경 시,\r\n"
            말 += "#fc0xFF6799FF#[토벤 머리] [멍한 얼굴]#k 이 적용됩니다.\r\n\r\n"
            말 += " #d여자 캐릭터#k로 변경 시,\r\n"
            말 += "#fc0xFF6799FF#[깜찍이 머리] [조용한 눈 얼굴]#k 이 적용됩니다.\r\n\r\n"
            말 += "정말로 #b성별#k을 바꾸시겠어요?";
            cm.sendYesNo(말);
        } else if (selection == 9) { // 헤어
            cm.dispose();
            cm.openNpc(9000217);
        } else if (selection == 10) { // 성형
            cm.dispose();
            cm.openNpc(9000231);
        } else if (selection == 11) { // 스페셜헤어
            cm.dispose();
            cm.openShop(22);
        } else if (selection == 12) { // 스페셜헤어
            cm.dispose();
            cm.openNpc(9062000);
        }

    } else if (status == 2) {
        if (st == 7) {
            cm.sendIllustOk("적용되었습니다.", 3, false);
            cm.dispose();
        } else if (status2 == 1) { // 머리 모양
            page = selection;
            beauty = 2;
            hairnew = Array();
            var idx = 0;
            if (cm.getPlayer().getJob() == 10112) {
                var msg = "현재 다양한 헤어 스타일이 준비되어 있습니다. ";
                msg = "여자 헤어 스타일 페이지를 선택해주시기 바랍니다.\r\n\r\n#b";
                for (var i = 0; i < fhair.length; ++i) {
                    msg += "#L" + i + "#" + i + "페이지 머리 스타일을 선택하겠습니다.#l\r\n";
                }
                cm.sendSimple(msg);
            } else {
                if (cm.getPlayer().getGender() == 0) {
                    for (var i = 0; i < mhair[page].length; ++i) {
                        var hair = mhair[page][i] + Integer.parseInt(cm.getPlayer().getHair() % 10);
                        //                        if (cm.isExistFH(hair)) {
                        hairnew[idx++] = hair;
                        //                        }
                    }
                } else {
                    for (var i = 0; i < fhair[page].length; ++i) {
                        var hair = fhair[page][i] + Integer.parseInt(cm.getPlayer().getHair() % 10);
                        //                        if (cm.isExistFH(hair)) {
                        hairnew[idx++] = hair;
                        //                        }
                    }
                }
                cm.askAvatar("#h #님이 원하시는 스타일을 골라보세요!", hairnew);
            }
        } else if (status2 == 2) { // 얼굴
            page = selection;
            beauty = 1;
            facenew = Array();
            var idx = 0;
            if (cm.getPlayer().getJob() == 10112) {
                var msg = "현재 다양한 성형 스타일이 준비되어 있습니다. ";
                msg = "여자 성형 스타일 페이지를 선택해주시기 바랍니다.\r\n\r\n#b";
                for (var i = 0; i < fface.length; ++i) {
                    msg += "#L" + i + "#" + i + "페이지 성형 스타일을 선택하겠습니다.#l\r\n";
                }
                cm.sendSimple(msg);
            } else {
                if (cm.getPlayer().getGender() == 0) {
                    for (var i = 0; i < mface[page].length; ++i) {
                        var face = mface[page][i] + cm.getPlayer().getFace() % 1000 - (cm.getPlayer().getFace() % 100);
                        //                        if (cm.isExistFH(face)) {
                        facenew[idx++] = face;
                        //                        }
                    }
                } else {
                    for (var i = 0; i < fface[page].length; ++i) {
                        var face = fface[page][i] + cm.getPlayer().getFace() % 1000 - (cm.getPlayer().getFace() % 100);
                        //                        if (cm.isExistFH(face)) {
                        facenew[idx++] = face;
                        //                        }
                    }
                }
                cm.askAvatar("특별히 #h #님을 위해 무료로 외형을 바꿔드릴게요…. 마음에 드시는 것으로 선택하세요.", facenew);
            }
        } else if (status2 == 8) {
            if (cm.getPlayer().getJob() == 10112 || cm.getPlayer().getJob() == 6001 || (cm.getPlayer().getJob() >= 6500 && cm.getPlayer().getJob() <= 6512)) {
                cm.sendNext("#fs11#현재 직업군은 사용할 수 없는 기능입니다.");
                cm.dispose();
                return;
            }
            if (!cm.getPlayer().getGender()) {
                cm.getPlayer().setHair(31002);
                cm.getPlayer().setFace(21078);
            } else {
                cm.getPlayer().setHair(30000);
                cm.getPlayer().setFace(20047);
            }
            cm.getPlayer().setGender(cm.getPlayer().getGender() ^ 1);
            cm.getPlayer().fakeRelog();
            cm.sendOk("#fs11##b성별#k이 정상적으로 변경 되었습니다.");
            cm.dispose();
        } else {
            selection = selection & 0xFF;
            selection2 = selection2 & 0xFF;
            if (cm.getPlayer().getJob() == 10112) {
                if (beauty == 1) {
                    cm.setZeroAvatar(cm.getPlayer().getGender(), skin[selection], skin[selection]);
                } else if (beauty == 3) {
                    cm.setZeroAvatar(cm.getPlayer().getGender(), haircolor[selection], haircolor_[selection2]);
                } else if (beauty == 5) {
                    if (cm.getPlayer().getGender() == 0) {
                        cm.setZeroAvatar(0, colors[selection], colors[selection2]);
                    } else {
                        cm.setZeroAvatar(1, colors_[selection], colors_[selection]);
                    }
                }
            } else {
                if (beauty == 1) {
                    cm.setAvatar(4000000, skin[selection]);
                } else if (beauty == 3) {
                    cm.setAvatar(4000000, haircolor[selection]);
                } else if (beauty == 5) {
                    cm.setAvatar(4000000, colors[selection]);
                }
            }
            cm.dispose();

        }
    } else if (status == 3) {
        if (cm.getPlayer().getJob() == 10112) {
            if (beauty == 2) { // 헤어
                page2 = selection;
                mhairnew = Array();
                fhairnew = Array();
                var idx = 0;
                for (var i = 0; i < mhair[page].length; ++i) {
                    var hair = mhair[page][i] + Integer.parseInt(cm.getPlayer().getHair() % 10);
                    //                    if (cm.isExistFH(hair)) {
                    mhairnew[idx++] = hair;
                    //                    }
                }
                idx = 0;
                for (var i = 0; i < fhair[page2].length; ++i) {
                    var hair = fhair[page2][i] + Integer.parseInt(cm.getPlayer().getSecondHair() % 10);
                    //                    if (cm.isExistFH(hair)) {
                    fhairnew[idx++] = hair;
                    //                    }
                }
                cm.sendStyle("#h #님이 원하시는 스타일을 골라보세요!", mhairnew, fhairnew);
            } else if (beauty == 1) { // 성형
                page2 = selection;
                mfacenew = Array();
                ffacenew = Array();
                var idx = 0;
                for (var i = 0; i < mface[page].length; ++i) {
                    var face = mface[page][i] + cm.getPlayer().getFace() % 1000 - (cm.getPlayer().getFace() % 100);
                    //                    if (cm.isExistFH(face)) {
                    mfacenew[idx++] = face;
                    //                    }
                }
                var idx = 0;
                for (var i = 0; i < fface[page2].length; ++i) {
                    var face = fface[page2][i] + cm.getPlayer().getFace() % 1000 - (cm.getPlayer().getFace() % 100);
                    //                    if (cm.isExistFH(face)) {
                    ffacenew[idx++] = face;
                    //                    }
                }
                cm.sendStyle("#h #님이 원하시는 스타일을 골라보세요!", mfacenew, ffacenew);
            }
        } else {
            selection = selection & 0xFF;
            if (beauty == 2 || beauty == 6) { // 헤어
                cm.setAvatar(4000000, hairnew[selection]);
            } else if (beauty == 1) { // 성형
                cm.setAvatar(4000000, facenew[selection]);
            }
            cm.dispose();
        }
    } else if (status == 4) {
        selection = selection & 0xFF;
        selection2 = selection2 & 0xFF;
        if (beauty == 2 || beauty == 6)
            cm.setZeroAvatar(4000000, mhairnew[selection], fhairnew[selection2]);
        else if (beauty == 1)
            cm.setZeroAvatar(4000000, mfacenew[selection], ffacenew[selection2]);
        cm.dispose();
    }
}