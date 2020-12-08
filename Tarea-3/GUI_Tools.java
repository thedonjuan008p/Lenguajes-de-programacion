import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JFileChooser;
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;

//Interface for blocks
interface DisplayBlock{
  public void writeBlock(JPanel panel, JButton button);
  public void fillColor(JButton button, String color);
};

//GUI
abstract class App{
  private static final String CURRENT_DIR = System.getProperty("user.dir");
  protected JFileChooser file_chooser;
  protected JFrame frame;
  protected JButton button_open;
  protected JButton button_save;
  protected JPanel panel_default_buttons;
  protected JPanel panel_block;
  protected JPanel panel_info;
  protected JLabel label_info_block;
  protected String selected_file;
  protected Container pane_content;

  //Constructor
  App(){

    //Create the frame
    this.frame = new JFrame("Tarea Java");
    this.pane_content = frame.getContentPane();

    //Top buttons panel
    this.panel_default_buttons = new JPanel();
    this.button_open = new JButton("Abrir archivo");
    this.button_save = new JButton("Salvar archivo");
    this.panel_default_buttons.add(this.button_open);
    this.panel_default_buttons.add(this.button_save);
    this.pane_content.add(this.panel_default_buttons, BorderLayout.NORTH);

    //Block buttons panel
    this.panel_block = new JPanel();
    this.panel_block.setPreferredSize(new Dimension(550, 300));

    //Schedule info panel
    this.panel_info = new JPanel();
    this.panel_info.setPreferredSize(new Dimension(350, 300));
    this.label_info_block = new JLabel("Información referente a asignaturas");
    this.panel_info.add(this.label_info_block);
    this.pane_content.add(this.panel_info, BorderLayout.EAST);

    //button actions
    button_open.addActionListener(e -> {
      try{
        this.getFile();
      }
      catch(Exception exception){
        exception.printStackTrace();
      }
    });
  }

  //button open function
  void getFile() throws Exception{
    file_chooser = new JFileChooser();
    file_chooser.setDialogTitle("Abrir horario JSON");
    file_chooser.setCurrentDirectory(new File(CURRENT_DIR));
    int returnValue = file_chooser.showOpenDialog(null);
    if(returnValue == JFileChooser.APPROVE_OPTION){
      selected_file = file_chooser.getSelectedFile().toString();
    }
  }

  final protected void start(){
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }

  abstract void setBlockButtons();
}
