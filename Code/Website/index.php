<?php
session_start();

//Import Module
include('module/langmodule.php');
include('module/trackmodule.php');
include('module/gdprTracker.php');
include('module/bddmodule.php');
include('module/serviceStatusmodule.php');
?>
<html>
  <head>
    <title><?php if($lang=="fr"){print("Accueil");}else{print("Homepage");}?> | Location Hunter</title>

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

      <?php
        if ($lang=="fr"){$tableNews="fr_news";}else{$tableNews="en_news";}
        $answer = $bdd->query('SELECT * from '.$tableNews.'  ORDER BY id');
        while ($dataA = $answer->fetch()) {
          print("<p class=pMain>
                      <fieldset class=fieldsetPara>
                        <legend><span class=legendTitle>".$dataA['title']."<span></legend>
                        <span class=textPara>".$dataA['text']."</span>
                      </fieldset>
                    </p> ");
        }
        $answer->closeCursor();
      ?>

    <!-- Sample to write a paragraph
      <p class=pMain>
        <fieldset class=fieldsetPara>
          <legend><span class=legendTitle>This is a Title<span></legend>
          <span class=textPara>this is a paragraph! Lorem Ipsum</span>
        </fieldset>
      </p>
    -->

    &nbsp;</div>
  <footer>
<?php
include('module/footer.php');
?>
  </footer>
</body>

</html>
