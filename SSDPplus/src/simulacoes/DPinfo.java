/*
 * 
 */
package simulacoes;

import dp.Const;
import dp.D;
import dp.Pattern;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import org.apache.commons.math3.stat.inference.ChiSquareTest;
//import org.apache.commons.math3.stat.inference;

/**
 *
 * @author Tarcísio Lucas
 */
public class DPinfo {
    
    public static double WRAcc(Pattern p, Base b){
        double TP = p.getTP();
        double FP = p.getFP();
        double numeroExemplosPositivo = b.getNumeroExemplosPositivo();
        double numeroExemplos = b.getNumeroExemplos();
        
        if(TP == 0.0 && FP == 0.0){
            return 0.0;
        }
        
        double sup = (TP+FP) / numeroExemplos;
        double conf = TP / (TP+FP);
        double confD = numeroExemplosPositivo / numeroExemplos;
        double wracc = sup * ( conf  - confD);
         
        return wracc;
    }
    
    public static double WRAccNormalized(Pattern p, Base b){
        double TP = p.getTP();
        double FP = p.getFP();
        double numeroExemplosPositivo = b.getNumeroExemplosPositivo();
        double numeroExemplos = b.getNumeroExemplos();
        
        if(TP == 0.0 && FP == 0.0){
            return 0.0;
        }
        
        double sup = (TP+FP) / numeroExemplos;
        double conf = TP / (TP+FP);
        double confD = numeroExemplosPositivo / numeroExemplos;
        double wracc = sup * ( conf  - confD);
         
        return 4 * wracc;
    }
    
    public static double Qg(Pattern p){
        double TP = p.getTP();
        double FP = p.getFP();
        double valor = TP/(FP + 1.0);
        return valor;
    }
    
    public static double DiffSup(Pattern p, Base b){        
        double TP = p.getTP();
        double FP = p.getFP();
        double numeroExemplosPositivo = b.getNumeroExemplosPositivo();
        double numeroExemplosNegativo = b.getNumeroExemplosNegativo();
        
        double suppP = TP / numeroExemplosPositivo;
        double suppN = FP / numeroExemplosNegativo;
        
        double valor = Math.abs(suppP - suppN);
        
        return valor;
    }
    
    public static double GrowthRate(Pattern p, Base b){        
        double TP = p.getTP();
        double FP = p.getFP();
        double numeroExemplosPositivo = b.getNumeroExemplosPositivo();
        double numeroExemplosNegativo = b.getNumeroExemplosNegativo();
        
        double suppP = TP / numeroExemplosPositivo;
        double suppN = FP / numeroExemplosNegativo;
        
        double valor;
        if(suppP == 0.0){
            valor = 0.0;
        }else if(suppN == 0.0){
            valor = Double.POSITIVE_INFINITY;
        }else{
            valor = suppP/suppN;
        }
        return valor;
    }
    
    public static double OddsRatio(Pattern p, Base b){        
        double TP = p.getTP();
        double FP = p.getFP();
        double numeroExemplosPositivo = b.getNumeroExemplosPositivo();
        double numeroExemplosNegativo = b.getNumeroExemplosNegativo();
        
        double suppP = TP / numeroExemplosPositivo;
        double suppN = FP / numeroExemplosNegativo;
        
        double valor = ( suppP /(1-suppP) ) / ( suppN / (1-suppN) );
        return valor;
    }
    
    public static double lift(Pattern p, Base b){
        double TP = p.getTP();
        double FP = p.getFP();
        double supCond = (double)(TP + FP) / (double)b.getNumeroExemplos(); //Suporte antecedente: número de exemplos da regra sobre o total |D|
        double supTarget = (double)b.getNumeroExemplosPositivo() / (double)b.getNumeroExemplos();  //Suporte consequente: número de exemplos com rótulo em relação ao total |Dp| / |D|
        double supDP = DPinfo.supp(p, b); //Suporte antecedente e consequente: count()
        
        double valor = supDP / (supCond * supTarget);
        
        return valor;
    }
    
    public static double p_value(Pattern p, Base b){
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
        n[0][0] = p.getTP();
        n[1][0] = b.getNumeroExemplosPositivo() - n[0][0];
        n[0][1] = p.getFP();
        n[1][1] = b.getNumeroExemplosNegativo() - n[0][1];
        ChiSquareTest chiTest = new ChiSquareTest();
        //Returns the observed significance level, or p-value, associated with a chi-square test of independence based on the input counts array, viewed as a two-way table.
        double p_value = chiTest.chiSquareTest(n);
        //System.out.println("pvalue: " + p_value);
        return p_value; 
    }
        
