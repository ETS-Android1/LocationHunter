<?php
if (isset($_GET['code'])&&isset($_GET['email'])){
  $code = $_GET['code'];
  $email = $_GET['email'];
  include('module/bddmodule2.php');
  $verif = 0;
  $answer = $bdd->query('SELECT * from account WHERE email=\''.$email.'\' AND enabled = 0');
  while ($dataA = $answer->fetch())
    {
      if($dataA['code']==$code)
      {
        $verif = 1;
      }
    }
    if ($verif == 1)
    {
     $bdd->exec('UPDATE account SET enabled = 1 WHERE email = \''.$email.'\' ');
      header('Location: http://www.locationhunter.ch/success.php');
    }
    else {
      header('Location: http://www.locationhunter.ch/error.php?error=1');
    }

}
?>
