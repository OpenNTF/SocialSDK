***REMOVED***
/**
 * Note : Code is released under the GNU LGPL
 *
 * Please do not change the header of this file
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU Lesser General Public License for more details.
 */

/**
 * Light PHP wrapper for the OAuth 2.0 protocol.
 *
 * This client is based on the OAuth2 specification draft v2.15
 * http://tools.ietf.org/html/draft-ietf-oauth-v2-15
 *
 * @author      Pierrick Charron <pierrick@webstart.fr>
 * @author      Anis Berejeb <anis.berejeb@gmail.com>
 * @version     1.2-dev
 */

namespace OAuth2;
require('Client.php');
class SBTKClient extends Client
{

    /**
     * getAuthenticationUrl
     *
     * @param string $auth_endpoint Url of the authentication endpoint
     * @param string $redirect_uri  Redirection URI
     * @param array  $extra_parameters  Array of extra parameters like scope or state (Ex: array('scope' => null, 'state' => ''))
     * @return string URL used for authentication
     */
    public function getAuthenticationUrl($auth_endpoint, $redirect_uri, array $extra_parameters = array())
    {
        $parameters = array_merge(array(
            'response_type' => 'code',
            'client_id'     => $this->client_id,
            'callback_uri'  => $redirect_uri
        ), $extra_parameters);
        return $auth_endpoint . '?' . http_build_query($parameters, null, '&');
    }
    
    /**
     * getAccessToken
     *
     * @param string $token_endpoint    Url of the token endpoint
     * @param int    $grant_type        Grant Type ('authorization_code', 'password', 'client_credentials', 'refresh_token', or a custom code (@see GrantType Classes)
     * @param array  $parameters        Array sent to the server (depend on which grant type you're using)
     * @return array Array of parameters required by the grant_type (CF SPEC)
     */
//     public function getAccessToken($token_endpoint, $grant_type, array $parameters)
//     {
//     	$parameters['grant_type'] = 'authorization_code'; 
//     	$http_headers = array();
//     	$parameters['client_id'] = $this->client_id; 

//     	return $this->executeRequest($token_endpoint, $parameters, self::HTTP_METHOD_POST, $http_headers, self::HTTP_FORM_CONTENT_TYPE_APPLICATION);
//     }

}