    public static double chi_quad(Pattern p, Base b){
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
        n[0][0] = p.getTP();
        n[1][0] = b.getNumeroExemplosPositivo() - n[0][0];
        n[0][1] = p.getFP();
        n[1][1] = b.getNumeroExemplosNegativo() - n[0][1];
        
//        //Cálculo no braço deu certo, está batendo com a API. Vou manter aqui para não perder o entendimento do processo, mas vou aplicar
//        //a API por questão de credibilidade.
//        //Calculando valores esperados!
//        double[][] nEsperado = new double[2][2];
//        for(int i = 0; i < 2; i++){            
//            for(int j = 0; j < 2; j++){
//                double nIQsum = 0.0;
//                double nQJsum = 0.0;
//                for(int q = 0; q < 2; q++){
//                    nIQsum += n[i][q];
//                    nQJsum += n[q][j];
//                }
//                nEsperado[i][j] = (nIQsum * nQJsum) / (double)b.getNumeroExemplos();                
//            }
//        }
//        
//        //Calculando chi-quadrado!
//        double chi = 0.0;
//        for(int i = 0; i < 2; i++){            
//            for(int j = 0; j < 2; j++){
//               chi += Math.pow(n[i][j] - nEsperado[i][j], 2) / nEsperado[i][j];
//            }
//        }        
//               
//         for(int i = 0; i < 2; i++){            
//            for(int j = 0; j < 2; j++){
//                System.out.println("Real:" + n[i][j] + " Esp:" + nEsperado[i][j]);                
//            }
//         }
          
        
        ChiSquareTest chiTest = new ChiSquareTest();
        double chi_quad = chiTest.chiSquare(n);
        //System.out.println("Chi_quad: " + chi + "/" + chi_quad);
        //System.out.println("chi_quad: " + chi_quad);
        
        return chi_quad;
    } 
    
    public static double supp(Pattern p, Base b){
        double TP = p.getTP();
        double numeroExemplos = b.getNumeroExemplos();
        
        double valor = TP / numeroExemplos;
        return valor;
    }
    
    public static double suppPositivo(Pattern p, Base b){
        double TP = p.getTP();
        double numeroExemplosPositivo = b.getNumeroExemplosPositivo();
        
        double valor = TP / numeroExemplosPositivo;
        return valor;
    }
    
    public static double suppNegativo(Pattern p, Base b){
        double FP = p.getFP();
        double numeroExemplosNegativo = b.getNumeroExemplosNegativo();
        
        double valor = FP / numeroExemplosNegativo;
        return valor;
    }
       
    public static double cov(Pattern p, Base b){
        double TP = p.getTP();
        double FP = p.getFP();
        double numeroExemplos = b.getNumeroExemplos();
          
        double valor = (TP + FP) / numeroExemplos;
        
        return valor;
    }
    
    public static double conf(Pattern p){
        double TP = p.getTP();
        double FP = p.getFP();
        
        double valor = TP / (TP+FP);
        return valor;
    }   
    
    public static double size(Pattern p){
        double valor = p.getItens().size();
        return valor;
    }

    public static double TP(Pattern p){
        return p.getTP();
    }
    
    public static double FP(Pattern p){
        return p.getFP();
    }
    
