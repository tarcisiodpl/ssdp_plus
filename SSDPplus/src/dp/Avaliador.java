/*
 * Classe tem como escopo avaliar DPs:
 * Função objetivo
 * 
 */
package dp;

import static java.lang.Math.sqrt;
import java.util.HashSet;
import java.util.Iterator;
import org.apache.commons.math3.stat.inference.ChiSquareTest;

/**
 *
 * @author Tarcísio Lucas
 */
public class Avaliador {
    
    public static final String METRICA_AVALIACAO_QG = "Qg";
    public static final String METRICA_AVALIACAO_SUB = "Sub";
    public static final String METRICA_AVALIACAO_WRACC = "WRAcc";
    public static final String METRICA_AVALIACAO_WRACC_NORMALIZED = "WRAccN";
    public static final String METRICA_AVALIACAO_WRACC_OVER_SIZE = "WRAccOverSize";
    public static final String METRICA_AVALIACAO_CHI_QUAD = "ChiQuadrado";
    public static final String METRICA_AVALIACAO_LIFT = "lift";
    public static final String METRICA_AVALIACAO_DIFF_SUPP = "DiffSup";
    
    
    public static double similaridade(Pattern p1, Pattern p2, String metricaSimilaridade){
        //REF: A Survey of Binary Similarity and Distance Measures (http://www.iiisci.org/Journal/CV$/sci/pdfs/GS315JG.pdf)
        double onlyA = 0.0;
        double onlyB = 0.0;
        double bothAB = 0.0;
        double neitherAB = 0.0;
        double Acount = 0.0;
        double Bcount =0.0;
        
        //POSITIVO
        boolean[] A = p1.getVrP();
        boolean[] B = p2.getVrP();
        for(int i = 0; i < A.length; i++){
            if(A[i]){
                Acount++;
            }
            if(B[i]){
                Bcount++;
            }
            if(A[i] && !B[i]){
                onlyA++;
            }
            if(B[i] && !A[i]){
                onlyB++;
            }
            if(A[i] && B[i]){
                bothAB++;
            }
            if(!A[i] && !B[i]){
                neitherAB++;                        
            }
        }
        
        
        //NEGATIVO
        A = p1.getVrN();
        B = p2.getVrN();
        for(int i = 0; i < A.length; i++){
            if(A[i]){
                Acount++;
            }
            if(B[i]){
                Bcount++;
            }
            if(A[i] && !B[i]){
                onlyA++;
            }
            if(B[i] && !A[i]){
                onlyB++;
            }
            if(A[i] && B[i]){
                bothAB++;
            }
            if(!A[i] && !B[i]){
                neitherAB++;                        
            }
        }
        
        double valor = 0;
        switch(metricaSimilaridade){
            case Const.SIMILARIDADE_JACCARD://
                valor = bothAB/(onlyA + onlyB + bothAB);
                break;            
            case Const.SIMILARIDADE_SOKAL_MICHENER://
                valor = (bothAB + neitherAB) / (onlyA + onlyB + bothAB + neitherAB);
                break;            
        }
        
        return valor;
        
    }
    
    
    /////////////////////////////////////////////////////////////////////////////////////
    // MÉTRICAS DE AVALIAÇÃO ////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Avaliação de uma DP considerando TP e PF
     * @param TP
     * @param FP
     * @param tipo
     * @return 
     */   
    public static double avaliar(int TP, int FP, String tipo){
        double qualidade = 0.0;
        
        switch(tipo){
            case Avaliador.METRICA_AVALIACAO_QG:
                qualidade = Avaliador.Qg(TP, FP);
                break;
            case Avaliador.METRICA_AVALIACAO_SUB:
                qualidade = Avaliador.sub(TP, FP);
                break;
            case Avaliador.METRICA_AVALIACAO_WRACC:
                qualidade = Avaliador.WRAcc(TP, FP);
                break;
            case Avaliador.METRICA_AVALIACAO_WRACC_NORMALIZED:
                qualidade = Avaliador.WRAccNormalized(TP, FP);
                break;
            case Avaliador.METRICA_AVALIACAO_WRACC_OVER_SIZE: //Divide pelo size na classe Pattern
                qualidade = Avaliador.WRAcc(TP, FP);
                break;
            case Avaliador.METRICA_AVALIACAO_CHI_QUAD:
                qualidade = Avaliador.chi_quad(TP, FP);
                break;
            case Avaliador.METRICA_AVALIACAO_LIFT:
                qualidade = Avaliador.lift(TP, FP);
                break;
            case Avaliador.METRICA_AVALIACAO_DIFF_SUPP:
                qualidade = Avaliador.DiffSup(TP, FP);
                break;
        }      
        return qualidade;
    }   
    
    
    
    
    public static double WRAcc(int TP, int FP){
        if(TP==0 && FP==0){
            return 0.0;
        }
        double sup = (double)(TP+FP) / (double)D.numeroExemplos;
        double conf = (double)TP / (double)(TP+FP);
        double confD = (double)D.numeroExemplosPositivo / (double)D.numeroExemplos;
        double wracc = sup * ( conf  - confD);
                       
        return wracc;
    }
    
