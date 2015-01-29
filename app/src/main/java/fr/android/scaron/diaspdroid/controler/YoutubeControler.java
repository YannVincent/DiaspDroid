package fr.android.scaron.diaspdroid.controler;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Sébastien on 29/01/2015.
 */
public class YoutubeControler {

    private static Logger LOGGEUR = LoggerFactory.getLogger(YoutubeControler.class);
    private static LogControler LOG = LogControler.getInstance(LOGGEUR);

    public static Map<String,String> rtspMapping = new Hashtable<String, String>();

    public static String getUrlVideoRTSP(final Context context, final String urlYoutube, final VideoView videoView) {
        try  {
            String gdy = "http://gdata.youtube.com/feeds/api/videos/";
            String id = extractYoutubeId(urlYoutube);
            String result = urlYoutube;

            FutureCallback<String> stringFutureCallback = new FutureCallback<String>() {
                @Override
                public void onCompleted(Exception e, String result) {
                    //<?xml version='1.0' encoding='UTF-8'?><entry xmlns='http://www.w3.org/2005/Atom' xmlns:gd='http://schemas.google.com/g/2005' xmlns:yt='http://gdata.youtube.com/schemas/2007' xmlns:media='http://search.yahoo.com/mrss/'><id>http://gdata.youtube.com/feeds/api/videos/todyDQVqqiE</id><published>2012-12-19T14:48:13.000Z</published><updated>2015-01-14T05:40:43.000Z</updated><category scheme='http://schemas.google.com/g/2005#kind' term='http://gdata.youtube.com/schemas/2007#video'/><category scheme='http://gdata.youtube.com/schemas/2007/categories.cat' term='People' label='People et blogs'/><title type='text'>YANN DESTAL - Rise and fall</title><content type='text'/><link rel='alternate' type='text/html' href='http://www.youtube.com/watch?v=todyDQVqqiE&amp;feature=youtube_gdata'/><link rel='http://gdata.youtube.com/schemas/2007#video.related' type='application/atom+xml' href='http://gdata.youtube.com/feeds/api/videos/todyDQVqqiE/related'/><link rel='http://gdata.youtube.com/schemas/2007#mobile' type='text/html' href='http://m.youtube.com/details?v=todyDQVqqiE'/><link rel='self' type='application/atom+xml' href='http://gdata.youtube.com/feeds/api/videos/todyDQVqqiE'/><author><name>Amandine Turcq</name><uri>http://gdata.youtube.com/feeds/api/users/TurcqAmandine</uri></author><gd:comments><gd:feedLink rel='http://gdata.youtube.com/schemas/2007#comments' href='http://gdata.youtube.com/feeds/api/videos/todyDQVqqiE/comments' countHint='7'/></gd:comments><yt:hd/><media:group><media:category label='People et blogs' scheme='http://gdata.youtube.com/schemas/2007/categories.cat'>People</media:category><media:content url='http://www.youtube.com/v/todyDQVqqiE?version=3&amp;f=videos&amp;app=youtube_gdata' type='application/x-shockwave-flash' medium='video' isDefault='true' expression='full' duration='251' yt:format='5'/><media:content url='rtsp://r5---sn-cg07luel.c.youtube.com/CiILENy73wIaGQkhqmoFDXKHthMYDSANFEgGUgZ2aWRlb3MM/0/0/0/video.3gp' type='video/3gpp' medium='video' expression='full' duration='251' yt:format='1'/><media:content url='rtsp://r5---sn-cg07luel.c.youtube.com/CiILENy73wIaGQkhqmoFDXKHthMYESARFEgGUgZ2aWRlb3MM/0/0/0/video.3gp' type='video/3gpp' medium='video' expression='full' duration='251' yt:format='6'/><media:description type='plain'/><media:keywords/><media:player url='http://www.youtube.com/watch?v=todyDQVqqiE&amp;feature=youtube_gdata_player'/><media:thumbnail url='http://i.ytimg.com/vi/todyDQVqqiE/0.jpg' height='360' width='480' time='00:02:05.500'/><media:thumbnail url='http://i.ytimg.com/vi/todyDQVqqiE/1.jpg' height='90' width='120' time='00:01:02.750'/><media:thumbnail url='http://i.ytimg.com/vi/todyDQVqqiE/2.jpg' height='90' width='120' time='00:02:05.500'/><media:thumbnail url='http://i.ytimg.com/vi/todyDQVqqiE/3.jpg' height='90' width='120' time='00:03:08.250'/><media:title type='plain'>YANN DESTAL - Rise and fall</media:title><yt:duration seconds='251'/></media:group><gd:rating average='5.0' max='5' min='1' numRaters='37' rel='http://schemas.google.com/g/2005#overall'/><yt:statistics favoriteCount='0' viewCount='4240'/></entry>
                    boolean url3gpFound = false;
                    String rtspUrl = "";
                    //On trouve le <media:content ...... /> qui contient type='video/3gpp' et  yt:format='1'
                    //<media:content url='rtsp://r5---sn-cg07luel.c.youtube.com/CiILENy73wIaGQkhqmoFDXKHthMYDSANFEgGUgZ2aWRlb3MM/0/0/0/video.3gp' type='video/3gpp' medium='video' expression='full' duration='251' yt:format='1'/>
                    while (!url3gpFound) {
                        int beginMediaContent = result.indexOf("<media:content");
                        if (beginMediaContent>-1) {
                            int endMediaContent = result.indexOf("/>", beginMediaContent + "<media:content".length());
//                            if (endMediaContent>-1) {
                            endMediaContent = endMediaContent + "/>".length();
                            String resultTroncated = result.substring(beginMediaContent, endMediaContent);
                            LOG.d("media content found is " + resultTroncated);
                            if (resultTroncated.contains("type='video/3gpp'") && resultTroncated.contains("yt:format='1'")) {
                                int beginMediaContentUrl = resultTroncated.indexOf("url='");
//                                    if (beginMediaContentUrl > -1) {
                                beginMediaContentUrl = beginMediaContentUrl + "url='".length();
                                int endMediaContentUrl = resultTroncated.indexOf("'", beginMediaContentUrl);
//                                        if (endMediaContentUrl>-1) {
                                rtspUrl = resultTroncated.substring(beginMediaContentUrl, endMediaContentUrl);
                                rtspMapping.put(urlYoutube, rtspUrl);
                                LOG.d("\t ** rtspUrl found is " + rtspUrl + " **");
                                url3gpFound = true;
//                                        }
//                                    }else{
//
//                                    }
                            } else {
                                // On tronque le resultat pour trouver le media suivant
                                result = result.substring(endMediaContent);
                            }
//                            }else{
//                            }
                        }else{
                            //On ne trouve pas de média
                            rtspUrl="";
                            url3gpFound = true;
                        }
                    }

                    if (url3gpFound && !rtspUrl.isEmpty()) {
//                        videoView.setVideoURI(Uri.parse(rtspUrl));
//                        MediaController mc = new MediaController(context);
//                        videoView.setMediaController(mc);
//                        videoView.requestFocus();
//                        videoView.start();
//                        mc.show();
//                        videoView.setVisibility(View.VISIBLE);

                        // Create a progressbar
                        final ProgressDialog pDialog = new ProgressDialog(context);
                        // Set progressbar title
                        pDialog.setTitle("Android Video Streaming Youtube");
                        // Set progressbar message
                        pDialog.setMessage("Buffering...");
                        pDialog.setIndeterminate(false);
                        pDialog.setCancelable(false);
                        // Show progressbar
                        pDialog.show();

                        try {
                            // Start the MediaController
                            MediaController mediacontroller = new MediaController(context);
                            mediacontroller.setAnchorView(videoView);
                            // Get the URL from String VideoURL
                            Uri video = Uri.parse(rtspUrl);
                            videoView.setMediaController(mediacontroller);
                            videoView.setVideoURI(video);

                        } catch (Exception exception) {
                            LOG.e(".getView Error : "+e.getMessage());
                            exception.printStackTrace();
                        }

                        videoView.requestFocus();
                        videoView.setVisibility(View.VISIBLE);
                        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            // Close the progress bar and play the video
                            public void onPrepared(MediaPlayer mp) {
                                pDialog.dismiss();
                                videoView.start();
                            }
                        });
                    }

                }
            };

//            FutureCallback <InputStream> inputStreamCallback = new FutureCallback<InputStream>() {
//                @Override
//                public void onCompleted(Exception e, InputStream result) {
//                    try {
//                        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//                        Document doc = documentBuilder.parse(result);
//                        Element el = doc.getDocumentElement();
//                        NodeList list = el.getElementsByTagName("media:content");///media:content
//                        String cursor = urlYoutube;
//                        for (int i = 0; i < list.getLength(); i++) {
//                            Node node = list.item(i);
//                            if (node != null) {
//                                NamedNodeMap nodeMap = node.getAttributes();
//                                HashMap<String, String> maps = new HashMap<String, String>();
//                                for (int j = 0; j < nodeMap.getLength(); j++) {
//                                    Attr att = (Attr) nodeMap.item(j);
//                                    maps.put(att.getName(), att.getValue());
//                                }
//                                if (maps.containsKey("yt:format")) {
//                                    String f = maps.get("yt:format");
//                                    if (maps.containsKey("url")) {
//                                        cursor = maps.get("url");
//                                    }
//                                    if (f.equals("1")) {
//                                        rtspMapping.put(urlYoutube, cursor);
//                                        videoView.setVideoURI(Uri.parse(cursor));
//                                        MediaController mc = new MediaController(context);
//                                        videoView.setMediaController(mc);
//                                        videoView.requestFocus();
//                                        videoView.start();
//                                        mc.show();
//                                        videoView.setVisibility(View.VISIBLE);
//                                        return;
////                                        return cursor;
//                                    }
//                                }
//                            }
//                        }
//                        rtspMapping.put(urlYoutube, cursor);
//                        videoView.setVideoURI(Uri.parse(cursor));
//                        MediaController mc = new MediaController(context);
//                        videoView.setMediaController(mc);
//                        videoView.requestFocus();
//                        videoView.start();
//                        mc.show();
//                        videoView.setVisibility(View.VISIBLE);
//                        return;
////                        return cursor;
//                    } catch (Exception ex) {
//                        LOG.e("Get Url Video RTSP Exception======>>" + ex.toString());
//                    }
//                    rtspMapping.put(urlYoutube, urlYoutube);
//                    videoView.setVideoURI(Uri.parse(urlYoutube));
//                    MediaController mc = new MediaController(context);
//                    videoView.setMediaController(mc);
//                    videoView.requestFocus();
//                    videoView.start();
//                    mc.show();
//                    videoView.setVisibility(View.VISIBLE);
//                    return;
////                    return urlYoutube;
//                }
//            };

