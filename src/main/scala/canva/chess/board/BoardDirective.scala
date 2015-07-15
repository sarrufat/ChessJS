package canva.chess.board

import scala.scalajs.js
import scala.scalajs.js.Any.fromFunction1
import scala.scalajs.js.UndefOr
import scala.scalajs.js.UndefOr.undefOr2jsAny
import scala.scalajs.js.annotation.JSExport

import org.scalajs.dom.Element
import org.scalajs.dom.html.Html

import org.sarrufat.chesschallenge._

import com.greencatsoft.angularjs.{ AttributeDirective, Attributes, Controller, injectable }

@JSExport
@injectable("board")
object BoardDirective extends AttributeDirective {
  import chess.webapp.ControllerData
  override val name = "board"
  override def link(scope: ScopeType, elems: Seq[Element], attrs: Attributes, controllers: Controller*) = {

    elems.headOption.map { _.asInstanceOf[org.scalajs.dom.html.Canvas] } foreach { x ⇒

      val cd = scope.asInstanceOf[ControllerData]
      scope.$watch(attrs(name), (newval: UndefOr[js.Any]) ⇒ { DrawBoard(x, newval.asInstanceOf[ResultPositions], (cd.currResultInfo.config.dimM, cd.currResultInfo.config.dimN)) })
    }
  }
}
