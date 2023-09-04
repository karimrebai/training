package actor.formation.scala.pingpong

import actor.formation.scala.pingpong.actors.{PingActor, PongActor}
import actor.formation.scala.pingpong.msg.InitMsg
import akka.actor.{ActorSystem, Props}


object Main extends App {
  println("StartingPingPong")
  val system = ActorSystem("PingPongSystem")
  val pong = system.actorOf(Props[PongActor]
    , name = "pong")
  val ping = system.actorOf(Props
    (new PingActor(pong)), name = "ping")
  println("SendingInitMsg")
  ping ! InitMsg
}
