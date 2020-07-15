/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolucionario;

import dp.Const;
import dp.Pattern;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 *
 * @author Tarcísio Lucas
 * @since 27/01/2013
 * @version 2.0 26/06/2017
 */
public class CRUZAMENTO {
    
    
    /////////////////////////////////////////////////////////////
    //UNIFORME TRADICIONAL //////////////////////////////////////
    /////////////////////////////////////////////////////////////
        
    /**Cruzamento gera população a partir de cruzamentos do tipo uniforme e de mutações
     *@author Tarcísio Pontes
     * @param P Pattern[] - população antiga 
     * @param taxaMutacao double - taxa de indivúduos que terão um gene modificado
     * @param tipoAvaliacao int - tipo de função de avaliação utilizada
     * @return Pattern[] - nova população
     */
    public static Pattern[] uniforme2Pop(Pattern[] P, double taxaMutacao, String tipoAvaliacao){
        int tamanhoPopulacao = P.length;
        Pattern[] Pnovo = new Pattern[tamanhoPopulacao];
        
        //int[] selecao = SELECAO.proporcao25_75(tamanhoPopulacao);
        int[] selecao = SELECAO.torneioBinario(tamanhoPopulacao, P);           
        
        int indiceSelecao = 0;
        int indicePnovo = 0;
        while(indicePnovo < Pnovo.length-1){//Cuidado para não acessar índices maiores que o tamanho do array                
            if(Const.random.nextDouble() > taxaMutacao){                    
                Pattern[] novos = CRUZAMENTO.uniforme2Individuos(P[selecao[indiceSelecao]], P[selecao[indiceSelecao+1]], tipoAvaliacao);
                indiceSelecao += 2;
                Pnovo[indicePnovo++] = novos[0];                    
                if(indicePnovo < Pnovo.length){
                    Pnovo[indicePnovo++] = novos[1];                                                        
                }
                
            }else{
                Pnovo[indicePnovo++] = MUTACAO.unGeneTrocaOuAdicionaOuExclui(P[selecao[indiceSelecao++]], tipoAvaliacao);                                                       
            }         
        
        //Imprimir itens nos idivíduos gerados via cruzamento
//        DPinfo.imprimirItens(P[selecao[indiceSelecao-2]]);
//        DPinfo.imprimirItens(P[selecao[indiceSelecao-1]]);
//        System.out.print("->");
//        DPinfo.imprimirItens(Pnovo[indicePnovo-2]);
//        DPinfo.imprimirItens(Pnovo[indicePnovo-1]);
//        System.out.println();
        }
        
        if(indicePnovo < Pnovo.length){
            Pnovo[indicePnovo] = MUTACAO.unGeneTrocaOuAdicionaOuExclui(P[selecao[indiceSelecao++]], tipoAvaliacao);                                                                   
        }
                     
        return Pnovo;
        
    }
        
    /**Cruzamento gera dois indivíduos a partir do método uniforme
     *@author Tarcísio Pontes
     * @param p1 Pattern[] - indivíduo 1 
     * @param p2 Pattern[] - indivíduo 2
     * @param tipoAvaliacao int - tipo de função de avaliação utilizada
     * @return Pattern[] - vetor com dois novos indivíduos
     */
    public static Pattern[] uniforme2Individuos(Pattern p1, Pattern p2, String tipoAvaliacao){
        Pattern[] novosPattern = new Pattern[2];
        HashSet<Integer> novoItens1 = new HashSet<>();
        HashSet<Integer> novoItens2 = new HashSet<>();

        Iterator iterator = p1.getItens().iterator();
        while(iterator.hasNext()){
            if(Const.random.nextBoolean()){
                novoItens1.add((Integer)iterator.next());
            }else{          
                novoItens2.add((Integer)iterator.next());
            }
        }
        iterator = p2.getItens().iterator();
        while(iterator.hasNext()){
            if(Const.random.nextBoolean()){
                novoItens1.add((Integer)iterator.next());
            }else{          
                novoItens2.add((Integer)iterator.next());
            }
        }
        novosPattern[0] = new Pattern(novoItens1, tipoAvaliacao);
        novosPattern[1] = new Pattern(novoItens2, tipoAvaliacao);
        return novosPattern;           
    }

    
    /////////////////////////////////////////////////////////////
    // AND                 //////////////////////////////////////
    /////////////////////////////////////////////////////////////
      
