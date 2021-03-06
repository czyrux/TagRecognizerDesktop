package tagrecognizerdesktop;

import de.unidue.tagrecognition.Message;
import de.unidue.tagrecognition.Tag;
import de.unidue.tagrecognition.TagContainer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * @brief Main View UI
 * @author Antonio Manuel Gutierrez Martinez
 * @version 1.0
 */
public class TagRecognitionView extends javax.swing.JPanel {

    /** Creates new form TagRecognitionView */
    public TagRecognitionView() {
        initComponents();
        
        initialStateButtons();
        
        //Colors array
        _colors = new Color[] {
            Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN, Color.GRAY
        };
        _indexColor = 0;
        
        //Data structure to control tags followed
        _followedTags = new HashMap<String, TagContainer>();
        
        //_followedTags.put("12321232", new TagContainer("12321232",Color.BLUE));
        //_followedTags.put("13221113", new TagContainer("13221113",Color.GREEN));
        TagContainer c = new TagContainer("12321113",_colors[_indexColor]);
        //c.setTag(new Tag(10, 10, "12321113", null));
        //c.setTag(new Tag(30, 30, "12321113", null));
        _followedTags.put("12321113", c );
        _indexColor = ((_indexColor + 1) % _colors.length) ;
        _followedTags.put("12221333", new TagContainer("12221333",_colors[_indexColor]));
        _indexColor = ((_indexColor + 1) % _colors.length) ;
        _followedTags.put("12321232", new TagContainer("12321232",_colors[_indexColor]));
        _indexColor = ((_indexColor + 1) % _colors.length) ;
        
        //Fill table
        fillTable();
        
        
        //View tab
        _viewActive = false;
        jTabbedPane1.setEnabledAt(1, false);
        
        //Initialize server
        _server = new ServerThread(this);
        Thread sf = new Thread(_server);
        jLabel_info.setText("<html>Sever listening in port : <b>" + _server.getPort() + "</b></html>");
        sf.start(); 
        
    }

