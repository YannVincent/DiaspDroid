package fr.android.scaron.diaspdroid.model;

/**
 * Created by Maison on 12/01/2015.
 */
public class OpenGraphCache {
//    "open_graph_cache": {
//        "title": "Un Pav\u00e9 Dans l'Asphalte - Unfamous Resistenza",
    String title;
//        "ob_type": "website",
    String ob_type;
//        "image": "https://fbcdn-sphotos-g-a.akamaihd.net/hphotos-ak-ash4/603100_398394743582765_1448253908_n.jpg",
    String image;
//        "description": "100 artistes alternatifs - 100 morceaux \u00e0 t\u00e9l\u00e9charger gratuitement !",
    String description;
//        "url": "http://www.unpavedanslasphalte.com/"
    String url;
//    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOb_type() {
        return ob_type;
    }

    public void setOb_type(String ob_type) {
        this.ob_type = ob_type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
