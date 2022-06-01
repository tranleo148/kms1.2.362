importPackage(Packages.tools.packet);
importPackage(java.lang);
importPackage(java.util);
importPackage(java.awt);
importPackage(Packages.server);

var status = -1;

function start(mode, type, selection) {
    if (mode == -1) {
        qm.dispose();
        return;
    }
    if (mode == 0) {
        if (status == 11) {
            qm.sendOkS("꽃이 지기 전에 꼭 가봤으면 좋겠는데... \r\n언제든 마음이 바뀌면 말해줘!\r\n\r\n\r\n#b#e※ 이벤트 기간\r\n - 2021년 6월 16일 23시 59분까지#n", 4, 9010010);
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
        qm.sendNextS("#e#b#h0##k#n! 오랜만이야!\r\n이번에도 내가 #r#e엄청난 소식#n#k을 들고 왔어!", 4, 9010010);
    } else if (status == 1) {
        qm.sendNextPrevS("#r#e엄청난 소식#n#k이요?", 2);
    } else if (status == 2) {
        qm.sendNextPrevS("혹시...#r#e에르다#n#k에 대해 들어봤어?\r\n\r\n#b#L0# 에르다? 처음 들어요. #l\r\n#L1# 들어봤어요!#k#l", 4, 9010010);
    } else if (status == 3) {
        if (selection == 0) {
            qm.sendNextS("#r#e에르다#n#k는 바로 이 #b#e세계를 구성하는 에너지#n#k야!\r\n에르다는 탄생과 소멸을 반복하며 이 세계를 구성하지!\r\n\r\n어때? 정말 멋지지?", 4, 9010010);
        } else {
            qm.sendNextS("역시 #e#b#h0##k#n! \r\n너처럼 대단한 용사는 #r#e에르다#n#k에 대해 알 줄 알았어!", 4, 9010010);
        }
    } else if (status == 4) {
        qm.sendNextPrevS("그런데... #r#e에르다#n#k는 왜요?", 2);
    } else if (status == 5) {
        qm.sendNextPrevS("모든 생명과 물질은 에르다로 구성되어 있잖아?\r\n그래서 #r#e세계의 강한 염원#n#k에 에르다가 반응한대!\r\n\r\n그런데 최근에 에르다에서 #b#e이상한 모습#n#k이 발견된 거야!", 4, 9010010);
    } else if (status == 6) {
        qm.sendNextPrevS("#r#e밝고 따스한 빛#n#k이 생겨나는 것이... \r\n\r\n꼭 무언가를 #fs16##b#e축하하는 모습#n#k#fs12#이라지 뭐야?", 4, 9010010);
    } else if (status == 7) {
        qm.sendNextPrevS("이 시기에 #b#e축하#n#k할 일이 뭐겠어!?\r\n\r\n바로 #r#e#fs20#메이플스토리의 18번째 생일!", 4, 9010010);
    } else if (status == 8) {
        qm.sendNextPrevS("여기서 끝이 아니야!\r\n#r#e에르다의 빛#n#k으로 #b#e정령의 숲 아르카나#n#k에 특별한 변화가 찾아왔다는 거야!", 4, 9010010);
    } else if (status == 9) {
        qm.sendNextPrevS("#b#e아르카나 깊은 숲속#n#k에 마치 봄이 온 것처럼 #r#e아름다운 꽃#n#k이 잔뜩 피어났다고 해!", 4, 9010010);
    } else if (status == 10) {
        qm.sendNextPrevS("그래서 #b#e아르카나#n#k의 정령들이 #e꽃이 피는 숲#n, \r\n#r#e<블루밍 포레스트>#n#k에 사람들을 초대하고 있어! ", 4, 9010010);
    } else if (status == 11) {
        qm.sendYesNoS("어때? 지금 바로 #r#e<블루밍 포레스트>#n#k에 가볼래?\r\n가서 메이플스토리의 생일도 축하하고 꽃의 축복을 받아봐!\r\n\r\n#b※ 수락 시 이벤트 맵으로 즉시 이동합니다. ", 4, 9010010);
    } else if (status == 12) {
        if (qm.getClient().getKeyValue("BloomingTuto") != null) {
            qm.getPlayer().setKeyValue(100790, "lv", "1");
            qm.getPlayer().setKeyValue(100794, "point", "0");
            qm.getPlayer().setKeyValue(100794, "sum", "0");
            qm.getPlayer().setKeyValue(100794, "today", "0");
            qm.getPlayer().setKeyValue(501378, "sp", "0");

            if (qm.getClient().getKeyValue("BloomingSkill") != null) {
                var str = qm.getClient().getKeyValue("BloomingSkill");
                var ab = str.split("");
                var skillid = 80003036;
                for (var a = 0; a < ab.length; a++) {
                    qm.getPlayer().setKeyValue(501378, a+"", ab[a]+"");
                    if (parseInt(ab[a]) > 0) {
                        qm.getPlayer().changeSkillLevel(skillid + a, parseInt(ab[a]), 3);
                    }
                }
            } else {
                for (var a = 0; a < 10; a++) {
                    qm.getPlayer().setKeyValue(501378, a+"", "0");
                }
            }
            qm.getPlayer().setKeyValue(501387, "flower", "0");
            qm.getPlayer().setKeyValue(501387, "mapTuto", "4");
            qm.getPlayer().setKeyValue(501367, "start", "1");
            if (qm.getClient().getKeyValue("Bloomingbloom") != null) {
                qm.getPlayer().setKeyValue(501367, "bloom", qm.getClient().getKeyValue("Bloomingbloom"));
            } else {
                qm.getPlayer().setKeyValue(501367, "bloom", "0");
            }
            if (qm.getClient().getKeyValue("BloominggiveSun") != null) {
                qm.getPlayer().setKeyValue(501367, "giveSun", qm.getClient().getKeyValue("BloominggiveSun"));
            } else {
                qm.getPlayer().setKeyValue(501367, "giveSun", "0");
            }
            if (qm.getClient().getKeyValue("BloomingSkillPoint") != null) {
                qm.getPlayer().setKeyValue(501378, "sp", qm.getClient().getKeyValue("BloomingSkillPoint"));
            } else {
                qm.getPlayer().setKeyValue(501378, "sp", "0");
            }
            if (qm.getClient().getKeyValue("BloomingReward") != null) {
                qm.getPlayer().setKeyValue(501367, "reward", qm.getClient().getKeyValue("BloomingReward"));
            } else {
                qm.getPlayer().setKeyValue(501367, "reward", "10");
            }
            if (qm.getClient().getKeyValue("week") != null) {
                qm.getPlayer().setKeyValue(501367, "week", qm.getClient().getKeyValue("week"));
            } else {
                qm.getPlayer().setKeyValue(501367, "week", "1");
            }
            if (qm.getClient().getKeyValue("getReward") != null) {
                qm.getPlayer().setKeyValue(501367, "getReward", qm.getClient().getKeyValue("getReward"));
            } else {
                qm.getPlayer().setKeyValue(501367, "getReward", "000000000000000000");
            }

            qm.getPlayer().changeSkillLevel(80003035, 1, 1);
            qm.forceCompleteQuest(100790);
            if (parseInt(qm.getClient().getKeyValue("BloomingTuto")) > 1) {
                qm.getPlayer().setKeyValue(501387, "flower", "1");
                qm.forceCompleteQuest(501394);
            }
            if (parseInt(qm.getClient().getKeyValue("BloomingTuto")) > 2) {
                qm.forceCompleteQuest(501375);
                qm.getPlayer().setKeyValue(501375, "start", "1");
                qm.getPlayer().setKeyValue(501387, "flower", "2");
            }
            if (parseInt(qm.getClient().getKeyValue("BloomingTuto")) > 3) {
                qm.forceCompleteQuest(501376);
                qm.getPlayer().setKeyValue(501376, "start", "1");
                qm.getPlayer().setKeyValue(501387, "flower", "5");
            }
            qm.warp(993192000);
            qm.dispose();
        } else {
            qm.warp(993192001);
            qm.getClient().send(SLFCGPacket.SetIngameDirectionMode(true, false, false, false));
            qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 15));
            qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 10, 1));
            qm.getClient().send(SLFCGPacket.MakeBlind(1, 255, 0, 0, 0, 0, 0));
            qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 7, 0, 2500, 0, 0, 0));
            qm.getClient().send(CField.UIPacket.getDirectionStatus(true));
            statusplus(1500);
        }
    } else if (status == 13) {
        qm.sendNextS("이곳은...", 17);
    } else if (status == 14) {
        qm.sendNextPrevS("들리나요... 들리나요?", 5, 1540940);
    } else if (status == 15) {
        qm.sendNextPrevS("이 목소리는...?", 17);
    } else if (status == 16) {
        qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 7, 8000, 1000, 8000, 0, 0));
        qm.getClient().send(SLFCGPacket.MakeBlind(0, 0, 0, 0, 0, 6000, 0));
        statusplus(1000);
    } else if (status == 17) {
        qm.getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/blackHeaven/toair"));
        statusplus(5400);
    } else if (status == 18) {
        qm.sendNextS("아, 다행이에요. 저희의 목소리가 들리는군요.", 5, 1540940);
    } else if (status == 19) {
        qm.sendNextPrevS("저희는 당신들이 #b#e<에르다>#n#k라고 부르는 존재들…\r\n우리는 이 #r#e세계를 구성하고 있는 에너지#n#k예요.", 5, 1540940);
    } else if (status == 20) {
        qm.sendNextPrevS("세계를 구성하는 존재들의 #r#e마음이 모여#n#k 저희에게 전달되면 우리는 #b#e흐름에 따라 반응#n#k하게 되어 있어요.", 5, 1540940);
    } else if (status == 21) {
        qm.sendNextPrevS("지금도 이 세계의 #r#e강렬한 마음#n#k이 느껴져요! \r\n\r\n긴 시간을 함께한 #b#e용사님들에 대한 감사함#n#k과\r\n#b#e메이플스토리의 생일을 축하#n#k하는 마음이요!", 5, 1540940);
    } else if (status == 22) {
        qm.sendNextPrevS("모두의 마음이 #r#e태양처럼 밝은 빛#n#k으로 나타나고 있어요!", 5, 1540940);
    } else if (status == 23) {
        qm.sendNextPrevS("이제 탄생한 빛이 비치는 곳으로 보내드릴게요.\r\n\r\n따듯한 빛 아래에서 #b#e봄의 축복#n#k을 받고,\r\n#r#e메이플스토리의 생일#n#k도 함께 축하해 주세요!", 5, 1540940);
    } else if (status == 24) {
        qm.getClient().send(SLFCGPacket.MakeBlind(1, 255, 240, 240, 240, 1300, 0));
        statusplus(1600);
    } else if (status == 25) {
        qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 5, 1, 0, 0));
        qm.getClient().send(SLFCGPacket.SetIngameDirectionMode(false, false, false, false));
        qm.dispose();
        qm.warp(993192002);
    }
}
function statusplus(millsecond) {
    qm.getClient().send(SLFCGPacket.InGameDirectionEvent("", 0x01, millsecond));
}