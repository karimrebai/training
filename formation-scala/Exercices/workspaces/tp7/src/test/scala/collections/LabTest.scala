package collections

import collections.CollectionRepository._
import org.scalatest.FunSuite


class LabTest extends FunSuite {

  test("String should contains 199 words") {
    assert(Lab.wordCount(getBigText()) == 199)
  }

  test("Class mean should be 10,15") {
    assert(Lab.moyenneGenerale(getNoteEleves()) == 10.15)
  }

  test("Each list should have following size 10 - 10") {
    assert(Lab.groupWord(getBigText())._1.length == 158)
    assert(Lab.groupWord(getBigText())._2.length == 41)
  }

  test("List must contains person older than 30 ") {
    assert(Lab.personMoreThan30years(personFactory()).find(p => p.age <= 30).size == 0)
  }

  test("global mean should 12") {
    assert(Lab.personMeanAge(personFactory()) == 24.761904761904763)
  }

  test("List of city should not contains duplicate Set(Puteau, Bordeaux, Limoge, Turin, Saint-Etienne, Lille, Paris, Reims, Nantes, Nice, Loix, Orange, Lyon)") {
    assert(Lab.cities(personFactory()).toString() == "Set(Puteau, Bordeaux, Limoge, Turin, Saint-Etienne, Lille, Paris, Reims, Nantes, Nice, Loix, Orange, Lyon)")
  }

  test("formatted, first element of collection should looks like this : Martin-Paris") {
    assert(Lab.formatPerson(personFactory())(0) == "Martin-Paris")
  }

  test("sorted, first element of collection should looks like this : Person()") {
    assert(Lab.sortPersonByAge(personFactory())(0) == Person("Remi", 5, "Lille"))
  }

  test("Sum of list should be 191") {
    assert(Lab.sum(CollectionRepository.getStringNumber()) == 191)
  }

  test("First element should be 5") {
    assert(Lab.firstONe(CollectionRepository.getStringNumber()) == "5")
  }

  test("First element should be List(15, 12, 3, 100, 23, 1, 32)") {
    assert(Lab.others(CollectionRepository.getStringNumber()) == List("15", "12", "3", "100", "23", "1", "32"))
  }

  test("first Note/Eleve Mapping should equals to (Eleve(\"Harry\", \"Cover\"), List(5.0, 15.0, 12.0, 3.0))") {
    assert(Lab.attributionNoteEleve(getNoteEleves(), getEleves()).head ==(Eleve("Harry", "Cover"), List(5.0, 15.0, 12.0, 3.0)))
  }


}
