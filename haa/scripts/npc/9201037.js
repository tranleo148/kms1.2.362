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
 * NPCID : 9201037
 * ScriptName : loveOath
 * NPCNameFunc : 게리와 샤티마
 * Location : 100000000 ( - )
 * 
 * @author T-Sun
 *
 */

var status = -1;
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
        qr = cm.getQuestRecord(130010);
        if (qr.getCustomData() == null) {
            qr.setCustomData("");
        }
        if (qr.getCustomData().equals("ing")) {
            cm.sendYesNo("\r\n당신이 사랑의 서약을 해주신다면 그 서약을 물건에 담아드리겠어요.\r\n\r\n서약의 마음을 가다듬고 오세요.");
        } else {
            cm.sendOk("진실한 사랑.. 우리 이쁘죠?");
            cm.dispose();
        }
    } else if (status == 1) {
        if (selection == -1) {
            cm.sendGetText("지금 이 자리에서 우리를 따라 맹세하세요.\r\n\r\n#b나의 사랑을 맹세합니다#k");
        } else {
            cm.sendOk("아직 서약의 마음을 가다듬지 못하셨나요? 준비가 되면 다시 말을 거세요.");
            cm.dispose();
        }
    } else if (status == 2) {
        var text = cm.getText();
        if (text.equals("나의 사랑을 맹세합니다")) {
            cm.sendGetText("\r\n#b진실한 마음으로 영원히 사랑하겠습니다#k");
        } else {
            cm.sendOk("서약이 틀렸어요. 제대로 마음을 가다듬고 다시 찾아오세요.");
            cm.dispose();
        }
    } else if (status == 3) {
        var text = cm.getText();
        if (text.equals("진실한 마음으로 영원히 사랑하겠습니다")) {
            cm.sendNext("잘 했어요. 당신의 서약을 이 곳에 담았답니다. 가져가세요. 그리고 그 서약을 절대 잊지마세요.");
        } else {
            cm.sendOk("서약이 틀렸어요. 제대로 마음을 가다듬고 다시 찾아오세요.");
            cm.dispose();
        }
    } else if (status == 4) {
        if (cm.canHold(4213000, 1)) {
            //cm.showQuestCompleteEffect();
            cm.gainItem(4213000, 1);
            cm.dispose();
        } else {
            cm.sendOk("기타 인벤토리 슬롯을 한칸 비우신 후 다시 찾아오세요.");
            cm.dispose();
        }
    }
}