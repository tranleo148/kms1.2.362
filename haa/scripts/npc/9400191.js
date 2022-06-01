
importPackage(java.lang);

var enter = "\r\n";
var seld = -1;

var isStart = false;

var guild1 = null;
var guild2 = null;

var guild1M = null;
var guild2M = null;

var guild1R = false;
var guild2R = false;

function start() {
	status = -1;
	action(1, 0, 0);
}
function action(mode, type, sel) {
	if (mode == 1) {
		status++;
	} else {
		cm.dispose();
		return;
	}

	if (cm.getPlayer().getGuild() == null) {
		cm.sendOk("길드가 없다면 이용할 수 없는 컨텐츠입니다.");
		cm.dispose();
		return;
	}
	if (cm.getChannelServer().getGuildWar() == null) {
		cm.getChannelServer().initMapleGuildWar();
	}

	guild1R = cm.getGuildWarReady(1);
	guild2R = cm.getGuildWarReady(2); // 만들어야함

	guild1 = cm.getMapleGuildWar(1);
	guild2 = cm.getMapleGuildWar(2);

	guild1M = cm.getGuildWarMaster(1);
	guild2M = cm.getGuildWarMaster(2);

	isStart = cm.isGuildWarStart();
	if (isStart) {
		cm.sendOk("이미 해당 채널에서는 #b"+guild1.getName()+" 길드#k와 #b"+guild2.getName()+" 길드#k간의 경쟁전이 이루어지고 있습니다. 다른 채널을 이용해주세요.");
		cm.dispose();
		return;
	}

	if (guild1 != null) {
		if (guild1 == cm.getPlayer().getGuild()) {
			if (!guild1R) {
				if (status == 0) {
					var msg = "#fs14##fn나눔고딕#[컨텐츠 :: 길드 경쟁전]#fs12#"+enter;
					msg += "현재 #b"+guild1.getName()+" 길드#k의 원정대장 : #b"+cm.getGuildWarMaster(1).getName()+"#k#b"+enter;
					msg += "#L1#원정대원의 목록을 확인하고 싶습니다."+enter;
					if (cm.getPlayer() != guild1M && cm.getChannelServer().getGuildWar().getMembers(1).indexOf(cm.getPlayer()) == -1)
					msg += "#L2#원정대의 대원이 되어 경쟁전에 참여하고 싶습니다.";
	
					if (cm.getPlayer() == guild1M) 
						msg += "#L3#원정대의 구성이 완료되었습니다.";
	
					cm.sendSimple(msg);
				} else if (status == 1) {
					seld = sel;
					switch (sel) {
						case 1:
							// 원정대 멤버 확인
							cm.sendOk("현재 #b"+guild1.getName()+" 길드#k 원정대에 참여중인 인원 리스트입니다."+enter+cm.getGuildWarMembers(1));
							cm.dispose();
							return;
						case 2:
							cm.sendYesNo("정말 원정대에 참여하시겠습니까?");
							break;
						case 3:
							if (cm.getPlayer() != guild1M) {
								cm.sendOk("원정대장만 선택 가능한 항목입니다.");
								cm.dispose();
								return;
							}
							cm.sendYesNo("정말 준비가 다 되었습니까? #b "+guild1.getName()+"길드#k의 총 배팅 금액은 #b"+cm.getGuildWarBettings(1)+" 메소#k 입니다. 상대를 찾으시겠습니까?");
							break;
					}
				} else if (status == 2) {
					switch (seld) {
						case 2:
							var msg = "#b#h ##k님께선 얼마를 배팅하고 길드 경쟁전에 참가하시겠습니까?"+enter;
							msg += "#r#fs13#※주의! 지금 배팅하는 금액은 즉시 빠져나가며, 경쟁전에서 이기거나, 원정대가 해체되는 경우를 제외하곤 절대 돌려받을 수 없으니 신중하게 선택해주세요.";
							cm.sendGetNumber(msg, 1, 1, cm.getPlayer().getMeso());
							break;
						case 3:
							cm.guildReady(1);
							var msg = "원정대의 준비가 완료되었습니다. 이제 경쟁할 길드가 나타난다면 경쟁전을 시작하게 됩니다."+enter;
							msg += "#r원정대원 전부는 원정대가 해체되거나, 시작하기 전까진 게임을 종료해서도, 이 맵을 벗어나서도 안됩니다.#k"+enter;
							msg += "자세한 정보 및 원정대 해체에 관한 내용은 제게 다시 말을 걸어주세요.";
							cm.sendOk(msg);
							cm.dispose();
							return;
					}
				} else if (status == 3) {
					if (seld == 2) {
						if (sel < 0 || sel > cm.getPlayer().getMeso()) {
							cm.sendOk("잘못된 금액입니다.");
							cm.dispose();
							return;
						}
						cm.guildWarBetting(sel, 1);
						cm.getChannelServer().getGuildWar().addMember(1, cm.getPlayer());
						cm.getPlayer().setKeyValue(202005261, "gwarMeso", ""+sel);
						cm.gainMeso(-sel);
						cm.sendOk("#b"+sel+"#k메소를 배팅하고 #b"+guild1.getName()+" 길드#k의 원정대에 참가하였습니다. 만약 원정대에서 나가고 싶다면 제게 다시 말을 걸어주시길 바랍니다.");
						cm.dispose();
						return;
					}
				}
			} else {
				if (cm.getGuildWarMaster(1) == cm.getPlayer() && guild2R) {
					if (status == 0) {
						var msg = "이제 곧 경쟁전이 시작됩니다. 마지막으로 확인해주세요.#b"+enter;
						msg += "#b"+guild2.getName()+" 길드#k의 총 배팅금 : #b"+cm.getGuildWarBettings(2)+" 메소"+enter;
						msg += "#L1#"+guild2.getName()+" 길드의 원정대 확인하기."+enter;

						msg += "#L2#경쟁전을 치룰 모든 준비가 되었습니다."+enter;
						msg += "#L3#경쟁전을 없던 일로 하고 싶습니다.";

						cm.sendSimple(msg);
					} else if (status == 1) {
						switch (sel) {
							case 1:
								cm.sendOk("현재 #b"+guild1.getName()+" 길드#k 원정대에 참여중인 인원 리스트입니다."+enter+cm.getGuildWarMembers(2));
								cm.dispose();
								return;
							case 2:
								cm.guildWarStart(1);
								cm.getPlayer().dropMessage(5, guild2.getName()+"길드가 시작할 때까지 기다리는 중 입니다.");
								cm.dispose();
								return;
							case 3:
								cm.getGuildWarMaster(2).dropMessage(5, guild1.getName()+"의 원정대 해체 요청으로 인해 경쟁전 매칭이 삭제되었습니다. 처음부터 다시 준비해주세요.");
								cm.clearGuildWar(1);
								cm.dispose();
								return;
						}
					}
				} else {
					if (status == 0) {
						if (cm.getGuildWarMaster(1) == cm.getPlayer()) {
							var msg = "이미 #b"+guild1.getName()+" 길드#k의 준비는 끝났습니다. 원하시는 항목이 있다면 선택해주세요.#b"+enter;
							msg += "#L1#원정대를 해체하고 싶습니다.";
							msg += "#L2#별 일 아닙니다.";
							cm.sendSimple(msg);
						}
						else {
							cm.sendOk("이미 #b"+guild1.getName()+" 길드#k의 준비는 끝났습니다.");
							cm.dispose();
							return;
						}
					}
					else if (status == 1) {
						if (sel == 1) {
							cm.clearGuildWar(1);
							cm.sendOk("해체가 완료되었습니다.");
							cm.dispose();
							return;
						} else {
							cm.dispose();
							return;
						}
					}
				}
			}
		} else {
			if (guild2 == null) {
				if (!guild1R) {
					cm.sendOk("아직 #b"+guild1.getName()+" 길드#k의 준비가 끝나지 않았습니다. 잠시 후 다시 시도해주세요.");
					cm.dispose();
					return;
				}

				if (status == 0)
					cm.sendYesNo("#b "+guild1.getName()+" 길드#k에게 대전을 신청하기 위해서는 메소를 배팅한 후, 원정대를 설립해야 합니다.\r\n원정대장이 되어 대원을 모집하겠습니까?");
				else if (status == 1) {
					cm.sendGetNumber("#b#h ##k님께선 얼마를 배팅하고 길드 경쟁전에 참가하시겠습니까?", 1, 1, cm.getPlayer().getMeso());
				} else if (status == 2) {
					if (sel < 0 || sel > cm.getPlayer().getMeso()) {
						cm.sendOk("잘못된 금액입니다.");
						cm.dispose();
						return;
					}
					cm.guildWarBetting(sel, 2);
					cm.guildWarMaster(2, cm.getPlayer());
					cm.setGuildWar(2, cm.getPlayer().getGuild());
					cm.getChannelServer().getGuildWar().addMember(2, cm.getPlayer());
					cm.getPlayer().setKeyValue(202005261, "gwarMeso", ""+sel);
					cm.gainMeso(-sel);
					cm.sendOk("#b"+sel+"#k메소를 배팅하고 #b"+cm.getPlayer().getGuild().getName()+" 길드#k의 원정대를 생성하였습니다. 참여를 원하는 길드원은 제게 말을 걸어 배팅하고 참가하시면 됩니다.");
					cm.dispose();
					return;
				}
			} else {
				if (guild2R) {
					if (cm.getGuildWarMaster(2) == cm.getPlayer() && guild1R) {
						if (status == 0) {
							var msg = "이제 곧 경쟁전이 시작됩니다. 마지막으로 확인해주세요.#b"+enter;
							msg += "#b"+guild1.getName()+" 길드#k의 총 배팅금 : #b"+cm.getGuildWarBetting(1)+"메소"+enter;
							msg += "#L1#"+guild1.getName()+" 길드의 원정대 확인하기."+enter;

							msg += "#L2#경쟁전을 치룰 모든 준비가 되었습니다."+enter;
							msg += "#L3#경쟁전을 없던 일로 하고 싶습니다.";

							cm.sendSimple(msg);
						} else if (status == 1) {
							switch (sel) {
								case 1:
									cm.sendOk("현재 #b"+guild1.getName()+" 길드#k 원정대에 참여중인 인원 리스트입니다."+enter+cm.getGuildWarMembers(1));
									cm.dispose();
									break;
								case 2:
									cm.guildWarStart(2);
									cm.getPlayer().dropMessage(5, guild1.getName()+" 길드가 시작할 때까지 기다리는 중 입니다.");
									cm.dispose();
									return;
								case 3:
									cm.getGuildWarMaster(1).dropMessage(5, guild2.getName()+"의 원정대 해체 요청으로 인해 경쟁전 매칭이 삭제되었습니다. 처음부터 다시 준비해주세요.");
									cm.clearGuildWar(2);
									cm.dispose();
									return;
							}
						}
					} else {
						cm.sendOk("이미 #b"+guild2.getName()+" 길드#k의 준비는 끝났습니다.");
						cm.dispose();
						return;
					}
				}
				if (guild2 == cm.getPlayer().getGuild()) {
					if (status == 0) {
						var msg = "#fs14##fn나눔고딕#[컨텐츠 :: 길드 경쟁전]#fs12#"+enter;
						msg += "현재 #b "+guild2.getName()+" 길드#k의 원정대장 : #b"+cm.getGuildWarMaster(2).getName()+"#k#b"+enter;
						msg += "#L1#원정대원의 목록을 확인하고 싶습니다."+enter;
						if (cm.getPlayer() != guild2M && cm.getChannelServer().getGuildWar().getMembers(2).indexOf(cm.getPlayer()) == -1)
						msg += "#L2#원정대의 대원이 되어 경쟁전에 참여하고 싶습니다.";
		
						if (cm.getPlayer() == guild2M) 
							msg += "#L3#원정대의 구성이 완료되었습니다.";
		
						cm.sendSimple(msg);
					} else if (status == 1) {
						seld = sel;
						switch (sel) {
							case 1:
								// 원정대 멤버 확인
								cm.sendOk("현재 #b"+guild2.getName()+" 길드#k 원정대에 참여중인 인원 리스트입니다."+enter+cm.getGuildWarMembers(2));
								cm.dispose();
								return;
							case 2:
								cm.sendYesNo("정말 원정대에 참여하시겠습니까?");
								break;
							case 3:
								if (cm.getPlayer() != guild2M) {
									cm.sendOk("원정대장만 선택 가능한 항목입니다.");
									cm.dispose();
									return;
								}
								cm.sendYesNo("정말 준비가 다 되었습니까? #b "+guild2.getName()+"길드#k의 총 배팅 금액은 #b"+cm.getGuildWarBettings(2)+" 메소#k 입니다.");
								break;
						}
					} else if (status == 2) {
						switch (seld) {
							case 2:
								var msg = "#b#h ##k님께선 얼마를 배팅하고 길드 경쟁전에 참가하시겠습니까?"+enter;
								msg += "#r#fs13#※주의! 지금 배팅하는 금액은 즉시 빠져나가며, 경쟁전에서 이기거나, 원정대가 해체되는 경우를 제외하곤 절대 돌려받을 수 없으니 신중하게 선택해주세요.";
								cm.sendGetNumber(msg, 1, 1, cm.getPlayer().getMeso());
								break;
							case 3:
								cm.guildReady(2);
								var msg = "원정대의 준비가 완료되었습니다. 이제 곧 시작됩니다."+enter;
								msg += "#r원정대원 전부는 원정대가 해체되거나, 시작하기 전까진 게임을 종료해서도, 이 맵을 벗어나서도 안됩니다.#k"+enter;
								msg += "자세한 정보 및 원정대 해체에 관한 내용은 제게 다시 말을 걸어주세요.";
								cm.sendOk(msg);
								cm.dispose();
								return;
						}
					} else if (status == 3) {
						if (seld == 2) {
							if (sel < 0 || sel > cm.getPlayer().getMeso()) {
								cm.sendOk("잘못된 금액입니다.");
								cm.dispose();
								return;
							}
							cm.guildWarBetting(sel, 2);
							cm.getChannelServer().getGuildWar().addMember(2, cm.getPlayer());
							cm.getPlayer().setKeyValue(202005261, "gwarMeso", ""+sel);
							cm.gainMeso(-sel);
							cm.sendOk("#b"+sel+"#k메소를 배팅하고 #b 길드#k의 원정대에 참가하였습니다. 만약 원정대에서 나가고 싶다면 제게 다시 말을 걸어주시길 바랍니다.");
							cm.dispose();
							return;
						}
					}
				} else {
					cm.sendOk("이미 #b"+guild1.getName()+"길드#k의 경쟁 길드가 정해졌습니다. 다른 채널에서 다른 길드와 시도해주세요.");
					cm.dispose();
					return;
				}
			}
		}
	} else {
		if (status == 0) {
			var msg = "#fs14##fn나눔고딕#[컨텐츠 :: 길드 경쟁전]#fs12##b"+enter;
			msg += "#L1#길드 경쟁전이 무엇인가요?"+enter;
			msg += "#L3#길드 경쟁전의 랭킹을 확인하고 싶습니다."+enter+enter;
			msg += "#L2#길드 경쟁전에 참여하고 싶습니다."+enter;
			cm.sendSimple(msg);
		} else if (status == 1) {
			if (sel == 1) {
				var msg = "길드 경쟁전은 블라블라";
				cm.sendOk(msg);
				cm.dispose();
				return;
			}
			if (sel == 3) {
				var msg = "길드 경쟁전의 랭킹은 상위 10위까지만 보여집니다."+enter;
				msg += cm.getGuildWarRanking();
				cm.sendOk(msg);
				cm.dispose();
			}
			if (isStart) {
				cm.sendOk("이미 해당 채널에서 경쟁 중인 길드가 있습니다. 다른 채널을 이용해주세요.");
				cm.dispose();
				return;
			}
	
			if (guild1 != null) {
				cm.sendOk("오류가 발생하였습니다.");
				cm.dispose();
				return;
			}

			cm.sendYesNo("아직 #b"+cm.getPlayer().getGuild().getName()+" 길드#k는 원정대가 존재하지 않습니다. 메소를 배팅한 후 원정대를 꾸려 대원을 모집하겠습니까?");
		} else if (status == 2) {
			cm.sendGetNumber("#b#h ##k님께선 얼마를 배팅하고 길드 경쟁전에 참가하시겠습니까?", 1, 1, cm.getPlayer().getMeso());
		} else if (status == 3) {
			if (sel < 0 || sel > cm.getPlayer().getMeso()) {
				cm.sendOk("잘못된 금액입니다.");
				cm.dispose();
				return;
			}
			cm.guildWarBetting(sel, 1);
			cm.guildWarMaster(1, cm.getPlayer());
			cm.setGuildWar(1, cm.getPlayer().getGuild());
			cm.getChannelServer().getGuildWar().addMember(1, cm.getPlayer());
			cm.getPlayer().setKeyValue(202005261, "gwarMeso", ""+sel);
			cm.gainMeso(-sel);
			cm.sendOk("#b"+sel+"#k메소를 배팅하고 #b"+cm.getPlayer().getGuild().getName()+" 길드#k의 원정대를 생성하였습니다. 참여를 원하는 길드원은 제게 말을 걸어 배팅하고 참가하시면 됩니다.");
			cm.dispose();
			return;
		}
	}
}