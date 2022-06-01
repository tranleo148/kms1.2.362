var data;
var day;
var item = -1;
function enter(pi) {
	getData();
	item = 2434745 + day;
	var dateid = "mPark_Date_" + day;
	pi.getClient().setKeyValue("mPark", "" + (parseInt(pi.getClient().getKeyValue("mPark")) + 1));

	if (pi.getClient().getKeyValue(dateid) == null) {
	    pi.getClient().setKeyValue(dateid, "1");
	} else {
	    pi.getClient().setKeyValue(dateid, "" + (parseInt(pi.getClient().getKeyValue(dateid)) + 1));
	}
	pi.openNpc(9071000, "mPark_Reward");

}

function getData() {
	time = new Date();
	year = time.getFullYear();
	month = time.getMonth() + 1;
	if (month < 10) {
		month = "0"+month;
	}
	date = time.getDate() < 10 ? "0"+time.getDate() : time.getDate();
	data = year+""+month+""+date;
	day = time.getDay();
}