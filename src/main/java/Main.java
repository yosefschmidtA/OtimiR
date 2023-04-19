import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.w3c.dom.ls.LSOutput;


public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("escreva o nome do arquivo que será lido: ");
        Scanner ler = new Scanner(System.in);
        String nomeArquivo = ler.nextLine();
        System.out.println("escreva o nome do arquivo de saída: ");
        String nomeArquivoSaida = ler.nextLine();
        System.out.println("escreva o nome do arquivo pro python ler");
        String nomearquivopython = ler.nextLine();
        double phiSegundoDomínio = 0;
        try
        {
            System.out.println("Escreva o valor o angulo para adicionar ao novo domínio: ");
            phiSegundoDomínio = ler.nextInt();
        }catch(ArrayIndexOutOfBoundsException exc)
        {
            System.out.println(exc);
        }
        int contador = -26;

        double resultadoFinal1 = 0d;
        double resultadoFinal2 = 0d;
        double resultadoFinal3 = 0d;
        double resultadoFinal4 = 0d;
        double resultadoFinal1ND = 0d;
        double resultadoFinal2ND = 0d;
        double resultadoFinal3ND = 0d;
        double resultadoFinal4ND = 0d;
        double resultadoFinalT1 = 0d;
        double resultadoFinalT2 = 0d;
        double Rtotal = 0d;
        List<Double> listaCoef = new ArrayList<>();
        int nTotal=0;
        List<Double> resultados = new ArrayList<>();
        List<Double> angulos = new ArrayList<>();
        List<Double> coluna4 = new ArrayList<>();
        List<Double> coluna5 = new ArrayList<>();
        List<Double> novaColuna4 = new ArrayList<>();
        List<Double> novaColuna5 = new ArrayList<>();
        List<Double> colunaTotal1 = new ArrayList<>();
        List<Double> colunaTotal2 = new ArrayList<>();
        List<Double> novaColunaTotal1N = new ArrayList<>();
        List<Double> novaColunaTotal2N = new ArrayList<>();
        List<Double> novaColunaTotal1ND = new ArrayList<>();
        List<Double> novaColunaTotal2ND = new ArrayList<>();
        List<Double> angulosPhi = new ArrayList<>();
        List<Double> coefficientsT = new ArrayList<>();
        Double somatório1=0d;
        Double somatório2=0d;
        double coeficiente1=0.9d;
        double coeficiente2=0.1d;
        FileWriter arquivop = new FileWriter("/home/user/Documents/"+nomearquivopython);
        PrintWriter gravarArquivo2 = new PrintWriter(arquivop);

        Scanner scannerNew = new Scanner(new FileReader(nomeArquivo));

        while (scannerNew.hasNextLine()) {
            String linhaAtual = scannerNew.nextLine();
            linhaAtual = linhaAtual.trim().replaceAll("\\s+", "¥");


            String[] linha = linhaAtual.split("¥");
            WeightedObservedPoints points1 = new WeightedObservedPoints();
            WeightedObservedPoints points2 = new WeightedObservedPoints();

            if (contador == 0)
                angulos.add(Double.parseDouble(linha[3]));


            if (contador > 0) {
                if (linha.length == 5) {
                    angulosPhi.add(Double.parseDouble(linha[0]));
                    coluna4.add(Double.parseDouble(linha[3]));
                    coluna5.add(Double.parseDouble(linha[4]));
                    colunaTotal1.add(Double.parseDouble(linha[3]));
                    colunaTotal2.add(Double.parseDouble(linha[4]));

                }
                for (int i=0; i< colunaTotal1.size(); i++)
                {
                    somatório1=somatório1 + Math.abs(colunaTotal1.get(i));
                    somatório2=somatório2 + Math.abs(colunaTotal2.get(i));
                }

                if (linha.length == 7 || (Objects.equals(linha[0], "fitted"))) {
                    Double maxValueColuna4 = 0d;
                    Double maxValueColuna5 = 0d;


                    for (Double double1 : coluna4) {
                        if(double1 < 0){
                           Math.abs(double1);
                        }
                        if (double1 > maxValueColuna4)
                            maxValueColuna4 = double1;
                    }
                    for (Double double1 : coluna5) {
                        if(double1 < 0){
                            Math.abs(double1);
                        }
                        if (double1 > maxValueColuna5)
                            maxValueColuna5 = double1;
                    }
                    for (Double value : coluna4) {
                        Double auxiliar = value/somatório1;
                        novaColuna4.add(auxiliar);

                    }
                    for (Double aDouble : coluna5) {
                        Double auxiliar = aDouble/somatório2;
                        novaColuna5.add(auxiliar);

                    }

                    for(int i = 0; i<novaColuna4.size(); i++) {
                            resultadoFinal1 += (novaColuna4.get(i)-novaColuna5.get(i))*(novaColuna4.get(i)-novaColuna5.get(i));
                            resultadoFinal2 += ((novaColuna4.get(i)*novaColuna4.get(i))+(novaColuna5.get(i)*novaColuna5.get(i)));

                    }

                    resultadoFinal3 = resultadoFinal1/resultadoFinal2;
                    resultadoFinal4 += resultadoFinal3;

                    resultados.add(resultadoFinal3);

                    for(int i=0; i<coluna4.size(); i++)
                    {
                        points1.add(angulosPhi.get(i),coluna4.get(i));
                    }
                    PolynomialCurveFitter fitador = PolynomialCurveFitter.create(4);
                    double[] coefficients = fitador.fit(points1.toList());
                    for(double i: coefficients)
                    {
                        coefficientsT.add(i);
                    }
                    for (int i=0; i<coluna4.size();i++)
                    {
                        gravarArquivo2.printf(angulosPhi.get(i)+","+coluna4.get(i)+","+coluna5.get(i)+"%n");

                    }
                    gravarArquivo2.printf("%n");
                    gravarArquivo2.flush();



                    coluna4.clear();
                    coluna5.clear();
                    novaColuna4.clear();
                    novaColuna5.clear();
                    angulosPhi.clear();

                    contador = 0;

                    if (Objects.equals(linha[0], "fitted"))
                        break;

                    resultadoFinal1 = 0;
                    resultadoFinal2 = 0;
                    angulos.add(Double.parseDouble(linha[3]));
                }
            }
            coeficiente1+=-0.1;
            coeficiente2+=0.1;
            contador++;
        }
        for (int i=0; i<coefficientsT.size(); i++)
        {
            System.out.println(coefficientsT.get(i));
        }
        XYSeries series = new XYSeries(("Polynomial"));
        for(double x = 0; x<=300; x+=0.01)
        {
            double y = coefficientsT.get(0) + coefficientsT.get(1)*x +coefficientsT.get(2)*Math.pow(x,2)+ coefficientsT.get(3)*Math.pow(x,3)+ coefficientsT.get(4)*Math.pow(x,4);
            series.add(x,y);
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Polynomial chart","phi angles","intensity",dataset,PlotOrientation.VERTICAL,true,true,false
        );
        ChartFrame frame = new ChartFrame("Polynomial Chart",chart);
        frame.setVisible(true);
        frame.setSize(800,600);
        for (Double auxiliar : colunaTotal1)
        {
            auxiliar= auxiliar/somatório1;
            novaColunaTotal1N.add(auxiliar);
        }
        for (Double auxiliar : colunaTotal2)
        {
            auxiliar= auxiliar/somatório2;
            novaColunaTotal2N.add(auxiliar);
        }
        for (int i=0; i<novaColunaTotal1N.size(); i++)
        {
            resultadoFinalT1+= ((novaColunaTotal1N.get(i)-novaColunaTotal2N.get(i))*(novaColunaTotal1N.get(i)-novaColunaTotal2N.get(i)));
            resultadoFinalT2+= ((novaColunaTotal1N.get(i)*novaColunaTotal1N.get(i))+(novaColunaTotal2N.get(i)* novaColunaTotal2N.get(i)));
        }


        Rtotal=resultadoFinalT1/resultadoFinalT2;

        FileWriter arquivo = new FileWriter("/home/user/Documents/"+nomeArquivoSaida);
        PrintWriter gravarArquivo = new PrintWriter(arquivo);

        for (int i = 0; i < resultados.size(); i++) {
            gravarArquivo.printf("       " + "Ângulo: " + angulos.get(i) + "  " + "R : " + resultados.get(i) + "%n");
            nTotal++;
            gravarArquivo.printf("-----------------------------------------%n");
        }
        gravarArquivo.printf("       "+"Rm = "+resultadoFinal4/nTotal);
        gravarArquivo.printf("        "+"Rfactor = "+ Rtotal);
        arquivo.close();
        gravarArquivo2.close();
        }

    }
