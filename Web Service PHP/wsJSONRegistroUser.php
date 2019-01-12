<?PHP
$hostname_localhost="localhost";
$database_localhost="id8437416_organizate";
$username_localhost="id8437416_admin";
$password_localhost="alex1998";

$conexion=mysqli_connect($hostname_localhost,$username_localhost,$password_localhost,$database_localhost);
if(isset($_POST["nombre"]) && isset($_POST["usuario"]) && isset($_POST["password"])){
	$nombre = $_POST["nombre"];
	$usuario = $_POST["usuario"];
	$password = $_POST["password"];

	$insert="INSERT INTO Usuario (`usu_nombre`,`usu_usuario`,`usu_password`) VALUES('{$nombre}','{$usuario}','{$password}')";
	$resultado_insert = mysqli_query($conexion,$insert);
		echo "{$resultado_insert}";
	if($resultado_insert){
		echo json_encode("registra");
	}else{
		echo json_encode("noregistra");
	}
}	
	mysqli_close($conexion);
?>