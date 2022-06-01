importPackage(java.lang);
importPackage(java.io);

var ck = 0;

var itemlist = [1042295, 1103428, 1152134, 1003244]

function start()
{
	St = -1;
	action(1, 0, 0);
}

function action(M, T, S) {
	if(M != 1)
	{
		cm.dispose();
		return;
	}

	if(M == 1)
	St++;

	if(St == 0)	{
	var talk ="제네시스 패키지를 오픈하시겠습니까?\r\n";
	talk +="#L0#상자를 오픈한다."
	cm.sendSimple(talk);
	} else if(St == 1)	{
	for(var i = 0; i<5; i++){
		item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(itemlist[i]);
		item.setStr(150);
		item.setDex(150);
		item.setInt(150);
		item.setLuk(150);
		item.setWatk(150);
		item.setMatk(150);
		Packages.server.MapleInventoryManipulator.addbyItem(cm.getClient(), item, false, true);
	}
		cm.gainItem(2439304, -1);
		cm.dispose();
	}
}