    public static double overallSuppPositive(Pattern[] P, Base b){
        
        double TPgrupo = 0;
        boolean[] vrpGrupo = new boolean[b.getNumeroExemplosPositivo()];
        
        for(int i = 0; i < P.length; i++){
            boolean[] vrpItem = P[i].getVrP();
            for(int j = 0; j < vrpItem.length; j++){
                if(vrpItem[j]){
                    vrpGrupo[j] = true;
                }
            }
        }
        
        for(int i = 0; i < vrpGrupo.length; i++){
            if(vrpGrupo[i]){
                TPgrupo++;
            }
        }
        
        double overallSuppPositive = (double)TPgrupo/(double)vrpGrupo.length;
        return overallSuppPositive;    
    } 
    
    
    public static double coverRedundancy(Pattern[] P, Base b){
        /*
        FONTE: Artigo Diverse subgroup set discovery, 2012 (DSSD)
        #Cover redundancy (CR)
        dataset S
        set of subgroups G
        cover count: c(t,G) 
                number of times t is covered by a subgroup in a subgroup set.

        expected cover count: c'(t,G)
                número de vezes que cada exemplo dividido pelo o total de exemplos.

        cover redundancy: CR(S,G) = { somaTodos[(c-c')/c'] } / |S|       
        */
        
        double numeroExemplosPositivos = 0.0;
        if(b == null){
            numeroExemplosPositivos = D.numeroExemplosPositivo;
        }else{
            numeroExemplosPositivos = b.getNumeroExemplosPositivo();
        }
        
        double[] frequenciaD = new double[(int)numeroExemplosPositivos];
        double frequenciaEsperadaD = 0.0;
               
        //Preechendo frequenciaD
        for(int i = 0; i < P.length; i++){
            boolean[] vrp = P[i].getVrP();
            for(int j = 0; j < vrp.length; j++){
                if(vrp[j]){
                    frequenciaD[j]++;
                    frequenciaEsperadaD++;
                }
            }
        }
        
        //Calculando frequência esperada
        frequenciaEsperadaD = frequenciaEsperadaD / numeroExemplosPositivos;
        
        //Calculando Cover Redundancy
        double coverRedundancy = 0.0;
        for(int i = 0; i < frequenciaD.length; i++){
            coverRedundancy += Math.abs(frequenciaD[i] - frequenciaEsperadaD) / frequenciaEsperadaD;
        }
        coverRedundancy = coverRedundancy / numeroExemplosPositivos;
               
        return coverRedundancy;    
    } 
    
    
    public static double descritionRedundancyDensity(Pattern[] P){
        /*
        Eu criei, não sei existe      
        DR1 = número itens únicos/número de itens
        */
        double descritionRedundancy1 = 0.0;
        
        double numeroItens = 0.0;
        HashSet<Integer> itensUnicos = new HashSet<>();
        
        for(int i = 0; i < P.length; i++){
            numeroItens += P[i].getItens().size();
            itensUnicos.addAll(P[i].getItens());
        }
        
        descritionRedundancy1 = itensUnicos.size() / numeroItens;
        
        return descritionRedundancy1;    
    }
    
    public static double descritionRedundancyDominator(Pattern[] P){
        /*
        Eu criei, não sei existe      
        DR2 = frequência que o item mais utilizado se repeta/k
        */
        
        //Capturando itens únicos nas top-k DPs
        HashSet<Integer> itensUnicosHS = new HashSet<>();
        for(int i = 0; i < P.length; i++){
            itensUnicosHS.addAll(P[i].getItens());
        }
        int[] itensUnicos = new int[itensUnicosHS.size()];
        
        //Salvando itens únicos num array
        Iterator iterator = itensUnicosHS.iterator();
        int indice = 0;
        while(iterator.hasNext()){
            itensUnicos[indice++] = (int) iterator.next();
        }
        
        //Contando quantas vezes cada item aparece na descrição das top-k DPs
        int[] frequenciaItensUnicos = new int[itensUnicos.length];
        for(int i = 0; i < itensUnicos.length; i++){
            for(int j = 0; j < P.length; j++){
                //se DP contém item único
                if(P[j].getItens().contains((Integer)itensUnicos[i])){
                    frequenciaItensUnicos[i]++;
                }
            }
        }
                
        //Identificando frequência do item mais frequênte
        int maxFrequency = 0;
        for(int i = 0; i < frequenciaItensUnicos.length; i++){
            if(maxFrequency < frequenciaItensUnicos[i]){
                maxFrequency = frequenciaItensUnicos[i];
            }
        }
        
        double descritionRedundancy2 = (double)maxFrequency/(double)P.length;
        
        return descritionRedundancy2;    
    }
    
