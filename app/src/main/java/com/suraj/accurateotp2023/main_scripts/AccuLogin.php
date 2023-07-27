<?php

$serverName = "SQL5030.myWindowsHosting.com"; 
$connectionInfo = array( "Database"=>"DB_A2B7AB_accdata", "UID"=>"DB_A2B7AB_accdata_admin", "PWD"=>"1Accusoft1");
$conn = sqlsrv_connect( $serverName, $connectionInfo);

$phone=$_POST['Phone'];
 $flag = $_POST['Flag'];


$query = "SELECT * from Users where Phone='$phone'and Flag = '$flag'";

$result = sqlsrv_query($conn,$query);
$rows=sqlsrv_has_rows($result);


if($rows===false)
{ 

	die("Error in query");

}

$response['students'] = array();

	
while($row=sqlsrv_fetch_array($result))

		{
			array_push($response['students'], $row);

		break;
			
}



		if($response)
		{

			$response['message']="Login Sucessful";

 	 $up = sqlsrv_query($conn,"UPDATE Users SET Flag = 'on' where Phone = '$phone'");


		}

	
else{

 	$response['message']="Login Failure";

 }

 echo json_encode($response);






?>



