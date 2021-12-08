<?php


try{
  $opts = array('http' =>
      array(
          'method'  => 'GET',
          'timeout' => 1
      )
  );
  $context  = stream_context_create($opts);
  $statutService = @file_get_contents('http://217.182.68.69:8080/api.py?command=getToday', false, $context);
}
catch (Exception $e)
{
	$statutService="";
}

if (Empty($statutService))
{
  $statutServiceShow ="close";
}
else {
  $statutServiceShow="open";
}
  ?>