    public static double WRAccNormalized(int TP, int FP){
        if(TP==0 && FP==0){
            return 0.0;
        }
        double sup = (double)(TP+FP) / (double)D.numeroExemplos;
        double conf = (double)TP / (double)(TP+FP);
        double confD = (double)D.numeroExemplosPositivo / (double)D.numeroExemplos;
        double wracc = sup * ( conf  - confD);
                       
        return 4 * wracc;
    }
    
    public static double Qg(int TP, int FP){
        double qg = (double)TP/(double)(FP+1);
        return qg;
    }
    
    public static double sub(int TP, int FP){
        double sub = TP-FP;
        return sub;
    }
    
    public static double chi_quad(int TP, int FP){
            /*
        	D1	D2	Sum
        p	n11	n12	n1
        p_	n21	n22	n2
        Sum	|Dp|	|Dn|	|D|
        
        n11 = TP (positivos presentes na DP)
        n21 = |Dp| - TP (positivos não presentes na DP)
        n12 = FP (negativos presentes na DP)        
        n11 = |Dn| - FP (negativos ausêntes na DP)
        
        REF: Discriminative pattern mining and its applications in bioinformatics      
        OBS: se chi == NaN método retorna zero.
        */      
        //Só é preciso isso para calcular via função pronta!
        long[][] n = new long[2][2];
        n[0][0] = TP;
        n[1][0] = D.numeroExemplosPositivo - n[0][0];
        n[0][1] = FP;
        n[1][1] = D.numeroExemplosNegativo - n[0][1];
        

        ChiSquareTest chiTest = new ChiSquareTest();
        double chi_quad = chiTest.chiSquare(n);
        //System.out.println("Chi_quad: " + chi + "/" + chi_quad);
        //System.out.println("chi_quad: " + chi_quad);
        if(Double.isNaN(chi_quad)){
            return 0;
        }       
        return chi_quad;
    }
    
    public static double p_value(int TP, int FP){
         /*
        	D1	D2	Sum
        p	n11	n12	n1
        p_	n21	n22	n2
        Sum	|Dp|	|Dn|	|D|
        
        n11 = TP (positivos presentes na DP)
        n21 = |Dp| - TP (positivos não presentes na DP)
        n12 = FP (negativos presentes na DP)        
        n11 = |Dn| - FP (negativos ausêntes na DP)
        
        REF: Discriminative pattern mining and its applications in bioinformatics      
        
        */      
        
        //Só é preciso isso para calcular via função pronta!
        long[][] n = new long[2][2];
        n[0][0] = TP;
        n[1][0] = D.numeroExemplosPositivo - n[0][0];
        n[0][1] = FP;
        n[1][1] = D.numeroExemplosNegativo - n[0][1];
        ChiSquareTest chiTest = new ChiSquareTest();
        //Returns the observed significance level, or p-value, associated with a chi-square test of independence based on the input counts array, viewed as a two-way table.
        double p_value = chiTest.chiSquareTest(n);
        
        if(Double.isNaN(p_value)){
            return 1.0;
        }
        //System.out.println("pvalue: " + p_value);
        return p_value;        
    }
    
    public static double lift(int TP, int FP){
        //Ref: https://en.wikipedia.org/wiki/Lift_(data_mining)
        double supCond = (double)(TP + FP) / (double)D.numeroExemplos; //Suporte antecedente: número de exemplos da regra sobre o total |D|
        double supTarget = (double)D.numeroExemplosPositivo / (double)D.numeroExemplos;  //Suporte consequente: número de exemplos com rótulo em relação ao total |Dp| / |D|
        double supDP = (double)TP/(double)D.numeroExemplos; //Suporte antecedente e consequente: count()
        
        if(supCond == 0 || supTarget == 0){
            return 0;
        }
        double valor = supDP / (supCond * supTarget);
        
        return valor;
    }   
           
