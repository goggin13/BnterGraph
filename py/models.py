from google.appengine.ext import db

class IPOAuth(db.Model):
    ip = db.StringProperty()
    request_token = db.StringProperty()
    request_secret = db.StringProperty()
    oauth_token = db.StringProperty()
    oauth_token_secret = db.StringProperty()