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
 * NPCID : 9201036
 * ScriptName : presentExchange
 * NPCNameFunc : 안젤리크 - 결혼선물 관리인
 * Location : 100000000 ( - )
 * Location : 680000200 ( - )
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
    if (cm.getPlayer().getMapId() == 680000200) {
        if (status == 0) {
            var agent = cm.getMarriageAgent();
            var entry = agent.getDataEntry();
            if (entry != null && agent.getDataEntry() != null) {
                if (entry.getMarriageId() == agent.getDataEntry().getMarriageId() && entry.getGroomId() == cm.getPlayer().getId() || entry.getBrideId() == cm.getPlayer().getId()) {
                    cm.sendOk("결혼을 진심으로 축하드려요~ 하객 분들은 저를 통해 결혼 선물을 여러분들께 드릴 수 있답니다. 결혼을 마치고 나가는 길에 저를 통해 선물을 수령하실 수 있으니 걱정 마세요~");
                    cm.dispose();
                } else {
                    cm.sendSimple("어서오세요. 신랑 신부에게 주고 싶은 선물은 제가 대신 관리하고 있어요. 어느쪽 하객분이신가요?\r\n #L0#신랑에게 선물을 주고 싶습니다.#l\r\n #L1#신부에게 선물을 주고 싶습니다.#l");
                }
            } else {
                cm.sendOk("음.. 무언가 오류가 생겼군요.");
                cm.dispose();
            }
        } else if (status == 1) {
            var entry = cm.getMarriageAgent().getDataEntry();
		cm.getPlayer().setWeddingGive(selection);
            if (selection == 0) {
                cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CWvsContext.showWeddingWishGiveDialog(entry.getGroomWishList()));
		cm.getClient().getSession().writeAndFlush(showWeddingWishGiveToServerResult(entry.getGroomWishList(), Packages.client.inventory.MapleInventoryType.getByType(1), entry.getGroomPresentList()));
            } else {
		cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CWvsContext.showWeddingWishGiveDialog(entry.getBrideWishList()));
		cm.getClient().getSession().writeAndFlush(showWeddingWishGiveToServerResult(entry.getBrideWishList(), Packages.client.inventory.MapleInventoryType.getByType(1), entry.getBridePresentList()));
            }
            //cm.openWeddingPresent(1, selection);
            cm.dispose();
        }
    } else {
        var entry = cm.getMarriageData();
        if (entry != null) {
            if (entry.getStatus() >= 2 && entry.getWeddingStatus() >= 8) {
                var gender = cm.getPlayerStat("GENDER");
                var gifts;
                if (entry.getGroomName() == cm.getPlayer().getName()) {
                    gifts = entry.getGroomPresentList();
                } else {
                    gifts = entry.getBridePresentList();
                }
                if (gifts.isEmpty()) {
                    cm.sendOk("받으실 수 있는 선물이 없군요.");
                } else {
                    cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CWvsContext.showWeddingWishRecvDialog(gifts));
                }
                cm.dispose();
            } else {
                cm.sendOk("선물을 받으실 수 있는 상태가 아닌 것 같군요.");
                cm.dispose();
            }
        } else {
            cm.sendOk("저는 결혼 선물을 대신 관리하고 있답니다.");
            cm.dispose();
        }
    }
}