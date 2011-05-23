# Matt Goggin
# 
# the controller for the BnterProfiler App.
# Presents the two static pages, and also the simple 
# wrapper API needed to support JSONP

# commands to start server and update
# dev_appserver.py /Users/goggin/Documents/CS/bntergraph
# appcfg.py update /Users/goggin/Documents/CS/bntergraph
# dev_appserver.py /Users/mattgoggin/Desktop/CS/BnterGraph
# appcfg.py update /Users/mattgoggin/Desktop/CS/BnterGraph
# '57ad8624c3bfae2193bd5dbec40d59fa'

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
from models import Edge
from google.appengine.ext import db
from pybnter import Bnter
from edge import EdgeManager

class UpdateFriends(webapp.RequestHandler):
   def get(self):
      user_name = self.request.get('user_name')
      if memcache.get(user_name + 'updated'):
         self.response.out.write(json.dumps({
            "page": 1,
            "pageCount": 1
         }))
         return
         
      ba = BnterOAuth(self.request.remote_addr)
      edgeManager = EdgeManager(ba.getToken())
      page = self.request.get('page')
      info = edgeManager.updateEdgesForUser(user_name.lower(), page)
      if info['page'] == info['pageCount']:
         logging.info("Update friends for %s" % user_name)
         memcache.set(user_name + 'updated', 1, 6000)
      self.response.out.write(json.dumps({
         "page": info['page'],
         "pageCount": info['pageCount']
      }))

class GetFriends(webapp.RequestHandler):
   def get(self):
      ba = BnterOAuth(self.request.remote_addr)
      edgeManager = EdgeManager(ba.getToken())
      user_name = self.request.get('user_name')
      edges = edgeManager.getEdgesForUser(user_name.lower())
      self.response.out.write(json.dumps(edges))

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

class Logout(webapp.RequestHandler):
   def get(self):
      IP = self.request.remote_addr
      ba = BnterOAuth(IP)
      ba.flush()
      self.redirect('/')

class Demo(webapp.RequestHandler):
   def get(self):
      IP = '184.74.167.234'
      ba = BnterOAuth(IP)
      template_values = {
        'user_name': 'laurenleto',
        'hide_welcome': False
      }

      path = os.path.join(os.path.dirname(__file__), '../html/index.html')
      self.response.out.write(template.render(path, template_values))

class Main(webapp.RequestHandler):
   def get(self):
      IP = self.request.remote_addr
      ba = BnterOAuth(IP)
      token = ba.getToken()
      if not token:
         self.redirect('/get_oauth')
      
      template_values = {
        'user_name': self.request.get('user_name'),
        'hide_welcome': self.request.get('hide_welcome')
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
                                     ('/logout', Logout),
                                     ('/update_friends', UpdateFriends),
                                     ('/get_friends', GetFriends), 
                                     ('/demo', Demo),
                                     ('/handle_oauth', HandleOauth)
                                    ], debug=True)

def main():
    run_wsgi_app(application)

if __name__ == "__main__":
    main()