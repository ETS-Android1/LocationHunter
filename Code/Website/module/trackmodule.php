<?php

function ip_info($ip = NULL, $purpose = "location", $deep_detect = TRUE) {
    $output = NULL;
    if (filter_var($ip, FILTER_VALIDATE_IP) === FALSE) {
        $ip = $_SERVER["REMOTE_ADDR"];
        if ($deep_detect) {
            if (filter_var(@$_SERVER['HTTP_X_FORWARDED_FOR'], FILTER_VALIDATE_IP))
                $ip = $_SERVER['HTTP_X_FORWARDED_FOR'];
            if (filter_var(@$_SERVER['HTTP_CLIENT_IP'], FILTER_VALIDATE_IP))
                $ip = $_SERVER['HTTP_CLIENT_IP'];
        }
    }
    $purpose    = str_replace(array("name", "\n", "\t", " ", "-", "_"), NULL, strtolower(trim($purpose)));
    $support    = array("country", "countrycode", "state", "region", "city", "location", "address");
    $continents = array(
        "AF" => "Africa",
        "AN" => "Antarctica",
        "AS" => "Asia",
        "EU" => "Europe",
        "OC" => "Australia (Oceania)",
        "NA" => "North America",
        "SA" => "South America"
    );
    if (filter_var($ip, FILTER_VALIDATE_IP) && in_array($purpose, $support)) {
        $ipdat = @json_decode(file_get_contents("http://www.geoplugin.net/json.gp?ip=" . $ip));
        if (@strlen(trim($ipdat->geoplugin_countryCode)) == 2) {
            switch ($purpose) {
                case "location":
                    $output = array(
                        "city"           => @$ipdat->geoplugin_city,
                        "region"          => @$ipdat->geoplugin_regionName,
                        "country"        => @$ipdat->geoplugin_countryName,
                        "country_code"   => @$ipdat->geoplugin_countryCode,
                        "continent"      => @$continents[strtoupper($ipdat->geoplugin_continentCode)],
                        "continent_code" => @$ipdat->geoplugin_continentCode
                    );
                    break;
            }
        }
    }
    return $output;
  }

if (! empty($_SESSION['page'])){
  // Track previous page information if was inside website
  $previousPage = $_SESSION['page'];
  $timePreviousPage = time()-$_SESSION['pageloadedtime'];
}
else {
  // Track previous page information if was outside website
  $previousPage = "%OutsideOfWebsite";
  $timePreviousPage = 0;
  $_SESSION['lang'] = "en";
}
  $actualPage = explode(".",basename($_SERVER['PHP_SELF']))[0];
  $id = session_id();
  $ip = $_SERVER['REMOTE_ADDR'];
  $ipInfoArray = ip_info($ip,"location");
  $ipLocation = $ipInfoArray["country"].",".$ipInfoArray["region"];



$_SESSION['page']=$actualPage;
$_SESSION['pageloadedtime']=time();

/*
print "ID: ".$id;
print "<br>";
print "Time on Previous Webpage: ".$timePreviousPage;
print "<br>";
print "Previous Webpage: ".$previousPage;
print "<br>";
print "Actual Webpage: ".$actualPage;
print "<br>";
print "Client IP: ".$ip;
print "<br>";
print "Client Location: ".$ipLocation;
*/

$dateUp = date("Y:m:d:H:i:s");
if ($timePreviousPage != 0)
{
  include('bddmodule.php');
  $bdd->exec('INSERT INTO tracking (ip,location,sessionID,page,pageTime,pageNext,langue,date) VALUES (\''.$ip.'\',\''.$ipLocation.'\',\''.$id.'\',\''.$previousPage.'\',\''.$timePreviousPage.'\',\''.$actualPage.'\',\''.$lang.'\', \''.$dateUp.'\') ');
}



 ?>
