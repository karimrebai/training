package actor.formation.scala.pingpong

import actor.formation.scala.pingpong.msg.{HaltMsg, InitMsg, PingMsg, PongMsg}
import akka.actor.{Actor, ActorRef}


object actors {


  class PingActor(pong: ActorRef) extends Actor {
    var started = false

    def receive = {
      case InitMsg =>
        started = true
        println("Ok starting with a Ping to Pong")
        pong ! PingMsg
      case PongMsg =>
        println("received Pong message")
        if (!started) {
          println("Discarded not yet started!!")
        } else {
          sender ! PingMsg
          println("replying to Pong with Ping")
        }
      case HaltMsg =>
        started = false
        println("Halt Msg received")
      //context.stop(self)
      case _ => println()
    }
  }


  class PongActor extends Actor {
    var counter = 0

    def newPing(sender: ActorRef) = {
      counter += 1
      println("received a newPing")
      if (counter < 3) {
        sender ! PongMsg
      } else {
        println("Max value reached..!")
        sender ! HaltMsg
      }
    }

    def receive = {
      case PingMsg =>
        newPing(sender)
      case _ => println("Message inconnu ")
    }
  }

}