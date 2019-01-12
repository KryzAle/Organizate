<?PHP
$hostname_localhost="localhost";
$database_localhost="id8437416_organizate";
$username_localhost="id8437416_admin";
$password_localhost="alex1998";

$json=array();
	if(isset($_POST["usuario"]) && isset($_POST["password"])){
		$usuario=$_POST["usuario"];
		$password=$_POST["password"];
		$conexion = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost,$database_localhost);
		$consulta ="SELECT * FROM Usuario WHERE usu_usuario = '{$usuario}' AND usu_password= '{$password}'";
		$resultado=mysqli_query($conexion,$consulta);
			
		if($registro=mysqli_fetch_array($resultado)){
			$result["id"]=$registro['usu_id'];
			$result["nombre"]=$registro['usu_nombre'];
			$result["usuario"]=$registro['usu_usuario'];
			$result["password"]=$registro['usu_password'];
			$json['usuario'][]=$result;
		}else{
			$resultar["id"]=0;
			$resultar["nombre"]='no registra';
			$resultar["usuario"]='no registra';
			$result["password"]='no registra';
			$json['usuario'][]=$resultar;
		}
		
		mysqli_close($conexion);
		echo json_encode($json);
	}
	else{
		$resultar["success"]=0;
		$resultar["message"]='Ws no Retorna';
		$json['usuario'][]=$resultar;
		echo json_encode($json);
	}
?>
