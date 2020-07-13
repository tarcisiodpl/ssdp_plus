# Introduction
SSDP+ is an evolutionary approach for mining diverse and more informative subgroups focused on high dimensional data sets. 
SSDP+ is an extension of the SSDP model (https://github.com/tarcisiodpl/ssdp) to provide diversity in a way that explore the relation between subgroups order to generate a more informative set of patterns. 

# Link for SSDP+ paper: 
https://ieeexplore.ieee.org/document/8477855

# Video: 

Rodando o SSDP+ (português): https://youtu.be/u175JvLD-38

# Description folders:

## SSDP+
Java implementation of SSDP+.
SSDP+ was implemented as a Netbeans project. Thus, you have to:
1) download and install the Netbeans with Java (https://www.oracle.com/technetwork/pt/java/javase/downloads/jdk-netbeans-jsp-3413153-ptb.html)
2) download the SSDPplus of this repository and open in Neatbeans
3) Class SSDPplus contains a main method that is a self explanatory way how to run the algorithm:
       
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
       
        

## experiments:
This folder contains the results of the experiments

## data sets:
This folder contains part of the databases used in the experiments
