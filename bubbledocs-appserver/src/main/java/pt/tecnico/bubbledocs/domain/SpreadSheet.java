package pt.tecnico.bubbledocs.domain;

import org.joda.time.LocalDate;


public class SpreadSheet extends SpreadSheet_Base {
	
	
    public SpreadSheet(String name,int linhas,int colunas) {
        super();
        this.setName(name);
        super.setLines(linhas);
        super.setColumns(colunas);
        super.setCreationDate(new LocalDate());
        super.setId(Bubbledocs.getInstance().gerarUniqueId());
    }
	
    /*
     * setLinhas - Overrided para que não se possa alterar o numero de linahs da folha.
     */
    @Override
    public void setLines(int lines){}
    
    /*
     * setColunas - Overrided para que não se possa alterar o numero de colunas da folha.
     */
    @Override
    public void setColumns(int columns){}
    
    /*
     * setId - Overrided para que não se possa alterar o ID atribuido pelo sistema.
     */
    @Override
    public void setId(int id){}
    
    /*
     * setDataCriacao - Overrided para que não se possa alterar a data de criacao da folha.
     */
    @Override
    public void setCreationDate(LocalDate date){}
    
    @Override
    public String toString(){
    	String s ="ID: "+getId()+"\nNome: "+getName()+"\nData Criação: "+getCreationDate().toString()+"\nDono: "+getOwner().getNome()+"\n";
    	return s;
    }
    
}