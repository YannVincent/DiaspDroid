package org.coding4coffee.diaspora.api;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.coding4coffee.diaspora.api.exceptions.AspectsNotFoundException;
import org.coding4coffee.diaspora.api.exceptions.CsrfTokenNotFoundException;
import org.coding4coffee.diaspora.api.exceptions.NotLoggedInException;
import org.coding4coffee.diaspora.api.exceptions.PodFailureException;
import org.coding4coffee.diaspora.api.exceptions.PostingException;
import org.coding4coffee.diaspora.api.upload.ProgressByteArrayEntity;
import org.coding4coffee.diaspora.api.upload.ProgressListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.android.scaron.diaspdroid.controler.LogControler;

/**
 * @author Benjamin Neff
 */
public class DiasporaClientImpl implements DiasporaClient {

	private static final Pattern CSRF_TOKEN_REGEX = Pattern.compile("content=\"([^\"]*)\"");

    private static Logger LOGGEUR = LoggerFactory.getLogger(DiasporaClientImpl.class);
    private static LogControler LOG = LogControler.getLoggeur(LOGGEUR);
    private static final String TAG = "DiasporaClientImpl";

	private final String podUrl;

	private final HttpClient session;
    private Header[] cookies;
    private String authenticityToken;

	DiasporaClientImpl(final String podUrl, final ClientConnectionManager cm) {
		this.podUrl = podUrl;

		final HttpParams httpParams = new BasicHttpParams();
		HttpClientParams.setRedirecting(httpParams, false);
		session = new DefaultHttpClient(cm, httpParams);
	}
    private String getAuthenticationToken() throws IOException, PodFailureException {
        String TAG_METHOD = TAG + ".getAuthenticationToken : ";
        LOG.d(TAG_METHOD + "Entrée");
        LOG.d(TAG_METHOD + "Requete HttpGet pour l'url " + podUrl + "/users/sign_in");
        final HttpGet aspectsRequest = new HttpGet(podUrl + "/users/sign_in");

        try {
            // send request
            LOG.d(TAG_METHOD + "Lancement de la requete HttpGet");
            final HttpResponse response = session.execute(aspectsRequest);
            // read response
            LOG.d(TAG_METHOD + "On lit le contenu de la réponse");
            final InputStream content = response.getEntity().getContent();
            final BufferedReader br = new BufferedReader(new InputStreamReader(content));

            String strLine;
            while ((strLine = br.readLine()) != null) {
//                // read until the csrf-token is found
//                LOG.d(TAG_METHOD + "On cherche le authenticity_token dans le contenu de la réponse ("+strLine+")");
//                // exemple de ligne : <input name="authenticity_token" type="hidden" value="XQJvmyRWQA+T2bIdLc4v6DZyn9dE22LMRP+8bfV1SWk=" />
//                if (strLine.contains("authenticity_token")) {
//                    LOG.d(TAG_METHOD + "On a trouvé le authenticity_token dans le contenu de la réponse");
//                    // skip the rest of the content
//                    LOG.d(TAG_METHOD + "On ingnore le reste du contenu de la réponse");
//                    response.getEntity().consumeContent();
//                    LOG.d(TAG_METHOD + "On isole le csrf-token dans le contenu de la ligne séléctionné");
//                    int indexDepartSearch = strLine.indexOf("<input name=\"authenticity_token\" type=\"hidden\" value=\"");
//                    int indexFinSearch = strLine.indexOf("\"", indexDepartSearch+"<input name=\"authenticity_token\" type=\"hidden\" value=\"".length());
//                    String authenticityToken = strLine.substring(indexDepartSearch+"<input name=\"authenticity_token\" type=\"hidden\" value=\"".length(),indexFinSearch);
//                    LOG.d(TAG_METHOD + "On retourne la valeur obtnue '"+authenticityToken+"'");
//                    return authenticityToken;
                // read until the csrf-token is found
                LOG.d(TAG_METHOD + "On cherche le csrf-token dans le contenu de la réponse ("+strLine+")");
                // exemple de ligne : <meta content="DY/tUHX9075K9SkfOrm7d33SbmU7ta7vRwoFNBezNdA=" name="csrf-token">
                if (strLine.contains("csrf-token")) {
                    LOG.d(TAG_METHOD + "On a trouvé le csrf-token dans le contenu de la réponse");
                    // skip the rest of the content
                    LOG.d(TAG_METHOD + "On ingnore le reste du contenu de la réponse");
                    response.getEntity().consumeContent();
                    LOG.d(TAG_METHOD + "On isole le csrf-token dans le contenu de la ligne séléctionné");
                    final Matcher csrfMatcher = CSRF_TOKEN_REGEX.matcher(strLine);
                    if (csrfMatcher.find()) {
                        LOG.d(TAG_METHOD + "On retourne la valeur obtnue '"+csrfMatcher.group(1)+"'");
                        return csrfMatcher.group(1);
                    }
                    break;
                }
            }
            throw new CsrfTokenNotFoundException(
                    "authenticity_token couldn't be found! Probably the diaspora behavior has changed.");
        } catch (final IOException e) {
            // reset http connection
            aspectsRequest.abort();
            LOG.e(TAG_METHOD + "IOException rencontrée : "+e.getMessage(),e);
            throw e;
        }
    }

