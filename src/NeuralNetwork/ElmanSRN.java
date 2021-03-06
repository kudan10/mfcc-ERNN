/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralNetwork;
import java.text.DecimalFormat;

/**
 *
 * @author DAN
 */
public class ElmanSRN {
    private int TIMESTEP;
    private int INPUT_NEURON = 12;
    private int HIDDEN_NEURON = 15;
    private int OUTPUT_NEURON = 10;
    private double LEARNING_RATE;
    private int JUMLAH_EPOCH;
    private int JUMLAH_SAMPLE_TRAINING;

    //private double[][] dataTraining = {{0,0,1}, {1,0,1}, {0,1,0}, {0,1,0}};
    private double[][] dataTraining = new double[TIMESTEP][INPUT_NEURON];
    private double[] dataTarget = new double[OUTPUT_NEURON];

    private double[][][] sampleDataTraining;
    private double[][] sampleDataTarget;

    private double[][][] sampleTes = {
            {{1,0,0,1,1,0,1,1,1,0,0,0}, {1,0,0,0,0,0,1,0,0,1,0,1}, {1,0,0,1,0,0,0,0,0,0,0,0}, {1,0,0,0,0,0,1,0,0,1,0,0}, {1,0,0,1,0,1,0,0,0,0,0,0}},
            {{5,0,0,0,0,0,0,0,0,0,0,0}, {1,0,0,0,0,0,0,0,0,1,0,1}, {1,0,0,0,0,0,0,0,0,0,0,0}, {1,0,0,0,0,0,1,0,0,1,0,0}, {0,0,0,1,0,1,0,0,0,0,0,0}},
            {{2,0,0,1,1,0,1,1,1,0,0,0}, {1,0,0,0,0,0,1,0,0,1,0,1}, {1,0,0,1,0,0,0,0,0,0,0,0}, {1,0,0,0,0,0,1,0,0,1,0,0}, {1,1,0,1,0,1,0,0,0,0,0,0}},
            {{3,0,0,1,1,0,1,1,1,0,0,0}, {1,0,0,0,0,0,1,0,0,1,0,1}, {1,0,0,1,0,0,0,0,0,0,0,0}, {1,0,0,0,0,0,1,0,0,1,0,0}, {1,1,0,1,0,1,0,0,0,0,0,0}},
            {{6,0,0,1,1,0,1,1,1,0,0,0}, {1,0,0,0,0,0,1,0,0,1,0,1}, {1,0,0,1,0,0,0,0,0,0,0,0}, {1,0,0,0,0,0,1,0,0,1,0,0}, {1,0,1,1,0,1,0,0,0,0,0,0}},
            {{9,0,0,1,1,0,1,1,1,0,0,0}, {1,0,0,0,0,0,1,0,0,1,0,1}, {1,0,0,1,0,0,0,0,0,0,0,0}, {1,0,0,0,0,0,1,0,0,1,0,0}, {1,1,1,1,0,1,0,0,0,0,0,0}}
    };

    private double[][][] bobotTIH = new double[TIMESTEP][INPUT_NEURON][HIDDEN_NEURON];
    private double[][][] bobotTCH = new double[TIMESTEP][HIDDEN_NEURON][HIDDEN_NEURON];
    private double[][] bobotHO = new double[HIDDEN_NEURON][OUTPUT_NEURON];
    private double[][] bobotBiasTIH = new double[TIMESTEP][HIDDEN_NEURON];
    private double[] bobotBiasHO = new double[OUTPUT_NEURON];

    private double[][] aktifHiddenT = new double[TIMESTEP][HIDDEN_NEURON];
    private double[][] contextTC = new double[TIMESTEP][HIDDEN_NEURON]; //TC = Timestep, Context
    private double[] netOutput = new double[OUTPUT_NEURON];
    private double[] aktifOutput = new double[OUTPUT_NEURON];
    private double[] oGrad = new double[OUTPUT_NEURON];

    private double[][] thGrad = new double[TIMESTEP][HIDDEN_NEURON];

