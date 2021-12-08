<?php
session_start();
session_destroy();
if (isset($_GET['redirect'])){
  header('Location: http://www.locationhunter.ch/'.$_GET['redirect'].'.php');
}else{
  header('Location: http://www.locationhunter.ch/');
}

?>
