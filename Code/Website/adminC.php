<!DOCTYPE html>
<html>
  <head>
    <title>Admin Connection Page | Location Hunter</title>

    <link rel=stylesheet href="mainStyleSheet.css">
    <link rel="apple-touch-icon" sizes="152x152" href="res/favicon/apple-touch-icon.png">
    <link rel="icon" type="image/png" sizes="32x32" href="res/favicon/favicon-32x32.png">
    <link rel="icon" type="image/png" sizes="16x16" href="res/favicon/favicon-16x16.png">

  </head>
  <body>
    <div class=divMenu>
      <img  src=res/LogoWebsite.png><br>
      <span class=pageAdminTitle>Location Hunter Administrator</span><br><br><br><br><br><br>
      <form action=admin.php method=post>
        <span class=textPara style='color:black;position:absolute;margin-left:-10%;'>
          Username: <input type=text name=username class=formInput placeholder="Username" required><br>
          Password: <input type=password name=password class=formInput placeholder="Password" required><br>
          <input type=submit value='Connexion' class=buttonCSS>
      </span>
      </form>
    </div>
  </body>
</html>
