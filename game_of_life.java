import java.util.Random;
public class Juego_vida{
    private char[][] caracteres;
    private int fila;
    private int columna;
    private char[][] modificado;
    Random r = new Random();
    Juego_vida(int fila_1,int columna_1){
    fila=fila_1;
    columna=columna_1;
    caracteres=new char[fila][columna];
    modificado=new char[fila][columna];
    inicializa_arreglo_original();
    while(true){
        try{
        imprime_arreglo(caracteres);//imprime nuevo arreglo original
        encontrar_vecinos(caracteres);//aplica las reglas y manda a llamar subrutina para detectar vecinos
        copia(caracteres,modificado);//copia el arreglo buffer al arreglo original
        Thread.sleep(400);//se espera 400 milisegundos entre cada iteración
        final String ANSI_CLS = "\u001b[2J";//caracter ASCII de escape
        System.out.print(ANSI_CLS);
        System.out.flush();
        System.out.print("\n");
        }
        catch(Exception e){
        System.out.print("Not possible");
        }
    }
    }
    private void inicializa_arreglo_original(){
    for(int i = 0; i<fila;i++){
        for (int j = 0;j<columna;j++){
        caracteres[i][j]=' ';    
        if(j>2 && j<columna-3 && i<fila-3 && i>2){
       	//dejar espacios en las esquinas suoerior,inferior, izquierda y derecha
            if(r.nextInt()<5)
            caracteres[i][j]='*';
        }
 
        }
    }
    }
    private void copia(char[][] arreglo1,char[][] arreglo2){//copiar de una matriz a otra
    for(int f=0;f<fila;f++){
        for(int c=0;c<columna;c++){
        arreglo1[f][c]=arreglo2[f][c];
        }
    }
    }
    private void imprime_arreglo(char[][] arreglo){
    for(int row=0;row<fila;row++){
        for(int col=0;col<columna;col++){
        System.out.print(arreglo[row][col]);
        }
        System.out.print("\n");
    }
    }
 
    private int neighbours(int posX,int posY,char [][] arreglo)
    {
    int vecino=0;
    for( int row = posX-1 ; row <= posX + 1; row++) // buscar vecinos desde(fila-1,comumna-1) hasta (fila+1,columna+1)
        {
 
        for(int col =  posY-1 ;  col <= posY + 1; col++)
            {
            if(row<fila && col<columna && row >= 0 && col >= 0 && (posX != row ||  posY != col) )
               //verificar que posicion de celda sea diferente a la original.
                {
                if(arreglo[row][col]=='*'){
                    vecino++;
                }
                }
            }
        }
    return vecino;
    }
    private void encontrar_vecinos(char[][] array){
    for(int fil=0;fil<fila;fil++){
        for(int colum=0;colum<columna;colum++){
        Regla(fil,colum,neighbours(fil,colum,array));//aplica subrutina para encontrar vecinos y aplicar regla a cada una de las celdas
        }
    }
    }
    private void Regla(int i,int j,int vecinos){
    if(caracteres[i][j]=='*'){
        if(vecinos>1 && vecinos<=3){
        modificado[i][j]='*';
        }
        if(vecinos<2 || vecinos>3){
        modificado[i][j]=' ';
        }
    }
    else if(caracteres[i][j]==' ' && vecinos==3){
        modificado[i][j]='*';
    }
    else{
           modificado[i][j]=' ';
    }
    }
 
    public static void main(String algo[]) {
    new Juego_vida(Integer.parseInt(algo[0]),Integer.parseInt(algo[1]));
    }
}
