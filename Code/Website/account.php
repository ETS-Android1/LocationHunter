<?php
session_start();

//Import Module
include('module/langmodule.php');
include('module/trackmodule.php');
include('module/gdprTracker.php');
include('module/bddmodule2.php');
include('module/serviceStatusmodule.php');
$capatcha = rand(1111111111,9999999999);
$_SESSION['capatcha'] = $capatcha;
?>
<html>
  <head>
    <title><?php if($lang=="fr"){print("Compte");}else{print("Account");}?> | Location Hunter</title>

    <link rel=stylesheet href="mainStyleSheet.css">
    <link rel="apple-touch-icon" sizes="152x152" href="res/favicon/apple-touch-icon.png">
    <link rel="icon" type="image/png" sizes="32x32" href="res/favicon/favicon-32x32.png">
    <link rel="icon" type="image/png" sizes="16x16" href="res/favicon/favicon-16x16.png">

  </head>

<body>
  <header>
    <?php
    include('module/header.php');
    ?>
  </header>
    <div class=divMain>

      <p class=pMain>
        <fieldset class=fieldsetPara>
          <legend><span class=legendTitle><?php if($lang=="fr"){print("Connexion");}else{print("Log In");}?><span></legend>
          <span class=textPara>
            <?php
            if(isset($_SESSION['userCo']) && isset($_SESSION['passCo'])){
              //Connected
              $pointsget = 0;
              $email = $_SESSION['userCo'];
              $password = $_SESSION['passCo'];
              $answer = $bdd->query('SELECT * from account WHERE email=\''.$email.'\' AND password=\''.$password.'\' ');
              while ($dataA = $answer->fetch()) {
                $pointsget = $dataA['points'];
              }
              $answer->closeCursor();

              print("You are Connected !<br>You have: ".$pointsget." points !<br><a href=logout.php?redirect=account>Logout</a>");
            }
            else{
              print("<form method=post action=accConnect.php>");
              if($lang=="fr"){print("E-Mail");}else{print("Email");}
              print(":<br>&nbsp;<input type=email name='email2' placeholder=Email class=formInput required><br>");
              if($lang=="fr"){print("Mot De Passe");}else{print("Password");}
              print(":<br>&nbsp;<input type=password name='password2' placeholder=Password class=formInput required><br>");
              print("&nbsp;<input type=submit class=buttonCSS value='Log In'>");
              print("</form>");
            }
            ?>
          </span>
        </fieldset>
      </p>

      <p class=pMain>
        <fieldset class=fieldsetPara>
          <legend><span class=legendTitle><?php if($lang=="fr"){print("Inscription");}else{print("Sign In");}?><span></legend>
          <span class=textPara>
            <form method=post action=accCreate.php>

              <?php if($lang=="fr"){print("E-Mail");}else{print("Email");}?>:<br>&nbsp;<input type=email name=email placeholder=Email class=formInput required><br>
              <?php if($lang=="fr"){print("Mot De Passe");}else{print("Password");}?>:<br>&nbsp;<input type=password name=password placeholder=Password class=formInput required><br>
              <?php if($lang=="fr"){print("Confirmez votre Mot De Passe");}else{print("Confirm Password");}?>:<br>&nbsp;<input type=password name=password2 placeholder="Repeat Password" class=formInput required><br>
              <?php if($lang=="fr"){print("Pseudonyme");}else{print("Username");}?>:<br>&nbsp;<input type=text name=username placeholder=Username class=formInput required maxlength="20"> maxlength: 20 characters<br>
                <?php if($lang=="fr"){print("Entrez ce code");}else{print("Enter this code");}?>: <?php print($capatcha); ?> <br>&nbsp;<input type=text name=capatcha placeholder=Capatcha class=formInput required maxlength="10" minlength="10"><br>
              &nbsp;<input type=submit class=buttonCSS value='Sig In'>

          </span>
        </fieldset>
      </p>

    &nbsp;</div>
  <footer>
    <?php
    include('module/footer.php');
    ?>
  </footer>
</body>

</html>
