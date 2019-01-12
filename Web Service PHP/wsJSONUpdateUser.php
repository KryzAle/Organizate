<?PHP
$hostname_localhost="localhost";
$database_localhost="id8437416_organizate";
$username_localhost="id8437416_admin";
$password_localhost="alex1998";

$conexion=mysqli_connect($hostname_localhost,$username_localhost,$password_localhost,$database_localhost);
if(isset($_POST["id"]) && isset($_POST["nombre"]) && isset($_POST["usuario"]) && isset($_POST["password"])){
	$id = $_POST["id"];
	$nombre = $_POST["nombre"];
	$usuario = $_POST["usuario"];
	$password = $_POST["password"];

	$sql="UPDATE Usuario SET usu_nombre = ?, usu_usuario = ?, usu_password = ? WHERE usu_id = ?";
	$stm=$conexion->prepare($sql);
	$stm->bind_param('sssi',$nombre,$usuario,$password,$id);
	if($stm->execute()){
		echo "Actualiza";
	}else{
		echo "noActualiza";
	}
}
	mysqli_close($conexion);
?>