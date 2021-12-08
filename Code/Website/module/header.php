<?php
print('<div class=divMenu><a href=index.php><img class=logoWeb src=res/LogoWebsite.png><span class=pageTitle>LocationHunter</span></a><ul class=ulMenu><li class=liMenu><a class=aMenu href=index.php>');
  if($lang=="fr"){print("Accueil");}else{print("Homepage");}
  print('</a></li> |
    <li class=liMenu><a class=aMenu href=score.php>Score</a></li> |
    <li class=liMenu><a class=aMenu href=account.php>');

    if($lang=="fr"){print("Compte");}else{print("Account");}

    print('</a></li> |
    <li class=liMenu><a class=aMenu href=download.php>');

    if($lang=="fr"){print("Téléchargement");}else{print("Download");}

    print('</a></li> |
    <li class=liMenu><a class=aMenu href=instruction.php>Instruction</a></li> |
    <span class=languageChange><li class=liMenu>');

    if($lang=="fr"){print("<a class=aMenu href=\"?lang=en\"><img src=res/EnglishFlag.png width=30px height=20px></a>");}else{print("<a class=aMenu href=\"?lang=fr\"><img src=res/FrenchFlag.png width=30px height=20px></a>");}

    print('</li></span></ul></div>'); 
?>
