package org.neuroph.netbeans.visual;

import org.neuroph.netbeans.visual.widgets.NeuralNetworkScene;
import java.awt.BorderLayout;
import java.io.IOException;
import javax.swing.JComponent;
import org.netbeans.spi.palette.PaletteController;
import org.netbeans.spi.palette.PaletteFactory;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.netbeans.visual.palette.NeurophDnDHandler;
import org.neuroph.netbeans.visual.palette.PaletteSupport;
import org.neuroph.util.benchmark.MyBenchmarkTask;
import org.openide.loaders.DataObject;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 * Top component which displays visual neural network editor.
 */
public final class VisualEditorTopComponent extends TopComponent {

    /**
     * path to the icon used by the component and its open action
     */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";
    private static final String PREFERRED_ID = "VisualEditorTopComponent";
    private JComponent view;
    private NeuralNetwork nnet;
    NeuralNetworkScene scene;
    PaletteController palette;
    InstanceContent content;
    AbstractLookup aLookup;
  //  SaveCookie saveCookie;    
    DataObject neuralNetDataObject;
    NeuralNetAndDataSet neuralNetAndDataSet;

    public InstanceContent getContent() {
        return content;
    }

    
    public VisualEditorTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(VisualEditorTopComponent.class, "CTL_GraphViewTopComponent"));
        setToolTipText(NbBundle.getMessage(VisualEditorTopComponent.class, "HINT_GraphViewTopComponent"));
    }

    public VisualEditorTopComponent(DataObject neuralNetDataObject) {
        this.neuralNetDataObject = neuralNetDataObject;
        this.nnet = neuralNetDataObject.getLookup().lookup(NeuralNetwork.class);
       // this.saveCookie = neuralNetDataObject.getLookup().lookup(SaveCookie.class);             
        initComponents();
        setName(nnet.getLabel());
        setToolTipText(NbBundle.getMessage(VisualEditorTopComponent.class, "HINT_GraphViewTopComponent"));
//        setIcon(ImageUtilities.loadImage(ICON_PATH, true));
        
        scene = new NeuralNetworkScene(this.nnet);
        scene.setTopComponent(this);
        view = scene.createView();

        viewPane.setViewportView(view);
        add(scene.createSatelliteView(), BorderLayout.WEST);

        scene.visualizeNetwork();

        content = new InstanceContent();
//      //  content.add(nnet);
//        content.add(saveCookie);

        aLookup = new AbstractLookup(content);
        try {
            //palette = PaletteSupport.createPalette();
            // http://bits.netbeans.org/dev/javadoc/org-netbeans-spi-palette/architecture-summary.html
            palette = PaletteFactory.createPalette("NeurophPalette", new PaletteSupport.MyPaletteActions(), null, new NeurophDnDHandler());
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

    }

    @Override
    public Lookup getLookup() {
        return new ProxyLookup(
                new Lookup[]{
                    scene.getLookup(),
                    Lookups.singleton(palette),
                    neuralNetDataObject.getLookup(),
                    aLookup
                });
            }

    public NeuralNetwork getNeuralNetwork() {
        return nnet;
    }

//    public void setNeuralNetwork(NeuralNetwork nnet) {
//        this.nnet = nnet;
//        scene.visualizeNetwork(nnet);
//
//    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        viewPane = new javax.swing.JScrollPane();

        setLayout(new java.awt.BorderLayout());
        add(viewPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane viewPane;
    // End of variables declaration//GEN-END:variables

    /**
     * Obtain the GraphViewTopComponent instance. Never call {@link #getDefault}
     * directly!
     */
//    public static synchronized GraphViewTopComponent findInstance() {
//        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
//        if (win == null) {
//            Logger.getLogger(GraphViewTopComponent.class.getName()).warning(
//                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
//            return getDefault();
//        }
//        if (win instanceof GraphViewTopComponent) {
//            return (GraphViewTopComponent) win;
//        }
//        Logger.getLogger(GraphViewTopComponent.class.getName()).warning(
//                "There seem to be multiple components with the '" + PREFERRED_ID
//                + "' ID. That is a potential source of errors and unexpected behavior.");
//        return getDefault();
//    }
    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
        //neuralNetDataObject =  Utilities.actionsGlobalContext().lookup(DataObject.class);

    }
    

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing - cleanup stuff...
    }


    @Override
    protected void componentActivated() {
        super.componentActivated(); //To change body of generated methods, choose Tools | Templates.
      
        DataSet dataSet = scene.getDataSet();
        if (dataSet!=null) {
            neuralNetAndDataSet= new NeuralNetAndDataSet(nnet, dataSet);                
            content.add(neuralNetAndDataSet);
            content.add(new TrainingController(neuralNetAndDataSet));
        }                       
    }

    @Override
    protected void componentDeactivated() {
        super.componentDeactivated(); //To change body of generated methods, choose Tools | Templates.
        if (neuralNetAndDataSet!=null) {
            content.remove(neuralNetAndDataSet);
        }
    }
    
    
    
    

    private void readPropertiesImpl(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    public void refresh() {
        scene.refresh();
    }

    
}
