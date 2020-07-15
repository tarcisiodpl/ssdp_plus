/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dp;

import simulacoes.DPinfo;
import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

/**
 *
 * @author Tarcísio Pontes
 * @since 27/01/2016
 * @version 1.0
 * OK revisão!
 */
public class Pattern implements Comparable<Pattern>, Serializable{
    private HashSet<Integer> itens;
    
    private String tipoAvaliacao;
    private boolean[] vrP;
    private boolean[] vrN;
    private int TP;
    private int FP;
    private double qualidade;
    
    //Atributos para dar mais opções ao cliente
    //private ArrayList<Pattern> sinonimos;
    //private ArrayList<Pattern> subPatterns;
    private Pattern[] similares;
        
    //Atributos estáticos
    public static int numeroIndividuosGerados = 0;
    public static int ITENS_OPERATOR = Const.PATTERN_AND;
       
    public static int maxSimulares = 5;
    public static String medidaSimilaridade = Const.SIMILARIDADE_JACCARD;
    
    
    public Pattern(HashSet<Integer> itens, String tipoAvaliacao){
        this.itens = itens;
        this.tipoAvaliacao = tipoAvaliacao;            
        if(Pattern.ITENS_OPERATOR == Const.PATTERN_AND){
            this.vrP = Avaliador.vetorResultantePositivoAND(itens); //Saber se somina ou é dominado. Isso ajuda!
            this.vrN = Avaliador.vetorResultanteNegativoAND(itens); //Saber se somina ou é dominado. Isso ajuda!
        }else if(Pattern.ITENS_OPERATOR == Const.PATTERN_OR){
            this.vrP = Avaliador.vetorResultantePositivoOR(itens); //Saber se somina ou é dominado. Isso ajuda!
            this.vrN = Avaliador.vetorResultanteNegativoOR(itens); //Saber se somina ou é dominado. Isso ajuda!        
        }   
        this.TP = Avaliador.TP(this.vrP);
        this.FP = Avaliador.FP(this.vrN);
        this.qualidade = Avaliador.avaliar(this.TP, this.FP, this.tipoAvaliacao);
        
        if(tipoAvaliacao.equals(Avaliador.METRICA_AVALIACAO_WRACC_OVER_SIZE)){
            if(itens.size() == 0){
                this.qualidade = 0;
            }else{
                this.qualidade = this.qualidade/(double)itens.size();
            }            
        }
        //this.sinonimos = new ArrayList<>();
        //this.subPatterns = new ArrayList<>();
        
        Pattern.numeroIndividuosGerados++;
    }

      
    public HashSet<Integer> getItens() {
        return itens;
    }

    public String getTipoAvaliacao() {
        return tipoAvaliacao;
    }

    public boolean[] getVrP() {
        return vrP;
    }

    public boolean[] getVrN() {
        return vrN;
    }

    public int getTP() {
        return TP;
    }

    public int getFP() {
        return FP;
    }

    public double getQualidade() {
        return qualidade;
    }

//    public ArrayList<Pattern> getSinonimos() {
//        return sinonimos;
//    }
//
//    public ArrayList<Pattern> getSubPatterns() {
//        return subPatterns;
//    }
    
    public Pattern[] getSimilares() {
        return similares;
    }

//    public void addSinonimo(Pattern sinonimo){
//        this.sinonimos.add(sinonimo);
//    }
//    
//    public void addSub(Pattern subPattern){
//        this.subPatterns.add(subPattern);
//    }
    
