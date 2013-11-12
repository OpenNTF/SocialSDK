import sys
execfile('oauthAdmin.py')
OAuthApplicationRegistrationService.addApplication(sys.argv[0],sys.argv[1],sys.argv[2])
clientSecret = OAuthApplicationRegistrationService.getApplicationById(sys.argv[0]).get('client_secret')
print clientSecret
