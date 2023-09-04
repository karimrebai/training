import akka.actor.{ActorSystem, Props}


object Main extends App {
  val system = ActorSystem("HelloSystem")
  // defaultActor constructor
  val helloActor = system.actorOf(Props[HelloWordActor], name = "helloactor")
  helloActor ! "hello"
  helloActor ! "buenos dias"
}
