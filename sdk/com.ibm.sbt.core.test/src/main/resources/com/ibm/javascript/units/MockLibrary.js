if (typeof _sbt == 'undefined' || window._sbt_bridge_compat) {
    _sbt = 0;

	    require({
        paths: {
            'sbt': prefix+'/sbt'
        }
    });
    require({
        paths: {
            'sbt/_bridge': prefix+'/_bridges/dojo-amd'
        }
    });
    require({
        paths: {
            'sbt/widget': prefix+'/dojo2'
        }
    });
    require({
        paths: {
            'sbtx': prefix+'/sbtx'
        }
    });
    require({
        paths: {
            'sbtx/widget': prefix+'/dojo2'
        }
    });
    require({
        paths: {
            'sbt/overload': overload+'/src/main/resources/com/ibm/javascript/units'
        }
    });
    var promiseCount = 0;
    
    define('sbt/config', ['sbt/Proxy', 'sbt/overload/MockServiceTransport', 'sbt/authenticator/Basic', 'sbt/Endpoint', 'sbt/ErrorTransport', 'sbt/authenticator/OAuth'], function (Proxy, Transport, Basic, Endpoint, ErrorTransport, OAuth) {
        var sbt = {};
        sbt.Properties = {
            "libraryUrl": prefix+'/test/com/ibm/javascript/units/MockLibrary.js',
            "serviceUrl": undefined,
            "sbtUrl": prefix+"/sbt"
        };
        sbt.Endpoints = {
            'smartcloud': new Endpoint({
                "authType": "basic",
                "isAuthenticated": true,
                "transport": new Transport({}),
                "name": "smartcloud",
                "authenticator": new Basic({
                    "url": "https:\/\/sbtintegration.swg.usma.ibm.com:8443\/sbt\/js\/sdk"
                }),
                "platform": "smartcloud",
                "isSmartCloud": true,
                "serviceMappings": {},
                "authenticationErrorCode": 403,
                "apiVersion": "3.0",
                //"baseUrl": "https:\/\/apps.na.collabserv.com"
            }),
            'connections': new Endpoint({
                "authType": "basic",
                "platform": "connections",
                "authenticator": new Basic({
                    "url": "https:\/\/sbtintegration.swg.usma.ibm.com:8443\/sbt\/js\/sdk"
                }),
                "isAuthenticated": true,
                "transport": new Transport({}),
                "serviceMappings": {},
                "name": "connections",
                "authenticationErrorCode": 401,
                //"baseUrl": "https:\/\/vhost0839.dc1.on.ca.compute.ihost.com:444",
                "apiVersion": "4.0",

            })
        };
        sbt.findEndpoint = function (endpointName) {
            return this.Endpoints[endpointName];
        };
        console.log('MOCKING SBT LIBRARY SERVICE INITIALIZED');
        return sbt;
    });
}