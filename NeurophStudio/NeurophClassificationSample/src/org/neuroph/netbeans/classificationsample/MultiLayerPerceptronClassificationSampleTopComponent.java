package org.neuroph.netbeans.classificationsample;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.netbeans.api.settings.ConvertAsProperties;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.core.data.DataSet;
import org.neuroph.netbeans.visual.TrainingController;
import org.neuroph.netbeans.visual.NeuralNetAndDataSet;
import org.neuroph.netbeans.project.NeurophProjectFilesFactory;
import org.neuroph.nnet.learning.LMS;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.openide.loaders.DataObject;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//org.neuroph.netbeans.classificationsample.mlperceptron//MultiLayerPerceptronSample//EN",
        autostore = false)
public final class MultiLayerPerceptronClassificationSampleTopComponent extends TopComponent implements LearningEventListener {

    private static MultiLayerPerceptronClassificationSampleTopComponent instance;
    private static final String PREFERRED_ID = "MultiLayerPerceptronSampleTopComponent";
    private Visualization2DPanel inputSpacePanel;
    private MultiLayerPerceptronClassificationSamplePanel controllsPanel;
    private SettingsTopComponent stc;
    private PerceptronSampleTrainingSet pst;
    private DataSet trainingSet;
    private int tsCount = 0;
    private NeuralNetwork neuralNetwork;
    private NeuralNetAndDataSet neuralNetAndDataSet;
    private TrainingController trainingController;
    private Thread firstCalculation = null;
    private int iterationCounter = 0;
    private InstanceContent content;
    private AbstractLookup aLookup;
    private DropTargetListener dtListener;
    private DropTarget dropTarget;
    private int acceptableActions = DnDConstants.ACTION_COPY;
    private boolean trainSignal = false;
    private ArrayList<Double> setValues;
    private ArrayList<Double[]> neuralNetworkInputs;

    public MultiLayerPerceptronClassificationSampleTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(MultiLayerPerceptronClassificationSampleTopComponent.class, "CTL_MultiLayerPerceptronSampleTopComponent"));
        setToolTipText(NbBundle.getMessage(MultiLayerPerceptronClassificationSampleTopComponent.class, "HINT_MultiLayerPerceptronSampleTopComponent"));
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);
        content = new InstanceContent();
        aLookup = new AbstractLookup(content);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 770, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    /**
     * Gets default instance. Do not use directly: reserved for *.settings files
     * only, i.e. deserialization routines; otherwise you could get a
     * non-deserialized instance. To obtain the singleton instance, use
     * {@link #findInstance}.
     */
    public static synchronized MultiLayerPerceptronClassificationSampleTopComponent getDefault() {
        if (instance == null) {
            instance = new MultiLayerPerceptronClassificationSampleTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the MultiLayerPerceptronClassificationSampleTopComponent instance.
     * Never call {@link #getDefault} directly!
     */
    public static synchronized MultiLayerPerceptronClassificationSampleTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(MultiLayerPerceptronClassificationSampleTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof MultiLayerPerceptronClassificationSampleTopComponent) {
            return (MultiLayerPerceptronClassificationSampleTopComponent) win;
        }
        Logger.getLogger(MultiLayerPerceptronClassificationSampleTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID
                + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    public Lookup getLookup() {
        return new ProxyLookup(new Lookup[]{
            super.getLookup(),
            aLookup
        });
    }

    @Override
    public void componentOpened() {
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    Object readProperties(java.util.Properties p) {
        if (instance == null) {
            instance = this;
        }
        instance.readPropertiesImpl(p);
        return instance;
    }

    private void readPropertiesImpl(java.util.Properties p) {
        String version = p.getProperty("version");
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    public DataSet getTrainingSet() {
        return trainingSet;
    }

    public void setTrainingSet(DataSet trainingSet) {
        this.trainingSet = trainingSet;
    }

    public NeuralNetwork getNeuralNetwork() {
        return neuralNetwork;
    }

    public void setNeuralNetwork(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
    }

    public boolean isTrainSignal() {
        return trainSignal;
    }

    public Visualization2DPanel getInputSpacePanel() {
        return inputSpacePanel;
    }

    public void setInputSpacePanel(Visualization2DPanel inputSpacePanel) {
        this.inputSpacePanel = inputSpacePanel;
    }

    public void setTrainSignal(boolean trainSignal) {
        this.trainSignal = trainSignal;
    }

    public ArrayList<Double[]> getInputs() {
        return neuralNetworkInputs;
    }

    public void setInputs(ArrayList<Double[]> inputs) {
        this.neuralNetworkInputs = inputs;
    }
    private int[] storedInputs;//2 chosen inputs for visualization

    public void setStoredInputs(int[] storedInputs) {
        this.storedInputs = storedInputs;
    }

    public boolean isAllPointsRemoved() {
        return inputSpacePanel.isAllPointsRemoved();
    }

    public boolean isPointDrawed() {
        return inputSpacePanel.isPointDrawed();
    }

    public boolean isDrawingLocked() {
        return inputSpacePanel.isDrawingLocked();
    }

    public void setDrawingLocked(boolean flag) {
        inputSpacePanel.setDrawingLocked(flag);
    }

    public void visualizationPreprocessing() {
        controllsPanel.visualizationPreprocessing();
    }

    public void setVisualizationStarted(boolean flag) {
        inputSpacePanel.setVisualizationStarted(flag);
    }

    /*
     * If point is drawed on panel, this method registers that event
     */
    public void setPointDrawed(boolean drawed) {
        inputSpacePanel.setPointDrawed(drawed);
    }

    public ArrayList<Double> getSetValues() {
        return setValues;
    }

    public void setSetValues(ArrayList<Double> setValues) {
        this.setValues = setValues;
    }

    /*
     * Draws points from dataset, with 2 specified inputs
     */
    public void drawPointsFromTrainingSet(DataSet dataSet, int[] inputs) {
        try {
            inputSpacePanel.setAllPointsRemoved(false);
            inputSpacePanel.drawPointsFromTrainingSet(dataSet, inputs);
        } catch (Exception e) {
        }
    }

    /*
     * Initializes panel regarding coordinate system domain (positive, or positive and negative inputs).
     * 
     */
    public void initializePanel(boolean positiveCoordinates) {
        if (inputSpacePanel != null) {
            this.remove(inputSpacePanel);
        }
        inputSpacePanel = new Visualization2DPanel();
        inputSpacePanel.setPositiveInputsOnly(positiveCoordinates);
        inputSpacePanel.setSize(570, 570);
        add(inputSpacePanel);
        inputSpacePanel.setLocation(0, 0);
        repaint();
    }

    /**
     * Creates new form BackpropagationSample
     */
    public void setTrainingSetForMultiLayerPerceptronSample(PerceptronSampleTrainingSet ps) {
        setSize(770, 600);
        trainingSet = new DataSet(2, 1);
        this.pst = ps;
        stc = SettingsTopComponent.findInstance();
        stc.initializePanel(this);
        controllsPanel = stc.getControllsPanel();
        stc.open();
        initializePanel(false);
        this.dtListener = new DTListener();
        this.dropTarget = new DropTarget(
                this,
                this.acceptableActions,
                this.dtListener,
                true);
    }

    /*
     * Creates neural network file within selected project
     */
    public void createNeuralNetworkFile(NeuralNetwork neuralNetwork) {
        NeurophProjectFilesFactory.getDefault().createNeuralNetworkFile(neuralNetwork);
    }

    /*
     * If custom (manual) dataset is specified, training set and training set name is created
     */
    public void customDataSetCheck() {
        if (inputSpacePanel.isPointDrawed()) {
            trainingSet = inputSpacePanel.getTrainingSet();
            tsCount++;
            trainingSet.setLabel("MlpSampleTrainingSet" + tsCount);
        }
    }

    /*
     * If custom (manual) dataset is specified, training set file is created
     */
    public void sampleTrainingSetFileCheck() {
        if (inputSpacePanel.isPointDrawed()) {
            NeurophProjectFilesFactory.getDefault().createTrainingSetFile(trainingSet);
            inputSpacePanel.setPointDrawed(false);
        }
    }

    /*
     * During training mode, if Show Points check box is selected, redrawing of dataset is required
     */
    public void showPointsOptionCheck() {
        if (MultiLayerPerceptronClassificationSamplePanel.SHOW_POINTS && inputSpacePanel.isAllPointsRemoved()) {
            try {
                inputSpacePanel.setAllPointsRemoved(false);
                drawPointsFromTrainingSet(trainingSet, InputSettngsDialog.getInstance().getStoredInputs());
            } catch (Exception e) {
            }
        }
    }

    /*
     * Collects all the information needed for training neural network
     */
    public void trainingPreprocessing() {
        trainSignal = true;
        neuralNetAndDataSet = new NeuralNetAndDataSet(neuralNetwork, trainingSet);
        trainingController = new TrainingController(neuralNetAndDataSet);
        neuralNetwork.getLearningRule().addListener(this);//adds learning rule to observer
        trainingController.setLmsParams(controllsPanel.getLearningRate(), controllsPanel.getMaxError(), controllsPanel.getMaxIteration());
        LMS learningRule = (LMS) this.neuralNetAndDataSet.getNetwork().getLearningRule();
        if (learningRule instanceof MomentumBackpropagation) {
            ((MomentumBackpropagation) learningRule).setMomentum(controllsPanel.getMomentum());
        }
        getInputSpacePanel().setNeuronColors(neuralNetwork);
    }

    /*
     * Stops training
     */
    public void stop() {
        neuralNetAndDataSet.stopTraining();
    }

    /*
     * Clears all points from panel
     */
    public void clear() {
        inputSpacePanel.clearPoints();
    }

    /*
     * Generates set values from [-k,k] domain, in order to simulate all neural network inputs.
     * This set is later used for generating variations with repetition of class k=numberOfinputs.
     * For each variation (in our case simulated input) we choose exactly 2 inputs for 2D visualization
     */
    public ArrayList<Double> generateSetValues(int size, double coef) {
        setValues = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            double value = 1 - i * coef;
            setValues.add(value);
        }
        return setValues;
    }

    /*
     * Removes neural network and dataset from content
     */
    public void removeNetworkAndDataSetFromContent() {
        try {
            content.remove(neuralNetAndDataSet);
            content.remove(trainingController);
        } catch (Exception ex) {
        }
        MultiLayerPerceptronClassificationSampleTopComponent.this.requestActive();
    }

    /*
     * Regarding selected dataset, this method initializes different coordinate system
     */
    public void coordinateSystemDomainCheck() {
        boolean positive = true;//only positive inputs are detected
        loop:
        for (int i = 0; i < trainingSet.size(); i++) {
            double[] inputs = trainingSet.getRowAt(i).getInput();
            for (int j = 0; j < inputs.length; j++) {
                if (inputs[j] < 0) {
                    positive = false;//both positive and negative inputs are detected
                    break loop;
                }
            }
        }
        initializePanel(positive);//panel initialization
        stc.getControllsPanel().setCheckPoints(positive);//updating Swing components 
    }

    /*
     * This method shows the information, eg. current name of dataset and neural network that are used for training
     */
    public void neuralNetworkAndDataSetInformationCheck(NeuralNetwork neuralNetvork, DataSet dataSet) {

        MultiLayerPerceptronClassificationSamplePanel mlp = stc.getControllsPanel();
        if (dataSet != null) {
            if (dataSet.getLabel() != null) {
                mlp.setDataSetInformation(dataSet.getLabel());
            } else {
                mlp.setDataSetInformation("Not selected");
            }
        } else {
            mlp.setDataSetInformation("Not selected");
        }
        if (neuralNetvork != null) {
            if (neuralNetvork.getLabel() != null) {
                mlp.setNeuralNetworkInformation(neuralNetvork.getLabel());
            } else {
                mlp.setNeuralNetworkInformation("Not selected");
            }
        } else {
            mlp.setNeuralNetworkInformation("Not selected");
        }
    }

    /*
     * During neural network training process, at  particular moment, the same process pauses for a moment,
     * and neural network is passed as an function argument.
     * This method simulates all the network inputs in selected domain: [-1,1] or [0,1].
     * These inputs serve as inputs to trained network.
     * Once the output is calculated, through each iteration, panel is updated with all 
     * current outputs, and specific training result is shown on panel.
     * This process repeats itself once the training is completed.
     */
    public void calculateNeuralNetworkAnswer(NeuralNetwork nn) {
        if (nn != null) {
            inputSpacePanel.setNeuralNetwork(nn);
            double initialCoordinate;
            double coefficient = 0.0357142857142857;//1/57=0.0357142857142857
            int size = 56;
            /*
             * Sets parameters either only for positive inputs or positive and negative inputs
             */
            if (inputSpacePanel.positiveInputsOnly()) {
                initialCoordinate = 0.0;
            } else {
                initialCoordinate = 1.0;
            }
            for (int i = 0; i < neuralNetworkInputs.size(); i++) {
                /*
                 * Cannot use Double type as neural network input,
                 * so conversion from Double to double type is required
                 */
                Double[] incompatibleInput = neuralNetworkInputs.get(i);
                double[] input = new double[incompatibleInput.length];
                for (int j = 0; j < input.length; j++) {
                    input[j] = incompatibleInput[j];
                }
                nn.setInput(input);
                nn.calculate();
                double output = nn.getLayerAt(nn.getLayersCount() - 1).getNeuronAt(0).getOutput();
                double xInput = input[storedInputs[0]] + initialCoordinate;
                double yInput = input[storedInputs[1]] + initialCoordinate;
                int x;
                int y;
                /*
                 * Transformation from Descartes' coordintes to panel coordinates
                 */
                if (inputSpacePanel.positiveInputsOnly()) {
                    x = (int) Math.abs(xInput * size);
                    y = size - (int) Math.abs(yInput * size);
                } else {
                    x = (int) Math.abs((xInput) / coefficient);
                    y = size - (int) Math.abs((yInput) / coefficient);
                }
                inputSpacePanel.setGridPoints(x, y, output);
            }
        }
    }

    @Override
    public void handleLearningEvent(LearningEvent le) {
        iterationCounter++;
        if (iterationCounter % 10 == 0) {
            NeuralNetwork nnet = neuralNetAndDataSet.getNetwork();
            nnet.pauseLearning();//pause
            calculateNeuralNetworkAnswer(nnet);//calculating network response and drawing output
            nnet.resumeLearning();//resume
        }
    }

    class DTListener implements DropTargetListener {

        @Override
        public void dragEnter(DropTargetDragEvent dtde) {
            dtde.acceptDrag(dtde.getDropAction());
        }

        @Override
        public void dragExit(DropTargetEvent dte) {
        }

        @Override
        public void dragOver(DropTargetDragEvent dtde) {
            dtde.acceptDrag(dtde.getDropAction());
        }

        @Override
        public void dropActionChanged(DropTargetDragEvent dtde) {
            dtde.acceptDrag(dtde.getDropAction());
        }

        @Override
        public void drop(DropTargetDropEvent e) {
            Transferable t = e.getTransferable();
            DataFlavor dataSetflavor = t.getTransferDataFlavors()[1];
            try {
                DataObject dataObject = (DataObject) t.getTransferData(dataSetflavor);
                DataSet dataSet = dataObject.getLookup().lookup(DataSet.class);//gets the objects from lookup listener
                NeuralNetwork neuralNet = dataObject.getLookup().lookup(NeuralNetwork.class);
                if (dataSet != null) {
                    clear();
                    setPointDrawed(false);
                    getInputSpacePanel().setDrawingLocked(true);
                    trainingSet = dataSet;
                    InputSettngsDialog isd = InputSettngsDialog.getInstance();
                    isd.initializeInformation(trainingSet);
                    isd.setVisible(true);
                    neuralNetworkAndDataSetInformationCheck(getNeuralNetwork(), trainingSet);
                    coordinateSystemDomainCheck();
                    getInputSpacePanel().drawPointsFromTrainingSet(trainingSet, isd.getStoredInputs());
                }
                if (neuralNet != null) {
                    setNeuralNetwork(neuralNet);
                    neuralNetworkAndDataSetInformationCheck(getNeuralNetwork(), trainingSet);
                }
                if ((trainingSet != null) && (getNeuralNetwork() != null)) {
                    removeNetworkAndDataSetFromContent();
                    trainingPreprocessing();
                    content.add(neuralNetAndDataSet);
                    content.add(trainingController);
                    neuralNetworkAndDataSetInformationCheck(getNeuralNetwork(), trainingSet);
                    MultiLayerPerceptronClassificationSampleTopComponent.this.requestActive();
                }
            } catch (UnsupportedFlavorException | IOException ex) {
                Exceptions.printStackTrace(ex);
            }
            e.dropComplete(true);
        }
    }
}
