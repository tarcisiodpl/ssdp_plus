package dp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

/**
 *
 * @author Tarcisio Lucas
 */
public class D {
   
    public static String nomeBase;
    public static String caminho;
    public static int numeroExemplos;
    public static int numeroExemplosPositivo;
    public static int numeroExemplosNegativo;
    public static int numeroAtributos;
    public static int numeroItens;
    
    public static String SEPARADOR = ","; 
    
    public static String[] nomeVariaveis;   
    
    public static int[] itemAtributo;
    public static int[] itemValor;
    public static String[] itemAtributoStr;
    public static String[] itemValorStr;
    
    public static int[][] Dp;
    public static int[][] Dn;
    
    public static int[] itensUtilizados;
    public static int numeroItensUtilizados;
    
    public static final int TIPO_CSV = 0;
    public static final int TIPO_ARFF = 1;
    public static final int TIPO_EXCEL = 2;
    
    public static String[][] dadosStr;
        
    public static String tipoDiscretizacao;
    
    public static String valorAlvo = "";
    public static String[] valoresAlvo;
    
    
    /** Recebe caminho para base de dados e tipo de formato e carrega base de dados na classe D 
     * @param caminho - caminho do arquivo completo
     * @param tipoArquivo - tipodo arquivo: CSV, ARFF, EXCEL, etc.
     * @throws FileNotFoundException 
     */    
    public static void CarregarArquivo_old(String caminho, int tipoArquivo) throws FileNotFoundException{
          
        //Passa dados do formato específico para um formato padrão definido por nós: String[][] dadosStr 
        D.dadosStr = null;
        switch(tipoArquivo){
            case D.TIPO_CSV:
                dadosStr = D.CVStoDadosStr(caminho);
                break;
            case D.TIPO_ARFF:
                //não implementado
                break;
            case D.TIPO_EXCEL:
                //não implementado
                break;
        }            
        
        //Carrega a partir do nosso formato em D
        D.dadosStrToD(dadosStr); 
        
        //Filtro determina os itens que serão considerados pelos algoritmos
        //Por padrão todos são aceitos
        D.numeroItensUtilizados = D.numeroItens;
        D.itensUtilizados = new int[D.numeroItensUtilizados];
        for(int l = 0; l < D.numeroItensUtilizados; l++){
            D.itensUtilizados[l] = l;
        }           
    }
    
    
    /** Recebe caminho para base de dados e tipo de formato e carrega base de dados na classe D 
     * @param caminho - caminho do arquivo completo
     * @param tipoArquivo - tipodo arquivo: CSV, ARFF, EXCEL, etc.
     * @throws FileNotFoundException 
     */    
    public static void CarregarArquivo(String caminho, int tipoArquivo) throws FileNotFoundException{
          
        //Passa dados do formato específico para um formato padrão definido por nós: String[][] dadosStr 
        D.dadosStr = null;
        switch(tipoArquivo){
            case D.TIPO_CSV:
                dadosStr = D.CVStoDadosStr(caminho);
                break;
            case D.TIPO_ARFF:
                //não implementado
                break;
            case D.TIPO_EXCEL:
                //não implementado
                break;
        }           
    }
    
    
    /** Gera D, Dp, Dn e itnes a partir da base salva no formato de matriz de String: dadosStr: String[][]  
     * @param rotulo: String valor de referência para dividir Dp e Dn 
     */    
    public static void GerarDpDn(String rotulo) throws FileNotFoundException{
        //Atribuindo alvo
        D.valorAlvo = rotulo;
        
        //Carrega a partir do nosso formato em D
        D.dadosStrToD(dadosStr); 
        
        //Filtro determina os itens que serão considerados pelos algoritmos
        //Por padrão todos são aceitos
        D.numeroItensUtilizados = D.numeroItens;
        D.itensUtilizados = new int[D.numeroItensUtilizados];
        for(int l = 0; l < D.numeroItensUtilizados; l++){
            D.itensUtilizados[l] = l;
        }
    }
    
