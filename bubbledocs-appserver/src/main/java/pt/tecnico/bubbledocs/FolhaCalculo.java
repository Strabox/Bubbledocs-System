package pt.tecnico.bubbledocs;

import java.util.Date;


public class FolhaCalculo extends FolhaCalculo_Base {
	
	private static int uniqueId = 0;
	
	private Date dataCriacao;
	
	
    public FolhaCalculo(String nome) {
        super();
        this.setNome(nome);
        this.setId(uniqueId++);
        this.dataCriacao = new Date();
    }

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
	
	
	
    
}
