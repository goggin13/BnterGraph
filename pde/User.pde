class User {
  int id;
  String name;
  
  User(int uid) {
    id = uid;
  }
  
  int getUid() {
    return id;
  }
  
  String getUidStr() {
    return Integer.toString(id);
  }
}
