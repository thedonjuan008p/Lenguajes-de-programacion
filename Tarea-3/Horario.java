



import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.*;
import java.util.concurrent.atomic.AtomicReference;

public class Horario extends App implements DisplayBlock{

    private static final String CURRENT_DIR = System.getProperty("user.dir");
    private final JButton[] botones = new JButton[42];
    private JSONObject json;

    @Override
    void setBlockButtons() {
        panel_block.removeAll();
        String[] dias = {"Lu","Ma","Mi","Ju","Vi","Sa"};
        String[] bloques = {"1-2","3-4","5-6","7-8","9-10","11-12","13-14"};
        this.panel_block.setLayout(new GridLayout(7,6));
        int index = 0;
        for (String b : bloques){
            for (String d : dias){
                JButton boton = new JButton(d+b);
                botones[index] = boton;
                this.panel_block.add(boton);
                index+=1;
            }
        }
    }

    @Override
    public void writeBlock(JPanel panel, JButton button) {
        Object asignaturasObj = json.get(button.getText());
        if (asignaturasObj != null){
            JSONArray a = (JSONArray) asignaturasObj;
            if (a.size() > 1){
                //topes.put(button.hashCode(),a);
                button.setText("Tope");
                button.addActionListener(e->{
                    AtomicReference<Integer> topeIndex = new AtomicReference<>(1);
                    JSONArray topes = (JSONArray) asignaturasObj;
                    JSONObject tope = (JSONObject) a.get(0);
                    JLabel asignatura = new JLabel("Asignatura: "+tope.get("Asignatura").toString());
                    JLabel tipo = new JLabel("Tipo: "+tope.get("Tipo").toString());
                    panel.removeAll();
                    panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
                    panel.add(asignatura);
                    panel.add(tipo);
                    JButton siguiente = new JButton("Siguiente");
                    siguiente.addActionListener(x->{
                        JSONObject topeNow = (JSONObject) topes.get(topeIndex.get());
                        asignatura.setText("Asignatura: "+topeNow.get("Asignatura").toString());
                        tipo.setText("Tipo: "+topeNow.get("Tipo").toString());
                        panel.updateUI();
                        if (topeIndex.get() == a.size()-1){
                            topeIndex.set(0);
                        }else{
                            topeIndex.updateAndGet(v -> v + 1);
                        }

                    });
                    panel.add(siguiente);
                    panel.updateUI();
                });
            } else{
                JSONObject data = (JSONObject) a.get(0);
                String asignatura = data.get("Asignatura").toString();
                String tipo = data.get("Tipo").toString();
                button.setText(asignatura);
                button.addActionListener(e->{
                    panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
                    panel.removeAll();
                    panel.add(new JLabel("Asignatura: "+asignatura));
                    panel.add(new JLabel("Tipo: "+tipo));
                    panel.updateUI();
                });
            }
        } else{
            button.addActionListener(e->{
                panel.removeAll();
                panel.add(new JLabel("Este bloque no presenta asignaturas"));
                panel.updateUI();
            });
        }
        }

    @Override
    public void fillColor(JButton button, String color) {
        Color c = new Color(Integer.valueOf(color.substring(1,3),16),
                Integer.valueOf(color.substring(3,5),16),
                Integer.valueOf(color.substring(5,7),16));
        button.setBackground(c);

    }

    @Override
    void getFile() throws Exception{
        file_chooser = new JFileChooser();
        file_chooser.setDialogTitle("Abrir horario JSON");
        file_chooser.setCurrentDirectory(new File(CURRENT_DIR));
        int returnValue = file_chooser.showOpenDialog(null);
        if(returnValue == JFileChooser.APPROVE_OPTION){
            pane_content.remove(panel_info);
            panel_info.removeAll();
            panel_info.add(label_info_block);
            pane_content.add(panel_info,BorderLayout.EAST);
            setBlockButtons();
            panel_info.updateUI();
            readFile(file_chooser.getSelectedFile());
        }
    }

    void readFile(File file) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(file));
        json = (JSONObject) obj;
        JSONObject colors = (JSONObject) json.get("Colors");
        for (JButton button : botones) {
            writeBlock(panel_info, button);
            if (button.getText() == "Tope") {
                fillColor(button, "#FF8000");
            } else {
                String color = (String) colors.get(button.getText());
                if (color != null) {
                    fillColor(button, color);
                } else {
                    fillColor(button, "#FFFFFF");
                }
            }
        }
    }
    public static void main(String[] args){
        Horario horario = new Horario();
        horario.setBlockButtons();
        horario.pane_content.add(horario.panel_block,BorderLayout.WEST);
        horario.start();
    }
}