package pt.tecnico.bubbledocs;

public class ADD extends ADD_Base {
    
    public ADD(Conteudo arg1,Conteudo arg2) {
        super();
        super.init(arg1, arg2);
    }
    
    @Override
    public int calcula(int arg1,int arg2){
    	return arg1 + arg2;
    }
    
}
