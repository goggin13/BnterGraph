import os
import sys
import cgi
ROOT = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, os.path.join(ROOT, 'oauth2'))
sys.path.insert(0, os.path.join(ROOT, 'httplib2'))
sys.path.insert(0, os.path.join(ROOT, '..'))
import oauth2 as oauth
from models import IPOAuth
import logging

class BnterOAuth():
   consumer_key = 'dbc6af1ab9d8e2daec6e6883e27a1af7'
   consumer_secret = '6b2394ebf2'
   request_token_url = 'http://bnter.com/oauth/request_token'
   access_token_url = 'http://bnter.com/oauth/access_token'
   authorize_url = 'http://bnter.com/oauth/authorize'

   def __init__(self, IP=-1):
      self.IP = IP
      self.setIP_OAuth()    
   
   def setIP_OAuth(self):
      self.IP_OAuth = IPOAuth.all().filter('ip = ', self.IP).get()

   def getIP_OAuth(self):
      if not self.IP_OAuth:
         self.setIP_OAuth()
      return self.IP_OAuth
   
   def getToken(self):
      if self.IP_OAuth:
         return self.IP_OAuth.oauth_token
      else:
         return None

   def getRedirectPath(self):
      path = "%s?oauth_token=%s" % (self.authorize_url, request_token)
      return path
   
   def setVerifier(self, oauth_verifier):
      self.oauth_verifier = oauth_verifier
      return self.setOAuthToken()

   def getOAuthClient(self):
      if self.IP_OAuth:
         return self.IP_OAuth
         
      consumer = oauth.Consumer(self.consumer_key, self.consumer_secret)
      client = oauth.Client(consumer)
      resp, content = client.request(self.request_token_url, "GET")
      if resp['status'] != '200':
         raise Exception("Invalid response %s." % resp['status'])

      request_token = dict(cgi.parse_qsl(content))
      # TO DO DELETE OLD ONE!!
      oauth_token = request_token['oauth_token']
      oauth_token_secret = request_token['oauth_token_secret']
      self.IP_OAuth = IPOAuth(ip = self.IP, 
                     request_token = oauth_token,
              oauth_token = '',
                     request_secret = oauth_token_secret)
      self.IP_OAuth.put()
      
      return "%s?oauth_token=%s" % (self.authorize_url, oauth_token)

   def getRequestToken(self):
      return self.IP_OAuth.request_token
      
   def getRequestTokenSecret(self):
      return self.IP_OAuth.request_secret
   
   def getOAuthToken(self):
      return oauth.Token(self.getRequestToken(), self.getRequestToken())
   
   def setOAuthToken(self):
      consumer = oauth.Consumer(self.consumer_key, self.consumer_secret)
      token = self.getOAuthToken()
      token.set_verifier(self.oauth_verifier)
      client = oauth.Client(consumer, token)
      resp, content = client.request(self.access_token_url, "POST")
      access_token = dict(cgi.parse_qsl(content))
      self.IP_OAuth.oauth_token = access_token['oauth_token']
      self.IP_OAuth.oauth_token_secret = access_token['oauth_token_secret']
      self.IP_OAuth.put()
      return self.getToken()
      
   def flush(self):
      if self.IP_OAuth:
         self.IP_OAuth.delete()

#ba = BnterOAuth()
#ba.getOAuthClient(12)
#id = raw_input("give me the id")
#print "id"
#print id
#ba.AfterRedirect(id)