    //Densidade é a quantidade
    public static double densidade(){        
        return 0.0;
    }
    

    
    /**Recebe caminho para arquivo .CSV ou .csv e retorna matriz de strings onde dadosStr:String[numeroExemplo][numeroAtributos].
     * Além disso: 
     * (1) Salva nome da base em D.nomeBase
     * (2) Salva os nomes dos atributos e do rótulo em D.nomeVariaveis
     * (3) Salva número de exemplos e de atributos
     * (4) Salva caminho da base em D.caminho
     * @param caminho
     * @return String[][] - String[numeroExemplo][numeroAtributos]
     * @throws FileNotFoundException 
     */
    private static String[][] CVStoDadosStr(String caminho) throws FileNotFoundException{
        //Lendo arquivo no formato padrão
        D.caminho = caminho;
        Scanner scanner = new Scanner(new FileReader(D.caminho))
                       .useDelimiter("\\n");
        ArrayList<String[]> dadosString = new ArrayList<>();        
              
        
        String[] palavras = D.caminho.split("\\\\");
        if(palavras.length == 1){
            palavras = D.caminho.split("/");//Caso separador de pastas seja / e  não \\
        }
        
        D.nomeBase = palavras[palavras.length-1].replace(".CSV", "");//Nome do arquivo é a última palavra (caso .CSV)
        D.nomeBase = D.nomeBase.replace(".csv", "");//(caso .csv)
                
        D.nomeVariaveis = scanner.next().split(D.SEPARADOR); //1º linha: nome das variáveis
        //Lipando nomes dos atributos
        for(int i = 0; i < D.nomeVariaveis.length; i++){
            D.nomeVariaveis[i] = D.nomeVariaveis[i].replaceAll("[\"\r\']", "");
        }
        
        D.numeroAtributos = D.nomeVariaveis.length-1; //último atributo é o rótulo
        while (scanner.hasNext()) {
            dadosString.add(scanner.next().split(D.SEPARADOR));
        }
        D.numeroExemplos = dadosString.size();
        
        HashSet<String> valoresAlvoHasSet = new HashSet<String>();
        String[][] dadosStr = new String[D.numeroExemplos][D.numeroAtributos+1];
        for(int i = 0; i < dadosString.size(); i++){
            String[] exemploBase = dadosString.get(i);//recebe linha de dados
            for(int j = 0; j < exemploBase.length; j++){
                dadosStr[i][j] = exemploBase[j].replaceAll("[\"\r\']", "");
            }
            //valoresAlvoHasSet.add(exemploBase[D.numeroAtributos]);
            valoresAlvoHasSet.add(dadosStr[i][D.numeroAtributos]);           
        }       
        
        //Coletanto valores distintos do atributo alvo
        D.valoresAlvo = new String[valoresAlvoHasSet.size()];
        Iterator iterator = valoresAlvoHasSet.iterator();
        int indice = 0;
        while(iterator.hasNext()){
            D.valoresAlvo[indice++] = (String) iterator.next();
        }
        Arrays.sort(D.valoresAlvo);
        
        return dadosStr;
    }
        