	@Override
	public boolean login(final String username, final String password) throws IOException {
        String TAG_METHOD = TAG + ".login : ";
        LOG.d(TAG_METHOD + "Entrée");


        LOG.d(TAG_METHOD + "Requete HttpPost pour l'url " + podUrl + "/users/sign_in");
		final HttpPost signInRequest = new HttpPost(podUrl + "/users/sign_in");

		try {
            authenticityToken = getAuthenticationToken();
			// add parameters
			final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            LOG.d(TAG_METHOD + "Ajout des elements à l'entitée  user[username]=" + username);
			nameValuePairs.add(new BasicNameValuePair("user[username]", username));
            LOG.d(TAG_METHOD + "Ajout des elements à l'entitée  user[password]=" + password);
			nameValuePairs.add(new BasicNameValuePair("user[password]", password));
            LOG.d(TAG_METHOD + "Ajout des elements à l'entitée  user[remember_me]=" + "1");
			nameValuePairs.add(new BasicNameValuePair("user[remember_me]", "1"));
            LOG.d(TAG_METHOD + "Ajout des elements à l'entitée  user[remember_me]=" + "1");
            nameValuePairs.add(new BasicNameValuePair("authenticity_token", authenticityToken));
            LOG.d(TAG_METHOD + "Ajout de l'entitée à la requete HttpPost");
			signInRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// send request
            LOG.d(TAG_METHOD + "Lancement de la requete HttpPost");
			final HttpResponse response = session.execute(signInRequest);
            cookies= response.getHeaders("Cookie");
            if (cookies==null){
                cookies = response.getHeaders("cookie");
            }
			// ignore content
            LOG.d(TAG_METHOD + "On ignore le contenu de la réponse");
			response.getEntity().consumeContent();

			// successful if redirect to startpage
            LOG.d(TAG_METHOD + "Si nous sommes redirigés le login est accepté");
			return isRedirected(response);
		} catch (final IOException e) {
            LOG.e(TAG_METHOD + "IOException rencontrée " + e.getMessage(), e);
			// reset http connection
			signInRequest.abort();
			throw e;
		} catch (PodFailureException e) {
            LOG.e(TAG_METHOD + "PodFailureException rencontrée " + e.getMessage(), e);
            // reset http connection
            signInRequest.abort();
            throw new IOException(e);
        }
    }

	@Override
	public String createPost(final String text, final String aspect) throws IOException, PodFailureException {
		return createPost(text, Arrays.asList(new String[] { aspect }));
	}

	@Override
	public String createPost(final String text, final Collection<String> aspects) throws IOException,
			PodFailureException {
        String TAG_METHOD = TAG + ".createPost : ";
        LOG.d(TAG_METHOD + "Entrée");
		// get CSRF token (and check if logged in)
		final String csrfToken = getCsrfToken();

		final HttpPost postRequest = new HttpPost(podUrl + "/status_messages");

		try {
			// add header
			postRequest.addHeader("content-type", "application/json");
			postRequest.addHeader("accept", "application/json");
			postRequest.addHeader("X-CSRF-Token", csrfToken);

			// build json with post data
			final JSONObject post = new JSONObject();
			post.put("status_message", new JSONObject().put("text", text));
			post.put("aspect_ids", new JSONArray(aspects));

			// add json to request
			postRequest.setEntity(new StringEntity(post.toString()));

			// send request
			final HttpResponse response = session.execute(postRequest);
			if (response.getStatusLine().getStatusCode() == 201) { // successful
				// get guid
				final JSONObject postInfo = new JSONObject(EntityUtils.toString(response.getEntity()));
				return postInfo.getString("guid");
			}
			// ignore content if not successful
			response.getEntity().consumeContent();
			throw new PostingException("Error while creating the post! Probably the diaspora behavior has changed.");
		} catch (final IOException e) {
			// reset http connection
			postRequest.abort();
			throw e;
		} catch (final ParseException e) {
			// reset http connection
			postRequest.abort();
			throw new IOException(e);
		} catch (final JSONException e) {
			// reset http connection
			postRequest.abort();
			throw new PodFailureException(e);
		}
	}

