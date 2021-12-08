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
    <title>Instruction | Location Hunter</title>

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
          <legend><span class=legendTitle><?php if($lang=="fr"){print("Instruction");}else{print("How to play");}?><span></legend>
          <span class=textPara>
            <?php if($lang=="fr"){print("
              Chaque jour à 10h00, une coordonnée GPS à Genève est tirée au sort. Plus rapidement tu l'atteins, plus tu gagnes des points. Ensuite, consulte ton score sur ce site internet.
              ");}
            else
            {print("
              Each day at 10:00 a.m. a GPS coordinates is drawn randomly. The faster you reach it, the more points you win. Then check out your score on this website.
              ");}?>
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
