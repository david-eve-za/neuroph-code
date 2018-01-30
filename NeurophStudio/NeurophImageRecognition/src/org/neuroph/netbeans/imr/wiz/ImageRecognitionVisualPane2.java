package org.neuroph.netbeans.imr.wiz;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import javax.imageio.ImageIO;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit;
import org.neuroph.netbeans.ide.imageeditor.ImageChangeListener;
import org.neuroph.netbeans.ide.imageeditor.SimpleImageEditor;
import org.neuroph.netbeans.imageexplorer.imagevalidator.ImageFileFilter;
import org.neuroph.netbeans.imageexplorer.imagevalidator.ImageIOFileFilter;
import org.neuroph.netbeans.project.CurrentProject;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.BeanTreeView;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;

/**
 *
 * @author Djordje
 */
public class ImageRecognitionVisualPane2 extends javax.swing.JPanel implements ExplorerManager.Provider, ImageChangeListener {

    private JFileChooser junkDirFileChooser;
    private JFileChooser junkFileChooser;
    private String junkDirPath;
    private String junkFilePath;
    ExplorerManager mgr = new ExplorerManager();
    private JFileChooser junkImageDirFileChooser;
    private JFileChooser junkImageFileChooser;
    private File junkImagesDir;
    private FileObject rootFileObject;
    private DataObject dataObject;
    private Node rootnode;
    ImageIOFileFilter ioFilter = new ImageIOFileFilter();
    ImageFileFilter filter = new ImageFileFilter();
    BufferedImage image;
    Image scaledImage;
    String lookupImagePath;
    private final Lookup lookup;
    private String junkDirectory;

    /** Creates new form ImageRecognitionVisualPane2 */
    public ImageRecognitionVisualPane2() throws IOException {
        initComponents();

        
       try {
            junkDirectory = CurrentProject.getInstance().getCurrentProject().getProjectDirectory().getPath() + "/Images/JunkDir/";
        } catch(NullPointerException exp) {
            JOptionPane.showMessageDialog(this, "Please select project", "Project required", JOptionPane.INFORMATION_MESSAGE);
        }
        
        ((BeanTreeView) jScrollPane1).setRootVisible(false);

        ActionMap map = getActionMap();

        map.put(DefaultEditorKit.copyAction, ExplorerUtils.actionCopy(mgr));
        map.put(DefaultEditorKit.cutAction, ExplorerUtils.actionCut(mgr));
        map.put(DefaultEditorKit.pasteAction, ExplorerUtils.actionPaste(mgr));
        map.put("delete", ExplorerUtils.actionDelete(mgr, true));

        InputMap keys = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        keys.put(KeyStroke.getKeyStroke("control C"), DefaultEditorKit.copyAction);
        keys.put(KeyStroke.getKeyStroke("control X"), DefaultEditorKit.cutAction);
        keys.put(KeyStroke.getKeyStroke("control V"), DefaultEditorKit.pasteAction);
        keys.put(KeyStroke.getKeyStroke("DELETE"), "delete");


        lookup = ExplorerUtils.createLookup(mgr, map);
        Lookup.Result<FileObject> nodes = lookup.lookupResult(FileObject.class);
        nodes.addLookupListener(new LookupListener() {

            public void resultChanged(LookupEvent le) {
                Lookup.Result localresult = (Result) le.getSource();
                Collection<FileObject> coll = localresult.allInstances();
                if (!coll.isEmpty()) {

                    for (Object o : coll) {
                        if (o instanceof FileObject) {
                            FileObject n = (FileObject) o;
                            lookupImagePath = n.getPath();
                        }
                    }
                    try {
                        coll = null;
                        imagePreview(lookupImagePath);
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            }
        });

        CreateJunkImageFolderAndTreeView();
    }

    @Override
    public String getName() {
        return "Select images not to recognize.";
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new BeanTreeView();
        addJunkImageButton = new javax.swing.JButton();
        addJunkDirectoryButton = new javax.swing.JButton();
        junkImagePreviewLabel = new javax.swing.JLabel();
        deleteJunkImageButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        widthLabel = new javax.swing.JLabel();
        heightLabel = new javax.swing.JLabel();

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Choose images that should not be recognized (avoid false recognition)");

        jScrollPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        addJunkImageButton.setText("Add image");
        addJunkImageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addJunkImageButtonActionPerformed(evt);
            }
        });

