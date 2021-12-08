<html>
  <head>
    <title>Confirm Your Email | Location Hunter</title>

    <link rel=stylesheet href="email.css">

  </head>

<body>
  <header>
    <div class=divMenu><a href=index.php><img class=logoWeb src=http://www.locationhunter.ch/res/LogoWebsite.png><span class=pageTitle>LocationHunter</span></a>
  </header>
    <div class=divMain>

      <p class=pMain>
        <fieldset class=fieldsetPara>
          <legend><span class=legendTitle>Click Here to confirm your Email | Cliquez ici afin de confirmer votre adresse E-mail<span></legend>
          <span class=textPara><br>
            <?php
              if (isset($_GET['code'])&&isset($_GET['email'])){
                print("<a href='http://www.locationhunter.ch/confirmed.php?code=".$_GET['code']."&email=".$_GET['email']."' style='color:white; text-decoration:none; background-color:lightgrey; padding:8px 8px; border: 1px solid white;'><strong>Validate</strong></a>");
              }
                else {
                  print("Your Email is not valid, please go back on our website an ask for a new one !");
                }
            ?>
            <br>
          </span>
        </fieldset>
      </p>

    &nbsp;</div>
  <footer>
    <br>
    <hr class=lineMenu>
    <div class=divFooter>
      &#169; 2018 Website by Maxime <br>
    </div>
  </footer>
</body>

</html>