    /**Recebe dados no formato de String e preenche classe D com o universo de itens e exemplos positivos e negativos
     * (1) Gera universos de itens (atributo, valores) carregando em itemAtributoStr(String[]) e itemValorStr(String[])
     * (2) Mapeia universo de itens no formato original para inteiros: itemAtributo(int[]) e itemValor(int[]) 
     * (3) Mapeia base de dados para o formato de inteiros
     * OBS: a posição do array é o Item no problema de Grupos Discriminativos. 
     * Posição i, por exemplo é um item que representa o atributo itemAtributoStr[i] com valor itemValorStr[i].
     * Tais valores são mapeados nos inteiros itemAtributo[i] e itemValor[i], formato final da base de dadosutilizadas pelos algoritmos.  
     * @param dadosStr 
     */
    private static void dadosStrToD(String[][] dadosStr){
                
        //Capturando os valores distintos de cada atributo
        ArrayList<HashSet<String>> valoresDistintosAtributos = new ArrayList<>(); //Amazena os valores distintos de cada atributo em um linha
        D.numeroItens = 0;
        for(int i = 0; i < D.numeroAtributos; i++){
            HashSet<String> valoresDistintosAtributo = new HashSet<>(); //Armazena valores distintos de apenas um atributo. Criar HashSet para armezenar valores distintos de um atributo. Não admite valores repetidos!
            for(int j = 0; j < D.numeroExemplos; j++){
                valoresDistintosAtributo.add(dadosStr[j][i]); //Coleção não admite valores repetidos a baixo custo computacional.
            }
            D.numeroItens += valoresDistintosAtributo.size();
            
            valoresDistintosAtributos.add(valoresDistintosAtributo); //Adiciona lista de valores distintos do atributo de índice i na posição i do atributo atributosEvalores
        }
        
        //Gera 4 arrays para armazenar o universo deatributos e valores no formato original (String) e mapeado para inteiro.
        D.itemAtributoStr = new String[D.numeroItens];
        D.itemValorStr = new String[D.numeroItens];
        D.itemAtributo = new int[D.numeroItens];
        D.itemValor = new int[D.numeroItens];
            
        //Carrega arrays com universos de itens com valores reais e respectivos inteiros mapeados
        int[][] dadosInt = new int[D.numeroExemplos][D.numeroAtributos]; //dados no formato inteiro: mais rápido compararinteiros que strings
        int indiceItem = 0; //Indice vai de zero ao número de itens total
        for(int indiceAtributo = 0; indiceAtributo < valoresDistintosAtributos.size(); indiceAtributo++){
            Iterator valoresDistintosAtributoIterator = valoresDistintosAtributos.get(indiceAtributo).iterator(); //Capturando valores distintos do atributo de indice i
            int indiceValor = 0; //vai mapear um inteiro distinto para cada valor distinto de cada variável
            
            //Para cada atributo: 
            //Atribui inteiro para atributo e a cada valor do atributo.  
            //Realizar mapeamento na matriz de dados no formato inteiro
            while(valoresDistintosAtributoIterator.hasNext()){
                D.itemAtributoStr[indiceItem] = D.nomeVariaveis[indiceAtributo]; //
                D.itemValorStr[indiceItem] = (String)valoresDistintosAtributoIterator.next();

                D.itemAtributo[indiceItem] = indiceAtributo;
                D.itemValor[indiceItem] = indiceValor;               
                
                //Preenche respectivo item (atributo, Valor) na matrix dadosInt com inteiro que mapeia valor categórico da base
                for(int m = 0; m < D.numeroExemplos; m++){
                    if(dadosStr[m][indiceAtributo].equals(D.itemValorStr[indiceItem])){
                        dadosInt[m][indiceAtributo] = D.itemValor[indiceItem];
                    }
                }
                indiceValor++;
                indiceItem++;
            }     
        } 
        
        //Gera Bases de exemplos positivos (D+) e negativos (D-)
        D.geraDpDn(dadosStr, dadosInt);
    }
    
    /**
     * Gerar bases D+ (ou Dp) e D- (ou Dn) no formato numérico considerando D.valorAlvo como classe alvo
     * @param dadosStr
     * @param dadosInt 
     */
    private static void geraDpDn(String[][] dadosStr, int[][] dadosInt){
        //Capturar número de exemplo positivos (y="p") e negativos (y="n")
        int indiceRotulo = D.numeroAtributos;
        D.numeroExemplosPositivo = 0;
        D.numeroExemplosNegativo = 0;
        //Contanto número de exemplos positivos e negativos
        for(int i = 0; i < D.numeroExemplos; i++){
            String y = dadosStr[i][indiceRotulo];
            //if(y.equals(D.valorAlvo) || y.equals("\"" + D.valorAlvo + "\"\r") || y.equals("\'" + D.valorAlvo + "\'\r") || y.equals(D.valorAlvo + "\r")){
            if(y.equals(D.valorAlvo)){
                D.numeroExemplosPositivo++;
            }else{
                D.numeroExemplosNegativo++;
            }
        }
        
        //inicializando Dp e Dn
        D.Dp = new int[D.numeroExemplosPositivo][D.numeroAtributos];
        D.Dn = new int[D.numeroExemplosNegativo][D.numeroAtributos];
        
        int indiceDp = 0;
        int indiceDn = 0;
        for(int i = 0; i < D.numeroExemplos; i++){
            String yValue = dadosStr[i][indiceRotulo];
            //if(yValue.equals(D.valorAlvo) || yValue.equals("\"" + D.valorAlvo + "\"\r") || yValue.equals("\'" + D.valorAlvo + "\'\r") || yValue.equals(D.valorAlvo + "\r")){
            if(yValue.equals(D.valorAlvo)){
                for(int j = 0; j < D.numeroAtributos; j++){
                    Dp[indiceDp][j] = dadosInt[i][j];
                }
                indiceDp++;
            }else{
                for(int j = 0; j < D.numeroAtributos; j++){
                    Dn[indiceDn][j] = dadosInt[i][j];
                }
                indiceDn++;            
            }
        }
        System.out.println();
    }
    
