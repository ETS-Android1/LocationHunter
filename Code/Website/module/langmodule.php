<?php
if(isset($_GET['lang']))
{
    if ($_GET['lang']=="en"){
      $_SESSION['lang']="en";
    }
    else if ($_GET['lang']=="fr")
    {
      $_SESSION['lang']="fr";
    }
}
if (!isset($_SESSION['lang'])){
    $_SESSION['lang'] = "en";
}
$lang = $_SESSION['lang'];
?>
