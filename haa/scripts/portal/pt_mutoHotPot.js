var recipes = [[4034959, 0, 1, 130000, 45000], [4034960, 1, 1, 130000, 45000], [4034961, 2, 1, 130000, 45000], [4034962, 3, 1, 130000, 45000], [4034963, 4, 1, 130000, 45000], [4034964, 5, 1, 130000, 45000], [4034965, 6, 2, 130000, 45000], [4034966, 7, 1, 130000, 45000], [4034967, 8, 1, 130000, 45000], [4034968, 9, 1, 130000, 45000], [4034969, 10, 1, 130000, 45000], [4034970, 11, 1, 130000, 45000], [4034971, 12, 1, 130000, 45000], [4034972, 13, 1, 130000, 45000], [4034973, 14, 1, 130000, 45000], [4034974, 15, 1, 130000, 45000]];

var recipeItems = [ [[4034942, 0, 5], [4034946, 0, 10]],
		[[4034944, 0, 5], [4034948, 0, 10]],
		[[4034942, 0, 5], [4034946, 0, 5], [4034950, 0, 10]],
		[[4034944, 0, 5], [4034948, 0, 5], [4034952, 0, 10]],
		[[4034946, 0, 5], [4034950, 0, 5], [4034954, 0, 10]],
		[[4034950, 0, 5], [4034952, 0, 5], [4034956, 0, 10]],
		[[4034958, 0, 1], [4034944, 0, 5], [4034948, 0, 5], [4034954, 0, 10]],
		[[4034958, 0, 5], [4034946, 0, 5], [4034952, 0, 5], [4034956, 0, 10]],
		[[4034943, 0, 5], [4034947, 0, 10]],
		[[4034945, 0, 5], [4034949, 0, 10]],
		[[4034943, 0, 5], [4034947, 0, 5], [4034951, 0, 10]],
		[[4034945, 0, 5], [4034949, 0, 5], [4034953, 0, 10]],
		[[4034947, 0, 5], [4034951, 0, 5], [4034955, 0, 10]],
		[[4034951, 0, 5], [4034953, 0, 5], [4034957, 0, 10]],
		[[4034958, 0, 1], [4034945, 0, 5], [4034949, 0, 5], [4034955, 0, 10]],
		[[4034958, 0, 5], [4034947, 0, 5], [4034953, 0, 5], [4034957, 0, 10]]
		];

function enter(pi) {
    item = pi.getPlayer().getRecipe().left;
    count = pi.getPlayer().getRecipe().right;

    var eim = pi.getPlayer().getEventInstance();

    if (eim == null || eim.getProperty("mutoRecipe") == null || item == 0 || count == 0) {
	return;
    }

    mutoRecipe = recipes[eim.getProperty("mutoRecipe")];

    rep = recipeItems[mutoRecipe[1]];

    right = false;


    for (i = 0; i < rep.length; ++i) {
	if (eim.getProperty("recipeItem" + i) != null) {
	    if (parseInt(eim.getProperty("recipeItem" + i)) == item) {
		right = true;
		eim.setProperty("recipeCount" + i, parseInt(eim.getProperty("recipeCount" + i)) + count);
		if (parseInt(eim.getProperty("recipeCount" + i)) > parseInt(eim.getProperty("recipeReq" + i))) {
		    eim.setProperty("recipeCount" + i, eim.getProperty("recipeReq" + i));
		}
		break;
	    }
	}
    }

    pi.getPlayer().getRecipe().left = 0;
    pi.getPlayer().getRecipe().right = 0;
    pi.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.setMutoRecipe(mutoRecipe, rep.length, pi.getPlayer()));
    pi.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.addItemMuto(pi.getPlayer()));

    finish = true;
    for (i = 0; i < rep.length; ++i) {
	if (eim.getProperty("recipeItem" + i) != null) {
	    if (parseInt(eim.getProperty("recipeCount" + i)) < parseInt(eim.getProperty("recipeReq" + i))) {
		finish = false;
	    }
	}
    }

    if (finish) {
	eim.setProperty("complete", "1");
	eim.schedule("recipe", 100);
    } else {
	if (right) {
	    pi.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.environmentChange("Map/Effect3.img/hungryMutoMsg/msg4", 16));
	} else {
	    pi.getPlayer().getMap().broadcastMessage(Packages.tools.packet.CField.environmentChange("Map/Effect3.img/hungryMutoMsg/msg3", 16));
	    pi.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.environmentChange("Map/Effect3.img/hungryMuto/good", 16));
	}
    }
}