    public static double metricaMedia(Pattern[] P, Base b, String tipoMetrica){
        if(P == null){
            return Double.NaN;
        }
        
        int i = 0;
        double total = 0.0;
        
        switch(tipoMetrica){
            case Const.METRICA_CONF:
                for(; i < P.length; i++){
                    total += DPinfo.conf(P[i]);
                }
                break;
            case Const.METRICA_COV:
                for(; i < P.length; i++){
                    total += DPinfo.cov(P[i], b);
                }
                break;
            case Const.METRICA_DIFF_SUP:
                for(; i < P.length; i++){
                    total += DPinfo.DiffSup(P[i], b);
                }
                break;
            case Const.METRICA_FP:
                for(; i < P.length; i++){
                    total += DPinfo.FP(P[i]);
                }
                break;
            case Const.METRICA_GROWTH_RATE:
                for(; i < P.length; i++){
                    total += DPinfo.GrowthRate(P[i], b);
                }
                break;
            case Const.METRICA_ODDS_RATIO:
                for(; i < P.length; i++){
                    total += DPinfo.OddsRatio(P[i], b);
                }
                break;
            case Const.METRICA_Qg:
                for(; i < P.length; i++){
                    total += DPinfo.Qg(P[i]);
                }
                break;
            case Const.METRICA_SIZE:
                for(; i < P.length; i++){
                    total += DPinfo.size(P[i]);
                }
                break;
            case Const.METRICA_SUPP:
                for(; i < P.length; i++){
                    total += DPinfo.supp(P[i], b);
                }
                break;
            case Const.METRICA_TP:
                for(; i < P.length; i++){
                    total += DPinfo.TP(P[i]);
                }
                break;
            case Const.METRICA_WRACC:
                for(; i < P.length; i++){
                    total += DPinfo.WRAcc(P[i], b);
                }
                break;
            case Const.METRICA_WRACC_NORMALIZED:
                for(; i < P.length; i++){
                    total += DPinfo.WRAccNormalized(P[i], b);
                }
                break;
            case Const.METRICA_SUPP_NEGATIVO:
                for(; i < P.length; i++){
                    total += DPinfo.suppNegativo(P[i], b);
                }
                break;
            case Const.METRICA_SUPP_POSITIVO:
                for(; i < P.length; i++){
                    total += DPinfo.suppPositivo(P[i], b);
                }
                break;
            case Const.METRICA_OVERALL_SUPP_POSITIVO:
                //Nesse caso o cálculo é diferente. Eu quero a união dos exemplos cobertos pelo conjunto de regras.
                return DPinfo.overallSuppPositive(P, b);
            case Const.METRICA_COVER_REDUNDANCY_POSITIVO:
                //Nesse caso o cálculo é diferente. Eu quero a união dos exemplos cobertos pelo conjunto de regras.
                return DPinfo.coverRedundancy(P, b);
            case Const.METRICA_DESCRIPTION_REDUNDANCY_DENSITY:
                //Nesse caso o cálculo é diferente. Eu quero a união dos exemplos cobertos pelo conjunto de regras.
                return DPinfo.descritionRedundancyDensity(P);
            case Const.METRICA_DESCRIPTION_REDUNDANCY_DOMINATOR:
                //Nesse caso o cálculo é diferente. Eu quero a união dos exemplos cobertos pelo conjunto de regras.
                return DPinfo.descritionRedundancyDominator(P);
            case Const.METRICA_K:
                //Nesse caso o cálculo é diferente. Eu quero a união dos exemplos cobertos pelo conjunto de regras.
                return P.length;            
            case Const.METRICA_CHI_QUAD:
                for(; i < P.length; i++){
                    total += DPinfo.chi_quad(P[i], b);
                }
                break;
            case Const.METRICA_P_VALUE:
                for(; i < P.length; i++){
                    total += DPinfo.p_value(P[i], b);
                }
                break;
            case Const.METRICA_LIFT:
                for(; i < P.length; i++){
                    total += DPinfo.lift(P[i], b);
                }
                break;
        }    
        
        double media = total/(double)i;
        
        return media;
    }
    
    public static double metricaMedia(Resultado[] R, Base b, String tipoMetrica){
        int i = 0;
        double total = 0.0;
                
        if(tipoMetrica.equals(Const.METRICA_TIME)){
            for(; i < R.length; i++){
                total += R[i].getTempoExecucao();
            }
        }else if(tipoMetrica.equals(Const.METRICA_NUMERO_TESTES)){
            for(; i < R.length; i++){
                total += R[i].getNumeroTestes();
            }
        }else{
            for(; i < R.length; i++){
                total += DPinfo.metricaMedia(R[i].getDPs(), b, tipoMetrica);
            }
        }
                       
        double media = total/(double)i;
        
        return media;
    }
    
//    public static double metricaMedia(Simulacao[] simulacoes, String tipoMetrica, BasesArrayList bases){
//        int i = 0;
//        double total = 0.0;
//        for(; i < simulacoes.length; i++){
//            Resultado[] resultados = simulacoes[i].getResultados();
//            Base base = bases.getBase( simulacoes[i].getNomeBase() );
//            total += DPinfo.metricaMedia(resultados, base, tipoMetrica);
//        }        
//                        
//        double media = total/(double)i;
//        
//        return media;
//    }

