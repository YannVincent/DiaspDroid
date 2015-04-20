package fr.android.scaron.diaspdroid.model;

import java.util.List;

/**
 * Created by Sébastien on 19/02/2015.
 */
/*
{
        "podcount": 173,
        "pods": [
        {
        "id": "147",
        "domain": "socializer.cc",
        "status": "up",
        "secure": "true",
        "score": "20",
        "userrating": "8.71",
        "adminrating": "0",
        "city": null,
        "state": "",
        "country": "DE",
        "lat": "51.8",
        "long": "10.5",
        "ip": "81.7.10.80",
        "ipv6": "yes",
        "hgitdate": "2014-12-06 12:16:00 +0100",
        "hgitref": "722d97d580add71aa62d5193fb843efb637d062d",
        "pingdomurl": "http:\/\/stats.pingdom.com\/qi002hk00udk\/632639",
        "pingdomlast": null,
        "monthsmonitored": "31",
        "uptimelast7": "99.56",
        "responsetimelast7": "670 ms",
        "hruntime": "4.470710",
        "hencoding": null,
        "datecreated": "2012-08-24 02:21:30.051104",
        "dateupdated": "2015-02-19 13:11:10",
        "datelaststats": "2015-02-19 13:11:10",
        "hidden": "no"
        },
        {
        "id": "114",
        "domain": "podmob.net",
        "status": "up",
        "secure": "true",
        "score": "20",
        "userrating": "0",
        "adminrating": "0",
        "city": null,
        "state": "",
        "country": "DE",
        "lat": "51.8",
        "long": "9.7",
        "ip": "78.46.173.153",
        "ipv6": "no",
        "hgitdate": "2014-10-30 21:30:03 +0100",
        "hgitref": "5cabd6924bc77a1cad06a139459c592b239b9e39",
        "pingdomurl": "http:\/\/stats.pingdom.com\/7cu63v1lou3y\/492821",
        "pingdomlast": null,
        "monthsmonitored": "37",
        "uptimelast7": "99.78",
        "responsetimelast7": "1,084 ms",
        "hruntime": "0.295354",
        "hencoding": null,
        "datecreated": "2012-02-25 01:32:11.515394",
        "dateupdated": "2015-02-19 13:11:12",
        "datelaststats": "2015-02-19 13:11:12",
        "hidden": "no"
        },

*/
public class Pods {

    private int podcount;
    private List<Pod> pods;

    public int getPodcount() {
        return podcount;
    }

    public void setPodcount(int podcount) {
        this.podcount = podcount;
    }

    public List<Pod> getPods() {
        return pods;
    }

    public void setPods(List<Pod> pods) {
        this.pods = pods;
    }
}
