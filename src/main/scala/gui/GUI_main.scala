import scala.meta._
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.layout.HBox
import scalafx.geometry.Insets
import scalafx.stage.FileChooser
import scalafx.stage.FileChooser.ExtensionFilter
import scalafx.scene.control.ScrollPane.ScrollBarPolicy
import scalafx.event.ActionEvent
import scalafx.event.EventHandler
import scalafx.scene.control.{TextArea,Label}
import scalafx.scene.text.{Text, TextFlow}
import scalafx.scene.layout._
import scalafx.scene._
import scalafx.geometry._
import scalafx.scene.control._
import scalafx.scene.paint.Color._
import scalafx.scene.paint._
import scalafx.stage._
import scala.io.Source
import scala.io.Source._
import java.nio.file.Path

object ScalaCodeAnalyzerMain extends JFXApp {
   
    var lines = ""
    var exampleTreeStruct = ""
    var input_text = new TextArea(){
        //vgrow = Priority.Always
        wrapText = true
        prefHeight = 480
        prefWidth = 570
    }

    var output_text = new TextArea() {
        //text = lines
        wrapText = true
        prefHeight = 480
        prefWidth = 570
       //vgrow = Priority.Always
    }
    var fileChooser = new FileChooser {
        title = "Open Scala File"
       /*  extensionFilters = Seq(
        new ExtensionFilter("Scala Files", "*.txt"),
        new ExtensionFilter("All Files", "*.*")
        ) */
        }

    val menu = new MenuBar {
        menus = List(
        new Menu("File") {
            items = List(
            new MenuItem("New"),
            new MenuItem("Open"){
                onAction = (ae : ActionEvent) =>  {

                    val file_src = fileChooser.showOpenDialog(stage)
                    if (file_src != null) {
                        //openFile(file_src);
                            val path = file_src.toPath()
                            val bytes = java.nio.file.Files.readAllBytes(path)
                            val text = new String(bytes, "UTF-8")
                            val input = Input.VirtualFile(path.toString, text)
                            val exampleTree = input.parse[Stat].get
                            exampleTreeStruct = exampleTree.structure
                            //println(exampleTreeStruct)
                            lines = fromFile(file_src).mkString
                            //println(lines)
                            input_text.text = lines 
                                                
                    }
                }
            }
            )
        },
        new Menu("Edit") {
            items = List(
            new MenuItem("Cut"),
            new MenuItem("Copy"),
            new MenuItem("Paste")
            )
        },
        new Menu("Help") {
            items = List(
                new MenuItem("About"),
                )
        }
        )
    }

    val show_ast_btn = new Button("show AST"){

    
        onAction = (ast_event : ActionEvent) => {
                    output_text.clear()
                    println("Button clicked")
                    //println(exampleTreeStruct)
                    output_text.text = exampleTreeStruct
    

            }
    }
    val process_btn = new Button("process"){
    onAction = (proc_event : ActionEvent) =>{
                output_text.clear()
            }
    }
    val in_clr_btn = new Button("clear input"){
        onAction = (in_clr_event : ActionEvent) => {
            input_text.clear()
        }
    }
    val out_clr_btn = new Button("clear output"){
        onAction = (out_clr_event : ActionEvent) => {
            output_text.clear()
        }
    }
    val button_content = new HBox{
        padding = Insets(50, 80, 50, 80)
        spacing = 20
        children = Seq(
            in_clr_btn, show_ast_btn,process_btn, out_clr_btn,
        )
        alignment = Pos.Center
    }
    
    val input_feild =new ScrollPane() {
    vbarPolicy = ScrollBarPolicy.ALWAYS
    hbarPolicy = ScrollBarPolicy.AS_NEEDED
    prefHeight = 480
    prefWidth = 600
    content = input_text
  }
  val output_feild = new ScrollPane() {
    vbarPolicy = ScrollBarPolicy.ALWAYS
    hbarPolicy = ScrollBarPolicy.AS_NEEDED
    prefHeight = 480
    prefWidth = 600
    content = output_text
  }
    val in_content = new HBox {
        padding = Insets(50, 80, 50, 80)
        spacing = 20
        children = Seq(
            input_feild,
            output_feild
        )
        alignment = Pos.Center
    }

  val myScene = new Scene(640, 720){
              fill = Color.rgb(38, 38, 38)
              root = new BorderPane {
                top = menu
                center = in_content
                bottom = button_content
              }
  }

  stage = new JFXApp.PrimaryStage {
        scene = myScene
        title = "Scala Code Analyzer v_0.1"       
  }
    stage.show
}