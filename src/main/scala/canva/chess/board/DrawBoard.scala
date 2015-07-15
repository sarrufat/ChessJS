package canva.chess.board

import org.scalajs.dom.Element
import org.sarrufat.chesschallenge._
import org.scalajs.dom
import org.scalajs.dom.html.Canvas
import org.scalajs.dom.raw.CanvasRenderingContext2D
import org.scalajs.dom.ext.Image
import org.scalajs.dom.raw.HTMLImageElement

object DrawBoard {
  lazy val image = dom.document.createElement("img").asInstanceOf[HTMLImageElement]
  def apply(canvas: Canvas, res: ResultPositions, dim: Dimension) = {
    //    def posToIndex(pos: Pos) = pos._1 * dim._2 + pos._2
    val ctx = canvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]
    // Calculdate the precise block size
    val BLOCK_SIZE = 50;
    canvas.width = dim._1 * BLOCK_SIZE
    canvas.height = dim._2 * BLOCK_SIZE
    def drawBlock(row: Int, col: Int) {
      ctx.fillStyle = (row + col) % 2 match {
        case 0 ⇒ "rgb(230,200,50)"
        case 1 ⇒ "rgb(90,90,50)"
      }
      ctx.fillRect(row * BLOCK_SIZE, col * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE)
      ctx.stroke
    }
    def drawPieces = {
      val pOrder = Array('P', 'R', 'N', 'B', 'Q', 'K').toSeq
      image.src = "img/pieces.png"
      for (pos ← res) {
        val delta = pOrder.indexOf(pos._2) * 50
        val canvasOffsetX = pos._1._1 * BLOCK_SIZE
        val canvasOffsetY = pos._1._2 * BLOCK_SIZE
        ctx.drawImage(image, delta, 0, BLOCK_SIZE, BLOCK_SIZE, canvasOffsetX, canvasOffsetY, BLOCK_SIZE, BLOCK_SIZE)
      }
    }
    for {
      x ← 0 until dim._1
      y ← 0 until dim._2
    } drawBlock(x, y)
    ctx.lineWidth = 2;
    ctx.strokeRect(0, 0, dim._1 * BLOCK_SIZE, dim._2 * BLOCK_SIZE);
    drawPieces
  }
}