    /**Reliza cruzamento do tipo AND entre indivíduos de duas populações distintas
     *@author Tarcísio Pontes
     * @param P1 Pattern[] - população 1 
     * @param P2 Pattern[] - população 2
     * @param tipoAvaliacao int - tipo de função de avaliação
     * @return Pattern[] - nova população
     */
    public static Pattern[] ANDduasPopulacoes(Pattern[] P1, Pattern[] P2, String tipoAvaliacao){
        int tamanhoPopulacao = P1.length;       
        Pattern[] Pnovo = new Pattern[tamanhoPopulacao];
        int[] indicesP1 = SELECAO.torneioBinario(tamanhoPopulacao, P1);
        int[] indicesP2 = SELECAO.torneioBinario(tamanhoPopulacao, P2);
        
        for(int i = 0; i < tamanhoPopulacao; i++){
            Pattern p1 = P1[indicesP1[i]];       
            Pattern p2 = P2[indicesP2[i]];
            Pnovo[i] = CRUZAMENTO.AND(p1, p2, tipoAvaliacao);
        }        
        return Pnovo;
    }    
    
    /**Reliza cruzamento do tipo AND entre dois indivíduos
     *@author Tarcísio Pontes
     * @param p1 Pattern - indivíduo 1 
     * @param p2 Pattern - indivíduo 2
     * @param tipoAvaliacao int - tipo de função de avaliação
     * @return Pattern - novo indivíduo
     * @since 27/01/2016     * 
     */
    public static Pattern AND(Pattern p1, Pattern p2, String tipoAvaliacao){
        HashSet<Integer> novoitens = new HashSet<>();
        novoitens.addAll(p1.getItens());
        novoitens.addAll(p2.getItens());
        
        return new Pattern(novoitens, tipoAvaliacao);
    }
    

    /////////////////////////////////////////////////////////////
    //UNIFORME RESTRITO A TAMANHO FIXO ///// ////////////////////
    /////////////////////////////////////////////////////////////
            
    /**Dois indivíduos de tamanho d geram outros dois do mesmo tamanho d
     * pelo método uniforme
     *@author Tarcísio Pontes
     * @param p1 Pattern[] - indivíduo 1 
     * @param p2 Pattern[] - indivíduo 2
     * @param tipoAvaliacao int - tipo de função de avaliação utilizada
     * @return Pattern[] - novos indivíduos
     */
    public static Pattern[] uniforme2D(Pattern p1, Pattern p2, String tipoAvaliacao){
        Pattern[] p = new Pattern[2];
        int d = p1.getItens().size();
        ArrayList<Integer> itensTodos = new ArrayList<>();
        itensTodos.addAll(p1.getItens());
        itensTodos.addAll(p2.getItens());
        
        HashSet<Integer> itens = new HashSet<>();
        while(itens.size() < d){
            itens.add(itensTodos.get(Const.random.nextInt(itensTodos.size())));
        }
        p[0] = new Pattern(itens, tipoAvaliacao);
        
        itens = new HashSet<>();
        while(itens.size() < d){
            itens.add(itensTodos.get(Const.random.nextInt(itensTodos.size())));
        }
        p[1] = new Pattern(itens, tipoAvaliacao);       
        
        return p;
    }
    
    
    /**Cruzamento gera população a partir de cruzamentos do tipo uniforme2D 
     * e mutações
     *@author Tarcísio Pontes
     * @param P Pattern[] - população antiga 
     * @param taxaMutacao double - taxa de indivúduos que terão um gene modificado
     * @param tipoAvaliacao int - tipo de função de avaliação utilizada
     * @return Pattern[] - nova população
     */
    public static Pattern[] uniforme2DPop(Pattern[] P, double taxaMutacao, String tipoAvaliacao){
        int tamanhoPopulacao = P.length;        
        Pattern[] Pnovo = new Pattern[tamanhoPopulacao];
               
        int indicePnovo = 0;
        int indiceP1 = 0;
        int indiceP2 = 0;
        while(indicePnovo < Pnovo.length){//Cuidado para não acessar índices maiores que o tamanho do array                     
            if(Const.random.nextDouble() > taxaMutacao){                                    
                //Obtendo índices do indivíduos que serão cruzados
//                if(random.nextDouble() > 0.75){//75% de chanses de ser selecionado um dos 25% mais relevantes
//                    indiceP1 = random.nextInt(P.length*1/4);
//                    indiceP2 = random.nextInt(P.length*1/4);
//                }else{//25% de chanses de ser selecionado totalmente aleatório
//                    indiceP1 = random.nextInt(P.length);
//                    indiceP2 = random.nextInt(P.length);
//                }
                indiceP1 = SELECAO.torneioBinario(P);
                indiceP2 = SELECAO.torneioBinario(P);
                
                Pattern[] novos = CRUZAMENTO.uniforme2D(P[indiceP1], P[indiceP2], tipoAvaliacao);
                Pnovo[indicePnovo++] = novos[0];
                if(indicePnovo < Pnovo.length){
                    Pnovo[indicePnovo++] = novos[1];                    
                }                                                                      
            }else{
//                if(random.nextDouble() > 0.75){//75% de chanses de ser selecionado um dos 25% mais relevantes
//                    indiceP1 = random.nextInt(P.length*1/4);                    
//                }else{//25% de chanses de ser selecionado totalmente aleatório
//                    indiceP1 = random.nextInt(P.length);                    
//                }
                indiceP1 = SELECAO.torneioBinario(P);                    
                Pnovo[indicePnovo++] = MUTACAO.unGeneD(P[indiceP1], tipoAvaliacao);                                                       
            }
        }

        return Pnovo;
    }  

    
    /////////////////////////////////////////////////////////////
    //OUTROS               //////////////////////////////////////
    /////////////////////////////////////////////////////////////
    
