<?php
session_start();

//Import Module
include('module/langmodule.php');
include('module/trackmodule.php');
include('module/gdprTracker.php');
include('module/bddmodule2.php');
include('module/serviceStatusmodule.php');
?>
<html>
  <head>
    <title>Error| Location Hunter</title>

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
          <legend><span class=legendTitle>Error<span></legend>
          <span class=textPara>
            <?php
            if(isset($_GET['error'])){
              if($_GET['error']==1)
              {
                print("Your email of activation is not valid, please go on the website and ask for a new one !");
              }
              else if($_GET['error']==2){
                print("This email is already registred");
              }
              else if($_GET['error']==3){
                print("An error occured with the Capatcha");
              }
              else if($_GET['error']==4){
                print("The two passwords are not the same");
              }
              else if($_GET['error']==5){
                print("Error, please verify your email and your password");
              }

            }
            else {
              print("You do not have any error, why are you on this page !");
            }
             ?>
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
