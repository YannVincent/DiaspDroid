package fr.android.scaron.diaspdroid.controler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Sébastien on 24/01/2015.
 */
public class DiaporaControler {

    private static Logger LOGGEUR = LoggerFactory.getLogger(DiaporaControler.class);
    private static LogControler LOG = LogControler.getInstance(LOGGEUR);
    static String POD = "framasphere.org";
    static String POD_URL = "https://"+POD;
    static String LOGIN_URL = POD_URL+"/users/sign_in";
    static String STREAM_URL = POD_URL+"/stream";
    static String POST_IMAGE = POD_URL + "/photos?photo%5Bpending%5D=true";
    static String TOKEN_VIDE = "";
    static String TOKEN_TEST = "4REWL0RLsEU5edVgVWuZL16XGAQkVuCYyzGirHvXjOI=";
    static String COOKIE_SESSION_LOGIN_VIDE = "";
    static String COOKIE_SESSION_LOGIN_TEST="BAh7B0kiD3Nlc3Npb25faWQGOgZFVEkiJWFkOGU5MjQ0NjRmOTM1MWQ1NTQ3NDZlZDc4MmUwYzA5BjsAVEkiEF9jc3JmX3Rva2VuBjsARkkiMTRSRVdMMFJMc0VVNWVkVmdWV3VaTDE2WEdBUWtWdUNZeXpHaXJIdlhqT0k9BjsARg%3D%3D--0f5956ca762a3f995f5d1dfa8c51db8d384f5e63";
    static String COOKIE_SESSION_STREAM_VIDE = "";
    static String COOKIE_SESSION_STREAM_TEST="BAh7CUkiD3Nlc3Npb25faWQGOgZFVEkiJWQzYTc1MDM4MGZkMGVkZDgxOTAzYjBiZWNmNjY3OGUwBjsAVEkiGXdhcmRlbi51c2VyLnVzZXIua2V5BjsAVFsHWwZpAhIJSSIiJDJhJDEwJFNyaGJkL3JLYkFzOWpZSTlxU1lVT08GOwBUSSIKZmxhc2gGOwBUbzolQWN0aW9uRGlzcGF0Y2g6OkZsYXNoOjpGbGFzaEhhc2gJOgpAdXNlZG86CFNldAY6CkBoYXNoewY6C25vdGljZVQ6DEBjbG9zZWRGOg1AZmxhc2hlc3sGOwpJIihWb3VzIMOqdGVzIMOgIHByw6lzZW50IGNvbm5lY3TDqS1lLgY7AFQ6CUBub3cwSSIQX2NzcmZfdG9rZW4GOwBGSSIxNE5oeVJKdWdqVll2ZDVwaXhRQ1NCZmZlbkl6bzRMZE1SZUx0UEVVVDJFdz0GOwBG--b1db6d257b315bfff3076ee8112f9a75a45d85ef";
    static String COOKIE_REMEMBER_TEST_VIDE = "";
    static String COOKIE_REMEMBER_TEST = "BAhbB1sGaQISCUkiIiQyYSQxMCRTcmhiZC9yS2JBczlqWUk5cVNZVU9PBjoGRVQ%3D--a111fa31a16b0451130d7598978cda8257466368; path=/; expires=Sun, 08-Feb-2015 21:52:35 GMT; HttpOnly; secure";

    static String TOKEN = TOKEN_VIDE;
    static String COOKIE_SESSION_LOGIN = COOKIE_SESSION_LOGIN_VIDE;
    static String COOKIE_SESSION_STREAM = COOKIE_SESSION_STREAM_VIDE;
    static String COOKIE_REMEMBER = COOKIE_REMEMBER_TEST_VIDE;
}
