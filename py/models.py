from google.appengine.ext import db

class IPOAuth(db.Model):
   ip = db.StringProperty()
   request_token = db.StringProperty()
   request_secret = db.StringProperty()
   oauth_token = db.StringProperty()
   oauth_token_secret = db.StringProperty()

class Edge(db.Model):
   node1 = db.StringProperty()
   node2 = db.StringProperty()
   edge_id = db.IntegerProperty()

class User(db.Model):
   username = db.StringProperty()
   image = db.StringProperty()