            Ion.with(context)
                    .load(gdy+id)
                    .asString()
                    .setCallback(stringFutureCallback);
//                    .asInputStream()
//                    .setCallback(inputStreamCallback);

//            //DEBUT CODE ORI
//            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//            URL url = new URL(gdy + id);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            Document doc = documentBuilder.parse(connection.getInputStream());
//            Element el = doc.getDocumentElement();
//            NodeList list = el.getElementsByTagName("media:content");///media:content
//            String cursor = urlYoutube;
//            for (int i = 0; i < list.getLength(); i++)
//            {
//                Node node = list.item(i);
//                if (node != null)
//                {
//                    NamedNodeMap nodeMap = node.getAttributes();
//                    HashMap<String, String> maps = new HashMap<String, String>();
//                    for (int j = 0; j < nodeMap.getLength(); j++)
//                    {
//                        Attr att = (Attr) nodeMap.item(j);
//                        maps.put(att.getName(), att.getValue());
//                    }
//                    if (maps.containsKey("yt:format"))
//                    {
//                        String f = maps.get("yt:format");
//                        if (maps.containsKey("url"))
//                        {
//                            cursor = maps.get("url");
//                        }
//                        if (f.equals("1"))
//                            return cursor;
//                    }
//                }
//            }
//            return cursor;
        } catch (Exception ex) {
            LOG.e("Get Url Video RTSP Exception======>>" + ex.toString());
        }
        return urlYoutube;

    }


    protected static String extractYoutubeId(String url) throws MalformedURLException
    {
        String id = null;
        try
        {
            String query = new URL(url).getQuery();
            if (query != null)
            {
                String[] param = query.split("&");
                for (String row : param)
                {
                    String[] param1 = row.split("=");
                    if (param1[0].equals("v"))
                    {
                        id = param1[1];
                    }
                }
            }
            else
            {
                if (url.contains("embed"))
                {
                    id = url.substring(url.lastIndexOf("/") + 1);
                }
            }
        }
        catch (Exception ex)
        {
            Log.e("Exception", ex.toString());
        }
        return id;
    }
}
