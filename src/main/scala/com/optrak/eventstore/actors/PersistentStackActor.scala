package com.optrak.eventstore.actors

import akka.actor._
import akka.persistence._
import org.joda.time._

sealed trait StackCommand
  case class PushItem(item: String) extends StackCommand
  case object PrintState extends StackCommand

case class ItemPushed(
    val item: String,
    val timestamp: DateTime = new DateTime
)

class PersistentStackActor extends PersistentActor {
  override def persistenceId = "sample-id-1"

  var state: List[String] = Nil

  def updateState(event: ItemPushed): Unit = event match {
    case ItemPushed(item, timestamp) => state = item :: state
  }
 
  val receiveRecover: Receive = {
    case evt: ItemPushed => updateState(evt)
  }
 
  val receiveCommand: Receive = {
    case PushItem(item) =>
      persist(ItemPushed(item, DateTime.now))(updateState)
    case PrintState => System.out.println(state)
  }
 
}
