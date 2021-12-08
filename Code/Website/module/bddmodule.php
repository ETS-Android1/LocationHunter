<?php
//Information
$host="localhost";
$user="root";
$pass="xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
$databaseName ="lhwebsite";
//

//Connection
try
{
	$bdd = new PDO('mysql:host='.$host.';dbname='.$databaseName.';charset=utf8', $user, $pass);
}
catch (Exception $e)
{
	die('Erreur : ' . $e->getMessage());
}
//
 ?>
