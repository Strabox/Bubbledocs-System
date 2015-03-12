package pt.tecnico.bubbledocs.domain;

public class MUL extends MUL_Base {
    
    public MUL(Conteudo arg1,Conteudo arg2) {
        super();
        super.init(arg1,arg2);
    }
    
    @Override
    public int calcula(int arg1,int arg2){
    	return arg1 * arg2;
    }
    
   
    
}
