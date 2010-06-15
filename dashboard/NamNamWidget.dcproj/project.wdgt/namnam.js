var ONE_DAY = (1000* 60 * 60 * 24);

function loadMensa(url, callback) {
    // Values you provide
    var onloadHandler = function() { xmlLoaded(xmlRequest, callback); };

    // XMLHttpRequest setup code
    var xmlRequest = new XMLHttpRequest();
    xmlRequest.onload = onloadHandler;
    xmlRequest.open("GET", url);
    xmlRequest.setRequestHeader("Cache-Control", "no-cache");
    xmlRequest.send(null);
}

// Called when an XMLHttpRequest loads a feed; works with the XMLHttpRequest setup snippet
function xmlLoaded(xmlRequest, callback) 
{
	if (xmlRequest.status == 200) {
        var container = JSON.parse(xmlRequest.responseText, reviver);
        currentMenues = container.Mensa;
        if(widget) widget.setPreferenceForKey(currentMenues,"currentMensaMenues");
        callback(getCurrentTagesmenue(currentMenues));
	} else {
		alert("Error fetching data: HTTP status " + xmlRequest.status);
	}
}

function reviver(key, value) {
    if(key == 'tag' || key == 'firstDate' || key == 'lastDate') {
        var dateParts = value.split('-');
        return new Date(dateParts[0],dateParts[1]-1 /* oh, the pita */,dateParts[2]);    
    } else return value;
}

function getCurrentTagesmenue(mensa) {
    var curDate = getCurrentTagesMenueDate(mensa);
    if(!curDate) return false;
    else return getTagesMenue(mensa, curDate);
}

function getCurrentTagesMenueDate(mensa) {
    var date = new Date();
    if(date.getHours() > 15) date.setTime(date.getTime()+ONE_DAY) // default to tomorrow starting at 16:00
    var m = getTagesMenue(mensa,date);
    if(m == null) return getNextTagesMenueDate(mensa, date);
    else return date;
}

function getNextTagesmenue(mensa, date) {
    var nextDate = getNextTagesMenueDate(mensa,date);
    if(!nextDate) return false;
    else return getTagesMenue(mensa, nextDate);
}

function getNextTagesMenueDate(mensa, date) {
    var myDate = new Date(date.getTime() + ONE_DAY); // clone so we don't modify original object
    if(myDate > mensa.lastDate) return false;
    if(myDate < mensa.firstDate) return mensa.firstDate;
    
    var m = getTagesMenue(mensa, myDate);
    while(m == null && myDate <= mensa.lastDate) {
        // 1 day (thank god we got this unified the world over!)
        myDate.setTime(myDate.getTime() + ONE_DAY);
        m = getTagesMenue(mensa,myDate);
    }
    if(m == null) return false;
    else return myDate;
}

function getPreviousTagesmenue(mensa, date) {
    var prevDate = getPreviousTagesMenueDate(mensa,date);
    if(!prevDate) return false;
    else return getTagesMenue(mensa, prevDate);
}

function getPreviousTagesMenueDate(mensa, date) {
    var myDate = new Date(date.getTime() - ONE_DAY); // clone so we don't modify original object
    if(myDate < mensa.firstDate) return false;
    if(myDate > mensa.lastDate) return mensa.lastDate;
    
    var m = getTagesMenue(mensa, myDate);
    while(m == null && myDate >= mensa.firstDate) {
        // 1 day (thank god we got this unified the world over!)
        myDate.setTime(myDate.getTime() - ONE_DAY);
        m = getTagesMenue(mensa,myDate);
    }
    if(m == null) return false;
    else return myDate;
}

function getTagesMenue(mensa, date) {
    if(!date || !mensa) {
        alert("Invalid usage: date or mensa is null");
        return false;
    }
    date = dateToDay(date);
    for(var n = 0 ; n < mensa.Tagesmenue.length; n++) {
        var tag = mensa.Tagesmenue[n].tag;
        if((tag - date) == 0) {
            return mensa.Tagesmenue[n];
        }
    }
    return null;
}

function prettyDate(theDate) {
    if(theDate == null) return "#error#";
    var str = '';
    
    var cmpDate = dateToDay(new Date());
    if((theDate - cmpDate) == 0) return "Heute";
    else if ((theDate - cmpDate) == -ONE_DAY) return "Gestern";
    else if ((theDate - cmpDate) == ONE_DAY) return "Morgen";
    
    switch(theDate.getDay()) {
        case 0: str += 'Sonntag, '; break;
        case 1: str += 'Montag, ';  break;
        case 2: str += 'Dienstag, ';  break;
        case 3: str += 'Mittwoch, ';  break;
        case 4: str += 'Donnerstag, ';  break;
        case 5: str += 'Freitag, ';  break;
        case 6: str += 'Samstag, ';  break;
        default: str += '???, ';  break;
    }
    
    str += theDate.getDate() + '.' + (theDate.getMonth()+1) + '.';
    
    return str;
}

function dateToDay(date) {
    if(!date) {
        alert("invalid usage: date is invalid");
        return false;
    }
    // set the hours, minutes, seconds and milliseconds to zero
    date.setHours(0);
    date.setMinutes(0);
    date.setSeconds(0);
    date.setMilliseconds(0);    
    return date;
}

function getURLForMensa(ort, mensa) {
    var ret = "http://namnam.bytewerk.org/files/Studiwerk-Erlangen-Nuernberg-Mensa-";
     switch(ort) {
        case 'Ingolstadt':
            ret += "IN";
            break;
        case 'Eichstätt':
            ret += "EI";
            break;
        case 'Ansbach':
            ret += "Ansbach";
            break;
        case 'Erlangen':
            switch(mensa){
                case 'Süd':
                    ret += "Sued-Erlangen";
                    break;
                case 'Langemarckplatz':
                    ret += "Erlangen-Langemarckplatz";
                    break;
                default:
                    return false;
            }
            break;
        case 'Nürnberg':
            switch(mensa){
                case 'Mensateria':
                    ret += "Mensateria-N";
                    break;
                case 'Regensburgerstr.':
                    ret += "Regensburgerstr.-N";
                    break;
                case 'Schütt':
                    ret += "Schuett-N";
                    break;
                default:
                    return false;
            }
            break;
        default:
            return false;
    }
    return ret + ".json";
}
