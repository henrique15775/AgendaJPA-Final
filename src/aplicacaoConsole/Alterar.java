package aplicacaoConsole;
/**********************************
 * IFPB - Curso Superior de Tec. em Sist. para Internet
 * POB - Persistencia de Objetos
 * Prof. Fausto Ayres
 *
 */

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;

import fachada.Fachada;

public class Alterar {

	public Alterar(){
		Fachada.inicializar();
		System.out.println("alterando...");
		try {
			Fachada.alterarPessoa("joao","02/01/1990", carregarFoto("m2.jpg"), null);
			Fachada.alterarTelefone("988880000", "999999999");
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		Fachada.finalizar();
		System.out.println("fim do programa");
	}


	public byte[] carregarFoto(String arquivo) {
		try {
			URL url = Fachada.class.getResource("/fotos/"+arquivo);
			File f = new File(url.toURI());				// pasta src/fotos (interna)
			//File f = new File("/imagens/"+arquivo);	//pasta proj/imagens (externa)
			BufferedImage buffer = ImageIO.read(f);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(buffer, "jpg", baos );
			byte[] bytesfoto = baos.toByteArray();
			baos.close();
			return bytesfoto;

			//alternativa
			//			Path path = FileSystems.getDefault().getPath(arquivo);
			//			byte[] bytesfoto = Files.readAllBytes(path);
		} catch (IOException e) {
			throw new RuntimeException("arquivo invalido:"+arquivo);
		} catch (URISyntaxException e) {
			throw new RuntimeException("arquivo invalido:"+arquivo);
		}	
	}

	//=================================================
	public static void main(String[] args) {
		new Alterar();
	}
}

