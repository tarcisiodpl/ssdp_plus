/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolucionario;

import dp.Avaliador;
import dp.Const;
import dp.Pattern;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;


/**
 *
 * @author Tarcísio Pontes
 * @since 27/01/2016
 * @version 1.0
 * Revisão ok!
 */
public class SELECAO {
        
    /**
     * Os 75% primeiros índices são selecionados dentre os 25% melhores,
     * considerando que os indivíduoes estão em ordenados do melhor para o pior.
     * Os 25% restantes são índices aleatórios entre zero e o tamanho da população
     * A quantidade de índies retornado é sempre do mesmo do tamanho da população.
     *@author Tarcísio Lucas
     * @param tamanhoPopulacao
     * @return int[]
     * @since 14/01/2016
     * @version 1.0
     */
    public static int[] proporcao25_75(int tamanhoPopulacao){
        int[] indices = new int[tamanhoPopulacao];
        int i = 0;
        for(; i < tamanhoPopulacao*0.75; i++){
            indices[i] = Const.random.nextInt(tamanhoPopulacao*1/4);
        }
        for(; i < tamanhoPopulacao; i++){
            indices[i] = Const.random.nextInt(tamanhoPopulacao);
        }
        return indices;
    }
    
    /**
     * Retorna índices vencedores em torneios binários (entre dois indivíduos)
     *@author Tarcísio Lucas
     * @param tamanhoPopulacao
     * @param  P população
     * @return int[] - índices vencedores de P
     * @since 27/01/2016
     * @version 1.0
     */
    public static int[] torneioBinario(int tamanhoPopulacao, Pattern[] P){
        int[] indices = new int[tamanhoPopulacao];
        for(int i = 0; i < indices.length; i++){
            int indiceP1 = Const.random.nextInt(P.length);
            int indiceP2 = Const.random.nextInt(P.length);
            if(P[indiceP1].getQualidade() > P[indiceP2].getQualidade()){
                indices[i] = indiceP1;
            }else{
                indices[i] = indiceP2;         
            }
        }
        return indices;
    }
    
    /**
     * Retorna índice vencedor em torneio binário (entre dois indivíduos aleatórios)
     *@author Tarcísio Lucas
     * @param  P população
     * @return int - índice vencedore de P
     * @since 27/01/2016
     * @version 1.0
     */
    public static int torneioBinario(Pattern[] P){
        int indiceP1 = Const.random.nextInt(P.length);
        int indiceP2 = Const.random.nextInt(P.length);
        if(P[indiceP1].getQualidade() > P[indiceP2].getQualidade()){
            return indiceP1;
        }else{
            return indiceP2;                     
        }        
    }
    
    
    /**
     * Recebe duas populações de mesmo tamanho T e retorna os T melhores
     * indivíduos distintos.
     *@author Tarcísio Lucas
     * @param P PAtterns[]
     * @param Pnovo PAtterns[]
     * @return Pattern[]
     * @since 27/01/2016
     * @version 1.0
     */
    public static Pattern[] selecionarMelhoresDistintos(Pattern[] P, Pattern[] Pnovo){
        int tamanhoPopulacao = P.length;
        Pattern[] PAsterisco = new Pattern[tamanhoPopulacao];        
        ArrayList<Pattern> patternAux = new ArrayList<>();
        
        //System.out.println("\tAdicioando P");
        patternAux.addAll(Arrays.asList(P));
        
        //System.out.println("\tAdicioando Pnovos");
        for (Pattern pnovo : Pnovo) {
            if (SELECAO.ehInedito(pnovo, patternAux)) {
                patternAux.add(pnovo);                
            }           
        }
        //int numeroIneditosNovo = patternAux.size() - P.length;
        
        //System.out.println("\tOrdenando...");
        Collections.sort(patternAux);
        //System.out.println("\tCopiando |P| melhores...");
        for(int i = 0; i < PAsterisco.length; i++){
            PAsterisco[i] = patternAux.get(i);
        }        
        //System.out.println("|Pnovo| = " + numeroIneditosNovo);
        return PAsterisco;
    }
        
