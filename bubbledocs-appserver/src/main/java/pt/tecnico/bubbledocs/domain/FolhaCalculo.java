package pt.tecnico.bubbledocs.domain;

import org.joda.time.LocalDate;


public class FolhaCalculo extends FolhaCalculo_Base {
	
	
    public FolhaCalculo(String nome,int linhas,int colunas) {
        super();
        this.setNome(nome);
        super.setLinhas(linhas);
        super.setColunas(colunas);
        this.setBubbledocsFolhas(Bubbledocs.getInstance());
        super.setDataCriacao(new LocalDate());
        super.setId(Bubbledocs.getInstance().gerarUniqueId());
    }
	
    /*
     * setLinhas - Overrided para que não se possa alterar o numero de linahs da folha.
     */
    @Override
    public void setLinhas(int linhas){}
    
    /*
     * setColunas - Overrided para que não se possa alterar o numero de colunas da folha.
     */
    @Override
    public void setColunas(int colunas){}
    
    /*
     * setId - Overrided para que não se possa alterar o ID atribuido pelo sistema.
     */
    @Override
    public void setId(int id){}
    
    /*
     * setDataCriacao - Overrided para que não se possa alterar a data de criacao da folha.
     */
    @Override
    public void setDataCriacao(LocalDate data){}
    
    @Override
    public String toString(){
    	String s ="ID: "+getId()+"\nNome: "+getNome()+"\nData Criação: "+getDataCriacao().toString()+"\nDono: "+getOwner().getNome()+"\n";
    	return s;
    }
    
}
