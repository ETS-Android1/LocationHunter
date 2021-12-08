<!--jquery.js, clippy.css et clippy.js sont des
bibliothèques javascript provenant d'internet:
- https://jquery.com/
- http://smore.com/clippy-js
-->
<?php
session_start();

include('module/config.php');

  if(isset($_GET['accept'])){
    if($_GET['accept']==1)
    {
      $_SESSION['gdpr'] = 1;
      if (isset($_SESSION['gdprBef']))
      {
        header('Location: /'.$servPath.$_SESSION['gdprBef']);
      }
      else {
        header('Location: /'.$servPath.'index.php');
      }

    }
  }
?>
<html>
  <head>
    <title>GPRD - Warning</title>
    <link rel=stylesheet href="mainStyleSheet.css">
    <link rel="stylesheet" type="text/css" href="module/clippy.css" media="all"> 
  </head>
  <body>
    Attention sur ce site Internet, nous collectons:<br>
    1) Votre adresse IP.<br>
    2) Les pages que vous visitez.<br><br>
    Ces données ne sont pas utilisées à des fins commerciales et ne permettent pas de vous identifier. Elles servent uniquement à l'amélioration du service et à des fins statistiques.
    <br><hr>
    Warning on this website we collect:<br>
    1) You IP adress.<br>
    2) The web pages visited.<br><br>
  Data are collected for statistical purposes only and do not allow the Administrator to identify you.
    <br>
    <form action=gdprwarning.php method=get>
      <fieldset>
        <legend>Accept the terms and condition to proceed</legend>
        <input type=hidden name=accept value=1>
        <input type=submit value=Accept class=buttonCSS> <!-- to disable connexion add disabled attribut -->
      </fieldset>
    </form>
    Thanks www.smore.com for developing the library <a href=http://smore.com/clippy-js>Clippy.js</a>
  </body>

  <script src="module/jquery-3.3.1-min.js"></script>

  <!-- Clippy.js -->
  <script src="module/clippy.min.js"></script>

  <!-- Init script -->
  <script type="text/javascript">
      clippy.load('Clippy', function(agent){
          // do anything with the loaded agent
          agent.show();
          agent.play("CheckingSomething");
          agent.speak("Will you accept our Terms and Condition ?");
      });
  </script>
</html>