    /**
     * Recebe 03 populações de mesmo tamanho T e retorna os T melhores
     * indivíduos distintos.
     *@author Tarcísio Lucas
     * @param P1 PAtterns[]
     * @param P2 PAtterns[]
     * @param P3 PAtterns[]
     * @return Pattern[]
     * @since 29/01/2016
     * @version 1.0
     */
    public static Pattern[] selecionarMelhoresDistintos(Pattern[] P1, Pattern[] P2, Pattern[] P3){
        int tamanhoPopulacao = P1.length;
        Pattern[] PAsterisco = new Pattern[tamanhoPopulacao];        
        ArrayList<Pattern> patternAux = new ArrayList<>();
        
        //System.out.println("\tAdicioando P");
        patternAux.addAll(Arrays.asList(P1));
        
        //System.out.println("\tAdicioando Pnovos");
        for (Pattern p2 : P2) {
            if (SELECAO.ehInedito(p2, patternAux)) {
                patternAux.add(p2);                
            }           
        }
        
        //System.out.println("\tAdicioando Pnovos");
        for (Pattern p3 : P3) {
            if (SELECAO.ehInedito(p3, patternAux)) {
                patternAux.add(p3);                
            }           
        }
        //int numeroIneditosNovo = patternAux.size() - P.length;
        
        //System.out.println("\tOrdenando...");
        Collections.sort(patternAux);
        //System.out.println("\tCopiando |P| melhores...");
        for(int i = 0; i < PAsterisco.length; i++){
            PAsterisco[i] = patternAux.get(i);
        }        
        //System.out.println("|Pnovo| = " + numeroIneditosNovo);
        return PAsterisco;
    }
       
    /**
     * Recebe duas populações de mesmo tamanho T e retorna os T melhores
     * indivíduos NÃO distintos! Ou seja, não controla se indivíduos são
     * distintos.
     *@author Tarcísio Lucas
     * @param P PAtterns[]
     * @param Pnovo PAtterns[]
     * @return Pattern[]
     * @since 27/01/2016
     * @version 1.0
     */
    public static Pattern[] selecionarMelhores(Pattern[] P, Pattern[] Pnovo){
        int tamanhoPopulacao = P.length;
        Pattern[] PAsterisco = new Pattern[tamanhoPopulacao];        
        Pattern[] PAuxiliar = new Pattern[2*tamanhoPopulacao];        
        System.arraycopy(P, 0, PAuxiliar, 0, P.length);        
        System.arraycopy(Pnovo, 0, PAuxiliar, P.length, Pnovo.length);        
        Arrays.sort(PAuxiliar);                
        System.arraycopy(PAuxiliar, 0, PAsterisco, 0, PAsterisco.length);                
        return PAsterisco;
    }
    
    
    /**
     * PAsterisco recebe aleatórios entre P e Pnovo, além de Pk.
     *@author Tarcísio Lucas
     * @param P Pattern[]
     * @param Pnovo Pattern[]
     * @param Pk Pattern[]
     * @return Pattern[]
     * @since 22/07/2017
     * @version 1.0
     */
    public static Pattern[] selecionarAleatorio(Pattern[] P, Pattern[] Pnovo, Pattern[] Pk){
        int tamanhoPopulacao = P.length;
        Pattern[] PAsterisco = new Pattern[tamanhoPopulacao];        
        
                
        int i = 0;
        if(Pk.length * 10 < tamanhoPopulacao){
            for(; i < Pk.length; i++){
                PAsterisco[i] = Pk[Const.random.nextInt(Pk.length)];
            }
        }else{
            for(; i < tamanhoPopulacao/10; i++){
                PAsterisco[i] = Pk[i];
            }
        }
        
        for(; i < tamanhoPopulacao; i++){
            if(Const.random.nextBoolean()){
                PAsterisco[i] = P[Const.random.nextInt(tamanhoPopulacao)];
            }else{
                PAsterisco[i] = Pnovo[Const.random.nextInt(tamanhoPopulacao)];
            }
        }
        Arrays.sort(PAsterisco);
        return PAsterisco;
    }
    
