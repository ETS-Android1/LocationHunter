<?php
include('config.php');
if (!isset($_SESSION['gdpr']))
{
  header('Location: /'.$servPath.'gdprwarning.php');

  if (isset($_GET['lang'])){
    $supp = "?lang=".$_GET['lang'];
  }
  else{
    $supp = "";
  }

  $_SESSION['gdprBef'] = basename($_SERVER['PHP_SELF']).$supp;
}
?>
