<?PHP
$hostname_localhost="localhost";
$database_localhost="id8437416_organizate";
$username_localhost="id8437416_admin";
$password_localhost="alex1998";

$json=array();
	if(isset($_POST["id_usuario"])){
	    $id = $_POST["id_usuario"];
		$conexion = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost,$database_localhost);

		$consulta="select * from Evento WHERE `eve_usu_id` = {$id}";
		$resultado=mysqli_query($conexion,$consulta);
		
		while($registro=mysqli_fetch_array($resultado)){
			$result["id_evento"]=$registro['eve_id'];
			$result["titulo"]=$registro['eve_titulo'];
			$result["inicio"]=$registro['eve_inicio'];
			$result["fin"]=$registro['eve_fin'];
			$result["fecha"]=$registro['eve_fecha'];
			$result["nota"]=$registro['eve_nota'];
			$result["notificar"]=$registro['eve_notificar'];
			$json['evento'][]=$result;
		}
		mysqli_close($conexion);
		echo json_encode($json);
	}
?>

