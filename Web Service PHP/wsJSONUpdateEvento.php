<?PHP
$hostname_localhost="localhost";
$database_localhost="id8437416_organizate";
$username_localhost="id8437416_admin";
$password_localhost="alex1998";

$conexion=mysqli_connect($hostname_localhost,$username_localhost,$password_localhost,$database_localhost);

if(isset($_POST["ubicacion"]) && isset($_POST["titulo"]) && isset($_POST["fecha"]) && isset($_POST["inicio"]) && isset($_POST["fin"]) && isset($_POST["nota"]) && isset($_POST["notificar"]) && isset($_POST["id_usuario"]) && isset($_POST["id_evento"])){
    
	$ubicacion = $_POST["ubicacion"];
	$titulo = $_POST["titulo"];
	$fecha = $_POST["fecha"];
	$inicio = $_POST["inicio"];
	$fin = $_POST["fin"];
	$nota = $_POST["nota"];
	$notificar = $_POST["notificar"];
	$id_usuario = $_POST["id_usuario"];
	$id_evento = $_POST["id_evento"];
	
$sql="UPDATE Evento SET eve_ubicacion = ?, eve_titulo = ?, eve_fecha = ?, eve_inicio = ?, eve_fin = ?, eve_nota = ?, eve_notificar = ?, eve_usu_id = ? WHERE eve_id = ?";
	$stm=$conexion->prepare($sql);
	$stm->bind_param('sssssssii',$ubicacion,$titulo,$fecha,$inicio,$fin,$nota,$notificar,$id_usuario,$id_evento);
	if($stm->execute()){
		echo json_encode("Actualiza");
	}else{
		echo json_encode("noActualiza");
	}
}	
	mysqli_close($conexion);
?>