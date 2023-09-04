
import akka.actor.Actor

class HelloWordActor extends Actor {

  def receive = {
    case "hello" => println("hello")
    case _ => println(" Sorry !!")
  }

}