    /**Gera PAsterisco da seguinte forma:
     * 50%: os melhores entre P e Pnovo
     * 50%: aleatório entre os desconsiderado na primeira parte.
     * Objetivo é gerar mais diversidade em P
     * @param P
     * @param Pnovo
     * @return 
     * @since 25/07/2017
     */
    public static Pattern[] selecionarMelhores50Aleatorio50(Pattern[] P, Pattern[] Pnovo){
        int tamanhoPopulacao = P.length;
        Pattern[] PAsterisco = new Pattern[tamanhoPopulacao];        
        Pattern[] PAuxiliar = new Pattern[2*tamanhoPopulacao];        
        System.arraycopy(P, 0, PAuxiliar, 0, P.length);        
        System.arraycopy(Pnovo, 0, PAuxiliar, P.length, Pnovo.length);        
        Arrays.sort(PAuxiliar);                
        
        //PAsterisco <- 50% melhores indivíduoes entre P e Pnovo.
        int indiceMelhores = PAsterisco.length/2;
        System.arraycopy(PAuxiliar, 0, PAsterisco, 0, indiceMelhores);
        
        //PAsterisco <- 50% aleatório entre os demais indivíduos. 
        //Os indivíduoes inseridos na primeira parte são desconsiderados aqui.
        int i = indiceMelhores;
        for(; i < PAsterisco.length; i++){
            int indiceRandom = indiceMelhores + Const.random.nextInt(PAuxiliar.length - indiceMelhores);
            PAsterisco[i] = PAuxiliar[indiceRandom];
        }
        return PAsterisco;
    }
    
    
    
    /**
     * Recebe duas populações de mesmo tamanho T e retorna os T melhores
     * indivíduos NÃO distintos! Ou seja, não controla se indivíduos são
     * distintos.
     *@author Tarcísio Lucas
     * @param P PAtterns[]
     * @param Pnovo PAtterns[]
     * @return Pattern[]
     * @since 27/01/2016
     * @version 1.0
     */
    public static Pattern[] selecionarMelhores(Pattern[] P1, Pattern[] P2, Pattern[] P3){
        int tamanhoPopulacao = P1.length;
        Pattern[] PAsterisco = new Pattern[tamanhoPopulacao];        
        Pattern[] PAuxiliar = new Pattern[3*tamanhoPopulacao];        
        System.arraycopy(P1, 0, PAuxiliar, 0, tamanhoPopulacao);        
        System.arraycopy(P2, 0, PAuxiliar, tamanhoPopulacao, tamanhoPopulacao);
        System.arraycopy(P3, 0, PAuxiliar, tamanhoPopulacao*2, tamanhoPopulacao);        
        Arrays.sort(PAuxiliar);                
        System.arraycopy(PAuxiliar, 0, PAsterisco, 0, PAsterisco.length);                
        return PAsterisco;
    }
    
    
    
    /**Atualiza Pk com os indivíduos de melhor qualidade presentes em PAsterísco. 
     * Retorna o número de substituições realizadas em Pk.
     *@author Tarcísio Lucas
     * @param Pk Pattern[] - top-k indivíduos ordenados e distintos
     * @param PAsterisco Pattern[] - população de indivíduos ordenados
     * @return Pattern[] - número de novos indivíduoes inseridos em Pk
     */
    public static int salvandoRelevantes(Pattern[] Pk, Pattern[] PAsterisco){
        int indiceP = 0;
        int novosk10 = 0;
        while(indiceP < PAsterisco.length && (PAsterisco[indiceP].getQualidade() > Pk[Pk.length-1].getQualidade())){
            if(SELECAO.ehRelevante(PAsterisco[indiceP], Pk)){
                Pk[Pk.length-1] = PAsterisco[indiceP];
                Arrays.sort(Pk);                                    
                novosk10++;
            }
            indiceP++;
        }
        return novosk10;
    }
    
    
    
    
    
