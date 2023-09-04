package collections

import collections.CollectionRepository.{Eleve, Person}


object Lab extends App {

  def wordCount(s: String): Int = {
    ???
  }

  def moyenneGenerale(l: List[List[Double]]): Double = {
    ???
  }

  def groupWord(text: String): (List[String], List[String]) = {
    ???
  }

  // exercice 7.4
  def personMoreThan30years(l: List[Person]): List[Person] = {
    ???
  }

  def personMeanAge(l: List[Person]): Double = {
    ???
  }

  def cities(l: List[Person]): Set[String] = {
    ???
  }

  def formatPerson(l: List[Person]): List[String] = {
    ???
  }

  def sortPersonByAge(l: List[Person]): List[Person] = {
    ???
  }

  //fin exercice 7.4

  def sum(l: List[String]): Int = ???

  def firstONe(l: List[String]): String = ???

  def others(l: List[String]): List[String] = ???

  def attributionNoteEleve(l: List[List[Double]], eleves: List[Eleve]): List[(Eleve, List[Double])] = ???
}
