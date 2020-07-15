/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulacoes;

import dp.Const;
import dp.D;
import java.io.File;
import java.io.FileNotFoundException;

/**
 *
 * @author Tarcísio Pontes
 */
public class Base {
    private String nome;
    private String caminhoBase;
    private int numeroExemplos;
    private int numeroExemplosPositivo;
    private int numeroExemplosNegativo;
    private int numeroAtributos;
    private int numeroItens;
    private String tipoDiscretizacao;
    private int formatacaoBase;

    public Base(String caminhoBase, int formatacaoBase) throws FileNotFoundException {
        this.caminhoBase = caminhoBase;
        this.formatacaoBase = formatacaoBase;
        this.carregarBaseEmD();
        this.nome = D.nomeBase;
        this.numeroAtributos = D.numeroAtributos;
        this.numeroExemplos = D.numeroExemplos;
        this.numeroExemplosPositivo = D.numeroExemplosPositivo;
        this.numeroExemplosNegativo = D.numeroExemplosNegativo;
        this.numeroItens = D.numeroItensUtilizados;
        this.tipoDiscretizacao = D.tipoDiscretizacao;
    }

    public void carregarBaseEmD() throws FileNotFoundException{
        D.CarregarArquivo(this.caminhoBase, this.formatacaoBase);
        D.GerarDpDn("p");
    }
    
    public String getNome() {
        return nome;
    }

    public String getCaminhoBase() {
        return caminhoBase;
    }

    public int getNumeroExemplos() {
        return numeroExemplos;
    }

    public int getNumeroExemplosPositivo() {
        return numeroExemplosPositivo;
    }

    public int getNumeroExemplosNegativo() {
        return numeroExemplosNegativo;
    }

    public int getNumeroAtributos() {
        return numeroAtributos;
    }

    public String getTipoDiscretizacao() {
        return tipoDiscretizacao;
    }
    
    public int getNumeroItens(){
        return this.numeroItens;
    }

    public static void main(String args[]) throws FileNotFoundException{
        String caminhoPastaArquivos = Const.CAMINHO_BASES;
        File diretorio = new File(caminhoPastaArquivos);
        File arquivos[] = diretorio.listFiles();
        
        Base[] bases = new Base[arquivos.length];
        
        for(int i = 0; i < bases.length; i++){
            bases[i] = new Base(arquivos[i].getAbsolutePath(), D.TIPO_CSV);
            System.out.println(bases[i].getNome());
        }
        //"C:/BD/Resultados"
        System.out.println();
    }
}
