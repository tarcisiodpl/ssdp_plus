/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package evolucionario;

import dp.Avaliador;
import dp.Const;
import dp.D;
import dp.Pattern;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import simulacoes.DPinfo;

/**
 *
 * @author TARCISIO
 */
public class SSDPplus {
    public static Pattern[] run(int k, String tipoAvaliacao, double similaridade, double maxTimeSegundos){
        long t0 = System.currentTimeMillis(); //Initial time
        
        Pattern[] Pk = new Pattern[k];                
        Pattern[] P = null;
        
        //Inicializa Pk com indivíduos vazios
        for(int i = 0; i < Pk.length;i++){
            Pk[i] = new Pattern(new HashSet<Integer>(), tipoAvaliacao);
        }
        
        //System.out.println("Inicializando população...");
        //Inicializa garantindo que P maior que Pk sempre! em bases pequenas isso nem sempre ocorre
        Pattern[] Paux = INICIALIZAR.D1(tipoAvaliacao);//P recebe população inicial
        if(Paux.length < k){
            P = new Pattern[k];            
            for(int i = 0; i < k; i++){
                if(i < Paux.length){
                    P[i] = Paux[i];
                }else{
                    P[i] = Paux[Const.random.nextInt(Paux.length-1)];
                }                
            }                
        }else{
            P = Paux;
        }      
        
        Arrays.sort(P);
        
        
        //System.arraycopy(P, 0, Pk, 0, k); //Inicializa Pk com os melhores indivíduos da população inicial
        SELECAO.salvandoRelevantesDPmais(Pk, P, similaridade);
                     
//        System.out.println("P0");        
//        System.out.println("Qualidade média k/P: " + Avaliador.avaliarMedia(Pk,k) + "/" + Avaliador.avaliarMedia(P,P.length));
//        System.out.println("Dimensão média k/P: " + Avaliador.avaliarMediaDimensoes(Pk,k) + "/" + Avaliador.avaliarMediaDimensoes(P,P.length));        
//        System.out.println("Cobertura +: " + Avaliador.coberturaPositivo(Pk,k));       
//        Avaliador.imprimirDimensaoQuantidade(Pk, k, 15);
//        Avaliador.imprimirDimensaoQuantidade(P, P.length, 15);

        int numeroGeracoesSemMelhoraPk = 0;
        int indiceGeracoes = 1;
        
        //Laço do AG
        Pattern[] Pnovo = null;
        Pattern[] PAsterisco = null;
        
        int tamanhoPopulacao = P.length;
        
        //System.out.println("Buscas...");
        for(int numeroReinicializacoes = 0; numeroReinicializacoes < 3; numeroReinicializacoes++){//Controle número de reinicializações
            //System.out.println("Reinicialização: " + numeroReinicializacoes);
            if(numeroReinicializacoes > 0){
                P = INICIALIZAR.aleatorio1_D_Pk(tipoAvaliacao, tamanhoPopulacao, Pk);
            }
        
            double mutationTax = 0.4; //Mutação inicia em 0.4. Crossover é sempre 1-mutationTax.
            //System.out.println("============================");
            while(numeroGeracoesSemMelhoraPk < 3){

                if(indiceGeracoes == 1){
                    Pnovo = CRUZAMENTO.ANDduasPopulacoes(P, P, tipoAvaliacao);
                    indiceGeracoes++; 
                }else{
                    Pnovo = CRUZAMENTO.uniforme2Pop(P, mutationTax, tipoAvaliacao);                 
                }                   
                PAsterisco = SELECAO.selecionarMelhores(P, Pnovo); 
                P = PAsterisco;   

                int novosK = SELECAO.salvandoRelevantesDPmais(Pk, PAsterisco, similaridade);//Atualizando Pk e coletando número de indivíduos substituídos
                double tempo = (System.currentTimeMillis() - t0)/1000.0; //time
                if(maxTimeSegundos > 0 && tempo > maxTimeSegundos){
                    return Pk;
                }
                //System.out.println("Modificações em Pk: " + novosK);
                //Definição automática de mutação de crossover
                if(novosK > 0 && mutationTax > 0.0){//Aumenta cruzamento se Pk estiver evoluindo e se mutação não não for a menos possível.
                    mutationTax -= 0.2;
                }else if(novosK == 0 && mutationTax < 1.0){//Aumenta mutação caso Pk não tenha evoluido e mutação não seja maior que o limite máximo.
                     mutationTax += 0.2;
                }
                //Critério de parada: 3x sem evoluir Pk com taxa de mutação 1.0
                if(novosK == 0 && mutationTax == 1.0){
                    numeroGeracoesSemMelhoraPk++;

                }else{
                    numeroGeracoesSemMelhoraPk = 0;
                }

                //Impriminto resultados
                
                //Avaliador.imprimirRegrasSimilares(Pk,k);
                //Avaliador.imprimirRegras(Pk,k);

    //            System.out.println("P" + indiceGeracoes);        
                //System.out.println("Qualidade média k/P: " + Avaliador.avaliarMedia(Pk,k) + "/" + Avaliador.avaliarMedia(P,P.length));
                //System.out.println("Dimensão média k/P: " + Avaliador.avaliarMediaDimensoes(Pk,k) + "/" + Avaliador.avaliarMediaDimensoes(P,P.length));        
                //System.out.println("Cobertura +: " + Avaliador.coberturaPositivo(Pk,k));
    //            System.out.println("Novos k: " + novosK);

    //            System.out.println("P" + indiceGeracoes);        
    //            System.out.println(Avaliador.avaliarMedia(P,P.length));
    //            System.out.println(Avaliador.avaliarMediaDimensoes(P,P.length));        
    //            Avaliador.imprimirDimensaoQuantidade(P, P.length, 15);         
    //                        
    //            System.out.println("K" + indiceGeracoes);        
    //            System.out.println(Avaliador.avaliarMedia(Pk,k));
    //            System.out.println(Avaliador.avaliarMediaDimensoes(Pk,k));        
    //            Avaliador.imprimirDimensaoQuantidade(Pk, k, 15);
                //Acompanhamento de taxa de mutação e cruzamento
                //System.out.println("Melhorias:" + novosK  + ",M:" + mutationTax + ",C:" + (1.0-mutationTax));                         

            } 
            
            numeroGeracoesSemMelhoraPk = 0;
        }
        
               
        
        //return Pbest;
        return Pk;
    }
  
    
    
    
    public static void main(String args[]) throws FileNotFoundException{
        //*******************************************
        //Data set                    ***************
        //*******************************************
        String caminho = "C:\\Users\\Tarcisio  Lucas\\Documents\\NetBeansProjects\\SSDPplus\\pastas\\bases\\"; 
        //String nomeBase = "alon-clean50-pn-width-2.CSV";
        //String nomeBase = "ENEM2014_81_NOTA_10k.csv";
        String nomeBase = "matrixBinaria-Global-100-p.csv";
        String caminhoBase = caminho + nomeBase;
       
        //separator database (CSV files)
        D.SEPARADOR = ","; 
        //Seed
        Const.random = new Random(Const.SEEDS[0]); 
        //*******************************************
        //END Data set                    ***************
        //*******************************************
        

        //*******************************************
        //SSDP+ parameters            ***************
        //*******************************************
        //k: number of subgroups
        int k = 5; 
        //Evaluation metric
        String tipoAvaliacao = Avaliador.METRICA_AVALIACAO_WRACC; 
            //String tipoAvaliacao = Avaliador.METRICA_AVALIACAO_QG;
        //ks: cache size
        Pattern.maxSimulares = 2; 
        //min_similarity
        double similaridade = 0.10;
        //Similarity function
        Pattern.medidaSimilaridade = Const.SIMILARIDADE_JACCARD; //similarity function (default JACCARD)
        //Target (atributevalue)
        String target = "p";
        
        //*******************************************
        //END SSDP+ parameters            ***************
        //*******************************************
        
        //It is not about the SSDP+
        Pattern.ITENS_OPERATOR = Const.PATTERN_AND;
        
        //max time simulation in second (-1 for infinity)
        double maxTimeSecond =  -1;      
        
        System.out.println("Loading data set...");
        D.CarregarArquivo(caminhoBase, D.TIPO_CSV); //Loading data set        
        D.GerarDpDn(target);
        //"6,80,104,116,134,145,151,153,156,256"; //target value
        //D.valorAlvo = "I-III";
        //D.valorAlvo = "IV-VII";
        
        
        
        //*******************************************
        //FILTER BY ATTRIBUTE, VALUES AND ITEMS *****
        //*******************************************
        //Filter by attribute
        //String[] filtrarAtributos = {"C009"};
        String[] filtrarAtributos = null;
        //Filter by values
        String[] filtrarValores = null;
        //String[] filtrarValores = {"", "NA"};
        //Filter by items
        String[][] filtrarAtributosValores = null;
        //String[][] filtrarAtributosValores = new String[2][2];
        //filtrarAtributosValores[0][0] = "D001";
        //filtrarAtributosValores[0][1] = "2";
        //filtrarAtributosValores[1][0] = "E01002";
        //filtrarAtributosValores[1][1] = "8";
        //*******************************************
        //EDN FILTER BY ATTRIBUTE, VALUES AND ITEMS *****
        //*******************************************
       
        
        //Executar filtros        
        D.filtrar(filtrarAtributos, filtrarValores, filtrarAtributosValores);
               
        Pattern.numeroIndividuosGerados = 0; //Initializing count of generated individuals
        System.out.println("### Data set:" + D.nomeBase + "(|I|=" + D.numeroItens + 
                "; |A|=" + D.numeroAtributos +
                "; |D|=" + D.numeroExemplos +
                "; |D+|=" + D.numeroExemplosPositivo +
                "; |D-|=" + D.numeroExemplosNegativo +
                 ")"); //database name
        
        
        
        System.out.println("SSDP+ running...");
        //Rodando SSDP
        long t0 = System.currentTimeMillis(); //Initial time
        //Pattern[] p = SSDPplus.run(k, tipoAvaliacao, similaridade);
        Pattern[] p = SSDPplus.run(k, tipoAvaliacao, similaridade, maxTimeSecond);
        double tempo = (System.currentTimeMillis() - t0)/1000.0; //time
        
        System.out.println("\n### Top-k subgroups:");
        Avaliador.imprimirRegras(p, k); 
        
        //Informations about top-k DPs:  
        System.out.println("### Data set:" + D.nomeBase + "(|I|=" + D.numeroItens + 
                "; |A|=" + D.numeroAtributos +
                "; |D+|=" + D.numeroExemplosPositivo +
                "; |D-|=" + D.numeroExemplosNegativo +
                 ")"); //database name
        System.out.println("Average " + tipoAvaliacao + ": " + Avaliador.avaliarMedia(p, k));
        System.out.println("Time(s): " + tempo);
        System.out.println("Average size: " + Avaliador.avaliarMediaDimensoes(p,k));        
        System.out.println("Coverage of all Pk DPs in relation to D+: " + Avaliador.coberturaPositivo(p, k)*100 + "%");
        System.out.println("Description Redundancy Item Dominador (|itemDominador|/k): " + DPinfo.descritionRedundancyDominator(p));
        System.out.println("Number of individuals generated: " + Pattern.numeroIndividuosGerados);
        
        System.out.println("\n### Top-k and caches");
        //Avaliador.imprimirRegrasSimilares(p, k); 
        String[] metricas = {
            Const.METRICA_QUALIDADE,
            Const.METRICA_SIZE,
            //Const.METRICA_WRACC,
            //Const.METRICA_Qg,
            //Const.METRICA_DIFF_SUP,
            //Const.METRICA_LIFT,
            //Const.METRICA_CHI_QUAD,
            //Const.METRICA_P_VALUE,
            //Const.METRICA_SUPP_POSITIVO,
            //Const.METRICA_SUPP_NEGATIVO,
            //Const.METRICA_COV,
            //Const.METRICA_CONF            
        };
        Avaliador.imprimirRegras(p, k, metricas, false, false, true);
              
    }
}
