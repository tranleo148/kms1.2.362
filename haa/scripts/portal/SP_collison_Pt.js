importPackage(Packages.packet.creators);

function enter(pi) {
	pi.getClient().getSession().write(MainPacketCreator.getGMText(20, "[알림] 메이플 월드로 이동하시려면 캐시샵 옆 스타플래닛/메이플월드 버튼을 이용해주세요."));
}