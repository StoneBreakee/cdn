import scala.io.Source

//单个元素最长为| -13|,即长度为4 
object MatrixTransform extends App {
  val spaceMap = Map(
    0 -> "", 
    1 -> " ",
    2 -> "  ",  
    3 -> "   ",  
    4 -> "    "   
  )

  val lines = Source.fromFile("aa").getLines;
  for(line <- lines){
    val eles = line.split(",");
    val str = eles.map(ele => {
      val len = 4 - ele.replaceAll(" ","").length
      spaceMap(len) + ele.replaceAll(" ","")
    }).mkString(",")
    println(str)
  }
}