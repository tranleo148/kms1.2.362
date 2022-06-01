/*
제작자 : qudtlstorl79@nate.com
*/

importPackage(java.util);
importPackage(java.lang);
importPackage(java.io);

importPackage(Packages.constants);
importPackage(Packages.client);
importPackage(Packages.client.stats);
importPackage(Packages.server);
var status = -1;

보라 = "#fMap/MapHelper.img/weather/starPlanet/7#";
파랑 = "#fMap/MapHelper.img/weather/starPlanet/8#";

getmhair = 0;
getfhair = 0;
getmface = 0;
getfface = 0;
count = 0;
mhair = [];
fhair = [];
mface = [];
fface = [];
mhair1 = [];
fhair1 = [];
mface1 = [];
fface1 = [];
realface = 0;
별회 = "#fUI/GuildMark.img/Mark/Pattern/00004001/15#";
function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection, selection2) {
    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        if (status == 0 && st2 == 2 && count == 0) {
            sel1 = selection;
            count++;
            status++;
        } else {
            status++;
        }
    }
    if (status == 0) {
        if (count == 0) {
            st = 1;
            if (cm.getPlayer().getJob() == 10112 || cm.getPlayer().getJob() == 10111 || cm.getPlayer().getJob() == 10110 || cm.getPlayer().getJob() == 10100) {
                st2 = 2;
            } else {
                st2 = cm.getPlayer().getGender();
            }

            var it = MapleItemInformationProvider.getInstance().getAllItems().iterator();
            while (it.hasNext()) {
                showhair = 0;
                var itemPair = it.next();
                getgen = Math.floor(itemPair.getLeft() / 1000);
                if (itemPair.getLeft() >= 30010 && itemPair.getLeft() <= 30017) {
                    showhair = 1;
                }
                if (itemPair.getLeft() >= 30070 && itemPair.getLeft() <= 30097) {
                    showhair = 1;
                }
                if (itemPair.getRight() != "" || itemPair.getRight() != null) {
                    if (itemPair.getLeft() % 10 == (cm.getPlayer().getHair() % 10) /*&& cm.isExistFH(itemPair.getLeft())*/) {
                        if (getgen == 30 || getgen == 32 || getgen == 33 || getgen == 35 || getgen == 36 || getgen == 40 || getgen == 43 || getgen == 45 || getgen == 46 || getgen == 57) {
                            if (showhair == 0) {
                                mhair1.push(itemPair.getLeft());
                            }
                        }
                    }
                    if (cm.getPlayer().getJob() == 10112 || cm.getPlayer().getJob() == 10111 || cm.getPlayer().getJob() == 10110 || cm.getPlayer().getJob() == 10100 || cm.getPlayer().getDressup() == true) {
                        gh = cm.getPlayer().getSecondHair();
                    } else {
                        gh = cm.getPlayer().getHair();
                    }
                    if (itemPair.getLeft() % 10 == (gh % 10) /*&& cm.isExistFH(itemPair.getLeft())*/) {
                        if (getgen == 31 || getgen == 34 || getgen == 37 || getgen == 38 || getgen == 39 || getgen == 41 || getgen == 42 || getgen == 44 || getgen == 47 || getgen == 48 || getgen == 57) {
                            if (showhair == 0) {
                                fhair1.push(itemPair.getLeft())
                            }
                        }
                    }
                    if ((Math.floor(itemPair.getLeft() / 100)) % 10 == 0) {
                        if (getgen == 20 || getgen == 23 || getgen == 25 || getgen == 27 || getgen == 50) {
                            if (showhair == 0) {
                                mface1.push(itemPair.getLeft());
                            }
                        }
                        if (getgen == 21 || getgen == 24 || getgen == 25 || getgen == 26 || getgen == 28 || getgen == 51) {
                            if (showhair == 0) {
                                fface1.push(itemPair.getLeft());
                            }
                        }
                    }
                }

            }
            mhair1.sort(function (a, b) {
                return parseInt(a) - parseInt(b)
            });
            fhair1.sort(function (a, b) {
                return parseInt(a) - parseInt(b)
            });
            mface1.sort(function (a, b) {
                return parseInt(a) - parseInt(b)
            });
            fface1.sort(function (a, b) {
                return parseInt(a) - parseInt(b)
            });
            for (i = 0; i < mhair1.length; i++) {
                gar = Math.floor(getmhair / 127);
                if (getmhair % 127 == 0) {
                    mhair[gar] = [];
                }
                getmhair++;
                mhair[gar].push(mhair1[i]);
            }
            for (i = 0; i < fhair1.length; i++) {
                gar = Math.floor(getfhair / 127);
                if (getfhair % 127 == 0) {
                    fhair[gar] = [];
                }
                getfhair++;
                fhair[gar].push(fhair1[i]);
            }
            for (i = 0; i < mface1.length; i++) {
                gar = Math.floor(getmface / 127);
                if (getmface % 127 == 0) {
                    mface[gar] = [];
                }
                getmface++;
                mface[gar].push(mface1[i]);
            }
            for (i = 0; i < fface1.length; i++) {
                gar = Math.floor(getfface / 127);
                if (getfface % 127 == 0) {
                    fface[gar] = [];
                }
                getfface++;
                fface[gar].push(fface1[i]);
            }
        }
        말 = "";
        타입 = "";
        if (st == 0) {
            말 += "" + 파랑 + "#e#fs12##fc0xFF6B66FF##e원하는 리스트#n#k#fs11#의 얼굴을 선택해보세요!\r\n\r\n"
            if (st2 == 0) {
                말 += "#r(총 " + mhair.length + "페이지로 구성되어 있습니다.)#k#n\r\n\r\n"
                for (i = 0; i < mhair.length; i++) {
                    말 += "#L" + i + "# " + (parseInt(i) + 1) + "페이지 헤어스타일을 선택하겠습니다.#l\r\n";
                }
            } else if (st2 == 1) {
                말 += "#r(총 " + fhair.length + "페이지로 구성되어 있습니다.)#k#n\r\n\r\n"
                for (i = 0; i < fhair.length; i++) {
                    말 += "#L" + i + "# " + (parseInt(i) + 1) + "페이지 헤어스타일을 선택하겠습니다.#l\r\n";
                }
            } else if (st2 == 2) {
                if (count == 0) {
                    말 += "#r(알파 헤어는 총 " + mhair.length + "페이지로 구성되어 있습니다.)#k#n\r\n\r\n"
                    for (i = 0; i < mhair.length; i++) {
                        말 += "#L" + i + "# " + (parseInt(i) + 1) + "페이지 헤어스타일을 선택하겠습니다.#l\r\n";
                    }
                } else {
                    말 += "#r(베타 헤어는 총 " + fhair.length + "페이지로 구성되어 있습니다.)#k#n\r\n\r\n"
                    for (i = 0; i < fhair.length; i++) {
                        말 += "#L" + i + "# " + (parseInt(i) + 1) + "페이지 헤어스타일을 선택하겠습니다.#l\r\n";
                    }

                }
            }
            타입 = "";
        } else if (st == 1) {
            말 += "" + 파랑 + "#e#fs12##fc0xFF6B66FF##e원하는 리스트#n#k#fs11#의 얼굴을 선택해보세요!\r\n\r\n"
            if (st2 == 0) {
                for (i = 0; i < mface.length; i++) {
                    if (i == 0) {
                        타입 = "" + 보라 + " #fc0xFF5C1DB5#A"
                    } else if (i == 1) {
                        타입 = "" + 보라 + " #fc0xFF4742DB#B"
                    } else if (i == 2) {
                        타입 = "" + 보라 + " #fc0xFFBD2B70#C"
                    } else if (i == 3) {
                        타입 = "" + 보라 + " #fc0xFF35B62C#D"
                    }
                    말 += "#L" + i + "#" + 타입 + " 리스트#k의 얼굴을 선택하겠습니다.#l\r\n";
                }
            } else if (st2 == 1) {
                for (i = 0; i < fface.length; i++) {
                    if (i == 0) {
                        타입 = "" + 보라 + " #fc0xFF5C1DB5#A"
                    } else if (i == 1) {
                        타입 = "" + 보라 + " #fc0xFF4742DB#B"
                    } else if (i == 2) {
                        타입 = "" + 보라 + " #fc0xFFBD2B70#C"
                    } else if (i == 3) {
                        타입 = "" + 보라 + " #fc0xFF35B62C#D"
                    }
                    말 += "#L" + i + "#" + 타입 + " 리스트#k의 얼굴을 선택하겠습니다.#l\r\n";
                }
            } else {
                if (count == 0) {
                    for (i = 0; i < mface.length; i++) {
                        if (i == 0) {
                            타입 = "" + 보라 + " #fc0xFF5C1DB5#A"
                        } else if (i == 1) {
                            타입 = "" + 보라 + " #fc0xFF4742DB#B"
                        } else if (i == 2) {
                            타입 = "" + 보라 + " #fc0xFFBD2B70#C"
                        } else if (i == 3) {
                            타입 = "" + 보라 + " #fc0xFF35B62C#D"
                        }
                        말 += "#L" + i + "#" + 타입 + " 리스트#k의 얼굴을 선택하겠습니다.#l\r\n";
                    }
                } else {
                    for (i = 0; i < fface.length; i++) {
                        if (i == 0) {
                            타입 = "" + 보라 + " #fc0xFF5C1DB5#A"
                        } else if (i == 1) {
                            타입 = "" + 보라 + " #fc0xFF4742DB#B"
                        } else if (i == 2) {
                            타입 = "" + 보라 + " #fc0xFFBD2B70#C"
                        } else if (i == 3) {
                            타입 = "" + 보라 + " #fc0xFF35B62C#D"
                        }
                        말 += "#L" + i + "#" + 타입 + " 리스트#k의 얼굴을 선택하겠습니다.\r\n";
                    }

                }
            }

        }
        cm.sendSimple(말);
    } else if (status == 1) {
        st4 = selection;
        if (st == 0) {
            if (st2 == 0) {
                st3 = mhair[selection];
            } else if (st2 == 1) {
                st3 = fhair[selection];
            }
        } else if (st == 1) {
            if (st2 == 0) {
                st3 = mface[selection];
            } else {
                st3 = fface[selection];
            }
        }
        if (cm.getPlayer().getJob() != 10112 && cm.getPlayer().getJob() != 10111 && cm.getPlayer().getJob() != 10110 && cm.getPlayer().getJob() != 10100) {
            cm.askCoupon(5152020, st3);
            //                cm.askAvatar("마음에 드시는 얼굴을 선택해보세요!", st3);
        } else {
            if (st == 0) {
                if (cm.getPlayer().getGender() == 0) {
                    cm.askCoupon(5150132, mhair[selection]);
                } else {
                    cm.askCoupon(5150132, fhair[selection]);
                }
            } else {
                if (cm.getPlayer().getGender() == 0) {
                    cm.askCoupon(5152020, mface[selection]);
                } else {
                    cm.askCoupon(5152020, fface[selection]);
                }
            }
        }
    }
}

function setAvatar(args) {
    if (args < 100) {
        if (cm.getPlayer().getDressup() == true) {
            cm.setAngelicSecondSkin(args);
        } else {
            cm.getPlayer().setSkinColor(args);
            cm.getPlayer().updateSingleStat(MapleStat.SKIN, args);
        }
    } else if (args < 30000) {
        if (cm.getPlayer().getDressup() == true) {
            cm.setAngelicSecondFace(args);
        } else {
            cm.getPlayer().setFace(args);
            cm.getPlayer().updateSingleStat(MapleStat.FACE, args);
        }
    } else {
        if (cm.getPlayer().getDressup() == true) {
            cm.setAngelicSecondHair(args);
        } else {
            cm.getPlayer().setHair(args);
            cm.getPlayer().updateSingleStat(MapleStat.HAIR, args);
        }
    }
    cm.getPlayer().equipChanged();
}