        addJunkDirectoryButton.setText("Add directory");
        addJunkDirectoryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addJunkDirectoryButtonActionPerformed(evt);
            }
        });

        junkImagePreviewLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        junkImagePreviewLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        deleteJunkImageButton.setText("Delete");
        deleteJunkImageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteJunkImageButtonActionPerformed(evt);
            }
        });

        editButton.setText("Edit image");
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel1)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 265, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(addJunkImageButton)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(addJunkDirectoryButton)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(deleteJunkImageButton)))
                        .add(45, 45, 45)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(editButton)
                            .add(junkImagePreviewLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 265, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jLabel1)
                .add(22, 22, 22)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 219, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(junkImagePreviewLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 219, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(addJunkImageButton)
                    .add(addJunkDirectoryButton)
                    .add(deleteJunkImageButton)
                    .add(editButton))
                .addContainerGap())
        );

        jLabel2.setText("Image Width:");

        jLabel3.setText("Image Height:");

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jLabel2)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(widthLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jLabel3)
                        .add(10, 10, 10)
                        .add(heightLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 43, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(167, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(widthLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(heightLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(layout.createSequentialGroup()
                        .add(320, 320, 320)
                        .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(44, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addJunkImageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addJunkImageButtonActionPerformed
        try {
            chooseJunkImageFile();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }//GEN-LAST:event_addJunkImageButtonActionPerformed

    private void addJunkDirectoryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addJunkDirectoryButtonActionPerformed
        try {
            chooseJunkImageDir();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }//GEN-LAST:event_addJunkDirectoryButtonActionPerformed

    private void deleteJunkImageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteJunkImageButtonActionPerformed
        try {
            deleteImage(lookupImagePath);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        rootFileObject.refresh();
        CreateJunkImageFolderAndTreeView();
    }//GEN-LAST:event_deleteJunkImageButtonActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        SimpleImageEditor imageEditor = new SimpleImageEditor();
        imageEditor.setVisible(true);
        imageEditor.setImage(lookupImagePath);
        imageEditor.setListener(this);
    }//GEN-LAST:event_editButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addJunkDirectoryButton;
    private javax.swing.JButton addJunkImageButton;
    private javax.swing.JButton deleteJunkImageButton;
    private javax.swing.JButton editButton;
    private javax.swing.JLabel heightLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel junkImagePreviewLabel;
    private javax.swing.JLabel widthLabel;
    // End of variables declaration//GEN-END:variables

    public void chooseJunkImageDir() throws IOException {
        if (junkDirFileChooser == null) {
            junkDirFileChooser = new JFileChooser();
            junkDirFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        }

        int returnVal = junkDirFileChooser.showDialog(null, "Select directory");
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            junkDirPath = junkDirFileChooser.getSelectedFile().getAbsolutePath();
            copyDirectory(junkDirPath);
        }

    }

    public void copyDirectory(String imageDirPath) throws IOException {
        File file = new File(imageDirPath);
        File[] files = file.listFiles(ioFilter);

        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                copyDirectory(files[i].getPath());
            } else {
                if (!files[i].isHidden()) {
                    try {
                        FileObject object = FileUtil.toFileObject(files[i]);
                        File fajl = new File((CurrentProject.getInstance().getCurrentProject().getProjectDirectory()).getPath() + "/Images/JunkDir");
                        if (fajl.exists()) {
                            rootFileObject = FileUtil.toFileObject(fajl);
                            if (rootFileObject != null) {
                                object.copy(rootFileObject, object.getName(), object.getExt());
                            }
                        }
                    } catch (IOException ex) {
                        //handle exception
                    }
                }
            }
        }
    }

    public boolean imageExist(String name) {
        boolean exists = (new File(junkDirectory + name).exists());
        return exists;
    }

    public void chooseJunkImageFile() throws IOException {
        if (junkFileChooser == null) {
            junkFileChooser = new JFileChooser();
            junkFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            junkFileChooser.setFileFilter(filter);
        }

        int returnVal = junkFileChooser.showDialog(null, "Select junk file");
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            junkFilePath = junkFileChooser.getSelectedFile().getAbsolutePath();
            String junkFileName = junkFileChooser.getSelectedFile().getName();

            File file = new File(junkFilePath);

            if (!file.isDirectory() && !imageExist(junkFileName)) {
                FileObject object = FileUtil.toFileObject(file);
                File fajl = new File((CurrentProject.getInstance().getCurrentProject().getProjectDirectory()).getPath() + "/Images/JunkDir");
                if (fajl.exists() && fajl != null) {
                    rootFileObject = FileUtil.toFileObject(fajl);
                    if (rootFileObject != null) {

                        object.copy(rootFileObject, object.getName(), object.getExt());
                    }
                }
            }
        }
    }

    public String getJunkImageDirPath() {
        return junkImagesDir.getAbsolutePath();
    }

    private void CreateJunkImageFolderAndTreeView() {
        this.junkImagesDir = new File((CurrentProject.getInstance().getCurrentProject().getProjectDirectory()).getPath() + "/Images/JunkDir");
        if (junkImagesDir != null) {
            if (!junkImagesDir.exists()) {
                if (!junkImagesDir.mkdir()) {
                    throw new RuntimeException("Junk images folder could not be created!");
                }
            }

            rootFileObject = FileUtil.toFileObject(junkImagesDir);
            if (rootFileObject != null) {
                try {
                    ((BeanTreeView) jScrollPane1).setRootVisible(true);
                    dataObject = DataObject.find(rootFileObject);
                    rootnode = dataObject.getNodeDelegate();
                    mgr.setRootContext(rootnode);
                } catch (DataObjectNotFoundException ex) {
                }
            }
        } else {
        throw new RuntimeException("Junk images folder could not be created!");
        }
    }

    public ExplorerManager getExplorerManager() {
        return mgr;
    }

    public Lookup getLookup() {
        return lookup;
    }

    public void imagePreview(String imgPath) throws IOException {
        if (imgPath != null) {
            File imageFile = new File(imgPath);
            if (imageFile.exists() && !imageFile.isDirectory()) {
                image = ImageIO.read(imageFile);
                int width = image.getWidth();
                int height = image.getHeight();
                widthLabel.setText(""+width);
                heightLabel.setText(""+height);
                
                double factor = resizeFactor(width, height, 230, 216);
                scaledImage = image.getScaledInstance((int) (width * factor), (int) (height * factor), Image.SCALE_DEFAULT);
                junkImagePreviewLabel.setIcon(new ImageIcon(scaledImage));
            }
        }
    }

    public double resizeFactor(int fileWidth, int fileHeight, int requiredWidth, int requiredHeight) {
        double factor1 = (double) requiredWidth / fileWidth;
        double factor2 = (double) requiredHeight / fileHeight;
        return Math.min(factor1, factor2);
    }

        public void deleteImage  (String imgPath) throws IOException {
        File deletionImage = new File(imgPath);
        if(deletionImage.exists()) {
            if(!deletionImage.delete()) {
                throw new RuntimeException("Image could not be deleted!");
          }
            CreateJunkImageFolderAndTreeView();
        }
    }

    public void imageChanged() {
             rootFileObject.refresh();
        CreateJunkImageFolderAndTreeView();
        try {
            imagePreview(lookupImagePath);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