    /**Atualiza Pk com os indivíduos de melhor qualidade presentes em PAsterísco. 
     * Retorna o número de substituições realizadas em Pk.
     * Redundância: indivíduos similares não são descartados e sim relacionados:
     * Mais detalhes depois!
     *@author Tarcísio Pontes
     * @param Pk Pattern[] - top-k indivíduos ordenados e distintos
     * @param PAsterisco Pattern[] - população de indivíduos ordenados
     * @return Pattern[] - número de novos indivíduoes inseridos em Pk
     */
    public static int salvandoRelevantesDPmais(Pattern[] Pk, Pattern[] PAsterisco, double similaridadeLimite){
        int novosk = 0;
        for( int i = 0; i < PAsterisco.length && (PAsterisco[i].getQualidade() > Pk[Pk.length-1].getQualidade()); i++){
            Pattern p_PAsterisco = PAsterisco[i];
            //Três possibilidades
            //(1) igual a alguma DP de Pk: descartar
            //(2) não similar a nenhum de Pk: troca por Pk[Pk-length-1]
            //(3) similar
            //(3.1) similar com p_Pk maior: p_Pk engloba como similar e não muda a ordem das DPs em Pk
            //(3.2) similar com p_PAsterisco maior: p_PAsterisco engloba como similar p_Pk, ocupa a vaga em Pk[i] e reordena-se Pk
            for(int j = 0; j < Pk.length; j++){
                Pattern p_Pk = Pk[j];
                //double similaridade = SELECAO.similaridadeDPpositivo(p_PAsterisco, p_Pk);
                double similaridade = Avaliador.similaridade(p_Pk, p_PAsterisco, Pattern.medidaSimilaridade);
                if(similaridade >= similaridadeLimite){// Houve similaridade
                    //Se eles tiverem os mesmos itens, descartar! (1)
                    if(p_PAsterisco.ehIgual(p_Pk)){
                        break; //sair do for que itera Pk 
                    }else{
                        //Se não, Se pk melhor que p* ou (pk == p* and pk.size <= p*.size)
                        if(p_Pk.getQualidade() > p_PAsterisco.getQualidade() ||
                                (p_Pk.getQualidade() == p_PAsterisco.getQualidade() && p_Pk.getItens().size() <= p_PAsterisco.getItens().size()) 
                                ){
                            boolean aproveitadoEmPk = p_Pk.addSimilar(p_PAsterisco);
                            if(aproveitadoEmPk){
                                novosk++;
                            }
                        }else{
                            Pk[j] = new Pattern(p_PAsterisco.getItens(), p_PAsterisco.getTipoAvaliacao());
                            //Adicionando p_Pk como filho de P_PAsterisco
                            Pk[j].addSimilar(p_Pk);
                            
                            //filhos de p_Pk podem ser adicionado a Pk.
                            if(p_Pk.getSimilares() != null){
                                SELECAO.salvandoRelevantesDPmais(Pk, p_Pk.getSimilares(), similaridadeLimite);
                            }                            
                            Arrays.sort(Pk);
                            novosk++;
                        }                        
                        break; //sair do for que itera Pk
                    }
                                       
                }else if(j == Pk.length-1){//Se dp.new não for similar a nenhuma DP de Pk, então ele substitui a última
                    Pk[Pk.length-1] = new Pattern(p_PAsterisco.getItens(), p_PAsterisco.getTipoAvaliacao());
                    Arrays.sort(Pk);                                    
                    novosk++;                    
                }       
            }//for percorre Pk     
        }
        return novosk;
    }
    
    
    
    
    
    /**
     * Retorna se um Pattern P é inédito em relação a um Conjunto de patterns.
     * @author Tarcísio Pontes
     * @param p Patteern
     * @param pList ArrayList<Pattern>
     * @return boolean
     * @since 27/01/2016
     * @version 1.0
     */
    private static boolean ehInedito(Pattern p, ArrayList<Pattern> pList){
        
        for(int i = 0; i < pList.size(); i++){
            if(SELECAO.ehIgual(p, pList.get(i))){
                return false;
            }
        }
        return true;
    }
    
    /**
     * Retorna se um Pattern P1 é igual a outro P2.
     * Lembrando com os itens num Patterns nessa implementação não são ordenados.
     * Isso dificutou um pouco a lógica desse método.
     * @author Tarcísio Pontes
     * @param p1 Patteern
     * @param p2 Patteern
     * @return boolean
     * @since 27/01/2016
     * @version 1.0
     */
    private static boolean ehIgual(Pattern p1, Pattern p2){
        if(p1.getQualidade() == p2.getQualidade()){//A maioria dos casos são resolvidos aqui.
            HashSet<Integer> itens1 = p1.getItens();
            HashSet<Integer> itens2 = p2.getItens();
        
            if(itens1.size() != itens2.size()){
                return false;
            }else{
                return itens1.containsAll(itens2);
            }
        }else{
            return false;
        }
        
        
    }
    
    /**Retorna se indivíduos candidato (fitness maior que pior indivíduo de Pk)
     * é dominado por algum indivíduo de Pk.
     * Se for igual a algum indivíduo ou cobrir subparte dos exemplos positivos
     * presentes e negativos ausêntes, ele é considerado dominado.
     *@author Tarcísio Pontes
     * @param p Pattern - indivíduo candidato a substituir o pior indivíduo de Pk.
     * @param Pk Pattern[] - população com os k melhores indivíduos
     * @return boolean - se novo indivíduo é relevante
     */
    public static boolean ehRelevante(Pattern p, Pattern[] Pk){
        for(int i = 0; i  < Pk.length; i++){
            if(Pk[i].sobrescreve(p) != -1){
                return false;
            }
        }
        return true;
    }
    
}
