package chess.webapp

import scala.annotation.meta.field
import scala.scalajs.js
import scala.scalajs.js.Any.{ fromFunction0, fromString }
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import org.scalajs.jquery.jQuery
import com.greencatsoft.angularjs.{ Angular, Controller }
import com.greencatsoft.angularjs.core.Scope
import scala.scalajs.js.annotation.JSExportAll
import org.sarrufat.chesschallenge.Config
import org.sarrufat.chesschallenge.Solver2
import com.greencatsoft.angularjs.inject
import org.sarrufat.chesschallenge.Results
import org.sarrufat.chesschallenge.ResultPositions

import canva.chess.board.BoardDirective

@JSExport
object ChessApp extends JSApp {

  def main(): Unit = {
    //    jQuery(setupUI _)
    val module = Angular.module("ChessApp", Seq("mm.foundation"))
    module.controller(ChessController)
    module.directive(BoardDirective)
    //    module.factory(ChessServiceFactory)
    //    module.service(ChessService)
  }
  def setupUI(): Unit = {
    jQuery("""<button type="button">Click me!</button>""")
      .click(addClickedMessage _)
      .appendTo(jQuery("body"))
    jQuery("body").append("<p>Hello World</p>")
  }
  def addClickedMessage(): Unit = {
    jQuery("body").append("<p>You clicked the button!</p>")
  }
}

trait ControllerData extends Scope {
  var boardT: String = js.native
  var pieces: js.Array[PieceModel] = js.native
  var count: Int = js.native
  var calculate: js.Function = js.native
  var closeAlert: js.Function1[Int, Unit] = js.native
  var alerts: js.Array[Alert] = js.native
  var resInfo: js.Array[ResultInfo] = js.native
  var currResultInfo: ResultInfo = js.native
  var cleanRes: js.Function = js.native
  var permOpt: Boolean = js.native
}

@JSExportAll
case class Alert(val atype: String, val msg: String)
@JSExportAll
case class ResultInfo(val config: Config, val ellapsed: Long, val results: Results, val iterations: Long) {
  private def verbosePieces = {
    val names = Map("K" -> "Kings", "Q" -> "Queens", "B" -> "Bishops", "R" -> "Rooks", "N" -> "Knights")
    config.pieces.map { p ⇒ p._2 + s" ${names.get(p._1).get} " } mkString (", ")
  }

  private def ellapsedFMT = ellapsed match {
    case x if (x < 5000L) ⇒ s"$ellapsed msecs."
    case x                ⇒ s"${ellapsed / 1000} secs."
  }

  def strMessage() = s" Found ${results.length} in $ellapsedFMT and $iterations Iterations for ${config.dimM}X${config.dimN} board with ${verbosePieces}"
  def jsResults() = js.Array(results: _*)
  def click() = ChessController.controlScope.foreach { scope ⇒ scope.currResultInfo = this }
}

@JSExportAll
case class PieceModel(val label: String) {
  var checked: Boolean = false
  var number: Int = 1

}

@JSExportAll
object ChessController extends Controller {

  val service = new ChessService
  override type ScopeType = ControllerData
  var controlScope: Option[ScopeType] = None
  import scala.scalajs._
  override def initialize(scope: ScopeType): Unit = {
    super.initialize(scope)
    scope.boardT = "4x4"
    scope.pieces = js.Array(PieceModel("Kings"), PieceModel("Queens"), PieceModel("Bishops"), PieceModel("Rooks"), PieceModel("Knights"))
    scope.count = 0
    scope.calculate = () ⇒ calculate()
    scope.cleanRes = () ⇒ cleanRes()
    scope.closeAlert = (idx: Int) ⇒ { scope.alerts = js.Array() }
    scope.alerts = js.Array()
    scope.resInfo = js.Array()
    controlScope = Some(scope)
    scope.permOpt = true
  }

  private def internalCalc(permOpt: Boolean) = {
    def getBoard(scope: ScopeType) = {
      scope.boardT.split('x') match {
        case Array(x, y) ⇒ (x.toInt, y.toInt)
        case _           ⇒ (0, 0)
      }
    }
    def getPieces(scope: ScopeType) = {
      Map(scope.pieces.filter { p ⇒ p.checked } map { p ⇒
        if (p.label != "Knights") p.label.take(1) -> p.number
        else "N" -> p.number
      }: _*)
    }
    controlScope.foreach { scope ⇒
      getBoard(scope) match {
        case (0, 0) ⇒ scope.alerts += Alert("error", "Internal error")
        case (x, y) ⇒ service solveAsync (Config(x, y, getPieces(scope), permOpt = permOpt), scope)
      }
    }
  }
  def calculate(): Unit = {
    scope.alerts = js.Array()
    scope.currResultInfo = null
    controlScope.foreach { scope ⇒
      scope.pieces.forall { p ⇒ !p.checked } match {
        case true  ⇒ scope.alerts += Alert("warning", "You must select one or more pieces")
        case false ⇒ internalCalc(scope.permOpt)
      }
    }
  }
  def cleanRes(): Unit = { scope.resInfo = js.Array() }
}
