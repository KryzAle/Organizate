<?PHP
$hostname_localhost="localhost";
$database_localhost="id8437416_organizate";
$username_localhost="id8437416_admin";
$password_localhost="alex1998";

$conexion=mysqli_connect($hostname_localhost,$username_localhost,$password_localhost,$database_localhost);
if(isset($_POST["id_redsocial"])){
	$id_redsocial = $_POST["id_redsocial"];
	

	$insert="INSERT INTO Usuario (`usu_nombre`,`usu_usuario`,`usu_password`) VALUES('{$id_redsocial}','{$id_redsocial}','{$id_redsocial}')";
	$resultado_insert = mysqli_query($conexion,$insert);
		
	if($resultado_insert){
		echo json_encode($resultado_insert);
	}else{
		echo json_encode("noregistra");
	}
}
	mysqli_close($conexion);
?>

