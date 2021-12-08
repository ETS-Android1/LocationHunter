<!DOCTYPE html>
<?php
session_start();
include('module/bddmodule2.php');

//set here the settings to connect to the admin webpage
$Username = "xxxxxxxxxxxxxxxx";
$Password = "xxxxxxxxxxxxxxxx";

if(!isset($_SESSION['usernameA']) || !isset($_SESSION['passwordA'])){
  if (!isset($_POST['username']) || !isset($_POST['password'])){
    header('Location: http://www.locationhunter.ch/adminC.php');
  }
  else{
    if(sha1($Password)==sha1($_POST['password']) && sha1($Username)==sha1($_POST['username'])){
      $_SESSION['usernameA'] = $Username;
      $_SESSION['passwordA'] = $Password;
      header('Location: http://www.locationhunter.ch/admin.php'); // to reload the page
    }
    else{
      header('Location: http://www.locationhunter.ch/adminC.php');
    }
  }
}
else{
  //load the admin page :D
  if(isset($_GET['newRandomCoord'])){
    $bdd->exec('UPDATE command SET value = 1 WHERE id = 0 ');
    header('Location: http://www.locationhunter.ch/admin.php');
  }


  if(isset($_GET['chgGoal'])){
    $idTG = $_GET['chgGoal'];
    $answer = $bdd->query('SELECT * from location WHERE id=\''.$idTG.'\' ');
    while ($dataA = $answer->fetch()) {
      $nCoord = $dataA['coord'];
      $nName = $dataA['name'];
    }
    $nDateheur = date("Y:m:d:H:i:s");
    $bdd->exec('INSERT INTO history (coord,name,dateheur) VALUES (\''.$nCoord.'\',\''.$nName.'\',\''.$nDateheur .'\') ');
    header('Location: http://www.locationhunter.ch/admin.php');
  }

  if (isset($_GET['delete'])){
    $idTG = $_GET['delete'];
    $answer = $bdd->query('DELETE from location WHERE id=\''.$idTG.'\' ');
    header('Location: http://www.locationhunter.ch/admin.php');
  }

  if (isset($_GET['newName']) && isset($_GET['newCoord'])){
    $bdd->exec('INSERT INTO location (coord,name) VALUES (\''.$_GET['newCoord'].'\',\''.$_GET['newName'].'\') ');
    header('Location: http://www.locationhunter.ch/admin.php');
  }


}
?>
<html>
  <head>
    <title>Administration Page | Location Hunter</title>

    <link rel=stylesheet href="mainStyleSheet.css">
    <link rel="apple-touch-icon" sizes="152x152" href="res/favicon/apple-touch-icon.png">
    <link rel="icon" type="image/png" sizes="32x32" href="res/favicon/favicon-32x32.png">
    <link rel="icon" type="image/png" sizes="16x16" href="res/favicon/favicon-16x16.png">

  </head>

  <body>
    <div class=divMenu>
      <img  src=res/LogoWebsite.png><br>
      <span class=pageAdminTitle>Location Hunter Administrator</span><br><br><br><br><br><br>

        <?php
        $answer = $bdd->query('SELECT * from location');
        while ($dataA = $answer->fetch()) {
          print($dataA['id']." - ". $dataA['name'] . " - <a href='admin.php?chgGoal=".$dataA['id']."'>Set as Today Goal</a> | <a href='admin.php?delete=".$dataA['id']."'>Delete</a><br>");
        }
        ?>

      <br>
      <form action=# method=get><input type=hidden name=newRandomCoord value=1><input type=submit value='New Random Coord' class=buttonCSS></form><br>
      <form action=chart.php method=get><input type=submit value='Stat Page' class=buttonCSS></form><br>
      <span class=legendTitle style='color:black;'>Add a new Location: </span><form action=# method=get>Name: <input type=text name=newName class=formInput placeholder="Name of Location"> |  GPS: <input type=text name=newCoord class=formInput placeholder="00.000000, 00.000000"><input type=submit value='Submit' class=buttonCSS></form><br>
      <form action='logout.php' method=get><input type=hidden name=redirect value=admin><input type=submit value='Logout' class=buttonCSS></form>
    </div>
  </body>
</html>