    //True se foi adicionado.
    //False se foi descartado no processo.
    public boolean addSimilar(Pattern similar){
        //Caso seja o primeiro similar
        if(this.similares == null){
            this.similares = new Pattern[Pattern.maxSimulares];
            this.similares[0] = new Pattern(similar.getItens(), similar.getTipoAvaliacao());
            //Preencher demais com vazio
            for(int i = 1; i < Pattern.maxSimulares; i++){
                this.similares[i] = new Pattern(new HashSet<Integer>(), this.tipoAvaliacao);
            }
            return true;//Foi adicionado!
        }else{//Caso não seja o primeiro similar
            if(similar.getQualidade() > this.similares[Pattern.maxSimulares-1].getQualidade()){
                if(!this.ehIgualSimilares(similar)){
                    this.similares[Pattern.maxSimulares-1] = new Pattern(similar.getItens(), similar.getTipoAvaliacao());
                    Arrays.sort(this.similares);
                    return true; //Foi adicionado!
                }
            }
        }
        
       

//        Pattern[] similaresDoSimilar = similar.getSimilares();
//        if(similaresDoSimilar != null){            
//            for(int i = 0; i < similaresDoSimilar.length; i++){
//                this.addSimilar(similaresDoSimilar[i]);
//            }
//        }
        return false;
    }
    
    public boolean ehIgual(Pattern p){
        return (p.getItens().containsAll(this.itens) && p.getItens().size() == this.itens.size());        
    }
    
    public boolean ehIgualSimilares(Pattern p){
        for(int i = 0; i < Pattern.maxSimulares; i++){
            if(p.ehIgual(this.similares[i])){
                return true;
            }
        }
        return false;        
    }
    
    /**
     * Retorna se indivíduo this sobrescreve o passado como parâmetro
     * Para um pattern sobrescrever outro é necessário possuir:
     * (1)todos os exemplos positivos do patternParâmetro e 
     * (2) não possuir nenhum dos exemplos negativos ausentes do patternParâmetros. 
     *@author Tarcísio Pontes
     * @param Pattern p
     * @return int (-1): não sobrescreve, (1): sovrescreve, (0): são sinônimos.
     * @since 27/01/2016
     * @version 1.0
     */
    public int sobrescreve(Pattern p){
       if(this.sobrescreveP(p) && this.sobrescreveN(p)){
           if(this.equivalente(p)){
                 return 0;
           }else{
               return 1;
           }
       }
       else{
           return -1;
       }
    }
    
    /**
     * Retorna se indivíduo this sobrescreve o passado como parâmetro em relação
     * aos exemplos positivos.
     * Lógica: Se meu vetorResultantePositivo interno contém todos os exemplos 
     * do vetorResultantePositivo passado como parâmetro, significa que ele
     * está contido dentro de mim e portanto, para alguns casos, ele é irrelevante.
     *@author Tarcísio Pontes
     * @param Pattern p
     * @return boolean se this sobrescreve em relação a exemplos positivos
     * @since 27/01/2016
     * @version 1.0
     */
    private boolean sobrescreveP(Pattern p){
        boolean[] vrPParametro = p.getVrP();
        for(int i = 0; i < vrPParametro.length; i++){
            if(vrPParametro[i] == true && this.vrP[i]!=true){
                return false;
            }
        }
        return true;
    }
    
    /**
     * Retorna se indivíduo this sobrescreve o passado como parâmetro em relação
     * aos exemplos negativos.
     * Lógica: Se para todo exemplo negativo, sempre que o vrN=false 
     * this.vrs=false significa que eu sou um superset do vetor passado como
     * parâmetro, logo, o sobrescrevo sem prejuízo.     * 
     *@author Tarcísio Pontes
     * @param Pattern p
     * @return boolean se this sobrescreve em relação a exemplos negativos
     * @since 27/01/2016
     * @version 1.0
     */
    private boolean sobrescreveN(Pattern p){
        boolean[] vrNParametro = p.getVrN();
        for(int i = 0; i < vrNParametro.length; i++){
            if(vrNParametro[i] == false && this.vrN[i]!=false){
                return false;
            }
        }
        return true;
    }
    
    
    /**
     * Retorna se indivíduo this cobre exatamente os mesmos exemplos positivos
     * e negativos do PAtterns passado como parâmetro
     * Lógica: Se tiverem os mesmos vetores resultantes Positivo e Negativo
     *@author Tarcísio Pontes
     * @param Pattern p
     * @return boolean se são equivalentes
     * @since 27/01/2016
     * @version 1.0
     */
    private boolean equivalente(Pattern p){
        boolean[] vrPParametro = p.vrP;
        boolean[] vrNParametro = p.vrN;
        
        for(int i = 0; i < vrPParametro.length; i++){
            if(vrPParametro[i] != this.vrP[i]){
                return false;
            }
        }
        
        for(int i = 0; i < vrNParametro.length; i++){
           if(vrNParametro[i] != this.vrN[i]){
               return false;
           } 
        }
        return true;
    }
    
