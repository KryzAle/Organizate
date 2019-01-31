<?PHP
$hostname_localhost="localhost";
$database_localhost="id8437416_organizate";
$username_localhost="id8437416_admin";
$password_localhost="alex1998";

$json=array();
	if(isset($_GET["id_redsocial"])){
		$red=$_GET["id_redsocial"];
		$conexion = mysqli_connect($hostname_localhost,$username_localhost,$password_localhost,$database_localhost);
		$consulta ="SELECT * FROM Usuario WHERE usu_usuario = '{$red}' AND usu_password= '{$red}'";
		$resultado=mysqli_query($conexion,$consulta);
			
		if($registro=mysqli_fetch_array($resultado)){
			$result["id"]=$registro['usu_id'];
			$result["nombre"]=$registro['usu_nombre'];
			$result["usuario"]=$registro['usu_usuario'];
			$result["password"]=$registro['usu_password'];
			$json['usuario'][]=$result;
		}else{
			$insert="INSERT INTO Usuario (`usu_nombre`,`usu_usuario`,`usu_password`) VALUES('{$red}','{$red}','{$red}')";
			$resultado_insert = mysqli_query($conexion,$insert);
			if ($resultado_insert) {
					$consulta ="SELECT * FROM Usuario WHERE usu_usuario = '{$red}' AND usu_password= '{$red}'";
					$resultado=mysqli_query($conexion,$consulta);
					if($registro=mysqli_fetch_array($resultado)){
					$result["id"]=$registro['usu_id'];
					$result["nombre"]=$registro['usu_nombre'];
					$result["usuario"]=$registro['usu_usuario'];
					$result["password"]=$registro['usu_password'];
					$json['usuario'][]=$result;
				}
					
			}else{
				echo json_encode("noregistra");
			}
		}
		echo json_encode($json);
		mysqli_close($conexion);
	}
	else{
		$resultar["success"]=0;
		$resultar["message"]='Ws no Retorna';
		$json['usuario'][]=$resultar;
		echo json_encode($json);
	}
?>
