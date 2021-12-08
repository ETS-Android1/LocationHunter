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
    <title><?php if($lang=="fr"){print("Téléchargement");}else{print("Download");}?> | Location Hunter</title>

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
          <legend><span class=legendTitle><?php if($lang=="fr"){print("Téléchargement");}else{print("Download");}?><span></legend>
          <span class=textPara>
            <?php if($lang=="fr"){print("Télécharger la dernière version de LocationHunter:<br><a href='https://play.google.com/store/apps/details?id=com.mzdev.maxime.locationhunt&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Disponible sur Google Play' width=20% height=20% src='https://play.google.com/intl/en_us/badges/images/generic/fr_badge_web_generic.png'/></a>");}
            else
            {print("Download the last version of LocationHunter:<br><a href='https://play.google.com/store/apps/details?id=com.mzdev.maxime.locationhunt&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Disponible sur Google Play' width=20% height=20% src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png'/></a>");}?>
            <hr color=white>
          </span>
          <br>
          <span class=textPara>
            <?php if($lang=="fr"){print("Autres Projets:<br>-L'application ConfirmAuth:<br><a href='https://play.google.com/store/apps/details?id=com.mzdev.maxime.rfc6238_authentificator&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Disponible sur Google Play' width=20% height=20% src='https://play.google.com/intl/en_us/badges/images/generic/fr_badge_web_generic.png'/></a>");}
            else
            {print("Other Projects:<br>-The application ConfirmAuth:<br><a href='https://play.google.com/store/apps/details?id=com.mzdev.maxime.rfc6238_authentificator&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Disponible sur Google Play' width=20% height=20% src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png'/></a>");}?>
          </span>
        </fieldset><br>
        <span class=textPara>&nbsp;&nbsp;Google Play and the Google Play logo are trademarks of Google LLC.</span>
      </p>


    &nbsp;</div>
  <footer>
    <?php
    include('module/footer.php');
    ?>
  </footer>
</body>

</html>
