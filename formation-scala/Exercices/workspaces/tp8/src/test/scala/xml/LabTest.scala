package xml

import org.scalatest.FunSuite


class LabTest extends FunSuite{

  test("All titles should be equals to List(\"XML Developer's Guide\", \"Midnight Rain\", \"Maeve Ascendant\", \"Oberon's Legacy\", \"The Sundered Grail“, \"Lover Birds\"\", \"Splish Splash\", \"Creepy Crawlies\", \"Paradox Lost\", \"Microsoft .NET: The Programming Bible, MSXML3: A Comprehensive Guide\", \"Visual Studio 7: A Comprehensive Guide\")"){
    assert(Lab.getAllTitles().head == "XML Developer's Guide")
    assert(Lab.getAllTitles().last == "Visual Studio 7: A Comprehensive Guide")
  }

  test("All books with genre Fantasy should equals to Seq(\"bk102\", \"bk103\", \"bk104\", \"bk105\")"){
    assert(Lab.getAllFantasyBooks() == Seq("bk102", "bk103", "bk104", "bk105"))
  }

  test("Book with id bk102 should have title Midnight Rain"){
    assert(Lab.getBookTitleById("bk102") == Seq("Midnight Rain"))
  }

  test("Number of book written by O'Brien should equals to 2"){
    assert(Lab.getNumberOfBookForOBrien() == 2)
  }

  test("Number of book that Book's description that have word horror should be equals to 1"){
    assert(Lab.getNumberOfBookWithdescriptionContainWord("horror") == 1)
  }

}