	@Override
	public String uploadPhoto(final byte[] photoBytes, final ProgressListener listener) throws IOException,
			PodFailureException {
        String TAG_METHOD = TAG + ".uploadPhoto : ";
        LOG.d(TAG_METHOD + "Entrée");
		// get CSRF token (and check if logged in)
        LOG.d(TAG_METHOD + "On récupère le csrf-token pour vérifier si on est authentifié");
		final String csrfToken = getCsrfToken();

        LOG.d(TAG_METHOD + "Requete HttpPost pour l'url " + podUrl + "/photos?photo%5Baspect_ids%5D=all&qqfile=uploaded.jpg");
		final HttpPost photoRequest = new HttpPost(podUrl + "/photos?photo%5Baspect_ids%5D=all&qqfile=uploaded.jpg");

		try {
			// add header
            LOG.d(TAG_METHOD + "On ajoute le header à la requete HttpPost pour l'url, content-type=" + "application/octet-stream");
			photoRequest.addHeader("content-type", "application/octet-stream");
            LOG.d(TAG_METHOD + "On ajoute le header à la requete HttpPost pour l'url, X-CSRF-Token=" + csrfToken);
			photoRequest.addHeader("X-CSRF-Token", csrfToken);

            LOG.d(TAG_METHOD + "On crée l'entité photo à partir des données brutes");
			final HttpEntity photoEntity = new ProgressByteArrayEntity(photoBytes, listener);
            LOG.d(TAG_METHOD + "On ajoute l'entité photo à la requete HttpPost");
			photoRequest.setEntity(photoEntity);

			// send request
            LOG.d(TAG_METHOD + "Lancement de la requete HttpPost");
			final HttpResponse response = session.execute(photoRequest);
            LOG.d(TAG_METHOD + "Code du statut de la requete HttpPost : "+response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() == 200) { // successful
				// get guid
                LOG.d(TAG_METHOD + "Récupération de l'objet photoJson résultat");
				final JSONObject photoJson = new JSONObject(EntityUtils.toString(response.getEntity()));
                LOG.d(TAG_METHOD + "Récupération de l'objet photoData contenu dans l'objet photoJson");
				final JSONObject photoData = photoJson.getJSONObject("data").getJSONObject("photo");
                LOG.d(TAG_METHOD + "Récupération de l'url dans l'objet photoData contenu dans l'objet photoJson");
                final String photoDataUrl = photoData.getJSONObject("unprocessed_image").getString("url");
                LOG.d(TAG_METHOD + "photoDataUrl : " + photoDataUrl);
                LOG.d(TAG_METHOD + "photoDataGuid : " + photoData.getString("guid"));
                return photoDataUrl;
			}
			// ignore content if not successful
			// response.getEntity().consumeContent();
            LOG.d(TAG_METHOD + "Réponse non attendue : " + EntityUtils.toString(response.getEntity()));
			// TODO
			throw new PodFailureException("Error while creating the post! Probably the diaspora behavior has changed.");
		} catch (final IOException e) {
			// reset http connection
			photoRequest.abort();
            LOG.e(TAG_METHOD + "IOException survenue : " + e.getMessage(), e);
			throw e;
		} catch (final ParseException e) {
			// reset http connection
			photoRequest.abort();
            LOG.e(TAG_METHOD + "ParseException survenue : " + e.getMessage(), e);
			throw new IOException(e);
		} catch (final JSONException e) {
			// reset http connection
			photoRequest.abort();
            LOG.e(TAG_METHOD + "JSONException survenue : " + e.getMessage(), e);
			throw new PodFailureException(e);
		}
	}