    public ElmanSRN(int TIMESTEP, double LEARNING_RATE, int JUMLAH_EPOCH, int JUMLAH_SAMPLE_TRAINING, double[][][] sampleDataTraining, double[][] sampleDataTarget) {
        this.TIMESTEP = TIMESTEP;
        this.LEARNING_RATE = LEARNING_RATE;
        this.JUMLAH_EPOCH = JUMLAH_EPOCH;
        this.JUMLAH_SAMPLE_TRAINING = JUMLAH_SAMPLE_TRAINING;
        this.sampleDataTraining = sampleDataTraining;
        this.sampleDataTarget = sampleDataTarget;
        
    }




    
    
    private void generateBobot(){
        DecimalFormat formatTiga = new DecimalFormat("#.###");

        for (int t = 0; t < TIMESTEP; t++) {
            double binput = 0.09;
            double bcontext = 0.04;
            double bbias = 0.15;

            for (int i = 0; i < INPUT_NEURON; i++) {
                for (int h = 0; h < HIDDEN_NEURON; h++) {
                    binput = binput + 0.001;
                    bobotTIH[t][i][h] = binput;
                    System.out.print(t+"=="+i+"=="+h);
                }
                //System.out.println();
            }

            //bobot bias input-hidden (untuk setiap timestep)
            for (int h = 0; h < HIDDEN_NEURON; h++) {
                bobotBiasTIH[t][h] = bbias + (h * 0.001);
            }

            for (int c = 0; c < HIDDEN_NEURON; c++) {
                for (int h = 0; h < HIDDEN_NEURON; h++) {
                    bcontext = bcontext + 0.001;
                    bobotTCH[t][c][h] = bcontext;
                }
            }
        }

        //bobot hidden->output
        double awal = 0.09;
        for (int h = 0; h < HIDDEN_NEURON; h++) {
            for (int o = 0; o < OUTPUT_NEURON; o++) {
                awal = awal + 0.001;
                bobotHO[h][o] = awal;
            }
        }

        //bobot bias hidden -> output (untuk setiap timestep)
        double bbias = 0.15;
        for (int o = 0; o < OUTPUT_NEURON; o++) {
            bobotBiasHO[o] = bbias + (o * 0.001);
        }


    }

    private void feedForward(){
        for (int t = 0; t < TIMESTEP; t++) {
//            System.out.println("TIMESTEP "+t);
            for (int h = 0; h < HIDDEN_NEURON; h++) {
//                System.out.println(t+" \t HIDDEN "+h);

                //input->hidden
                double sumInput = 0.0;
                for (int i = 0; i < INPUT_NEURON; i++) {
                    sumInput = sumInput + (dataTraining[t][i] * bobotTIH[t][i][h]);
                }

                //context->hidden
                double sumContext = 0.0;
                for (int c = 0; c < HIDDEN_NEURON; c++) {
                    sumContext = sumContext + (contextTC[t][c] * bobotTCH[t][c][h]);
                }

                if (t == TIMESTEP-1){
                    //hitung hidden terakhir
                    aktifHiddenT[t][h] = Math.tanh(sumInput + sumContext + bobotBiasTIH[t][h]);
                }else{
                    aktifHiddenT[t][h] = Math.tanh(sumInput + sumContext + bobotBiasTIH[t][h]);
                    contextTC[t+1][h] = aktifHiddenT[t][h];
//                    System.out.println(aktifHiddenT[t][h] +"  |  "+ contextTC[t+1][h]);
                }
            }

        }

        //hitung output
        for (int o = 0; o < OUTPUT_NEURON; o++) {
            double sum = 0.0;
            for (int h = 0; h < HIDDEN_NEURON; h++) {
                sum = sum + (aktifHiddenT[TIMESTEP-1][h] * bobotHO[h][o]);
            }
            netOutput[o] = sum + bobotBiasHO[o];
        }

        //menghitung softmax
        System.out.print("Hasil Output : ");
        for (int o = 0; o < OUTPUT_NEURON; o++) {
            aktifOutput[o] = softmax(netOutput[o]);
            System.out.print(aktifOutput[o]+" \t");
        }
        System.out.println();

        hitungError();
    }

