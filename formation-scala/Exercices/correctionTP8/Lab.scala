package xml

import _root_.scala.collection.immutable.Seq
import _root_.scala.xml.XML
import scala.collection.immutable.Seq
import scala.xml.XML


object Lab extends App {

    def getXmlContent() = XML.load("src/main/scala/xml/data.xml")

    def getAllTitles(): Seq[String] = {
        val xml = getXmlContent()
        (xml \\ "catalog" \\ "book" \\ "title").map(n => n.text)
    }

    def getAllFantasyBooks() = {
        val xml = getXmlContent()
        (xml \\ "catalog" \\ "book").filter(n => (n  \\ "genre").text == "Fantasy").map(n => (n \\ "@id").text)
    }

    def getBookTitleById(id:String) = {
        val xml = getXmlContent()
        (xml \\ "catalog" \ "book" ).filter(n => (n \ "@id").text == id).map(n => (n \\ "title").text)
    }

    def getNumberOfBookForOBrien() = {
        val xml = getXmlContent()
        (xml \\ "catalog" \\ "book" ).filter(n => ((n \ "author").text == "O'Brien, Tim")).size
    }

    def getNumberOfBookWithdescriptionContainWord(word: String) = {
        val xml = getXmlContent()
        (xml \\ "catalog" \\ "book" ).filter(n => ((n \ "description").text contains(word))).size
    }

}