	@Override
	public Map<String, String> getAspects() throws IOException, PodFailureException {
        String TAG_METHOD = TAG + ".getAspects : ";
        LOG.d(TAG_METHOD + "Entrée");
		final HttpGet aspectsRequest = new HttpGet(podUrl + "/bookmarklet");

		try {
			// send request
			final HttpResponse response = session.execute(aspectsRequest);
			// read response
			final InputStream content = response.getEntity().getContent();
			final BufferedReader br = new BufferedReader(new InputStreamReader(content));

			String strLine;
			while ((strLine = br.readLine()) != null) {
				// read until the user attributes are found
				if (strLine.contains("window.current_user_attributes")) {
					// get json
					final String jsonString = strLine.substring(strLine.indexOf("= ") + 2);
					// parse json
					final JSONObject userInfo = new JSONObject(jsonString);
					final JSONArray aspects = userInfo.getJSONArray("aspects");

					final Map<String, String> aspectMap = new HashMap<String, String>();
					for (int i = 0; i < aspects.length(); ++i) {
						// read all aspects and add to map
						final JSONObject aspect = aspects.getJSONObject(i);
						aspectMap.put(aspect.getString("id"), aspect.getString("name"));
					}

					// skip the rest of the content
					response.getEntity().consumeContent();
					return aspectMap;
				}
			}
			throw isRedirected(response) ? new NotLoggedInException("Not logged in!") : new AspectsNotFoundException(
					"No user attributes found in response! Probably the diaspora behavior has changed.");
		} catch (final IOException e) {
			// reset http connection
			aspectsRequest.abort();
			throw e;
		} catch (final JSONException e) {
			// reset http connection
			aspectsRequest.abort();
			throw new PodFailureException(e);
		}
	}

	private String getCsrfToken() throws IOException, PodFailureException {
        String TAG_METHOD = TAG + ".getCsrfToken : ";
        LOG.d(TAG_METHOD + "Entrée");
        LOG.d(TAG_METHOD + "Requete HttpGet pour l'url " + podUrl + "/bookmarklet");
		final HttpGet aspectsRequest = new HttpGet(podUrl + "/bookmarklet");

		try {
			// send request
            LOG.d(TAG_METHOD + "Lancement de la requete HttpGet");
			final HttpResponse response = session.execute(aspectsRequest);
			// read response
            LOG.d(TAG_METHOD + "On lit le contenu de la réponse");
			final InputStream content = response.getEntity().getContent();
			final BufferedReader br = new BufferedReader(new InputStreamReader(content));

			String strLine;
			while ((strLine = br.readLine()) != null) {
				// read until the csrf-token is found
                LOG.d(TAG_METHOD + "On cherche le csrf-token dans le contenu de la réponse ("+strLine+")");
                // exemple de ligne : <meta content="DY/tUHX9075K9SkfOrm7d33SbmU7ta7vRwoFNBezNdA=" name="csrf-token">
				if (strLine.contains("csrf-token")) {
                    LOG.d(TAG_METHOD + "On a trouvé le csrf-token dans le contenu de la réponse");
					// skip the rest of the content
                    LOG.d(TAG_METHOD + "On ingnore le reste du contenu de la réponse");
					response.getEntity().consumeContent();
                    LOG.d(TAG_METHOD + "On isole le csrf-token dans le contenu de la ligne séléctionné");
					final Matcher csrfMatcher = CSRF_TOKEN_REGEX.matcher(strLine);
					if (csrfMatcher.find()) {
                        LOG.d(TAG_METHOD + "On retourne la valeur obtnue '"+csrfMatcher.group(1)+"'");
						return csrfMatcher.group(1);
					}
					break;
				}
			}
			throw isRedirected(response) ? new NotLoggedInException("Not logged in!") : new CsrfTokenNotFoundException(
					"CSRF-Token couldn't be found! Probably the diaspora behavior has changed.");
		} catch (final IOException e) {
			// reset http connection
			aspectsRequest.abort();
            LOG.e(TAG_METHOD + "IOException rencontrée : "+e.getMessage(),e);
			throw e;
		}
	}

	private boolean isRedirected(final HttpResponse response) {
        String TAG_METHOD = TAG + ".isRedirected : ";
        LOG.d(TAG_METHOD + "Entrée");
        LOG.d(TAG_METHOD + "Sortie avec la valeur "+(response.getStatusLine().getStatusCode() == 302));
		return response.getStatusLine().getStatusCode() == 302;
	}
}
