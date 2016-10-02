package models;

import akka.stream.javadsl.Zip;

import java.io.*;
import java.util.zip.*;

public class Util {

    public boolean validaCredenciais(String nome, String email, String senha){
        EmailValidator userMail = new EmailValidator();
        if (nome.length() > 2)
            if (nome.length() < 21) if (senha.length() > 7) if (userMail.validate(email)) return true;
        return false;
    }

    public void comprimeZip(File objeto) throws IOException{
        int cont;
        int BUFFER = 4096;
        byte[] dados = new byte[BUFFER];

        BufferedInputStream origem = null;
        FileInputStream streamDeEntrada = null;
        FileOutputStream destino = null;
        ZipOutputStream saida = null;
        ZipEntry entry = null;

        try {
            destino = new FileOutputStream(new File(objeto.getAbsolutePath()+".zip"));
            saida = new ZipOutputStream(new BufferedOutputStream(destino));
            streamDeEntrada = new FileInputStream(objeto);
            origem = new BufferedInputStream(streamDeEntrada, BUFFER);
            entry = new ZipEntry(objeto.getName());
            saida.putNextEntry(entry);

            while((cont = origem.read(dados, 0, BUFFER)) != -1) {
                saida.write(dados, 0, cont);
            }

            origem.close();
            saida.close();
        } catch(IOException e) {
            e.printStackTrace();
        }

    }

    public void comprimeGzip(File objeto){

            try {
                GZIPOutputStream saidaZip = new GZIPOutputStream(new FileOutputStream(objeto.getAbsolutePath()+".gz"));
                byte[] out = objeto.getAbsolutePath().getBytes("ISO8859_1"); //Codifica o arquivo em utf-8
                saidaZip.write(out, 0, out.length);
                saidaZip.finish();
                saidaZip.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

}
