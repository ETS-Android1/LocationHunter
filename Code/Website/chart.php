<?php
session_start();

if(!isset($_SESSION['usernameA']) || !isset($_SESSION['passwordA'])){
  header('Location: http://www.locationhunter.ch/adminC.php');
}

// load the data section
include('module/bddmodule.php');
$enCount = 0;
$frCount = 0;
$landStat = array();
$landList = array();
$sessionStat = array();
$sessionList = array();
$sessionListBis = array();
$dateConnectionArray = array();
$pageStat = array();
$indexPSigma = 0;
$indexPCounter = 0;
$instructionPSigma = 0;
$instructionPCounter = 0;
$scorePSigma = 0;
$scorePCounter = 0;
$accountPSigma = 0;
$accountPCounter = 0;
$downloadPSigma = 0;
$downloadPCounter = 0;
$errorPSigma = 0;
$errorPCounter = 0;

$answer = $bdd->query('SELECT * from tracking  ORDER BY id');
while ($dataA = $answer->fetch()) {
  if($dataA['langue']=="fr"){
    $frCount++;
  }
  else if ($dataA['langue']=="en"){
    $enCount++;
  }

  $dataLand = explode("," , $dataA['location']);

  if ((! $landStat[$dataLand[0]])==1){
    $landStat[$dataLand[0]] = 1;
    array_push($landList,$dataLand[0]);
  }
  else{
    $landStat[$dataLand[0]]++;
  }


  if ((! $sessionStat[$dataA['sessionID']])==1){
    $sessionStat[$dataA['sessionID']] = 1;
    array_push($sessionList,$dataA['sessionID'] );
  }else{
    $sessionStat[$dataA['sessionID']]++;
  }

  if ($dataA['page']=="index"){
    $indexPSigma += $dataA['pageTime'];
    $indexPCounter++;
  }
  elseif ($dataA['page']=="instruction") {
    $instructionPSigma += $dataA['pageTime'];
    $instructionPCounter++;
  }
  elseif ($dataA['page']=="score") {
    $scorePSigma += $dataA['pageTime'];
    $scorePCounter++;
  }
  elseif ($dataA['page']=="account") {
    $accountPSigma += $dataA['pageTime'];
    $accountPCounter++;
  }
  elseif ($dataA['page']=="download") {
    $downloadPSigma += $dataA['pageTime'];
    $downloadPCounter++;
  }
  elseif ($dataA['page']=="error") {
    $errorPSigma += $dataA['pageTime'];
    $errorPCounter++;
  }

  $dateArray = explode(":",$dataA['date']);
  $dateRebuild = $dateArray[0].":".$dateArray[1].":".$dateArray[2];
  if(!in_array($dataA['sessionID'],$sessionListBis)){
    array_push($sessionListBis,$dataA['sessionID']);
    if(!($dateConnectionArray[$dateRebuild]))
    {
      $dateConnectionArray[$dateRebuild] = 1;
    }else{
      $dateConnectionArray[$dateRebuild]++;
    }
  }




}


foreach ($sessionList as $sessionU) {
  if((! $pageStat[$sessionStat[$sessionU]]) == 1){
    $pageStat[$sessionStat[$sessionU]] = 1;
  }
  else{
    $pageStat[$sessionStat[$sessionU]]++;
  }
}

$indexTM = $indexPSigma/$indexPCounter;
$instructionTM = $instructionPSigma/$instructionPCounter;
$scoreTM = $scorePSigma/$scorePCounter;
$accountTM = $accountPSigma/$accountPCounter;
$downloadTM = $downloadPSigma/$downloadPCounter;
$errorTM = $errorPSigma/$errorPCounter;


?>

