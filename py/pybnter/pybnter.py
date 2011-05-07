import os
import sys
ROOT = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, os.path.join(ROOT, 'httplib2'))
import httplib2
import json

class Bnter():
   token = ''
  
   def __init__(self, token):
      self.token = token
   
   def url(self, call):
      path = "http://bnter.com/api/v1/%s?oauth_token=%s"
      return path % (call, self.token)
   
   def request(self, call):
      h = httplib2.Http()
      url = self.url(call)
      resp, content = h.request(url, "GET")
      return json.loads(content)
      
   def getUserDashboard(self):
      response = self.request('user/dashboard.json')
      conversations = response['conversations']
      print conversations

   def getEdges(self):
      h = httplib2.Http()
      url = 'http://bnterdev.com/get_conversations.php'
      resp, content = h.request(url, "GET")
      edges = json.loads(content)
      return edges

b = Bnter('57ad8624c3bfae2193bd5dbec40d59fa')
#b.getUserDashboard()
b.getEdges()