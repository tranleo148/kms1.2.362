/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 ~ 2010 Patrick Huy <patrick.huy@frz.cc> 
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License version 3
 as published by the Free Software Foundation. You may not use, modify
 or distribute this program under any other version of the
 GNU Affero General Public License.
 
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.
 
 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * NPCID : 9201002
 * ScriptName : HighPriest
 * NPCNameFunc : 르베르토4세 - 신부님
 * Location : 100000000 ( - )
 * Location : 680000210 ( - )
 * 
 * @author T-Sun
 *
 */

var status = -1;
function action(mode, type, selection) {
    if (mode == 1 && type != 1 && type != 11) {
        status++;
    } else {
        if ((type == 1 || type == 11) && mode == 1) {
            status++;
            selection = 1;
        } else if ((type == 1 || type == 11) && mode == 0) {
            status++;
            selection = 0;
        } else {
            qm.dispose();
            return;
        }
    }
    if (status == 0) {
        if (cm.getPlayer().getMapId() == 680000210) {
            cm.sendOk("험..");
            cm.dispose();
            return;
        }
        var marrydata = cm.getMarriageData(cm.getPlayer().getMarriageId());
        task = 0;
        if (marrydata != null) {
            if (marrydata.getWeddingStatus() == 7) {
                if (marrydata.getBrideName() == cm.getPlayer().getName()) {
                    qr = cm.getQuestRecord(130010);
                    if (qr.getCustomData() == null) {
                        qr.setCustomData("");
                    }
                    if (qr.getCustomData().equals("ing")) {
                        if (cm.haveItem(4213000, 1)) {
                            task = 1;
                            cm.sendNext("서약서를 가져왔군. 수고했네, 자 여기 서약서를 줄테니 클라랜스 수녀님께 가봐요.\r\n결혼식장에서 보세.")
                        } else {
                            cm.sendOk("아직 게리와 샤티마를 찾지 못한겐가? 게리와 샤티마를 찾아가서 #b사랑의 서약#k을 받아오게나.");
                            cm.dispose();
                        }
                    } else if (qr.getCustomData().equals("end")) {
                        if (!cm.haveItem(4213000, 1)) {
                            qr.setCustomData("");
                        }
                        cm.sendOk("험험, 내 주례를 기대하게나.");
                        cm.dispose();
                    } else {
                        cm.askAcceptDecline("행복한 결혼식을 앞둔 신부로군. 주례가 필요해서 날 찾아온건가? 암암. 귀여운 커플에게는 당연히 주례를 해줘야지. 그 전에 게리와 샤티마를 찾아가서 사랑의 서약서를 받아오는 것이 좋겠군요.");
                    }
                } else {
                    cm.sendOk("신부가 결혼 주례를 신청하게나.");
                    cm.dispose();
                }
            } else {
                cm.sendOk("험, 주례를 받고 싶은건가? 그 전에 결혼식 예약이 먼저인 듯 하구만.");
                cm.dispose();
            }
        } else {
            cm.sendOk("나는 이 성당의 신부라네.");
            cm.dispose();
        }
    } else if (status == 1) {
        if (task == 1) {
            cm.gainItem(4213001, 1);
            cm.gainItem(4213000, -1);
            qr.setCustomData("end");
            cm.dispose();
            return;
        }
        if (selection == -1) {
            if (qr.getCustomData().isEmpty()) {
                qr.setCustomData("ing");
            }
            cm.sendOk("게리와 샤티마는 이 근처 어디에 있을텐데...잘 찾아보게.");
            cm.dispose();
        } else {
            cm.sendOk("흐음, 너무 서두르지 말고 천천히 결정하게, 준비가 되면 다시 찾아오도록 하게.");
            cm.dispose();
        }
    }
}