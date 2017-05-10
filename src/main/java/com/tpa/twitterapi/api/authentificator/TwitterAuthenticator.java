/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tpa.twitterapi.api.authentificator;

import com.google.api.client.auth.oauth.OAuthAuthorizeTemporaryTokenUrl;
import com.google.api.client.auth.oauth.OAuthCredentialsResponse;
import com.google.api.client.auth.oauth.OAuthGetAccessToken;
import com.google.api.client.auth.oauth.OAuthGetTemporaryToken;
import com.google.api.client.auth.oauth.OAuthHmacSigner;
import com.google.api.client.auth.oauth.OAuthParameters;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.tpa.twitterapi.tools.singleton.logger.LoggerSingleton;
import com.tpa.twitterapi.tools.singleton.property.PropertySingleton;
import com.tpa.twitterapi.tools.singleton.scanner.ScannerSingleton;
import com.tpa.twitterapi.exception.TwitterAuthenticationException;
import com.tpa.twitterapi.exception.WrongConversionException;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * You just to call the default constructor to use this class.
 * 
 * User will have to:
 * 
 * 1. Visit the random link to Twitter authentification and to authorize the Twitter application
 * 2. Copy/paste the given PIN in the console
 * 
 * @author Alexandre Veremme @ The POC Agency | alex [at] the-poc-agency.com
 */
public final class TwitterAuthenticator {
    
    protected static final PropertySingleton PROPERTY_SINGLETON = PropertySingleton.getInstance();
    protected static final Logger LOGGER = LoggerSingleton.getInstance().getLogger(TwitterAuthenticator.class);

    // Required for authentification
    private static final String AUTHORIZE_URL = "https://api.twitter.com/oauth/authorize";
    private static final String ACCESS_TOKEN_URL = "https://api.twitter.com/oauth/access_token";
    private static final String REQUEST_TOKEN_URL = "https://api.twitter.com/oauth/request_token";
    
    private String consumerKey;
    private String consumerSecret;

    private HttpRequestFactory httpRequestFactory;
    private HttpTransport httpTransport;

    public TwitterAuthenticator() throws TwitterAuthenticationException {
        setHttpRequestFactory();
    }
    
    /**
     * 
     * @return the consumer key from the config.properties
     */
    private synchronized String getConsumerKey() {
        
        if (consumerKey == null) {
            consumerKey = PROPERTY_SINGLETON.getProperty("twitter.consumerKey");
            LOGGER.info("Consumer key acquired");
        }
        
        return consumerKey;
    }

    /**
     * 
     * @return the consumer secret from the config.properties
     */
    private synchronized String getConsumerSecret() {
        
        if (consumerKey == null) {
            consumerSecret = PROPERTY_SINGLETON.getProperty("twitter.consumerSecret");
            LOGGER.info("Consumer secret acquired");
        }
        
        return consumerSecret;
    }
    
    /**
     * 
     * @return the default NetHttpTransport
     */
    private synchronized HttpTransport getHttpTransport() {
        
        if (httpTransport == null) {
            httpTransport = new NetHttpTransport();
        }
        
        return httpTransport;
    }
    
    /**
     * This is the first step of the authentification
     * 
     * The input signer must have the property "clientSharedSecret", equals to getConsumerSecret()
     * 
     * @param signer OAuthHmacSigner
     * @return the OAuthTemporaryToken
     * @throws TwitterAuthenticationException 
     */
    private synchronized String getOAuthTemporaryToken(OAuthHmacSigner signer) throws TwitterAuthenticationException {
        
        OAuthGetTemporaryToken requestToken = new OAuthGetTemporaryToken(REQUEST_TOKEN_URL);
        requestToken.consumerKey = getConsumerKey();
        requestToken.transport = getHttpTransport();
        requestToken.signer = signer;

        OAuthCredentialsResponse requestTokenResponse;
        try {
            requestTokenResponse = requestToken.execute();
        } catch (IOException e) {
            throw new TwitterAuthenticationException("Unable to acquire temporary token: " + e.getMessage(), e);
        }
        
        LOGGER.info("Temporary token acquired");
        
        return requestTokenResponse.token;
    }
    
