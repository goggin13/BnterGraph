from models import Edge, User
from pybnter import Bnter
from google.appengine.ext import db
from google.appengine.api import memcache

class EdgeSet():
   edges = []
   keys = []

   def __init__(self):
      self.edges = []
      self.keys = []

   def getUrl(self, username):
      cacheKey = 'url_%s' % username
      url = memcache.get(cacheKey)
      if not url:
         url = db.GqlQuery("SELECT * FROM User " +
                           "WHERE username = :1",
                           username).get().image
         url = url.replace("w320_h320", "w50_h50")
         memcache.set(cacheKey, url)
      return url
      
   def getEdges(self):
      return self.edges
   
   def addOrUpdate(self, n1, n2):
      key = n1 + n2
      if key in self.keys:
         for e in self.edges:
            if e['node1'] == n1 and e['node2'] == n2:
               e['weight'] += 1;
      else:
         self.edges.append({
            'node1': {
               'name': n1,
               'image': self.getUrl(n1)
            },
            'node2': {
               'name': n2,
               'image': self.getUrl(n2)
            },
            'weight': 1
         })
         self.keys.append(n1 + n2)
      
class EdgeManager():
   token = None
   api = None
   
   def __init__(self, token):
      self.token = token
      self.API = Bnter(token)
   
   # increase weight of edge from user1 to user2, 
   # if we haven't already accounted for this 
   # conversations
   def updateEdge(self, n1, n2, conversation_id):
      edge = db.GqlQuery("SELECT * FROM Edge " +
                          "WHERE node1 = :1 AND " +
                                "node2 = :2 AND " +
                                "edge_id = :3",
                          n1, n2, conversation_id).get()
      if not edge:
         edge = Edge(
           node1 = n1.lower(),
           node2 = n2.lower(),
           edge_id = conversation_id
         ).put()
      return edge

   def saveUserInfo(self, users):
      for u in users:
         user = db.GqlQuery("SELECT * FROM User " +
                             "WHERE username = :1",
                             u['username']).get()
         if not user:
            user = User(
               username = u['username'],
               image = u['url']
            ).put()
      
   def updateNodeSet(self, nodeSet):
      cid = nodeSet['id']
      users = nodeSet['users']
      user1 = users[0]['username']
      user2 = users[1]['username']
      
      self.saveUserInfo(users)
      self.updateEdge(user1, user2, cid)
      if len(users) == 3:
         user3 = users[2]['username']
         self.updateEdge(user2, user3, cid)
         self.updateEdge(user1, user3, cid)
      
   def fromCache(self, key):
      obj = memcache.get(key)
      return obj
      
   def setCache(self, key, obj):
      memcache.set(key, obj, 1800)
      
   def updateEdgesForUser(self, user_name):
      cacheKey = "edgesFor%s" % user_name
      nodeSets = self.fromCache(cacheKey)
      
      if not nodeSets:
         nodeSets = self.API.getAttributionSets(user_name)
         self.setCache(cacheKey, nodeSets)
      
      for nodeSet in nodeSets:
         self.updateNodeSet(nodeSet)
      
   def getEdgesForUser(self, user_name):
      edges = db.GqlQuery("SELECT * FROM Edge " +
                           "WHERE node1 = :1 LIMIT 1",
                           user_name).fetch(100)
      edges2 = db.GqlQuery("SELECT * FROM Edge " +
                           "WHERE node2 = :1 LIMIT 1",
                           user_name).fetch(100)
      edgeSet = EdgeSet()
      edges.extend(edges2)
      for e in edges:
        edgeSet.addOrUpdate(e.node1, e.node2)
      return edgeSet.getEdges()
      