<html>
  <head>
    <title>Service Statistique | Location Hunter</title>

    <link rel=stylesheet href="mainStyleSheet.css">
    <link rel="apple-touch-icon" sizes="152x152" href="res/favicon/apple-touch-icon.png">
    <link rel="icon" type="image/png" sizes="32x32" href="res/favicon/favicon-32x32.png">
    <link rel="icon" type="image/png" sizes="16x16" href="res/favicon/favicon-16x16.png">
    <!--Load the AJAX API-->
    <!-- Using google chart API -->
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">

      google.charts.load('current', {'packages':['corechart']});
      google.charts.setOnLoadCallback(drawChart);




      function drawChart() {

        // begin of Language PieChart
        var dataLanguage = new google.visualization.DataTable();
        dataLanguage.addColumn('string', 'Topping');
        dataLanguage.addColumn('number', 'Slices');
        dataLanguage.addRows([
          <?php
          print("['En',".$enCount."],['Fr',".$frCount."]  ");
          ?>
        ]);
        var optionsLanguage = {'title':'User Language Stat',
                       'width':400,
                       'height':300,
                       'backgroundColor':'#f2f2f2'};
        var chartLanguage = new google.visualization.PieChart(document.getElementById('chart_div_Language'));
        chartLanguage.draw(dataLanguage, optionsLanguage);
        // END

        // begin of Location PieChart
        var dataLand = new google.visualization.DataTable();
        dataLand.addColumn('string', 'Topping');
        dataLand.addColumn('number', 'Slices');
        dataLand.addRows([
          <?php
          $counter = 1;
          $landNum = count($landList);
            foreach ($landList as $landU) {
              print("['".$landU."',".$landStat[$landU]."]");
              if ($counter!=$landNum){
                print(",");
              }
              $counter++;
            }
          ?>
        ]);
        var optionsLand = {'title':'Connection by country',
        'width':400,
        'height':300,
        'backgroundColor':'#f2f2f2'
      };
      var chartLand = new google.visualization.PieChart(document.getElementById('chart_div_Land'));
      chartLand.draw(dataLand, optionsLand);
      //END


      //begin of Page Stat
      var dataPage = new google.visualization.DataTable();
      dataPage.addColumn('string', 'Topping');
      dataPage.addColumn('number', 'Slices');
      dataPage.addRows([

        <?php
        $counter = 1;
        $pageNum = count($pageStat);
          foreach ($pageStat as $key => $value) {
            print("['".$key."',".$value."]");
            if ($counter != $pageNum){
              print(",");
            }
            $counter++;
          }

         ?>

      ]);

      var optionsPage = {'title':'How many page are visited by Session',
      'width':400,
      'height':300,
      'backgroundColor':'#f2f2f2'
      };
      var chartPage = new google.visualization.PieChart(document.getElementById('chart_div_Page'));
      chartPage.draw(dataPage,optionsPage);
      //END


      //begin of Rep of time on website Moy
      var tempsMoyen = new google.visualization.DataTable();
      tempsMoyen.addColumn('string', 'Topping');
      tempsMoyen.addColumn('number', 'Slices');
      tempsMoyen.addRows([
        <?php
        print("['index',".$indexTM."], ['instruction',".$instructionTM."], ['score',".$scoreTM."], ['account', ".$accountTM."], ['download', ".$downloadTM."], ['error', ".$errorTM."]");
        ?>
      ]);

      var optionsTM = {'title':'Répartition du temps en moyenne sur le site par page',
      'width':400,
      'height':300,
      'backgroundColor':'#f2f2f2'
      };

      var chartTM = new google.visualization.PieChart(document.getElementById('chart_div_TM'));
      chartTM.draw(tempsMoyen,optionsTM);
      //END


      //begin line chart  test
      var dataTest = google.visualization.arrayToDataTable([
    ['Date', 'Visite'],
    <?php
    $counter = 1;
    $countDateConectionArray = count($dateConnectionArray);
      foreach ($dateConnectionArray as $key => $value) {
        print("['".$key."',".$value."]");
        if ($counter != $countDateConectionArray){
          $counter++;
          print(",");
        }
      }
    ?>
    ]);

  var optionsTest = {
    title: 'Visite du site web',
    curveType: 'function',
    'backgroundColor':'#f2f2f2',
    legend: { position: 'bottom' }
  };

  var chartTest = new google.visualization.LineChart(document.getElementById('curve_chart'));

  chartTest.draw(dataTest, optionsTest);

      //END

      }

    </script>
  </head>

  <body>
    <div class=divMenu>
    <img  src=res/LogoWebsite.png><br>
    <span class=pageAdminTitle>Location Hunter Administrator</span><br><br><br><br><br><br>
    <div id="chart_div_Language" align="center"></div>
    <div id="chart_div_Land" align="center"></div>
    <div id="chart_div_Page" align="center"></div>
    <div id="chart_div_TM" align="center"></div>
    <div id="curve_chart" style="width: 900px; height: 500px;"></div>
  </div>

  </body>
</html>