    /**Reliza cruzamento do uniforme, gerando 2 indivíduos e AND gernado mais um
     *@author Tarcísio Pontes
     * @param p1 Pattern[] - indivíduo 1 
     * @param p2 Pattern[] - indivíduo 2 
     * @param tipoAvaliacao int - tipo de função de avaliação
     * @return Pattern[] - novos indivíduos
     */
    public static Pattern[] uniforme2AND(Pattern p1, Pattern p2, String tipoAvaliacao){
        Pattern[] novosPattern = new Pattern[3];
        HashSet<Integer> novoitens1 = new HashSet<>();
        HashSet<Integer> novoitens2 = new HashSet<>();
        HashSet<Integer> novoitens3 = new HashSet<>();
        
        novoitens3.addAll(p1.getItens());
        novoitens3.addAll(p2.getItens());
        
        Iterator iterator = novoitens3.iterator();
        while(iterator.hasNext()){
            if(Const.random.nextBoolean()){
                novoitens1.add((Integer)iterator.next());
            }else{          
                novoitens2.add((Integer)iterator.next());
            }
        }
               
        novosPattern[0] = new Pattern(novoitens1, tipoAvaliacao);
        novosPattern[1] = new Pattern(novoitens2, tipoAvaliacao);
        novosPattern[2] = new Pattern(novoitens3, tipoAvaliacao);
        return novosPattern;           
    }
    
    /**Cruzamento gera um indivíduo a partir do método uniforme
     *@author Tarcísio Pontes
     * @param p1 Pattern - indivíduo 1 
     * @param p2 Pattern - indivíduo 2
     * @param tipoAvaliacao int - tipo de função de avaliação utilizada
     * @return Pattern - novo indivíduo
     */
    public static Pattern uniforme1(Pattern p1, Pattern p2, String tipoAvaliacao){

        HashSet<Integer> itens = new HashSet<>();
        itens.addAll(p1.getItens());
        itens.addAll(p2.getItens());

        HashSet<Integer> novoItens = new HashSet<>();                  

        while(novoItens.size() == 0){
            Iterator iterator = itens.iterator();
            while(iterator.hasNext()){
                if(Const.random.nextBoolean()){
                    novoItens.add((Integer)iterator.next());
                }
            }
        }     

        return new Pattern(novoItens, tipoAvaliacao);
    }
  
    
        
    /**Reliza cruzamento do tipo AND entre indivíduos de duas populações de dimensões P1 e PD garantindo
     * que cada indivíduo terá dimensão D+1
     * Adiciona indivíduos de 1D aleatoriamente caso o cruzamento não gere um indivíduo de dimensão desejada.
    *@author Tarcísio Pontes
     * @param P1 Pattern[] - população dimensão 1 
     * @param PD Pattern[] - população dimensão D
     * @param tipoAvaliacao int - tipo de função de avaliação
     * @param  tamanhoPopulacao int - quantos indivíduos se deseja
     * @return Pattern[] - nova população
     */
    public static Pattern[] ANDP1PD(Pattern[] P1, Pattern[] PD, String tipoAvaliacao, int tamanhoPopulacao){
        int dimensaoExigida = PD[0].getItens().size()+1;
        Pattern[] Pnovo = new Pattern[tamanhoPopulacao];
        int indicePNovo = 0;
        int indiceP1 = 0;
        int indicePD = 0;
        while(indicePNovo < Pnovo.length){
            if(Const.random.nextDouble() > 0.75){//75% de chanses de ser selecionado um dos 25% mais relevantes
                indiceP1 = Const.random.nextInt(P1.length*1/4);
                indicePD = Const.random.nextInt(PD.length*1/4);
            }else{//25% de chanses de ser selecionado totalmente aleatório
                indiceP1 = Const.random.nextInt(P1.length);
                indicePD = Const.random.nextInt(PD.length);
            }
            HashSet<Integer> itensNovo = new HashSet<>();
            itensNovo.addAll(P1[indiceP1].getItens());
            itensNovo.addAll(PD[indicePD].getItens());
            if(itensNovo.size() == dimensaoExigida){
                Pnovo[indicePNovo++] = new Pattern(itensNovo, tipoAvaliacao);
            }
        }
        return Pnovo;
    }

}
