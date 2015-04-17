# Projecto de Sistemas Distribuídos #

## Primeira entrega ##

Grupo de SD Nº 3

André Pires 76046 pardal.pires@gmail.com

Miguel Cruz 76102 slf450x@hotmail.com

Mauro Teles 70200 mdcateles@gmail.com

Repositório:
[tecnico-softeng-distsys-2015/A_15_03_17-project](https://github.com/tecnico-softeng-distsys-2015/A_15_03_17-project/)

-------------------------------------------------------------------------------

## Serviço SD-ID 

### Instruções de instalação 
*(Como colocar o projecto a funcionar numa máquina do laboratório)*

[0] Iniciar sistema operativo Linux

[1] Iniciar servidores de apoio

JUDDI:
> $ cd ~
> $ wget http://disciplinas.tecnico.ulisboa.pt/leic-sod/2014-2015/download/juddi-3.2.1_tomcat-7.0.57_port-8081.zip
> $ unzip juddi-3.2.1_tomcat-7.0.57_port-8081.zip
> $ export CATALINA_HOME=/afs/.ist.utl.pt/users/0/9/ist1*****/juddi-3.2.1_tomcat-7.0.57_port-8081
> $ export PATH=${PATH}:${CATALINA_HOME}/bin
> $ chmod +x $CATALINA_HOME/bin/*.sh
> $ ./juddi-3.2.1_tomcat-7.0.57_port-8081/bin/startup.sh

[2] Criar pasta temporária

> cd ~
> mkdir project

[3] Obter versão entregue

> git clone https://github.com/tecnico-softeng-distsys-2015/A_15_03_17-project
> git checkout tags/R_2


[4] Construir e executar **servidor**

> cd ~/project/C_15_03_17-project/sd-id
> mvn clean test
> mvn exec:java


[5] Construir **cliente**

> cd ~/project/C_15_03_17-project/sd-id-cli
> mvn clean package


-------------------------------------------------------------------------------

### Instruções de teste: ###
*(Como verificar que todas as funcionalidades estão a funcionar correctamente)*


[1] Executar **cliente de testes** ...

> cd sd-id-cli
> mvn test


[2] Executar

> mvn exec:java

-------------------------------------------------------------------------------
**FIM**