    /**
     * Personal implementation of DefaultTableCellRenderer
     */
    public class MyRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                     boolean isSelected, boolean hasFocus,
                                                     int row, int column) {
            ImageIcon icon = null;
            if(value.equals(Color.RED)) {
                icon = new ImageIcon(getClass().getResource("/tagrecognizerdesktop/resources/red24.png"));
            } else if(value.equals(Color.GREEN)) {
                icon = new ImageIcon(getClass().getResource("/tagrecognizerdesktop/resources/green24.png"));
            } else if(value.equals(Color.BLUE)) {
                icon = new ImageIcon(getClass().getResource("/tagrecognizerdesktop/resources/blue24.png"));
            } else if(value.equals(Color.YELLOW)) {
                icon = new ImageIcon(getClass().getResource("/tagrecognizerdesktop/resources/yellow24.png"));
            } else if(value.equals(Color.GRAY)) {
                icon = new ImageIcon(getClass().getResource("/tagrecognizerdesktop/resources/grey24.png"));
            } else {
                icon = new ImageIcon(getClass().getResource("/tagrecognizerdesktop/resources/grey24.png"));
            }

            setIcon(icon);
            setHorizontalAlignment(CENTER);
            return this;
      }
    }
    
    /**
     * Fill followed table
     */
    private void fillTable() {
        //Clean table
        while(((DefaultTableModel)jTable_follow.getModel()).getRowCount()>0)
            ((DefaultTableModel)jTable_follow.getModel()).removeRow(0);

        //Get info
        TagContainer[] _l_tags = new TagContainer[_followedTags.values().size()];
        _l_tags = (TagContainer[]) _followedTags.values().toArray(_l_tags);

        // Put values on table   
        String[] columnNames = {"Followed","Color","Code"};
        Object[][] data = new Object[_l_tags.length][];
        for(int i=0;i<_l_tags.length;i++) {
            data[i] = new Object[3];
            data[i][0] = _l_tags[i].getFollowed();
            data[i][1] = _l_tags[i].getColor();
            data[i][2] = _l_tags[i].getCode();
        }
        
        //Define model
        _model = new DefaultTableModel(data, columnNames) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
            
            
        };

        //Render to align text
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);


        jTable_follow.setModel(_model);
        jTable_follow.getColumn("Color").setCellRenderer(new MyRenderer());
        jTable_follow.setRowHeight(25);
        jTable_follow.getColumn("Color").setWidth(40);
        jTable_follow.getColumn("Color").setMinWidth(40);
        jTable_follow.getColumn("Color").setMaxWidth(40);
        jTable_follow.getColumn("Code").setCellRenderer(renderer);
        
        //add listener to column
        jTable_follow.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event ) {
                if(!event.getValueIsAdjusting()) {
                    int row = jTable_follow.getSelectedRow();
                    if (row >= 0 && row < jTable_follow.getRowCount() ) {
                        String code = (String)jTable_follow.getValueAt(row,2);
                        TagContainer c = (TagContainer) _followedTags.get(code);
                        jTable_follow.setValueAt(!c.getFollowed(), row, 0);
                        if (c!=null) {
                            c.setFollowed(!c.getFollowed());
                            setPoints();                        
                        }
                    }
                    jTable_follow.clearSelection();
                }
            }
        });
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollBar1 = new javax.swing.JScrollBar();
        jPanel_controls = new javax.swing.JPanel();
        jButton_calibrate = new javax.swing.JButton();
        jButton_start = new javax.swing.JButton();
        jButton_stop = new javax.swing.JButton();
        jButton_receiveImage = new javax.swing.JButton();
        jLabel_androidControls = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jLabel_log = new javax.swing.JLabel();
        jLabel_image = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane_table = new javax.swing.JScrollPane();
        jTable_follow = new javax.swing.JTable();
        jButton_addTag = new javax.swing.JButton();
        jButton_removeTag = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel_info = new javax.swing.JLabel();

        jScrollBar1.setName("jScrollBar1"); // NOI18N

        setMinimumSize(new java.awt.Dimension(1000, 650));
        setName("Form"); // NOI18N
        setPreferredSize(new java.awt.Dimension(1024, 650));

        jPanel_controls.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel_controls.setName("jPanel_controls"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(tagrecognizerdesktop.TagRecognizer.class).getContext().getResourceMap(TagRecognitionView.class);
        jButton_calibrate.setIcon(resourceMap.getIcon("jButton_calibrate.icon")); // NOI18N
        jButton_calibrate.setText(resourceMap.getString("jButton_calibrate.text")); // NOI18N
        jButton_calibrate.setName("jButton_calibrate"); // NOI18N
        jButton_calibrate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_calibrateActionPerformed(evt);
            }
        });

        jButton_start.setIcon(resourceMap.getIcon("jButton_start.icon")); // NOI18N
        jButton_start.setText(resourceMap.getString("jButton_start.text")); // NOI18N
        jButton_start.setName("jButton_start"); // NOI18N
        jButton_start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_startActionPerformed(evt);
            }
        });

        jButton_stop.setIcon(resourceMap.getIcon("jButton_stop.icon")); // NOI18N
        jButton_stop.setText(resourceMap.getString("jButton_stop.text")); // NOI18N
        jButton_stop.setName("jButton_stop"); // NOI18N
        jButton_stop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_stopActionPerformed(evt);
            }
        });

        jButton_receiveImage.setIcon(resourceMap.getIcon("jButton_receiveImage.icon")); // NOI18N
        jButton_receiveImage.setText(resourceMap.getString("jButton_receiveImage.text")); // NOI18N
        jButton_receiveImage.setName("jButton_receiveImage"); // NOI18N
        jButton_receiveImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_receiveImageActionPerformed(evt);
            }
        });

        jLabel_androidControls.setFont(resourceMap.getFont("jLabel_androidControls.font")); // NOI18N
        jLabel_androidControls.setIcon(resourceMap.getIcon("jLabel_androidControls.icon")); // NOI18N
        jLabel_androidControls.setLabelFor(jPanel_controls);
        jLabel_androidControls.setText(resourceMap.getString("jLabel_androidControls.text")); // NOI18N
        jLabel_androidControls.setName("jLabel_androidControls"); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel_controlsLayout = new org.jdesktop.layout.GroupLayout(jPanel_controls);
        jPanel_controls.setLayout(jPanel_controlsLayout);
        jPanel_controlsLayout.setHorizontalGroup(
            jPanel_controlsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel_controlsLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel_controlsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jButton_stop, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
                    .add(jButton_start, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
                    .add(jButton_calibrate, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
                    .add(jButton_receiveImage, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
                    .add(jLabel_androidControls, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel_controlsLayout.setVerticalGroup(
            jPanel_controlsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel_controlsLayout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel_androidControls)
                .add(18, 18, 18)
                .add(jButton_receiveImage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 45, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButton_calibrate, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 45, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButton_start, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 45, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButton_stop, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 45, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18))
        );

        jButton_receiveImage.getAccessibleContext().setAccessibleName(resourceMap.getString("jButton1.AccessibleContext.accessibleName")); // NOI18N

        jTabbedPane1.setName("jTabbedPane1"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jLabel_log.setText(resourceMap.getString("jLabel_log.text")); // NOI18N
        jLabel_log.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel_log.setName("jLabel_log"); // NOI18N
        jScrollPane1.setViewportView(jLabel_log);

        jTabbedPane1.addTab(resourceMap.getString("jScrollPane1.TabConstraints.tabTitle"), jScrollPane1); // NOI18N

        jLabel_image.setText(resourceMap.getString("jLabel_image.text")); // NOI18N
        jLabel_image.setName("jLabel_image"); // NOI18N
        jTabbedPane1.addTab(resourceMap.getString("jLabel_image.TabConstraints.tabTitle"), jLabel_image); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel1.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanel1.border.titleFont"))); // NOI18N
        jPanel1.setName("jPanel1"); // NOI18N

        jScrollPane_table.setName("jScrollPane_table"); // NOI18N

        jTable_follow.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Followed", "Color", "Code"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable_follow.setName("jTable_follow"); // NOI18N
        jScrollPane_table.setViewportView(jTable_follow);
        jTable_follow.getColumnModel().getColumn(0).setResizable(false);
        jTable_follow.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("jTable_follow.columnModel.title0")); // NOI18N
        jTable_follow.getColumnModel().getColumn(1).setResizable(false);
        jTable_follow.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("jTable_follow.columnModel.title1")); // NOI18N
        jTable_follow.getColumnModel().getColumn(2).setResizable(false);
        jTable_follow.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("jTable_follow.columnModel.title2")); // NOI18N

        jButton_addTag.setIcon(resourceMap.getIcon("jButton_addTag.icon")); // NOI18N
        jButton_addTag.setText(resourceMap.getString("jButton_addTag.text")); // NOI18N
        jButton_addTag.setName("jButton_addTag"); // NOI18N
        jButton_addTag.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_addTagActionPerformed(evt);
            }
        });

        jButton_removeTag.setIcon(resourceMap.getIcon("jButton_removeTag.icon")); // NOI18N
        jButton_removeTag.setText(resourceMap.getString("jButton_removeTag.text")); // NOI18N
        jButton_removeTag.setName("jButton_removeTag"); // NOI18N
        jButton_removeTag.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_removeTagActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane_table, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jButton_addTag, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jButton_removeTag, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .add(jScrollPane_table, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jButton_addTag, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jButton_removeTag, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel_message.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanel_message.border.titleFont"))); // NOI18N
        jPanel2.setName("jPanel_message"); // NOI18N

        jLabel_info.setFont(resourceMap.getFont("jLabel_info.font")); // NOI18N
        jLabel_info.setText(resourceMap.getString("jLabel_info.text")); // NOI18N
        jLabel_info.setName("jLabel_info"); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel_info, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 642, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jLabel_info, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(6, 6, 6)
                        .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 707, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel_controls, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(56, 56, 56))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 479, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(jPanel_controls, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 278, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * Set the buttons to an initial state
     */
    private void initialStateButtons () {
        jButton_receiveImage.setEnabled(true);
        jButton_start.setEnabled(true);
        jButton_stop.setEnabled(false);
        jButton_calibrate.setEnabled(true);
    }
    
    /**
     * Actions made closing app
     */
    public void closeApp() {
        if (_server != null ) {
            _server.closeServer();
            _server = null;
        }
    }
    
    /**
     * Actions with calibrate button
     */
    private void jButton_calibrateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_calibrateActionPerformed
	//Update state of buttons
        jButton_receiveImage.setEnabled(false);
        jButton_start.setEnabled(false);
        jButton_stop.setEnabled(false);
        jButton_calibrate.setEnabled(false);
        
        //Write message
        jLabel_info.setText("<html><i>Calibrating...</i> It can take a while.</html>");
        
        //Send command
        sendCMD(Message.CALIBRATE);
        
    }//GEN-LAST:event_jButton_calibrateActionPerformed

    /**
     * Actions with calibrate button
     */
    private void jButton_startActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_startActionPerformed
        //Update state of buttons
        jButton_receiveImage.setEnabled(false);
        jButton_start.setEnabled(false);
        jButton_stop.setEnabled(true);
        jButton_calibrate.setEnabled(false);
        
        //Write message
        jLabel_info.setText("Waiting information from android application.");
        //Send command
        sendCMD(Message.START_SEARCH);
    }//GEN-LAST:event_jButton_startActionPerformed

    /**
     * Actions with stop button
     */
    private void jButton_stopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_stopActionPerformed
        initialStateButtons();
        sendCMD(Message.STOP_SEARCH);
    }//GEN-LAST:event_jButton_stopActionPerformed

    /**
     * Actions with to receive image
     */
    private void jButton_receiveImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_receiveImageActionPerformed
        jButton_receiveImage.setEnabled(false);
        jButton_start.setEnabled(false);
        jButton_stop.setEnabled(false);
        jButton_calibrate.setEnabled(false);
        sendCMD(Message.SEND_VIEW);
    }//GEN-LAST:event_jButton_receiveImageActionPerformed

    /**
     * Actions with add tag from followed table
     */
    private void jButton_addTagActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_addTagActionPerformed
        String code = (String)JOptionPane.showInputDialog(
                    this,
                    "Insert code tag to follow:\n",
                    "TagRecognizer Desktop",
                    JOptionPane.PLAIN_MESSAGE);
        //Check
        boolean error = false;
        if (code.equals("") || code == null ) {
            showError("Code cannot be empty");
            error = true;
        } 
        else if (_followedTags.containsKey(code)){
            showError("Tag already followed");
            error = true;
        }
        else {
            for (int i=0 ; i<code.length() && !error ; i++ ){
                if (code.charAt(i) != '1' && code.charAt(i) !='2' && code.charAt(i) !='3') {
                    error = true;
                }
            }
            if (error){
                showError("The code can only contain values 1, 2 or 3");
            }
        }
        //Add tag
        if (!error) {
            _followedTags.put(code, new TagContainer(code,_colors[_indexColor]));
            _indexColor = ((_indexColor + 1) % _colors.length) ;
            fillTable();
        }
    }//GEN-LAST:event_jButton_addTagActionPerformed

    /**
     * Actions with remove tag from followed table
     */
    private void jButton_removeTagActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_removeTagActionPerformed
        int row = jTable_follow.getSelectedRow();
        if (row >= 0) {
            String code = (String)jTable_follow.getValueAt(row,2);
            _followedTags.remove(code);
            fillTable();
        }
    }//GEN-LAST:event_jButton_removeTagActionPerformed

    /**
     * Show error string on the notification panel
     */
    private void showError (String s ) {
        JOptionPane.showMessageDialog(this,
                s, "TagRecognizer Desktop",
                JOptionPane.PLAIN_MESSAGE,
                new ImageIcon(getClass().getResource("/tagrecognizerdesktop/resources/warning.png")));
    }
    
    /**
     * Send cmd to android server application
     * @param cmd 
     */
    private void sendCMD(Message cmd) {
        CmdSender sender = new CmdSender(this,cmd);
        Thread sf = new Thread(sender);
        sf.start();
    }
    
    /**
     * Get string associated to colour
     * @param c
     * @return 
     */
    private String getStringColor ( Color c ) {
        String color = "";
        if ( c == Color.RED )
            color = "red";
        else if ( c == Color.BLUE )
            color = "blue";
        else if ( c == Color.YELLOW )
            color = "yellow";
        else if ( c == Color.GREEN )
            color = "green";
        else if ( c == Color.GRAY )
            color = "gray";
        
        
        return color;
    }
    
    /**
     * Update UI with the list of tags
     * @param tags 
     */
    public synchronized void updateUI(ArrayList<Tag> tags) {
        if ( tags.size() > 0) {
            String info = new String();
            info += "<p>Received " + tags.size() + " tags at <b>" + 
                    new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").
                    format(tags.get(0).getTime()) + "</b><br />";
            
            for ( Tag t : tags ){
                if ( _followedTags.containsKey(t.getCode())) {
                    //Get container
                    TagContainer c = (TagContainer)_followedTags.get(t.getCode());
                    
                    //Add tag
                    c.setTag(t);
                    //Log text
                    info += "Position (" + "x=" + t.getX() + ",y=" + t.getY() + 
                        "). Code: <b><i><font color=\"" + 
                            getStringColor(c.getColor())
                            + "\">" + t.getCode() + "</font></i></b><br /><br />";
                }
                
            }
            info += "</p>";
            
            //Update UI
            this.updateLog(info);
            
            //If there graphical view
            if (this._viewActive) {
                //Draw points
                setPoints();
            }
        }
        
    }
    
    /**
     * Set point in the image
     */
    private void setPoints() {
        if (!_viewActive)
            return;
        
        //Prepare image
        _image = new ImageIcon(_imgData);
        BufferedImage img = new BufferedImage(_image.getIconWidth(), _image.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.drawImage(_image.getImage(), 0, 0, null);
                
        //Draw points
        for (Entry<String,TagContainer> v : _followedTags.entrySet()) {
            if (v.getValue().getFollowed()) {
                ArrayList<Tag> tags = v.getValue().getTags();
                for ( int i=0; i< tags.size() ; i++  ) {
                    //Set color
                    g.setColor(v.getValue().getColor());
                    //Draw circle
                    g.fillOval( (int)((tags.get(i).getX() * _image.getIconWidth()) / 100) , 
                                (int)((tags.get(i).getY() * _image.getIconHeight()) / 100 ),
                               15, 15);
                    //Draw line
                    /*if (i!=0)
                        g.drawLine((int)((tags.get(i).getX() * _image.getIconWidth()) / 100),
                                (int)((tags.get(i).getY() * _image.getIconWidth()) / 100),
                                (int)((tags.get(i-1).getX() * _image.getIconWidth()) / 100),
                                (int)((tags.get(i-1).getY() * _image.getIconWidth()) / 100));*/
                }
            }
            
        }
        
                
        g.dispose();
        _image = new ImageIcon(img);
        //Set icon
        Image scaled = _image.getImage().getScaledInstance(jLabel_image.getWidth(), jLabel_image.getHeight(), Image.SCALE_DEFAULT);
        jLabel_image.setIcon(new ImageIcon(scaled));
    }
    
    /**
     * Update log view with html string
     * @param info 
     */
    private void updateLog( String info ) {        
        String backup = jLabel_log.getText();
        backup = backup.replaceAll("<html>", "");
        backup = backup.replaceAll("</html>", "");
        jLabel_log.setText("<html>" + backup + info + "</html>");
    }
    
    /**
     * Update UI with message
     * @param msg 
     */
    public synchronized void updateUI (Message msg ) {
        String str = new String();
        switch(msg) {
            case CALIBRATION_FAIL:
               str = "<html><b>Process completed:</b>Calibration couldn't been done.</html>";
               break;
            case CALIBRATION_OK:
               str = "<html><b>Process completed:</b>Calibration done.</html>";
               break;
           case ERROR_CONNECTING:
               str = "<html><b>Error:</b> Unable to connect with android application.</html>";
               break;
           case ERROR_SENDING:
               str = "<html><b>Error:</b> Unable to send command to android application.</html>";
               break;
           case ERROR_CREATING_SERVER:
               str = "<html><b>Error:</b> Unable to create server";
               break;
           default:
               break;
        }
        
        //Update gui
        jLabel_info.setText(str);
        initialStateButtons();
                
    }
    
    /**
     * Update graphic view
     * @param imgData 
     */
    public synchronized void updateUI ( byte[] imgData ) {
        
        //Prepare the image
        _imgData = imgData;
        _image = new ImageIcon(imgData);
        
        //Scale it
        Image scaled = _image.getImage().getScaledInstance(jLabel_image.getWidth(), jLabel_image.getHeight(), Image.SCALE_DEFAULT);
        jLabel_image.setIcon(new ImageIcon(scaled));
        
        //Update gui
        _viewActive = true;
        jTabbedPane1.setEnabledAt(1, true);
        initialStateButtons();
    }

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_addTag;
    private javax.swing.JButton jButton_calibrate;
    private javax.swing.JButton jButton_receiveImage;
    private javax.swing.JButton jButton_removeTag;
    private javax.swing.JButton jButton_start;
    private javax.swing.JButton jButton_stop;
    private javax.swing.JLabel jLabel_androidControls;
    private javax.swing.JLabel jLabel_image;
    private javax.swing.JLabel jLabel_info;
    private javax.swing.JLabel jLabel_log;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel_controls;
    private javax.swing.JScrollBar jScrollBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane_table;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable_follow;
    // End of variables declaration//GEN-END:variables
    
    private ServerThread _server = null;
    private boolean _viewActive;
    private ImageIcon _image;
    private byte[] _imgData;
    private HashMap<String, TagContainer> _followedTags;
    private TableModel _model;
    private Color[] _colors;
    private int _indexColor;
}
