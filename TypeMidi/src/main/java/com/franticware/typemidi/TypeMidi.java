package com.franticware.typemidi;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;

public class TypeMidi extends javax.swing.JFrame {
    
    public TypeMidi() {
        initComponents();
        strokeTimes = new ArrayList<>();
    }
    
    List<Long> strokeTimes;
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabelMsPer4 = new javax.swing.JLabel();
        jTextFieldName = new javax.swing.JTextField();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        jButtonGenerate = new javax.swing.JButton();
        Reset = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jCheckBoxMusicxml = new javax.swing.JCheckBox();
        jTextFieldMsPerNote = new javax.swing.JTextField();
        jLabelMsPer = new javax.swing.JLabel();
        jComboBoxNote = new javax.swing.JComboBox<>();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        jPanel3 = new javax.swing.JPanel();
        jCheckBoxMidi = new javax.swing.JCheckBox();
        jTextFieldMsPerStroke = new javax.swing.JTextField();
        jLabelMsPer3 = new javax.swing.JLabel();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(0, 0));
        jCheckBoxSmpte = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaMain = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("TypeMidi © 2022 Vojtěch Salajka");
        setMinimumSize(new java.awt.Dimension(800, 600));
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.PAGE_AXIS));

        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 32));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        jLabelMsPer4.setText("output name base:");
        jPanel1.add(jLabelMsPer4);

        jTextFieldName.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jTextFieldName.setText("typemidi");
        jPanel1.add(jTextFieldName);
        jPanel1.add(filler3);

        jButtonGenerate.setText("Generate");
        jButtonGenerate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGenerateActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonGenerate);

        Reset.setText("Reset");
        Reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ResetActionPerformed(evt);
            }
        });
        jPanel1.add(Reset);

        getContentPane().add(jPanel1);

        jPanel2.setMaximumSize(new java.awt.Dimension(32767, 32));
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));

        jCheckBoxMusicxml.setSelected(true);
        jCheckBoxMusicxml.setText("musicxml:");
        jPanel2.add(jCheckBoxMusicxml);

        jTextFieldMsPerNote.setText("30");
        jPanel2.add(jTextFieldMsPerNote);

        jLabelMsPer.setText("ms per");
        jPanel2.add(jLabelMsPer);

        jComboBoxNote.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "eighth", "16th", "32nd", "64th" }));
        jComboBoxNote.setSelectedIndex(1);
        jComboBoxNote.setToolTipText("");
        jPanel2.add(jComboBoxNote);
        jPanel2.add(filler1);

        getContentPane().add(jPanel2);

        jPanel3.setMaximumSize(new java.awt.Dimension(32767, 32));
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.LINE_AXIS));

        jCheckBoxMidi.setSelected(true);
        jCheckBoxMidi.setText("midi:");
        jPanel3.add(jCheckBoxMidi);

        jTextFieldMsPerStroke.setText("100");
        jPanel3.add(jTextFieldMsPerStroke);

        jLabelMsPer3.setText("ms per keystroke");
        jPanel3.add(jLabelMsPer3);
        jPanel3.add(filler2);

        jCheckBoxSmpte.setSelected(true);
        jCheckBoxSmpte.setText("SMPTE     ");
        jPanel3.add(jCheckBoxSmpte);

        getContentPane().add(jPanel3);

        jTextAreaMain.setColumns(20);
        jTextAreaMain.setRows(5);
        jTextAreaMain.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextAreaMainKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTextAreaMain);

        getContentPane().add(jScrollPane1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonGenerateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGenerateActionPerformed
        if (jCheckBoxMusicxml.isSelected())
        {
            writeXml();            
        }
        if (jCheckBoxMidi.isSelected())
        {
            writeMid();            
        }
        writeTxt();
    }//GEN-LAST:event_jButtonGenerateActionPerformed

    private void jTextAreaMainKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextAreaMainKeyPressed
        long ms = System.currentTimeMillis();
        if (firstPress)
        {
            firstPress = false;
            startMs = ms;
        }        
        ms -= startMs;        
        strokeTimes.add(ms);
    }//GEN-LAST:event_jTextAreaMainKeyPressed

    private void ResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ResetActionPerformed
        firstPress = true;
        strokeTimes.clear();
        jTextAreaMain.setText("");
    }//GEN-LAST:event_ResetActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            javax.swing.UIManager.LookAndFeelInfo[] installedLookAndFeels=javax.swing.UIManager.getInstalledLookAndFeels();
            for (UIManager.LookAndFeelInfo installedLookAndFeel : installedLookAndFeels) {
                if ("Nimbus".equals(installedLookAndFeel.getName())) {
                    javax.swing.UIManager.setLookAndFeel(installedLookAndFeel.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TypeMidi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(() -> {
            new TypeMidi().setVisible(true);
        });
    }
    
    private boolean firstPress = true;
    private long startMs = 0;
    
    private static List<Byte> intToBytes(int val)
    {
        List<Byte> bl = new ArrayList<>();
        for (int i = 0; i != 4; ++i)
        {
            bl.add((byte)(val >> ((3 - i) * 8)));
        }
        return bl;
    }
    
    private static List<Byte> msToBytes(int val)
    {
        List<Byte> bl = new ArrayList<>();
        
        while (val > 127)
        {
            bl.add((byte)((val & 127)));
            val >>= 7;
        }
        bl.add((byte)(val & 127));
        
        List<Byte> ret = new ArrayList<>();
        
        for (int i = 0; i != bl.size(); ++i)
        {
            byte b = bl.get(bl.size() - 1 - i);             
            if (i != bl.size() - 1)
            {
                b |= 128;
            }            
            ret.add(b);
        }        
        return ret;
    }
    
    private void writeMid()
    {
        int msPerStroke;
        try {
            msPerStroke = Integer.parseInt(jTextFieldMsPerStroke.getText());
        } catch (NumberFormatException e) {
            msPerStroke = 0;
        }
        
        if (msPerStroke < 1)
        {
            msPerStroke = 1;
            jTextFieldMsPerStroke.setText("" + msPerStroke);
        }
        
        String filename = jTextFieldName.getText() + "-" + msPerStroke + "ms-per-stroke";
        
        if (jCheckBoxSmpte.isSelected())
        {
            filename += "-smpte";          
        }
        
        Path path = Paths.get(filename + ".mid");
        
        List<Byte> bl = new ArrayList<>(); // midi data
                
        bl.add((byte)0x4d); // MThd
        bl.add((byte)0x54);
        bl.add((byte)0x68);
        bl.add((byte)0x64);
        
        bl.add((byte)0); // header length = 6
        bl.add((byte)0);
        bl.add((byte)0);
        bl.add((byte)6);
        
        bl.add((byte)0); // midi type 0, track count 1
        bl.add((byte)0);
        bl.add((byte)0);
        bl.add((byte)1);
        
        if (jCheckBoxSmpte.isSelected())
        {
            bl.add((byte)0xe7); // SMPTE timing 1 ms
            bl.add((byte)0x28);            
        }
        else
        {
            bl.add((byte)0x01); // 500 ticks per 1/4 note
            bl.add((byte)0xf4);            
        }
        
        bl.add((byte)0x4d); // MTrk
        bl.add((byte)0x54);
        bl.add((byte)0x72);
        bl.add((byte)0x6b);        
        
        final byte key = (byte)0x3C;
        
        List<Byte> el = new ArrayList<>(); // midi events        
        
        if (!jCheckBoxSmpte.isSelected())
        {
            // time signature
            el.add((byte)0);
            el.add((byte)0xff);
            el.add((byte)0x58);
            el.add((byte)0x04);
            el.add((byte)0x04);
            el.add((byte)0x02);
            el.add((byte)0x18);
            el.add((byte)0x08);

            // bpm
            el.add((byte)0);
            el.add((byte)0xff);
            el.add((byte)0x51);
            el.add((byte)0x03);
            el.add((byte)0x07);
            el.add((byte)0xa1);
            el.add((byte)0x20);            
        }
        
        long prevMs = 0;
        
        for (int i = 0; i != strokeTimes.size(); ++i)
        {
            long ms = strokeTimes.get(i);
            
            long diff = (ms - prevMs);
            prevMs = ms;
                        
            if (i == 0)        
            {
                el.addAll(msToBytes(0));        
                el.add((byte)0x93);
                el.add(key);
                el.add((byte)127);                
            }
            else
            {
                long strokeTime = diff;
                if (strokeTime > msPerStroke)
                {
                    strokeTime = strokeTime - msPerStroke;
                    
                    el.addAll(msToBytes(msPerStroke));        
                    el.add((byte)0x83);
                    el.add(key);
                    el.add((byte)127);
                    
                    el.addAll(msToBytes((int) strokeTime));        
                    el.add((byte)0x93);
                    el.add(key);
                    el.add((byte)127);   
                }
                else
                {
                    el.addAll(msToBytes((int) strokeTime));        
                    el.add((byte)0x83);
                    el.add(key);
                    el.add((byte)127);
                    
                    el.addAll(msToBytes(0));        
                    el.add((byte)0x93);
                    el.add(key);
                    el.add((byte)127);   
                }
            }
        }
        
        el.addAll(msToBytes(msPerStroke));        
        el.add((byte)0x83);
        el.add(key);
        el.add((byte)127);
        
        el.addAll(msToBytes(500));        
        el.add((byte)0xff);
        el.add((byte)0x2f);
        el.add((byte)0);
        
        
        bl.addAll(intToBytes(el.size())); // track size
        bl.addAll(el);
        
        // this is dumb
        byte[] bytes = new byte[bl.size()];
        for (int i = 0; i < bl.size(); i++) 
        {
            bytes[i] = bl.get(i);
        }
 
        try {
            Files.write(path, bytes);
        }
        catch (IOException e) {
        }        
    }
    
    private void writeTxt()
    {
        String filename = jTextFieldName.getText();        
        {
            PrintWriter writer2;
            try {
                writer2 = new PrintWriter(new FileOutputStream(filename + ".txt", false));

                writer2.println(jTextAreaMain.getText());
                writer2.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(TypeMidi.class.getName()).log(Level.SEVERE, null, ex);
            }                
        }        
    }
    
    private void writeXml()
    {
        int msPerNote;
        try {
            msPerNote = Integer.parseInt(jTextFieldMsPerNote.getText());
        } catch (NumberFormatException e) {
            msPerNote = 0;
        }
        
        if (msPerNote < 1)
        {
            msPerNote = 10;
            jTextFieldMsPerNote.setText("" + msPerNote);
        }
        
        String note = jComboBoxNote.getSelectedItem().toString();
        
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        String nowAsISO = df.format(new Date());        
        
        
        final int notesPerMeasureArr[]  = {8, 16, 32, 64};
        final int measureDivisionsArr[] = {2,  4,  8, 16};
        int notesPerMeasure = notesPerMeasureArr[jComboBoxNote.getSelectedIndex()];
        int measureDivisions = measureDivisionsArr[jComboBoxNote.getSelectedIndex()];
        int measureNumber = 2;
        int measureCount = notesPerMeasure; // todo rename variable
        String filename = jTextFieldName.getText() + "-" + msPerNote + "ms-per-" + note;        
        {
            PrintWriter writer;
            try {
                writer = new PrintWriter(new FileOutputStream(filename + ".musicxml", false));
                writer.println(
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
"<!DOCTYPE score-partwise PUBLIC \"-//Recordare//DTD MusicXML 3.1 Partwise//EN\" \"http://www.musicxml.org/dtds/partwise.dtd\">\n" +
"<score-partwise version=\"3.1\">\n" +
"  <identification>\n" +
"    <encoding>\n" +
"      <software>TypeMidi prototype</software>\n" +
"      <encoding-date>"+nowAsISO+"</encoding-date>\n" +
"      <supports element=\"accidental\" type=\"yes\"/>\n" +
"      <supports element=\"beam\" type=\"yes\"/>\n" +
"      <supports element=\"print\" attribute=\"new-page\" type=\"no\"/>\n" +
"      <supports element=\"print\" attribute=\"new-system\" type=\"no\"/>\n" +
"      <supports element=\"stem\" type=\"yes\"/>\n" +
"      </encoding>\n" +
"    </identification>\n" +
"  <defaults>\n" +
"    <scaling>\n" +
"      <millimeters>7</millimeters>\n" +
"      <tenths>40</tenths>\n" +
"      </scaling>\n" +
"    <page-layout>\n" +
"      <page-height>1697.14</page-height>\n" +
"      <page-width>1200</page-width>\n" +
"      <page-margins type=\"even\">\n" +
"        <left-margin>85.7143</left-margin>\n" +
"        <right-margin>85.7143</right-margin>\n" +
"        <top-margin>85.7143</top-margin>\n" +
"        <bottom-margin>85.7143</bottom-margin>\n" +
"        </page-margins>\n" +
"      <page-margins type=\"odd\">\n" +
"        <left-margin>85.7143</left-margin>\n" +
"        <right-margin>85.7143</right-margin>\n" +
"        <top-margin>85.7143</top-margin>\n" +
"        <bottom-margin>85.7143</bottom-margin>\n" +
"        </page-margins>\n" +
"      </page-layout>\n" +
"    <word-font font-family=\"Edwin\" font-size=\"10\"/>\n" +
"    <lyric-font font-family=\"Edwin\" font-size=\"10\"/>\n" +
"    </defaults>\n" +
"  <part-list>\n" +
"    <part-group type=\"start\" number=\"1\">\n" +
"      <group-symbol>brace</group-symbol>\n" +
"      </part-group>\n" +
"    <score-part id=\"P1\">\n" +
"      <part-name>Piano</part-name>\n" +
"      <part-abbreviation>Pno.</part-abbreviation>\n" +
"      <score-instrument id=\"P1-I1\">\n" +
"        <instrument-name>Piano</instrument-name>\n" +
"        </score-instrument>\n" +
"      <midi-device id=\"P1-I1\" port=\"1\"></midi-device>\n" +
"      <midi-instrument id=\"P1-I1\">\n" +
"        <midi-channel>1</midi-channel>\n" +
"        <midi-program>1</midi-program>\n" +
"        <volume>78.7402</volume>\n" +
"        <pan>0</pan>\n" +
"        </midi-instrument>\n" +
"      </score-part>\n" +
"    </part-list>\n" +
"  <part id=\"P1\">\n" +
"    <measure number=\"1\">\n" +
"      <print>\n" +
"        <system-layout>\n" +
"          <system-margins>\n" +
"            <left-margin>50.00</left-margin>\n" +
"            <right-margin>0.00</right-margin>\n" +
"            </system-margins>\n" +
"          <top-system-distance>70.00</top-system-distance>\n" +
"          </system-layout>\n" +
"        </print>\n" +
"      <attributes>\n" +
"        <divisions>"+measureDivisions+"</divisions>\n" +
"        <key>\n" +
"          <fifths>0</fifths>\n" +
"          </key>\n" +
"        <time>\n" +
"          <beats>4</beats>\n" +
"          <beat-type>4</beat-type>\n" +
"          </time>\n" +
"        <clef>\n" +
"          <sign>G</sign>\n" +
"          <line>2</line>\n" +
"          </clef>\n" +
"        </attributes>"
                    );                   

                long prevMs = 0;
                for (long ms : strokeTimes) 
                { 
                    long diff = (ms - prevMs);
                    prevMs = ms;                        
                    long beats = (diff + msPerNote/2)/msPerNote;
                    if (beats == 0)
                    {
                        beats = 1;                            
                    }                        
                    for (long i = 0; i < beats; ++i)
                    {
                        if (i + 1 == beats)
                        {
                            writer.println("<note>\n" +
"        <pitch>\n" +
"          <step>C</step>\n" +
"          <octave>5</octave>\n" +
"          </pitch>\n" +
"        <duration>1</duration>\n" +
"        <voice>1</voice>\n" +
"        <type>"+note+"</type>\n" +
"        <stem>down</stem>\n" +
"        </note>");
                        }
                        else
                        {
                            writer.println("      <note>\n" +
"        <rest/>\n" +
"        <duration>1</duration>\n" +
"        <voice>1</voice>\n" +
"        <type>"+note+"</type>\n" +
"        </note>");                                
                        }                           
                        --measureCount;
                        if (measureCount == 0)
                        {
                            writer.println("      </measure>\n" +
"    <measure number=\""+measureNumber+"\">");                                
                            ++measureNumber;
                            measureCount = notesPerMeasure;
                        }
                    }
                }       
                for (int i = 0; i < measureCount; ++i)
                {
                    writer.println("      <note>\n" +
"        <rest/>\n" +
"        <duration>1</duration>\n" +
"        <voice>1</voice>\n" +
"        <type>"+note+"</type>\n" +
"        </note>");
                }
                writer.println("<barline location=\"right\">\n" +
"        <bar-style>light-heavy</bar-style>\n" +
"        </barline>\n" +
"      </measure>\n" +
"    </part>\n" +
"  </score-partwise>");
                writer.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(TypeMidi.class.getName()).log(Level.SEVERE, null, ex);
            }                
        }
    }    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Reset;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.JButton jButtonGenerate;
    private javax.swing.JCheckBox jCheckBoxMidi;
    private javax.swing.JCheckBox jCheckBoxMusicxml;
    private javax.swing.JCheckBox jCheckBoxSmpte;
    private javax.swing.JComboBox<String> jComboBoxNote;
    private javax.swing.JLabel jLabelMsPer;
    private javax.swing.JLabel jLabelMsPer3;
    private javax.swing.JLabel jLabelMsPer4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextAreaMain;
    private javax.swing.JTextField jTextFieldMsPerNote;
    private javax.swing.JTextField jTextFieldMsPerStroke;
    private javax.swing.JTextField jTextFieldName;
    // End of variables declaration//GEN-END:variables
    
}
