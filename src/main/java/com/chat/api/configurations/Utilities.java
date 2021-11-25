package com.chat.api.configurations;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

import com.chat.api.repositories.entities.User;
import com.chat.api.services.CustomUserDetailsService.CustomUserPrincipal;
import com.chat.api.services.UserService;


public final class Utilities {
	
	public static String UPLOAD_PATH = System.getProperty("user.dir")+System.getProperty("file.separator")+"src"+
	System.getProperty("file.separator")+"main"+System.getProperty("file.separator")+"resources"+
			System.getProperty("file.separator")+"uploads";
	
	/**
	 * Get current connected user if exist
	 * @return User 
	 */
	public static User getLoggedUser(UserService userService) 
	{
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.isAuthenticated()) {
			return userService.findByLogin(auth.getName());
		}
		
		return null;
	}	
	
	//Create a new access token
	public static OAuth2AccessToken createAccessToken(DefaultTokenServices tokenServices, User user) {
    	return getAccessToken(tokenServices, user, "chat-app");
    }
    
    private static OAuth2AccessToken getAccessToken(DefaultTokenServices tokenServices, User user, String clientId) { 
        HashMap<String, String> authorizationParameters = new HashMap<String, String>();
        authorizationParameters.put("scope", "read");
        authorizationParameters.put("username", user.getLogin());
        authorizationParameters.put("client_id", clientId);
        authorizationParameters.put("grant", "password");

        Set<String> responseType = new HashSet<String>();
        responseType.add("password");

        Set<String> scopes = new HashSet<String>();
        scopes.add("read");
        scopes.add("write");
        scopes.add("trust");
        

        CustomUserPrincipal customUserPrincipal = new CustomUserPrincipal(user);
        
        OAuth2Request authorizationRequest = new OAuth2Request(authorizationParameters, clientId, customUserPrincipal.getAuthorities(), true,
                scopes, null, "", responseType, null);
 
        
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(customUserPrincipal,
                null, customUserPrincipal.getAuthorities());

        OAuth2Authentication authenticationRequest = new OAuth2Authentication(authorizationRequest,
                authenticationToken);
        authenticationRequest.setAuthenticated(true);
        OAuth2AccessToken accessToken = tokenServices.createAccessToken(authenticationRequest);

        return accessToken;
    }
    
   
}


