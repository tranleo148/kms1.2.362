var status = 0;

importPackage(Packages.constants);


function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
if (cm.getPlayer().getLevel() >= 100) {
	if (cm.getPlayer().getMapId() == 993000750) {
		var jessica = "#fn나눔고딕 Extrabold#전국에서 입소문난 설귀도 낚시터!\r\n이 곳이 그렇게 월척이 잘 잡히기로 소문이 자자하지..\r\n";
		jessica += "#L0##d낚시에 대한 설명#k\r\n";
		jessica += "#L1##b낚시 용품 구입";
		//jessica += "#L2#물고기 교환#k\r\n";
		jessica += "#L3##r광장 이동#k";
		cm.sendSimple(jessica);
	} else {
	cm.sendOk("#fn나눔고딕 Extrabold#세월을 낚는 재미란.. 즐겨본자만이 아는법이지..");
	cm.dispose();
	}
} else {
cm.sendOk("#fn나눔고딕 Extrabold##r낚시 이용은 레벨 100 이상만 이용 가능합니다.",9062004);
cm.dispose();
}
        } else if (status == 1) {
	if (selection == 0) {
		cm.sendOk("#fn나눔고딕 Extrabold#나에게 낚시 용품을 구입한 후 좋은 자리를 선점하게나..\r\n미끼를 소지 후 의자에 앉아 있으면 낚시가 진행된다네..\r\n만약 물고기를 많이 모아 오면 좋은 것으로 교환해 주겠다네..");
		cm.dispose();

	} else if (selection == 1) {
		var jessica2 = "#fn나눔고딕 Extrabold##b원하시는 품목을 선택 해보게나..#k\r\n#r인벤토리가 꽉차면 못 받을 수 있으니 주의 바란다네..#k\r\n";
		jessica2 += "#L0##i3015394# #r낚시 의자#k #d(5.000.000)#k\r\n";
		jessica2 += "#L1##i4035000# #r미끼 구입 50 개#k #d(10.000.000)#k\r\n";
		jessica2 += "#L2##i4035000# #r미끼 구입 500 개#k #d(100.000.000)#k";
		cm.sendSimple(jessica2);
	} else if (selection == 3) {
		cm.dispose();
		cm.warp(100000000,0);
                cm.sendOk("#fn나눔고딕 Extrabold##d다음에 또 여유를 즐기러 오시게.. 젊은이..#k");
	} else if (selection == 2) {
		var jessica3 = "#fn나눔고딕 Extrabold##b골라! 골라! 잽싸게 골라버려!#k\r\n#r인벤토리가 꽉차면 못 받을 수 있으니 주의 바란다네..#k\r\n";
		jessica3 += "\r\n-------------------------------------------------------------------------------\r\n";
		jessica3 += "#L3##r[S]#k #i1142949# #b#t1142949##k#l\r\n            #d올스탯 600 공, 마 300#k #r[ #i4001187# #i4001188# #i4001189# 5000 개 ]#k\r\n";
                jessica3 += "#L4##r[S]#k #i2430218# #b#t2430218##k#l\r\n            #d소비 아이템#k #r[ #i4001187# #i4001188# #i4001189# 500 개 ]#k\r\n";
		jessica3 += "#L5##r[S]#k #i4001861# #d1 천 5 백만 메소#k #r[ #i4001187# #i4001188# #i4001189# 50개 ]#k#l\r\n";
		jessica3 += "#L6##r[S]#k #i4001861# #d1 억 5 천만 메소#k #r[ #i4001187# #i4001188# #i4001189# 500개 ]#k#l\r\n";
		jessica3 += "#L7##r[S]#k #i4001861# #d15 억원 메소#k #r[ #i4001187# #i4001188# #i4001189# 5000개 ]#k#l\r\n";
		jessica3 += "\r\n-------------------------------------------------------------------------------\r\n";
		jessica3 += "#L8##i1012329# #b올스탯 300 공, 마 300#k #r[ #i4001187# #i4001188# #i4001189# 1000 개 ]#k#l\r\n";
		jessica3 += "\r\n-------------------------------------------------------------------------------\r\n";
		jessica3 += "#L9##i1672004# #b올스탯 300 공, 마 300#k #r[ #i4001187# #i4001188# #i4001189# 800 개 ]#k#l\r\n";
		jessica3 += "#L10##i1662002# #b올스탯 300 공, 마 300#k #r[ #i4001187# #i4001188# #i4001189# 800 개 ]#k#l\r\n";
		jessica3 += "#L11##i1662003# #b올스탯 300 공, 마 300#k #r[ #i4001187# #i4001188# #i4001189# 800 개 ]#k#l\r\n";
		jessica3 += "\r\n-------------------------------------------------------------------------------\r\n"
		jessica3 += "#L12##i1102369# #b올스탯 200 공, 마 200#k #r[ #i4001187# #i4001188# #i4001189# 500 개 ]#k#l\r\n";
		jessica3 += "#L13##i1152090# #b올스탯 200 공, 마 200#k #r[ #i4001187# #i4001188# #i4001189# 500 개 ]#k#l\r\n";
		jessica3 += "\r\n-------------------------------------------------------------------------------\r\n"
		jessica3 += "#L100##i4001187# #b음치#k #r500 개#k → #i4001188# #b몸치#k #r250 개#k#l\r\n";
		jessica3 += "#L101##i4001187# #b음치#k #r500 개#k → #i4001189# #b박치#k #r250 개#k#l\r\n";
		jessica3 += "#L102##i4001188# #b몸치#k #r500 개#k → #i4001187# #b음치#k #r250 개#k#l\r\n";
		jessica3 += "#L103##i4001188# #b몸치#k #r500 개#k → #i4001189# #b박치#k #r250 개#k#l\r\n";
		jessica3 += "#L104##i4001189# #b박치#k #r500 개#k → #i4001187# #b음치#k #r250 개#k#l\r\n";
		jessica3 += "#L105##i4001189# #b박치#k #r500 개#k → #i4001188# #b몸치#k #r250 개#k#l\r\n";
		jessica3 += "\r\n-------------------------------------------------------------------------------\r\n";
		cm.sendSimple(jessica3);
		}

        } else if (status == 2) {

	if (selection == 0) {
		if (cm.getMeso() >= 5000000) {
		if (cm.canHold(3015394)) {
		cm.gainItem(3015394, 1);
		cm.gainMeso(-5000000);
		cm.sendOk(" #fn나눔고딕 Extrabold##d다음에 또 오게나..#k");
		cm.dispose();
		    } else {
		        cm.sendOk("#fn나눔고딕 Extrabold##r설치칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
	} else {
		cm.sendOk("#fn나눔고딕 Extrabold##r돈이 부족한 것 같군..#k" );
		cm.dispose();
		}

	} else if (selection == 1) {
		if (cm.getMeso() >= 10000000) {
		if (cm.canHold(4035000)) {
		cm.gainItem(4035000, 50);
		cm.gainMeso(-10000000);
		cm.sendOk("#fn나눔고딕 Extrabold##d항상 싱싱한 미끼! 다음에 또 오게나..#k");
		cm.dispose();
		    } else {
		        cm.sendOk("#fn나눔고딕 Extrabold##r소비칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
	} else {
		cm.sendOk("#fn나눔고딕 Extrabold##r돈이 부족한 것 같군..#k" );
		cm.dispose();
	}

	} else if (selection == 2) {
		if (cm.getMeso() >= 100000000) {
		if (cm.canHold(4035000)) {
		cm.gainItem(4035000, 500);
		cm.gainMeso(-100000000);
		cm.sendOk("#fn나눔고딕 Extrabold##d항상 싱싱한 미끼! 다음에 또 오게나..#k");
		cm.dispose();
		    } else {
		        cm.sendOk("#fn나눔고딕 Extrabold##r소비칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
	} else {
		cm.sendOk("#fn나눔고딕 Extrabold##r돈이 부족한 것 같군..#k" );
		cm.dispose();
	}

	} else if (selection == 3) {
		if (cm.haveItem(4001187,5000) && cm.haveItem(4001188,5000) && cm.haveItem(4001189,5000)) {
		if (cm.canHold(1142949)) {
		cm.gainItem(4001187, -5000);
		cm.gainItem(4001188, -5000);
		cm.gainItem(4001189, -5000);
                cm.setAllStat(1142949,600,300,0);
		cm.sendOk("#fn나눔고딕 Extrabold##b#t1142949##k 를 교환 하였습니다.\r\n자네야말로 진정한 #d낚시 도사#k 라네..!");
		cm.dispose();
		    } else {
		        cm.sendOk("#fn나눔고딕 Extrabold##r장비칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
	} else {
		cm.sendOk("#fn나눔고딕 Extrabold##r물고기를 좀 더 잡아오게나..#k" );
		cm.dispose();
	}

	} else if (selection == 4) {
		if (cm.haveItem(4001187,500) && cm.haveItem(4001188,500) && cm.haveItem(4001189,500)) {
		if (cm.canHold(2430218)) {
		cm.gainItem(4001187, -500);
		cm.gainItem(4001188, -500);
		cm.gainItem(4001189, -500);
		cm.gainItem(2430218, 1);
		cm.sendOk("#fn나눔고딕 Extrabold##b폭풍 성장의 비약#k 을 교환 하였습니다.\r\n누구보다 #d빠른 성장#k 을 위해서 노력 하는군..! ");
		cm.dispose();
		    } else {
		        cm.sendOk("#fn나눔고딕 Extrabold##r소비칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
	} else {
		cm.sendOk("#fn나눔고딕 Extrabold##r물고기를 좀 더 잡아오게나..#k" );
		cm.dispose();
	}

	} else if (selection == 5) {
		if (cm.haveItem(4001187,50) && cm.haveItem(4001188,50) && cm.haveItem(4001189,50)) {
		cm.gainItem(4001187, -50);
		cm.gainItem(4001188, -50);
		cm.gainItem(4001189, -50);
                cm.gainMeso(15000000);
		cm.sendOk("#fn나눔고딕 Extrabold##r1 천 5 백만 메소#k 를 교환 하였습니다.\r\n#b"+ServerConstants.serverName+"#k 의 #d만수르#k 가 되어보게나..!");
		cm.dispose();
	} else {
		cm.sendOk("#fn나눔고딕 Extrabold##r물고기를 좀 더 잡아오게나..#k" );
		cm.dispose();
	}

	} else if (selection == 6) {
		if (cm.haveItem(4001187,500) && cm.haveItem(4001188,500) && cm.haveItem(4001189,500)) {
		cm.gainItem(4001187, -500);
		cm.gainItem(4001188, -500);
		cm.gainItem(4001189, -500);
                cm.gainMeso(150000000);
		cm.sendOk("#fn나눔고딕 Extrabold##r1 억 5 천만 메소#k 를 교환 하였습니다.\r\n#b"+ServerConstants.serverName+"#k 의 #d만수르#k 가 되어보게나..!");
		cm.dispose();
	} else {
		cm.sendOk("#fn나눔고딕 Extrabold##r물고기를 좀 더 잡아오게나..#k" );
		cm.dispose();
	}

	} else if (selection == 7) {
		if (cm.haveItem(4001187,5000) && cm.haveItem(4001188,5000) && cm.haveItem(4001189,5000)) {
		cm.gainItem(4001187, -5000);
		cm.gainItem(4001188, -5000);
		cm.gainItem(4001189, -5000);
                cm.gainMeso(1500000000);
		cm.sendOk("#fn나눔고딕 Extrabold##r15 억원 메소#k 를 교환 하였습니다.\r\n#b"+ServerConstants.serverName+"#k 의 #d만수르#k 가 되어보게나..!");
		cm.dispose();
	} else {
		cm.sendOk("#fn나눔고딕 Extrabold##r물고기를 좀 더 잡아오게나..#k" );
		cm.dispose();
	}

	} else if (selection == 8) {
		if (cm.haveItem(4001187,1000) && cm.haveItem(4001188,1000) && cm.haveItem(4001189,1000)) {
		if (cm.canHold(1012329)) {
		cm.gainItem(4001187, -1000);
		cm.gainItem(4001188, -1000);
		cm.gainItem(4001189, -1000);
                cm.setAllStat(1012329,300,300,0);
		cm.sendOk("#fn나눔고딕 Extrabold##d마음에 들길 바라네..!#k");
		cm.dispose();
		    } else {
		        cm.sendOk("#fn나눔고딕 Extrabold##r장비칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
	} else {
		cm.sendOk("#fn나눔고딕 Extrabold##r물고기를 좀 더 잡아오게나..#k" );
		cm.dispose();
	}

	} else if (selection == 9) {
		if (cm.haveItem(4001187,800) && cm.haveItem(4001188,800) && cm.haveItem(4001189,800)) {
		if (cm.canHold(1672004)) {
		cm.gainItem(4001187, -800);
		cm.gainItem(4001188, -800);
		cm.gainItem(4001189, -800);
                cm.setAllStat(1672004,300,300,0);
		cm.sendOk("#fn나눔고딕 Extrabold##d마음에 들길 바라네..!#k");
		cm.dispose();
		    } else {
		        cm.sendOk("#fn나눔고딕 Extrabold##r장비칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
	} else {
		cm.sendOk("#fn나눔고딕 Extrabold##r물고기를 좀 더 잡아오게나..#k" );
		cm.dispose();
	}

	} else if (selection == 10) {
		if (cm.haveItem(4001187,800) && cm.haveItem(4001188,800) && cm.haveItem(4001189,800)) {
		if (cm.canHold(1662002)) {
		cm.gainItem(4001187, -800);
		cm.gainItem(4001188, -800);
		cm.gainItem(4001189, -800);
                cm.setAllStat(1662002,300,300,0);
		cm.sendOk("#fn나눔고딕 Extrabold##d마음에 들길 바라네..!#k");
		cm.dispose();
		    } else {
		        cm.sendOk("#fn나눔고딕 Extrabold##r장비칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
	} else {
		cm.sendOk("#fn나눔고딕 Extrabold##r물고기를 좀 더 잡아오게나..#k" );
		cm.dispose();
	}

	} else if (selection == 11) {
		if (cm.haveItem(4001187,800) && cm.haveItem(4001188,800) && cm.haveItem(4001189,800)) {
		if (cm.canHold(1662003)) {
		cm.gainItem(4001187, -800);
		cm.gainItem(4001188, -800);
		cm.gainItem(4001189, -800);
                cm.setAllStat(1662003,300,300,0);
		cm.sendOk("#fn나눔고딕 Extrabold##d마음에 들길 바라네..!#k");
		cm.dispose();
		    } else {
		        cm.sendOk("#fn나눔고딕 Extrabold##r장비칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
	} else {
		cm.sendOk("#fn나눔고딕 Extrabold##r물고기를 좀 더 잡아오게나..#k" );
		cm.dispose();
	}

	} else if (selection == 12) {
		if (cm.haveItem(4001187,500) && cm.haveItem(4001188,500) && cm.haveItem(4001189,500)) {
		if (cm.canHold(1102369)) {
		cm.gainItem(4001187, -500);
		cm.gainItem(4001188, -500);
		cm.gainItem(4001189, -500);
                cm.setAllStat(1102369,200,200,0);
		cm.sendOk("#fn나눔고딕 Extrabold##d마음에 들길 바라네..!#k");
		cm.dispose();
		    } else {
		        cm.sendOk("#fn나눔고딕 Extrabold##r장비칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
	} else {
		cm.sendOk("#fn나눔고딕 Extrabold##r물고기를 좀 더 잡아오게나..#k" );
		cm.dispose();
		}
	} else if (selection == 13) {
		if (cm.haveItem(4001187,500) && cm.haveItem(4001188,500) && cm.haveItem(4001189,500)) {
		if (cm.canHold(1152090)) {
		cm.gainItem(4001187, -500);
		cm.gainItem(4001188, -500);
		cm.gainItem(4001189, -500);
                cm.setAllStat(1152090,200,200,0);
		cm.sendOk("#fn나눔고딕 Extrabold##d마음에 들길 바라네..!#k");
		cm.dispose();
		    } else {
		        cm.sendOk("#fn나눔고딕 Extrabold##r장비칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
	} else {
		cm.sendOk("#fn나눔고딕 Extrabold##r물고기를 좀 더 잡아오게나..#k" );
		cm.dispose();
		}

	} else if (selection == 100) {
		if (cm.haveItem(4001187,500)) {
		if (cm.canHold(4001188)) {
		cm.gainItem(4001187, -500);
		cm.gainItem(4001188, 250);
		cm.sendOk("#fn나눔고딕 Extrabold##d신선하고 싱싱한 물고기가 많다네~ 또 오게나!~#k");
		cm.dispose();
		    } else {
		        cm.sendOk("#fn나눔고딕 Extrabold##r기타칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
	} else {
		cm.sendOk("#fn나눔고딕 Extrabold##r물고기를 좀 더 잡아오게나..#k" );
		cm.dispose();
		}
	} else if (selection == 101) {
		if (cm.haveItem(4001187,500)) {
		if (cm.canHold(4001189)) {
		cm.gainItem(4001187, -500);
		cm.gainItem(4001189, 250);
		cm.sendOk("#fn나눔고딕 Extrabold##d신선하고 싱싱한 물고기가 많다네~ 또 오게나!~#k");
		cm.dispose();
		    } else {
		        cm.sendOk("#fn나눔고딕 Extrabold##r기타칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
	} else {
		cm.sendOk("#fn나눔고딕 Extrabold##r물고기를 좀 더 잡아오게나..#k" );
		cm.dispose();
		}
	} else if (selection == 102) {
		if (cm.haveItem(4001188,500)) {
		if (cm.canHold(4001187)) {
		cm.gainItem(4001188, -500);
		cm.gainItem(4001187, 250);
		cm.sendOk("#fn나눔고딕 Extrabold##d신선하고 싱싱한 물고기가 많다네~ 또 오게나!~#k");
		cm.dispose();
		    } else {
		        cm.sendOk("#fn나눔고딕 Extrabold##r기타칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
	} else {
		cm.sendOk("#fn나눔고딕 Extrabold##r물고기를 좀 더 잡아오게나..#k" );
		cm.dispose();
		}
	} else if (selection == 103) {
		if (cm.haveItem(4001188,500)) {
		if (cm.canHold(4001189)) {
		cm.gainItem(4001188, -500);
		cm.gainItem(4001189, 250);
		cm.sendOk("#fn나눔고딕 Extrabold##d신선하고 싱싱한 물고기가 많다네~ 또 오게나!~#k");
		cm.dispose();
		    } else {
		        cm.sendOk("#fn나눔고딕 Extrabold##r기타칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
	} else {
		cm.sendOk("#fn나눔고딕 Extrabold##r물고기를 좀 더 잡아오게나..#k" );
		cm.dispose();
		}
	} else if (selection == 104) {
		if (cm.haveItem(4001189,500)) {
		if (cm.canHold(4001187)) {
		cm.gainItem(4001189, -500);
		cm.gainItem(4001187, 250);
		cm.sendOk("#fn나눔고딕 Extrabold##d신선하고 싱싱한 물고기가 많다네~ 또 오게나!~#k");
		cm.dispose();
		    } else {
		        cm.sendOk("#fn나눔고딕 Extrabold##r기타칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
	} else {
		cm.sendOk("#fn나눔고딕 Extrabold##r물고기를 좀 더 잡아오게나..#k" );
		cm.dispose();
		}
	} else if (selection == 105) {
		if (cm.haveItem(4001189,500)) {
		if (cm.canHold(4001188)) {
		cm.gainItem(4001189, -500);
		cm.gainItem(4001188, 250);
		cm.sendOk("#fn나눔고딕 Extrabold##d신선하고 싱싱한 물고기가 많다네~ 또 오게나!~#k");
		cm.dispose();
		    } else {
		        cm.sendOk("#fn나눔고딕 Extrabold##r기타칸에 빈 공간이 없습니다.#k");
		        cm.dispose();
		    }
	} else {
		cm.sendOk("#fn나눔고딕 Extrabold##r물고기를 좀 더 잡아오게나..#k" );
		cm.dispose();
		}
}
}
}
}