    //Retorna o número de vezes que cada Pattern de Pdistintos aparece em P.
    public static int[] frequenciaPatterns(ArrayList<Pattern> P, Pattern[] Pdistintos){
        int[] frequencia = new int[Pdistintos.length];
        
        for(int i = 0; i < Pdistintos.length; i++){
            for(int j = 0; j < P.size(); j++){
                if(DPinfo.isEqual(Pdistintos[i], P.get(j))){
                    frequencia[i]++;
                }
            }
        }
        
        return frequencia;
    }

    public static Pattern[] patternDistintos(ArrayList<Pattern> P){
        ArrayList<Pattern> pDistintosAux = new ArrayList<>();
        pDistintosAux.add(P.get(0));
        for(int i = 1; i < P.size(); i++){
            if(DPinfo.isDistinto(pDistintosAux, P.get(i))){
                pDistintosAux.add(P.get(i));
            }
        }
        
        
        Pattern[] pDistintosArray = new Pattern[pDistintosAux.size()];
        for(int i = 0; i < pDistintosArray.length; i++){
            pDistintosArray[i] = pDistintosAux.get(i);
        }        
        
        return pDistintosArray;        
    }
    
    private static boolean isEqual(Pattern p1, Pattern p2){
        HashSet<Integer> itens1 = p1.getItens();
        HashSet<Integer> itens2 = p2.getItens();        
        return ( itens1.containsAll(itens2) && itens2.containsAll(itens1) );                
    }
    
    private static boolean isDistinto(ArrayList<Pattern> P, Pattern p){
        for(int i = 0; i < P.size(); i++){
            if(DPinfo.isEqual(p, P.get(i))){
                return false;
            }        
        }
        return true;
    }
 
    private static int getPosicaoRanking(int itemOriginal, int[] rankingDP1){
        for(int i = 0; i < rankingDP1.length; i++){
            if(rankingDP1[i] == itemOriginal){
                return i;
            }
        }
        return -1;
    }
    
//    public static String toString(Simulacao s){
//        String str = "B = " + s.getNomeBase() + ", Ag = " + s.getAlgoritmo() + "\n";        
//        
//        //Adicionando todas as DPs da simulação: número de repetições * número de k
//        ArrayList<Pattern> P_all = new ArrayList<Pattern>();
//        for(int i = 0; i < s.getResultados().length; i++){
//            Pattern[] p_Resultado = s.getResultados()[i].getDPs();
//            for(int j = 0; j < p_Resultado.length; j++){
//                P_all.add(p_Resultado[j]);
//            }
//        }
//        
//        Pattern[] p_dist = DPinfo.patternDistintos(P_all);
//        int[] frequencia = DPinfo.frequenciaPatterns(P_all, p_dist);
//        
//        for(int i = 0; i < p_dist.length; i++){
//            str += i + "_";
//            Pattern p = p_dist[i];
//            HashSet<Integer> itens = p.getItens();
//            if(itens.size() == 0){
//                str += "{}";
//            }
//            else{
//                Iterator iterator = itens.iterator();
//                str += "{";
//                while(iterator.hasNext()){
//                    int item = (int)iterator.next();
//                    //str += item + "(" + DPinfo.getPosicaoRanking(item, s.getRankingDP1()) + "),";
//                    str += DPinfo.getPosicaoRanking(item, s.getRankingDP1()) + ",";
//                }                
//                str += "}";
//            }    
//            str += "(" + frequencia[i] + ") -> TP=" + p.getTP() + "/FP=" + p.getFP() + "\n";
//        }
//        
//        return str;
//    }
    
    public static void imprimirDPsimulacoes(ArrayList<Simulacao> simulacoes){
        for(int i = 0; i < simulacoes.size(); i++){
            //System.out.println( DPinfo.toString(simulacoes.get(i)) );
        }
    }
    
    public static void imprimirItens(Pattern p){
        HashSet<Integer> itens = p.getItens();
        Iterator iterator = itens.iterator();
        System.out.print("(");
        while(iterator.hasNext()){
            System.out.print(iterator.next()+",");
        }
        System.out.print(")");
    }
}
