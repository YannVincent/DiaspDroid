package org.coding4coffee.diaspora.api.ssl;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * @author Benjamin Neff
 */
public class MockSSLSocketFactory extends SSLSocketFactory {

	public MockSSLSocketFactory() throws NoSuchAlgorithmException, KeyManagementException, CertificateException, KeyStoreException, IOException, UnrecoverableKeyException {
		super(getMockSSLContextv2());
	}





    private static KeyStore getMockSSLContextv2() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, IOException, CertificateException {
        final KeyStore trusted = KeyStore.getInstance("SSL");
        trusted.load(null, "".toCharArray());
        return trusted;
    }

	private static SSLContext getMockSSLContext() throws NoSuchAlgorithmException, KeyManagementException {
		final SSLContext sslContext = SSLContext.getInstance("SSL");

		// set up a TrustManager that trusts everything
		sslContext.init(null, new TrustManager[] { new X509TrustManager() {
			@Override
			public X509Certificate[] getAcceptedIssuers() {
				// ignore check
				return null;
			}

			@Override
			public void checkClientTrusted(final X509Certificate[] certs, final String authType) {
				// ignore check
			}

			@Override
			public void checkServerTrusted(final X509Certificate[] certs, final String authType) {
				// ignore check
			}
		} }, new SecureRandom());
		return sslContext;
	}
}
