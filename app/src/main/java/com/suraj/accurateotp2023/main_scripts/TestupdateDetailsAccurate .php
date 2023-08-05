<?php

$serverName = "SQL5030.myWindowsHosting.com"; 
$connectionInfo = array( "Database"=>"DB_A2B7AB_accdata", "UID"=>"DB_A2B7AB_accdata_admin", "PWD"=>"1Accusoft1");
$conn = sqlsrv_connect( $serverName, $connectionInfo);

$id=$_POST['User_ID'];
$name = $_POST['Doneby'];
$version = $_POST['Version'];
$client = $_POST['Client'];
$count = $_POST['Count'];
$printcnt = $_POST['Printcnt'];
date_default_timezone_set('Asia/Kolkata');
$time = date('d-m-Y H:i');
$A3Value = $_POST['A3Value'];
$A4Value = $_POST['A4Value'];
$A5Value = $_POST['A5Value'];


$query = "UPDATE Users set Count='$count' where User_ID = $id";



$query1 = "INSERT INTO Recharge ([User_id],[DoneBy],[Version],[PrintCnt],[Client],[Device],[Date], [A3], [A4], [A5]) VALUES('$id','$name','$version','$printcnt','$client','Mobile','$time','$A3Value','$A4Value','$A5Value')";

$result = sqlsrv_query($conn,$quercvy);
$result1 = sqlsrv_query($conn,$query1);
$response =array();

 if($result && $result1){
 	$response['success']=1;
 	$response['message']="Sucessfully";
 }else{
 	$response['success']=0;
 	$response['message']="Failure";
 }
 echo json_encode($response);

?>


