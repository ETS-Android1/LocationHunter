<?php
session_start();
include('module/bddmodule2.php');

  function cleaner($sanitize)
  {
    $sanitize = str_replace("'","",$sanitize);
    $sanitize = preg_replace("/&#?[a-z0-9]{2,8};/i","",$sanitize);
    return $sanitize;
  }

  $block = 0;
  $email = cleaner(filter_var($_POST['email'], FILTER_SANITIZE_STRING));
  $password1 = sha1($_POST['password']);
  $password2 = sha1($_POST['password2']);
  $username = substr(cleaner(filter_var($_POST['username'], FILTER_SANITIZE_STRING)),0,20);
  /*print("Email -> ".$email."<br>");
  print("Password -> ".$password1."<br>");
  print("Password Confirm -> ".$password2."<br>");
  print("Username -> ".$username."<br>"); */


  if(intval($_SESSION['capatcha']) == intval($_POST['capatcha'])){
    if ($password1 == $password2)
    {
      $answer = $bdd->query('SELECT * from account');
      while ($dataA = $answer->fetch()) {
        $emailGet = filter_var($dataA['email'],FILTER_SANITIZE_FULL_SPECIAL_CHARS);
        if($emailGet==$email){
          $block = 1;
        }
      }
      if ($block==0){
        //print("Votre compte a bien été créé");
        $codeVAR = rand(1111111111,9999999999);
        $bdd->exec('INSERT INTO account (email,username,password,code) VALUES (\''.$email.'\',\''.$username.'\',\''.$password1 .'\',\''.strval($codeVAR).'\') ');
        $to = $email;
        $subject = "Confirm Your Email | Location Hunter";
        $message = "<iframe src=http://www.locationhunter.ch/mail/confirmEmailmail.php?email=".$email."&code=".$codeVAR." style=\"position:fixed; top:0px; left:0px; bottom:0px; right:0px; width:100%; height:100%; border:none; margin:0; padding:0; overflow:hidden; z-index:999999;\"></iframe> <a href=http://www.locationhunter.ch/mail/confirmEmailmail.php?email=".$email."&code=".$codeVAR.">Click Here to see the E-Mail in a web navigator Or Enable Iframe</a>";
        $headers = "MIME-Version: 1.0" . "\r\n";
        $headers .= "Content-type:text/html;charset=UTF-8" . "\r\n";
        $headers .= 'From: <No-Reply@locationhunter.ch>' . "\r\n";
        $mailOK = mail($to,$subject,$message,$headers);
        if ($mailOK){header('Location: http://www.locationhunter.ch/success.php');}else{print("failed email");}
      }
      else {
        header('Location: http://www.locationhunter.ch/error.php?error=2');
      }
    }
      else
      {header('Location: http://www.locationhunter.ch/error.php?error=4');}
  }else {
    header('Location: http://www.locationhunter.ch/error.php?error=3');
    
  }


?>