    /**
     * Retonra de Pattern p é maior, igual ou menor que this considerando o
     * atributo qualidade.
     * Lógica: (1): se this maior que p, (0) se iguais e (-1) se this menos que p (confirmar!)
     * Utilizaod pela função Arrays.sort(Pattern[])
     *@author Tarcísio Pontes
     * @param Pattern p
     * @return int
     * @since 27/01/2016
     * @version 1.0
     */
    @Override
    public int compareTo(Pattern p) {
        double compareQuantity = p.getQualidade();
        double sub = compareQuantity - this.qualidade;
        
        if(sub > 0){
            return 1;
        }else if(sub == 0){
            return 0;
        }else{
            return -1;
        }        
    }
    
//    @Override
//    public String toString() {
//        //Capturando e ordenando conteúdo
//        Iterator iterator = itens.iterator();
//        int[] itensArray = new int[itens.size()];
//        int indice = 0;
//        while(iterator.hasNext()){
//            itensArray[indice++] = (int)iterator.next();
//        }           
//        Arrays.sort(itensArray);
//
//        //Salvando em string
//        StringBuilder str = new StringBuilder("{");
//        int j = 0;
//        for(; j < itensArray.length-1; j++){
//            str.append(itensArray[j]);
//            str.append(",");
//        }
//        str.append(itensArray[j]);
//        str.append("} -> ");
//        str.append(this.qualidade);
//        str.append("(");
//        str.append(this.TP);
//        str.append("p,");
//        str.append(this.FP);
//        str.append("n)");       
//        
//        str.append("\n{");       
//        
//        for(int i = 0; i < this.vrP.length; i++){
//            
//            if(this.vrP[i]){
//                str.append("1");
//            }else{
//                str.append("0");
//            }
//            if(i != this.vrP.length-1){
//                str.append(",");
//            }else{
//                str.append("} ");
//            }
//        }
//        
//        str.append("{");    
//        for(int i = 0; i < this.vrN.length; i++){
//            
//            if(this.vrN[i]){
//                str.append("1");
//            }else{
//                str.append("0");
//            }
//            if(i != this.vrN.length-1){
//                str.append(",");
//            }else{
//                str.append("} ");
//            }
//        }
//        return str.toString();
//    }
//    
    //Imprime regras em texto.
    public String toString2() {
        //Capturando e ordenando conteúdo
        Iterator iterator = itens.iterator();
        int[] itensArray = new int[itens.size()];
        int indice = 0;
        while(iterator.hasNext()){
            itensArray[indice++] = (int)iterator.next();
        }           
        Arrays.sort(itensArray);

        //Salvando em string
        StringBuilder str = new StringBuilder("{");
        int j = 0;
        for(; j < itensArray.length-1; j++){
            str.append(D.itemAtributoStr[ itensArray[j] ] + " = " + D.itemValorStr[ itensArray[j] ]);
            //str.append(itensArray[j]);
            str.append(",");
        }
        if(itens.size() > 0){
            str.append(D.itemAtributoStr[ itensArray[j] ] + " = " + D.itemValorStr[ itensArray[j] ]);
            //str.append(itensArray[j]);
        }
        str.append("} -> ");
        str.append(this.qualidade);
        str.append("(");
        str.append(this.TP);
        str.append("p,");
        str.append(this.FP);
        str.append("n)");  
        str.append("(conf=");
        str.append(DPinfo.conf(this));
        str.append(")");
        
//        str.append("\n{");       
//        
//        for(int i = 0; i < this.vrP.length; i++){
//            
//            if(this.vrP[i]){
//                str.append("1");
//            }else{
//                str.append("0");
//            }
//            if(i != this.vrP.length-1){
//                str.append(",");
//            }else{
//                str.append("} ");
//            }
//        }
//        
//        str.append("{");    
//        for(int i = 0; i < this.vrN.length; i++){
//            
//            if(this.vrN[i]){
//                str.append("1");
//            }else{
//                str.append("0");
//            }
//            if(i != this.vrN.length-1){
//                str.append(",");
//            }else{
//                str.append("} ");
//            }
//        }
        return str.toString();
    }
    
    
    //Imprime regras em texto incluindo similares de cada DP
    public String toString3() {
        if(this.itens.size() == 0){
            return "";
        }
        //Capturando e ordenando conteúdo
        Iterator iterator = itens.iterator();
        int[] itensArray = new int[itens.size()];
        int indice = 0;
        while(iterator.hasNext()){
            itensArray[indice++] = (int)iterator.next();
        }           
        
        Arrays.sort(itensArray);

        //Salvando em string
        StringBuilder str = new StringBuilder("{");
        int j = 0;
        for(; j < itensArray.length-1; j++){
            str.append(D.itemAtributoStr[ itensArray[j] ] + " = " + D.itemValorStr[ itensArray[j] ]);
            str.append(",");
        }
        str.append(D.itemAtributoStr[ itensArray[j] ] + " = " + D.itemValorStr[ itensArray[j] ]);
        str.append("} -> ");
        str.append(this.qualidade);
        str.append("(");
        str.append(this.TP);
        str.append("p,");
        str.append(this.FP);
        str.append("n)");       
        str.append("\n"); 
        if(this.similares != null){            
            for(int i = 0; i < this.similares.length; i++){
                str.append("\t");
                str.append(this.similares[i].toString3());
            }
        }        
//        str.append("\n{");       
//        
//        for(int i = 0; i < this.vrP.length; i++){
//            
//            if(this.vrP[i]){
//                str.append("1");
//            }else{
//                str.append("0");
//            }
//            if(i != this.vrP.length-1){
//                str.append(",");
//            }else{
//                str.append("} ");
//            }
//        }
//        
//        str.append("{");    
//        for(int i = 0; i < this.vrN.length; i++){
//            
//            if(this.vrN[i]){
//                str.append("1");
//            }else{
//                str.append("0");
//            }
//            if(i != this.vrN.length-1){
//                str.append(",");
//            }else{
//                str.append("} ");
//            }
//        }
          return str.toString(); 
    }

    
    //Imprime regras em texto incluindo similares de cada DP
    public String toString(String[] metricas, boolean imprimirCoberturaDp, boolean imprimirCoberturaDn, boolean imprimirSimilares) {
        if(this.itens.size() == 0){
            return "";
        }
        //Capturando e ordenando conteúdo
        Iterator iterator = itens.iterator();
        int[] itensArray = new int[itens.size()];
        int indice = 0;
        while(iterator.hasNext()){
            itensArray[indice++] = (int)iterator.next();
        }           
        
        Arrays.sort(itensArray);

        //Salvando em string
        StringBuilder str = new StringBuilder("{");
        int j = 0;
        for(; j < itensArray.length-1; j++){
            str.append(D.itemAtributoStr[ itensArray[j] ] + "=" + D.itemValorStr[ itensArray[j] ]);
            //str.append(itensArray[j]);
            str.append(",");
        }
        str.append(D.itemAtributoStr[ itensArray[j] ] + "=" + D.itemValorStr[ itensArray[j] ]);
        //str.append(itensArray[j]);
        str.append("}->" + D.valorAlvo);
        str.append(" (" + D.valorAlvo + "=" + this.TP + ", others=" + this.FP + ")");
        
        //Imprimindo métricas passadas como parâmetro.
        DecimalFormat df2 = new DecimalFormat("#.##");
        DecimalFormat df4 = new DecimalFormat("#.####");
        df2.setRoundingMode(RoundingMode.HALF_UP);
        df4.setRoundingMode(RoundingMode.HALF_UP);
        
        if(metricas != null){
            str.append("\n\t[");           
            for(int i = 0; i < metricas.length; i++){
                String metrica = metricas[i];
                switch(metrica){
                    case Const.METRICA_QUALIDADE:
                        str.append("Qualidade=" + df4.format(this.qualidade));
                        break;
                    case Const.METRICA_WRACC:
                        str.append("WRAcc=" + df4.format(Avaliador.WRAcc(this.TP, this.FP)));
                        break;
                    case Const.METRICA_Qg:
                        str.append("Qg=" + df2.format(Avaliador.Qg(this.TP, this.FP)));
                        break;
                    case Const.METRICA_CHI_QUAD:
                        str.append("Chi2=" + df2.format(Avaliador.chi_quad(this.TP, this.FP)));
                        break;    
                    case Const.METRICA_P_VALUE:
                        str.append("pValue=" + df4.format(Avaliador.p_value(this.TP, this.FP)));
                        break;
                    case Const.METRICA_LIFT:
                        str.append("Lift=" + df2.format(Avaliador.lift(this.TP, this.FP)));
                        break;
                    case Const.METRICA_DIFF_SUP:
                        str.append("DiffSup=" + df4.format(Avaliador.DiffSup(this.TP, this.FP)));
                        break;
                    case Const.METRICA_SIZE:
                        str.append("Size=" + df2.format(this.itens.size()));
                        break;
                    case Const.METRICA_COV:
                        str.append("Cov=" + df4.format(Avaliador.cov(this.TP, this.FP)));
                        break;
                    case Const.METRICA_CONF:
                        str.append("Conf=" + df4.format(Avaliador.conf(this.TP, this.FP)));
                        break;
                    case Const.METRICA_SUPP_POSITIVO:
                        str.append("Sup(+)=" + df4.format(Avaliador.suppPositivo(this.TP)));
                        break;
                    case Const.METRICA_SUPP_NEGATIVO:
                        str.append("Sup(-)=" + df4.format(Avaliador.suppNegativo(this.FP)));
                        break;                    
                }
                if(i < metricas.length-1){
                    str.append("|");
                }else{
                    str.append("]");
                }
            }
        }
        
        //Imprimindo cobertura D+
        if(imprimirCoberturaDp){
            str.append("\n\tD+:[");
            for(int i = 0; i < this.vrP.length; i++){
                if(this.vrP[i]){
                    str.append("X");
                }else{
                    str.append(" ");
                }
                if(i != this.vrP.length-1){
                    str.append("|");
                }else{
                    str.append("]");
                }
            }
        }
        
        //Imprimindo cobertura D-
        if(imprimirCoberturaDn){
            str.append("\n\tD-:[");
            for(int i = 0; i < this.vrN.length; i++){
                if(this.vrN[i]){
                   str.append("X");
                }else{
                    str.append(" ");
                }
                if(i != this.vrN.length-1){
                   str.append("|");
                }else{
                    str.append("]");
                }
            }
        }
               
        //Imprimir similares
        if(imprimirSimilares && this.similares != null){            
            str.append("\n");
            for(int i = 0; i < this.similares.length; i++){
                str.append("   cache(" + i + "):");
                str.append(this.similares[i].toString(metricas, imprimirCoberturaDp, imprimirCoberturaDn, !imprimirSimilares));
                str.append(" ||Similarity:" + df4.format(Avaliador.similaridade(this, this.similares[i], Pattern.medidaSimilaridade))+ "||");
                str.append("\n");
            }
        }
        return str.toString(); 
    }
}
