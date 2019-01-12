<?PHP
$hostname_localhost="localhost";
$database_localhost="id8437416_organizate";
$username_localhost="id8437416_admin";
$password_localhost="alex1998";

$conexion=mysqli_connect($hostname_localhost,$username_localhost,$password_localhost,$database_localhost);
if(isset($_POST["ubicacion"]) && isset($_POST["titulo"]) && isset($_POST["fecha"])&& isset($_POST["inicio"])&& isset($_POST["fin"])&& isset($_POST["nota"])&& isset($_POST["notificar"])&& isset($_POST["id"])){
    
	$ubicacion = $_POST["ubicacion"];
	$titulo = $_POST["titulo"];
	$fecha = $_POST["fecha"];
	$inicio = $_POST["inicio"];
	$fin = $_POST["fin"];
	$nota = $_POST["nota"];
	$notificar = $_POST["notificar"];
	$id = $_POST["id"];
	
	$insert="INSERT INTO Evento (`eve_ubicacion`, `eve_titulo`, `eve_fecha`, `eve_inicio`, `eve_fin`, `eve_nota`, `eve_notificar`, `eve_usu_id`) VALUES('{$ubicacion}','{$titulo}','{$fecha}','{$inicio}','{$fin}','{$nota}','{$notificar}','{$id}')";
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