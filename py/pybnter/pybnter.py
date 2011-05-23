import os
import sys
ROOT = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, ROOT)
import httplib2
from urllib import urlencode
try:
   import json
except Exception:
   from django.utils import simplejson as json

class Bnter():
   token = ''
   
   def __init__(self, token):
      self.token = token
   
   def url(self, call):
      path = "http://bnter.com/api/v1/%s?oauth_token=%s"
      return path % (call, self.token)
   
   def request(self, call, data=None):
      h = httplib2.Http('')
      url = self.url(call) + "&" + urlencode(data)
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

   def extractUsersFromResponse(self, response):
      sets = []
      for c in response['conversations']:
         users = self.extractUsersFromConversation(c)
         if len(users['users']) > 1:
            sets.append(users)
      return sets
      
   def extractUsersFromConversation(self, conversation):
      users = []
      curUsers = []
      for m in conversation['messages']:
         sn = m['sender_user']['screen_name'] if 'id' in m['sender_user'] else None
         if sn and sn not in curUsers:
            users.append({
               'username': sn.strip().lower(),
               'url': m['profile_image_url']
            })
            curUsers.append(sn)
      users.sort()
      return dict(id=int(conversation['id']), users=users)

   # a list of {cid:xx, [user1, user2, user3]}
   def getAttributionSets(self, screen_name, page):
      data = {
         'per_page': 50,
         'page': page,
         'user_screen_name': screen_name
      }
      response = self.request('user/mentions.json', data)
      sets = self.extractUsersFromResponse(response)

      #for i in range(2, response['total_pages']):
         #data['page'] = i
         #response = self.request('user/mentions.json', data)
         #sets.extend(self.extractUsersFromResponse(response))
      return {
         'nodeSets': sets,
         'page': int(page),
         'pageCount': int(response['total_pages'])
      }

#b = Bnter('57ad8624c3bfae2193bd5dbec40d59fa')
#b.getUserDashboard()
#b.getEdges()
#print b.getAttributionSets('goggin13')
#nodeSets = b.getAttributionSets('mallory')
def updateEdge(n1, n2, id):
   print n1, n2, id

def updateNodeSet(nodeSet):
   #print nodeSet
   cid = nodeSet['id']
   users = nodeSet['users']
      
   updateEdge(users[0], users[1], cid)
   if len(users) == 3:
      updateEdge(users[1], users[2], cid)
      updateEdge(users[0], users[2], cid)
      
#for nodeSet in nodeSets:
 #  updateNodeSet(nodeSet)