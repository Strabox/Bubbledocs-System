# Bubble Docs SD-STORE
# Projecto de Sistemas Distribuídos #

## Primeira entrega ##

Grupo de SD 17


André Esteves 64722 andre.neves.esteves@tecnico.ulisboa.pt

Artur Fonseca 75456 artur.fonseca@tecnico.ulisboa.pt

Ricardo Pires 75513 ricardo_pires2@live.com.pt



Repositório:
[tecnico-softeng-distsys-2015/A_15_03_17-project](https://github.com/tecnico-softeng-distsys-2015/A_15_03_17-project/)


-------------------------------------------------------------------------------

## Serviço SD-STORE 


### Instruções de instalação 
*(Como colocar o projecto a funcionar numa máquina do laboratório)*

[0] Iniciar sistema operativo

Linux


[1] Iniciar servidores de apoio

JUDDI:

>  download from http://disciplinas.tecnico.ulisboa.pt/leic-sod/2014-2015/download/juddi-3.2.1_tomcat-7.0.57_port-8081.zip

> unzip

> cd bin

> chmod +x *.sh 

> ./startup.sh

[2] Criar pasta temporária

> mkdir proj

[3] Obter versão entregue

> git clone -b SD-STORE_R_1 https://github.com/tecnico-softeng-distsys-2015/A_15_03_17-project/



[4] Construir e executar **servidor**

> cd sd-store

> mvn clean compile 

> mvn exec:java


[5] Construir **cliente**

> cd sd-store-cli

> mvn clean compile




-------------------------------------------------------------------------------

### Instruções de teste: ###
*(Como verificar que todas as funcionalidades estão a funcionar correctamente)*


[1] Executar **cliente de testes** 

> cd sd-store-cli

> mvn test


[2] Executar **testes do servidor**
> cd sd-store

> mvn test





-------------------------------------------------------------------------------
**FIM**
