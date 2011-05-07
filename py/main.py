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

class CronJob(webapp.RequestHandler):
  def deleteAllEdges(self):
    edges = db.GqlQuery("SELECT * FROM Edge")
    entites = edges.fetch(1000)
    while entites:
      db.delete(entites)
      entites = edges.fetch(1000)

  def addOrUpdateEdge(self, n1, n2):
    n1 = max(n1, n2)
    n2 = min(n1, n2)
    edge = db.GqlQuery("SELECT * FROM Edge " +
                       "WHERE node1 = :1 AND node2 = :2 ",
                       n1, n2).get()
    if edge:
      edge.weight = edge.weight + 1
    else:
      edge = Edge(
        node1 = n1,
        node2 = n2,
        weight = 1
      )
    edge.put()
    
  def get(self):
    self.deleteAllEdges()
    b = Bnter('57ad8624c3bfae2193bd5dbec40d59fa')
    edges = b.getEdges()
    for edge in edges:
      logging.debug("adding edge %d %d" % (int(edge[0]), int(edge[1])))
      self.addOrUpdateEdge(int(edge[0]), int(edge[1]))

    edges = b.getEdges()
    for edge in edges:
      logging.debug("adding edge %d %d" % (int(edge[0]), int(edge[1])))
      self.addOrUpdateEdge(int(edge[0]), int(edge[1]))    

class GetFriends(webapp.RequestHandler):
   def get(self):
      user_id = int(self.request.get('user_id'))
      edges = db.GqlQuery("SELECT * FROM Edge " +
                          "WHERE node2 = :1",
                          user_id).fetch(100)
      edgeList = []
      for e in edges:
        edgeList.append({
          'node1': e.node1,
          'node2': e.node2,
          'weight': e.weight
        })
      self.response.out.write(json.dumps(edgeList))

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
        'user_id': '2'
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
                                     ('/handle_oauth', HandleOauth), 
                                     ('/cron', CronJob)
                                    ], debug=True)

def main():
    run_wsgi_app(application)

if __name__ == "__main__":
    main()