    /**
     * Gera arquivo de dicionário .txt imprimindo valores de atributo e valor original e respectivos inteiros aos quais forma mapeados 
     * @param caminhoPastaSalvar - onde será salvo o arquivo com o dicionário
     * @throws IOException 
     */
    public static void recordDicionario(String caminhoPastaSalvar) throws IOException{
        String nomeArquivo = caminhoPastaSalvar + "\\" + D.nomeBase + "Dic.txt";
        String separadorDicionario = ",";
        File file = new File(nomeArquivo);
        // creates the file
        file.createNewFile();
        // creates a FileWriter Object
        FileWriter writer = new FileWriter(file); 
        // Writes the content to the file
        
        writer.write("@Nome: " + D.nomeBase + "\r\n"); 
        writer.write("@Info: Atributos=" + D.numeroAtributos +  separadorDicionario + "|D|=" +  D.numeroExemplos + separadorDicionario + "|Dp|=" + D.numeroExemplosPositivo + separadorDicionario + "|Dn|=" + D.numeroExemplosNegativo
            + separadorDicionario + "|I|=" + D.numeroItensUtilizados + "\r\n"); 
        //writer.write(); 
        writer.write("@Dicionario:Item,Atributo,Valor" + "\r\n"); 
        for(int i = 0; i < D.numeroItensUtilizados; i++){
            writer.write(i + separadorDicionario + D.itemAtributoStr[i] + separadorDicionario + itemValorStr[i] + "\r\n");           
        }      
        writer.flush();
        writer.close();
    }
    
    /**
     * Imprime dicionário no console. É um alternativa ao método recordDicionario que salva em arquivo.
     * @deprecated 
     * OBS: esse método pode estar defasado!
     */
    public static void imprimirDicionario(){        
        System.out.println("@Nome:" + D.nomeBase);
        System.out.println("@Info:Atributos=" + D.numeroAtributos + " ; |D|=" +  D.numeroExemplos + " ; |Dp|=" + D.numeroExemplosPositivo + " ; |Dn|=" + D.numeroExemplosNegativo
            + "; |I|=" + D.numeroItensUtilizados);
        //System.out.println("@Dicionario: Item;atributoOriginal;valorOriginal;atributoInt;valorInt");
        System.out.println("@Dicionario: Item;Atributo;Valor");
        for(int i = 0; i < D.numeroItensUtilizados; i++){
            //System.out.println(i + ";" + D.itemAtributoStr[i] + ";" + itemValorStr[i] + ";" + D.itemAtributo[i] + ";" + D.itemValor[i]);
            System.out.println(i + ";" + D.itemAtributoStr[i] + ";" + itemValorStr[i]);
        }        
    }

    /**
     * Filtra atributos, valores e intens (atributo,valor) passados como parâmetros.
     * Os itens filtrados não serão consideraodos pelos algoritmos nas buscas.
     * @param atributos
     * @param valores
     * @param atributosValores 
     */
    public static void filtrar(String[] atributos, String[] valores, String[][] atributosValores){
        ArrayList<Integer> itensPosFiltro = new ArrayList<Integer>();
        for(int i = 0; i < D.numeroItens; i++){
            if(D.filtroAtributoContempla(atributos, i) || 
                    D.filtroValorContempla(valores, i) || 
                    D.filtroAtributoValorContempla(atributosValores, i)){
                continue;
            }else{
                itensPosFiltro.add(i); //Adicione caso não perteça a nenhum filtro
            }
        }
        
        D.numeroItensUtilizados = itensPosFiltro.size();
        D.itensUtilizados = new int[D.numeroItensUtilizados];
        for(int i = 0; i < D.itensUtilizados.length; i++){
            D.itensUtilizados[i] = itensPosFiltro.get(i);
        }        
    }
    
