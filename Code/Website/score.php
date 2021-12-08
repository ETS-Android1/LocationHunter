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
    <title>Score | Location Hunter</title>

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
          <legend><span class=legendTitle>Top 5 Best Score<span></legend>
          <span class=textPara>
            <table>
              <tr>
                <th class=scoreTab><?php if($lang=="fr"){print("&nbsp;Classement&nbsp;");}else{print("&nbsp;Ranking&nbsp;");}?></th>
                <th class=scoreTab2><?php if($lang=="fr"){print("&nbsp;Pseudonyme&nbsp;");}else{print("&nbsp;Username&nbsp;");}?></th>
                <th class=scoreTab> Points </th>
              </tr>
      <?php
        $count =1;
        $answer = $bdd->query('SELECT * from account WHERE points!=0 AND enabled=1 ORDER BY points DESC LIMIT 5');
        while ($dataA = $answer->fetch()) {
          print("<tr><th class=scoreTab3>".$count."</th><th class=scoreTab3>".$dataA['username']."</th><th class=scoreTab3>".$dataA['points']."</th></tr>");
          $count += 1;
        }
        $answer->closeCursor();
      ?>
            </table>
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