    private void hitungError(){
        //HITUNG ERROR OUTPUT
        for (int o = 0; o < OUTPUT_NEURON; o++) {
            //hitung derivative
            oGrad[o] = (aktifOutput[o] * (1-aktifOutput[o])) * (dataTarget[o] - aktifOutput[o]) ;
//            System.out.println(oGrad[o]);
        }

        //HITUNG ERROR HIDDEN PADA TIMESTEP
        for (int t = TIMESTEP-1; t >= 0; t--) {
//            System.out.println("TIMESTEP KE- "+ t);
            double derivative = 0.0;
            for (int h = 0; h < HIDDEN_NEURON; h++) {
                double sum = 0.0;
//                System.out.print("HIDDEN (T, H) "+t+", "+h+" \t");

                if (t == TIMESTEP-1){
                    //derivative
                    derivative = (1 - aktifHiddenT[t][h]) * (1 + aktifHiddenT[t][h]);
                    //sum
                    for (int o = 0; o < OUTPUT_NEURON; o++) {
                        sum = sum + (oGrad[o] * bobotHO[h][o]);
                    }
                    thGrad[t][h] = derivative * sum;
//                    System.out.println(thGrad[t][h]);
                }else{
                    //derivative
                    derivative = (1 - aktifHiddenT[t][h]) * (1 + aktifHiddenT[t][h]);
                    //sum
                    for (int c = 0; c < HIDDEN_NEURON; c++) {
                        sum = sum + (thGrad[t+1][c] * bobotTCH[t+1][h][c]);
                    }
                    thGrad[t][h] = sum * derivative;
//                    System.out.print(thGrad[t][h]);
//                    System.out.println();
                }
            }
        }
    }

    private void hitungDelta(){
        DecimalFormat formatTiga = new DecimalFormat("#.####### ##");

        System.out.println("============ HITUNG DELTA & UPDATE BOBOT ====================");
        for (int t = 0; t < TIMESTEP; t++) {
            System.out.println("TIMESTEP "+t);
            //INPUT -> HIDDEN
            for (int i = 0; i < INPUT_NEURON; i++) {
                for (int h = 0; h < HIDDEN_NEURON; h++) {
                    double delta = 0.0;
                    delta = LEARNING_RATE * dataTraining[t][i] * thGrad[t][h];
                    bobotTIH[t][i][h] = delta + bobotTIH[t][i][h];
//                    System.out.print(formatTiga.format(bobotTIH[t][i][h])+" \t");
                }
//                System.out.println();
            }

            //HIDDEN -> OUTPUT
            for (int c = 0; c < HIDDEN_NEURON; c++) {
                for (int h = 0; h < HIDDEN_NEURON; h++) {
                    double delta = 0.0;
                    delta = LEARNING_RATE * contextTC[t][c] * thGrad[t][h];
                    bobotTCH[t][c][h] = delta + bobotTCH[t][c][h];
//                    System.out.print(formatTiga.format(bobotTCH[t][c][h])+" \t");
                }
//                System.out.println();
            }

            //hitung delta untuk setiap bias pada timestep -> hidden
            for (int h = 0; h < HIDDEN_NEURON; h++) {
                double delta = 0.0;
                delta = LEARNING_RATE * 1 * thGrad[t][h];
                bobotBiasTIH[t][h] = delta + bobotBiasTIH[t][h];
            }
        }

        //update bobot pada HIDDEN -> OUTPUT
        for (int h = 0; h < HIDDEN_NEURON; h++) {
            for (int o = 0; o < OUTPUT_NEURON; o++) {
                double delta = 0.0;
                delta = LEARNING_RATE * aktifHiddenT[TIMESTEP-1][h] * oGrad[o];

                //update bobot
                bobotHO[h][o] = delta + bobotHO[h][o];
            }
        }

        //update bobot bias pada HIDDEN -> OUTPUT
        for (int o = 0; o < OUTPUT_NEURON; o++) {
            double delta = 0.0;
            delta = LEARNING_RATE * 1 * oGrad[o];
            bobotBiasHO[o] = delta + bobotBiasHO[o];
        }
    }

    private double softmax(double val){
        double totalExp = 0.0;
        for (int o = 0; o < OUTPUT_NEURON; o++) {
            totalExp = totalExp + Math.exp(netOutput[o]);
        }
        return Math.exp(val)/totalExp;
    }

