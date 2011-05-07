<?php

$link = mysql_connect('bnterdev.com', 'root', 'root');
if (!$link) {
    die('Not connected : ' . mysql_error());
}

// make foo the current db
$db_selected = mysql_select_db('bnter_old', $link);
if (!$db_selected) {
    die ('Can\'t use foo : ' . mysql_error());
}

$sql = "SELECT 
	utc1.conversation_id, 
	
	if ( ifnull(u1.is_preuser, 1) = 0, utc1.user_id, 0) as user1, 
	if ( ifnull(u2.is_preuser, 1) = 0, utc2.user_id, 0) as user2, 
	if ( ifnull(u3.is_preuser, 1) = 0, utc3.user_id, 0) as user3, 
	
	utc2.user_id  as user2, 
	u2.is_preuser as pre2, 
	
	utc3.user_id as user3, 
	u3.is_preuser as pre3
	
FROM user_to_conversation utc1  
  LEFT JOIN users u1 ON
    utc1.user_id = u1.id


  LEFT JOIN user_to_conversation utc2 ON
    utc1.conversation_id = utc2.conversation_id
  LEFT JOIN users u2 ON
    utc2.user_id = u2.id


  LEFT JOIN user_to_conversation utc3 ON
    utc1.conversation_id = utc3.conversation_id AND utc3.participant_id = 3
  LEFT JOIN users u3 ON
    utc3.user_id = u3.id

	
WHERE
  utc1.participant_id = 1 
  AND utc2.participant_id = 2
  AND ifnull(u1.is_preuser,1) + ifnull(u2.is_preuser,1) + ifnull(u3.is_preuser,1) < 2
limit 1000
";

$result = mysql_query($sql, $link);
if (!$result) {
	die('Invalid query: ' . mysql_error());
}

$edges = array();
while ($row = mysql_fetch_assoc($result)) {
	
	  if ($row['user1']) {
			$user1 = $row['user1'];
		} else {
			$user1 = false;	
		}
		
		if ($row['user2']) {
			$user2 = $row['user2'];
		} else {
			$user2 = false;
		}
		
		if ($row['user3']) {
			$user3 = $row['user3'];
		} else {
			$user3 = false;
		}
		
		if ($user1 && $user2) {
			$edges[] = array($user1, $user2);
		}
		if ($user1 && $user3) {
			$edges[] = array($user1, $user3);
		}
		if ($user2 && $user3) {
			$edges[] = array($user2, $user3);
		}
}
die(json_encode($edges));
