package chess.webapp

import com.greencatsoft.angularjs.{ Service, injectable }
import org.sarrufat.chesschallenge.Config
import com.greencatsoft.angularjs.Factory
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.concurrent.Future
import scala.scalajs.js
import org.sarrufat.chesschallenge.Solver2

class ChessService {
  def solveAsync(conf: Config, scope: ControllerData): Unit = {
    val solver = Solver2(conf)
    val future = Future { solver.solve }
    for { results ← future } { scope.resInfo += ResultInfo(conf, solver.elepasedTimeMS, results); scope.$apply() }
  }
}

//object ChessServiceFactory extends Factory[ChessService] {
//  override val name = "$ChessService"
//  override def apply(): ChessService = new ChessService
//}