    /**
     * Método retorna se item passado como parâmetro pertence ao grupo de atributos que devem ser desconsiderados na busca
     * @param atributos - String[] com valores de atributos que devem ser filtrados
     * @param item - item que deve ou não ser filtrado com base no filtro
     * @return 
     */
    private static boolean filtroAtributoContempla(String[] atributos, int item){
        if(atributos == null){
            return false;
        }else{
            for(int j = 0; j < atributos.length; j++){
                //if(D.comparaStrVar(atributos[j], D.itemAtributoStr[item])){
                if(atributos[j].equals(itemAtributoStr[item])){
                    return true;
                }
            }            
        }    
        return false;
    }
            
    
    /**
     * Método retorna se item passado como parâmetro pertence ao grupo de VALORES que devem ser desconsiderados na busca
     * @param valores - String[] com valores de atributos que devem ser filtrados
     * @param item - item que deve ou não ser filtrado com base no filtro
     * @return 
     */
    private static boolean filtroValorContempla(String[] valores, int item){
        if(valores == null){
            return false;
        }else{
            for(int j = 0; j < valores.length; j++){
                //if( D.comparaStrVar(valores[j], D.itemValorStr[item]) ){
                if( valores[j].equals(D.itemValorStr[item]) ){
                    return true;
                }
            }            
        }    
        return false;
    }
    
    
    /**
     * Método retorna se item passado como parâmetro pertence ao grupo de intens (atributo, valor) que devem ser desconsiderados na busca
     * @param atributosValores - String[][]
     * @param item - item que deve ou não ser filtrado com base no filtro
     * @return 
     */
    private static boolean filtroAtributoValorContempla(String[][] atributosValores, int item){
        if(atributosValores == null){
            return false;
        }else{
            for(int j = 0; j < atributosValores.length; j++){
                //if(D.comparaStrVar(atributosValores[j][0], D.itemAtributoStr[item]) &&
                //   D.comparaStrVar(atributosValores[j][1], D.itemValorStr[item]) ){
                if(atributosValores[j][0].equals(D.itemAtributoStr[item]) &&
                    atributosValores[j][1].equals(D.itemValorStr[item]) ){
                
                    return true;
                }
            }            
        }    
        return false;
    }
    
    /**
     * Compara duas strings com variações de formatos provavelemnte devido a fomatação do testo (ISO, ANSI, etc.) Não sei se estácobrindo todoas as possibilidades.
     * Deve ter uma forma mais elegante de lidar com esse problema!!!
     * @param palavra
     * @param palavraVariacoes
     * @return 
     */
    private static boolean comparaStrVar(String palavraVariacoes, String palavra){
        return (palavra.equals(palavraVariacoes) //|| 
                //palavra.equals( "\"" + palavraVariacoes  + "\"") || 
                //palavra.equals( "\"" + palavraVariacoes  + "\"\r") || 
                //palavra.equals("\'" + palavraVariacoes  + "\'\r") || 
                //palavra.equals(palavraVariacoes  + "\r")
                );       
    } 
    
    
    
    public static void main(String args[]) throws FileNotFoundException, IOException{
        
//        String caminho = Const.CAMINHO_BASES + "amazon_cells_labelled.csv";
//        
//        D.CarregarArquivo(caminho, D.TIPO_CSV);
//              
//        System.out.println();
        
        String caminhoPastaArquivos = Const.CAMINHO_BASES;
        
        File diretorio = new File(caminhoPastaArquivos);
        File arquivos[] = diretorio.listFiles();
        D.SEPARADOR = ",";
        for(int i = 0; i < arquivos.length; i++){  
        //for(int i = 0; i < 2; i++){  
                String caminhoBase = arquivos[i].getAbsolutePath();
                D.CarregarArquivo(caminhoBase, D.TIPO_CSV);
                D.GerarDpDn("p");
                System.out.println("[" + i + "]");
                //D.imprimirDicionario();
                D.recordDicionario(Const.CAMINHO_DICIONARIOS);                
        }
    }

}