    private int maximum(final double[] vector)
    {
        // This function returns the index of the maximum of vector().
        int indeksMax = 0;
        double max = vector[indeksMax];

        for(int index = 0; index < OUTPUT_NEURON; index++)
        {
            if(vector[index] > max){
                max = vector[index];
                indeksMax = index;
            }
        }
        return indeksMax;
    }

    public void main(){
        neuralNetwork();
    }

    public void neuralNetwork(){
        int sample = 0; // (indeks/pointer) data sample yang akan dilatih oleh ERNN
        generateBobot();

        for (int epoch = 0; epoch< JUMLAH_EPOCH; epoch++) {
            //adjusting the pointer of input data sample
            if (sample == sampleDataTarget.length){
                sample = 0;
            }

            //do the training process
            System.out.println(sample);

            //select the data input to train
            for (int j = 0; j < TIMESTEP; j++) {
                for (int k = 0; k < INPUT_NEURON; k++) {
//                    dataTraining[j][k] = sampleDataTraining[sample][j][k];
//                    System.out.println(sample+" === "+j+" === "+k);
                    System.out.println(sampleDataTraining[0][0][0]);
                }
            }

            //select the output pattern for related data sample
            for (int j = 0; j < OUTPUT_NEURON; j++) {
                dataTarget[j] = sampleDataTarget[sample][j];
            }

            feedForward();
            hitungDelta();

            sample++; //add the pointer for input data sample by 1
        }

        //hitung akurasi dari hasil pelatihan terhadap dataTraining (getTrainingState)
        getTrainingState();

        //test dengan data uji
//        testDenganDataUji();
    }


    public void getTrainingState(){
        double sum = 0.0;
        for (int i = 0; i < JUMLAH_SAMPLE_TRAINING; i++) {

            //masukkan sample input pada data training pada setiap timestep ke tempatnya
            for (int j = 0; j < TIMESTEP; j++) {
                for (int k = 0; k < INPUT_NEURON; k++) {
                    dataTraining[j][k] = sampleDataTraining[i][j][k];
                }
            }

            //masukkan output yang diharapkan dari sample data input tersebut
            for (int j = 0; j < OUTPUT_NEURON; j++) {
                dataTarget[j] = sampleDataTarget[i][j];
            }

            //lakukan feedforward (pengujian)
            feedForward();

            //hitung akurasi yang dihasilkan terhadap data latih
            if(maximum(aktifOutput) == maximum(dataTarget)){
                sum += 1;
                for (int j = 0; j < dataTraining.length; j++) {
                    for (int k = 0; k < dataTraining[0].length; k++) {
                        System.out.print(dataTraining[j][k]+"\t");
                    }
                }

                System.out.println("\n"+maximum(aktifOutput) + "\t" + maximum(dataTarget));
            }else{
                System.out.println("ini data yang salah dikenali");
                for (int j = 0; j < dataTraining.length; j++) {
                    for (int k = 0; k < dataTraining[0].length; k++) {
                        System.out.print(dataTraining[j][k]+"\t");
                    }
                }
                System.out.println("\n"+maximum(aktifOutput) + "\t" + maximum(dataTarget));
            }
        }

        System.out.println("Network is " + ((double)sum / (double)JUMLAH_SAMPLE_TRAINING * 100.0) + "% correct.");
    }

    public void testDenganDataUji(){
        for (int i = 0; i < JUMLAH_SAMPLE_TRAINING; i++) {
            //masukkan sample input pada data training pada setiap timestep ke tempatnya
            for (int j = 0; j < TIMESTEP; j++) {
                for (int k = 0; k < INPUT_NEURON; k++) {
                    dataTraining[j][k] = sampleTes[i][j][k];
                }
            }

            //lakukan feedforward (pengujian)
            feedForward();

            //tampilkan hasil pengenalan
            for (int j = 0; j < TIMESTEP; j++) {
                for (int k = 0; k < INPUT_NEURON; k++) {
                    System.out.print(dataTraining[j][k]+"\t");;
                }
            }

            System.out.print("Output: " + maximum(aktifOutput) + "\n");
        }
    }

}
