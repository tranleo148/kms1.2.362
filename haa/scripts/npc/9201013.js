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
 * NPCID : 9201013
 * ScriptName : cathedralCoordinator
 * NPCNameFunc : 마가렛 수녀님 - 결혼식장 예약 도우미
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
        wishlist = 0;
        marrydata = cm.getMarriageData(cm.getPlayer().getMarriageId());
        if (marrydata != null) {
            if (marrydata.getWeddingStatus() >= 1 && marrydata.getWeddingStatus() < 7) {
                // 위시리스트 작성
                cm.sendNext("결혼식 예약접수를 완료하기 전에 하객들로부터 결혼선물로 받고 싶은 아이템 리스트를 작성하실 수 있습니다. 결혼 선물은 결혼식이 끝난 후, #b안젤리크#k양을 찾아가면 받으실 수 있을거에요.");
                wishlist = 1;
                return;
            } else if (marrydata.getStatus() == 2) {
                cm.sendNext("이미 결혼을 하신 커플이군요. 딱히 결혼식을 다시 예약하실 필요는 없습니다.");
                cm.dispose();
                return;
            } else if (marrydata.getWeddingStatus() >= 7) {
                cm.sendNext("이미 결혼식이 예약되어 있습니다.");
                cm.dispose();
                return;
            } else if (marrydata.getStatus() == 1 && marrydata.getWeddingStatus() == 0) {
                cm.sendNext("성당 결혼식을 예약하기 전에, 기타창을 3개이상 비우세요.");
            } else {
                cm.sendOk("예약할 필요는 없어 보입니다.");
                cm.dispose();
            }
        } else {
            cm.sendNext("성당 결혼식을 예약하려면 먼저 약혼을 해야합니다.");
            cm.dispose();
            return;
        }
    } else if (status == 1) {
        if (wishlist == 1) {
            if (cm.getInvSlots(4) >= 3) {
                marrydata = cm.getMarriageData(cm.getPlayer().getMarriageId());
                if (marrydata.getGroomName() == cm.getPlayer().getName()) {
                    if ((marrydata.getWeddingStatus() & 2) > 0) {
                        cm.sendOk("이미 위시리스트를 등록하셨습니다. 여성분께서 위시리스트 작성을 끝낼 때 까지 기다려 주세요.");
                        cm.dispose();
                    } else {
                        cm.sendWeddingWishListInputDlg();
                        cm.dispose();
                    }
                } else if (marrydata.getBrideName() == cm.getPlayer().getName()) {
                    if ((marrydata.getWeddingStatus() & 4) > 0) {
                        cm.sendOk("이미 위시리스트를 등록하셨습니다. 남성분께서 위시리스트 작성을 끝낼 때 까지 기다려 주세요.");
                        cm.dispose();
                    } else {
                        cm.sendWeddingWishListInputDlg();
                        cm.dispose();
                    }
                }
            } else {
                cm.sendOk("기타창을 3칸 비우신 후 위시리스트를 등록하실 수 있습니다.");
                cm.dispose();
            }
        } else {
            cm.sendSimple("우선, 오늘 당신 정말 멋지군요! 저는 결혼식 준비를 도와드리러 왔습니다. 저는 예약과 초대를 도와드리거나 결혼에 필요한 것들에 대해 알려드린답니다. 무엇을 도와드릴까요?\r\n#b#L0#여기서 어떻게 결혼하죠?#l\r\n#L1#프리미엄 예약을 하고 싶어요.#l\r\n#L2#스위티 예약을 하고 싶어요.#l\r\n#L3#조촐한 예약을 하고 싶어요.#l");
        }
    } else if (status == 2) {
        if (selection == 0) {
            cm.sendOk("To get married in the Cathedral, you'll need #ra Cathedral Wedding Ticket, any Engagement Ring or an Empty Engagement Ring Box and most of all, love#k. Soon as you have them, we'll be happy to assist with your Wedding plans! If you reserved the Cathedral don't forget to see High Priest John for the Officiator's permission.");
            cm.dispose();
        } else if (selection >= 1) {
            if (true) {
                if (cm.getPlayer().getParty() == null) {
                    cm.sendOk("#b신랑과 신부#k가 될 두 사람이 파티를 하고 있어야 결혼식 예약이 가능합니다.");
                    cm.dispose();
                } else {
                    var result = cm.checkWeddingReservation();
                    if (result == 1 || result == 2) {
                        cm.sendOk("#b신랑과 신부#k가 될 두 사람이 파티를 하고 있어야 결혼식 예약이 가능합니다.");
                        cm.dispose();
                    } else if (result == 4) {
                        cm.sendOk("#b신랑과 신부#k가 될 두 사람은 현재 맵에 함께 있어야 결혼식 예약이 가능합니다.");
                        cm.dispose();
                    } else if (result == 3) {
                        cm.sendOk("아직 약혼을 하지 않으신 것 같군요.");
                        cm.dispose();
                    } else if (result == 5) {
                        cm.sendOk("#b신랑과 신부#k가 될 두 사람만 파티를 하고 있어야 결혼식 예약이 가능합니다.");
                        cm.dispose();
                    } else if (result == 6) {
                        cm.sendOk("이미 두 사람은 결혼을 마친 상태인 것 같군요.");
                        cm.dispose();
                    } else if (result == 7) {
                        cm.sendOk("약혼은 이미 깨진 것 같군요.");
                        cm.dispose();
                    } else if (result == 8) {
                        cm.sendOk("이미 결혼식 예약이 되었습니다.");
                        cm.dispose();
                    } else if (result != 0) {
                        cm.sendOk("알 수 없는 오류군요. 지금은 결혼식 예약을 할 수 없겠습니다.");
                        cm.dispose();
                    } else {
                        selectedItem = selection;
                        cm.sendNext("약혼했군요. 그럼 지금부터 예약을 하죠.");
                    }
                }
            } else {
                cm.sendOk("기타 인벤토리 창을 3칸 이상 비워주세요.");
                cm.dispose();
            }
        }
    } else if (status == 3) {
        cm.sendNext("예약접수를 완료하기 전에 하객들로부터 결혼선물로 받고 싶은 아이템 리스트를 작성하실 수 있습니다. 결혼 선물은 결혼식이 끝난 후, #b안젤리크#k양을 찾아가면 받으실 수 있을거에요.");
    } else if (status == 4) {
        var ret = 0;
        var itemid = 0;
        if (selectedItem == 1) {
            itemid = 5251006;
        } else if (selectedItem == 2) {
            itemid = 5251005;
        } else if (selectedItem == 3) {
            itemid = 5251004;
        }
        if (!cm.haveItem(itemid, 1)) {
            cm.sendOk("결혼식 예약에 필요한 티켓은 제대로 갖고 계십니까? 다시 확인해주세요.");
            cm.dispose();
            return;
        }
        ret = cm.makeWeddingReservation(itemid);
        if (ret > 0) {
            cm.sendOk("결혼식 예약에 문제가 있군요. 처음부터 다시 시도해주세요.");
        } else {
            cm.gainItem(itemid, -1);
        }
        cm.partyMessage(5, "청첩장은 결혼식을 시작하기 전 나눠줄 수 있으니, 하객들을 모으고 하객들이 입장준비를 마쳤을때 클라랜스 수녀님을 통해 결혼식을 시작해주세요.");
        cm.dispose();
    }
}