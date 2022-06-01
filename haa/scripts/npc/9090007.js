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
 * NPCID : 9201014
 * ScriptName : divorce
 * NPCNameFunc : 필라 - 이혼 상담
 * Location : 100000000 ( - )
 * 
 * @author T-Sun
 *
 */

importPackage(java.lang);

var status = -1;
var task = -1;
function action(mode, type, selection) {
    if (mode == 1 && type != 1) {
        status++;
    } else {
        if (type == 1 && mode == 1) {
            status++;
            selection = 1;
        } else if (type == 1 && mode == 0) {
            status++;
            selection = 0;
        } else {
            cm.dispose();
            return;
        }
    }
    if (status == 0) {
        var str = "얼굴에 근심이 가득해 보이는군요. 저에게 상담하시고 싶은 일이 있으신가요?";
        var data = cm.getMarriageData();
        if (data == null || data.getStatus() < 2) {
            cm.sendOk(str);
            cm.dispose();
            return;
        }
        str += "\r\n#b #L0#이혼 상담을 하러 왔습니다.#l";
        if (data.getGroomId() == cm.getPlayer().getId()) {
            time = data.getDivorceTimeGroom();
        } else if (data.getBrideId() == cm.getPlayer().getId()) {
            time = data.getDivorceTimeBride();
        }
        if (time > 0) {
            str += "\r\n #L1#이혼 신청을 취소하러 왔습니다.#l";
        }
        cm.sendSimple(str);
    } else if (status == 1) {
        data = cm.getMarriageData();
        if (selection == 0) {
            if (time <= 0) {
                task = 0;
                cm.sendNext("이런.. 안타깝군요. 정말 이것이 최선인지 생각은 해 보셨나요? 후우.. 당신들을 처음 봤을때 부터 예감이 좋진 않았지만.. 정말 슬픈 일이군요.");
            } else {
                if (time + 86400 * 1000 * 3 > System.currentTimeMillis()) {
                    cm.sendOk("아직 이혼 신청을 접수하신지 72시간이 경과하지 않으신 것 같군요. 이혼은 정말 신중하게 생각하셔야 합니다. 만약 이혼 신청을 취소하시고 싶으시다면 제게 다시 말을 걸어주세요.");
                    cm.dispose();
                } else {
                    cm.sendNext("이혼 신청을 접수하신지 72시간이 지나셨군요. 이혼을 진행하실 수 있겠군요.");
                    task = 1;
                }
            }
        } else if (selection == 1) {
            cm.sendYesNo("이혼 신청을 취소하러 오셨군요. 잘 생각하셨어요. 지금 취소하시면 이혼 접수는 취소됩니다.");
            task = 2;
        }
    }
    if (task == 0) {
        if (status == 2) {
            cm.sendYesNo("이혼을 하고 싶다면, 3일 간의 유예 기간을 드리고, 그때까지 마음이 변하지 않으신다면 이혼을 하실 수 있습니다. 부디 마음을 바꾸고 돌아오시길 바랍니다. 3일 내에 이혼 신청은 언제든지 취소할 수 있습니다.");
        } else if (status == 3) {
            data = cm.getMarriageData();
            if (selection == -1) {
                if (data.getGroomId() == cm.getPlayer().getId()) {
                    data.setDivorceTimeGroom(System.currentTimeMillis());
                } else if (data.getBrideId() == cm.getPlayer().getId()) {
                    data.setDivorceTimeBride(System.currentTimeMillis());
                }
                cm.sendOk("이혼 신청이 접수되었습니다. 72시간 후 제게 다시 찾아오셔서 이혼을 진행하실 수 있습니다.");
                cm.dispose();
            } else {
                cm.sendOk("이혼은 신중하게 생각해주세요.");
                cm.dispose();
            }
        }
    } else if (task == 1) {
        if (status == 2) {
            cm.sendYesNo("다시 한번 묻겠습니다.. 정말 이혼을 진행하시겠습니까?");
        } else if (status == 3) {
            if (selection == -1) {
                Packages.server.marriage.MarriageManager.getInstance().deleteMarriage(cm.getPlayer().getMarriageId());
                cm.dispose();
            } else {
                cm.sendOk("이혼은 신중하게 생각해주세요.");
                cm.dispose();
            }
        }
    } else if (task == 2) {
        if (status == 2) {
            data = cm.getMarriageData();
            if (selection == -1) {
                if (data.getGroomId() == cm.getPlayer().getId()) {
                    data.setDivorceTimeGroom(0);
                } else if (data.getBrideId() == cm.getPlayer().getId()) {
                    data.setDivorceTimeBride(0);
                }
                cm.sendOk("이혼 신청이 취소되었습니다.");
                cm.dispose();
            } else {
                cm.sendOk("음. 이혼 신청을 취소하러 오신줄 알았습니다만.");
                cm.dispose();
            }
        }
    }
}