# Matt Goggin
# 
# the controller for the BnterProfiler App.
# Presents the two static pages, and also the simple 
# wrapper API needed to support JSONP

# commands to start server and update
# dev_appserver.py /Users/mattgoggin/Desktop/CS/BnterGraph
# appcfg.py update /Users/mattgoggin/Desktop/CS/BnterGraph

import os
import sys
import logging
ROOT = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, ROOT)
sys.path.insert(0, os.path.join(ROOT, 'pybnter'))
from bnterAuth import BnterOAuth
from google.appengine.ext import webapp
from google.appengine.ext.webapp import template
from google.appengine.ext.webapp.util import run_wsgi_app
from google.appengine.api import urlfetch
from google.appengine.api import memcache
from django.utils import simplejson as json



class GetFriends(webapp.RequestHandler):
   def get(self):
      friends = {
         "Friend1": "data"
      }
      self.response.out.write(json.dumps(friends))

class GetOauth(webapp.RequestHandler):
   def get(self):
      IP = self.request.remote_addr
      ba = BnterOAuth(IP)
      path = ba.getOAuthClient()
      self.redirect(path)
      
class HandleOauth(webapp.RequestHandler):
   def get(self):
      IP = self.request.remote_addr
      oauth_token = self.request.get('oauth_token')
      ba = BnterOAuth(IP)
      ba.setVerifier(oauth_token)
      self.redirect('/')

class Main(webapp.RequestHandler):
   def get(self):
      IP = self.request.remote_addr
      ba = BnterOAuth(IP)
      token = ba.getToken()
      if False and not token:
         self.redirect('/get_oauth')
      template_values = {
      }

      path = os.path.join(os.path.dirname(__file__), '../html/index.html')
      self.response.out.write(template.render(path, template_values))

# /_ah/warmup
# simply here to suppress 404's on our dashboard from GAE hitting this URL
class WarmupHandler(webapp.RequestHandler):
    def get(self):
        logging.info('Warmup Request') 

 
application = webapp.WSGIApplication(
                                    [
                                     ('/', Main),
                                     ('/get_oauth', GetOauth), 
                                     ('/get_friends', GetFriends), 
                                     ('/handle_oauth', HandleOauth)
                                    ], debug=True)

def main():
    run_wsgi_app(application)

if __name__ == "__main__":
    main()