    public static double DiffSup(int TP, int FP){        
        double suppP = (double) TP / (double) D.numeroExemplosPositivo;
        double suppN = (double) FP / (double) D.numeroExemplosNegativo;
        
        double valor = Math.abs(suppP - suppN);
        
        return valor;
    }
    
    public static double suppPositivo(int TP){
        double valor = (double)TP / (double) D.numeroExemplosPositivo;
        return valor;
    }
    
    public static double suppNegativo(int FP){
        double valor = (double) FP / (double) D.numeroExemplosNegativo;
        return valor;
    }
       
    public static double cov(int TP, int FP){
        //Ref: survey 2011
        double valor = (double)(TP + FP) / (double) D.numeroExemplos;
        
        return valor;
    }
    
    public static double conf(int TP, int FP){
        double valor = (double)TP / (double)(TP+FP);
        return valor;
    }   
        
    public static int TP(boolean[] vrP){
        int TP = 0;
        for(int i = 0; i < vrP.length; i++){            
            if(vrP[i]){
                TP++;
            }                       
        }        
        return TP;
    }
    
    public static int FP(boolean[] vrN){
        int FP = 0;
        for(int i = 0; i < vrN.length; i++){            
            if(vrN[i]){
                FP++;
            }                       
        }        
        return FP;
    }
    
    /**
     * Qualidade média de um conjunto de DPs
     * @param p
     * @param k
     * @return 
     */
    public static double avaliarMedia(Pattern[] p, int k){
        double total = 0.0;
        int i = 0;
        for(; i < k; i++){
            total += p[i].getQualidade();
        }
        return total/(double)i;
    }
    
    /**
     * Dimensão média de um conjunto de DPs
     * @param p
     * @param k
     * @return 
     */
    public static double avaliarMediaDimensoes(Pattern[] p, int k){
        int total = 0;
        int i = 0;
        for(; i < k; i++){
            total += p[i].getItens().size();
        }
        return (double)total/(double)i;
    }
    
    /**
     * Percentual de exemplos positivos cobertos por um conjunto de DPs
     * @param p
     * @param k
     * @return 
     */
    public static double coberturaPositivo(Pattern[] p, int k){
        double coberturaP = 0.0;
        boolean[] vrpGrupo = new boolean[D.numeroExemplosPositivo];
        
        for(int i = 0; i < k; i++){
            boolean[] vrpItem = p[i].getVrP();
            for(int j = 0; j < vrpItem.length; j++){
                if(vrpItem[j]){
                    vrpGrupo[j] = true;
                }
            }
        }
        
        for(int i = 0; i < vrpGrupo.length; i++){
            if(vrpGrupo[i]){
                coberturaP = coberturaP + 1;
            }
        }
        //System.out.println("Numero P: " + coberturaP);
        coberturaP = coberturaP/(double)vrpGrupo.length;
        return coberturaP;
    }
    
    
    /////////////////////////////////////////////////////////////////////////////////////
    // EXEMPLOS COBERTOS - VRP e VRN/////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////
        

    /**
     * Recebe um conjunto de itens e retorna os exemplos positivos cobertos por ele considerando operador AND
     * @param itens
     * @return vetorResultantePositivo - boolean[]
     */
    public static boolean[] vetorResultantePositivoAND(HashSet<Integer> itens){
        boolean[] vetorResultantePositivo = new boolean[D.numeroExemplosPositivo];
        
        for(int i = 0; i < D.numeroExemplosPositivo; i++){            
            vetorResultantePositivo[i] = Avaliador.patternContemplaExemploAND(itens, D.Dp[i]);            
        }      
        
        return vetorResultantePositivo;
    }
    
    /**
     * Recebe um conjunto de itens e retorna os exemplos negativos cobertos por ele
     * @param itens
     * @return 
     */
    public static boolean[] vetorResultanteNegativoAND(HashSet<Integer> itens){
        boolean[] vetorResultanteNegativo = new boolean[D.numeroExemplosNegativo];
        
        for(int i = 0; i < D.numeroExemplosNegativo; i++){            
            vetorResultanteNegativo[i] = Avaliador.patternContemplaExemploAND(itens, D.Dn[i]);                        
        }     
        
        return vetorResultanteNegativo;
    }
    
