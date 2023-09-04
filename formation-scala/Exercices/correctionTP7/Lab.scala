package collections

import collections.CollectionRepository
import collections.CollectionRepository.Eleve
import collections.CollectionRepository.Person
import collections.CollectionRepository.{Eleve, Person}


object Lab extends App {

    def wordCount(s: String): Int = {
        s.split(" ").size
    }

    def mean(l: List[Double]): Double = l.sum / l.size

    def moyenneGenerale(l: List[List[Double]]): Double = {
        mean(CollectionRepository.getNoteEleves().flatMap(l => List(mean(l))))
    }

    def groupWord(text: String): (List[String], List[String]) = {
        text.split(" ").toList.partition(w => w.length > 4)
    }

    // exercice 7.4
    def personMoreThan30years(l: List[Person]): List[Person] = {
        l.filter(p => p.age > 30)
    }

    def personMeanAge(l: List[Person]): Double = {
        mean(l.map(p => p.age.toDouble))
    }

    def cities(l: List[Person]): Set[String] = {
        l.map(p => p.ville).toSet
    }

    def formatPerson(l: List[Person]): List[String] = {
        l.map(p => p.nom + "-" + p.ville)
    }

    def sortPersonByAge(l: List[Person]): List[Person] = {
        l.sortBy(p => p.age)
    }

    //fin exercice 7.4

    def sum(l: List[String]): Int = l.map(s => s.toInt).fold(0)((a, b) => a + b)

    def firstONe(l: List[String]): String = l.head

    def others(l: List[String]): List[String] = l.tail

    def attributionNoteEleve(l: List[List[Double]], eleves: List[Eleve]): List[(Eleve, List[Double])] = eleves.zip(l)
}
