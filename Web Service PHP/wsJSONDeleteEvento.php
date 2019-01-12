<?PHP
$hostname_localhost="localhost";
$database_localhost="id8437416_organizate";
$username_localhost="id8437416_admin";
$password_localhost="alex1998";

$conexion=mysqli_connect($hostname_localhost,$username_localhost,$password_localhost,$database_localhost);

if(isset($_POST["id_evento"])){
   
	$id_evento = $_POST["id_evento"];
	
$sql="DELETE FROM Evento WHERE eve_id = ?";
	$stm=$conexion->prepare($sql);
	$stm->bind_param('i',$id_evento);
	if($stm->execute()){
		echo json_encode("Borra");
	}else{
		echo json_encode("noBorra");
	}
}	
	mysqli_close($conexion);
?>