package xml

import scala.collection.immutable.Seq


object Lab extends App {

  /**
   * load XML file
   * @return
   */
  def getXmlContent() = ???

  def getAllTitles(): Seq[String] = {
    val xml = getXmlContent()
    ???
  }


  def getAllFantasyBooks() = {
    val xml = getXmlContent()
    ???
  }

  def getBookTitleById(id:String) = {
    val xml = getXmlContent()
    ???
  }

  def getNumberOfBookForOBrien() = {
    val xml = getXmlContent()
    ???

  }

  def getNumberOfBookWithdescriptionContainWord(word: String) = {
    val xml = getXmlContent()
    ???
  }

}