    /**
     * This is the second step of the authentification
     * 
     * The input is obtained by the method getOAuthTemporaryToken()
     * 
     * User will have to give the retrieved PIN after visiting the random Twitter link
     * 
     * @param signer OAuthHmacSigner
     * @return the authorized Pin
     * @throws TwitterAuthenticationException 
     */
    private synchronized String getOAuthAuthorizePin(String oAuthTemporaryToken) throws TwitterAuthenticationException {
        
        OAuthAuthorizeTemporaryTokenUrl oAuthAuthorizeTemporaryTokenUrl = new OAuthAuthorizeTemporaryTokenUrl(AUTHORIZE_URL);
        oAuthAuthorizeTemporaryTokenUrl.temporaryToken = oAuthTemporaryToken;

        String authorizePin;
        try {
            authorizePin = ScannerSingleton.getInstance().scanValue(String.class, null, "Go to the following link in your browser and copy/paste the retreived PIN: " + oAuthAuthorizeTemporaryTokenUrl.build());
        } catch (WrongConversionException ex) {
            throw new TwitterAuthenticationException("Unable to read entered PIN");
        }
        
        return authorizePin;
    }
    
    /**
     * This is the third step of the authentification
     * 
     * This method uses values from the 2 last steps
     * 
     * @param signer OAuthHmacSigner
     * @return the OAuthCredentialsResponse
     * @throws TwitterAuthenticationException 
     */
    private synchronized OAuthCredentialsResponse getOAuthCredentialsResponse(OAuthHmacSigner signer, String authorizePin, String oAuthTemporaryToken) throws TwitterAuthenticationException {
        
        OAuthGetAccessToken oAuthGetAccessToken = new OAuthGetAccessToken(ACCESS_TOKEN_URL);
        oAuthGetAccessToken.signer = signer;
        oAuthGetAccessToken.verifier = authorizePin;
        oAuthGetAccessToken.temporaryToken = oAuthTemporaryToken;
        oAuthGetAccessToken.consumerKey = getConsumerKey();
        
        oAuthGetAccessToken.transport = getHttpTransport();
        
        OAuthCredentialsResponse oAuthCredentialsResponse;
        try {
            oAuthCredentialsResponse = oAuthGetAccessToken.execute();
        } catch (IOException e) {
            throw new TwitterAuthenticationException("Unable to authorize access: " + e.getMessage(), e);
        }
        
        LOGGER.info("Authorization was successful");
        
        return oAuthCredentialsResponse;
    }
    
    /**
     * This method is called by the default constructor
     * 
     * And creates the HttpRequestFactory, you will be able to use in your next calls
     * 
     * @throws TwitterAuthenticationException 
     */
    private synchronized void setHttpRequestFactory() throws TwitterAuthenticationException {
        
        OAuthHmacSigner oAuthHmacSigner = new OAuthHmacSigner();
        oAuthHmacSigner.clientSharedSecret = getConsumerSecret();

        String oAuthTemporaryToken = getOAuthTemporaryToken(oAuthHmacSigner);
        String oAuthAuthorizePin = getOAuthAuthorizePin(oAuthTemporaryToken);
        
        OAuthCredentialsResponse oAuthCredentialsResponse = getOAuthCredentialsResponse(oAuthHmacSigner, oAuthAuthorizePin, oAuthTemporaryToken);
        
        oAuthHmacSigner.tokenSharedSecret = oAuthCredentialsResponse.tokenSecret;

        OAuthParameters oAuthParameters = new OAuthParameters();
        oAuthParameters.signer = oAuthHmacSigner;
        oAuthParameters.consumerKey = getConsumerKey();
        oAuthParameters.token = oAuthCredentialsResponse.token;
        
        httpRequestFactory = getHttpTransport().createRequestFactory(oAuthParameters);
    }
    
    /**
     * Returns the authenticated HttpRequestFactory
     * 
     * @return HttpRequestFactory you can use to talk to Twitter
     * @throws TwitterAuthenticationException 
     */
    public HttpRequestFactory getHttpRequestFactory() throws TwitterAuthenticationException {
        
        if (httpRequestFactory == null) {
            throw new TwitterAuthenticationException("Authorization request need be executed before!");
        }
        
        return httpRequestFactory;
    }
}
