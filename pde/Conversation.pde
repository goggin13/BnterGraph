class Conversation {
  int id;
  ArrayList participants;
  ArrayList messages;
  
  Conversation(int cid, ArrayList p, ArrayList m) {
    id = cid;
    participants = p;
    messages = m;
    usersToNodes(p);
    usersToEdges(p);
  }
  
  void usersToNodes(ArrayList users) {
    Iterator it = users.iterator();
    while (it.hasNext()) {
      User u = (User)it.next();
      int id = u.getUid();
      g.addNodeIf(id);
    }
  }
  
  void usersToEdges(ArrayList users) {
    Iterator it = users.iterator();
    Iterator it2 = users.iterator();
    while (it.hasNext()) {
      User u1 = (User)it.next();
      while (it2.hasNext()) {
        User u2 = (User)it2.next();
        g.addEdge(u1.getUid(), u2.getUid());   
      }
    }
  }
}
