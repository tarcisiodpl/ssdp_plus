/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dp;

import java.util.Random;

/**
 *
 * @author tarcisio_pontes
 */
public class Const {
    public final static String ALGORITMO_SSDP = "SSDP";
    public final static String ALGORITMO_SSDPmais = "SSDPplus";
    public final static String ALGORITMO_SSDPmaisS00 = "SSDPplusS00";
    public final static String ALGORITMO_SSDPmaisS10 = "SSDPplusS10";
    public final static String ALGORITMO_SSDPmaisS20 = "SSDPplusS20";
    public final static String ALGORITMO_SSDPmaisS30 = "SSDPplusS30";
    public final static String ALGORITMO_SSDPmaisS40 = "SSDPplusS40";
    public final static String ALGORITMO_SSDPmaisS50 = "SSDPplusS50";
    public final static String ALGORITMO_SSDPmaisS60 = "SSDPplusS60";
    public final static String ALGORITMO_SSDPmaisS70 = "SSDPplusS70";
    public final static String ALGORITMO_SSDPmaisS80 = "SSDPplusS80";    
    public final static String ALGORITMO_SSDPmaisS90 = "SSDPplusS90";
    public final static String ALGORITMO_SSDPmaisS100 = "SSDPplusS100";
    
    public final static String ALGORITMO_GulosoDplus = "GulosoDplus";
    public final static String ALGORITMO_GulosoDplus4D = "GulosoDplus4D";
    
//    public final static String ALGORITMO_SSDPmais90Pbest = "SSDPplusS90Pbest";
//    public final static String ALGORITMO_SSDPmais50Pbest = "SSDPplusS50Pbest";
//    public final static String ALGORITMO_SSDPmais10Pbest = "SSDPplusS10Pbest";
//    public final static String ALGORITMO_SSDPmais90Prb = "SSDPplusS90Prb";
//    public final static String ALGORITMO_SSDPmais50Prb = "SSDPplusS50Prb";
//    public final static String ALGORITMO_SSDPmais10Prb = "SSDPplusS10Prb";
//    
//    public final static String ALGORITMO_SSDPmaisPbest = "SSDPplusPbest";
//    public final static String ALGORITMO_SSDPmaisPbest2k = "SSDPplusPbest2k";
//    public final static String ALGORITMO_SSDPmaisPbest2kselection = "SSDPplusPbest2kS";
//    public final static String ALGORITMO_SSDPmaisRandom50Best50 = "SSDPplusPrb";
//    public final static String ALGORITMO_SSDPmaisRandom50Best502k = "SSDPplusPrb2k";
//    public final static String ALGORITMO_SSDPmaisRandom50Best502kselection = "SSDPplusPrb2kS";
//    
    
    
    public final static String ALGORITMO_SSDPmaisRandom = "SSDPmaisRandom";
       
    public final static String ALGORITMO_SD = "SD";
    public final static String ALGORITMO_SD_RSS = "SDrss";
    
    public final static String ALGORITMO_ExaustivoK = "ExaustivoK";
    public final static String ALGORITMO_Aleatorio1M = "Aleatorio1M";
    public final static String ALGORITMO_Aleatorio2M = "Aleatorio2M";
    
    public final static String METRICA_QUALIDADE = "Qualidade";
    public final static String METRICA_TIME = "Time";
    public final static String METRICA_NUMERO_TESTES = "Testes";
    public final static String METRICA_Qg = "Qg";
    public final static String METRICA_WRACC = "WRAcc";
    public final static String METRICA_WRACC_NORMALIZED = "WRAccN";
    public final static String METRICA_DIFF_SUP = "DiffSup";
    public final static String METRICA_CHI_QUAD = "Chi_Quad";
    public final static String METRICA_P_VALUE = "p_value";
    public final static String METRICA_LIFT = "Lift";
    public final static String METRICA_GROWTH_RATE = "GrowthRate";
    public final static String METRICA_ODDS_RATIO = "OddsRatio";
    public final static String METRICA_SUPP = "supp";
    public final static String METRICA_SUPP_POSITIVO = "suppP";
    public final static String METRICA_SUPP_NEGATIVO = "suppN";
    public final static String METRICA_OVERALL_SUPP_POSITIVO = "overollSuppP";
    public final static String METRICA_OVERALL_SUPP_NEGATIVO = "overollSuppN";
    public final static String METRICA_COVER_REDUNDANCY_POSITIVO = "CoverRedundancyP";
    public final static String METRICA_DESCRIPTION_REDUNDANCY_DENSITY = "DescripRDensity";
    public final static String METRICA_DESCRIPTION_REDUNDANCY_DOMINATOR = "DescripRDominator";    
    public final static String METRICA_COV = "cov";
    public final static String METRICA_CONF = "conf";
    public final static String METRICA_SIZE = "size";
    public final static String METRICA_K = "K";
    public final static String METRICA_TP = "TP";
    public final static String METRICA_FP = "FP";
    
    public final static String SIMILARIDADE_JACCARD = "JACCARD";
    public final static String SIMILARIDADE_SOKAL_MICHENER = "SOKAL_MICHENER";
    
    public final static int PATTERN_OR = 0;
    public final static int PATTERN_AND = 1;

//    public final static String CAMINHO = "../dist/pastas/";
//    public final static String CAMINHO_BASES = Const.CAMINHO + "bases/";
//    public final static String CAMINHO_BASES_EDITADAS = Const.CAMINHO + "bases_editadas/";
//    public final static String CAMINHO_RESULTADOS = Const.CAMINHO + "resultados/";
//    public final static String CAMINHO_RESULTADOS_OBJ = Const.CAMINHO + "resultados_obj/";  
//    public final static String CAMINHO_RELATORIO = Const.CAMINHO + "relatorios/";
//    public final static String CAMINHO_DICIONARIOS = Const.CAMINHO + "dicionarios/";
//    public final static String CAMINHO_INDICE = Const.CAMINHO + "indice.txt";
    
    public final static String CAMINHO_BASES = "../DP4/pastas/bases/";
    public final static String CAMINHO_BASES_EDITADAS = "../DP4/pastas/bases_editadas/";
    public final static String CAMINHO_RESULTADOS = "../DP4/pastas/resultados/";
    public final static String CAMINHO_RESULTADOS_OBJ = "../DP4/pastas/resultados_obj/";  
    public final static String CAMINHO_RELATORIO = "../DP4/pastas/relatorios/";
    public final static String CAMINHO_DICIONARIOS = "../DP4/pastas/dicionarios/";
    public final static String CAMINHO_INDICE = "../DP4/pastas/indice.txt";
       
    public static Random random;   
    public final static long[] SEEDS = {179424673, 125164703, 132011827, 124987441, 123979721 , 119777719, 117705823 , 112131119, 108626351, 107980007, 
        106368047, 99187427, 98976029, 97875523, 96763291, 95808337, 94847387, 87552823, 86842271 , 80650457, 78220001, 74585729, 73852469 , 68750849, 58160551 , 
        45320477, 31913771, 24096223, 16980937, 8261369};
}
