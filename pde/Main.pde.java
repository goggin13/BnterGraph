int SCREEN_WIDTH = window.innerWidth;
int SCREEN_HEIGHT = window.innerHeight * .99;
BufferedReader reader;
ArrayList conversations;
ArrayList nodes;
//Graph g;
color backgroundColor = color(0, 0, 0);
MyProgressBar progressBar;
int p = 0;

void addToConversations(String line) {
  String[] pieces = split(line, ":");
  int cid = int(pieces[0]);
  String messageStr = pieces[1];
  String userStr = pieces[2];

  // get messages
  ArrayList messages = new ArrayList();
  pieces = split(messageStr, "{}");
  for (int i = 0; i < pieces.length; i++) {
    messages.add(new Message(pieces[i]));
  }

  // get users
  ArrayList users = new ArrayList();
  int uid;
  pieces = split(userStr, ",");
  for (int i = 0; i < pieces.length; i++) {
    uid = int(pieces[i]);
    users.add(new User(uid));
  }
  
  conversations.add(new Conversation(cid, users, messages));
}

void setup() {
  //g = new Graph();
  size(SCREEN_WIDTH, SCREEN_HEIGHT);
  background(backgroundColor);
  progressBar = new MyProgressBar(1000);
  conversations = new ArrayList();

  //reader = createReader("conversations.txt");
  /*addToConversations("18007:message1{}message2{}message3:1,2,3");
  addToConversations("18007:message1{}message2{}message3:9,8,1");
  addToConversations("18007:message1{}message2{}message3:9,2,1"); 
  addToConversations("18007:message1{}message2{}message3:9,3,1"); 
  addToConversations("18007:message1{}message2{}message3:9,4,1"); 
  addToConversations("18007:message1{}message2{}message3:9,5,1");  
  addToConversations("18007:message1{}message2{}message3:8,4,11"); 
  addToConversations("18007:message1{}message2{}message3:8,4,11"); */ 

}

function processConversations () {
	
}

function loadFriends () {
	var f = function () {
		$.getJSON('/get_friends', function () {
			p = 900;
		});
	};
	setTimeout(f, 2000);
} 

void draw() {
	if (p === 0) {           // fire ajax calls
		loadFriends();
		p = 1;
	} else if (p < 1000) {  // keep updating the progress bar
  	
		progressBar.updatePB(p);
		p = p + 1;
  } else if (p < 2000) {   // transition from progress to graph
		background(backgroundColor);
		$('#header').show();
		$('#stats_div').show();
		p = 3000;
	} else {   // main method
		//g.draw();
	}
  
}











    

