<?php
session_start();

include('module/bddmodule2.php');

function cleaner($sanitize)
{
  $sanitize = str_replace("'","",$sanitize);
  $sanitize = preg_replace("/&#?[a-z0-9]{2,8};/i","",$sanitize);
  return $sanitize;
}
$email = cleaner(filter_var($_POST['email2'], FILTER_SANITIZE_STRING));
$password = sha1($_POST['password2']);

$connect = 0;
$answer = $bdd->query('SELECT * from account WHERE email=\''.$email.'\' AND password=\''.$password.'\' AND enabled=1 ');
while ($dataA = $answer->fetch()) {
  $connect = 1;
}
$answer->closeCursor();

if($connect==0){
  print("Error");
  header('Location: http://www.locationhunter.ch/error.php?error=5');
}
else{
  $_SESSION['userCo']=$email;
  $_SESSION['passCo']=$password;
  header('Location: http://www.locationhunter.ch/account.php');
}


?>
