package com.franticware.midifreq;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class MidiFreq extends javax.swing.JFrame {
    
    /** Creates new form Find */
    public MidiFreq() 
    {        
        initComponents();
        setSize(800, 600);
        String[] instrumentNames = {"1 Acoustic Grand Piano","2 Bright Acoustic Piano","3 Electric Grand Piano","4 Honky-tonk Piano","5 Electric Piano 1","6 Electric Piano 2","7 Harpsichord","8 Clavinet","9 Celesta","10 Glockenspiel",
            "11 Music Box","12 Vibraphone","13 Marimba","14 Xylophone","15 Tubular Bells","16 Dulcimer","17 Drawbar Organ","18 Percussive Organ","19 Rock Organ","20 Church Organ","21 Reed Organ","22 Accordion","23 Harmonica","24 Tango Accordion",
            "25 Acoustic Guitar (nylon)","26 Acoustic Guitar (steel)","27 Electric Guitar (jazz)","28 Electric Guitar (clean)","29 Electric Guitar (muted)","30 Overdriven Guitar","31 Distortion Guitar","32 Guitar harmonics","33 Acoustic Bass",
            "34 Electric Bass (finger)","35 Electric Bass (pick)","36 Fretless Bass","37 Slap Bass 1","38 Slap Bass 2","39 Synth Bass 1","40 Synth Bass 2","41 Violin","42 Viola","43 Cello","44 Contrabass","45 Tremolo Strings","46 Pizzicato Strings",
            "47 Orchestral Harp","48 Timpani","49 String Ensemble 1","50 String Ensemble 2","51 Synth Strings 1","52 Synth Strings 2","53 Choir Aahs","54 Voice Oohs","55 Synth Voice","56 Orchestra Hit","57 Trumpet","58 Trombone","59 Tuba",
            "60 Muted Trumpet","61 French Horn","62 Brass Section","63 Synth Brass 1","64 Synth Brass 2","65 Soprano Sax","66 Alto Sax","67 Tenor Sax","68 Baritone Sax","69 Oboe","70 English Horn","71 Bassoon","72 Clarinet","73 Piccolo","74 Flute",
            "75 Recorder","76 Pan Flute","77 Blown Bottle","78 Shakuhachi","79 Whistle","80 Ocarina","81 Lead 1 (square)","82 Lead 2 (sawtooth)","83 Lead 3 (calliope)","84 Lead 4 (chiff)","85 Lead 5 (charang)","86 Lead 6 (voice)","87 Lead 7 (fifths)",
            "88 Lead 8 (bass + lead)","89 Pad 1 (new age)","90 Pad 2 (warm)","91 Pad 3 (polysynth)","92 Pad 4 (choir)","93 Pad 5 (bowed)","94 Pad 6 (metallic)","95 Pad 7 (halo)","96 Pad 8 (sweep)","97 FX 1 (rain)","98 FX 2 (soundtrack)",
            "99 FX 3 (crystal)","100 FX 4 (atmosphere)","101 FX 5 (brightness)","102 FX 6 (goblins)","103 FX 7 (echoes)","104 FX 8 (sci-fi)","105 Sitar","106 Banjo","107 Shamisen","108 Koto","109 Kalimba","110 Bag pipe","111 Fiddle","112 Shanai",
            "113 Tinkle Bell","114 Agogo","115 Steel Drums","116 Woodblock","117 Taiko Drum","118 Melodic Tom","119 Synth Drum","120 Reverse Cymbal","121 Guitar Fret Noise","122 Breath Noise","123 Seashore","124 Bird Tweet","125 Telephone Ring",
            "126 Helicopter","127 Applause","128 Gunshot"};
        jComboBoxInstrument.removeAllItems();
        for (String s : instrumentNames)
        {
            jComboBoxInstrument.addItem(s);
        }        
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
        } catch (MidiUnavailableException ex) {
            JOptionPane.showMessageDialog(null, "Midi Unavailable", "Error", JOptionPane.ERROR_MESSAGE);
        }        
    }
    
    Sequencer sequencer;
    
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
        
    private static List<Byte> pitchBendToBytes(float bend, int ch) 
    {
        List<Byte> bl = new ArrayList<>();
        
        float pitchBend = bend * 4096;
        
        int pitchBendData = (int)Math.floor(pitchBend + 8192.0 + 0.5);
        int pitchBendLsb = pitchBendData & 0x7F;
        int pitchBendMsb = pitchBendData >> 7;
        
        bl.add((byte)(0xE0+ch));
        bl.add((byte)pitchBendLsb);
        bl.add((byte)pitchBendMsb);
        
        return bl;        
    }
    
    class MidiKey 
    {
        public int key;
        public float fract;
    }
    
    private MidiKey getMidiKey(float freq) 
    {
        MidiKey ret = new MidiKey();
        
        float key = (float)(12.0*Math.log(freq/440)/Math.log(2) + 69);
        ret.key = (int)key;
        ret.fract = key - ret.key;
        
        if (ret.fract > 0.5)
        {
            ret.fract -= 1;
            ret.key += 1;
        }
        
        return ret;
    }
    
    private byte[] buildMidiData(boolean doBend)
    {
        String freqText = jTextAreaFreqs.getText();        
        String freqsStr[] = freqText.split("\\s+");                
        List<MidiKey> mks = new ArrayList<>();        
        for (String freq : freqsStr)
        {
            try {
                float f = Float.parseFloat(freq);       
                if (f > 16)
                {
                    MidiKey mk = getMidiKey(f);
                    if (mk.key >= 0 && mk.key <= 127)
                    {
                        mks.add(mk);
                    }                                    
                }
                
            } catch (NumberFormatException e) {
            }
        }
                
        int arpMs;        
        try {
            arpMs = Integer.parseInt(jTextFieldArpMs.getText());
        } catch (NumberFormatException e) {
            arpMs = 20;
        }
        
        int lengthMs;   
        try {
            lengthMs = Integer.parseInt(jTextFieldToneMs.getText());
        } catch (NumberFormatException e) {
            lengthMs = 20;
        }
        
        if (!jCheckBoxArp.isSelected())
        {
            arpMs = 0;
        }
        
        int instrumentI = jComboBoxInstrument.getSelectedIndex();
        
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
        
        boolean jCheckBoxSmpteisSelected = false;
        
        if (jCheckBoxSmpteisSelected)
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
        
        List<Byte> el = new ArrayList<>(); // midi events
                
        if (!jCheckBoxSmpteisSelected)
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
        
        for (int i = 0; i != 15; ++i)
        {
            int channel = i;
            if (channel >= 9) ++channel;
            
            if (i < mks.size())
            {
                // program change
                el.addAll(msToBytes(i != 0 ? arpMs : 0));        
                el.add((byte)(0xC0 + channel));
                el.add((byte)instrumentI);

                if (doBend)
                {
                    el.addAll(msToBytes(0));
                    el.addAll(pitchBendToBytes(mks.get(i).fract, channel));                    
                }

                // note on
                el.addAll(msToBytes(0));        
                el.add((byte)(0x90+channel));
                el.add((byte)mks.get(i).key);
                el.add((byte)127);                  
            }
        }
        
        for (int i = 0; i != 15; ++i)
        {
            int channel = i;
            if (channel >= 9) ++channel;
            
            if (i < mks.size())
            {
                // note off
                el.addAll(msToBytes( i == 0 ? lengthMs : 0));        
                el.add((byte)(0x80+channel));
                el.add((byte)mks.get(i).key);
                el.add((byte)127);                
            }
        }        
        
        // end
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
        
        return bytes;
    }
    
    private void playMidi(boolean doBend) {
        byte[] midiData = buildMidiData(doBend);        
        InputStream midiStream = new ByteArrayInputStream(midiData);        
        try {
            sequencer.setSequence(midiStream);
        } catch (IOException | InvalidMidiDataException ex) {
            Logger.getLogger(MidiFreq.class.getName()).log(Level.SEVERE, null, ex);
        } 
        // Starts playback of the MIDI data in the currently loaded sequence.
        sequencer.start();        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jLabelInstrument = new javax.swing.JLabel();
        jComboBoxInstrument = new javax.swing.JComboBox<>();
        jTextFieldToneMs = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jCheckBoxArp = new javax.swing.JCheckBox();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        jTextFieldArpMs = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jButtonNoBend = new javax.swing.JButton();
        jButtonPlay = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jTextFieldFile = new javax.swing.JTextField();
        jButtonSave = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaFreqs = new javax.swing.JTextArea();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MidiFreq © 2022 Vojtěch Salajka");
        setMinimumSize(new java.awt.Dimension(400, 300));
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.PAGE_AXIS));

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));

        jLabelInstrument.setText("Instrument: ");
        jPanel2.add(jLabelInstrument);

        jComboBoxInstrument.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxInstrument.setMaximumSize(new java.awt.Dimension(32767, 27));
        jPanel2.add(jComboBoxInstrument);

        jTextFieldToneMs.setText("1000");
        jTextFieldToneMs.setMaximumSize(new java.awt.Dimension(2147483647, 27));
        jPanel2.add(jTextFieldToneMs);

        jLabel1.setText("ms |");
        jPanel2.add(jLabel1);

        jCheckBoxArp.setSelected(true);
        jCheckBoxArp.setText("Arp.");
        jPanel2.add(jCheckBoxArp);
        jPanel2.add(filler1);

        jTextFieldArpMs.setText("50");
        jTextFieldArpMs.setMaximumSize(new java.awt.Dimension(2147483647, 27));
        jPanel2.add(jTextFieldArpMs);

        jLabel2.setText("ms  ");
        jPanel2.add(jLabel2);

        jButtonNoBend.setText("Tempered ▶");
        jButtonNoBend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNoBendActionPerformed(evt);
            }
        });
        jPanel2.add(jButtonNoBend);

        jButtonPlay.setText("Exact ▶");
        jButtonPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPlayActionPerformed(evt);
            }
        });
        jPanel2.add(jButtonPlay);

        getContentPane().add(jPanel2);

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        jTextFieldFile.setText("midifreq");
        jTextFieldFile.setMaximumSize(new java.awt.Dimension(2147483647, 27));
        jPanel1.add(jTextFieldFile);

        jButtonSave.setText("Save");
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonSave);

        getContentPane().add(jPanel1);

        jTextAreaFreqs.setColumns(20);
        jTextAreaFreqs.setRows(5);
        jTextAreaFreqs.setText("330\n440\n660\n880");
        jScrollPane2.setViewportView(jTextAreaFreqs);

        getContentPane().add(jScrollPane2);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        String filenameMid = jTextFieldFile.getText() + ".mid";
        String filenameTxt = jTextFieldFile.getText() + ".txt";
        Path pathMid = Paths.get(filenameMid);
        Path pathTxt = Paths.get(filenameTxt);
        byte[] midiData = buildMidiData(true);
        try {
            Files.write(pathMid, midiData);
            Files.write(pathTxt, jTextAreaFreqs.getText().getBytes());
        }
        catch (IOException e) {
        }
    }//GEN-LAST:event_jButtonSaveActionPerformed

    private void jButtonPlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPlayActionPerformed
        playMidi(true);        
    }//GEN-LAST:event_jButtonPlayActionPerformed

    private void jButtonNoBendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNoBendActionPerformed
        playMidi(false);
    }//GEN-LAST:event_jButtonNoBendActionPerformed
    
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
            java.util.logging.Logger.getLogger(MidiFreq.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new MidiFreq().setVisible(true);
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    private javax.swing.JButton jButtonNoBend;
    private javax.swing.JButton jButtonPlay;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JCheckBox jCheckBoxArp;
    private javax.swing.JComboBox<String> jComboBoxInstrument;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelInstrument;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextAreaFreqs;
    private javax.swing.JTextField jTextFieldArpMs;
    private javax.swing.JTextField jTextFieldFile;
    private javax.swing.JTextField jTextFieldToneMs;
    // End of variables declaration//GEN-END:variables
    
}