    /**
     * Recebe um exemplos de Dp ou Dn e  verifica se o exemplo contempla o conjunto de itens passado itens
     * considerando operador AND
     * @param itens
     * @param exemplo
     * @return 
     */
    private static boolean patternContemplaExemploAND(HashSet<Integer> itens, int[] exemplo){
        Iterator iterator = itens.iterator();
        while(iterator.hasNext()){
            int item = (int)iterator.next();
            int itemAtributo = D.itemAtributo[item];
            int itemValor = D.itemValor[item];
            if(exemplo[itemAtributo] != itemValor){
                return false;                    
            } 
        }       
        return true; 
    }
    
    
    /**
     * Recebe um conjunto de itens e retorna os exemplos positivos cobertos por ele considerando operador OR
     * @param itens
     * @return vetorResultantePositivo - boolean[]
     */
    public static boolean[] vetorResultantePositivoOR(HashSet<Integer> itens){
        boolean[] vetorResultantePositivo = new boolean[D.numeroExemplosPositivo];
        
        for(int i = 0; i < D.numeroExemplosPositivo; i++){            
            vetorResultantePositivo[i] = Avaliador.patternContemplaExemploOR(itens, D.Dp[i]);                        
        }      
        
        return vetorResultantePositivo;
    }
    
    /**
     * Recebe um conjunto de itens e retorna os exemplos negativos cobertos por ele considerando operador OR
     * @param itens
     * @return vetorResultantePositivo - boolean[]
     */
    public static boolean[] vetorResultanteNegativoOR(HashSet<Integer> itens){
        boolean[] vetorResultanteNegativo = new boolean[D.numeroExemplosNegativo];
        
        for(int i = 0; i < D.numeroExemplosNegativo; i++){            
            vetorResultanteNegativo[i] = Avaliador.patternContemplaExemploOR(itens, D.Dn[i]);            
        }      
        
        return vetorResultanteNegativo;
    }
    
    /**
     * Recebe um exemplos de Dp ou Dn e  verifica se o exemplo contempla o conjunto de itens passado itens consideranto operador OR
     * @param itens
     * @param exemplo
     * @return 
     */
    private static boolean patternContemplaExemploOR(HashSet<Integer> itens, int[] exemplo){
        Iterator iterator = itens.iterator();
        while(iterator.hasNext()){
            int item = (int)iterator.next();
            int itemAtributo = D.itemAtributo[item];
            int itemValor = D.itemValor[item];
            if(exemplo[itemAtributo] == itemValor){
                return true;                    
            } 
        }       
        return false; 
    }
    
    
    /////////////////////////////////////////////////////////////////////////////////////
    // IMPRIMIR CONJUNTO DE DPs /////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////
        
    public static void imprimir(Pattern[] p, int kPrimeiros){
        for(int i = 0; i < kPrimeiros; i++){
            System.out.println(p[i].toString());        
        }
    }
    
      
    //Imprime regras em texto
    public static void imprimirRegras(Pattern[] p, int kPrimeiros){
        Pattern vazio = new Pattern(new HashSet<Integer>(), p[0].getTipoAvaliacao());
        System.out.println(vazio.toString2());
        for(int i = 0; i < kPrimeiros; i++){
            System.out.println(p[i].toString2());        
        }        
    }
    
    //Imprime regras em texto
    public static void imprimirRegras(Pattern[] p, int kPrimeiros, String[] metricas, boolean imprimirCoberturaDp, boolean imprimirCoberturaDn, boolean imprimirSimilares){
        Pattern vazio = new Pattern(new HashSet<Integer>(), p[0].getTipoAvaliacao());
        System.out.println(vazio.toString2());
        for(int i = 0; i < kPrimeiros; i++){
            System.out.println(p[i].toString(metricas, imprimirCoberturaDp, imprimirCoberturaDn, imprimirSimilares));        
        }        
    }
    
    
    public static void imprimirDimensaoQuantidade(Pattern[] p, int kPrimeiros, int dDimensoes){
        int[] dimensaoQuantidade = new int[dDimensoes]; //Até dimensão 10
        for(int i = 0; i < kPrimeiros; i++){
            int dimensao = p[i].getItens().size();
            dimensaoQuantidade[dimensao]++;
        }
               
        for(int i = 0; i < dDimensoes; i++){
            System.out.println(/*"D" + i + ":" +*/ dimensaoQuantidade[i]/* + ", "*/);
        }
        
        System.out.println();
    }

    

    
}
